package rs.ac.bg.etf.pp1;

import rs.etf.pp1.symboltable.concepts.Obj;

import java.util.ArrayList;
import java.util.List;

public class ClassObj
{
    private static List<ClassObj> classes;

    public static void init()
    {
        classes = new ArrayList<>();
    }

    public static List<ClassObj> getClasses()
    {
        return classes;
    }

    private ClassObj parent;
    private Obj thisClassObj;
    private String name;
    private List<MethodObj> methods;
    private List<String> overridenMethods;
    private List<Obj> fields;

    public ClassObj(ClassObj parent, Obj thisClassObj, String name)
    {
        this.parent = parent;
        this.thisClassObj = thisClassObj;
        this.name = name;
        this.methods = new ArrayList<>();
        this.fields = new ArrayList<>();
        this.overridenMethods = new ArrayList<>();
        classes.add(this);
    }

    public ClassObj(Obj thisClassObj, String name)
    {
        this(null, thisClassObj, name);
    }

    public ClassObj getParent()
    {
        return parent;
    }

    public void setParent(ClassObj parent)
    {
        this.parent = parent;
    }

    public Obj getThisClassObj()
    {
        return thisClassObj;
    }

    public String getName()
    {
        return name;
    }

    public List<MethodObj> getMethods()
    {
        return methods;
    }

    public List<Obj> getFields()
    {
        return fields;
    }

    public boolean HasParent()
    {
        return parent != null;
    }

    public void AddMethod(MethodObj method)
    {
        methods.add(method);
    }

    public void AddField(Obj field)
    {
        fields.add(field);
    }

    public void AddOverridenMethod(String methodName)
    {
        overridenMethods.add(methodName);
    }

    public boolean IsOverriden(String methodName)
    {
        return overridenMethods.stream().anyMatch(s -> { return s.equals(methodName); });
    }
}
