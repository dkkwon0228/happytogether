/*

2009mar24: Gera pontos de acordo com distribuicao normal(0,1).

--

Verificacao de distrib normal.

Minitab:

1. colar dados coluna
2. Graph, histogram, with fit
3. probability plot
4. Stat, Basic Statiscs, Normality Test, Kolmogorov-Smirnov


Matlab (plota sino):

x=-3:0.1:3;
y=exp(-x.*x/2)/sqrt(2*pi);
plot (x,y);


Derive:

(exp(-x*x/2)/sqrt(2*pi))
.integral definida de -3 a 3: approx 0.9973002039


*/
package m00;

import java.util.*;


public class normal01 {
	private Random rn;
	double res;
	private double x2;
	private boolean x2_valid = false;
	private double sigma;


        //frequency (Normal): 0.1%, 2.1%, 13.6%, 34.1%, 34.1%, 13.6%, 2.1%, 0.1%
        int pos = -1;
        double[] vn;
	//8 intervalos:
        int freq[] = new int[8];
	//(-inf,-3*sigma), (-3,-2): 0, 1
	//(-2,-1), (-1,0): 2, 3
	//(0,1), (1,2): 4, 5
	//(2,3), (3*sigma,inf): 6, 7

	public normal01(int tot) {
		this.commonInit(1.0, tot);
	}
	public normal01(double sigma, int tot) {
		this.commonInit(sigma, tot);
	}
	public double[] getValues() {
		return this.vn;
	}
	public void commonInit(double sigma, int tot) {
		Date da = new Date();
		long seed = da.getTime();
		this.rn = new Random(seed);

		this.sigma = sigma;

		this.vn = new double[tot];
		for (int i = 0; i < tot; i++)
			this.vn[i] = 0.0;

		for (int i = 0; i < tot; i++) {
			double d = gaussian()*sigma;
//this.rn.nextDouble();     // random value in range 0.0 - 1.0
			this.res = d;
			//System.out.println(this.res);

			this.vn[++this.pos] = this.res;
		}

		//this.updateFrequency();
	}
/*
	private void updateFrequency() {
		for (int i = 0; i < 8; i++)
			this.freq[i] = 0;
		int tot = this.vn.length;
		for (int i = 0; i < tot; i++) {
			double v = this.vn[i];
			if (v < -3*sigma)
				this.freq[0]++;
			for (int j = 1; j < 7; j++) {
				int k = j - 1;
				if (v > (-3+k)*sigma && v < (-2+k)*sigma)
					this.freq[j]++;
			}
			if (v > 3*sigma)
				this.freq[7]++;
		}
		System.out.println("Frequencies:");
		for (int i = 0; i < 8; i++) {
			double fr = (double) this.freq[i] / (double) tot;
			System.out.println(fr);
		}
	}
*/
	public double gaussian() {
		double x;
		double x1;
		if (x2_valid) {
			x2_valid = false;
			return x2;
		}
		/*
		* Algorithm P (Polar method for normal deviates),
		* Knuth, D., "The Art of Computer Programming", Vol. 2, 3rd Edition, p. 122
		*/
		do {
			x1 = 2.0 * this.rn.nextDouble() - 1.0;
			x2 = 2.0 * this.rn.nextDouble() - 1.0;
			x = x1 * x1 + x2 * x2;
		} while (x >= 1.0);
		x1 = x1 * Math.sqrt((-2.0) * Math.log(x) / x);
		x2 = x2 * Math.sqrt((-2.0) * Math.log(x) / x);
		x2_valid = true;
		return x1;
	}

/*
	public static void main(String[] args) {  
		if (args.length != 2) {
			System.out.println("usage: test1 sigma tot");
			System.exit(1);
		}
		double sigma = Double.parseDouble(args[0]);
		int tot = Integer.parseInt(args[1]);
		normal01 a = new normal01(sigma, tot);
	}
*/
}

