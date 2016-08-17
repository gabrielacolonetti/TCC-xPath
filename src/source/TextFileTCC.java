package source;

import java.util.ArrayList;
import java.util.List;

import datastructures.implementations.datatypes.TextFile;

public class TextFileTCC extends TextFile {
	private List<JaccardSimilaridade> listaDeCoeficientes = new ArrayList<JaccardSimilaridade>();
	
	
	public List<JaccardSimilaridade> getListaDeCoeficientes() {
		return listaDeCoeficientes;
	}


	public void setListaDeCoeficientes(List<JaccardSimilaridade> listaDeCoeficientes) {
		this.listaDeCoeficientes = listaDeCoeficientes;
	}

	

	public TextFileTCC(String title, String content, int index) {
		super(title, content, index);
	}
}