package rs.ac.bg.etf.pp1;

import rs.etf.pp1.symboltable.Tab;
import rs.etf.pp1.symboltable.concepts.Obj;
import rs.etf.pp1.symboltable.concepts.Struct;

import java.util.ArrayList;
import java.util.List;

public class MethodObj
{
    public static class Pair<K,V>
    {
        private K key;
        private V value;

        public Pair(K key, V value)
        {
            this.key = key;
            this.value = value;
        }

        public K getKey()
        {
            return key;
        }

        public void setKey(K key)
        {
            this.key = key;
        }

        public V getValue()
        {
            return value;
        }

        public void setValue(V value)
        {
            this.value = value;
        }
    }
    private static List<MethodObj> globalMethods;

    public static List<MethodObj> allMethods;

    private static int staticId;

    public static void init()
    {
        staticId = 0;
        globalMethods = new ArrayList<>();
        allMethods = new ArrayList<>();
        Obj ord = Tab.ordObj;
        Obj len = Tab.lenObj;
        Obj chr = Tab.chrObj;
        MethodObj tmp = new MethodObj(ord.getName(), ord, ord.getType(), null);
        tmp.AddParam(new ArrayList<>(ord.getLocalSymbols()).get(0));
        allMethods.add(tmp);
        tmp = new MethodObj(len.getName(), len, len.getType(), null);
        tmp.AddParam(new ArrayList<>(len.getLocalSymbols()).get(0));
        allMethods.add(tmp);
        tmp = new MethodObj(chr.getName(), chr, chr.getType(), null);
        tmp.AddParam(new ArrayList<>(chr.getLocalSymbols()).get(0));
        allMethods.add(tmp);
    }

    public static List<MethodObj> getGlobalMethods()
    {
        return globalMethods;
    }

    private String name;
    private Obj method;
    private Struct returnType;
    private ClassObj owner;
    private List<Obj> params;
    private List<Pair<Obj, Integer>> optParams;
    private boolean isConstructor;
    private int localVarCount;
    public int id;

    public MethodObj(String name, Obj method, Struct returnType, ClassObj owner)
    {
        this.name = name;
        this.method = method;
        this.returnType = returnType;
        this.owner = owner;
        this.params = new ArrayList<>();
        this.optParams = new ArrayList<>();
        this.isConstructor = false;
        this.id = staticId++;
        this.localVarCount = 0;
    }

    public MethodObj(String name, Obj method, Struct returnType)
    {
        this(name, method, returnType, null);
        globalMethods.add(this);
    }

    public String getName()
    {
        return name;
    }

    public Obj getMethod()
    {
        return method;
    }

    public Struct getReturnType()
    {
        return returnType;
    }

    public ClassObj getOwner()
    {
        return owner;
    }

    public List<Obj> getParams()
    {
        return params;
    }

    public List<Pair<Obj, Integer>> getOptParams()
    {
        return optParams;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setMethod(Obj method)
    {
        this.method = method;
    }

    public void setReturnType(Struct returnType)
    {
        this.returnType = returnType;
    }

    public void setOwner(ClassObj owner)
    {
        this.owner = owner;
    }

    public void setParams(List<Obj> params)
    {
        this.params = params;
    }

    public void setOptParams(List<Pair<Obj, Integer>> optParams)
    {
        this.optParams = optParams;
    }

    public boolean IsGlobal()
    {
        return owner == null;
    }

    public void AddParam(Obj o)
    {
        params.add(o);
    }

    public void AddOptParam(Obj o, Integer val)
    {
        optParams.add(new Pair<>(o, val));
    }

    public boolean isConstructor()
    {
        return isConstructor;
    }

    public void setConstructor(boolean constructor)
    {
        isConstructor = constructor;
    }

    public int getLocalVarCount()
    {
        return localVarCount;
    }

    public void addLocalVar()
    {
        localVarCount++;
    }
}
