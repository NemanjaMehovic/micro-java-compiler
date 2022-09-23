package rs.ac.bg.etf.pp1;

import org.apache.log4j.Logger;
import rs.ac.bg.etf.pp1.ast.*;
import rs.etf.pp1.mj.runtime.Code;
import rs.etf.pp1.symboltable.Tab;
import rs.etf.pp1.symboltable.concepts.Obj;
import rs.etf.pp1.symboltable.concepts.Struct;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

public class CodeGenerator extends VisitorAdaptor
{

    private static final HashMap<Object, Integer> mulOpMap;
    private static final HashMap<Object, Integer> addOpMap;
    private static final HashMap<Object, Integer> relOpMap;

    static
    {
        mulOpMap = new HashMap<>();
        mulOpMap.put(MulOpStar.class, Code.mul);
        mulOpMap.put(MulOpPercent.class, Code.rem);
        mulOpMap.put(MulOpSlash.class, Code.div);

        addOpMap = new HashMap<>();
        addOpMap.put(AddOpPlus.class, Code.add);
        addOpMap.put(AddOpMinus.class, Code.sub);

        relOpMap = new HashMap<>();
        relOpMap.put(RelOpEqu.class, Code.eq);
        relOpMap.put(RelOpNeq.class, Code.ne);
        relOpMap.put(RelOpGtr.class, Code.gt);
        relOpMap.put(RelOpGeq.class, Code.ge);
        relOpMap.put(RelOpLss.class, Code.lt);
        relOpMap.put(RelOpLeq.class, Code.le);
    }

    private final Logger log = Logger.getLogger(getClass());
    private int dataSize = 0;
    private int startProgramAddress = 0;
    private int vft = 0;
    private List<Integer> vftData = new ArrayList<>();
    private Stack<Integer> paramsStack = new Stack<>();
    private int numOfParmsUsedInCall = 0;
    private MethodObj currentMethod = null;

    public int getStartProgramAddress()
    {
        return startProgramAddress;
    }

    public int getDataSize()
    {
        return dataSize;
    }

    public void setDataSize(int dataSize)
    {
        this.dataSize = dataSize;
    }

    public int getVft()
    {
        return vft;
    }

    public void setVft(int vft)
    {
        this.vft = vft;
    }

    public boolean generate(String fileName)
    {
        File tmpFile = new File(fileName);
        if(tmpFile.exists())
            tmpFile.delete();
        try(FileOutputStream stream = new FileOutputStream(tmpFile);)
        {
            Code.write(stream);
            return true;
        }
        catch(Exception e)
        {
            log.error(e.getMessage());
            return false;
        }
    }

    public void visit(SingleDesignator singleDesignator)
    {
        MethodObj tmp = null;
        if(singleDesignator.obj.getKind() == Obj.Meth)
            tmp = MethodObj.allMethods.stream().filter(methodObj -> { return methodObj.getMethod() == singleDesignator.obj;}).collect(Collectors.toList()).get(0);
        //Ako promenljvia/metoda pripada klasi moramo ucitati klasu/this sto je uvek prvi parametar u metodi
        if(singleDesignator.obj.getKind() == Obj.Fld || (tmp != null  && !tmp.IsGlobal()))
            Code.put(Code.load_n);
        if(singleDesignator.getParent() instanceof Designator)
            Code.load(singleDesignator.obj);
    }

    public void visit(DesignatorThis designatorThis)
    {
        if(designatorThis.getParent() instanceof Designator)
            Code.load(designatorThis.obj);
    }

    public void visit(DesignatorMultiple designatorMultiple)
    {
        if(designatorMultiple.getParent() instanceof Designator)
            Code.load(designatorMultiple.obj);
    }

    public void visit(DesignatorArray designatorArray)
    {
        if(designatorArray.getParent() instanceof Designator)
            Code.load(designatorArray.obj);
    }

    public void visit(NumberFactor numberFactor)
    {
        Code.loadConst(numberFactor.getN1());
    }

    public void visit(CharFactor charFactor)
    {
        Code.loadConst(charFactor.getC1());
    }

    public void visit(BoolFactor boolFactor)
    {
        Code.loadConst(boolFactor.getB1() ? 1 : 0);
    }

