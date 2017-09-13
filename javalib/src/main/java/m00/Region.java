/*

2008mar18

*/

package m00;

import java.util.*;

public class Region {
    private LinkedList pixels;
    private int sumx, sumy, sumg, area;

    public Region() {
        this.pixels = new LinkedList();
        this.sumx = 0;
        this.sumy = 0;
        this.sumg = 0;
    }
    /*
    public Region(LinkedList pixels, int sumx, int sumy, int sumg) {
        this.pixels = pixels;
        this.sumx = sumx;
        this.sumy = sumy;
        this.sumg = sumg;
    }
    */
    public void addPixel(Pixel p) {
        this.pixels.addLast(p);
        this.sumx += p.getx();
        this.sumy += p.gety();
        this.sumg += p.getg();
    }
    
    public LinkedList getPixels() {
        return this.pixels;
    }
    
    public int getArea() {
        this.area = pixels.size();
        return this.area;
    }
    
    public int getSumX() {
        return this.sumx;
    }
    public int getSumY() {
        return this.sumy;
    }
    public int getSumG() {
        return this.sumg;
    }
}
