/**
 * 
 */
package edu.ccce.clustering.dbscan.entity;

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * @author lixuefeng 2007-6-25
 */
public class PointMatrix {

	public static int START_CLUSTER = 1;
	
	int	   clusterID = START_CLUSTER;
	double width = 1024;
	double height = 768;
	double e_distance = 0.1;
	int	   n_neighbor = 5;
	
	
	int backgroud_flag = 0;
	
	/**
	 * save all the point 
	 */
	private ArrayList<RecordPoint> points = new ArrayList<RecordPoint>();
	
	/**
	 *  keep the all the close points 
	 */
	private LinkedHashMap<RecordPoint,ArrayList<RecordPoint>> matrix = new LinkedHashMap<RecordPoint,ArrayList<RecordPoint>>();
	
	
	/**
	 *  keep all the core point 
	 */
	private ArrayList<RecordPoint> corePointList = new ArrayList<RecordPoint>();
	
	
	
	public ArrayList<RecordPoint> dumpPoints(){
		return points;		
	}
	/**
	 * @param from
	 * @param toPoint
	 * 
	 *  add a point to matrix
	 */
	private void addPoint(RecordPoint fromPoint,RecordPoint toPoint){
		// add to martix
		ArrayList<RecordPoint> neighborPoints = null;
		neighborPoints = matrix.get(fromPoint);
		if(neighborPoints == null){
			neighborPoints = new ArrayList<RecordPoint>();
			matrix.put(fromPoint, neighborPoints);
		}
		neighborPoints.add(toPoint);
		
		//	if some point has more than n neighbor,then put it to corePointList
		if(neighborPoints.size() > n_neighbor && corePointList.contains(fromPoint) == false){
			corePointList.add(fromPoint);	
		}
	}
	
	/**
	 * @param point
	 * 
	 * 	push a point to matrix
	 */
	public void pushPoint(RecordPoint point){
		// first,push this point to pool
		points.add(point);
		
		// second, calculate the distance from this point,and add it to matrix if the distance is smaller then e
		for(RecordPoint oldPoint:points){
			if(oldPoint.ID == point.ID)
				continue;
			double distance = RecordPoint.calculateDiscance(oldPoint, point, width, height);
			if(e_distance > distance){
				// double add
				//System.out.println("dis:" + distance);
				addPoint(oldPoint,point);
				addPoint(point,oldPoint);
			}
		}
	}
	
	
	/**
	 *  joint all the core point
	 */
	public void joinCorePoint(){
		// dye all the core point
		for(RecordPoint corePoint:corePointList){
			int rgb = corePoint.getRGB();
			if(corePoint.cls == RecordPoint.DEFAULT_CLUSTER){	// lonely core point
				corePoint.cls = clusterID ++;
				rgb = generateRandomRGB();
			}
			dyeing(corePoint,rgb);
		}
	}
	/**
	 * @param core
	 * 	dye all the core object by core point
	 */
	private void dyeing(RecordPoint corePoint,int rgb){
		int coreCluster = corePoint.cls;
		corePoint.setRGB(rgb);		
		ArrayList<RecordPoint> neighborPoints = matrix.get(corePoint);
		for(RecordPoint neighborPoint:neighborPoints){
			neighborPoint.cls = coreCluster;
			neighborPoint.setRGB(rgb);
		}
	}
	
	
	/**
	 * @param e_distance the e_distance to set
	 */
	public final void setE_distance(double e_distance) {
		this.e_distance = e_distance;
	}
	/**
	 * @param height the height to set
	 */
	public final void setHeight(double height) {
		this.height = height;
	}
	/**
	 * @param n_neighbor the n_neighbor to set
	 */
	public final void setN_neighbor(int n_neighbor) {
		this.n_neighbor = n_neighbor;
	}
	/**
	 * @param width the width to set
	 */
	public final void setWidth(double width) {
		this.width = width;
	}
	/**
	 * @return the height
	 */
	public final double getHeight() {
		return height;
	}
	/**
	 * @return the width
	 */
	public final double getWidth() {
		return width;
	}
	
	public int generateRandomRGB(){
		
		if(backgroud_flag == 0){
			backgroud_flag = 1;
			return 0xffffffff;
		}
		
		int r = (int) Math.round(Math.random()/1.0 *255);
		int g = (int) Math.round(Math.random()/1.0 *255);
		int b = (int) Math.round(Math.random()/1.0 *255);
		int rgb = 0xff000000;
		
		rgb |= ((r << 16) & 0xff0000);
		rgb |= ((g << 8) & 0xff00);
		rgb |= (b & 0xff);
		
		return rgb;
	}
}
