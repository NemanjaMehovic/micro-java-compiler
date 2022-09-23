package rs.ac.bg.etf.pp1;

import org.apache.log4j.Logger;
import rs.ac.bg.etf.pp1.ast.*;
import rs.etf.pp1.symboltable.Tab;
import rs.etf.pp1.symboltable.concepts.Obj;
import rs.etf.pp1.symboltable.concepts.Struct;
import rs.etf.pp1.symboltable.structure.HashTableDataStructure;
import rs.etf.pp1.symboltable.visitors.DumpSymbolTableVisitor;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;


public class SemanticAnalyzer extends VisitorAdaptor
{
    public static final Struct boolType = new Struct(Struct.Bool);
    public static final Struct voidType = new Struct(Struct.None);

    private boolean errorDetected = false;
    private final Logger log = Logger.getLogger(getClass());
    private Obj workingType = null;
    private String workingTypeName = null;
    private SyntaxNode forError = null;

    private Obj method = null;
    private MethodObj methodObj = null;
    private Struct returnTypeMeth = null;
    private boolean returnCalled = false;
    private int numberOfForm = 0;
    private int numberOfOpt = 0;
    private int brojGlobalnihPromenljivi = 0;

    private Obj currentClass = null;
    private ClassObj classObj = null;

    private int loop = 0;

    private Stack<List<Struct>> callParamsStack = new Stack<>();
    private List<Struct> callParams = null;

    public boolean isErrorDetected()
    {
        return errorDetected;
    }

    private void report_error(String message, SyntaxNode info)
    {
        errorDetected = true;
        StringBuilder msg = new StringBuilder(message);
        int line = (info == null) ? 0 : info.getLine();
        if(line != 0)
            msg.append(" na liniji ").append(line);
        log.error(msg.toString());
    }

    private void report_info(String message, SyntaxNode info)
    {
        StringBuilder msg = new StringBuilder(message);
        int line = (info == null) ? 0 : info.getLine();
        if(line != 0)
            msg.append(" na liniji ").append(line);
        log.info(msg.toString());
    }

    public int getBrojGlobalnihPromenljivi()
    {
        return brojGlobalnihPromenljivi;
    }

    private String getObjectPrint(Object toPrint)
    {
        DumpSymbolTableVisitor tmp = new DumpSymbolTableVisitor();
        if(toPrint instanceof Obj)
            tmp.visitObjNode((Obj) toPrint);
        else if(toPrint instanceof Struct)
            tmp.visitStructNode((Struct) toPrint);
        return tmp.getOutput();
    }

    public SemanticAnalyzer()
    {
        super();
        Tab.init();
        ClassObj.init();
        MethodObj.init();
        Tab.currentScope().addToLocals(new Obj(Obj.Type, "bool", boolType));
    }

    public void visit(ProgramName programName)
    {
        programName.obj = Tab.insert(Obj.Prog, programName.getName(), Tab.noType);
        Tab.openScope();
    }

    public void visit(Program program)
    {
        boolean flag = true;

        for(MethodObj tmpObj : MethodObj.getGlobalMethods())
            if(tmpObj.getName().equals("main"))
            {
                if(tmpObj.getOptParams().size() != 0 || tmpObj.getParams().size() != 0)
                    report_error("main ne sme da ima parametre.", program);
                if(tmpObj.getReturnType() != voidType)
                    report_error("Povratni tip main-a mora biti void", program);
                flag = false;
                break;
            }

        if(flag)
            report_error("main nije pronadjen.", program);

        brojGlobalnihPromenljivi = Tab.currentScope().getnVars();
        Tab.chainLocalSymbols(program.getProgramName().obj);
        Tab.closeScope();
    }

    public void visit(Type type)
    {
        Obj found = Tab.find(type.getType());
        workingTypeName = type.getType();
        if(found == Tab.noObj)
        {
            report_error(type.getType() + " ne postoji.", type);
            workingType = type.obj = Tab.noObj;
            return;
        }
        if(found.getKind() != Obj.Type)
        {
            report_error(type.getType() + " nije tip.", type);
            workingType = type.obj = Tab.noObj;
            return;
        }
        workingType = type.obj = found;
    }

    public void visit(ConstD constD)
    {
        workingType = null;
    }

    public void visit(ConstTypeName constTypeName)
    {
        Struct type = constTypeName.getType().obj.getType();
        if(!(type == Tab.intType || type == Tab.charType || type == boolType))
            report_error("Konstante mogu biti samo int,char i bool.", constTypeName);
    }

    public void visit(NumberConst numberConst)
    {
        Obj found = Tab.currentScope().findSymbol(numberConst.getName());
        if(found != null)
        {
            report_error(numberConst.getName() + " je vec deklarisano u trenutnom opsegu.", numberConst);
            numberConst.obj = Tab.noObj;
            return;
        }
        if(workingType.getType() != Tab.intType)
        {
            report_error("Tip konstante i sama konstanta " + numberConst.getName() + " se ne slazu.", numberConst);
            numberConst.obj = Tab.noObj;
            return;
        }
        Obj tmpConst = Tab.insert(Obj.Con, numberConst.getName(), Tab.intType);
        tmpConst.setAdr(numberConst.getVal());
        numberConst.obj = tmpConst;
    }

