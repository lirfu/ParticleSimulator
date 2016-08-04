package com.lirfu.physicssimulator;

import java.awt.Graphics;

import javax.swing.JPanel;

public class Grid extends JPanel {
	private int precision;
	private Position anchor;
	private GRIDTYPE type;
	
	public Grid(int width, int height, int precision, GRIDTYPE type, Position anchor) {
		setSize(width, height);
		this.precision = precision;
		this.anchor = new Position(0, 0);
		this.type = type;
	}
	
	@Override
	public void paint(Graphics g) {
		super.paintComponent(g);
		
		switch (type) {
			case DOT:
				for (int x = 0; x < getWidth(); x += precision) {
					for (int y = 0; y < getHeight(); y += precision) {
						g.drawOval(x, y, 1, 1);
					}
				}
				break;
			case LINE:
				for (int x = 0; x < getWidth(); x += precision)
					g.drawLine(x + (int) (anchor.X % precision), 0, x + (int) (anchor.X % precision), getHeight());
				for (int y = 0; y < getHeight(); y += precision)
					g.drawLine(0, y + (int) (anchor.Y % precision), getWidth(), y + (int) (anchor.Y % precision));
				break;
		}
		
	}
	
	public enum GRIDTYPE {
		DOT, LINE
	}
}
