package com.optimasc.datatypes.aggregate;

import omg.org.astm.type.NamedTypeReference;
import omg.org.astm.type.TypeReference;
import omg.org.astm.type.UnnamedTypeReference;

import com.optimasc.datatypes.Datatype;
import com.optimasc.datatypes.DefaultMemberObject;
import com.optimasc.datatypes.MemberObject;
import com.optimasc.datatypes.aggregate.ArrayType.Dimension;
import com.optimasc.datatypes.defined.IntType;
import com.optimasc.datatypes.defined.LatinCharType;
import com.optimasc.datatypes.defined.ShortType;
import com.optimasc.datatypes.defined.UCS2StringType;
import com.optimasc.datatypes.defined.UnsignedByteType;
import com.optimasc.datatypes.generated.FormalParameterType;
import com.optimasc.datatypes.generated.ProcedureType;
import com.optimasc.datatypes.generated.FormalParameterType.ParameterType;
import com.optimasc.datatypes.primitives.BooleanType;
import com.optimasc.datatypes.primitives.EnumeratedType;
import com.optimasc.datatypes.primitives.IntegralType;
import com.optimasc.datatypes.primitives.VoidType;
import com.optimasc.datatypes.primitives.EnumeratedType.EnumerationElement;

import junit.framework.TestCase;

