
package m00;

public abstract class BP {

    protected int tot_sites, tot_labels, tot_iters;
    protected VertexBP[] refv;
    
    public BP() {
    }
    
    public BP(Graph GI, Graph GM, int iters, float peso, CostBP cost) {
        this.tot_sites = GI.getNumVertices();
        this.tot_labels = GM.getNumVertices();
        this.tot_iters = iters;
        this.refv = new VertexBP[this.tot_sites];

        Vertex[] refvi = GI.getVertices();
        Vertex[] refvm = GM.getVertices();

        //rede de propagacao das mensagens:
        //Dp
        float[][] data = new float[tot_sites][tot_labels];
        for (int i = 0; i < tot_sites; i++)
            for (int value = 0; value < tot_labels; value++) {
                Vertex vi = refvi[i];
                Vertex vm = refvm[value];
                data[i][value] = cost.getCV(vi, vm);
            }

        //V_G = V_{G_I}
        for (int i = 0; i < this.tot_sites; i++) {
            Vertex vi = refvi[i];
            VertexBP v = new VertexBP(vi, cost, refvm, peso, GM.getAdjacencyList(), this.tot_labels);
            this.refv[i] = v;
            v.setDp(data[i]);
        }
        //E_G = E_{G_I}
        int[][] inp_adj = GI.getAdjacencyList();
        for (int i=0; i < this.tot_sites; i++) {
            VertexBP v1 = this.refv[i];
            int k = 0;
            while(k < this.tot_sites && inp_adj[i][k] > -1) {
                int j = inp_adj[i][k++];
                VertexBP v2 = refv[j];
                this.addEdge(v1, v2);
            }
        }

        //graph matching
        this.initMsgBP();
        //this.beliefPropagation();
    }

    public abstract void beliefPropagation();

    
    public void initMsgBP() {
        for (int i=0; i<this.tot_sites; i++) {
            VertexBP v = this.refv[i];
            v.initMsg();
        }
    }
    
    public void oneBeliefPropagation(int iter) {
        //System.out.println( (iter+1) );
        for (int i=0; i<this.tot_sites; i++) {
            VertexBP v = this.refv[i];
            v.sendMsgToAllNeighbors(iter);
        }
    }

    public int[] getResult() {
        return this.getResult(this.tot_iters);
    }
    
    public int[] getResult(int iters) {
        int[] matching = new int[this.tot_sites];
        for (int i=0; i<this.tot_sites; i++) {
            VertexBP v = this.refv[i];
            int best = v.getBelief(iters);
            matching[i] = best;
        }
        return matching;
    }

    public void addEdge(VertexBP v1, VertexBP v2) {
        v1.addNeighbor(v2);
        v2.addNeighbor(v1);
    }
}


