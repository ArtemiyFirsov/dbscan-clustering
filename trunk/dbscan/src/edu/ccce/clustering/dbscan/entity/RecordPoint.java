/**
 * 
 */
package edu.ccce.clustering.dbscan.entity;

/**
 * @author lixuefeng 2007-6-25
 */
public class RecordPoint {
	
	public static int DEFAULT_CLUSTER = -1;
	
	public int ID = 0;
	public int x = 0;
	public int y = 0;
	public short r = 0;
	public short g = 0;
	public short b = 0;
	public int cls = DEFAULT_CLUSTER;	
	
	protected static int[]  pixels = null;
	
	
	public static void setPixels(int[] p){
		pixels = p;
	}
	public void setRGB(int rgb){
		pixels[ID] = rgb;
	}
	public int getRGB(){
		return pixels[ID];
	}
	
	/**
	 * @param from	the point from
	 * @param to   the point to
	 * @return	the distance entity
	 */
	public static double calculateDiscance(RecordPoint from,RecordPoint to,double width,double height){
		
		
		double pointdistance = -1;
		
		if(from != null && to != null){
			double distance_x = Math.pow((from.x - to.x)/width,2);	// this may calcalate a standarded diff.
			double distance_y = Math.pow((from.y - to.y)/height,2);
			double distance_r = Math.pow((from.r - to.r)/255.0,2);
			double distance_g = Math.pow((from.g - to.g)/255.0,2);
			double distance_b = Math.pow((from.b - to.b)/255.0,2);
			double distance = Math.sqrt(distance_x + distance_y + distance_r + distance_g + distance_b);
			pointdistance = distance;
		}		
		return pointdistance;		
	}



	/**
	 * @param id
	 * @param x
	 * @param y
	 * @param r
	 * @param g
	 * @param b
	 * @param cls
	 */
	protected RecordPoint(int id, int x, int y, short r, short g, short b) {
		super();
		ID = id;
		this.x = x;
		this.y = y;
		this.r = r;
		this.g = g;
		this.b = b;
	}



	/**
	 * @param id
	 * @param r
	 */
	public RecordPoint() {
		super();
	}
}
