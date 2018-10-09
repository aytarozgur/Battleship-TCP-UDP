package battleship.Network;

import battleship.GUI.LobbyUDP;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import javax.swing.Timer;

/**
 *
 * @author Ozgur Aytar
 * @author Ozgur Yildiz
 */
public class UDPServer implements Runnable {
    public static int udpServerPortNb = 4444;
    public static int responseWaitTime = 3000;
    private byte[] buffer = new byte[256];
    public static String initCode = "Battleship";
    public boolean running;
    public String gameName;
    public LobbyUDP lobbyPtr2;
    private Timer responseListenTimer;
    private DatagramSocket socket;
    private Thread thread;

    public UDPServer(LobbyUDP lobbyPtr2, boolean broadcastServer) {
        this.lobbyPtr2 = lobbyPtr2;
        try {
            if(broadcastServer)
                socket = new DatagramSocket(udpServerPortNb);
            else
                socket = new DatagramSocket();
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void startServer(String gameName) {
        // start as broadcast listener
        if(running)
            return;

        running = true;
        this.gameName = gameName;
        start();
    }

    public void startServerBroadcast() {
        // send broadcast and start as response listener
        if(running)
            return;

        running = true;
        byte[] buffer = initCode.getBytes();
        try {
            start();
            InetAddress broadcastAddress = InetAddress.getByName("255.255.255.255");
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, broadcastAddress, udpServerPortNb);
            socket.send(packet);
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
        responseListenTimer = new Timer(responseWaitTime, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                stopServer();

            }
        });
        responseListenTimer.setRepeats(false);
        responseListenTimer.start();
    }

    public void stopServer() {
        running = false;
    }

    public void start()
    {
        if (thread == null) {
            thread = new Thread(this);
            thread.start();
        }
    }

    @Override
    public void run() {
        try {
            while (running) {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);
                InetAddress address = packet.getAddress();
                int port = packet.getPort();
                byte[] data = packet.getData();
                String infoString = new String(data);
                if(infoString.startsWith(initCode)) {
                    // return Ready
                    String dataStr = gameName;
                    data = dataStr.getBytes();
                    packet = new DatagramPacket(data, data.length, address, port);
                    socket.send(packet);
                    System.out.println(address + " asked for available games");
                }
                else {

                    lobbyPtr2.HandleUDPResponse(address, infoString);
                    System.out.println(infoString + " found");
                }
            }
            socket.close();
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
