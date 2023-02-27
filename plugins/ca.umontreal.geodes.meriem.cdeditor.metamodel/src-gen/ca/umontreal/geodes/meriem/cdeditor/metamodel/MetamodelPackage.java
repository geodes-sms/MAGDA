/**
 */
package ca.umontreal.geodes.meriem.cdeditor.metamodel;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each operation of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see ca.umontreal.geodes.meriem.cdeditor.metamodel.MetamodelFactory
 * @model kind="package"
 * @generated
 */
public interface MetamodelPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "metamodel";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http://www.umontreal.geodes/metamodel";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "metamodel";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	MetamodelPackage eINSTANCE = ca.umontreal.geodes.meriem.cdeditor.metamodel.impl.MetamodelPackageImpl.init();

	/**
	 * The meta object id for the '{@link ca.umontreal.geodes.meriem.cdeditor.metamodel.impl.ModelImpl <em>Model</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see ca.umontreal.geodes.meriem.cdeditor.metamodel.impl.ModelImpl
	 * @see ca.umontreal.geodes.meriem.cdeditor.metamodel.impl.MetamodelPackageImpl#getModel()
	 * @generated
	 */
	int MODEL = 0;

	/**
	 * The feature id for the '<em><b>Operation</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MODEL__OPERATION = 0;

	/**
	 * The feature id for the '<em><b>Clazz</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MODEL__CLAZZ = 1;

	/**
	 * The feature id for the '<em><b>Attribute</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MODEL__ATTRIBUTE = 2;

	/**
	 * The feature id for the '<em><b>Attributecondidate</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MODEL__ATTRIBUTECONDIDATE = 3;

	/**
	 * The feature id for the '<em><b>Clazzcondidate</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MODEL__CLAZZCONDIDATE = 4;

	/**
	 * The feature id for the '<em><b>Association</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MODEL__ASSOCIATION = 5;

	/**
	 * The number of structural features of the '<em>Model</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MODEL_FEATURE_COUNT = 6;

	/**
	 * The number of operations of the '<em>Model</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MODEL_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link ca.umontreal.geodes.meriem.cdeditor.metamodel.impl.NamedElementImpl <em>Named Element</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see ca.umontreal.geodes.meriem.cdeditor.metamodel.impl.NamedElementImpl
	 * @see ca.umontreal.geodes.meriem.cdeditor.metamodel.impl.MetamodelPackageImpl#getNamedElement()
	 * @generated
	 */
	int NAMED_ELEMENT = 1;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NAMED_ELEMENT__NAME = 0;

	/**
	 * The number of structural features of the '<em>Named Element</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NAMED_ELEMENT_FEATURE_COUNT = 1;

	/**
	 * The number of operations of the '<em>Named Element</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NAMED_ELEMENT_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link ca.umontreal.geodes.meriem.cdeditor.metamodel.impl.TypedElementImpl <em>Typed Element</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see ca.umontreal.geodes.meriem.cdeditor.metamodel.impl.TypedElementImpl
	 * @see ca.umontreal.geodes.meriem.cdeditor.metamodel.impl.MetamodelPackageImpl#getTypedElement()
	 * @generated
	 */
	int TYPED_ELEMENT = 2;

	/**
	 * The feature id for the '<em><b>Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TYPED_ELEMENT__TYPE = 0;

	/**
	 * The number of structural features of the '<em>Typed Element</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TYPED_ELEMENT_FEATURE_COUNT = 1;

	/**
	 * The number of operations of the '<em>Typed Element</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TYPED_ELEMENT_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link ca.umontreal.geodes.meriem.cdeditor.metamodel.impl.AssociationCandidateImpl <em>Association Candidate</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see ca.umontreal.geodes.meriem.cdeditor.metamodel.impl.AssociationCandidateImpl
	 * @see ca.umontreal.geodes.meriem.cdeditor.metamodel.impl.MetamodelPackageImpl#getAssociationCandidate()
	 * @generated
	 */
	int ASSOCIATION_CANDIDATE = 3;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ASSOCIATION_CANDIDATE__NAME = NAMED_ELEMENT__NAME;

	/**
	 * The feature id for the '<em><b>Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ASSOCIATION_CANDIDATE__TYPE = NAMED_ELEMENT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Source</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ASSOCIATION_CANDIDATE__SOURCE = NAMED_ELEMENT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Target</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ASSOCIATION_CANDIDATE__TARGET = NAMED_ELEMENT_FEATURE_COUNT + 2;

	/**
	 * The number of structural features of the '<em>Association Candidate</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ASSOCIATION_CANDIDATE_FEATURE_COUNT = NAMED_ELEMENT_FEATURE_COUNT + 3;

	/**
	 * The number of operations of the '<em>Association Candidate</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ASSOCIATION_CANDIDATE_OPERATION_COUNT = NAMED_ELEMENT_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link ca.umontreal.geodes.meriem.cdeditor.metamodel.impl.ClazzImpl <em>Clazz</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see ca.umontreal.geodes.meriem.cdeditor.metamodel.impl.ClazzImpl
	 * @see ca.umontreal.geodes.meriem.cdeditor.metamodel.impl.MetamodelPackageImpl#getClazz()
	 * @generated
	 */
	int CLAZZ = 4;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CLAZZ__NAME = NAMED_ELEMENT__NAME;

	/**
	 * The feature id for the '<em><b>Generalizes</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CLAZZ__GENERALIZES = NAMED_ELEMENT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Specializes</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CLAZZ__SPECIALIZES = NAMED_ELEMENT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Has</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CLAZZ__HAS = NAMED_ELEMENT_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Is Member</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CLAZZ__IS_MEMBER = NAMED_ELEMENT_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Attributes</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CLAZZ__ATTRIBUTES = NAMED_ELEMENT_FEATURE_COUNT + 4;

	/**
	 * The number of structural features of the '<em>Clazz</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CLAZZ_FEATURE_COUNT = NAMED_ELEMENT_FEATURE_COUNT + 5;

	/**
	 * The number of operations of the '<em>Clazz</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CLAZZ_OPERATION_COUNT = NAMED_ELEMENT_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link ca.umontreal.geodes.meriem.cdeditor.metamodel.impl.AttributeImpl <em>Attribute</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see ca.umontreal.geodes.meriem.cdeditor.metamodel.impl.AttributeImpl
	 * @see ca.umontreal.geodes.meriem.cdeditor.metamodel.impl.MetamodelPackageImpl#getAttribute()
	 * @generated
	 */
	int ATTRIBUTE = 5;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ATTRIBUTE__NAME = NAMED_ELEMENT__NAME;

	/**
	 * The feature id for the '<em><b>Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ATTRIBUTE__TYPE = NAMED_ELEMENT_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Attribute</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ATTRIBUTE_FEATURE_COUNT = NAMED_ELEMENT_FEATURE_COUNT + 1;

	/**
	 * The number of operations of the '<em>Attribute</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ATTRIBUTE_OPERATION_COUNT = NAMED_ELEMENT_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link ca.umontreal.geodes.meriem.cdeditor.metamodel.impl.ClazzCandidateImpl <em>Clazz Candidate</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see ca.umontreal.geodes.meriem.cdeditor.metamodel.impl.ClazzCandidateImpl
	 * @see ca.umontreal.geodes.meriem.cdeditor.metamodel.impl.MetamodelPackageImpl#getClazzCandidate()
	 * @generated
	 */
	int CLAZZ_CANDIDATE = 6;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CLAZZ_CANDIDATE__NAME = NAMED_ELEMENT__NAME;

	/**
	 * The feature id for the '<em><b>Attributecondidate</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CLAZZ_CANDIDATE__ATTRIBUTECONDIDATE = NAMED_ELEMENT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Confidence</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CLAZZ_CANDIDATE__CONFIDENCE = NAMED_ELEMENT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Related To</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CLAZZ_CANDIDATE__RELATED_TO = NAMED_ELEMENT_FEATURE_COUNT + 2;

	/**
	 * The number of structural features of the '<em>Clazz Candidate</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CLAZZ_CANDIDATE_FEATURE_COUNT = NAMED_ELEMENT_FEATURE_COUNT + 3;

	/**
	 * The number of operations of the '<em>Clazz Candidate</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CLAZZ_CANDIDATE_OPERATION_COUNT = NAMED_ELEMENT_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link ca.umontreal.geodes.meriem.cdeditor.metamodel.impl.AttributeCandidateImpl <em>Attribute Candidate</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see ca.umontreal.geodes.meriem.cdeditor.metamodel.impl.AttributeCandidateImpl
	 * @see ca.umontreal.geodes.meriem.cdeditor.metamodel.impl.MetamodelPackageImpl#getAttributeCandidate()
	 * @generated
	 */
	int ATTRIBUTE_CANDIDATE = 7;

	/**
	 * The feature id for the '<em><b>Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ATTRIBUTE_CANDIDATE__TYPE = TYPED_ELEMENT__TYPE;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ATTRIBUTE_CANDIDATE__NAME = TYPED_ELEMENT_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Attribute Candidate</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ATTRIBUTE_CANDIDATE_FEATURE_COUNT = TYPED_ELEMENT_FEATURE_COUNT + 1;

	/**
	 * The number of operations of the '<em>Attribute Candidate</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ATTRIBUTE_CANDIDATE_OPERATION_COUNT = TYPED_ELEMENT_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link ca.umontreal.geodes.meriem.cdeditor.metamodel.impl.AssociationImpl <em>Association</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see ca.umontreal.geodes.meriem.cdeditor.metamodel.impl.AssociationImpl
	 * @see ca.umontreal.geodes.meriem.cdeditor.metamodel.impl.MetamodelPackageImpl#getAssociation()
	 * @generated
	 */
	int ASSOCIATION = 8;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ASSOCIATION__NAME = NAMED_ELEMENT__NAME;

	/**
	 * The feature id for the '<em><b>Lower Bound</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ASSOCIATION__LOWER_BOUND = NAMED_ELEMENT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Source</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ASSOCIATION__SOURCE = NAMED_ELEMENT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Target</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ASSOCIATION__TARGET = NAMED_ELEMENT_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Upper Bound</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ASSOCIATION__UPPER_BOUND = NAMED_ELEMENT_FEATURE_COUNT + 3;

	/**
	 * The number of structural features of the '<em>Association</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ASSOCIATION_FEATURE_COUNT = NAMED_ELEMENT_FEATURE_COUNT + 4;

	/**
	 * The number of operations of the '<em>Association</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ASSOCIATION_OPERATION_COUNT = NAMED_ELEMENT_OPERATION_COUNT + 0;

	/**
	 * Returns the meta object for class '{@link ca.umontreal.geodes.meriem.cdeditor.metamodel.Model <em>Model</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Model</em>'.
	 * @see ca.umontreal.geodes.meriem.cdeditor.metamodel.Model
	 * @generated
	 */
	EClass getModel();

	/**
	 * Returns the meta object for the containment reference list '{@link ca.umontreal.geodes.meriem.cdeditor.metamodel.Model#getOperation <em>Operation</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Operation</em>'.
	 * @see ca.umontreal.geodes.meriem.cdeditor.metamodel.Model#getOperation()
	 * @see #getModel()
	 * @generated
	 */
	EReference getModel_Operation();

	/**
	 * Returns the meta object for the containment reference list '{@link ca.umontreal.geodes.meriem.cdeditor.metamodel.Model#getClazz <em>Clazz</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Clazz</em>'.
	 * @see ca.umontreal.geodes.meriem.cdeditor.metamodel.Model#getClazz()
	 * @see #getModel()
	 * @generated
	 */
	EReference getModel_Clazz();

	/**
	 * Returns the meta object for the containment reference list '{@link ca.umontreal.geodes.meriem.cdeditor.metamodel.Model#getAttribute <em>Attribute</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Attribute</em>'.
	 * @see ca.umontreal.geodes.meriem.cdeditor.metamodel.Model#getAttribute()
	 * @see #getModel()
	 * @generated
	 */
	EReference getModel_Attribute();

	/**
	 * Returns the meta object for the containment reference list '{@link ca.umontreal.geodes.meriem.cdeditor.metamodel.Model#getAttributecondidate <em>Attributecondidate</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Attributecondidate</em>'.
	 * @see ca.umontreal.geodes.meriem.cdeditor.metamodel.Model#getAttributecondidate()
	 * @see #getModel()
	 * @generated
	 */
	EReference getModel_Attributecondidate();

	/**
	 * Returns the meta object for the containment reference list '{@link ca.umontreal.geodes.meriem.cdeditor.metamodel.Model#getClazzcondidate <em>Clazzcondidate</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Clazzcondidate</em>'.
	 * @see ca.umontreal.geodes.meriem.cdeditor.metamodel.Model#getClazzcondidate()
	 * @see #getModel()
	 * @generated
	 */
	EReference getModel_Clazzcondidate();

	/**
	 * Returns the meta object for the containment reference list '{@link ca.umontreal.geodes.meriem.cdeditor.metamodel.Model#getAssociation <em>Association</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Association</em>'.
	 * @see ca.umontreal.geodes.meriem.cdeditor.metamodel.Model#getAssociation()
	 * @see #getModel()
	 * @generated
	 */
	EReference getModel_Association();

	/**
	 * Returns the meta object for class '{@link ca.umontreal.geodes.meriem.cdeditor.metamodel.NamedElement <em>Named Element</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Named Element</em>'.
	 * @see ca.umontreal.geodes.meriem.cdeditor.metamodel.NamedElement
	 * @generated
	 */
	EClass getNamedElement();

	/**
	 * Returns the meta object for the attribute '{@link ca.umontreal.geodes.meriem.cdeditor.metamodel.NamedElement#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see ca.umontreal.geodes.meriem.cdeditor.metamodel.NamedElement#getName()
	 * @see #getNamedElement()
	 * @generated
	 */
	EAttribute getNamedElement_Name();

	/**
	 * Returns the meta object for class '{@link ca.umontreal.geodes.meriem.cdeditor.metamodel.TypedElement <em>Typed Element</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Typed Element</em>'.
	 * @see ca.umontreal.geodes.meriem.cdeditor.metamodel.TypedElement
	 * @generated
	 */
	EClass getTypedElement();

	/**
	 * Returns the meta object for the attribute '{@link ca.umontreal.geodes.meriem.cdeditor.metamodel.TypedElement#getType <em>Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Type</em>'.
	 * @see ca.umontreal.geodes.meriem.cdeditor.metamodel.TypedElement#getType()
	 * @see #getTypedElement()
	 * @generated
	 */
	EAttribute getTypedElement_Type();

	/**
	 * Returns the meta object for class '{@link ca.umontreal.geodes.meriem.cdeditor.metamodel.AssociationCandidate <em>Association Candidate</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Association Candidate</em>'.
	 * @see ca.umontreal.geodes.meriem.cdeditor.metamodel.AssociationCandidate
	 * @generated
	 */
	EClass getAssociationCandidate();

	/**
	 * Returns the meta object for the reference '{@link ca.umontreal.geodes.meriem.cdeditor.metamodel.AssociationCandidate#getSource <em>Source</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Source</em>'.
	 * @see ca.umontreal.geodes.meriem.cdeditor.metamodel.AssociationCandidate#getSource()
	 * @see #getAssociationCandidate()
	 * @generated
	 */
	EReference getAssociationCandidate_Source();

	/**
	 * Returns the meta object for the reference '{@link ca.umontreal.geodes.meriem.cdeditor.metamodel.AssociationCandidate#getTarget <em>Target</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Target</em>'.
	 * @see ca.umontreal.geodes.meriem.cdeditor.metamodel.AssociationCandidate#getTarget()
	 * @see #getAssociationCandidate()
	 * @generated
	 */
	EReference getAssociationCandidate_Target();

	/**
	 * Returns the meta object for class '{@link ca.umontreal.geodes.meriem.cdeditor.metamodel.Clazz <em>Clazz</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Clazz</em>'.
	 * @see ca.umontreal.geodes.meriem.cdeditor.metamodel.Clazz
	 * @generated
	 */
	EClass getClazz();

	/**
	 * Returns the meta object for the reference list '{@link ca.umontreal.geodes.meriem.cdeditor.metamodel.Clazz#getGeneralizes <em>Generalizes</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Generalizes</em>'.
	 * @see ca.umontreal.geodes.meriem.cdeditor.metamodel.Clazz#getGeneralizes()
	 * @see #getClazz()
	 * @generated
	 */
	EReference getClazz_Generalizes();

	/**
	 * Returns the meta object for the reference '{@link ca.umontreal.geodes.meriem.cdeditor.metamodel.Clazz#getSpecializes <em>Specializes</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Specializes</em>'.
	 * @see ca.umontreal.geodes.meriem.cdeditor.metamodel.Clazz#getSpecializes()
	 * @see #getClazz()
	 * @generated
	 */
	EReference getClazz_Specializes();

	/**
	 * Returns the meta object for the reference list '{@link ca.umontreal.geodes.meriem.cdeditor.metamodel.Clazz#getHas <em>Has</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Has</em>'.
	 * @see ca.umontreal.geodes.meriem.cdeditor.metamodel.Clazz#getHas()
	 * @see #getClazz()
	 * @generated
	 */
	EReference getClazz_Has();

	/**
	 * Returns the meta object for the reference '{@link ca.umontreal.geodes.meriem.cdeditor.metamodel.Clazz#getIsMember <em>Is Member</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Is Member</em>'.
	 * @see ca.umontreal.geodes.meriem.cdeditor.metamodel.Clazz#getIsMember()
	 * @see #getClazz()
	 * @generated
	 */
	EReference getClazz_IsMember();

	/**
	 * Returns the meta object for the containment reference list '{@link ca.umontreal.geodes.meriem.cdeditor.metamodel.Clazz#getAttributes <em>Attributes</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Attributes</em>'.
	 * @see ca.umontreal.geodes.meriem.cdeditor.metamodel.Clazz#getAttributes()
	 * @see #getClazz()
	 * @generated
	 */
	EReference getClazz_Attributes();

	/**
	 * Returns the meta object for class '{@link ca.umontreal.geodes.meriem.cdeditor.metamodel.Attribute <em>Attribute</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Attribute</em>'.
	 * @see ca.umontreal.geodes.meriem.cdeditor.metamodel.Attribute
	 * @generated
	 */
	EClass getAttribute();

	/**
	 * Returns the meta object for class '{@link ca.umontreal.geodes.meriem.cdeditor.metamodel.ClazzCandidate <em>Clazz Candidate</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Clazz Candidate</em>'.
	 * @see ca.umontreal.geodes.meriem.cdeditor.metamodel.ClazzCandidate
	 * @generated
	 */
	EClass getClazzCandidate();

	/**
	 * Returns the meta object for the containment reference list '{@link ca.umontreal.geodes.meriem.cdeditor.metamodel.ClazzCandidate#getAttributecondidate <em>Attributecondidate</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Attributecondidate</em>'.
	 * @see ca.umontreal.geodes.meriem.cdeditor.metamodel.ClazzCandidate#getAttributecondidate()
	 * @see #getClazzCandidate()
	 * @generated
	 */
	EReference getClazzCandidate_Attributecondidate();

	/**
	 * Returns the meta object for the attribute '{@link ca.umontreal.geodes.meriem.cdeditor.metamodel.ClazzCandidate#getConfidence <em>Confidence</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Confidence</em>'.
	 * @see ca.umontreal.geodes.meriem.cdeditor.metamodel.ClazzCandidate#getConfidence()
	 * @see #getClazzCandidate()
	 * @generated
	 */
	EAttribute getClazzCandidate_Confidence();

	/**
	 * Returns the meta object for the reference '{@link ca.umontreal.geodes.meriem.cdeditor.metamodel.ClazzCandidate#getRelatedTo <em>Related To</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Related To</em>'.
	 * @see ca.umontreal.geodes.meriem.cdeditor.metamodel.ClazzCandidate#getRelatedTo()
	 * @see #getClazzCandidate()
	 * @generated
	 */
	EReference getClazzCandidate_RelatedTo();

	/**
	 * Returns the meta object for class '{@link ca.umontreal.geodes.meriem.cdeditor.metamodel.AttributeCandidate <em>Attribute Candidate</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Attribute Candidate</em>'.
	 * @see ca.umontreal.geodes.meriem.cdeditor.metamodel.AttributeCandidate
	 * @generated
	 */
	EClass getAttributeCandidate();

	/**
	 * Returns the meta object for class '{@link ca.umontreal.geodes.meriem.cdeditor.metamodel.Association <em>Association</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Association</em>'.
	 * @see ca.umontreal.geodes.meriem.cdeditor.metamodel.Association
	 * @generated
	 */
	EClass getAssociation();

	/**
	 * Returns the meta object for the attribute '{@link ca.umontreal.geodes.meriem.cdeditor.metamodel.Association#getLowerBound <em>Lower Bound</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Lower Bound</em>'.
	 * @see ca.umontreal.geodes.meriem.cdeditor.metamodel.Association#getLowerBound()
	 * @see #getAssociation()
	 * @generated
	 */
	EAttribute getAssociation_LowerBound();

	/**
	 * Returns the meta object for the reference '{@link ca.umontreal.geodes.meriem.cdeditor.metamodel.Association#getSource <em>Source</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Source</em>'.
	 * @see ca.umontreal.geodes.meriem.cdeditor.metamodel.Association#getSource()
	 * @see #getAssociation()
	 * @generated
	 */
	EReference getAssociation_Source();

	/**
	 * Returns the meta object for the reference '{@link ca.umontreal.geodes.meriem.cdeditor.metamodel.Association#getTarget <em>Target</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Target</em>'.
	 * @see ca.umontreal.geodes.meriem.cdeditor.metamodel.Association#getTarget()
	 * @see #getAssociation()
	 * @generated
	 */
	EReference getAssociation_Target();

	/**
	 * Returns the meta object for the attribute '{@link ca.umontreal.geodes.meriem.cdeditor.metamodel.Association#getUpperBound <em>Upper Bound</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Upper Bound</em>'.
	 * @see ca.umontreal.geodes.meriem.cdeditor.metamodel.Association#getUpperBound()
	 * @see #getAssociation()
	 * @generated
	 */
	EAttribute getAssociation_UpperBound();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	MetamodelFactory getMetamodelFactory();

	/**
	 * <!-- begin-user-doc -->
	 * Defines literals for the meta objects that represent
	 * <ul>
	 *   <li>each class,</li>
	 *   <li>each feature of each class,</li>
	 *   <li>each operation of each class,</li>
	 *   <li>each enum,</li>
	 *   <li>and each data type</li>
	 * </ul>
	 * <!-- end-user-doc -->
	 * @generated
	 */
	interface Literals {
		/**
		 * The meta object literal for the '{@link ca.umontreal.geodes.meriem.cdeditor.metamodel.impl.ModelImpl <em>Model</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see ca.umontreal.geodes.meriem.cdeditor.metamodel.impl.ModelImpl
		 * @see ca.umontreal.geodes.meriem.cdeditor.metamodel.impl.MetamodelPackageImpl#getModel()
		 * @generated
		 */
		EClass MODEL = eINSTANCE.getModel();

		/**
		 * The meta object literal for the '<em><b>Operation</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference MODEL__OPERATION = eINSTANCE.getModel_Operation();

		/**
		 * The meta object literal for the '<em><b>Clazz</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference MODEL__CLAZZ = eINSTANCE.getModel_Clazz();

		/**
		 * The meta object literal for the '<em><b>Attribute</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference MODEL__ATTRIBUTE = eINSTANCE.getModel_Attribute();

		/**
		 * The meta object literal for the '<em><b>Attributecondidate</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference MODEL__ATTRIBUTECONDIDATE = eINSTANCE.getModel_Attributecondidate();

		/**
		 * The meta object literal for the '<em><b>Clazzcondidate</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference MODEL__CLAZZCONDIDATE = eINSTANCE.getModel_Clazzcondidate();

		/**
		 * The meta object literal for the '<em><b>Association</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference MODEL__ASSOCIATION = eINSTANCE.getModel_Association();

		/**
		 * The meta object literal for the '{@link ca.umontreal.geodes.meriem.cdeditor.metamodel.impl.NamedElementImpl <em>Named Element</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see ca.umontreal.geodes.meriem.cdeditor.metamodel.impl.NamedElementImpl
		 * @see ca.umontreal.geodes.meriem.cdeditor.metamodel.impl.MetamodelPackageImpl#getNamedElement()
		 * @generated
		 */
		EClass NAMED_ELEMENT = eINSTANCE.getNamedElement();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute NAMED_ELEMENT__NAME = eINSTANCE.getNamedElement_Name();

		/**
		 * The meta object literal for the '{@link ca.umontreal.geodes.meriem.cdeditor.metamodel.impl.TypedElementImpl <em>Typed Element</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see ca.umontreal.geodes.meriem.cdeditor.metamodel.impl.TypedElementImpl
		 * @see ca.umontreal.geodes.meriem.cdeditor.metamodel.impl.MetamodelPackageImpl#getTypedElement()
		 * @generated
		 */
		EClass TYPED_ELEMENT = eINSTANCE.getTypedElement();

		/**
		 * The meta object literal for the '<em><b>Type</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute TYPED_ELEMENT__TYPE = eINSTANCE.getTypedElement_Type();

		/**
		 * The meta object literal for the '{@link ca.umontreal.geodes.meriem.cdeditor.metamodel.impl.AssociationCandidateImpl <em>Association Candidate</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see ca.umontreal.geodes.meriem.cdeditor.metamodel.impl.AssociationCandidateImpl
		 * @see ca.umontreal.geodes.meriem.cdeditor.metamodel.impl.MetamodelPackageImpl#getAssociationCandidate()
		 * @generated
		 */
		EClass ASSOCIATION_CANDIDATE = eINSTANCE.getAssociationCandidate();

		/**
		 * The meta object literal for the '<em><b>Source</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ASSOCIATION_CANDIDATE__SOURCE = eINSTANCE.getAssociationCandidate_Source();

		/**
		 * The meta object literal for the '<em><b>Target</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ASSOCIATION_CANDIDATE__TARGET = eINSTANCE.getAssociationCandidate_Target();

		/**
		 * The meta object literal for the '{@link ca.umontreal.geodes.meriem.cdeditor.metamodel.impl.ClazzImpl <em>Clazz</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see ca.umontreal.geodes.meriem.cdeditor.metamodel.impl.ClazzImpl
		 * @see ca.umontreal.geodes.meriem.cdeditor.metamodel.impl.MetamodelPackageImpl#getClazz()
		 * @generated
		 */
		EClass CLAZZ = eINSTANCE.getClazz();

		/**
		 * The meta object literal for the '<em><b>Generalizes</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CLAZZ__GENERALIZES = eINSTANCE.getClazz_Generalizes();

		/**
		 * The meta object literal for the '<em><b>Specializes</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CLAZZ__SPECIALIZES = eINSTANCE.getClazz_Specializes();

		/**
		 * The meta object literal for the '<em><b>Has</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CLAZZ__HAS = eINSTANCE.getClazz_Has();

		/**
		 * The meta object literal for the '<em><b>Is Member</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CLAZZ__IS_MEMBER = eINSTANCE.getClazz_IsMember();

		/**
		 * The meta object literal for the '<em><b>Attributes</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CLAZZ__ATTRIBUTES = eINSTANCE.getClazz_Attributes();

		/**
		 * The meta object literal for the '{@link ca.umontreal.geodes.meriem.cdeditor.metamodel.impl.AttributeImpl <em>Attribute</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see ca.umontreal.geodes.meriem.cdeditor.metamodel.impl.AttributeImpl
		 * @see ca.umontreal.geodes.meriem.cdeditor.metamodel.impl.MetamodelPackageImpl#getAttribute()
		 * @generated
		 */
		EClass ATTRIBUTE = eINSTANCE.getAttribute();

		/**
		 * The meta object literal for the '{@link ca.umontreal.geodes.meriem.cdeditor.metamodel.impl.ClazzCandidateImpl <em>Clazz Candidate</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see ca.umontreal.geodes.meriem.cdeditor.metamodel.impl.ClazzCandidateImpl
		 * @see ca.umontreal.geodes.meriem.cdeditor.metamodel.impl.MetamodelPackageImpl#getClazzCandidate()
		 * @generated
		 */
		EClass CLAZZ_CANDIDATE = eINSTANCE.getClazzCandidate();

		/**
		 * The meta object literal for the '<em><b>Attributecondidate</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CLAZZ_CANDIDATE__ATTRIBUTECONDIDATE = eINSTANCE.getClazzCandidate_Attributecondidate();

		/**
		 * The meta object literal for the '<em><b>Confidence</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CLAZZ_CANDIDATE__CONFIDENCE = eINSTANCE.getClazzCandidate_Confidence();

		/**
		 * The meta object literal for the '<em><b>Related To</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CLAZZ_CANDIDATE__RELATED_TO = eINSTANCE.getClazzCandidate_RelatedTo();

		/**
		 * The meta object literal for the '{@link ca.umontreal.geodes.meriem.cdeditor.metamodel.impl.AttributeCandidateImpl <em>Attribute Candidate</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see ca.umontreal.geodes.meriem.cdeditor.metamodel.impl.AttributeCandidateImpl
		 * @see ca.umontreal.geodes.meriem.cdeditor.metamodel.impl.MetamodelPackageImpl#getAttributeCandidate()
		 * @generated
		 */
		EClass ATTRIBUTE_CANDIDATE = eINSTANCE.getAttributeCandidate();

		/**
		 * The meta object literal for the '{@link ca.umontreal.geodes.meriem.cdeditor.metamodel.impl.AssociationImpl <em>Association</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see ca.umontreal.geodes.meriem.cdeditor.metamodel.impl.AssociationImpl
		 * @see ca.umontreal.geodes.meriem.cdeditor.metamodel.impl.MetamodelPackageImpl#getAssociation()
		 * @generated
		 */
		EClass ASSOCIATION = eINSTANCE.getAssociation();

		/**
		 * The meta object literal for the '<em><b>Lower Bound</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ASSOCIATION__LOWER_BOUND = eINSTANCE.getAssociation_LowerBound();

		/**
		 * The meta object literal for the '<em><b>Source</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ASSOCIATION__SOURCE = eINSTANCE.getAssociation_Source();

		/**
		 * The meta object literal for the '<em><b>Target</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ASSOCIATION__TARGET = eINSTANCE.getAssociation_Target();

		/**
		 * The meta object literal for the '<em><b>Upper Bound</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ASSOCIATION__UPPER_BOUND = eINSTANCE.getAssociation_UpperBound();

	}

} //MetamodelPackage
