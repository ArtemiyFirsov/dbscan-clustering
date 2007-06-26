package edu.ccce.clustering.dbscan.entity;

import junit.framework.TestCase;

public class RecordPointTest extends TestCase {

	public RecordPointTest(String name) {
		super(name);
	}

	protected void setUp() throws Exception {
	}

	protected void tearDown() throws Exception {
	}

	public final void testCalculateDiscance() {
		RecordPoint from  = new RecordPoint("0",1024,768,(short)255,(short)255,(short)255);
		RecordPoint to = new RecordPoint("1",0,0,(short)0,(short)0,(short)0);
		
		double distance = RecordPoint.calculateDiscance(from, to,1024,768);
		System.out.println(distance);
		
	}

}
