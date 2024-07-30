package com.optimasc.datatypes.primitives;

import com.optimasc.datatypes.DatatypeTest;

import java.math.BigInteger;

import com.optimasc.datatypes.BoundedFacet;

import junit.framework.TestCase;

public abstract class AbstractNumberTest extends DatatypeTest
{
  AbstractNumberType defaultInstance;
  
  protected void setUp() throws Exception
  {
    super.setUp();
  }

  protected void tearDown() throws Exception
  {
    super.tearDown();
  }

  
  /*----------------------- Generic API ------------------------*/
  public abstract void testGetClassType();
  
  
  public void testStringAttributes()
  {
    testBasicDataType(defaultInstance);
  }
  
  public void testUserData()
  {
    testUserData(defaultInstance);
  }

  public void testProperties()
  {
    assertEquals(true,defaultInstance.isOrdered());
    assertEquals(true,defaultInstance.isNumeric());
    if (defaultInstance instanceof BoundedFacet)
    {
      assertEquals(false,((BoundedFacet)defaultInstance).isBounded());
    }
  }
  
}
