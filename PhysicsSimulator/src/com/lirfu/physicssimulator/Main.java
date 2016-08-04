package com.lirfu.physicssimulator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.swing.JFrame;

public class Main extends JFrame {
	private Simulator simulator;
	
	public static void main(String[] args) {
		Main myFrame = new Main();
		myFrame.setSize(1024, 600);
		myFrame.setLocationRelativeTo(null);
		myFrame.setDefaultCloseOperation(myFrame.EXIT_ON_CLOSE);
		myFrame.setVisible(true);
	}
	
	public Main() {
		simulator = new Simulator(600, 400);
		add(simulator);
		new Thread(terminal).start();
	}
	
	private Runnable terminal = new Runnable() {
		@Override
		public void run() {
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			Engine_Command term = new Engine_Command(simulator);
			
			System.out.println("Terminal simulation started! (for help enter: help)");
			System.out.println("Enter commands:");
			try {
				while (true) {
					System.out.print("$> ");
					String input = reader.readLine();
					
					System.out.println(term.process(input));
				}
			} catch (IOException e) {
				System.out.println("Error opening input stream.");
			}
		}
	};
}
