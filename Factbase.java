package fbase;

import java.io.*;

import java.util.*;

/**
 * @author pconnor1
 *
 */

public class Factbase {

  static ArrayList<String>  nodeStack; // this is used for describe nodes. 
  
  static Factbase           p;

  public static void main(String[] args) {
    p = new Factbase();
    p.init();
    p.run();
  }

  //new (adds labels, returns to one-way attrs)
  // node: n::ID::label::name
  // attribute: a::ID::label::name
  // edge: e::ID::srcNodeID::dstNodeID::attrID
  //old (still in effect cos new is buggy) 
  // node: n::ID::name
  // attribute: a::ID::labelA::labelB
  // edge: e::ID::srcNodeID::dstNodeID::attrID
  public void init() {
    nodes = new ArrayList<Node>();
    attrs = new ArrayList<Attr>();
    edges = new ArrayList<Edge>();

    nodeStack   = new ArrayList<String>();
    
    openLog();
  }

  public void run() {
    String inputLine = "";
    //load();// load the priority list
    loadText("factsOut.txt");
    showsize();
    while (!(inputLine.equals("quit"))) {
      inputLine = fbase.Helper.getUserInput("cmd>");
      if (!(inputLine == null)) {
        parse(inputLine);
      } else {
        inputLine = "";
      }
    }
  }

  
  static ArrayList<Node>  nodes;
  static ArrayList<Attr>  attrs;
  static ArrayList<Edge>  edges;
  
  /**
   * Top level parser
   * 
   * @param i
   *          - String to be parsed
   */
  public void parse(String ii) {
    boolean parsed = false;
    if (ii == null) {
      parsed = true;
    }
    String i = ii.replaceAll("  ", " ");
    if ((!parsed) && (i.equals(""))) {
      parsed = true;
    }
    if ((!parsed) && (i.equals("help"))) {
      System.out.println("help        - this help");
      System.out.println("add (item)  - add an item");
      System.out.println("attr (attribute) - add an attribute");
      System.out.println("describe    - list all facts about an item");
      System.out.println("drop (item) - remove an item");
      System.out.println("find (item) - find an item by (partial) name");
      System.out.println("list (item) - list an item");
      System.out.println("list all    - list all items");
      System.out.println("list nodes  - list all nodes");
      System.out.println("list attrs  - list all attributes");
      System.out.println("list edges  - list all edges");
      System.out.println("load (textfile) - load from text file");
      System.out.println("more        - describe more");
      System.out.println("save        - save data");
      System.out.println("size        - show number of items in factbase");
      System.out.println("quit        - save data and exit the program");
      System.out.println("abort       - exit the program without saving");
      parsed = true;
    }
    if ((!parsed) && (i.equals("save"))) {
      parsed = true;
      System.out.print("Saving...");
      saveAll();
      saveAllFacts();
      System.out.println("Done");
    }
    if ((!parsed) && (i.equals("quit"))) {
      System.out.println("Quitting...");
      parsed = true;
      cleanup();
    }
    if ((!parsed) && (i.equals("abort"))) {
      System.out.println(i+": not implemented");
      //System.out.println("Aborting...");
      //System.out.println("Done");
      parsed = true;
    }
    if ((!parsed) && (i.contains("add "))) {
      // System.out.println(i+": not implemented");
      add(i.replace("add ",""));
      //nodes.add(new Node("Generic Node",i.replace("add ",""),nextID()));
      parsed = true;
    }
    //attr
    if ((!parsed) && (i.contains("attr "))) {
      addAttr(i.replace("attr ",""));
      //attrs.add(new Attr("definition", i.replace("attr ",  ""), nextID()));
      parsed = true;
    }
    if ((!parsed) && (i.contains("describe all"))) {
      describeAllNodes();
      parsed = true;
    }
    if ((!parsed) && (i.contains("find "))) {
      String qq=i.replace("find ","");
      findNodeByPartial(qq);
      findAttrByPartial(qq);
      findEdgeByPartial(qq);
      parsed = true;
    }
    if ((!parsed) && (i.contains("describe "))) {
      // System.out.println(i+": not implemented");
      describe(i);
      parsed = true;
    }
    if ((!parsed) && (i.contains("drop "))) {
      System.out.println(i + ": not implemented");
      // drop(i);
      parsed = true;
    }
    if ((!parsed) && (i.equals("list"))) {
      System.out.println(i + ": not implemented");
      // list();
      parsed = true;
    }
    if ((!parsed) && (i.equals("list all"))) {
      listNodes();
      listAttrs();
      listEdges();
      parsed = true;
    }
    if ((!parsed) && (i.equals("list nodes"))) {
      listNodes();
      parsed = true;
    }
    if ((!parsed) && (i.equals("list attrs"))) {
      listAttrs();
      parsed = true;
    }
    if ((!parsed) && (i.equals("list edges"))) {
      listEdges();
      parsed = true;
    }
    if ((!parsed) && (i.contains("load "))) {
      // System.out.println(i + ": not implemented");
      loadText(i);
      parsed = true;
    }
    if ((!parsed) && (i.equals("more"))) {
      describeMore();
      parsed = true;
    }
    if ((!parsed) && (i.equals("size"))) {
      showsize();
      parsed = true;
    }
    if (!parsed) {
      parsed = parse2(i);
    }
    if (!parsed) {
      System.out.println(i + ": not understood");
//      System.out.println("parseinfo:\n"+parse2output);
    }
  }

