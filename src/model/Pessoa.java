package model;

import java.util.HashMap;

public class Pessoa {
	private String nome;
	private String id;
	private HashMap<String,String> areas = new HashMap<String, String>();
	private String codUniversidade ;

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
	public String getCodUniversidade() {
		return codUniversidade;
	}
	public void setCodUniversidade(String codUniversidade) {
		this.codUniversidade = codUniversidade;
	}
	
	

	
	
	
}
