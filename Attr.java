package fbase;

public class Attr extends Fact {
  Attr() {
    //super.generate and get an ID
  }

  Attr(String label, String n1) {
    //super.generate and get an ID
    setLabel(label);
    setName1(n1);
    isAttr = true;
  }

  Attr(String label, String n1, int id) {
    setID(id);
    setLabel(label);
    setName1(n1);
    isAttr = true;
  }

  public boolean exists(String n1) {
    boolean retval=false;
      if (Name1.equals(n1)) {
        retval=true;
      }
    return retval;
  }
  
  String Name1;
  public String getName1() {
    return Name1;
  }
  public void setName1(String name1) {
    Name1 = name1;
  }

  public String toCommand() {
    return ("attr "+Label.replace(" ", "_")+" "+Name1.replace(" ", "_"));
  }

  public String toString() {
    return (Label+" "+Name1);
  }

  public String toFbItem() {
    return ("a::"+ID+"::"+Label.replace(" ", "_")+"::"+Name1.replace(" ", "_"));
  }
}
