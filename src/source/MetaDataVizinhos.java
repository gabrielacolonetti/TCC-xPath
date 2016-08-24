package source;

import java.util.List;

import datastructures.core.DataFeature;

public class MetaDataVizinhos extends DataFeature {

	private List<String> vizinhos;
	
	public MetaDataVizinhos(List<String> vizinhos) {
		this.vizinhos = vizinhos;
	}
		
	@Override
	public boolean equals(DataFeature arg0) {
		return this.vizinhos.equals(((MetaDataVizinhos)arg0).vizinhos);
	}

}
