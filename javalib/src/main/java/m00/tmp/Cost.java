/*

2008ago28: custo baseado no Grafo de Deformacao

*/

package m00.tmp;

import m00.Ferramentas;
import m00.Graph;
import m00.Vertex;

// Cost function
// = alpha * cv + (1-alpha) * ce
// = alpha * cv + (1-alpha) * [delta * ca + (1-delta) * cm]
public abstract class Cost {
    protected Graph input;        // G_i
    protected Graph model;        // G_m
    protected DeformationGraph Gd;

    private float alpha;
    private float delta;
    private float mxdst;   //normalizes ce
    
    //debug
    public int maxne = 0;
    
    //adjacency list of the model
    private int[][] ladj;
    
    public Cost(Graph inp, Graph mdl, float alpha, float delta, float mxdst) {
        this.input = inp;
        this.model = mdl;
        this.alpha = alpha;
        this.delta = delta;
        this.mxdst = mxdst;
        this.ladj = model.getAdjacencyList();
        this.Gd = null;
    }
    
    //2008dez11: teste BGM, usa apenas Shape Context...
    public Cost(Graph inp, Graph mdl) {
        this.input = inp;
        this.model = mdl;
        this.alpha = 0.0f;
        this.delta = 0.0f;
        this.mxdst = 0.0f;
        this.ladj = model.getAdjacencyList();
        this.Gd = null;
    }

    public void setGd(DeformationGraph Gd) {
        this.Gd = Gd;
    }
    
    public void setAlpha(float alpha) {
        this.alpha = alpha;
    }
    public float getAlpha() {
        return this.alpha;
    }
    
    // calculates the cost of the candidate label vm for current input vertex
    public float getCost(Vertex vi, Vertex vm) {
        this.Gd.atualiza(vi, vm); // calculates deformation graph
        float[] cost = new float[8];
        for (int i=0; i<8; i++)
            cost[i] = 0.0f;
        modificado(vm, cost);
        // cost[0]=ne, cost[1]=aca, cost[2]=acm
        // cost[3]=ca, cost[4]=cm, cost[5]=ce
        // cost[6]=cv, cost[7]=c
        
        float cv = this.costCV(vi, vm);
        
        float ce = cost[5];
        float c = alpha * cv + (1.0f - alpha)*ce;
        cost[6] = cv;
        cost[7] = c;
        this.Gd.restaura(vm);
        return c;
    }

    // cada problema de casamento tem o seu custo cv particular, 
    // por ex. segmentacao � dist euclidiana RGB, spots � uma formula particular...
    public abstract float costCV(Vertex vi, Vertex vm);

    public void modificado(Vertex vm, float[] cost) { 
        ace_modificado(vm, cost);
        // cost[0]=ne, cost[1]=aca, cost[2]=acm
        float ce = 0.0f;
        float ca = 0.0f;
        float cm = 0.0f;
        int ne = (int) cost[0];
        float aca = cost[1];
        float acm = cost[2];
        if (ne > 0) {
            float cea = aca / (float)(ne);
            float cem = acm / (float)(ne);
            ce = delta*cea + (1.0f - delta)*cem;
            ca = (1.0f-alpha)*delta*cea;
            cm = (1.0f-alpha)*(1.0f - delta)*cem;
        }
        cost[3] = ca;
        cost[4] = cm;
        cost[5] = ce;

        int vm_id = vm.getId();
        if (Gd.cont[vm_id] <= 0) {
            System.out.println("Error: Cost.modificado() !");
            System.exit(1);
        }
        
    }

    // vm2 = candidate label for current input vertex
    public void ace_modificado(Vertex vm2, float[] cost) {
        float acm = 0.0f, aca = 0.0f; // accumulated edge costs: modular and angular
        int vm2_id = vm2.getId();
        int totvm = model.getNumVertices();
        int ne = 0;
        
        // calculates ce for all neighbors of vm2
        int k = 0;
        while(k < totvm && ladj[vm2_id][k] > -1) {
            int i = ladj[vm2_id][k++];
            ne++;
            float grdx = (float)(Gd.x[vm2_id])-(float)(Gd.x[i]);
            Vertex vm1 = Gd.refvm[i];
            float mdx = (float)(vm2.getx()-vm1.getx());
            float grdy = (float)(Gd.y[vm2_id])-(float)(Gd.y[i]);
            float mdy = (float)(vm2.gety()-vm1.gety());
            float gr_module = (float) Ferramentas.euclideanDistance(0.0f,0.0f,grdx,grdy);
            float mdl_module = (float)Ferramentas.euclideanDistance(0.0f,0.0f,mdx,mdy);
            if (gr_module > 0.0f && mdl_module > 0.0f)
                aca = aca + Math.abs( (grdx*mdx+grdy*mdy)/(gr_module*mdl_module) -1.0f ) / 2.0f;
                /*
            else {
                //aca += 1.0f;
                aca += 0.0f;
//@@@@@@2008abr28: acho que nunca deve passar por aqui, pois sempre vm2 != vm1 
//e somente em um caso muito remoto que gr_module == 0 !!!
//System.out.println("ca == 0 --------------------");
            }
            */
            acm = acm + Math.abs(gr_module-mdl_module)/mxdst;
        }
        cost[0] = (float)(ne);
        cost[1] = aca;
        cost[2] = acm;
    
        //debug
        if (maxne < ne) 
            maxne = ne;
    }
}

////////////////////////////////////////////////////////////////////////////