  /** Secondary pre-parser called from parse2
   * 
   * @param j
   * @return
   */
  public String parse1a(String j) {
    parse2output = parse2output + ("\nP1A:      raw: \""+j+"\""+"\n");
    String retval = j;
    String k = j.replaceAll("  ", " ").replaceAll(" ",  "_");
    String k3 = j.replaceAll("  ", " ");
    String ktemp = "";
    parse2output = parse2output + ("\nP1A:    clean: \""+k+"\""+"\n");
    for (int t=0; t<nodes.size(); t++) {
      String k2=nodes.get(t).getNodeName();
      //parse2output = parse2output + ("P1A:(n)     Does k: "+k+"\n");
      //parse2output = parse2output + ("P1A:(n) contain k2: "+k2+"\n");
      if (k.contains(k2)) {
        parse2output = parse2output + ("P1A:(n) Node "+k2+"\n");
        ktemp = k2.replace("_", " ");
        k3 = k3.replace(ktemp, k2);
      }
    }
    for (int t=0; t<attrs.size(); t++) {
      String k2=attrs.get(t).getName1();
      //parse2output = parse2output + ("P1A:(a)     Does k: "+k+"\n");
      //parse2output = parse2output + ("P1A:(a) contain k2: "+k2+"\n");
      if (k.contains(k2)) {
        parse2output = parse2output + ("P1A:(a) Attr "+k2+"\n");
        ktemp = k2.replace("_", " ");
        parse2output = parse2output + ("P1A:(a) temp "+ktemp+"\n");
        k3 = k3.replace(ktemp, k2);
        parse2output = parse2output + ("P1A:(a) Output now: "+k3+"\n");
        t = t + attrs.size();
      }
    }
    //System.out.println();
    retval = k3;
    parse2output = parse2output + ("P1A:       Revised: "+retval+"\n");
    return retval.replaceAll("  ", " ");
  }
  
