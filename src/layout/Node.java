/**
 * Kevin Layfield
 * Dr. Wellington
 * 2009
 * Sr Research - Cooperative Pathfinding Vs. Local Repair Pathfinding
 */
package layout;

import java.awt.Color;


/**
 * The Class Node.
 */
public class Node {
	
	/**
	 * The Enum Status.
	 */
	public enum Status {

/** The obstacle. */
obstacle,

/** The start. */
start,

/** The end. */
end,

/** The occupied. */
occupied,

/** The empty. */
empty}
	
	/** The node status. */
	Status nodeStatus;
	
	/** The unit num. */
	int unitNum;
	
	/** The number of the unit who will end on this node. */
	int endNode=0;
	
	/** denotes whether or not this node is the destination for a unit. */
	boolean ender=false;
	
	/** denotes whether or not the unit has actually reached this node yet. */
	boolean ended = false;
	
	/** The node coordinates. */
	int x,y;
	
	/** A* statistics. */
	int f,g,h;
	
	/** The parent node. */
	int parentNode=9999;
	
	/** The previous color of this node. */
	Color prevColor;
	
	/**
	 * Instantiates a new node.
	 * 
	 * @param x the x
	 * @param y the y
	 */
	public Node(int x, int y){
		this.x=x;
		this.y=y;
		prevColor=Color.white;
	}
	
	/**
	 * Gets the occupier.
	 * 
	 * @return returns the number of the unit on this node
	 */
	public int getOccupier(){
		return unitNum;
	}
	
	/**
	 * Gets the previous color.
	 * 
	 * @return returns the color that this node was before a unit occupied it
	 */
	public Color getPrevColor(){
		return prevColor;
	}
	
	/**
	 * Sets the previous color.
	 * 
	 * @param prevColor is the color that is being recorded as the color of the node before a unit occupied it
	 */
	public void setPrevColor(Color prevColor){
		this.prevColor=prevColor;
	}
	
	/**
	 * Gets the status.
	 * 
	 * @return returns the status of the node (occupied, obstacle, end)
	 */
	public Status getStatus(){
		return nodeStatus;
	}
	
	/**
	 * sets the status of the node as being empty.
	 */
	public void setAsEmpty(){
		nodeStatus = Status.empty;
		unitNum=-1;
	}
	
	/**
	 * sets the status of the node as being an obstacle.
	 */
	public void setAsObstacle() {
		nodeStatus = Status.obstacle;
	}
	
	/**
	 * sets the status of the node as being a starting position.
	 */
	public void setAsStartingPoint() {
		nodeStatus = Status.occupied;
	}
	
	/**
	 * sets the status of the node as being an endpoint.
	 * 
	 * @param endNode is the unit who calls this his destination
	 */
	public void setAsEndingPoint(int endNode) {
		nodeStatus = Status.end;
		this.endNode=endNode;
		ender=true;
	}
	
	/**
	 * Gets the end node.
	 * 
	 * @return returns the unit number whose destination this is
	 */
	public int getEndNode(){
		return endNode;
	}
	
	/**
	 * Checks if is ender.
	 * 
	 * @return returns true if this node is an end point
	 */
	public boolean isEnder(){
		return ender;
	}
	
	/**
	 * Checks if is ended.
	 * 
	 * @return returns true if the node was an ender and is now occupied by its corresponding unit
	 */
	public boolean isEnded(){
		return ended;
	}
	
	/**
	 * denotes that this end point has been reached by a unit.
	 */
	public void setEnded(){
		ended=true;
	}
	
	/**
	 * sets this node as being occupied by a unit.
	 * 
	 * @param unitNumber is the unit who is currently on this node
	 */
	public void setAsOccupied(int unitNumber) {
		nodeStatus = Status.occupied;
		unitNum = unitNumber;
	}
	
	/**
	 * Gets the x value.
	 * 
	 * @return returns this node's x value
	 */
	public int getX(){
		return x;
	}
	
	/**
	 * Gets the y value.
	 * 
	 * @return returns this node's y value
	 */
	public int getY(){
		return y;
	}
	
	/**
	 * Sets the x.
	 * 
	 * @param x is the node's new x value
	 */
	public void setX(int x){
		this.x = x;
	}
	
	/**
	 * Sets the y.
	 * 
	 * @param y is the node's new y value
	 */
	public void setY(int y){
		this.y = y;
	}
	
	/**
	 * calculates f for this node.
	 * 
	 * @return returns the f value for this node
	 */
	public int calculateF(){
		f=getH()+getG();
		return f;
	}
	
	/**
	 * g is a measure of cost from the starting location to this location.
	 * 
	 * @param parent is the node that was used to in successive movement to reach this node
	 * @param moveCost is how much it costs to move from the parent to this node
	 * 
	 * @return return the g value for this node
	 */
	public int calculateG(Node parent,int moveCost){
		int tempG = parent.getG()+moveCost;
		return tempG;
	}
	
	/**
	 * h is a measure of cost from the current node to the ending location.
	 * The heuristic in this case is Diagonal Distance
	 * 
	 * @param destination is the goal node for this unit
	 * 
	 * @return return the h value for this node
	 */
	public int calculateH(Node destination){
		int tempH;
		int xDistance = Math.abs(x-destination.getX());
		int yDistance = Math.abs(y-destination.getY());
		if(xDistance>yDistance){
			tempH=14*yDistance + 10*(xDistance-yDistance);
		}
		else
			tempH=14*xDistance + 10*(yDistance - xDistance);
		return tempH;
	}
	
	/**
	 * calculates f for this position.
	 */
	public void setF(){
		f=calculateF();
	}
	
	/**
	 * calculates and sets g for this node.
	 * 
	 * @param parent is the node that was used in successive movement to reach this node
	 * @param moveCost is the cost in movie from the parent to this node
	 */
	public void setG(Node parent, int moveCost){
		g=calculateG(parent, moveCost);
	}
	
	/**
	 * calculates and sets h for this node.
	 * 
	 * @param destination is the node that is trying to be reached from this location
	 */
	public void setH(Node destination){
		
		h=calculateH(destination);
	}
	
	/**
	 * Gets the f.
	 * 
	 * @return returns f for this ndoe
	 */
	public int getF(){
		return f;
	}
	
	/**
	 * Gets the g.
	 * 
	 * @return returns g for this node
	 */
	public int getG(){
		return g;
	}
	
	/**
	 * Gets the h.
	 * 
	 * @return returns h for this node
	 */
	public int getH(){
		return h;
	}
	
	/**
	 * Sets the parent node.
	 * 
	 * @param nodeNum is the number of the node who was used to reach this node in successive movement
	 */
	public void setParentNode(int nodeNum){
		parentNode=nodeNum;
	}
	
	/**
	 * Gets the parent node.
	 * 
	 * @return returns the number of the node that was used to reach this node in successive movement
	 */
	public int getParentNode(){
		return parentNode;
	}
	
	/**
	 * Sets the nodes values to their original state.
	 */
	public void setToOriginals(){
		//set all occupied nodes as empty
		if(nodeStatus == Status.occupied){
			setAsEmpty();
		}
		//if the node had a unit end on it, reset that value
		if(ended == true){
			ended=false;
		}
		f=0;
		g=0;
		h=0;
	}
	
	/**
	 * resets the nodes f, g, and h values.
	 */
	public void resetFGH(){
		f=0;
		g=0;
		h=0;
	}
	
}
