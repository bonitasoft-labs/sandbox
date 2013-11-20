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

import static org.fest.assertions.Assertions.assertThat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.xmi.impl.EcoreResourceFactoryImpl;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.launching.JavaRuntime;
import org.fest.assertions.Condition;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Romain Bioteau
 *
 */
public class JPAAnnotatedPojoGeneratorTest {

	private JPAAnnotatedPojoGenerator annotatedJPAPojoGenerator;
	private EPackage ePackage;
	private static IProject project;
	private EClass carClassifier;
	private static Resource ecoreResource;


	@BeforeClass
	public static void beforeClass() throws Exception{
		//ecoreResource = new EcoreResourceFactoryImpl().createResource(URI.createFileURI(JPAAnnotatedModelGeneratorTest.class.getResource("poc.ecore").getFile()));
		ecoreResource= new EcoreResourceFactoryImpl().createResource(URI.createFileURI(FileLocator.toFileURL(Activator.getDefault().getBundle().getResource("src-test/org/bonitasoft/poc/texo/poc.ecore")).getFile()));
		prepareTargetGenerationProject();
	}

	private static void prepareTargetGenerationProject() throws CoreException,
			JavaModelException {
		NullProgressMonitor monitor = new NullProgressMonitor();
		project = ResourcesPlugin.getWorkspace().getRoot().getProject("texo-test");
	
		if(project.exists()){
			project.close(monitor);
			project.delete(true,true, monitor);
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
		annotatedJPAPojoGenerator = new JPAAnnotatedPojoGenerator();
		ecoreResource.unload();
		ecoreResource.load(Collections.emptyMap());
		ePackage = (EPackage) ecoreResource.getContents().get(0);
		carClassifier = (EClass) ePackage.getEClassifier("Car");
	}
	

	@Test
	public void shouldGenerateJPAPojo_AddAnIdField() throws IOException{
		annotatedJPAPojoGenerator.buildAnnotatedPojo(Collections.singletonList(ePackage));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void shouldAddIdAttribute_ThrowAnIllegalArgumentExceptionIfAnIdAttributeAlreadyExists() throws IOException{
		EClass myEclassWithIdFeature = EcoreFactory.eINSTANCE.createEClass();
		EAttribute idAttribute =  EcoreFactory.eINSTANCE.createEAttribute();
		idAttribute.setName(JPAAnnotatedPojoGenerator.ID_FEATURE_NAME);
		idAttribute.setEType(EcorePackage.Literals.ELONG);
		myEclassWithIdFeature.getEStructuralFeatures().add(idAttribute);
		annotatedJPAPojoGenerator.addIdAttributeToEClass(myEclassWithIdFeature);
	}
	
	@Test
	public void shouldAddIdAttribute_AddAnIdAttributeOfTypeLongOnAnEClass() throws IOException{
		annotatedJPAPojoGenerator.addIdAttributeToEClass(carClassifier);
		EStructuralFeature idFeature = carClassifier.getEStructuralFeature(JPAAnnotatedPojoGenerator.ID_FEATURE_NAME);
		assertThat(idFeature).isNotNull();
		assertThat(idFeature.getEType()).isNotNull().isEqualTo(EcorePackage.Literals.ELONG);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void shouldAddVersionAttribute_ThrowAnIllegalArgumentExceptionIfAVersionAttributeAlreadyExists() throws IOException{
		EClass myEclassWithIdFeature = EcoreFactory.eINSTANCE.createEClass();
		EAttribute idAttribute =  EcoreFactory.eINSTANCE.createEAttribute();
		idAttribute.setName(JPAAnnotatedPojoGenerator.VERSION_FEATURE_NAME);
		idAttribute.setEType(EcorePackage.Literals.ELONG);
		myEclassWithIdFeature.getEStructuralFeatures().add(idAttribute);
		annotatedJPAPojoGenerator.addVersionAttributeToEClass(myEclassWithIdFeature);
	}
	
	@Test
	public void shouldAddVersionAttribute_AddAVersionAttributeOfTypeLongOnAnEClass() throws IOException{
		annotatedJPAPojoGenerator.addVersionAttributeToEClass(carClassifier);
		EStructuralFeature idFeature = carClassifier.getEStructuralFeature(JPAAnnotatedPojoGenerator.VERSION_FEATURE_NAME);
		assertThat(idFeature).isNotNull();
		assertThat(idFeature.getEType()).isNotNull().isEqualTo(EcorePackage.Literals.ELONG);
	}
	
	@Test
	public void shouldPrepareEPackage_AddAnIdAndAVersionAttributeOfTypeLongOnAllEClassesOfTheEPackage() throws IOException{
		annotatedJPAPojoGenerator.prepareEPackage(ePackage);
		assertThat(ePackage.getEClassifiers()).satisfies(new Condition<List<?>>() {
			
			@Override
			public boolean matches(List<?> value) {
				for(Object v : value){
					EClass eClass = (EClass) v;
					assertThat(eClass.getEStructuralFeature(JPAAnnotatedPojoGenerator.ID_FEATURE_NAME)).isNotNull();
					assertThat(eClass.getEStructuralFeature(JPAAnnotatedPojoGenerator.ID_FEATURE_NAME).getEType()).isEqualTo(EcorePackage.Literals.ELONG);
					assertThat(eClass.getEStructuralFeature(JPAAnnotatedPojoGenerator.VERSION_FEATURE_NAME)).isNotNull();
					assertThat(eClass.getEStructuralFeature(JPAAnnotatedPojoGenerator.VERSION_FEATURE_NAME).getEType()).isEqualTo(EcorePackage.Literals.ELONG);
				}
				return true;
			}
		});
	}
	
	@Test
	public void shouldPrepareEPackages_AddAnIdAndAVersionAttributeOfTypeLongOnAllEClassesOfAllTheEPackage() throws IOException{
		annotatedJPAPojoGenerator.prepareEPackages(Collections.singletonList(ePackage));
		assertThat(ePackage.getEClassifiers()).satisfies(new Condition<List<?>>() {
			
			@Override
			public boolean matches(List<?> value) {
				for(Object v : value){
					EClass eClass = (EClass) v;
					assertThat(eClass.getEStructuralFeature(JPAAnnotatedPojoGenerator.ID_FEATURE_NAME)).isNotNull();
					assertThat(eClass.getEStructuralFeature(JPAAnnotatedPojoGenerator.ID_FEATURE_NAME).getEType()).isEqualTo(EcorePackage.Literals.ELONG);
					assertThat(eClass.getEStructuralFeature(JPAAnnotatedPojoGenerator.VERSION_FEATURE_NAME)).isNotNull();
					assertThat(eClass.getEStructuralFeature(JPAAnnotatedPojoGenerator.VERSION_FEATURE_NAME).getEType()).isEqualTo(EcorePackage.Literals.ELONG);
				}
				return true;
			}
		});
	}

}
