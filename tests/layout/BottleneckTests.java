/**
 * Kevin Layfield
 * Dr. Wellington
 * 2009
 * Sr Research - Cooperative Pathfinding Vs. Local Repair Pathfinding
 */
package layout;

import java.util.ArrayList;

import junit.framework.TestCase;

/**
 * The Class BottleneckTests.
 */
public class BottleneckTests extends TestCase {
	//Only local repair is tested because the functionality is the same for both algorithms
	/** The lr. */
	LocalRepair lr;
	//object created by 30 sets of data produced in test
	/** The mg. */
	MetricGatherer mg;
	
	/**
	 * Test number of units placed is correct.
	 */
	@SuppressWarnings("unchecked")
	public void testNumberOfUnitsPlaced(){
		//instance of main class
		GridLayoutDemo demo = new GridLayoutDemo("Test");
		//creates variable that tracks which scenario we are using
		int metricScenario = 1;
		//sets number of units to place to 100
		int metricUnitNum=100;
		//creates a non visual representation of the grid
		demo.createGridAndResTable();
		//sets scenario to bottleneck
		metricScenario = 1;
		//temporary copy of metric scenario value
		int tempMetrScen = metricScenario;
		//if this is a bottleneck scenario
		if(metricScenario==1){
			//create 30 bottleneck sets of data
			for(int i=0;i<30;i++){
				demo.getRandomizedBottleneck(metricUnitNum);
			}
		}
		//retrieves all of the metric arrays
		ArrayList<Object> metricArrays = demo.getMetricArrays();
		//extracts sets of res table data
		ArrayList<ResTable> res = (ArrayList<ResTable>)metricArrays.get(0);
		//retrieves sets of node data
		ArrayList<Node[]> nodes = (ArrayList<Node[]>)metricArrays.get(1);
		//create the metric gatherer with the collected sets of data
		mg = new MetricGatherer((ArrayList<Node[]>)metricArrays.get(1),(ArrayList<ArrayList<LRUnit>>)metricArrays.get(4),(ArrayList<Node[]>)metricArrays.get(2),(ArrayList<ArrayList<LRUnit>>)metricArrays.get(5),(ArrayList<ArrayList<HCAUnit>>)metricArrays.get(3),(ArrayList<ResTable>)metricArrays.get(0));
		//for all 30 sets of data
		for(int a = 0; a <30; a++){
			int unitCount = 0;
			//for each node
			for(int b = 1; b <1157; b++){
				//if the node is occupied
				if(nodes.get(a)[b].getStatus()==Node.Status.occupied){
					//increase count of units
					unitCount++;
				}
				//if the corresponding node on the res table is occupied
				if(res.get(a).getResNode().get(1156+b).getStatus()==ResNode.Status.occupied){
					//increase count of units
					unitCount++;
				}
			}
			//100 units should have been added for both the res table and the grid
			assertEquals(unitCount,200);
		}
		//clear the metric arrays that were just created
		demo.clearMetricArrays();
		//reset the recently erased value of metric scenario
		metricScenario = tempMetrScen;
		
	}
	
