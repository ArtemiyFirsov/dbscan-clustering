/**
 * 
 */
package edu.ccce.clustering.dbscan.entity;

/**
 * @author lixuefeng 2007-6-25
 */
public class RecordPointUtil {
	
	public static int DEFAULT_CLUSTER = -1;
	public static int BACKGROUND_RGB = 0xffffffff;
	public static boolean bColorData = false;
	public static int cls = DEFAULT_CLUSTER;	
	public static int[] dynCount = null;	// dyn count for every point
	protected static int[]  pixels = null;
	
	public static boolean isBackGround(Integer pointID){
		if(bColorData == false && pixels[pointID] == BACKGROUND_RGB)
			return true;
		return false;
	}
	
	public static void setPixels(int[] p){
		pixels = p;
		dynCount = new int[pixels.length];		// must be zero by default.
		
	}
	public static void setRGB(int pointID,int rgb){
		if(dynCount[pointID] == 0){
			pixels[pointID] = rgb;
			dynCount[pointID] ++;
		}		
	}
	public static int getRGB(int pointID){
		return pixels[pointID];
	}
	public static int getDynCount(int pointID){
		return dynCount[pointID];
	}
	
	/**
	 * @param from	the point from
	 * @param to   the point to
	 * @return	the distance entity
	 */
	public static double calculateDiscance(int fromID,int toID,double width,double height){
		// x1,y1 x2,y2
		int x1 = (int) (fromID % (int)width);
		int y1 = (int) (fromID / (int)width);
		int x2 = (int) (toID % (int)width);
		int y2 = (int) (toID /(int)width);
		
		// r1,g1,b1
		int px1 = pixels[fromID];
		int r1 = (px1 >> 16) & 0xff;
		int g1 = (px1 >> 8) & 0xff;
		int b1 = (px1) & 0xff;
		
		// r2,g2,b2
		int px2 = pixels[toID];
		int r2 = (px2 >> 16) & 0xff;
		int g2 = (px2 >> 8) & 0xff;
		int b2 = (px2) & 0xff;
		
		// calculate
		double distance_x = Math.pow((x1 - x2)/width,2);	// this may calcalate a standarded diff.
		double distance_y = Math.pow((y1 - y2)/height,2);
		double distance_r = Math.pow((r1 - r2)/255.0,2);
		double distance_g = Math.pow((g1 - g2)/255.0,2);
		double distance_b = Math.pow((b1 - b2)/255.0,2);
		double distance = Math.sqrt(distance_x + distance_y + distance_r + distance_g + distance_b);
				
		return distance;		
	}

	

	public static boolean isBColorData() {
		return bColorData;
	}

	public static void setBColorData(boolean colorData) {
		bColorData = colorData;
	}

	public static int getBACKGROUND_RGB() {
		return BACKGROUND_RGB;
	}

	public static void setBACKGROUND_RGB(int background_rgb) {
		BACKGROUND_RGB = background_rgb;
	}
}
