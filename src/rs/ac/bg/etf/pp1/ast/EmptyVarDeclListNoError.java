// generated with ast extension for cup
// version 0.8
// 23/7/2022 20:18:35


package rs.ac.bg.etf.pp1.ast;

public class EmptyVarDeclListNoError extends VarDeclListNoError {

    public EmptyVarDeclListNoError () {
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
        buffer.append("EmptyVarDeclListNoError(\n");

        buffer.append(tab);
        buffer.append(") [EmptyVarDeclListNoError]");
        return buffer.toString();
    }
}