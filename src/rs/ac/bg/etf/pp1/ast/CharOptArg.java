// generated with ast extension for cup
// version 0.8
// 23/7/2022 20:18:35


package rs.ac.bg.etf.pp1.ast;

public class CharOptArg extends OptArgsTypes {

    private String Name;
    private Character Val;

    public CharOptArg (String Name, Character Val) {
        this.Name=Name;
        this.Val=Val;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name=Name;
    }

    public Character getVal() {
        return Val;
    }

    public void setVal(Character Val) {
        this.Val=Val;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("CharOptArg(\n");

        buffer.append(" "+tab+Name);
        buffer.append("\n");

        buffer.append(" "+tab+Val);
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [CharOptArg]");
        return buffer.toString();
    }
}
