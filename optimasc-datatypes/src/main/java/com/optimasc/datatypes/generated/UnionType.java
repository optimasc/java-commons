package com.optimasc.datatypes.generated;

import java.text.ParseException;
import java.util.Vector;

import com.optimasc.datatypes.Datatype;
import com.optimasc.datatypes.DatatypeException;
import com.optimasc.datatypes.PatternFacet;
import com.optimasc.datatypes.visitor.TypeVisitor;

/** Represents a value that can be one or more different
 *  datatypes.
 *  
 *  @author Carl Eric Codere
 *
 */
public class UnionType extends Datatype implements PatternFacet
{
  protected static final Object INSTANCE_TYPE = new Object();
  
  /* Represents each possible datatype allowed. */
  protected Vector allowedTypes;

  public UnionType()
  {
    super(Datatype.OTHER,false);
    allowedTypes = new Vector();
  }

  public UnionType(String name, String comment, int type, int flags)
  {
    super(name, comment, type, false, flags);
  }

  public int getSize()
  {
    return -1;
  }

  public Class getClassType()
  {
    return Object.class;
  }

  public Object getObjectType()
  {
    return INSTANCE_TYPE;
  }

  public void validate(Object value) throws IllegalArgumentException, DatatypeException
  {
    int i;
    boolean success = false;
    Exception thrownException = null;
    /* We go through each alllowed types, and if one is ok, then we stop. */
    for (i = 0; i < allowedTypes.size(); i++)
    {
      try 
      {
        Datatype datatype = (Datatype)allowedTypes.elementAt(i);
        datatype.validate(value);
        success = true;
        break;
      } catch (DatatypeException e)
      {
        thrownException = e;
        continue;
      }
    }
    if (success == false)
    {
      DatatypeException.throwIt(DatatypeException.ERROR_ILLEGAL_VALUE, "Illegal value");
    }
  }

  public Object parse(String value) throws ParseException
  {
    int i;
    boolean success = false;
    ParseException thrownException = null;
    /* We go through each alllowed types, and if one is ok, then we stop. */
    for (i = 0; i < allowedTypes.size(); i++)
    {
      try 
      {
        Datatype datatype = (Datatype)allowedTypes.elementAt(i);
        return datatype.parse(value);
      } catch (ParseException e)
      {
        thrownException = e;
        continue;
      }
    }
    if (success == false)
    {
      throw thrownException;
    }
    return null;
  }

  public Object accept(TypeVisitor v, Object arg)
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  public void addVariantType(Datatype datatype)
  {
    allowedTypes.add(datatype);
  }
  
  public Datatype getVariantType(int index)
  {
    return (Datatype) allowedTypes.elementAt(index);
  }
  
  public int getVariantCount()
  {
    return allowedTypes.size();
  }

  public String getPattern()
  {
    String pattern = "";
    Datatype type = null;
    for (int i = 0; i < allowedTypes.size(); i++)
    {
      type = (Datatype) allowedTypes.get(i);
      if (type instanceof PatternFacet)
      {
        pattern = pattern + "|" + ((PatternFacet)type).getPattern();  
      }
    }
    if (pattern.length()==0)
    {
      return ".*";
    }
    return pattern;
  }

  public void setPattern(String value)
  {
    throw new IllegalArgumentException("Cannot set pattern value in a union type.");
  }
  
  

}
