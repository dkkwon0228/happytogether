/*
2008ago28: superclasse para custos BP
.calculo fixo para custo estrutural CE
.custo de aparencia CV � virtual (depende de cada problema, ex. spots matching, segmentacao, ...)
*/
package m00;

public abstract class CostBP {

    protected float delta;
    protected float mxdst;   // normalizes ce
    protected float maxPenalty = 1.0f;
    
    protected Ferramentas fe = new Ferramentas();

    public CostBP(float delta, float mxdst) {
        this.delta = delta;
        this.mxdst = mxdst;
    }

    public abstract float getCV(Vertex vi, Vertex vm);

    public float getMaxPenalty() {
        return this.maxPenalty;
    }

    //2008ago28: neste ponto nao considera a adjacencia de GM
    //           pois ela j� � considerada no calculo linear da mensagem (vide sendMsg linear, BP7.java)
    //cE entre um par de arestas (uma aresta de Gi e uma aresta de Gm)
    public float getCE(Vertex vi, Vertex vm, Vertex vi2, Vertex vm2) {
        
        float ca = this.maxPenalty, cm = this.maxPenalty;
        int vm_id = vm.getId();
        int vm2_id = vm2.getId();
//2008mai12:
//.compara Gi e Gm
//.(caso 3 eq 2 consul) penaliza 'rotulos diferentes' qdo nao ha aresta no modelo 
        //if (this.adjm[vm_id][vm2_id] || vm_id == vm2_id) 
        {
            float gmdx = (float)(vm.getx()-vm2.getx());
            float gmdy = (float)(vm.gety()-vm2.gety());
            float gm_module = (float)fe.euclideanDistance(0.0,0.0,gmdx,gmdy);
        
            float gidx = (float)(vi.getx()-vi2.getx());
            float gidy = (float)(vi.gety()-vi2.gety());
            float gi_module = (float)fe.euclideanDistance(0.0,0.0,gidx,gidy);
        
            cm = Math.abs(gi_module-gm_module)/mxdst;
            if (gi_module > 0.0f && gm_module > 0.0f)
                ca = Math.abs( (gidx*gmdx+gidy*gmdy)/(gi_module*gm_module) -1.0f ) / 2.0f;
            else ca = 0.0f;  //deve cair aqui se vm_id == vm2_id
            //else ca = maxpenalty;
        }
        float ce = delta * ca + (1.0f - delta) * cm;
        return ce;
        
        /*
//System.out.println("MSMO Potts...");
//System.exit(0);
        return 0.0f;
        */
    }

}

