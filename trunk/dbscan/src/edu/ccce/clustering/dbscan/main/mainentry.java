/**
 * 
 */
package edu.ccce.clustering.dbscan.main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import edu.ccce.clustering.dbscan.entity.PointMatrix;
import edu.ccce.clustering.dbscan.entity.RecordPoint;

/**
 * @author lixuefeng 2007-6-25
 */
public class mainentry {

	public static String inputFileName = "D:\\MyDocuments\\DBScan_Doc\\test1.dbx";
	public static String outputFileName = "D:\\MyDocuments\\DBScan_Doc\\test1_out.dbx";
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		PointMatrix ptMatrix = new PointMatrix();
		
		try {
			File inputFile   = new File(inputFileName);
			FileReader in   = new FileReader(inputFile);
			BufferedReader br = new BufferedReader(in);
			String line = null;
			
			line = br.readLine();
			String[] wh = line.split("\t");
			int w = Integer.parseInt(wh[0]);
			int h = Integer.parseInt(wh[1]);
			
			ptMatrix.setWidth(w);
			ptMatrix.setHeight(h);
			ptMatrix.setN_neighbor(5);
			ptMatrix.setE_distance(1.0);
			
			br.readLine(); // reader header
			
			do{
				RecordPoint recordPoint = new RecordPoint();
				line = br.readLine();
				String[] data = line.split("\t");
				recordPoint.ID = data[0];
				recordPoint.x = Integer.parseInt(data[1]);
				recordPoint.y = Integer.parseInt(data[2]);
				recordPoint.r = (short) Integer.parseInt(data[3]);
				recordPoint.g = (short) Integer.parseInt(data[4]);
				recordPoint.b = (short) Integer.parseInt(data[5]);
				
				ptMatrix.pushPoint(recordPoint);
				
				//System.out.println(line);
			}while(line != null);
			
			br.close();
			in.close();
				
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		ptMatrix.joinCorePoint();
		
		
		
		
		try {
			File outputFile = new File(outputFileName);
			FileWriter out = new FileWriter(outputFile);
			BufferedWriter bw = new BufferedWriter(out);
			
			
			String wh = ptMatrix.getWidth() + "\t" + ptMatrix.getHeight() + "\r\n";
			String header = "PID\tx\ty\tr\tg\tb\tcls\r\n";
			
			bw.write(wh);
			bw.write(header);
			
			ArrayList<RecordPoint> points = ptMatrix.dumpPoints();
			for(RecordPoint pt:points){
				String record = pt.ID + "\t" + pt.x + "\t" + pt.y + "\t" + pt.r + "\t" + pt.g + "\t" + pt.b + "\t" + pt.cls + "\r\n";
				bw.write(record);
			}
			
			bw.close();
			out.close();			
			
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		


	}

}
