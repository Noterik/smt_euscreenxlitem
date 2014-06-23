package org.springfield.lou.application.types.conditions;

public class ConditionCounter implements IConditionCounter {
	
	private int ticks;

	public ConditionCounter() {
		// TODO Auto-generated constructor stub
		this.ticks = 0;
	}

	@Override
	public void tick() {
		// TODO Auto-generated method stub
		ticks++;
	}

	@Override
	public int getTicks() {
		// TODO Auto-generated method stub
		return ticks;
	}
	
	public void reset(){
		ticks = 0;
	}

}
