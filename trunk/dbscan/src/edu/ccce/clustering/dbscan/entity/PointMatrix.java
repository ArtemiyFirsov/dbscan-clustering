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

	double width = 1024;
	double height = 768;
	double e_distance = 1.5;
	int	   n_neighbor = 5;
	
	private LinkedHashMap<String,RecordPoint> points = new LinkedHashMap<String,RecordPoint>();
	private LinkedHashMap<String,ArrayList<String>> matrix = new LinkedHashMap<String,ArrayList<String>>();
	private ArrayList<String> corePointList = new ArrayList<String>();
	
	
	
	public LinkedHashMap<String,RecordPoint> dumpPoints(){
		return points;		
	}
	protected void addPoint(String from,String to){
		// add to martix
		ArrayList<String> neighbor = null;
		neighbor = matrix.get(from);
		if(neighbor == null){
			neighbor = new ArrayList<String>();
			matrix.put(from, neighbor);
		}
		neighbor.add(to);
		
		//	if some point has more than n neighbor,then put it to corePointList
		if(neighbor.size() > n_neighbor){
			corePointList.add(from);	
		}
	}
	
	public void pushPoint(RecordPoint point){
		// first,push this point to pool
		points.put(point.ID,point);
		
		// second, calculate the distance from this point,and add it to matrix if the distance is smaller then e
		for(RecordPoint oldPoint:points.values()){
			double distance = RecordPoint.calculateDiscance(oldPoint, point, width, height);
			if(e_distance < distance){
				// double add
				addPoint(oldPoint.ID,point.ID);
				addPoint(point.ID,oldPoint.ID);
			}
		}
	}
	
	public void clusteringMatrix(){
		
		// join two corePoint if they are too close.
		// set them to the same cluster
	}
	

}
