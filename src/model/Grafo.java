package model;


import java.util.List;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

public class Grafo {

	SimpleWeightedGraph<String, DefaultWeightedEdge> grafo;
	
	public Grafo(){
		grafo = new SimpleWeightedGraph<String, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		
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
	
}
