package rs.ac.bg.etf.pp1;

import rs.ac.bg.etf.pp1.ast.ConditionMultiple;
import rs.ac.bg.etf.pp1.ast.SyntaxNode;
import rs.etf.pp1.mj.runtime.Code;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class ConditionsSolver
{
    private List<Integer> jumpToSuccess;
    private List<Integer> jumpToNextCheckOrFailure;
    private int skipElseAdr;

    private List<Integer> continueJumps;
    private List<Integer> breakJumps;
    private int whileStartAdr;

    private boolean forLoopConditions;

    public ConditionsSolver(boolean forLoopConditions)
    {
        this.forLoopConditions = forLoopConditions;
        this.jumpToSuccess = new ArrayList<>();
        this.jumpToNextCheckOrFailure = new ArrayList<>();
        this.continueJumps = new ArrayList<>();
        this.breakJumps = new ArrayList<>();
        this.whileStartAdr = 0;
        this.skipElseAdr = 0;
    }

    private static Stack<ConditionsSolver> stack = new Stack<>();
    private static Stack<ConditionsSolver> whileStack = new Stack<>();
    private static ConditionsSolver currentSolver = null;
    private static List<Integer> currentContinueJumps = null;
    private static List<Integer> currentBreakJumps = null;

    public static void startNewIf()
    {
        if(currentSolver != null)
        {
            stack.push(currentSolver);
            if(currentSolver.forLoopConditions)
            {
                whileStack.push(currentSolver);
                currentContinueJumps = currentSolver.continueJumps;
                currentBreakJumps = currentSolver.breakJumps;
            }
        }
        currentSolver = new ConditionsSolver(false);
    }

    public static void startElse()
    {
        Code.putJump(0);
        currentSolver.skipElseAdr = Code.pc - 2;
        for(int address : currentSolver.jumpToNextCheckOrFailure)
            Code.fixup(address);
        currentSolver.jumpToNextCheckOrFailure.clear();
    }

    public static void endIf()
    {
        for(int address : currentSolver.jumpToNextCheckOrFailure)
            Code.fixup(address);
        currentSolver.jumpToNextCheckOrFailure.clear();
        currentSolver = stack.size() == 0 ? null : stack.pop();
        if(currentSolver != null && currentSolver.forLoopConditions)
        {
            whileStack.pop();
            currentBreakJumps = currentSolver.breakJumps;
            currentContinueJumps = currentSolver.continueJumps;
        }
    }

    public static void endIfElse()
    {
        Code.fixup(currentSolver.skipElseAdr);
        currentSolver = stack.size() == 0 ? null : stack.pop();
        if(currentSolver != null && currentSolver.forLoopConditions)
        {
            whileStack.pop();
            currentBreakJumps = currentSolver.breakJumps;
            currentContinueJumps = currentSolver.continueJumps;
        }
    }

    public static void startWhile()
    {
        if(currentSolver != null)
        {
            stack.push(currentSolver);
            if(currentSolver.forLoopConditions)
                whileStack.push(currentSolver);
        }
        currentSolver = new ConditionsSolver(true);
        currentSolver.whileStartAdr = Code.pc;
        currentBreakJumps = currentSolver.breakJumps;
        currentContinueJumps = currentSolver.continueJumps;
    }

    public static void resolveContinue()
    {
        for(int address : currentSolver.continueJumps)
            Code.fixup(address);
        currentSolver.continueJumps.clear();
    }

    public static void endWhile()
    {
        for(int address : currentSolver.jumpToNextCheckOrFailure)
            Code.fixup(address);
        currentSolver.jumpToNextCheckOrFailure.clear();
        for(int address : currentSolver.breakJumps)
            Code.fixup(address);
        currentSolver.breakJumps.clear();
        currentSolver = stack.size() == 0 ? null : stack.pop();
        if(currentSolver != null)
        {
            if(currentSolver.forLoopConditions)
            {
                whileStack.pop();
                currentBreakJumps = currentSolver.breakJumps;
                currentContinueJumps = currentSolver.continueJumps;
            }
            else
            {
                ConditionsSolver tmp = whileStack.peek();
                currentBreakJumps = tmp.breakJumps;
                currentContinueJumps = tmp.continueJumps;
            }
        }
    }

    public static void addBreak()
    {
        Code.putJump(0);
        currentBreakJumps.add(Code.pc - 2);
    }

    public static void addContinue()
    {
        Code.putJump(0);
        currentContinueJumps.add(Code.pc - 2);
    }

    public static void addJumpToNextCheck(int adr)
    {
        currentSolver.jumpToNextCheckOrFailure.add(adr);
    }

    private static void whileWhatToDo()
    {
        Code.putJump(currentSolver.whileStartAdr);
        for(int address : currentSolver.jumpToNextCheckOrFailure)
            Code.fixup(address);
        currentSolver.jumpToNextCheckOrFailure.clear();

    }

    private static void ifWhatToDo(SyntaxNode node)
    {
        if(node.getParent() instanceof ConditionMultiple)
        {
            Code.putJump(0);
            currentSolver.jumpToSuccess.add(Code.pc - 2);
            for(int address : currentSolver.jumpToNextCheckOrFailure)
                Code.fixup(address);
            currentSolver.jumpToNextCheckOrFailure.clear();
        }
        else
        {
            for(int address : currentSolver.jumpToSuccess)
                Code.fixup(address);
            currentSolver.jumpToSuccess.clear();
        }
    }

    public static void checkConditionWhatToDo(SyntaxNode node)
    {
        if(currentSolver.forLoopConditions)
            whileWhatToDo();
        else
            ifWhatToDo(node);
    }
}