  /**
   * Secondary parser - called from Parse if the simple commands in Parse are
   * not found
   * 
   * @param j
   *          - String - string to be parsed
   * @return boolean - true if parsed
   */
  String parse2output = "";
  public boolean parse2(String j) {
    log();
    log("P2: Parsing: "+j);
    boolean retval = false;
    parse2output = "";
    String j2 = parse1a(j);
    String[] i = j2.split(" ");
    String pattern = "";
    for (int t = 0; t < i.length; t++) {
      boolean done = false;
      for (int u = 0; u < nodes.size(); u++) {
        if (nodes.get(u).getNodeName().equals(i[t])) {
          String i2 = i[t].replaceAll("_", " ");
          parse2output = parse2output + i2 + " (node) ";
          pattern = pattern + "N";
          retval = true;
          done = true;
        }
      }
      for (int u = 0; u < attrs.size(); u++) {
        if (attrs.get(u).getName1().equals(i[t])) {
          String i2 = i[t].replaceAll("_", " ");
          parse2output = parse2output + i2 + " (attr) ";
          pattern = pattern + "A";
          retval = true;
          done = true;
        }
      }
      if (!done) {
        String i2 = i[t].replaceAll("_", " ");
        parse2output = parse2output + i2 + " (query) ";
        pattern = pattern + "Q";
        retval = true;
      }
    }
    if (retval) {
      retval = false;
      log("P2: Pattern: "+pattern);
      if (pattern.equals("NAQ")) {
        addNode(i[2]);
        String retmsg = "Added node: "+i[2];
        parse2output = parse2output + retmsg;
        log(retmsg.trim().replace("_", " "));
        Node nodestAA=getNode(i[0]);
        Node nodestAB=getNode(i[2]);
        Attr attrstAA=getAttr(i[1]);
        if ((nodestAA!=null)&&(nodestAB!=null)&&(attrstAA!=null)) {
          edges.add(new Edge(nodestAA,nodestAB,attrstAA,nextID()));
          logAndPrint("Added: \""+i[0]+" "+i[1]+" "+i[2]+"\"");
        } else {
          System.out.println("Issue adding (new item) edge: "+i[0]+" "+i[1]+" "+i[2]);
        }
        retmsg = "Added edge: \"" + i[0] + " " + i[1] + " " + i[2] + "\" ";
        parse2output = parse2output + retmsg;
        log(retmsg.trim().replace("_", " "));
        retval = true;
      }
      if (pattern.equals("NAN")) {
        Node nodestAA=getNode(i[0]);
        Node nodestAB=getNode(i[2]);
        Attr attrstAA=getAttr(i[1]);
        log("MAYBE adding (plain) edge: "+i[0]+" "+i[1]+" "+i[2]);
        if ((nodestAA!=null)&&(nodestAB!=null)&&(attrstAA!=null)) {
          edges.add(new Edge(nodestAA,nodestAB,attrstAA,nextID()));
          logAndPrint("Added: \""+i[0]+" "+i[1]+" "+i[2]+"\"");
        } else {
          log("Issue adding (plain) edge: "+i[0]+" "+i[1]+" "+i[2]);
        }
        retval = true;
      }
      if (pattern.equals("QAN")) {
        retval = true;
        if (!printEdgeByAttrLabelAndNodeAName(i[1], i[2])) {
          describe(i[2]);
        }
      }
      if (pattern.equals("ANA")) {
        retval = true;
        describe(i[2]);
      }
      if (pattern.equals("QAQ")) {
        retval = false;
      }
      if (pattern.equals("AN")) {
        if (!printEdgeByAttrLabelAndNodeAName(i[0], i[1])) {
          describe(i[1]);
        }
        retval = true;
      }
      if (pattern.equals("NA")) {
        if (!printEdgeByAttrLabelAndNodeAName(i[1], i[0])) {
          describe(i[0]);
        }
        retval = true;
      }
    }
    log(parse2output);
    return retval;
  }

  /*************** Verbs ***************/

  public void listall() {
  }

  public void list() {
  }

  public void add(String s) {
    String t1 = s.replaceAll("  ", " ").replace("add ", "");
    String t = t1.replace(" ", "_");
    addNode(t);
  }

  public void attr(String s) {
    String t1 = s.replaceAll("  ", " ").replace("attr ", "");
    String[]t = t1.split(" ");
    if (t.length==1) {
      addAttr(t[0]);
    }
    System.out.println("Added attr: " + t1);
  }

  public void drop(String s) {
  }

