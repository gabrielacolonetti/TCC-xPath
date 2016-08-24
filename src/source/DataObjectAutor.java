package source;

import java.util.List;

import datastructures.core.DataObject;
import datastructures.core.MetaData;

public class DataObjectAutor extends DataObject {
	
	public static final int POS_AUTOR = 0;
//	public static final int POS_VIZINHOS = 0;
	
	public DataObjectAutor(String autor){
		this.addFeature(new MetaData("nome", autor));
	}
//	public DataObjectAutor(String autor, List<String> vizinhos){
//		this.addFeature(new MetaData("nome", autor));
//		this.addFeature(new MetaDataVizinhos(vizinhos));
//	}
	
	@Override
	public String toString() {
		return this.getFeatureList().get(POS_AUTOR).toString();
	}
	
}