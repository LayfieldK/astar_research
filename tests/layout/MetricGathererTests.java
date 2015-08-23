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
 * The Class MetricGathererTests.
 */
public class MetricGathererTests extends TestCase {
	//pathfinding object
	/** The lr. */
	LocalRepair lr;
	//metric gathering object
	/** The mg. */
	MetricGatherer mg;
	
	/**
	 * Test that it produces 30 different layouts.
	 */
	@SuppressWarnings("unchecked")
	public void test30DifferentLayouts(){
		//instance of main class
		GridLayoutDemo demo = new GridLayoutDemo("Test");
		//tracks scenario type
		int metricScenario = 1;
		//number of units to place
		int metricUnitNum=100;
		//non visual representation of grid
		demo.createGridAndResTable();
		//for all 3 scenarios
		for(int j =1;j<4;j++){
			metricScenario = j;
			//temporary copy of metric scenario value
			int tempMetrScen = metricScenario;
			//if this is a bottleneck scenario
			if(metricScenario==1){
				//create 30 bottleneck sets of data
				for(int i=0;i<30;i++){
					demo.getRandomizedBottleneck(metricUnitNum);
				}
			}
			//if this is a chaotic intersection scenario
			if(metricScenario==2){
				//create 30 CI sets of data
				for(int i = 0;i<30;i++){
					demo.getRandomizedCI(metricUnitNum);
				}
			}
			//if this is a random layout scenario
			if(metricScenario==3){
				//create 30 random sets of data
				for(int i = 0; i<30;i++){
					demo.getRandomizedRandom(metricUnitNum);
				}
			}
			//retrieve data sets
			ArrayList<Object> metricArrays = demo.getMetricArrays();
			//extract specific data sets
			ArrayList<ResTable> res = (ArrayList<ResTable>)metricArrays.get(0);
			ArrayList<Node[]> nodes = (ArrayList<Node[]>)metricArrays.get(1);
			ArrayList<Node[]> nodes1 = (ArrayList<Node[]>)metricArrays.get(2);
			ArrayList<ArrayList<LRUnit>> lrunit = (ArrayList<ArrayList<LRUnit>>)metricArrays.get(4);
			ArrayList<ArrayList<LRUnit>> lrunit1 = (ArrayList<ArrayList<LRUnit>>)metricArrays.get(5);
			ArrayList<ArrayList<HCAUnit>> hcaunit = (ArrayList<ArrayList<HCAUnit>>)metricArrays.get(3);
			//create the metric gatherer with the collected sets of data
			mg = new MetricGatherer((ArrayList<Node[]>)metricArrays.get(1),(ArrayList<ArrayList<LRUnit>>)metricArrays.get(4),(ArrayList<Node[]>)metricArrays.get(2),(ArrayList<ArrayList<LRUnit>>)metricArrays.get(5),(ArrayList<ArrayList<HCAUnit>>)metricArrays.get(3),(ArrayList<ResTable>)metricArrays.get(0));
			//for each data set
			for(int a = 0; a <30; a++){
				//assert that each set of data after the current one is not equal to this current one
				for(int b = a+1; b <30; b++){
					assertEquals(nodes.get(a),nodes.get(a));
					assertNotSame(nodes.get(a),nodes.get(b));
					assertNotSame(nodes1.get(a),nodes1.get(b));
					assertNotSame(lrunit.get(a),lrunit.get(b));
					assertNotSame(lrunit1.get(a),lrunit1.get(b));
					assertNotSame(hcaunit.get(a),hcaunit.get(b));
					assertNotSame(res.get(a),res.get(b));
				}
			}
			//clear the metric arrays that were just created
    		demo.clearMetricArrays();
    		//reset the recently erased value of metric scenario
			metricScenario = tempMetrScen;
		}
    }
    	
	
	
	/**
	 * Test that the layouts are consistent between both algorithms.
	 */
	@SuppressWarnings("unchecked")
	public void testLayoutsConsistentForBothAlgorithms(){
		//instance of main class
		GridLayoutDemo demo = new GridLayoutDemo("Test");
		//tracks scenario type
		int metricScenario = 1;
		//number of units to be placed
		int metricUnitNum=100;
		//non visual representation of the grid
		demo.createGridAndResTable();
		//for each scenario
		for(int j =1;j<4;j++){
			metricScenario = j;
			//temporary copy of metric scenario value
			int tempMetrScen = metricScenario;
			//if this is a bottleneck scenario
			if(metricScenario==1){
				//create 30 bottleneck sets of data
				for(int i=0;i<30;i++){
					demo.getRandomizedBottleneck(metricUnitNum);
				}
			}
			//if this is a chaotic intersection scenario
			if(metricScenario==2){
				//create 30 CI sets of data
				for(int i = 0;i<30;i++){
					demo.getRandomizedCI(metricUnitNum);
				}
			}
			//if this is a random layout scenario
			if(metricScenario==3){
				//create 30 random sets of data
				for(int i = 0; i<30;i++){
					demo.getRandomizedRandom(metricUnitNum);
				}
			}
			//retrieve data sets
			ArrayList<Object> metricArrays = demo.getMetricArrays();
			//extract individual data sets
			ArrayList<ResTable> res = (ArrayList<ResTable>)metricArrays.get(0);
			ArrayList<Node[]> nodes = (ArrayList<Node[]>)metricArrays.get(1);
			ArrayList<ArrayList<LRUnit>> lrunit = (ArrayList<ArrayList<LRUnit>>)metricArrays.get(4);
			ArrayList<ArrayList<HCAUnit>> hcaunit = (ArrayList<ArrayList<HCAUnit>>)metricArrays.get(3);
			//create the metric gatherer with the collected sets of data
			mg = new MetricGatherer((ArrayList<Node[]>)metricArrays.get(1),(ArrayList<ArrayList<LRUnit>>)metricArrays.get(4),(ArrayList<Node[]>)metricArrays.get(2),(ArrayList<ArrayList<LRUnit>>)metricArrays.get(5),(ArrayList<ArrayList<HCAUnit>>)metricArrays.get(3),(ArrayList<ResTable>)metricArrays.get(0));
			//for each data set
			for(int a = 0; a <30; a++){
				//for each data set after the current one
				for(int b = a+1; b <30; b++){
					//assert that unit placement is the same between algorithms
					assertEquals(lrunit.get(a).get(0).getX(),hcaunit.get(a).get(0).getX());
					assertEquals(lrunit.get(a).get(0).getY(),hcaunit.get(a).get(0).getY());
					for(int i = 1; i<1157;i++){
						assertEquals(nodes.get(a)[i].getX(),res.get(a).getResNode().get(1156+i).getX());
						assertEquals(nodes.get(a)[i].getY(),res.get(a).getResNode().get(1156+i).getY());

					}
				}
			}
			//clear the metric arrays that were just created
    		demo.clearMetricArrays();
    		//reset the recently erased value of metric scenario
			metricScenario = tempMetrScen;
		}
		
	}
}
