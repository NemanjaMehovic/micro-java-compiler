// generated with ast extension for cup
// version 0.8
// 23/7/2022 20:18:35


package rs.ac.bg.etf.pp1.ast;

public class VarDNoError extends VarDeclNoError {

    private Type Type;
    private VarListNoError VarListNoError;

    public VarDNoError (Type Type, VarListNoError VarListNoError) {
        this.Type=Type;
        if(Type!=null) Type.setParent(this);
        this.VarListNoError=VarListNoError;
        if(VarListNoError!=null) VarListNoError.setParent(this);
    }

    public Type getType() {
        return Type;
    }

    public void setType(Type Type) {
        this.Type=Type;
    }

    public VarListNoError getVarListNoError() {
        return VarListNoError;
    }

    public void setVarListNoError(VarListNoError VarListNoError) {
        this.VarListNoError=VarListNoError;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(Type!=null) Type.accept(visitor);
        if(VarListNoError!=null) VarListNoError.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Type!=null) Type.traverseTopDown(visitor);
        if(VarListNoError!=null) VarListNoError.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Type!=null) Type.traverseBottomUp(visitor);
        if(VarListNoError!=null) VarListNoError.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("VarDNoError(\n");

        if(Type!=null)
            buffer.append(Type.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(VarListNoError!=null)
            buffer.append(VarListNoError.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [VarDNoError]");
        return buffer.toString();
    }
}