  public void showsize() {
    System.out.println();
    System.out.println("       nodes:" + (nodes.size()));
    System.out.println("  attributes:" + (attrs.size()));
    System.out.println("       edges:" + (edges.size()));
    System.out.println("       total:" + (nodes.size()+attrs.size()+edges.size()) );
    System.out.println();

  }

  public void describe(String ds) {
    // if item is a node describe it
    boolean isNode = false;
    boolean isAttr = false;
    String t = ds.replace("describe ", "").replace(" ", "_");
    //log("Describe: "+t);
    isNode = nodeExists(t);
    isAttr = attrAExists(t);
    if (!((isNode) || (isAttr))) {
      System.out.println("Can't find anything about \"" + (t) + "\"");
    } else {
      System.out.println(t + " exists");
    }
    if (isNode) { 
      nodeStack.clear();
      describeNode(t); 
    }
    if (isAttr) {describeAttr(t); }
  }

  public void describeMore() {
    log();
    // because Describe adds to the nodestack, we take the original 
    // contents of the nodestack and ignore add-ons.
    int nodeStackSize=nodeStack.size();
    logAndPrint("More about "+nodeStackName+":");
    if(nodeStackSize>0) {
      log("NodeStack contains:");
      for (int t=0; t<nodeStackSize;  t++) {
        log(nodeStack.get(t));
        describeNode(nodeStack.get(t));
      }
      nodeStack.clear();
    } else {
      logAndPrint("No more");
    }
  }

  String nodeStackName="";
  public boolean describeNode(String t) { 
  nodeStackName=t;
  boolean nodeExists = false; 
  int dd = nodeIndex(t.replace(" ", "_")); 
  if (dd >= 0) { 
    int currentNodeID = nodes.get(dd).getID(); 
    // and print all edges 
    if (edges.size() > 0) { 
      for (int fe = 0; fe < edges.size(); fe++) { 
        // edge: e::ID::srcNodeID::dstNodeID::attrID 
        // int fxEdge = edgeIDs.get(fe);
        Edge qq = edges.get(fe); 
        int fxNode1 = qq.getN1().getID(); 
        int fxNode2 = qq.getN2().getID(); 
        int fxAttr = qq.getA1().getID(); 
        if (fxNode1 == currentNodeID) { 
          int fx2Index = getNodeIndexByID(fxNode2); 
          if (fx2Index > -1) { 
            for (int fb = 0; fb < attrs.size(); fb++) { 
              if (attrs.get(fb).getID() == fxAttr) { 
                System.out.println(t + " " +  
                     attrs.get(fb).getName1().replace("_", " ") + " " +  
                     nodes.get(fx2Index).getNodeName().replace("_", " ")); 
                for (int tx=0; tx<nodeStack.size(); tx++) { 
                  if (nodeStack.get(tx).equals(nodes.get(fx2Index).getNodeName())) { 
                    nodeExists = true; 
                  } 
                } 
                if (!nodeExists){
                  nodeStack.add(nodes.get(fx2Index).getNodeName());
                  log ("Nodestack.add "+nodes.get(fx2Index).getNodeName());
                } //orange 
              } 
            } 
          } 
        } 
      } 
    } 
    nodeExists = true; 
  } 
  return nodeExists; 
  } 

  public boolean describeAttr(String t) {
    boolean attrExists = false;
    nodeStackName=t;
    //System.out.println("DESCRIBE_ATTR: Checking: "+t);
    int dd = attrIndex(t.replace(" ", "_"));
    //System.out.println("DESCRIBE_ATTR: Got index: "+dd);
    if (dd >= 0) {
      System.out.println(t + " is an attribute");
      attrExists = true;
      int currentAttrID = attrs.get(dd).getID();
      // and print all edges
      for (int fe = 0; fe < edges.size(); fe++) {
        boolean ok = printEdgeByIndexAndAttrAID(fe, currentAttrID);
        if (ok) {
          attrExists = true;
        }
      }
    }
    return attrExists;
  }

