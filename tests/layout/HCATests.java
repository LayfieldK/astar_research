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
 * The Class HCATests.
 */
public class HCATests extends TestCase {
	//2dimensional grid
	/** The nodetest. */
	Node nodetest[] = new Node[1157];
	//3dimensional grid
	/** The res. */
	ResTable res = new ResTable();
	
	/**
	 * Test pathfinding in simple situation.
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
		//set node 100 as occupied
		nodetest[100].setAsOccupied(0);
		res.getResNode().get(1156+100).setAsOccupied(0);
		//set ending point as node 90
		nodetest[90].setAsEndingPoint(0);
		res.getResNode().get(1156+90).setAsEndingPoint(0);
		//create a unit with start and end point
		LRUnit lrunit = new LRUnit(nodetest[100],nodetest[90]);
		HCAUnit hcaunit = new HCAUnit(res.getResNode().get(1156+100));
		hcaunit.setEndingPoint(res.getResNode().get(90));
		//add unit to lists of units
		ArrayList<LRUnit> unitsLR = new ArrayList<LRUnit>();
		ArrayList<HCAUnit> unitsHCA = new ArrayList<HCAUnit>();
		unitsLR.add(lrunit);
		unitsHCA.add(hcaunit);
		//create pathfinding objects
		LocalRepair lr = new LocalRepair(unitsLR, nodetest);
		HCA hca = new HCA(unitsHCA, res, nodetest);
		//find path for local repair
		lrunit.setPath(lr.calculateUnitPath(0));
		lrunit.setOptimalLength(lrunit.getPath());
		lrunit.getPath().remove(0);
		//move through path one node at a time
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
		//find path for hca
		hcaunit.setPath(hca.calculateUnitPath(0));
		hcaunit.getPath().remove(0);
		//assert correct optimal path length using lrunit's initial calculation
		assertEquals(lrunit.getOptimalLength(),100);
		//assert correct actual path length using hca's calculation
		assertEquals(hcaunit.getTotalPathLength(),100);
		
	}
	
	/**
	 * Test pathfinding is correct when an obstacle is in the way.
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
		//place units
		nodetest[100].setAsOccupied(0);
		res.getResNode().get(1156+100).setAsOccupied(0);
		//place destinations
		nodetest[90].setAsEndingPoint(0);
		res.getResNode().get(1156+90).setAsEndingPoint(0);
		//create units
		LRUnit lrunit = new LRUnit(nodetest[100],nodetest[90]);
		HCAUnit hcaunit = new HCAUnit(res.getResNode().get(1156+100));
		hcaunit.setEndingPoint(res.getResNode().get(1156+90));
		//add unis to unit collections
		ArrayList<LRUnit> unitsLR = new ArrayList<LRUnit>();
		ArrayList<HCAUnit> unitsHCA = new ArrayList<HCAUnit>();
		unitsLR.add(lrunit);
		unitsHCA.add(hcaunit);
		//place obstacles between start and end points
		nodetest[95].setAsObstacle();
		res.getResNode().get(1156+95).setAsObstacle();
		//create pathfinding objects
		LocalRepair lr = new LocalRepair(unitsLR, nodetest);
		HCA hca = new HCA(unitsHCA,res,nodetest);
		//calculate path to obtain optimal length
		lrunit.setPath(lr.calculateUnitPath(0));
		lrunit.setOptimalLength(lrunit.getPath());
		lrunit.getPath().remove(0);
		//move through path one node at a time
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
		//find path using hca
		hcaunit.setPath(hca.calculateUnitPath(0));
		hcaunit.getPath().remove(0);
		//assert optimal length is correct
		assertEquals(lrunit.getOptimalLength(),108);
		//assert actual length is correct
		assertEquals(hcaunit.getTotalPathLength(),108);
	}
	
	/**
	 * Test that pathfinding is correct when there is a concave barrier between the start and end points.
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
		//place starting points
		nodetest[245].setAsOccupied(0);
		res.getResNode().get(1156+245).setAsOccupied(0);
		//place ending points
		nodetest[90].setAsEndingPoint(0);
		res.getResNode().get(1156+250).setAsEndingPoint(0);
		//create units
		LRUnit lrunit = new LRUnit(nodetest[245],nodetest[250]);
		HCAUnit hcaunit = new HCAUnit(res.getResNode().get(1156+245));
		hcaunit.setEndingPoint(res.getResNode().get(1156+250));
		//add unit to unit lists
		ArrayList<LRUnit> unitsLR = new ArrayList<LRUnit>();
		ArrayList<HCAUnit> unitsHCA = new ArrayList<HCAUnit>();
		unitsLR.add(lrunit);
		unitsHCA.add(hcaunit);
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
		res.getResNode().get(1156+176).setAsObstacle();
		res.getResNode().get(1156+177).setAsObstacle();
		res.getResNode().get(1156+178).setAsObstacle();
		res.getResNode().get(1156+179).setAsObstacle();
		res.getResNode().get(1156+213).setAsObstacle();
		res.getResNode().get(1156+247).setAsObstacle();
		res.getResNode().get(1156+281).setAsObstacle();
		res.getResNode().get(1156+315).setAsObstacle();
		res.getResNode().get(1156+312).setAsObstacle();
		res.getResNode().get(1156+313).setAsObstacle();
		res.getResNode().get(1156+314).setAsObstacle();
		//create pathfinding objects
		LocalRepair lr = new LocalRepair(unitsLR, nodetest);
		HCA hca = new HCA(unitsHCA,res,nodetest);
		//obtain optimal path length by initial path calculation on lr
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
		//find actual path length using hca
		hcaunit.setPath(hca.calculateUnitPath(0));
		hcaunit.getPath().remove(0);
		//assert correct value for optimal length
		assertEquals(lrunit.getOptimalLength(),132);
		//assert correct path length
		assertEquals(lrunit.getTotalPathLength(),132);
	}
	
	/**
	 * Test pathfinding is correct when two units have a potential collision in their paths.
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
		//place starting points and ending points for two units
		nodetest[100].setAsOccupied(0);
		nodetest[89].setAsOccupied(1);
		nodetest[99].setAsEndingPoint(1);
		res.getResNode().get(1156+100).setAsOccupied(0);
		res.getResNode().get(1156+89).setAsOccupied(0);
		res.getResNode().get(1156+99).setAsEndingPoint(1);
		nodetest[90].setAsEndingPoint(0);
		res.getResNode().get(1156+90).setAsEndingPoint(0);
		//create two unit objects
		LRUnit lrunit = new LRUnit(nodetest[100],nodetest[90]);
		LRUnit lrunit1 = new LRUnit(nodetest[89],nodetest[99]);
		HCAUnit hcaunit = new HCAUnit(res.getResNode().get(1156+100));
		HCAUnit hcaunit1 = new HCAUnit(res.getResNode().get(1156+89));
		hcaunit.setEndingPoint(res.getResNode().get(1156+90));
		hcaunit1.setEndingPoint(res.getResNode().get(1156+99));
		//create lists for holding units and add the units to them
		ArrayList<LRUnit> unitsLR = new ArrayList<LRUnit>();
		ArrayList<HCAUnit> unitsHCA = new ArrayList<HCAUnit>();
		unitsLR.add(lrunit);
		unitsLR.add(lrunit1);
		unitsHCA.add(hcaunit);
		unitsHCA.add(hcaunit1);
		//create pathfinding objects
		LocalRepair lr = new LocalRepair(unitsLR, nodetest);
		HCA hca = new HCA(unitsHCA, res, nodetest);
		//obtain optimal path lengths with lr
		lrunit.setPath(lr.calculateUnitPath(0));
		lrunit.setOptimalLength(lrunit.getPath());
		lrunit.getPath().remove(0);
		lrunit1.setPath(lr.calculateUnitPath(1));
		lrunit1.setOptimalLength(lrunit1.getPath());
		lrunit1.getPath().remove(0);
		//find actual path lengths with hca
		hcaunit.setPath(hca.calculateUnitPath(0));
		hcaunit.getPath().remove(0);
		hcaunit1.setPath(hca.calculateUnitPath(1));
		hcaunit1.getPath().remove(0);
		//steps through path one node at a time
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
		//asserts correct values for optimal length for both units
		assertEquals(lrunit.getOptimalLength(),100);
		assertEquals(lrunit1.getOptimalLength(),100);
		//asserts total path lengths are correct
		assertEquals(hcaunit.getTotalPathLength(),100);
		assertEquals(hcaunit1.getTotalPathLength(),108);
		
	}
}
