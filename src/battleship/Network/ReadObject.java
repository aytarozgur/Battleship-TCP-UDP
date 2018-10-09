package battleship.Network;

import battleship.Engine.Game;

import java.io.ObjectInputStream;
import java.net.Socket;
/**
 *
 * @author Ozgur Aytar
 * @author Ozgur Yildiz
 */
public class ReadObject extends Thread {
    Thread thread;    
    private ObjectInputStream objectReader;
    private Socket socket;
    private Object tmp;
    private Game game;
    private boolean running = true;

    public ReadObject(Socket socket, Game game) {
        this.socket = socket;
        this.game = game;
        thread = new Thread(this);
        thread.start();
    }
            
    @Override
    public void run(){                
        try {
            while(running){
            objectReader = new ObjectInputStream(socket.getInputStream());
            tmp = objectReader.readObject();
            if (tmp != null && tmp instanceof Message){
                this.game.handleOponentMessage((Message)tmp);
            }
            }
        } catch(Exception e) {
            System.out.println("Network Exception ReadObject: "+e.getMessage());
        }
    }
   
}
