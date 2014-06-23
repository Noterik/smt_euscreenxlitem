package org.springfield.lou.application.types.conditions;

import java.util.ArrayList;
import java.util.List;

import org.springfield.lou.fs.FsNode;

public abstract class FilterCondition implements IFilterCondition {
	
	private List<FsNode> passed;

	public FilterCondition() {
		// TODO Auto-generated constructor stub
		this.passed = new ArrayList<FsNode>();
	}
	
	public List<FsNode> getPassed(){
		return passed;
	}
	
	public void clearPassed(){
		this.passed.clear();
	}

}