  void describeAllNodes() {
    System.out.println();
    System.out.println("Describing all " + (nodes.size()) + " nodes:");
    System.out.println();
    for (int da = 0; da < nodes.size(); da++) {
      describe(nodes.get(da).getNodeName());
      System.out.println();
    }
  }

  boolean printEdgeByIndexAndAttrAID(int fe, int currentAttrID) {
    boolean ok = false;
    // edge: e::ID::srcNodeID::dstNodeID::attrID
    // int fxEdge = edgeIDs.get(fe);
    Edge ed = edges.get(fe);
    int fxNode1 = ed.getN1().getID();
    int fxNode2 = ed.getN2().getID();
    int fxAttr = ed.getA1().getID();
    boolean found1 = false;
    boolean found2 = false;
    boolean foundAttr = false;
    int fx1Index = getNodeIndexByID(fxNode1);
    int fx2Index = getNodeIndexByID(fxNode2);
    int fxAttrIndex = getAttrIndexByID(fxAttr);
    if (fxAttr == currentAttrID) {
      if ((fx1Index > -1) && (fx2Index > -1) && (fxAttrIndex > -1)) {
        String fxNodeName1 = "";
        String fxNodeName2 = "";
        String fxAttrLabel = "";
        for (int fb = 0; fb < nodes.size(); fb++) {
          if (nodes.get(fb).getID() == fxNode1) {
            fxNodeName1 = nodes.get(fb).getNodeName().replace("_", " ");
            found1 = true;
          }
          if (nodes.get(fb).getID() == fxNode2) {
            fxNodeName2 = nodes.get(fb).getNodeName().replace("_", " ");
            found2 = true;
          }
        }
        for (int fb = 0; fb < attrs.size(); fb++) {
          if (nodes.get(fb).getID() == fxAttr) {
            fxAttrLabel = attrs.get(fb).getName1().replace("_", " ");
            foundAttr = true;
          }
        }
        if ((found1) && (found2) && (foundAttr)) {
          System.out.println("A->B: "+fxNodeName1 + " " + fxAttrLabel + " "
              + fxNodeName2);
          ok = true;
        }
      }
    }
    return ok;
  }


  String descEdgeByIndex(int fe) {
    String retval = "";
    Edge ed = edges.get(fe);
    int fxNode1 = ed.getN1().getID();
    int fxNode2 = ed.getN2().getID();
    int fxAttr = ed.getA1().getID();
    int fx1Index = getNodeIndexByID(fxNode1);
    int fx2Index = getNodeIndexByID(fxNode2);
    int fxAttrIndex = getAttrIndexByID(fxAttr);
    if ((fx1Index > -1) && (fx2Index > -1) && (fxAttrIndex > -1)) {
      String fxNodeName1 = "";
      String fxNodeName2 = "";
      String fxAttrLabel = "";
      fxNodeName1 = nodes.get(fx1Index).getNodeName().replace("_", " ");
      fxNodeName2 = nodes.get(fx2Index).getNodeName().replace("_", " ");
      fxAttrLabel = attrs.get(fxAttrIndex).getName1().replace("_", " ");
      retval = (fxNodeName1 + " " + fxAttrLabel + " " + fxNodeName2);
    }
    return retval;
  }

