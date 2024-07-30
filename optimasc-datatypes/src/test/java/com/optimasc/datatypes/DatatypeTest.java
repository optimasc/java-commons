package com.optimasc.datatypes;

import com.optimasc.datatypes.Datatype;

import junit.framework.TestCase;

public abstract class DatatypeTest extends TestCase
{
  public static final String SAMPLE_NAME = "DatatypeName";
  public static final String SAMPLE_COMMENT = "This is my comment";
  
  
  /** Tests the base properties (fundamental facets
   *  according to XML Schema) of this datatype.
   *  
   *  Fundamental facets of a datatype are as
   *  follows:
   *  <ul>
   *   <li>ordered</li>
   *   <li>numeric</li>
   *   <li>bounded</li>
   *  <ul>
   *  
   */
  public abstract void testProperties();
  /** Tests the returning class type for
   *  the java representation of this class.
   *  
   */
  public abstract void testGetClassType();
  
  /** Tests the actual comment and name
   *  associated with this datatype.
   */
  public abstract void testStringAttributes();
  
  public abstract void testUserData();
  
  public static void testUserData(Datatype datatype)
  {
    
  }
  

  public static void testBasicDataType(Datatype datatype)
  {
    datatype.setComment(SAMPLE_COMMENT);
    assertEquals(SAMPLE_COMMENT, datatype.getComment());
  }


}
