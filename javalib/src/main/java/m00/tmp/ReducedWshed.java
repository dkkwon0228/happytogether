
/*

2009ago30: reduzir tempo de pre-processamento de agrupamento de regioes

*/

package m00.tmp;

import m00.Blur;
import m00.Components;
import m00.Consts;
import m00.Conversion;
import m00.Coords2d;
import m00.Elapsed;
import m00.Ferramentas;
import m00.Gradient;
import m00.Pixel;
import m00.QTsampling;
import m00.VRsampling;
import m01.*;

import java.awt.image.*;
import java.util.*;

import java.awt.*;
import java.awt.image.BufferedImage;


public class ReducedWshed {
    BufferedImage imRGB;
    int totr;
    int w, h;
    int minsize;
    int[][] mtLab;
    int[][] mtLabBeforeVanishWshed;

    Consts cts = new Consts();
    Ferramentas1 fe = new Ferramentas1();

    public BufferedImage getImRGB() {
        return this.imRGB;
    }

    //rotulos das regioes
    public int[][] getRegionsMatrix() {
        return this.mtLab;
    }
    
    //total de regioes
    public int getTotalRegions() {
        return this.totr;
    }
    
    public BufferedImage getImWshed() {
        return this.calcBufferedImage();
    }
    public BufferedImage getImWshed(Color cor, BufferedImage imRGB_) {
        return this.calcBufferedImage(cor, imRGB_);
    }
    
    public int[][] getRefLabels() {
        return this.getRegionsMatrix();
    }
    
    //regioes com linhas do watershed
    public int[][] getRefLabelsBeforeVanishWatershed() {
        return this.mtLabBeforeVanishWshed;
    }
    
    public boolean[][] getWshedLines() {
        boolean[][] wshedline = this.calcContorno();
        return wshedline;
    }

    public void calcMatrizBeforeVanishWshed(Watershed opWshed) {
        this.mtLabBeforeVanishWshed = opWshed.getRefLabelsBeforeVanishWatershed();
        for (int x = 0; x < w; x++)
            for (int y = 0; y < h; y++)
                this.mtLabBeforeVanishWshed[x][y] -= 1;

        BufferedImage res = new BufferedImage(w, h, BufferedImage.TYPE_3BYTE_BGR);
        WritableRaster oup = res.getRaster();
        for (int x = 0; x < w; x++)
            for (int y = 0; y < h; y++) 
                if (this.mtLabBeforeVanishWshed[x][y] == -1){
                    oup.setSample(x,y,0,255);
                    oup.setSample(x,y,1,0);
                    oup.setSample(x,y,2,0);
                }
                else
                    for (int c=0; c<3; c++)
                        oup.setSample(x,y,c,0);
        //fe.saveImage(res, "zzz-debug-beforeVanishWshed.png");
    }

    //2008set23: calcula marcadores
    public void calculaMarcadoresParaWatershed(BufferedImage im, LinkedList markers, LinkedList cmarkers) {
        double[][] mtSrc, mtEvl = null;

        Gradient opGradient = new Gradient(im);
        double[][] mtMod = opGradient.getModuleNormalizedAndInverted();

        int blur = 4;
        Blur opBlur = new Blur(mtMod, im.getWidth(), im.getHeight(), blur);
        opBlur.Normalize();
        double[][] mtBlur = opBlur.getBlur();
        BufferedImage bEvl = opBlur.showNormalized();

        int NIters = 5;
        int criteria = 1;
        float valCriteria = 20.0f;
        int maxPixCell = 500;
        QTsampling opQTs = new QTsampling(bEvl, criteria, valCriteria, maxPixCell, false);

        VRsampling opVRs = new VRsampling(bEvl, opQTs.getPoints()); //Voronoi
        opVRs.prepareDisplay();
        opVRs.voronoiLabels();

        for (int iters=1; iters<=NIters; iters++) {
            opVRs.evCenters();
            opVRs.voronoiLabels();
        }
        
        LinkedList l1 = opVRs.getMarkers(); //markers
        LinkedList l2 = opVRs.getPoints(); //cmarkers
        Iterator it1 = l1.iterator();
        while (it1.hasNext()) {
            Pixel p = (Pixel)it1.next();
            markers.addLast(p);
        }
        Iterator it2 = l2.iterator();
        while (it2.hasNext()) {
            Pixel p = (Pixel)it2.next();
            cmarkers.addLast(p);
        }
        
boolean drOrig = true;
boolean drMean = false;
boolean drEdges = true;
boolean drCenters = true;
BufferedImage res = opVRs.showResults(drOrig, drMean, drEdges, drCenters);
fe.saveImage(res,"zzz-debug-markers.png");

    }
    
