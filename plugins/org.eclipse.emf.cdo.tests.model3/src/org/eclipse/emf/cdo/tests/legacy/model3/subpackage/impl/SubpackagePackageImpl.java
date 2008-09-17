/**
 * <copyright>
 * </copyright>
 *
 * $Id: SubpackagePackageImpl.java,v 1.1.2.1 2008-09-17 13:03:52 estepper Exp $
 */
package org.eclipse.emf.cdo.tests.legacy.model3.subpackage.impl;

import org.eclipse.emf.cdo.tests.legacy.model3.Model3Package;
import org.eclipse.emf.cdo.tests.legacy.model3.impl.Model3PackageImpl;
import org.eclipse.emf.cdo.tests.legacy.model3.subpackage.SubpackageFactory;
import org.eclipse.emf.cdo.tests.legacy.model3.subpackage.SubpackagePackage;
import org.eclipse.emf.cdo.tests.model3.subpackage.Class2;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.impl.EPackageImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class SubpackagePackageImpl extends EPackageImpl implements SubpackagePackage
{
  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass class2EClass = null;

  /**
   * Creates an instance of the model <b>Package</b>, registered with
   * {@link org.eclipse.emf.ecore.EPackage.Registry EPackage.Registry} by the package
   * package URI value.
   * <p>Note: the correct way to create the package is via the static
   * factory method {@link #init init()}, which also performs
   * initialization of the package, or returns the registered package,
   * if one already exists.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.emf.ecore.EPackage.Registry
   * @see org.eclipse.emf.cdo.tests.legacy.model3.subpackage.SubpackagePackage#eNS_URI
   * @see #init()
   * @generated
   */
  private SubpackagePackageImpl()
  {
    super(eNS_URI, SubpackageFactory.eINSTANCE);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private static boolean isInited = false;

  /**
   * Creates, registers, and initializes the <b>Package</b> for this
   * model, and for any others upon which it depends.  Simple
   * dependencies are satisfied by calling this method on all
   * dependent packages before doing anything else.  This method drives
   * initialization for interdependent packages directly, in parallel
   * with this package, itself.
   * <p>Of this package and its interdependencies, all packages which
   * have not yet been registered by their URI values are first created
   * and registered.  The packages are then initialized in two steps:
   * meta-model objects for all of the packages are created before any
   * are initialized, since one package's meta-model objects may refer to
   * those of another.
   * <p>Invocation of this method will not affect any packages that have
   * already been initialized.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #eNS_URI
   * @see #createPackageContents()
   * @see #initializePackageContents()
   * @generated
   */
  public static SubpackagePackage init()
  {
    if (isInited)
      return (SubpackagePackage)EPackage.Registry.INSTANCE.getEPackage(SubpackagePackage.eNS_URI);

    // Obtain or create and register package
    SubpackagePackageImpl theSubpackagePackage = (SubpackagePackageImpl)(EPackage.Registry.INSTANCE
        .getEPackage(eNS_URI) instanceof SubpackagePackageImpl ? EPackage.Registry.INSTANCE.getEPackage(eNS_URI)
        : new SubpackagePackageImpl());

    isInited = true;

    // Initialize simple dependencies
    EcorePackage.eINSTANCE.eClass();

    // Obtain or create and register interdependencies
    Model3PackageImpl theModel3Package = (Model3PackageImpl)(EPackage.Registry.INSTANCE
        .getEPackage(Model3Package.eNS_URI) instanceof Model3PackageImpl ? EPackage.Registry.INSTANCE
        .getEPackage(Model3Package.eNS_URI) : Model3Package.eINSTANCE);

    // Create package meta-data objects
    theSubpackagePackage.createPackageContents();
    theModel3Package.createPackageContents();

    // Initialize created meta-data
    theSubpackagePackage.initializePackageContents();
    theModel3Package.initializePackageContents();

    // Mark meta-data to indicate it can't be changed
    theSubpackagePackage.freeze();

    return theSubpackagePackage;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getClass2()
  {
    return class2EClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getClass2_Class1()
  {
    return (EReference)class2EClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public SubpackageFactory getSubpackageFactory()
  {
    return (SubpackageFactory)getEFactoryInstance();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private boolean isCreated = false;

  /**
   * Creates the meta-model objects for the package.  This method is
   * guarded to have no affect on any invocation but its first.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void createPackageContents()
  {
    if (isCreated)
      return;
    isCreated = true;

    // Create classes and their features
    class2EClass = createEClass(CLASS2);
    createEReference(class2EClass, CLASS2__CLASS1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private boolean isInitialized = false;

  /**
   * Complete the initialization of the package and its meta-model.  This
   * method is guarded to have no affect on any invocation but its first.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void initializePackageContents()
  {
    if (isInitialized)
      return;
    isInitialized = true;

    // Initialize package
    setName(eNAME);
    setNsPrefix(eNS_PREFIX);
    setNsURI(eNS_URI);

    // Obtain other dependent packages
    Model3Package theModel3Package = (Model3Package)EPackage.Registry.INSTANCE.getEPackage(Model3Package.eNS_URI);

    // Create type parameters

    // Set bounds for type parameters

    // Add supertypes to classes

    // Initialize classes and features; add operations and parameters
    initEClass(class2EClass, Class2.class, "Class2", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getClass2_Class1(), theModel3Package.getClass1(), theModel3Package.getClass1_Class2(), "class1",
        null, 0, -1, Class2.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES,
        !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
  }

} //SubpackagePackageImpl
