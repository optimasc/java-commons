/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.optimasc.datatypes.visitor;

import omg.org.astm.type.NamedTypeReference;
import omg.org.astm.type.TypeReference;
import omg.org.astm.type.UnnamedTypeReference;

import com.optimasc.datatypes.MemberObject;
import com.optimasc.datatypes.Type;
import com.optimasc.datatypes.UnknownType;
import com.optimasc.datatypes.aggregate.ArrayType;
import com.optimasc.datatypes.aggregate.ClassType;
import com.optimasc.datatypes.aggregate.InterfaceType;
import com.optimasc.datatypes.aggregate.RecordType;
import com.optimasc.datatypes.aggregate.SequenceType;
import com.optimasc.datatypes.aggregate.SetType;
import com.optimasc.datatypes.aggregate.TableType;
import com.optimasc.datatypes.defined.ASCIICharType;
import com.optimasc.datatypes.defined.BinaryType;
import com.optimasc.datatypes.defined.ByteType;
import com.optimasc.datatypes.defined.CurrencyType;
import com.optimasc.datatypes.defined.DateType;
import com.optimasc.datatypes.defined.DoubleType;
import com.optimasc.datatypes.defined.IntType;
import com.optimasc.datatypes.defined.LatinCharType;
import com.optimasc.datatypes.defined.LatinStringType;
import com.optimasc.datatypes.defined.LongType;
import com.optimasc.datatypes.defined.ObjectIdentifierType;
import com.optimasc.datatypes.defined.ShortType;
import com.optimasc.datatypes.defined.SingleType;
import com.optimasc.datatypes.defined.StringType;
import com.optimasc.datatypes.defined.TimestampType;
import com.optimasc.datatypes.defined.UCS2CharType;
import com.optimasc.datatypes.defined.UCS2StringType;
import com.optimasc.datatypes.defined.UnsignedByteType;
import com.optimasc.datatypes.defined.UnsignedIntType;
import com.optimasc.datatypes.defined.UnsignedShortType;
import com.optimasc.datatypes.defined.VisibleCharType;
import com.optimasc.datatypes.generated.ExceptionType;
import com.optimasc.datatypes.generated.FormalParameterType;
import com.optimasc.datatypes.generated.NameSpaceType;
import com.optimasc.datatypes.generated.PointerType;
import com.optimasc.datatypes.generated.ProcedureType;
import com.optimasc.datatypes.generated.ReferenceType;
import com.optimasc.datatypes.generated.UnionType;
import com.optimasc.datatypes.primitives.BooleanType;
import com.optimasc.datatypes.primitives.CharacterType;
import com.optimasc.datatypes.primitives.DateTimeType;
import com.optimasc.datatypes.primitives.DecimalType;
import com.optimasc.datatypes.primitives.DurationType;
import com.optimasc.datatypes.primitives.EnumeratedType;
import com.optimasc.datatypes.primitives.IntegralType;
import com.optimasc.datatypes.primitives.PrimitiveType;
import com.optimasc.datatypes.primitives.RealType;
import com.optimasc.datatypes.primitives.TimeType;
import com.optimasc.datatypes.primitives.URIType;
import com.optimasc.datatypes.primitives.VoidType;

/** Default type visitor that goes through all type elements
 *  and subelements of each type.
 *
 * @author Carl Eric Codere
 */
public class DefaultTypeVisitor implements TypeVisitor {

    public Object visit(ArrayType n, Object arg)
    {
      n.setBaseTypeReference((TypeReference) n.getBaseTypeReference().accept(this, arg));
      return n;
    }

    public Object visit(BinaryType n, Object arg)
    {
      return n;
    }

    public Object visit(BooleanType n, Object arg)
    {
      return n;
    }

    public Object visit(ClassType n, Object arg)
    {
      int derivesCount = n.getDerivesFromCount();
      for (int i=0; i < derivesCount; i++)
      {
        n.setDerivesFrom(i,(NamedTypeReference) n.getDerivesFrom(i).accept(this, arg));
      }
      int count = n.getMemberCount();
      for (int i=0; i < count; i++)
      {
        MemberObject member = (MemberObject) n.getMember(i);
        member.setDefinitionType((TypeReference)member.getDefinitionType().accept(this, arg));
      }
      return n;
    }
    
    public Object visit(InterfaceType n, Object arg)
    {
      int derivesCount = n.getDerivesFromCount();
      for (int i=0; i < derivesCount; i++)
      {
        n.setDerivesFrom(i,(NamedTypeReference) n.getDerivesFrom(i).accept(this, arg));
      }
      int count = n.getMemberCount();
      for (int i=0; i < count; i++)
      {
        MemberObject member = (MemberObject) n.getMember(i);
        member.setDefinitionType((TypeReference)member.getDefinitionType().accept(this, arg));
      }
      return n;
    }
    

    public Object visit(DateType n, Object arg)
    {
        return n;
    }
    
    public Object visit(DecimalType n, Object arg)
    {
        return n;
    }
    