    public void visit(CharConst charConst)
    {
        Obj found = Tab.currentScope().findSymbol(charConst.getName());
        if(found != null)
        {
            report_error(charConst.getName() + " je vec deklarisano u trenutnom opsegu.", charConst);
            charConst.obj = Tab.noObj;
            return;
        }
        if(workingType.getType() != Tab.charType)
        {
            report_error("Tip konstante i sama konstanta " + charConst.getName() + " se ne slazu.", charConst);
            charConst.obj = Tab.noObj;
            return;
        }
        Obj tmpConst = Tab.insert(Obj.Con, charConst.getName(), Tab.charType);
        tmpConst.setAdr(charConst.getVal());
        charConst.obj = tmpConst;
    }

    public void visit(BooleanConst booleanConst)
    {
        Obj found = Tab.currentScope().findSymbol(booleanConst.getName());
        if(found != null)
        {
            report_error(booleanConst.getName() + " je vec deklarisano u trenutnom opsegu.", booleanConst);
            booleanConst.obj = Tab.noObj;
            return;
        }
        if(workingType.getType() != boolType)
        {
            report_error("Tip konstante i sama konstanta " + booleanConst.getName() + " se ne slazu.", booleanConst);
            booleanConst.obj = Tab.noObj;
            return;
        }
        Obj tmpConst = Tab.insert(Obj.Con, booleanConst.getName(), boolType);
        tmpConst.setAdr(booleanConst.getVal() ? 1 : 0);
        booleanConst.obj = tmpConst;
    }

    public void visit(ArrayVar var)
    {
        Obj found = Tab.currentScope().findSymbol(var.getName());
        if(found != null)
        {
            report_error(var.getName() + " je vec deklarisano u trenutnom opsegu.", var);
            return;
        }
        Obj tmp = Tab.insert(currentClass != null ? Obj.Fld : Obj.Var, var.getName(), new Struct(Struct.Array, workingType.getType()));
        if(classObj != null)
        {
            classObj.AddField(tmp);
            tmp.setAdr(tmp.getAdr() + 1);//prvo polje u klasi sluzi za vft
        }
    }

    public void visit(NonArrayVar var)
    {
        Obj found = Tab.currentScope().findSymbol(var.getName());
        if(found != null)
        {
            report_error(var.getName() + " je vec deklarisano u trenutnom opsegu.", var);
            return;
        }
        Obj tmp = Tab.insert(currentClass != null ? Obj.Fld : Obj.Var, var.getName(), workingType.getType());
        if(classObj != null)
        {
            classObj.AddField(tmp);
            tmp.setAdr(tmp.getAdr() + 1);//prvo polje u klasi sluzi za vft
        }
    }

    public void visit(MethodVoid meth)
    {
        method = new Obj(Obj.Meth, meth.getName(), voidType);
        if(classObj == null)
            methodObj = new MethodObj(meth.getName(), method, voidType);
        else
            methodObj = new MethodObj(meth.getName(), method, voidType, classObj);
        returnTypeMeth = voidType;
        forError = meth;
        //otvaramo scope da bi smo gledali samo parametre funkcije
        Tab.openScope();
    }

    public void visit(MethodType meth)
    {
        method = new Obj(Obj.Meth, meth.getName(), workingType.getType());
        if(classObj == null)
            methodObj = new MethodObj(meth.getName(), method, workingType.getType());
        else
            methodObj = new MethodObj(meth.getName(), method, workingType.getType(), classObj);
        returnTypeMeth = workingType.getType();
        forError = meth;
        //otvaramo scope da bi smo gledali samo parametre funkcije
        Tab.openScope();
    }

    private boolean MethodCheckForOverride(Obj found)
    {
        if(found.getKind() != Obj.Meth || found.getType() != returnTypeMeth || returnTypeMeth == Tab.noType || !classObj.HasParent() || classObj.IsOverriden(found.getName()))
            return false;

        List<MethodObj> list = classObj.getParent().getMethods().stream().filter(methodObj1 -> {
            return methodObj1.getName().equals(found.getName());
        }).collect(Collectors.toList());
        if(list.size() == 0)
            return false;

        MethodObj tmp = list.get(0);
        if(tmp.getParams().size() != methodObj.getParams().size() || tmp.getOptParams().size() != methodObj.getOptParams().size())
            return false;

        boolean flag = true;
        for(int i = 0; i < tmp.getParams().size(); i++)
            if(!tmp.getParams().get(i).getType().equals(methodObj.getParams().get(i).getType()))
            {
                flag = false;
                break;
            }
        if(flag)
            for(int i = 0; i < tmp.getOptParams().size(); i++)
            {
                MethodObj.Pair<Obj, Integer> superPair = tmp.getOptParams().get(i);
                MethodObj.Pair<Obj, Integer> currPair = methodObj.getOptParams().get(i);
                if(!superPair.getKey().getType().equals(currPair.getKey().getType()) || !superPair.getValue().equals(currPair.getValue()))
                {
                    flag = false;
                    break;
                }
            }
        return flag;
    }

