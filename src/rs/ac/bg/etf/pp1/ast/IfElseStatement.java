// generated with ast extension for cup
// version 0.8
// 23/7/2022 20:18:35


package rs.ac.bg.etf.pp1.ast;

public class IfElseStatement extends SingleStatement {

    private IfForGen IfForGen;
    private IfConditionError IfConditionError;
    private Statement Statement;
    private ElseForGen ElseForGen;
    private Statement Statement1;

    public IfElseStatement (IfForGen IfForGen, IfConditionError IfConditionError, Statement Statement, ElseForGen ElseForGen, Statement Statement1) {
        this.IfForGen=IfForGen;
        if(IfForGen!=null) IfForGen.setParent(this);
        this.IfConditionError=IfConditionError;
        if(IfConditionError!=null) IfConditionError.setParent(this);
        this.Statement=Statement;
        if(Statement!=null) Statement.setParent(this);
        this.ElseForGen=ElseForGen;
        if(ElseForGen!=null) ElseForGen.setParent(this);
        this.Statement1=Statement1;
        if(Statement1!=null) Statement1.setParent(this);
    }

    public IfForGen getIfForGen() {
        return IfForGen;
    }

    public void setIfForGen(IfForGen IfForGen) {
        this.IfForGen=IfForGen;
    }

    public IfConditionError getIfConditionError() {
        return IfConditionError;
    }

    public void setIfConditionError(IfConditionError IfConditionError) {
        this.IfConditionError=IfConditionError;
    }

    public Statement getStatement() {
        return Statement;
    }

    public void setStatement(Statement Statement) {
        this.Statement=Statement;
    }

    public ElseForGen getElseForGen() {
        return ElseForGen;
    }

    public void setElseForGen(ElseForGen ElseForGen) {
        this.ElseForGen=ElseForGen;
    }

    public Statement getStatement1() {
        return Statement1;
    }

    public void setStatement1(Statement Statement1) {
        this.Statement1=Statement1;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(IfForGen!=null) IfForGen.accept(visitor);
        if(IfConditionError!=null) IfConditionError.accept(visitor);
        if(Statement!=null) Statement.accept(visitor);
        if(ElseForGen!=null) ElseForGen.accept(visitor);
        if(Statement1!=null) Statement1.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(IfForGen!=null) IfForGen.traverseTopDown(visitor);
        if(IfConditionError!=null) IfConditionError.traverseTopDown(visitor);
        if(Statement!=null) Statement.traverseTopDown(visitor);
        if(ElseForGen!=null) ElseForGen.traverseTopDown(visitor);
        if(Statement1!=null) Statement1.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(IfForGen!=null) IfForGen.traverseBottomUp(visitor);
        if(IfConditionError!=null) IfConditionError.traverseBottomUp(visitor);
        if(Statement!=null) Statement.traverseBottomUp(visitor);
        if(ElseForGen!=null) ElseForGen.traverseBottomUp(visitor);
        if(Statement1!=null) Statement1.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("IfElseStatement(\n");

        if(IfForGen!=null)
            buffer.append(IfForGen.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(IfConditionError!=null)
            buffer.append(IfConditionError.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Statement!=null)
            buffer.append(Statement.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ElseForGen!=null)
            buffer.append(ElseForGen.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Statement1!=null)
            buffer.append(Statement1.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [IfElseStatement]");
        return buffer.toString();
    }
}
