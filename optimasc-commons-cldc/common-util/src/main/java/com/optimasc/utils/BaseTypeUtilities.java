package com.optimasc.utils;

import com.optimasc.date.JulianDateTime;
import com.optimasc.text.StringUtilities;

import java.util.Calendar;
import java.util.Date;

/*
 * 
 * See License.txt for more information on the licensing terms
 * for this source code.
 * 
 * THIS SOFTWARE IS PROVIDED "AS IS" AND ANY EXPRESSED OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
 * PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL
 * OPTIMA SC INC. OR ITS CONTRIBUTORS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
 * OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

/**
 * Contains classes for converting a type to another type with error checking.
 * The module is completely compatible with CLDC 1.1
 *
 * Missing: UUID Identifier Base64 hexBinary
 *
 * @author Carl Eric Codere
 */
public class BaseTypeUtilities
{


  /**
   * Converts an object to a Boolean value, returning null if the value is
   * invalid. The following input object types are currently recognized: String,
   * Double, Float, Long, Integer, Short, Byte and Boolean.
   *
   * @param obj
   * @return
   */
  public static Boolean ObjectToBoolean(Object obj)
  {
    if (obj == null)
      return null;
    if (obj instanceof Boolean)
      return (Boolean) obj;
    if (obj instanceof Integer)
    {
      Integer value = (Integer) obj;
      if (value.intValue() == 0)
      {
        return Boolean.FALSE;
      }
      else if (value.intValue() == 1)
      {
        return Boolean.TRUE;
      }
      // Cannot be converted to a Boolean
      return null;
    }
    if (obj instanceof String)
    {
      String value = (String) obj;
      value = value.toUpperCase();
      if (value.equals("0") || value.equals("FALSE"))
      {
        return Boolean.FALSE;
      }
      else if (value.equals("1") || value.equals("TRUE"))
      {
        return Boolean.TRUE;
      }
      return null;
    }
    if (obj instanceof Long)
    {
      Long value = (Long) obj;
      if (value.longValue() == 0)
      {
        return Boolean.FALSE;
      }
      else if (value.longValue() == 1)
      {
        return Boolean.TRUE;
      }
      // Cannot be converted to a Boolean
      return null;
    }
    if (obj instanceof Short)
    {
      Short value = (Short) obj;
      if (value.shortValue() == 0)
      {
        return Boolean.FALSE;
      }
      else if (value.shortValue() == 1)
      {
        return Boolean.TRUE;
      }
      // Cannot be converted to a Boolean
      return null;
    }
    if (obj instanceof Byte)
    {
      Byte value = (Byte) obj;
      if (value.byteValue() == 0)
      {
        return Boolean.FALSE;
      }
      else if (value.byteValue() == 1)
      {
        return Boolean.TRUE;
      }
      // Cannot be converted to a Boolean
      return null;
    }
    if (obj instanceof Float)
    {
      Float value = (Float) obj;
      if (value.intValue() == 0)
      {
        return Boolean.FALSE;
      }
      else if (value.intValue() == 1)
      {
        return Boolean.TRUE;
      }
      // Cannot be converted to a Boolean
      return null;
    }
    if (obj instanceof Double)
    {
      Double value = (Double) obj;
      if (value.intValue() == 0)
      {
        return Boolean.FALSE;
      }
      else if (value.intValue() == 1)
      {
        return Boolean.TRUE;
      }
      // Cannot be converted to a Boolean
      return null;
    }

    return null;
  }

