/*

2008mar18

Apos chamar o construtor, chamar a criacao dos vertices e das arestas:
buildModelVerticesAndEdges()

Esta funcao ficou separada pois a criacao do vertices sao diferentes para input e model.

*/

package m01;

import m00.*;

import java.awt.image.*;
import java.awt.*;


///////////////////////////////////////////////////////////////////////////////
public class ModelGraph1 extends InputGraph1 {
    private BufferedImage strk;
    private WritableRaster inp2;
    private int totlabels;
    private boolean[] incluir;
    private Color[] cor;
    private Consts cts = new Consts();
    
    public ModelGraph1() {
        super();
    }

    public BufferedImage getStrokeImage() {
        return this.strk;
    }
    
    //reduz regioes
    public ModelGraph1(String fnimg, String fnstrk, int minsize) {
        super(fnimg, minsize);
        this.calcStrokes(fnstrk);
    }
    public ModelGraph1(BufferedImage img, BufferedImage strk, int minsize) {
    //public ModelGraph1(String fnimg, BufferedImage strk, int minsize) {
        //super(fnimg, minsize);
        super(img, minsize);
        this.calcStrokes(strk);
    }

    //blur
    public ModelGraph1(String fnimg, String fnstrk, int blur, String msg) {
        super(fnimg, blur, msg);
        this.calcStrokes(fnstrk);
    }
    public ModelGraph1(BufferedImage img, BufferedImage strk, int blur, String msg) {
    //public ModelGraph1(String fnimg, BufferedImage strk, int blur, String msg) {
        //super(fnimg, blur, msg);
        super(img, blur, msg);
        this.calcStrokes(strk);
    }

    public ModelGraph1(BufferedImage imRGB, BufferedImage strk, ReducedWshed opWshed) {
        super();
        this.opWshed = opWshed;
        this.totr = this.opWshed.getTotalRegions(); //total de regioes
        
        this.im = imRGB;
        this.inp = this.im.copyData(null);
        this.w = im.getWidth();
        this.h = im.getHeight();

        this.calcStrokes(strk);
    }
    
    public Color getColor(int vmid) {
        return this.cor[vmid];
    }
    
    private void calcStrokes(String fnstrk) {
        BufferedImage strk = Ferramentas.loadImage(fnstrk);
        this.calcStrokes(strk);
    }
    private void calcStrokes(BufferedImage strk) {
        this.strk = strk;
        this.inp2 = this.strk.copyData(null);
        this.incluir = new boolean[this.totr];
        this.cor = new Color[this.totr];
        for (int i = 0; i < this.totr; i++) {
            this.incluir[i] = false;
            this.cor[i] = null;
        }
        int[][] mtLab = this.opWshed.getRefLabels();
        int[][] mtLab2 = this.opWshed.getRefLabelsBeforeVanishWatershed();
        for (int x=0; x<this.w; x++)
            for (int y=0; y<this.h; y++) {
                //if(this.strk.getRGB(x, y) != corTransp.getRGB()) {
                if(inp2.getSample(x,y,0) != cts.corTransp.getRed() || 
                   inp2.getSample(x,y,1) != cts.corTransp.getGreen() || 
                   inp2.getSample(x,y,2) != cts.corTransp.getBlue()) {
                    int i = mtLab[x][y];
                    int j = mtLab2[x][y];//wshedlines
                    if (i >= 0 && !incluir[i] && j >= 0) {
                        incluir[i] = true;
                        cor[i] = new Color(this.strk.getRGB(x, y));//this.strk.getColor(x, y);
                    }
                }
            }
        // acerta vetor de cores
        int i = 0, j = 0;
        while (j < this.totr) {
            while (i < this.totr && cor[i] != null) 
                i++;
            j = i+1;
            while (j < this.totr && cor[j] == null) 
                j++;
            if (j < this.totr) {
                Color tmp = cor[i];
                cor[i] = cor[j];
                cor[j] = tmp;
            }
        }
    }

