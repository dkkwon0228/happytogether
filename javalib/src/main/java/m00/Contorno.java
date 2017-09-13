/*

2008out03

1. contorno
2. componentes
3. pontos

java -Xmx1024m Contorno ../picsShapes/A/a01.bmp



*/

package m00;

//import m00.*;
import m01.*;


import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.*;
import java.util.*;


public class Contorno {

    private Ferramentas1 fe = new Ferramentas1();
    private Consts cts = new Consts();
    
    private int totc = 0; // total de componentes conexas
    int numPontos = 100; //50;//100;  // numero de pontos no contorno para cada componente
    int porcentagem;
    
    //lista dos pontos
    private LinkedList ptx;
    private LinkedList pty;
    
///////////////////////////////////////////////////////////////////
    //lista dos pontos para cada componente (dados auxiliares)
    private LinkedList px = new LinkedList();
    private LinkedList py = new LinkedList();
    
    public int getNumComponents() {
        return this.totc;
    }
    public LinkedList getPointsX() {
        return this.ptx;
    }
    public LinkedList getPointsY() {
        return this.pty;
    }
/*
    public Contorno(String fn) {
        BufferedImage im = fe.loadImage(fn);
        init(im);
    }
    public Contorno(BufferedImage im) {
        init(im);
    }
    public Contorno(String fn, int numPontos) {
        this.numPontos = numPontos;
        BufferedImage im = fe.loadImage(fn);
        init(im);
    }
    public Contorno(BufferedImage im, int numPontos) {
        this.numPontos = numPontos;
        init(im);
    }
    */
    public Contorno(BufferedImage im, int porcentagem, String msg) {
        this.porcentagem = porcentagem;
        //init2(im);
        init3(im);
    }
    //2009out13: teste COIL20
    public Contorno(BufferedImage im, int porcentagem, boolean[][] b) {
        this.porcentagem = porcentagem;
        int w = im.getWidth();
        int h = im.getHeight();
        BufferedImage tmp = new BufferedImage(w, h, BufferedImage.TYPE_3BYTE_BGR); 
        BufferedImage bimg = fe.calcContornoRegioes(tmp, b, Color.white); // fundo preto, obj branco
//fe.saveImage(bimg,"zzzz.png");
//System.exit(1);
        this.init3a(bimg);
    }
    public void printPoints() {
        Iterator itx = this.ptx.iterator();
        Iterator ity = this.pty.iterator();
        while (itx.hasNext()) {
            int x = ((Integer)itx.next()).intValue();
            int y = ((Integer)ity.next()).intValue();
            System.out.println("x = "+x+"    y = "+y);
        }
    }
    //2009mar05: igualmente espacados: componentes separadas
    public void init3(BufferedImage im) {
        int w = im.getWidth();
        int h = im.getHeight();

        Conversion opConv = new Conversion(im, true);
        BufferedImage imRGB = opConv.imgConverted();
        boolean[][] b = fe.calcContorno(imRGB);
        BufferedImage tmp = new BufferedImage(w, h, BufferedImage.TYPE_3BYTE_BGR); 
        BufferedImage bimg = fe.calcContornoRegioes(tmp, b, Color.white); // fundo preto, obj branco
        this.init3a(bimg);
    }
    
