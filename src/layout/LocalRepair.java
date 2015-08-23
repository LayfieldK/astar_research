/**
 * Kevin Layfield
 * Dr. Wellington
 * 2009
 * Sr Research - Cooperative Pathfinding Vs. Local Repair Pathfinding
 */
package layout;

import java.util.ArrayList;
import java.util.Random;

import layout.Node.Status;

/**
 * The Class LocalRepair.
 */
public class LocalRepair {
	
	/** The collection of units. */
	ArrayList<LRUnit> unit;

	/** The grid layout. */
	Node node[];

	/** The values associated with checking nodes surrounding any node on the grid. */
	int adjacents[]={69,68,67,35,33,1,0,-1};
	
	/** The open list. */
	ArrayList<Node> openList;
	
	/** The closed list. */
	ArrayList<Node> closedList;
	
	/** The unit number. */
	int unitNumber=0;
	
	/** The destination index. */
	int destIndex=0;
	
	/** The number of units done. */
	int unitsDone=0;
	
	/** The calculated path. */
	ArrayList<Integer> path;
	
	/** Denotes whether or not a target has been found by A*. */
	boolean targNotFound=false;
	
	/**
	 * Instantiates a new local repair.
	 * 
	 * @param unit list of units
	 * @param node grid layout
	 */
	public LocalRepair(ArrayList<LRUnit> unit,Node node[]) {
		this.unit=unit;
		this.node = node;
	}
	
	/**
	 * Gets the unit list.
	 * 
	 * @return the unit list
	 */
	public ArrayList<LRUnit> getUnit(){
		return unit;
	}
	
	/**
	 * Sets the unit number.
	 * 
	 * @param i the new unit number
	 */
	public void setUnitNumber(int i){
		unitNumber = i;
	}
	
	/**
	 * Gets the unit number.
	 * 
	 * @return the unit number
	 */
	public int getUnitNumber(){
		return unitNumber;
	}
	
	/**
	 * Gets the number of units done.
	 * 
	 * @return the units done
	 */
	public int getUnitsDone(){
		return unitsDone;
	}
	
	/**
	 * Gets target status.
	 * 
	 * @return the boolean value that denotes whether or not A* has found the target
	 */
	public boolean getTargNotFound(){
		return targNotFound;
	}
	
	/**
	 * Next unit.
	 */
	public void nextUnit(){
		unitNumber++;
	}
	
