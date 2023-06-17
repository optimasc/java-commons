package com.optimasc.datatypes.aggregate;

import java.text.ParseException;
import java.util.Vector;

import com.optimasc.datatypes.Datatype;
import com.optimasc.datatypes.DatatypeException;
import com.optimasc.datatypes.visitor.TypeVisitor;

/** This datatype represents a table datatype that consists of fields
 *  representing columns of data.
 *  
 *  This is equivalent to the following datatypes:
 *  <ul>
 *   <li>table ISO/IEC 11404 General purpose datatype</li>
 *  </ul>
 * 
 * @author Carl Eric Codere
 */

public class TableType extends AggregateType
{

  public TableType()
  {
    super(Datatype.OTHER);
  }

  public Object accept(TypeVisitor v, Object arg)
  {
    return v.visit(this,arg);
  }
}
