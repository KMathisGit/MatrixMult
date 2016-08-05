     /********************
   ****** Kevin Mathis ******
 **** Parallel Matrix Mult ****
******* February 4, 2016 *******
  ****************************/

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;
import java.util.Scanner;

/*The following class generates 2 matrices storing random double values from 1 to 100.
  Saves the two matrices into 2 separate files (located inside this project folder).
  Then does matrix multiplication on the 2 matrices to generate a third matrix and save it as well.
  Program uses parallel processing through the use of Threads. 
  By splitting the matrices into 4 sections and letting each thread handle its own section.
  Uses 4 threads to match my CPUs 4 cores. 
  ---- Average run-time for 3000x3000 resulting matrix = 110540 ms ----
  */

public class ParallelMatMult {

	//initializing matrices
	public static double[][] a = new double[1500][1500];
	public static double[][] b = new double[1500][1500];
	public static double[][] c = new double[a.length][b[0].length];

	public static void main(String[] args) throws Exception {
		PopulateArray(a); //Method for populating matrices with random numbers (0-5)
		PopulateArray(b);
		WriteArrayFile("a.txt", a); //Method to write matrix to file
		WriteArrayFile("b.txt", b);
		//Initializing threads to use for parallel processing in matrix multiplication
		Thread1 m1 = new Thread1(); 
		Thread2 m2 = new Thread2();
		Thread3 m3 = new Thread3();
		Thread4 m4 = new Thread4();
		long t1 = System.currentTimeMillis(); //Recording time just before threads start start doing matrix multiplication
		m1.start();
		m2.start();
		m3.start();
		m4.start();
		m1.join();
		m2.join();
		m3.join();
		m4.join();
		long t2 = System.currentTimeMillis(); //Recording ending time after all threads have completed
		storeMatrix(c, "results.txt"); //Storing results in file
		System.out.println("Total run-time for matrix multiplication: " + (t2 - t1) + " ms");
	}
	
	private static void PopulateArray(double[][] array) {
		for (int i = 0; i < array.length; i++) {
			for (int j = 0; j < array[0].length; j++) {
				double number = 1 + new Random().nextDouble() * 100;
				array[i][j] = number;
			}
		}
	}
	
	
	private static void WriteArrayFile(String FileName, double[][] array) {
		try {
			FileOutputStream fos = new FileOutputStream(FileName);
			PrintWriter pw = new PrintWriter(fos);
			for (int i = 0; i < array.length; i++) {
				for (int j = 0; j < array[0].length; j++) {
					double number = array[i][j];
					pw.print(number);
					pw.print(" ");
				}
				pw.println();
			}
			pw.close();
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void storeMatrix(double c[][], String filename) throws Exception {
		int rows = c.length;
		int cols = c[0].length;
		PrintWriter pw = new PrintWriter(new FileOutputStream(filename));
		pw.println("rows   " + rows + "      columns     " + cols);
		
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				pw.print(c[i][j] + "    ");
			}
			pw.println();
		}
		pw.close();
	}

	//Creating Thread1 class
	public static class Thread1 extends Thread {

		@Override
		public void run() {
			int i = 0;
			int m = a.length;
			int n = b[0].length;
			int endingRow = (a.length) / 4; //Will run up to the (length/4)th index AKA the first quarter
			System.out.println("Thread1 - Starting on row: " + i + "  Ending on row: " + endingRow);
			//Do matrix multiplication up to the (length/4)th index 
			for (i = 0; i <= endingRow; i++) {
				for (int j = 0; j < n; j++) {
					for (int l = 0; l < b.length; l++) {
						c[i][j] = c[i][j] + a[i][l] * b[l][j];
					}
				}
			}
		}
	}
	
	//Creating Thread2 class
	public static class Thread2 extends Thread {

		@Override
		public void run() {
			int m = a.length;
			int n = b[0].length;
			int startingRow = ((a.length) / 4) + 1; //Will begin handling the elements that Thread1 left off on
			int endingRow = (a.length) / 2 + 1; //Will run up to the third quarter of elements AKA middle
			System.out.println("Thread2 - Starting on row: " + startingRow + "  Ending on row: " + endingRow);

			for (int i = startingRow; i < endingRow; i++) {
				for (int j = 0; j < n; j++) {
					for (int l = 0; l < b.length; l++) {
						c[i][j] = c[i][j] + a[i][l] * b[l][j];
					}
				}
			}
		}
	}

	public static class Thread3 extends Thread {

		@Override
		public void run() {
			int m = a.length;
			int n = b[0].length;
			int startingRow = (a.length) / 2 + 1; //Will begin handling the elements that Thread 2 left off on
			int endingRow = ((3 * (a.length)) / 4) + 1; //Will run up to the last quarter of elements AKA 3/4 or 75%
			System.out.println("Thread3 - Starting on row: " + startingRow + "  Ending on row: " + endingRow);

			for (int i = startingRow; i < endingRow; i++) {
				for (int j = 0; j < n; j++) {
					for (int l = 0; l < b.length; l++) {
						c[i][j] = c[i][j] + a[i][l] * b[l][j];
					}
				}
			}
		}
	}

	public static class Thread4 extends Thread {

		@Override
		public void run() {
			int m = a.length;
			int n = b[0].length;
			int startingRow = ((3 * (a.length)) / 4) + 1; //Will begin where Thread3 left off on
			int endingRow = a.length; //Will run for the rest of the elements
			System.out.println("Thread4 - Starting on row: " + startingRow + "  Ending on row: " + endingRow);

			for (int i = startingRow; i < m; i++) {
				for (int j = 0; j < n; j++) {
					for (int l = 0; l < b.length; l++) {
						c[i][j] = c[i][j] + a[i][l] * b[l][j];
					}
				}
			}
		}
	}
	
}
