package battleship.Engine;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

public class BattleField implements IBattleField {

    private int width = 20;
    private int heigth = 1;
    private HashSet<Ship> ships;
    private Field[][] fields;
    private int hitCount;

    public BattleField(int width , int heigth) {
        hitCount = 0;
        if (width > 5) {
            this.width = width;
        }
        if (heigth > 5) {
            this.width = heigth;
        }

        ships = new HashSet<>();

        initFields();

    }

    private void initFields() {
        fields = new Field[getWidth()][getHeigth()];
        for (int i = 0; i < getWidth(); i++) {
            for (int j = 0; j < getHeigth(); j++) {
                Field field = new Field();
                fields[i][j] = field;
            }
        }

    }

    @Override
    public boolean setShip(Ship shipToPlace) {

        boolean canPlace = canPlaceShip(shipToPlace);

        // add ship
        if (canPlace) {
            // if already in list update 
            Iterator<Ship> shipIterator = ships.iterator();
            while (shipIterator.hasNext()) {
                Ship old = shipIterator.next();
                if (old.equals(shipToPlace)) {
                    ArrayList<Field> fields = old.getFields();
                    for (Field f : fields) {
                        f.setShip(null);
                    }
                    // remove list
                    old.getFields().clear();
                    shipIterator.remove();
                    break;
                }
            }
            try {
                ships.add(shipToPlace);
                buildFinalFieldList();
            }
            catch (Exception e1) {
                System.out.println("You're at wrong place");
            }
        }
        return canPlace;
    }

    private boolean canPlaceShip(Ship shipToPlace) {
        boolean canPlace = true;
        //1. check if its still in the field
        canPlace = shipToPlace.getStartPoint().getX() + shipToPlace.getSize() <= width;

        if (canPlace == false) {
            return canPlace;
        }
        // second check if there is no other ship
        for (Ship otherShip : ships) {
            if (otherShip.Cross(shipToPlace)) {
                canPlace = false;
                break;
            }
        }
        return canPlace;

    }

    /**
     * Hits a field from battlefield
     *
     * @param x
     * @param y
     * @return if hit or not
     */
    @Override
    public boolean hitField(int x, int y) {

        Field tolook = getFields()[y][x];
        if(tolook.getBattleState() == null)
        {
            if (tolook.getFieldState() == eFieldState.Empty) {
                tolook.setBattleState(eFieldBattleState.Missed);
                return false;
            } else {
                tolook.setBattleState(eFieldBattleState.Hit);
                setHitCount();
                return true;
            }
        }
        else
        {
        throw new UnknownError("Error" + x + " " + y);
        }
    }

    /**
     * sets the battle state of a Field
     *
     * @param hit
     * @param x
     * @param y
     */
    @Override
    public void setFieldState(boolean hit, int x, int y) {
        Field tolook = getFields()[y][x];
        if (hit) {
            tolook.setBattleState(eFieldBattleState.Hit);
        } else {
            tolook.setBattleState(eFieldBattleState.Missed);
        }
    }

    /**
     *
     */
    private void setHitCount() {
        hitCount++;
    }

    @Override
    public int getHitCount() {

        int procentualHit = 0;
        int totalToHit = 0;
        for (Ship s : ships) {
            totalToHit += s.getSize();
        }
        if (totalToHit == 0) {
            return procentualHit;
        }
        procentualHit = hitCount / totalToHit* 100;

        return procentualHit;
    }

    @Override
    public void applyShipPositions() {
        buildFinalFieldList();
    }

    private void buildFinalFieldList() {

        // set field values foreach ship which was placed
        try {
            for (Ship s : ships) {

                // Just call it once
                ArrayList<Coordinates> coordinates = s.getCoordinates();
                for (Coordinates c : coordinates) {
                    getFields()[c.getY()][c.getX()].setShip(s);
                }
            }
        }
        catch (Exception e) {
            System.out.println("You're at wrong place");
        }
    }

    /**
     * @return the width
     */
    @Override
    public int getWidth() {
        return width;
    }

    /**
     * @return the heigth
     */
    @Override
    public int getHeigth() {
        return heigth;
    }

    /**
     * @return the fields
     */
    @Override
    public Field[][] getFields() {
        return fields;
    }
    


}