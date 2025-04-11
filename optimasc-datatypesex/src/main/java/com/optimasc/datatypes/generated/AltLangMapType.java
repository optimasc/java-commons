package com.optimasc.datatypes.generated;

import omg.org.astm.type.UnnamedTypeReference;

/** Represents a map type that contains a default value. This is mainly
 *  used to represent a Lang Alt map used by the XMP Specification.
 *  
 *  The {@link #parseObject(String)} method accepts different formats for
 *  the parsing. If there is only one value with no key separator
 *  then it will be correctly be parsed and will be considered
 *  the value with the x-default key. 
 *  
 * @author Carl Eric Codere
 *
 */
public class AltLangMapType extends MapType
{
  /** The x-default string for localized properties */
  String X_DEFAULT = "x-default";
  
  public AltLangMapType()
  {
      super();
      setKeyDatatype(new UnnamedTypeReference(new LanguageType()));
  }
  
}
