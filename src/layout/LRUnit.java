/**
 * Kevin Layfield
 * Dr. Wellington
 * 2009
 * Sr Research - Cooperative Pathfinding Vs. Local Repair Pathfinding
 */
package layout;

import java.util.ArrayList;

/**
 * The Class LRUnit.
 */
public class LRUnit {
	
	/** The unit's destination coordinates. */
	int locX,locY,targX,targY;
	
	/** The open list. */
	Node openList[];
	
	/** The closed list. */
	Node closedList[];
	
	/** The unit's current node. */
	Node node;
	
	/** The starting point. */
	Node startingPoint;
	
	/** The ending point. */
	Node endingPoint;
	
	/** The number of cycles. */
	int cycles =0;
	
	/** The unit's optimal length. */
	int optimalLength;
	
	/** Denotes whether or not a unit has reached its destination. */
	boolean done = false;
	
	/** The calculated path. */
	ArrayList<Integer> path;
	
	/** The actual path. */
	ArrayList<Integer> actualPath = new ArrayList<Integer>();
	
	/** The ints checked during cycle calculation. */
	ArrayList<Integer> intsChecked = new ArrayList<Integer>();
	
	/**
	 * Instantiates a new lR unit.
	 * 
	 * @param node the unit's starting node
	 */
	public LRUnit(Node node){
		this.node = node;
		startingPoint=node;
		locX = node.getX();
		locY = node.getY();
		//add starting location to unit's actual path
		actualPath.add(locX+locY*34-34);
	}
	
	/**
	 * Instantiates a new lR unit.
	 * 
	 * @param node the unit's starting node
	 * @param endingPoint the unit's ending node
	 */
	public LRUnit(Node node,Node endingPoint){
		this.node = node;
		startingPoint=node;
		locX = node.getX();
		locY = node.getY();
		//add starting location to unit's actual path
		actualPath.add(locX+locY*34-34);
		this.endingPoint = endingPoint;
		targX=endingPoint.getX();
		targY=endingPoint.getY();
	}
	
	/**
	 * Sets the unit's path.
	 * 
	 * @param path is the projected path for this unit in terms of integers that represent grid locations
	 */
	public void setPath(ArrayList<Integer> path){
		this.path=path;
	}
	
	/**
	 * Gets the path.
	 * 
	 * @return returns the current projected path for the unit
	 */
	public ArrayList<Integer> getPath(){
		return path;
	}
	
	/**
	 * Adds the to actual path.
	 * 
	 * @param node is the node being added to the unit's actual path
	 */
	public void addToActualPath(int node){
		actualPath.add(node);
	}
	
	/**
	 * Gets the actual path.
	 * 
	 * @return the actual path
	 */
	public ArrayList<Integer> getActualPath(){
		return actualPath;
	}
	
	/**
	 * Gets the total path length.
	 * 
	 * @return returns the total number of turns that this unit took to reach its destination
	 */
	public int getTotalPathLength(){
		//total path length
		int length=0;
		//for each node in the actual path array
		for(int i = 1; i <actualPath.size();i++){
			//if the current move is a diagonal one, add 14 to the total path length
			if(actualPath.get(i)-actualPath.get(i-1)==33 || actualPath.get(i)-actualPath.get(i-1)==35 ||actualPath.get(i)-actualPath.get(i-1)==-33 ||actualPath.get(i)-actualPath.get(i-1)==-35){
				length+=14;
			}
			//if the current move is an adjacent one, add 10 to the total path length
			else{
				length+=10;
			}
		}
		return length;
	}
	
	/**
	 * Gets the cycles.
	 * 
	 * @return returns the number of times this unit occupied an ununique space
	 */
	public int getCycles(){
		//clear the list of node numbers checked for cycles
		intsChecked.clear();
		//reset number of cycles
		cycles=0;
		//for each node number on the actual path
		for(int i = 0;i<actualPath.size();i++){
			//if the current node number is not on the list of node numbers already checked
			if(!intsChecked.contains(actualPath.get(i))){
				//count the number of times this node number appears through the rest of the path
				for(int j = i+1;j<actualPath.size();j++){
					if(actualPath.get(i).equals(actualPath.get(j))){
						cycles++;
					}
				}
				//add this node number to the list of node numbers checked
				intsChecked.add(actualPath.get(i));
			}
		}
		
		return cycles;
	}
	
	/**
	 * Sets the ending point.
	 * 
	 * @param endingPoint is the node being set as this unit's destination
	 */
	public void setEndingPoint(Node endingPoint){
		this.endingPoint=endingPoint;

	}
	
	/**
	 * Gets the ending point.
	 * 
	 * @return returns the node that represents the destination for this unit
	 */
	public Node getEndingPoint(){
		return endingPoint;
	}
	
	/**
	 * Gets the y.
	 * 
	 * @return returns current y value for this unit
	 */
	public int getY(){
		return locY;
	}
	
	/**
	 * Gets the x.
	 * 
	 * @return returns current x value for this unit
	 */
	public int getX(){
		return locX;
	}
	
	/**
	 * Sets the y.
	 * 
	 * @param y is the new y value for this unit
	 */
	public void setY(int y){
		this.locY=y;
	}
	
	/**
	 * Sets the x.
	 * 
	 * @param x is the new x value for this unit
	 */
	public void setX(int x){
		this.locX=x;
	}
	
	/**
	 * Gets the targ x.
	 * 
	 * @return returns the x value of the destination for this unit
	 */
	public int getTargX(){
		return targX;
		
	}
	
	/**
	 * Gets the targ y.
	 * 
	 * @return returns the y value of the destination for this unit
	 */
	public int getTargY(){
		return targY;
		
	}
	
	/**
	 * Checks if the unit is done.
	 * 
	 * @return returns true if the unit has reached its destination
	 */
	public boolean isDone(){
		return done;
	}
	
	/**
	 * alert that the unit is done with its traversal.
	 */
	public void setDone(){
		done=true;
	}
	
	/**
	 * Gets the node.
	 * 
	 * @return returns the node that the unit is currently on
	 */
	public Node getNode(){
		return node;
	}
	
	/**
	 * Sets the node.
	 * 
	 * @param node sets the node that this unit currently resides on
	 */
	public void setNode(Node node){
		this.node = node;
	}
	
	/**
	 * Sets the optimal length.
	 * 
	 * @param path the unit's path
	 */
	public void setOptimalLength(ArrayList<Integer> path){
		//for each move on this proposed path
		for(int i = 1; i <path.size();i++){
			//if the move is diagonal, add 14 to the path length
			if(path.get(i)-path.get(i-1)==33 || path.get(i)-path.get(i-1)==35 ||path.get(i)-path.get(i-1)==-33 ||path.get(i)-path.get(i-1)==-35){
				optimalLength+=14;
			}
			//if the move is adjacent, add 10 to the path length
			else{
				optimalLength+=10;
			}
		}
	}
	
	/**
	 * Gets the optimal length.
	 * 
	 * @return returns the length of the path from start to finish for a unit if there were no other units on the grid
	 */
	public int getOptimalLength() {
		return optimalLength;
	}
	
	/**
	 * Sets the unit's values to their original states.
	 */
	public void setToOriginals() {
		//reset starting point
		setNode(startingPoint);
		//reset coordinates
		setX(startingPoint.getX());
		setY(startingPoint.getY());
		//sets unit as not done pathfinding
		done=false;
		//erase actual path
		actualPath.clear();
		//erase proposed path
		path.clear();
	}
}
