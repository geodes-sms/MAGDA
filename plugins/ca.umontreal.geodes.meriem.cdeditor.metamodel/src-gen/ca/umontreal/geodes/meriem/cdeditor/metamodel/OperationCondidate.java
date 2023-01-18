/**
 */
package ca.umontreal.geodes.meriem.cdeditor.metamodel;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Operation Condidate</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link ca.umontreal.geodes.meriem.cdeditor.metamodel.OperationCondidate#getSource <em>Source</em>}</li>
 *   <li>{@link ca.umontreal.geodes.meriem.cdeditor.metamodel.OperationCondidate#getTarget <em>Target</em>}</li>
 * </ul>
 *
 * @see ca.umontreal.geodes.meriem.cdeditor.metamodel.MetamodelPackage#getOperationCondidate()
 * @model
 * @generated
 */
public interface OperationCondidate extends NamedElement, TypedElement {
	/**
	 * Returns the value of the '<em><b>Source</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Source</em>' reference.
	 * @see #setSource(Clazz)
	 * @see ca.umontreal.geodes.meriem.cdeditor.metamodel.MetamodelPackage#getOperationCondidate_Source()
	 * @model required="true"
	 * @generated
	 */
	Clazz getSource();

	/**
	 * Sets the value of the '{@link ca.umontreal.geodes.meriem.cdeditor.metamodel.OperationCondidate#getSource <em>Source</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Source</em>' reference.
	 * @see #getSource()
	 * @generated
	 */
	void setSource(Clazz value);

	/**
	 * Returns the value of the '<em><b>Target</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Target</em>' reference.
	 * @see #setTarget(Clazz)
	 * @see ca.umontreal.geodes.meriem.cdeditor.metamodel.MetamodelPackage#getOperationCondidate_Target()
	 * @model required="true"
	 * @generated
	 */
	Clazz getTarget();

	/**
	 * Sets the value of the '{@link ca.umontreal.geodes.meriem.cdeditor.metamodel.OperationCondidate#getTarget <em>Target</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Target</em>' reference.
	 * @see #getTarget()
	 * @generated
	 */
	void setTarget(Clazz value);

} // OperationCondidate
