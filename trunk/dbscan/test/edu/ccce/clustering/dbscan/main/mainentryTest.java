package edu.ccce.clustering.dbscan.main;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import junit.framework.TestCase;

public class mainentryTest extends TestCase {

	public static String logFile = "D:\\MyDocuments\\DBScan_Doc\\performance.log";
	
	public class mainTest{
		public String inputFileName = null;
		public String outputFileName = null;
		public String runName = "NoName";
		
		public int    n_neighbor =  10;
		public double e_distance = 0.023;
		public boolean bColorData = false;
		public long	  current_cost = 0;
		public mainTest(int n_neighbor, double e_distance, boolean colorData) {
			super();
			this.n_neighbor = n_neighbor;
			this.e_distance = e_distance;
			bColorData = colorData;
		}
		public long getCurrent_cost() {
			return current_cost;
		}
		public void setInputFileName(String inputFileName) {
			this.inputFileName = inputFileName;
		}
		public void setOutputFileName(String outputFileName) {
			this.outputFileName = outputFileName;
		}
		
		@Override
		public String toString() {
			// TODO Auto-generated method stub
			return "---------------------------- " + runName + " --------------------------------------\r\n"+
					"n_neighbor: " + n_neighbor + "\r\n" +
					"e_distance: " + e_distance + "\r\n" +
					"bColorData: " + bColorData + "\r\n" +
					"inputFileName: " + inputFileName + "\r\n" +
					"outputFileName: " + outputFileName + "\r\n" +
					"current_cost: " + current_cost + " Millis\r\n";
		}
		public void runTest(String runName){
			this.runName = runName;
			mainentry.n_neighbor = n_neighbor;
			mainentry.e_distance = e_distance;
			mainentry.bColorData = bColorData;
			mainentry.inputFileName = inputFileName;
			mainentry.outputFileName = outputFileName;
			mainentry.main(null);
			this.current_cost = mainentry.current_cost;
		}
	}
	
	
	public void testMain() {

		
		String test15File = "D:\\MyDocuments\\DBScan_Doc\\test15.gif";
		String test15File_out1 = "D:\\MyDocuments\\DBScan_Doc\\test15_out1.jpg";
		String test15File_out2 = "D:\\MyDocuments\\DBScan_Doc\\test15_out2.jpg";
		String test15File_out3 = "D:\\MyDocuments\\DBScan_Doc\\test15_out3.jpg";
	
		String test14File = "D:\\MyDocuments\\DBScan_Doc\\test14.gif";
		String test14File_out1 = "D:\\MyDocuments\\DBScan_Doc\\test14_out1.jpg";
		String test14File_out2 = "D:\\MyDocuments\\DBScan_Doc\\test14_out2.jpg";
		String test14File_out3 = "D:\\MyDocuments\\DBScan_Doc\\test14_out3.jpg";
		
		// batch test 15
		mainTest run15_1 = new mainTest(5,0.04,false);
		run15_1.setInputFileName(test15File);
		run15_1.setOutputFileName(test15File_out1);
	
		mainTest run15_2 = new mainTest(5,0.023,false);
		run15_2.setInputFileName(test15File);
		run15_2.setOutputFileName(test15File_out2);
		
		mainTest run15_3 = new mainTest(10,0.023,false);
		run15_3.setInputFileName(test15File);
		run15_3.setOutputFileName(test15File_out3);
		
		// batch test14
		mainTest run14_1 = new mainTest(5,0.04,false);
		run14_1.setInputFileName(test14File);
		run14_1.setOutputFileName(test14File_out1);
		
		mainTest run14_2 = new mainTest(5,0.023,false);
		run14_2.setInputFileName(test14File);
		run14_2.setOutputFileName(test14File_out2);
		
		mainTest run14_3 = new mainTest(10,0.023,false);
		run14_3.setInputFileName(test14File);
		run14_3.setOutputFileName(test14File_out3);
		
		
		
		
		try {
			File outputFile = new File(logFile);
			FileWriter out = new FileWriter(outputFile);
			BufferedWriter bw = new BufferedWriter(out);
			
			// perform them one by one.
			run15_1.runTest("run15_1");
			bw.write(run15_1.toString());
			run15_2.runTest("run15_2");
			bw.write(run15_2.toString());
			run15_3.runTest("run15_3");
			bw.write(run15_3.toString());
			
			run14_1.runTest("run14_1");
			bw.write(run14_1.toString());
			run14_2.runTest("run14_2");
			bw.write(run14_2.toString());
			run14_3.runTest("run14_3");
			bw.write(run14_3.toString());
			
			
			bw.close();
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

		
	}

}
