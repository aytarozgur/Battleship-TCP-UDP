package battleship.GUI;

import battleship.Engine.Game;
import battleship.Network.GameInfo;
import battleship.Network.IClient;
import battleship.Network.TCPClient;
import battleship.Network.TCPServer;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import javax.swing.*;

/**
 *
 * @author Ozgur Aytar
 * @author Ozgur Yildiz
 */
public class LobbyTCP extends JFrame
{
    private JPanel pnlLobby;
    private JPanel pnlButtons;
    private List lstGames;
    private JButton btnCreateGame;
    private JButton btnJoinGame;
    private JTextField txtIP;
    private JButton btnJoinGameIP;
    private ArrayList<GameInfo> gameList;
    private TCPServer broadcastServer;
    private TCPServer responseServer;
    private boolean waitMode;
    private TCPClient player;
    
    public LobbyTCP() throws UnknownHostException {
        //Window Settings
        super("LobbyTCP");
        
        //Initialize Components
        InitializeComponents();
        
        // UDP servers
        broadcastServer = new TCPServer(this, true);
        responseServer = new TCPServer(this, false);
        gameList = new ArrayList<>();
    }

    private void InitializeComponents() throws UnknownHostException {
        //Window Settings
        setSize(500, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        //Main Panel
        pnlLobby = new JPanel(new GridLayout(1, 1));
        pnlLobby.setBackground(Color.WHITE);  
        this.add(pnlLobby);
        
        //List Games
        lstGames = new List();
        lstGames.setSize(250, 250);
        pnlLobby.add(lstGames, BorderLayout.WEST);
        
        //ButtonPanel
        pnlButtons = new JPanel();
        pnlButtons.setLayout(new BoxLayout(pnlButtons, BoxLayout.Y_AXIS));
        pnlLobby.add(pnlButtons, BorderLayout.EAST);

        //Button CreateGame / Cancel
        btnCreateGame = new JButton("Create Game");
        btnCreateGame.setSize(50, 250);
        btnCreateGame.addActionListener(new ActionListener(){ 
            @Override
            public void actionPerformed(ActionEvent e){
                if(waitMode) {
                    try {
                        player.disconnect();
                        broadcastServer.stopServer();
                    } catch(Exception ex) {
                        System.out.println(ex.getMessage());
                    }
                    SetGUIMode(true);
                }
                else {
                    HostGame();
                }
            }
        });
        pnlButtons.add(btnCreateGame, BorderLayout.EAST);
        
        //Button JoinGame
        btnJoinGame = new JButton("Join Game");
        btnJoinGame.setSize(50, 250);
        btnJoinGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int index = lstGames.getSelectedIndex();
                if(index<0)
                    return;
                try {
                    JoinGame(gameList.get(index).getAddress());
                } catch(Exception ex) {
                    System.out.println(ex.getMessage());
                }
            }
        });
        pnlButtons.add(btnJoinGame, BorderLayout.EAST);
        
        // Button Join Game Through IP
        btnJoinGameIP = new JButton("Join Game Through IP");
        btnJoinGameIP.setSize(50, 250);
        btnJoinGameIP.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String ip = JOptionPane.showInputDialog("Enter remote IP:");
                if (ip != null){                
                    try {
                        InetAddress adr = InetAddress.getByName(ip);
                        JoinGame(adr);
                    } catch (Exception ex) {
                        System.out.println(ex.getMessage());
                    }
                }
            }
        });
        pnlButtons.add(btnJoinGameIP, BorderLayout.EAST);

        txtIP = new JTextField("Your IP:\n"+ InetAddress.getLocalHost());
        txtIP.setSize(50,250);
        txtIP.setEditable(false);
        pnlButtons.add(txtIP, BorderLayout.SOUTH);
        //Set visibility
        setVisible(true);
    }
    
    public void HandleUDPResponse(InetAddress address, String gameName)
    {
        gameList.add(new GameInfo(address, gameName));
        UpdateGameList();
    }
    
    public void UpdateGameList() {
        lstGames.removeAll();
        for(GameInfo i:gameList) {
           lstGames.add(i.toString());
        }
        
    }
    
    public void StartGameServer(String gameName)
    {
        if(broadcastServer.running)
            broadcastServer.stopServer();
        
        broadcastServer.startServer(gameName);
    }
    
    public void JoinGame(InetAddress adr)
    {
        TCPClient player = new TCPClient();
        if(player.connect(adr)){
            StartGame(player);
        } else {
            JOptionPane.showMessageDialog(null, "Could not connect to: "+adr, "IP conflict", WIDTH);
            try{
                player.disconnect();
            }
            catch (Exception e) {
                System.out.println("Exception LobbyTCP player.disconnect(): "+e.getMessage());
            }
        }
    }
    
    public void StartGame(IClient oponent)
    {
        Game game = new Game(oponent);
        oponent.registerGame(game);
        PlayingWindow playingWindow = new PlayingWindow(game);
    }
    
    public void HostGame()
    {
        String gameName = JOptionPane.showInputDialog("New Game Name:");
        StartGameServer(gameName);
        player = new TCPClient();
        player.host(this);
        SetGUIMode(false);
    }
    
    public void SetGUIMode(boolean enable)
    {
        waitMode = !enable;
        if(waitMode)
            btnCreateGame.setText("Cancel");
        else
            btnCreateGame.setText("Create Game");
        btnJoinGame.setEnabled(enable);
        btnJoinGameIP.setEnabled(enable);
    }
}
