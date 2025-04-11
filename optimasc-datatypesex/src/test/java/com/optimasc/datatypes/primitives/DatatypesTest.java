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
import com.optimasc.datatypes.aggregate.SequenceType;
import com.optimasc.datatypes.defined.LatinCharType;
import com.optimasc.datatypes.defined.LatinStringType;
import com.optimasc.datatypes.defined.UCS2CharType;

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
  //  datatype.setName(SAMPLE_NAME);
    //assertEquals(SAMPLE_NAME, datatype.getName());
    datatype.setComment(SAMPLE_COMMENT);
    assertEquals(SAMPLE_COMMENT, datatype.getComment());
  }


  }
