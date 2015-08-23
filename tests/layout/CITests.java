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
 * The Class CITests.
 */
public class CITests extends TestCase {
	
	/** The lr. */
	LocalRepair lr;
	
	/** The mg. */
	MetricGatherer mg;
	
	/**
	 * Test number of units placed is correct.
	 */
	@SuppressWarnings("unchecked")
	public void testNumberOfUnitsPlaced(){
		//instance of main class
		GridLayoutDemo demo = new GridLayoutDemo("Test");
		//which scenario we are running
		int metricScenario = 1;
		//number of units to be placed
		int metricUnitNum=100;
		//non visual representation of the grid
		demo.createGridAndResTable();
		//indicates chaotic intersection scenario
		metricScenario = 2;
		//temporary copy of metric scenario value
		int tempMetrScen = metricScenario;
		//if this is a CI scenario
		if(metricScenario==2){
			//create 30 CI sets of data
			for(int i=0;i<30;i++){
				demo.getRandomizedCI(metricUnitNum);
			}
		}
		//retrieves 30 sets of data
		ArrayList<Object> metricArrays = demo.getMetricArrays();
		//extracts res table data sets
		ArrayList<ResTable> res = (ArrayList<ResTable>)metricArrays.get(0);
		//extracts node data sets
		ArrayList<Node[]> nodes = (ArrayList<Node[]>)metricArrays.get(1);
		//create the metric gatherer with the collected sets of data
		mg = new MetricGatherer((ArrayList<Node[]>)metricArrays.get(1),(ArrayList<ArrayList<LRUnit>>)metricArrays.get(4),(ArrayList<Node[]>)metricArrays.get(2),(ArrayList<ArrayList<LRUnit>>)metricArrays.get(5),(ArrayList<ArrayList<HCAUnit>>)metricArrays.get(3),(ArrayList<ResTable>)metricArrays.get(0));
		//for each data set
		for(int a = 0; a <30; a++){
			int unitCount = 0;
			//for each node
			for(int b = 1; b <1157; b++){
				//if the node is occupied increase unit count
				if(nodes.get(a)[b].getStatus()==Node.Status.occupied){
					unitCount++;
				}
				//if the resnode is occupied increase unit count
				if(res.get(a).getResNode().get(1156+b).getStatus()==ResNode.Status.occupied){
					unitCount++;
				}
			}
			//100 units for both restable and grid
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
		//instance of main class
		GridLayoutDemo demo = new GridLayoutDemo("Test");
		//tracks which scenario we are running
		int metricScenario = 2;
		//number of units to be placed
		int metricUnitNum=100;
		//non visual representation of grid
		demo.createGridAndResTable();
		//indicates chaotic intersection scenario
		metricScenario = 2;
		//temporary copy of metric scenario value
		int tempMetrScen = metricScenario;
		//if this is a CI scenario
		if(metricScenario==2){
			//create 30 CI sets of data
			for(int i=0;i<30;i++){
				demo.getRandomizedCI(metricUnitNum);
			}
		}
		//retrieve 30 data sets
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
				//if the node is an ending point, increase variable value
				if(nodes.get(a)[b].getStatus()==Node.Status.end){
					unitCount++;
				}
				//if the resnode is an ending point, increase variable value
				if(res.get(a).getResNode().get(1156+b).getStatus()==ResNode.Status.end){
					unitCount++;
				}
			}
			//should be 100 ending points for the restable 
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
		//instance of main class
		GridLayoutDemo demo = new GridLayoutDemo("Test");
		//tracks which scenario to run
		int metricScenario = 1;
		//how many units to place
		int metricUnitNum=100;
		//non visual representation of the grid
		demo.createGridAndResTable();
		//indicates chaotic intersection scenario
		metricScenario = 2;
		//temporary copy of metric scenario value
		int tempMetrScen = metricScenario;
		//if this is a CI scenario
		if(metricScenario==2){
			//create 30 CI sets of data
			for(int i=0;i<30;i++){
				demo.getRandomizedCI(metricUnitNum);
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
			//tracks locations of each unit
			ArrayList<Integer> unitLocations = new ArrayList<Integer>();
			//for each unit, add node number to arraylist
			for(int b = 0; b <100; b++){
				unitLocations.add(lrunit.get(a).get(b).getX()+lrunit.get(a).get(b).getY()*34-34);
			}
			//assert that units are not placed in sequential locations
			assertFalse(unitLocations.get(0) == 36 && unitLocations.get(1)== 37 && unitLocations.get(2) == 38 && unitLocations.get(3) == 39);
		}
		//clear the metric arrays that were just created
		demo.clearMetricArrays();
		//reset the recently erased value of metric scenario
		metricScenario = tempMetrScen;
	}
	
	/**
	 * Test unit numbers are placed in random order.
	 */
	@SuppressWarnings("unchecked")
	public void testUnitsInRandomOrder(){
		//instance of main class
		GridLayoutDemo demo = new GridLayoutDemo("Test");
		//tracks scenario type
		int metricScenario = 1;
		//number of units to place
		int metricUnitNum=100;
		//non visual representation of grid
		demo.createGridAndResTable();
		//indicate CI scenario
		metricScenario = 2;
		//temporary copy of metric scenario value
		int tempMetrScen = metricScenario;
		//if this is a CI scenario
		if(metricScenario==2){
			//create 30 CI sets of data
			for(int i=0;i<30;i++){
				demo.getRandomizedCI(metricUnitNum);
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
			//tracks unit numbers
			ArrayList<Integer> units = new ArrayList<Integer>();
			//for each node
			for(int b = 1; b <1157; b++){
				//if the node is occupied, record the number of the unit occupying it
				if(nodes.get(a)[b].getStatus()==Node.Status.occupied){
					units.add(nodes.get(a)[b].getOccupier());
				}
			}
			//array of ints 0 to 99
			ArrayList<Integer> ordered = new ArrayList<Integer>();
			for(int i = 0; i < units.size(); i++){
				ordered.add(i);
			}
			//the order of the units encountered should not be sequential
			assertNotSame(units,ordered);
		}
		//clear the metric arrays that were just created
		demo.clearMetricArrays();
		//reset the recently erased value of metric scenario
		metricScenario = tempMetrScen;
	}
	
	/**
	 * Test to make sure that ending points are across from starting points.
	 */
	@SuppressWarnings("unchecked")
	public void testEndingPointsAcrossFromStartingPoints(){
		//instance of main class
		GridLayoutDemo demo = new GridLayoutDemo("Test");
		//tracks scenario type
		int metricScenario = 1;
		//number of units to be placed
		int metricUnitNum=100;
		//non visual representation of grid
		demo.createGridAndResTable();
		//indicates chaotic intersection scenario
		metricScenario = 2;
		//temporary copy of metric scenario value
		int tempMetrScen = metricScenario;
		//if this is a CI scenario
		if(metricScenario==2){
			//create 30 CI sets of data
			for(int i=0;i<30;i++){
				demo.getRandomizedCI(metricUnitNum);
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
			//unit starting nodes
			ArrayList<Integer> unitLocations = new ArrayList<Integer>();
			//unit ending nodes
			ArrayList<Integer> unitDestinations = new ArrayList<Integer>();
			//for each unit
			for(int b = 0; b <100; b++){
				//add unit starting points and ending points to array lists
				unitLocations.add(lrunit.get(a).get(b).getX()+lrunit.get(a).get(b).getY()*34-34);
				unitDestinations.add(lrunit.get(a).get(b).getEndingPoint().getX() + lrunit.get(a).get(b).getEndingPoint().getY()*34-34);
				//asserts that the ending point for each unit is directly across the map from where it started
				if(unitLocations.get(b)/34 == 2){
					assertEquals((int)unitDestinations.get(b),(int)unitLocations.get(b)+34);
				}
				if(unitLocations.get(b)%34 == 33 && unitLocations.get(b)!=67 && unitLocations.get(b)!=1121){
					assertEquals((int)unitDestinations.get(b),(int)unitLocations.get(b)-30);
				}
				if(unitLocations.get(b)%34 == 2 && unitLocations.get(b)!=36 && unitLocations.get(b)!=1090){
					assertEquals((int)unitDestinations.get(b),(int)unitLocations.get(b)+30);
				}
				if(unitLocations.get(b)/34 == 33){
					assertEquals((int)unitDestinations.get(b),(int)unitLocations.get(b)-34);
				}
			}
		}
		//clear the metric arrays that were just created
		demo.clearMetricArrays();
		//reset the recently erased value of metric scenario
		metricScenario = tempMetrScen;
	}
}
