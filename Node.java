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
    //con label return ("add "+Label.replace(" ","_")+" "+nodeName.replace(" ", "_"));
    return ("add "+nodeName.replace(" ", "_"));
  }

  public String toString() {
    //con label return (Label +" "+nodeName);
    return (nodeName);
  }

  public String toFbItem() {
    //con label return ("n::"+ID+"::"+Label.replace(" ", "_")+"::"+nodeName.replace(" ", "_"));
    return ("n::"+ID+"::"+nodeName.replace(" ", "_"));
  }
}
