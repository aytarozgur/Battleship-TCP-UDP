package battleship.Network;


import battleship.Engine.Game;
import battleship.GUI.LobbyUDP;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.*;

public class UDPClient extends Thread implements IClient {
    private boolean running, playing, opponentready, waitforclient;
    private boolean ishost = false;
    private boolean isClient = false;
    private int port = 45678;
    private Thread thread;

    private InetAddress ipAddress;
    public String gameName;
    public static String initCode = "Battleship";

    private DatagramSocket serverSocket;
    private byte[] buffer = new byte[256];
    private Socket connectionSocket;
    private Socket clientSocket;

    private ObjectOutputStream objectWriter;
    private ReadObject writeObject;

    private Game game;
    private LobbyUDP lobby;


    public UDPClient()
    {
        ishost = false;
        start();
    }
    public Socket getSender() {
        return clientSocket;
    }

    public void start() {
        thread = new Thread(this);
        thread.start();
        running = true;
        playing = false;
        System.out.println("UDP Thread started");
    }

    public boolean isHost() {
        return ishost;
    }

    public boolean opponentReady() {
        return opponentready;
    }

    public boolean host(LobbyUDP alobby) {
        this.lobby = alobby;
        waitforclient = true;
        ishost = true;
        try {
            serverSocket = new DatagramSocket(port);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            return false;
        }
        System.out.println("Host");
        playing = true;
        return true;
    }

    public synchronized boolean connect(InetAddress ipadr) {
        ishost = false;
        try {
            clientSocket = new Socket(ipadr, port);
            isClient = true;
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            return false;
        }
        System.out.println("attached to: "+clientSocket.getInetAddress());
        playing = true;
        return true;
    }

    public InetAddress getInetAddress(){
        return ipAddress;
    }

    public void disconnect() throws IOException {
        running = false;
        if(ishost == true) {
            serverSocket.close();
        } else {
            clientSocket.close();
        }
        ishost = false;
        playing = false;
        opponentready = false;
    }



    public void run() {
        try {
            while(running) {
                Thread.sleep(10);
                if(playing == true) {
                    if (isClient == true){

                        writeObject = new ReadObject(clientSocket, game);
                        isClient = false;
                        System.out.println("is Client");
                        running = false;
                    }

                    if(ishost == true) {
                        if(waitforclient == true) {

//                            DatagramPacket  dp = new DatagramPacket(buffer , buffer.length , ipAddress , port);
//                            serverSocket.send(dp);

                            DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
                            serverSocket.receive(reply);

                            //Startgame
                            lobby.StartGame(this);

                            writeObject = new ReadObject(connectionSocket, game);

                            System.out.println("Client connection accepted from: "+connectionSocket.getInetAddress());
                            waitforclient = false;
                            running = false;
                        }
                    }
                }
            }
//            serverSocket.close();
        } catch(InterruptedException | IOException ex) {
            System.out.println("UDPNetwork Exception: "+ex.getMessage());
        }
    }

    public void close() {
        if(running == true) {
            running = false;
            System.out.println("UDP Thread closed");
        }
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getPort() {
        return port;
    }

    /**
     *
     * @param sender
     */
    public void setSender(Socket sender) {

    }

    public DatagramSocket getReceiver() {
        return serverSocket;
    }
    /**
     *
     * @param receiver
     */
    public void setReceiver(ServerSocket receiver) {

    }

    @Override
    public void sendMessage(Message message) {
        try {
            if (ishost){
                objectWriter = new ObjectOutputStream(connectionSocket.getOutputStream());
            } else {
                objectWriter = new ObjectOutputStream(clientSocket.getOutputStream());
            }
            objectWriter.writeObject(message);
            objectWriter.flush();
        }
        catch (Exception e){
            System.out.println("Exception: sendMessage " + e.getMessage());
        }
    }

    @Override
    public void registerGame(Game game) {
        this.game = game;

    }

    private ePlayerState state;

    @Override
    public ePlayerState getPlayerState() {
        return state;
    }

    @Override
    public void setState(ePlayerState state) {
        this.state = state;
    }

}
