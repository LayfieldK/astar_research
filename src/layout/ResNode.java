/**
 * Kevin Layfield
 * Dr. Wellington
 * 2009
 * Sr Research - Cooperative Pathfinding Vs. Local Repair Pathfinding
 */
package layout;


/**
 * The Class ResNode.
 */
public class ResNode {
	
	/** The node's coordinates. */
	int x, y, z;
	
	/**
	 * The Enum Status.
	 */
	public enum Status {

/** obstacle. */
obstacle,

/** start. */
start,

/** end. */
end,

/** occupied. */
occupied,

/** empty. */
empty}
	
	/** The node status. */
	Status nodeStatus;
	
	/** The unit number. */
	int unitNum;
	
	/** The unit whose end node this is. */
	int endNode=0;
	
	/** denotes whether or not these x and y coordinates will be an ending position. */
	boolean ender=false;
	
	/** denotes whether or not the node has had a unit end on it. */
	boolean ended = false;
	
	/** The A* statistics. */
	int f,g,h;
	
	/** The parent node. */
	int parentNode=-1;

	/**
	 * Instantiates a new res node.
	 * 
	 * @param x the x
	 * @param y the y
	 * @param z the z
	 */
	public ResNode(int x, int y, int z){
		this.y = x;
		this.x = y;
		this.z = z;
		setAsEmpty();
	}
	
	/**
	 * Gets the x.
	 * 
	 * @return returns the node's x value
	 */
	public int getX(){
		return x;
	}
	
	/**
	 * Gets the y.
	 * 
	 * @return returns the node's y value
	 */
	public int getY(){
		return y;
	}
	
	/**
	 * Gets the z.
	 * 
	 * @return returns the node's z value
	 */
	public int getZ(){
		return z;
	}
	
	/**
	 * Sets the x.
	 * 
	 * @param x is the node's new x value
	 */
	public void setX(int x){
		this.x=x;
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
	 * Sets the z.
	 * 
	 * @param z is the node's new z value
	 */
	public void setZ(int z){
		this.z = z;
	}
	
	/**
	 * increases this node's z value.
	 */
	public void incrementZ(){
		z++;
	}
	
	/**
	 * Gets the status.
	 * 
	 * @return returns the status (occupied, obstacle, end) of the node
	 */
	public Status getStatus(){
		return nodeStatus;
	}
	
	/**
	 * sets the node as being unoccupied by anything.
	 */
	public void setAsEmpty(){
		nodeStatus = Status.empty;
		unitNum=-1;
	}
	
	/**
	 * sets the node as an un-traversable object in the grid.
	 */
	public void setAsObstacle() {

		nodeStatus = Status.obstacle;
	}

	/**
	 * sets the node as the ending point for a unit.
	 * 
	 * @param endNode is the number of the unit that is calling this its destination
	 */
	public void setAsEndingPoint(int endNode) {
		nodeStatus = Status.end;
		this.endNode=endNode;
		ender=true;
	}
	
	/**
	 * Gets the g.
	 * 
	 * @return returns g value
	 */
	public int getG() {
		return g;
	}
	
	/**
	 * calculates and sets g for this node.
	 * 
	 * @param parent is this node's parent
	 * @param moveCost is how much it costs to move from parent to here
	 */
	public void setG(ResNode parent, int moveCost) {
		g=calculateG(parent, moveCost);
	}
	
	/**
	 * calculates and sets F for this node.
	 */
	public void setF() {
		f=calculateF();
	}
	
	/**
	 * returns this node's f value.
	 * 
	 * @return the F
	 */
	public int getF() {
		return f;
	}
	
	/**
	 * calculates and sets H for this node.
	 * 
	 * @param destination is the goal node
	 */
	public void calculateSetH(ResNode destination){
		
		h=calculateH(destination);
	}
	
	/**
	 * Sets the h.
	 * 
	 * @param h the new h
	 */
	public void setH(int h){
		this.h = h;
	}
	
	/**
	 * h is the cost involved from getting from this node to the end node.
	 * *****************
	 * Legacy Code, Manhattan distance heuristic is no longer used
	 * *****************
	 * 
	 * @param destination is the goal point
	 * 
	 * @return returns h value
	 */
	public int calculateH(ResNode destination) {
		h = 10 * Math.abs(destination.getX()-this.getX());
		h+= 10 * Math.abs(destination.getY()-this.getY());
		return h;
	}
	
	/**
	 * statistic that is compared for efficiency.
	 * 
	 * @return returns f value
	 */
	public int calculateF() {
		f=getH()+getG();
		return f;
	}
	
	/**
	 * Gets the h.
	 * 
	 * @return returns h value
	 */
	public int getH() {
		return h;
	}
	
	/**
	 * Sets the parent node.
	 * 
	 * @param i is the number of the node to be this node's parent
	 */
	public void setParentNode(int i) {
		parentNode=i;
		
	}
	
	/**
	 * calculates g, which represents cost from starting position to position in question.
	 * 
	 * @param parent is the node moved from to get here
	 * @param moveCost is how much cost is involved from moving from parent to here
	 * 
	 * @return returns g value for the node
	 */
	public int calculateG(ResNode parent, int moveCost) {
		int tempG = parent.getG()+moveCost;
		return tempG;
	}
	
	/**
	 * Gets the parent node.
	 * 
	 * @return returns the number of the node who was the last node in line of movement before this one
	 */
	public int getParentNode() {
		return parentNode;
	}
	
	/**
	 * marks node as occupied and records who is on it.
	 * 
	 * @param unitNumber is the unit who is currently on the node
	 */
	public void setAsOccupied(int unitNumber) {
		nodeStatus = Status.occupied;
		unitNum = unitNumber;
	}
	
	/**
	 * Gets the occupier.
	 * 
	 * @return returns the number of the unit who is currently on this node
	 */
	public int getOccupier(){
		return unitNum;
	}
	
	/**
	 * Gets the end node.
	 * 
	 * @return returns the number of the unit whose destination this is
	 */
	public int getEndNode(){
		return endNode;
	}
	
	/**
	 * Checks if is ender.
	 * 
	 * @return returns true if this node is the destination of a unit
	 */
	public boolean isEnder(){
		return ender;
	}
	
	/**
	 * Checks if is ended.
	 * 
	 * @return returns true if this was an ender and its unit has reached it
	 */
	public boolean isEnded(){
		return ended;
	}
	
	/**
	 * denotes that this node was an ender and its corresponding unit has reached it.
	 */
	public void setEnded(){
		ended=true;
	}

	/**
	 * Sets the nodes values to their original state.
	 */
	public void setToOriginals() {
		//if the node is occupied, make it empty
		if(nodeStatus == Status.occupied){
			setAsEmpty();
		}
		//if the node had a unit end on it, reset that value as well
		if(ended == true){
			ended=false;
		}
	}

	

}
