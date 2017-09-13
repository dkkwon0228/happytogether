/*

2008mar18

*/

package m00;

import java.util.*;
import java.awt.image.*;
import java.awt.*;
import java.io.*;

public class Graph {
    protected LinkedList vertices, edges;
    protected Vertex[] refv;
    protected int[][] adj;
    protected boolean[][]adjm;
    protected int totv, tote;
    protected int[] cont;
    
    private Ferramentas fe = new Ferramentas();
    
    protected float minx=99999.0f, miny=99999.0f, maxx=-99999.0f, maxy=-99999.0f;

    public Graph() {
        this.vertices = new LinkedList();
        this.edges = new LinkedList();
        this.totv = 0;
        this.tote = 0;
        this.refv = null;
        this.adj = null;
    }
    
    public Vertex addVertex(Region r) {
        Vertex v = new Vertex(r);
        this.vertices.addLast(v);
        return v;
    }

    public Vertex addVertex(Vertex v) {
        this.vertices.addLast(v);
        return v;
    }

    public Vertex addVertex(float x, float y) {
        Vertex v = new Vertex(x, y);
        this.vertices.addLast(v);
        return v;
    }
    
    public Edge addEdge(Vertex from, Vertex to) {
        Edge e = new Edge(from, to);
        this.edges.addLast(e);
        return e;
    }
    
    // constroi matriz de adjacencia  e refv[]
    public void finalizaConstrucaoVertices() {
        Iterator it = this.vertices.iterator();
        this.totv = this.vertices.size();
        this.refv = new Vertex[this.totv];
        int ii = 0;
        while (it.hasNext()) {
            Vertex v = (Vertex)it.next();
            refv[ii] = v;
            v.setId(ii);
            ii++;
        }
    }

    public void finalizaConstrucaoArestas() {
        //matriz de adjacencia
        this.adjm = new boolean[this.totv][this.totv]; //matriz de adj
        this.adj = new int[this.totv][this.totv]; //lista de adj
        for (int i = 0; i<this.totv; i++)
            for (int j = 0; j<this.totv; j++) {
                adj[i][j] = -1; //vazio
                adjm[i][j] = false; //vazio
            }
        this.cont = new int[this.totv];
        for (int i = 0; i<this.totv; i++)
            cont[i] = 0;
        Iterator it = this.edges.iterator();
        this.tote = this.edges.size();
        while (it.hasNext()) {
            Edge e = (Edge)it.next();
            Vertex from = e.getFrom();
            Vertex to = e.getTo();
            int i = from.getId();
            int j = to.getId();
            if (i != j) {
//System.out.println("i="+i+"  j="+j+"  conti="+cont[i]);
                adj[i][cont[i]] = j;
                cont[i]++;
                adjm[i][j] = true;
            }
        }
    }
    
    public boolean[][] getAdjMatrix() {
        return this.adjm;
    }
    
    public int getNumVertices() {
        return this.totv;
    }
    public int getNumEdges() {
        return this.tote;
    }
    public Vertex[] getVertices() {
        return this.refv;
    }
    //Uso: "enquanto adj[i][k++] > -1" 
    public int[][] getAdjacencyList() {
        return this.adj;
    }

    public BufferedImage showEdges(BufferedImage im, int pencil_len) {
        int dot_len = 5;
        return showEdges(im, pencil_len, dot_len);
    }
    public BufferedImage showEdges(BufferedImage im, int pencil_len, int dot_len) {
        return showEdges(im, pencil_len, dot_len, Color.black, Color.black);
    }
    public BufferedImage showEdges(BufferedImage im, int line_len, int dot_len, Color line_color, Color dot_color) {
        /*
        BufferedImage res = im;
        Graphics2D g2d = res.createGraphics();
        Vertex[] refv = this.getVertices();
        int totv = this.getNumVertices();
        boolean[][] adj = this.getAdjMatrix();
        g2d.setStroke(new BasicStroke(pencil_len));
        for (int i=0; i<totv-1; i++) {
            Vertex v1 = refv[i];
            for (int j=i+1; j<totv; j++) 
                if (adj != null && adj[i][j] == true) {
                    Vertex v2 = refv[j];
                    g2d.setColor(Color.black);//blue);
                    g2d.drawLine((int)v1.getx(), (int)v1.gety(), (int)v2.getx(), (int)v2.gety());
                }
        }
        */
        BufferedImage res = showEdgesOnly(im, line_len, line_color);
        Graphics2D g2d = res.createGraphics();
        Vertex[] refv = this.getVertices();
        int totv = this.getNumVertices();
Color newc = dot_color; //new Color(0,0,0);
//Color newc = new Color(100,100,100);//50, 50,50);
int tam = dot_len; //5;//3;
        for (int i=0; i<totv; i++) {
            Vertex v1 = refv[i];
            g2d.setColor(newc);//Color.green);
            g2d.fillRect((int)v1.getx()-tam/2, (int)v1.gety()-tam/2, tam, tam);
        }
        return res;
    }
    public BufferedImage showEdgesOnly(BufferedImage im, int pencil_len, Color cor) {
        BufferedImage res = im;
        Graphics2D g2d = res.createGraphics();
        Vertex[] refv = this.getVertices();
        int totv = this.getNumVertices();
        boolean[][] adj = this.getAdjMatrix();
        g2d.setStroke(new BasicStroke(pencil_len));
        g2d.setColor(cor);//Color.black);//blue);
        for (int i=0; i<totv-1; i++) {
            Vertex v1 = refv[i];
            for (int j=i+1; j<totv; j++) 
                if (adj != null && adj[i][j] == true) {
                    Vertex v2 = refv[j];
                    g2d.drawLine((int)v1.getx(), (int)v1.gety(), (int)v2.getx(), (int)v2.gety());
                }
        }
        return res;
    }
    public float maxEdgeLen() {
        Vertex[] refv = this.getVertices();
        int totv = this.getNumVertices();
        boolean[][] adj = this.getAdjMatrix();
        float max = 0.0f;
        for (int i=0; i<totv-1; i++) {
            Vertex v1 = refv[i];
            for (int j=i+1; j<totv; j++) 
                if (adj != null && adj[i][j] == true) {
                    Vertex v2 = refv[j];
                    float d = (float) fe.euclideanDistance((int)v1.getx(), (int)v1.gety(), 
                                                            (int)v2.getx(), (int)v2.gety());
                    if (max > d)
                        max = d;
                }
        }
        return max;
    }

