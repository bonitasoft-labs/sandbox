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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.texo.annotations.annotationsmodel.AnnotatedModel;
import org.eclipse.emf.texo.generator.AnnotationManager;
import org.eclipse.emf.texo.generator.ArtifactGenerator;
import org.eclipse.emf.texo.generator.ModelAnnotator;
import org.eclipse.emf.texo.generator.ModelController;
import org.eclipse.emf.texo.modelgenerator.xtend.ModelTemplate;

/**
 * @author Romain Bioteau
 *
 */
public class JPAAnnotatedPojoGenerator {

	protected static final String ID_FEATURE_NAME = "id";
	protected static final String VERSION_FEATURE_NAME = "version";

	public void buildAnnotatedPojo(List<EPackage> ePackages) {
		try{
			AnnotationManager.enableAnnotationSystem(AnnotationManager.JPA_ANNOTATION_SYSTEM_ID);
			
			final ModelController modelController = new ModelController();
			prepareEPackages(ePackages);
			modelController.setEPackages(ePackages);
			AnnotatedModel annotatedModel = new JPAAnnotatedModelGenerator().buildAnnotatedModel(ePackages);
			annotatedModel.setGeneratingSources(true);
			modelController.getAnnotationManager().setAnnotatedModel(annotatedModel);
			modelController.annotate(new ArrayList<ModelAnnotator>());

			final ArtifactGenerator artifactGenerator = new ArtifactGenerator();
			artifactGenerator.setXTendTemplate(new ModelTemplate());
			artifactGenerator.setModelController(modelController);
			artifactGenerator.setProjectName("texo-test");
			artifactGenerator.setDoDao(true);
			artifactGenerator.setOutputFolder("src-gen");
			artifactGenerator.run();
		}finally{
			AnnotationManager.removeEnabledAnnotationSystem(AnnotationManager.JPA_ANNOTATION_SYSTEM_ID);
		}
	}

	protected void prepareEPackage(EPackage ePackage) {
		for(EClassifier eClassifier : ePackage.getEClassifiers()){
			if(eClassifier instanceof EClass){
				addIdAttributeToEClass((EClass) eClassifier);
				addVersionAttributeToEClass((EClass) eClassifier);
			}
		}
	}
	
	protected void addIdAttributeToEClass(EClass eClass) {
		addEAttributeToEClass(eClass,ID_FEATURE_NAME,EcorePackage.Literals.ELONG);
	}

	protected void addVersionAttributeToEClass(EClass eClass) {
		addEAttributeToEClass(eClass,VERSION_FEATURE_NAME,EcorePackage.Literals.ELONG);
	}

	private void addEAttributeToEClass(EClass eClass, String featureName, EClassifier eType) {
		if(eClass.getEStructuralFeature(featureName)!= null){
			throw new IllegalArgumentException("EClass "+eClass.getName()+" already have an attribute named "+featureName+" declared");
		}
		EAttribute newFeature = EcoreFactory.eINSTANCE.createEAttribute();
		newFeature.setName(featureName);
		newFeature.setEType(eType);
		eClass.getEStructuralFeatures().add(newFeature);
	}

	protected void prepareEPackages(List<EPackage> ePackages) {
		for(EPackage ePackage : ePackages){
			prepareEPackage(ePackage);
		}
	}

	

}
