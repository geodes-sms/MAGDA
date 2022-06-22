/**
 */
package ca.umontreal.geodes.meriem.cdeditor.metamodel;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Model</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link ca.umontreal.geodes.meriem.cdeditor.metamodel.Model#getNamedelement <em>Namedelement</em>}</li>
 *   <li>{@link ca.umontreal.geodes.meriem.cdeditor.metamodel.Model#getTypedelement <em>Typedelement</em>}</li>
 *   <li>{@link ca.umontreal.geodes.meriem.cdeditor.metamodel.Model#getOperation <em>Operation</em>}</li>
 *   <li>{@link ca.umontreal.geodes.meriem.cdeditor.metamodel.Model#getClazz <em>Clazz</em>}</li>
 *   <li>{@link ca.umontreal.geodes.meriem.cdeditor.metamodel.Model#getAttribute <em>Attribute</em>}</li>
 * </ul>
 *
 * @see ca.umontreal.geodes.meriem.cdeditor.metamodel.MetamodelPackage#getModel()
 * @model
 * @generated
 */
public interface Model extends EObject {
	/**
	 * Returns the value of the '<em><b>Namedelement</b></em>' containment reference list.
	 * The list contents are of type {@link ca.umontreal.geodes.meriem.cdeditor.metamodel.NamedElement}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Namedelement</em>' containment reference list.
	 * @see ca.umontreal.geodes.meriem.cdeditor.metamodel.MetamodelPackage#getModel_Namedelement()
	 * @model containment="true"
	 * @generated
	 */
	EList<NamedElement> getNamedelement();

	/**
	 * Returns the value of the '<em><b>Typedelement</b></em>' containment reference list.
	 * The list contents are of type {@link ca.umontreal.geodes.meriem.cdeditor.metamodel.TypedElement}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Typedelement</em>' containment reference list.
	 * @see ca.umontreal.geodes.meriem.cdeditor.metamodel.MetamodelPackage#getModel_Typedelement()
	 * @model containment="true"
	 * @generated
	 */
	EList<TypedElement> getTypedelement();

	/**
	 * Returns the value of the '<em><b>Operation</b></em>' containment reference list.
	 * The list contents are of type {@link ca.umontreal.geodes.meriem.cdeditor.metamodel.Operation}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Operation</em>' containment reference list.
	 * @see ca.umontreal.geodes.meriem.cdeditor.metamodel.MetamodelPackage#getModel_Operation()
	 * @model containment="true"
	 * @generated
	 */
	EList<Operation> getOperation();

	/**
	 * Returns the value of the '<em><b>Clazz</b></em>' containment reference list.
	 * The list contents are of type {@link ca.umontreal.geodes.meriem.cdeditor.metamodel.Clazz}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Clazz</em>' containment reference list.
	 * @see ca.umontreal.geodes.meriem.cdeditor.metamodel.MetamodelPackage#getModel_Clazz()
	 * @model containment="true"
	 * @generated
	 */
	EList<Clazz> getClazz();

	/**
	 * Returns the value of the '<em><b>Attribute</b></em>' containment reference list.
	 * The list contents are of type {@link ca.umontreal.geodes.meriem.cdeditor.metamodel.Attribute}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Attribute</em>' containment reference list.
	 * @see ca.umontreal.geodes.meriem.cdeditor.metamodel.MetamodelPackage#getModel_Attribute()
	 * @model containment="true"
	 * @generated
	 */
	EList<Attribute> getAttribute();

} // Model
