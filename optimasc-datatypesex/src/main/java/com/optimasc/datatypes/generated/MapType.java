package com.optimasc.datatypes.generated;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.StringTokenizer;

import com.optimasc.datatypes.ConstructedSimple;
import com.optimasc.datatypes.Datatype;
import com.optimasc.datatypes.DatatypeException;
import com.optimasc.datatypes.utils.VisualMap;
import com.optimasc.datatypes.visitor.DatatypeVisitor;

/** Alternate list of key-value pairs. */
public class MapType extends Datatype implements ConstructedSimple
{
  protected Datatype valueDatatype;
  protected Datatype keyDatatype;
  protected static final LinkedHashMap INSTANCE_OBJECT = new LinkedHashMap();
  
  /** Default separator character to separate elements in the list. */
  protected static final String ENTRY_SEPARATOR_CHAR = ";";
  /** Default separator character to separate the key from the value in the list. */
  protected static final String KEY_SEPARATOR_CHAR = "=";
  protected String entrySeparatorChar = ENTRY_SEPARATOR_CHAR;
  protected String keySeparatorChar = KEY_SEPARATOR_CHAR;
  
  public MapType()
  {
      super(Datatype.OTHER);
      setElementType(new StringTypeEx());
      setKeyDatatype(new StringTypeEx());
  }

  public Datatype getElementType()
  {
    return valueDatatype;
  }

  public void setElementType(Datatype value)
  {
    valueDatatype = value;
  }

  @Override
  public int getSize()
  {
    return -1;
  }

  @Override
  public Class getClassType()
  {
    return HashMap.class;
  }

  @Override
  public Object getObjectType()
  {
    return INSTANCE_OBJECT;
  }

  @Override
  public void validate(Object value) throws IllegalArgumentException, DatatypeException
  {
    checkClass(value);
    HashMap v = (HashMap)value;
    /* Check each value in the map. */
    Iterator keyIterator = v.keySet().iterator();
    while (keyIterator.hasNext())
    {
      Object key = keyIterator.next();
      keyDatatype.validate(key);
      valueDatatype.validate(v.get(key));
    }
  }

  @Override
  public Object parse(String value) throws ParseException
  {
    VisualMap<Object,Object> visualMap = new VisualMap<Object, Object>();
    StringTokenizer st = new StringTokenizer(value,entrySeparatorChar);
    while (st.hasMoreTokens()) 
    {
        String entryString = st.nextToken();
        String entry[] = entryString.split(keySeparatorChar);
        visualMap.put(keyDatatype.parse(entry[0]),valueDatatype.parse(entry[1]));
    }
    try
    {
      validate(visualMap);
    } catch (IllegalArgumentException e)
    {
      throw new ParseException("Error while parsing list items.",0);
    } catch (DatatypeException e)
    {
      throw new ParseException("Error while parsing list items.",0);
    }
    return visualMap;
  }

  @Override
  public Object accept(DatatypeVisitor v, Object arg)
  {
    // TODO Auto-generated method stub
    return null;
  }

  public Datatype getKeyDatatype()
  {
    return keyDatatype;
  }

  public void setKeyDatatype(Datatype keyDatatype)
  {
    this.keyDatatype = keyDatatype;
  }

  public String getEntrySeparatorChar()
  {
    return entrySeparatorChar;
  }

  public void setEntrySeparatorChar(String entrySeparatorChar)
  {
    this.entrySeparatorChar = entrySeparatorChar;
  }

  public String getKeySeparatorChar()
  {
    return keySeparatorChar;
  }

  public void setKeySeparatorChar(String keySeparatorChar)
  {
    this.keySeparatorChar = keySeparatorChar;
  }
  
  
  
  
}
