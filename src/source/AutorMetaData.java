package source;

import datastructures.core.MetaData;

public class AutorMetaData extends MetaData {

	public AutorMetaData(String id, String value) {
		super(id, value);
	}
	
	public String toString() {
		return "<" + this.getValue() +">";
	}

	
	
}
