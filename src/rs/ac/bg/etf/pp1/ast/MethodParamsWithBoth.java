// generated with ast extension for cup
// version 0.8
// 23/7/2022 20:18:35


package rs.ac.bg.etf.pp1.ast;

public class MethodParamsWithBoth extends MethodParams {

    private FormParsList FormParsList;
    private OptArgsList OptArgsList;

    public MethodParamsWithBoth (FormParsList FormParsList, OptArgsList OptArgsList) {
        this.FormParsList=FormParsList;
        if(FormParsList!=null) FormParsList.setParent(this);
        this.OptArgsList=OptArgsList;
        if(OptArgsList!=null) OptArgsList.setParent(this);
    }

    public FormParsList getFormParsList() {
        return FormParsList;
    }

    public void setFormParsList(FormParsList FormParsList) {
        this.FormParsList=FormParsList;
    }

    public OptArgsList getOptArgsList() {
        return OptArgsList;
    }

    public void setOptArgsList(OptArgsList OptArgsList) {
        this.OptArgsList=OptArgsList;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(FormParsList!=null) FormParsList.accept(visitor);
        if(OptArgsList!=null) OptArgsList.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(FormParsList!=null) FormParsList.traverseTopDown(visitor);
        if(OptArgsList!=null) OptArgsList.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(FormParsList!=null) FormParsList.traverseBottomUp(visitor);
        if(OptArgsList!=null) OptArgsList.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("MethodParamsWithBoth(\n");

        if(FormParsList!=null)
            buffer.append(FormParsList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(OptArgsList!=null)
            buffer.append(OptArgsList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [MethodParamsWithBoth]");
        return buffer.toString();
    }
}
