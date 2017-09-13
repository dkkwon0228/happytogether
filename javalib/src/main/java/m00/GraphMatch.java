/*

2008mar18

Calculates graph matching using a deformation graph to avoid
the topological differences between input and model graphs.

*/

package m00;

import java.io.*;

////////////////////////////////////////////////////////////////////////////
// algorithm for graph matching
public class GraphMatch {
    protected float alpha;
    protected float delta;
    protected Graph input;        // G_i
    protected Graph model;        // G_m
    protected int totvi, totvm;
    protected Vertex[] refvi;
    protected Vertex[] refvm;
    protected Cost custo;
    private Ferramentas fe = new Ferramentas();

    protected float[] mcost;
    protected int[] result;
    
    public GraphMatch(Graph inp, Graph mdl, float alpha, float delta) {
        this.input = inp;
        this.model = mdl;
        this.alpha = alpha; 
        this.delta = delta;
        this.custo = null;

        this.totvi = this.input.getNumVertices();
        this.refvi = this.input.getVertices();
        this.totvm = this.model.getNumVertices();
        this.refvm = this.model.getVertices();
    
        this.mcost = new float[this.totvi]; 
        this.result = new int[this.totvi]; // graph matching
        for (int i = 0; i < this.totvi; i++)
            result[i] = -1;
        
        //this.runMatchingAlgorithm();
    }
    
    public Graph getInputGraph() {
        return input;
    }
    public Graph getModelGraph() {
        return model;
    }
    
    public void setCost(Cost custo) {
        this.custo = custo;
    }
    
    // algorithm for graph matching (homomorphism)
    protected int[] runMatchingAlgorithm() {
        int cont = 0;
        //greedy algorithm
        for (int ii=0; ii<this.totvi; ii++) {
    /*
            if (cont % 100 == 0)
                System.out.print("["+cont+"]");
            else
                System.out.print(".");
                */
            Vertex vi = this.refvi[ii];
            int min = -1; // empty
            float mincost = 9999.9f; //infinity
            for (int jj=0; jj<this.totvm; jj++) {
                Vertex vm = this.refvm[jj];
                float cost = custo.getCost(vi, vm); // local deformation cost
                if(min == -1 || cost < mincost) {
                    mincost = cost;
                    min = jj;
                }
            }
            cont++;
            result[ii] = min;
            mcost[ii] = mincost;
        }
        return result;
    }
    //graph matching: maximal common subgraph
    //algoritmo guloso usando vm's e vi's que ainda nao foram casados
    protected boolean runMatchingAlgorithmMCS() {
        return this.runMatchingAlgorithmMCS(this.custo.getAlpha());
    }
    protected boolean runMatchingAlgorithmMCS(float alpha) {
System.out.println("alpha="+alpha);
        this.custo.setAlpha(alpha);
        // verifica casamento atual (deve ser mcs)
        boolean[] usouvm = new boolean[this.totvm];
        for (int i = 0; i < this.totvm; i++)
            usouvm[i] = false;
        for (int i = 0; i < this.totvi; i++)
            if (result[i] >= 0)
                usouvm[result[i]] = true;
        int cont = 0;
        //greedy algorithm
        boolean modificou = false;
        for (int ii=0; ii<this.totvi; ii++) 
            if (result[ii] < 0) { // vi nao rotulado
                Vertex vi = this.refvi[ii];
                int min = -1; // empty
                float mincost = 9999.9f; //infinity
                for (int jj=0; jj<this.totvm; jj++) 
                    if (!usouvm[jj]){
                        Vertex vm = this.refvm[jj];
                        float cost = this.custo.getCost(vi, vm); // local deformation cost
                        if(min == -1 || cost < mincost) {
                            mincost = cost;
                            min = jj;
                            modificou = true;
                        }
                    }
                cont++;
                result[ii] = min;
                mcost[ii] = mincost;
            }
        if (modificou)
            this.postProcessing();
        return modificou;
    }
    public int[] getMatching() {
        return this.result;
    }
    public float getMatchingCost() {
        float sum = 0.0f;
        for (int ii=0; ii<this.totvi; ii++)
            sum += mcost[ii];
        return sum;
    }
    public float[] getmcost() {
        return this.mcost;
    }
    
    public void print() {
        for (int ii=0; ii<totvi; ii++)
            System.out.println(ii+" : "+this.result[ii]);
    }
    
    public void saveMatching(String fout) {
        try{
          File fileTXT = new File(fout);
          Writer ftxt = new BufferedWriter( new FileWriter(fileTXT) );
          for (int ii=0; ii<totvi; ii++) {
            int best = this.result[ii];
            ftxt.write(ii+" "+best+"\n");
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

    //desempata para calculo de isomorfismo (na verdade, (inexact) Maximal Common Subgraph)
    public int[] postProcessing() {
        return fe.desempataRotulos(this.result, this.mcost, this.totvm);
        /*
        // 1. desempate de rotulos
        int[][] r = new int[this.totvm][this.totvi];
        int[] contv = new int[this.totvm];
        for (int i=0; i<this.totvm; i++) {
            contv[i] = 0;
            for (int j=0; j<this.totvi; j++)
                r[i][j] = -1;
        }
        for (int i=0; i<this.totvi; i++) {
            int vmid = result[i];
            r[vmid][contv[vmid]] = i;
            contv[vmid]++;
        }
        for (int i=0; i<this.totvm; i++) {
            if (contv[i] > 1) {
                int min = r[i][0];
                float minc = mcost[min];
                for (int j=1; j<contv[i]; j++) {
                    int viid = r[i][j];
                    if (mcost[viid] < minc) {
                        min = viid;
                        minc = mcost[min];
                    }
                }
                for (int j=0; j<contv[i]; j++) {
                    int viid = r[i][j];
                    result[viid] = -2;
                }
                result[min] = i;
            }
        }
        return result;
        */
    }    
    
    
}

////////////////////////////////////////////////////////////////////////////
