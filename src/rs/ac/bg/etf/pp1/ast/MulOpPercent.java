// generated with ast extension for cup
// version 0.8
// 23/7/2022 20:18:35


package rs.ac.bg.etf.pp1.ast;

public class MulOpPercent extends Mulop {

    public MulOpPercent () {
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
        buffer.append("MulOpPercent(\n");

        buffer.append(tab);
        buffer.append(") [MulOpPercent]");
        return buffer.toString();
    }
}
