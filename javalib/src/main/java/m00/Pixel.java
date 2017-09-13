/*

2008mar18

*/

package m00;

public class Pixel {
    private int x, y, g;
    private int label;   //2008set23: usado para obter marcadores (QTsampling.java)
    private float f;  //idem
    
    public Pixel(int x, int y, int g) {
        this.x = x;
        this.y = y;
        this.g = g;
    }
    
    public int getx() {
        return this.x;
    }
    public int gety() {
        return this.y;
    }
    public int getg() {
        return this.g;
    }
    public void setg(int g) {
        this.g = g;
    }

    ///////////////////////
    public void setLabel(int label) {
        this.label = label;
    }
    public int getLabel() {
        return label;
    }

    ////////////////////// VRsampling.java
    public void setx(int x) {
        this.x = x;
    }
    public void sety(int y) {
        this.y = y;
    }
    
    //////////////////////
    public void setfloat(float f) {
        this.f = f;
    }
    public float getfloat() {
        return this.f;
    }
    
}
