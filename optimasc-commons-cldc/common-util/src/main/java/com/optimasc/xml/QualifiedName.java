package com.optimasc.xml;

/** Represents an XML Qualified name and its associated namespace URI */
public class QualifiedName
{
  
  /** Returns the local part of a qualified name.
   * 
   * @param qualifiedName The qualified name.
   * @return  The local part of the qualified name.
   */
  public static String getLocalPart(String qualifiedName)
  {
    String s = qualifiedName.substring(qualifiedName.indexOf(':') + 1);
    return s;
  }
  
  
  /** Returns the expanded name of this qualified name. The
   *  expanded name syntax uses the RDF expanded name
   *  definition, i,e: determined by appending the local name part 
   *  of the qualified name after the namespace name (URI reference) 
   *  part of the QName
   * 
   * @param namespaceURI [in] The namespace URI 
   * @param qualifiedName [in] The qualified name or local name.
   * @return  The expanded name
   */
  public static String getExpandedName(String namespaceURI, String qualifiedName)
  {
    String localPart =  getLocalPart(qualifiedName);
    if (namespaceURI == null)
    {
      return localPart;
    }
    return namespaceURI + localPart; 
  }
  

  /** Returns the prefix name of a XML Qualified name. In
   *  the case that the qualified name has no prefix,
   *  the return value is the default prefix
   *  which is an empty string, as specified
   *  by the XML specifications.
   *  
   *  @see javax.xml.XMLConstants#DEFAULT_NS_PREFIX 
   * 
   * @param qualifiedName The QName
   * @return  The prefix part of the qualified name.
   */
  public static String getPrefix(String qualifiedName)
  {
    if (qualifiedName.indexOf(":")==-1)
    {
      return "";
    }
    return qualifiedName.substring(0, qualifiedName.indexOf(':'));
  }
  
  
}
