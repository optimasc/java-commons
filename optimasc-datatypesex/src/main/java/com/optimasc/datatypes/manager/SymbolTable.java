package com.optimasc.datatypes.manager;

import java.util.Map;

import javax.xml.namespace.QName;

import com.optimasc.datatypes.Datatype;
import com.optimasc.datatypes.Type;

/** Represents an interface to access a symbol table */
public interface SymbolTable<K,V> extends Map<K, V>
{
  /** Get specified named type. The name is used
   *  to identified a type using a QName format.
   * 
   * @param name
   * @return
   */
  public V get(String name);

  /** Register the specified namespace with the proposed prefix. */
  public void registerNamespace(String namespaceURI, String suggestedPrefix);
  
  /**
   * Obtain the URI for a registered namespace prefix.
   * <p>
   * It is not an error if the namespace prefix is not registered.
   * 
   * @param namespacePrefix
   *            The prefix for the namespace. Must not be null or the empty
   *            string.
   * @return Returns the URI registered for this prefix or null.
   */
  public String getNamespaceURI(String prefix);
  
  /**
   * @return Returns the registered namespace/prefix-pairs as map, where the keys are the
   *         prefixes and the values are the namespaces.
   */
  public Map getPrefixes();
  
  
}