public class AggregateTypesTest extends TestCase
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
  
  
  
  public void testArrayType()
  {
    ArrayType datatype = new ArrayType();
    ArrayType otherDatatype = new ArrayType();
    testBasicDataType(datatype);
    assertEquals(false, datatype.isOrdered());
    // The dimensions are unlimited by default.
    assertEquals(null,datatype.getRanks());
    
    // It is required to define the base type of the arrays.
    datatype.setBaseTypeReference(IntType.DEFAULT_TYPE_REFERENCE);
    assertEquals(IntType.DEFAULT_TYPE_REFERENCE,datatype.getBaseTypeReference());
    assertEquals(datatype, datatype);
    assertFalse(otherDatatype.equals(datatype));
    
    otherDatatype.setBaseTypeReference(LatinCharType.DEFAULT_TYPE_REFERENCE);
    assertEquals(LatinCharType.DEFAULT_TYPE_REFERENCE,otherDatatype.getBaseTypeReference());

    assertFalse(otherDatatype.equals(datatype));
    // Now set ranks different for both
    Dimension[] dataTypeRanks = new Dimension[]{new Dimension(0,255)};
    Dimension[] otherDataTypeRanks = new Dimension[]{new Dimension(1,32)};
    datatype.setRanks(dataTypeRanks);
    otherDatatype.setRanks(otherDataTypeRanks);
    
    assertFalse(otherDatatype.equals(datatype));
    
    datatype.setRanks(dataTypeRanks);
    otherDatatype.setRanks(dataTypeRanks);
    assertFalse(otherDatatype.equals(datatype));
    
    otherDatatype.setBaseTypeReference(IntType.DEFAULT_TYPE_REFERENCE);
    assertEquals(otherDatatype,datatype);
    
  }

  /** Common method used for testing data-only related aggregate types. */
  protected void internalTestDataAggregateType(AggregateType datatype, AggregateType otherDatatype)
  {
    testBasicDataType(datatype);
    assertEquals(true, datatype.isOrdered());
    assertEquals(otherDatatype, datatype);
    assertEquals(datatype, datatype);
    assertEquals(0,datatype.getMemberCount());
    assertEquals(true,datatype.isOrdered());
    assertEquals(false,datatype.isPacked());
    datatype.setPacked(true);
    datatype.setPacked(false);
    assertEquals(false,datatype.isPacked());
    
    assertEquals(true,datatype.isOrdered());
    assertEquals(null,datatype.lookupMember("member1",true));
    
    // Now create some fields/columns
    MemberObject[] members = new MemberObject[] 
        {
           new DefaultMemberObject("member0",IntType.DEFAULT_TYPE_REFERENCE),
           new DefaultMemberObject("member1",UCS2StringType.TYPE_REFERENCE),
           new DefaultMemberObject("member2",BooleanType.getInstance())
        };
    for (int i=0; i < members.length; i++)
    {
      datatype.addMember(members[i]);
    }
    assertEquals(3,datatype.getMemberCount());
    for (int i=0; i < members.length; i++)
    {
      assertEquals(members[i],datatype.getMember(i));
    }
    // Search symbols, both in case-sensitive and non-case senstitive
    // mode
    assertEquals(members[1],datatype.lookupMember("member1",false));
    assertEquals(members[1],datatype.lookupMember("member1",true));
    assertEquals(null,datatype.lookupMember("meMber1",false));
    assertEquals(members[1],datatype.lookupMember("meMber1",true));

    assertFalse(otherDatatype.equals(datatype));
    MemberObject[] otherMembers = new MemberObject[] 
        {
           new DefaultMemberObject("member0",IntType.DEFAULT_TYPE_REFERENCE),
           new DefaultMemberObject("member1",UCS2StringType.TYPE_REFERENCE),
           new DefaultMemberObject("member2",ShortType.DEFAULT_TYPE_REFERENCE)
        };
    for (int i=0; i < otherMembers.length; i++)
    {
      otherDatatype.addMember(otherMembers[i]);
    }
    assertFalse(otherDatatype.equals(datatype));
    // Replace all members of other datatype with the one with datatype.
    for (int i=0; i < members.length; i++)
    {
      otherDatatype.setMember(i, members[i]);
    }
    for (int i=0; i < members.length; i++)
    {
      assertEquals(members[i],otherDatatype.getMember(i));
    }
    
    assertEquals(otherDatatype,datatype);
  }

  public void testRecordTypeDefault()
  {
    AggregateType datatype = new RecordType();
    AggregateType otherDatatype = new RecordType();
    
    internalTestDataAggregateType(datatype,otherDatatype);
  }
  
  
  
  
  public void testTableType()
  {
    AggregateType datatype = new TableType();
    AggregateType otherDatatype = new TableType();
    
    internalTestDataAggregateType(datatype,otherDatatype);
  }
  

  /** Common method used for testing data/method  related aggregate types
   *  (Interfaces and classes). */
  protected void internalTestAggregateType(DerivableAggregateType datatype, DerivableAggregateType otherDatatype)
  {
    testBasicDataType(datatype);
    assertEquals(true, datatype.isOrdered());
    assertEquals(otherDatatype, datatype);
    assertEquals(datatype, datatype);
    assertEquals(0,datatype.getMemberCount());
    assertEquals(0, datatype.getDerivesFromCount());
    assertEquals(true,datatype.isOrdered());
    assertEquals(false,datatype.isPacked());
    datatype.setPacked(true);
    datatype.setPacked(false);
    assertEquals(false,datatype.isPacked());
    
    assertEquals(true,datatype.isOrdered());
    assertEquals(null,datatype.lookupMember("member1",true));
    
    TypeReference standardProcedure = ProcedureType.DEFAULT_TYPE_REFERENCE;
    FormalParameterType[] complexParams = 
    new FormalParameterType[]
        {
          new FormalParameterType(ShortType.DEFAULT_TYPE_REFERENCE, ParameterType.ByValue),
          new FormalParameterType(IntType.DEFAULT_TYPE_REFERENCE, ParameterType.ByValue)
        };
   TypeReference complexReturnType = VoidType.getInstance();          
   TypeReference complexProcedure = new UnnamedTypeReference(new ProcedureType(complexReturnType,complexParams));
    
    
    // Now create some fields/columns
    MemberObject[] members = new MemberObject[] 
        {
           new DefaultMemberObject("member0",IntType.DEFAULT_TYPE_REFERENCE),
           new DefaultMemberObject("member1",UCS2StringType.TYPE_REFERENCE),
           new DefaultMemberObject("member2",BooleanType.getInstance()),
           new DefaultMemberObject("member3",complexProcedure)
        };
    // Add some members
    for (int i=0; i < members.length; i++)
    {
      datatype.addMember(members[i]);
    }
    assertEquals(4,datatype.getMemberCount());
    for (int i=0; i < members.length; i++)
    {
      assertEquals(members[i],datatype.getMember(i));
    }
    // Search symbols, both in case-sensitive and non-case senstitive
    // mode
    assertEquals(members[1],datatype.lookupMember("member1",false));
    assertEquals(members[1],datatype.lookupMember("member1",true));
    assertEquals(null,datatype.lookupMember("meMber1",false));
    assertEquals(members[1],datatype.lookupMember("meMber1",true));

    assertFalse(otherDatatype.equals(datatype));
    MemberObject[] otherMembers = new MemberObject[] 
        {
           new DefaultMemberObject("member0",IntType.DEFAULT_TYPE_REFERENCE),
           new DefaultMemberObject("member1",UCS2StringType.TYPE_REFERENCE),
           new DefaultMemberObject("member2",ShortType.DEFAULT_TYPE_REFERENCE),
           new DefaultMemberObject("member3",standardProcedure)
        };
    
    // Add some different members
    for (int i=0; i < otherMembers.length; i++)
    {
      otherDatatype.addMember(otherMembers[i]);
    }
    // Here, type identity is false.
    assertFalse(otherDatatype.equals(datatype));
    // Replace all members of other datatype with the one with datatype.
    // So all members will be equal.
    for (int i=0; i < members.length; i++)
    {
      otherDatatype.setMember(i, members[i]);
    }
    for (int i=0; i < members.length; i++)
    {
      assertEquals(members[i],otherDatatype.getMember(i));
    }
    // Here it is type identity.
    assertEquals(otherDatatype,datatype);
    
    // Change derivedFrom by adding to it an interface
    // derivation 
    InterfaceType intf = new InterfaceType();
    MemberObject[] interfaceMembers = new MemberObject[] 
        {
           new DefaultMemberObject("member3",complexProcedure)
        };
    for (int i=0; i < interfaceMembers.length; i++)
    {
      intf.addMember(interfaceMembers[i]);
    }
    datatype.addDerivesFrom(new NamedTypeReference("myintf",intf));
    assertFalse(otherDatatype.equals(datatype));
  }
  
  public void testClassType()
  {
    ClassType datatype = new ClassType();
    ClassType otherDatatype = new ClassType();
    
    internalTestAggregateType(datatype,otherDatatype);
  }
  
  
  public void testInterfaceType()
  {
    InterfaceType datatype = new InterfaceType();
    InterfaceType otherDatatype = new InterfaceType();
    
    internalTestAggregateType(datatype,otherDatatype);
  }
  
  // Test sets that are within range of a byte
  public void testSetTypeValidRange()
  {
    SetType datatype = new SetType();
    SetType otherDatatype = new SetType();
    testBasicDataType(datatype);
    assertEquals(false, datatype.isOrdered());
    assertEquals(UnsignedByteType.getInstance(),datatype.getBaseTypeReference());
    assertEquals(UnsignedByteType.getInstance(),otherDatatype.getBaseTypeReference());
    // Both based on unsignedByte reference
    assertEquals(otherDatatype, datatype);
    assertEquals(datatype, datatype);
    
    EnumeratedType enumType = new EnumeratedType();
    // More complex choice options
    Object[] dataTypeEnum = new Object[]{
        new EnumerationElement("Choice1",1),
        new EnumerationElement("Choice2",2),
        new EnumerationElement("Choice3",3),
        new EnumerationElement("Choice4",4)};
    enumType.setChoices(dataTypeEnum);
    NamedTypeReference namedType = new NamedTypeReference("MyEnum",enumType);
    
    datatype.setBaseTypeReference(namedType);
    // datatype is set of enum that fits within a byte.
    // otherDatatype is default set of byte.
    // No Type identity
    assertFalse(datatype.equals(otherDatatype));
    
    // Make them identical again
    otherDatatype.setBaseTypeReference(namedType);
    assertEquals(otherDatatype, datatype);
    
    // Make one of them equal to
    TypeReference unnamedType = UnsignedByteType.getInstance();

    // Make them difference, one on Enum and other on valid range
    otherDatatype.setBaseTypeReference(unnamedType);
    // No Type identity
    assertFalse(datatype.equals(otherDatatype));
    
    // Make both base types the same
    datatype.setBaseTypeReference(unnamedType);
    assertEquals(otherDatatype, datatype);
  }
  
  // Test sets that are NOT within range of a byte, hence not allowed
  public void testSetTypeInvalidRange()
  {
    boolean success;
    SetType datatype = new SetType();
    testBasicDataType(datatype);
    assertEquals(false, datatype.isOrdered());
    assertEquals(UnsignedByteType.getInstance(),datatype.getBaseTypeReference());
    assertEquals(datatype, datatype);
    
    EnumeratedType enumType = new EnumeratedType();
    // More complex choice options
    Object[] dataTypeEnum = new Object[]{
        new EnumerationElement("Choice1",1),
        new EnumerationElement("Choice2",2),
        new EnumerationElement("Choice3",3),
        // This is out of bounds
        new EnumerationElement("Choice4",1024)};
    enumType.setChoices(dataTypeEnum);
    NamedTypeReference namedType = new NamedTypeReference("MyEnum",enumType);

  
    success = false;
    try { 
    
      datatype.setBaseTypeReference(namedType);
    } catch (IllegalArgumentException e)
    {
      success = true;
    }
    assertTrue(success);
    
    // Make one of them equal to
    IntegralType rangeType = new IntegralType(0,Short.MAX_VALUE);
    TypeReference unnamedType = new UnnamedTypeReference(rangeType);

    success = false;
    try { 
    
      datatype.setBaseTypeReference(unnamedType);
    } catch (IllegalArgumentException e)
    {
      success = true;
    }
    assertTrue(success);
    
    success = false;
    try { 
    
      datatype.setBaseTypeReference(IntType.DEFAULT_TYPE_REFERENCE);
    } catch (IllegalArgumentException e)
    {
      success = true;
    }
    assertTrue(success);
    
    
  }
  

}
