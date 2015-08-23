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
 * The Class LocalRepairTests.
 */
public class LocalRepairTests extends TestCase {
	//2dimensional grid
	/** The nodetest. */
	Node nodetest[] = new Node[1157];
	
	/**
	 * Test correct path is produced in simple situation.
	 */
	public void testStraightLine(){
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
		//find path using lr
		lrunit.setPath(lr.calculateUnitPath(0));
		lrunit.setOptimalLength(lrunit.getPath());
		lrunit.getPath().remove(0);
		//step through path one node at a time
		while(!lrunit.isDone()){
			int square = lr.executeNextMove(0);
			//if the unit isn't done, add this new node to the unit's actual path
			if(!lr.getUnit().get(0).isDone()){
				lr.getUnit().get(0).addToActualPath(square);
			}	
			//if the unit is done, and this is the first turn it has been done, add the node to the unit's actual path
			else{
				if(lr.getUnit().get(0).getActualPath().get(lr.getUnit().get(0).getActualPath().size()-1)!=square){
					lr.getUnit().get(0).addToActualPath(square);
				}
			}
		}
		//assert optimal and actual path length
		assertEquals(lrunit.getOptimalLength(),100);
		assertEquals(lrunit.getTotalPathLength(),100);
		
	}
	
	/**
	 * Test that pathfinding works when an obstacle is in the way.
	 */
	public void testStraightLineWithImpedingObstacle(){
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
		//set starting and ending points on grid
		nodetest[100].setAsOccupied(0);
		nodetest[90].setAsEndingPoint(0);
		//create unit object
		LRUnit lrunit = new LRUnit(nodetest[100],nodetest[90]);
		//add unit to unit list
		ArrayList<LRUnit> unitsLR = new ArrayList<LRUnit>();
		unitsLR.add(lrunit);
		//place obstacle on unit's path
		nodetest[95].setAsObstacle();
		//create pathfinding object
		LocalRepair lr = new LocalRepair(unitsLR, nodetest);
		//find path for unit
		lrunit.setPath(lr.calculateUnitPath(0));
		lrunit.setOptimalLength(lrunit.getPath());
		lrunit.getPath().remove(0);
		//step through path one node at a time
		while(!lrunit.isDone()){
			int square = lr.executeNextMove(0);
			//if the unit isn't done, add this new node to the unit's actual path
			if(!lr.getUnit().get(0).isDone()){
				lr.getUnit().get(0).addToActualPath(square);
			}	
			//if the unit is done, and this is the first turn it has been done, add the node to the unit's actual path
			else{
				if(lr.getUnit().get(0).getActualPath().get(lr.getUnit().get(0).getActualPath().size()-1)!=square){
					lr.getUnit().get(0).addToActualPath(square);
				}
			}
		}
		//assert optimal and actual path lengths are correct
		assertEquals(lrunit.getOptimalLength(),108);
		assertEquals(lrunit.getTotalPathLength(),108);
	}
	
	/**
	 * Test that pathfinding works when there is a concave barrier between start and end points.
	 */
	public void testConcaveBarrierBetweenStartAndEnd(){
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
		//place starting point and ending point
		nodetest[245].setAsOccupied(0);
		nodetest[90].setAsEndingPoint(0);
		//create unit object
		LRUnit lrunit = new LRUnit(nodetest[245],nodetest[250]);
		//add unit to unit list
		ArrayList<LRUnit> unitsLR = new ArrayList<LRUnit>();
		unitsLR.add(lrunit);
		//place obstacles
		nodetest[176].setAsObstacle();
		nodetest[177].setAsObstacle();
		nodetest[178].setAsObstacle();
		nodetest[179].setAsObstacle();
		nodetest[213].setAsObstacle();
		nodetest[247].setAsObstacle();
		nodetest[281].setAsObstacle();
		nodetest[315].setAsObstacle();
		nodetest[312].setAsObstacle();
		nodetest[313].setAsObstacle();
		nodetest[314].setAsObstacle();
		//create pathfinding object
		LocalRepair lr = new LocalRepair(unitsLR, nodetest);
		//find path for unit
		lrunit.setPath(lr.calculateUnitPath(0));
		lrunit.setOptimalLength(lrunit.getPath());
		lrunit.getPath().remove(0);
		//step through path one node at a time
		while(!lrunit.isDone()){
			int square = lr.executeNextMove(0);
			//if the unit isn't done, add this new node to the unit's actual path
			if(!lr.getUnit().get(0).isDone()){
				lr.getUnit().get(0).addToActualPath(square);
			}	
			//if the unit is done, and this is the first turn it has been done, add the node to the unit's actual path
			else{
				if(lr.getUnit().get(0).getActualPath().get(lr.getUnit().get(0).getActualPath().size()-1)!=square){
					lr.getUnit().get(0).addToActualPath(square);
				}
			}
		}
		//assert optimal and actual path lengths are correct
		assertEquals(lrunit.getOptimalLength(),132);
		assertEquals(lrunit.getTotalPathLength(),132);
	}
	
	/**
	 * Test that pathfinding is correct when a collision occurs.
	 */
	public void testTwoUnitsStraightLineHeadOnCollision(){
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
		//place starting and ending points on the grid
		nodetest[100].setAsOccupied(0);
		nodetest[89].setAsOccupied(1);
		nodetest[99].setAsEndingPoint(1);
		nodetest[90].setAsEndingPoint(0);
		//create two unit objects
		LRUnit lrunit = new LRUnit(nodetest[100],nodetest[90]);
		LRUnit lrunit1 = new LRUnit(nodetest[89],nodetest[99]);
		//add units to unit list
		ArrayList<LRUnit> unitsLR = new ArrayList<LRUnit>();
		unitsLR.add(lrunit);
		unitsLR.add(lrunit1);
		//create pathfinding object
		LocalRepair lr = new LocalRepair(unitsLR, nodetest);
		//find paths for both units
		lrunit.setPath(lr.calculateUnitPath(0));
		lrunit.setOptimalLength(lrunit.getPath());
		lrunit.getPath().remove(0);
		lrunit1.setPath(lr.calculateUnitPath(1));
		lrunit1.setOptimalLength(lrunit1.getPath());
		lrunit1.getPath().remove(0);
		//step through both paths until both units are done
		while(lr.getUnitsDone() < lr.getUnit().size()){
			//set unit number to 0
    		lr.setUnitNumber(0);  
    		//for each unit, execute its next move
			for(int i = 0;i<lr.getUnit().size();i++){   	
				int square = lr.executeNextMove(i);
				//if the unit isn't done, add this new node to the unit's actual path
				if(!lr.getUnit().get(i).isDone()){
					lr.getUnit().get(i).addToActualPath(square);
				}	
				//if the unit is done, and this is the first turn it has been done, add the node to the unit's actual path
				else{
					if(lr.getUnit().get(i).getActualPath().get(lr.getUnit().get(i).getActualPath().size()-1)!=square){
						lr.getUnit().get(i).addToActualPath(square);
					}
				}
			}
		}
		//assert correct path optimal lengths for both units
		assertEquals(lrunit.getOptimalLength(),100);
		assertEquals(lrunit1.getOptimalLength(),100);
		//assert that at least one of the units had a longer than optimal path length
		assertFalse(lrunit1.getTotalPathLength()==100 && lrunit.getTotalPathLength()==100);
		
	}
}
