// generated with ast extension for cup
// version 0.8
// 23/7/2022 20:18:35


package rs.ac.bg.etf.pp1.ast;

public class IfStatement extends SingleStatement {

    private IfForGen IfForGen;
    private IfConditionError IfConditionError;
    private Statement Statement;

    public IfStatement (IfForGen IfForGen, IfConditionError IfConditionError, Statement Statement) {
        this.IfForGen=IfForGen;
        if(IfForGen!=null) IfForGen.setParent(this);
        this.IfConditionError=IfConditionError;
        if(IfConditionError!=null) IfConditionError.setParent(this);
        this.Statement=Statement;
        if(Statement!=null) Statement.setParent(this);
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

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(IfForGen!=null) IfForGen.accept(visitor);
        if(IfConditionError!=null) IfConditionError.accept(visitor);
        if(Statement!=null) Statement.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(IfForGen!=null) IfForGen.traverseTopDown(visitor);
        if(IfConditionError!=null) IfConditionError.traverseTopDown(visitor);
        if(Statement!=null) Statement.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(IfForGen!=null) IfForGen.traverseBottomUp(visitor);
        if(IfConditionError!=null) IfConditionError.traverseBottomUp(visitor);
        if(Statement!=null) Statement.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("IfStatement(\n");

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

        buffer.append(tab);
        buffer.append(") [IfStatement]");
        return buffer.toString();
    }
}
