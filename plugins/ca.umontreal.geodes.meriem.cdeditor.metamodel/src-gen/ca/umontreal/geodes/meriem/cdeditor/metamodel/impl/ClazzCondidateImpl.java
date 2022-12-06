/**
 */
package ca.umontreal.geodes.meriem.cdeditor.metamodel.impl;

import ca.umontreal.geodes.meriem.cdeditor.metamodel.AttributeCondidate;
import ca.umontreal.geodes.meriem.cdeditor.metamodel.Clazz;
import ca.umontreal.geodes.meriem.cdeditor.metamodel.ClazzCondidate;
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
 * An implementation of the model object '<em><b>Clazz Condidate</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link ca.umontreal.geodes.meriem.cdeditor.metamodel.impl.ClazzCondidateImpl#getAttributecondidate <em>Attributecondidate</em>}</li>
 *   <li>{@link ca.umontreal.geodes.meriem.cdeditor.metamodel.impl.ClazzCondidateImpl#getConfidence <em>Confidence</em>}</li>
 *   <li>{@link ca.umontreal.geodes.meriem.cdeditor.metamodel.impl.ClazzCondidateImpl#isAccept <em>Accept</em>}</li>
 *   <li>{@link ca.umontreal.geodes.meriem.cdeditor.metamodel.impl.ClazzCondidateImpl#getRelatedTo <em>Related To</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ClazzCondidateImpl extends NamedElementImpl implements ClazzCondidate {
	/**
	 * The cached value of the '{@link #getAttributecondidate() <em>Attributecondidate</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAttributecondidate()
	 * @generated
	 * @ordered
	 */
	protected EList<AttributeCondidate> attributecondidate;

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
	 * The default value of the '{@link #isAccept() <em>Accept</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isAccept()
	 * @generated
	 * @ordered
	 */
	protected static final boolean ACCEPT_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isAccept() <em>Accept</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isAccept()
	 * @generated
	 * @ordered
	 */
	protected boolean accept = ACCEPT_EDEFAULT;

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
	protected ClazzCondidateImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return MetamodelPackage.Literals.CLAZZ_CONDIDATE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<AttributeCondidate> getAttributecondidate() {
		if (attributecondidate == null) {
			attributecondidate = new EObjectContainmentEList<AttributeCondidate>(AttributeCondidate.class, this,
					MetamodelPackage.CLAZZ_CONDIDATE__ATTRIBUTECONDIDATE);
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
			eNotify(new ENotificationImpl(this, Notification.SET, MetamodelPackage.CLAZZ_CONDIDATE__CONFIDENCE,
					oldConfidence, confidence));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isAccept() {
		return accept;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setAccept(boolean newAccept) {
		boolean oldAccept = accept;
		accept = newAccept;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MetamodelPackage.CLAZZ_CONDIDATE__ACCEPT, oldAccept,
					accept));
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
							MetamodelPackage.CLAZZ_CONDIDATE__RELATED_TO, oldRelatedTo, relatedTo));
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
			eNotify(new ENotificationImpl(this, Notification.SET, MetamodelPackage.CLAZZ_CONDIDATE__RELATED_TO,
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
		case MetamodelPackage.CLAZZ_CONDIDATE__ATTRIBUTECONDIDATE:
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
		case MetamodelPackage.CLAZZ_CONDIDATE__ATTRIBUTECONDIDATE:
			return getAttributecondidate();
		case MetamodelPackage.CLAZZ_CONDIDATE__CONFIDENCE:
			return getConfidence();
		case MetamodelPackage.CLAZZ_CONDIDATE__ACCEPT:
			return isAccept();
		case MetamodelPackage.CLAZZ_CONDIDATE__RELATED_TO:
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
		case MetamodelPackage.CLAZZ_CONDIDATE__ATTRIBUTECONDIDATE:
			getAttributecondidate().clear();
			getAttributecondidate().addAll((Collection<? extends AttributeCondidate>) newValue);
			return;
		case MetamodelPackage.CLAZZ_CONDIDATE__CONFIDENCE:
			setConfidence((Integer) newValue);
			return;
		case MetamodelPackage.CLAZZ_CONDIDATE__ACCEPT:
			setAccept((Boolean) newValue);
			return;
		case MetamodelPackage.CLAZZ_CONDIDATE__RELATED_TO:
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
		case MetamodelPackage.CLAZZ_CONDIDATE__ATTRIBUTECONDIDATE:
			getAttributecondidate().clear();
			return;
		case MetamodelPackage.CLAZZ_CONDIDATE__CONFIDENCE:
			setConfidence(CONFIDENCE_EDEFAULT);
			return;
		case MetamodelPackage.CLAZZ_CONDIDATE__ACCEPT:
			setAccept(ACCEPT_EDEFAULT);
			return;
		case MetamodelPackage.CLAZZ_CONDIDATE__RELATED_TO:
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
		case MetamodelPackage.CLAZZ_CONDIDATE__ATTRIBUTECONDIDATE:
			return attributecondidate != null && !attributecondidate.isEmpty();
		case MetamodelPackage.CLAZZ_CONDIDATE__CONFIDENCE:
			return confidence != CONFIDENCE_EDEFAULT;
		case MetamodelPackage.CLAZZ_CONDIDATE__ACCEPT:
			return accept != ACCEPT_EDEFAULT;
		case MetamodelPackage.CLAZZ_CONDIDATE__RELATED_TO:
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
		result.append(", Accept: ");
		result.append(accept);
		result.append(')');
		return result.toString();
	}

} //ClazzCondidateImpl
