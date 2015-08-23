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
 * The Class RandomLayoutTests.
 */
public class RandomLayoutTests extends TestCase {
	//pathfinding object
	LocalRepair lr;
	//metric gathering object
	MetricGatherer mg;
	
	/**
	 * Test number of units placed is correct.
	 */
	@SuppressWarnings("unchecked")
	public void testNumberOfUnitsPlaced(){
		//instance of main class
		GridLayoutDemo demo = new GridLayoutDemo("Test");
		//tracks scenario type
		int metricScenario = 1;
		//number of units to be placed
		int metricUnitNum=100;
		//non visual representation of grid
		demo.createGridAndResTable();
		//indicate random layout scenario
		metricScenario = 3;
		//temporary copy of metric scenario value
		int tempMetrScen = metricScenario;
		//if this is a random scenario
		if(metricScenario==3){
			//create 30 random sets of data
			for(int i=0;i<30;i++){
				demo.getRandomizedRandom(metricUnitNum);
			}
		}
		//retrieve data sets
		ArrayList<Object> metricArrays = demo.getMetricArrays();
		//extract restable data sets
		ArrayList<ResTable> res = (ArrayList<ResTable>)metricArrays.get(0);
		//extract node data sets
		ArrayList<Node[]> nodes = (ArrayList<Node[]>)metricArrays.get(1);
		//create the metric gatherer with the collected sets of data
		mg = new MetricGatherer((ArrayList<Node[]>)metricArrays.get(1),(ArrayList<ArrayList<LRUnit>>)metricArrays.get(4),(ArrayList<Node[]>)metricArrays.get(2),(ArrayList<ArrayList<LRUnit>>)metricArrays.get(5),(ArrayList<ArrayList<HCAUnit>>)metricArrays.get(3),(ArrayList<ResTable>)metricArrays.get(0));
		//for each data set
		for(int a = 0; a <30; a++){
			int unitCount = 0;
			//for each node
			for(int b = 1; b <1157; b++){
				//if the node is occupied increase unit number
				if(nodes.get(a)[b].getStatus()==Node.Status.occupied){
					unitCount++;
				}
				if(res.get(a).getResNode().get(1156+b).getStatus()==ResNode.Status.occupied){
					unitCount++;
				}
			}
			//should be 100 units on both grids
			assertEquals(unitCount,200);
		}
		//clear the metric arrays that were just created
		demo.clearMetricArrays();
		//reset the recently erased value of metric scenario
		metricScenario = tempMetrScen;
		
	}
	
	/**
	 * Test number of destination nodes is correct.
	 */
	@SuppressWarnings("unchecked")
	public void testNumberOfDestinationNodes(){
		//instance of main class
		GridLayoutDemo demo = new GridLayoutDemo("Test");
		//tracks scenario type
		int metricScenario = 1;
		//number of units to be placed
		int metricUnitNum=100;
		//non visual representation of grid
		demo.createGridAndResTable();
		//indicates random layout scenario
		metricScenario = 3;
		//temporary copy of metric scenario value
		int tempMetrScen = metricScenario;
		//if this is a random scenario
		if(metricScenario==3){
			//create 30 random sets of data
			for(int i=0;i<30;i++){
				demo.getRandomizedRandom(metricUnitNum);
			}
		}
		//retrieve data sets
		ArrayList<Object> metricArrays = demo.getMetricArrays();
		//extract res table data sets
		ArrayList<ResTable> res = (ArrayList<ResTable>)metricArrays.get(0);
		//extract node data sets
		ArrayList<Node[]> nodes = (ArrayList<Node[]>)metricArrays.get(1);
		//create the metric gatherer with the collected sets of data
		mg = new MetricGatherer((ArrayList<Node[]>)metricArrays.get(1),(ArrayList<ArrayList<LRUnit>>)metricArrays.get(4),(ArrayList<Node[]>)metricArrays.get(2),(ArrayList<ArrayList<LRUnit>>)metricArrays.get(5),(ArrayList<ArrayList<HCAUnit>>)metricArrays.get(3),(ArrayList<ResTable>)metricArrays.get(0));
		//for each data set
		for(int a = 0; a <30; a++){
			int unitCount = 0;
			//for each node
			for(int b = 1; b <1157; b++){
				//if the node is an ending point, increasevariable value
				if(nodes.get(a)[b].getStatus()==Node.Status.end){
					unitCount++;
				}
				if(res.get(a).getResNode().get(1156+b).getStatus()==ResNode.Status.end){
					unitCount++;
				}
			}
			//should be 100 ending points for both grids
			assertEquals(unitCount,200);
		}
		//clear the metric arrays that were just created
		demo.clearMetricArrays();
		//reset the recently erased value of metric scenario
		metricScenario = tempMetrScen;
	}
	
