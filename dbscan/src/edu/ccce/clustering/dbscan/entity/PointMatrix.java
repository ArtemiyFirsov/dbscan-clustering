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
	double width = 1024;		// default
	double height = 768;		// default
	double e_distance = 0.1;	// default
	int	   n_neighbor = 5;	// default
		
	/**
	 * save all the point 
	 */
	private ArrayList<Integer> points = new ArrayList<Integer>();
	
	/**
	 *  keep the all the close points 
	 */
	private LinkedHashMap<Integer,ArrayList<Integer>> matrix = new LinkedHashMap<Integer,ArrayList<Integer>>();
	
	
	/**
	 *  keep all the core point 
	 */
	private ArrayList<Integer> corePointList = new ArrayList<Integer>();

	/**
	 * @param from
	 * @param toPoint
	 * 
	 *  add a point to matrix
	 */
	private void addPoint(Integer fromPoint,Integer toPoint){
		// add to martix
		ArrayList<Integer> neighborPoints = null;
		neighborPoints = matrix.get(fromPoint);
		if(neighborPoints == null){
			neighborPoints = new ArrayList<Integer>();
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
	public void pushPoint(Integer pointID){
		// first,push this point to pool
		
		if(RecordPointUtil.isBackGround(pointID) == true)		// skip background point.
			return;
		
		// second, calculate the distance from this point,and add it to matrix if the distance is smaller then e
		for(int id = 0 ; id < pointID ; id ++){
//			if(oldPoint.ID == point.ID)
//				continue;
			double distance = RecordPointUtil.calculateDiscance(id, pointID, width, height);
			if(e_distance > distance){
				// double add
				//System.out.println("dis:" + distance);
				addPoint(id,pointID);
				addPoint(pointID,id);
			}
		}
		
		points.add(pointID);
	}
	
	
	/**
	 *  joint all the core point
	 */
	public void joinCorePoint(){
		// dye all the core point
		for(Integer corePointID:corePointList){
			int rgb = RecordPointUtil.getRGB(corePointID);
			if(RecordPointUtil.getDynCount(corePointID) == 0){	// lonely core point
				rgb = generateRandomRGB();
			}
			dyeing(corePointID,rgb);
		}
	}
	/**
	 * @param core
	 * 	dye all the core object by core point
	 */
	private void dyeing(Integer corePointID,int rgb){
		RecordPointUtil.setRGB(corePointID, rgb);	
		ArrayList<Integer> neighborPoints = matrix.get(corePointID);
		for(Integer neighborPointID:neighborPoints){
			RecordPointUtil.setRGB(neighborPointID, rgb);
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
		int r = (int) Math.round(Math.random()/1.0 *235+10);	// (10~245)
		int g = (int) Math.round(Math.random()/1.0 *235+10);
		int b = (int) Math.round(Math.random()/1.0 *235+10);
		int rgb = 0xff000000;
		
		rgb |= ((r << 16) & 0xff0000);
		rgb |= ((g << 8) & 0xff00);
		rgb |= (b & 0xff);
		
		return rgb;
	}
}
