package model;

import java.util.ArrayList;
import java.util.List;

public class Publicacao {
	private String ano;
	private String titulo;
	private Pessoa autor;
	private List<Pessoa> coautores = new ArrayList<Pessoa>();

	public Publicacao() {

	}

	public Publicacao( String t,String a) {
		this.titulo = t;
		this.ano = a;
		
	}

	public Pessoa getAutor() {
		return autor;
	}

	public void setAutor(Pessoa autor) {
		this.autor = autor;
	}

	public List<Pessoa> getCoautores() {
		return coautores;
	}

	public void setCoautores(List<Pessoa> coautores) {
		this.coautores = coautores;
	}
	
	public String getAno() {
		return ano;
	}

	public void setAno(String ano) {
		this.ano = ano;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	
	public boolean coautorNaLista(Pessoa p) {
		for (Pessoa pes : coautores) {
			if(pes.getId().trim().equals(p.getId().trim())||pes.getNome().trim().equals(p.getNome().trim()))
				return true;
		}

			return false;
	}

	@Override
	public boolean equals(Object obj) {
		Publicacao p = (Publicacao) obj;

		// TODO Auto-generated method stub
		
		return (this.titulo.equals(p.getTitulo()) && this.ano.equals(p.getAno()));
	}

	
}
