package model;


import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

public class Grafo {

	SimpleWeightedGraph<String, DefaultWeightedEdge> grafo;
	List<Grupo> listaDegrupos = new ArrayList<Grupo>();

	
	public Grafo(){
		grafo = new SimpleWeightedGraph<String, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		
	}
	
	public double retornaPeso(DefaultWeightedEdge e){
		return grafo.getEdgeWeight(e);
	}

	public String toString() {
		String retorno = "";
		for (DefaultWeightedEdge e : grafo.edgeSet()) {
			
			retorno += "\n" + e + "[" + grafo.getEdgeWeight(e) + "]";			
		}
//		return grafo.
//				toString();
		return retorno;
	}
	
	public void criaVertice(List<Tupla> listaDeTuplas){
		String p1;
		String p2;
		
		//varre lista de tuplas
		for (Tupla tupla : listaDeTuplas) {
			p1 = tupla.getP1().getNome();
			p2 = tupla.getP2().getNome();
			if (p1 == null || p2 == null) {
				System.err.println("Autor nulo!");
				System.exit(1);
			}
			grafo.addVertex(p1);
			grafo.addVertex(p2);
			DefaultWeightedEdge e = new DefaultWeightedEdge();
			grafo.setEdgeWeight(e, tupla.getPeso());
			grafo.addEdge(p1, p2, e);
			
			
			

		}
	}
	

	
	
	
	public String retornaVertice(DefaultWeightedEdge e){
		String s= grafo.getEdgeSource(e);
		//grafo.getAllEdges();
		return s;
	}
	
	
	
	public void criaGrupo(){
		SimpleWeightedGraph<String, DefaultWeightedEdge> grafoCopia = new SimpleWeightedGraph<String, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		grafoCopia = grafo;
		
		Set<DefaultWeightedEdge> listaAresta = grafoCopia.edgeSet();
		double maior = 0;
		
		DefaultWeightedEdge arestaMaior = new DefaultWeightedEdge() ;
		Grupo grupo = new Grupo();
		
		for (DefaultWeightedEdge dw : listaAresta) {
			double peso = retornaPeso(dw);
			if(peso >maior ){ //&& peso>3
				maior = peso;
				arestaMaior = dw;
			}
		}
		
		
		grafoCopia.setEdgeWeight(arestaMaior, 0);
//		grupo.getGrupos().add(arestaMaior);
//		String s = retornaVertice(dw);

	}
	
}