	/**
	 * Test number of destination nodes placed is correct.
	 */
	@SuppressWarnings("unchecked")
	public void testNumberOfDestinationNodes(){
		//instance of the main class
		GridLayoutDemo demo = new GridLayoutDemo("Test");
		//variable that tracks which scenario we run
		int metricScenario = 1;
		//number of units to place
		int metricUnitNum=100;
		//creates non visual representation of grid
		demo.createGridAndResTable();
		//sets scenario to bottleneck
		metricScenario = 1;
		//temporary copy of metric scenario value
		int tempMetrScen = metricScenario;
		//if this is a bottleneck scenario
		if(metricScenario==1){
			//create 30 bottleneck sets of data
			for(int i=0;i<30;i++){
				demo.getRandomizedBottleneck(metricUnitNum);
			}
		}
		//retrieve 30 sets of data
		ArrayList<Object> metricArrays = demo.getMetricArrays();
		//extract res table data sets
		ArrayList<ResTable> res = (ArrayList<ResTable>)metricArrays.get(0);
		//extract node data sets
		ArrayList<Node[]> nodes = (ArrayList<Node[]>)metricArrays.get(1);
		//create the metric gatherer with the collected sets of data
		mg = new MetricGatherer((ArrayList<Node[]>)metricArrays.get(1),(ArrayList<ArrayList<LRUnit>>)metricArrays.get(4),(ArrayList<Node[]>)metricArrays.get(2),(ArrayList<ArrayList<LRUnit>>)metricArrays.get(5),(ArrayList<ArrayList<HCAUnit>>)metricArrays.get(3),(ArrayList<ResTable>)metricArrays.get(0));
		//for each set of data
		for(int a = 0; a <30; a++){
			int unitCount = 0;
			//for each node
			for(int b = 1; b <1157; b++){
				//if the node is an ending point, increment variable
				if(nodes.get(a)[b].getStatus()==Node.Status.end){
					unitCount++;
				}
				//if corresponding node on res table is ending point, increment variable
				if(res.get(a).getResNode().get(1156+b).getStatus()==ResNode.Status.end){
					unitCount++;
				}
			}
			//should be 100 ending points for grid and restable
			assertEquals(unitCount,200);
		}
		//clear the metric arrays that were just created
		demo.clearMetricArrays();
		//reset the recently erased value of metric scenario
		metricScenario = tempMetrScen;
	}
	
	/**
	 * Test units are placed in random locations.
	 */
	@SuppressWarnings("unchecked")
	public void testUnitsInRandomLocations(){
		//instance of the main class
		GridLayoutDemo demo = new GridLayoutDemo("Test");
		//variable tracks which scenario we are running
		int metricScenario = 1;
		//number of units to place
		int metricUnitNum=100;
		//creates non visual representation of the grid
		demo.createGridAndResTable();
		//sets scenario to bottleneck
		metricScenario = 1;
		//temporary copy of metric scenario value
		int tempMetrScen = metricScenario;
		//if this is a bottleneck scenario
		if(metricScenario==1){
			//create 30 bottleneck sets of data
			for(int i=0;i<30;i++){
				demo.getRandomizedBottleneck(metricUnitNum);
			}
		}
		//retrieve 30 sets of data
		ArrayList<Object> metricArrays = demo.getMetricArrays();
		//extract lrunit data sets
		ArrayList<ArrayList<LRUnit>> lrunit = (ArrayList<ArrayList<LRUnit>>)metricArrays.get(4);
		//create the metric gatherer with the collected sets of data
		mg = new MetricGatherer((ArrayList<Node[]>)metricArrays.get(1),(ArrayList<ArrayList<LRUnit>>)metricArrays.get(4),(ArrayList<Node[]>)metricArrays.get(2),(ArrayList<ArrayList<LRUnit>>)metricArrays.get(5),(ArrayList<ArrayList<HCAUnit>>)metricArrays.get(3),(ArrayList<ResTable>)metricArrays.get(0));
		//for all 30 sets of data
		for(int a = 0; a <30; a++){
			//array that tracks unit locations
			ArrayList<Integer> unitLocations = new ArrayList<Integer>();
			//for each unit
			for(int b = 0; b <100; b++){
				//adds current unit node number to array
				unitLocations.add(lrunit.get(a).get(b).getX()+lrunit.get(a).get(b).getY()*34-34);
			}
			//this expression would be true if units are placed in sequential spots rather than in random locations
			assertFalse(unitLocations.get(0) == 36 && unitLocations.get(1)== 38 && unitLocations.get(2) == 40 && unitLocations.get(3) == 42);
		}
		//clear the metric arrays that were just created
		demo.clearMetricArrays();
		//reset the recently erased value of metric scenario
		metricScenario = tempMetrScen;
	}
	
