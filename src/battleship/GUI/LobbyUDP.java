package battleship.GUI;

import battleship.Engine.Game;
import battleship.Network.GameInfo;
import battleship.Network.IClient;
import battleship.Network.UDPClient;
import battleship.Network.UDPServer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.util.ArrayList;

/**
 *
 * @author Ozgur Aytar
 * @author Ozgur Yildiz
 */
public class LobbyUDP extends JFrame
{
    private JPanel pnlLobby;
    private JPanel pnlButtons;
    private List lstGames;
    private JButton btnCreateGame;
    private JButton btnJoinGame;
    private JButton btnRefresh;
    private ArrayList<GameInfo> gameList;
    private UDPServer broadcastServer;
    private UDPServer responseServer;
    private boolean waitMode;
    private UDPClient player;

    public LobbyUDP()
    {
        //Window Settings
        super("LobbyUDP");

        //Initialize Components
        InitializeComponents();

        // UDP servers
        broadcastServer = new UDPServer(this, true);
        responseServer = new UDPServer(this, false);
        gameList = new ArrayList<>();
    }

    private void InitializeComponents()
    {
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

        // Button Refresh List
        btnRefresh = new JButton("Refresh List using Broadcast");
        btnRefresh.setSize(50, 250);
        btnRefresh.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PerformBroadcast();
            }
        });
        pnlButtons.add(btnRefresh, BorderLayout.SOUTH);

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

    public void PerformBroadcast()
    {
        if(!responseServer.running) {
            gameList.clear();
            UpdateGameList();
        }
        responseServer.startServerBroadcast();
    }

    public void JoinGame(InetAddress adr)
    {
        UDPClient player = new UDPClient();
        if(player.connect(adr)){
            StartGame(player);
        } else {
            JOptionPane.showMessageDialog(null, "Could not connect to: "+adr, "IP conflict", WIDTH);
            try{
                player.disconnect();
            }
            catch (Exception e) {
                System.out.println("Exception LobbyUDP player.disconnect(): "+e.getMessage());
            }
        }
    }

    public void StartGame(IClient oponent)
    {
        Game game2 = new Game(oponent);
        oponent.registerGame(game2);
        PlayingWindow playingWindow = new PlayingWindow(game2);
    }

    public void HostGame()
    {
        String gameName = JOptionPane.showInputDialog("New Game Name:");
        StartGameServer(gameName);
        player = new UDPClient();
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
        btnRefresh.setEnabled(enable);

    }
}
