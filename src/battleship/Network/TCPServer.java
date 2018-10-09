package battleship.Network;

import battleship.GUI.LobbyTCP;

import javax.swing.*;
import java.io.*;
import java.net.*;

public class TCPServer implements Runnable {
    public static int tcpServerPortNb = 4444;
    public static int responseWaitTime2 = 3000;
    public static String initCode = "Battleship";
    public boolean running;
    public String gameName;
    public LobbyTCP lobbyPtr;
    String clientSentence;
    String capitalizedSentence;
    private Timer responseListenTimer;
    private ServerSocket socket2;
    private Thread thread;

    public TCPServer(LobbyTCP lobbyPtr, boolean broadcastServer2) {
        this.lobbyPtr = lobbyPtr;
        try {
            if (broadcastServer2)
                socket2 = new ServerSocket(tcpServerPortNb);
            else
                socket2 = new ServerSocket();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    public void startServer(String gameName) {
        // start as broadcast listener
        if (running)
            return;

        running = true;
        this.gameName = gameName;
        start();
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
                Socket connectionSocket = socket2.accept();
                ObjectInputStream ois = new ObjectInputStream( connectionSocket.getInputStream());
                ObjectOutputStream oos = new ObjectOutputStream( connectionSocket.getOutputStream());
            }
            socket2.close();
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
