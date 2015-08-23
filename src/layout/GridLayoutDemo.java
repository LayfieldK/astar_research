/**
 * Kevin Layfield
 * Dr. Wellington
 * 2009
 * Sr Research - Cooperative Pathfinding Vs. Local Repair Pathfinding
 */

package layout;

/*
 * GridLayoutDemo.java
 *
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.LineBorder;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Random;

import layout.Node.Status;

// TODO: Auto-generated Javadoc
/**
 * The Class GridLayoutDemo.
 */
public class GridLayoutDemo extends JFrame {
    
    /** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/** The Constant placementList. */
	static final String placementList[] = {"Starting Point", "Ending Point", "Obstacle"};
    
    /** The Constant pathfindingList. */
    static final String pathfindingList[] = {"Local Repair A*", "Hierarchical Cooperative A*"};
    
    /** The Constant maxGap. */
    final static int maxGap = 20;
    
    /** The pathfinding combo box. */
    JComboBox placementComboBox, pathfindingComboBox;
    
    /** The pathfinding initiation button. */
    JButton applyButton = new JButton("Find Paths");
    
    /** The gather metrics button. */
    JButton metricsButton = new JButton("Gather Metrics");
    
    /** The bottleneck button. */
    JButton pre1Button = new JButton("Bottleneck Layout");
    
    /** The chaotic intersections button. */
    JButton pre2Button = new JButton("Chaotic Intersections Layout");
    
    /** The random layout button. */
    JButton randButton = new JButton("Random Grid Layout");
    
    /** The clear grid button. */
    JButton clearButton = new JButton("Clear Grid");
    
    /** The next move button. */
    JButton nextButton = new JButton("Next Move");
    
    /** The restart button. */
    JButton restartButton = new JButton("Start New Grid");
    
    /** The experiment layout. */
    GridLayout experimentLayout = new GridLayout(35,35);
    
    /** The starting placed. invisible label that helps track number of units placed */
    JLabel startingPlaced=new JLabel("0");
    
    /** The local repair pathfinding object. */
    LocalRepair lr;
    
    /** The hca pathfinding object. */
    HCA hca;
    
    /** The metric gathering object. */
    MetricGatherer mg;
    
    /** The ending placed invisible label that helps track number of ending points placed. */
    JLabel endingPlaced = new JLabel("0");
    
    /** The buttons panel. */
    JPanel controls = new JPanel();
    
    /** The second button panel. */
    JPanel controls2 = new JPanel();
    
    /** The visual representation of nodes. */
    JLabel label[][] = new JLabel[35][35];
    
    /** The actual grid. */
    Node node[] = new Node[1157];
    
    /** The collection of units to be used in local repair. */
    ArrayList<LRUnit> lrunit = new ArrayList<LRUnit>();
    
    /** The duplicate copy of units for local repair. */
    ArrayList<LRUnit> lrunittest = new ArrayList<LRUnit>();
    
    /** The collection of units to be used by HCA. */
    ArrayList<HCAUnit> hcaunit = new ArrayList<HCAUnit>();
    
    /** The duplicate copy of units for HCA. */
    ArrayList<HCAUnit> hcaunittest = new ArrayList<HCAUnit>();
    
    /** The reservation table used by HCA. */
    ResTable res;
    
    /** The duplicate copy of the reservation table used by HCA. */
    ResTable restest;
    
    /** The unit count. */
    int unitCount=0;
    
    /** The number of ending locations on the grid. */
    int endCount=0;
    
    /** The number of starting locations on the grid. */
    int startCount=0;
    
    /** Denotes whether or not paths have already been found and visual progression of pathfinding has begun. */
    boolean started=false;
	
	/** The number of moves that have been made during HCA visual progression. */
	int hcaMoveNum=1;
    
	/** The random number generator. */
	Random gen = new Random();
	
	/** The collection of reservation tables to be be processed all at once by metric gatherer. */
	ArrayList<ResTable> metricRes = new ArrayList<ResTable>();
	
	/** The collection of hca unit lists to be be processed all at once by metric gatherer. */
	ArrayList<ArrayList<HCAUnit>> metricHCA = new ArrayList<ArrayList<HCAUnit>>();
	
	/** The collection of local repair unit lists to be be processed all at once by metric gatherer. */
	ArrayList<ArrayList<LRUnit>> metricLR = new ArrayList<ArrayList<LRUnit>>();
	
	/** The collection of duplicate local repair unit lists to be be processed all at once by metric gatherer. */
	ArrayList<ArrayList<LRUnit>> metricLRA = new ArrayList<ArrayList<LRUnit>>();

	/** The collection of grid layouts to be be processed all at once by metric gatherer. */
	ArrayList<Node[]> metricNode = new ArrayList<Node[]>();
	
	/** The collection of duplicate grid layouts to be be processed all at once by metric gatherer. */
	ArrayList<Node[]> metricNodeA = new ArrayList<Node[]>();

	/** A number corresponding to which scenario is being run by metric gatherer. */
	int metricScenario = 0;
	
	/** The number of units being tested in the metric gatherer. */
	int metricUnitNum = 0;

    /**
     * Instantiates a new grid layout.
     * 
     * @param name the name of the grid layout
     */
    public GridLayoutDemo(String name) {
        super(name);
        //no resizing window so as to not allow distortion of the grid
        setResizable(false);
    }
    
    /**
     * Initializes buttons and drop down menus.
     */
    public void initGaps() {
        placementComboBox = new JComboBox(placementList);
        pathfindingComboBox = new JComboBox(pathfindingList);
        controls2.setVisible(false);
    }

    /**
     * Gets the local repair object.
     * 
     * @return the local repair object
     */
    public LocalRepair getLR(){
    	return lr;
    }
    
    /**
     * Gets the hca object.
     * 
     * @return the hca object
     */
    public HCA getHCA(){
    	return hca;
    }
    
    /**
     * Checks if no path exists for a set of units and a grid layout.
     * 
     * @param lrunite the lrunite
     * @param nodee the nodee
     * 
     * @return true, if is no path
     */
    public boolean isNoPath(ArrayList<LRUnit> lrunite, Node nodee[]){
    	lr = new LocalRepair(lrunite,nodee);
    	//for each unit, calculate path
    	for(int i = 0;i<lr.getUnit().size();i++){
			lr.getUnit().get(i).setPath(lr.calculateUnitPath(i));
    	}
    	//if one of the units couldn't find a path, return true
    	if(lr.getTargNotFound()){
    		return true;
    	}
    	//if all units found paths, return false
    	return false;
    }
    
    /**
     * Creates an obstacle on a 2 dimensional plane.
     * 
     * @param x the x coordinate
     * @param y the y coordinate
     */
    public void createObstacle(int x, int y){
    	//visualize the obstacle
    	label[x][y].setBackground(Color.BLACK);
    	//place obstacle on real grid
    	node[x+y*34-34].setAsObstacle();
    }
    
    /**
     * Creates the obstacle on the 2 dimensional plane and the reservation table.
     * 
     * @param x the x
     * @param y the y
     */
    public void createObstacle2(int x, int y){
    	//visualize the obstacle
    	label[x][y].setBackground(Color.BLACK);
    	//place obstacle on real grid
    	node[x*34+y-34].setAsObstacle();
    	//set the obstacle in the reservation table
    	res.getResNode().get(x * 34+y-34+1156).setAsObstacle();
    }
    
    /**
     * places an obstacle on a grid with no visual representation
     * Used by tests and metric gathering.
     * 
     * @param x the x
     * @param y the y
     */
    public void obstNoGUI(int x, int y){
    	//place obstacle on real grid
    	node[x*34+y-34].setAsObstacle();
    	//set the obstacle in the reservation table
    	res.getResNode().get(x * 34+y-34+1156).setAsObstacle();
    }
    
    /**
     * Creates the obstacle for the metric gatherer.
     * 
     * @param x the x coord
     * @param y the y coord
     * @param metric the metric grid layout
     * @param metricR the metric reservation table
     */
    public void createObstacleMetric(int x, int y, Node metric[], ResTable metricR){
    	//set the 2 dimensional obstacle
    	metric[x*34+y-34].setAsObstacle();
    	//set the 3 dimensional obstacle on the reservation table
    	metricR.getResNode().get(x * 34+y-34+1156).setAsObstacle();
    }
    
    /**
     * Removes the obstacle.
     * 
     * @param x the x coord
     * @param y the y coord
     */
    public void removeObstacle(int x, int y){
    	//visually erase obstacle
    	label[x][y].setBackground(Color.WHITE);
    	//remove obstacle from real grid
    	node[x*34+y-34].setAsEmpty();
    	//remove obstacle from reservation table
    	res.getResNode().get(x * 34+y-34+1156).setAsEmpty();
    }
    