    public void visit(MethodSigEnd methodSigEnd)
    {
        //Zatvaramo scope posto smo zavrsili proveru parametra
        boolean flag = true;
        Tab.closeScope();
        Obj found = Tab.currentScope().findSymbol(methodObj.getName());
        if(currentClass == null)
        {
            if(found != null)
            {
                report_error(methodObj.getName() + " je vec deklarisano u trenutnom opsegu.", forError);
                method = null;
            }
            if(method != null)
            {
                method = Tab.insert(method.getKind(), method.getName(), method.getType());
                methodObj.setMethod(method);
            }
        }
        else if(found != null)
        {
            if(!MethodCheckForOverride(found))
            {
                report_error(methodObj.getName() + " je vec deklarisano u trenutnom opsegu.", forError);
                method = null;
            }
            else
            {
                method = found;
                found.setLocals(new HashTableDataStructure());
                List<MethodObj> list = classObj.getMethods().stream().filter(methodObj1 -> {
                    return methodObj1.getName().equals(method.getName());
                }).collect(Collectors.toList());
                MethodObj tmpMethodObj = list.get(0);
                tmpMethodObj.setParams(methodObj.getParams());
                tmpMethodObj.setOptParams(methodObj.getOptParams());
                methodObj = tmpMethodObj;
                methodObj.setMethod(method);
                classObj.AddOverridenMethod(method.getName());
                flag = false;
            }
        }
        else
        {
            method = Tab.insert(method.getKind(), method.getName(), method.getType());
            methodObj.setMethod(method);
        }
        Tab.openScope();
        int add = 0;
        if(currentClass != null)
        {
            Tab.insert(Obj.Var, "this", currentClass == Tab.noObj ? Tab.noType : currentClass.getType());
            if(flag)
                classObj.AddMethod(methodObj);
            numberOfForm++;
            add = 1;
        }
        List<Obj> params = new ArrayList<>();
        Obj tmp = null;
        for(Obj param : methodObj.getParams())
        {
            tmp = Tab.insert(param.getKind(), param.getName(), param.getType());
            tmp.setFpPos(param.getFpPos() + add);
            params.add(tmp);
        }
        methodObj.setParams(params);
        List<MethodObj.Pair<Obj, Integer>> optArgs = new ArrayList<>();
        for(MethodObj.Pair<Obj, Integer> optArg : methodObj.getOptParams())
        {
            tmp = Tab.insert(optArg.getKey().getKind(), optArg.getKey().getName(), optArg.getKey().getType());
            tmp.setFpPos(optArg.getKey().getFpPos() + add);
            optArgs.add(new MethodObj.Pair<Obj, Integer>(tmp, optArg.getValue()));
        }
        methodObj.setOptParams(optArgs);
        methodSigEnd.obj = method;
    }

    public void visit(FormParsNonArray formParsNonArray)
    {
        Obj found = Tab.currentScope().findSymbol(formParsNonArray.getName());
        if(found != null)
        {
            report_error(formParsNonArray.getName() + " je vec deklarisano u trenutnom opsegu.", formParsNonArray);
            numberOfForm++;
            return;
        }
        Obj tmp = new Obj(Obj.Var, formParsNonArray.getName(), formParsNonArray.getType().obj.getType());
        methodObj.AddParam(tmp);
        tmp.setFpPos(numberOfForm++);
    }

    public void visit(FormParsArray formParsArray)
    {
        Obj found = Tab.currentScope().findSymbol(formParsArray.getName());
        if(found != null)
        {
            report_error(formParsArray.getName() + " je vec deklarisano u trenutnom opsegu.", formParsArray);
            numberOfForm++;
            return;
        }
        Obj tmp = new Obj(Obj.Var, formParsArray.getName(), new Struct(Struct.Array, formParsArray.getType().obj.getType()));
        methodObj.AddParam(tmp);
        tmp.setFpPos(numberOfForm++);
    }

    public void visit(OptArgs optArgs)
    {
        Obj tmp = optArgs.getOptArgsTypes().obj;
        if(tmp != Tab.noObj)
            tmp.setFpPos(numberOfForm + numberOfOpt);
        numberOfOpt++;
    }

    public void visit(NumberOptArg optArg)
    {
        Obj found = Tab.currentScope().findSymbol(optArg.getName());
        if(found != null)
        {
            report_error(optArg.getName() + " je vec deklarisano u trenutnom opsegu.", optArg);
            optArg.obj = Tab.noObj;
            return;
        }
        if(workingType.getType() != Tab.intType)
        {
            report_error("Tip konstante i sama konstanta " + optArg.getName() + " se ne slazu.", optArg);
            optArg.obj = Tab.noObj;
            return;
        }
        Obj tmpConst = new Obj(Obj.Var, optArg.getName(), Tab.intType);
        methodObj.AddOptParam(tmpConst, optArg.getVal());
        optArg.obj = tmpConst;
    }

    public void visit(CharOptArg optArg)
    {
        Obj found = Tab.currentScope().findSymbol(optArg.getName());
        if(found != null)
        {
            report_error(optArg.getName() + " je vec deklarisano u trenutnom opsegu.", optArg);
            optArg.obj = Tab.noObj;
            return;
        }
        if(workingType.getType() != Tab.charType)
        {
            report_error("Tip konstante i sama konstanta " + optArg.getName() + " se ne slazu.", optArg);
            optArg.obj = Tab.noObj;
            return;
        }
        Obj tmpConst = new Obj(Obj.Var, optArg.getName(), Tab.charType);
        methodObj.AddOptParam(tmpConst, (int) optArg.getVal());
        optArg.obj = tmpConst;
    }

    public void visit(BooleanOptArg optArg)
    {
        Obj found = Tab.currentScope().findSymbol(optArg.getName());
        if(found != null)
        {
            report_error(optArg.getName() + " je vec deklarisano u trenutnom opsegu.", optArg);
            optArg.obj = Tab.noObj;
            return;
        }
        if(workingType.getType() != boolType)
        {
            report_error("Tip konstante i sama konstanta " + optArg.getName() + " se ne slazu.", optArg);
            optArg.obj = Tab.noObj;
            return;
        }
        Obj tmpConst = new Obj(Obj.Var, optArg.getName(), boolType);
        methodObj.AddOptParam(tmpConst, optArg.getVal() ? 1 : 0);
        optArg.obj = tmpConst;
    }

