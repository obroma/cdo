/**
 * <copyright>
 * </copyright>
 *
 * $Id: EPackageDefValidator.java,v 1.1 2008-12-29 14:01:20 estepper Exp $
 */
package org.eclipse.emf.cdo.cdodefs.validation;


/**
 * A sample validator interface for {@link org.eclipse.emf.cdo.cdodefs.EPackageDef}.
 * This doesn't really do anything, and it's not a real EMF artifact.
 * It was generated by the org.eclipse.emf.examples.generator.validator plug-in to illustrate how EMF's code generator can be extended.
 * This can be disabled with -vmargs -Dorg.eclipse.emf.examples.generator.validator=false.
 */
public interface EPackageDefValidator
{
  boolean validate();

  boolean validateNsURI(String value);
}
