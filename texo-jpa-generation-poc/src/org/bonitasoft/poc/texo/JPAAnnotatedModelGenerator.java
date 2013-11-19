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

import java.util.List;

import org.eclipse.core.runtime.Assert;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.texo.annotations.annotationsmodel.AnnotatedEClass;
import org.eclipse.emf.texo.annotations.annotationsmodel.AnnotatedEPackage;
import org.eclipse.emf.texo.annotations.annotationsmodel.AnnotatedModel;
import org.eclipse.emf.texo.annotations.annotationsmodel.AnnotationsmodelFactory;
import org.eclipse.emf.texo.modelgenerator.modelannotations.EPackageModelGenAnnotation;
import org.eclipse.emf.texo.modelgenerator.modelannotations.ModelcodegeneratorFactory;
import org.eclipse.emf.texo.orm.annotations.model.orm.Entity;
import org.eclipse.emf.texo.orm.annotations.model.orm.OrmFactory;
import org.eclipse.emf.texo.orm.ormannotations.EClassORMAnnotation;
import org.eclipse.emf.texo.orm.ormannotations.OrmannotationsFactory;

/**
 * @author Romain Bioteau
 *
 */
public class JPAAnnotatedModelGenerator {
	
	private AnnotationsmodelFactory annotationModelFactory = AnnotationsmodelFactory.eINSTANCE;
	private OrmannotationsFactory ormAnnotationModelFactory = OrmannotationsFactory.eINSTANCE;
	private ModelcodegeneratorFactory modelcodegeneratorFactory = ModelcodegeneratorFactory.eINSTANCE;
	private OrmFactory ormFactory = OrmFactory.eINSTANCE;
	
	public JPAAnnotatedModelGenerator(){
	}

	protected AnnotatedEClass buildAnnotatedEClass(EClass eClass) {
		AnnotatedEClass createAnnotatedEClass = annotationModelFactory.createAnnotatedEClass();
		createAnnotatedEClass.setEClass(eClass);
		EClassORMAnnotation ormAnnotaiton =  ormAnnotationModelFactory.createEClassORMAnnotation();
		Entity createEntity = ormFactory.createEntity();
		createEntity.setCacheable(true);
		ormAnnotaiton.setEntity(createEntity);
		createAnnotatedEClass.getEClassAnnotations().add(ormAnnotaiton);
		return createAnnotatedEClass;
	}

	protected AnnotatedEPackage buildAnnotatedEPackage(EPackage ePackage) {
		Assert.isNotNull(ePackage,"ePackage");
		AnnotatedEPackage annotatedEPackage = annotationModelFactory.createAnnotatedEPackage();
		annotatedEPackage.setEPackage(ePackage);
		EPackageModelGenAnnotation createEPackageModelGenAnnotation = modelcodegeneratorFactory.createEPackageModelGenAnnotation();
		createEPackageModelGenAnnotation.setAddRuntimeModelBehavior(false);
		annotatedEPackage.getEPackageAnnotations().add(createEPackageModelGenAnnotation);
		for(EClassifier eClassifier : ePackage.getEClassifiers()){
			annotatedEPackage.getAnnotatedEClassifiers().add(buildAnnotatedEClass((EClass) eClassifier));
		}
		return annotatedEPackage;
	}

	public AnnotatedModel buildAnnotatedModel(List<EPackage> ePackages) {
		Assert.isNotNull(ePackages, "ePackages");
		Assert.isTrue(!ePackages.isEmpty(), "ePackages cannot be empty");
		AnnotatedModel createAnnotatedModel = annotationModelFactory.createAnnotatedModel();
		for(EPackage ePackage : ePackages){
			createAnnotatedModel.getAnnotatedEPackages().add(buildAnnotatedEPackage(ePackage));
		}
		return createAnnotatedModel;
	}

}