    //segm2.java /////////////////////////////////////////////////////
    //-> nao reduz wshed (j� reduzido com blur; mas 'entorta' bordas!)
    public ReducedWshed(String fn, int blur, String msg) { 
        BufferedImage im = Ferramentas.loadImage(fn);
        this.init2(im, blur, msg);
    }
    public ReducedWshed(BufferedImage im, int blur, String msg) { 
        this.init2(im, blur, msg);
    }
    public void init2(BufferedImage im, int blur, String msg) {
        //System.out.println(msg);
        this.w = im.getWidth();
        this.h = im.getHeight();
        Conversion opConv = new Conversion(im, true);
        this.imRGB = opConv.imgConverted();
        Watershed opWshed;
        //2008set23: watershed with markers
        if (msg == "markers") {
            LinkedList markers = new LinkedList();
            LinkedList cmarkers = new LinkedList();
            this.calculaMarcadoresParaWatershed(im, markers, cmarkers);

System.out.println("markers: "+markers.size());
System.out.println("cmarkers: "+cmarkers.size());

            opConv = new Conversion(im, false);
            BufferedImage imgray = opConv.imgConverted();
            opWshed = new Watershed(imgray, blur, cmarkers, markers);
            
BufferedImage res = opWshed.getImWshed(); //calcBufferedImage();
fe.saveImage(res,"zzz-debug-watershedWithMarkers.png");
            
            this.mtLab = this.matrizWshed(opWshed);
            this.acertaIds();
        }
        else {
            opWshed = new Watershed(this.imRGB, blur);
            this.mtLab = this.matrizWshed(opWshed);
            this.acertaIds();
    
            //this.minsize = -1;
            System.gc();
            
            this.minsize = 25;//9;
            this.reduzTotalRegioes();
            /*
            int tot = 1;
//System.out.println("Running heuristic to reduce the number of regions. Please wait...");
            while (tot > 0) {
                tot = this.reduzTotalRegioes(); //resultado em mtLab
                System.gc();
            }
            */
        }

        calcMatrizBeforeVanishWshed(opWshed);
    }
    //segm.java /////////////////////////////////////////////////////
    //Watershed with merging of small regions ( area < minsize )
    public ReducedWshed(String fn, int minsize) { 
        BufferedImage im = Ferramentas.loadImage(fn);
        this.init(im, minsize);
    }
    public ReducedWshed(BufferedImage im, int minsize) { 
        this.init(im, minsize);
    }
    public void init(BufferedImage im, int minsize) { 
    /*
Elapsed elap = new Elapsed();
elap.setFirstEvent();
System.out.println("ReducedWshed...........");
*/
        Conversion opConv = new Conversion(im, true);
        this.imRGB = opConv.imgConverted();
        this.w = this.imRGB.getWidth();
        this.h = this.imRGB.getHeight();
        this.mtLab = this.matrizWshed();
        /*
elap.setLastEvent();
System.out.println("Time: "+elap.getElapInHMS()+"("+elap.getElapInMillis()+" msecs)");
System.exit(1);
*/
        this.acertaIds();
        this.minsize = minsize;
        
        System.gc();
        
        this.reduzTotalRegioes();
        /*
//System.out.println("Running heuristic to reduce the number of regions. Please wait...");
        int tot = 1;
        while (tot > 0) {
            tot = this.reduzTotalRegioes(); //resultado em mtLab
            System.gc();
        }
        */
        
        //2008jun07: teste segm frame65: java -Xmx1024m segm ../frame0065.jpg ../zzz-debug-strk-w65.png 9
        //-> correcao do p� exige mais traco amarelo: falha pode ser do filtro conexo?
        double[][] tmp = new double[w][h];
        for (int x = 0; x < w; x++)
            for (int y = 0; y < h; y++)
                tmp[x][y] = this.mtLab[x][y];
        Watershed opWshed = new Watershed(tmp, im);
        calcMatrizBeforeVanishWshed(opWshed);
/*        
        String fout = "zzz-debug-wshed-reduzido.png";
        fe.saveImage(this.calcBufferedImage(), fout);
        */
    }

    //debug
    private boolean[][] calcContorno() {
        return fe.calcContorno(this.mtLab, this.w, this.h);
    }
    
