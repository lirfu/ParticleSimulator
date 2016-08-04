package com.lirfu.physicssimulator;

public class Vector {
	public double dx;
	public double dy;
	
	public Vector(double dx, double dy) {
		this.dx = dx;
		this.dy = dy;
	}
	
	public double getModule() {
		return Math.sqrt(dx * dx + dy * dy);
	}
	
	@Override
	public String toString() {
		return "dx=" + dx + " dy=" + dy;
	}
}