    public void init3a(BufferedImage bimg) {
        //componentes
        Conversion opConv = new Conversion(bimg, false);
        BufferedImage imgray = opConv.imgConverted();
        BufferedImage im = imgray;
        int w = im.getWidth();
        int h = im.getHeight();
        int[][] c = rotulaComponentes(im);

px.removeFirst();
py.removeFirst();
px.addLast(-1);
py.addLast(-1);
        //componentes
        this.ptx = new LinkedList();
        this.pty = new LinkedList();
        Iterator itx = px.iterator();
        Iterator ity = py.iterator();
LinkedList tmpx = new LinkedList();
LinkedList tmpy = new LinkedList();
        while (itx.hasNext()) {
            int x = ((Integer)itx.next()).intValue();
            int y = ((Integer)ity.next()).intValue();
            if (x < 0) {
//System.out.println("********novo componente");
this.samplePointsPercentage(tmpx, tmpy, w, h);
this.ptx.addLast(-1);
this.pty.addLast(-1);
//zera listas auxiliares (para representar cada componente)
tmpx.clear();
tmpy.clear();
if (!itx.hasNext())
    break;
                x = ((Integer)itx.next()).intValue();
                y = ((Integer)ity.next()).intValue();
            }
tmpx.addLast(x);
tmpy.addLast(y);
        }
        
    }
    private void samplePointsPercentage(LinkedList lx, LinkedList ly, int w, int h) {
        //vetor de pontos
        int max = lx.size();
if (max > 0) {
        int[] vx = new int[max]; //////////////////////////////////////////////////////
        int[] vy = new int[max]; //////////////////////////////////////////////////////
        for (int j = 0; j < max; j++) {
            vx[j] = -1;
            vy[j] = -1;
        }
        Iterator itx = lx.iterator();
        Iterator ity = ly.iterator();
        int cont = 0;
        while (itx.hasNext()) {
            int x = ((Integer)itx.next()).intValue();
            int y = ((Integer)ity.next()).intValue();
            vx[cont] = x;
            vy[cont] = y;
            cont++;
        }

        //pontos
        boolean[][] inseriu = new boolean[w][h];
        for (int x = 0; x < w; x++)
            for (int y = 0; y < h; y++)
                inseriu[x][y] = false;
        //'acende' somente alguns pixels
        int div = 100/this.porcentagem;
        //float divf = (float)max/(float)this.numPontos;
        //System.out.println("divf="+divf);
        //int div = (int)divf;
        //System.out.println("div="+div);
        //if ((divf-(float)div)>0.5f)
        //    div++;
        int t = max / div;
        //System.out.println("Total: "+t);
        for (int k = 0; k < t; k++) {
            int j = k * div;
            if (j >= max)
                j = max-1;
            int x = vx[j];
            int y = vy[j];
            if (!inseriu[x][y]) {
                inseriu[x][y] = true;
                this.ptx.addLast(x);
                this.pty.addLast(y);
            }
        }
}
    }
    /*
    //igualmente espacados
    public void init2(BufferedImage im) {
        int w = im.getWidth();
        int h = im.getHeight();

        Conversion opConv = new Conversion(im, true);
        BufferedImage imRGB = opConv.imgConverted();
        boolean[][] b = fe.calcContorno(imRGB);
        BufferedImage tmp = new BufferedImage(w, h, BufferedImage.TYPE_3BYTE_BGR); 
        BufferedImage bimg = fe.calcContornoRegioes(tmp, b, Color.white);

        //componentes
        opConv = new Conversion(bimg, false);
        BufferedImage imgray = opConv.imgConverted();
        im = imgray;
        int[][] c = rotulaComponentes(im);

        //componentes
        this.ptx = new LinkedList();
        this.pty = new LinkedList();
        Iterator itx = px.iterator();
        Iterator ity = py.iterator();
        while (itx.hasNext()) {
            int x = ((Integer)itx.next()).intValue();
            int y = ((Integer)ity.next()).intValue();
            if (x < 0) {
//System.out.println("********novo componente: "+ptx.size());
                x = ((Integer)itx.next()).intValue();
                y = ((Integer)ity.next()).intValue();
            }
            ptx.addLast(x);
            pty.addLast(y);
        }
//System.out.println("*****: "+ptx.size());
        
        //vetor de pontos
        int max = ptx.size();
        int[] vx = new int[max]; //////////////////////////////////////////////////////
        int[] vy = new int[max]; //////////////////////////////////////////////////////
        for (int j = 0; j < max; j++) {
            vx[j] = -1;
            vy[j] = -1;
        }
        itx = ptx.iterator();
        ity = pty.iterator();
        int cont = 0;
        while (itx.hasNext()) {
            int x = ((Integer)itx.next()).intValue();
            int y = ((Integer)ity.next()).intValue();
            vx[cont] = x;
            vy[cont] = y;
            cont++;
        }
        
        //pontos
        boolean[][] inseriu = new boolean[w][h];
        for (int x = 0; x < w; x++)
            for (int y = 0; y < h; y++)
                inseriu[x][y] = false;
        //'acende' somente alguns pixels
        int div = 100/this.porcentagem;
        this.ptx = new LinkedList();
        this.pty = new LinkedList();
        //float divf = (float)max/(float)this.numPontos;
        //System.out.println("divf="+divf);
        //int div = (int)divf;
        //System.out.println("div="+div);
        //if ((divf-(float)div)>0.5f)
        //    div++;
        int t = max / div;
        //System.out.println("Total: "+t);
        for (int k = 0; k < t; k++) {
            int j = k * div;
            if (j >= max)
                j = max-1;
            int x = vx[j];
            int y = vy[j];
            if (!inseriu[x][y]) {
                inseriu[x][y] = true;
                ptx.addLast(x);
                pty.addLast(y);
            }
        }
    }
    
    public void init(BufferedImage im) {
        int w = im.getWidth();
        int h = im.getHeight();

        //contorno (uso mesma funcao do mapa de rotulos, por isso imagem colorida)
        Conversion opConv = new Conversion(im, true);
        BufferedImage imRGB = opConv.imgConverted();
//fe.saveImage(imRGB,"zzz-imRGB.png");
        boolean[][] b = fe.calcContorno(imRGB);
        BufferedImage tmp = new BufferedImage(w, h, BufferedImage.TYPE_3BYTE_BGR); 
        BufferedImage bimg = fe.calcContornoRegioes(tmp, b, Color.white);
        //fe.saveImage(bimg,"zzz0.png");

        //componentes
        opConv = new Conversion(bimg, false);
        BufferedImage imgray = opConv.imgConverted();
        im = imgray;
        int[][] c = rotulaComponentes(im);

        /*
        BufferedImage res = fe.copyImage(im);
        //limpa imagem
        Graphics2D g2d = res.createGraphics();
        g2d.setColor(Color.black);
        g2d.fillRect(0,0,w,h);
        WritableRaster oup = res.getRaster();
        for (int x = 0; x < w; x++)
            for (int y = 0; y < h; y++)
                oup.setSample(x,y,0,(c[x][y] * 50)%255);
        //fe.saveImage(res, "zzz1.png");
        *
        
        //componentes
        this.ptx = new LinkedList();
        this.pty = new LinkedList();
        Iterator itx = px.iterator();
        Iterator ity = py.iterator();
        while (itx.hasNext()) {
            int x = ((Integer)itx.next()).intValue();
            int y = ((Integer)ity.next()).intValue();
//System.out.println(x+"  "+y);
            if (x < 0) {
            /*
                cont++;
                ptx[cont] = new LinkedList();
                pty[cont] = new LinkedList();
                *
                x = ((Integer)itx.next()).intValue();
                y = ((Integer)ity.next()).intValue();
//System.out.println(x+"  "+y);
            }
            ptx.addLast(x);
            pty.addLast(y);
        }
        
        //vetor de pontos
        int max = ptx.size();
        int[] vx = new int[max]; //////////////////////////////////////////////////////
        int[] vy = new int[max]; //////////////////////////////////////////////////////
        for (int j = 0; j < max; j++) {
            vx[j] = -1;
            vy[j] = -1;
        }
        itx = ptx.iterator();
        ity = pty.iterator();
        int cont = 0;
        while (itx.hasNext()) {
            int x = ((Integer)itx.next()).intValue();
            int y = ((Integer)ity.next()).intValue();
            vx[cont] = x;
            vy[cont] = y;
            cont++;
        }
        
        //pontos
        boolean[][] inseriu = new boolean[w][h];
        for (int x = 0; x < w; x++)
            for (int y = 0; y < h; y++)
                inseriu[x][y] = false;
        //'acende' somente alguns pixels
        //int div = 100/porcentagem;
        this.ptx = new LinkedList();
        this.pty = new LinkedList();
        float divf = (float)max/(float)this.numPontos;
        //System.out.println("divf="+divf);
        int div = (int)divf;
        //System.out.println("div="+div);
        if ((divf-(float)div)>0.5f)
            div++;
        int t = max / div;
        //System.out.println("Total: "+t);
        for (int k = 0; k < t; k++) {
            int j = k * div;
            if (j >= max)
                j = max-1;
            int x = vx[j];
            int y = vy[j];
            if (!inseriu[x][y]) {
                inseriu[x][y] = true;
                ptx.addLast(x);
                pty.addLast(y);
            }
            //System.out.println(i+" "+x+" "+y);
        }
        /*
        g2d.setColor(Color.black);
        g2d.fillRect(0,0,w,h);
        itx = ptx.iterator();
        ity = pty.iterator();
        while (itx.hasNext()) {
            int x = ((Integer)itx.next()).intValue();
            int y = ((Integer)ity.next()).intValue();
            oup.setSample(x,y,0,255);
        }
        String fout = String.format("zzz-%02dpoints.png", this.numPontos);
        //fe.saveImage(res, fout);
        *
    }
*/
    public int[][] rotulaComponentes(BufferedImage im) {
        Conversion opConv = new Conversion(im, false); //grayscale
        BufferedImage imgray = opConv.imgConverted();
        int w = im.getWidth();
        int h = im.getHeight();
        WritableRaster inp = imgray.copyData(null);
        int[][] mtLab = new int[w][h];
        for (int x=0; x<w; x++)
            for (int y=0; y<h; y++) {
                int gray = inp.getSample(x,y,0);
                if (gray > 200)
                    mtLab[x][y] = 1;
                else
                    mtLab[x][y] = 0;
            }
        return rotulaComponentes(mtLab);
    }
    public int[][] rotulaComponentes(int[][] mtLab) {
        int w = mtLab.length;
        int h = mtLab[0].length;
//System.out.println("w="+w+"  h="+h);
        int[][] rotulos = new int[w][h];
        
        boolean[][] inseriu = new boolean[w][h];
        
        for (int x=0; x<w; x++)
            for (int y=0; y<h; y++) {
                rotulos[x][y] = -1;
                inseriu[x][y] = false;
            }
        int r = -1;
        LinkedList lp = new LinkedList();
        for (int x=0; x<w; x++)
            for (int y=0; y<h; y++) {
                if (rotulos[x][y] == -1) {
                    r++;
                    lp.addFirst(new Coords2d(x,y));
                    
                    if (mtLab[x][y] > 0) {
                        px.addLast(-1);
                        py.addLast(-1);
                        px.addLast(x);
                        py.addLast(y);
                        inseriu[x][y] = true;
                    }
                 }
                while(!lp.isEmpty()) {
                    Coords2d p = (Coords2d)lp.removeFirst();
                    int x3 = p.getx(), y3 = p.gety();
                    rotulos[x3][y3] = r;
                    for (int k=0; k<8; k++) {
                        int x2 = x3 + cts.viz8x[k];
                        int y2 = y3 + cts.viz8y[k];
                        if (x2 >= 0 && x2 < w && y2 >= 0 && y2 < h && rotulos[x2][y2] == -1) {
//System.out.println("x3="+x3+"  y3="+y3+"       x2="+x2+"  y2="+y2);
                            if (mtLab[x3][y3]==mtLab[x2][y2]) {
                                lp.addFirst(new Coords2d(x2,y2));

                                if (mtLab[x][y] > 0 && !inseriu[x2][y2]) {
                                    px.addLast(x2);
                                    py.addLast(y2);
                                    inseriu[x2][y2] = true;
                                }
                            }
                        }
                    }
                }
            }
        this.totc = r;
//System.out.println("Total componentes: "+this.totc);
        for (int x=0; x<w; x++)
            for (int y=0; y<h; y++)
                if (rotulos[x][y] == -1) {
                    System.out.println("Error: comps; rotulos[x][y] == -1");
                    System.exit(1);
                }
        return rotulos;
    }
/*
    public static void main(String args[]) {
        if (args.length == 1) {
            String fn = args[0];
            Contorno a = new Contorno(fn);
        }
        else
            System.out.println("Use: java Contorno input.png");
    }
*/
}

