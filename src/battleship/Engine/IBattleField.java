package battleship.Engine;

public interface IBattleField {

    /**
     *
     * @param shipToPlace
     */
    boolean setShip(Ship shipToPlace);

    /**
     * Request receive
     *
     * @param x
     * @param y
     */
    boolean hitField(int x, int y);

    /**
     * Show response from opponent
     *
     * @param x
     * @param y
     */
    void setFieldState(boolean hit, int x, int y);

    /**
     * gets percentage of the totally hit ships
     *
     * @return
     */
    int getHitCount();

    int getWidth();

    int getHeigth();

    Field[][] getFields();

    void applyShipPositions();
 
}