    public void desloc(float dx, float dy) {
        // desloca x e y de cada vertice somando dx e dy, respect
        Vertex[] refv = this.getVertices();
        int totv = this.getNumVertices();
        for (int i=0; i<totv; i++) {
            Vertex v = refv[i];
            float nx = v.getx() + dx;
            v.setx(nx);
            float ny = v.gety() + dy;
            v.sety(ny);
        }
    }

    public void buildEdgesDelaunayTriangulation() {
        TriangDelaunay d = new TriangDelaunay(this.totv);
        d.triangularizaG(this);
        this.finalizaConstrucaoArestas();
    }

    public void buildEdgesCompleteGraph() {
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
    }

    //2009mar17: parece que nao dah certo
    public void buildEdgesTriangulation2BlocksCompleteGraphs(int limx, int limy) {
        buildEdgesDelaunayTriangulation();
        //constroi dois blocos completos:
        int totv = this.getNumVertices();
        Vertex[] refv = this.getVertices();
        for (int i = 0; i < totv-1; i++)
            for (int j = i+1; j < totv; j++) {
                Vertex v1 = refv[i];
                Vertex v2 = refv[j];
                float x1, y1, x2, y2;
                x1 = v1.getx();
                y1 = v1.gety();
                x2 = v2.getx();
                y2 = v2.gety();
                //ainda nao inseriu e os dois pontos estao do mesmo lado (acima ou abaixo)
                if (!adjm[i][j] && 
                    ((y1 < limy && y2 < limy) || (y1 >= limy && y2 >= limy)) &&
                    ((x1 < limx && x2 < limx) || (x1 >= limx && x2 >= limx))) {
                    adj[i][cont[i]] = j;
                    cont[i]++;
                    adjm[i][j] = true;
                    adj[j][cont[j]] = i;
                    cont[j]++;
                    adjm[j][i] = true;

                    this.addEdge(v1, v2);
                    this.addEdge(v2, v1);
                }
            }
        this.tote = this.edges.size();
    }

    //2009mar17: teste com raio
    public void buildEdgesTriangulationDisksCompleteGraphs(int raio) {
        buildEdgesDelaunayTriangulation();
        //constroi dois blocos completos:
        int totv = this.getNumVertices();
        Vertex[] refv = this.getVertices();
        for (int i = 0; i < totv-1; i++)
            for (int j = i+1; j < totv; j++) {
                Vertex v1 = refv[i];
                Vertex v2 = refv[j];
                float x1, y1, x2, y2;
                x1 = v1.getx();
                y1 = v1.gety();
                x2 = v2.getx();
                y2 = v2.gety();
                float dist = (float) fe.euclideanDistance(x1,y1, x2,y2);
                //ainda nao inseriu e os dois pontos estao do mesmo lado (acima ou abaixo)
                if (!adjm[i][j] && dist <= raio) {
                    adj[i][cont[i]] = j;
                    cont[i]++;
                    adjm[i][j] = true;
                    adj[j][cont[j]] = i;
                    cont[j]++;
                    adjm[j][i] = true;

                    this.addEdge(v1, v2);
                    this.addEdge(v2, v1);
                }
            }
        this.tote = this.edges.size();
    }

