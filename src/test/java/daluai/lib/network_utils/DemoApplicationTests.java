package daluai.lib.network_utils;

import org.junit.jupiter.api.Test;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
	 * Interface depends on computer, so this test might fail for different computers.
	 * Test that it locates wlo1.
	 * @author daluai
	 */
	@Test
	public void localProbe() {
		LocalIpProbe.IpResult ipResult = Objects.requireNonNull(LocalIpProbe.firstActiveIPv4Interface());

		String interfaceName = ipResult.networkInterface().getName();
		assertNotNull(interfaceName);
        assertTrue(interfaceName.startsWith("wl"));

		String ipAddress = ipResult.inetAddress().getHostAddress();
        assertFalse(ipAddress.isEmpty());
	}
}
