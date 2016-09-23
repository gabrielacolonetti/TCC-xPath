package source;

import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import datastructures.core.DataObject;
import datastructures.core.MetaData;

public class DataObjectAutor extends DataObject {
	
	public static final int POS_AUTOR = 0;
//	public static final int POS_VIZINHOS = 0;
	 AutorMetaData mt;
	 HashMap<String, String> areas;
	
	public HashMap<String, String> getLista() {
		return areas;
	}

	public void setLista(HashMap<String, String> lista) {
		this.areas = lista;
	}

	public MetaData getMt() {
		return mt;
	}

	public DataObjectAutor(){}
	

	public DataObjectAutor(String autor){
		mt = new AutorMetaData("nome", autor);
		this.addFeature(mt);
	
	//	this.addFeature(new MetaData("nome", autor));
	}
//	public DataObjectAutor(String autor, List<String> vizinhos){
//		this.addFeature(new MetaData("nome", autor));
//		this.addFeature(new MetaDataVizinhos(vizinhos));
//	}
	
	@Override
	public String toString() {
		return this.getFeatureList().get(POS_AUTOR).toString()+" "+getLista().toString() ;
	}
	
	
	
}