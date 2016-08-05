import java.util.Random;
import java.util.concurrent.TimeUnit;


public class MatMult {

	public static void main(String[] args) {
		int[][] a = new int[1500][1500];
		int[][] b = new int[1500][1500];
		populateArray(a);
		populateArray(b);
		long startTime = System.nanoTime();
		multiplyMatrices(a,b);
		long endTime = System.nanoTime();
		long runTime = TimeUnit.NANOSECONDS.toMillis(endTime) - TimeUnit.NANOSECONDS.toMillis(startTime);
		System.out.print("Runtime: " + runTime + " ms");

	}

	private static void populateArray(int[][] a) {
		for(int i = 0; i < a.length; i++){
			for(int j = 0; j < a[0].length; j++){
				a[i][j] = new Random().nextInt(5)+1;
			}
		}		
	}

	private static int[][] multiplyMatrices(int a[][], int b[][]) {
		int mA = a.length;
		int nA = a[0].length;
		int mB = b.length;
		int nB = b[0].length;
		if(mB != nA) throw new RuntimeException("Invalid dimensions");
		int[][] c = new int[mA][nB];
		for(int i = 0; i < mA; i++){
			for(int j = 0; j < nB; j++){
				for(int k = 0; k < nA; k++){
					c[i][j] += a[i][k] * b[k][j];
				}
			}
		}
		return c;
	}
}
