package org.springfield.lou.application.types.conditions;

import java.util.ArrayList;
import java.util.Iterator;

import org.springfield.fs.FsNode;

public class OrCondition extends FilterCondition {

	ArrayList<FilterCondition> conditions;

	public OrCondition() {
		// TODO Auto-generated constructor stub
		this.conditions = new ArrayList<FilterCondition>();
	}
	
	public OrCondition(ArrayList<FilterCondition> conditions){
		this.conditions = conditions;
	}
	
	public void add(FilterCondition condition){
		this.conditions.add(condition);
	}

	@Override
	public boolean allow(FsNode node) {
		// TODO Auto-generated method stub
		for(Iterator<FilterCondition> iter = this.conditions.iterator(); iter.hasNext();){
			IFilterCondition condition = iter.next();
			if(condition.allow(node)){
				return true;
			}
		}
		return false;
	}

}
