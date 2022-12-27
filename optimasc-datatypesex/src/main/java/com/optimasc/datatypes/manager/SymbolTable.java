package com.optimasc.datatypes.manager;

import java.util.Map;

import javax.xml.namespace.QName;

import com.optimasc.datatypes.Datatype;

/** Represents an interface to access symbol table */
public interface SymbolTable<K,V> extends Map<K, V>
{
  public Datatype get(String name);

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
