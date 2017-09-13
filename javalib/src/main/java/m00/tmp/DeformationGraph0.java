/*

2009jan08

Para muitos problemas (senao todos?),
o grafo de deformacao pode ser usado apenas
para deformar a estrutura (nao precisa ser
usado para avaliar a aparencia).

Logo, esta classe apenas concretiza a classe abstrata.

*/

package m00.tmp;

import m00.*;

// Deformation graph (Gd)
public class DeformationGraph0 extends DeformationGraph {
    
    public DeformationGraph0(Graph input, Graph model) {
        super(input,model);
    }
    
    public void atualizaAtributos(Vertex vi, Vertex vm) {
        //nao faz nada
    } 

    public void restauraAtributos(Vertex vm) {
        //nao faz nada
    }
    
}

