package source;

import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

import datastructures.core.DataObject;
import datastructures.core.MetaData;

public class DataObjectAutor extends DataObject {
	
	public static final int POS_AUTOR = 0;
//	public static final int POS_VIZINHOS = 0;
	AutorMetaData mt;
	HashMap<String, String> areas;
	TreeSet<String> especializacao = new TreeSet<String>();
	TreeSet<String> areasConhecimento = new TreeSet<String>();
	
	
	public TreeSet<String> getEspecializacao() {
		return especializacao;
	}

	public void setEspecializacao(TreeSet<String> especializacao) {
		this.especializacao = especializacao;
	}

	public TreeSet<String> getAreaConhecimento() {
		return areaConhecimento;
	}

	public void setAreaConhecimento(TreeSet<String> areaConhecimento) {
		this.areaConhecimento = areaConhecimento;
	}

	TreeSet<String> areaConhecimento = new TreeSet<String>();
	
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
		return this.getFeatureList().get(POS_AUTOR).toString();
		//return this.getFeatureList().get(POS_AUTOR).toString()+" "+getEspecializacao().toString();
	}
	
	
	
}