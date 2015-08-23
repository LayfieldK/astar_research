/**
 * Kevin Layfield
 * Dr. Wellington
 * 2009
 * Sr Research - Cooperative Pathfinding Vs. Local Repair Pathfinding
 */
package layout;


import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;


/**
 * The Class MetricGatherer.
 */
public class MetricGatherer {
	
	/** The grid of nodes. */
	Node node[];
	
	/** The units that are meant for local repair. */
	ArrayList<LRUnit> lrunit;
	
	/** The units that are meant for hca. */
	ArrayList<HCAUnit> hcaunit;
	
	/** The copied version of lrunit. */
	ArrayList<LRUnit> tempUnit= new ArrayList<LRUnit>();
	
	/** The copied version of hcaunit. */
	ArrayList<HCAUnit> tempHCA= new ArrayList<HCAUnit>();
	
	/** The reservation table. */
	ResTable res;
	
	/** The copy of the reservation table. */
	ResTable tempRes;
	
	/** The set of all 30 reservation tables for a set of metric gathering. */
	ArrayList<ResTable> metricRes = new ArrayList<ResTable>();
	
	/** The set of all 30 hcaunits for a set of metric gathering. */
	ArrayList<ArrayList<HCAUnit>> metricHCA = new ArrayList<ArrayList<HCAUnit>>();
	
	/** The set of all 30 lrunits for a set of metric gathering. */
	ArrayList<ArrayList<LRUnit>> metricLR = new ArrayList<ArrayList<LRUnit>>();
	
	/** The set of all 30 grid layouts (for local repair) for a set of metric gathering. */
	ArrayList<Node[]> metricNode = new ArrayList<Node[]>();
	
	/** a duplicate copy of the 30 lrunit collections. */
	ArrayList<ArrayList<LRUnit>> metricLRA = new ArrayList<ArrayList<LRUnit>>();
	
	/** a duplicate copy of the 30 grid layouts for local repair. */
	ArrayList<Node[]> metricNodeA = new ArrayList<Node[]>();
	
	/**
	 * Instantiates a new metric gatherer.
	 * 
	 * @param node the node
	 * @param lrunit the lrunit
	 * @param hcaunit the hcaunit
	 * @param res the res
	 */
	public MetricGatherer(Node node[], ArrayList<LRUnit> lrunit, ArrayList<HCAUnit> hcaunit, ResTable res){
		this.node = node;
		this.hcaunit = hcaunit;
		this.lrunit = lrunit;
		this.res=res;
		
	}
	
	/**
	 * Instantiates a new metric gatherer.
	 * 
	 * @param metricNode the metric node
	 * @param metricLR the metric lr
	 * @param metricNodeA the metric node a
	 * @param metricLRA the metric lra
	 * @param metricHCA the metric hca
	 * @param metricRes the metric res
	 */
	public MetricGatherer(ArrayList<Node[]> metricNode,
			ArrayList<ArrayList<LRUnit>> metricLR,
			ArrayList<Node[]> metricNodeA,
			ArrayList<ArrayList<LRUnit>> metricLRA,
			ArrayList<ArrayList<HCAUnit>> metricHCA,
			ArrayList<ResTable> metricRes) {
		this.metricNode=metricNode;
		this.metricLR=metricLR;
		this.metricNodeA=metricNodeA;
		this.metricLRA=metricLRA;
		this.metricHCA=metricHCA;
		this.metricRes=metricRes;
		metricRes=null;
		metricHCA=null;
		metricLRA=null;
		metricNodeA=null;
		metricLR=null;
		metricNode=null;
	}

	/**
	 * Produce files.
	 */
	public void produceFiles(){
		produceHCAFiles();
		produceLRFiles();
		
	}
	
