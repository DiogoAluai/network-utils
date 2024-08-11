package daluai.lib.network_utils;

import org.junit.Test;

import java.net.InetAddress;
import java.util.Objects;

import static org.junit.Assert.*;

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
        assertEquals("wlo1", interfaceName);

		String ipAddress = ipResult.inetAddress().getHostAddress();
        assertFalse(ipAddress.isEmpty());
	}
}