    //debug
    public BufferedImage calcBufferedImage() {
        Conversion opConv = new Conversion(this.imRGB, false);
        BufferedImage imgray = opConv.imgConverted();
        return this.calcBufferedImage(Color.red, fe.grayToRGB(imgray));
    }
    public BufferedImage calcBufferedImage(Color cor, BufferedImage imRGB_) {
        BufferedImage res = new BufferedImage(this.w, this.h, BufferedImage.TYPE_3BYTE_BGR);
        WritableRaster oup = res.getRaster();
        boolean[][] wshedline = this.calcContorno();
        WritableRaster inp = imRGB_.copyData(null);
        for (int x = 0; x < w; x++)
            for (int y = 0; y < h; y++) {
                if (wshedline[x][y]) {
                    oup.setSample(x, y, 0, cor.getRed());
                    oup.setSample(x, y, 1, cor.getGreen());
                    oup.setSample(x, y, 2, cor.getBlue());
                }
                else {
                    int gray = inp.getSample(x, y, 0);
                    for (int c=0; c<3; c++)
                        oup.setSample(x, y, c, inp.getSample(x, y, c));
                }
            }
        return res;
    }
    //devolve a matriz dos rotulos das regioes do watershed
    public int[][] matrizWshed() {
        int blur = 0;
/*
Elapsed elap = new Elapsed();
elap.setFirstEvent();
System.out.println("ReducedWshed...........");
*/
        Watershed opWshed = new Watershed(this.imRGB, blur);
        /*
elap.setLastEvent();
System.out.println("Time: "+elap.getElapInHMS()+"("+elap.getElapInMillis()+" msecs)");
System.exit(1);
*/
        //calcMatrizBeforeVanishWshed(opWshed);
        return matrizWshed(opWshed);
    }
    public int[][] matrizWshed(Watershed opWshed) {
        BufferedImage res = opWshed.getImWshed();
String fout = "zzz-debug-wshed.png";
//fe.saveImage(res, fout);
        this.totr = opWshed.getTotalRegions();
        int[][] mtLab = opWshed.getRefLabels();
        Components c = new Components(mtLab);
        mtLab = c.getComponents();
        this.totr = c.getTotalComponents();
        return mtLab;
    }
    //vetor com a lista de adjacencia entre regioes
    public LinkedList[] adjacenciaWshed() {
        LinkedList[] la = new LinkedList[this.totr];
        for (int i = 0; i < this.totr; i++)
            la[i] = new LinkedList();
        for (int x = 0; x < w; x++)
            for (int y = 0; y < h; y++) {
                int i = this.mtLab[x][y]; // indice da regiao
                for (int k=0; k<4; k++) {
                    int x2 = x + cts.viz4x[k];
                    int y2 = y + cts.viz4y[k];
                    if (x2 >= 0 && x2 < w && y2 >= 0 && y2 < h) {
                        int j = this.mtLab[x2][y2];
                        la[i].add(j);
                        la[j].add(i);
                    }
                }
            }
        return la;
    }
    public void reduzTotalRegioesAcelerado1() {
        Elapsed elap = new Elapsed();
        elap.setFirstEvent();
        LinkedList[] r = new LinkedList[this.totr];
        //adjacencia
        LinkedList[] la = this.adjacenciaWshed();
        for (int i = 0; i < this.totr; i++)
            r[i] = new LinkedList();
        for (int x=0; x<this.w; x++)
            for (int y=0; y<this.h; y++) {
                int i = mtLab[x][y];
                Coords2d p = new Coords2d(x,y);
                r[i].add(p);
            }
        //regioes pequenas
        boolean[] eh_pequeno = new boolean[this.totr];
        for (int i = 0; i < this.totr; i++)
            eh_pequeno[i] = false;
        int tot_small = 0;
        for (int i = 0; i < this.totr; i++)
            if (r[i].size() < this.minsize) {
                eh_pequeno[i] = true;
                tot_small++;
            }
        System.out.println("Min size: "+this.minsize);
        System.out.println("Total small: "+tot_small+" / "+this.totr);
//////////////////////////////////////////////////
        //tudo o que eh pequeno fica com um mesmo rotulo:
        int novo_rotulo = this.totr;
        for (int x=0; x<this.w; x++)
            for (int y=0; y<this.h; y++)
                if(this.mtLab[x][y] == novo_rotulo) {
                    System.out.println("Erro: mtLab, novo_rotulo!");
                    System.exit(1);
                }
        for (int i = 0; i < this.totr; i++)
            if (eh_pequeno[i]) {
                LinkedList pixels = r[i];
                Iterator it = pixels.iterator();
                while (it.hasNext()) {
                    Coords2d p = (Coords2d)it.next();
                    int x = p.getx(), y = p.gety();
                    this.mtLab[x][y] = novo_rotulo;
                }
            }
        int[] tmp = new int[1];
        int[][] comps = fe.rotulaComponentes(this.mtLab, tmp);
        this.mtLab = comps;
        this.totr = tmp[0];
        System.out.println("Total regions after reduction: "+this.totr);
    }
    //merge de regioes pequenas ( area < this.minsize )
    public void reduzTotalRegioes() {
        for (int i=0; i<3; i++)
        this.reduzTotalRegioesAcelerado();
//            this.reduzTotalRegioesAntigoELento();
    }
    public int reduzTotalRegioesAntigoELento() {
        WritableRaster inp = this.imRGB.copyData(null);

        //regioes do watershed: conjunto de pixels; (usa para acertar mtLab)
        LinkedList[] r = new LinkedList[this.totr];
        //cores: RGB
        float[] gr = new float[this.totr];
        float[] gg = new float[this.totr];
        float[] gb = new float[this.totr];
        //adjacencia
        LinkedList[] la = this.adjacenciaWshed();
        for (int i = 0; i < this.totr; i++) {
            r[i] = new LinkedList();
            gr[i] = 0.0f;
            gg[i] = 0.0f;
            gb[i] = 0.0f;
        }
        for (int x=0; x<this.w; x++)
            for (int y=0; y<this.h; y++) {
                int i = mtLab[x][y];
                Coords2d p = new Coords2d(x,y);
                r[i].add(p);
                gr[i] += inp.getSample(x,y,0);
                gg[i] += inp.getSample(x,y,1);
                gb[i] += inp.getSample(x,y,2);
            }
        for (int i = 0; i < this.totr; i++) {
            gr[i] /= (float)r[i].size();
            gg[i] /= (float)r[i].size();
            gb[i] /= (float)r[i].size();
        }
        //regioes pequenas
        LinkedList pequenos = new LinkedList();
        for (int i = 0; i < this.totr; i++) {
            if (r[i].size() < this.minsize)
                pequenos.add(i);
            if (r[i].size() < 0) {
                System.out.println("Error: area[i] < 0");
                System.exit(1);
            }
        }
        System.out.println("Min size: "+this.minsize);
        System.out.println("Total small: "+pequenos.size()+" / "+this.totr);
        
        //merge de regioes pequenas
        while(!pequenos.isEmpty()) {
            int i = ((Integer)pequenos.removeFirst()).intValue();
            //escolhe uma regiao adjacente de aparencia mais proxima
            int maisproximo = -1;
            float mindist = 999;
            Iterator it = la[i].iterator();
            while(it.hasNext()) {
                int j = ((Integer)it.next()).intValue();
                if(r[j].size() > 0 && j != i) {
                    float gri, ggi, gbi, grj, ggj, gbj;
                    float dist = (float)fe.euclideanDistance(gr[i],gg[i],gb[i],gr[j],gg[j],gb[j]);
                    if (dist < mindist) {
                        maisproximo = j;
                        mindist = dist;
                    }
                }
            }
            if (maisproximo >= 0) {
                //merge das info: mtLab, r (lista de pixels por regiao), cores, la (lista de adjacencia) 
                LinkedList pixels = r[i];
                it = pixels.iterator();
                int id = maisproximo;
                while (it.hasNext()) {
                    Coords2d p = (Coords2d)it.next();
                    int x = p.getx(), y = p.gety();
                    this.mtLab[x][y] = id;
                }
                int areai = r[i].size();
                int areaid = r[id].size();
                r[id].addAll(r[i]);
                r[i].clear();
                gr[id] = (gr[id]*(float)areaid + gr[i]*(float)areai)/(float)(areaid+areai);
                gg[id] = (gg[id]*(float)areaid + gg[i]*(float)areai)/(float)(areaid+areai);
                gb[id] = (gb[id]*(float)areaid + gb[i]*(float)areai)/(float)(areaid+areai);
                la[id].addAll(la[i]);
                la[i].clear();
            }
        }
        //acerta os identificadores das regioes em mtLab, que ficou com 'buracos' apos merge
        int totalAposReducao = fe.acertaIdentificadores(this.mtLab, r) + 1;
        System.out.println("Total regions after reduction: "+totalAposReducao);
        this.totr = totalAposReducao;

        int totpequenos = 0;
        for (int i = 0; i < this.totr; i++)
            if (r[i].size() > 0 && r[i].size() < this.minsize)
                    totpequenos++;
        return totpequenos;
    }
    //deixa bordas 'lisas' das regioes de mtLab
    public void filtroConexo(int k) {
        //linha vertical
        LinkedList se1 = new LinkedList();
        se1.add(new Coords2d(0,1));
        se1.add(new Coords2d(0,-1));
        //linha horizontal
        LinkedList se2 = new LinkedList();
        se2.add(new Coords2d(1,0));
        se2.add(new Coords2d(-1,0));
        
        for (int i=0; i<k; i++) {
            this.mtLab = fe.supinf(this.mtLab, se1);
            this.mtLab = fe.infsup(this.mtLab, se1);
            this.mtLab = fe.supinf(this.mtLab, se2);
            this.mtLab = fe.infsup(this.mtLab, se2);
        }
        
        this.acertaIds();
    }
    public void acertaIds() {
        LinkedList[] r = new LinkedList[this.totr];
        for (int i = 0; i < this.totr; i++)
            r[i] = new LinkedList();
        for (int x=0; x<this.w; x++)
            for (int y=0; y<this.h; y++) {
                int i = this.mtLab[x][y];
                Coords2d p = new Coords2d(x,y);
                r[i].add(p);
            }
        this.totr = fe.acertaIdentificadores(this.mtLab, r) + 1;
    }
    
/*
    public static void main( String args[] ) {
        if (args.length == 2) {
            String f1 = args[0];
            int minsize = Integer.parseInt(args[1]);
            wshed6 s = new wshed6(f1, minsize);
        }
        else
            System.out.println("Use: java -Xmx1024m wshed6 image.jpg minsize");
    }
*/