    public void visit(ArrayVarNoError var)
    {
        Obj found = Tab.currentScope().findSymbol(var.getName());
        if(found != null)
        {
            report_error(var.getName() + " je vec deklarisano u trenutnom opsegu.", var);
            return;
        }
        Tab.insert(Obj.Var, var.getName(), new Struct(Struct.Array, workingType.getType()));
        if(methodObj != null)
            methodObj.addLocalVar();
    }

    public void visit(NonArrayVarNoError var)
    {
        Obj found = Tab.currentScope().findSymbol(var.getName());
        if(found != null)
        {
            report_error(var.getName() + " je vec deklarisano u trenutnom opsegu.", var);
            return;
        }
        Tab.insert(Obj.Var, var.getName(), workingType.getType());
        if(methodObj != null)
            methodObj.addLocalVar();
    }

    public void visit(MethodDecl methodDecl)
    {
        if(method != null)
        {
            method.setLevel(numberOfForm + numberOfOpt);
            method.setFpPos(numberOfOpt);
            Tab.chainLocalSymbols(method);
            if(!MethodObj.allMethods.contains(methodObj))
                MethodObj.allMethods.add(methodObj);
        }
        else if(classObj != null)
            classObj.getMethods().remove(methodObj);
        else
            MethodObj.getGlobalMethods().remove(methodObj);
        if(!returnCalled && returnTypeMeth != voidType)
            report_error("Metoda mora imati return u sebi ako nije void.", methodDecl);
        numberOfOpt = 0;
        numberOfForm = 0;
        returnCalled = false;
        returnTypeMeth = null;
        method = null;
        methodObj = null;
        Tab.closeScope();
    }

    public void visit(ClassDecl classDecl)
    {
        if(currentClass != Tab.noObj)
            Tab.chainLocalSymbols(currentClass.getType());
        currentClass = null;
        classObj = null;
        Tab.closeScope();
    }

    public void visit(ClassName className)
    {
        Obj found = Tab.currentScope().findSymbol(className.getName());
        if(found != null)
        {
            report_error(className.getName() + " je vec deklarisano u trenutnom opsegu.", className);
            currentClass = Tab.noObj;
        }
        else
            currentClass = Tab.insert(Obj.Type, className.getName(), new Struct(Struct.Class));
        classObj = new ClassObj(currentClass, className.getName());
        className.obj = currentClass;
        Tab.openScope();
    }

    public void visit(ClassEx classEx)
    {
        if(workingType.getType().getKind() != Struct.Class)
        {
            report_error(workingTypeName + " nije klasa.", classEx);
            return;
        }
        Obj found = Tab.find(classEx.getType().getType());
        for(ClassObj tmp : ClassObj.getClasses())
            if(tmp.getThisClassObj() == found)
            {
                classObj.setParent(tmp);
                break;
            }

        ClassObj parent = classObj.getParent();
        //TODO mozda izbaci
        if(currentClass != Tab.noObj)
            currentClass.getType().setElementType(parent.getThisClassObj().getType());
        //TODO dovde
        for(Obj field : parent.getFields())
        {
            Obj tmp = Tab.insert(field.getKind(), field.getName(), field.getType());
            tmp.setAdr(tmp.getAdr() + 1);//prvo polje u klasi sluzi za vft
            classObj.AddField(tmp);
        }
        for(MethodObj method : parent.getMethods())
        {
            String name = method.getName();
            Struct returnType = method.getReturnType();
            boolean constructorFlag = false;
            if(method.getName().equals(parent.getName()) && method.getReturnType() == Tab.noType)
            {
                name = currentClass.getName();
                constructorFlag = true;
            }
            if(Tab.currentScope().findSymbol(name) != null)
            {
                report_error(name + " je vec deklarisano u trenutnom opsegu.(problem kod nasledjivanja).", classEx);
                continue;
            }
            Obj tmpMethod = Tab.insert(Obj.Meth, name, returnType);
            tmpMethod.setFpPos(method.getMethod().getFpPos());
            tmpMethod.setLevel(method.getMethod().getLevel());

            MethodObj tmpMethodObj = new MethodObj(name, tmpMethod, returnType, classObj);
            tmpMethodObj.setConstructor(constructorFlag);
            MethodObj.allMethods.add(tmpMethodObj);
            classObj.AddMethod(tmpMethodObj);

            Tab.openScope();
            Tab.insert(Obj.Var, "this", currentClass == Tab.noObj ? Tab.noType : currentClass.getType());
            for(Obj var : method.getParams())
            {
                Obj tmp = Tab.insert(var.getKind(), var.getName(), var.getType());
                tmp.setFpPos(var.getFpPos());
                tmpMethodObj.AddParam(tmp);
            }
            for(MethodObj.Pair<Obj, Integer> pair : method.getOptParams())
            {
                Obj tmp = Tab.insert(pair.getKey().getKind(), pair.getKey().getName(), pair.getKey().getType());
                tmp.setFpPos(pair.getKey().getFpPos());
                tmpMethodObj.AddOptParam(tmp, pair.getValue());
            }
            for(Obj locals : method.getMethod().getLocalSymbols())
                if(locals.getFpPos() == 0 && !locals.getName().equals("this"))
                    Tab.insert(locals.getKind(), locals.getName(), locals.getType());
            Tab.chainLocalSymbols(tmpMethod);
            Tab.closeScope();
        }
    }

