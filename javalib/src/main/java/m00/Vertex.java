/*

2008mar18

*/

package m00;

public class Vertex {
    private Region r;
    private float x, y; // centroide
    private float g; // nivel medio de cinza
    
    private int id; //indice no grafo

    public Vertex(Region r) {
        this.r = r;
        this.id = -1;
        this.update();
    }

    public Vertex(float x, float y) {
        this.r = null;
        this.id = -1;
        this.x = x;
        this.y = y;
        this.g = 0;
    }

    public void update() {
        this.calculaCentroide();
        this.calculaNivelMedio();
    }
    
    public void calculaCentroide() {
        this.x = (float)r.getSumX() / (float)r.getArea();
        this.y = (float)r.getSumY() / (float)r.getArea();
    }
    
    public void calculaNivelMedio() {
        this.g = (float)r.getSumG() / (float)r.getArea();
    }
    
    public void setId(int id) {
        this.id = id;
    }
    public int getId() {
        return this.id;
    }

    public Region getRegion() {
        return this.r;
    }

    public float getx() {
        return this.x;
    }
    public float gety() {
        return this.y;
    }
    
    public float getg() {
        return this.g;
    }
    
    public void setx(float x) {
        this.x = x;
    }
    public void sety(float y) {
        this.y = y;
    }

    public int getArea() {
        return this.r.getArea();
    }
}
