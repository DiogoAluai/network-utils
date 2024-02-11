package daluai.lib.network_utils;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class DemoApplicationTests {

	@Test
    public void contextLoads() {
	}

	@Test
	public void checkSanity() {
		assertEquals(2 + 2, 4);
		assertNotEquals(10 + 9, 21);
	}

	/**
	 * Interface depends on computer, so this test might fail for diferent computers.
	 * Test that it locates wlo1.
	 * @author daluai
	 */
	@Test
	public void localProbe() {
		LocalIpProbe.firstActiveIPv4Address()
	}
}
