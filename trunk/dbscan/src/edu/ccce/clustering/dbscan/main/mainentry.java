/**
 * 
 */
package edu.ccce.clustering.dbscan.main;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.awt.image.PixelGrabber;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

import edu.ccce.clustering.dbscan.entity.PointMatrix;
import edu.ccce.clustering.dbscan.entity.RecordPoint;

/**
 * @author lixuefeng 2007-6-25
 */
public class mainentry {

	public static String inputFileName = "D:\\MyDocuments\\DBScan_Doc\\test6.gif";
	public static String outputFileName = "D:\\MyDocuments\\DBScan_Doc\\test6_out.jpeg";
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		PointMatrix ptMatrix = new PointMatrix();
		
		System.out.println("----------------------------->read input");
		
		
		Image image = Toolkit.getDefaultToolkit().getImage(inputFileName);
		
		ImageObserver objserver = new ImageObserver(){

			public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
				
				return true;
			}
			
		};
		
		int width = image.getWidth(objserver);
		
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		width = image.getWidth(objserver);

		int height = image.getHeight(objserver);

		
		
		int size = width * height;

		int[] pixels = new int[size];

		PixelGrabber pixelgrabber = new PixelGrabber(image, 0, 0, width, height, pixels, 0, width);

		boolean bRet = false;
		try {
			bRet = pixelgrabber.grabPixels(); //读取像素入数组
		 }
		 catch (InterruptedException e){
			 e.printStackTrace();
		 }

		 if(bRet == false){
			 System.out.println("----------------------------->read image false");
			 return;
		 }
	 
		
		ptMatrix.setWidth(width);
		ptMatrix.setHeight(height);
		ptMatrix.setN_neighbor(7);
		ptMatrix.setE_distance(0.05);
		RecordPoint.setPixels(pixels);
		int id = 0;
		for(int w = 0;w < width;w ++){
			 for(int h = 0;h < height;h ++){
				 int pixel = pixels[id];
				 int alpha = (pixel >> 24) & 0xff;
				 int red = (pixel >> 16) & 0xff;
				 int green = (pixel >> 8) & 0xff;
				 int blue = (pixel) & 0xff;
				 
				RecordPoint recordPoint = new RecordPoint();
				recordPoint.ID = id ;
				recordPoint.x = w;
				recordPoint.y = h;
				recordPoint.r = (short)red;
				recordPoint.g = (short)green;
				recordPoint.b = (short)blue;
				ptMatrix.pushPoint(recordPoint);
				System.out.println(id);
				id++;
			 }
		 }
		
		
//		try {
//			File inputFile   = new File(inputFileName);
//			FileReader in   = new FileReader(inputFile);
//			BufferedReader br = new BufferedReader(in);
//			String line = null;
//			
//			line = br.readLine();
//			String[] wh = line.split("\t");
//			int w = Integer.parseInt(wh[0]);
//			int h = Integer.parseInt(wh[1]);
//			

//			
//			br.readLine(); // reader header
//			
//			do{
//				
//				line = br.readLine();
//				if(line != null){
//					RecordPoint recordPoint = new RecordPoint();
//					String[] data = line.split("\t");
//					recordPoint.ID = data[0];
//					recordPoint.x = Integer.parseInt(data[1]);
//					recordPoint.y = Integer.parseInt(data[2]);
//					recordPoint.r = (short) Integer.parseInt(data[3]);
//					recordPoint.g = (short) Integer.parseInt(data[4]);
//					recordPoint.b = (short) Integer.parseInt(data[5]);
//					ptMatrix.pushPoint(recordPoint);
//					System.out.println(line);
//				}
//				
//			}while(line != null);
//			
//			br.close();
//			in.close();
//				
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}catch(Exception e){
//			e.printStackTrace();
//		}
		
		System.out.println("----------------------------->joinCorePoint");
		ptMatrix.joinCorePoint();
		
		
		
		System.out.println("----------------------------->write output");
		
		
	
	
	

		try {
			File outputFile = new File(outputFileName);
			FileOutputStream output = new FileOutputStream(outputFile);
			//BufferedOutputStream out = new BufferedOutputStream (output);
			JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(output);
			
			BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			
			bi.setRGB(0,0,width,height,pixels,0, width);
			
			JPEGEncodeParam param = encoder.getDefaultJPEGEncodeParam(bi);
			param.setQuality(1.0f, true);
			encoder.setJPEGEncodeParam(param);
			try {
			encoder.encode(bi);
			}
			catch(IOException e) {
			e.printStackTrace();
			}
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		

		
		
//		try {
//			File outputFile = new File(outputFileName);
//			FileWriter out = new FileWriter(outputFile);
//			BufferedWriter bw = new BufferedWriter(out);
//			
//			
//			String wh = ptMatrix.getWidth() + "\t" + ptMatrix.getHeight() + "\r\n";
//			String header = "PID\tx\ty\tr\tg\tb\tcls\r\n";
//			
//			bw.write(wh);
//			bw.write(header);
//			
//			ArrayList<RecordPoint> points = ptMatrix.dumpPoints();
//			for(RecordPoint pt:points){
//				String record = pt.ID + "\t" + pt.x + "\t" + pt.y + "\t" + pt.r + "\t" + pt.g + "\t" + pt.b + "\t" + pt.cls + "\r\n";
//				bw.write(record);
//			}
//			
//			bw.close();
//			out.close();			
//			
//			
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}catch(Exception e){
//			e.printStackTrace();
//		}
		
		System.out.println("----------------------------->finished output");

	}

}
