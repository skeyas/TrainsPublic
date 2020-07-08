import java.util.*;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException; 
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.Object;

public class Graph {
   private static Map<Vertex, List<Edge>> adjMap; //implement adjacency list using a map
   
   //constructor
   
   public Graph() {
      adjMap = new HashMap<Vertex, List<Edge>>();
   }
   
   //populate graph with vertices and edges defined in a text file 
   private boolean fromFileConstruct(String filename) {
      BufferedReader f;
      boolean flag = false;
      boolean custom = false;
      try   {
         f = new BufferedReader(new FileReader(filename));
         String[] tokenizedString;
         try   {
            String line = f.readLine();
            while(line != null)  {
               tokenizedString = line.split("\\s+|,\\s*|\\.\\s*"); //use regular expressions to split string
               for (String token : tokenizedString)   { //iterate through token to find vertex names and weight of edge
                  if(token.toUpperCase().equals("CUSTOMIZE")) {
                     custom = true;
                  }
                  char a = '~';
                  char b = '~';
                  int c = Integer.parseInt(token.replaceAll("[\\D]", "")); //find integer in token
                  for(int i = 0; i < token.length(); i++)   {
                     if(Character.isLetter(token.charAt(i)))  {
                        if(a == '~')   {
                           a = token.charAt(i); //set a equal to character
                        }
                        else  {
                           b = token.charAt(i); //set b equal to character
                           break;
                        }
                     }
                  }
                  this.addEdge(a, b, c); //add edge with source a, destination b, and weight c to the graph
               }
               line = f.readLine(); //read the next line
            }
         }
         catch (IOException e) {
            System.out.println("Empty file.");
         }
      }
      catch (FileNotFoundException fe) {
         System.out.println("File not found.");
      }
      return custom;
   }
   
   //add edge to graph
   
   protected void addEdge(char source, char dest, int weight)  {
      boolean flag = false;
      boolean inList = false;
      Vertex v = new Vertex(source);
      Vertex u = new Vertex(dest);
      Edge vu = new Edge(weight, v, u);
      for(Map.Entry<Vertex,List<Edge>> entry : adjMap.entrySet()) {
         if(entry.getKey().equals(v))  { //if source found in map add destination to list
            flag = true;
            for(Edge e : entry.getValue())   { //iterate through list
               if(e.getEndpoint().equals(u)) { //if endpoint already in list do not add again
                  inList = true;
               }
            }
            if(inList == false) { //add edge to list
               entry.getValue().add(vu);  
            }
         }
      }
      if(flag == false) { //if source not found in map, add it
         List<Edge> lst = new ArrayList<Edge>();
         lst.add(vu);
         adjMap.put(v, lst);
      }
   }
   
   //find edge between two vertices
   
   private Edge findEdge(Vertex source, Vertex dest)   {
      for(Map.Entry<Vertex,List<Edge>> entry : adjMap.entrySet()) { //check if vertices are both in map
         if(entry.getKey().equals(source))   {
            for(Edge e: entry.getValue())   { //iterate through list;
               if(e.getEndpoint().equals(dest)) {
                  return e;
               }
            }
         }
      }
      return null;
   }
   
   //call to recursive function
   
   protected int findShortest(Vertex source, Vertex dest) {
      this.clear();
      int shortest = findShortest(source, dest, 0, 0);
      return shortest;
   }
   
   //find shortest path between two vertices
      
   private int findShortest(Vertex source, Vertex dest, int distance, int shortestPath){
      boolean startInMap = false;
      boolean endInMap = false;
      List<Edge> lst = null;
      for(Map.Entry<Vertex,List<Edge>> entry : adjMap.entrySet()) { //check if vertices are both in map
         if(entry.getKey().equals(source))  {
            startInMap = true;
            lst = entry.getValue();
         }
         if(entry.getKey().equals(dest))   {
            endInMap = true;
         }
      }
            
      if(startInMap && endInMap) {
         source.setStatus(true);
         for(int i = 0; i < lst.size(); i++) { //iterate through list
            Edge e = lst.get(i);
            if(e.getEndpoint().equals(dest))  { //if reached the destination update distance
               distance = distance + e.getWeight();
               if(shortestPath == 0 || distance < shortestPath)  { //if this is the first time through the function
                                                                   //or the distance is less than the current shortest
                                                                   //path update the shortest path
                  shortestPath = distance;
               }
               source.setStatus(false); //reset status of vertex
               return shortestPath;
            }
            if(e.getEndpoint().getStatus() == false)  { ///if vertex hasn't been visited update the distance
               distance = distance + e.getWeight();
               shortestPath = findShortest(e.getEndpoint(), dest, distance, shortestPath);
               distance = distance - e.getWeight();
            }
         }
      }
      else  {
         System.out.println("NO SUCH ROUTE");
      }
      
      return shortestPath;
  }
  