    /**
     * Adds the components to pane.
     * 
     * @param pane the pane
     */
    public void addComponentsToPane(final Container pane) {
    	//create buttons panel and combo boxes
        initGaps();
        //creates new JPanel
        final JPanel compsToExperiment = new JPanel();
        //sets the layout of the panel
        compsToExperiment.setLayout(experimentLayout);
        //add both button panels to the layout
        controls.setLayout(new GridLayout(0,4));
        controls2.setLayout(new GridLayout(0,2));
        //Set up components preferred size
        JButton b = new JButton("Just fake button");
        Dimension buttonSize = b.getPreferredSize();
        compsToExperiment.setPreferredSize(new Dimension((int)(buttonSize.getWidth() * 6.75),
                (int)(buttonSize.getHeight() * 25)));
        //construct new reservation table
        res = new ResTable();
        //from 1 to 34 horizontally and vertically
        //This creates the grid
        for(int i=1;i<=34;i++){
        	for(int j=1;j<=34;j++){
        		//create a new node
        		node[i+(j*34-34)]= new Node(i,j);
        	}
        }
        //again, from 1 to 34 horizontally and vertically
        for(int i=1;i<=34;i++){
        	for(int j=1;j<=34;j++){
        		//create a new label for those coordinates (visual representation of grid)
	        	label[i][j]=new JLabel();
	        	//add the label to the layout, each label represents a node
	        	compsToExperiment.add(label[i][j]);
	        	//create a black border around each label
	        	label[i][j].setBorder(new LineBorder(Color.BLACK,1));
	        	//set the label as opaque (makes color changes possible)
	        	label[i][j].setOpaque(true);
	        	//set the label as white to represent empty
	        	label[i][j].setBackground(Color.WHITE);
	        	//if the label is a border node on the grid, make that node/label an obstacle
	        	if(i==1 || j==1 || i ==34 || j==34){
	        		createObstacle(i,j);
	        	}
	        	//x and y values
	        	final int indexI=i;
	        	final int indexJ=j;
	        	//adds a mouse click listener on each label on the grid
	        	label[i][j].addMouseListener(new MouseAdapter(){
	        		public void mousePressed(MouseEvent me)
	        		{   
	        			//reads value from invisible label that tells how many units have been placed
	        			startCount= Integer.parseInt(startingPlaced.getText());
	        			//reads value from invisible label that tells how many ending locations have been placed
	        			endCount=Integer.parseInt(endingPlaced.getText());
	        			//text value of the placement type combo box
	        			String placement = (String)placementComboBox.getSelectedItem();
	        			//if the combo box was 'obstacle' and pathfinding hasn't begun yet, and the label is currently white or black
	        			if(placement.equalsIgnoreCase("obstacle")&& started==false &&((me.getComponent().getBackground().equals(Color.WHITE)) || (me.getComponent().getBackground().equals(Color.BLACK)))){
	        				//resets metric arrays because user input has been accepted
	        				clearMetricArrays();
	        				//sets the node as an obstacle
	        				if(!(node[indexI*34+indexJ-34].getStatus()==Status.obstacle)){
	        					createObstacle2(indexI,indexJ);
	        				}
	        				else{
	        					//if the node wasn't an obstacle, remove the obstacle
	        					removeObstacle(indexI,indexJ);
	        				}
	        			}
	        			//if the placement type was starting point and the background of the label was white
	        			if(placement.equalsIgnoreCase("starting point")&& started==false &&(me.getComponent().getBackground().equals(Color.WHITE))){
	        				//set the label as green
	        				me.getComponent().setBackground(Color.GREEN);
	        				//increase number of starting points placed
	        				startCount++;
	        				//increase starting number on invisible label
	        				startingPlaced.setText(Integer.toString(startCount));
	        				//puts the unit number on the starting point
	        				label[indexI][indexJ].setText(Integer.toString(startCount));
	        				//set that node as a starting point
	        				node[indexI*34+(indexJ-34)].setAsStartingPoint();
	        				//create a new unit object for both pathfinding algorithms
	        				lrunit.add(new LRUnit(node[indexI*34+(indexJ-34)]));
	        				hcaunit.add(new HCAUnit(res.getResNode().get(indexI*34+(indexJ-34) + 1156)) );
	        				//increase unit count
	        				unitCount++;
	        				//erase metric arrays because user input has been accepted
	        				clearMetricArrays();
	        			}
	        			//if the placement type is ending point, pathfinding hasn't started, and the background of the label is white
	        			if(placement.equalsIgnoreCase("ending point")&& started==false &&(me.getComponent().getBackground().equals(Color.WHITE))){
	        				//check to see that starting points is higher than ending points placed
	        				if(unitCount>endCount){
	        					//sets the label as red
	        					me.getComponent().setBackground(Color.RED);
	        					//increase number of ending points placed
	        					endCount++;
	        					//increment value of ending places on invisible label
	        					endingPlaced.setText(Integer.toString(endCount));
	        					//set text of the destination label = to the corresponding unit number
	        					label[indexI][indexJ].setText(""+endCount);
	        					//set the node as an ending point
	        					node[indexI*34+(indexJ-34)].setAsEndingPoint(endCount);
	        					//set this as the corresponding ending point to the unit object
	        					lrunit.get(endCount-1).setEndingPoint(node[indexI*34+(indexJ-34)]);
	        					hcaunit.get(endCount-1).setEndingPoint(res.getResNode().get(indexI*34+(indexJ-34)+1156));
	        					//set the ending point on the reservation table
	        					res.getResNode().get(indexI*34+indexJ-34+1156).setAsEndingPoint(endCount);
	        					//clear the metric arrays because user input has been accepted
	        					clearMetricArrays();
	        				}
	        				//if there are an equal amount of starting and ending points already
	        				else{
	        					JOptionPane.showMessageDialog(me.getComponent(),"You can't place more ending locations than starting locations.");
	        				}
	        			}
	        		} 
	        	});
	        }
        }
        
        //label that assists user
        JLabel plt = new JLabel("Placement Type");
        controls.add(plt);
        plt.setBorder(new LineBorder(Color.BLACK,1));
        //add placement type combo box to control panel
        controls.add(placementComboBox);
        //label that assists user
        JLabel pat = new JLabel("Pathfinding Type");
        controls.add(pat);
        pat.setBorder(new LineBorder(Color.BLACK,1));
        //combo box that lets user choose algorithm
        controls.add(pathfindingComboBox);
        //bottleneck button
        controls.add(pre1Button);
        //chaotic intersections button
        controls.add(pre2Button);
        //random layout button
        controls.add(randButton);
        //clear grid button
        controls.add(clearButton);
        //find paths button
        controls.add(applyButton);
        //gather metrics button
        controls.add(metricsButton);
        //start a new grid button
        controls2.add(restartButton);
        //next move for all units button
        controls2.add(nextButton);
        
        //if someone clicks the gather metrics button
        metricsButton.addActionListener(new ActionListener(){
        	public void actionPerformed(ActionEvent l){
        		//create pathfinding object of lr type
        		lr = new LocalRepair(lrunit,node);
        		//tracks the unit number that has no possible path
            	int noPathNum=0;
            	//tracks whether or not this is the first unit in a layout that had no path
            	boolean firstNoPath=false;
            	//for each unit
            	for(int i = 0;i<lr.getUnit().size();i++){
            		//calculate a path for this unit
        			lr.getUnit().get(i).setPath(lr.calculateUnitPath(i));
        			//if a path was found
        			if(!lr.getTargNotFound()){
        				//this path length is the optimal path length for each unit, record it
        				lr.getUnit().get(i).setOptimalLength(lr.getUnit().get(i).getPath());
        				//remove starting node from path for each unit
        				lr.getUnit().get(i).getPath().remove(0);
        			}
        			//if this is the first unit to have no possible path
        			if(lr.getTargNotFound() && firstNoPath==false){
        				//record unit number
        				noPathNum=i+1;
        				//tell user that a unit has no path available
    					JOptionPane.showMessageDialog(controls,"Unit " + (noPathNum) +" has no possible path to its corresponding destination.  Try a different grid layout.");
    					//stop the pathfinding process
    					started=false;
    					//denote that a unit in this layout has already had no path available
        				firstNoPath=true;
        				
        			}
        			//lr object tracks unit number
        			lr.nextUnit();
        		}
            	//if a unit found no path, do nothing
            	if(lr.getTargNotFound()){}
            	//if all units found paths
            	else{
            		//if this isn't one of the 3 predetermined scenarios
            		if(metricScenario==0){
            			//create a metric gatherer with the current grid, reservation table, and unit lists
            			mg = new MetricGatherer(node,lrunit,hcaunit,res);
            			//produce output files
            			mg.produceFiles();
            		}
            		//if this is one of the 3 predetermined scenarios
            		else{
            			//temporary copy of metric scenario value
        				int tempMetrScen = metricScenario;
        				//if this is a bottleneck scenario
            			if(metricScenario==1){
            				//create 30 bottleneck sets of data
            				for(int i=0;i<30;i++){
            					getRandomizedBottleneck(metricUnitNum);
            				}
            			}
            			//if this is a chaotic intersection scenario
            			if(metricScenario==2){
            				//create 30 CI sets of data
            				for(int i = 0;i<30;i++){
            					getRandomizedCI(metricUnitNum);
            				}
            			}
            			//if this is a random layout scenario
            			if(metricScenario==3){
            				//create 30 random sets of data
            				for(int i = 0; i<30;i++){
            					getRandomizedRandom(metricUnitNum);
            				}
            			}
            			//create the metric gatherer with the collected sets of data
            			mg = new MetricGatherer(metricNode,metricLR,metricNodeA,metricLRA,metricHCA,metricRes);
            			//produce output files
            			mg.produceFiles();
            			//clear the metric arrays that were just created
                		clearMetricArrays();
                		//reset the recently erased value of metric scenario
            			metricScenario = tempMetrScen;
            		}
            		//clear the visual grid and the real grid
            		clearGrid();
            	}
        	}
        });
        
        //if the start new grid button is clicked in the middle of a visual pathfinding process
        restartButton.addActionListener(new ActionListener(){
        	public void actionPerformed(ActionEvent k){
        		//clear the grid visually and for real
        		clearGrid();
        		//remove the pathfinding control panel
        		controls2.setVisible(false);
        		//revert to the placement control panel
        		controls.setVisible(true);
        		//allow placement of object on the grid again
        		started=false;
        	}
        });
        
        //if the random grid layout button is clicked during setup phase
        randButton.addActionListener(new ActionListener(){
        	public void actionPerformed(ActionEvent j){
        		//initialize number of units to use
        		int numUnits=101;
        		//create a duplicate reservation table
        		restest = new ResTable();
        		//create a duplicate collection of hca units
        		hcaunittest = new ArrayList<HCAUnit>();
        		//until the user inputs a valid number of units to use
        		while(numUnits>100 || numUnits <1){
        			//ask user how many units to place
	        		numUnits= Integer.parseInt(JOptionPane.showInputDialog(null, 
	                "How many units do you want to be placed (1-100)?"));
        		}
        		//if the user decides to gather metrics, this same number of units will be used all 30 trials
        		metricUnitNum=numUnits;
        		//repeat loop until a random layout has been created where all units can reach their destinations
        		do{
	        		clearGrid();
	        		//list of all nodes on the grid, they are removed as they are used for units, destinations, obstacles
	        		ArrayList<Integer> drawPool = new ArrayList<Integer>();
	        		//list of obstacle locations
	        		ArrayList<Integer> obstacles = new ArrayList<Integer>();
	        		//list of starting locations
	        		ArrayList<Integer> starts = new ArrayList<Integer>();
	        		//list of ending locations
	        		ArrayList<Integer> ends = new ArrayList<Integer>();
	        		//adds every node to the draw pool
	        		for(int x = 2;x<34;x++){
	        			for(int y = 2;y<34;y++){
	        				drawPool.add(x+y*34-34);
	        			}
	        		}
	        		//random number generator
	        		Random gen = new Random();
	        		//random number
	        		int drawIndex=0;
	        		//place obstacles on 30% of the grid
	        		for(int h =0;h<205;h++){
	        			//randomly pull a node from the draw pool
	        			drawIndex=gen.nextInt(drawPool.size());
	        			//add that node to the obstacles list
	        			obstacles.add(drawPool.get(drawIndex));
	        			//remove that number from the draw pool
	        			drawPool.remove(drawIndex);
	        		}
	        		//place however many units the user wanted to place
	        		for(int h=0;h<numUnits;h++){
	        			//pull from the draw pool again
	        			drawIndex=gen.nextInt(drawPool.size());
	        			starts.add(drawPool.get(drawIndex));
	        			drawPool.remove(drawIndex);
	        		}
	        		//place the same number of ending points as starting points
	        		for(int h=0;h<numUnits;h++){
	        			//pull from the draw pool again
	        			drawIndex=gen.nextInt(drawPool.size());
	        			ends.add(drawPool.get(drawIndex));
	        			drawPool.remove(drawIndex);
	        		}
	        		//for each unit
	        		for(int h = 0; h< numUnits; h++){
	        			//use the starts array to place a starting point on the grid
	        			node[starts.get(h)].setAsStartingPoint();
	        			//increase number of units placed
	        			startCount++;
	        			//set the invisible label's text to reflect placed unit number
	    				startingPlaced.setText(Integer.toString(startCount));
	    				//set text for the label corresponding to the unit's starting place
	    				label[starts.get(h)/34 +1][starts.get(h)%34].setText(Integer.toString(startCount));
	    				//set unit starting place as green
	    				label[starts.get(h)/34 +1][starts.get(h)%34].setBackground(Color.GREEN);
	    				//add the unit to the unit lists for both algorithms
	    				lrunit.add(new LRUnit(node[starts.get(h)]));
	    				hcaunit.add(new HCAUnit(res.getResNode().get(starts.get(h)+1156)));
	    				//also add it to the duplicate copy of hca unis
	    				hcaunittest.add(new HCAUnit(res.getResNode().get(starts.get(h)+1156)));
	    				//increase total number of units
	    				unitCount++;
	    				//increase total number of ending points
	    				endCount++;
	    				//set the text of the invisible label that tracks number of destinations placed
						endingPlaced.setText(Integer.toString(endCount));
						//sets the text of the destination label to reflect the corresponding unit number
						label[ends.get(h)/34 +1][ends.get(h)%34].setText(""+endCount);
						//sets the label as red
						label[ends.get(h)/34 +1][ends.get(h)%34].setBackground(Color.RED);
						//sets the node as an ending point in the grid
						node[ends.get(h)].setAsEndingPoint(h+1);
						//sets the node as an ending point on the reservation table
						res.getResNode().get(ends.get(h)+1156).setAsEndingPoint(h+1);
						//and on the duplicate reservation table
						restest.getResNode().get(ends.get(h)+1156).setAsEndingPoint(h+1);
						//set the ending points for the units for both algorithms and the duplicate hca unit list
						lrunit.get(endCount-1).setEndingPoint(node[ends.get(h)]);
						hcaunit.get(endCount-1).setEndingPoint(res.getResNode().get(ends.get(h)+1156));
						hcaunittest.get(endCount-1).setEndingPoint(res.getResNode().get(ends.get(h)+1156));
	        		}
	        		//creates obstacles that were randomly pulled earlier
	        		for(int h=0;h<obstacles.size();h++){
	        			createObstacle2(obstacles.get(h)/34+1,obstacles.get(h)%34);
	        		}
        		}while(isNoPath(lrunit,node));
        		//sets the metric scenario  to the random scenario value
        		metricScenario=3;
        	}
        });
        
        //if someone clicks the chaotic intersections button
        pre2Button.addActionListener(new ActionListener(){
        	public void actionPerformed(ActionEvent i){
        		//clear the grid
        		clearGrid();
        		//instantiate number of units to place
        		int numUnits=101;
        		//while input isn't valid
        		while(numUnits>100 || numUnits<1){
        			//ask user how many units to place
	        		numUnits= Integer.parseInt(JOptionPane.showInputDialog(null, 
	                "How many units do you want to be placed (1-100)?"));
        		}
        		//if user wants to gather metrics, use the same number of units that he or she just entered all 30 times
        		metricUnitNum=numUnits;
        		//staring locations
        		ArrayList<Integer> starts = new ArrayList<Integer>();
        		//ending locations
        		ArrayList<Integer> ends = new ArrayList<Integer>();
        		//index variable
        		int k=0;
        		//sets top row of grid as starting points
        		while(starts.size()<32){
        			starts.add(36+k);
        			k++;
        		}
        		//sets right edge of grid as starting points
        		k=3;
        		while(starts.size()<62){
        			starts.add((34*k)-1);
        			k++;
        		}
        		//sets bottom row of grid as starting points
        		k=1;
        		while(starts.size()<93){
        			starts.add(1156-34-k);
        			k++;
        		}
        		//sets left edge of grid as starting points
        		k=0;
        		while(starts.size()<124){
        			starts.add(1090-34*k);
        			k++;
        		}
        		//removes certain nodes that are not meant to be start points but were on the edges
        		starts.remove(123);
        		starts.remove(121);
        		starts.remove(108);
        		starts.remove(96);
        		starts.remove(94);
        		starts.remove(85);
        		starts.remove(83);
        		starts.remove(81);
        		starts.remove(77);
        		starts.remove(75);
        		starts.remove(73);
        		starts.remove(71);
        		starts.remove(61);
        		starts.remove(59);
        		starts.remove(46);
        		starts.remove(34);
        		starts.remove(32);
        		starts.remove(23);
        		starts.remove(21);
        		starts.remove(19);
        		starts.remove(15);
        		starts.remove(13);
        		starts.remove(11);
        		starts.remove(9);
        		//creates destination nodes that are directly across from corresponding starting positions
        		for(int l=0;l<25;l++){
        			ends.add(starts.get(l)+(34*30));
        		}
        		for(int l=25;l<50;l++){
        			ends.add(starts.get(l)-30);
        		}
        		for(int l=50;l<75;l++){
        			ends.add(starts.get(l)-(34*30));
        		}
        		for(int l=75;l<100;l++){
        			ends.add(starts.get(l)+30);
        		}
        		//list of starting positions that have been randomly assigned
        		ArrayList<Integer> random = new ArrayList<Integer>();
        		//shuffled starting positions
        		ArrayList<Integer> newStarts = new ArrayList<Integer>();
        		//shuffled ending positions
        		ArrayList<Integer> newEnds = new ArrayList<Integer>();
        		//for each unit, 
        		for(int a = 0; a<numUnits;a++){
        			//randomly pick one of the starting nodes
        			int genNum = gen.nextInt(starts.size());
        			//keep picking starting nodes until you get one that hasn't been used yet
        			while(random.contains(genNum)){
        				genNum=gen.nextInt(starts.size());
        			}
        			//add the starting node to the random list
        			random.add(genNum);
        			//add the new starting point to the newstarts list
        			newStarts.add(starts.get(genNum));
        			//add the corresponding ending point to the newends list
        			newEnds.add(ends.get(genNum));
        		}
        		//set the original starts list equal to the new one
        		starts=newStarts;
        		//set the original ends list equal to the new one
        		ends=newEnds;
        		//for each unit
        		for(int j = 0; j< numUnits; j++){
        			//use the starts array to place a starting point on the grid
        			node[starts.get(j)].setAsStartingPoint();
        			//increase number of units placed
        			startCount++;
        			//set the invisible label's text to reflect placed unit number
    				startingPlaced.setText(Integer.toString(startCount));
    				//set text for the label corresponding to the unit's starting place
    				label[starts.get(j)/34 +1][starts.get(j)%34].setText(Integer.toString(startCount));
    				//set unit starting place as green
    				label[starts.get(j)/34 +1][starts.get(j)%34].setBackground(Color.GREEN);
    				//add the unit to the unit lists for both algorithms
    				lrunit.add(new LRUnit(node[starts.get(j)]));
    				hcaunit.add(new HCAUnit(res.getResNode().get(starts.get(j)+1156)));
    				//increase total number of units
    				unitCount++;
    				//increase total number of ending points
    				endCount++;
    				//set the text of the invisible label that tracks number of destinations placed
					endingPlaced.setText(Integer.toString(endCount));
					//sets the text of the destination label to reflect the corresponding unit number
					label[ends.get(j)/34 +1][ends.get(j)%34].setText(""+endCount);
					//sets the label as red
					label[ends.get(j)/34 +1][ends.get(j)%34].setBackground(Color.RED);
					//sets the node as an ending point in the grid
					node[ends.get(j)].setAsEndingPoint(j+1);
					//sets the node as an ending point on the reservation table
					res.getResNode().get(ends.get(j)+1156).setAsEndingPoint(j+1);
					//set the ending points for the units for both algorithms and the duplicate hca unit list
					lrunit.get(endCount-1).setEndingPoint(node[ends.get(j)]);
					hcaunit.get(endCount-1).setEndingPoint(res.getResNode().get(ends.get(j)+1156));
        		}
        		//sets the metric scenario value equal to chaotic intersections
        		metricScenario=2;	
        	}
        });
        
        //if the user clicks the bottleneck button
        pre1Button.addActionListener(new ActionListener(){
        	public void actionPerformed(ActionEvent h){
        		//clear the grid
        		clearGrid();
        		//instantiate number of units to be placed in scenario
        		int numUnits=101;
        		//while input is invalid
        		while(numUnits>100 || numUnits<1){
        			//ask user how many units to place
	        		numUnits= Integer.parseInt(JOptionPane.showInputDialog(null, 
	                "How many units do you want to be placed (1-100)?"));
        		}
        		//if the user wants to gather metrics with this scenario, always use the name number of units
        		metricUnitNum=numUnits;
        		//starting locations
        		ArrayList<Integer> starts = new ArrayList<Integer>();
        		//ending locations
        		ArrayList<Integer> ends = new ArrayList<Integer>();
        		//obstacle locations
        		ArrayList<Integer> obstacles = new ArrayList<Integer>();
        		
        		//add all of the possible starting locations to a list
        		starts.add(36);
        		starts.add(38);
        		starts.add(40);
        		starts.add(42);
        		starts.add(44);
        		starts.add(46);
        		starts.add(48);
        		starts.add(50);
        		starts.add(52);
        		starts.add(54);
        		starts.add(56);
        		starts.add(58);
        		starts.add(60);
        		starts.add(62);
        		starts.add(64);
        		starts.add(66);
        		starts.add(71);
        		starts.add(73);
        		starts.add(75);
        		starts.add(77);
        		starts.add(79);
        		starts.add(81);
        		starts.add(83);
        		starts.add(85);
        		starts.add(87);
        		starts.add(89);
        		starts.add(91);
        		starts.add(93);
        		starts.add(95);
        		starts.add(97);
        		starts.add(99);       		
        		starts.add(101);
        		starts.add(104);
        		starts.add(106);
        		starts.add(108);
        		starts.add(110);
        		starts.add(112);
        		starts.add(114);
        		starts.add(116);
        		starts.add(118);
        		starts.add(120);       		
        		starts.add(122);
        		starts.add(124);
        		starts.add(126);
        		starts.add(128);
        		starts.add(130);
        		starts.add(132);
        		starts.add(134);
        		starts.add(139);
        		starts.add(141);
        		starts.add(143);
        		starts.add(145);       		
        		starts.add(147);
        		starts.add(149);
        		starts.add(151);
        		starts.add(153);
        		starts.add(155);
        		starts.add(157);
        		starts.add(159);
        		starts.add(161);
        		starts.add(163);
        		starts.add(165);
        		starts.add(167);
        		starts.add(169);
        		starts.add(172);
        		starts.add(174);
        		starts.add(176);
        		starts.add(178);
        		starts.add(180);
        		starts.add(182);
        		starts.add(184);
        		starts.add(186);       		
        		starts.add(188);
        		starts.add(190);
        		starts.add(192);
        		starts.add(194);
        		starts.add(196);
        		starts.add(198);
        		starts.add(200);
        		starts.add(202);
        		starts.add(207);
        		starts.add(209);       		
        		starts.add(211);
        		starts.add(213);
        		starts.add(215);
        		starts.add(217);
        		starts.add(219);
        		starts.add(221);
        		starts.add(223);
        		starts.add(225);
        		starts.add(227);
        		starts.add(229);       		
        		starts.add(231);
        		starts.add(233);
        		starts.add(235);
        		starts.add(237);
        		starts.add(252);
        		starts.add(254);
        		starts.add(256);
        		starts.add(258);
        		
        		//places all obstacle locations
        		obstacles.add(376);
        		obstacles.add(377);
        		obstacles.add(406);
        		obstacles.add(407);
        		obstacles.add(410);
        		obstacles.add(411);
        		obstacles.add(412);
        		obstacles.add(413);
        		obstacles.add(438);
        		obstacles.add(439);
        		obstacles.add(440);
        		obstacles.add(441);
        		obstacles.add(444);
        		obstacles.add(445);
        		obstacles.add(446);
        		obstacles.add(447);
        		obstacles.add(448);
        		obstacles.add(449);
        		obstacles.add(470);
        		obstacles.add(471);
        		obstacles.add(472);
        		obstacles.add(473);
        		obstacles.add(474);
        		obstacles.add(475);
        		obstacles.add(478);
        		obstacles.add(479);
        		obstacles.add(480);
        		obstacles.add(481);
        		obstacles.add(482);
        		obstacles.add(483);
        		obstacles.add(484);
        		obstacles.add(485);
        		obstacles.add(502);
        		obstacles.add(503);
        		obstacles.add(504);
        		obstacles.add(505);
        		obstacles.add(506);
        		obstacles.add(507);
        		obstacles.add(508);
        		obstacles.add(509);
        		obstacles.add(512);
        		obstacles.add(513);
        		obstacles.add(514);
        		obstacles.add(515);
        		obstacles.add(516);
        		obstacles.add(517);
        		obstacles.add(518);
        		obstacles.add(519);
        		obstacles.add(520);
        		obstacles.add(521);
        		obstacles.add(534);
        		obstacles.add(535);
        		obstacles.add(536);
        		obstacles.add(537);
        		obstacles.add(538);
        		obstacles.add(539);
        		obstacles.add(540);
        		obstacles.add(541);
        		obstacles.add(542);
        		obstacles.add(543);
        		obstacles.add(546);
        		obstacles.add(547);
        		obstacles.add(548);
        		obstacles.add(549);
        		obstacles.add(550);
        		obstacles.add(551);
        		obstacles.add(552);
        		obstacles.add(553);
        		obstacles.add(554);
        		obstacles.add(555);
        		obstacles.add(556);
        		obstacles.add(557);
        		obstacles.add(566);
        		obstacles.add(567);
        		obstacles.add(568);
        		obstacles.add(569);
        		obstacles.add(570);
        		obstacles.add(571);
        		obstacles.add(572);
        		obstacles.add(573);
        		obstacles.add(574);
        		obstacles.add(575);
        		obstacles.add(576);
        		obstacles.add(577);
        		obstacles.add(580);
        		obstacles.add(581);
        		obstacles.add(582);
        		obstacles.add(583);
        		obstacles.add(584);
        		obstacles.add(585);
        		obstacles.add(586);
        		obstacles.add(587);
        		obstacles.add(588);
        		obstacles.add(589);
        		obstacles.add(590);
        		obstacles.add(591);
        		obstacles.add(592);
        		obstacles.add(593);
        		obstacles.add(598);
        		obstacles.add(599);
        		obstacles.add(600);
        		obstacles.add(601);
        		obstacles.add(602);
        		obstacles.add(603);
        		obstacles.add(604);
        		obstacles.add(605);
        		obstacles.add(606);
        		obstacles.add(607);
        		obstacles.add(608);
        		obstacles.add(609);
        		obstacles.add(610);
        		obstacles.add(611);
        		obstacles.add(614);
        		obstacles.add(615);
        		obstacles.add(616);
        		obstacles.add(617);
        		obstacles.add(618);
        		obstacles.add(619);
        		obstacles.add(620);
        		obstacles.add(621);
        		obstacles.add(622);
        		obstacles.add(623);
        		obstacles.add(624);
        		obstacles.add(625);
        		obstacles.add(626);
        		obstacles.add(627);
        		obstacles.add(628);
        		obstacles.add(631);
        		obstacles.add(632);
        		obstacles.add(633);
        		obstacles.add(634);
        		obstacles.add(635);
        		obstacles.add(636);
        		obstacles.add(637);
        		obstacles.add(638);
        		obstacles.add(639);
        		obstacles.add(640);
        		obstacles.add(641);
        		obstacles.add(642);
        		obstacles.add(643);
        		obstacles.add(644);
        		obstacles.add(645);
        		
        		//most ending locations are added in a simple loop, these four don't fit the loop pattern so they are added manually
        		ends.add(1002);
        		ends.add(1003);
        		ends.add(1004);
        		ends.add(1005);
        		int i = 0;
        		//loop that adds all ending locations to a lsit
        		while(ends.size()<starts.size()){
        			if((1022+i) % 34 >1){
        				ends.add(1022+i);
        			}
        			i++;
        		}
        		//tracks starting locations that have already been used
        		ArrayList<Integer> random = new ArrayList<Integer>();
        		//shuffled starting locations
        		ArrayList<Integer> newStarts = new ArrayList<Integer>();
        		//shuffled ending locations
        		ArrayList<Integer> newEnds = new ArrayList<Integer>();
        		
        		//for each unit
        		for(int a = 0; a<numUnits;a++){
        			//pick a random starting location from the list
        			int genNum = gen.nextInt(starts.size());
        			//keep getting new random starting locations until you get one that isn't already in use
        			while(random.contains(genNum)){
        				genNum=gen.nextInt(starts.size());
        			}
        			//add that location to the random list
        			random.add(genNum);
        			//add the starting location to the newstarts list
        			newStarts.add(starts.get(genNum));
        			//add the ending location the newends list
        			newEnds.add(ends.get(genNum));
        		}
        		//reassign starts and ends lists
        		starts=newStarts;
        		ends=newEnds;
        		//for each unit
        		for(int j = 0;j<numUnits;j++){
        			//use the starts array to place a starting point on the grid
        			node[starts.get(j)].setAsStartingPoint();
        			//increase number of units placed
        			startCount++;
        			//set the invisible label's text to reflect placed unit number
    				startingPlaced.setText(Integer.toString(startCount));
    				//set text for the label corresponding to the unit's starting place
    				label[starts.get(j)/34 +1][starts.get(j)%34].setText(""+startCount);
    				//set unit starting place as green
    				label[starts.get(j)/34 +1][starts.get(j)%34].setBackground(Color.GREEN);
    				//add the unit to the unit lists for both algorithms
    				lrunit.add(new LRUnit(node[starts.get(j)]));
    				hcaunit.add(new HCAUnit(res.getResNode().get(starts.get(j)+1156)));
    				//increase total number of units
    				unitCount++;
    				//increase total number of ending points
    				endCount++;
    				//set the text of the invisible label that tracks number of destinations placed
					endingPlaced.setText(Integer.toString(endCount));
					//sets the text of the destination label to reflect the corresponding unit number
					label[ends.get(j)/34 +1][ends.get(j)%34].setText(""+endCount);
					//sets the label as red
					label[ends.get(j)/34 +1][ends.get(j)%34].setBackground(Color.RED);
					//sets the node as an ending point in the grid
					node[ends.get(j)].setAsEndingPoint(endCount);
					//sets the node as an ending point on the reservation table
					res.getResNode().get(ends.get(j)+1156).setAsEndingPoint(endCount);
					//set the ending points for the units for both algorithms
					lrunit.get(endCount-1).setEndingPoint(node[ends.get(j)]);
					hcaunit.get(endCount-1).setEndingPoint(res.getResNode().get(ends.get(j)+1156));
        		}
        		//place all obstacles
        		for(int j=0;j<obstacles.size();j++){
        			createObstacle2(obstacles.get(j)/34+1,obstacles.get(j)%34);
        		}
        		//set scenario value equal to bottleneck
        		metricScenario=1;
        	}
        });
        
        //if the user clicks the clear grid button
        clearButton.addActionListener(new ActionListener(){
        	public void actionPerformed(ActionEvent g){
        		//clear visual and data representation of the grid and res table
        		clearGrid();
        		//erase metric gathering arrays
        		clearMetricArrays();
        	}
        });
        
        //if the user clicks the next move button during pathfinding
        nextButton.addActionListener(new ActionListener(){
        	public void actionPerformed(ActionEvent f){
        		//if local repair is selected as pathfinding algorithm
        		String pathfindingType = (String)pathfindingComboBox.getSelectedItem();
                if(pathfindingType.equals("Local Repair A*")){
                	//get LR object
	        		lr=getLR();
	        		//if there are still units that arent done with their paths
	        		if(lr.getUnitsDone() < lr.getUnit().size()){
	        			//set the unit number to 0
		        		lr.setUnitNumber(0);
		    			//for each unit
		    			for(int i = 0;i<lr.getUnit().size();i++){
		    				for(int j = 1; j < 1157; j++){
	                			node[j].resetFGH();
	                		}
		    				//set current node to white
		    				setLabelWhite(lr.getUnit().get(i).getX(),lr.getUnit().get(i).getY());
		    				//if the current node isn't an ender
		    				if(!(node[lr.getUnit().get(i).getX()+lr.getUnit().get(i).getY()*34-34].isEnder())){
		    					//erase all text from the label
		    					label[lr.getUnit().get(i).getY()][lr.getUnit().get(i).getX()].setText("");
		    				}
		    				//execute the next move for the unit and get the number of the unit's new node
		    				int square = lr.executeNextMove(i);
		    				//if the unit isn't done with its path
		    				if(!lr.getUnit().get(i).isDone()){
		    					//add the new node to its actual path
		    					lr.getUnit().get(i).addToActualPath(square);
		    				}
		    				//if the unit is done with its path
		    				else{
		    					//if this is the first turn where the unit has been done
		    					if(lr.getUnit().get(i).getActualPath().get(lr.getUnit().get(i).getActualPath().size()-1)!=square){
		    						//add the final node to the actual path
		    						lr.getUnit().get(i).addToActualPath(square);
		    					}
		    				}
		    				//the x and y coordinates of the unit's new node
		    				int tempX,tempY;
		    				tempX=square%34;
		    				tempY=square/34 + 1;
		    				//if the new node is the unit's destination and no other unit is on that node
		    				if(lr.getUnit().get(i).isDone() && node[square].getStatus()!=Status.occupied){
		    					//make the label cyan in color
		    					label[lr.getUnit().get(i).getY()][lr.getUnit().get(i).getX()].setBackground(Color.CYAN);
		    				}
		    				//if the new node is another unit's end node and its already there,
		    				if(node[square].isEnded() && node[square].getStatus()==Status.occupied){
		    					//make the label green
		    					label[lr.getUnit().get(i).getY()][lr.getUnit().get(i).getX()].setBackground(Color.GREEN);
		    					//record that this unit occupies this node
		    					int unitNum = node[square].getOccupier()+1;
		    					//set the text of the label equal to the unit number
		    					label[lr.getUnit().get(i).getY()][lr.getUnit().get(i).getX()].setText(""+unitNum);
		    				}
		    				//otherwise just make the label text read the unit's number
		    				else{
		    					label[tempY][tempX].setText(""+(i+1));
		    				}
		    				//if the current unit isn't done with its path, make its label green
		    				if(!(lr.getUnit().get(i).isDone())){
		    					setLabelGreen(tempX,tempY);
		    				}
		    			}
	        		}
	        		//if all units are done with their paths, create an output file
	        		else{
	        			 createOutputFileLocalRepair();
	        		}
                }
                //if the pathfinding algorithm is HCA*
                else if(pathfindingType.equals("Hierarchical Cooperative A*")){
                	//get the hca object
                	hca=getHCA();
                	//increase the counter for how many turns hca has run
                	hcaMoveNum++;
                	//if there are still units who are not done with their paths
            		if(hca.getUnitsDone() < hca.getUnit().size()){
	            		//set the unit number to 0	
	            		hca.setUnitNumber(0);
	        			//for each unit
	        			for(int i = 0;i<hca.getUnit().size();i++){
	        				//execute the next move for the current unit
	        				hca.executeNextMove(i);
	        				//if the unit is now done
	        				if(hca.getUnit().get(i).isDone()){
	        					//make its background cyan and put the unit number as the label text
	    						label[hca.getUnit().get(i).getY()][hca.getUnit().get(i).getX()].setBackground(Color.CYAN);
	    						label[hca.getUnit().get(i).getY()][hca.getUnit().get(i).getX()].setText(""+(i+1));
	    					}
	        			}
	        			//for each visual node on the grid
	        			for(int j = 1; j<1156;j++){
	        				//the layer of the reservation table that will be displayed
	    					int thisLayerSquare = j+1156*hcaMoveNum;
	    					//if this is an ender node that hasn't had its unit reach it yet, make it red with the unit number as the text
	    					if(res.getResNode().get(thisLayerSquare%1156+1156).getStatus()==ResNode.Status.end && label[res.getResNode().get(thisLayerSquare).getY()][res.getResNode().get(thisLayerSquare).getX()].getBackground()!=Color.CYAN){
	    						label[res.getResNode().get(thisLayerSquare).getY()][res.getResNode().get(thisLayerSquare).getX()].setBackground(Color.RED);
	    						label[res.getResNode().get(thisLayerSquare).getY()][res.getResNode().get(thisLayerSquare).getX()].setText(""+res.getResNode().get(thisLayerSquare%1156+1156).getEndNode());
	    					}
	    					//if the node is occupied, make it green with the unit number as the text
	    					if(res.getResNode().get(thisLayerSquare).getStatus()==ResNode.Status.occupied){
	    						label[res.getResNode().get(thisLayerSquare).getY()][res.getResNode().get(thisLayerSquare).getX()].setBackground(Color.GREEN);
	    						label[res.getResNode().get(thisLayerSquare).getY()][res.getResNode().get(thisLayerSquare).getX()].setText(""+(res.getResNode().get(thisLayerSquare).getOccupier()+1));
	    					}
	    					//if the node is empty, erase all text and make it white
	    					if(!(res.getResNode().get(thisLayerSquare%1156+1156).getStatus()==ResNode.Status.end) && res.getResNode().get(thisLayerSquare).getStatus()==ResNode.Status.empty && label[res.getResNode().get(thisLayerSquare).getY()][res.getResNode().get(thisLayerSquare).getX()].getBackground()!=Color.CYAN){
	    						label[res.getResNode().get(thisLayerSquare).getY()][res.getResNode().get(thisLayerSquare).getX()].setBackground(Color.WHITE);
	    						label[res.getResNode().get(thisLayerSquare).getY()][res.getResNode().get(thisLayerSquare).getX()].setText("");
	    					}
	    				}
            		}
            		//if all units are done with their paths, create an output file
            		else{
            			 createOutputFileHCA();
            		}
                }
        	}
        });
        
        //if the user clicks the "find paths" button
        applyButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
            	//if the number of starting points and ending points match up
                if(unitCount==endCount){
                	//disallow placement of objects on grid
	                started=true;
	                //if pathfinding type is local repair A*
	                String pathfindingType = (String)pathfindingComboBox.getSelectedItem();
	                if(pathfindingType.equals("Local Repair A*")){
	                	//create a local repair object
	                	lr = new LocalRepair(lrunit,node);
	                	//reset variable that tracks which unit cant find a path
	                	int noPathNum=0;
	                	//returns true if a unit is the first unit that was unable to find a path
	                	boolean firstNoPath=false;
	                	//for each unit
	                	for(int i = 0;i<lr.getUnit().size();i++){
	                		//reset nodes
	                		for(int j = 1; j < 1157; j++){
	                			node[j].resetFGH();
	                		}
	                		//calculate a path for the unit
	            			lr.getUnit().get(i).setPath(lr.calculateUnitPath(i));
	            			//if a path was found
	            			if(!lr.getTargNotFound()){
	            				//set the optimal length value equal to this path's length
	            				lr.getUnit().get(i).setOptimalLength(lr.getUnit().get(i).getPath());
	            				//remove starting node from the path
	            				lr.getUnit().get(i).getPath().remove(0);
	            			}
	            			//if a unit could not find a path and this is the first time that this has occurred
	            			if(lr.getTargNotFound() && firstNoPath==false){
	            				//record the unit number
	            				noPathNum=i+1;
	            				//tell user that a unit has no path
	        					JOptionPane.showMessageDialog(controls,"Unit " + (noPathNum) +" has no possible path to its corresponding destination.  Try a different grid layout.");
	        					//reallow placement of objects on grid
	        					started=false;
	        					//this prevents multiple message dialogs telling user that each unit couldn't find a path
	            				firstNoPath=true;
	            			}
	            			//lr object tracks unit number
	            			lr.nextUnit();	
	            		}
	                	//if a unit couldn't find a path do nothing
	                	if(lr.getTargNotFound()){}
	                	//otherwise, replace the setup buttons with the pathfinding buttons
	                	else{
	                		controls.setVisible(false);
	                		controls2.setVisible(true);
	                	}
	                }
	                //if the pathfinding algorithm is HCA*
	                else if(pathfindingType.equals("Hierarchical Cooperative A*")){
	                	//create a local repair object (used for finding optimal length)
	                	lr = new LocalRepair(lrunit,node);
	                	//tracks units that cant find a path
	                	int noPathNum=0;
	                	//denotes whether or not a unit is the first unit to be unable to find a path
	                	boolean firstNoPath=false;
	                	//for each unit
	                	for(int i = 0;i<lr.getUnit().size();i++){
	                		//reset nodes
	                		for(int j = 1; j < 1157; j++){
	                			node[j].resetFGH();
	                		}
	                		//calculate unit path
	            			lr.getUnit().get(i).setPath(lr.calculateUnitPath(i));
	            			//if a path was found
	            			if(!lr.getTargNotFound()){
	            				//set the optimal length for the unit
	            				lr.getUnit().get(i).setOptimalLength(lr.getUnit().get(i).getPath());
	            				//remove starting node from unit path
	            				lr.getUnit().get(i).getPath().remove(0);
	            			}
	            			//if this is the first unit that was unable to find a path
	            			if(lr.getTargNotFound() && firstNoPath==false){
	            				//record unit number
	            				noPathNum=i+1;
	            				//inform user of the error
	        					JOptionPane.showMessageDialog(controls,"Unit " + (noPathNum) +" has no possible path to its corresponding destination.  Try a different grid layout.");
	        					//reallow object placement on grid
	        					started=false;
	        					//prevent multiple message dialogs from printing for multiple units
	            				firstNoPath=true;            				
	            			}
	            			//lr object tracks unit numbers
	            			lr.nextUnit();
	            		}
	                	//create a hca object for pathfinding
	                	hca = new HCA(hcaunit,res,node);
	                	//list of units that must find paths first in order to not be overrun by other units
	                	ArrayList<Integer> mustGoFirst = new ArrayList<Integer>();
	                	//number of units that couldn't find paths because they were overrun
	        	    	int notFounds = 0;
	                	//execute this code until all units were able to find paths
	                	do{
	                		
	                		//resets the reservation table to its original state before pathfinding began
	        	    		hca.getResTable().setToOriginals();
	        	    		//for each unit, set the units to their original positions as well
	        	    		for(int i = 0;i<hca.getUnit().size();i++){
	        	    			hca.getUnit().get(i).setToOriginals();
	        	    		}
	        	    		//for each unit that must find paths first
	        	    		for(int i = 0;i<mustGoFirst.size();i++){
	        	    			//reset nodes
		                		for(int j = 1; j < 1157; j++){
		                			node[j].resetFGH();
		                		}
	        		    		//calculate unit path
	        					hca.getUnit().get(mustGoFirst.get(i)).setPath(hca.calculateUnitPath(mustGoFirst.get(i)));
	        					//if the unit found a path, remove the starting node from its path
	        					if(!hca.getTargNotFound()){
	        						hca.getUnit().get(mustGoFirst.get(i)).getPath().remove(0);
	        					}
	        					//hca tracks unit number
	        					hca.nextUnit();
	        				}
	        	    		//resets the number of units that couldn't find paths
	        	    		notFounds=0;
	        	    		//for each unit
		        	    	for(int i = 0;i<hca.getUnit().size();i++){
		        	    		//reset nodes
		                		for(int j = 1; j < 1157; j++){
		                			node[j].resetFGH();
		                		}
		        	    		//if this unit wasn't one of the units on the must go first list
		        	    		if(hca.getUnit().get(i).getPath().size()==0){
		        	    			//calculate unit path
			        				hca.getUnit().get(i).setPath(hca.calculateUnitPath(i));
			        				//if the unit found a path
			        				if(!hca.getTargNotFound()){
			        					//remove starting node from unit path
			        					hca.getUnit().get(i).getPath().remove(0);
			        				}
			        				//if the unit was overrun by other units
			        				else{
			        					//add the unit to the must go first list
			        					mustGoFirst.add(i);
			        					//denote that the target wasn't found
			        					hca.setTargNotFound(false);
			        					//increase number of units that couldn't find paths
			        					notFounds++;
			        				}
			        				//hca tracks unit number
			        				hca.nextUnit();
		        	    		}
		        			}
	        	    	}while(notFounds>0);
	                	//if a unit couldn't find a path, do nothing
	                	if(lr.getTargNotFound()){}
	                	//otherwise, remove setup buttons and show pathfinding buttons
	                	else{
	                		controls.setVisible(false);
	                		controls2.setVisible(true);
	                	}
	                }
                }
                //if you haven't placed matching ending points for starting points
                else{
    				JOptionPane.showMessageDialog(controls,"You have not placed ending points for all of your units.");
                }
            }
        });
        //put the grid on the top part of the window
        pane.add(compsToExperiment, BorderLayout.NORTH);
        //put a separator betweenthe grid and buttons in the center of the window
        pane.add(new JSeparator(), BorderLayout.CENTER);
        //put the controls on the bottom of the window
        pane.add(controls, BorderLayout.SOUTH);
        pane.add(controls2);
    }
    
    /**
     * Creates the output file.
     */
    protected void createOutputFileLocalRepair() {
    	try{
    	    // Create file 
    	    FileWriter fstream = new FileWriter("localRepair.txt");
    	    BufferedWriter out = new BufferedWriter(fstream);
    	    //for each unit
    	    for(int i=0; i < lrunit.size();i++){
    	    	//append total unit number, unit number, unit number, total path cost, optimal path cost, cycles
    	    	out.append(lrunit.size() + "\t"+(i+1)+"\t" + (i+1) + "\t" +(lrunit.get(i).getTotalPathLength())+ "\t" + lrunit.get(i).getOptimalLength()+"\t"+lrunit.get(i).getCycles()+"\n");
			}
    	    //close file
    	    out.close();
	    }catch (Exception e){
	    	//Catch exception if any
	    	System.err.println("Error: " + e.getMessage());
	    }
	}
    
    /**
     * Creates the output file hca.
     */
    protected void createOutputFileHCA() {

    	try{
    	    // Create file 
    	    FileWriter fstream = new FileWriter("hca.txt");
    	    BufferedWriter out = new BufferedWriter(fstream);
    	    //for each unit
    	    for(int i=0; i < hcaunit.size();i++){
    	    	//append total unit number, unit number, unit number, total path cost, optimal path cost, cycles
    	    	out.append(lrunit.size()+ "\t"+ (i+1) + "\t" + (i+1) + "\t" +(hcaunit.get(i).getTotalPathLength())+ "\t" + lrunit.get(i).getOptimalLength() + "\t"+hcaunit.get(i).getCycles()+"\n");
    	    }
    	    //close file
    	    out.close();
	    }catch (Exception e){
	    	//Catch exception if any
	    	System.err.println("Error: " + e.getMessage());
	    }
	}

	/**
	 * Create the GUI and show it.  For thread safety,
	 * this method is invoked from the
	 * event dispatch thread.
	 */
    private static void createAndShowGUI() {
        //Create and set up the window.
        GridLayoutDemo frame = new GridLayoutDemo("Pathfinder");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //Set up the content pane.
        frame.addComponentsToPane(frame.getContentPane());
        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }
    
    /**
     * Sets the label white.
     * 
     * @param x the x coord
     * @param y the y coord
     */
    public void setLabelWhite(int x, int y){
    	//if the node was an ender and has ended, make it cyan and put the unit number there as the text value
    	if(node[x+y*34-34].isEnder()){
    		if(node[x+y*34-34].isEnded()){
        		label[y][x].setBackground(Color.CYAN);
        		label[y][x].setText(""+(node[x+y*34-34].getEndNode()));

        	}
    		//if the node is an ender and the unit hasn't ended yet, make it red with the unit number as the text value
    		else{
    			label[y][x].setBackground(Color.RED);
    			label[y][x].setText(""+(node[x+y*34-34].getEndNode()));
    		}
    	}
    	//if the node isn't an ender, make it white
    	else{
    		label[y][x].setBackground(Color.WHITE);
    	}
    }
    
    /**
     * Sets the label green.
     * 
     * @param x the x coord
     * @param y the y coord
     */
    public void setLabelGreen(int x, int y){
    	label[y][x].setBackground(Color.GREEN);   	
    }
    
    /**
     * Clear grid.
     */
    public void clearGrid(){
    	//make all nodes on the grid empty
    	for(int i=1;i<1157;i++){
			node[i].setAsEmpty();
		}
    	//clear the reservation table
    	res.getResNode().clear();
    	//construct a new reservation table
    	res=new ResTable();
    	//for all labels that aren't on the edge
		for(int i=2;i<34;i++){
			for(int j=2;j<34;j++){
				//make the labels white with no text
				label[i][j].setBackground(Color.WHITE);
				label[i][j].setText("");
			}
		}
		//for all edge labels and nodes
		for(int i=1;i<=34;i++){
        	for(int j=1;j<=34;j++){
        		node[i+(j*34-34)]= new Node(i,j);
        		//make them obstacles
        		if(i==1 || j==1 || i ==34 || j==34){
            		createObstacle(i,j);
            	}
        	}
        }
		//clear unit lists
		lrunit.clear();
		hcaunit.clear();
		//reset total hca moves
		hcaMoveNum=1;
		//reset number of units placed
		startingPlaced.setText(""+0);
		//reset numebr of destinations placed
		endingPlaced.setText(""+0);
		//reset all literal placement counters
		endCount=0;
		unitCount=0;
		startCount=0;
    }
    
    /**
     * Clear grid when there is no GUI.
     * Used by tests and metric gathering
     */
    public void clearGridNoGUI(){
    	//make all nodes on the grid empty
    	for(int i=1;i<1157;i++){
			node[i].setAsEmpty();
		}
    	//clear the reservation table
    	res.getResNode().clear();
    	//construct a new reservation table
    	res=new ResTable();
		//for all edge labels and nodes
		for(int i=1;i<=34;i++){
        	for(int j=1;j<=34;j++){
        		node[i+(j*34-34)]= new Node(i,j);
        		//make them obstacles
        		if(i==1 || j==1 || i ==34 || j==34){
            		obstNoGUI(i,j);
            	}
        	}
        }
		//clear unit lists
		lrunit.clear();
		hcaunit.clear();
		//reset total hca moves
		hcaMoveNum=1;
		//reset all literal placement counters
		endCount=0;
		unitCount=0;
		startCount=0;
    }
    
    /**
     * Clear metric arrays.
     * Clears all data that would be used to gather metrics.
     */
    public void clearMetricArrays(){
    	metricNode.clear();
    	metricHCA.clear();
    	metricLR.clear();
    	metricRes.clear();
    	metricNodeA.clear();
    	metricLRA.clear();
    	metricScenario=0;
    	metricUnitNum=0;
    }
    
    /**
     * Gets the randomized bottleneck data sets.
     * 
     * @param numOfUnits the number of units
     */
    public void getRandomizedBottleneck(int numOfUnits){
    	//clear grid
    	clearGridNoGUI();
    	//number of units to be placed
		int numUnits;
		numUnits=numOfUnits;
		//reservation table
		restest = new ResTable();
		//hca unit list
		hcaunittest = new ArrayList<HCAUnit>();
		//lr unit list
		lrunittest = new ArrayList<LRUnit>();
		//duplicate lr unit list
		ArrayList<LRUnit> lrunittesta = new ArrayList<LRUnit>();
		//starting locatons
		ArrayList<Integer> starts = new ArrayList<Integer>();
		//ending locations
		ArrayList<Integer> ends = new ArrayList<Integer>();
		//obstacle locations
		ArrayList<Integer> obstacles = new ArrayList<Integer>();
		
		//all possible starting locations
		starts.add(36);
		starts.add(38);
		starts.add(40);
		starts.add(42);
		starts.add(44);
		starts.add(46);
		starts.add(48);
		starts.add(50);
		starts.add(52);
		starts.add(54);		
		starts.add(56);
		starts.add(58);
		starts.add(60);
		starts.add(62);
		starts.add(64);
		starts.add(66);
		starts.add(71);
		starts.add(73);
		starts.add(75);
		starts.add(77);
		starts.add(79);
		starts.add(81);
		starts.add(83);
		starts.add(85);
		starts.add(87);
		starts.add(89);
		starts.add(91);
		starts.add(93);
		starts.add(95);
		starts.add(97);
		starts.add(99);		
		starts.add(101);
		starts.add(104);
		starts.add(106);
		starts.add(108);
		starts.add(110);
		starts.add(112);
		starts.add(114);
		starts.add(116);
		starts.add(118);
		starts.add(120);		
		starts.add(122);
		starts.add(124);
		starts.add(126);
		starts.add(128);
		starts.add(130);
		starts.add(132);
		starts.add(134);
		starts.add(139);
		starts.add(141);
		starts.add(143);
		starts.add(145);		
		starts.add(147);
		starts.add(149);
		starts.add(151);
		starts.add(153);
		starts.add(155);
		starts.add(157);
		starts.add(159);
		starts.add(161);
		starts.add(163);
		starts.add(165);		
		starts.add(167);
		starts.add(169);
		starts.add(172);
		starts.add(174);
		starts.add(176);
		starts.add(178);
		starts.add(180);
		starts.add(182);
		starts.add(184);
		starts.add(186);		
		starts.add(188);
		starts.add(190);
		starts.add(192);
		starts.add(194);
		starts.add(196);
		starts.add(198);
		starts.add(200);
		starts.add(202);
		starts.add(207);
		starts.add(209);		
		starts.add(211);
		starts.add(213);
		starts.add(215);
		starts.add(217);
		starts.add(219);
		starts.add(221);
		starts.add(223);
		starts.add(225);
		starts.add(227);
		starts.add(229);		
		starts.add(231);
		starts.add(233);
		starts.add(235);
		starts.add(237);
		starts.add(252);
		starts.add(254);
		starts.add(256);
		starts.add(258);
		
		//add all obstacle locations
		obstacles.add(376);
		obstacles.add(377);
		obstacles.add(406);
		obstacles.add(407);
		obstacles.add(410);
		obstacles.add(411);
		obstacles.add(412);
		obstacles.add(413);
		obstacles.add(438);
		obstacles.add(439);
		obstacles.add(440);
		obstacles.add(441);
		obstacles.add(444);
		obstacles.add(445);
		obstacles.add(446);
		obstacles.add(447);
		obstacles.add(448);
		obstacles.add(449);
		obstacles.add(470);
		obstacles.add(471);
		obstacles.add(472);
		obstacles.add(473);
		obstacles.add(474);
		obstacles.add(475);
		obstacles.add(478);
		obstacles.add(479);
		obstacles.add(480);
		obstacles.add(481);
		obstacles.add(482);
		obstacles.add(483);
		obstacles.add(484);
		obstacles.add(485);
		obstacles.add(502);
		obstacles.add(503);
		obstacles.add(504);
		obstacles.add(505);
		obstacles.add(506);
		obstacles.add(507);
		obstacles.add(508);
		obstacles.add(509);
		obstacles.add(512);
		obstacles.add(513);
		obstacles.add(514);
		obstacles.add(515);
		obstacles.add(516);
		obstacles.add(517);
		obstacles.add(518);
		obstacles.add(519);
		obstacles.add(520);
		obstacles.add(521);
		obstacles.add(534);
		obstacles.add(535);
		obstacles.add(536);
		obstacles.add(537);
		obstacles.add(538);
		obstacles.add(539);
		obstacles.add(540);
		obstacles.add(541);
		obstacles.add(542);
		obstacles.add(543);
		obstacles.add(546);
		obstacles.add(547);
		obstacles.add(548);
		obstacles.add(549);
		obstacles.add(550);
		obstacles.add(551);
		obstacles.add(552);
		obstacles.add(553);
		obstacles.add(554);
		obstacles.add(555);
		obstacles.add(556);
		obstacles.add(557);
		obstacles.add(566);
		obstacles.add(567);
		obstacles.add(568);
		obstacles.add(569);
		obstacles.add(570);
		obstacles.add(571);
		obstacles.add(572);
		obstacles.add(573);
		obstacles.add(574);
		obstacles.add(575);
		obstacles.add(576);
		obstacles.add(577);
		obstacles.add(580);
		obstacles.add(581);
		obstacles.add(582);
		obstacles.add(583);
		obstacles.add(584);
		obstacles.add(585);
		obstacles.add(586);
		obstacles.add(587);
		obstacles.add(588);
		obstacles.add(589);
		obstacles.add(590);
		obstacles.add(591);
		obstacles.add(592);
		obstacles.add(593);
		obstacles.add(598);
		obstacles.add(599);
		obstacles.add(600);
		obstacles.add(601);
		obstacles.add(602);
		obstacles.add(603);
		obstacles.add(604);
		obstacles.add(605);
		obstacles.add(606);
		obstacles.add(607);
		obstacles.add(608);
		obstacles.add(609);
		obstacles.add(610);
		obstacles.add(611);
		obstacles.add(614);
		obstacles.add(615);
		obstacles.add(616);
		obstacles.add(617);
		obstacles.add(618);
		obstacles.add(619);
		obstacles.add(620);
		obstacles.add(621);
		obstacles.add(622);
		obstacles.add(623);
		obstacles.add(624);
		obstacles.add(625);
		obstacles.add(626);
		obstacles.add(627);
		obstacles.add(628);
		obstacles.add(631);
		obstacles.add(632);
		obstacles.add(633);
		obstacles.add(634);
		obstacles.add(635);
		obstacles.add(636);
		obstacles.add(637);
		obstacles.add(638);
		obstacles.add(639);
		obstacles.add(640);
		obstacles.add(641);
		obstacles.add(642);
		obstacles.add(643);
		obstacles.add(644);
		obstacles.add(645);
		
		//ending position locations that don't fit the following loop pattern
		ends.add(1002);
		ends.add(1003);
		ends.add(1004);
		ends.add(1005);
		int i = 0;
		//the rest of the ending positions
		while(ends.size()<starts.size()){
			if((1022+i) % 34 >1){
				ends.add(1022+i);
			}
			i++;
		}
		//locations that have been drawn randomly as starting locations
		ArrayList<Integer> random = new ArrayList<Integer>();
		//shuffled starting locations
		ArrayList<Integer> newStarts = new ArrayList<Integer>();
		//shuffled ending locations
		ArrayList<Integer> newEnds = new ArrayList<Integer>();
		
		//for each unit
		for(int a = 0; a<numUnits;a++){
			//pick a random starting location
			int genNum = gen.nextInt(starts.size());
			//until you draw a location that hasn't already been used
			while(random.contains(genNum)){
				genNum=gen.nextInt(starts.size());
			}
			//add the location to the random list
			random.add(genNum);
			//add the location to the new starts list
			newStarts.add(starts.get(genNum));
			//add the corresponding destination to the new ends list
			newEnds.add(ends.get(genNum));
		}
		//sets the start and end lists to be equal to the shuffled ones
		starts=newStarts;
		ends=newEnds;
		//two copies of the grid layout
		Node nodetest[] = new Node[1157];
		Node nodetesta[] = new Node[1157];
		//create a full set of nodes for two copies of the same grid
		for(int a=1;a<=34;a++){
        	for(int j=1;j<=34;j++){
        		nodetest[a+(j*34-34)]= new Node(a,j);
        		nodetesta[a+(j*34-34)]= new Node(a,j);
        	}
        }
		//set edge nodes as obstacles
		for(int a=1;a<=34;a++){
        	for(int j=1;j<=34;j++){
	        	if(a==1 || j==1 || a ==34 || j==34){
	        		nodetest[a*34+j-34].setAsObstacle();
	        		nodetesta[a*34+j-34].setAsObstacle();
	        	}
        	}
		}
		//for each unit
		for(int j = 0;j<numUnits;j++){
			//use the starts array to place a starting point on the grid
			nodetest[starts.get(j)].setAsOccupied(unitCount);
			nodetesta[starts.get(j)].setAsOccupied(unitCount);
			restest.getResNode().get(starts.get(j)+1156).setAsOccupied(unitCount);
			//increase number of units placed
			startCount++;
			//add the unit to the unit lists for both algorithms
			lrunittest.add(new LRUnit(nodetest[starts.get(j)]));
			lrunittesta.add(new LRUnit(nodetesta[starts.get(j)]));
			hcaunittest.add(new HCAUnit(restest.getResNode().get(starts.get(j)+1156)));
			//increase total number of units
			unitCount++;
			//increase total number of ending points
			endCount++;
			//sets the node as an ending point in the grid
			nodetest[ends.get(j)].setAsEndingPoint(endCount);
			nodetesta[ends.get(j)].setAsEndingPoint(endCount);
			//sets the node as an ending point on the reservation table
			restest.getResNode().get(ends.get(j)+1156).setAsEndingPoint(endCount);
			//set the ending points for the units for both algorithms
			lrunittest.get(endCount-1).setEndingPoint(nodetest[ends.get(j)]);
			lrunittesta.get(endCount-1).setEndingPoint(nodetesta[ends.get(j)]);
			hcaunittest.get(endCount-1).setEndingPoint(restest.getResNode().get(ends.get(j)+1156));
		}
		//place all obstacles
		for(int j=0;j<obstacles.size();j++){
			nodetest[obstacles.get(j)].setAsObstacle();
			nodetesta[obstacles.get(j)].setAsObstacle();
			restest.getResNode().get(obstacles.get(j)+1156).setAsObstacle();			
		}
		//add these data sets to the metric arrays
		metricRes.add(restest);
		metricNode.add(nodetest);
		metricNodeA.add(nodetesta);
		metricHCA.add(hcaunittest);
		metricLR.add(lrunittest);
		metricLRA.add(lrunittesta);
    }
    
    /**
     * Gets the randomized chaotic intersection data sets.
     * 
     * @param numOfUnits the number of units
     */
    public void getRandomizedCI(int numOfUnits){
    	//clear the grid
    	clearGridNoGUI();
    	//two copies of the two dimensional grid
    	Node nodetest[] = new Node[1157];
    	Node nodetesta[] = new Node[1157];
    	//reservation table
    	restest = new ResTable();
    	//create a full set of nodes for the 2 dimensional grids
    	for(int a=1;a<=34;a++){
        	for(int j=1;j<=34;j++){
        		nodetest[a+(j*34-34)]= new Node(a,j);
        		nodetesta[a+(j*34-34)]= new Node(a,j);
        	}
        }
    	//set the edges of the grids as obstacles
    	for(int a=1;a<=34;a++){
        	for(int j=1;j<=34;j++){		
	        	if(a==1 || j==1 || a ==34 || j==34){
	        		nodetest[a*34+j-34].setAsObstacle();
	        		nodetesta[a*34+j-34].setAsObstacle();
	        		//also set obstacles on the reservations table
	    	    	restest.getResNode().get(a*34+j-34+1156).setAsObstacle();
	        	}
        	}
		}
    	//number of units to place
		int numUnits;
		numUnits=numOfUnits;
		//list of hca units and lr units
		hcaunittest = new ArrayList<HCAUnit>();
		lrunittest = new ArrayList<LRUnit>();
		//duplicate list of lr units
		ArrayList<LRUnit> lrunittesta = new ArrayList<LRUnit>();
		//starting locations
		ArrayList<Integer> starts = new ArrayList<Integer>();
		//ending locations
		ArrayList<Integer> ends = new ArrayList<Integer>();
		int k=0;
		//places starting locations along top edge
		while(starts.size()<32){
			starts.add(36+k);
			k++;
		}
		//places starting locations along the right edge
		k=3;
		while(starts.size()<62){
			starts.add((34*k)-1);
			k++;
		}
		//places starting locations along the bottom edge
		k=1;
		while(starts.size()<93){
			starts.add(1156-34-k);
			k++;
		}
		//places starting locations along the left edge
		k=0;
		while(starts.size()<124){
			starts.add(1090-34*k);
			k++;
		}
		
		//remove unwanted starting locations from the previous loops
		starts.remove(123);
		starts.remove(121);
		starts.remove(108);
		starts.remove(96);
		starts.remove(94);
		starts.remove(85);
		starts.remove(83);
		starts.remove(81);
		starts.remove(77);
		starts.remove(75);
		starts.remove(73);
		starts.remove(71);
		starts.remove(61);
		starts.remove(59);
		starts.remove(46);
		starts.remove(34);
		starts.remove(32);
		starts.remove(23);
		starts.remove(21);
		starts.remove(19);
		starts.remove(15);
		starts.remove(13);
		starts.remove(11);
		starts.remove(9);
		
		//add corresponding ending locations for the starting locations
		for(int l=0;l<25;l++){
			ends.add(starts.get(l)+(34*30));
		}
		for(int l=25;l<50;l++){
			ends.add(starts.get(l)-30);
		}
		for(int l=50;l<75;l++){
			ends.add(starts.get(l)-(34*30));
		}
		for(int l=75;l<100;l++){
			ends.add(starts.get(l)+30);
		}
		//list of starting locations that have been randomly drawn
		ArrayList<Integer> random = new ArrayList<Integer>();
		//shuffled starting locations
		ArrayList<Integer> newStarts = new ArrayList<Integer>();
		//shuffled ending locations
		ArrayList<Integer> newEnds = new ArrayList<Integer>();
		//for each unit
		for(int a = 0; a<numUnits;a++){
			//pull a random value from the starts list
			int genNum = gen.nextInt(starts.size());
			//until you draw a location that hasn't already been drawn
			while(random.contains(genNum)){
				genNum=gen.nextInt(starts.size());
			}
			//add this location to the list of already drawn locations
			random.add(genNum);
			//add the location to the new starts list
			newStarts.add(starts.get(genNum));
			//add the corresponding destination to the new ends list
			newEnds.add(ends.get(genNum));
		}
		//set the starts and ends lists as the shuffled versions
		starts=newStarts;
		ends=newEnds;
		//for each unit
		for(int j = 0; j< numUnits; j++){
			//use the starts array to place a starting point on the grid
			nodetest[starts.get(j)].setAsOccupied(unitCount);
			nodetesta[starts.get(j)].setAsOccupied(unitCount);
			restest.getResNode().get(starts.get(j)+1156).setAsOccupied(unitCount);
			//increase number of units placed
			startCount++;
			//add the unit to the unit lists for both algorithms
			lrunittest.add(new LRUnit(nodetest[starts.get(j)]));
			lrunittesta.add(new LRUnit(nodetesta[starts.get(j)]));
			hcaunittest.add(new HCAUnit(restest.getResNode().get(starts.get(j)+1156)));
			//increase total number of units
			unitCount++;
			//increase total number of ending points
			endCount++;
			//sets the node as an ending point in the grid
			nodetest[ends.get(j)].setAsEndingPoint(endCount);
			nodetesta[ends.get(j)].setAsEndingPoint(endCount);
			//sets the node as an ending point on the reservation table
			restest.getResNode().get(ends.get(j)+1156).setAsEndingPoint(endCount);
			//set the ending points for the units for both algorithms
			lrunittest.get(endCount-1).setEndingPoint(nodetest[ends.get(j)]);
			lrunittesta.get(endCount-1).setEndingPoint(nodetesta[ends.get(j)]);
			hcaunittest.get(endCount-1).setEndingPoint(restest.getResNode().get(ends.get(j)+1156));
		}
		//add these sets of data to the metric arrays
		metricRes.add(restest);
		metricNode.add(nodetest);
		metricNodeA.add(nodetesta);
		metricHCA.add(hcaunittest);
		metricLR.add(lrunittest);
		metricLRA.add(lrunittesta);
    }
    
    /**
     * Gets the randomized random grid layouts.
     * 
     * @param numOfUnits the number of units
     */
    public void getRandomizedRandom(int numOfUnits){
    	//number of units to place
    	int numUnits;
    	//three copies of the lr unit list
		ArrayList<LRUnit> lrunittesta = new ArrayList<LRUnit>();
		ArrayList<LRUnit> lrunittestb = new ArrayList<LRUnit>();
		ArrayList<LRUnit> lrunittestc = new ArrayList<LRUnit>();
		//one copy of the hca unit list
		ArrayList<HCAUnit> hcaunittesta = new ArrayList<HCAUnit>();
		//reservation table
		ResTable restesta = new ResTable();
		//three copies of the 2 dimensional grid
		Node nodetesta[];
		Node nodetestb[];
		Node nodetestc[];
		//execute this section of code until a grid is produced where all units have paths
		do{
			//clear the grid
			clearGridNoGUI();
			//instantiate the three grids
			nodetesta = new Node[1157];
			nodetestb = new Node[1157];
			nodetestc = new Node[1157];
			//clear the three lists of lr units
			lrunittesta.clear();
			lrunittestb.clear();
			lrunittestc.clear();
			//clear the reservation table
			restesta.getResNode().clear();
			//construct new reservation table
			restesta = new ResTable();
			//constructs new nodes for the three grids until all nodes are in place
			for(int a=1;a<=34;a++){
	        	for(int j=1;j<=34;j++){
	        		nodetesta[a+(j*34-34)]= new Node(a,j);
	        		nodetestb[a+(j*34-34)]= new Node(a,j);
	        		nodetestc[a+(j*34-34)]= new Node(a,j);
	        	}
	        }
			//for all edge nodes
			for(int a=1;a<=34;a++){
	        	for(int j=1;j<=34;j++){
	        		//set nodes as obstacles on grids and reservation table
		        	if(a==1 || j==1 || a ==34 || j==34){
		        		nodetesta[a*34+j-34].setAsObstacle();
		        		nodetestb[a*34+j-34].setAsObstacle();
		        		nodetestc[a*34+j-34].setAsObstacle();
		    	    	restesta.getResNode().get(a*34+j-34+1156).setAsObstacle();
		        	}
	        	}
			}
			//clear the hca unit list
			hcaunittesta.clear();
			//make the unit list new
			hcaunittesta = new ArrayList<HCAUnit>();
			//make the three lr unit lists new
			lrunittesta = new ArrayList<LRUnit>();
			lrunittestb = new ArrayList<LRUnit>();
			lrunittestc = new ArrayList<LRUnit>();
			//set number of units to place
			numUnits=numOfUnits;
			//list of all nodes that can potentially be used for object placement
			ArrayList<Integer> drawPool = new ArrayList<Integer>();
			//obstacle locations
			ArrayList<Integer> obstacles = new ArrayList<Integer>();
			//starting locations
			ArrayList<Integer> starts = new ArrayList<Integer>();
			//ending locations
			ArrayList<Integer> ends = new ArrayList<Integer>();
			//clear all of these lists
			drawPool.clear();
			obstacles.clear();
			starts.clear();
			ends.clear();
			//add all nodes to the draw pool
			for(int x = 2;x<34;x++){
				for(int y = 2;y<34;y++){
					drawPool.add(x+y*34-34);
				}
			}
			//random number generator
			Random gen = new Random();
			//random node that is selected
			int drawIndex=0;
			//30% of nodes on grid must be obstacles
			for(int h =0;h<205;h++){
				//random node number that remains in draw pool
				drawIndex=gen.nextInt(drawPool.size());
				//add obstacle location
				obstacles.add(drawPool.get(drawIndex));
				//remove location from draw pool
				drawPool.remove(drawIndex);
			}
			//for the number of units to be placed
			for(int h=0;h<numUnits;h++){
				//draw a random remaining location
				drawIndex=gen.nextInt(drawPool.size());
				//add a starting point at that location
				starts.add(drawPool.get(drawIndex));
				//remove that location from the draw pool
				drawPool.remove(drawIndex);
			}
			//for the number of units to be placed
			for(int h=0;h<numUnits;h++){
				//draw a random remaining location
				drawIndex=gen.nextInt(drawPool.size());
				//add an ending point at that location
				ends.add(drawPool.get(drawIndex));
				//remove that location from the draw pool
				drawPool.remove(drawIndex);
			}
			//for each unit
			for(int h = 0; h< numUnits; h++){
				//use the starts array to place a starting point on the grid
				nodetesta[starts.get(h)].setAsOccupied(unitCount);
				nodetestb[starts.get(h)].setAsOccupied(unitCount);
				nodetestc[starts.get(h)].setAsOccupied(unitCount);
				restesta.getResNode().get(starts.get(h)+1156).setAsOccupied(unitCount);
				//increase number of units placed
				startCount++;
				//add the unit to the unit lists for both algorithms
				lrunittesta.add(new LRUnit(nodetesta[starts.get(h)]));
				lrunittestb.add(new LRUnit(nodetestb[starts.get(h)]));
				lrunittestc.add(new LRUnit(nodetestc[starts.get(h)]));
				hcaunittesta.add(new HCAUnit(restesta.getResNode().get(starts.get(h)+1156)));
				//increase total number of units
				unitCount++;
				//increase total number of ending points
				endCount++;
				//sets the node as an ending point in the grid
				nodetesta[ends.get(h)].setAsEndingPoint(h+1);
				nodetestb[ends.get(h)].setAsEndingPoint(h+1);
				nodetestc[ends.get(h)].setAsEndingPoint(h+1);
				//sets the node as an ending point on the reservation table
				restesta.getResNode().get(ends.get(h)+1156).setAsEndingPoint(h+1);
				//set the ending points for the units for both algorithms
				lrunittesta.get(endCount-1).setEndingPoint(nodetesta[ends.get(h)]);
				lrunittestb.get(endCount-1).setEndingPoint(nodetestb[ends.get(h)]);
				lrunittestc.get(endCount-1).setEndingPoint(nodetestc[ends.get(h)]);
				hcaunittesta.get(endCount-1).setEndingPoint(restesta.getResNode().get(ends.get(h)+1156));
			}
			//place obstacles on grids, reservation table, labels
			for(int j=0;j<obstacles.size();j++){
				nodetesta[obstacles.get(j)].setAsObstacle();
				nodetestb[obstacles.get(j)].setAsObstacle();
				nodetestc[obstacles.get(j)].setAsObstacle();
				restesta.getResNode().get(obstacles.get(j)+1156).setAsObstacle();
			}
		}while(isNoPath(lrunittestc,nodetestc));
		//add data sets to the metric arrays
		metricRes.add(restesta);
		metricNode.add(nodetesta);
		metricNodeA.add(nodetestb);
		metricHCA.add(hcaunittesta);
		metricLR.add(lrunittesta);
		metricLRA.add(lrunittestb);
		//erase structures for memory purposes
		nodetesta=null;
		nodetestb=null;
		nodetestc=null;
		restesta=null;
		lrunittesta=null;
		lrunittestb=null;
		lrunittestc=null;
		hcaunittesta=null;
    }
    
    /**
     * Produces n array of arrays that contain data for 30 runs of pathfinding for both algorithms.
     * 
     * @return metricArrays An array of arrays that contain data for 30 runs of pathfinding for both algorithms
     */
    
    public ArrayList<Object> getMetricArrays(){
    	ArrayList<Object> metricArrays = new ArrayList<Object>();
    	metricArrays.add(metricRes);
    	metricArrays.add(metricNode);
    	metricArrays.add(metricNodeA);
    	metricArrays.add(metricHCA);
    	metricArrays.add(metricLR);
    	metricArrays.add(metricLRA);
    	return metricArrays;
    }
    
    /**
     * Creates a blank grid and reservation table without producing any visual elements
     * This is strictly used by tests.
     */
    public void createGridAndResTable(){
    	//construct new reservation table
        res = new ResTable();
        //from 1 to 34 horizontally and vertically
        //This creates the grid
        for(int i=1;i<=34;i++){
        	for(int j=1;j<=34;j++){
        		//create a new node
        		node[i+(j*34-34)]= new Node(i,j);
        	}
        }
        for(int i=1;i<=34;i++){
        	for(int j=1;j<=34;j++){
	        	//if the label is a border node on the grid, make that node/label an obstacle
	        	if(i==1 || j==1 || i ==34 || j==34){
	        		obstNoGUI(i,j);
	        	}
        	}
        }
    }
    
    /**
     * The main method.
     * 
     * @param args the arguments
     */
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
        } catch (UnsupportedLookAndFeelException ex) {
            ex.printStackTrace();
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
        } catch (InstantiationException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        /* Turn off metal's use of bold fonts */
        UIManager.put("swing.boldMetal", Boolean.FALSE);
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
}
