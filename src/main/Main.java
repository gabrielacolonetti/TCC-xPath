package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import clusteringstrategies.implementation.clustering.BestStarClusteringStrategy;
import clusteringstrategies.implementation.clustering.KmedoidsClusteringStrategy;
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

		CalcFilesUtil calcFilesUtil = new CalcFilesUtil("curriculos/testeArtigos");
//		CalcFilesUtil calcFilesUtil = new CalcFilesUtil("curriculosplus");
		List<File> listaDeCurriculoXML = calcFilesUtil.getCurriculosXML();
//		List<Publicacao> listaPublicacao = ManipuladorXML.geraListaDePublicacoes(listaDeCurriculoXML);
		Map<String,List<Pessoa>> listaPublicacao = ManipuladorXML.geraListaDePublicacoesUFSC(listaDeCurriculoXML);
		
		
		
		//List<Tupla> listaTupla = ManipuladorXML.criaPares();
		List<Tupla> listaTupla = ManipuladorXML.criaParesUFSC();
		Map<String, Pessoa> mapaPessoas = ManipuladorXML.getMapaPessoas();
			
		//cria grafo
		Grafo g = new Grafo();
		g.criaVertice(listaTupla);
		g.setThreshold(3);
		
		//calculo similaridade
		HashMap<String, Set<String>> quantidadeVertices = g.retornaTodosVerticesGrafo();
		List<JaccardSimilaridade> listaCoeficientesJaccard = g.coeficienteJaccard();
		List<CosineSimilaridade> listaCoeficientesCosine =  g.coeficienteCosine();
		List<OverlapSimilaridade> listaCoeficientesOverlap = g.coeficienteOverlap();
		
		for (OverlapSimilaridade overlapSimilaridade : listaCoeficientesOverlap) {
			System.out.println(overlapSimilaridade.getP1()+" "+overlapSimilaridade.getP2()+" "+overlapSimilaridade.getCalculo());
		}
		
		//clusterizando utilizando best star
		JaccardSimilaridade j = new JaccardSimilaridade();
		CosineSimilaridade c = new CosineSimilaridade();
		OverlapSimilaridade o = new OverlapSimilaridade();
		

		//criando cluster usando ursa
		ClusteringProcess primeiroProcesso = new ClusteringProcess();
		ClusteringProcess segundoProcesso = new ClusteringProcess();
		primeiroProcesso.setClusteringStrategy(new BestStarClusteringStrategy(0.05));

		//alterar a lista de coeficientes que deseja clusterizar
		//HashMap<String, HashMap<String, Double>> hashCoeficientes = j.criaHashMap(listaCoeficientesJaccard);
		//HashMap<String, HashMap<String, Double>> hashCoeficientes = c.criaHashMap(listaCoeficientesCosine);
		HashMap<String, HashMap<String, Double>> hashCoeficientes = o.criaHashMap(listaCoeficientesOverlap);

		//List <String> autores = j.listaDeTodosAutores(quantidadeVertices);
		//List <String> autores = c.listaDeTodosAutores(quantidadeVertices);
		List <String> autores = o.listaDeTodosAutores(quantidadeVertices);
		
     	//Matrix2D matrizDeSimilaridades = j.criaMatriz(hashCoeficientes, autores.size(), autores);
     	//Matrix2D matrizDeSimilaridades = c.criaMatriz(hashCoeficientes, autores.size(), autores);
     	Matrix2D matrizDeSimilaridades = o.criaMatriz(hashCoeficientes, autores.size(), autores);
     	
//
//     	for (String autor1 : hashCoeficientes.keySet()) {
//     		for (Entry<String,Double> tupla: hashCoeficientes.get(autor1).entrySet() ) {
//     			if (autor1.contains("Ronaldo") && tupla.getKey().contains("Carina")) {
//     				System.out.println("Similaridade <" + autor1 + "> e <" + tupla.getKey() + "> = " + tupla.getValue());
//     			}
//     		}
//     	}
     	
     	//System.out.println("\n\n\n");
     	
     	
		//System.out.println(matrizDeSimilaridades);
     	
     	
		//inserindo data object
     	int valordeK = 0;
		DataObjectAutor autor;
		for(int k=0;k<autores.size();k++){
			String nomeautor = autores.get(k);
			HashMap<String, String> areas = mapaPessoas.get(nomeautor).getAreas();
			autor = new DataObjectAutor(nomeautor);
			autor.setIndex(k);
			autor.setLista(areas);
			primeiroProcesso.addDataObject(autor);
//			segundoProcesso.addDataObject(autor);
			
		}
		
		primeiroProcesso.similarityMatrix = matrizDeSimilaridades;
		primeiroProcesso.dataClusters = primeiroProcesso.clusteringStrategy.executeClustering(primeiroProcesso.dataObjects, primeiroProcesso.similarityMatrix);
		PrintStream p = new PrintStream("clusters.txt");
		System.setOut(p);

		for (DataCluster cluster : primeiroProcesso.getDataClusters()) {
				cluster.print();
//				if(cluster.getDataObjects().size() > 1){
//					valordeK++;
//				}
				
//				for (DataObject autImp: cluster.getDataObjects()) {
//					String autImp1 = ((DataObjectAutor)autImp).getMt().getValue();
//					for (DataObject autImpX: cluster.getDataObjects()) {
//						String autImp2 = ((DataObjectAutor)autImpX).getMt().getValue();
//						
//						System.out.println("Similaridade " + autImp1 + " e " + autImp2 + " = " + hashCoeficientes.get(autImp1).get(autImp2));
//					}
//				}
	}
//		segundoProcesso.setClusteringStrategy(new KmedoidsClusteringStrategy(valordeK, -1, 2, 2));
//		segundoProcesso.similarityMatrix = matrizDeSimilaridades;
//		segundoProcesso.dataClusters = segundoProcesso.clusteringStrategy.executeClustering(segundoProcesso.dataObjects, segundoProcesso.similarityMatrix);
		
//		for (DataCluster clusterK : segundoProcesso.getDataClusters()) {
//			clusterK.print();
//		}
		
//		List<Grupo> listaDegrupos = g.criaGrupoAlgoritmoSimples();

//		for (Entry<String, List<Pessoa>> titulo : listaPublicacao.entrySet()) {
//		System.out.println("\n"+titulo.getKey());
//		for (Pessoa autor : titulo.getValue()) {
//			System.out.println(autor.getNome());
//		}
//	}

//		//imprimi a lista de tuplas
//		for (Tupla tupla : listaTupla) {
//			System.out.println(
//					"\n" + tupla.getP1().getNome() + "---" + tupla.getP2().getNome() +
//					"---" + tupla.getPeso());
//
//			for (int i = 0; i < tupla.getPublicacoesUFSC().size(); i++) {
//				System.out.println(
//						tupla.getPublicacoesUFSC().get(i));
//
//			}
//		}

		
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
		p.close();


		
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