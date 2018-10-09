
package battleship.GUI;

import battleship.Engine.Game;
import battleship.Engine.IBattleField;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 *
 * @author Ozgur Aytar
 * @author Ozgur Yildiz
 */
public class BattleFieldGrid extends JPanel {
    
    Game game;
    IBattleField field;
    private eBattleFieldMode mode;
    private boolean isPlayerGrid;
    ArrayList<GuiField> displayedFields;

    public BattleFieldGrid(Game game, boolean isPlayerGrid) {
        this.game = game;
        this.isPlayerGrid = isPlayerGrid;
        
        displayedFields = new ArrayList<>();
        if(isPlayerGrid)
            field = game.getPlayerGrid();
        else
            field = game.getOpenentGrid();
        setLayout(new GridLayout(field.getWidth(), field.getHeigth()));
        buildField();
    }
    
    private void buildField() {
        for (int i = 0; i < field.getWidth(); i++) {
            for (int j = 0; j < field.getHeigth(); j++) {
                GuiField displayField = new GuiField(this.field, game, field.getFields()[i][j], j, i);
                add(displayField);
                displayedFields.add(displayField);
            }
        }
        if (isPlayerGrid) {
            setMode(eBattleFieldMode.Design);
        } else {
            setMode(eBattleFieldMode.Displaying);
    
        }
    }
    
    public void UpdateLayout()
    {
        for(GuiField button :displayedFields)
        {
            button.UpdateLayout();
        }
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
        
        for (GuiField g : displayedFields) {
            g.setMode(mode);
        }
    }
}