  //call to the recursive function
  
  public int numPathsDistance(Vertex source, Vertex dest, int distance)   {
      this.clear();
      return numPathsDistance(source, dest, distance, 0);
  }
   
   //compute the number of paths where the total distance is less than a given distance
   
   private int numPathsDistance(Vertex source, Vertex dest, int distance, int currWeight)  {
      boolean startInMap = false;
      boolean endInMap = false;
      int numPaths = 0;
      List<Edge> lst = null;
      
      for(Map.Entry<Vertex,List<Edge>> entry : adjMap.entrySet()) { //check if both vertices are in the map
         if(entry.getKey().equals(source))  {
            startInMap = true;
            lst = entry.getValue();
         }
         if(entry.getKey().equals(dest))  {
            endInMap = true;
         }
      }
      
      if(startInMap && endInMap) {
         for(int i = 0; i < lst.size(); i++) {
            Edge e = lst.get(i);
            currWeight = currWeight + e.getWeight(); //update current path weight
            if(currWeight < distance) {
               if(e.getEndpoint().equals(dest)) {
                  numPaths++; //update number of paths
                  numPaths = numPaths + numPathsDistance(e.getEndpoint(), dest, distance, currWeight); //recursively call function
                  continue;
               }
               else  {
                  numPaths = numPaths + numPathsDistance(e.getEndpoint(), dest, distance, currWeight); //recursively call function
                  currWeight = currWeight - e.getWeight();  //backtrack             
               }
            }
            else  {
               currWeight = currWeight - e.getWeight();
            }
         }
      }
      else  {
         System.out.println("NO SUCH ROUTE"); //no route exists that can satisfy the parameters
      }
      return numPaths;
   }
   
   //function to call recursive function
   
   protected int maxStopsPaths(Vertex source, Vertex dest, int max)  {
      int paths = maxStopsPaths(source, dest, max, 0);
      this.clear();
      return paths;
   }
   
   //compute the number of paths with a given maximum number of stops
   
   private int maxStopsPaths(Vertex source, Vertex dest, int max, int currDepth)  {
      boolean startInMap = false;
      boolean endInMap = false;
      int numPaths = 0;
      List<Edge> lst = null;
      
      for(Map.Entry<Vertex,List<Edge>> entry : adjMap.entrySet()) { //check if both source and destination are in the map
         if(entry.getKey().equals(source))  {
            startInMap = true;
            lst = entry.getValue();
         }
         if(entry.getKey().equals(dest))   {
            endInMap = true;
         }
      }
      
      if(startInMap && endInMap) {
         if(currDepth == max) {
            return 0;
         }
         currDepth++;
         source.setStatus(true);
         for(int i = 0; i < lst.size(); i++) { //iterate through list
            Edge e = lst.get(i);
            if(e.getEndpoint().equals(dest))  { //if destination reached, update the number of paths
               numPaths++;
               continue;
            }
            else if(e.getEndpoint().getStatus() == false)  {
               numPaths = numPaths + maxStopsPaths(e.getEndpoint(), dest, max, currDepth); //recursively call function
               currDepth = currDepth - 1; //decrement
            }
         }
      }
      else  {
         System.out.println("NO SUCH ROUTE");
      }
      return numPaths;
   }
   
   //return the distance of a route with every vertex provided
   
   protected int distRoute(List<Vertex> vList) {
      List<Edge> lst = new ArrayList<Edge>();
      int distance = 0;
      for(int i = 0; i < vList.size() - 1; i++) { //iterate through list
         Edge e = findEdge(vList.get(i), vList.get(i+1)); //find the edge between each vertex and the next in the list
         if(e == null)  {
            System.out.println("NO SUCH ROUTE"); //if no edge exists no route exists
            return -1;
         }
         else  {
            distance = distance + e.getWeight();
         }
      }
      return distance;
   }
   
   //set all vertices in graph to not visited
   
   private void clear() {
      for(Map.Entry<Vertex, List<Edge>> entry: adjMap.entrySet()) { //iterate through map
         List<Edge> lst = new ArrayList<Edge>();
         lst = entry.getValue();
         for(int i = 0; i < lst.size(); i++) { //iterate through list
            lst.get(i).getStart().setStatus(false);
            lst.get(i).getEndpoint().setStatus(false);
         }
      }
   }
   
   //function to call recursive function
   
   protected int numPathsExactStops(Vertex source, Vertex dest, int max) {
      this.clear(); //ensure status of all vertices set to unvisited
      return numPathsExactStops(source, dest, max, 0);
   }
   
