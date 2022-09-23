package rs.ac.bg.etf.pp1;

import rs.etf.pp1.symboltable.Tab;
import rs.etf.pp1.symboltable.concepts.Struct;

import java.util.List;

public class StructCompare
{
    public static boolean EquivalentTypes(Struct src, Struct dst)
    {
        return src == dst || (src.getKind() == Struct.Array && dst.getKind() == Struct.Array && EquivalentTypes(src.getElemType(), dst.getElemType()));
    }

    public static boolean CompatibleTypes(Struct src, Struct dst)
    {
        return EquivalentTypes(src, dst) || ((src.isRefType() && dst.equals(Tab.nullType)) || (dst.isRefType() && src.equals(Tab.nullType)));
    }

    public static boolean CompatibleTypesDuringAssignment(Struct src, Struct dst)
    {
        boolean flag = EquivalentTypes(src, dst) || (dst.isRefType() && src.equals(Tab.nullType));

        if(flag)
            return flag;

        if(src.getKind() == Struct.Class && dst.getKind() == Struct.Class)
        {
            List<ClassObj> list = ClassObj.getClasses();
            ClassObj srcObject = null;
            ClassObj dstObject = null;
            for(ClassObj classObj : list)
            {
                if(!classObj.getThisClassObj().equals(Tab.noObj) && classObj.getThisClassObj().getType().equals(src))
                    srcObject = classObj;
                if(!classObj.getThisClassObj().equals(Tab.noObj) && classObj.getThisClassObj().getType().equals(dst))
                    dstObject = classObj;
                if(srcObject != null && dstObject != null)
                    break;
            }
            if(srcObject == null || dstObject == null)
                return false;
            while(srcObject != dstObject && srcObject.HasParent())
                srcObject = srcObject.getParent();
            return srcObject == dstObject;
        }

        return false;
    }
}
