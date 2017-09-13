package m00;

import java.awt.*;
import java.awt.image.*;

import m01.*;

public class Components {
    BufferedImage imRGB;
    int totc;
    int w, h;
    int[][] comps;
    Color[] cores;

    Consts cts = new Consts();
    Ferramentas1 fe = new Ferramentas1();

    public Components(String fn) { 
        BufferedImage im = Ferramentas.loadImage(fn);
        this.init(im);
    }
    public Components(BufferedImage im) { 
        this.init(im);
    }
    public void init(BufferedImage im) { 
        Conversion opConv = new Conversion(im, true);
        this.imRGB = opConv.imgConverted();
        this.w = this.imRGB.getWidth();
        this.h = this.imRGB.getHeight();

        int[] tmp = new int[1];
        this.comps = fe.rotulaComponentes(this.imRGB, tmp);
        this.totc = tmp[0];
        //System.out.println("Total componentes: "+this.totc);
        this.cores = this.coresComponentes(this.imRGB, comps);

        this.agrupaMesmaCor();
        this.cores = this.coresComponentes(this.imRGB, this.comps);

        //comps = filtroConexo(comps);
        
        //String fout = "zzzC-filtroconexo.png";
        //fe.saveImage(this.calcBufferedImage(comps, cores), fout);
    }
    private void agrupaMesmaCor() {
        int[] newlabel = new int[this.totc];
        for (int i = 0; i < this.totc; i++) 
            newlabel[i] = -1;
        int cont2 = 0;
        for (int i = 0; i < this.totc; i++) {
            Color cor = this.cores[i];
            int nl = newlabel[i];
            if (nl == -1) {
                nl = cont2++;
                for (int j = 0; j < this.totc; j++) {
                    if (fe.sameRGB(this.cores[j],cor))
                        newlabel[j] = nl;
                }
            }
        }
        this.totc = cont2;
        for (int x=0; x<w; x++)
            for (int y=0; y<h; y++) {
            /*
                if (this.comps[x][y] < 0) {
                }
                else
                */
                this.comps[x][y] = newlabel[this.comps[x][y]];
            }
    }

    
    public Components(int[][] mtLab) {
        int[] tmp = new int[1];
        this.comps = fe.rotulaComponentes(mtLab, tmp);
        this.totc = tmp[0];
    }
    public int getTotalComponents() {
        return this.totc;
    }
    public int[][] getComponents() {
        return this.comps;
    }
    /*
    2009jun11: movi para Ferramentas1.java
    public int[][] rotulaComponentes(BufferedImage imRGB) {
        WritableRaster inp = imRGB.copyData(null);
        int w = imRGB.getWidth();
        int h = imRGB.getHeight();
        int[][] mtLab = new int[w][h];
        for (int x=0; x<w; x++)
            for (int y=0; y<h; y++) {
                int r = inp.getSample(x,y,0);
                int g = inp.getSample(x,y,1);
                int b = inp.getSample(x,y,2);
                int cor = r + g*256 + b*256*256;
                mtLab[x][y] = cor;
            }
        return rotulaComponentes(mtLab);
    }
    
    public int[][] rotulaComponentes(int[][] mtLab) {
        int[] tmp = new int[1];
        int[][] comps = fe.regionGrowing(mtLab, tmp);
        this.totc = tmp[0];
        return comps;
    }
*/
    public void simplifica() {
        this.comps = fe.filtroConexo(this.comps);
    }
    public BufferedImage getBufferedImage() {
        return this.calcBufferedImage(this.comps, this.cores);
    }
    /*
    2009jun11: movi para Ferramentas1.java
    public int[][] filtroConexo(int[][] m) {
        LinkedList se = new LinkedList();
        
        //linha vertical
        se.add(new Coords2d(0,2));
        se.add(new Coords2d(0,1));
        se.add(new Coords2d(0,-1));
        se.add(new Coords2d(0,-2));
        m = fe.supinf(m, se);
        m = fe.infsup(m, se);

        //linha horizontal
        se.clear();
        se.add(new Coords2d(2,0));
        se.add(new Coords2d(1,0));
        se.add(new Coords2d(-1,0));
        se.add(new Coords2d(-2,0));
        m = fe.supinf(m, se);
        m = fe.infsup(m, se);
        
        return m;
    }
*/
    public Color[] coresComponentes(BufferedImage imRGB, int[][] comps) {
        return fe.coresComponentes(imRGB, comps, this.totc);
        /* 2009jun11: movi para Ferramentas1.java
        Color[] cores = new Color[this.totc];
        for (int i = 0; i < this.totc; i++)
            cores[i] = null;
        WritableRaster inp = imRGB.copyData(null);
        for (int x=0; x<this.w; x++)
            for (int y=0; y<this.h; y++) {
                int r = inp.getSample(x,y,0);
                int g = inp.getSample(x,y,1);
                int b = inp.getSample(x,y,2);
                cores[comps[x][y]] = new Color(r,g,b);
                //cores[comps[x][y]] = new Color(16*comps[x][y],16*comps[x][y],16*comps[x][y]);
            }
        for (int i = 0; i < this.totc; i++)
            if (cores[i] == null) {
                System.out.println("Error: cores; cores[i] == null");
                System.exit(1);
            }
        return cores;
        */
    }

    public BufferedImage calcBufferedImage(int[][] comps, Color[] cores) {
        return fe.calcCompsColorMap(comps, cores);
    /* 2009jun11: movi para Ferramentas1.java
        int w = comps.length;
        int h = comps[0].length;
        BufferedImage res = new BufferedImage(w, h, BufferedImage.TYPE_3BYTE_BGR);
        WritableRaster oup = res.getRaster();
        for (int x = 0; x < w; x++)
            for (int y = 0; y < h; y++) {
                int i = comps[x][y];
                oup.setSample(x, y, 0, cores[i].getRed());
                oup.setSample(x, y, 1, cores[i].getGreen());
                oup.setSample(x, y, 2, cores[i].getBlue());
            }
        return res;
        */
    }

     public void saveSegmPartsEdgeBlur(BufferedImage immRGB, Color cor, String arqs) {
         fe.saveSegmParts(immRGB, cor, arqs, this.comps, this.totc, true);
     }
     public void saveSegmParts(BufferedImage immRGB, Color cor, String arqs) {
         fe.saveSegmParts(immRGB, cor, arqs, this.comps, this.totc, false);
    /* 2009jun11: movi para Ferramentas1.java
        boolean[][] contorno = fe.calcContorno(this.imRGB);
        BufferedImage res = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);//TYPE_3BYTE_BGR);
        Graphics2D g2d = res.createGraphics();
        g2d.setColor(cor);
        WritableRaster inp = immRGB.copyData(null);
        WritableRaster oup = res.getRaster();
        WritableRaster oupA = res.getAlphaRaster();
        for (int i = 0; i < this.totc; i++) {
            String fout = String.format(arqs, i);
            g2d.fillRect(0,0,w,h);
            fe.corTransparente(res, cor);
            for (int x=0; x<w; x++)
            for (int y=0; y<h; y++)
                if (this.comps[x][y] == i && !contorno[x][y]) {
                    oup.setSample(x,y,0,inp.getSample(x,y,0));
                    oup.setSample(x,y,1,inp.getSample(x,y,1));
                    oup.setSample(x,y,2,inp.getSample(x,y,2));
                    oupA.setSample(x,y,0,255);
                }
            Ferramentas.saveImage(res, fout);
        }
        */
    }


}