    private void superFunctionCall(ActParsList parsList)
    {
        ClassObj parent = currentMethod.getOwner().getParent();//mora da postoji semantika bi uhvatila suprutno
        MethodObj found = null;
        for(MethodObj parentMethod : parent.getMethods())
            if((currentMethod.isConstructor() && parentMethod.isConstructor()) || (!currentMethod.isConstructor() && currentMethod.getName().equals(parentMethod.getName())))
            {
                found = parentMethod;
                break;
            }

        for(int i = 0; i < numOfParmsUsedInCall; i++)
            Code.put(Code.pop);
        Code.put(Code.load_n);
        CodeGenerator tmpGen = new CodeGenerator();
        parsList.traverseBottomUp(tmpGen);
        int tmpNumOfParmsUsedInCall = tmpGen.numOfParmsUsedInCall;

        if(tmpNumOfParmsUsedInCall < (found.getParams().size() + found.getOptParams().size()))
        {
            int tmp = tmpNumOfParmsUsedInCall - found.getParams().size();
            for(int i = tmp; i < found.getOptParams().size(); i++)
            {
                MethodObj.Pair<Obj, Integer> pair = found.getOptParams().get(i);
                Code.loadConst(pair.getValue());
            }
        }

        Code.loadConst(parent.getThisClassObj().getAdr());
        Code.put(Code.invokevirtual);
        for(char c : found.getName().toCharArray())
            Code.put4(c);
        Code.put4(-1);
    }

    public void visit(FactorSuper factorSuper)
    {
        superFunctionCall(factorSuper.getActParsList());
        numOfParmsUsedInCall = paramsStack.size() != 0 ? paramsStack.pop() : 0;
    }

    public void visit(DesignatorFactor designatorFactor)
    {
        Code.load(designatorFactor.getDesignator().obj);
    }

    private void functionCall(MethodObj method, Designator designator)
    {
        if(method.getMethod() == Tab.lenObj)
        {
            Code.put(Code.arraylength);
            return;
        }
        if(method.getMethod() == Tab.ordObj || method.getMethod() == Tab.chrObj)
            return;

        if(numOfParmsUsedInCall < (method.getParams().size() + method.getOptParams().size()))
        {
            int tmp = numOfParmsUsedInCall - method.getParams().size();
            for(int i = tmp; i < method.getOptParams().size(); i++)
            {
                MethodObj.Pair<Obj, Integer> pair = method.getOptParams().get(i);
                Code.loadConst(pair.getValue());
            }
        }

        if(method.IsGlobal())
        {
            int o = method.getMethod().getAdr() - Code.pc;
            Code.put(Code.call);
            Code.put2(o);
            return;
        }

        designator.traverseBottomUp(new CodeGenerator());
        Code.put(Code.getfield);
        Code.put2(0);
        Code.put(Code.invokevirtual);
        for(char c : method.getName().toCharArray())
            Code.put4(c);
        Code.put4(-1);
    }

    public void visit(DesignatorActParsFactor factor)
    {
        Obj tmp = factor.getMakeNewActParmsListDesignator().getDesignator().obj;
        List<MethodObj> list = MethodObj.allMethods.stream().filter(methodObj -> {
            return methodObj.getMethod() == tmp;
        }).collect(Collectors.toList());
        functionCall(list.get(0), factor.getMakeNewActParmsListDesignator().getDesignator());
        numOfParmsUsedInCall = paramsStack.size() != 0 ? paramsStack.pop() : 0;
    }

    public void visit(TypeFactor typeFactor)
    {
        Obj tmp = typeFactor.getType().obj;
        List<ClassObj> list = ClassObj.getClasses().stream().filter(classObj -> {
            return classObj.getThisClassObj() == tmp;
        }).collect(Collectors.toList());
        ClassObj classObj = list.get(0);//uvek treba da postoji
        Code.put(Code.new_);
        Code.put2(classObj.getFields().size() * 4 + 4);
        Code.put(Code.dup);
        Code.loadConst(classObj.getThisClassObj().getAdr());
        MethodObj constructor = null;
        for(MethodObj methodObj : classObj.getMethods())
            if(methodObj.isConstructor())
            {
                constructor = methodObj;
                Code.put(Code.dup2);
                break;
            }
        Code.put(Code.putfield);
        Code.put2(0);
        if(constructor == null)
            return;
        Code.put(Code.invokevirtual);
        for(char c : constructor.getName().toCharArray())
            Code.put4(c);
        Code.put4(-1);
    }

    public void visit(TypeExprFactor typeExprFactor)
    {
        Code.put(Code.newarray);
        Code.put(typeExprFactor.struct.getElemType() == Tab.charType ? 0 : 1);
    }

    public void visit(ActParsMultiple actParsMultiple)
    {
        numOfParmsUsedInCall++;
    }

    public void visit(ActParsSingle actParsSingle)
    {
        numOfParmsUsedInCall++;
    }

    private void newParamsList()
    {
        if(numOfParmsUsedInCall > 0)
            paramsStack.push(numOfParmsUsedInCall);
        numOfParmsUsedInCall = 0;
    }

    public void visit(MakeNewActParmsListSuper notImportant)
    {
        newParamsList();
    }

