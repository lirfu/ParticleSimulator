package com.lirfu.physicssimulator;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.LinkedList;

import javax.swing.JPanel;

import com.lirfu.physicssimulator.Grid.GRIDTYPE;

public class Simulator extends JPanel {
	private JPanel context = this;
	public LinkedList<Particle> particles;
	public LinkedList<Position> positionTray;
	
	/** Screen refresh time. */
	public int refreshTime = 10;
	/** Time quant between two calculations (screen refreshes). */
	public double timeQuant = 0.1;
	/** Number of particle tray points. */
	public int maxTrayPoints = 700;
	public boolean drawTray = false;
	public boolean drawCoordinateLines = false;
	public boolean drawRelations = true;
	public boolean drawSpeedVector = true;
	public boolean trackCenter = true;
	
	public double zoom = 1;
	private double minZoom = 0.2;
	
	public int time = 0;
	public int skipPoints = 5;
	public Position screenAnchor = new Position(0, 0);
	
	public Simulator(int width, int height) {
		setSize(width, height);
		
		particles = new LinkedList<>();
		positionTray = new LinkedList<>();
		
//		particles.add(new Particle("Planet", 400, 400, new Vector(-13, 0), 1e5, 20));
//		particles.add(new Particle("Sun", 400, 600, new Vector(0.2, 0), 1e15, 20));
		
		particles.add(new Particle("Nucleus 8+", 300, 300, new Vector(0, 0), 1e14, 10));
		
		particles.add(new Particle("Electron 1-", 100, 300, new Vector(0, 6), 1e8, 10));
		particles.add(new Particle("Electron 1-", 500, 300, new Vector(0, -6), 1e8, 10));
		particles.add(new Particle("Electron 1-", 300, 100, new Vector(-6, 0), 1e8, 10));
		particles.add(new Particle("Electron 1-", 300, 500, new Vector(6, 0), 1e8, 10));
		
		particles.add(new Particle("Electron 1-", 150, 300, new Vector(0, 7), 1e8, 10));
		particles.add(new Particle("Electron 1-", 450, 300, new Vector(0, -7), 1e8, 10));
		particles.add(new Particle("Electron 1-", 300, 150, new Vector(-7, 0), 1e8, 10));
		particles.add(new Particle("Electron 1-", 300, 450, new Vector(7, 0), 1e8, 10));
		
		addMouseMotionListener(new MouseMotionListener() {
			@Override
			public void mouseMoved(MouseEvent e) {
			}
			
			@Override
			public void mouseDragged(MouseEvent e) {
				Position mousePosition = new Position(e.getPoint());
				Position screenCenter = new Position(getWidth() / 2, getHeight() / 2);
				
				getGraphics().fillOval((int) screenCenter.X - 5, (int) screenCenter.Y - 5, 10, 10);
				getGraphics().fillOval((int) mousePosition.X - 5, (int) mousePosition.Y - 5, 10, 10);
				getGraphics().drawLine((int) mousePosition.X, (int) mousePosition.Y, (int) screenCenter.X, (int) screenCenter.Y);
				
				if (e.isShiftDown()) { // Zoom
					zoom = mousePosition.distanceFrom(screenCenter) / 100;
				} else { // Reallocate anchor.
					screenAnchor.add(screenCenter.relativeDistanceFrom(mousePosition)).multiplyValuesByFactor(0.9);
				}
			}
		});
		
		start();
	}
	
