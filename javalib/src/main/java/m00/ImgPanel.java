/*

2008mar18

image panel to display the input image and strokes

*/

package m00;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.*;
import java.awt.image.*;

////////////////////////////////////////////////////////////////////////////
public class ImgPanel extends JPanel{   

    public BufferedImage image, strk, dest, contorno;
    private Consts cts = new Consts();
    private Color corTransp;
    private Ferramentas fe = new Ferramentas();

    private Graphics2D destG;
    WritableRaster inpstrk, oupstrk;
    
    int sx=-1, sy=-1, sw, sh; //select
    
    boolean hideStrokes = false;

    public ImgPanel(String fn) {   
        super();
        BufferedImage im = fe.loadImage(fn);
        this.inicia(im);
    }
    public ImgPanel(BufferedImage im) {   
        super();
        this.inicia(im);
    }
    public void inicia(BufferedImage im) {   
        this.corTransp = cts.corTransp;
        this.image = im;
    
        dest = new BufferedImage(im.getWidth(), im.getHeight(), BufferedImage.TYPE_INT_ARGB);  
        this.destG = dest.createGraphics();
        this.strk = new BufferedImage(im.getWidth(), im.getHeight(), BufferedImage.TYPE_INT_ARGB);  
        limpaImagem(this.strk);
        //corTransparente(this.strk);

        //2008jun06: resultado da segmentacao (contorno)
        this.contorno = new BufferedImage(im.getWidth(), im.getHeight(), BufferedImage.TYPE_INT_ARGB);  
        limpaImagem(this.contorno);
        //corTransparente(this.contorno);
    
        this.inpstrk = this.strk.copyData(null);
        this.oupstrk = this.strk.getAlphaRaster();
    }   

    public void setHideStrokes(boolean flag) {
        this.hideStrokes = flag;
    }

    public void limpaImagem(BufferedImage im) {
        Graphics2D g2d = im.createGraphics();
        g2d.setColor(corTransp);
        g2d.fillRect(0,0,im.getWidth(),im.getHeight());
        this.corTransparente(im);
    }
    public void limpaImagemStroke() {
        Graphics2D g2d = this.strk.createGraphics();
        g2d.setColor(corTransp);
        g2d.fillRect(0,0,this.strk.getWidth(),this.strk.getHeight());
        this.corTransparente(this.strk);
    }
    public void limpaImagemContorno() {
        Graphics2D g2d = this.contorno.createGraphics();
        g2d.setColor(corTransp);
        g2d.fillRect(0,0,this.contorno.getWidth(),this.contorno.getHeight());
        this.corTransparente(this.contorno);
    }
    public void corTransparente(BufferedImage im) {
        fe.corTransparente(im, corTransp);
    }
    public void setSelectPosition(int sx, int sy, int sw, int sh) {
        this.sx = sx-sw/2;
        this.sy = sy-sh/2;
        this.sw = sw;
        this.sh = sh;
    }
    private void montaImagemSobreposta() {
        destG.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC, 1.0f));
        destG.drawImage(this.image, 0, 0, null);
        
        if (!this.hideStrokes) {
            destG.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
            corTransparente(this.strk);
            destG.drawImage(this.strk, 0, 0, null);
        }

        destG.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
        destG.drawImage(this.contorno, 0, 0, null);
        
        if (this.sx > -1000) {
            //quadradoGraphics.clearRect(0,0,dim.width,dim.width);
            destG.setColor(Color.red);
            //quadradoGraphics.drawString("Bad Double-buffered",10,10);
            // draw the rect at the current mouse position
            // to the offscreen image
            destG.drawRect(sx,sy,sw,sh);
        }
    }

    public void paintComponent(Graphics g) {
        //super.paintComponent(g); 
        montaImagemSobreposta(); //dest
        //g.drawImage(dest, 0, 0, null);   
        g.drawImage(dest, 0, 0, this);   
    }

    public static BufferedImage loadImage(String ref) {   
        BufferedImage bimg = null;   
        try {   
            bimg = ImageIO.read(new File(ref));   
        } catch (Exception e) {   
            e.printStackTrace();   
        }   
        return bimg;   
    }  
    
    public BufferedImage getBufferedImage() {
        return image;
    }
    public BufferedImage getStrokeImage() {
        return strk; //para penciltool
    }
    public void setBufferedImage(BufferedImage im) {
        this.image = im;
    }
    public void setStrokeImage(BufferedImage stroke) {
        this.strk = stroke;
        corTransparente(this.strk);
        this.inpstrk = this.strk.copyData(null);
        this.oupstrk = this.strk.getAlphaRaster();
    }
    public void setContorno(BufferedImage contorno) {
        this.contorno = contorno;
    }
    public BufferedImage getContorno() {
        return this.contorno;
    }
}  


///////////////////////////////////////////////////////////////////////////////////////////////////
