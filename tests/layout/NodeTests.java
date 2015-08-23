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
 * The Class NodeTests.
 */
public class NodeTests extends TestCase {
	//2dimensional grid
	/** The nodetest. */
	Node nodetest[] = new Node[1157];
	
	/**
	 * Test g value for a node in specific location.
	 */
	public void testG(){
		//create grid
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
		//place unit starting and ending points on grid
		nodetest[100].setAsOccupied(0);
		nodetest[90].setAsEndingPoint(0);
		//create unit object
		LRUnit lrunit = new LRUnit(nodetest[100],nodetest[90]);
		//add unit to unit list
		ArrayList<LRUnit> unitsLR = new ArrayList<LRUnit>();
		unitsLR.add(lrunit);
		//create pathfinding object
		LocalRepair lr = new LocalRepair(unitsLR, nodetest);
		//find path for unit
		lrunit.setPath(lr.calculateUnitPath(0));
		lrunit.setOptimalLength(lrunit.getPath());
		lrunit.getPath().remove(0);
		//assert correct g value for node halfway through path
		assertEquals(nodetest[95].getG(),50);
	}
	
	/**
	 * Test h value for a node at a specific location.
	 */
	public void testH(){
		//create grid
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
		//place starting and ending points on grid
		nodetest[100].setAsOccupied(0);
		nodetest[90].setAsEndingPoint(0);
		//create unit object
		LRUnit lrunit = new LRUnit(nodetest[100],nodetest[90]);
		//add unit to unit list
		ArrayList<LRUnit> unitsLR = new ArrayList<LRUnit>();
		unitsLR.add(lrunit);
		//create pathfinding object
		LocalRepair lr = new LocalRepair(unitsLR, nodetest);
		//find path for unit
		lrunit.setPath(lr.calculateUnitPath(0));
		lrunit.setOptimalLength(lrunit.getPath());
		lrunit.getPath().remove(0);
		//assert correct h value for node halfway through path
		assertEquals(nodetest[95].getH(),50);
	}
	
	/**
	 * Test f value for a node at a specific location.
	 */
	public void testF(){
		//create grid
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
		//place starting and ending point on grid
		nodetest[100].setAsOccupied(0);
		nodetest[90].setAsEndingPoint(0);
		//create unit object
		LRUnit lrunit = new LRUnit(nodetest[100],nodetest[90]);
		//add unit to list of units
		ArrayList<LRUnit> unitsLR = new ArrayList<LRUnit>();
		unitsLR.add(lrunit);
		//create pathfinding object
		LocalRepair lr = new LocalRepair(unitsLR, nodetest);
		//find path for unit
		lrunit.setPath(lr.calculateUnitPath(0));
		lrunit.setOptimalLength(lrunit.getPath());
		lrunit.getPath().remove(0);
		//assert f value is correct for node halfway through path
		assertEquals(nodetest[95].getF(),100);
	}
}
