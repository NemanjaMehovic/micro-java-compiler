// generated with ast extension for cup
// version 0.8
// 23/7/2022 20:18:35


package rs.ac.bg.etf.pp1.ast;

public class DesignatorStatementAssign extends DesignatorStatement {

    private DesignatorAssignOp DesignatorAssignOp;

    public DesignatorStatementAssign (DesignatorAssignOp DesignatorAssignOp) {
        this.DesignatorAssignOp=DesignatorAssignOp;
        if(DesignatorAssignOp!=null) DesignatorAssignOp.setParent(this);
    }

    public DesignatorAssignOp getDesignatorAssignOp() {
        return DesignatorAssignOp;
    }

    public void setDesignatorAssignOp(DesignatorAssignOp DesignatorAssignOp) {
        this.DesignatorAssignOp=DesignatorAssignOp;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(DesignatorAssignOp!=null) DesignatorAssignOp.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(DesignatorAssignOp!=null) DesignatorAssignOp.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(DesignatorAssignOp!=null) DesignatorAssignOp.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("DesignatorStatementAssign(\n");

        if(DesignatorAssignOp!=null)
            buffer.append(DesignatorAssignOp.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [DesignatorStatementAssign]");
        return buffer.toString();
    }
}
