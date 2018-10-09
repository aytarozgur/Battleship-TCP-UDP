package battleship.GUI;

import battleship.Engine.eOrientation;
import battleship.Network.eGameState;

/**
 *
 * @author Ozgur Aytar
 * @author Ozgur Yildiz
 */
public interface IGameGUI {
    void updateState(eBattleFieldMode state);
    
    void updateGameState(eGameState state);
    void addChatMessage(String message);
    void updateLayout();
    eOrientation getOrientation();
}