    public void buildModelVerticesAndEdgesCompleteGraph() {
        this.buildVertices(this.incluir);
        /*2008set23: passei para Graph.java
        //grafo completo
        int totv = this.getNumVertices();
        Vertex[] refv = this.getVertices();
	for (int i = 0; i < totv-1; i++)
	    for (int j = i+1; j < totv; j++) {
                Vertex v1 = refv[i];
                Vertex v2 = refv[j];
                this.addEdge(v1, v2);
                this.addEdge(v2, v1);
	    }
        this.finalizaConstrucaoArestas();
*/
        this.buildEdgesCompleteGraph();
    }

    public void buildModelVerticesAndEdges() {
        this.buildVertices(this.incluir);
        /*2008set23: passei para Graph.java
        //2007out25: teste de triangularizacao O(n log n)
        int totv = this.getNumVertices();
        TriangDelaunay d = new TriangDelaunay(totv);
        d.triangularizaGM(this);
        this.finalizaConstrucaoArestas();
        */
        this.buildEdgesDelaunayTriangulation();
    }


    //desenho do modelo: verts coloridos sobre watershed
    public BufferedImage calcBufferedImage() {
        BufferedImage res = Ferramentas.copyImage(this.opWshed.getImWshed());
        Graphics2D g2d = res.createGraphics();
        Vertex[] refv = this.getVertices();
        int totv = this.getNumVertices();
        for (int i=0; i<totv; i++) {
            Vertex v = refv[i];
            Color c = this.cor[i];
            g2d.setColor(c);
            g2d.fillRect((int)v.getx()-1, (int)v.gety()-1, 3, 3);
        }
        return res;
    }
    
    // draws the edges
    public BufferedImage showModel(int tam) {
        return this.showModel(this.getImWshed(), tam);
        /*
        BufferedImage res = showEdges(this.getImWshed(), 1);
        Graphics2D g2d = res.createGraphics();
        Vertex[] refv = this.getVertices();
        int totv = this.getNumVertices();
        boolean[][] adj = this.getAdjMatrix();
        for (int i=0; i<totv; i++) {
            Vertex v1 = refv[i];
            Color c = this.cor[i];
            g2d.setColor(c);
            g2d.fillRect((int)v1.getx()-tam/2, (int)v1.gety()-tam/2, tam, tam);
        }
        return res;
        */
    }
    public BufferedImage showModel(BufferedImage im2, int tam) {
        return showModel(im2,tam,1);
    }
    public BufferedImage showModel(BufferedImage im2, int tam, int tamlinha) {
        BufferedImage res = showEdges(im2, tamlinha);
        Graphics2D g2d = res.createGraphics();
        Vertex[] refv = this.getVertices();
        int totv = this.getNumVertices();
        boolean[][] adj = this.getAdjMatrix();
        for (int i=0; i<totv; i++) {
            Vertex v1 = refv[i];
            Color c = this.cor[i];
            g2d.setColor(c);
            g2d.fillRect((int)v1.getx()-tam/2, (int)v1.gety()-tam/2, tam, tam);
        }
        return res;
    }
/*
    public static void main( String args[] ) {
        if (args.length == 3) {
            Elapsed elap = new Elapsed();
            elap.setFirstEvent();

            String f1 = args[0];
            String f2 = args[1];
            int nb = Integer.parseInt(args[2]);

            ModelGraph1 a = new ModelGraph1(f1, f2, nb);
            //inclui todos as regioes
            a.buildModelVerticesAndEdges();
            System.out.println("GM:");
            a.print();

            elap.setLastEvent();
            System.out.println(" "+elap.getElapInMillis()+" msecs");
            System.out.println("Time: "+elap.getElapInHMS());
        }
        else
            System.out.println("Use: java -Xmx1024m ModelGraph1 image.jpg stroke.bmp blur");
    }
    */
}    
