package battleship.Engine;

public class Field {

    public Field()
    {
        setFieldState(eFieldState.Empty);
    }
    
    private Ship ship;
    private eFieldState fieldState;
    private eFieldBattleState battleState;

    public eFieldState getFieldState() {
        return this.fieldState;
    }

    /**
     *
     * @param fieldState
     */
    public final void setFieldState(eFieldState fieldState) {
        this.fieldState = fieldState;
    }

    public eFieldBattleState getBattleState() {
        return this.battleState;
    }

    /**
     *
     * @param battleState
     */
    public void setBattleState(eFieldBattleState battleState) {
        this.battleState = battleState;
    }

    /**
     * @return the ship
     */
    public Ship getShip() {
        return ship;
    }

    /**
     * @param ship the ship to set
     */
    public final void setShip(Ship ship) {
        this.ship = ship;
        if(ship != null)
        {
            setFieldState(eFieldState.Filled);
            ship.setFieldReference(this);
        }
        else
            setFieldState(eFieldState.Empty);
    }
    
}