    public void visit(ConstructorName constructorName)
    {
        boolean flag = true;
        if(!constructorName.getName().equals(classObj.getName()))
        {
            report_error("Ime klase i konstruktora moraju biti isti (" + constructorName.getName() + ", " + classObj.getName() + ").", constructorName);
            method = null;
            methodObj = new MethodObj(constructorName.getName(), null, Tab.noType, classObj);
        }
        else
        {
            List<MethodObj> list = classObj.getMethods().stream().filter(MethodObj::isConstructor).collect(Collectors.toList());
            if(list.size() == 0)
            {
                if(Tab.currentScope().findSymbol(constructorName.getName()) != null)
                {
                    report_error(constructorName.getName() + " je vec deklarisano u trenutnom opsegu.", constructorName);
                    method = null;
                    methodObj = new MethodObj(constructorName.getName(), null, Tab.noType, classObj);
                }
                else
                {
                    method = Tab.insert(Obj.Meth, constructorName.getName(), Tab.noType);
                    methodObj = new MethodObj(constructorName.getName(), null, Tab.noType, classObj);
                }
            }
            else
            {
                methodObj = list.get(0);
                method = methodObj.getMethod();
                method.setLocals(new HashTableDataStructure());
                flag = false;
            }
        }
        methodObj.setConstructor(true);
        if(flag)
            classObj.AddMethod(methodObj);
        forError = constructorName;
        methodObj.setMethod(method);
        returnTypeMeth = Tab.noType;
        Tab.openScope();
        Tab.insert(Obj.Var, "this", currentClass == Tab.noObj ? Tab.noType : currentClass.getType());
    }

    public void visit(ConstructorDecl constructorDecl)
    {
        if(method != null)
        {
            Tab.chainLocalSymbols(method);
            if(!MethodObj.allMethods.contains(methodObj))
                MethodObj.allMethods.add(methodObj);
        }
        else
            classObj.getMethods().remove(methodObj);
        numberOfOpt = 0;
        numberOfForm = 0;
        returnTypeMeth = null;
        returnCalled = false;
        method = null;
        methodObj = null;
        Tab.closeScope();
    }

    public void visit(NumberFactor factor)
    {
        factor.struct = Tab.intType;
    }

    public void visit(CharFactor factor)
    {
        factor.struct = Tab.charType;
    }

    public void visit(BoolFactor factor)
    {
        factor.struct = boolType;
    }

    private boolean checkMethodParamsMatch(MethodObj superMethod)
    {
        if(superMethod.getParams().size() > callParams.size() || callParams.size() > (superMethod.getParams().size() + superMethod.getOptParams().size()))
            return false;
        if(superMethod.getMethod() == Tab.lenObj && callParams.size() == 1 && callParams.get(0).getKind() == Struct.Array)
            return true;
        int i;
        for(i = 0; i < superMethod.getParams().size() && i < callParams.size(); i++)
            if(!StructCompare.CompatibleTypesDuringAssignment(callParams.get(i), superMethod.getParams().get(i).getType()))
                return false;
        for(; i < (superMethod.getParams().size() + superMethod.getOptParams().size()) && i < callParams.size(); i++)
            if(!StructCompare.CompatibleTypesDuringAssignment(callParams.get(i), superMethod.getOptParams().get(i - superMethod.getParams().size()).getKey().getType()))
                return false;
        return true;
    }

    public void visit(FactorSuper factor)
    {
        if(currentClass == null)
        {
            report_error("Pokusaj poziva super moetode van klase.", factor);
            factor.struct = Tab.noType;
            callParams = callParamsStack.size() != 0 ? callParamsStack.pop() : null;
            return;
        }
        if(!classObj.HasParent())
        {
            report_error("Pokusaj poziva super moetode u klasi koja ne extenduje drugu klasu.", factor);
            factor.struct = Tab.noType;
            callParams = callParamsStack.size() != 0 ? callParamsStack.pop() : null;
            return;
        }
        if(method == null)
        {
            report_error("Trenutna metoda nije lepo deklarisana ne moze se zakljuciti da li postoji super metoda.", factor);
            factor.struct = Tab.noType;
            callParams = callParamsStack.size() != 0 ? callParamsStack.pop() : null;
            return;
        }
        String methodNameToUse = methodObj.getName();
        boolean useParent = false;
        if(methodObj.getName().equals(classObj.getName()))
        {
            methodNameToUse = classObj.getParent().getName();
            useParent = true;
        }
        final String finalMethodNameToUse = methodNameToUse;
        List<MethodObj> tmpMethods;
        if(useParent)
            tmpMethods = classObj.getParent().getMethods().stream().filter(methodObj1 -> {
                return methodObj1.getName().equals(finalMethodNameToUse) && methodObj1.getReturnType() == methodObj.getReturnType();
            }).collect(Collectors.toList());
        else
            tmpMethods = classObj.getMethods().stream().filter(methodObj1 -> {
                return methodObj1.getName().equals(finalMethodNameToUse) && methodObj1.getReturnType() == methodObj.getReturnType();
            }).collect(Collectors.toList());
        if(tmpMethods.size() == 0)
        {
            report_error("Pokusaj poziva super moetode koja ne postoji u parent klasi", factor);
            factor.struct = Tab.noType;
            callParams = callParamsStack.size() != 0 ? callParamsStack.pop() : null;
            return;
        }
        else if(!checkMethodParamsMatch(tmpMethods.get(0)))
        {
            report_error("Neispravni parametri za poziv metode.", factor);
            factor.struct = Tab.noType;
            callParams = callParamsStack.size() != 0 ? callParamsStack.pop() : null;
            return;
        }
        else if(methodObj.getName().equals(classObj.getName()) && methodObj.getReturnType() == Tab.noType)
            report_info("Poziv konstruktora nadklase : " + getObjectPrint(tmpMethods.get(0).getMethod()), factor);
        factor.struct = methodObj.getReturnType();
        callParams = callParamsStack.size() != 0 ? callParamsStack.pop() : null;
    }

