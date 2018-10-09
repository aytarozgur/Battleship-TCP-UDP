/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package battleship.Engine;

/**
 *
 * @author Ozgur Aytar
 * @author Ozgur Yildiz
 */
public class Coordinates implements java.io.Serializable, Cloneable{


    public Coordinates(int x,int y) {
            setX(x);
            setY(y);
    }
    private int x;
    private int y;

    public final int getX() {
        return this.x;
    }

    /**
     *
     * @param x
     */
    public final void setX(int x) {
        if (x < 0) {
            return;
        }
        this.x = x;
    }

    public final int getY() {
        return this.y;
    }

    /**
     *
     * @param y
     */
    public final void setY(int y) {
        if (y < 0) {
            return;
        }
        this.y = y;
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj)
        {
            return true;
        }
        if(obj != null)
        {
            if(obj instanceof Coordinates)
            {
                Coordinates otherCoordinates = (Coordinates)obj;
                if(otherCoordinates.getX() == this.getX() && otherCoordinates.getY() == this.getY())
                {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.getX()^this.getY();
    }

    @Override
    public String toString() {
        return String.format("X:%d Y: %d", this.x, this.y);
    }

     public Coordinates clone() throws CloneNotSupportedException {
        return (Coordinates)super.clone();
    }
    
    
    
    
    
  
}