  /**
   * Converts an object to a Long value, returning null if the value is invalid.
   * The following input object types are currently recognized: String, Double,
   * Float, Long, Integer, Short, Byte and Boolean, Date, Calendar (converted to
   * Date epoch format)
   *
   * @param obj
   * @return
   */
  public static Long ObjectToLong(Object obj)
  {
    if (obj == null)
      return null;
    if (obj instanceof Boolean)
    {
      Boolean value = (Boolean) obj;
      if (value.booleanValue() == true)
        return new Long(1);
      if (value.booleanValue() == false)
        return new Long(0);
    }
    if (obj instanceof Integer)
    {
      Integer value = (Integer) obj;
      return new Long(value.longValue());
    }
    if (obj instanceof String)
    {
      String value = (String) obj;
      // Remove the leading + character if present so
      // it conforms to XML Schema
      if (value.startsWith("+") == true)
        value = value.substring(1);
      try
      {
        return new Long(Long.parseLong(value));
      } catch (NumberFormatException e)
      {
        return null;
      }
    }
    if (obj instanceof Long)
    {
      return (Long) obj;
    }
    if (obj instanceof Date)
    {
      Date value = (Date) obj;
      return new Long(value.getTime());
    }
    if (obj instanceof Calendar)
    {
      Calendar value = (Calendar) obj;
      return new Long(value.getTime().getTime());
    }
    if (obj instanceof Short)
    {
      Short value = (Short) obj;
      return new Long(value.shortValue());
    }
    if (obj instanceof Byte)
    {
      Byte value = (Byte) obj;
      return new Long(value.byteValue());
    }
    if (obj instanceof Float)
    {
      Float value = (Float) obj;
      return new Long(value.longValue());
    }
    if (obj instanceof Double)
    {
      Double value = (Double) obj;
      return new Long(value.longValue());
    }
    return null;
  }

  /**
   * Converts an object to a String value, returning null if the value is
   * invalid. It calls the toString() method, making it callable from anywhere.
   *
   * @param obj
   * @return
   */
  public static String ObjectToString(Object obj)
  {
    if (obj == null)
      return null;
    return obj.toString();
  }

  /**
   * Converts an object to a byte array value, returning null if the value is
   * invalid. Numeric values are stored in big endian format.
   *
   * Currently supports: - String - byte[]
   *
   * @param obj
   * @return
   */
  public static byte[] ObjectToBytes(Object obj)
  {
    if (obj == null)
      return null;
    if (obj instanceof byte[])
    {
      return (byte[]) obj;
    }
    if (obj instanceof String)
    {
      String value = (String) obj;
      return value.getBytes();
    }
    return null;
  }

  /**
   * Converts an object to a Calendar value, returning null if the value is
   * invalid. The following inputs are supported: - Calendar - Long (assumed to
   * be in Date epoch format) - String (assumed to be in ISO 8601 format) -
   * Float/Double (assumed to be a Julian Day)
   *
   *
   * @param obj
   * @return
   */
  public static Calendar ObjectToCalendar(Object obj)
  {
    if (obj == null)
      return null;
    if (obj instanceof Calendar)
    {
      return (Calendar) obj;
    }
    if (obj instanceof Long)
    {
      Long value = (Long) obj;
      Calendar cal = Calendar.getInstance();
      cal.setTime(new Date(value.longValue()));
      return cal;
    }
    if (obj instanceof String)
    {
      String value = (String) obj;
      try
      {
        //            return ISO8601Date.parseDate(value);
      } catch (IllegalArgumentException e)
      {
        return null;
      }
      return null;
    }
    if (obj instanceof Double)
    {
      Double value = (Double) obj;
      return JulianDateTime.decode(value.doubleValue());
    }
    if (obj instanceof Float)
    {
      Float value = (Float) obj;
      return JulianDateTime.decode(value.floatValue());
    }
    return null;
  }

  /**
   * Converts an object to a Double value, returning null if the value is
   * invalid. The following inputs are supported: Calendar (converted to a
   * Double julian day), Float, Double, Long, Short, Integer, Byte and String.
   *
   *
   * @param obj
   * @return
   */
  public static Double ObjectToDouble(Object obj)
  {
    if (obj == null)
      return null;
    if (obj instanceof Double)
    {
      return (Double) obj;
    }
    if (obj instanceof Integer)
    {
      Integer value = (Integer) obj;
      return new Double(value.intValue());
    }
    if (obj instanceof String)
    {
      String value = (String) obj;
      try
      {
        return Double.valueOf(value);
      } catch (NumberFormatException e)
      {
        return null;
      }
    }
    if (obj instanceof Long)
    {
      Long value = (Long) obj;
      return new Double(value.longValue());
    }
    if (obj instanceof Short)
    {
      Short value = (Short) obj;
      return new Double(value.shortValue());
    }
    if (obj instanceof Byte)
    {
      Byte value = (Byte) obj;
      return new Double(value.byteValue());
    }
    if (obj instanceof Float)
    {
      Float value = (Float) obj;
      return new Double(value.floatValue());
    }
    if (obj instanceof Calendar)
    {
      Calendar value = (Calendar) obj;
      return new Double(JulianDateTime.encode(value));
    }
    return null;
  }

}
