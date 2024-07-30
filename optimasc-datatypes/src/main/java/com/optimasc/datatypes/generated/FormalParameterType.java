package com.optimasc.datatypes.generated;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.optimasc.datatypes.Datatype;
import com.optimasc.datatypes.DatatypeException;
import com.optimasc.datatypes.TypeUtilities.TypeCheckResult;
import com.optimasc.datatypes.visitor.TypeVisitor;
import com.optimasc.utils.UserConfiguration;

import omg.org.astm.type.TypeReference;

/** Represents the parameter data type of a method/procedure. */
public class FormalParameterType extends Datatype implements UserConfiguration
{

  /**
   * Represents how parameters are passed to the routine.
   * 
   * Standard pre-Java 1.5 Typesafe enumeration
   */
  public static final class ParameterType implements Comparable
  {
    /**
     * A copy of the value is created, for reference types, a copy of the
     * reference is created pointing to the original data of the original
     * reference.
     */
    public static final ParameterType ByValue = new ParameterType("ByValue");
    /**
     * Values are passed by value or reference, but the value cannot be assigned
     * to.
     */
    public static final ParameterType ByConst = new ParameterType("ByConst");
    /**
     * Values are passed by reference (pointer to the original data), and any
     * changes to the parameter affects the original data
     */
    public static final ParameterType ByReference = new ParameterType("ByReference");
    /**
     * Values are passed by reference (pointer to the original data), and any
     * changes to the parameter affects the original data. It is required for
     * the parameter to be assigned to in the routine.
     */
    public static final ParameterType ByOutReference = new ParameterType("ByOutReference");

    private final String name;
    private static int nextOrdinal = 0;
    private final int ordinal = nextOrdinal++;

    private ParameterType(String enumSymName)
    {
      this.name = enumSymName;
    }

    /**
     * Parse text into an element of this enumeration.
     *
     * @param aText
     *          takes one of the values allowed in the enumeration symbols.
     */
    public static ParameterType valueOf(String aText)
    {
      Iterator iter = VALUES.iterator();
      while (iter.hasNext())
      {
        ParameterType p = (ParameterType) iter.next();
        if (aText.equals(p.toString()))
        {
          return p;
        }
      }
      //this method is unusual in that IllegalArgumentException is
      //possibly thrown not at its beginning, but at its end.
      throw new IllegalArgumentException("Cannot parse into an element of Suit : '"
          + aText + "'");
    }

    public int compareTo(Object that)
    {
      return ordinal - ((ParameterType) that).ordinal;
    }

    public String toString()
    {
      return name;
    }
    
    /**
     * These two lines are all that's necessary to export a List of VALUES.
     */
     private static final ParameterType[] values = {ByValue, ByConst, ByReference, ByOutReference};
     //VALUES needs to be located here, otherwise illegal forward reference
     public static final List VALUES = Collections.unmodifiableList(Arrays.asList(values));

  }

  /** Actual data type of this parameter */
  protected TypeReference type;
  /** User data associated with this parameter */
  protected Map userData;
  /** Parameter type information */
  protected ParameterType parameterType;

  public FormalParameterType(TypeReference typ, ParameterType paramType)
  {
    super(false);
    this.type = typ;
    this.parameterType = paramType;
    userData = new HashMap();
  }

  /** Returns the datatype associated with this parameter */
  public TypeReference getTypeReference()
  {
    return type;
  }
  
  public void setTypeReference(TypeReference typ)
  {
    this.type = typ;
  }

  public boolean equals(Object obj)
  {
    if (obj == null)
      return false;
    if (obj == this)
      return true;
    if ((obj instanceof FormalParameterType) == false)
    {
      return false;
    }
    FormalParameterType otherObj = (FormalParameterType) obj;
    if (otherObj.getTypeReference().equals(getTypeReference()))
    {
      if (otherObj.parameterType == parameterType)
      {
        return true;
      }
    }
    return false;
  }

  public Object getUserData(String key)
  {
    return userData.get(key);
  }

  public Object setUserData(String key, Object data)
  {
    return userData.put(key, data);
  }

  public ParameterType getParameterType()
  {
    return parameterType;
  }

  public void setParameterType(ParameterType parameterType)
  {
    this.parameterType = parameterType;
  }

  public Class getClassType()
  {
    // TODO Auto-generated method stub
    return null;
  }

  public Object accept(TypeVisitor v, Object arg)
  {
    return v.visit(this,arg);
  }

  public Object toValue(Object value, TypeCheckResult conversionResult)
  {
    return null;
  }

}
