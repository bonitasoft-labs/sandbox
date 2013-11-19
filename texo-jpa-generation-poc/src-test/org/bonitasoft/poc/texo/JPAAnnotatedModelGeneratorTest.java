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
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import org.eclipse.core.runtime.AssertionFailedException;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.xmi.impl.EcoreResourceFactoryImpl;
import org.eclipse.emf.texo.annotations.annotationsmodel.AnnotatedEClass;
import org.eclipse.emf.texo.annotations.annotationsmodel.AnnotatedEPackage;
import org.eclipse.emf.texo.annotations.annotationsmodel.AnnotatedModel;
import org.eclipse.emf.texo.annotations.annotationsmodel.EClassAnnotation;
import org.eclipse.emf.texo.annotations.annotationsmodel.EPackageAnnotation;
import org.eclipse.emf.texo.modelgenerator.modelannotations.EPackageModelGenAnnotation;
import org.eclipse.emf.texo.orm.annotations.model.orm.Entity;
import org.eclipse.emf.texo.orm.ormannotations.EClassORMAnnotation;
import org.eclipse.emf.texo.orm.ormannotations.EClassifierORMAnnotation;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Romain Bioteau
 *
 */
public class JPAAnnotatedModelGeneratorTest {

	private JPAAnnotatedModelGenerator jpaAnnotatedModelGenerator;
	private static EPackage ePackage;
	private static EClass carClassifier;

	
	@BeforeClass
	public static void loadPocModel() throws IOException{
		Resource ecoreResource = new EcoreResourceFactoryImpl().createResource(URI.createFileURI(JPAAnnotatedModelGeneratorTest.class.getResource("poc.ecore").getFile()));
		ecoreResource.load(Collections.emptyMap());
		ePackage = (EPackage) ecoreResource.getContents().get(0);
		carClassifier = (EClass) ePackage.getEClassifier("Car");
	}
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		jpaAnnotatedModelGenerator = new JPAAnnotatedModelGenerator();
	}
	
	@Test
	public void shouldCarClassifierNotNull(){
		assertThat(carClassifier).overridingErrorMessage("Car not found in poc.ecore").isNotNull();
	}
	
	
	@Test
	public void shouldCreateAnAnotatedEClass_FromEcoreClass() {
		AnnotatedEClass buildAnnotatedEClass = jpaAnnotatedModelGenerator.buildAnnotatedEClass(carClassifier);
		assertThat(buildAnnotatedEClass).isNotNull().overridingErrorMessage("Fail to build an AnnotatedEClass from Car");
		assertThat(buildAnnotatedEClass.getEClass()).isEqualTo(carClassifier)
			.overridingErrorMessage("Wrong EClass referenced in AnnotatedEclass");
	}

	@Test
	public void shouldAnAnnotatedEClass_HaveAnEntityAnnotation(){
		AnnotatedEClass buildAnnotatedEClass = jpaAnnotatedModelGenerator.buildAnnotatedEClass(carClassifier);
		assertNotNull("Fail to build an AnnotatedEClass from Car",buildAnnotatedEClass);
		EList<EClassAnnotation> eClassAnnotations = buildAnnotatedEClass.getEClassAnnotations();
		EClassORMAnnotation ormAnnotation = (EClassORMAnnotation) findEObjectFromCollection(eClassAnnotations, EClassifierORMAnnotation.class);
		assertThat(ormAnnotation).isNotNull()
			.overridingErrorMessage("ORM Annotation not found on AnnotatedEClass: Car");
		Entity entity = ormAnnotation.getEntity();
		assertThat(entity).isNotNull().overridingErrorMessage("@Entity not found on AnnotatedEclass : Car");
		assertThat(entity.isCacheable()).overridingErrorMessage("Entity is not cacheable").isTrue();
	}
	
	
	@Test
	public void shouldCreateAnnotatedEClasses_FromAnEPackage(){
		AnnotatedEPackage annotatedEPackage = jpaAnnotatedModelGenerator.buildAnnotatedEPackage(ePackage);
		assertThat(annotatedEPackage).isNotNull().overridingErrorMessage("AnnotatedEPackage should not be null");
		assertThat(annotatedEPackage.getEPackage()).isNotNull();
		assertThat(annotatedEPackage.getEPackage()).isEqualTo(ePackage);
		assertThat(annotatedEPackage.getAnnotatedEClassifiers()).isNotEmpty().hasSize(6);
	}
	
	@Test
	public void shouldAnAnnotatedEPackage_disabledRuntimeBehavior(){
		AnnotatedEPackage annotatedEPackage = jpaAnnotatedModelGenerator.buildAnnotatedEPackage(ePackage);
		assertThat(annotatedEPackage).isNotNull().overridingErrorMessage("AnnotatedEPackage should not be null");
		assertThat(annotatedEPackage.getEPackage()).isNotNull();
		assertThat(annotatedEPackage.getEPackage()).isEqualTo(ePackage);
		
		EList<EPackageAnnotation> ePackageAnnotations = annotatedEPackage.getEPackageAnnotations();
		EPackageModelGenAnnotation modelGenAnnotation = (EPackageModelGenAnnotation) findEObjectFromCollection(ePackageAnnotations, EPackageModelGenAnnotation.class);
		assertThat(modelGenAnnotation).isNotNull();
		assertThat(modelGenAnnotation.isAddRuntimeModelBehavior()).isFalse();
	}
	
	@Test(expected = AssertionFailedException.class)
	public void shouldBuildAnnotatedEPackage_ThrowAnIllegalArgumentException_WhenEPackageIsNull(){
		jpaAnnotatedModelGenerator.buildAnnotatedEPackage(null);
	}
	
	@Test
	public void shouldBuildAnAnnotatedModel_FromACollectionOfEPackages(){
		AnnotatedModel annotatedModel = jpaAnnotatedModelGenerator.buildAnnotatedModel(Collections.singletonList(ePackage));
		assertThat(annotatedModel).isNotNull();
		assertThat(annotatedModel.getAnnotatedEPackages()).isNotEmpty().hasSize(1);
	}
	
	@Test(expected = AssertionFailedException.class)
	public void shouldBuildAnnotatedModel_ThrowAnIllegalArgumentException_WhenEPackagListIsNull(){
		jpaAnnotatedModelGenerator.buildAnnotatedModel(null);
	}
	
	@Test(expected = AssertionFailedException.class)
	public void shouldBuildAnnotatedModel_ThrowAnIllegalArgumentException_WhenEPackagListIsEmpty(){
		jpaAnnotatedModelGenerator.buildAnnotatedModel(new ArrayList<EPackage>());
	}
	
	private EObject findEObjectFromCollection(Collection<? extends EObject> collection, Class<?> type){
		for (EObject eObject : collection) {
			if(type.isAssignableFrom(eObject.getClass())){
				return eObject;
			}
		}
		return null;
	}
}
