package org.springfield.lou.application.types;

import org.springfield.lou.application.types.conditions.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springfield.lou.fs.*;
import org.springfield.lou.application.types.conditions.IFilterCondition;

public class Filter {
	
	private ArrayList<FilterCondition> conditions;
	
	public Filter(){
		this.conditions = new ArrayList<FilterCondition>();
	}
	
	public Filter(ArrayList<FilterCondition> conditions){
		this.conditions = conditions;
	}
	
	public void run(List<FsNode> nodes){
		if(this.conditions.size() > 0){
			for(Iterator<FsNode> iter = nodes.iterator() ; iter.hasNext(); ) {
				FsNode n = (FsNode)iter.next();	
				for(Iterator<FilterCondition> condIter = this.conditions.iterator(); condIter.hasNext();){
					IFilterCondition condition = condIter.next();
					condition.allow(n);
				}
			}	
		}
	}
		
	public List<FsNode> apply(List<FsNode> nodes){
		List<FsNode> results = new ArrayList<FsNode>();
		
		if(this.conditions.size() > 0){
			for(Iterator<FsNode> iter = nodes.iterator() ; iter.hasNext(); ) {
				FsNode n = (FsNode)iter.next();	
				for(Iterator<FilterCondition> condIter = this.conditions.iterator(); condIter.hasNext();){
					IFilterCondition condition = condIter.next();
					if(condition.allow(n)){
						results.add(n);
						break;
					}
				}
			}	
			return results;
		}
		return nodes;
		
	}

	public ArrayList<FilterCondition> getConditions() {
		return conditions;
	}
	
	public void addCondition(FilterCondition condition){
		this.conditions.add(condition);
	}
}
