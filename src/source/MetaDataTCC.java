package source;

import java.util.List;

import datastructures.core.MetaData;

public class MetaDataTCC extends MetaData{

	public MetaDataTCC(String id, String value) {
		super(id, value);
		// TODO Auto-generated constructor stub
	}
	MetaData autor;
	List<MetaData> vizinhos;
	
	public MetaData getAutor() {
		return autor;
	}
	public void setAutor(MetaData autor) {
		this.autor = autor;
	}
	public List<MetaData> getVizinhos() {
		return vizinhos;
	}
	public void setVizinhos(List<MetaData> vizinhos) {
		this.vizinhos = vizinhos;
	}
	
}
