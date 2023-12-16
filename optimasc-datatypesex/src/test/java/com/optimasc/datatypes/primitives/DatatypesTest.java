package com.optimasc.datatypes.primitives;

import java.text.ParseException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.XMLGregorianCalendar;

import omg.org.astm.type.UnnamedTypeReference;

import com.optimasc.datatypes.Datatype;
import com.optimasc.datatypes.EnumerationFacet;
import com.optimasc.datatypes.LengthFacet;
import com.optimasc.datatypes.PatternFacet;
import com.optimasc.datatypes.aggregate.ListType;
import com.optimasc.datatypes.aggregate.SequenceListType;
import com.optimasc.datatypes.aggregate.SequenceType;
import com.optimasc.datatypes.derived.LatinCharType;
import com.optimasc.datatypes.derived.LatinStringType;
import com.optimasc.datatypes.derived.UCS2CharType;

import junit.framework.TestCase;

public class DatatypesTest extends TestCase
{

  public static final int SAMPLE_MIN_LENGTH = 0;
  public static final int SAMPLE_MAX_LENGTH = 1024;

  public static final String SAMPLE_NAME = "DatatypeName";
  public static final String SAMPLE_COMMENT = "This is my comment";

  protected void setUp() throws Exception
  {
    super.setUp();
  }

  protected void tearDown() throws Exception
  {
    super.tearDown();
  }

  public void testBasicDataType(Datatype datatype)
  {
    datatype.setName(SAMPLE_NAME);
    assertEquals(SAMPLE_NAME, datatype.getName());
    datatype.setComment(SAMPLE_COMMENT);
    assertEquals(SAMPLE_COMMENT, datatype.getComment());
  }


  public void testSequenceType()
  {
    boolean success = false;
    Vector v;
    ListType datatype = new SequenceListType();
    /* List of string types. */
    datatype.setBaseTypeReference(new UnnamedTypeReference(new LatinStringType()));
    ListType otherDatatype = new SequenceListType();
    otherDatatype.setBaseTypeReference(new UnnamedTypeReference(new LatinStringType()));
    testBasicDataType(datatype);
    assertEquals(Vector.class, datatype.getClassType());
    assertTrue(Vector.class.isInstance(datatype.getObjectType()));
    assertEquals(datatype, datatype);
    assertEquals(otherDatatype, datatype);

    datatype.setDelimiter(";");
    try
    {
      v = (Vector) datatype.parse("one;two;three");
      assertEquals(3,v.size());
      assertEquals("one",v.get(0));
      assertEquals("two",v.get(1));
      assertEquals("three",v.get(2));
    } catch (ParseException e)
    {
      fail();
    }

    /* Check the length values. valid use case */
    datatype.setMinLength(0);
    datatype.setMaxLength(1);
    try
    {
      v = (Vector) datatype.parse("one");
      assertEquals(1,v.size());
      assertEquals("one",v.get(0));
    } catch (ParseException e)
    {
      fail();
    }

    /* Invalid use case. */
    success = false;
    try
    {
      v = (Vector) datatype.parse("one;two;three");
    } catch (ParseException e)
    {
      success = true;
    }
    assertEquals(true,success);



  }

}
