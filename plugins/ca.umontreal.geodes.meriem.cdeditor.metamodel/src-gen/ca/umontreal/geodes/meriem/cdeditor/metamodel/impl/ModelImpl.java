/**
 */
package ca.umontreal.geodes.meriem.cdeditor.metamodel.impl;

import ca.umontreal.geodes.meriem.cdeditor.metamodel.Attribute;
import ca.umontreal.geodes.meriem.cdeditor.metamodel.Clazz;
import ca.umontreal.geodes.meriem.cdeditor.metamodel.MetamodelPackage;
import ca.umontreal.geodes.meriem.cdeditor.metamodel.Model;
import ca.umontreal.geodes.meriem.cdeditor.metamodel.NamedElement;
import ca.umontreal.geodes.meriem.cdeditor.metamodel.Operation;
import ca.umontreal.geodes.meriem.cdeditor.metamodel.TypedElement;

import java.util.Collection;

import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Model</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link ca.umontreal.geodes.meriem.cdeditor.metamodel.impl.ModelImpl#getNamedelement <em>Namedelement</em>}</li>
 *   <li>{@link ca.umontreal.geodes.meriem.cdeditor.metamodel.impl.ModelImpl#getTypedelement <em>Typedelement</em>}</li>
 *   <li>{@link ca.umontreal.geodes.meriem.cdeditor.metamodel.impl.ModelImpl#getOperation <em>Operation</em>}</li>
 *   <li>{@link ca.umontreal.geodes.meriem.cdeditor.metamodel.impl.ModelImpl#getClazz <em>Clazz</em>}</li>
 *   <li>{@link ca.umontreal.geodes.meriem.cdeditor.metamodel.impl.ModelImpl#getAttribute <em>Attribute</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ModelImpl extends MinimalEObjectImpl.Container implements Model {
	/**
	 * The cached value of the '{@link #getNamedelement() <em>Namedelement</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNamedelement()
	 * @generated
	 * @ordered
	 */
	protected EList<NamedElement> namedelement;

	/**
	 * The cached value of the '{@link #getTypedelement() <em>Typedelement</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTypedelement()
	 * @generated
	 * @ordered
	 */
	protected EList<TypedElement> typedelement;

	/**
	 * The cached value of the '{@link #getOperation() <em>Operation</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOperation()
	 * @generated
	 * @ordered
	 */
	protected EList<Operation> operation;

	/**
	 * The cached value of the '{@link #getClazz() <em>Clazz</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getClazz()
	 * @generated
	 * @ordered
	 */
	protected EList<Clazz> clazz;

	/**
	 * The cached value of the '{@link #getAttribute() <em>Attribute</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAttribute()
	 * @generated
	 * @ordered
	 */
	protected EList<Attribute> attribute;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ModelImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return MetamodelPackage.Literals.MODEL;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<NamedElement> getNamedelement() {
		if (namedelement == null) {
			namedelement = new EObjectContainmentEList<NamedElement>(NamedElement.class, this,
					MetamodelPackage.MODEL__NAMEDELEMENT);
		}
		return namedelement;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<TypedElement> getTypedelement() {
		if (typedelement == null) {
			typedelement = new EObjectContainmentEList<TypedElement>(TypedElement.class, this,
					MetamodelPackage.MODEL__TYPEDELEMENT);
		}
		return typedelement;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Operation> getOperation() {
		if (operation == null) {
			operation = new EObjectContainmentEList<Operation>(Operation.class, this,
					MetamodelPackage.MODEL__OPERATION);
		}
		return operation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Clazz> getClazz() {
		if (clazz == null) {
			clazz = new EObjectContainmentEList<Clazz>(Clazz.class, this, MetamodelPackage.MODEL__CLAZZ);
		}
		return clazz;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Attribute> getAttribute() {
		if (attribute == null) {
			attribute = new EObjectContainmentEList<Attribute>(Attribute.class, this,
					MetamodelPackage.MODEL__ATTRIBUTE);
		}
		return attribute;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
		case MetamodelPackage.MODEL__NAMEDELEMENT:
			return ((InternalEList<?>) getNamedelement()).basicRemove(otherEnd, msgs);
		case MetamodelPackage.MODEL__TYPEDELEMENT:
			return ((InternalEList<?>) getTypedelement()).basicRemove(otherEnd, msgs);
		case MetamodelPackage.MODEL__OPERATION:
			return ((InternalEList<?>) getOperation()).basicRemove(otherEnd, msgs);
		case MetamodelPackage.MODEL__CLAZZ:
			return ((InternalEList<?>) getClazz()).basicRemove(otherEnd, msgs);
		case MetamodelPackage.MODEL__ATTRIBUTE:
			return ((InternalEList<?>) getAttribute()).basicRemove(otherEnd, msgs);
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
		case MetamodelPackage.MODEL__NAMEDELEMENT:
			return getNamedelement();
		case MetamodelPackage.MODEL__TYPEDELEMENT:
			return getTypedelement();
		case MetamodelPackage.MODEL__OPERATION:
			return getOperation();
		case MetamodelPackage.MODEL__CLAZZ:
			return getClazz();
		case MetamodelPackage.MODEL__ATTRIBUTE:
			return getAttribute();
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
		case MetamodelPackage.MODEL__NAMEDELEMENT:
			getNamedelement().clear();
			getNamedelement().addAll((Collection<? extends NamedElement>) newValue);
			return;
		case MetamodelPackage.MODEL__TYPEDELEMENT:
			getTypedelement().clear();
			getTypedelement().addAll((Collection<? extends TypedElement>) newValue);
			return;
		case MetamodelPackage.MODEL__OPERATION:
			getOperation().clear();
			getOperation().addAll((Collection<? extends Operation>) newValue);
			return;
		case MetamodelPackage.MODEL__CLAZZ:
			getClazz().clear();
			getClazz().addAll((Collection<? extends Clazz>) newValue);
			return;
		case MetamodelPackage.MODEL__ATTRIBUTE:
			getAttribute().clear();
			getAttribute().addAll((Collection<? extends Attribute>) newValue);
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
		case MetamodelPackage.MODEL__NAMEDELEMENT:
			getNamedelement().clear();
			return;
		case MetamodelPackage.MODEL__TYPEDELEMENT:
			getTypedelement().clear();
			return;
		case MetamodelPackage.MODEL__OPERATION:
			getOperation().clear();
			return;
		case MetamodelPackage.MODEL__CLAZZ:
			getClazz().clear();
			return;
		case MetamodelPackage.MODEL__ATTRIBUTE:
			getAttribute().clear();
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
		case MetamodelPackage.MODEL__NAMEDELEMENT:
			return namedelement != null && !namedelement.isEmpty();
		case MetamodelPackage.MODEL__TYPEDELEMENT:
			return typedelement != null && !typedelement.isEmpty();
		case MetamodelPackage.MODEL__OPERATION:
			return operation != null && !operation.isEmpty();
		case MetamodelPackage.MODEL__CLAZZ:
			return clazz != null && !clazz.isEmpty();
		case MetamodelPackage.MODEL__ATTRIBUTE:
			return attribute != null && !attribute.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} //ModelImpl
