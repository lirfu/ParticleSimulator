package com.lirfu.physicssimulator;

public class Engine_Physics {
	/**
	 * Performs an elastic collision on two bodies. Changes their velocities depending on their masses and directions.
	 * @param p1 One particle.
	 * @param p2 Second particle.
	 * @return Are the two bodies merged? Default: false.
	 */
	public static boolean elasticCollision(Particle p1, Particle p2) {
		double m1 = p1.getMass();
		double m2 = p2.getMass();
		double M = m1 + m2;
		double v1x = p1.getSpeed().dx;
		double v2x = p2.getSpeed().dx;
		double v1y = p1.getSpeed().dy;
		double v2y = p2.getSpeed().dy;
		double k = 0.7;
		
		double v1xnew = (m1 * v1x + m2 * v2x - k * m2 * (v1x - v2x)) / M;
		double v2xnew = (m1 * v1x + m2 * v2x - k * m1 * (v2x - v1x)) / M;
		double v1ynew = (m1 * v1y + m2 * v2y - k * m2 * (v1y - v2y)) / M;
		double v2ynew = (m1 * v1y + m2 * v2y - k * m1 * (v2y - v1y)) / M;
		
		p1.setSpeed(new Vector(v1xnew, v1ynew));
		p2.setSpeed(new Vector(v2xnew, v2ynew));
		
		return false;
	}
}
