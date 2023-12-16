package com.optimasc.datatypes.generated;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.StringTokenizer;

import omg.org.astm.type.TypeReference;
import omg.org.astm.type.UnnamedTypeReference;

import com.optimasc.datatypes.ConstructedSimple;
import com.optimasc.datatypes.Datatype;
import com.optimasc.datatypes.DatatypeException;
import com.optimasc.datatypes.Parseable;
import com.optimasc.datatypes.Type;
import com.optimasc.datatypes.derived.UCS2StringType;
import com.optimasc.datatypes.utils.VisualMap;
import com.optimasc.datatypes.visitor.TypeVisitor;

/** Alternate list of key-value pairs. */
public class MapType extends Datatype implements ConstructedSimple, Parseable
{
  protected TypeReference valueDatatypeReference;
  protected Type valueDatatype;
  protected TypeReference keyDatatypeReference;
  protected Type keyDatatype;
  protected static final LinkedHashMap INSTANCE_OBJECT = new LinkedHashMap();
  
  /** Default separator character to separate elements in the list. */
  protected static final String ENTRY_SEPARATOR_CHAR = ";";
  /** Default separator character to separate the key from the value in the list. */
  protected static final String KEY_SEPARATOR_CHAR = "=";
  protected String entrySeparatorChar = ENTRY_SEPARATOR_CHAR;
  protected String keySeparatorChar = KEY_SEPARATOR_CHAR;
  
  public MapType()
  {
      super(Datatype.OTHER,false);
      setBaseTypeReference(UCS2StringType.TYPE_REFERENCE);
      setKeyDatatype(UCS2StringType.TYPE_REFERENCE);
  }

  public TypeReference getBaseTypeReference()
  {
    return valueDatatypeReference;
  }

  public void setBaseTypeReference(TypeReference value)
  {
    valueDatatypeReference = value;
    valueDatatype = value.getType();
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

  /** Validates the values, which must be a {@link Map}. 
   * 
   */
  @Override
  public void validate(Object value) throws IllegalArgumentException, DatatypeException
  {
    checkClass(value);
    Map v = (Map)value;
    /* Check each value in the map. */
    Iterator keyIterator = v.keySet().iterator();
    while (keyIterator.hasNext())
    {
      Object key = keyIterator.next();
      if (keyDatatype instanceof Datatype)
      {
        ((Datatype)keyDatatype).validate(key);
      }
      if (valueDatatype instanceof Datatype)
      {
        ((Datatype)valueDatatype).validate(v.get(key));
      }
    }
  }

  public Object parse(String value) throws ParseException
  {
    VisualMap<Object,Object> visualMap = new VisualMap<Object, Object>();
    StringTokenizer st = new StringTokenizer(value,entrySeparatorChar);
    while (st.hasMoreTokens()) 
    {
        String entryString = st.nextToken();
        String entry[] = entryString.split(keySeparatorChar);
        
        Object keyParsedValue = entry[0];
        if (keyDatatype instanceof Parseable)
        {
          keyParsedValue =((Parseable)keyDatatype).parse(entry[0]);
        }
        Object valueParsedValue = entry[1];
        if (valueDatatype instanceof Parseable)
        {
          valueParsedValue = ((Parseable)valueDatatype).parse(entry[1]);
        }
        visualMap.put(keyParsedValue,valueParsedValue);
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
  public Object accept(TypeVisitor v, Object arg)
  {
    // TODO Auto-generated method stub
    return null;
  }

  public TypeReference getKeyDatatype()
  {
    return keyDatatypeReference;
  }

  public void setKeyDatatype(TypeReference keyDatatype)
  {
    this.keyDatatypeReference = keyDatatype;
    this.keyDatatype = keyDatatype.getType();
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