	/**
	 * Test units in random locations.
	 */
	@SuppressWarnings("unchecked")
	public void testUnitsInRandomLocations(){
		//instance of main class
		GridLayoutDemo demo = new GridLayoutDemo("Test");
		//tracks scenario type
		int metricScenario = 1;
		//number of units to be placed
		int metricUnitNum=100;
		//non visual grid representation
		demo.createGridAndResTable();
		//indicates random grid layout
		metricScenario = 3;
		//temporary copy of metric scenario value
		int tempMetrScen = metricScenario;
		//if this is a random scenario
		if(metricScenario==3){
			//create 30 random sets of data
			for(int i=0;i<30;i++){
				demo.getRandomizedRandom(metricUnitNum);
			}
		}
		//retrieve data sets
		ArrayList<Object> metricArrays = demo.getMetricArrays();
		//extract lrunit data sets
		ArrayList<ArrayList<LRUnit>> lrunit = (ArrayList<ArrayList<LRUnit>>)metricArrays.get(4);
		//create the metric gatherer with the collected sets of data
		mg = new MetricGatherer((ArrayList<Node[]>)metricArrays.get(1),(ArrayList<ArrayList<LRUnit>>)metricArrays.get(4),(ArrayList<Node[]>)metricArrays.get(2),(ArrayList<ArrayList<LRUnit>>)metricArrays.get(5),(ArrayList<ArrayList<HCAUnit>>)metricArrays.get(3),(ArrayList<ResTable>)metricArrays.get(0));
		//for each data set
		for(int a = 0; a <30; a++){
			//tracks unit locations
			ArrayList<Integer> unitLocations = new ArrayList<Integer>();
			//for each unit, record location
			for(int b = 0; b <100; b++){
				unitLocations.add(lrunit.get(a).get(b).getX()+lrunit.get(a).get(b).getY()*34-34);
			}
			//assert that units are not placed sequentially in starting points
			assertFalse(unitLocations.get(0) == 36 && unitLocations.get(1)== 38 && unitLocations.get(2) == 40 && unitLocations.get(3) == 42);
		}
		//clear the metric arrays that were just created
		demo.clearMetricArrays();
		//reset the recently erased value of metric scenario
		metricScenario = tempMetrScen;
	}
	
	/**
	 * Test units in random order.
	 */
	@SuppressWarnings("unchecked")
	public void testUnitsInRandomOrder(){
		//instance of main class
		GridLayoutDemo demo = new GridLayoutDemo("Test");
		//tracks scenario type
		int metricScenario = 1;
		//number of units to be placed
		int metricUnitNum=100;
		//non visual represenation of the grid
		demo.createGridAndResTable();
		//indicate random layout scenario
		metricScenario = 3;
		//temporary copy of metric scenario value
		int tempMetrScen = metricScenario;
		//if this is a random scenario
		if(metricScenario==3){
			//create 30 random sets of data
			for(int i=0;i<30;i++){
				demo.getRandomizedRandom(metricUnitNum);
			}
		}
		//retrieve data sets
		ArrayList<Object> metricArrays = demo.getMetricArrays();
		//retrieve node data sets
		ArrayList<Node[]> nodes = (ArrayList<Node[]>)metricArrays.get(1);
		//create the metric gatherer with the collected sets of data
		mg = new MetricGatherer((ArrayList<Node[]>)metricArrays.get(1),(ArrayList<ArrayList<LRUnit>>)metricArrays.get(4),(ArrayList<Node[]>)metricArrays.get(2),(ArrayList<ArrayList<LRUnit>>)metricArrays.get(5),(ArrayList<ArrayList<HCAUnit>>)metricArrays.get(3),(ArrayList<ResTable>)metricArrays.get(0));
		//for each data set
		for(int a = 0; a <30; a++){
			ArrayList<Integer> units = new ArrayList<Integer>();
			//for each node, if node is occupied record unit number
			for(int b = 1; b <1157; b++){
				if(nodes.get(a)[b].getStatus()==Node.Status.occupied){
					units.add(nodes.get(a)[b].getOccupier());
				}
			}
			//numbers from 0 to 99
			ArrayList<Integer> ordered = new ArrayList<Integer>();
			for(int i = 0; i < units.size(); i++){
				ordered.add(i);
			}
			//asserts that unit locations are not in sequential order for starting locations
			assertNotSame(units,ordered);
		}
		//clear the metric arrays that were just created
		demo.clearMetricArrays();
		//reset the recently erased value of metric scenario
		metricScenario = tempMetrScen;
	}
	
