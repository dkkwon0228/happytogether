/*

2008aset22: inclui grade...

2008jun06: segm.java usa reducao sem blur
           segm2.java usa blur

2008jun04: 'ReducedWshed' -> reduz o numero de regioes (sem deformar as bordas, ou seja, sem borramento)


2008mar11

Grafo (de entrada) sobre watershed da imagem de entrada.
Cada regiao do watershed eh representada por um vertice do grafo.
Nao ha construcao de arestas.

Cada vertice guarda como atributo os niveis medio de cinza de cada regiao
para cada um dos 3 canais RGB.


2008mar18

Apos chamar o construtor, chamar a criacao dos vertices:
buildVertices()

Esta funcao ficou separada pois a criacao do vertices sao diferentes para input e model.

*/

package m01;

import m00.*;

import java.awt.image.*;
import java.awt.*;
///////////////////////////////////////////////////////////////////////////////
public class InputGraph1 extends Graph {
    protected ReducedWshed opWshed;
    protected BufferedImage im;
    protected WritableRaster inp;
    public float[][] rgb;
    public int w, h;
    protected int totr; //total de regioes do watershed
    
    protected Consts cts = new Consts();

    Ferramentas1 fe = new Ferramentas1();

    public InputGraph1() {
        super();
    }
    
    //segm.java: segmentacao exige bordas mais apuradas
    public InputGraph1(String fn, int minsize) {
        super();
        BufferedImage im = Ferramentas.loadImage(fn);
        init(im, minsize);        
    }
    public InputGraph1(BufferedImage im, int minsize) {
        super();
        init(im, minsize);
    }
    public void init(BufferedImage im, int minsize) {
System.out.println("Running heuristic to reduce the number of regions. Please wait...");
        this.opWshed = new ReducedWshed(im, minsize);
        this.totr = this.opWshed.getTotalRegions(); //total de regioes
        commonInit(im);
    }
    public void commonInit(BufferedImage im) {
        //se imagem grayscale, replica para tres canais RGB
        Conversion opConv = new Conversion(im, true);
        BufferedImage imRGB = opConv.imgConverted();
        this.im = imRGB;
        this.inp = this.im.copyData(null);
        this.w = im.getWidth();
        this.h = im.getHeight();
    }
    
    //segm2.java: blur para eliminacao de ruido, 
    //pois contarah mais a info estrutural para reaproveitar modelo
    public InputGraph1(String fn, int blur, String msg) {
        super();
        BufferedImage im = Ferramentas.loadImage(fn);
        init2(im, blur, msg);
    }
    public InputGraph1(BufferedImage im, int blur, String msg) {
        super();
        init2(im, blur, msg);
    }
    /*
    public void init2(BufferedImage im, int blur, String msg) {
        this.opWshed = new ReducedWshed(im, blur, msg);
        this.totr = this.opWshed.getTotalRegions(); //total de regioes
        commonInit(im);
    }
    */
    public void init2(BufferedImage im, int blur, String msg) {
        //BufferedImage im2 = im;
        if (msg == "grade") {
            //Conversion opConv = new Conversion(im, true);
            //BufferedImage imRGB = opConv.imgConverted();
            fe.grade(im);
        }
        this.opWshed = new ReducedWshed(im, blur, msg); //2008set23: trata wshed com markers
        this.totr = this.opWshed.getTotalRegions(); //total de regioes

        commonInit(im);
    }
/////////////////////////////////////////////////////
    public ReducedWshed getOpWshed() {
        return this.opWshed;
    }
    
    public float[] getRGB(int id) {
        return this.rgb[id];
    }
    
    public BufferedImage getImRGB() {
        return this.im;
    }

//2010ago03: teste MSRM (Ning et al. PR2010)
    public BufferedImage getWshedPartition() {
        return this.opWshed.calcWshedImagePartition();
    }
    
    public BufferedImage getImWshed() {
        return this.opWshed.getImWshed();
    }

