package fbase;

import java.io.*;

import java.util.*;

/**
 * @author pconnor1
 *
 */

public class Factbase {

  static ArrayList<String>  nodeLabels;
  static ArrayList<String>  nodeNames;
  static ArrayList<Integer> nodeIDs;
  static ArrayList<Integer> attrIDs;
  static ArrayList<String>  attrLabelAs;
  static ArrayList<Integer> edgeIDs;
  static ArrayList<Integer> edgeANodes;
  static ArrayList<Integer> edgeBNodes;
  static ArrayList<Integer> edgeAttrs;

  static ArrayList<String>  nodeStack; // this is used for describe nodes. 
  
  static int                numItems = 0;
  static int                numNodes = 0;
  static int                numAttrs = 0;
  static int                numEdges = 0;
  static Factbase           p;

  public static void main(String[] args) {
    p = new Factbase();
    p.init();
    p.run();
  }

  // node: n::ID::name
  // attribute: a::ID::labelA::labelB
  // edge: e::ID::srcNodeID::dstNodeID::attrID
  public void init() {
    nodeLabels  = new ArrayList<String>();
    nodeNames   = new ArrayList<String>();
    nodeIDs     = new ArrayList<Integer>();
    attrIDs     = new ArrayList<Integer>();
    attrLabelAs = new ArrayList<String>();
    edgeIDs     = new ArrayList<Integer>();
    edgeANodes  = new ArrayList<Integer>();
    edgeBNodes  = new ArrayList<Integer>();
    edgeAttrs   = new ArrayList<Integer>();

    nodeStack   = new ArrayList<String>();

  }

