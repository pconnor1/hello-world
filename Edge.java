package fbase;

public class Edge extends Fact {
  Node n1;
  Node n2;
  Attr a1;
  
  /** We have no way of putting a label on an edge at this time **
  Edge(String lbl, Node na, Node nb, Attr aa) {
    setLabel(lbl);
    setN1(na);
    setN2(nb);
    setA1(aa);
    isEdge = true;
  }
  
  Edge(String lbl, Node na, Node nb, Attr aa, int id) {
    setLabel(lbl);
    setID(id);
    setN1(na);
    setN2(nb);
    setA1(aa);
    isEdge = true;
  }
  **/

  Edge(Node na, Node nb, Attr aa) {
    setLabel("");
    setN1(na);
    setN2(nb);
    setA1(aa);
    isEdge = true;
  }
  
  Edge(Node na, Node nb, Attr aa, int id) {
    setLabel("");
    setID(id);
    setN1(na);
    setN2(nb);
    setA1(aa);
    isEdge = true;
  }
  public Node getN1() {
    return n1;
  }
  public void setN1(Node n1) {
    this.n1 = n1;
  }
  public Node getN2() {
    return n2;
  }
  public void setN2(Node n2) {
    this.n2 = n2;
  }
  public Attr getA1() {
    return a1;
  }
  public void setA1(Attr a1) {
    this.a1 = a1;
  }

  public String toCommand() {
    return (n1.getNodeName() + " " + a1.getName1() + " " + n2.getNodeName());
  }

  public String toString() {
    return (this.toCommand());
  }

  public String toFbItem() {
    return ("e::"+ID+"::"+n1.getID()+"::"+n2.getID()+"::"+a1.getID()+"::"+this.toCommand());
  } 
  
}