    public Object visit(EnumeratedType n, Object arg)
    {
       return n;
    }

    public Object visit(IntegralType n, Object arg)
    {
        return n;
    }
    
    public Object visit(ExceptionType n, Object arg)
    {
        return n;
    }
    

/*    public Object visit(LatinCharType n, Object arg)
    {
        return n;
    }*/

    public Object visit(RealType n, Object arg)
    {
        return n;
    }

    public Object visit(RecordType n, Object arg)
    {
      int count = n.getMemberCount();
      for (int i=0; i < count; i++)
      {
        MemberObject member = (MemberObject) n.getMember(i);
        member.setDefinitionType((TypeReference)member.getDefinitionType().accept(this, arg));
      }
      return n;
    }
    
    public Object visit(TableType n, Object arg)
    {
      int count = n.getMemberCount();
      for (int i=0; i < count; i++)
      {
        MemberObject member = (MemberObject) n.getMember(i);
        member.setDefinitionType((TypeReference)member.getDefinitionType().accept(this, arg));
      }
      return n;
    }
    

    public Object visit(PointerType n, Object arg)
    {
        n.setBaseTypeReference((TypeReference) n.getBaseTypeReference().accept(this, arg));
        return null;
    }
    
    public Object visit(ReferenceType n, Object arg)
    {
        n.setBaseTypeReference((TypeReference) n.getBaseTypeReference().accept(this, arg));
        return null;
    }
    

    public Object visit(ProcedureType n, Object arg)
    {
      n.setReturnType((TypeReference) n.getReturnType().accept(this, arg));
      n.setParent((ClassType) n.getParent().accept(this, arg));
      for (int i=0; i < n.getParameterCount(); i++)
      {
        n.setParameter(i, (FormalParameterType) n.getParameter(i).accept(this, arg));
      }
      return n;
    }
    
    
    public Object visit(SequenceType n, Object arg)
    {
      return n;
    }
    

    public Object visit(TimeType n, Object arg)
    {
      return n;
    }
    
    public Object visit(TimestampType n, Object arg)
    {
      return n;
    }
    
/*    public Object visit(VisibleCharType n, Object arg)
    {
      return n;
    }*/
    

/*    public Object visit(ASCIICharType n, Object arg)
    {
      return n;
    }*/
    
    
    public Object visit(ObjectIdentifierType n, Object arg)
    {
      return n;
    }    
    

/*    public Object visit(UCS2CharType n, Object arg)
    {
      return n;
    }*/

    public Object visit(UnknownType n, Object arg)
    {
      return n;
    }


/*    public Object visit(ByteType n, Object arg)
    {
      return n;
    }

    public Object visit(DoubleType n, Object arg)
    {
      return n;
    }

    public Object visit(IntType n, Object arg)
    {
      return n;
    }


    public Object visit(LongType n, Object arg)
    {
      return n;
    }

    public Object visit(ShortType n, Object arg)
    {
      return n;
    }


    public Object visit(SingleType n, Object arg)
    {
      return n;
    }

    public Object visit(UnsignedByteType n, Object arg)
    {
      return n;
    }

    public Object visit(UnsignedIntType n, Object arg)
    {
      return n;
    }

    public Object visit(UnsignedShortType n, Object arg)
    {
      return n;
    }

    public Object visit(CurrencyType n, Object arg)
    {
      return n;
    }

    public Object visit(LatinStringType n, Object arg)
    {
      return n;
    }
    
    public Object visit(UCS2StringType n, Object arg)
    {
      return n;
    }*/
    

    public Object visit(DurationType n, Object arg)
    {
      return n;
    }


    public Object visit(UnionType n, Object arg)
    {
      int count = n.getMemberCount();
      for (int i=0; i < count; i++)
      {
        MemberObject member = (MemberObject) n.getMember(i);
        member.setDefinitionType((TypeReference)member.getDefinitionType().accept(this, arg));
      }
      return n;
    }

    public Object visit(VoidType n, Object arg)
    {
      return n;
    }

    public Object visit(SetType n, Object arg)
    {
      return n;
    }
    
    public Object visit(NameSpaceType n, Object arg)
    {
      return n;
    }

    public Object visit(NamedTypeReference n, Object arg)
    {
      n.setType((Type) n.getType().accept(this, arg));
      return n;
    }

    public Object visit(UnnamedTypeReference n, Object arg)
    {
      n.setType((Type) n.getType().accept(this, arg));
      return n;
    }

    public Object visit(FormalParameterType n, Object arg)
    {
      n.setTypeReference((TypeReference) n.getTypeReference().accept(this, arg));
      return n;
    }

    public Object visit(CharacterType n, Object arg)
    {
      return n;
    }

    public Object visit(DateTimeType n, Object arg)
    {
      return n;
    }

    public Object visit(URIType n, Object arg)
    {
      return n;
    }

    public Object visit(StringType n, Object arg)
    {
      return n;
    }


}
