/*
 2009jul20
 */


package m15;

import m00.*;
import m01.*;

import java.awt.*;
import java.awt.image.*;

////////////////////////////////////////////////////////////////////////////
public class Ferramentas15 extends Ferramentas1 {

    public BufferedImage calcMapaRotulos(int[][] rpixels, int w, int h, ModelGraph15 model) {
        BufferedImage mapaRotulos = new BufferedImage(w, h, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D g2d = mapaRotulos.createGraphics();
        g2d.setColor(Color.white);
        g2d.fillRect(0,0,w,h);
        
        for (int x=0; x<w; x++)
            for (int y=0; y<h; y++)
                if (rpixels[x][y] > -1)
                    mapaRotulos.setRGB(x, y, model.getColor(rpixels[x][y]).getRGB());
        return mapaRotulos;
    }

    public void saveResultsSegm(InputGraph15 GI, ModelGraph15 GM, BufferedImage map) {
        this.saveResultsSegm(GI, GM, map, Color.green);
    }
    public void saveResultsSegm(InputGraph15 GI, ModelGraph15 GM, BufferedImage map, Color cor) {
        BufferedImage im = GI.getImRGB();
        BufferedImage strk = GM.getStrokeImage();
        Ferramentas.saveImage(strk,"zzz-stroke.png");
        //Ferramentas.saveImage(map, "zzz-map.png"); // map of labels
BufferedImage mapa = this.copyImageRGB(map);
this.mudaCorMapa(mapa);
Ferramentas.saveImage(mapa, "zzz-map.png"); // map of labels

        boolean[][] contorno = this.calcContorno(map);
cor = Color.white;
        BufferedImage tmp = this.calcContornoRegioes(GI.getImRGB(),contorno,cor,1);
        //this.saveImage(tmp, "zzz-contouroverim.png");// contours over RGB image
        BufferedImage strokes = this.copyImageARGB(GM.getStrokeImage());
        this.mudaCorStrokesEngrossa(strokes);
        this.saveImage(this.sobrepoe(tmp,strokes), "zzz-strkovercontouroverim.png");
        tmp = this.calcContornoRegioes(GI.getImWshed(),contorno,Color.green);
        this.saveImage(tmp, "zzz-contouroverwshed.png"); // contours over wshed regions
        
//2010ago03: teste MSRM (Ning et al. PR2010)
        this.saveImage(this.engrossaRGB(GI.getWshedPartition(),Color.black,2), "zzz-wshedPartition.png"); // contours over wshed regions
        
        Components c = new Components(map);
        c.saveSegmParts(GI.getImRGB(), Color.white, "zzz-parts-%03d.png");
        //System.out.println("components, edge blur");
        c.saveSegmPartsEdgeBlur(GI.getImRGB(), Color.white, "zzz-partsb-%03d.png");
        /*
        //contorno grosso sobre imagem de entrada
        rescontorno a = new rescontorno(map, GI.getImRGB());
        tmp = a.getResult();
        Ferramentas.saveImage(tmp, "zzz-contourover-thick.png");
        this.saveImage(this.sobrepoe(GI.getImRGB(),GM.getStrokeImage()), "zzz-strkoverim.png");
        */
        BufferedImage res = this.montaImagemSobreposta(im, mapa, 0.6f);
        res = this.montaImagemSobreposta(res, strokes);
        this.saveImage(res,"zzz-result.png");

    }

    private void mudaCorStrokesEngrossa(BufferedImage imARGB) {
        WritableRaster inp = imARGB.getRaster();
        WritableRaster inpa = imARGB.getAlphaRaster();
        int h = imARGB.getHeight();
        int w = imARGB.getWidth();
        Color fg1 = Color.red;
        Color bg1 = Color.blue;
        Color fg2 = fg1;//Color.green;//blue;
        Color bg2 = bg1;//Color.red;//green;
        //Color fg2 = Color.green;
        //Color bg2 = Color.red;
        int fg1r = fg1.getRed();
        int fg1g = fg1.getGreen();
        int fg1b = fg1.getBlue();
        int fg2r = fg2.getRed();
        int fg2g = fg2.getGreen();
        int fg2b = fg2.getBlue();
        int bg1r = bg1.getRed();
        int bg1g = bg1.getGreen();
        int bg1b = bg1.getBlue();
        int bg2r = bg2.getRed();
        int bg2g = bg2.getGreen();
        int bg2b = bg2.getBlue();
        int[][] fgrot = new int[h][w];
        int[][] bgrot = new int[h][w];
        for (int y = 0; y < h; y++)
            for (int x = 0; x < w; x++) {
                fgrot[y][x] = 0;
                bgrot[y][x] = 0;
                int r = inp.getSample(x,y,0);
                int g = inp.getSample(x,y,1);
                int b = inp.getSample(x,y,2);
                if (this.sameRGB(r,g,b, fg1r, fg1g, fg1b))
                    fgrot[y][x] = 1;
                else
                if (this.sameRGB(r,g,b, bg1r, bg1g, bg1b))
                    bgrot[y][x] = 1;
            }
//this.saveImage(this.imGray(fgrot),"zzzz-fg.png");
//this.saveImage(this.imGray(bgrot),"zzzz-bg.png");
        fgrot=this.dilatacaoCruz(fgrot);
        bgrot=this.dilatacaoCruz(bgrot);
//this.saveImage(this.imGray(fgrot),"zzzz-fg2.png");
//this.saveImage(this.imGray(bgrot),"zzzz-bg2.png");
        for (int y = 0; y < h; y++)
            for (int x = 0; x < w; x++) {
                if (fgrot[y][x] > 0) {
                    inp.setSample(x,y,0,fg2r);
                    inp.setSample(x,y,1,fg2g);
                    inp.setSample(x,y,2,fg2b);
                    inpa.setSample(x,y,0,255);
                }
                else
                    if (bgrot[y][x] > 0) {
                    inp.setSample(x,y,0,bg2r);
                    inp.setSample(x,y,1,bg2g);
                    inp.setSample(x,y,2,bg2b);
                    inpa.setSample(x,y,0,255);
                }
            }
    }

    public void mudaCorMapa(BufferedImage imRGB) {
        WritableRaster inp = imRGB.getRaster();
        int h = imRGB.getHeight();
        int w = imRGB.getWidth();
        Color fg1 = Color.red;
        Color bg1 = Color.blue;
        Color fg2 = Color.white;
        Color bg2 = Color.black;
        int fg1r = fg1.getRed();
        int fg1g = fg1.getGreen();
        int fg1b = fg1.getBlue();
        int fg2r = fg2.getRed();
        int fg2g = fg2.getGreen();
        int fg2b = fg2.getBlue();
        int bg1r = bg1.getRed();
        int bg1g = bg1.getGreen();
        int bg1b = bg1.getBlue();
        int bg2r = bg2.getRed();
        int bg2g = bg2.getGreen();
        int bg2b = bg2.getBlue();
       for (int y = 0; y < h; y++)
            for (int x = 0; x < w; x++) {
                int r = inp.getSample(x,y,0);
                int g = inp.getSample(x,y,1);
                int b = inp.getSample(x,y,2);
                if (this.sameRGB(r,g,b, fg1r, fg1g, fg1b)) {
                    inp.setSample(x,y,0,fg2r);
                    inp.setSample(x,y,1,fg2g);
                    inp.setSample(x,y,2,fg2b);
                }
                else
                    if (this.sameRGB(r,g,b, bg1r, bg1g, bg1b)) {
                    inp.setSample(x,y,0,bg2r);
                    inp.setSample(x,y,1,bg2g);
                    inp.setSample(x,y,2,bg2b);
                    }
            }
    }
    /*
    private void mudaCorStroke(BufferedImage imRGB) {
        WritableRaster inp = imRGB.getRaster();
        int h = imRGB.getHeight();
        int w = imRGB.getWidth();
        Color fg1 = Color.red;
        Color bg1 = Color.blue;
Color fg1a = Color.green;
        Color fg2 = Color.blue;
Color fg2a = Color.red;
        Color bg2 = Color.green;
        int fg1r = fg1.getRed();
        int fg1g = fg1.getGreen();
        int fg1b = fg1.getBlue();
        int fg2r = fg2.getRed();
        int fg2g = fg2.getGreen();
        int fg2b = fg2.getBlue();
int fg2ar = fg2a.getRed();
int fg2ag = fg2a.getGreen();
int fg2ab = fg2a.getBlue();
        int bg1r = bg1.getRed();
        int bg1g = bg1.getGreen();
        int bg1b = bg1.getBlue();
        int bg2r = bg2.getRed();
        int bg2g = bg2.getGreen();
        int bg2b = bg2.getBlue();
int fg1ar = fg1a.getRed();
int fg1ag = fg1a.getGreen();
int fg1ab = fg1a.getBlue();
       for (int y = 0; y < h; y++)
            for (int x = 0; x < w; x++) {
                int r = inp.getSample(x,y,0);
                int g = inp.getSample(x,y,1);
                int b = inp.getSample(x,y,2);
                if (this.sameRGB(r,g,b, fg1r, fg1g, fg1b)) {
                    inp.setSample(x,y,0,fg2r);
                    inp.setSample(x,y,1,fg2g);
                    inp.setSample(x,y,2,fg2b);
                }
                else
                    if (this.sameRGB(r,g,b, bg1r, bg1g, bg1b)) {
                    inp.setSample(x,y,0,bg2r);
                    inp.setSample(x,y,1,bg2g);
                    inp.setSample(x,y,2,bg2b);
                    }
                else
                    if (this.sameRGB(r,g,b, fg1ar, fg1ag, fg1ab)) {
                    inp.setSample(x,y,0,fg2ar);
                    inp.setSample(x,y,1,fg2ag);
                    inp.setSample(x,y,2,fg2ab);
                    }
            }
    }
    */
}
