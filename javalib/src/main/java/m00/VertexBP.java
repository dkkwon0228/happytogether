
package m00;

import java.util.*;


public class VertexBP {
    private LinkedList neighbors;
    private VertexBP[] refv; // referencias para os vizinhos
    private float[][][] msg; // msg recebida pelos vizinhos; 1 para cada rotulo; par/impar
    private int totviz;
    private float[] Dp; // custo de aparencia, para cada rotulo
    private Consts cts = new Consts();
    private float INF;

    private Vertex vi; //referencia deste vertice ao seu correspondente em G_I
    private CostBP cost;
    private Vertex[] refvm;
    private float peso;
    private int[][] mdl_adj; //lista de adjacencia do modelo para calculo linear das mensagens
    private int tot_labels;
    private float maxPenalty;


    public VertexBP(Vertex vi, CostBP cost, Vertex[] refvm, float peso, int[][] mdl_adj, int tot_labels) {
        this.neighbors = new LinkedList();
        this.vi = vi;
        this.cost = cost;
        this.refvm = refvm;
        this.peso = peso;
        this.mdl_adj = mdl_adj;
        this.tot_labels = tot_labels;
        this.INF = cts.INF;
        this.maxPenalty = this.cost.getMaxPenalty();
        //this.maxPenalty = 0.1f;//this.cost.getMaxPenalty();
    }

    public void sendMsg(VertexBP q, int k) {
        this.sendMsgLinear(q, k);
        //this.sendMsgQuadratico(q, k);
    }

    //linear
    public void sendMsgLinear(VertexBP q, int k) {
        //calcula soma minima das mensagens com ce=MaxPenalty
        float minsum = INF;
        for (int fp = 0; fp < this.tot_labels; fp++) {
            float sum = 0.0f;
            for (int i=0; i < this.totviz; i++)
                if (this.refv[i] != q)
                    sum = sum + this.msg[i][fp][k];
            sum = sum + this.Dp[fp]; 
            if (sum < minsum) 
                minsum = sum;
        }
        
        float[] dst = new float[this.tot_labels];
        for (int fq = 0; fq < this.tot_labels; fq++) {
            float min = minsum + this.peso*this.maxPenalty; //usa penalizacao maxima de ce=1
            int kk = 0;
            //usa adjacencia do Modelo para calculo linear
            while(kk < this.tot_labels && this.mdl_adj[fq][kk] > -1) { 
                int fp = this.mdl_adj[fq][kk++];
                float sum = 0.0f;
                for (int i=0; i < this.totviz; i++)
                    if (this.refv[i] != q)
                        sum = sum + this.msg[i][fp][k];
                sum = sum + this.Dp[fp]; 
                
                //v1 = p    e    v2 = q
                Vertex vi1 = this.vi;
                Vertex vm1 = this.refvm[fp];
                VertexBP v2 = q;
                Vertex vi2 = v2.getVi();
                Vertex vm2 = this.refvm[fq];
                float V = this.peso * this.cost.getCE(vi1, vm1, vi2, vm2);
                
                float tmp = sum + V;
                if (tmp < min) 
                    min = tmp;
            }
            //considera fp == fq:
            {
                int fp = fq;
                float sum = 0.0f;
                for (int i=0; i < this.totviz; i++)
                    if (this.refv[i] != q)
                        sum = sum + this.msg[i][fp][k];
                sum = sum + this.Dp[fp];
                
                //v1 = p    e    v2 = q
                Vertex vi1 = this.vi;
                Vertex vm1 = this.refvm[fp];
                VertexBP v2 = q;
                Vertex vi2 = v2.getVi();
                Vertex vm2 = this.refvm[fq];
                float V = this.peso * this.cost.getCE(vi1, vm1, vi2, vm2);
                
                float tmp = sum + V;
                if (tmp < min) 
                    min = tmp;
            }
            
            dst[fq] = min;
        }
        // normalize
        float val = 0;
        for (int value = 0; value < this.tot_labels; value++) 
            val += dst[value];
        
        val /= this.tot_labels;
        for (int value = 0; value < this.tot_labels; value++) 
            dst[value] -= val;
    
        //devo ajustar a msg de q (recebida de p)
        q.receiveMsg(this, (k+1)%2, dst);
    }

