/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.optimasc.datatypes.visitor;

import com.optimasc.datatypes.UnknownType;
import com.optimasc.datatypes.VariableInstance;
import com.optimasc.datatypes.aggregate.ArrayType;
import com.optimasc.datatypes.aggregate.ClassType;
import com.optimasc.datatypes.aggregate.RecordType;
import com.optimasc.datatypes.derived.ByteType;
import com.optimasc.datatypes.derived.CurrencyType;
import com.optimasc.datatypes.derived.DateType;
import com.optimasc.datatypes.derived.DoubleType;
import com.optimasc.datatypes.derived.IntType;
import com.optimasc.datatypes.derived.LatinCharType;
import com.optimasc.datatypes.derived.LongType;
import com.optimasc.datatypes.derived.ShortType;
import com.optimasc.datatypes.derived.SingleType;
import com.optimasc.datatypes.derived.TimestampType;
import com.optimasc.datatypes.derived.UCS2CharType;
import com.optimasc.datatypes.derived.UnsignedByteType;
import com.optimasc.datatypes.derived.UnsignedIntType;
import com.optimasc.datatypes.derived.UnsignedShortType;
import com.optimasc.datatypes.generated.PointerType;
import com.optimasc.datatypes.generated.ProcedureType;
import com.optimasc.datatypes.generated.UnionType;
import com.optimasc.datatypes.primitives.BinaryType;
import com.optimasc.datatypes.primitives.BooleanType;
import com.optimasc.datatypes.primitives.CharacterType;
import com.optimasc.datatypes.primitives.DateTimeType;
import com.optimasc.datatypes.primitives.DurationType;
import com.optimasc.datatypes.primitives.EnumType;
import com.optimasc.datatypes.primitives.IntegralType;
import com.optimasc.datatypes.primitives.PrimitiveType;
import com.optimasc.datatypes.primitives.RealType;
import com.optimasc.datatypes.primitives.StringType;
import com.optimasc.datatypes.primitives.TimeType;
import com.optimasc.datatypes.primitives.VoidType;

/**
 *
 * @author Carl
 */
public class DefaultTypeVisitor implements TypeVisitor {

    public Object visit(ArrayType n, Object arg)
    {
        n.getBaseType().accept(this, arg);
        return null;
    }

    public Object visit(BinaryType n, Object arg)
    {
        return null;
    }

    public Object visit(BooleanType n, Object arg)
    {
        return null;
    }

    public Object visit(ClassType n, Object arg)
    {
        int i;
        for (i = 0; i < n.geFieldCount(); i++)
        {
            n.geField(i).accept(this,arg);
        }
        for (i = 0; i < n.geMethodCount(); i++)
        {
            n.geMethod(i).accept(this,arg);
        }
        return null;
    }

    public Object visit(DateType n, Object arg)
    {
        return null;
    }

    public Object visit(EnumType n, Object arg)
    {
        return null;
    }

    public Object visit(IntegralType n, Object arg)
    {
        return null;
    }

    public Object visit(LatinCharType n, Object arg)
    {
        return null;
    }

    public Object visit(RealType n, Object arg)
    {
        return null;
    }

    public Object visit(RecordType n, Object arg)
    {
        int i;
        for (i = 0; i < n.geFieldCount(); i++)
        {
            n.geField(i).accept(this, arg);
        }
        return null;
    }

    public Object visit(PointerType n, Object arg)
    {
        n.getBaseType().accept(this, arg);
        return null;
    }

    public Object visit(ProcedureType n, Object arg)
    {
        int i;
        for (i = 0; i < n.getParameterCount(); i++)
        {
            n.getParameter(i).accept(this, arg);
        }
        n.getReturnType().accept(this, arg);
        return null;
    }

    public Object visit(TimeType n, Object arg)
    {
        return null;
    }
    
    public Object visit(TimestampType n, Object arg)
    {
        return null;
    }
    

    public Object visit(UCS2CharType n, Object arg)
    {
        return null;
    }

    public Object visit(UnknownType n, Object arg)
    {
        return null;
    }


    public Object visit(VariableInstance n, Object arg)
    {
        n.getDataType().accept(this,arg);
        return null;
    }

    public Object visit(ByteType n, Object arg)
    {
        return null;
    }

    public Object visit(DoubleType n, Object arg)
    {
        return null;
    }

    public Object visit(IntType n, Object arg)
    {
        return null;
    }


    public Object visit(LongType n, Object arg)
    {
        return null;
    }

    public Object visit(ShortType n, Object arg)
    {
        return null;
    }


    public Object visit(SingleType n, Object arg)
    {
        return null;
    }

    public Object visit(UnsignedByteType n, Object arg)
    {
        return null;
    }

    public Object visit(UnsignedIntType n, Object arg)
    {
        return null;
    }

    public Object visit(UnsignedShortType n, Object arg)
    {
        return null;
    }

    public Object visit(CurrencyType n, Object arg)
    {
        return null;
    }

    public Object visit(StringType n, Object arg)
    {
      return null;
    }

    public Object visit(CharacterType n, Object arg)
    {
      // TODO Auto-generated method stub
      return null;
    }

    public Object visit(DateTimeType n, Object arg)
    {
      // TODO Auto-generated method stub
      return null;
    }

    public Object visit(DurationType n, Object arg)
    {
      // TODO Auto-generated method stub
      return null;
    }


    public Object visit(UnionType n, Object arg)
    {
      // TODO Auto-generated method stub
      return null;
    }

    public Object visit(VoidType n, Object arg)
    {
      // TODO Auto-generated method stub
      return null;
    }

}