   //compute the number of paths with an exact number of stops
   
   private int numPathsExactStops(Vertex source, Vertex dest, int stops, int currDepth) {
      int total = maxStopsPaths(source, dest, stops, currDepth) - maxStopsPaths(source, dest, stops - 1, currDepth); //call function that calculates 
                                                                                                                     //number of paths with at most stop stops
                                                                                                                     //call function that calculates number of 
                                                                                                                     //paths with at most (stop - 1) stops
                                                                                                                     //return diffence
      return total;
   }
   
   //print map both to console and to a text file
   
   private BufferedWriter printMap(String filename) throws IOException  {
      BufferedWriter w = null;
      String outputFile = "";
      
      //create output file
      try   {
         for(int i = 0; i < filename.length(); i++)   {
            if(filename.charAt(i) != '.')  {
               outputFile = outputFile + filename.charAt(i);
            }
            else  {
               outputFile = outputFile + "Output" + filename.charAt(i);
            }
         }
         w = new BufferedWriter(new FileWriter(outputFile));
      }
      catch (IOException ex) {
         System.out.println("Failed");
      }
      w.write("Map:");
      w.newLine();
      for(Map.Entry<Vertex, List<Edge>> entry: adjMap.entrySet()) { //iterate through map
         w.write("\t");
         System.out.print(entry.getKey().getName() + ": "); //print source vertex
         if(w != null)  {
            w.write(entry.getKey().getName() + ": ");   //write source vertex to file
         }
         for(int i = 0; i < entry.getValue().size(); i++) { //iterate through list
            System.out.print(entry.getValue().get(i).getEndpoint().getName() + " "); //print name of endpoint
            if(w != null)  {
               w.write(entry.getValue().get(i).getEndpoint().getName() + " "); //write endpoint to file
            }
         }
         System.out.println();
         if(w != null)  {
            w.newLine();
         }
      }
      System.out.println();
      w.newLine();
      return w;
   }
   
   //by default compute and output everything requested in the question
   
   private void defaultCall(String filename) throws IOException {
      BufferedWriter w = printMap(filename);
      Vertex a = new Vertex('A');
      Vertex b = new Vertex('B');
      Vertex c = new Vertex('C');
      Vertex d = new Vertex('D');
      Vertex e = new Vertex('E');
      
      List<Vertex> l = new ArrayList<Vertex>();
      l.add(a);
      l.add(b);
      l.add(c);
      
      String outputFile = "";
      
      for(int i = 0; i < filename.length(); i++)   {
         if(filename.charAt(i) != '.') {
            outputFile = outputFile + filename.charAt(i);
         }
         else  {
            outputFile = outputFile + "Output.";
         }
      }
            
      int dist = distRoute(l);
      if(dist != -1) {
         System.out.println(dist);
         w.write(String.valueOf(dist));
         w.newLine();
      }
      else  {
         w.write("NO SUCH ROUTE");
         w.newLine();
      }
      l.clear();
      
      l.add(a);
      l.add(d);
      dist = distRoute(l);
      if(dist != -1) {
         System.out.println(dist);
         w.write(String.valueOf(dist));
         w.newLine();
      }
      else  {
         w.write("NO SUCH ROUTE");
         w.newLine();
      }
      
      l.add(c);
      dist = distRoute(l);
      if(dist != -1) {
         System.out.println(dist);
         w.write(String.valueOf(dist));
         w.newLine();
      }
      else  {
         w.write("NO SUCH ROUTE");
         w.newLine();
      }
      
      l.clear();
      l.add(a);
      l.add(e);
      l.add(b);
      l.add(c);
      l.add(d);
      dist = distRoute(l);
      if(dist != -1) {
         System.out.println(dist);
         w.write(String.valueOf(dist));
         w.newLine();
      }
      else  {
         w.write("NO SUCH ROUTE");
         w.newLine();
      }
      
      l.clear();
      l.add(a);
      l.add(e);
      l.add(d);
      dist = distRoute(l);
      if(dist != -1) {
         System.out.println(dist);
         w.write(String.valueOf(dist));
         w.newLine();
      }
      else  {
         w.write("NO SUCH ROUTE");
         w.newLine();
      }
      
      int max = maxStopsPaths(c, c, 3);
      System.out.println(max);
      w.write(String.valueOf(max));
      w.newLine();
      
      int exact = numPathsExactStops(a, c, 4);
      System.out.println(exact);
      w.write(String.valueOf(exact));
      w.newLine();
      
      int shortest = findShortest(a, c);
      if(shortest != 0)   {
         System.out.println(shortest);
         w.write(String.valueOf(shortest));
         w.newLine();
      }
      else  {
         w.write("NO SUCH ROUTE");
         w.newLine();
      }
      
      shortest = findShortest(b, b);
      if(shortest != 0)   {
         System.out.println(shortest);
         w.write(String.valueOf(shortest));
         w.newLine();
      }
      else  {
         w.write("NO SUCH ROUTE");
         w.newLine();
      }
      
      int numRoutes = numPathsDistance(c, c, 30);
      System.out.println(numRoutes);
      w.write(String.valueOf(numRoutes));
      w.newLine();
      w.close();
   }
   
