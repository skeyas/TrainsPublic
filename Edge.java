import java.util.*;

public class Edge {
   private int weight;
   private Vertex start;
   private Vertex end;
   public Edge(int distance, Vertex source, Vertex dest)   {
      weight = distance;
      start = source;
      end = dest;
   }
   
   public Vertex getStart()   {
      return start;
   }
   
   public Vertex getEndpoint()   {
      return end;
   }
   
   public int getWeight()  {
      return weight;
   }
   
   public boolean equals(Edge e) {
      if(this.start.equals(e.getStart()) && this.end.equals(e.getEndpoint()))  {
         return true;
      }
      else  {
         return false;
      }
   }
}