    public BufferedImage getImWshed(Color cor, BufferedImage imRGB_) {
        return this.opWshed.getImWshed(cor, imRGB_);
    }
    
    public int getTotalRegions() {
        return this.totr;
    }
    
    //constantes de normalizacao para custo em GraphMatch2.java
    public float getDmax() {
        float dmax = (float)Ferramentas.euclideanDistance(0.0,0.0,this.w,this.h);//sqrt(w^2 + h^2)
        return dmax;
    }
    public float getDpix() {
        float dpix = (float)Ferramentas.euclideanDistance(0.0,0.0,0.0,255.0,255.0,255.0); //=255*sqrt(3)
        return dpix;
    }

    //inclui todas as regioes no grafo
    public void buildVertices() {
        boolean[] incluir = new boolean[this.totr];
        for (int i = 0; i < this.totr; i++)
            incluir[i] = true;
        this.buildVertices(incluir);
    }
    public void buildVertices(boolean[] incluir) {
        int[] idr = new int[this.totr];
        int[][] mtLab = this.opWshed.getRefLabels();
        Region[] refr = new Region[this.totr];
        int tot = 0;
        for (int i = 0; i < this.totr; i++)
            if (incluir[i] == true) {
                Region r = new Region();
                refr[i] = r;
                idr[i] = tot;
                tot++;
            }
            else {
                refr[i] = null;
                idr[i] = -1;
            }
        this.rgb = new float[tot][3];
        for (int i = 0; i < tot; i++)
            for (int c = 0; c < 3; c++)
                this.rgb[i][c] = 0.0f;
        for (int x=0; x<this.w; x++)
            for (int y=0; y<this.h; y++) {
                int i = mtLab[x][y];
                if (i >= 0 && incluir[i] == true) {
                    Pixel p = new Pixel(x, y, 0); //usarei nivel medio de cada canal RGB
                    Region r = refr[i];
                    r.addPixel(p);
                    for (int c = 0; c < 3; c++) {
                        float g = (float)inp.getSample(x,y,c);
                        rgb[idr[i]][c] += g;
                    }
                }
            }
        for (int i = 0; i < this.totr; i++)
            if (incluir[i] == true) {
                Region r = refr[i];
                this.addVertex(r); //jah atualiza centroides...
            }
        this.finalizaConstrucaoVertices(); // agora posso atualizar atributos de cores RGB...
        Vertex[] refv = this.getVertices();
        for (int i = 0; i < this.totr; i++)
            if (incluir[i] == true) {
                Vertex v = refv[idr[i]];
                Region r = refr[i];
                for (int c = 0; c < 3; c++) {
                    int totpixels = r.getArea();
                    this.rgb[idr[i]][c] /= totpixels;
                }
                //2008jul01: converte rgb para cielab
                //this.rgb[idr[i]] = fe.rgb2cielab(this.rgb[idr[i]]);
            }
        //System.out.println("Grafo com cielab.");
    }
    
    public void print() {
        Vertex[] refv = this.getVertices();
        int totv = this.getNumVertices();
        System.out.println("\ntotv = "+totv+" id x y r g b Area");
        for (int i=0; i<totv; i++) {
            Vertex v = refv[i];
            float r = rgb[i][0];
            float g = rgb[i][1];
            float b = rgb[i][2];
            System.out.println(v.getId()+" "+v.getx()+" "+v.gety()+" "+r+" "+g+" "+b+" "+v.getArea());
        }
        int tote = this.getNumEdges();
        System.out.println("\ntote = "+tote);
        int[][]adj = this.getAdjacencyList();
        for (int i = 0; i < totv; i++) {
            int k = 0;
            System.out.print(i+": ");
            while(k < totv && adj[i][k] > -1) {
                int j = adj[i][k];
                System.out.print(j+" ");
                k++;
            }
            System.out.println();
        }
        System.out.println("\n");
    }
    
