/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.optimasc.datatypes.defined;

import omg.org.astm.type.UnnamedTypeReference;

import com.optimasc.datatypes.Datatype;
import com.optimasc.datatypes.primitives.DateTimeType;
import com.optimasc.datatypes.visitor.TypeVisitor;
import com.optimasc.date.DateTime;
import com.optimasc.lang.GregorianDateTime;

/** The date type represents a date in the proleptic Gregorian Calender.
 * 
 *  This is equivalent to the following datatypes:
 *  <ul>
 *   <li><code>time(day, 10, 0)</codE> ISO/IEC 11404 General purpose datatype</li>
 *   <li><code>date</code> XMLSchema built-in datatype</li>
 *  </ul>
 *  
 * @author Carl Eric Codere
 */
public class DateType extends DateTimeType
{
  public static final DateType DEFAULT_INSTANCE = new DateType();
  public static final UnnamedTypeReference DEFAULT_TYPE_REFERENCE = new UnnamedTypeReference(DEFAULT_INSTANCE);

    public DateType()
    {
        super(DateTime.TimeAccuracy.DAY,false);
    }

    public Object accept(TypeVisitor v, Object arg)
    {
        return v.visit(this,arg);
    }

}