	/**
	 * Calculate unit path.
	 * 
	 * @param unitNum the unit number
	 * 
	 * @return the path via array list of node numbers
	 */
	public ArrayList<Integer> calculateUnitPath(int unitNum){
		//potential nodes that are in close vicinity to nodes you have already examined
		openList = new ArrayList<Node>();
		//nodes that have been examined by A*, potentials for calculated path
		closedList =new ArrayList<Node>();
		//tracks whether or not a destination is enclosed by obstacles in relation to the unit
		boolean targContained = false;
		//statistic to be compared for examination
		int f;
		//position on open list belonging to node with lowest f score
		int savedOpenIndex=0;
		//the lowest f value on the open list
		int lowf = 9999;
		//current coordinates being examined
		int curX,curY;
		//cost in path length for a potential move
		int moveCost;
		//for each node, set no parent node
		for(int i = 1; i < 1157;i++){
			node[i].setParentNode(9999);
		}
		//add the unit's starting point to the open list
		openList.add(unit.get(unitNum).getNode()); 
		//while we know the target is not containted, and a path to it exists
		while(targContained==false && targNotFound==false){
			//reset index of lowest f
			savedOpenIndex=0;
			//reset lowest f
			lowf=9999;
			//for each node on the open list
			for(int i=0;i<openList.size();i++){
				//record current node's f value
				f = openList.get(i).getF();
				//if this is lower than the current lowest f value, record new lowf and index of this node
				if(f<lowf){
					lowf=f;
					savedOpenIndex=i;
				}
			}
			//add the node from open list with the lowest f value to the closed list
			closedList.add(openList.get(savedOpenIndex));
			//make the current x value equal to the x value of the node you just added to closed list
			curX=openList.get(savedOpenIndex).getX();
			//make the current y value equal to the y value of the node you just added to closed list
			curY=openList.get(savedOpenIndex).getY();
			//remove this node from open list
			openList.remove(savedOpenIndex);
			//number of times this unit has tried to have been placed
			int attempts=0;
			//for each of the 8 surrounding nodes
			for(int i =0;i<adjacents.length;i++){
				//if this is a diagonally touching node, movecost would be 14
				if(i==0 || i==2 || i == 5 || i==7){
					moveCost = 14;
				}
				//if this is an adjacently touching node, movecost would be 10
				else{
					moveCost = 10;
				}
				//if this node isn't on the closed list, isn't an obstacle, and
			if(!((checkIfOnClosedList(node[curX+(curY*34-adjacents[i])]) || (node[curX+(curY*34-adjacents[i])].getStatus()==Status.obstacle)) ||
					//if its a diagonally touching node, it's corresponding adjacent nodes are not obstacles
					//(I.E. no cutting corners of obstacles)
					(i==0 && node[curX+(curY*34-adjacents[i]+1)].getStatus()==Status.obstacle) || 
					(i==0 && node[curX+(curY*34-adjacents[i]+34)].getStatus()==Status.obstacle) ||
					(i==2 && node[curX+(curY*34-adjacents[i]-1)].getStatus()==Status.obstacle) || 
					(i==2 && node[curX+(curY*34-adjacents[i]+34)].getStatus()==Status.obstacle) ||
					(i==5 && node[curX+(curY*34-adjacents[i]+1)].getStatus()==Status.obstacle) || 
					(i==5 && node[curX+(curY*34-adjacents[i]-34)].getStatus()==Status.obstacle) ||
					(i==7 && node[curX+(curY*34-adjacents[i]-1)].getStatus()==Status.obstacle) ||
					(i==7 && node[curX+(curY*34-adjacents[i]-34)].getStatus()==Status.obstacle) 
					)){
					//if the node being looked at is on the open list already
					if(checkIfOnOpenList(node[curX+(curY*34-adjacents[i])])){
						//if the node's g value coming from the current closed list node is lower than the node's previous g value
						if(node[curX+(curY*34-adjacents[i])].calculateG(node[curX+(curY*34-34)],moveCost) < node[curX+(curY*34-adjacents[i])].getG()){
							//set the current closed list node as the parent of this node
							node[curX+(curY*34-adjacents[i])].setParentNode(curX+(curY*34-34));
							//recalculate the g and f values for this node
							node[curX+(curY*34-adjacents[i])].setG(node[curX+(curY*34-34)],moveCost);
							node[curX+(curY*34-adjacents[i])].setF();
						}
					}
					//if the node being looked at is not on the open list
					else{
						//add the node to the open list
						openList.add(node[curX+(curY*34-adjacents[i])]);
						//set the parent node as the current closed list node
						node[curX+(curY*34-adjacents[i])].setParentNode(curX+(curY*34-34));
						//calculate g, h, and f values for the node
						node[curX+(curY*34-adjacents[i])].setG(node[curX+(curY*34-34)],moveCost);
						node[curX+(curY*34-adjacents[i])].setH(unit.get(unitNum).getEndingPoint());
						node[curX+(curY*34-adjacents[i])].setF();
					}
				}
				//increase the number of times this unit has tried to have been placed	
				attempts++;	
			}
			//for each node on the closed list
			for(int i=0;i<closedList.size();i++){
				//if the node is the destination node
				if(closedList.get(i).getX()==unit.get(unitNum).getEndingPoint().getX() && closedList.get(i).getY()==unit.get(unitNum).getEndingPoint().getY()){
					//set the target as contained
					targContained=true;
					//record the appropriate entry in the closed list as the destination index
					destIndex = i;
				}
			}
			//if there are no more nodes on the open list, no path to the destination exists
			if(openList.isEmpty()){
				targNotFound=true;
			}	
		}
		//if a path has been found
		if(targNotFound==false){
			//the calculated path
		    path = new ArrayList<Integer>();
		    //adds the end point to the path
			path.add(closedList.get(destIndex).getX() + closedList.get(destIndex).getY()*34  - 34);
			//sets the new destination index as the parent node of the ending node
			destIndex=closedList.get(destIndex).getParentNode();
			//repeats the process until the starting node is reached
			while(destIndex!=9999){
				//insert previous node into the path
				path.add(0,destIndex);
				//get this node's parent as the next node to add
				destIndex = node[destIndex].getParentNode();
			}
		}
		return path;
	}
	
