package battleship.GUI;

import battleship.Engine.Coordinates;
import battleship.Engine.Field;
import battleship.Engine.Game;
import battleship.Engine.IBattleField;
import battleship.Engine.eFieldBattleState;
import battleship.Engine.eFieldState;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JOptionPane;

/**
 *
 * @author Ozgur Aytar
 * @author Ozgur Yildiz
 */
public class GuiField extends JButton {

    IBattleField battleField;
    Game game;
    int positionX;
    int positionY;
    private eBattleFieldMode mode;
    private Field field;

    public GuiField(IBattleField battleField,Game game, Field field, int x, int y) {
        this.battleField = battleField;
        this.game = game;
        this.field = field;
        this.positionX = x;
        this.positionY = y;
        this.addActionListener(new FieldListener());
        this.mode = eBattleFieldMode.Design;

    }

    /**
     * @return the mode
     */
    public eBattleFieldMode getMode() {
        return mode;
    }

    /**
     * @param mode the mode to set
     */
    public void setMode(eBattleFieldMode mode) {
        this.mode = mode;
        UpdateLayout();
        

    }

    public void UpdateLayout() {
        // cannot be set anymore
        if (field.getBattleState() != null) {
            setBattleState();
            return;
        }
        // Opponent grid can only show
        if (mode == eBattleFieldMode.Displaying) {
            setEnabled(false);
            
        } 
        else {
            
            setEnabled(true);
            // designMode
            if(field.getFieldState() == eFieldState.Filled)
            {
                setBackground(Color.black);
            }
        }
    }

    private void setBattleState() {
       
        if (field.getBattleState() == eFieldBattleState.Hit) {
            setBackground(Color.red);
            setText("X");

        } else {
            setBackground(Color.blue);
            setText("~");
        }
        setEnabled(false);

    }
    
    private void showMessage(String message)
    {
        JOptionPane.showMessageDialog(this.getParent(),message);
    }

    class FieldListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                if (mode == eBattleFieldMode.Playable) {
                    game.sendAttackRequest(positionX, positionY);
                } else if (mode == eBattleFieldMode.Design) {
                    game.getShipToPlace().setStartPoint(new Coordinates(positionX, positionY));
                    game.getShipToPlace().setOrientation(game.getSelectedOrientation());
                    if (game.placeCurrentShip()) {
                        game.updateLayout();
                    }
                } else {
                    throw new UnsupportedOperationException("Field mustn't be enabled, worng mode");
                }
            } catch (NullPointerException e1) {
                System.out.println("You putted 5 boats");
            }
        }
    }
}
