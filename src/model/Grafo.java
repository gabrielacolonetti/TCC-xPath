package model;


import java.util.List;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

public class Grafo {

	SimpleWeightedGraph<String, DefaultWeightedEdge> grafo;
	
	public Grafo(){
		
		
	}
	
	public void criaVertice(List<Tupla> listaDeTuplas){
		String p1;
		String p2;
		
		//varre lista de tuplas
		for (Tupla tupla : listaDeTuplas) {
			p1 = tupla.getP1().getNome();
			p2 = tupla.getP2().getNome();
			grafo.addVertex(p1);
			grafo.addVertex(p2);
			DefaultWeightedEdge e = new DefaultWeightedEdge();
			grafo.setEdgeWeight(e, tupla.getPeso());
			grafo.addEdge(p1, p2, e);

		}
	}
	
}
