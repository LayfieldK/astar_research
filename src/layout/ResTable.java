/**
 * Kevin Layfield
 * Dr. Wellington
 * 2009
 * Sr Research - Cooperative Pathfinding Vs. Local Repair Pathfinding
 */
package layout;

import java.util.ArrayList;


/**
 * The Class ResTable.
 */
public class ResTable {
	
	/** The collection of resnodes. */
	ArrayList<ResNode> resNode; 
	
	/** The current highest z value on the table (most number of time steps ahead). */
	int maxZ = 0;
	
	/**
	 * Instantiates a new res table.
	 */
	public ResTable(){
		//set the highest z value to 1
		maxZ++;
		//instantiate resNode
		resNode = new ArrayList<ResNode>();
		//adds a node that will never ever be referenced, simply a placeholder
		resNode.add(new ResNode(0,0,0));
		//creates new resNodes for the first z layer of the table
		for(int i=1;i<=34;i++){
			for(int j = 1;j<=34;j++){
				resNode.add(new ResNode(i,j,1));
			}
		}
		//sets the first z layer as all obstacles - part of an obstacle frame for the table
		//to prevent indexOutOfBounds exceptions
		for(int i=1;i<1157;i++){
			resNode.get(i).setAsObstacle();
		}
		//increase the max z layer
		maxZ++;
		//add a new layer to the table
		for(int i = 1;i<=34;i++){
			for(int j=1;j<=34;j++){
				resNode.add(new ResNode(i,j,2));
			}
		}
		//set all boundary nodes as obstacles
		for(int i = 1;i<=34;i++){
			for(int j=1;j<=34;j++){
				int gridLoc = getGridLoc(i,j,2);
				if(i == 1 || j == 1 || i == 34 || j == 34){
					resNode.get(gridLoc).setAsObstacle();
				}
			}
		}
		//add another z layer
		maxZ++;
		//set the entire layer as obstacles, completing the frame
		for(int i=1;i<=34;i++){
			for(int j=1;j<=34;j++){
				resNode.add(new ResNode(i,j,3));
				resNode.get(2312+(i*34-34+j)).setAsObstacle();
			}
		}
	}
	
	/**
	 * Gets the max z.
	 * 
	 * @return returns the maximum value of z on the table...that is, it returns the current maximum time value
	 */
	public int getMaxZ(){
		return maxZ;
	}
	
	/**
	 * increments the maximum time value.  in doing so, it adds a full layer of x and y values before the last z value on the table
	 * the last z value on the table is comprised solely of obstacles.  all obstacles on the previous z layer are transferred to the new one
	 */
	public void addToZ(){
		for(int i = 34;i>=1;i--){
			//inserts new layer of nodes before the last layer
			for(int j=34;j>=1;j--){
				resNode.add(maxZ*1156-1156+1,new ResNode(i,j,maxZ));
			}
		}
		//if the node on the layer prior to this one was an obstacle, make it an obstacle on this layer as well
		for(int i = 1;i<=34;i++){
			for(int j=1;j<=34;j++){
				int gridLoc = getGridLoc(i,j,maxZ);
				
				if(i == 1 || j == 1 || i == 34 || j == 34 || resNode.get(getGridLoc(i,j,maxZ-1)).getStatus()==(ResNode.Status.obstacle)){
					resNode.get(gridLoc).setAsObstacle();		
				}
			}
		}
		//increments z value for last layer of nodes
		for(int i = 1;i<=34;i++){
			for(int j=1;j<=34;j++){
				resNode.get(getGridLoc(i,j,maxZ+1)).incrementZ();
			}
		}
		//increment z value
		maxZ++;
		
	}
	
	/**
	 * locks in the unit's path through the table so that future units don't use the x and y values that this unit uses at the same time.
	 * 
	 * @param path is the arraylist of integers that represent the node numbers of the unit's path through the table
	 * @param unitNum is the number of the unit whose path is being locked
	 */
	public void lockPath(ArrayList<Integer> path, int unitNum){
		for(int i =0;i<path.size();i++){
			resNode.get(path.get(i)).setAsOccupied(unitNum);
		}
	}
	
	/**
	 * Erase path.
	 * erases the path of the designated unit from the table
	 * 
	 * @param unitNum the unit number
	 */
	public void erasePath(int unitNum){
		for(int i = 0;i<resNode.size();i++){
			if(resNode.get(i).getOccupier()==unitNum){
				resNode.get(i).setAsEmpty();
			}
		}
	}
	
	/**
	 * Gets the grid location.
	 * 
	 * @param i is the x value
	 * @param j is the y value
	 * @param z is the z value
	 * 
	 * @return returns a converted value that denotes the number of the node in the 3 dimensional table given the 3 dimensions
	 */
	public int getGridLoc(int i, int j, int z){
		
		int gridLoc = i+(j*34-34) + (z*1156-1156);
		return gridLoc;
	}
	
	/**
	 * Gets the res node.
	 * 
	 * @return returns the list of resnodes held within the restable
	 */
	public ArrayList<ResNode> getResNode(){
		return resNode;
	}

	/**
	 * Sets the to originals.
	 */
	public void setToOriginals() {
		//sets each of the nodes on the table equal to their original values before pathfinding
		for(int i = 2313;i<getResNode().size();i++){
			getResNode().get(i).setToOriginals();
		}
	}
	
}