	/**
	 * Execute next move.
	 * 
	 * @param i the unit number
	 * 
	 * @return the number of the node that the unit ends up on
	 */
	public int executeNextMove(int i) {
		//if the unit's current path length is more than 0
		if(unit.get(i).getPath().size()>0){
			//the node that the unit wants to occupy
			int nodeNum = unit.get(i).getPath().get(0);
			//if this node is already occupied
			if(node[nodeNum].getStatus()==Status.occupied){
				//randomly generate a number between 0 and 7, to decide which node to try to move to instead
				Random gen = new Random();
				int rInd=gen.nextInt(adjacents.length);
				//tracks number of times the unit has tried to have been placed
				int attempts=0;
				//wile this random node is an obstacle, is occupied, or is a diagonally touching node with a corresponding adjacent node which is an obstacle
				while(node[unit.get(i).getX()+unit.get(i).getY()*34-adjacents[rInd]].getStatus()==Status.obstacle || node[unit.get(i).getX()+unit.get(i).getY()*34-adjacents[rInd]].getStatus()==Status.occupied ||
				(rInd==0 && node[unit.get(i).getX()+(unit.get(i).getY()*34-adjacents[rInd]+1)].getStatus()==Status.obstacle) || 
				(rInd==0 && node[unit.get(i).getX()+(unit.get(i).getY()*34-adjacents[rInd]+34)].getStatus()==Status.obstacle) ||
				(rInd==2 && node[unit.get(i).getX()+(unit.get(i).getY()*34-adjacents[rInd]-1)].getStatus()==Status.obstacle) || 
				(rInd==2 && node[unit.get(i).getX()+(unit.get(i).getY()*34-adjacents[rInd]+34)].getStatus()==Status.obstacle) ||
				(rInd==5 && node[unit.get(i).getX()+(unit.get(i).getY()*34-adjacents[rInd]+1)].getStatus()==Status.obstacle) || 
				(rInd==5 && node[unit.get(i).getX()+(unit.get(i).getY()*34-adjacents[rInd]-34)].getStatus()==Status.obstacle) ||
				(rInd==7 && node[unit.get(i).getX()+(unit.get(i).getY()*34-adjacents[rInd]-1)].getStatus()==Status.obstacle) || 
				(rInd==7 && node[unit.get(i).getX()+(unit.get(i).getY()*34-adjacents[rInd]-34)].getStatus()==Status.obstacle)){ 
					//increments the number of the adjacents array to check
					rInd=(rInd+1)%(adjacents.length-1);
					//increase the number of attempts at placement
					attempts++;
					//if you've tried to place the unit in every surrounding node unsuccessfully
					if(attempts==adjacents.length){
						//return the number of the node that the unit is already on
						return unit.get(i).getX()+unit.get(i).getY()*34-34;
					}
				}
				//sets previously occupied node as empty
				node[unit.get(i).getX()+unit.get(i).getY()*34-34].setAsEmpty();
				//sets the node number as the node which was just occupied
				nodeNum=unit.get(i).getX()+unit.get(i).getY()*34-adjacents[rInd];
				//sets new x and y coordinates for the unit
				unit.get(i).setX(node[nodeNum].getX());
				unit.get(i).setY(node[nodeNum].getY());
				//sets the current node for the unit
				unit.get(i).setNode(node[unit.get(i).getX()+unit.get(i).getY()*34-34]);
				//sets new node as occupied
				node[nodeNum].setAsOccupied(i);
				//calculates a new path for the unit starting from this new location
				unit.get(i).setPath(calculateUnitPath(i));
				//remove starting node from path
				unit.get(i).getPath().remove(0);
				//if the new node is equal to the destination of the unit
				if(nodeNum==(unit.get(i).getEndingPoint().getX()+unit.get(i).getEndingPoint().getY()*34-34)){
					//increase number of units that are done
					unitsDone++;
					//set the node that the unit ended on as empty
					node[nodeNum].setAsEmpty();
				}
				//return the number of the node that the unit is on
				return nodeNum;
			}
			//set old node as empty
			node[unit.get(i).getX()+unit.get(i).getY()*34-34].setAsEmpty();
			//set new coordinates
			unit.get(i).setX(node[nodeNum].getX());
			unit.get(i).setY(node[nodeNum].getY());
			//set new node as occupied
			node[nodeNum].setAsOccupied(i);
			//if the node number is the same as the ending node for the unit
			if(nodeNum==(unit.get(i).getEndingPoint().getX()+unit.get(i).getEndingPoint().getY()*34-34)){
				//increase the number of finished units
				unitsDone++;
				//set the node as an ended location
				node[nodeNum].setEnded();
				//set the unit as being done
				unit.get(i).setDone();
				//set the node as being empty
				node[nodeNum].setAsEmpty();
			}
			//remove current node from path
			unit.get(i).getPath().remove(0);
			//return unit's current node
			return nodeNum;
		}
		//if the unit's path length is 0
		else{
			//set the unit as being done
			unit.get(i).setDone();
			//return the unit's current location
			return unit.get(i).getX()+unit.get(i).getY()*34-34;
		}
	}
	
	/**
	 * Check if on open list.
	 * 
	 * @param node the node
	 * 
	 * @return true, if successful
	 */
	public boolean checkIfOnOpenList(Node node){
		for(int i=0;i<openList.size();i++){
			if(openList.get(i).getX()==node.getX() && openList.get(i).getY()==node.getY()){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Check if on closed list.
	 * 
	 * @param node the node
	 * 
	 * @return true, if successful
	 */
	public boolean checkIfOnClosedList(Node node){
		for(int i=0;i<closedList.size();i++){
			if(closedList.get(i).getX()==node.getX() && closedList.get(i).getY()==node.getY()){
				return true;
			}
		}
		return false;
	}
}
