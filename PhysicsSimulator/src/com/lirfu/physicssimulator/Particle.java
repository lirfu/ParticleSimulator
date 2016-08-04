package com.lirfu.physicssimulator;

import java.awt.Graphics;

import javax.swing.JPanel;

public class Particle extends JPanel {
	private String name;
	private Position position;
	private Vector speed;
	private double mass;
	private double radius;
	
	public Particle(String name, double x, double y, Vector speed, double mass, double radius) {
		this.name = name;
		this.position = new Position(x, y);
		this.speed = speed;
		this.mass = mass;
		this.radius = radius;
	}
	
	public double getMass() {
		return this.mass;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setPosition(double x, double y) {
		this.position = new Position(x, y);
	}
	
	public Position getPosition() {
		return this.position;
	}
	
	public double getRadius() {
		return this.radius;
	}
	
	public Vector getSpeed() {
		return this.speed;
	}
	
	public void setSpeed(Vector v) {
		this.speed = v;
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		int zoom = 100;
		g.fillOval((int) (position.X - radius), (int) (position.Y - radius), (int) (2 * radius), (int) (2 * radius));
		g.drawString(this.name, (int) (position.X), (int) (position.Y - radius));
	}
	
	public void paintWithOffsetAndZoom(Graphics g, Position p, double zoom) {
		super.paint(g);
		g.fillOval((int) ((position.X - radius - p.X) * zoom), (int) ((position.Y - radius - p.Y) * zoom), (int) (2 * radius * zoom),
				(int) (2 * radius * zoom));
		g.drawString(this.name, (int) ((position.X - p.X) * zoom), (int) ((position.Y - radius - p.Y) * zoom));
	}
	
	public boolean isCollision(Particle p) {
		return (this.position.distanceFrom(p.position) - Math.min(this.radius, p.getRadius()) <= (Math.max(this.radius, p.getRadius())));
	}
}
