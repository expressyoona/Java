import java.io.FileWriter;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;

public class TTTServer extends Thread {

    public final static int PORT = 7331;
    private final static int DATA_LENGTH = 1024;
    private static int ctr = 0;
    private boolean flag;
    private DatagramSocket socket;
    //Save clients IP.
    private ArrayList<InetAddress> clientAddresses;
    //Save clients port.
    private ArrayList<Integer> clientPorts;
    private HashSet<String> existingClients;
    //Save name of winner
    private String rs;
    //Save IP of winner
    private InetAddress ip;

    public TTTServer() throws IOException {
        socket = new DatagramSocket(PORT);
        clientAddresses = new ArrayList();
        clientPorts = new ArrayList();
        existingClients = new HashSet();
        Inet4Address myip = (Inet4Address) InetAddress.getLocalHost();
        System.out.println("Server is working at " + myip.getHostAddress());
    }

    public void saveResult() throws IOException {
        FileWriter fw = new FileWriter("./result.txt", true);
        fw.write("\n" + rs + "(" + ip.getHostAddress() + ")" + " - " + "[" + new Date().toString() + "]");
        fw.close();
    }
    
    public void run() {
        byte[] buf = new byte[DATA_LENGTH];
        while (true) {
            try {
                //Buf = 0x1024
                Arrays.fill(buf, (byte) 0);
                //Receive DP from client
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                socket.receive(packet);
                String content = new String(buf, buf.length);
                //Get IP Address
                InetAddress clientAddress = packet.getAddress();
                int clientPort = packet.getPort();

                String id = clientAddress.toString() + "," + clientPort;
                //If client didn't exist in Set then save Id, Port and Address.
                if (!existingClients.contains(id)) {
                    ctr++;
                    existingClients.add(id);
                    clientPorts.add(clientPort);
                    clientAddresses.add(clientAddress);
                }
                if (content.trim().equals("New Player")) {
                    byte[] data = null;
                    int k = clientAddresses.indexOf(packet.getAddress());
                    if (k == 0) {
                        data = "You first".getBytes();
                    } else if (k == 1) {
                        data = "Not you".getBytes();
                        //If k == 1 then both 2 player are connected! Permit start game.
                    }
                    packet = new DatagramPacket(data, data.length, packet.getAddress(), packet.getPort());
                    socket.send(packet);
                    if (clientAddresses.size() == 2) {
                        
                    }
                }
                if (content.trim().equals("I won!")) {
                    int k = clientAddresses.indexOf(clientAddress);
                    if (k == 0) {
                        rs = "Player 1 win!";
                        ip = clientAddress;
                    } else if (k == 1) {
                        rs = "Player 2 win!";
                        ip = clientAddress;
                    }
                    saveResult();
                    System.out.println(rs);
                }
                //System.out.println("Player " + clientAddresses.indexOf(clientAddress) + 1 + ": " + content);
                byte[] data = (content).getBytes();
                for (int i = 0; i < clientAddresses.size(); i++) {
                    InetAddress cl = clientAddresses.get(i);
                    if (!cl.equals(clientAddress)) {
                        System.out.println("Sent to " + cl.getHostAddress());
                        int cp = clientPorts.get(i);
                        packet = new DatagramPacket(data, data.length, cl, cp);
                        socket.send(packet);
                    }
                }
            } catch (Exception e) {
                System.err.println(e);
            }
        }
    }

    public static void main(String args[]) throws Exception {
        TTTServer s = new TTTServer();
        s.start();
    }
}
