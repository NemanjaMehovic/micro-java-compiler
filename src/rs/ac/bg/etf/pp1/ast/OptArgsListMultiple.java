// generated with ast extension for cup
// version 0.8
// 23/7/2022 20:18:35


package rs.ac.bg.etf.pp1.ast;

public class OptArgsListMultiple extends OptArgsList {

    private OptArgsList OptArgsList;
    private OptArgs OptArgs;

    public OptArgsListMultiple (OptArgsList OptArgsList, OptArgs OptArgs) {
        this.OptArgsList=OptArgsList;
        if(OptArgsList!=null) OptArgsList.setParent(this);
        this.OptArgs=OptArgs;
        if(OptArgs!=null) OptArgs.setParent(this);
    }

    public OptArgsList getOptArgsList() {
        return OptArgsList;
    }

    public void setOptArgsList(OptArgsList OptArgsList) {
        this.OptArgsList=OptArgsList;
    }

    public OptArgs getOptArgs() {
        return OptArgs;
    }

    public void setOptArgs(OptArgs OptArgs) {
        this.OptArgs=OptArgs;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(OptArgsList!=null) OptArgsList.accept(visitor);
        if(OptArgs!=null) OptArgs.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(OptArgsList!=null) OptArgsList.traverseTopDown(visitor);
        if(OptArgs!=null) OptArgs.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(OptArgsList!=null) OptArgsList.traverseBottomUp(visitor);
        if(OptArgs!=null) OptArgs.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("OptArgsListMultiple(\n");

        if(OptArgsList!=null)
            buffer.append(OptArgsList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(OptArgs!=null)
            buffer.append(OptArgs.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [OptArgsListMultiple]");
        return buffer.toString();
    }
}
