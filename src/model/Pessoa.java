package model;

//import java.util.ArrayList;
//import java.util.List;

public class Pessoa {
	private String nome;
	private String id;
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
	
	
	
//	public List<Publicacao> getPublicacoes() {
//		return publicacoes;
//	}
//	public void setPublicacoes(List<Publicacao> publicacoes) {
//		this.publicacoes = publicacoes;
//	}
//	public List<Pessoa> getCoautores() {
//		return coautores;
//	}
//	public void setCoautores(List<Pessoa> coautores) {
//		this.coautores = coautores;
//	}
	
	
	
}
