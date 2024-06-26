package com.optimasc.datatypes.generated;

import java.text.ParseException;
import java.util.Vector;

import com.optimasc.datatypes.Datatype;
import com.optimasc.datatypes.DatatypeException;
import com.optimasc.datatypes.MemberObject;
import com.optimasc.datatypes.PatternFacet;
import com.optimasc.datatypes.TypeUtilities.TypeCheckResult;
import com.optimasc.datatypes.aggregate.AggregateType;
import com.optimasc.datatypes.visitor.TypeVisitor;

/** Represents a value that can be one or more different
 *  datatypes.
 *  
 *  @author Carl Eric Codere
 *
 */
public class UnionType extends AggregateType
{
  public UnionType()
  {
    super(Datatype.OTHER);
  }
  
  public UnionType(MemberObject[] fields)
  {
    super(Datatype.OTHER,fields);
  }
  

  public Class getClassType()
  {
    return Object.class;
  }



  public Object accept(TypeVisitor v, Object arg)
  {
    return v.visit(this, arg);
  }

  public Object toValue(Object value, TypeCheckResult conversionResult)
  {
    return null;
  }
  


}