	/**
	 * Test units are placed in a random order.
	 */
	@SuppressWarnings("unchecked")
	public void testUnitsInRandomOrder(){
		//create instance of main class
		GridLayoutDemo demo = new GridLayoutDemo("Test");
		//variable that tracks which scenario we are running
		int metricScenario = 1;
		//number of units to place
		int metricUnitNum=100;
		//create nonvisual representation of grid
		demo.createGridAndResTable();
		//set scenario to bottleneck
		metricScenario = 1;
		//temporary copy of metric scenario value
		int tempMetrScen = metricScenario;
		//if this is a bottleneck scenario
		if(metricScenario==1){
			//create 30 bottleneck sets of data
			for(int i=0;i<30;i++){
				demo.getRandomizedBottleneck(metricUnitNum);
			}
		}
		//retrieve 30 sets of data
		ArrayList<Object> metricArrays = demo.getMetricArrays();
		//extract node data sets
		ArrayList<Node[]> nodes = (ArrayList<Node[]>)metricArrays.get(1);
		//create the metric gatherer with the collected sets of data
		mg = new MetricGatherer((ArrayList<Node[]>)metricArrays.get(1),(ArrayList<ArrayList<LRUnit>>)metricArrays.get(4),(ArrayList<Node[]>)metricArrays.get(2),(ArrayList<ArrayList<LRUnit>>)metricArrays.get(5),(ArrayList<ArrayList<HCAUnit>>)metricArrays.get(3),(ArrayList<ResTable>)metricArrays.get(0));
		//for each set of data
		for(int a = 0; a <30; a++){
			//tracks unit numbers
			ArrayList<Integer> units = new ArrayList<Integer>();
			//for each node
			for(int b = 1; b <1157; b++){
				//if the node is occupied by a unit, add the unit's number to the array list
				if(nodes.get(a)[b].getStatus()==Node.Status.occupied){
					units.add(nodes.get(a)[b].getOccupier());
				}
			}
			//an arraylist with numbers stretching from 0 to 100 in order
			ArrayList<Integer> ordered = new ArrayList<Integer>();
			for(int i = 0; i < units.size(); i++){
				ordered.add(i);
			}
			//this asserts that the units are not placed sequentially
			assertNotSame(units,ordered);
		}
		//clear the metric arrays that were just created
		demo.clearMetricArrays();
		//reset the recently erased value of metric scenario
		metricScenario = tempMetrScen;
	}
	
	/**
	 * Test ending points are in random locations.
	 */
	@SuppressWarnings("unchecked")
	public void testEndingPointsRandom(){
		//instance of main class
		GridLayoutDemo demo = new GridLayoutDemo("Test");
		//variable tracks scenario type
		int metricScenario = 1;
		//number of units to place
		int metricUnitNum=100;
		//create nonvisual representation of grid
		demo.createGridAndResTable();
		//sets scenario to bottleneck
		metricScenario = 1;
		//temporary copy of metric scenario value
		int tempMetrScen = metricScenario;
		//if this is a bottleneck scenario
		if(metricScenario==1){
			//create 30 bottleneck sets of data
			for(int i=0;i<30;i++){
				demo.getRandomizedBottleneck(metricUnitNum);
			}
		}
		//retrieve 30 data sets
		ArrayList<Object> metricArrays = demo.getMetricArrays();
		//extract node data sets
		ArrayList<Node[]> nodes = (ArrayList<Node[]>)metricArrays.get(1);
		//create the metric gatherer with the collected sets of data
		mg = new MetricGatherer((ArrayList<Node[]>)metricArrays.get(1),(ArrayList<ArrayList<LRUnit>>)metricArrays.get(4),(ArrayList<Node[]>)metricArrays.get(2),(ArrayList<ArrayList<LRUnit>>)metricArrays.get(5),(ArrayList<ArrayList<HCAUnit>>)metricArrays.get(3),(ArrayList<ResTable>)metricArrays.get(0));
		//for each data set
		for(int a = 0; a <30; a++){
			//tracks ending point locations
			ArrayList<Integer> ends = new ArrayList<Integer>();
			//for each node
			for(int b = 1; b <1157; b++){
				//if the node is an ending point, add the location to the arraylist
				if(nodes.get(a)[b].getStatus()==Node.Status.end){
					ends.add(nodes.get(a)[b].getEndNode());
				}
			}
			//arraylist that stretches from 0 to 99 in sequence
			ArrayList<Integer> ordered = new ArrayList<Integer>();
			for(int i = 0; i < ends.size(); i++){
				ordered.add(i);
			}
			//asserts that actual end points arent listed in order on the grid
			assertNotSame(ends,ordered);
		}
		//clear the metric arrays that were just created
		demo.clearMetricArrays();
		//reset the recently erased value of metric scenario
		metricScenario = tempMetrScen;
	}

}
