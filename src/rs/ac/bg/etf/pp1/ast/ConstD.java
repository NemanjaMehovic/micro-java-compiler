// generated with ast extension for cup
// version 0.8
// 23/7/2022 20:18:35


package rs.ac.bg.etf.pp1.ast;

public class ConstD extends ConstDecl {

    private ConstTypeName ConstTypeName;
    private ConstList ConstList;

    public ConstD (ConstTypeName ConstTypeName, ConstList ConstList) {
        this.ConstTypeName=ConstTypeName;
        if(ConstTypeName!=null) ConstTypeName.setParent(this);
        this.ConstList=ConstList;
        if(ConstList!=null) ConstList.setParent(this);
    }

    public ConstTypeName getConstTypeName() {
        return ConstTypeName;
    }

    public void setConstTypeName(ConstTypeName ConstTypeName) {
        this.ConstTypeName=ConstTypeName;
    }

    public ConstList getConstList() {
        return ConstList;
    }

    public void setConstList(ConstList ConstList) {
        this.ConstList=ConstList;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(ConstTypeName!=null) ConstTypeName.accept(visitor);
        if(ConstList!=null) ConstList.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ConstTypeName!=null) ConstTypeName.traverseTopDown(visitor);
        if(ConstList!=null) ConstList.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ConstTypeName!=null) ConstTypeName.traverseBottomUp(visitor);
        if(ConstList!=null) ConstList.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ConstD(\n");

        if(ConstTypeName!=null)
            buffer.append(ConstTypeName.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ConstList!=null)
            buffer.append(ConstList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ConstD]");
        return buffer.toString();
    }
}