    public void visit(ActParsMultiple actParsMultiple)
    {
        callParams.add(actParsMultiple.getExpr().struct);
    }

    public void visit(ActParsSingle actParsSingle)
    {
        callParams.add(actParsSingle.getExpr().struct);
    }

    public void visit(MakeNewActParmsListSuper notImportant)
    {
        if(callParams != null)
            callParamsStack.push(callParams);
        callParams = new ArrayList<>();
    }

    public void visit(MakeNewActParmsListDesignator notImportant)
    {
        if(callParams != null)
            callParamsStack.push(callParams);
        callParams = new ArrayList<>();
    }

    public void visit(ExprMultiple exprMultiple)
    {
        if(exprMultiple.getExpr().struct != Tab.intType || exprMultiple.getTerm().struct != Tab.intType)
        {
            report_error("Operandi za addop moraju biti tipa int.", exprMultiple);
            exprMultiple.struct = Tab.noType;
            return;
        }
        exprMultiple.struct = Tab.intType;
    }

    public void visit(SingleExpr singleExpr)
    {
        singleExpr.struct = singleExpr.getTerm().struct;
    }

    public void visit(SingleNegExpr singleNegExpr)
    {
        if(singleNegExpr.getTerm().struct != Tab.intType)
        {
            report_error("Operand za - operand mora biti tipa int.", singleNegExpr);
            singleNegExpr.struct = Tab.noType;
            return;
        }
        singleNegExpr.struct = Tab.intType;
    }

    public void visit(TermMultiple termMultiple)
    {
        if(termMultiple.getFactor().struct != Tab.intType || termMultiple.getTerm().struct != Tab.intType)
        {
            report_error("Operandi za mulop moraju biti tipa int.", termMultiple);
            termMultiple.struct = Tab.noType;
            return;
        }
        termMultiple.struct = Tab.intType;
    }

    public void visit(SingleTerm singleTerm)
    {
        singleTerm.struct = singleTerm.getFactor().struct;
    }

    public void visit(DesignatorFactor factor)
    {
        Obj tmpObj = factor.getDesignator().obj;
        if(tmpObj.getKind() != Obj.Var && tmpObj.getKind() != Obj.Con && tmpObj.getKind() != Obj.Fld && tmpObj.getKind() != Obj.Elem)
        {
            report_error(tmpObj.getName() + " nije promenljiva/polje/konstanta", factor);
            factor.struct = Tab.noType;
        }
        else
            factor.struct = tmpObj.getType();
    }

    public void visit(DesignatorMultiple designator)
    {
        Obj left = designator.getDesignator().obj;
        String right = designator.getName();
        if(left.getType().getKind() != Struct.Class || (left.getKind() != Obj.Var && left.getKind() != Obj.Fld && left.getKind() != Obj.Elem))
        {
            report_error("Levi operand . mora biti tipa klase.", designator);
            designator.obj = Tab.noObj;
            return;
        }
        Obj found = null;
        if(designator.getDesignator() instanceof DesignatorThis)
            found = Tab.currentScope().getOuter().findSymbol(right);//Outer scope uvek postoji
        else
            found = left.getType().getMembersTable().searchKey(right);
        if(found == null)
        {
            report_error(right + " ne postoji kao polje/metoda klase " + left.getName(), designator);
            designator.obj = Tab.noObj;
            return;
        }
        designator.obj = found;
        if(found.getKind() == Obj.Fld)
            report_info("Pristup polju " + getObjectPrint(found), designator);
    }

    public void visit(DesignatorArray designator)
    {
        Obj left = designator.getDesignator().obj;
        Struct right = designator.getExpr().struct;
        if(left.getType().getKind() != Struct.Array)
        {
            report_error("Operand za operand[expr] mora biti niz.", designator);
            designator.obj = Tab.noObj;
        }
        else if(right != Tab.intType)
        {
            report_error("Expr za operand[expr] mora biti tipa int.", designator);
            designator.obj = Tab.noObj;
        }
        else
        {
            report_info("Pristup elementu niza " + getObjectPrint(left), designator);
            designator.obj = new Obj(Obj.Elem, left.getName() + "[]", left.getType().getElemType());
        }
    }

    private boolean isMethodParam(Obj var)
    {
        if(methodObj == null)
            return false;

        List<Obj> tmp = methodObj.getParams().stream().filter(obj -> {
            return obj.getName().equals(var.getName());
        }).collect(Collectors.toList());

        List<MethodObj.Pair<Obj, Integer>> tmp2 = methodObj.getOptParams().stream().filter(obj -> {
            return obj.getKey().getName().equals(var.getName());
        }).collect(Collectors.toList());

        return tmp.size() != 0 || tmp2.size() != 0;
    }

    public void visit(SingleDesignator designator)
    {
        Obj found = Tab.find(designator.getName());
        if(found == Tab.noObj)
        {
            report_error(designator.getName() + " ne postoji.", designator);
            designator.obj = Tab.noObj;
            return;
        }
        designator.obj = found;
        if(found.getKind() == Obj.Fld)
            report_info("Pristup polju " + getObjectPrint(found), designator);
        if(found.getKind() != Obj.Var)
            return;
        if(found.getLevel() == 0)
            report_info("Pristup globalnoj promenljivi " + getObjectPrint(found), designator);
        else if(isMethodParam(found))
            report_info("Pristup parametru metode " + getObjectPrint(found), designator);
        else
            report_info("Pristup lokalnoj promenljivi " + getObjectPrint(found), designator);
    }

