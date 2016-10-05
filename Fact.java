package fbase;

public class Fact {
  
  int ID;
  String Label;

  boolean isNode = false;
  boolean isAttr = false;
  boolean isEdge = false;

  public int getID() {
    return ID;
  }

  public void setID(int iD) {
    ID = iD;
  }
  
  public String getLabel() {
    return Label;
  }

  public void setLabel(String label) {
    Label = label;
  }



}