  boolean printEdgeByAttrLabelAndNodeAName(String attr, String node) {
    boolean retval = false;
    //System.out.println("printEdgeByAttrLabelAndNodeName(" + attr + ", " + node + ")");
    int maybeAttr = getAttrIDByLabel(attr);
    int maybeNode2 = nodeIDByName(node);
    int fx1Index = -1;
    int fx2Index = getNodeIndexByID(maybeNode2);
    int fxAttrIndex = getAttrIndexByID(maybeAttr);
    if ((fx2Index > -1) && (fxAttrIndex > -1)) {
      boolean found = false;
      for (int k = 0; k < edges.size(); k++) {
        if ((edges.get(k).getA1().getID() == maybeAttr)
            && (edges.get(k).getN2().getID() == maybeNode2)) {
          fx1Index = getNodeIndexByID(edges.get(k).getN1().getID());
          String fxNodeName1 = nodes.get(fx1Index).getNodeName().replace("_", " ");
          String fxNodeName2 = nodes.get(fx2Index).getNodeName().replace("_", " ");
          String fxAttrLabel = attrs.get(fxAttrIndex).getName1().replace("_", " ");
          System.out.println("a>b: "+fxNodeName1 + " " + fxAttrLabel + " "
              + fxNodeName2);
          // found = true;
          retval = true;
        }
      }
      if (!found) {
        // System.out.println("None");
      }
    } else {
      // System.out.println("None");
    }
    return retval;
  }


  /*************** Helpers ***************/

  public void load() {
    String s = "";
    try {
      BufferedReader itemfile = new BufferedReader(new FileReader("fbitems.txt"));
      while ((s = itemfile.readLine()) != null) {
        // System.out.println(s);
        if (s.contains("::")) {
          String[] t = s.split("::");
          if (t[0].equals("n")) {
            addNode(t[1], t[2]);
          }
          if (t[0].equals("a")) {
            if (t.length==3) {
              addAttr(Integer.parseInt(t[1]), t[2]);
            }
          }
          if (t[0].equals("e")) {
            addEdge(t[1], t[2], t[3], t[4]);
          }
        }
      }
      itemfile.close();
    } catch (Exception ef) {
    }
  }

  public void loadText(String fileName) {
    String s = "";
    try {
      log();
      s=fileName.replace("load ", "");
      logAndPrint("loadText: attempting to open "+s);
      BufferedReader itemfile = new BufferedReader(new FileReader(s));
      logAndPrint("loadText: reading "+s);
      while ((s = itemfile.readLine()) != null) {
        parse(s);
      }
      itemfile.close();
    } catch (Exception ef) {
      logAndPrint("loadText: unhappy!");
      logAndPrint(ef.toString());
    }
  }
  
  public boolean nodeExists(String ts_name) {
    boolean retval = false;
    for (int t = 0; t < nodes.size(); t++) {
      String q = nodes.get(t).getNodeName();
      if (q.equals(ts_name)) {
        retval = true;
        t = t + nodes.size();
      }
    }
    return retval;
  }

  public int nodeIDByName(String ts_name) {
    int retval = -1;
    for (int t = 0; t < nodes.size(); t++) {
      String q = nodes.get(t).getNodeName();
      if (q.equals(ts_name)) {
        retval = nodes.get(t).getID();
        t = t + nodes.size();
      }
    }
    return retval;
  }

  public void findNodeByPartial(String ts_name) {
    for (int t = 0; t < nodes.size(); t++) {
      String q = nodes.get(t).getNodeName();
      if (q.contains(ts_name)) {
        System.out.println(nodes.get(t).getNodeName());
      }
    }
  }
  
  public void findAttrByPartial(String ts_name) {
    for (int t = 0; t < attrs.size(); t++) {
      String q = attrs.get(t).getName1();
      if (q.contains(ts_name)) {
        System.out.println(attrs.get(t).getName1());
      }
    }
  }
  
  public void findEdgeByPartial(String ts_name) {
    boolean found = false;
    for (int t = 0; t < edges.size(); t++) {
      found = false;
      Edge q = edges.get(t);
      if (q.getN1().getNodeName().contains(ts_name)) { found = true; }
      if (q.getN2().getNodeName().contains(ts_name)) { found = true; }
      if (q.getA1().getName1().contains(ts_name)) { found = true; }
      if (found) {
        System.out.println(edges.get(t).toString());
      }
    }
  }

  public int nodeIndex(String ts_name) {
    int retval = -1;
    for (int t = 0; t < nodes.size(); t++) {
      String q = nodes.get(t).getNodeName();
      if (q.equals(ts_name)) {
        retval = t;
        t = t + nodes.size();
      }
    }
    return retval;
  }
  
