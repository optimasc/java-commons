package com.optimasc.datatypes.io;

import java.io.InputStream;
import java.util.Hashtable;

import javax.xml.namespace.QName;

import com.optimasc.datatypes.Datatype;
import com.optimasc.datatypes.manager.TypeSymbolTable;

public interface Deserializer
{
  public TypeSymbolTable load(InputStream stream);  
}
