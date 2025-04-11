package com.optimasc.datatypes.manager;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

import javax.xml.XMLConstants;
import javax.xml.namespace.QName;

import omg.org.astm.type.NamedTypeReference;

import com.optimasc.datatypes.Type;
import com.optimasc.datatypes.io.XMLSchemaDeserializer;
import com.optimasc.xml.QualifiedName;

/** A type symbol table. Implements a symbol table that consists
 *  of names and a type reference.
 *
 * @author Carl Eric Codere
 *
 */
public class TypeSymbolTable
{
  /* Represents XMLSchema prefixes if any. */
  protected Hashtable<String,String>  schemaRegistry;
  protected Hashtable<String, NamedTypeReference> values;

  public TypeSymbolTable()
  {
    super();
    schemaRegistry = new Hashtable<String, String>();
    values = new Hashtable<String,NamedTypeReference>();
    registerNamespace(XMLSchemaDeserializer.XSD_NAMESPACE,"xsd");
  }

  /** Return the named type reference */
  public NamedTypeReference get(String name)
  {
    return values.get(getID(name));
  }

  /** Return the type associated with the named type reference */
  public Type getType(String name)
  {
    return values.get(getID(name)).getType();
  }

  /** Adds a new named type reference in this symbol table,
   *  with the name equal to <code>getTypeName()</code> of
   *  the named type reference.
   *
   * @param value [in] The named type reference to add.
   */
  public NamedTypeReference put(NamedTypeReference value)
  {
    return values.put(value.getTypeName(), value);
  }


  /** Creates a new named type reference from the specified
   *  name and value.
   *
   * @param name [in] The type name
   * @param value [in] The type associated with this type.
   */
/*  public NamedTypeReference put(String name, Type value)
  {
    NamedTypeReference typ = new NamedTypeReference(name,value);
    return values.put(getID(name), typ);
  }*/

  public NamedTypeReference put(QName name, Type value)
  {
    String n = name.getPrefix()+":"+name.getLocalPart();
    registerNamespace(name.getPrefix(),name.getNamespaceURI());
    NamedTypeReference typ = new NamedTypeReference(n,value);
    return values.put(getID(n), typ);
  }


  public int size()
  {
    return values.size();
  }

  public NamedTypeReference remove(String key)
  {
    return values.remove(getID(key));
  }

  public Enumeration<String> keys()
  {
    return values.keys();
  }

  public Set<String> keySet()
  {
    return values.keySet();
  }



  /** Return the full identification string,
   *  taking into account namespace prefixes.
   *
   * @param name [in] The name of the identifier
   *   with potential namespace prefix.
   * @return The fully qualified name of the
   *  identifier, with any namespace prefix
   *   being resolved.
   */
  protected String getID(String name)
  {
    QName qName;
    String prefix = QualifiedName.getPrefix(name);
    String localPart = QualifiedName.getLocalPart(name);
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
    return new QName(namespaceURI,localPart,prefix).toString();
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





  public boolean isEmpty()
  {
    return values.isEmpty();
  }

  public void clear()
  {
    values.clear();
  }


}