    public void reduzTotalRegioesAcelerado() {
        WritableRaster inp = this.imRGB.copyData(null);

        //regioes do watershed: conjunto de pixels; (usa para acertar mtLab)
        LinkedList[] r = new LinkedList[this.totr];
        //cores: RGB
        float[] gr = new float[this.totr];
        float[] gg = new float[this.totr];
        float[] gb = new float[this.totr];
        //adjacencia
        LinkedList[] la = this.adjacenciaWshed();
        for (int i = 0; i < this.totr; i++) {
            r[i] = new LinkedList();
            gr[i] = 0.0f;
            gg[i] = 0.0f;
            gb[i] = 0.0f;
        }
        for (int x=0; x<this.w; x++)
            for (int y=0; y<this.h; y++) {
                int i = mtLab[x][y];
                Coords2d p = new Coords2d(x,y);
                r[i].add(p);
                gr[i] += inp.getSample(x,y,0);
                gg[i] += inp.getSample(x,y,1);
                gb[i] += inp.getSample(x,y,2);
            }
        for (int i = 0; i < this.totr; i++) {
            gr[i] /= (float)r[i].size();
            gg[i] /= (float)r[i].size();
            gb[i] /= (float)r[i].size();
        }
        //regioes pequenas
        LinkedList pequenos = new LinkedList();
        for (int i = 0; i < this.totr; i++) {
            if (r[i].size() < this.minsize)
                pequenos.add(i);
            if (r[i].size() < 0) {
                System.out.println("Error: area[i] < 0");
                System.exit(1);
            }
        }
        System.out.println("Min size: "+this.minsize);
        System.out.println("Total small: "+pequenos.size()+" / "+this.totr);

//converte (listas de vizinhos e de regioes) usando lista ligada com concatenacao O(1)
listaLigadaInteger[] la2 = new listaLigadaInteger[this.totr];
listaLigadaInteger[] r2x = new listaLigadaInteger[this.totr];
listaLigadaInteger[] r2y = new listaLigadaInteger[this.totr];
for (int i = 0; i < this.totr; i++) {
    la2[i] = new listaLigadaInteger();
    Iterator it = la[i].iterator();
    while(it.hasNext()) {
        int j = ((Integer)it.next()).intValue();
        la2[i].addLast(j);
    }
    
    r2x[i] = new listaLigadaInteger();
    r2y[i] = new listaLigadaInteger();
    it = r[i].iterator();
    while (it.hasNext()) {
        Coords2d p = (Coords2d)it.next();
        int x = p.getx(), y = p.gety();
        r2x[i].addLast(x);
        r2y[i].addLast(y);
    }
}
        
        //merge de regioes pequenas
        while(!pequenos.isEmpty()) {
            int i = ((Integer)pequenos.removeFirst()).intValue();
            //escolhe uma regiao adjacente de aparencia mais proxima
            int maisproximo = -1;
            float mindist = 999;
            //Iterator it = la[i].iterator();
            CelulaInteger c = la2[i].getFirst();
            //while(it.hasNext()) {
            while(c != null) {
                //int j = ((Integer)it.next()).intValue();
                int j = c.getval();
                c = c.getsuc();
                //if(r[j].size() > 0 && j != i) {
                if(r2x[j].getSize() > 0 && j != i) {
                    float gri, ggi, gbi, grj, ggj, gbj;
                    float dist = (float)fe.euclideanDistance(gr[i],gg[i],gb[i],gr[j],gg[j],gb[j]);
                    if (dist < mindist) {
                        maisproximo = j;
                        mindist = dist;
                    }
                }
            }
            if (maisproximo >= 0) {
                //merge das info: mtLab, r (lista de pixels por regiao), cores, la (lista de adjacencia) 
                int id = maisproximo;
                /*
                LinkedList pixels = r[i];
                it = pixels.iterator();
                while (it.hasNext()) {
                    Coords2d p = (Coords2d)it.next();
                    int x = p.getx(), y = p.gety();
                    this.mtLab[x][y] = id;
                }
                */
                /*
                int areai = r[i].size();
                int areaid = r[id].size();
                r[id].addAll(r[i]);
                r[i].clear();
                */
                int areai = r2x[i].getSize();
                int areaid = r2x[id].getSize();
                r2x[id].concat(r2x[i]);
                r2x[i].destroy();
                r2y[id].concat(r2y[i]);
                r2y[i].destroy();
                gr[id] = (gr[id]*(float)areaid + gr[i]*(float)areai)/(float)(areaid+areai);
                gg[id] = (gg[id]*(float)areaid + gg[i]*(float)areai)/(float)(areaid+areai);
                gb[id] = (gb[id]*(float)areaid + gb[i]*(float)areai)/(float)(areaid+areai);
                la2[id].concat(la2[i]);
                la2[i].destroy();
                //la[id].addAll(la[i]);
                //la[i].clear();
            }
        }

//converte lista ligada para lista de regioes
for (int i = 0; i < this.totr; i++) {
    r[i] = new LinkedList();
    CelulaInteger cx = r2x[i].getFirst();
    CelulaInteger cy = r2y[i].getFirst();
    while (cx != null) {
        int x = cx.getval();
        cx = cx.getsuc();
        int y = cy.getval();
        cy = cy.getsuc();
        Coords2d p = new Coords2d(x,y);
        r[i].add(p);
    }
}

        //acerta os identificadores das regioes em mtLab, que ficou com 'buracos' apos merge
        int totalAposReducao = fe.acertaIdentificadores(this.mtLab, r) + 1;
        System.out.println("Total regions after reduction: "+totalAposReducao);
        this.totr = totalAposReducao;
    }
}

