package daluai.lib.network_utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.Objects;

public class LocalIpProbe {

    private static final Logger LOG = LoggerFactory.getLogger(LocalIpProbe.class);

    public record IpResult(NetworkInterface networkInterface, InetAddress inetAddress) {
    }

    /**
     * Gets the IPv4 address of the first active, non-loopback network interface.
     * @return The IPv4 address as a String, or null if no suitable address is found.
     */
    public static String firstActiveIPv4Address() {
        String firstActiveInetAddress = Objects.requireNonNull(firstActiveIPv4Interface()).inetAddress().getHostAddress();
        System.out.println("Using IPv4 Address: " + firstActiveInetAddress);
        return firstActiveInetAddress;
    }

    public static IpResult firstActiveIPv4Interface() {
        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();

            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = networkInterfaces.nextElement();

                if (networkInterface.isUp() && !networkInterface.isLoopback() && !networkInterface.getName().startsWith("docker")) {
                    Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();

                    while (inetAddresses.hasMoreElements()) {
                        InetAddress inetAddress = inetAddresses.nextElement();

                        // Check for IPv4 address
                        if (inetAddress.getAddress().length == 4) {
                            System.out.println("Using network interface: " + networkInterface.getName());
                            return new IpResult(networkInterface, inetAddress);
                        }
                    }
                }
            }
        } catch (SocketException e) {
            LOG.error("Failed to fetch first active ip", e);
        }
        return null;
    }
}

