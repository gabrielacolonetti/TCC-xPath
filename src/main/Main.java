package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import model.CalcFilesUtil;
import model.Grafo;
import model.Grupo;
import model.Pessoa;
import model.Pessoa;
import model.Publicacao;
import model.Tupla;
import source.ManipuladorXML;

public class Main {

	public static void main(String[] args) throws FileNotFoundException {

		CalcFilesUtil calcFilesUtil = new CalcFilesUtil("curriculos/testeNome");
//		CalcFilesUtil calcFilesUtil = new CalcFilesUtil("curriculos/debug");
		List<File> listaDeCurriculoXML = calcFilesUtil.getCurriculosXML();

		List<Publicacao> listaPublicacao = ManipuladorXML.geraListaDePublicacoes(listaDeCurriculoXML);
		//List<Tupla> listaTupla = ManipuladorXML.criaPares();

		Grafo g = new Grafo();
		//g.criaVertice(listaTupla);
//		 System.out.println(g);
		g.setThreshold(3);
		//List<Grupo> listaDegrupos = g.criaGrupo();
//		PrintStream p = new PrintStream("grupos.txt");
//		System.setOut(p);
		//lista de publicacao
//		for (Publicacao p1 : listaPublicacao) {
//			System.out.println("\n\n\n"+p1.getTitulo()+"--"+p1.getAno());
//			System.out.println(p1.getAutor().getNome());
//
//
//			List<Pessoa> coautores = p1.getCoautores();
//			for (Pessoa paux : coautores) {
//				System.out.println(paux.getNome());
//
//
//			}
//
//		}

			for (int i =0; i < listaPublicacao.size(); i++)	{
				HashMap<String,String> areas = listaPublicacao.get(i).getAutor().getAreas();
				System.out.println(listaPublicacao.get(i).getAutor().getNome()+" cod: "+listaPublicacao.get(i).getAutor().getCodUniversidade());
				System.out.println(listaPublicacao.get(i).getCoautores().get(0).getNome()+" cod: " +listaPublicacao.get(i).getCoautores().get(0).getCodUniversidade());

				for (String area: areas.keySet()){
					
					System.out.println("Area: "+areas.get(area)+ " Especialização: "+ area);
				
				}
				
					
					
			}
		
		
		
//		for (int i = 0; i < listaDegrupos.size(); i++) {
			
//            System.setOut(p);
//			System.out.println("Grupo " + i + "\n\n");
//			
//			for (int j = 0; j < listaDegrupos.get(i).getGrupos().size(); j++) {
//				DefaultWeightedEdge ligacao = listaDegrupos.get(i).getGrupos().get(j);
//				ligacao = g.getObjetoGrafo().getEdge(g.getGrafoCopia().getEdgeSource(ligacao), g.getGrafoCopia().getEdgeTarget(ligacao));
//				System.out.println(ligacao + " - " + 
//						g.getObjetoGrafo().getEdgeWeight(ligacao));
//
//			}
//			System.out.println("\n\n");
//		}
//		p.close();


//		 for (Tupla tupla : listaTupla) {
//		 System.out.println(
//		 "\n" + tupla.getP1().getNome() + "---" + tupla.getP2().getNome() +
//		 "---" + tupla.getPeso());
//		
//		 for (int i = 0; i < tupla.getPublicacoes().size(); i++) {
//		 System.out.println(
//		 tupla.getPublicacoes().get(i).getTitulo() + "-" +
//		 tupla.getPublicacoes().get(i).getAno());
//		
//		 }
//		 }

		 
	}
}