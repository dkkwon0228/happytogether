/*

2008mar18

.this class enables the user to draw the traces over the input image
.'each color represents a different label'

*/

package m00;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;

////////////////////////////////////////////////////////////////////////////
public class PencilTool {

    private ImgPanel drawPanel;
    private ColorPanel colorMenu;

    private BufferedImage strk; // strokes
    
    private int  eraserLength = 10;
    private int penLength = 2;//5;
    private int mousex = 0;
    private int mousey = 0;
    private int prevx = 0;
    private int prevy = 0;
    private boolean initialPen = true;
    private Color corTransp = Color.white;

    Graphics2D g2d;
    WritableRaster inpstrk, oupstrk;
    int w, h;
    private Ferramentas fe = new Ferramentas();

    public PencilTool(ImgPanel p, ColorPanel c) {
        this.drawPanel = p;
        this.strk = p.getStrokeImage();
        this.colorMenu = c;
        this.g2d  = (Graphics2D) this.strk.getGraphics();
        this.inpstrk = this.strk.copyData(null);
        this.oupstrk = this.strk.getAlphaRaster();
        this.w = this.strk.getWidth();
        this.h = this.strk.getHeight();
    }
    
    public BufferedImage getStrokeImage() {
        return this.strk;
    }
    
    public void setPenLength(int len) {
        this.penLength = len;
    }
    public void setEraserLength(int len) {
        this.eraserLength = len;
    }

    // draws traces
    public void penOperation(MouseEvent e){
        Graphics2D g  = g2d;
        g.setColor(this.colorMenu.getCurrentColor());
        g.setStroke(new BasicStroke(this.penLength));
        if (initialPen) {
            setGraphicalDefaults(e);
            initialPen = false;
            g.drawLine(prevx,prevy,mousex,mousey);
        }
        if (mouseHasMoved(e)) {
            mousex = e.getX();
            mousey = e.getY();
            g.drawLine(prevx,prevy,mousex,mousey);
            prevx = mousex;
            prevy = mousey;
        }
        this.drawPanel.repaint();
    }

    // erases traces
    public void eraserOperation(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        this.drawPanel.setSelectPosition(x,y,eraserLength*2,eraserLength*2);
        //this.g2d.setColor(new Color(255,255,255,0));
        this.g2d.setColor(this.corTransp);
        this.g2d.fillRect(x-eraserLength, y-eraserLength,eraserLength*2,eraserLength*2);
        fe.corTransparente(this.inpstrk, this.oupstrk, this.corTransp, this.w, this.h);
        this.drawPanel.repaint();
    }

    public boolean mouseHasMoved(MouseEvent e) {
        return (mousex != e.getX() || mousey != e.getY());
    }

    public void setGraphicalDefaults(MouseEvent e) {
        mousex   = e.getX();
        mousey   = e.getY();
        prevx    = e.getX();
        prevy    = e.getY();
    }

    public void releasedPen() {
        initialPen = true;
    }

    public void releasedEraser() {
        this.drawPanel.setSelectPosition(-1000,-100,-100,-100);  //desabilita selection
        this.drawPanel.repaint();
//fe.saveImage(this.drawPanel.getStrokeImage(),"zzzz.png");
    }
}