	/**
	 * Test ending points in random order.
	 */
	@SuppressWarnings("unchecked")
	public void testEndingPointsRandom(){
		//instance of main class
		GridLayoutDemo demo = new GridLayoutDemo("Test");
		//tracks which scenario this is
		int metricScenario = 1;
		//number of units to place
		int metricUnitNum=100;
		//non visual representation of grid
		demo.createGridAndResTable();
		//indicates random layout grid
		metricScenario = 3;
		//temporary copy of metric scenario value
		int tempMetrScen = metricScenario;
		//if this is a random scenario
		if(metricScenario==3){
			//create 30 random sets of data
			for(int i=0;i<30;i++){
				demo.getRandomizedRandom(metricUnitNum);
			}
		}
		//retrieve data sets
		ArrayList<Object> metricArrays = demo.getMetricArrays();
		//extract node data sets
		ArrayList<Node[]> nodes = (ArrayList<Node[]>)metricArrays.get(1);
		//create the metric gatherer with the collected sets of data
		mg = new MetricGatherer((ArrayList<Node[]>)metricArrays.get(1),(ArrayList<ArrayList<LRUnit>>)metricArrays.get(4),(ArrayList<Node[]>)metricArrays.get(2),(ArrayList<ArrayList<LRUnit>>)metricArrays.get(5),(ArrayList<ArrayList<HCAUnit>>)metricArrays.get(3),(ArrayList<ResTable>)metricArrays.get(0));
		//for each data set
		for(int a = 0; a <30; a++){
			ArrayList<Integer> ends = new ArrayList<Integer>();
			//for each node, if it is an end position record the unit number
			for(int b = 1; b <1157; b++){
				if(nodes.get(a)[b].getStatus()==Node.Status.end){
					ends.add(nodes.get(a)[b].getEndNode());
				}
			}
			//numbers from 0 to 99
			ArrayList<Integer> ordered = new ArrayList<Integer>();
			for(int i = 0; i < ends.size(); i++){
				ordered.add(i);
			}
			//make sure the units arent placed in order
			assertNotSame(ends,ordered);
		}
		//clear the metric arrays that were just created
		demo.clearMetricArrays();
		//reset the recently erased value of metric scenario
		metricScenario = tempMetrScen;
	}

	/**
	 * Test that 30% of the nodes are obstacles.
	 */
	@SuppressWarnings("unchecked")
	public void test30PercentObstacles(){
		//create instance of main class
		GridLayoutDemo demo = new GridLayoutDemo("Test");
		//tracks scenario type
		int metricScenario = 1;
		//number of unis to place
		int metricUnitNum=100;
		//non visual representation of grid
		demo.createGridAndResTable();
		//indicates random layout scenario
		metricScenario = 3;
		//temporary copy of metric scenario value
		int tempMetrScen = metricScenario;
		//if this is a bottleneck scenario
		if(metricScenario==3){
			//create 30 bottleneck sets of data
			for(int i=0;i<30;i++){
				demo.getRandomizedRandom(metricUnitNum);
			}
		}
		//retrieve data sets
		ArrayList<Object> metricArrays = demo.getMetricArrays();
		//extract node data sets
		ArrayList<Node[]> nodes = (ArrayList<Node[]>)metricArrays.get(1);
		//create the metric gatherer with the collected sets of data
		mg = new MetricGatherer((ArrayList<Node[]>)metricArrays.get(1),(ArrayList<ArrayList<LRUnit>>)metricArrays.get(4),(ArrayList<Node[]>)metricArrays.get(2),(ArrayList<ArrayList<LRUnit>>)metricArrays.get(5),(ArrayList<ArrayList<HCAUnit>>)metricArrays.get(3),(ArrayList<ResTable>)metricArrays.get(0));
		//for each data set
		for(int a = 0; a <30; a++){
			int obstacleCount = 0;
			//for each node, if it is an obstacle, increment obstacle count
			for(int b = 1; b <1157; b++){
				if(nodes.get(a)[b].getStatus()==Node.Status.obstacle){
					obstacleCount++;
				}
			}
			assertEquals(obstacleCount,337);
		}
		//clear the metric arrays that were just created
		demo.clearMetricArrays();
		//reset the recently erased value of metric scenario
		metricScenario = tempMetrScen;
		
	}
}
