package com.lirfu.physicssimulator;

public abstract class Command {
	private String name, desc;
	
	protected Command(String name, String description) {
		this.name = name;
		this.desc = description;
	}
	
	public String getName() {
		return this.name.toUpperCase();
	}
	
	public String getDescription() {
		return this.desc;
	}
	
	abstract public String execute(Simulator s, String input);
}
