/**
 * Kevin Layfield
 * Dr. Wellington
 * 2009
 * Sr Research - Cooperative Pathfinding Vs. Local Repair Pathfinding
 */
package layout;

import java.util.ArrayList;

/**
 * The Class HCAUnit.
 */
public class HCAUnit {
	
	/** The destination coordinates. */
	int locX,locY,targX,targY;
	
	/** The open list. */
	ResNode openList[];
	
	/** The closed list. */
	ResNode closedList[];
	
	/** The unit's current node. */
	ResNode node;
	
	/** The starting point. */
	ResNode startingPoint;
	
	/** The ending point. */
	ResNode endingPoint;
	
	/** The number of cycles for this unit. */
	int cycles =0;
	
	/** The optimal length for this unit. */
	int optimalLength;
	
	/** The value that tracks a unit's pathfinding completion. */
	boolean done = false;
	
	/** The calculated path. */
	ArrayList<Integer> path = new ArrayList<Integer>();
	
	/** The actual path. */
	ArrayList<Integer> actualPath = new ArrayList<Integer>();
	
	/** The integers checked when calculating cycles. */
	ArrayList<Integer> intsChecked = new ArrayList<Integer>();
	
	/** The length of the path. */
	int length;
	
	/**
	 * Instantiates a new hCA unit.
	 * 
	 * @param node the node
	 */
	public HCAUnit(ResNode node){
		this.node = node;
		startingPoint=node;
		locX = node.getX();
		locY = node.getY();
		
	}
	
	/**
	 * Sets the path.
	 * 
	 * @param path is the series of numbers that represent where this unit will occupy on the restable
	 */
	public void setPath(ArrayList<Integer> path){
		this.path=path;
		//actual path is always the same as calculated path for HCA
		setActualPath(path);
		//total cost for a given path
		int moveCostTotal=0;
		//calculates total cost
		for(int i = 1;i<path.size();i++){
			//if the currently checked move was made diagonally, add 14 to move cost
			if(path.get(i)-path.get(i-1)==1121 || path.get(i)-path.get(i-1)==1123 || path.get(i)-path.get(i-1)==1189 || path.get(i)-path.get(i-1)==1191){
				moveCostTotal+=14;
			}
			//if the checked move was adjacent or no move at all, add 10 to move cost
			else{
				moveCostTotal+=10;
			}
		}
		//sets the total path length to what was just calculated
		setTotalPathLength(moveCostTotal);
	}
	
	/**
	 * Sets the total path length.
	 * 
	 * @param i the new total path length
	 */
	public void setTotalPathLength(int i){
		length=i;
	}
	
	/**
	 * Gets the path.
	 * 
	 * @return returns the path for this unit
	 */
	public ArrayList<Integer> getPath(){
		return path;
	}
	
	/**
	 * Adds the to actual path.
	 * 
	 * @param path the path
	 */
	public void setActualPath(ArrayList<Integer> path){
		for(int i =0;i<path.size();i++){
			actualPath.add(path.get(i));
		}
	}
	
	/**
	 * Gets the total path length.
	 * 
	 * @return returns the number of turns it took the unit to reach its goal
	 */
	public int getTotalPathLength(){
		return length;
	}
	
	/**
	 * Gets the cycles.
	 * 
	 * @return returns the number of times this unit occupied an ununique space
	 */
	public int getCycles(){
		//clears the array that contains node numbers that have already been checked
		intsChecked.clear();
		//resets number of cycles
		cycles=0;
		//checks each node in the path
		for(int i = 0;i<actualPath.size();i++){
			//if the number of the current node hasn't already been checked for cycles
			if(!intsChecked.contains(actualPath.get(i)%1156)){
				//count how many more times this node value appears in the path
				for(int j = i+1;j<actualPath.size();j++){
					int check1=actualPath.get(i)%1156;
					int check2=actualPath.get(j)%1156;
					if(check1==check2){
						cycles++;
					}
				}
				//adds node number to the list of nodes that were already checked
				intsChecked.add(actualPath.get(i)%1156);
			}
		}
		
		return cycles;
	}
	
	/**
	 * Sets the ending point.
	 * 
	 * @param endingPoint is the node that is this unit's goal location
	 */
	public void setEndingPoint(ResNode endingPoint){
		this.endingPoint=endingPoint;
		targX=endingPoint.getX();
		targY=endingPoint.getY();
	}
	
	/**
	 * Gets the ending point.
	 * 
	 * @return returns the Node that represents this unit's destination
	 */
	public ResNode getEndingPoint(){
		return endingPoint;
	}
	
	/**
	 * Gets the y.
	 * 
	 * @return return this unit's y value
	 */
	public int getY(){
		return locY;
	}
	
	/**
	 * Gets the x.
	 * 
	 * @return returns this unit's x value
	 */
	public int getX(){
		return locX;
	}
	
	/**
	 * Sets the y.
	 * 
	 * @param y is the unit's new y value
	 */
	public void setY(int y){
		this.locY=y;
	}
	
	/**
	 * Sets the x.
	 * 
	 * @param x is the unit's new x value
	 */
	public void setX(int x){
		this.locX=x;
	}
	
	/**
	 * Gets the targ x.
	 * 
	 * @return returns the x value of the destination
	 */
	public int getTargX(){
		return targX;
		
	}
	
	/**
	 * Gets the targ y.
	 * 
	 * @return returns the y value of the destination
	 */
	public int getTargY(){
		return targY;
		
	}
	
	/**
	 * Checks if is done.
	 * 
	 * @return returns true if the unit has reached its destination
	 */
	public boolean isDone(){
		return done;
	}
	
	/**
	 * denotes that this unit has reached its destination.
	 */
	public void setDone(){
		done=true;
	}
	
	/**
	 * Gets the node.
	 * 
	 * @return returns the node that this unit currently occupies
	 */
	public ResNode getNode(){
		return node;
	}
	
	/**
	 * Sets the node.
	 * 
	 * @param node is the node that is being set as this unit's current node
	 */
	public void setNode(ResNode node){
		this.node = node;
	}
	
	/**
	 * Sets the optimal length.
	 * 
	 * @param length is the value being denoted as this unit's optimum path length
	 */
	public void setOptimalLength(int length){
		optimalLength = length;
	}
	
	/**
	 * Gets the optimal length.
	 * 
	 * @return returns the distance from the start to end for this unit if there were no other units on the grid
	 */
	public int getOptimalLength() {
		return optimalLength;
	}

	/**
	 * Sets all values relating to the unit's path to what they were before the path was found.
	 */
	public void setToOriginals() {
		//sends unit back to starting point
		setNode(startingPoint);
		//set x coordinate to original value
		setX(startingPoint.getX());
		//set y coordinate to original value
		setY(startingPoint.getY());
		//denotes that the unit is not done finding its path
		done=false;
		//erases path
		actualPath.clear();
		//resets path length
		setTotalPathLength(0);
		path.clear();
	}
}
