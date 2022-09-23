// generated with ast extension for cup
// version 0.8
// 23/7/2022 20:18:35


package rs.ac.bg.etf.pp1.ast;

public class VarDeclListNoErrorHasItems extends VarDeclListNoError {

    private VarDeclListNoError VarDeclListNoError;
    private VarDeclNoError VarDeclNoError;

    public VarDeclListNoErrorHasItems (VarDeclListNoError VarDeclListNoError, VarDeclNoError VarDeclNoError) {
        this.VarDeclListNoError=VarDeclListNoError;
        if(VarDeclListNoError!=null) VarDeclListNoError.setParent(this);
        this.VarDeclNoError=VarDeclNoError;
        if(VarDeclNoError!=null) VarDeclNoError.setParent(this);
    }

    public VarDeclListNoError getVarDeclListNoError() {
        return VarDeclListNoError;
    }

    public void setVarDeclListNoError(VarDeclListNoError VarDeclListNoError) {
        this.VarDeclListNoError=VarDeclListNoError;
    }

    public VarDeclNoError getVarDeclNoError() {
        return VarDeclNoError;
    }

    public void setVarDeclNoError(VarDeclNoError VarDeclNoError) {
        this.VarDeclNoError=VarDeclNoError;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(VarDeclListNoError!=null) VarDeclListNoError.accept(visitor);
        if(VarDeclNoError!=null) VarDeclNoError.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(VarDeclListNoError!=null) VarDeclListNoError.traverseTopDown(visitor);
        if(VarDeclNoError!=null) VarDeclNoError.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(VarDeclListNoError!=null) VarDeclListNoError.traverseBottomUp(visitor);
        if(VarDeclNoError!=null) VarDeclNoError.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("VarDeclListNoErrorHasItems(\n");

        if(VarDeclListNoError!=null)
            buffer.append(VarDeclListNoError.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(VarDeclNoError!=null)
            buffer.append(VarDeclNoError.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [VarDeclListNoErrorHasItems]");
        return buffer.toString();
    }
}
