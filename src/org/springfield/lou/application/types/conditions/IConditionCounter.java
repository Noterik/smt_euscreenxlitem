package org.springfield.lou.application.types.conditions;

public interface IConditionCounter {
	public void tick();
	public int getTicks();
	public void reset();
}