  public void run() {
    String inputLine = "";
    //load();// load the priority list
    loadText("factsOut.txt");
    showsize();
    objDemo();   
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
  
  public void objDemo() {
    
    nodes = new ArrayList<Node>();
    attrs = new ArrayList<Attr>();
    edges = new ArrayList<Edge>();
    
    System.out.println();
    System.out.println("objDemo:Begin");
    System.out.println();
    
    nodes.add(new Node("Generic Node","Node A",0));
    System.out.println("Nodes[0]: "+nodes.get(0).toString());
    nodes.add(new Node("Generic Node","Node B",1));
    System.out.println("Nodes[1]: "+nodes.get(1).toString());
    attrs.add(new Attr("definition", "is", 2));
    System.out.println("Attrs[0]: "+attrs.get(0).toString());
    edges.add(new Edge(nodes.get(0),nodes.get(1),attrs.get(0),3));
    System.out.println("Edges[0]: "+edges.get(0).toString());

    System.out.println();
    System.out.println("Renaming Nodes[0] - making no change to Edges[0]");
    nodes.get(0).setNodeName("Peanut");
    System.out.println("Nodes[0]: "+nodes.get(0).toString());
    System.out.println("Edges[0]: "+edges.get(0).toString());
    System.out.println("Edges[0] picked up the rename automatically");

    System.out.println();
    System.out.println(nodes.get(0).toFbItem());
    System.out.println(nodes.get(1).toFbItem());
    System.out.println(attrs.get(0).toFbItem());
    System.out.println(edges.get(0).toFbItem());

    System.out.println();
    System.out.println("Finding node 1");
    for (int t=0; t<nodes.size(); t++) {
      if (nodes.get(t).getID()==1) {
        System.out.println("found node ID 1: "+nodes.get(t).toString());
      }
    }

    System.out.println();
    System.out.println("Commands:");
    System.out.println();
    for (int t=0; t<nodes.size(); t++) {
      System.out.println(nodes.get(t).toCommand());
    }
    for (int t=0; t<attrs.size(); t++) {
      System.out.println(attrs.get(t).toCommand());
    }
    for (int t=0; t<edges.size(); t++) {
      System.out.println(edges.get(t).toCommand());
    }

    System.out.println();
    System.out.println("objDemo:End");
    System.out.println();
    
  }
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
      add(i);
      parsed = true;
    }
    if ((!parsed) && (i.contains("attr "))) {
      attr(i);
      parsed = true;
    }
    if ((!parsed) && (i.contains("describe all"))) {
      describeAllNodes();
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
      System.out.println("parseinfo:\n"+parse2output);
    }
  }

  /** Secondary pre-parser
   * 
   * @param j
   * @return
   */
  public String parse1a(String j) {
    parse2output = parse2output + ("\nP1A:   raw: \""+j+"\""+"\n");
    String retval = j;
    String k = j.replaceAll("  ", " ").replaceAll(" ",  "_");
    String k3 = j.replaceAll("  ", " ");
    String ktemp = "";
    parse2output = parse2output + ("\nP1A: clean: \""+k+"\""+"\n");
    for (int t=0; t<numNodes; t++) {
      String k2=nodeNames.get(t);
      if (k.contains(k2)) {
        parse2output = parse2output + ("P1A: Node "+k2+"\n");
        ktemp = k2.replace("_", " ");
        k3 = k3.replace(ktemp, k2);
      }
    }
    for (int t=0; t<numAttrs; t++) {
      String k2=attrLabelAs.get(t);
      parse2output = parse2output + ("P1A:     Does k: "+k+"\n");
      parse2output = parse2output + ("P1A: contain k2: "+k2+"\n");
      if (k.contains(k2)) {
        parse2output = parse2output + ("P1A: Attr "+k2+"\n");
        ktemp = k2.replace("_", " ");
        parse2output = parse2output + ("P1A: temp "+ktemp+"\n");
        k3 = k3.replace(ktemp, k2);
        parse2output = parse2output + ("P1A: Output now: "+k3+"\n");
      }
    }
    //System.out.println();
    retval = k3;
    parse2output = parse2output + ("P1A: Revised: "+retval+"\n");
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
    boolean retval = false;
    parse2output = "";
    String j2 = parse1a(j);
    String[] i = j2.split(" ");
    String pattern = "";
    for (int t = 0; t < i.length; t++) {
      boolean done = false;
      for (int u = 0; u < numNodes; u++) {
        if (nodeNames.get(u).equals(i[t])) {
          String i2 = i[t].replaceAll("_", " ");
          parse2output = parse2output + i2 + " (node) ";
          pattern = pattern + "N";
          retval = true;
          done = true;
        }
      }
      for (int u = 0; u < numAttrs; u++) {
        if (attrLabelAs.get(u).equals(i[t])) {
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
      //System.out.println("PATTERN: "+pattern);
      if (pattern.equals("NAQ")) {
        //output = output + "\nI think you want to create the new item: " + i[2]
        //    + "\n" + "and create the relationship \"" + i[0] + " " + i[1] + " "
        //    + i[2] + "\" ";
        addNode(i[2]);
        String retmsg = "Added node: "+i[2];
        parse2output = parse2output + retmsg;
        System.out.println(retmsg.trim().replace("_", " "));
        int nodeAA = nodeIDByName(i[0]);
        int nodeBA = nodeIDByName(i[2]);
        int attrAA = getAttrIDByLabel(i[1]);
        if ((nodeAA >= 0) && (nodeBA >= 0) && (attrAA >= 0)) {
          addEdge(Integer.toString(numItems), Integer.toString(nodeAA),
              Integer.toString(nodeBA), Integer.toString(attrAA));
        }
        retmsg = "Added edge: \"" + i[0] + " " + i[1] + " " + i[2] + "\" ";
        parse2output = parse2output + retmsg;
        System.out.println(retmsg.trim().replace("_", " "));
        retval = true;
      }
      if (pattern.equals("NAN")) {
        parse2output = parse2output + "\n I think you want to create the relationship \"" + i[0] + " " + i[1] + " " + i[2] + "\" ";
        //System.out.println(output);
        // kumquat
        int nodeA = nodeIDByName(i[0]);
        //System.out.println("Got node 1. ID: "+nodeA);
        int nodeB = nodeIDByName(i[2]);
        //System.out.println("Got node 2. ID: "+nodeB);
        int attrA = getAttrIDByLabel(i[1]);
        //System.out.println("Got attr.   ID: "+attrA);
        if ((nodeA >= 0) && (nodeB >= 0) && (attrA >= 0)) {
          addEdge(Integer.toString(numItems), Integer.toString(nodeA),
              Integer.toString(nodeB), Integer.toString(attrA));
          retval = true;
          String retmsg = "Added edge: \"" + i[0] + " " + i[1] + " " + i[2] + "\" ";
          parse2output = parse2output + retmsg;
          System.out.println(retmsg.trim().replace("_", " "));
        }
      }
      if (pattern.equals("QAN")) {
        retval = true;
        //System.out.println(output);
        //System.out.println("QAN: Trying: "+i[1]+", "+i[2]);
        if (!printEdgeByAttrLabelAndNodeAName(i[1], i[2])) {
          if (!printEdgeByAttrLabelAndNodeBName(i[1], i[2])) {
            //System.out.println("QAN: Describing: "+i[2]);
            describe(i[2]);
          }
        }
      }
      if (pattern.equals("ANA")) {
        retval = true;
        describe(i[2]);
      }
      if (pattern.equals("QAQ")) {
        retval = false;
      }
    }
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
    System.out.println("Added node: " + t1);
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
    System.out.println("     nodes:" + (numNodes));
    System.out.println("attributes:" + (numAttrs));
    System.out.println("     edges:" + (numEdges));
    System.out.println("     total:" + (numItems));
    System.out.println();
  }

  public void describe(String ds) {
    // if item is a node describe it
    boolean isNode = false;
    boolean isAttr = false;
    String t = ds.replace("describe ", "");
    //System.out.println("DESCRIBE: Checking nodes for:"+t);
    nodeStack.clear();
    isNode = describeNode(t);
    //System.out.println("DESCRIBE: Checking attrs for:"+t);
    isAttr = describeAttr(t);
    if (!((isNode) || (isAttr))) {
      System.out.println("Can't find anything about \"" + (t) + "\"");
    }
  }

  public void describeMore() {
    if(nodeStack.size()>0) {
      System.out.println("More about "+nodeStackName+":");
      for (int t=0; t<nodeStack.size();  t++) {
        describeNode(nodeStack.get(t));
      }
      nodeStack.clear();
    } else {
      System.out.println("No more");
    }
  }

  String nodeStackName="";
  public boolean describeNode(String t) {
    nodeStackName=t;
    boolean nodeExists = false;
    int dd = nodeIndex(t.replace(" ", "_"));
    if (dd >= 0) {
      //System.out.println(t);
      int currentNodeID = nodeIDs.get(dd);
      // and print all edges
      if (numEdges > 0) {
        for (int fe = 0; fe < numEdges; fe++) {
          // edge: e::ID::srcNodeID::dstNodeID::attrID
          // int fxEdge = edgeIDs.get(fe);
          int fxNode1 = edgeANodes.get(fe);
          int fxNode2 = edgeBNodes.get(fe);
          int fxAttr = edgeAttrs.get(fe);
          if (fxNode1 == currentNodeID) {
            int fx2Index = getNodeIndexByID(fxNode2);
            if (fx2Index > -1) {
              for (int fb = 0; fb < numAttrs; fb++) {
                if (attrIDs.get(fb) == fxAttr) {
                  System.out.println(t + " " + 
                       attrLabelAs.get(fb).replace("_", " ") + " " + 
                       nodeNames.get(fx2Index).replace("_", " "));
                  for (int tx=0; tx<nodeStack.size(); tx++) {
                    if (nodeStack.get(tx).equals(nodeNames.get(fx2Index))) {
                      nodeExists = true;
                    }
                  }
                  if (!nodeExists){nodeStack.add(nodeNames.get(fx2Index));} //orange
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
    //System.out.println("DESCRIBE_ATTR: Checking: "+t);
    int dd = attrIndex(t.replace(" ", "_"));
    //System.out.println("DESCRIBE_ATTR: Got index: "+dd);
    if (dd >= 0) {
      System.out.println(t + " is an attribute");
      attrExists = true;
      int currentAttrID = attrIDs.get(dd);
      // and print all edges
      if (numEdges > 0) {
        for (int fe = 0; fe < numEdges; fe++) {
          boolean ok = printEdgeByIndexAndAttrAID(fe, currentAttrID);
          if (ok) {
            attrExists = true;
          }
        }
      }
    }
    return attrExists;
  }

  void describeAllNodes() {
    System.out.println();
    System.out.println("Describing all " + (numNodes) + " nodes:");
    System.out.println();
    for (int da = 0; da < numNodes; da++) {
      describe(nodeNames.get(da));
      System.out.println();
    }
  }

  boolean printEdgeByIndexAndAttrAID(int fe, int currentAttrID) {
    boolean ok = false;
    // edge: e::ID::srcNodeID::dstNodeID::attrID
    // int fxEdge = edgeIDs.get(fe);
    int fxNode1 = edgeANodes.get(fe);
    int fxNode2 = edgeBNodes.get(fe);
    int fxAttr = edgeAttrs.get(fe);
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
        for (int fb = 0; fb < numNodes; fb++) {
          if (nodeIDs.get(fb) == fxNode1) {
            fxNodeName1 = nodeNames.get(fb).replace("_", " ");
            found1 = true;
          }
          if (nodeIDs.get(fb) == fxNode2) {
            fxNodeName2 = nodeNames.get(fb).replace("_", " ");
            found2 = true;
          }
        }
        for (int fb = 0; fb < numAttrs; fb++) {
          if (nodeIDs.get(fb) == fxAttr) {
            fxAttrLabel = attrLabelAs.get(fb).replace("_", " ");
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
    int fxNode1 = edgeANodes.get(fe);
    int fxNode2 = edgeBNodes.get(fe);
    int fxAttr = edgeAttrs.get(fe);
    int fx1Index = getNodeIndexByID(fxNode1);
    int fx2Index = getNodeIndexByID(fxNode2);
    int fxAttrIndex = getAttrIndexByID(fxAttr);
    if ((fx1Index > -1) && (fx2Index > -1) && (fxAttrIndex > -1)) {
      String fxNodeName1 = "";
      String fxNodeName2 = "";
      String fxAttrLabel = "";
      fxNodeName1 = nodeNames.get(fx1Index).replace("_", " ");
      fxNodeName2 = nodeNames.get(fx2Index).replace("_", " ");
      fxAttrLabel = attrLabelAs.get(fxAttrIndex).replace("_", " ");
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
      for (int k = 0; k < numEdges; k++) {
        if ((edgeAttrs.get(k) == maybeAttr)
            && (edgeBNodes.get(k) == maybeNode2)) {
          fx1Index = getNodeIndexByID(edgeANodes.get(k));
          String fxNodeName1 = nodeNames.get(fx1Index).replace("_", " ");
          String fxNodeName2 = nodeNames.get(fx2Index).replace("_", " ");
          String fxAttrLabel = attrLabelAs.get(fxAttrIndex).replace("_", " ");
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

  boolean printEdgeByAttrLabelAndNodeBName(String attr, String node) {
    boolean retval = false;
    //System.out.println("printEdgeByAttrLabelAndNodeName(" + attr + ", " + node + ")");
    int maybeAttr = getAttrIDByLabel(attr);
    int maybeNode1 = nodeIDByName(node);
    int fx2Index = -1;
    int fx1Index = getNodeIndexByID(maybeNode1);
    int fxAttrIndex = getAttrIndexByID(maybeAttr);
    if ((fx1Index > -1) && (fxAttrIndex > -1)) {
      boolean found = false;
      for (int k = 0; k < numEdges; k++) {
        if ((edgeAttrs.get(k) == maybeAttr)
            && (edgeBNodes.get(k) == maybeNode1)) {
          fx1Index = getNodeIndexByID(edgeANodes.get(k));
          String fxNodeName1 = nodeNames.get(fx1Index).replace("_", " ");
          String fxNodeName2 = nodeNames.get(fx2Index).replace("_", " ");
          String fxAttrLabel = attrLabelAs.get(fxAttrIndex).replace("_", " ");
          System.out.println("b>a: "+fxNodeName2 + " " + fxAttrLabel + " "
              + fxNodeName1);
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
          // 9/23 added length check
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
      s=fileName.replace("load ", "");
      System.out.println("loadText: attempting to open "+s);
      BufferedReader itemfile = new BufferedReader(new FileReader(s));
      System.out.println("loadText: reading "+s);
      while ((s = itemfile.readLine()) != null) {
        parse(s);
      }
      itemfile.close();
    } catch (Exception ef) {
      System.out.println("loadText: unhappy!");
      System.out.println(ef);
    }
  }
  
  public boolean nodeExists(String ts_name) {
    boolean retval = false;
    if (numNodes > 0) {
      for (int t = 0; t < numNodes; t++) {
        String q = nodeNames.get(t);
        if (q.equals(ts_name)) {
          retval = true;
          t = numNodes;
        }
      }
    }
    return retval;
  }

  public int nodeIDByName(String ts_name) {
    int retval = -1;
    if (numNodes > 0) {
      for (int t = 0; t < numNodes; t++) {
        String q = nodeNames.get(t);
        if (q.equals(ts_name)) {
          retval = nodeIDs.get(t);
          t = numNodes;
        }
      }
    }
    return retval;
  }

  public int nodeIndex(String ts_name) {
    int retval = -1;
    if (numNodes > 0) {
      for (int t = 0; t < numNodes; t++) {
        String q = nodeNames.get(t);
        if (q.equals(ts_name)) {
          retval = t;
          t = numNodes;
        }
      }
    }
    return retval;
  }

  public boolean nodeIDExists(int ts_node) {
    boolean retval = false;
    if (numNodes > 0) {
      for (int t = 0; t < numNodes; t++) {
        int q = nodeIDs.get(t);
        if (q == (ts_node)) {
          retval = true;
          t = numNodes;
        }
      }
    }
    return retval;
  }

  public int getNodeIndexByID(int ts_node) {
    int retval = -1;
    if (numNodes > 0) {
      for (int t = 0; t < numNodes; t++) {
        int q = nodeIDs.get(t);
        if (q == (ts_node)) {
          retval = t;
          t = numNodes;
        }
      }
    }
    return retval;
  }

  public void addNode(String ts_id, String ts_name) {
    if (!(nodeExists(ts_name))) {
      nodeNames.add(ts_name);
      nodeIDs.add(Integer.parseInt(ts_id));
      numItems = numItems + 1;
      numNodes = numNodes + 1;
    }
  }

  public void addNode(String ts_name) {
    addNode(Integer.toString(numItems),ts_name);
  }

  public boolean attrAExists(String ts_name) {
    boolean retval = false;
    if (numAttrs > 0) {
      for (int t = 0; t < numAttrs; t++) {
        String q = attrLabelAs.get(t);
        if (q.equals(ts_name)) {
          retval = true;
          t = numAttrs;
        }
      }
    }
    return retval;
  }

  public int attrIndex(String ts_name) {
    int retval = -1;
    if (numAttrs > 0) {
      for (int t = 0; t < numAttrs; t++) {
        String q = ""; 
        q = attrLabelAs.get(t);
        if (q.equals(ts_name)) {
          retval = t;
        }
        if (retval>=0) {t = numAttrs;}
      }
    }
    return retval;
  }

  public int getAttrIndexByID(int ts_attr) {
    int retval = -1;
    if (numAttrs > 0) {
      for (int t = 0; t < numAttrs; t++) {
        int q = attrIDs.get(t);
        if (q == (ts_attr)) {
          retval = t;
          t = numAttrs;
        }
      }
    }
    return retval;
  }

  public boolean attrIDExists(int ts_attr) {
    boolean retval = false;
    if (numAttrs > 0) {
      for (int t = 0; t < numAttrs; t++) {
        int q = attrIDs.get(t);
        if (q == (ts_attr)) {
          retval = true;
          t = numAttrs;
        }
      }
    }
    return retval;
  }

  public int getAttrByID(int ts_attr) {
    int retval = -1;
    if (numAttrs > 0) {
      for (int t = 0; t < numAttrs; t++) {
        int q = nodeIDs.get(t);
        if (q == (ts_attr)) {
          retval = t;
          t = numAttrs;
        }
      }
    }
    return retval;
  }

  public int getAttrIDByLabel(String ts_attr) {
    int retval = -1;
    if (numAttrs > 0) {
      for (int t = 0; t < numAttrs; t++) {
        String q = ""; 
        if (retval==-1) {
          q = attrLabelAs.get(t);
          if (q.equals(ts_attr)) {
            int r = attrIDs.get(t);
            retval = r;
          }
        }
      }
    }
    return retval;
  }
  
  public void addAttr(int ts_id, String ts_label) {
    if (!(attrAExists(ts_label))) {
      attrLabelAs.add(ts_label);
      attrIDs.add(ts_id);
      numItems = numItems + 1;
      numAttrs = numAttrs + 1;
    }
  }

  public void addAttr(String ts_name) {
    //System.out.println("Adding attr: "+ts_name+"/is");
    addAttr(numItems,ts_name);
  }

  public void addEdge(String ts_id, String ts_NodeA, String ts_NodeB, String ts_attr) {
    int maybeNodeA = Integer.parseInt(ts_NodeA);
    int maybeNodeB = Integer.parseInt(ts_NodeB);
    int maybeAttr = Integer.parseInt(ts_attr);
    // if ((nodeIDExists(maybeNodeA))&&
    // (nodeIDExists(maybeNodeB))&&
    // (attrIDExists(maybeAttr))) {
    edgeIDs.add(Integer.parseInt(ts_id));
    edgeANodes.add(maybeNodeA);
    edgeBNodes.add(maybeNodeB);
    edgeAttrs.add(maybeAttr);
    //System.out.println("Added edge: "+ts_NodeA+" "+ts_attr+" "+ts_NodeB);
    numItems = numItems + 1;
    numEdges = numEdges + 1;
    // }
  }

  public void listNodes() {
    System.out.println(numNodes+" nodes:");
    for (int t = 0; t < numNodes; t++) {
      String rn = nodeNames.get(t).replace("_", " ");
      System.out.println(rn);
    }
  }
  
  public void listAttrs() {
    System.out.println(numAttrs+" attributes:");
    for (int t = 0; t < numAttrs; t++) {
      String rn = attrLabelAs.get(t).replace("_", " ");
      System.out.println("\""+rn+"\"");
    }
  }

  public void listEdges() {
    System.out.println(numEdges+" edges:");
    for (int t = 0; t < numEdges; t++) {
      System.out.println(descEdgeByIndex(t));
    }
  }

  public void saveAll() {
    try {
      //BufferedWriter itemfile = new BufferedWriter(new FileWriter("itemsOut.txt"));
      BufferedWriter itemfile = new BufferedWriter(new FileWriter("fbitems.txt"));
      for (int t = 0; t < numNodes; t++) {
        String rn = nodeNames.get(t).replace(" ", "_");
        Integer rc = nodeIDs.get(t) + 0000;
        itemfile.write("n::" + rc + "::" + rn + "\n");
      }
      for (int t = 0; t < numAttrs; t++) {
        String rn1 = attrLabelAs.get(t).replace(" ", "_");
        Integer rc = attrIDs.get(t) + 0000;
        itemfile.write("a::" + rc + "::" + rn1 + "\n");
      }
      for (int t = 0; t < numEdges; t++) {
        Integer rid = edgeIDs.get(t) + 0000;
        Integer ra = edgeANodes.get(t) + 0000;
        Integer rb = edgeBNodes.get(t) + 0000;
        Integer rattr = edgeAttrs.get(t) + 0000;
        String x = descEdgeByIndex(t);
        itemfile.write("e::" + rid + "::" + ra + "::" + rb + "::" + rattr + "::" + x + "\n");
      }
      itemfile.close();
    } catch (Exception ef) {
    }
  }
  
  public void saveAllFacts() {
    try {
      BufferedWriter itemfile = new BufferedWriter(new FileWriter("factsOut.txt"));
      for (int t = 0; t < numNodes; t++) {
        String rn = nodeNames.get(t).replace(" ", "_");
        itemfile.write("add " + rn + "\n");
      }
      for (int t = 0; t < numAttrs; t++) {
        String rn1 = attrLabelAs.get(t).replace(" ", "_");
        itemfile.write("attr " + rn1 + "\n");
      }
      for (int t = 0; t < numEdges; t++) {
        String x = descEdgeByIndex(t);
        itemfile.write(x + "\n");
      }
      itemfile.close();
    } catch (Exception ef) {
    }
  }

  public void cleanup() {
    System.out.print("Cleaning up...");
    saveAll();
    saveAllFacts();
    System.out.println("Done");
  }

}