   //option to customize which functions are called and which parameters are used
   
   private void customCall(String filename) throws IOException  {
      BufferedWriter w = printMap(filename);
      boolean flag = false;
      BufferedReader r = new BufferedReader(new InputStreamReader(System.in));
      while(flag == false) {
         System.out.println("Which of the following would you like to compute?"); //output menu
         System.out.println("\t1. Distance of route");
         System.out.println("\t2. Number of trips starting at a point and ending at another point with a maximum number of stops");
         System.out.println("\t3. Number of trips starting at a point and ending at another point with an exact number of stops");
         System.out.println("\t4. Length of the shortest route between two points");
         System.out.println("\t5. Number of different routes between two points with a maximum distance");
         int option = Integer.parseInt(r.readLine());
         if(option == 1)  {
            List<Vertex> l = new ArrayList<Vertex>();
            System.out.println("How many stops are in your route?");
            int num = Integer.parseInt(r.readLine());
            for(int i = 0; i < num; i++)  {
               System.out.println("Enter stop name:");
               Vertex city = new Vertex(r.readLine().charAt(0));
               l.add(city);
            }
            int dist = distRoute(l);
            if(dist != -1) {
               System.out.println(dist);
               w.write(String.valueOf(dist));
               w.newLine();
            }
            else  {
               w.write("NO SUCH ROUTE");
               w.newLine();
            }
         }
         else if(option == 2)  {
            System.out.println("Source?");
            Vertex source = new Vertex(r.readLine().charAt(0));
            System.out.println("Destination?");
            Vertex dest = new Vertex(r.readLine().charAt(0));
            System.out.println("How many stops is the maximum?");
            int max = Integer.parseInt(r.readLine());
            int paths = maxStopsPaths(source, dest, max);
            System.out.println(paths);
            w.write(String.valueOf(paths));
            w.newLine();
         }
         else if(option == 3)  {
            System.out.println("Source?");
            Vertex source = new Vertex(r.readLine().charAt(0));
            System.out.println("Destination?");
            Vertex dest = new Vertex(r.readLine().charAt(0));
            System.out.println("How many stops?");
            int stops = Integer.parseInt(r.readLine());
            int exact = numPathsExactStops(source, dest, stops);
            System.out.println(exact);
            w.write(String.valueOf(exact));
            w.newLine();
         }
         else if(option == 4)  {
            System.out.println("Source?");
            Vertex source = new Vertex(r.readLine().charAt(0));
            System.out.println("Destination?");
            Vertex dest = new Vertex(r.readLine().charAt(0));
            int dist = findShortest(source, dest);
            if(dist != 0)  {
               System.out.println(dist);
               w.write(String.valueOf(dist));
               w.newLine();
            }
            else  {
               w.write("NO SUCH ROUTE");
               w.newLine();
            }
         }
         else if(option == 5)  {
            System.out.println("Source?");
            Vertex source = new Vertex(r.readLine().charAt(0));
            System.out.println("Destination?");
            Vertex dest = new Vertex(r.readLine().charAt(0));
            System.out.println("Maximum distance?");
            int dist = Integer.parseInt(r.readLine());
            int num = numPathsDistance(source, dest, dist);
            System.out.println(num);
            w.write(String.valueOf(num));
            w.newLine();
         }
         else  {
            System.out.println("Not a valid response.");
         }
         System.out.println();
         System.out.println("Would you like to compute something else? (Y/N)");
         String s = r.readLine();
         if(s.toUpperCase().equals("NO") || s.charAt(0) == 'N' || s.charAt(0) == 'n')  {
            flag = true;
            break;
         }
      }
      w.close();   
   }
   
   public static void main(String[] args) throws IOException {
      System.out.print("Enter file name, including extension: ");
      BufferedReader r = new BufferedReader(new InputStreamReader(System.in));
      String filename = r.readLine();
      Graph g = new Graph(); //call constructor
      boolean custom = g.fromFileConstruct(filename); //call function to populate graph from file data
      if(custom == false)  {
         g.defaultCall(filename);
      }
      else  {
         g.customCall(filename);
      }
   }
}