	public void start() {
		new Thread(new Runnable() {
			public void run() {
				while (true) {
//					synchronized (context) {
					calculateNextFrame();
//					}
					invalidate();
					repaint();
					try {
						synchronized (context) {
							context.wait(refreshTime);
						}
					} catch (InterruptedException e) {
						System.out.println("Interrupted!");
					}
				}
			}
		}).start();
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		synchronized (context) {
			if (zoom < minZoom)
				zoom = minZoom;
			
			g.setColor(Color.decode("#AAAAAA"));
			new Grid(getWidth(), getHeight(), (int) (10 * zoom), GRIDTYPE.LINE, screenAnchor).paint(g);
			
			g.setColor(Color.decode("#AA3333"));
			if (drawTray) {
				for (Position tmp : positionTray) {
					g.fillOval((int) ((tmp.X - screenAnchor.X) * zoom), (int) ((tmp.Y - screenAnchor.Y) * zoom), 3, 3);
				}
				
			}
			if (drawRelations)
				for (int i = 0; i < particles.size() - 1; i++) {
					Position first = particles.get(i).getPosition();
					for (int j = i + 1; j < particles.size(); j++) {
						Position second = particles.get(j).getPosition();
						g.drawLine((int) ((first.X - screenAnchor.X) * zoom), (int) ((first.Y - screenAnchor.Y) * zoom),
								(int) ((second.X - screenAnchor.X) * zoom), (int) ((second.Y - screenAnchor.Y) * zoom));
						Position middle = first.middleFrom(second);
						g.fillOval((int) ((middle.X - screenAnchor.X - 2) * zoom), (int) ((middle.Y - screenAnchor.Y - 2) * zoom), 4, 4);
//						g.drawString(" " + first.distanceFrom(second), (int) (middle.X - screenAnchor.X), (int) (middle.Y - screenAnchor.Y));
					}
				}
			
			for (Particle tmp : particles) {
				g.setColor(Color.decode("#222222"));
				tmp.paintWithOffsetAndZoom(g, screenAnchor, zoom);
				if (drawCoordinateLines) {
					g.setColor(Color.decode("#000000"));
					if ((tmp.getPosition().X - screenAnchor.X) * zoom < 0)
						g.drawString("X: " + (int) ((tmp.getPosition().X - screenAnchor.X) * zoom), 0, (int) ((tmp.getPosition().Y - screenAnchor.Y) * zoom));
					else if ((tmp.getPosition().X - screenAnchor.X) * zoom > getWidth())
						g.drawString("X: " + (int) ((tmp.getPosition().X - screenAnchor.X) * zoom), getWidth() - 50,
								(int) ((tmp.getPosition().Y - screenAnchor.Y) * zoom));
					if ((tmp.getPosition().Y - screenAnchor.Y) * zoom < 0)
						g.drawString("Y: " + (int) ((tmp.getPosition().Y - screenAnchor.Y) * zoom), (int) ((tmp.getPosition().X - screenAnchor.X) * zoom), 10);
					else if ((tmp.getPosition().Y - screenAnchor.Y) * zoom > getHeight())
						g.drawString("Y: " + (int) ((tmp.getPosition().Y - screenAnchor.Y) * zoom), (int) ((tmp.getPosition().X - screenAnchor.X) * zoom),
								getHeight());
					
					g.drawLine(0, (int) ((tmp.getPosition().Y - screenAnchor.Y) * zoom), getWidth(), (int) ((tmp.getPosition().Y - screenAnchor.Y) * zoom));
					g.drawLine((int) ((tmp.getPosition().X - screenAnchor.X) * zoom), 0, (int) ((tmp.getPosition().X - screenAnchor.X) * zoom), getHeight());
				}
				if (drawSpeedVector) {
					g.setColor(Color.decode("#00FF00"));
					
					int endX = (int) tmp.getPosition().X;
					if ((int) (tmp.getSpeed().dx) < 0)
						endX -= tmp.getRadius();
					else if ((int) (tmp.getSpeed().dx) > 0)
						endX += tmp.getRadius();
					int endY = (int) tmp.getPosition().Y;
					if ((int) (tmp.getSpeed().dy) < 0)
						endY -= tmp.getRadius();
					else if ((int) (tmp.getSpeed().dy) > 0)
						endY += tmp.getRadius();
					
					g.drawLine((int) ((tmp.getPosition().X - screenAnchor.X) * zoom), (int) ((tmp.getPosition().Y - screenAnchor.Y) * zoom),
							(int) ((endX - screenAnchor.X) * zoom), (int) ((endY - screenAnchor.Y) * zoom));
				}
			}
			if (time <= 400) {
				g.setColor(Color.decode("#AABFFF"));
				g.fillRect(getWidth() - 200, 0, 200, 30);
				g.setColor(Color.decode("#000000"));
				g.drawString("Mouse drag to move around.", getWidth() - 190, 10);
				g.drawString("Shift + Mouse drag to zoom.", getWidth() - 190, 20);
			}
		}
	}
	
	/** Calculates the next position for all particles. */
	private void calculateNextFrame() {
		LinkedList<Particle> new_list = new LinkedList<>();
		// Calculate changes for all particles.
		for (int i = 0; i < particles.size(); i++) {
			Particle p_this = particles.get(i);
			double aX = 0;
			double aY = 0;
			
			// Calculate acceleration from forces of other particles on this particle.
			for (int j = 0; j < particles.size(); j++) {
				if (j == i)
					continue;
				Particle p_tmp = particles.get(j);
				
				if (p_this.isCollision(p_tmp)) {
				} else {
					double angle = Math.acos((p_tmp.getPosition().X - p_this.getPosition().X) / p_tmp.getPosition().distanceFrom(p_this.getPosition()));
					angle *= (p_tmp.getPosition().Y - p_this.getPosition().Y < 0 ? -1 : 1);
					
					aX += Constants.Gravitational * p_tmp.getMass() / Math.pow(p_this.getPosition().distanceFrom(p_tmp.getPosition()), 2) * Math.cos(angle);
					aY += Constants.Gravitational * p_tmp.getMass() / Math.pow(p_this.getPosition().distanceFrom(p_tmp.getPosition()), 2) * Math.sin(angle);
				}
			}
			
			// new speed vector.
			double vX = p_this.getSpeed().dx + aX * timeQuant;
			double vY = p_this.getSpeed().dy + aY * timeQuant;
//			System.out.println(p_this.getName() + new Position(vX, vY));
			// new particle position.
			double newX = p_this.getPosition().X + p_this.getSpeed().dx * timeQuant + 0.5 * aX * timeQuant * timeQuant;
			double newY = p_this.getPosition().Y + p_this.getSpeed().dy * timeQuant + 0.5 * aY * timeQuant * timeQuant;
			
			Particle newParticle = new Particle(p_this.getName(), newX, newY, new Vector(vX, vY), p_this.getMass(), p_this.getRadius());
			
			// Calculate collisions.
			new_list.forEach((Particle p) -> {
				if (p.isCollision(newParticle)) {
//					System.out.println("Collision! " + p.getName() + " with " + newParticle.getName());
					Engine_Physics.elasticCollision(p, newParticle);
				}
			});
			// Store the new 'particle' (position).
			new_list.add(newParticle);
			
			// Add this position to tray.
			if (drawTray && time % skipPoints == 0) {
				while (positionTray.size() >= maxTrayPoints)
					positionTray.removeFirst();
				positionTray.add(new Position(newX, newY));
			}
			if (trackCenter) {
				// Find the new system center.
				Position systemCenter = new Position(0, 0);
				for (Particle p : new_list) {
					systemCenter.X += p.getPosition().X;
					systemCenter.Y += p.getPosition().Y;
				}
				systemCenter.X /= new_list.size();
				systemCenter.Y /= new_list.size();
				systemCenter.X -= getWidth() / 2 / zoom;
				systemCenter.Y -= getHeight() / 2 / zoom;
				screenAnchor = systemCenter;
			}
		}
		this.particles = new_list;
		time++;
	}
}
