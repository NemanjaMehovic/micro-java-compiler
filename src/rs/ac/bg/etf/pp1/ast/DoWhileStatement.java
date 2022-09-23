// generated with ast extension for cup
// version 0.8
// 23/7/2022 20:18:35


package rs.ac.bg.etf.pp1.ast;

public class DoWhileStatement extends SingleStatement {

    private DoForCheck DoForCheck;
    private Statement Statement;
    private WhileForGen WhileForGen;
    private Condition Condition;

    public DoWhileStatement (DoForCheck DoForCheck, Statement Statement, WhileForGen WhileForGen, Condition Condition) {
        this.DoForCheck=DoForCheck;
        if(DoForCheck!=null) DoForCheck.setParent(this);
        this.Statement=Statement;
        if(Statement!=null) Statement.setParent(this);
        this.WhileForGen=WhileForGen;
        if(WhileForGen!=null) WhileForGen.setParent(this);
        this.Condition=Condition;
        if(Condition!=null) Condition.setParent(this);
    }

    public DoForCheck getDoForCheck() {
        return DoForCheck;
    }

    public void setDoForCheck(DoForCheck DoForCheck) {
        this.DoForCheck=DoForCheck;
    }

    public Statement getStatement() {
        return Statement;
    }

    public void setStatement(Statement Statement) {
        this.Statement=Statement;
    }

    public WhileForGen getWhileForGen() {
        return WhileForGen;
    }

    public void setWhileForGen(WhileForGen WhileForGen) {
        this.WhileForGen=WhileForGen;
    }

    public Condition getCondition() {
        return Condition;
    }

    public void setCondition(Condition Condition) {
        this.Condition=Condition;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(DoForCheck!=null) DoForCheck.accept(visitor);
        if(Statement!=null) Statement.accept(visitor);
        if(WhileForGen!=null) WhileForGen.accept(visitor);
        if(Condition!=null) Condition.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(DoForCheck!=null) DoForCheck.traverseTopDown(visitor);
        if(Statement!=null) Statement.traverseTopDown(visitor);
        if(WhileForGen!=null) WhileForGen.traverseTopDown(visitor);
        if(Condition!=null) Condition.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(DoForCheck!=null) DoForCheck.traverseBottomUp(visitor);
        if(Statement!=null) Statement.traverseBottomUp(visitor);
        if(WhileForGen!=null) WhileForGen.traverseBottomUp(visitor);
        if(Condition!=null) Condition.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("DoWhileStatement(\n");

        if(DoForCheck!=null)
            buffer.append(DoForCheck.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Statement!=null)
            buffer.append(Statement.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(WhileForGen!=null)
            buffer.append(WhileForGen.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Condition!=null)
            buffer.append(Condition.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [DoWhileStatement]");
        return buffer.toString();
    }
}
