/**
 */
package ca.umontreal.geodes.meriem.cdeditor.metamodel.impl;

import ca.umontreal.geodes.meriem.cdeditor.metamodel.AttributeCandidate;
import ca.umontreal.geodes.meriem.cdeditor.metamodel.Clazz;
import ca.umontreal.geodes.meriem.cdeditor.metamodel.ClazzCandidate;
import ca.umontreal.geodes.meriem.cdeditor.metamodel.MetamodelPackage;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Clazz Candidate</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link ca.umontreal.geodes.meriem.cdeditor.metamodel.impl.ClazzCandidateImpl#getAttributecondidate <em>Attributecondidate</em>}</li>
 *   <li>{@link ca.umontreal.geodes.meriem.cdeditor.metamodel.impl.ClazzCandidateImpl#getConfidence <em>Confidence</em>}</li>
 *   <li>{@link ca.umontreal.geodes.meriem.cdeditor.metamodel.impl.ClazzCandidateImpl#getRelatedTo <em>Related To</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ClazzCandidateImpl extends NamedElementImpl implements ClazzCandidate {
	/**
	 * The cached value of the '{@link #getAttributecondidate() <em>Attributecondidate</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAttributecondidate()
	 * @generated
	 * @ordered
	 */
	protected EList<AttributeCandidate> attributecondidate;

	/**
	 * The default value of the '{@link #getConfidence() <em>Confidence</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getConfidence()
	 * @generated
	 * @ordered
	 */
	protected static final int CONFIDENCE_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getConfidence() <em>Confidence</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getConfidence()
	 * @generated
	 * @ordered
	 */
	protected int confidence = CONFIDENCE_EDEFAULT;

	/**
	 * The cached value of the '{@link #getRelatedTo() <em>Related To</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRelatedTo()
	 * @generated
	 * @ordered
	 */
	protected Clazz relatedTo;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ClazzCandidateImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return MetamodelPackage.Literals.CLAZZ_CANDIDATE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<AttributeCandidate> getAttributecondidate() {
		if (attributecondidate == null) {
			attributecondidate = new EObjectContainmentEList<AttributeCandidate>(AttributeCandidate.class, this,
					MetamodelPackage.CLAZZ_CANDIDATE__ATTRIBUTECONDIDATE);
		}
		return attributecondidate;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getConfidence() {
		return confidence;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setConfidence(int newConfidence) {
		int oldConfidence = confidence;
		confidence = newConfidence;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MetamodelPackage.CLAZZ_CANDIDATE__CONFIDENCE,
					oldConfidence, confidence));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Clazz getRelatedTo() {
		if (relatedTo != null && relatedTo.eIsProxy()) {
			InternalEObject oldRelatedTo = (InternalEObject) relatedTo;
			relatedTo = (Clazz) eResolveProxy(oldRelatedTo);
			if (relatedTo != oldRelatedTo) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE,
							MetamodelPackage.CLAZZ_CANDIDATE__RELATED_TO, oldRelatedTo, relatedTo));
			}
		}
		return relatedTo;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Clazz basicGetRelatedTo() {
		return relatedTo;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setRelatedTo(Clazz newRelatedTo) {
		Clazz oldRelatedTo = relatedTo;
		relatedTo = newRelatedTo;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MetamodelPackage.CLAZZ_CANDIDATE__RELATED_TO,
					oldRelatedTo, relatedTo));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
		case MetamodelPackage.CLAZZ_CANDIDATE__ATTRIBUTECONDIDATE:
			return ((InternalEList<?>) getAttributecondidate()).basicRemove(otherEnd, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
		case MetamodelPackage.CLAZZ_CANDIDATE__ATTRIBUTECONDIDATE:
			return getAttributecondidate();
		case MetamodelPackage.CLAZZ_CANDIDATE__CONFIDENCE:
			return getConfidence();
		case MetamodelPackage.CLAZZ_CANDIDATE__RELATED_TO:
			if (resolve)
				return getRelatedTo();
			return basicGetRelatedTo();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
		case MetamodelPackage.CLAZZ_CANDIDATE__ATTRIBUTECONDIDATE:
			getAttributecondidate().clear();
			getAttributecondidate().addAll((Collection<? extends AttributeCandidate>) newValue);
			return;
		case MetamodelPackage.CLAZZ_CANDIDATE__CONFIDENCE:
			setConfidence((Integer) newValue);
			return;
		case MetamodelPackage.CLAZZ_CANDIDATE__RELATED_TO:
			setRelatedTo((Clazz) newValue);
			return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
		case MetamodelPackage.CLAZZ_CANDIDATE__ATTRIBUTECONDIDATE:
			getAttributecondidate().clear();
			return;
		case MetamodelPackage.CLAZZ_CANDIDATE__CONFIDENCE:
			setConfidence(CONFIDENCE_EDEFAULT);
			return;
		case MetamodelPackage.CLAZZ_CANDIDATE__RELATED_TO:
			setRelatedTo((Clazz) null);
			return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
		case MetamodelPackage.CLAZZ_CANDIDATE__ATTRIBUTECONDIDATE:
			return attributecondidate != null && !attributecondidate.isEmpty();
		case MetamodelPackage.CLAZZ_CANDIDATE__CONFIDENCE:
			return confidence != CONFIDENCE_EDEFAULT;
		case MetamodelPackage.CLAZZ_CANDIDATE__RELATED_TO:
			return relatedTo != null;
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy())
			return super.toString();

		StringBuilder result = new StringBuilder(super.toString());
		result.append(" (confidence: ");
		result.append(confidence);
		result.append(')');
		return result.toString();
	}

} //ClazzCandidateImpl
