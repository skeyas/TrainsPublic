import static org.junit.Assert.*;
 
import java.util.*;
 
import org.junit.BeforeClass;
import org.junit.Test;
 
public class GraphTest {
	static Graph g;
	static Vertex a, b, c, d, e;
 
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		g = new Graph();
 
      g.addEdge('A', 'B', 5);
      g.addEdge('B', 'C', 4);
      g.addEdge('C', 'D', 8);
      g.addEdge('D', 'C', 8);
      g.addEdge('D', 'E', 6);
      g.addEdge('A', 'D', 5);
      g.addEdge('C', 'E', 2);
      g.addEdge('E', 'B', 3);
      g.addEdge('A', 'E', 7);      
      
		a = new Vertex('A');
		b = new Vertex('B');
		c = new Vertex('C');
		d = new Vertex('D');
		e = new Vertex('E');
	}
 
	@Test
	public void testDistRouteABC() throws Exception {
      List<Vertex> l = new ArrayList<Vertex>();
		l.add(a);
		l.add(b);
		l.add(c);
		assertEquals(9, g.distRoute(l));
	}
 
	@Test
	public void testDistRouteAD() throws Exception {
		List<Vertex> l = new ArrayList<Vertex>();
		l.add(a);
		l.add(d);
      assertEquals(5, g.distRoute(l));
	}
 
	@Test
	public void testDistRouteADC() throws Exception  {
		List<Vertex> l = new ArrayList<Vertex>();
		l.add(a);
		l.add(d);
      l.add(c);
      assertEquals(13, g.distRoute(l));
	}
 
	@Test
	public void testDistRouteAEBCD() throws Exception  {
		List<Vertex> l = new ArrayList<Vertex>();
		l.add(a);
      l.add(e);
      l.add(b);
      l.add(c);
      l.add(d);
      assertEquals(22, g.distRoute(l));
	}
 
	@Test
	public void testDistRouteAED() throws Exception  {
		List<Vertex> l = new ArrayList<Vertex>();
		l.add(a);
		l.add(e);
      l.add(d);
      assertEquals(-1, g.distRoute(l));
	}
 
	@Test
	public void testMaxStopsPathCC3() throws Exception {
		int num = g.maxStopsPaths(c, c, 3);
		assertEquals(2, num);
	}
 
	@Test
	public void testNumPathsExactStopsAC4() throws Exception {
		int exact = g.numPathsExactStops(a, c, 4);
		assertEquals(3, exact);
	}
 
	@Test
	public void testFindShortestAC() throws Exception {
		int shortest = g.findShortest(a, c);
		assertEquals(9, shortest);
	}
 
	@Test
	public void testFindShortestBB() throws Exception {
		int shortest = g.findShortest(b, b);
		assertEquals(9, shortest);
	}
 
	@Test
	public void testNumPathsDistanceCC30() throws Exception {
		int numRoutes = g.numPathsDistance(c, c, 30);
		assertEquals(7, numRoutes);
	} 
}