	/**
	 * Produce hca file.
	 */
	public void produceHCAFiles(){
		//Used for finding optimal length of paths
		LocalRepair lr;
		//two copies of 2 dimensional grid
		Node tempNode[];
		Node tempNode2[];
		//Pathfinding algorithm
		HCA hca;
		tempRes=null;
		//move through all 30 sets of grid layouts
		for(int a = 0;a<30;a++){
			hca=null;
			lr=null;
			tempNode=null;
			tempNode2=null;
			tempRes=null;
			//if this is NOT a random grid layout, a bottleneck, or a chaotic intersection layout
			if(metricRes.size()==0){
				//clear the unit list
				tempUnit.clear();
				//for each unit that was passed in, create a duplicate to work with
				for(int i = 0;i<lrunit.size();i++){
					tempUnit.add(new LRUnit(new Node(lrunit.get(i).getX(),lrunit.get(i).getY()),new Node(lrunit.get(i).getEndingPoint().getX(),lrunit.get(i).getEndingPoint().getY())));
				}
				//make duplicate grid layouts to work with
				tempNode = node;
				tempNode2=node;
				//construct a local repair object for finding paths
				lr = new LocalRepair(tempUnit,tempNode);
			}
			//if this IS a random grid layout, bottleneck, or chaotic intersection layout
			else{
				//create a local repair object out of this particular set of data
				lr = new LocalRepair(metricLR.get(a),metricNode.get(a));
			}
			//for each unit, find paths, set optimal length
	    	for(int i = 0;i<lr.getUnit().size();i++){
	    		if(metricNode.size()>0){
	        		for(int j = 1; j < 1157; j++){
	        			metricNode.get(a)[j].resetFGH();
	        		}
        		}
        		if(metricNode.size()==0){
	        		for(int j = 1; j < 1157; j++){
	        			tempNode[j].resetFGH();
	        		}
        		}
				lr.getUnit().get(i).setPath(lr.calculateUnitPath(i));
				if(!lr.getTargNotFound()){
					lr.getUnit().get(i).setOptimalLength(lr.getUnit().get(i).getPath());
					lr.getUnit().get(i).addToActualPath(lr.getUnit().get(i).getPath().get(0));
					lr.getUnit().get(i).getPath().remove(0);
				}
				
				//keeps track of unit number in the lr object
				lr.nextUnit();
	    		
			}
	    	
	    	//if this is NOT a random grid layout, bottleneck, or chaotic intersection
	    	if(metricRes.size()==0){
	    		//create a copy of the passed in grid layout
		    	tempNode = node;
		    	//clear the list of hca units
		    	tempHCA.clear();
		    	//create a new reservation table
				tempRes = new ResTable();
				
				//create a duplicate of the res table's first layer
				for(int i =1157;i<2313;i++){
					tempRes.getResNode().set(i, new ResNode(res.getResNode().get(i).getX(),res.getResNode().get(i).getY(),res.getResNode().get(i).getZ()));
					if(res.getResNode().get(i).getStatus()==ResNode.Status.empty){
						tempRes.getResNode().get(i).setAsEmpty();
					}
					if(res.getResNode().get(i).getStatus()==ResNode.Status.obstacle){
						System.out.println(i);
						tempRes.getResNode().get(i).setAsObstacle();
					}
					if(res.getResNode().get(i).getStatus()==ResNode.Status.occupied){
						int occ = res.getResNode().get(i).getOccupier();
						tempRes.getResNode().get(i).setAsOccupied(occ);
					}
					if(res.getResNode().get(i).getStatus()==ResNode.Status.end){
						int end = res.getResNode().get(i).getEndNode();
						tempRes.getResNode().get(i).setAsEndingPoint(end);
					}

				}
				//create a duplicate of the units that were passed in
				for(int i = 0;i<hcaunit.size();i++){
					tempHCA.add(new HCAUnit(new ResNode(hcaunit.get(i).getX(),hcaunit.get(i).getY(),hcaunit.get(i).getNode().getZ())));
					tempHCA.get(i).setEndingPoint(new ResNode(hcaunit.get(i).getTargX(),hcaunit.get(i).getTargY(),2));
					tempRes.getResNode().get(hcaunit.get(i).getTargX()+hcaunit.get(i).getTargY()*34-34+1156).setAsEndingPoint(i);
				}
				//reset Nodes
				for(int j = 1; j < 1157; j++){
        			tempNode[j].resetFGH();
        		}
				//create an hca object with these data structures
				hca = new HCA(hcaunit,tempRes,tempNode);
	    	}
	    	//if this IS a random grid layout, bottleneck, or chaotic intersection
	    	else{
	    		//set this grid layout to be equal to the current loop's grid layout
	    		tempNode2 = metricNode.get(a);
	    		if(metricNode.size()>0){
	        		for(int j = 1; j < 1157; j++){
	        			metricNode.get(a)[j].resetFGH();
	        		}
        		}
        		if(metricNode.size()==0){
	        		for(int j = 1; j < 1157; j++){
	        			tempNode[j].resetFGH();
	        		}
        		}
	    		//create an hca object with this loop's structures
				hca = new HCA(metricHCA.get(a),metricRes.get(a),tempNode2);
				
	    	}
	    	//list of units that must find their paths first to avoid getting pinched out of existence by other units
	    	ArrayList<Integer> mustGoFirst = new ArrayList<Integer>();
	    	//number of units that couldn't find paths
	    	int notFounds = 0;
	    	//loop that continues until all unit's find paths
	    	do{
	    		//resets res table to original values
	    		hca.getResTable().setToOriginals();
	    		//resets hca units to original positions and values
	    		for(int i = 0;i<hca.getUnit().size();i++){
	    			hca.getUnit().get(i).setToOriginals();
	    		}
	    		//for units that were found to need to go first
	    		for(int i = 0;i<mustGoFirst.size();i++){
	    			//reset nodes
	    			if(metricNode.size()>0){
    	        		for(int j = 1; j < 1157; j++){
    	        			metricNode.get(a)[j].resetFGH();
    	        		}
            		}
            		if(metricNode.size()==0){
    	        		for(int j = 1; j < 1157; j++){
    	        			tempNode2[j].resetFGH();
    	        		}
            		}
		    		//calculate these units' paths
	    			hca.getUnit().get(mustGoFirst.get(i)).setPath(hca.calculateUnitPath(mustGoFirst.get(i)));
					//remove starting node from unit path
	    			if(!hca.getTargNotFound()){
						hca.getUnit().get(mustGoFirst.get(i)).getPath().remove(0);
					}
					//hca object tracks unit numbers
					hca.nextUnit();
		    		
				}
	    		//resets number of units that couldn't find paths
	    		notFounds=0;
	    		//for the rest of the units
		    	for(int i = 0;i<hca.getUnit().size();i++){
		    		//reset nodes
	    			if(metricNode.size()>0){
    	        		for(int j = 1; j < 1157; j++){
    	        			metricNode.get(a)[j].resetFGH();
    	        		}
            		}
            		if(metricNode.size()==0){
    	        		for(int j = 1; j < 1157; j++){
    	        			tempNode2[j].resetFGH();
    	        		}
            		}
		    		//if this unit hasn't found a path yet (wasn't on the mustGoFirst list
		    		if(hca.getUnit().get(i).getPath().size()==0){
		    			//find this unit's path
						hca.getUnit().get(i).setPath(hca.calculateUnitPath(i));
						//if the unit found the destination
						if(!hca.getTargNotFound()){
							//remove the starting node from the path
							hca.getUnit().get(i).getPath().remove(0);
						}
						//if the unit couldn't find its path
						else{
							//add it to the list of units that must find path's first
							mustGoFirst.add(i);
							//reset flag variable
							hca.setTargNotFound(false);
							//increase number of units that couldn't find a path
							notFounds++;
						}
						//hca object tracks unit numbers internally
						hca.nextUnit();
		    		}
				}	
	    	}while(notFounds>0);
	    	//clears old objects no longer in use for memory purposes
	    	if(metricRes.size()>0){
		    	metricHCA.set(a,null);
		    	metricRes.set(a, null);
		    	metricLR.set(a,null);
		    	metricNode.set(a,null);
	    	}
	    	//creates a text file with metrics from this run
	    	createOutputFileHCA(a,hca.getUnit(),lr.getUnit());
	    	//erases more objects for memory purposes
	    	hca=null;
	    	tempRes=null;
	    	tempNode=null;
	    	tempNode2=null;
	    	lr=null;
		}

    }

