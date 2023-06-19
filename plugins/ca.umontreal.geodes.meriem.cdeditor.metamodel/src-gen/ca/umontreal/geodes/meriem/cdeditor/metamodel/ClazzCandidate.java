/**
 */
package ca.umontreal.geodes.meriem.cdeditor.metamodel;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Clazz Candidate</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link ca.umontreal.geodes.meriem.cdeditor.metamodel.ClazzCandidate#getAttributecondidate <em>Attributecondidate</em>}</li>
 *   <li>{@link ca.umontreal.geodes.meriem.cdeditor.metamodel.ClazzCandidate#getConfidence <em>Confidence</em>}</li>
 *   <li>{@link ca.umontreal.geodes.meriem.cdeditor.metamodel.ClazzCandidate#getRelatedTo <em>Related To</em>}</li>
 * </ul>
 *
 * @see ca.umontreal.geodes.meriem.cdeditor.metamodel.MetamodelPackage#getClazzCandidate()
 * @model
 * @generated
 */
public interface ClazzCandidate extends NamedElement {
	/**
	 * Returns the value of the '<em><b>Attributecondidate</b></em>' containment reference list.
	 * The list contents are of type {@link ca.umontreal.geodes.meriem.cdeditor.metamodel.AttributeCandidate}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Attributecondidate</em>' containment reference list.
	 * @see ca.umontreal.geodes.meriem.cdeditor.metamodel.MetamodelPackage#getClazzCandidate_Attributecondidate()
	 * @model containment="true"
	 * @generated
	 */
	EList<AttributeCandidate> getAttributecondidate();

	/**
	 * Returns the value of the '<em><b>Confidence</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Confidence</em>' attribute.
	 * @see #setConfidence(int)
	 * @see ca.umontreal.geodes.meriem.cdeditor.metamodel.MetamodelPackage#getClazzCandidate_Confidence()
	 * @model
	 * @generated
	 */
	int getConfidence();

	/**
	 * Sets the value of the '{@link ca.umontreal.geodes.meriem.cdeditor.metamodel.ClazzCandidate#getConfidence <em>Confidence</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Confidence</em>' attribute.
	 * @see #getConfidence()
	 * @generated
	 */
	void setConfidence(int value);

	/**
	 * Returns the value of the '<em><b>Related To</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Related To</em>' reference.
	 * @see #setRelatedTo(Clazz)
	 * @see ca.umontreal.geodes.meriem.cdeditor.metamodel.MetamodelPackage#getClazzCandidate_RelatedTo()
	 * @model
	 * @generated
	 */
	Clazz getRelatedTo();

	/**
	 * Sets the value of the '{@link ca.umontreal.geodes.meriem.cdeditor.metamodel.ClazzCandidate#getRelatedTo <em>Related To</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Related To</em>' reference.
	 * @see #getRelatedTo()
	 * @generated
	 */
	void setRelatedTo(Clazz value);

} // ClazzCandidate