  public boolean nodeIDExists(int ts_node) {
    boolean retval = false;
    for (int t = 0; t < nodes.size(); t++) {
      int q = nodes.get(t).getID();
      if (q == (ts_node)) {
        retval = true;
        t = t + nodes.size();
      }
    }
    return retval;
  }

  public int getNodeIndexByID(int ts_node) {
    int retval = -1;
      for (int t = 0; t < nodes.size(); t++) {
        int q = nodes.get(t).getID();
        if (q == (ts_node)) {
          retval = t;
          t = t + nodes.size();
        }
      }
    return retval;
  }

  public void addNode(String ts_id, String ts_name) {
    log("addNode: Adding: "+ts_name);
    if (!(nodeExists(ts_name))) {
      nodes.add(new Node("Generic Node",ts_name,Integer.parseInt(ts_id)));
      System.out.println("Added node: " + ts_name);
    } else {
      logAndPrint(ts_name+" already exists.");
    }
  }

  public void addNode(String ts_name) {
    addNode(Integer.toString(nextID()),ts_name);
  }

  public boolean attrAExists(String ts_name) {
    boolean retval = false;
      for (int t = 0; t < attrs.size(); t++) {
      String q = attrs.get(t).getName1();
      if (q.equals(ts_name)) {
        retval = true;
        t = t + attrs.size();
      }
    }
    return retval;
  }

  public int attrIndex(String ts_name) {
    int retval = -1;
    for (int t = 0; t < attrs.size(); t++) {
      String q = ""; 
      q = attrs.get(t).getName1();
      if (q.equals(ts_name)) {
        retval = t;
      }
      if (retval>=0) {t = t + attrs.size();}
    }
    return retval;
  }

  public int getAttrIndexByID(int ts_attr) {
    int retval = -1;
    for (int t = 0; t < attrs.size(); t++) {
      int q = attrs.get(t).getID();
      if (q == (ts_attr)) {
        retval = t;
        t = t + attrs.size();
      }
    }
    return retval;
  }

  public boolean attrIDExists(int ts_attr) {
    boolean retval = false;
    for (int t = 0; t < attrs.size(); t++) {
      int q = attrs.get(t).getID();
      if (q == (ts_attr)) {
        retval = true;
        t = t + attrs.size();
      }
    }
    return retval;
  }

  public int getAttrByID(int ts_attr) {
    int retval = -1;
      for (int t = 0; t < attrs.size(); t++) {
        int q = attrs.get(t).getID();
        if (q == (ts_attr)) {
          retval = t;
          t = t + attrs.size();
        }
      }
    return retval;
  }

  public int getAttrIDByLabel(String ts_attr) {
    int retval = -1;
    for (int t = 0; t < attrs.size(); t++) {
      String q = ""; 
      if (retval==-1) {
        q = attrs.get(t).getName1();
        if (q.equals(ts_attr)) {
          int r = attrs.get(t).getID();
          retval = r;
          t = t + attrs.size();
        }
      }
    }
    return retval;
  }
  
  int nextID=0;
  public int nextID() {
    return nextID++;
  }
  
  public void addAttr(int ts_id, String ts_label) {
    if (!(attrAExists(ts_label))) {
      attrs.add(new Attr("definition", ts_label, ts_id));
    }
  }

  public void addAttr(String ts_name) {
    addAttr(nextID(), ts_name);
  }

  public void addEdge(String ts_id, String ts_NodeA, String ts_NodeB, String ts_attr) {
    int maybeNodeA = Integer.parseInt(ts_NodeA);
    int maybeNodeB = Integer.parseInt(ts_NodeB);
    int maybeAttr = Integer.parseInt(ts_attr);
     if ((nodeIDExists(maybeNodeA))&&
     (nodeIDExists(maybeNodeB))&&
     (attrIDExists(maybeAttr))) {
      edges.add(new Edge(nodes.get(maybeNodeA),nodes.get(maybeNodeB),attrs.get(maybeAttr),nextID()));
    //System.out.println("Added edge: "+ts_NodeA+" "+ts_attr+" "+ts_NodeB);
     }
  }

