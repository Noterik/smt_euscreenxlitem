package org.springfield.lou.application.types.conditions;

import org.springfield.fs.FsNode;

public class NotCondition extends FilterCondition {
	
	private FilterCondition condition;

	public NotCondition(FilterCondition condition) {
		// TODO Auto-generated constructor stub
		this.condition = condition;
	}
	
	@Override
	public boolean allow(FsNode node) {
		// TODO Auto-generated method stub
		return !condition.allow(node);
	}

}
