package org.springfield.lou.application.types.conditions;

import org.springfield.lou.fs.FsNode;

public class TypeCondition extends FilterCondition {
	
	private String allowedValue;

	public TypeCondition(String allowedValue) {
		// TODO Auto-generated constructor stub
		super();
		
		this.allowedValue = allowedValue;
	}

	@Override
	public boolean allow(FsNode node) {
		// TODO Auto-generated method stub
		
		if(node.getName().equals(allowedValue)){
			this.getPassed().add(node);
			return true;
		}
		return false;
	}

	public String getAllowedValue() {
		return allowedValue;
	}

}