  public void listNodes() {
    System.out.println(nodes.size()+" nodes:");
    for (int t = 0; t < nodes.size(); t++) {
      String rn = nodes.get(t).getNodeName().replace("_", " ");
      System.out.println(rn);
    }
  }
  
  public void listAttrs() {
    System.out.println(attrs.size()+" attributes:");
    for (int t = 0; t < attrs.size(); t++) {
      String rn = attrs.get(t).getName1().replace("_", " ");
      System.out.println("\""+rn+"\"");
    }
  }

  public void listEdges() {
    System.out.println(edges.size()+" edges:");
    for (int t = 0; t < edges.size(); t++) {
      System.out.println(descEdgeByIndex(t));
    }
  }

  public Node getNode(String n) {
    Node retval = null;
    Node w = null;
      for (int t=0; t<nodes.size(); t++) {
        w=nodes.get(t);
        if (w.getNodeName().equals(n)) {
          retval = w;
          t=t+nodes.size();
        }
      }
    return retval;
  }
  
  public Attr getAttr(String n) {
    Attr retval = null;
    Attr w = null;
      for (int t=0; t<attrs.size(); t++) {
        w=attrs.get(t);
        if (w.getName1().equals(n)) {
          retval = w;
          t=t+attrs.size();
        }
      }
    return retval;
  }
  
  public void saveAll() {
    // node: n::ID::label::name
    // attribute: a::ID::label::name
    // edge: e::ID::srcNodeID::dstNodeID::attrID
    try {
      //BufferedWriter itemfile = new BufferedWriter(new FileWriter("itemsOut.txt"));
      BufferedWriter itemfile = new BufferedWriter(new FileWriter("fbitems.txt"));
      for (int t = 0; t < nodes.size(); t++) {
        Node qq=nodes.get(t);
        itemfile.write(qq.toFbItem()+ "\n");
      }
      for (int t = 0; t < attrs.size(); t++) {
        Attr qq = attrs.get(t);
        itemfile.write(qq.toFbItem()+ "\n");
      }
      for (int t = 0; t < edges.size(); t++) {
        Edge qq = edges.get(t);
        itemfile.write(qq.toFbItem()+ "\n");
      }
      itemfile.close();
    } catch (Exception ef) {
    }
  }
  
  public void saveAllFacts() {
    try {
      BufferedWriter itemfile = new BufferedWriter(new FileWriter("factsOut.txt"));
      for (int t=0; t<nodes.size(); t++) {
        itemfile.write(nodes.get(t).toCommand()+ "\n");
      }
      for (int t=0; t<attrs.size(); t++) {
        itemfile.write(attrs.get(t).toCommand()+ "\n");
      }
      for (int t=0; t<edges.size(); t++) {
        itemfile.write(edges.get(t).toCommand()+ "\n");
      }
      itemfile.close();
    } catch (Exception ef) {
    }
  }

  BufferedWriter logfile;  
  boolean logOpen = false;

  public void openLog() {
    if (!(logOpen)) {
      try {
        logfile = new BufferedWriter(new FileWriter("log.txt"));
        logOpen = true;
      } catch (Exception e) {
        //
      }
    }
  }
  
  public void logAndPrint(String s) {
    log(s);
    System.out.println(s);
  }
  
    public void log() {
    log("");
  }
  
  public void log(String s) {
    if (logOpen) {
      try {
        logfile.write(s + "\n");
        logfile.flush();
      } catch (Exception e) {
        //
      }
    }
  }
  
  public void closeLog() {
    if (logOpen) {
      try {
        logfile.close();
      } catch (Exception e) {
        //
      }
    }
  }
  
  public void cleanup() {
    System.out.print("Cleaning up...");
    saveAll();
    saveAllFacts();
    closeLog();
    System.out.println("Done");
  }

}
