package fbase;

public class Node extends Fact {
  
  Node(String l, String s){
    isNode = true;
    setLabel(l);
    setNodeName(s);
  }

  String nodeName;
  
  Node(String l, String s, int id){
    isNode = true;
    setID(id);
    setLabel(l);
    setNodeName(s);
  }
  
  public String getNodeName() {
    return nodeName;
  }

  public void setNodeName(String nodeName) {
    this.nodeName = nodeName;
  }

  public boolean exists(String n1) {
    boolean retval=false;
      if (nodeName.equals(n1)) {
        retval=true;
      }
    return retval;
  }
  
  public String getNodeNamebyID(int id_in) {
    if (id_in==ID) {
      return nodeName;
    } else {
      return null;
    }
  }

  public String toCommand() {
    return ("add "+Label.replace(" ","_")+" "+nodeName.replace(" ", "_"));
  }

  public String toString() {
    return (Label +" "+nodeName);
  }

  public String toFbItem() {
    return ("n::"+ID+"::"+Label.replace(" ", "_")+"::"+nodeName.replace(" ", "_"));
  }
}