class listaLigadaInteger {
    private CelulaInteger cur=null, first=null;
    private int tot=0;
    public listaLigadaInteger() {
    }
    public void print() {
        CelulaInteger c = first;
        while (c != null) {
            System.out.print(c.getval()+" ");
            c = c.getsuc();
        }
        System.out.println("");
    }
    public void addLast(int i) {
        CelulaInteger novo = new CelulaInteger(i);
        if (first == null) {
            first = novo;
            cur = first;
            tot=1;
        }
        else {
            cur.setsuc(novo);
            novo.setpred(cur);
            cur = novo;
            tot++;
        }
    }
    public CelulaInteger getFirst() {
        return this.first;
    }
    public CelulaInteger getLast() {
        return this.cur;
    }
    public int getSize() {
        return this.tot;
    }
    public void destroy() {
        first=null;
        cur=null;
        tot=0;
    }
    //concatena lista no final
    public void concat(listaLigadaInteger lista) {
        CelulaInteger novo = lista.getFirst();
        cur.setsuc(novo);
        novo.setpred(cur);
        cur = lista.getLast();
        tot += lista.getSize();
        lista.destroy();
    }
}
class CelulaInteger {
    private int val;
    private CelulaInteger pred=null, suc=null;
    public CelulaInteger(int i) {
        this.val = i;
    }
    public void setpred(CelulaInteger c) {
        this.pred = c;
    }
    public void setsuc(CelulaInteger c) {
        this.suc = c;
    }
    public CelulaInteger getpred() {
        return this.pred;
    }
    public CelulaInteger getsuc() {
        return this.suc;
    }
    public int getval() {
        return this.val;
    }
}
