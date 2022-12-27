package com.optimasc.datatypes.io;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Hashtable;

import com.optimasc.datatypes.Datatype;

public interface Serializer
{
  public void save(OutputStream stream);  
}
