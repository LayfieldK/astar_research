/**
 * Kevin Layfield
 * Dr. Wellington
 * 2009
 * Sr Research - Cooperative Pathfinding Vs. Local Repair Pathfinding
 */
package layout;

import java.util.ArrayList;


import layout.Node.Status;

/**
 * The Class LocalRepair.
 */
public class RRA {
	
	/** The unit. */
	LRUnit unit;
	
	/** The two dimensional grid. */
	Node node[];
	
	/** Denotes whether or not the node you are searching for has been found. */
	boolean reqNodeFound=false;
	
	/** The list of values that correspond to surrounding nodes of the current node. */
	int adjacents[]={69,68,67,35,33,1,0,-1};

	/** The open list. */
	ArrayList<Node> openList = new ArrayList<Node>();
	
	/** The closed list. */
	ArrayList<Node> closedList = new ArrayList<Node>();
	
	/** The unit number. */
	int unitNumber=0;
	
	/** The destination index. */
	int destIndex=0;
	
	/** The units done. */
	int unitsDone=0;
	
	/** The path. */
	ArrayList<Integer> path;
	
	/** The targ not found. */
	boolean targNotFound=false;
	
	/**
	 * Instantiates a new local repair.
	 * 
	 * @param unit the unit
	 * @param node the node
	 */
	public RRA(LRUnit unit ,Node node[]) {
		this.unit=unit;
		this.node = node;
	}
	
	
	/**
	 * Calculate true distance.
	 * 
	 * @param reqNode the requested node
	 * 
	 * @return the requested node's g value
	 */
	public int calculateTrueDistance(int reqNode){
		//for each node in the closed list
		for(int i = 0;i<closedList.size();i++){
			//if the requested node is on the closed list
			if(closedList.get(i).getX()+closedList.get(i).getY()*34-34 == reqNode){
				//return that node's g value
				return closedList.get(i).getG();
			}
		}
		//current open list node's f value
		int f;
		//the index in the open list of the lowest f value
		int savedOpenIndex=0;
		//current lowest f value in open list
		int lowf = 9999;
		//current coordinates
		int curX,curY;
		//cost to move to potential node
		int moveCost;
		//sets all nodes on the grid to have no parent node
		for(int i = 1; i < 1157;i++){
			node[i].setParentNode(9999);
		}
		//adds the unit's current position to the open list
		openList.add(unit.getNode()); 
		//denotes whether or not the requested node's g value has been obtained yet
		reqNodeFound=false;
		//while the requested node has still not been found
		while(reqNodeFound==false){
			//initializes the position of the lowest f value in the open list
			savedOpenIndex=0;
			//initializes the lowest f value
			lowf=9999;
			//searches each node on the open list
			for(int i=0;i<openList.size();i++){
				//sets current open list node's f value
				f = openList.get(i).calculateF();
				//checks to see if this open list node's f value is the lowest yet
				if(f<lowf){
					//if it is, set the new lowest value, and save this index on the open list
					lowf=f;
					savedOpenIndex=i;
				}
			}
			//add the node with the lowest f value on the open list to the closed list
			closedList.add(openList.get(savedOpenIndex));
			//if this node is the requested node, record that you have found the node
			if(openList.get(savedOpenIndex).getX()+openList.get(savedOpenIndex).getY()*34-34==reqNode){
				reqNodeFound=true;
			}
			//sets current x to this newly searched node's x value
			curX=openList.get(savedOpenIndex).getX();
			//sets current y to this newly searched node's y value
			curY=openList.get(savedOpenIndex).getY();
			//remove the node from the open list
			openList.remove(savedOpenIndex);
			
			//for each of the eight surrounding nodes of the current node
			for(int i =0;i<adjacents.length;i++){
				//if the node being checked is a diagonal move, the movecost is 14
				if(i==0 || i==2 || i == 5 || i==7){
					moveCost = 14;
				}
				//if the node being checked is an adjacent move, the movecost is 10
				else{
					moveCost = 10;
				}
				
				//if the node being checked is not already on the closed list, and isn't an obstacle, and...
				if(!((checkIfOnClosedList(node[curX+(curY*34-adjacents[i])]) || (node[curX+(curY*34-adjacents[i])].getStatus()==Status.obstacle)) ||					
					//you're not moving to a diagonal node where one of the corresponding adjacent nodes is an obstacle
					//(in other words, you can't cut the corner of an obstacle)
					(i==0 && node[curX+(curY*34-adjacents[i]+1)].getStatus()==Status.obstacle) ||
					(i==0 && node[curX+(curY*34-adjacents[i]+34)].getStatus()==Status.obstacle) ||
					(i==2 && node[curX+(curY*34-adjacents[i]-1)].getStatus()==Status.obstacle) || 
					(i==2 && node[curX+(curY*34-adjacents[i]+34)].getStatus()==Status.obstacle) ||
					(i==5 && node[curX+(curY*34-adjacents[i]+1)].getStatus()==Status.obstacle) || 
					(i==5 && node[curX+(curY*34-adjacents[i]-34)].getStatus()==Status.obstacle) ||
					(i==7 && node[curX+(curY*34-adjacents[i]-1)].getStatus()==Status.obstacle) || 
					(i==7 && node[curX+(curY*34-adjacents[i]-34)].getStatus()==Status.obstacle) 
					)){
					//if the searched node is already on the open list
					if(checkIfOnOpenList(node[curX+(curY*34-adjacents[i])])){
						//if the node's new g value is less than the previous g value
						if(node[curX+(curY*34-adjacents[i])].calculateG(node[curX+(curY*34-34)],moveCost) < node[curX+(curY*34-adjacents[i])].getG()){
							//set the parent node of this searched node to fall in line with this new, shorter path
							node[curX+(curY*34-adjacents[i])].setParentNode(curX+(curY*34-34));
							//recalculate new g and f values for the node
							node[curX+(curY*34-adjacents[i])].setG(node[curX+(curY*34-34)],moveCost);
							node[curX+(curY*34-adjacents[i])].setF();
						}
					}
					//if the searched node is not on the open list
					else{
						//add it to the open list
						openList.add(node[curX+(curY*34-adjacents[i])]);
						//set the parent node as the node you are searching surrounding nodes from
						node[curX+(curY*34-adjacents[i])].setParentNode(curX+(curY*34-34));
						//set g h and f values for the node
						node[curX+(curY*34-adjacents[i])].setG(node[curX+(curY*34-34)],moveCost);
						node[curX+(curY*34-adjacents[i])].setH(unit.getEndingPoint());

						node[curX+(curY*34-adjacents[i])].setF();
					}
				}
					
			}
			//for each entry on the closed list
			for(int i=0;i<closedList.size();i++){
				//if any entry on the closed list matches the destination node for the unit
				if(closedList.get(i).getX()==unit.getEndingPoint().getX() && closedList.get(i).getY()==unit.getEndingPoint().getY()){
					//set the destination index to that index in the closed list
					destIndex = i;
				}
			}
			//if no more nodes are searchable on the grid, denote that the destination wasn't found
			if(openList.isEmpty()){
				targNotFound=true;
			}
		}
		//return the g value for the requested node
		int nodeG = node[reqNode].getG();
		return nodeG ;
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
