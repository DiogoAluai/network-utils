package daluai.lib.network_utils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class LocalIpProbe {

    /**
     * Gets the IPv4 address of the first active, non-loopback network interface.
     * @return The IPv4 address as a String, or null if no suitable address is found.
     */
    public static String firstActiveIPv4Address() {
        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();

            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = networkInterfaces.nextElement();

                if (networkInterface.isUp() && !networkInterface.isLoopback()) {
                    Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
                    
                    while (inetAddresses.hasMoreElements()) {
                        InetAddress inetAddress = inetAddresses.nextElement();
                        
                        // Check for IPv4 address
                        if (inetAddress.getAddress().length == 4) {
                            System.out.println("Using network interface: " + networkInterface.getName());
                            System.out.println("IPv4 Address: " + inetAddress.getHostAddress());
                            return inetAddress.getHostAddress();
                        }
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }

        return null; // No suitable address found
    }
}