    // cria uma aresta entre regioes adjacentes
    public void buildEdges() {
        this.buildEdges4viz();
        //this.buildEdgesDelaunayTriangulation();
        //this.buildEdges8viz();
        this.finalizaConstrucaoArestas();
    }
    public void buildEdges4viz() {
        int totv = this.getNumVertices();
        boolean[][] adj = new boolean[totv][totv];
        for (int i = 0; i < totv; i++)
            for (int j = 0; j < totv; j++)
                adj[i][j] = false;
        int[][] mtLab = this.opWshed.getRefLabels();
        for (int x = 0; x < w; x++)
            for (int y = 0; y < h; y++) {
                int i = mtLab[x][y]; // indice da regiao ou vertice
                for (int k=0; k<4; k++) {
                    int x2 = x + cts.viz4x[k];
                    int y2 = y + cts.viz4y[k];
                    //2008mai30: corrigido (ficava 1 vertice isolado! AlexFreire)
                    if (x2 >= 0 && x2 < w && y2 >= 0 && y2 < h) {
                        int j = mtLab[x2][y2];
                        adj[i][j] = true;
                        adj[j][i] = true;
                    }
                }
            }
        Vertex[] refv = this.getVertices();
        for (int i = 0; i < totv; i++)
            for (int j = 0; j < totv; j++)
                if (adj[i][j] && i != j)
                    addEdge(refv[i], refv[j]);
        //this.finalizaConstrucaoArestas();
    }
    public void buildEdges8viz() {
        int totv = this.getNumVertices();
        boolean[][] adj = new boolean[totv][totv];
        for (int i = 0; i < totv; i++)
            for (int j = 0; j < totv; j++)
                adj[i][j] = false;
        int[][] mtLab = this.opWshed.getRefLabels();
        for (int x = 0; x < w; x++)
            for (int y = 0; y < h; y++) {
                int i = mtLab[x][y]; // indice da regiao ou vertice
                for (int k=0; k<8; k++) {
                    int x2 = x + cts.viz8x[k];
                    int y2 = y + cts.viz8y[k];
                    //2008mai30: corrigido (ficava 1 vertice isolado! AlexFreire)
                    if (x2 >= 0 && x2 < w && y2 >= 0 && y2 < h) {
                        int j = mtLab[x2][y2];
                        adj[i][j] = true;
                        adj[j][i] = true;
                    }
                }
            }
        Vertex[] refv = this.getVertices();
        for (int i = 0; i < totv; i++)
            for (int j = 0; j < totv; j++)
                if (adj[i][j] && i != j)
                    addEdge(refv[i], refv[j]);
        //this.finalizaConstrucaoArestas();
    }

    public BufferedImage showEdges(BufferedImage im) {
        BufferedImage res = im;
        Graphics2D g2d = res.createGraphics();
        Vertex[] refv = this.getVertices();
        int totv = this.getNumVertices();
        boolean[][] adj = this.getAdjMatrix();
        for (int i=0; i<totv-1; i++) {
            Vertex v1 = refv[i];
            for (int j=i+1; j<totv; j++) 
            if (adj[i][j] == true) {
                Vertex v2 = refv[j];
                g2d.setColor(Color.blue);
                g2d.drawLine((int)v1.getx(), (int)v1.gety(), (int)v2.getx(), (int)v2.gety());
            }
        }
        return res;
    }
    public BufferedImage showEdgesOverWshed() {
        return this.showEdges(this.getImWshed());
    }

    public static void main( String args[] ) {
        if (args.length == 2) {
            String f1 = args[0];
            int nb = Integer.parseInt(args[1]);
            InputGraph1 a = new InputGraph1(f1, nb);
            //inclui todos as regioes
            a.buildVertices();
            a.buildEdges();
            a.print();
        }
        else
            System.out.println("Use: java -Xmx1024m InputGraph1 image.jpg blur");
    }
}

