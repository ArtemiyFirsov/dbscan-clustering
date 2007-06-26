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
	double e_distance = 1.5;
	int	   n_neighbor = 5;
	
	
	/**
	 * save all the point 
	 */
	private LinkedHashMap<String,RecordPoint> points = new LinkedHashMap<String,RecordPoint>();
	
	/**
	 *  keep the all the close points 
	 */
	private LinkedHashMap<String,ArrayList<RecordPoint>> matrix = new LinkedHashMap<String,ArrayList<RecordPoint>>();
	
	
	/**
	 *  keep all the core point 
	 */
	private ArrayList<String> corePointList = new ArrayList<String>();
	
	
	
	public LinkedHashMap<String,RecordPoint> dumpPoints(){
		return points;		
	}
	/**
	 * @param from
	 * @param toPoint
	 * 
	 *  add a point to matrix
	 */
	private void addPoint(String from,RecordPoint toPoint){
		// add to martix
		ArrayList<RecordPoint> neighborPoints = null;
		neighborPoints = matrix.get(from);
		if(neighborPoints == null){
			neighborPoints = new ArrayList<RecordPoint>();
			matrix.put(from, neighborPoints);
		}
		neighborPoints.add(toPoint);
		
		//	if some point has more than n neighbor,then put it to corePointList
		if(neighborPoints.size() > n_neighbor){
			corePointList.add(from);	
		}
	}
	
	/**
	 * @param point
	 * 
	 * 	push a point to matrix
	 */
	public void pushPoint(RecordPoint point){
		// first,push this point to pool
		points.put(point.ID,point);
		
		// second, calculate the distance from this point,and add it to matrix if the distance is smaller then e
		for(RecordPoint oldPoint:points.values()){
			double distance = RecordPoint.calculateDiscance(oldPoint, point, width, height);
			if(e_distance < distance){
				// double add
				addPoint(oldPoint.ID,point);
				addPoint(point.ID,oldPoint);
			}
		}
	}
	
	
	/**
	 *  joint all the core point
	 */
	public void joinCorePoint(){
		// dye all the core point
		for(String corePoint:corePointList){
			RecordPoint core = points.get(corePoint);
			if(core.cls == RecordPoint.DEFAULT_CLUSTER){	// lonely core point
				core.cls = clusterID ++;
			}
			dyeing(core);
		}
	}
	/**
	 * @param core
	 * 	dye all the core object by core point
	 */
	private void dyeing(RecordPoint core){
		String coreID = core.ID;
		int    coreCluster = core.cls; 
		ArrayList<RecordPoint> neighborPoints = matrix.get(coreID);
		for(RecordPoint neighborPoint:neighborPoints){
			neighborPoint.cls = coreCluster;
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
}
