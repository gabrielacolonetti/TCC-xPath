package model;

import java.util.HashMap;
import java.util.Set;
import java.util.TreeSet;

public class Pessoa {
	private String nome;
	private String id;
	private HashMap<String,String> areas = new HashMap<String, String>();
	TreeSet<String> especializacao = new TreeSet<String>();
	TreeSet<String> areaConhecimento = new TreeSet<String>();
	//private String codUniversidade ;

	//private List<Publicacao> publicacoes = new ArrayList<Publicacao>();
	//private List<Pessoa> coautores = new ArrayList<Pessoa>();
	
	
	public Pessoa() {
		
	}
	public Pessoa(String nome, String id){
		this.nome = nome;
		this.id = id;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

	public HashMap<String, String> getAreas() {
		return areas;
	}
	public void setAreas(HashMap<String, String> areas) {
		this.areas = areas;
	}
	
	public TreeSet<String> getEspecializacao(){
		for ( String area : areas.keySet()) {
			especializacao.add(area);
		}
		return especializacao;
	}
	
	public TreeSet<String> getAreaConhecimento(){
		for ( String area : areas.values()) {
			areaConhecimento.add(area);
		}
		return areaConhecimento;
	}
	
//	public String getCodUniversidade() {
//		return codUniversidade;
//	}
//	public void setCodUniversidade(String codUniversidade) {
//		this.codUniversidade = codUniversidade;
//	}
	
	

	
	
	
}
