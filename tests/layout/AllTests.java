/**
 * Kevin Layfield
 * Dr. Wellington
 * 2009
 * Sr Research - Cooperative Pathfinding Vs. Local Repair Pathfinding
 */

package layout;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * The Class AllTests.
 */
public class AllTests {

	/**
	 * Suite.
	 */
	public static Test suite() {
		TestSuite suite = new TestSuite("Test for layout");
		//$JUnit-BEGIN$
		suite.addTestSuite(GUIandPlacement.class);
		suite.addTestSuite(ResNodeTests.class);
		suite.addTestSuite(HCATests.class);
		suite.addTestSuite(NodeTests.class);
		suite.addTestSuite(LocalRepairTests.class);
		suite.addTestSuite(BottleneckTests.class);
		suite.addTestSuite(RandomLayoutTests.class);
		suite.addTestSuite(CITests.class);
		suite.addTestSuite(MetricGathererTests.class);
		//$JUnit-END$
		return suite;
	}

}