    // quadratico:
    public void sendMsgQuadratico(VertexBP q, int k) {
        int total_rotulos = this.tot_labels;
        float[] dst = new float[total_rotulos];
        for (int fq = 0; fq < total_rotulos; fq++) {
            float min = INF;
            for (int fp = 0; fp < total_rotulos; fp++) {
                float sum = 0.0f;
                for (int i=0; i<totviz; i++)
                    if (refv[i] != q)
                        sum = sum + msg[i][fp][k];
                sum = sum + Dp[fp];
                
                //v1 = p    e    v2 = q
                Vertex vi1 = this.vi;
                Vertex vm1 = this.refvm[fp]; //@@@@@@@@ verificar se ordem importa !!!!!!!!!
                VertexBP v2 = q;
                Vertex vi2 = v2.getVi();
                Vertex vm2 = this.refvm[fq];
                float V = this.peso * this.cost.getCE(vi1, vm1, vi2, vm2);
                
                float tmp = sum + V;
                if (tmp < min) 
                    min = tmp;
            }
            dst[fq] = min;
        }
        // normalize
        float val = 0;
        for (int value = 0; value < total_rotulos; value++) 
            val += dst[value];
        
        val /= total_rotulos;
        for (int value = 0; value < total_rotulos; value++) 
            dst[value] -= val;
    
        //devo ajustar a msg de q (recebida de p)
        q.receiveMsg(this, (k+1)%2, dst);
    }

    public Vertex getVi() {
        return this.vi;
    }


    public void addNeighbor(VertexBP v) {
        this.neighbors.addLast(v);
    }

    public LinkedList getNeighbors() {
        return this.neighbors;
    }

    public void setDp(float[] Dp) {
        this.Dp = Dp;
    }
    
    public int getNumNeighbors() {
        return totviz;
    }
    
    public void initMsg() {
        this.totviz = this.neighbors.size();
        this.refv = new VertexBP[this.totviz];
        this.msg = new float[this.totviz][this.tot_labels][2];
        Iterator it = this.neighbors.iterator();
        int i = 0;
        while (it.hasNext()) { 
            VertexBP v = (VertexBP)it.next();
            this.refv[i] = v;
            for (int j=0; j < this.tot_labels; j++)
                for (int k=0; k<2; k++)
                    this.msg[i][j][k] = 0.0f;
            i++;
        }
    }

    public void receiveMsg(VertexBP p, int k, float[] dst) {
        int ind_p = 0;
        for (int i=0; i < this.totviz; i++)
            if (this.refv[i] == p) {
                ind_p = i;
                break;
            }
        for (int i=0; i < this.tot_labels; i++)
            this.msg[ind_p][i][k] = dst[i];
    }
    public void sendMsgToAllNeighbors(int iter) {
        int k = iter % 2;
        for (int i=0; i < this.totviz; i++) {
            VertexBP q = this.refv[i];
            this.sendMsg(q, k);
        }
    }
    //total de iters at� o momento...
    public int getBelief(int total_iters) {
        int k = total_iters % 2;
        int best = 0;
        float best_val = INF;
        for (int value = 0; value < this.tot_labels; value++) {
            float val = this.Dp[value];
            for (int i=0; i < this.totviz; i++)
                val = val + this.msg[i][value][k];
            if (val < best_val) {
                best_val = val;
                best = value;
            }
        }
        return best;
    }

    //preciso devolver o custo para desempatar spots
    public float[] getBelief2(int total_iters) {
        int k = total_iters % 2;
        int best = 0;
        float best_val = INF;
        for (int value = 0; value < this.tot_labels; value++) {
            float val = this.Dp[value];
            for (int i=0; i < this.totviz; i++)
                val = val + this.msg[i][value][k];
            if (val < best_val) {
                best_val = val;
                best = value;
            }
        }
        float[] res = {best,best_val};
        return res;
    }

}