	/**
	 * Creates the output file for an hca run.
	 * 
	 * @param z the numbered run from 1 to 30 that this output corresponds to
	 * @param tempHCAUnit the temp hca unit
	 * @param tempLRUnit the temp lr unit
	 */
	protected void createOutputFileHCA(int z, ArrayList<HCAUnit> tempHCAUnit, ArrayList<LRUnit> tempLRUnit) {

    	try{
    	    // Create file 
    	    FileWriter fstream = new FileWriter("hca"+".txt",true);
    	    BufferedWriter out = new BufferedWriter(fstream); 
    	    //for each unit
			for(int i=0; i < tempHCAUnit.size();i++){
				//append to the file - number of units in this run, number run you are on, unit number, actual path length, optimal path length, and number of cycles
				out.append(tempLRUnit.size()+ "\t"+ (z+1) + "\t" + (i+1) + "\t" +(tempHCAUnit.get(i).getTotalPathLength())+ "\t" + tempLRUnit.get(i).getOptimalLength() + "\t"+tempHCAUnit.get(i).getCycles()+"\n");
			}
			//close file
			out.close();
    	}catch (Exception e){
		    //Catch exception if any
    		System.err.println("Error: " + e.getMessage());
		}
	}

	
	
	/**
	 * Produce lr files.
	 */
	public void produceLRFiles(){
		//Pathfinding algorithm
		LocalRepair lr;
		//grid layout duplicate
		Node tempNode[]= new Node[1157];
		//for each of the 30 runs
		for(int z = 0; z < 30; z++){
			//if this isn't a random grid layout, bottleneck, or chaotic intersection
			if(metricNodeA.size()==0){
				//clear unit list
				tempUnit.clear();
				//create duplicates of the units passed in
				for(int i = 0;i<lrunit.size();i++){
					tempUnit.add(new LRUnit(new Node(lrunit.get(i).getX(),lrunit.get(i).getY()),new Node(lrunit.get(i).getEndingPoint().getX(),lrunit.get(i).getEndingPoint().getY())));
				}
				//duplicate of the grid layout passed in
				tempNode = node;
				//create lr object for pathfinding using the duplicate objects
				lr = new LocalRepair(tempUnit,tempNode);
			}
			//if this is a random grid layout, bottleneck, or chaotic intersection
			else{
				//create a lr object with this run's structures
				lr = new LocalRepair(metricLRA.get(z),metricNodeA.get(z));
			}
			//for each unit
        	for(int i = 0;i<lr.getUnit().size();i++){
        		//reset nodes
        		if(metricNodeA.size()>0){
	        		for(int j = 1; j < 1157; j++){
	        			metricNodeA.get(z)[j].resetFGH();
	        		}
        		}
        		if(metricNodeA.size()==0){
	        		for(int j = 1; j < 1157; j++){
	        			tempNode[j].resetFGH();
	        		}
        		}
        		//find path
        		lr.getUnit().get(i).setPath(lr.calculateUnitPath(i));
    			if(!lr.getTargNotFound()){
    				//set optimal length
    				lr.getUnit().get(i).setOptimalLength(lr.getUnit().get(i).getPath());
    				//remove starting node from path length
    				lr.getUnit().get(i).getPath().remove(0);
    			}
    			//lr object tracks unit number
    			lr.nextUnit();	
    		}
        	//while some units are not done traversing
        	while(lr.getUnitsDone() < lr.getUnit().size()){
        		//set unit number to 0
        		lr.setUnitNumber(0);  
        		//for each unit, execute its next move
    			for(int i = 0;i<lr.getUnit().size();i++){  
    				//reset nodes
            		if(metricNodeA.size()>0){
    	        		for(int j = 1; j < 1157; j++){
    	        			metricNodeA.get(z)[j].resetFGH();
    	        		}
            		}
            		if(metricNodeA.size()==0){
    	        		for(int j = 1; j < 1157; j++){
    	        			tempNode[j].resetFGH();
    	        		}
            		}
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
        	//if this is a random grid layout, bottleneck, or chaotic intersection
        	if(metricLRA.size()>0){
        		//create text file for this run
        		createOutputFileLocalRepair(z,metricLRA.get(z));
        	}
        	//even if it isn't a predetermined scenario
        	else{
        		//create text file for this run, with different structures
        		createOutputFileLocalRepair(z,tempUnit);
        	}
        	//memory saving
        	lr=null;
		}
	}
	
	 /**
 	 * Creates the output file local repair.
 	 * 
 	 * @param z the z
 	 * @param tempUnit the temp unit
 	 */
 	protected void createOutputFileLocalRepair(int z,ArrayList<LRUnit> tempUnit) {
	    	try{
	    	    // Create file 
	    	    FileWriter fstream = new FileWriter("localRepair"+".txt",true);
	    	    BufferedWriter out = new BufferedWriter(fstream);
	    	   
	    	    
				for(int i=0; i < tempUnit.size();i++){
					//append number of units, run number, unit number, total path length, optimal path length, and number of cycles
					out.append(tempUnit.size() + "\t"+(z+1)+"\t" + (i+1) + "\t" +(tempUnit.get(i).getTotalPathLength())+ "\t" + tempUnit.get(i).getOptimalLength()+"\t"+tempUnit.get(i).getCycles()+"\n");
				}
				//close output file
				out.close();
	    	}catch (Exception e){
	    		//Catch exception if any
			    System.err.println("Error: " + e.getMessage());
			}
		}
}
