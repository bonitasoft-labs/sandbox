/**
 * Copyright (C) 2013 BonitaSoft S.A.
 * BonitaSoft, 32 rue Gustave Eiffel - 38000 Grenoble
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2.0 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.bonitasoft.poc.texo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.xmi.impl.EcoreResourceFactoryImpl;
import org.eclipse.emf.texo.annotations.annotationsmodel.AnnotatedModel;
import org.eclipse.emf.texo.generator.AnnotationManager;
import org.eclipse.emf.texo.generator.ArtifactGenerator;
import org.eclipse.emf.texo.generator.ModelAnnotator;
import org.eclipse.emf.texo.generator.ModelController;
import org.eclipse.emf.texo.modelgenerator.xtend.ModelTemplate;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.launching.JavaRuntime;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Romain Bioteau
 *
 */
public class AnnotatedJPAPojoGeneratorTest {

	private AnnotatedJPAPojoGenerator annotatedJPAPojoGenerator;
	private static EPackage ePackage;
	private static EClass carClassifier;
	private static IProject project;


	@BeforeClass
	public static void loadPocModel() throws Exception{
		Resource ecoreResource = new EcoreResourceFactoryImpl().createResource(URI.createFileURI(FileLocator.toFileURL(Activator.getDefault().getBundle().getResource("src-test/org/bonitasoft/poc/texo/poc.ecore")).getFile()));
		ecoreResource.load(Collections.emptyMap());
		ePackage = (EPackage) ecoreResource.getContents().get(0);
		carClassifier = (EClass) ePackage.getEClassifier("Car");

		project = ResourcesPlugin.getWorkspace().getRoot().getProject("texo-test");
		NullProgressMonitor monitor = new NullProgressMonitor();
		if(project.exists()){
			project.delete(true, monitor);
		}

		project.create(monitor);
		project.open(monitor);
		
		IProjectDescription descriptor = project.getDescription();
	
		descriptor.setNatureIds(new String[]{JavaCore.NATURE_ID});
		project.setDescription(descriptor, monitor);
		
		final IJavaProject javaProject = JavaCore.create(project);
		javaProject.setOption(JavaCore.COMPILER_COMPLIANCE, JavaCore.VERSION_1_6);
		javaProject.setOption(JavaCore.COMPILER_SOURCE, JavaCore.VERSION_1_6);
		javaProject.setOption(JavaCore.COMPILER_CODEGEN_TARGET_PLATFORM, JavaCore.VERSION_1_6);
		javaProject.setOption(JavaCore.CORE_JAVA_BUILD_INVALID_CLASSPATH, "ignore");
		IFolder folder = project.getFolder("src-gen");
		folder.create(true, true, monitor);

		List<IClasspathEntry> entries = new ArrayList<IClasspathEntry>();
		//SET Java container
		entries.add(JavaCore.newContainerEntry(JavaRuntime.newJREContainerPath(JavaRuntime.getExecutionEnvironmentsManager().getEnvironment("JavaSE-1.6"))));
		entries.add(JavaCore.newSourceEntry(folder.getFullPath()));
		javaProject.setRawClasspath(entries.toArray(new IClasspathEntry[entries.size()]),true,monitor);
		
		project.build(IncrementalProjectBuilder.FULL_BUILD, monitor);

	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		annotatedJPAPojoGenerator = new AnnotatedJPAPojoGenerator();
	}

	@Test
	public void shouldGenerateJPAPojo_AddAnIdField() throws IOException{
		AnnotationManager.enableAnnotationSystem(AnnotationManager.JPA_ANNOTATION_SYSTEM_ID);
		
		final ModelController modelController = new ModelController();
		List<EPackage> ePackages = Collections.singletonList(ePackage);
		modelController.setEPackages(ePackages);
		AnnotatedModel annotatedModel = new JPAAnnotatedModelGenerator().buildAnnotatedModel(ePackages);
		annotatedModel.setGeneratingSources(true);
		modelController.getAnnotationManager().setAnnotatedModel(annotatedModel);
		modelController.annotate(new ArrayList<ModelAnnotator>());
		
		final ArtifactGenerator artifactGenerator = new ArtifactGenerator();
		artifactGenerator.setXTendTemplate(new ModelTemplate());
		artifactGenerator.setModelController(modelController);
		artifactGenerator.setProjectName("texo-test");
		artifactGenerator.setDoDao(false);
		artifactGenerator.setOutputFolder("src-gen");
		artifactGenerator.run();
	}

}
