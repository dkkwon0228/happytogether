/*

2008set22: ShapeContext

Shape Matching and Object Recognition Using Shape Contexts
Serge Belongie, Jitendra Malik and Jan Puzicha

*/

package m00;

//import m00.*;

import java.io.*;
import java.util.*;

public class ShapeContext {

    private Ferramentas fe = new Ferramentas();    

    private int raioSC = 300;//50; // raio do disco em pixels
    //setores para Shape Context
    private int tot_r = 5;  //raio
    private int tot_t = 12; //theta
    private int totSC = tot_r * tot_t;
    
    private float[][] sc; // Shape Context

    public float[][] getSC() {
        return sc;
    }

    // 2011set26: test Belongie's SC by loading data from txt file
    public ShapeContext(String fntxt) {
        try {
            FileInputStream fin = new FileInputStream (fntxt);
            DataInputStream d = new DataInputStream(fin);
            int[] dims = fe.getIntegersFromTxt(d);
            int totPoints = dims[0];
            int totSC = dims[1];
            this.sc = new float[totPoints][totSC];
            for (int i = 0; i < totPoints; i++)
                for (int k = 0; k < totSC; k++)
                    sc[i][k] = 0.0f;
            for (int i=0; i<totPoints; i++) {
                int[] vals = fe.getIntegersFromTxt(d);
                for (int k = 0; k < totSC; k++)
                    sc[i][k] = vals[k];
            }
            fin.close();
        }
        catch (IOException e) {
            System.err.println ("Unable to read from file: "+fntxt);
            System.exit(-1);
        }
    }

    public ShapeContext(int raioSC, LinkedList lx, LinkedList ly, int tot_r, int tot_t) {
        this.tot_r = tot_r;
        this.tot_t = tot_t;
        this.totSC = tot_r * tot_t;
        this.init(raioSC, lx, ly);
    }

    public ShapeContext(int raioSC, LinkedList lx, LinkedList ly) {
        this.init(raioSC, lx, ly);
    }
    public void init(int raioSC, LinkedList lx, LinkedList ly) {

        this.raioSC = raioSC;

        float[] vt = new float[tot_t]; // pi/6, pi/3, pi/2, 2pi/3, 5pi/6, pi, ..., 2pi
        for (int i = 0; i < tot_t; i++)
            vt[i] = (2.0f*(float)Math.PI /(float)tot_t) * (float) (i+1);
        float base = 2.0f;
        float max = (float)Math.pow(base,tot_r);
        float multiplica = raioSC/max;
        float[] vr = new float[tot_r];
        for (int i = 0; i < tot_r; i++)
            vr[i] = (float)Math.pow(2.0f,i+1) * multiplica;

        //bin#1: raio1, ang1; b#2:r2,a1; b#3:r3,a1; b#4:r4,a1; b#5:r5,a1;
        //b#6:r1,a2; ... b#10:r5,a2;
        //b#11: r1, a3; ... b#15: r5, a3;
        //b#16:     a4       #20
        //b#21:     a5       #25
        //b#26:     a6       #30
        //b#31:     a7       #35
        //b#36:     a8       #40
        //b#41:     a9       #45
        //b#46:     a10      #50
        //b#51:     a11      #55
        //b#56:     a12      #60
        //Shape Context
        int totPoints = lx.size();
        this.sc = new float[totPoints][totSC];
        for (int i = 0; i < totPoints; i++)
            for (int k = 0; k < totSC; k++)
                sc[i][k] = 0.0f;


        ////////////////////////////////////////////////////////////////////////
        int[] vx = new int[totPoints];
        int[] vy = new int[totPoints];

        Iterator itx = lx.iterator();
        Iterator ity = ly.iterator();
        for (int i = 0; i < totPoints; i++) {
            int x = ((Integer)itx.next()).intValue();
            int y = ((Integer)ity.next()).intValue();
            vx[i] = x;
            vy[i] = y;
        }

        //matriz de distancias
        float[][] dist = new float[totPoints][totPoints];
        for (int i = 0; i < totPoints; i++)
            for (int j = 0; j < totPoints; j++) {
                int x1 = vx[i];
                int y1 = vy[i];
                int x2 = vx[j];
                int y2 = vy[j];
                dist[i][j] = (float)fe.euclideanDistance(x1,y1, x2,y2);
            }

        //cria lista de pontos dentro do disco, para cada ponto
        LinkedList[] ldisc = new LinkedList[totPoints];
        for (int i = 0; i < totPoints; i++) {
            ldisc[i] = new LinkedList();
            for (int j = 0; j < totPoints; j++) {
                if (i != j && dist[i][j] <= raioSC)
                    ldisc[i].addLast(j);
            }
        }
        
        ////////////////////////////////////////////////////////////////////////

        /*
Cos theta = cateto adjacente / hipotenusa
sen 30 = 1/2
cos 30 = sqrt(3)/2
cos 60 = 1/2
sen 60 = sqrt(3)/2
cos theta = " (grdx*mdx+grdy*mdy)/(gr_module*mdl_module) "

i: x1,y1
j: x2,y2
dx = x2-x1
dy = y2-y1
v = j - i
cos theta = dx / |v| ==> theta = arc cos dx/|v|
se (dy < 0) entao theta:= 2PI - theta
         */
        for (int i = 0; i < totPoints; i++) {
            int total = ldisc[i].size();
            Iterator it = ldisc[i].iterator();
            int x1 = vx[i];
            int y1 = vy[i];
            while(it.hasNext()) {
                int j = ((Integer)it.next()).intValue();
                int x2 = vx[j];
                int y2 = vy[j];
                //cos theta
                float dx = x2 - x1;
                float dy = y2 - y1;
                float modv = dist[i][j]; //(float)fe.euclideanDistance(x1,y1, x2,y2);
                float cos_theta = dx / modv; //adjacente/hipotenusa
                float theta = (float)Math.acos(cos_theta);
                if (dy < 0)
                    theta = 2.0f*(float)Math.PI - theta;
                float raio = modv;
                //tenho raio e theta, agora localiza 'bin' por angulo e raio
                int id_r=0, id_t=0;
                for (int ii = 0; ii < tot_r; ii++)
                    if (raio <= vr[ii]) {
                        id_r = ii;
                        break;
                    }
                for (int jj = 0; jj < tot_t; jj++)
                    if (theta <= vt[jj]) {
                        id_t = jj;
                        break;
                    }
                //identificador do bin
                int id_bin = id_t*tot_r+id_r;
                sc[i][id_bin] += 1.0f;
            }
            if (total > 0)
                for (int ii=0; ii < totSC; ii++)
                    sc[i][ii] = sc[i][ii] / (float)total;
        }
        
    }


}