    public void buildEdges(boolean[][] adj) {
        Vertex[] refv = this.getVertices();
        int totv = refv.length;
        for (int i = 0; i < totv; i++)
            for (int j = 0; j < totv; j++) {
		if (i != j && adj[i][j]) { 
                    Vertex v1 = refv[i];
                    Vertex v2 = refv[j];
                    this.addEdge(v1, v2);
		}
            }
        this.finalizaConstrucaoArestas();
    }

    // totv
    // id x y
    public void saveVertices(String fout) {
        try{
            File fileTXT = new File(fout);
            Writer ftxt = new BufferedWriter( new FileWriter(fileTXT) );
            int totv = this.getNumVertices();
            ftxt.write(totv+"\n");
            Vertex[] refv = this.getVertices();
            for (int i=0; i<totv; i++) {
                Vertex v = refv[i];
                ftxt.write(i+" "+v.getx()+" "+v.gety()+"\n");
            }
            ftxt.close();
        } 
        catch (FileNotFoundException e) {
            System.out.println("Error: Cannot open file for writing.");
        } 
        catch (IOException e) {
            System.out.println("Error: Cannot write to file.");
        }
    }

    public void saveEdges(String fout) {
        try{
            File fileTXT = new File(fout);
            Writer ftxt = new BufferedWriter( new FileWriter(fileTXT) );
            int tote = this.getNumEdges();
            ftxt.write(tote+"\n");
            int[][] adjl = this.getAdjacencyList();
            int totv = this.getNumVertices();
            for (int i = 0; i < totv; i++)
                for (int j = 0; j < totv; j++) {
                    if (adjl[i][j] <= -1)
                        continue;
                    ftxt.write(i+" "+adjl[i][j]+"\n");
                }
            ftxt.close();
        } 
        catch (FileNotFoundException e) {
            System.out.println("Error: Cannot open file for writing.");
        } 
        catch (IOException e) {
            System.out.println("Error: Cannot write to file.");
        }
    }

    public void loadVertices(String fntxt) {
        try {
            FileInputStream fin = new FileInputStream (fntxt);
            DataInputStream d = new DataInputStream(fin);
            this.totv = fe.getIntegerFromTxt(d);
            for (int i=0; i<this.totv; i++) {
                  double[] res = fe.readTextLineDoubles(d); // id, x, y
                  float x = (float)res[1];
                  float y = (float)res[2];
                  this.addVertex(x, y); // os id's dos vertices obedecem esta ordem!
                  if (x < minx) minx = x;
                  if (y < miny) miny = y;
                  if (x > maxx) maxx = x;
                  if (y > maxy) maxy = y;
            }
            this.finalizaConstrucaoVertices();
            fin.close();
        }
        catch (IOException e) {
            System.err.println ("Unable to read from file: "+fntxt);
            System.exit(-1);
        }
    }

    public void loadEdges(String fntxt) {
        try {
            FileInputStream fin = new FileInputStream (fntxt);
            DataInputStream d = new DataInputStream(fin);
            Vertex[] refv = this.getVertices();
            this.tote = fe.getIntegerFromTxt(d);
            for (int i=0; i<this.tote; i++) {
                  double[] res = fe.readTextLineDoubles(d); // v1, v2
                  int i1 = (int)res[0];
                  int i2 = (int)res[1];
                  Vertex v1 = refv[i1];
                  Vertex v2 = refv[i2];
                  this.addEdge(v1, v2);
            }
            this.finalizaConstrucaoArestas();
            fin.close();
        }
        catch (IOException e) {
            System.err.println ("Unable to read from file: "+fntxt);
            System.exit(-1);
        }
    }

    //2011ago30: lista adicional de outliers
    public void loadVerticesOutliers(String fntxt, String fntxtOutliers) {
        try {
            FileInputStream fin = new FileInputStream (fntxt);
            DataInputStream d = new DataInputStream(fin);
            this.totv = fe.getIntegerFromTxt(d);
            for (int i=0; i<this.totv; i++) {
                  double[] res = fe.readTextLineDoubles(d); // id, x, y
                  float x = (float)res[1];
                  float y = (float)res[2];
                  this.addVertex(x, y); // os id's dos vertices obedecem esta ordem!
                  if (x < minx) minx = x;
                  if (y < miny) miny = y;
                  if (x > maxx) maxx = x;
                  if (y > maxy) maxy = y;
            }
            fin = new FileInputStream (fntxtOutliers);
            d = new DataInputStream(fin);
            int totOutliers = fe.getIntegerFromTxt(d);
            for (int i=0; i<totOutliers; i++) {
                  double[] res = fe.readTextLineDoubles(d); // x, y
                  float x = (float)res[0];
                  float y = (float)res[1];
                  this.addVertex(x, y); 
                  if (x < minx) minx = x;
                  if (y < miny) miny = y;
                  if (x > maxx) maxx = x;
                  if (y > maxy) maxy = y;
            }
            this.totv += totOutliers;
            this.finalizaConstrucaoVertices();
            fin.close();
        }
        catch (IOException e) {
            System.err.println ("Unable to read from file: "+fntxt);
            System.exit(-1);
        }
    }

}
