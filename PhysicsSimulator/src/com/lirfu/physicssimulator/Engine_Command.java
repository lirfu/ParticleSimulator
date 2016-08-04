package com.lirfu.physicssimulator;

import com.lirfu.physicssimulator.commands.Center;
import com.lirfu.physicssimulator.commands.Relations;
import com.lirfu.physicssimulator.commands.Tray;

public class Engine_Command {
	private Simulator simulator;
	
	private static Command[] commands = { new Tray(), new Relations(), new Center() };
	
	public Engine_Command(Simulator simulatorContext) {
		this.simulator = simulatorContext;
	}
	
	public String process(String input) {
		String output = "";
		
		if (input.trim().isEmpty()) {
			return "";
		} else if (input.toUpperCase().equals("HELP")) {
			output = "";
			for (Command c : commands) {
				output += c.getName() + " - " + c.getDescription() + '\n';
			}
			
		} else {
			for (Command c : commands) {
				if (c.getName().equals(input.split(" ")[0].toUpperCase()))
					try {
						output = c.execute(simulator, input.substring(input.indexOf(" ")));
					} catch (StringIndexOutOfBoundsException e) {
						output = "";
					}
			}
		}
		
		if (output.isEmpty())
			return "Command not found!";
		return output;
	}
	
}