    public void visit(DesignatorThis designator)
    {
        if(classObj == null)
        {
            report_error("Pokusaj poziva this van klase. ", designator);
            designator.obj = Tab.noObj;
            return;
        }
        //Teorecki ne bi trebalo da bude ikad null posto uvek ubacujemo this
        designator.obj = Tab.currentScope().findSymbol("this");
        report_info("Pristup this parametru " + getObjectPrint(designator.obj), designator);
    }

    public void visit(DesignatorActParsFactor factor)
    {
        Obj tmp = factor.getMakeNewActParmsListDesignator().getDesignator().obj;
        if(tmp.getKind() != Obj.Meth)
        {
            report_error(tmp.getName() + " nije metoda.", factor);
            factor.struct = Tab.noType;
            callParams = callParamsStack.size() != 0 ? callParamsStack.pop() : null;
            return;
        }
        List<MethodObj> methodObjs = MethodObj.allMethods.stream().filter(methodObj1 -> {
            return methodObj1.getMethod() == tmp;
        }).collect(Collectors.toList());
        MethodObj tmpMethodObj = methodObjs.get(0);
        if(!checkMethodParamsMatch(tmpMethodObj))
        {
            report_error("Parametri nisu dobri za poziv metode " + tmpMethodObj.getName(), factor);
            factor.struct = Tab.noType;
        }
        else if(tmpMethodObj.IsGlobal())
        {
            report_info("Poziv globalne metode " + getObjectPrint(tmp), factor);
            factor.struct = tmp.getType();
        }
        else
        {
            //Da li se ovo smatra kao pristup polju? idk
            report_info("Poziv metode koja pripada klasi " + getObjectPrint(tmp), factor);
            factor.struct = tmp.getType();
        }
        callParams = callParamsStack.size() != 0 ? callParamsStack.pop() : null;
    }

    public void visit(TypeFactor factor)
    {
        if(workingType.getType().getKind() != Struct.Class)
        {
            report_error(workingTypeName + " nije klasa.", factor);
            factor.struct = Tab.noType;
            return;
        }
        factor.struct = workingType.getType();
        report_info("Pravljenje objekta klase " + getObjectPrint(workingType), factor);
    }

    public void visit(TypeExprFactor factor)
    {
        factor.struct = new Struct(Struct.Array, workingType.getType());
        Struct tmp = factor.getExpr().struct;
        if(tmp != Tab.intType)
        {
            report_error("Velicina niza nije int.", factor);
            factor.struct = Tab.noType;
        }
    }

    public void visit(ExprFactor factor)
    {
        factor.struct = factor.getExpr().struct;
    }

    public void visit(CondFactNoRelop condition)
    {
        Struct tmp = condition.getExpr().struct;
        if(tmp != boolType)
            report_error("Uslov mora biti tipa bool.", condition);
    }

    public void visit(CondFactRelop condition)
    {
        Struct expr1 = condition.getExpr().struct;
        Struct expr2 = condition.getExpr1().struct;
        if(!StructCompare.CompatibleTypes(expr1, expr2))
            report_error("Tipovi moraju biti kompatibilni prilikom poredjenja", condition);
        else if(expr1.isRefType() || expr2.isRefType())
        {
            SyntaxNode operation = condition.getRelop();
            if(operation instanceof RelOpEqu || operation instanceof RelOpNeq)
                return;
            report_error("Nad tipovima koji su reference moze se koristiti samo == i !=.", condition);
        }
    }

    public void visit(DesignatorStatementInc statement)
    {
        Obj tmp = statement.getDesignator().obj;
        if(tmp.getType() != Tab.intType)
            report_error("Inkrement se moze koristiti samo nad tipovima int.", statement);
        else if(tmp.getKind() != Obj.Var && tmp.getKind() != Obj.Fld && tmp.getKind() != Obj.Elem)
            report_error("Operand za inkrement mora označavati promenljivu, element niza ili polje objekta unutrasnje klase.", statement);
    }

    public void visit(DesignatorStatementDec statement)
    {
        Obj tmp = statement.getDesignator().obj;
        if(tmp.getType() != Tab.intType)
            report_error("Dekrement se moze koristiti samo nad tipovima int.", statement);
        else if(tmp.getKind() != Obj.Var && tmp.getKind() != Obj.Fld && tmp.getKind() != Obj.Elem)
            report_error("Operand za dekrement mora označavati promenljivu, element niza ili polje objekta unutrasnje klase.", statement);
    }

    public void visit(DesignatorAssign statement)
    {
        Obj dst = statement.getDesignator().obj;
        Struct src = statement.getExpr().struct;
        if(!StructCompare.CompatibleTypesDuringAssignment(src, dst.getType()))
            report_error("Tipovi moraju biti kompatibilni prilikom dodele.", statement);
        else if(dst.getKind() != Obj.Var && dst.getKind() != Obj.Fld && dst.getKind() != Obj.Elem)
            report_error("Levi operan pri dodeli mora označavati promenljivu, element niza ili polje objekta unutrasnje klase.", statement);
    }

