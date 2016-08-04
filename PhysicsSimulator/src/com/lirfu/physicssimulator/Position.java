package com.lirfu.physicssimulator;

import java.awt.Point;

public class Position {
	public double X;
	public double Y;
	
	public Position(double x, double y) {
		this.X = x;
		this.Y = y;
	}
	
	public Position(Point p) {
		this.X = p.getX();
		this.Y = p.getY();
	}
	
	/** Adds the X and Y values respectively of given position to this position. */
	public Position add(Position p) {
		this.X += p.X;
		this.Y += p.Y;
		return this;
	}
	
	/** Multiplies the coordinate values with the given factor. */
	public Position multiplyValuesByFactor(double factor) {
		this.X *= factor;
		this.Y *= factor;
		return this;
	}
	
	/** Returns a position containing the relative XY distances of this position from the given position. */
	public Position relativeDistanceFrom(Position p) {
		return new Position(p.X - this.X, p.Y - this.Y);
	}
	
	public double distanceFrom(Position p) {
		return Math.sqrt((this.X - p.X) * (this.X - p.X) + (this.Y - p.Y) * (this.Y - p.Y));
	}
	
	public Position middleFrom(Position p) {
		return new Position((this.X + p.X) / 2, (this.Y + p.Y) / 2);
	}
	
	@Override
	public String toString() {
		return "(" + this.X + " , " + this.Y + ")";
	}
}