    public void visit(MakeNewActParmsListDesignator notImportant)
    {
        newParamsList();
    }

    public void visit(TermMultiple termMultiple)
    {
        Code.put(mulOpMap.get(termMultiple.getMulop().getClass()));
    }

    public void visit(SingleNegExpr negExpr)
    {
        Code.put(Code.neg);
    }

    public void visit(ExprMultiple exprMultiple)
    {
        Code.put(addOpMap.get(exprMultiple.getAddop().getClass()));
    }

    public void visit(DesignatorAssign assign)
    {
        Code.store(assign.getDesignator().obj);
    }

    private void incDecFunction(Obj param, Designator designator, int code)
    {
        designator.traverseBottomUp(new CodeGenerator());//Mora da ucitamo ponovo adresu promenljive da bih smo je iskoristili za racunanje i storovanje
        Code.load(param);
        Code.loadConst(1);
        Code.put(code);
        Code.store(param);
    }

    public void visit(DesignatorStatementInc inc)
    {
        incDecFunction(inc.getDesignator().obj, inc.getDesignator(), Code.add);
    }

    public void visit(DesignatorStatementDec dec)
    {
        incDecFunction(dec.getDesignator().obj, dec.getDesignator(), Code.sub);
    }

    public void visit(DesignatorStatementSuper designatorStatementSuper)
    {
        superFunctionCall(designatorStatementSuper.getActParsList());
        if(!currentMethod.isConstructor() && currentMethod.getReturnType() != SemanticAnalyzer.voidType)
            Code.put(Code.pop);
        numOfParmsUsedInCall = paramsStack.size() != 0 ? paramsStack.pop() : 0;
    }

    public void visit(DesignatorStatementAct designatorStatement)
    {
        Obj tmp = designatorStatement.getMakeNewActParmsListDesignator().getDesignator().obj;
        List<MethodObj> list = MethodObj.allMethods.stream().filter(methodObj -> {
            return methodObj.getMethod() == tmp;
        }).collect(Collectors.toList());
        functionCall(list.get(0), designatorStatement.getMakeNewActParmsListDesignator().getDesignator());
        if(!list.get(0).isConstructor() && list.get(0).getReturnType() != SemanticAnalyzer.voidType)
            Code.put(Code.pop);
        numOfParmsUsedInCall = paramsStack.size() != 0 ? paramsStack.pop() : 0;
    }

    public void visit(ClassDecl classDecl)
    {
        Obj tmp = classDecl.getClassName().obj;
        List<ClassObj> list = ClassObj.getClasses().stream().filter(classObj -> {
            return classObj.getThisClassObj() == tmp;
        }).collect(Collectors.toList());
        ClassObj classObj = list.get(0);//uvek treba da postoji
        classObj.getThisClassObj().setAdr(dataSize);
        for(MethodObj methodObj : classObj.getMethods())
        {
            for(char c : methodObj.getName().toCharArray())
            {
                vftData.add((int) c);
                dataSize++;
            }
            vftData.add(-1);
            dataSize++;
            if(classObj.HasParent() && methodObj.getMethod().getAdr() == 0)//mora has parent u slucaju da je klasa i metoda prva stvar u data
            {
                ClassObj parent = classObj.getParent();
                MethodObj found = null;
                for(MethodObj parentMethod : parent.getMethods())
                    if((methodObj.isConstructor() && parentMethod.isConstructor()) || (!methodObj.isConstructor() && methodObj.getName().equals(parentMethod.getName())))
                    {
                        found = parentMethod;
                        break;
                    }
                methodObj.getMethod().setAdr(found.getMethod().getAdr());//found nikad ne bi trebalo da je null
            }
            vftData.add(methodObj.getMethod().getAdr());
            dataSize++;
        }
        vftData.add(-2);
        dataSize++;
    }

    public void visit(MethodSigEnd methodSigEnd)
    {
        Obj tmp = methodSigEnd.obj;
        List<MethodObj> list = MethodObj.allMethods.stream().filter(methodObj -> {
            return methodObj.getMethod() == tmp;
        }).collect(Collectors.toList());
        MethodObj methodObj = list.get(0);
        currentMethod = methodObj;
        if(methodObj.IsGlobal() && methodObj.getName().equals("main"))
        {
            startProgramAddress = Code.pc;
            for(int val : vftData)
            {
                Code.loadConst(val);
                Code.put(Code.putstatic);
                Code.put2(vft++);
            }
        }
        tmp.setAdr(Code.pc);
        int paramCount = methodObj.getParams().size() + methodObj.getOptParams().size() + (!methodObj.IsGlobal() ? 1 : 0);
        int varCount = methodObj.getLocalVarCount();
        Code.put(Code.enter);
        Code.put(paramCount);
        Code.put(paramCount + varCount);
    }

