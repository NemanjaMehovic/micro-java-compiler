// generated with ast extension for cup
// version 0.8
// 23/7/2022 20:18:35


package rs.ac.bg.etf.pp1.ast;

public class DesignatorStatementAct extends DesignatorStatement {

    private MakeNewActParmsListDesignator MakeNewActParmsListDesignator;
    private ActParsList ActParsList;

    public DesignatorStatementAct (MakeNewActParmsListDesignator MakeNewActParmsListDesignator, ActParsList ActParsList) {
        this.MakeNewActParmsListDesignator=MakeNewActParmsListDesignator;
        if(MakeNewActParmsListDesignator!=null) MakeNewActParmsListDesignator.setParent(this);
        this.ActParsList=ActParsList;
        if(ActParsList!=null) ActParsList.setParent(this);
    }

    public MakeNewActParmsListDesignator getMakeNewActParmsListDesignator() {
        return MakeNewActParmsListDesignator;
    }

    public void setMakeNewActParmsListDesignator(MakeNewActParmsListDesignator MakeNewActParmsListDesignator) {
        this.MakeNewActParmsListDesignator=MakeNewActParmsListDesignator;
    }

    public ActParsList getActParsList() {
        return ActParsList;
    }

    public void setActParsList(ActParsList ActParsList) {
        this.ActParsList=ActParsList;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(MakeNewActParmsListDesignator!=null) MakeNewActParmsListDesignator.accept(visitor);
        if(ActParsList!=null) ActParsList.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(MakeNewActParmsListDesignator!=null) MakeNewActParmsListDesignator.traverseTopDown(visitor);
        if(ActParsList!=null) ActParsList.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(MakeNewActParmsListDesignator!=null) MakeNewActParmsListDesignator.traverseBottomUp(visitor);
        if(ActParsList!=null) ActParsList.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("DesignatorStatementAct(\n");

        if(MakeNewActParmsListDesignator!=null)
            buffer.append(MakeNewActParmsListDesignator.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ActParsList!=null)
            buffer.append(ActParsList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [DesignatorStatementAct]");
        return buffer.toString();
    }
}
