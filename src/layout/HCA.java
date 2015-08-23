/**
 * Kevin Layfield
 * Dr. Wellington
 * 2009
 * Sr Research - Cooperative Pathfinding Vs. Local Repair Pathfinding
 */
package layout;

import java.util.ArrayList;


/**
 * The Class HCA.
 */
public class HCA {
	
	/** The collection of units. */
	ArrayList<HCAUnit> unit;
	
	/** The two d grid layout. */
	Node twoDNode[];
	
	/** The reservation table. */
	ResTable res;
	
	/** numbers corresponding to the 9 possible nodes to be examined from a given node. */
	int adjacents[]={-1087,-1088,-1089,-1121,-1122,-1123,-1155,-1156,-1157};
	
	/** The open list. */
	ArrayList<ResNode> openList;
	
	/** The closed list. */
	ArrayList<ResNode> closedList;
	
	/** The unit number. */
	int unitNumber=0;
	
	/** The destination index. */
	int destIndex=0;
	
	/** The number of units done. */
	int unitsDone=0;
	
	/** The path. */
	ArrayList<Integer> path;
	
	/** denotes whether or not the target node has been found. */
	boolean targNotFound=false;
	
	/**
	 * Instantiates a new hCA.
	 * 
	 * @param hcaunit the list of units
	 * @param res the reservation table
	 * @param twoDNode the two d grid representation
	 */
	public HCA(ArrayList<HCAUnit> hcaunit, ResTable res, Node twoDNode[]) {
		this.unit=hcaunit;
		this.twoDNode=twoDNode;
		this.res = res;
	}
	
	/**
	 * Gets the unit.
	 * 
	 * @return Returns an arraylist of all units pertaining to this grid layout
	 */
	public ArrayList<HCAUnit> getUnit() {
		return unit;
	}
	
	/**
	 * Sets the unit number.
	 * 
	 * @param i sets the current unit
	 */
	public void setUnitNumber(int i){
		unitNumber = i;
	}
	
	/**
	 * Gets the unit number.
	 * 
	 * @return returns the unit number of the current unit
	 */
	public int getUnitNumber(){
		return unitNumber;
	}
	
	/**
	 * Gets the units done.
	 * 
	 * @return returns the number of units that have reached their destinations
	 */
	public int getUnitsDone(){
		return unitsDone;
	}
	
	/**
	 * Gets the status of path validity.
	 * 
	 * @return returns true if there is no path from the start to end point
	 */
	public boolean getTargNotFound(){
		return targNotFound;
	}
	
	/**
	 * Sets the path validity.
	 * 
	 * @param value the new targ not found
	 */
	public void setTargNotFound(boolean value){
		targNotFound = value;
	}
	
	/**
	 * Increments to work with the next unit.
	 */
	public void nextUnit(){
		unitNumber++;
	}
	
