package com.optimasc.datatypes.generated;

import omg.org.astm.type.TypeReference;

import com.optimasc.datatypes.Datatype;
import com.optimasc.datatypes.defined.IntType;
import com.optimasc.datatypes.defined.LatinStringType;
import com.optimasc.datatypes.defined.ShortType;
import com.optimasc.datatypes.generated.FormalParameterType.ParameterType;
import com.optimasc.datatypes.primitives.BooleanType;
import com.optimasc.datatypes.primitives.VoidType;

import junit.framework.TestCase;

public class GeneratedTypesTests extends TestCase
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
    datatype.setComment(SAMPLE_COMMENT);
    assertEquals(SAMPLE_COMMENT, datatype.getComment());
  }
  
  public void testReferenceType()
  {
    ReferenceType datatype = new ReferenceType();
    ReferenceType otherDatatype = new ReferenceType();
    testBasicDataType(datatype);
    assertEquals(false, datatype.isOrdered());
    // Default is a generic pointer
    assertEquals(VoidType.getInstance().getType(),datatype.getBaseTypeReference().getType());
    assertEquals(datatype, datatype);
    assertEquals(otherDatatype, datatype);
    
    datatype.setBaseTypeReference(LatinStringType.TYPE_REFERENCE);
    assertEquals(LatinStringType.TYPE_REFERENCE,datatype.getBaseTypeReference());
    
    assertFalse(datatype.equals(otherDatatype));
    
    // Set same pointer type for equality check
    otherDatatype.setBaseTypeReference(LatinStringType.TYPE_REFERENCE);
    
    assertTrue(datatype.equals(otherDatatype));
  }
  
  
  
  public void testProcedureType()
  {
    boolean success = false;
    ProcedureType datatype = new ProcedureType();
    ProcedureType otherDatatype = new ProcedureType();
    assertEquals(false, datatype.isOrdered());
    assertEquals(0,datatype.getParameterCount());
    assertEquals(VoidType.getInstance().getType(),datatype.getReturnType().getType());
    assertEquals(null,datatype.getParent());
    
    
    FormalParameterType[] leftParams = 
        new FormalParameterType[]
            {
              new FormalParameterType(ShortType.DEFAULT_TYPE_REFERENCE, ParameterType.ByValue),
              new FormalParameterType(IntType.DEFAULT_TYPE_REFERENCE, ParameterType.ByValue)
            };
     TypeReference leftReturnType = VoidType.getInstance();          
     FormalParameterType[] rightParams =
      new FormalParameterType[]
        {
          new FormalParameterType(ShortType.DEFAULT_TYPE_REFERENCE, ParameterType.ByValue),
          new FormalParameterType(IntType.DEFAULT_TYPE_REFERENCE, ParameterType.ByValue)
        };
     TypeReference rightReturnType = VoidType.getInstance();
     
     datatype.setReturnType(leftReturnType);
     assertEquals(leftReturnType,datatype.getReturnType());
     for (int i=0; i < leftParams.length; i++)
     {
       datatype.addParameter(leftParams[i]);
       assertEquals(leftParams[i],datatype.getParameter(i));
     }
     assertEquals(2,datatype.getParameterCount());
     
     otherDatatype.setReturnType(rightReturnType);
     assertEquals(rightReturnType,otherDatatype.getReturnType());
     for (int i=0; i < rightParams.length; i++)
     {
       otherDatatype.addParameter(rightParams[i]);
       assertEquals(rightParams[i],otherDatatype.getParameter(i));
     }
     assertEquals(2,otherDatatype.getParameterCount());
     
     // Type identity equal parameters, return type and type of parameters.
     assertTrue(datatype.equals(otherDatatype));
     
     //------------ Create different parameter types - one by value and one other by reference
     leftParams = 
         new FormalParameterType[]
             {
               new FormalParameterType(ShortType.DEFAULT_TYPE_REFERENCE, ParameterType.ByValue),
               new FormalParameterType(IntType.DEFAULT_TYPE_REFERENCE, ParameterType.ByReference)
             };
      leftReturnType = VoidType.getInstance();          
      rightParams =
       new FormalParameterType[]
         {
           new FormalParameterType(ShortType.DEFAULT_TYPE_REFERENCE, ParameterType.ByValue),
           new FormalParameterType(IntType.DEFAULT_TYPE_REFERENCE, ParameterType.ByValue)
         };
      rightReturnType = VoidType.getInstance();
      
      datatype.setReturnType(leftReturnType);
      assertEquals(leftReturnType,datatype.getReturnType());
      for (int i=0; i < leftParams.length; i++)
      {
        datatype.setParameter(i,leftParams[i]);
        assertEquals(leftParams[i],datatype.getParameter(i));
      }
      assertEquals(2,datatype.getParameterCount());
      
      otherDatatype.setReturnType(rightReturnType);
      assertEquals(rightReturnType,otherDatatype.getReturnType());
      for (int i=0; i < rightParams.length; i++)
      {
        otherDatatype.setParameter(i,rightParams[i]);
        assertEquals(rightParams[i],otherDatatype.getParameter(i));
      }
      assertEquals(2,otherDatatype.getParameterCount());
     
      assertFalse(datatype.equals(otherDatatype));
      
      //------------ Create different return type
      leftParams = 
          new FormalParameterType[]
              {
                new FormalParameterType(ShortType.DEFAULT_TYPE_REFERENCE, ParameterType.ByValue),
                new FormalParameterType(IntType.DEFAULT_TYPE_REFERENCE, ParameterType.ByValue)
              };
       leftReturnType = VoidType.getInstance();          
       rightParams =
        new FormalParameterType[]
          {
            new FormalParameterType(ShortType.DEFAULT_TYPE_REFERENCE, ParameterType.ByValue),
            new FormalParameterType(IntType.DEFAULT_TYPE_REFERENCE, ParameterType.ByValue)
          };
       rightReturnType = BooleanType.getInstance();
       
       datatype.setReturnType(leftReturnType);
       assertEquals(leftReturnType,datatype.getReturnType());
       for (int i=0; i < leftParams.length; i++)
       {
         datatype.setParameter(i,leftParams[i]);
         assertEquals(leftParams[i],datatype.getParameter(i));
       }
       assertEquals(2,datatype.getParameterCount());
       
       otherDatatype.setReturnType(rightReturnType);
       assertEquals(rightReturnType,otherDatatype.getReturnType());
       for (int i=0; i < rightParams.length; i++)
       {
         otherDatatype.setParameter(i,rightParams[i]);
         assertEquals(rightParams[i],otherDatatype.getParameter(i));
       }
       assertEquals(2,otherDatatype.getParameterCount());
      
       assertFalse(datatype.equals(otherDatatype));
  }
  
  public void testPointerType()
  {
    PointerType datatype = new PointerType();
    PointerType otherDatatype = new PointerType();
    testBasicDataType(datatype);
    assertEquals(true, datatype.isOrdered());
    // Default is a generic pointer
    assertEquals(VoidType.getInstance().getType(),datatype.getBaseTypeReference().getType());
    assertEquals(datatype, datatype);
    assertEquals(otherDatatype, datatype);
    
    datatype.setBaseTypeReference(LatinStringType.TYPE_REFERENCE);
    assertEquals(LatinStringType.TYPE_REFERENCE,datatype.getBaseTypeReference());
    
    assertFalse(datatype.equals(otherDatatype));
    
    // Set same pointer type for equality check
    otherDatatype.setBaseTypeReference(LatinStringType.TYPE_REFERENCE);
    
    assertTrue(datatype.equals(otherDatatype));
  }
  
  

}
