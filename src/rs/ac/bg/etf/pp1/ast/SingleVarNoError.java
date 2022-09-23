// generated with ast extension for cup
// version 0.8
// 23/7/2022 20:18:35


package rs.ac.bg.etf.pp1.ast;

public class SingleVarNoError extends VarListNoError {

    private VarTypeNoError VarTypeNoError;

    public SingleVarNoError (VarTypeNoError VarTypeNoError) {
        this.VarTypeNoError=VarTypeNoError;
        if(VarTypeNoError!=null) VarTypeNoError.setParent(this);
    }

    public VarTypeNoError getVarTypeNoError() {
        return VarTypeNoError;
    }

    public void setVarTypeNoError(VarTypeNoError VarTypeNoError) {
        this.VarTypeNoError=VarTypeNoError;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(VarTypeNoError!=null) VarTypeNoError.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(VarTypeNoError!=null) VarTypeNoError.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(VarTypeNoError!=null) VarTypeNoError.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("SingleVarNoError(\n");

        if(VarTypeNoError!=null)
            buffer.append(VarTypeNoError.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [SingleVarNoError]");
        return buffer.toString();
    }
}