	/**
	 * Calculate unit path.
	 * 
	 * @param unitNum is the number of the unit whose path will be calculated
	 * 
	 * @return Returns a path from a unit's starting to ending point in the form of an arraylist of integers.
	 * These integers represent node numbers.
	 */
	public ArrayList<Integer> calculateUnitPath(int unitNum){
		//list of nodes to be considered
		openList = new ArrayList<ResNode>();
		//list of potential path nodes that have been examined
		closedList =new ArrayList<ResNode>();
		//denotes that the target has been added to the closed list
		boolean targContained = false;
		//shortest path available from starting point to destination
		path = new ArrayList<Integer>();
		//Reverse resumable A* - used as a heuristic for HCA*, uses 2 dimensional representation of the grid
		RRA rra = new RRA(new LRUnit(twoDNode[unit.get(unitNum).getTargX()+unit.get(unitNum).getTargY()*34-34],twoDNode[unit.get(unitNum).getX()+unit.get(unitNum).getY()*34-34]),twoDNode);
		//test statistic for finding best potential nodes to examine
		int f;
		//index of the node on the open list with the lowest f value
		int savedOpenIndex;
		//lowest f value on the open list
		int lowf;
		//current coordinates being examined
		int curX,curY;
		//current third dimension on the reservation table --- time
		int z=0;
		//cost to move from one node to another
		int moveCost;
		//set all nodes on the reservation table to have no parent
		for(int i = 1; i < res.getResNode().size();i++){
			res.getResNode().get(i).setParentNode(-1);
		}
		//add unit's starting point to open list
		openList.add(unit.get(unitNum).getNode()); 
		//while the target hasn't been found and it hasn't been proven that a path doesn't exist
		while(targContained==false && targNotFound==false ){
			savedOpenIndex=0;
			lowf=9999;
			//for each node on the open list
			for(int i=0;i<openList.size();i++){
				//record the node's f value
				f = openList.get(i).calculateF();
				//if this node's f value is lower than the current lowest f value, record this node's f value as lowest
				if(f<lowf){
					lowf=f;
					//save index of the node on open list
					savedOpenIndex=i;
				}
			}
			//adds the node with the lowest f value on the open list to the closed list
			closedList.add(openList.get(savedOpenIndex));
			//sets the z value equal to the z value of the node that was just added to the closed list
			z= openList.get(savedOpenIndex).getZ()-1;
			//if we are approaching the end of the reservations z layers, add another
			if(res.getMaxZ()-2<=z){
				res.addToZ();
			}
			//sets x and y coordinates to the coordinates of the node that was just added to the closed list
			curX=openList.get(savedOpenIndex).getX();
			curY=openList.get(savedOpenIndex).getY();
			//remove the said node from the open list
			openList.remove(savedOpenIndex);
			//number of times this unit has attempted to have been placed
			int attempts=0;
			//for each of the 9 possible movements from the current closed list node
			for(int i =0;i<adjacents.length;i++){
				//if the movement is diagonal, move cost is 14
				if(i==0 || i==2 || i == 6 || i==8){
					moveCost = 14;
				}
				//if the movement is adjacent, move cost is 10
				else{
					moveCost = 10;
				}
				//if the node being considered isn't already on the closed list,isn't an obstacle, isn't occupied,...
				if(!((checkIfOnClosedList(res.getResNode().get(curX+z*1156+(curY*34-adjacents[i]))) || (res.getResNode().get(curX+z*1156+(curY*34-adjacents[i])).getStatus()==ResNode.Status.obstacle) || (res.getResNode().get(curX+z*1156+(curY*34-adjacents[i])).getStatus()==ResNode.Status.occupied))||
						//isn't a diagonal movement where the unit would clip the corner of an adjacent obstacle,
						(i==0 && res.getResNode().get(curX+z*1156+(curY*34-adjacents[i]+1)).getStatus()==ResNode.Status.obstacle) || 
						(i==0 && res.getResNode().get(curX+z*1156+(curY*34-adjacents[i]+34)).getStatus()==ResNode.Status.obstacle) ||
						(i==2 && res.getResNode().get(curX+z*1156+(curY*34-adjacents[i]-1)).getStatus()==ResNode.Status.obstacle) || 
						(i==2 && res.getResNode().get(curX+z*1156+(curY*34-adjacents[i]+34)).getStatus()==ResNode.Status.obstacle) ||
						(i==6 && res.getResNode().get(curX+z*1156+(curY*34-adjacents[i]+1)).getStatus()==ResNode.Status.obstacle) || 
						(i==6 && res.getResNode().get(curX+z*1156+(curY*34-adjacents[i]-34)).getStatus()==ResNode.Status.obstacle) ||
						(i==8 && res.getResNode().get(curX+z*1156+(curY*34-adjacents[i]-1)).getStatus()==ResNode.Status.obstacle) || 
						(i==8 && res.getResNode().get(curX+z*1156+(curY*34-adjacents[i]-34)).getStatus()==ResNode.Status.obstacle) || 
						//and isn't attempting to move to a node which would create an appearance where two units move through each other
						(i==1 && res.getResNode().get(curX+z*1156+(curY*34-adjacents[1]-1156)).getStatus()==ResNode.Status.occupied && res.getResNode().get(curX+z*1156+(curY*34-34)+1156).getStatus()==ResNode.Status.occupied  && res.getResNode().get(curX+z*1156+(curY*34-adjacents[1]-1156)).getOccupier()==res.getResNode().get(curX+z*1156+(curY*34-34)+1156).getOccupier())||//&& res.getResNode().get(curX+z*1156+(curY*34-adjacents[1]-1156)).getOccupier() == res.getResNode().get(curX+z*1156+(curY*34-34)+1156).getOccupier())||	
						(i==3 && res.getResNode().get(curX+z*1156+(curY*34-adjacents[3]-1156)).getStatus()==ResNode.Status.occupied && res.getResNode().get(curX+z*1156+(curY*34-34)+1156).getStatus()==ResNode.Status.occupied && res.getResNode().get(curX+z*1156+(curY*34-adjacents[3]-1156)).getOccupier()==res.getResNode().get(curX+z*1156+(curY*34-34)+1156).getOccupier())||//&& res.getResNode().get(curX+z*1156+(curY*34-adjacents[3]-1156)).getOccupier() == res.getResNode().get(curX+z*1156+(curY*34-34)+1156).getOccupier())||						
						(i==5 && res.getResNode().get(curX+z*1156+(curY*34-adjacents[5]-1156)).getStatus()==ResNode.Status.occupied && res.getResNode().get(curX+z*1156+(curY*34-34)+1156).getStatus()==ResNode.Status.occupied && res.getResNode().get(curX+z*1156+(curY*34-adjacents[5]-1156)).getOccupier()==res.getResNode().get(curX+z*1156+(curY*34-34)+1156).getOccupier())||//&& res.getResNode().get(curX+z*1156+(curY*34-adjacents[5]-1156)).getOccupier() == res.getResNode().get(curX+z*1156+(curY*34-34)+1156).getOccupier())||						
						(i==7 && res.getResNode().get(curX+z*1156+(curY*34-adjacents[7]-1156)).getStatus()==ResNode.Status.occupied && res.getResNode().get(curX+z*1156+(curY*34-34)+1156).getStatus()==ResNode.Status.occupied && res.getResNode().get(curX+z*1156+(curY*34-adjacents[7]-1156)).getOccupier()==res.getResNode().get(curX+z*1156+(curY*34-34)+1156).getOccupier())||// && res.getResNode().get(curX+z*1156+(curY*34-adjacents[7]-1156)).getOccupier() == res.getResNode().get(curX+z*1156+(curY*34-34)+1156).getOccupier())||						
						(i==0 && res.getResNode().get(curX+z*1156+(curY*34-adjacents[0]-1156)).getStatus()==ResNode.Status.occupied && res.getResNode().get(curX+z*1156+(curY*34-adjacents[0]-1156)).getOccupier()==res.getResNode().get(curX+z*1156+(curY*34-34)+1156).getOccupier())||						
						(i==2 && res.getResNode().get(curX+z*1156+(curY*34-adjacents[2]-1156)).getStatus()==ResNode.Status.occupied && res.getResNode().get(curX+z*1156+(curY*34-adjacents[2]-1156)).getOccupier()==res.getResNode().get(curX+z*1156+(curY*34-34)+1156).getOccupier())||						
						(i==6 && res.getResNode().get(curX+z*1156+(curY*34-adjacents[6]-1156)).getStatus()==ResNode.Status.occupied && res.getResNode().get(curX+z*1156+(curY*34-adjacents[6]-1156)).getOccupier()==res.getResNode().get(curX+z*1156+(curY*34-34)+1156).getOccupier())||						
						(i==8 && res.getResNode().get(curX+z*1156+(curY*34-adjacents[8]-1156)).getStatus()==ResNode.Status.occupied && res.getResNode().get(curX+z*1156+(curY*34-adjacents[8]-1156)).getOccupier()==res.getResNode().get(curX+z*1156+(curY*34-34)+1156).getOccupier())
				)){
					//if the node is on the open list already
					if(checkIfOnOpenList(res.getResNode().get((curX+z*1156+(curY*34-adjacents[i]))))){
						//if the node's g value coming from the current closed list node is lower than the g value if it were coming from its original parent
						if(res.getResNode().get(curX+z*1156+(curY*34-adjacents[i])).calculateG(res.getResNode().get(curX+z*1156+curY*34-34),moveCost) < res.getResNode().get(curX+z*1156+curY*34-adjacents[i]).getG()){
							//make the current closed list node the parent of this node being considered
							res.getResNode().get(curX+z*1156+(curY*34-adjacents[i])).setParentNode(curX+z*1156+(curY*34-34));
							//set new g and f values for this node
							res.getResNode().get(curX+z*1156+(curY*34-adjacents[i])).setG(res.getResNode().get(curX+z*1156+curY*34-34),moveCost);
							res.getResNode().get(curX+z*1156+(curY*34-adjacents[i])).setF();
						}
					}
					//if the node isn't on the open list yet
					else{
						//add it to the open list
						openList.add(res.getResNode().get(curX +z*1156+(curY*34-adjacents[i])));
						//make the current closed list node the parent of this node
						res.getResNode().get(curX+z*1156+(curY*34-adjacents[i])).setParentNode(curX+1156*z+(curY*34-34));
						//set g, f, and h values
						res.getResNode().get(curX+z*1156+(curY*34-adjacents[i])).setG(res.getResNode().get(curX+z*1156+curY*34-34),moveCost);
						res.getResNode().get(curX+z*1156+(curY*34-adjacents[i])).setH(rra.calculateTrueDistance((curX+z*1156+(curY*34-adjacents[i]))%1156));
						res.getResNode().get(curX+z*1156+(curY*34-adjacents[i])).setF();
					}
				}
				//increase number of times the unit has attempted to have been placed	
				attempts++;	
			}
			//for each node on the closed list
			for(int i=0;i<closedList.size();i++){
				//if the node has the same x and y coordinates of the destination
				if(closedList.get(i).getX()==unit.get(unitNum).getEndingPoint().getX() && closedList.get(i).getY()==unit.get(unitNum).getEndingPoint().getY()){
					//designate that the target has been found
					targContained=true;
					//set the index of the destination within the closed list
					destIndex = i;
				}
			}
			//if the open list is empty, all possible nodes that are in movable range of unit have been checked
			if(openList.isEmpty()){
				//designate that no path exists
				targNotFound=true;
			}	
		}
		//if it hasn't been proven that no path exists and the destination has been found
		if(targNotFound==false){
			//calculated path
			path = new ArrayList<Integer>();
			//add the destination node to the path
			path.add(closedList.get(destIndex).getX()  + closedList.get(destIndex).getY() *34 - 34 +closedList.get(destIndex).getZ() * 1156-1156);
			//set the new destination index as the parent node of the destination --- previous node in the path
			destIndex=closedList.get(destIndex).getParentNode();
			//while you can still trace nodes back to a node with a parent
			while(destIndex!=-1){
				//insert the previous node in the path for the unit
				path.add(0,destIndex);
				//set the new destination index as the parent of this node
				destIndex = res.getResNode().get(destIndex).getParentNode();
			}
		}
		//reserve the unit's path in the reservation table
		res.lockPath(path,unitNum);
		return path;
	}
	
