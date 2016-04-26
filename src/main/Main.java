package main;


import java.io.File;
import java.util.List;

import model.CalcFilesUtil;
import model.Grafo;
//import model.Pessoa;
import model.Publicacao;
import model.Tupla;
import source.ManipuladorXML;


public class Main {

	
	public static void main(String[] args) {
		
		
		CalcFilesUtil calcFilesUtil = new CalcFilesUtil("curriculos/");
		List<File> listaDeCurriculoXML = calcFilesUtil.getCurriculosXML();
		
		
		
		List<Publicacao> listaPublicacao = ManipuladorXML.geraListaDePublicacoes(listaDeCurriculoXML);
		List<Tupla> listaTupla = ManipuladorXML.criaPares();
		
//		for (Tupla tupla : listaTupla) {
//			System.out.println("\n\n"+ tupla.getP1().getNome()+"---"+ tupla.getP2().getNome()+"---"+tupla.getPeso());
//			
//			for(int i =0; i<tupla.getPublicacoes().size();i++){
//				System.out.println(tupla.getPublicacoes().get(i).getTitulo()+"-"+ tupla.getPublicacoes().get(i).getAno());
//				
//			}
//		}
//		
//		Grafo g = new Grafo();
//		g.criaVertice(listaTupla);
//		
		
		
		
		
//		for (Publicacao p : listaPublicacao) {
//			System.out.println("\n\n\n"+p.getTitulo()+"--"+p.getAno());
//			System.out.println(p.getAutor().getNome());
//		    
//			List<Pessoa> coautores = p.getCoautores();
//			for (Pessoa paux : coautores) {
//				System.out.println(paux.getNome());
//
//			}
//
//		}
		
		
	}
}
