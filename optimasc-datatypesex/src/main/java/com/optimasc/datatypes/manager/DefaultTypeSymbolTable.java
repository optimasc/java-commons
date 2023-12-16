package com.optimasc.datatypes.manager;

import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;
import java.util.Map;

import javax.xml.XMLConstants;
import javax.xml.namespace.QName;

import omg.org.astm.type.TypeReference;

import com.optimasc.datatypes.Datatype;
import com.optimasc.datatypes.Type;
import com.optimasc.datatypes.io.Deserializer;
import com.optimasc.datatypes.io.XMLSchemaDeserializer;

public class DefaultTypeSymbolTable extends Hashtable<QName,Type> implements TypeSymbolTable
{
  /* Represents all type definitions. */
  protected Hashtable<String,String>  schemaRegistry;
  
  public DefaultTypeSymbolTable() 
  {
    super();
    schemaRegistry = new Hashtable<String, String>();
    registerNamespace(XMLSchemaDeserializer.XSD_NAMESPACE,"xsd");
  }

  public Type get(String name)
  {
    QName qName;
    String prefix = getPrefixFromQName(name);
    String localPart = getLocalPartFromQName(name);
    String namespaceURI = XMLConstants.NULL_NS_URI;
    if (prefix == null)
    {
      prefix = XMLConstants.DEFAULT_NS_PREFIX;
    }
    namespaceURI = schemaRegistry.get(prefix);
    if (namespaceURI==null)
    {
      namespaceURI = XMLConstants.NULL_NS_URI;
    }
    qName = new QName(namespaceURI,localPart,prefix);
    return get(qName);
  }


  public void registerNamespace(String namespaceURI, String suggestedPrefix)
  {
    schemaRegistry.put(suggestedPrefix, namespaceURI);
  }

  public String getNamespaceURI(String prefix)
  {
    return schemaRegistry.get(prefix);
  }

  public Map getPrefixes()
  {
    return schemaRegistry;
  }
  
  /** Returns the local part of a qualified namespace name.
   * 
   * @param qualifiedName The QName
   * @return  The local part of the qualified name.
   */
  public static String getLocalPartFromQName(String qualifiedName)
  {
    String s = qualifiedName.substring(qualifiedName.indexOf(':') + 1);
    /* Check if it ends with an array element */
    int index = s.lastIndexOf("[");
    if (index != -1)
      s = s.substring(0, index);
    return s;
  }

  /** Returns the prefix name of a XML Qualified namespace.
   * 
   * @param qualifiedName The QName
   * @return  The prefix part of the qualified name.
   */
  public static String getPrefixFromQName(String qualifiedName)
  {
    if (qualifiedName.indexOf(":")==-1)
    {
      return "";
    }
    return qualifiedName.substring(0, qualifiedName.indexOf(':'));
  }

}
