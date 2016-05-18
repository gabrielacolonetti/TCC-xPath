package model;


import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

public class Grafo {

	SimpleWeightedGraph<String, DefaultWeightedEdge> grafo;
	List<Grupo> listaDegrupos = new ArrayList<Grupo>();
	SimpleWeightedGraph<String, DefaultWeightedEdge> grafoCopia = new SimpleWeightedGraph<String, DefaultWeightedEdge>(DefaultWeightedEdge.class);
	int threshold = 1;

	public int getThreshold() {
		return threshold;
	}


	public void setThreshold(int threshold) {
		this.threshold = threshold;
	}


	public SimpleWeightedGraph<String, DefaultWeightedEdge> getObjetoGrafo() {
		return grafo;
	}

	
	public SimpleWeightedGraph<String, DefaultWeightedEdge> getGrafoCopia() {
		return grafoCopia;
	}


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
			if (p1 == p2 ) {
				System.err.println("Autores iguais!");
				continue;
			}
			try {
			
			incluirDupla(grafo, p1, p2, tupla);
			incluirDupla(grafoCopia, p1, p2, tupla);
			} catch (Exception e) {
				for (Publicacao p : tupla.getPublicacoes()) {
					System.out.println(p.getTitulo());
				}
				throw e;
			}
			



		}
	}


	private void incluirDupla(SimpleWeightedGraph<String, DefaultWeightedEdge> grafoIncluir, String p1, String p2, Tupla tupla) {
		grafoIncluir.addVertex(p1);
		grafoIncluir.addVertex(p2);
		DefaultWeightedEdge e = new DefaultWeightedEdge();
		grafoIncluir.setEdgeWeight(e, tupla.getPeso());
		grafoIncluir.addEdge(p1, p2, e);
	}

	public void addArestasMaiorNoGrupo(Grupo grupo, SimpleWeightedGraph<String, DefaultWeightedEdge> grafoCopia, List<DefaultWeightedEdge> arestasPesoMaior){
		for (DefaultWeightedEdge dw : arestasPesoMaior) {
			grupo.getGrupos().add(dw);
			grafoCopia.setEdgeWeight(dw, 0);
		}
	}

	public void verificaPesoArestasV1(Set<DefaultWeightedEdge> arestas, SimpleWeightedGraph<String, DefaultWeightedEdge> grafoCopia, Grupo grupo){
		     
		for (DefaultWeightedEdge dw : arestas) {
			double peso = retornaPeso(dw);
			if(peso>=getThreshold()){
				grafoCopia.setEdgeWeight(dw, 0);
				grupo.getGrupos().add(dw);
				retornaArestasVertice1(dw, grupo, grafoCopia);
			}
		}
		 
	}
	
	public void verificaPesoArestasV2(Set<DefaultWeightedEdge> arestas, SimpleWeightedGraph<String, DefaultWeightedEdge> grafoCopia, Grupo grupo){
	     
		for (DefaultWeightedEdge dw : arestas) {
			double peso = retornaPeso(dw);
			if(peso>=getThreshold()){
				grafoCopia.setEdgeWeight(dw, 0);
				grupo.getGrupos().add(dw);
				retornaArestasVertice2(dw, grupo, grafoCopia);
			}
		}
		 
	}




	public Grupo retornaArestasVertice1(DefaultWeightedEdge e,Grupo grupo, SimpleWeightedGraph<String, DefaultWeightedEdge> grafoCopia){
		//pego vertice1
		String v1= grafoCopia.getEdgeSource(e);

		//pego as arestas do vertice1
		Set<DefaultWeightedEdge> arestasV1 =  grafoCopia.edgesOf(v1);
		verificaPesoArestasV1(arestasV1,grafoCopia,grupo);
		return grupo;
		
	}
	
	public Grupo retornaArestasVertice2(DefaultWeightedEdge e,Grupo grupo, SimpleWeightedGraph<String, DefaultWeightedEdge> grafoCopia){
		//pego vertice1
		String v1= grafoCopia.getEdgeTarget(e);

		//pego as arestas do vertice1
		Set<DefaultWeightedEdge> arestasV2 =  grafoCopia.edgesOf(v1);
		verificaPesoArestasV1(arestasV2,grafoCopia,grupo);
		return grupo;
		
	}



	public List<Grupo> criaGrupo(){


		Set<DefaultWeightedEdge> listaAresta = grafoCopia.edgeSet();
		double maior = 0;

		DefaultWeightedEdge arestaMaior = new DefaultWeightedEdge() ;
		Grupo grupo = new Grupo();


		for (DefaultWeightedEdge dw : listaAresta) {
			double peso = retornaPeso(dw);
			if(peso >maior && peso>=threshold){ //&& peso>3
				maior = peso;
				arestaMaior = dw;
			}
		}
		grafoCopia.setEdgeWeight(arestaMaior, 0);
		grupo.getGrupos().add(arestaMaior);
		
		
		retornaArestasVertice1(arestaMaior,grupo, grafoCopia);
		retornaArestasVertice2(arestaMaior,grupo, grafoCopia);
		listaDegrupos.add(grupo);
		
		Set<DefaultWeightedEdge> listaAresta1 = grafoCopia.edgeSet();
		

		for (DefaultWeightedEdge dw : listaAresta1) {

			double peso = retornaPeso(dw);
			if(peso>=threshold){
				criaGrupo();
				
				
			}
			
		}
		incluirGruposRestantes();
		return listaDegrupos;



	}


	private void incluirGruposRestantes() {
		Grupo grupo = new Grupo();
		Set<DefaultWeightedEdge> listaAresta1 = grafoCopia.edgeSet();


		for (DefaultWeightedEdge dw : listaAresta1) {

			double peso = retornaPeso(dw);
			if(peso<=threshold && peso!=0){
				grafoCopia.setEdgeWeight(dw, 0);
				grupo.getGrupos().add(dw);
				listaDegrupos.add(grupo);
			}

		}

	}

}