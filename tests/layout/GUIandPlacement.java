/**
 * Kevin Layfield
 * Dr. Wellington
 * 2009
 * Sr Research - Cooperative Pathfinding Vs. Local Repair Pathfinding
 */
package layout;


import junit.framework.TestCase;
import layout.Node.Status;

/**
 * The Class GUIandPlacement.
 */
public class GUIandPlacement extends TestCase {
	//2dimensional grid
	/** The nodetest. */
	Node nodetest[] = new Node[1157];
	//3dimensional grid
	/** The res. */
	ResTable res = new ResTable();
	
	/**
	 * Test that adding a unit is recognized.
	 */
	public void testAddUnit(){
		//creates a grid
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
		//sets node 100 as occupied
		nodetest[100].setAsOccupied(1);
		//does the same for res table
		res.getResNode().get(100).setAsOccupied(1);
		//asserts that this node is occupied for both grids
		assertEquals(nodetest[100].getStatus(),Status.occupied);
		assertEquals(res.getResNode().get(100).getStatus(),ResNode.Status.occupied);
	}
	
	/**
	 * Test the ability to remove a unit.
	 */
	public void testRemoveUnit(){
		//creates grid
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
		//sets node 100 as occupied for both algorithms
		nodetest[100].setAsOccupied(1);
		res.getResNode().get(100).setAsOccupied(1);
		//resets the node as being empty for both algorithms
		nodetest[100].setAsEmpty();
		res.getResNode().get(100).setAsEmpty();
		//asserts that the node is now empty
		assertEquals(nodetest[100].getStatus(),Status.empty);
		assertEquals(res.getResNode().get(100).getStatus(),ResNode.Status.empty);
	}
	
	/**
	 * Test clear grid functionality.
	 */
	public void testClearGrid(){
		//creates grid
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
		//sets node 100 as occupied for both algorithms
		nodetest[100].setAsOccupied(1);
		res.getResNode().get(1156+100).setAsOccupied(1);
		//sets node 105 as an obstacle for both algorithms
		nodetest[105].setAsObstacle();
		res.getResNode().get(1156+105).setAsObstacle();
		//asserts that everything is as it should be prior to clearing
		assertEquals(nodetest[100].getStatus(),Status.occupied);
		assertEquals(res.getResNode().get(1156+100).getStatus(),ResNode.Status.occupied);
		assertEquals(nodetest[105].getStatus(),Status.obstacle);
		assertEquals(res.getResNode().get(1156+105).getStatus(),ResNode.Status.obstacle);
		//make all nodes on the grid empty
    	for(int i=1;i<1157;i++){
			nodetest[i].setAsEmpty();
		}
    	//clear the reservation table
    	res.getResNode().clear();
    	//construct a new reservation table
    	res=new ResTable();
		//for all edge labels and nodes
		for(int i=1;i<=34;i++){
        	for(int j=1;j<=34;j++){
        		//make them obstacles
        		if(i==1 || j==1 || i ==34 || j==34){
            		nodetest[i+(j*34-34)].setAsObstacle();
            	}
        	}
        }
		//asserts that the previously tested nodes are now all empty
		assertEquals(nodetest[100].getStatus(),Status.empty);
		assertEquals(res.getResNode().get(1156+100).getStatus(),ResNode.Status.empty);
		assertEquals(nodetest[105].getStatus(),Status.empty);
		assertEquals(res.getResNode().get(1156+105).getStatus(),ResNode.Status.empty);
		
	}
	
	/**
	 * Test the ability to place an obstacle on the grid.
	 */
	public void testAddObstacle(){
		//creates grid
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
		//creates obstacle on node 105 for both algorithims
		nodetest[105].setAsObstacle();
		res.getResNode().get(1156+105).setAsObstacle();
		//asserts that the node has obstacle status
		assertEquals(nodetest[105].getStatus(),Status.obstacle);
		assertEquals(res.getResNode().get(1156+105).getStatus(),ResNode.Status.obstacle);
	}
	
	/**
	 * Test the ability to remove an obstacle from the grid.
	 */
	public void testRemoveObstacle(){
		//creates grid
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
		//sets node 105 as obstacle for both algorithms
		nodetest[105].setAsObstacle();
		res.getResNode().get(1156+105).setAsObstacle();
		//asserts that the node has obstacle status
		assertEquals(nodetest[105].getStatus(),Status.obstacle);
		assertEquals(res.getResNode().get(1156+105).getStatus(),ResNode.Status.obstacle);
		//resets node status to empty
		nodetest[105].setAsEmpty();
		res.getResNode().get(1156+105).setAsEmpty();
		//assert that the node is no longer an obstacle
		assertEquals(nodetest[105].getStatus(),Status.empty);
		assertEquals(res.getResNode().get(1156+105).getStatus(),ResNode.Status.empty);
	}
	
	/**
	 * Test the recognition of destination nodes on the map.
	 */
	public void testAddUnitDestination(){
		for(int a=1;a<=34;a++){
			//create grid
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
		nodetest[100].setAsOccupied(1);
		res.getResNode().get(100).setAsOccupied(1);
		//create unit with starting point 100, ending point 90
		LRUnit lrunit = new LRUnit(nodetest[100],nodetest[90]);
		HCAUnit hcaunit = new HCAUnit(res.getResNode().get(100));
		hcaunit.setEndingPoint(res.getResNode().get(90));
		//assert that the starting point is occupied
		assertEquals(nodetest[100].getStatus(),Status.occupied);
		assertEquals(res.getResNode().get(100).getStatus(),ResNode.Status.occupied);
		//assert that the ending point is recorded for the unit
		assertEquals(lrunit.getEndingPoint(),nodetest[90]);
		assertEquals(hcaunit.getEndingPoint(),res.getResNode().get(90));
		
	}
}
