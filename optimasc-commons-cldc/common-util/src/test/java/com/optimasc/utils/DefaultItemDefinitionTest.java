package com.optimasc.utils;

import junit.framework.TestCase;

public class DefaultItemDefinitionTest extends TestCase
{

  protected void setUp() throws Exception
  {
    super.setUp();
  }

  protected void tearDown() throws Exception
  {
    super.tearDown();
  }

  public void testValidateObjectIdentifier()
  {
    /* Valid test cases */
    assertEquals(-1,DefaultItemDefinition.validateObjectIdentifier("1.2.4", false));
    assertEquals(-1,DefaultItemDefinition.validateObjectIdentifier("13.253.4", false));
    assertEquals(-1,DefaultItemDefinition.validateObjectIdentifier("13.253.4", true));
    assertEquals(255,DefaultItemDefinition.validateObjectIdentifier("13.253.4{255}", true));
    assertEquals(1,DefaultItemDefinition.validateObjectIdentifier("13.253.4645{1}", true));
    
    boolean fail = true;

    fail = true;
    try 
    {
     assertEquals(-1,DefaultItemDefinition.validateObjectIdentifier("A", false));
    } catch (IllegalArgumentException e)
    {
      fail = false;
    }
    assertFalse(fail);
    
    fail = true;
    try 
    {
     assertEquals(-1,DefaultItemDefinition.validateObjectIdentifier(".", false));
    } catch (IllegalArgumentException e)
    {
      fail = false;
    }
    assertFalse(fail);

    fail = true;
    try 
    {
     assertEquals(-1,DefaultItemDefinition.validateObjectIdentifier("1.", false));
    } catch (IllegalArgumentException e)
    {
      fail = false;
    }
    assertFalse(fail);

    fail = true;
    try 
    {
     assertEquals(-1,DefaultItemDefinition.validateObjectIdentifier("1D", false));
    } catch (IllegalArgumentException e)
    {
      fail = false;
    }
    assertFalse(fail);
    
    fail = true;
    try 
    {
     assertEquals(-1,DefaultItemDefinition.validateObjectIdentifier("1{", false));
    } catch (IllegalArgumentException e)
    {
      fail = false;
    }
    assertFalse(fail);
    
    fail = true;
    try 
    {
     assertEquals(-1,DefaultItemDefinition.validateObjectIdentifier("1.{", true));
    } catch (IllegalArgumentException e)
    {
      fail = false;
    }
    assertFalse(fail);
    
    fail = true;
    try 
    {
     assertEquals(-1,DefaultItemDefinition.validateObjectIdentifier("1.23{A}", true));
    } catch (IllegalArgumentException e)
    {
      fail = false;
    }
    assertFalse(fail);
    
  }
}
