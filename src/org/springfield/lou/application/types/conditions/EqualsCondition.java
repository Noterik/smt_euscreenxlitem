package org.springfield.lou.application.types.conditions;

import org.springfield.fs.FsNode;

public class EqualsCondition extends FilterCondition {
	
	private String field;
	private String allowedValue;
	private String fieldSeperator = ",";
	private boolean trim = true;
	private boolean caseSensitive = false;
	
	public EqualsCondition(String field, String allowedValue) {
		// TODO Auto-generated constructor stub
		this.field = field;
		this.allowedValue = allowedValue;
	}
	
	public EqualsCondition(String field, String allowedValue, boolean caseSensitive){
		this(field, allowedValue);
		this.caseSensitive = caseSensitive;
	}
	
	public EqualsCondition(String field, String allowedValue, String fieldSeperator){
		this(field, allowedValue);
		this.fieldSeperator = fieldSeperator;
	}
	
	public EqualsCondition(String field, String allowedValue, boolean caseSensitive, String fieldSeperator){
		this(field, allowedValue, caseSensitive);
		this.fieldSeperator = fieldSeperator;
	}

	@Override
	public boolean allow(FsNode node) {
		String value;
		if(field.equals("id")){
			value = node.getId();
		}else{
			value = node.getProperty(field);
		}
		
		if(value != null){
			
			if(!caseSensitive){
				value = value.toLowerCase();
				allowedValue = allowedValue.toLowerCase();
			}
			
			if(trim){
				value = value.trim();
			}
			
			String[] values;
			if(fieldSeperator != null && !fieldSeperator.equals("")){
				values = value.split(fieldSeperator);
			}else{
				values = new String[]{value};
			}
						
			for(int i = 0; i < values.length; i++){
				String singleValue = values[i];
				
				if(allowedValue.equals(singleValue)){
					this.getPassed().add(node);
					return true;
				}
				
			}
		}
		return false;
	}
	
	public String getField(){
		return field;
	}
	
	public String getAllowedValue(){
		return this.allowedValue;
	}

}
