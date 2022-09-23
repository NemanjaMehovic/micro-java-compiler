// generated with ast extension for cup
// version 0.8
// 23/7/2022 20:18:35


package rs.ac.bg.etf.pp1.ast;

public class ConstructorDecl implements SyntaxNode {

    private SyntaxNode parent;
    private int line;
    private ConstructorName ConstructorName;
    private VarDeclListNoError VarDeclListNoError;
    private StatementList StatementList;

    public ConstructorDecl (ConstructorName ConstructorName, VarDeclListNoError VarDeclListNoError, StatementList StatementList) {
        this.ConstructorName=ConstructorName;
        if(ConstructorName!=null) ConstructorName.setParent(this);
        this.VarDeclListNoError=VarDeclListNoError;
        if(VarDeclListNoError!=null) VarDeclListNoError.setParent(this);
        this.StatementList=StatementList;
        if(StatementList!=null) StatementList.setParent(this);
    }

    public ConstructorName getConstructorName() {
        return ConstructorName;
    }

    public void setConstructorName(ConstructorName ConstructorName) {
        this.ConstructorName=ConstructorName;
    }

    public VarDeclListNoError getVarDeclListNoError() {
        return VarDeclListNoError;
    }

    public void setVarDeclListNoError(VarDeclListNoError VarDeclListNoError) {
        this.VarDeclListNoError=VarDeclListNoError;
    }

    public StatementList getStatementList() {
        return StatementList;
    }

    public void setStatementList(StatementList StatementList) {
        this.StatementList=StatementList;
    }

    public SyntaxNode getParent() {
        return parent;
    }

    public void setParent(SyntaxNode parent) {
        this.parent=parent;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line=line;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(ConstructorName!=null) ConstructorName.accept(visitor);
        if(VarDeclListNoError!=null) VarDeclListNoError.accept(visitor);
        if(StatementList!=null) StatementList.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ConstructorName!=null) ConstructorName.traverseTopDown(visitor);
        if(VarDeclListNoError!=null) VarDeclListNoError.traverseTopDown(visitor);
        if(StatementList!=null) StatementList.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ConstructorName!=null) ConstructorName.traverseBottomUp(visitor);
        if(VarDeclListNoError!=null) VarDeclListNoError.traverseBottomUp(visitor);
        if(StatementList!=null) StatementList.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ConstructorDecl(\n");

        if(ConstructorName!=null)
            buffer.append(ConstructorName.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(VarDeclListNoError!=null)
            buffer.append(VarDeclListNoError.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(StatementList!=null)
            buffer.append(StatementList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ConstructorDecl]");
        return buffer.toString();
    }
}
