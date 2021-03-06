package model;

import java.util.ArrayList;
import java.util.List;


public class Tupla {

	private Pessoa p1;
	private Pessoa p2;
	private int peso;
	private List<Publicacao> publicacoes = new ArrayList<Publicacao>();
	private List<String> publicacoesUFSC = new ArrayList<String>();
	
	public List<String> getPublicacoesUFSC() {
		return publicacoesUFSC;
	}
	public void setPublicacoesUFSC(List<String> publicacoesUFSC) {
		this.publicacoesUFSC = publicacoesUFSC;
	}
	public Pessoa getP1() {
		return p1;
	}
	public void setP1(Pessoa p1) {
		this.p1 = p1;
	}
	public Pessoa getP2() {
		return p2;
	}
	public void setP2(Pessoa p2) {
		this.p2 = p2;
	}
	public int getPeso() {
		return peso;
	}
	public void setPeso(int peso) {
		this.peso = peso;
	}
	public List<Publicacao> getPublicacoes() {
		return publicacoes;
	}
	public void setPublicacoes(List<Publicacao> publicacoes) {
		this.publicacoes = publicacoes;
	}
	
	public boolean tuplaExiste(Tupla tupla) {
		if(tupla.getP1().getNome().equals(this.getP1().getNome())|| tupla.getP1().getId().equals(this.getP1().getId())){
			return true;
		}
		return false;
	}
	
	public boolean saoMembros(Pessoa p1, Pessoa p2) {
		return (p1.equals(this.p1) && p2.equals(this.p2)) ||
			   (p1.equals(this.p2) && p2.equals(this.p1));
	}
	
	
	
	
}
