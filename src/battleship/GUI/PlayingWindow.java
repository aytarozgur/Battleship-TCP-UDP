package battleship.GUI;

import battleship.Engine.*;
import battleship.Network.Message;
import battleship.Network.eGameState;
import battleship.Network.eMessageType;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import javax.swing.JRadioButton;

import javax.swing.JTextField;
import javax.swing.JLabel;
/**
 *
 * @author Ozgur Aytar
 * @author Ozgur Yildiz
 */
public class PlayingWindow extends JFrame implements IGameGUI {

    JPanel leftPanel;
    JPanel rigthPanel;
    JPanel centerPanel;
    JPanel bottomPanel;
    BattleFieldGrid playerGrid;
    BattleFieldGrid oponentGrid;
    JComboBox cmbAvailableShips;
    JTextField chatInput;
    List chatOutput;
    Game game;
    JLabel txtVertical;

    public PlayingWindow(Game game) {
        super("BattleShip");
        this.game = game;
        this.game.registerGUI(this);
        setLayout(new BorderLayout());
        setSize(600, 600);
        addWindowListener(new WindowListernerExt());
        buildPanels();
        gameSetup();
        setVisible(true);
    }

    private void buildPanels() {
        leftPanel = new JPanel();
        leftPanel.setLayout(new GridLayout(3, 1));
        rigthPanel = new JPanel(new GridLayout(0, 1));
        centerPanel = new JPanel(new GridLayout(0, 2, 5, 15));
        bottomPanel = new JPanel();
        bottomPanel.setLayout(new BorderLayout());
        add(leftPanel, BorderLayout.WEST);
        add(rigthPanel, BorderLayout.EAST);
        add(centerPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void gameSetup() {
        playerGrid = new BattleFieldGrid(game, true);
        JButton buttonApplyShips = new JButton("Apply ship settings");
        buttonApplyShips.addActionListener(
                new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JButton buttonApplyShips = (JButton) e.getSource();
                buttonApplyShips.setVisible(false);

                // to do check if all ships are placed
                // player is ready
                game.sendReadyRequest();

            }
        });

        cmbAvailableShips = new JComboBox();
        this.game.setShipToPlace(new Ship(eShipType.boat1));
        cmbAvailableShips.addItem(new Ship(eShipType.boat1));
        cmbAvailableShips.addItem(new Ship(eShipType.boat2));
        cmbAvailableShips.addItem(new Ship(eShipType.boat3));
        cmbAvailableShips.addItem(new Ship(eShipType.boat4));
        cmbAvailableShips.addItem(new Ship(eShipType.boat5));

        cmbAvailableShips.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cmbAvailableShips_SelectedChanged(e);
            }
        });
        leftPanel.add(cmbAvailableShips);

        txtVertical = new JLabel("<html>You can put exactly 5 boat <br/> and only vertical orientation</html>");
        leftPanel.add(txtVertical);
        JButton buttonChat = new JButton("Send");
        buttonChat.addActionListener(
                new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                game.sendChatMessage(chatInput.getText());
                chatInput.setText("");
            }
        });
        chatOutput = new List();
        chatInput = new JTextField();
        chatInput.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent evt) {
                int key = evt.getKeyCode();
                if (key == KeyEvent.VK_ENTER) {
                    game.sendChatMessage(chatInput.getText());
                    chatInput.setText("");
                }
            }
        });
        oponentGrid = new BattleFieldGrid(game, false);
        centerPanel.add(playerGrid);
        centerPanel.add(oponentGrid);
        //left
        JPanel bottomLeft = new JPanel();
        bottomLeft.setLayout(new BoxLayout(bottomLeft, BoxLayout.Y_AXIS));

        bottomLeft.add(buttonApplyShips);

        JPanel bottomCenter = new JPanel();
        bottomCenter.setLayout(new BoxLayout(bottomCenter, BoxLayout.Y_AXIS));
        bottomCenter.add(chatOutput);
        bottomCenter.add(chatInput);

        JPanel bottomRigth = new JPanel();
        bottomRigth.setLayout(new BoxLayout(bottomRigth, BoxLayout.Y_AXIS));
        bottomRigth.add(buttonChat);

        bottomPanel.add(bottomLeft, BorderLayout.WEST);
        bottomPanel.add(bottomCenter, BorderLayout.CENTER);
        bottomPanel.add(bottomRigth, BorderLayout.EAST);

    }

    private void cmbAvailableShips_SelectedChanged(ActionEvent e) {
        Ship selectedShip = (Ship) cmbAvailableShips.getSelectedItem();
        this.game.setShipToPlace(selectedShip);
        playerGrid.UpdateLayout();
    }

    /**
     * this mehtod is used to set the mode of the gui
     *
     * @param state
     */
    @Override
    public void updateState(eBattleFieldMode state) {

        switch (state) {
            case Design:
                playerGrid.setMode(eBattleFieldMode.Playable);
                oponentGrid.setMode(eBattleFieldMode.Displaying);
                break;
            // this means its players turn
            case Playable:

                playerGrid.setMode(eBattleFieldMode.Displaying);
                oponentGrid.setMode(eBattleFieldMode.Playable);
                break;
            // this means player has to wait
            case Displaying:

                playerGrid.setMode(eBattleFieldMode.Displaying);
                oponentGrid.setMode(eBattleFieldMode.Displaying);
                break;
        }
        // update gui
        playerGrid.UpdateLayout();
        oponentGrid.UpdateLayout();
    }

    /**
     *
     * @param state
     */
    @Override
    public void updateGameState(eGameState state) {
        switch (state) {
            case abort:
                JOptionPane.showMessageDialog(this.getParent(), "Oponent left the current game");
                System.exit(0);
                break;
            case won:
                ImageIcon winIcon = new ImageIcon(PlayingWindow.class.getResource("win.gif"));
                JOptionPane.showMessageDialog(this.getParent(), "", "congratulation!!!", JOptionPane.INFORMATION_MESSAGE, winIcon);
                System.exit(0);
                break;
            case lost:
                ImageIcon lostIcon = new ImageIcon(PlayingWindow.class.getResource("lost.gif"));
                JOptionPane.showMessageDialog(this.getParent(), "", "LOOSER", JOptionPane.INFORMATION_MESSAGE, lostIcon);
                System.exit(0);
                break;
        }
    }

    @Override
    public void addChatMessage(String message) {
        chatOutput.add(message);
        chatOutput.select(chatOutput.getItemCount() - 1);

    }

    @Override
    public void updateLayout() {
        playerGrid.UpdateLayout();
        oponentGrid.UpdateLayout();
        if (cmbAvailableShips.getItemCount() > 0) {
            cmbAvailableShips.removeItem(game.getShipToPlace());
        }
    }

    class WindowListernerExt extends WindowAdapter {

        @Override
        public void windowClosing(WindowEvent e) {
            Message abortMessage = new Message();
            abortMessage.setMessageType(eMessageType.gameState);
            abortMessage.setDataContainer(eGameState.abort);
            // Oponent is null at the moment
            game.getOpenent().sendMessage(abortMessage);

            //System.exit(0);
        }
    }
    public eOrientation getOrientation() {
        return eOrientation.Vertical;
    }
}
