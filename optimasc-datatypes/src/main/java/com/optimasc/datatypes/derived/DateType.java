/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.optimasc.datatypes.derived;

import omg.org.astm.type.UnnamedTypeReference;

import com.optimasc.datatypes.Datatype;
import com.optimasc.datatypes.primitives.DateTimeType;
import com.optimasc.datatypes.visitor.TypeVisitor;
import com.optimasc.lang.GregorianDateTime;

/** The date type represents a date in the proleptic Gregorian Calender.
 *  Internally the date type can be represented as the following binary
 *  values:
 *   year: signed 16-bit value
 *   month: signed 8-bit value (valid range 01-12)
 *   day: signed 8-bit value (valid range 01-31)
 *
 *  This is equivalent to the following datatypes:
 *  <ul>
 *   <li>time(day, 10, 0) ISO/IEC 11404 General purpose datatype</li>
 *   <li>date XMLSchema built-in datatype</li>
 *  </ul>
 *  
 * @author Carl Eric Codere
 */
public class DateType extends DateTimeType
{
  public static final UnnamedTypeReference DEFAULT_TYPE_REFERENCE = new UnnamedTypeReference(new DateTimeType());

  
    public static final int MASK_YEAR  =  0xFFFF0000;
    public static final int SHIFT_YEAR =  16;
    public static final int MASK_MONTH = 0x0000FF00;
    public static final int SHIFT_MONTH =  8;
    public static final int MASK_DAY =   0x000000FF;
    public static final int SHIFT_DAY =  0;
    
    /** Creates a new boolean type definition.
     *
     * @param name The datatype name associated with this  type.
     * @param comment The comment or note associated with this datatype.
     */
    public DateType()
    {
        super(Datatype.DATE);
        setResolution(RESOLUTION_DAY);
    }

    /** Converts an internal date representation to a GregorianDateTime instance using
     *  the GMT timezone.
     *
     * @param value
     * @return
     * @throws IllegalArgumentException
     */
    public static GregorianDateTime DateValueToCalendar(int value) throws IllegalArgumentException
    {
        GregorianDateTime  cal = new GregorianDateTime();

        short year = (short)(((value & MASK_YEAR) >> SHIFT_YEAR) & 0xFFFF);
        byte month = (byte)(((value & MASK_MONTH) >> SHIFT_MONTH) & 0x7F);
        byte day = (byte)(((value & MASK_DAY) >> SHIFT_DAY) & 0x7F);

        if ((month < 1) || (month > 12))
            throw new IllegalArgumentException("Illegal month value.");

        if ((day < 1) || (day > 31))
            throw new IllegalArgumentException("Illegal day value.");

        cal.setYear(year);
        cal.setMonth(month);
        cal.setDay(day);
        cal.setTimezone(0);
        return cal;
    }

    public Object accept(TypeVisitor v, Object arg)
    {
        return v.visit(this,arg);
    }

}