    private void returnCode()
    {
        Code.put(Code.exit);
        Code.put(Code.return_);
    }

    public void visit(MethodDecl methodDecl)
    {
        if(currentMethod.getReturnType() == SemanticAnalyzer.voidType)
        {
            returnCode();
            currentMethod = null;
            return;
        }
        //Moze samo da se desi ako je return u neki if
        Code.put(Code.trap);//Ako metoda treba da vraca vrednost i nije izasla kroz return baci gresku
        Code.put(0);
        currentMethod = null;
    }

    public void visit(ConstructorName constructorName)
    {
        String name = constructorName.getName();
        List<ClassObj> list = ClassObj.getClasses().stream().filter(classObj -> {
            return classObj.getName().equals(name);
        }).collect(Collectors.toList());
        ClassObj classObj = list.get(0);
        MethodObj constructor = null;
        for(MethodObj method : classObj.getMethods())
            if(method.isConstructor())
            {
                constructor = method;
                currentMethod = method;
                break;
            }
        constructor.getMethod().setAdr(Code.pc);
        Code.put(Code.enter);
        Code.put(1);
        Code.put(constructor.getLocalVarCount() + 1);
    }

    public void visit(ConstructorDecl constructorDecl)
    {
        returnCode();
        currentMethod = null;
    }

    public void visit(ReturnNoExprStatement returnNoExprStatement)
    {
        returnCode();
    }

    public void visit(ReturnExprStatement returnExprStatement)
    {
        returnCode();
    }

    public void visit(BreakStatement breakStatement)
    {
        ConditionsSolver.addBreak();
    }

    public void visit(ContinueStatement continueStatement)
    {
        ConditionsSolver.addContinue();
    }

    public void visit(IfForGen ifForGen)
    {
        ConditionsSolver.startNewIf();
    }

    public void visit(IfStatement ifStatement)
    {
        ConditionsSolver.endIf();
    }

    public void visit(ElseForGen elseForGen)
    {
        ConditionsSolver.startElse();
    }

    public void visit(IfElseStatement ifElseStatement)
    {
        ConditionsSolver.endIfElse();
    }

    public void visit(DoForCheck doForCheck)
    {
        ConditionsSolver.startWhile();
    }

    public void visit(WhileForGen whileForGen)
    {
        ConditionsSolver.resolveContinue();
    }

    public void visit(DoWhileStatement doWhileStatement)
    {
        ConditionsSolver.endWhile();
    }

    public void visit(CondFactNoRelop condFactNoRelop)
    {
        Code.loadConst(1);
        Code.putFalseJump(Code.eq, 0);
        ConditionsSolver.addJumpToNextCheck(Code.pc - 2);
    }

    public void visit(CondFactRelop condFactRelop)
    {
        Code.putFalseJump(relOpMap.get(condFactRelop.getRelop().getClass()), 0);
        ConditionsSolver.addJumpToNextCheck(Code.pc - 2);
    }

    public void visit(SingleCondition singleCondition)
    {
        ConditionsSolver.checkConditionWhatToDo(singleCondition);
    }

    public void visit(ConditionMultiple conditionMultiple)
    {
        ConditionsSolver.checkConditionWhatToDo(conditionMultiple);
    }

    public void visit(ReadStatement readStatement)
    {
        Obj tmp = readStatement.getDesignator().obj;
        if(tmp.getType() == SemanticAnalyzer.boolType)
        {
            Code.put(Code.read);
            Code.loadConst(0);
            Code.putFalseJump(Code.eq, 0);
            int adrToFix = Code.pc - 2;

            Code.loadConst(0);
            Code.store(tmp);
            Code.putJump(0);
            Code.fixup(adrToFix);

            adrToFix = Code.pc - 2;
            Code.loadConst(1);
            Code.store(tmp);
            Code.fixup(adrToFix);
        }
        else if(tmp.getType() == Tab.charType)
        {
            Code.put(Code.bread);
            Code.store(tmp);
        }
        else
        {
            Code.put(Code.read);
            Code.store(tmp);
        }
    }

    private void print(int pad, Struct tmp)
    {
        Code.loadConst(pad);
        if(tmp == Tab.intType || tmp == SemanticAnalyzer.boolType)
            Code.put(Code.print);
        else
            Code.put(Code.bprint);
    }

    public void visit(PrintNumStatement printNumStatement)
    {
        print(printNumStatement.getN2(), printNumStatement.getExpr().struct);
    }

    public void visit(PrintNoNumStatement printNoNumStatement)
    {
        print(1, printNoNumStatement.getExpr().struct);
    }
}
