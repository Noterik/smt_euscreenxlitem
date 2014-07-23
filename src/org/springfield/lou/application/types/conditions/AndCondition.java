package org.springfield.lou.application.types.conditions;

import java.util.ArrayList;
import java.util.Iterator;

import org.springfield.fs.FsNode;

public class AndCondition extends FilterCondition {
	
	ArrayList<IFilterCondition> conditions;

	public AndCondition() {
		// TODO Auto-generated constructor stub
		this.conditions = new ArrayList<IFilterCondition>();
	}
	
	public AndCondition(ArrayList<IFilterCondition> conditions){
		this.conditions = conditions;
	}
	
	public void add(FilterCondition condition){
		this.conditions.add(condition);
	}

	@Override
	public boolean allow(FsNode node) {
		// TODO Auto-generated method stub
		for(Iterator<IFilterCondition> iter = this.conditions.iterator(); iter.hasNext();){
			IFilterCondition condition = iter.next();
			if(!condition.allow(node)){
				return false;
			}
		}
		return true;
	}

}