	/**
	 * Execute next move.
	 * 
	 * @param i is number of the unit who is moving
	 * 
	 * @return returns the number of the node that the unit is located on after a move
	 */
	public int executeNextMove(int i) {
		//if the unit still has moves left in his path
		if(unit.get(i).getPath().size()>0){
			//get the number of the node that is the next move in the unit's path
			int nodeNum = unit.get(i).getPath().get(0);
			//if the node is the destination for the unit
			if(nodeNum%1156==(unit.get(i).getEndingPoint().getX()+unit.get(i).getEndingPoint().getY()*34-34)){
				//increase the counter for how many units have reached their destination
				unitsDone++;
				//set the node as being a finished status
				res.getResNode().get(nodeNum).setEnded();
				//designate that the unit has completed its path
				unit.get(i).setDone();
				//set the destination node as empty
				res.getResNode().get(nodeNum).setAsEmpty();
			}
			//set the unit's new x value
			unit.get(i).setX(res.getResNode().get(nodeNum).getX());
			//set the unit's new y value
			unit.get(i).setY(res.getResNode().get(nodeNum).getY());
			//remove the current node from the remainder of the unit's path
			unit.get(i).getPath().remove(0);
			//if the unit is now done with its path
			if(unit.get(i).getPath().size()==0){
				//set it as done, and make its node empty
				unit.get(i).setDone();
				res.getResNode().get(nodeNum).setAsEmpty();
			}
			//return the number of the node that the unit now resides on
			return nodeNum;
		}
		//if the unit is already done with its path
		else{
			//reset the unit as being done
			unit.get(i).setDone();
			//return the node that the unit was already on
			return unit.get(i).getX()+unit.get(i).getY()*34-34;
		}
		
	}
	
	/**
	 * Check if on open list.
	 * 
	 * @param node is a node being considered for traversal by a unit
	 * 
	 * @return returns true if the node in question is on the open list
	 */
	public boolean checkIfOnOpenList(ResNode node){
		for(int i=0;i<openList.size();i++){
			if(openList.get(i).getX()==node.getX() && openList.get(i).getY()==node.getY() && openList.get(i).getZ()==node.getZ()){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Check if on closed list.
	 * 
	 * @param node is a node being considered for traversal by a unit
	 * 
	 * @return returns true if the node in question is on the closed list
	 */
	public boolean checkIfOnClosedList(ResNode node){
		for(int i=0;i<closedList.size();i++){
			if(closedList.get(i).getX()==node.getX() && closedList.get(i).getY()==node.getY() && closedList.get(i).getZ()==node.getZ()){
				return true;
			}
		}
		return false;
	}


	/**
	 * Gets the res table.
	 * 
	 * @return the res table
	 */
	public ResTable getResTable() {
		return res;
	}
}
