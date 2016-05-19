package tletters.featureextraction;

import org.apache.commons.math3.complex.Complex;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;

public class ZernikeMoments implements ExtractionAlgorithm {

    public ZernikeMoments(){}

    private int imsize;
    private static int order = 12;  //for order<13 works well, but for larger odrder there are numerical errors

    public double[] extractFeatures(BufferedImage bufferedImage) {

        if(bufferedImage == null){
            System.out.println("There is no image!");
            return null;
        }

        if(bufferedImage.getHeight() != bufferedImage.getWidth()){
            System.out.println("the image is not square, it will be cropped!");
            if(bufferedImage.getHeight() < bufferedImage.getWidth()){
                imsize=bufferedImage.getHeight();
            }else{
                imsize=bufferedImage.getWidth();
            }
        }else{
            imsize=bufferedImage.getHeight();
        }
        int[][] pixels = new int[imsize][imsize]; //cutting and prepearing image
        for( int i = 0; i < imsize; i++ ){
            for( int j = 0; j < imsize; j++ ) {
                int temp = bufferedImage.getRGB(i, j);
                if (temp == -1) {
                    pixels[j][i] = 1;
                } else {
                    pixels[j][i] = 0;
                }
            }
        }

        MyZernike zs1=zernike_bf(imsize,order); //computing Zernike base functions
        Complex[] v1=zernike_mom(pixels,zs1);   //computing Zernike moments

        double[] v1ABS = new double[v1.length]; //getting abs from complex moments
        for(int i=0; i<v1.length; i++)
            v1ABS[i] = v1[i].abs();

        return v1ABS;
    }


    private MyZernike zernike_bf(int SZ, int ORDER){
        int[] F=factorial(ORDER);

        ArrayList<int[]> pq=zernike_orderlist(ORDER);
        int len = pq.size();
        double szh=SZ/2;

        int[][] pqind = new int[2*ORDER+1][2*ORDER+1];
        for(int[] row: pqind)
            Arrays.fill(row, -1);


        ArrayList<int[]> src = new ArrayList<int[]>();
        for(int[] temp:pq){
            src.add(temp.clone());
        }

        for(int[] temp : src){
            temp[0] += ORDER+1;
            temp[1] += ORDER+1;
        }

        for(int i=0; i<len; i++){
            pqind[src.get(i)[0]-1][src.get(i)[1]-1] = i;
        }

        int[][][] Rmns=new int[1+2*ORDER+1][1+2*ORDER+1][1+2*ORDER+1];
        for(int[][] temp1: Rmns)
            for(int[] temp2: temp1)
                Arrays.fill(temp2, 0);

        int min = len;
        for(int flat=0; flat<min; flat++ ){
            int m=pq.get(flat)[0];
            int n=pq.get(flat)[1];
            int mpnh=(int)Math.floor((m+Math.abs(n))/2);
            int mmnh=(int)Math.floor((m-Math.abs(n))/2);
            for(int s=0; s<=mmnh; s++){
                Rmns[ORDER+m][ORDER+n][s]=(((int)Math.pow(-1, s))*F[m-s])/(F[s]*F[mpnh-s]*F[mmnh-s]);
            }
        }

        double rho, theta;

        Complex[][][] ZBF=new Complex[SZ][SZ][len];
        for(int i=0; i<SZ; i++)
            for(int j=0; j<SZ; j++)
                for(int k=0; k<len; k++)
                    ZBF[i][j][k] = new Complex(0,0);

        for(int y=1; y<=SZ; y++){
            for(int x=1; x<=SZ; x++){
                rho=Math.sqrt(Math.pow(szh-x, 2)+Math.pow(szh-y, 2));
                theta=Math.atan2(szh-y,szh-x);

                if(rho>szh) continue;
                rho=rho/szh;
                if(theta<0) theta=theta+2*Math.PI;

                for(int flat=0; flat<len; flat++) {
                    int m = pq.get(flat)[0];
                    int n = pq.get(flat)[1];

                    double R = 0;

                    for(int s = 0; s<=((m-Math.abs(n))/2); s++) {
                        R = R + Rmns[ORDER + m][ORDER + n][s] * (Math.pow(rho, (m - 2 * s)));

                    }
                    ZBF[y-1][x-1][flat] = multyply(new Complex(R,0) , exp(new Complex(0, n * theta)) );
                }
            }
        }
        return new MyZernike(ORDER, pq, pqind, ZBF);
    }

    public Complex multyply(Complex a, Complex b) {
        return new Complex(a.getReal()*b.getReal() - a.getImaginary()*b.getImaginary(),
                a.getReal()*b.getImaginary() + a.getImaginary()*b.getReal());
    }

    public Complex exp(Complex c) {
        return new Complex(Math.exp(c.getReal()) * Math.cos(c.getImaginary()), Math.exp(c.getReal()) * Math.sin(c.getImaginary()));
    }


    private Complex[] zernike_mom(int[][] I, MyZernike ZBFSTR){
        Complex[][][] bf = ZBFSTR.bf;
        ArrayList<int[]> pq=ZBFSTR.orders;
        int[][] id = ZBFSTR.index;

        int len = pq.size();
        Complex[] Z = new Complex[len];
        for(int i=0; i<len; i++)
            Z[i] = new Complex(0,0);

        for(int flat=0; flat<len; flat++){
            int m=pq.get(flat)[0];
            Z[flat] = new Complex((m+1)/Math.PI);
            Complex temp = new Complex(0,0);
            for(int x=0; x<I[0].length; x++){
                for(int y=0; y<I[0].length; y++){
                    temp = temp.add(multyply(new Complex(I[x][y],0) , bf[x][y][flat].conjugate()));
                }
            }

            Z[flat] = multyply(Z[flat],temp);
        }
        return Z;
    }

    public class MyZernike{
        int ORDER;
        ArrayList<int[]> orders;
        int[][] index;
        Complex[][][] bf;

        public MyZernike(int ORDER, ArrayList<int[]> orders, int[][] index, Complex[][][] bf){
            this.ORDER = ORDER;
            this.orders = orders;
            this.index = index;
            this.bf = bf;
        }
    }

    private int[] factorial(int n){
        int[] output = new int[n+1];
        output[0] = 1;
        for(int i=1; i<=n; i++){
            output[i] = output[i-1]*i;
        }
        return output;
    }

    private ArrayList<int[]> zernike_orderlist(int order){
        ArrayList<int[]> PQ = new ArrayList<int[]>();
        for(int p=0; p<=order; p++){
            for(int q=0; q<=p; q++){
                if(Math.abs(p-q)%2 == 0){
                    PQ.add(new int[] {p, q});
                }
            }
        }
        return PQ;
    }
}