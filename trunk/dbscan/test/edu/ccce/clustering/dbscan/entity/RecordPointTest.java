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

		double distance = RecordPointUtil.calculateDiscance(1212, 44554,1024,768);
		System.out.println(distance);
		
	}

}
