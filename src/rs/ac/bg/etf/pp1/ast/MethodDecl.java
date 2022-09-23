// generated with ast extension for cup
// version 0.8
// 23/7/2022 20:18:35


package rs.ac.bg.etf.pp1.ast;

public class MethodDecl implements SyntaxNode {

    private SyntaxNode parent;
    private int line;
    private MethodName MethodName;
    private MethodParams MethodParams;
    private MethodSigEnd MethodSigEnd;
    private VarDeclListNoError VarDeclListNoError;
    private StatementList StatementList;

    public MethodDecl (MethodName MethodName, MethodParams MethodParams, MethodSigEnd MethodSigEnd, VarDeclListNoError VarDeclListNoError, StatementList StatementList) {
        this.MethodName=MethodName;
        if(MethodName!=null) MethodName.setParent(this);
        this.MethodParams=MethodParams;
        if(MethodParams!=null) MethodParams.setParent(this);
        this.MethodSigEnd=MethodSigEnd;
        if(MethodSigEnd!=null) MethodSigEnd.setParent(this);
        this.VarDeclListNoError=VarDeclListNoError;
        if(VarDeclListNoError!=null) VarDeclListNoError.setParent(this);
        this.StatementList=StatementList;
        if(StatementList!=null) StatementList.setParent(this);
    }

    public MethodName getMethodName() {
        return MethodName;
    }

    public void setMethodName(MethodName MethodName) {
        this.MethodName=MethodName;
    }

    public MethodParams getMethodParams() {
        return MethodParams;
    }

    public void setMethodParams(MethodParams MethodParams) {
        this.MethodParams=MethodParams;
    }

    public MethodSigEnd getMethodSigEnd() {
        return MethodSigEnd;
    }

    public void setMethodSigEnd(MethodSigEnd MethodSigEnd) {
        this.MethodSigEnd=MethodSigEnd;
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
        if(MethodName!=null) MethodName.accept(visitor);
        if(MethodParams!=null) MethodParams.accept(visitor);
        if(MethodSigEnd!=null) MethodSigEnd.accept(visitor);
        if(VarDeclListNoError!=null) VarDeclListNoError.accept(visitor);
        if(StatementList!=null) StatementList.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(MethodName!=null) MethodName.traverseTopDown(visitor);
        if(MethodParams!=null) MethodParams.traverseTopDown(visitor);
        if(MethodSigEnd!=null) MethodSigEnd.traverseTopDown(visitor);
        if(VarDeclListNoError!=null) VarDeclListNoError.traverseTopDown(visitor);
        if(StatementList!=null) StatementList.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(MethodName!=null) MethodName.traverseBottomUp(visitor);
        if(MethodParams!=null) MethodParams.traverseBottomUp(visitor);
        if(MethodSigEnd!=null) MethodSigEnd.traverseBottomUp(visitor);
        if(VarDeclListNoError!=null) VarDeclListNoError.traverseBottomUp(visitor);
        if(StatementList!=null) StatementList.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("MethodDecl(\n");

        if(MethodName!=null)
            buffer.append(MethodName.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(MethodParams!=null)
            buffer.append(MethodParams.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(MethodSigEnd!=null)
            buffer.append(MethodSigEnd.toString("  "+tab));
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
        buffer.append(") [MethodDecl]");
        return buffer.toString();
    }
}
