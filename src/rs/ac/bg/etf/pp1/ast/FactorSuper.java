// generated with ast extension for cup
// version 0.8
// 23/7/2022 20:18:35


package rs.ac.bg.etf.pp1.ast;

public class FactorSuper extends Factor {

    private MakeNewActParmsListSuper MakeNewActParmsListSuper;
    private ActParsList ActParsList;

    public FactorSuper (MakeNewActParmsListSuper MakeNewActParmsListSuper, ActParsList ActParsList) {
        this.MakeNewActParmsListSuper=MakeNewActParmsListSuper;
        if(MakeNewActParmsListSuper!=null) MakeNewActParmsListSuper.setParent(this);
        this.ActParsList=ActParsList;
        if(ActParsList!=null) ActParsList.setParent(this);
    }

    public MakeNewActParmsListSuper getMakeNewActParmsListSuper() {
        return MakeNewActParmsListSuper;
    }

    public void setMakeNewActParmsListSuper(MakeNewActParmsListSuper MakeNewActParmsListSuper) {
        this.MakeNewActParmsListSuper=MakeNewActParmsListSuper;
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
        if(MakeNewActParmsListSuper!=null) MakeNewActParmsListSuper.accept(visitor);
        if(ActParsList!=null) ActParsList.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(MakeNewActParmsListSuper!=null) MakeNewActParmsListSuper.traverseTopDown(visitor);
        if(ActParsList!=null) ActParsList.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(MakeNewActParmsListSuper!=null) MakeNewActParmsListSuper.traverseBottomUp(visitor);
        if(ActParsList!=null) ActParsList.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("FactorSuper(\n");

        if(MakeNewActParmsListSuper!=null)
            buffer.append(MakeNewActParmsListSuper.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ActParsList!=null)
            buffer.append(ActParsList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [FactorSuper]");
        return buffer.toString();
    }
}
