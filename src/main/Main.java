package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import clusteringstrategies.implementation.clustering.BestStarClusteringStrategy;
import datastructures.core.DataCluster;
import datastructures.core.DataObject;
import datastructures.core.Matrix2D;
import model.CalcFilesUtil;
import model.Grafo;
import model.Grupo;
import model.Pessoa;
import model.Pessoa;
import model.Publicacao;
import model.Tupla;
import source.CosineSimilaridade;
import source.DataObjectAutor;
import source.JaccardSimilaridade;
import source.ManipuladorXML;
import source.OverlapSimilaridade;

public class Main {

	public static void main(String[] args) throws FileNotFoundException {

//		CalcFilesUtil calcFilesUtil = new CalcFilesUtil("curriculos/testeNome");
		CalcFilesUtil calcFilesUtil = new CalcFilesUtil("curriculos/basic");
		List<File> listaDeCurriculoXML = calcFilesUtil.getCurriculosXML();
		List<Publicacao> listaPublicacao = ManipuladorXML.geraListaDePublicacoes(listaDeCurriculoXML);
		List<Tupla> listaTupla = ManipuladorXML.criaPares();
		
		//cria grafo
		Grafo g = new Grafo();
		g.criaVertice(listaTupla);
		//System.out.println(g);
		g.setThreshold(3);
		
		//calculo similaridade
		HashMap<String, Set<String>> quantidadeVertices = g.retornaTodosVerticesGrafo();
		List<JaccardSimilaridade> listaDeCoeficientes = g.coeficienteJaccard();
		//g.coeficienteJaccard();
		g.coeficienteCosine();
		g.coeficienteOverlap();
		
		//clusterizando utilizando best star
		JaccardSimilaridade j = new JaccardSimilaridade();
		
		for(CosineSimilaridade c : g.coeficientesCosine){
			System.out.println("\ncoeficientes Cosine");
			System.out.println(c.getP1()+" "+c.getP2());
			System.out.println(c.getCalculo());
		}
		
		for (JaccardSimilaridade s: listaDeCoeficientes) {
			System.out.println("\ncoeficientes Jaccard");
			System.out.println(s.getP1()+" "+ s.getP2());
			System.out.println(s.getCalculo());
		}
		
		
		for(OverlapSimilaridade c : g.coeficientesOverlap){
			System.out.println("\ncoeficientes Overlap");
			System.out.println(c.getP1()+" "+c.getP2());
			System.out.println(c.getCalculo());
		}
		
		//criando cluster usando ursa		
		ClusteringProcess primeiroProcesso = new ClusteringProcess();
		primeiroProcesso.setClusteringStrategy(new BestStarClusteringStrategy(0.3));
		Matrix2D matrizDeSimilaridades = j.criaMatriz(listaDeCoeficientes, g.quantidadeVertices.size(), g.getQuantidadeVertices());
		System.out.println(matrizDeSimilaridades);
		DataObjectAutor autor;
		for (Entry<String, Set<String>> data : g.getQuantidadeVertices().entrySet()) {
			String nomeautor = data.getKey();
			autor = new DataObjectAutor(nomeautor);			
			//System.out.println(autor);
			primeiroProcesso.addDataObject(autor);
		}
		primeiroProcesso.similarityMatrix = matrizDeSimilaridades;
		primeiroProcesso.dataClusters = primeiroProcesso.clusteringStrategy.executeClustering(primeiroProcesso.dataObjects, primeiroProcesso.similarityMatrix);

		int index =0;
		for (DataCluster cluster : primeiroProcesso.getDataClusters()) {
				//cluster.print();
			System.out.println("\nCluster "+ index);
				for (int i =0 ; i< cluster.getDataObjects().size();i++){
					DataObjectAutor data = (DataObjectAutor) cluster.getDataObjects().get(i);
					System.out.println(data.getMt().getValue());
				}
				index++;
							
		}
		
//		List<Grupo> listaDegrupos = g.criaGrupoAlgoritmoSimples();
//		PrintStream p = new PrintStream("grupos.txt");
//		System.setOut(p);
		
		//lista de publicacao imprimi titulo, ano, autor, coautores
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

		//imprimi area e especialidade do autor principal
//			for (int i =0; i < listaPublicacao.size(); i++)	{
//				HashMap<String,String> areas = listaPublicacao.get(i).getAutor().getAreas();
//				
//				for (String area: areas.keySet()){
//					
//					System.out.println("Area: "+areas.get(area)+ " Especialização: "+ area);
//				
//				}
//			}
		
		
		//imprimi a lista de grupos com seus pesos
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


		//imprimi a lista de tuplas
		
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
		
		//imprimi os vertices e seus vizinhos	
//	    HashMap<String,Set<String>> mapaDeVerticeQuantidadeVizinhos = g.retornaTodosVerticesGrafo();
//		for (Map.Entry<String,Set<String>> v1 : mapaDeVerticeQuantidadeVizinhos.entrySet()) {
//			System.out.println("vertice: "+v1.getKey());
//			for (String vizinhos: v1.getValue()){
//				
//				System.out.println("vizinho: " +vizinhos);
//			}
//		}
		
	
		 
	}
}