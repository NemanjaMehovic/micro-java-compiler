// generated with ast extension for cup
// version 0.8
// 23/7/2022 20:18:35


package rs.ac.bg.etf.pp1.ast;

public class ClassDecl implements SyntaxNode {

    private SyntaxNode parent;
    private int line;
    private ClassName ClassName;
    private ClassExtends ClassExtends;
    private VarDeclList VarDeclList;
    private ClassMethodsBody ClassMethodsBody;

    public ClassDecl (ClassName ClassName, ClassExtends ClassExtends, VarDeclList VarDeclList, ClassMethodsBody ClassMethodsBody) {
        this.ClassName=ClassName;
        if(ClassName!=null) ClassName.setParent(this);
        this.ClassExtends=ClassExtends;
        if(ClassExtends!=null) ClassExtends.setParent(this);
        this.VarDeclList=VarDeclList;
        if(VarDeclList!=null) VarDeclList.setParent(this);
        this.ClassMethodsBody=ClassMethodsBody;
        if(ClassMethodsBody!=null) ClassMethodsBody.setParent(this);
    }

    public ClassName getClassName() {
        return ClassName;
    }

    public void setClassName(ClassName ClassName) {
        this.ClassName=ClassName;
    }

    public ClassExtends getClassExtends() {
        return ClassExtends;
    }

    public void setClassExtends(ClassExtends ClassExtends) {
        this.ClassExtends=ClassExtends;
    }

    public VarDeclList getVarDeclList() {
        return VarDeclList;
    }

    public void setVarDeclList(VarDeclList VarDeclList) {
        this.VarDeclList=VarDeclList;
    }

    public ClassMethodsBody getClassMethodsBody() {
        return ClassMethodsBody;
    }

    public void setClassMethodsBody(ClassMethodsBody ClassMethodsBody) {
        this.ClassMethodsBody=ClassMethodsBody;
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
        if(ClassName!=null) ClassName.accept(visitor);
        if(ClassExtends!=null) ClassExtends.accept(visitor);
        if(VarDeclList!=null) VarDeclList.accept(visitor);
        if(ClassMethodsBody!=null) ClassMethodsBody.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ClassName!=null) ClassName.traverseTopDown(visitor);
        if(ClassExtends!=null) ClassExtends.traverseTopDown(visitor);
        if(VarDeclList!=null) VarDeclList.traverseTopDown(visitor);
        if(ClassMethodsBody!=null) ClassMethodsBody.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ClassName!=null) ClassName.traverseBottomUp(visitor);
        if(ClassExtends!=null) ClassExtends.traverseBottomUp(visitor);
        if(VarDeclList!=null) VarDeclList.traverseBottomUp(visitor);
        if(ClassMethodsBody!=null) ClassMethodsBody.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ClassDecl(\n");

        if(ClassName!=null)
            buffer.append(ClassName.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ClassExtends!=null)
            buffer.append(ClassExtends.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(VarDeclList!=null)
            buffer.append(VarDeclList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ClassMethodsBody!=null)
            buffer.append(ClassMethodsBody.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ClassDecl]");
        return buffer.toString();
    }
}
