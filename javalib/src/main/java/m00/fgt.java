//Fast Gauss Transform
//2009jul16

package m00;

import java.util.*;

public class fgt{
	private int Dim;
	private int NSources;
	private double[] pSources;
	private double[] pSigmas;
	private double[] pWeights;
	private double bandwid;
	private int pterms;
	private int pd;
	private double[] A_k;
	private int K_N;
	private double ratio_far;
	private double[] xc;
	private int[] xcind;
	private int[] indx;
	private int[] xheads;
	private int[] xboxsz;
	private double rx2;
    private int INT_MAX = 999999999;
	private Date daa;
	private Random rnn;
	private Ferramentas fe = new Ferramentas();

public fgt(int dim, double[] u, double[][] ms, int ns, double h, int p, int K, double e) {
    double[] vs = fe.matrix2vectorCol(ms); //converte matriz para vetor (por coluna)
    this.inicia(dim, u, vs, ns, h, p, K, e);
}

public fgt(int dim, double[] u, double[] x, int ns, double h, int p, int K, double e) {
    this.inicia(dim, u, x, ns, h, p, K, e);
}

public void inicia(int dim, double[] u, double[] x, int ns, double h, int p, int K, double e) {
	this.daa = new Date();
	long seedd = daa.getTime();
	this.rnn = new Random(seedd);
	Dim = dim;
	NSources = ns;	
	pWeights = u;
	pSources = x;
	bandwid = h;
	pterms = p;
	K_N = K;
	ratio_far = e;
	pd = nchoosek(pterms+Dim-1, Dim);
	A_k = new double[pd*K_N];
	xc = new double[K_N*Dim];
	xcind = new int[K_N];
	indx = new int[NSources];
	xheads = new int[K_N];
	xboxsz = new int[K_N];
	rx2 = dkcenter(Dim,NSources,K_N,pSources,xcind,indx,xboxsz);
	Compute_Centers(xc,pSources,Dim,NSources,indx,K_N,xboxsz);
	TaylorExpansion(Dim, pterms);
}

public double[] computeNormalizedWeights(double[][] t) {
    int tott = t[0].length;
    double[] pt = new double[tott];
    this.Compute_v_i(t, pt);
    double min=9999.9, max=0.0;
    for (int i = 0; i < tott; i++) {
        if (pt[i] < min)
            min = pt[i];
        if (pt[i] > max)
            max = pt[i];
    }
    for (int i = 0; i < tott; i++) 
        pt[i] = (pt[i]-min)/(max-min);
    return pt;
}

public void Compute_v_i(double[][] t, double[] weigths) {
    int tott = t[0].length;
    double[] vt = fe.matrix2vectorCol(t);
    this.Compute_v_i(vt, tott, weigths);
}
public void Compute_v_i(double[] y, int mt, double[] v_i) {
	double[] dy = new double[Dim];
	double[] prods = new double[pd];
	int[] heads = new int[Dim];
	for (int m=0; m < mt; m++) {	
		v_i[m] = 0.0;
		int mbase = m*Dim;
		double sumgx = 0.0;
		for (int kn=0; kn < K_N; kn++) {
			int xbase = kn*Dim;
			double sum2 = 0.0;
			for (int i = 0; i < Dim; i++) {
				dy[i] = (y[mbase+i] - xc[xbase+i])/bandwid;
				sum2 += dy[i] * dy[i];
			}
			if (sum2 > ratio_far) continue;
			for (int i = 0; i < Dim; i++)
				heads[i] = 0;
			prods[0] = Math.exp(-sum2);
			for (int k=1, t=1, tail=1; k < pterms; k++, tail=t) {
				for (int i = 0; i < Dim; i++) {
					int head = heads[i];
					heads[i] = t;
					for ( int j = head; j < tail; j++, t++)
						prods[t] = dy[i] * prods[j];
				}
			}
			for (int i = 0; i < pd; i++)
				v_i[m] += A_k[kn*pd+i]*prods[i];
		}
	}
}	

private void Compute_Centers(double[] xc, double[] x, int d, int n, int[] ind, int K, int[] bxsz) {
	for (int i = 0; i < d*K; i++)
		xc[i] = 0.0;
	for (int i = 0, nd = 0; i < n; i++,nd+=d) {
		int ibase = ind[i]*d;
		for (int j = 0; j < d; j++)
			xc[ibase+j] += x[nd+j];
	}
	for (int i = 0, ibase = 0; i < K; i++,ibase+=d)
		for (int j = 0; j < d; j++)
			xc[ibase+j] /= bxsz[i];
}

private void TaylorExpansion(int d, int p) {
	double[] C_k = new double[pd];
	Compute_C_k(d, p, C_k);
	Compute_A_k(xc, C_k);
}

private void Compute_C_k(int d, int p, double[] C_k) {
	int[] heads = new int[d+1];
	int[] cinds = new int[pd];
	for (int i = 0; i < d; i++)
		heads[i] = 0;
	heads[d] = INT_MAX;
	cinds[0] = 0;
	C_k[0] = 1.0;
	for (int k=1, t=1, tail=1; k < p; k++, tail=t) {
		for (int i = 0; i < d; i++) {
			int head = heads[i];
			heads[i] = t;
			for ( int j = head; j < tail; j++, t++) {
				cinds[t] = (j < heads[i+1])? cinds[j] + 1 : 1;
				C_k[t] = 2.0 * C_k[j];
				C_k[t] /= (double) cinds[t];
			}
		}
	}
}

private void Compute_A_k(double[] xc, double[] C_k) {
	double[] dx = new double[Dim];
	double[] prods = new double[pd];
	int[] heads = new int[Dim];
	for (int i = 0; i < pd*K_N; i++)
		A_k[i] = 0.0;
	for (int n=0; n < NSources; n++) {
		int nbase = n*Dim;
		int ix2c = indx[n];
		int ix2cbase = ix2c*Dim;
		double sum = 0.0;
		for (int i = 0; i < Dim; i++) {
			dx[i] = (pSources[nbase+i] - xc[ix2cbase+i])/bandwid;
			sum -= dx[i] * dx[i];
			heads[i] = 0;
		}
		prods[0] = Math.exp(sum);
		for (int k=1, t=1, tail=1; k < pterms; k++, tail=t) {
			for (int i = 0; i < Dim; i++) {
				int head = heads[i];
				heads[i] = t;
				for ( int j = head; j < tail; j++, t++)
					prods[t] = dx[i] * prods[j];
			}
		}
		for (int i = 0; i < pd; i++)
			A_k[ix2c*pd+i] += pWeights[n]*prods[i];
	}
	for (int k = 0; k < K_N; k++) {
		for (int i=0; i < pd; i++)
			A_k[k*pd+i] *= C_k[i];
	}
}

private double dkcenter(int dim, int n, int K, double[] x, int[] cn, int[] cind, int[] cnn) {
	int incu = 1;
	double[] dist_C = new double[n];
    int ind = Math.abs(rnn.nextInt()) % n;
    int icn = 0;
    cn[icn++] = ind;
	int ix_j = 0;
	for (int j = 0; j < n; ix_j += dim, j++) {
		dist_C[j] = (j==ind)? 0.0:ddist(dim, x, ix_j, x, ind*dim);
		cind[j] = 0;
	}
	for(int i = 1; i < K; i++) {	 
		ind = idmax(n,dist_C);
        cn[icn++] = ind;
		ix_j = 0;
		for (int j = 0; j < n; ix_j += dim, j++) {
		        double d = (j==ind)? 0.0:ddist(dim, x, ix_j, x, ind*dim);
			if (d < dist_C[j]) {
				dist_C[j] = d;
				cind[j] = i;
			}
		}
	}
	ind = idmax(n,dist_C);
	double radius = dist_C[ind];
	for (int i = 0; i < K; i++)
		cnn[i] = 0;
	for (int i = 0; i < n; i++)
		cnn[cind[i]]++;
	return Math.sqrt(radius);
}

private int nchoosek(int n, int k) {
	int n_k = n - k;
	if (k < n_k) {
		k = n_k;
		n_k = n - k;
	}
	int  nchsk = 1; 
	for ( int i = 1; i <= n_k; i++) {
		nchsk *= (++k);
		nchsk /= i;
	}
	return nchsk;
}

private double ddist(int d, double[] x, int ix, double[] y, int iy) {
    double t, s = 0.0;
    for (int i = d; i != 0; i--) {
        t = x[ix++] - y[iy++];
        s += t * t;
    }
    return s;
}

private int idmax(int n, double[] x) {
	int k = 0;
	double t = -1.0;
	for (int i = 0; i < n; i++)
	    if( t < x[i] ) {
		t = x[i];
		k = i;
	    }
	return k;
}

}
