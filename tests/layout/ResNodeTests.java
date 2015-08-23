/**
 * Kevin Layfield
 * Dr. Wellington
 * 2009
 * Sr Research - Cooperative Pathfinding Vs. Local Repair Pathfinding
 */
package layout;

import java.util.ArrayList;

import junit.framework.TestCase;

/**
 * The Class ResNodeTests.
 */
public class ResNodeTests extends TestCase {

	/** The nodetest. */
	Node nodetest[] = new Node[1157];
	
	/** The res. */
	ResTable res = new ResTable();
	
	/**
	 * Test g value for a node at a specific point.
	 */
	public void testG(){
		for(int a=1;a<=34;a++){
	    	for(int j=1;j<=34;j++){
	    		nodetest[a+(j*34-34)]= new Node(a,j);
	    	}
	    }
		//set edge nodes as obstacles
		for(int a=1;a<=34;a++){
	    	for(int j=1;j<=34;j++){
	        	if(a==1 || j==1 || a ==34 || j==34){
	        		nodetest[a*34+j-34].setAsObstacle();
	        	}
	    	}
		}
		nodetest[100].setAsOccupied(0);
		res.getResNode().get(100).setAsOccupied(0);
		nodetest[90].setAsEndingPoint(0);
		res.getResNode().get(90).setAsEndingPoint(0);
		LRUnit lrunit = new LRUnit(nodetest[100],nodetest[90]);
		HCAUnit hcaunit = new HCAUnit(res.getResNode().get(100));
		hcaunit.setEndingPoint(res.getResNode().get(90));
		ArrayList<LRUnit> unitsLR = new ArrayList<LRUnit>();
		ArrayList<HCAUnit> unitsHCA = new ArrayList<HCAUnit>();
		unitsLR.add(lrunit);
		unitsHCA.add(hcaunit);
		LocalRepair lr = new LocalRepair(unitsLR, nodetest);
		lrunit.setPath(lr.calculateUnitPath(0));
		lrunit.setOptimalLength(lrunit.getPath());
		lrunit.getPath().remove(0);
		HCA hca = new HCA(unitsHCA,res,nodetest);
		hcaunit.setPath(hca.calculateUnitPath(0));
		hcaunit.getPath().remove(0);
		assertEquals(res.getResNode().get(1156*5 + 95).getG(),50);
	}
	
	/**
	 * Test h value for a node at a specific point.
	 */
	public void testH(){
		for(int a=1;a<=34;a++){
	    	for(int j=1;j<=34;j++){
	    		nodetest[a+(j*34-34)]= new Node(a,j);
	    	}
	    }
		//set edge nodes as obstacles
		for(int a=1;a<=34;a++){
	    	for(int j=1;j<=34;j++){
	        	if(a==1 || j==1 || a ==34 || j==34){
	        		nodetest[a*34+j-34].setAsObstacle();
	        	}
	    	}
		}
		nodetest[100].setAsOccupied(0);
		res.getResNode().get(100).setAsOccupied(0);
		nodetest[90].setAsEndingPoint(0);
		res.getResNode().get(90).setAsEndingPoint(0);
		HCAUnit hcaunit = new HCAUnit(res.getResNode().get(100));
		hcaunit.setEndingPoint(res.getResNode().get(90));
		ArrayList<HCAUnit> unitsHCA = new ArrayList<HCAUnit>();
		unitsHCA.add(hcaunit);
		HCA hca = new HCA(unitsHCA,res,nodetest);
		hcaunit.setPath(hca.calculateUnitPath(0));
		hcaunit.getPath().remove(0);
		assertEquals(res.getResNode().get(1156 * 5 + 95).getH(),50);
	}
	
	/**
	 * Test f value for a node at a specific point.
	 */
	public void testF(){
		for(int a=1;a<=34;a++){
	    	for(int j=1;j<=34;j++){
	    		nodetest[a+(j*34-34)]= new Node(a,j);
	    	}
	    }
		//set edge nodes as obstacles
		for(int a=1;a<=34;a++){
	    	for(int j=1;j<=34;j++){
	        	if(a==1 || j==1 || a ==34 || j==34){
	        		nodetest[a*34+j-34].setAsObstacle();
	        	}
	    	}
		}
		nodetest[100].setAsOccupied(0);
		res.getResNode().get(100).setAsOccupied(0);
		nodetest[90].setAsEndingPoint(0);
		res.getResNode().get(90).setAsEndingPoint(0);
		HCAUnit hcaunit = new HCAUnit(res.getResNode().get(100));
		hcaunit.setEndingPoint(res.getResNode().get(90));
		ArrayList<HCAUnit> unitsHCA = new ArrayList<HCAUnit>();
		unitsHCA.add(hcaunit);
		HCA hca = new HCA(unitsHCA,res,nodetest);
		hcaunit.setPath(hca.calculateUnitPath(0));
		hcaunit.getPath().remove(0);
		assertEquals(res.getResNode().get(1156 * 5 + 95).getF(),100);
	}
}
