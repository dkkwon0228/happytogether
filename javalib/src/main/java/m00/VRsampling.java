package m00;
/*
 * Voronoi.java
 *
 * Created on 25 de Fevereiro de 2007, 11:02
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
import java.awt.*;
import java.awt.image.*;
import java.util.*;

public class VRsampling {
    private BufferedImage inpImg;
    private BufferedImage oupImg;
    private BufferedImage pthImg;
    private Graphics2D gdrw;
    private double[][] mtGray;
    private int[][] mtLab;
    private LinkedList linp;
    private int w, h;
    private double[] xant, yant; 

    private Consts cte = new Consts();
    
    /** Creates a new instance of Voronoi */
    public VRsampling(BufferedImage inp, LinkedList l) {
        inpImg = inp;
        linp = l;
        xant = new double[linp.size()+1];
        yant = new double[linp.size()+1];
        Iterator pti = linp.iterator();
        while (pti.hasNext()) {
             Pixel pt = (Pixel)pti.next();
             xant[pt.getLabel()] = pt.getx();
             yant[pt.getLabel()] = pt.gety();
        }
        w = inpImg.getWidth();
        h = inpImg.getHeight();

        mtLab = new int[w][h];
        
        WritableRaster input = inpImg.copyData(null);
        mtGray = new double[w][h];
        for (int i=0; i<w; i++)
            for (int j=0; j<h; j++) {
                   mtLab[i][j] = 0;
                   mtGray[i][j] = (double)input.getSample(i, j, 0);
            }
    }
    
    public void voronoiLabels() {
	for (int i=0; i<w; i++)
            for (int j=0; j<h; j++) {
                double dist_min = w*w + h*h;
                Pixel nearest = null;
                Iterator pti = linp.iterator();
                while (pti.hasNext()) {
                    Pixel pt = (Pixel)pti.next();
                    double difx = i-pt.getx();
                    double dify = j-pt.gety();
                    double dist = difx*difx + dify*dify;
                    if (dist < dist_min) {
                        dist_min = dist;
                        nearest = pt;
                    }
                }
                if (nearest != null) mtLab[i][j] = nearest.getLabel();
            }        
    }

    public void qtreeLabels() {
	for (int i=0; i<w; i++)
            for (int j=0; j<h; j++) {
                double dist_min = w*w + h*h;
                Pixel nearest = null;
                Iterator pti = linp.iterator();
                while (pti.hasNext()) {
                    Pixel pt = (Pixel)pti.next();
                    double difx = Math.abs(i-pt.getx());
                    double dify = Math.abs(j-pt.gety());
                    double dist = Math.max(difx, dify);
                    if (dist < dist_min) {
                        dist_min = dist;
                        nearest = pt;
                    }
                }
                if (nearest != null) mtLab[i][j] = nearest.getLabel();
            }        
    }
    
    public void prepareDisplay() {
        pthImg = new BufferedImage(w, h, BufferedImage.TYPE_3BYTE_BGR);
        gdrw = pthImg.createGraphics();
        oupImg = new BufferedImage(w, h, BufferedImage.TYPE_3BYTE_BGR);
    }
    
    public void drawCellMeanGrayLevel() {
        double[] cmean = new double[linp.size()+1];
        double[] csize = new double[linp.size()+1];
        for (int k=0; k<=linp.size(); k++)
            cmean[k] = 0.0;
	for (int i=0; i<w; i++)
            for (int j=0; j<h; j++) {
                 cmean[mtLab[i][j]] += mtGray[i][j]; 
                 csize[mtLab[i][j]] += 1.0; 
            }
        for (int k=0; k<=linp.size(); k++)
            cmean[k] /= csize[k];

        WritableRaster output = oupImg.getRaster();
        for (int i=0; i < w; i++)
            for (int j=0; j < h; j++) {
                int c = (int)(Math.floor(cmean[mtLab[i][j]]));
                output.setSample(i, j, 0, c);
                output.setSample(i, j, 1, c);
                output.setSample(i, j, 2, c);
            }
    }

    public void drawOriginal() {
        WritableRaster output = oupImg.getRaster();
        WritableRaster input = inpImg.copyData(null);
        WritableRaster path = pthImg.copyData(null);
	for (int i=0; i<w-1; i++) {
            for (int j=0; j<h-1; j++) {
                output.setSample(i, j, 0, input.getSample(i, j, 0));
                output.setSample(i, j, 1, input.getSample(i, j, 0));
                output.setSample(i, j, 2, input.getSample(i, j, 0));
                if (!((path.getSample(i, j, 0) == 0) && (path.getSample(i, j, 1) == 0) && (path.getSample(i, j, 2) == 0))) {
                    output.setSample(i, j, 0, 255);
                    output.setSample(i, j, 1, 255);
                    output.setSample(i, j, 2, 0);
                }
            }
	}
    }
    
    public void drawCellBounds() {
        WritableRaster output = oupImg.getRaster();
	for (int i=0; i<w-1; i++) {
            for (int j=0; j<h-1; j++) {
                if ((mtLab[i][j] != mtLab[i][j+1]) || (mtLab[i][j] != mtLab[i+1][j])) {
                   output.setSample(i, j, 0, 0);
                   output.setSample(i, j, 1, 0);
                   output.setSample(i, j, 2, 255);
		}
            }
	}
    }
    
    public void drawCellPoints() {
        WritableRaster output = oupImg.getRaster();
        Iterator kit = linp.iterator();
        while (kit.hasNext()) {
            Pixel px = (Pixel)kit.next();
            if ((px.getx() >= 0) && (px.gety() >= 0) && (px.getx() < w) && (px.gety() < h)) {
                output.setSample(px.getx(), px.gety(), 0, 0);
                output.setSample(px.getx(), px.gety(), 1, 255);
                output.setSample(px.getx(), px.gety(), 2, 0);
            }
            for (int k=0; k<8; k++) {
                int cx = px.getx() + cte.viz8x[k];
                int cy = px.gety() + cte.viz8y[k];
                if ((cx >= 0) && (cy >= 0) && (cx < w) && (cy < h)) {
                    if ((px.getx() > 0) && (px.gety() > 0) && (px.getx() < w) && (px.gety() < h)) {
                        output.setSample(cx, cy, 0, 0);
                        output.setSample(cx, cy, 1, 255);
                        output.setSample(cx, cy, 2, 0);
                    }
                }
            }
        }
    }
    
    public BufferedImage showResults(boolean original, boolean mean, boolean edges, boolean points) {
        if (original) drawOriginal();
        if (mean) drawCellMeanGrayLevel();
        if (edges) drawCellBounds();
        if (points) drawCellPoints();
        return (oupImg);
    }

    public void evCenters() {
        double[] weight = new double[linp.size()+1];
        double[] xsum   = new double[linp.size()+1];
        double[] ysum   = new double[linp.size()+1];
        for (int k=0; k<linp.size(); k++) {
            weight[k] = 0.0; xsum[k] = 0.0; ysum[k] = 0.0;
        }
        for (int i=0; i < w; i++)
            for (int j=0; j < h; j++) {
                int lab = mtLab[i][j];
                double dinp = mtGray[i][j] + 0.001;
                if (lab > 0) {
                    xsum[lab] += dinp*i;
                    ysum[lab] += dinp*j;
                    weight[lab] += dinp;
                }
            }        
        Iterator pxit = linp.iterator();
        while (pxit.hasNext()) {
            Pixel px = (Pixel)pxit.next();
            int lab = px.getLabel();
            if (weight[lab] != 0) {
                xant[lab] = px.getx();
                yant[lab] = px.gety();
                px.setx((int)(xsum[lab]/weight[lab]));
                px.sety((int)(ysum[lab]/weight[lab]));
                gdrw.setColor(Color.YELLOW);
                gdrw.drawLine(px.getx(), px.gety(), (int)xant[lab], (int)yant[lab]);
            }
        }
    }
        
    public LinkedList getPoints() {
        return linp;
    }
    
    public LinkedList getMarkers() {
        LinkedList markers = new LinkedList();
        Iterator lit = linp.iterator();
        while (lit.hasNext()) {
            Pixel ppx = (Pixel)lit.next();
            markers.addLast(ppx);
            for (int k=0; k<4; k++) {
                int xx = ppx.getx() + cte.viz4x[k];
                int yy = ppx.gety() + cte.viz4y[k];
                if ((xx >= 0) && (yy >= 0) && (xx < w) && (yy < h)) {
                    Pixel npx = new Pixel(xx, yy, (int)mtGray[xx][yy]);
                    npx.setLabel(ppx.getLabel());
                    markers.addLast(npx);
                }
            }
        }
        return markers; 
    }
}