    public void visit(DesignatorStatementSuper statement)
    {
        if(currentClass == null)
        {
            report_error("Pokusaj poziva super moetode van klase.", statement);
            callParams = callParamsStack.size() != 0 ? callParamsStack.pop() : null;
            return;
        }
        if(!classObj.HasParent())
        {
            report_error("Pokusaj poziva super moetode u klasi koja ne extenduje drugu klasu.", statement);
            callParams = callParamsStack.size() != 0 ? callParamsStack.pop() : null;
            return;
        }
        if(method == null)
        {
            report_error("Trenutna metoda nije lepo deklarisana ne moze se zakljuciti da li postoji super metoda.", statement);
            callParams = callParamsStack.size() != 0 ? callParamsStack.pop() : null;
            return;
        }
        String methodNameToUse = methodObj.getName();
        boolean useParent = false;
        if(methodObj.getName().equals(classObj.getName()))
        {
            methodNameToUse = classObj.getParent().getName();
            useParent = true;
        }
        final String finalMethodNameToUse = methodNameToUse;
        List<MethodObj> tmpMethods;
        if(useParent)
            tmpMethods = classObj.getParent().getMethods().stream().filter(methodObj1 -> {
                return methodObj1.getName().equals(finalMethodNameToUse) && methodObj1.getReturnType() == methodObj.getReturnType();
            }).collect(Collectors.toList());
        else
            tmpMethods = classObj.getMethods().stream().filter(methodObj1 -> {
                return methodObj1.getName().equals(finalMethodNameToUse) && methodObj1.getReturnType() == methodObj.getReturnType();
            }).collect(Collectors.toList());
        if(tmpMethods.size() == 0)
            report_error("Pokusaj poziva super moetode koja ne postoji u parent klasi", statement);
        else if(!checkMethodParamsMatch(tmpMethods.get(0)))
            report_error("Neispravni parametri za poziv metode.", statement);
        else if(methodObj.getName().equals(classObj.getName()) && methodObj.getReturnType() == Tab.noType)
            report_info("Poziv konstruktora nadklase : " + getObjectPrint(tmpMethods.get(0).getMethod()), statement);
        callParams = callParamsStack.size() != 0 ? callParamsStack.pop() : null;
    }

    public void visit(DesignatorStatementAct statement)
    {
        Obj tmp = statement.getMakeNewActParmsListDesignator().getDesignator().obj;
        if(tmp.getKind() != Obj.Meth)
        {
            report_error(tmp.getName() + " nije metoda.", statement);
            callParams = callParamsStack.size() != 0 ? callParamsStack.pop() : null;
            return;
        }
        List<MethodObj> methodObjs = MethodObj.allMethods.stream().filter(methodObj1 -> {
            return methodObj1.getMethod() == tmp;
        }).collect(Collectors.toList());
        MethodObj tmpMethodObj = methodObjs.get(0);
        if(!checkMethodParamsMatch(tmpMethodObj))
            report_error("Parametri nisu dobri za poziv metode " + tmpMethodObj.getName(), statement);
        else if(tmpMethodObj.IsGlobal())
            report_info("Poziv globalne metode " + getObjectPrint(tmp), statement);
        else
            report_info("Poziv metode koja pripada klasi " + getObjectPrint(tmp), statement);
        callParams = callParamsStack.size() != 0 ? callParamsStack.pop() : null;
    }

    public void visit(DoForCheck doForCheck)
    {
        loop++;
    }

    public void visit(DoWhileStatement doWhileStatement)
    {
        loop--;
    }

    public void visit(BreakStatement breakStatement)
    {
        if(loop == 0)
            report_error("Break se moze koristiti samo unutar petlje.", breakStatement);
    }

    public void visit(ContinueStatement continueStatement)
    {
        if(loop == 0)
            report_error("Continue se moze koristiti samo unutar petlje.", continueStatement);
    }

    public void visit(ReturnNoExprStatement returnNoExprStatement)
    {
        returnCalled = true;
        if(returnTypeMeth != Tab.noType && returnTypeMeth != voidType)
            report_error("Ocekuje se povratna vrednost.", forError);
    }

    public void visit(ReturnExprStatement returnExprStatement)
    {
        returnCalled = true;
        if(methodObj.isConstructor())
            report_error("Return u konstruktoru ne moze vracati vrednost.", forError);
        else if(!StructCompare.EquivalentTypes(returnExprStatement.getExpr().struct, methodObj.getReturnType()))
            report_error("Povratna vrednost i ocekivana vrednost nisu ekvivalentni.", forError);
    }

    public void visit(ReadStatement readStatement)
    {
        Obj tmp = readStatement.getDesignator().obj;
        if(tmp.getKind() != Obj.Var && tmp.getKind() != Obj.Fld && tmp.getKind() != Obj.Elem)
            report_error("Operand mora oznacavati promenljivu, element niza ili polje unutar objekta.", readStatement);
        else if(tmp.getType() != Tab.intType && tmp.getType() != Tab.charType && tmp.getType() != boolType)
            report_error("Operand  mora biti tipa int, char ili bool.", readStatement);
    }

    public void visit(PrintNoNumStatement printNoNumStatement)
    {
        Struct tmp = printNoNumStatement.getExpr().struct;
        if(tmp != Tab.intType && tmp != Tab.charType && tmp != boolType)
            report_error("Operand  mora biti tipa int, char ili bool.", printNoNumStatement);
    }

    public void visit(PrintNumStatement printNumStatement)
    {
        Struct tmp = printNumStatement.getExpr().struct;
        if(tmp != Tab.intType && tmp != Tab.charType && tmp != boolType)
            report_error("Operand  mora biti tipa int, char ili bool.", printNumStatement);
    }
}