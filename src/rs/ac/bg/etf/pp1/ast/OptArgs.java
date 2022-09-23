// generated with ast extension for cup
// version 0.8
// 23/7/2022 20:18:35


package rs.ac.bg.etf.pp1.ast;

public class OptArgs implements SyntaxNode {

    private SyntaxNode parent;
    private int line;
    private Type Type;
    private OptArgsTypes OptArgsTypes;

    public OptArgs (Type Type, OptArgsTypes OptArgsTypes) {
        this.Type=Type;
        if(Type!=null) Type.setParent(this);
        this.OptArgsTypes=OptArgsTypes;
        if(OptArgsTypes!=null) OptArgsTypes.setParent(this);
    }

    public Type getType() {
        return Type;
    }

    public void setType(Type Type) {
        this.Type=Type;
    }

    public OptArgsTypes getOptArgsTypes() {
        return OptArgsTypes;
    }

    public void setOptArgsTypes(OptArgsTypes OptArgsTypes) {
        this.OptArgsTypes=OptArgsTypes;
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
        if(Type!=null) Type.accept(visitor);
        if(OptArgsTypes!=null) OptArgsTypes.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Type!=null) Type.traverseTopDown(visitor);
        if(OptArgsTypes!=null) OptArgsTypes.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Type!=null) Type.traverseBottomUp(visitor);
        if(OptArgsTypes!=null) OptArgsTypes.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("OptArgs(\n");

        if(Type!=null)
            buffer.append(Type.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(OptArgsTypes!=null)
            buffer.append(OptArgsTypes.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [OptArgs]");
        return buffer.toString();
    }
}
