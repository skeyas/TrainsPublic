import java.util.*;

public class Vertex  {
   private boolean visited;
   private char id;
   public Vertex(char name) {
      visited = false;
      id = name;
   }
   public void setStatus(boolean stat)   {
      visited = stat;
   }
   public boolean getStatus() {
      return visited;
   }
   public char getName()   {
      return id;
   }
   public boolean equals(Vertex v)  {
      if(this.id == v.getName()) {
         return true;
      }
      else  {
         return false;
      }
   }
}