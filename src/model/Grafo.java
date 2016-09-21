package model;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;
import source.CosineSimilaridade;
import source.JaccardSimilaridade;
import source.OverlapSimilaridade;

public class Grafo {

	public List<JaccardSimilaridade> coeficientesJaccard = new ArrayList<JaccardSimilaridade>();
	public List<CosineSimilaridade> coeficientesCosine = new ArrayList<CosineSimilaridade>();
	public List<OverlapSimilaridade> coeficientesOverlap = new ArrayList<OverlapSimilaridade>();

	SimpleWeightedGraph<String, DefaultWeightedEdge> grafo;
	List<Grupo> listaDegrupos = new ArrayList<Grupo>();
	SimpleWeightedGraph<String, DefaultWeightedEdge> grafoCopia = new SimpleWeightedGraph<String, DefaultWeightedEdge>(DefaultWeightedEdge.class);
	int threshold = 1;
	public HashMap<String,Set<String> > quantidadeVertices = new HashMap<String, Set<String>>();

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
			if (p1.equals(p2)) {
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

	public HashMap<String, Set<String>> getQuantidadeVertices() {
		return quantidadeVertices;
	}

	public void setQuantidadeVertices(HashMap<String, Set<String>> quantidadeVertices) {
		this.quantidadeVertices = quantidadeVertices;
	}

	public List<Grupo> criaGrupoAlgoritmoSimples(){


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
				criaGrupoAlgoritmoSimples();


			}

		}
		incluirGruposRestantes();
		return listaDegrupos;



	}

	public Grupo retornaArestasVertice1(DefaultWeightedEdge arestaMaior,Grupo grupo, SimpleWeightedGraph<String, DefaultWeightedEdge> grafoCopia){
		//pego vertice1
		String v1= grafoCopia.getEdgeSource(arestaMaior);

		//pego as arestas do vertice1
		if(v1 !=null){
			Set<DefaultWeightedEdge> arestasV1 =  grafoCopia.edgesOf(v1);
			verificaPesoArestasV1(arestasV1,grafoCopia,grupo);
			return grupo;
		}
		return grupo;
	}

	public Grupo retornaArestasVertice2(DefaultWeightedEdge arestaMaior,Grupo grupo, SimpleWeightedGraph<String, DefaultWeightedEdge> grafoCopia){
		//pego vertice1
		String v1= grafoCopia.getEdgeTarget(arestaMaior);

		//pego as arestas do vertice1
		if (v1 != null){
			Set<DefaultWeightedEdge> arestasV2 =  grafoCopia.edgesOf(v1);
			verificaPesoArestasV1(arestasV2,grafoCopia,grupo);
			return grupo;
		}
		return grupo;
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

	private void incluirGruposRestantes() {
		Set<DefaultWeightedEdge> listaAresta1 = grafoCopia.edgeSet();


		for (DefaultWeightedEdge dw : listaAresta1) {

			double peso = retornaPeso(dw);

			if(peso<=threshold && peso!=0){
				Grupo grupo = new Grupo();
				grafoCopia.setEdgeWeight(dw, 0);
				grupo.getGrupos().add(dw);
				listaDegrupos.add(grupo);
			}

		}

	}

	public HashMap<String, Set<String>> retornaTodosVerticesGrafo(){
		//recupera todos os vertices do grafo
		Set<String> listaTodosVertices = grafo.vertexSet();

		for (String vertice : listaTodosVertices) {
			//lista de vizinhos
			Set<String> vizinhos = new TreeSet<String>();

			//pego todas as arestas do vertice
			Set<DefaultWeightedEdge> arestasVizinhas = grafo.edgesOf(vertice);

			//varredura na lista de arestas
			for (DefaultWeightedEdge dw : arestasVizinhas) {
				String grafoSource = grafo.getEdgeSource(dw);
				String grafoTarget = grafo.getEdgeTarget(dw);
				if(grafoSource.equals(vertice)){
					vizinhos.add(grafoTarget);
				}else{
					vizinhos.add(grafoSource);
				}
				quantidadeVertices.put(vertice,vizinhos);
			}
		}
		return quantidadeVertices;
	}

	public List<JaccardSimilaridade> coeficienteJaccard(){
		for (Map.Entry<String,Set<String>> v1 : quantidadeVertices.entrySet()) {
			for (Map.Entry<String,Set<String>> v2 : quantidadeVertices.entrySet()) {
		
//				if (v1.getKey().equals("Ronaldo dos Santos Mello") && v2.getKey().equals("Carina Friedrich Dorneles")) {
//					
//					System.out.println("debug");
//				}
//				
				
				JaccardSimilaridade j = new JaccardSimilaridade();

				if(v1.getKey().equals(v2.getKey())){
					continue;
				}else{
					
					j.setP1(v1.getKey());
					j.setP2(v2.getKey());

					Set<String> interseccao = new TreeSet(v1.getValue());
					interseccao.retainAll(v2.getValue());
					double qtdInterseccao = interseccao.size();
					//System.out.println(qtdInterseccao+" Interseccao de "+ v1.getKey()+" e "+ v2.getKey() );

					Set<String> uniao = new TreeSet(v1.getValue());
					uniao.addAll(v2.getValue());
					double qtdUniao = uniao.size();
					//System.out.println(qtdUniao+" Uniao de "+ v1.getKey()+" e "+ v2.getKey() );

					j.setInterseccao(qtdInterseccao);
					j.setUniao(qtdUniao);
					j.realizaCalculo();
					coeficientesJaccard.add(j);
				}
			}
		}
		return coeficientesJaccard;
	}
	
	public List<CosineSimilaridade> coeficienteCosine(){
		for (Map.Entry<String,Set<String>> v1 : quantidadeVertices.entrySet()) {			
			for (Map.Entry<String,Set<String>> v2 : quantidadeVertices.entrySet()) {

				CosineSimilaridade c = new CosineSimilaridade();

				if(v1.getKey().equals(v2.getKey())){
					continue;
				}else{


					c.setP1(v1.getKey());
					c.setP2(v2.getKey());
					Set<String> interseccao = new TreeSet(v1.getValue());
					interseccao.retainAll(v2.getValue());
					double qtdInterseccao = interseccao.size();
									//System.out.println(qtdInterseccao+" Interseccao de "+ v1.getKey()+" e "+ v2.getKey() );
									
									//System.out.println("qtd vertice 1 "+v1.getValue().size());
									//System.out.println("qtd vertice 2 "+v2.getValue().size());

					c.setQtdV1(v1.getValue().size());
					c.setQtdV2(v2.getValue().size());
					c.setInterseccao(qtdInterseccao);
					c.realizaCalculo();
					coeficientesCosine.add(c);
				}
			}
		}
		return coeficientesCosine;
	}
				
    public List<OverlapSimilaridade> coeficienteOverlap(){
			for (Map.Entry<String,Set<String>> v1 : quantidadeVertices.entrySet()) {			
				for (Map.Entry<String,Set<String>> v2 : quantidadeVertices.entrySet()) {
					
					OverlapSimilaridade o = new OverlapSimilaridade();
									
					if(v1.getKey().equals(v2.getKey())){
						continue;
					}else{
					

					o.setP1(v1.getKey());
					o.setP2(v2.getKey());
					Set<String> interseccao = new TreeSet(v1.getValue());
					interseccao.retainAll(v2.getValue());
					double qtdInterseccao = interseccao.size();
//					System.out.println(qtdInterseccao+" Interseccao de "+ v1.getKey()+" e "+ v2.getKey() );
//					
//					System.out.println("qtd vertice 1 "+v1.getValue().size());
//					System.out.println("qtd vertice 2 "+v2.getValue().size());

					o.setQtdV1(v1.getValue().size());
	                o.setQtdV2(v2.getValue().size());
					o.setInterseccao(qtdInterseccao);
					o.realizaCalculo();
					coeficientesOverlap.add(o);
					}
				}
			}
		return coeficientesOverlap;
	}
	public void calculaJaccardOld(){

		for (Map.Entry<String,Set<String>> v1 : quantidadeVertices.entrySet()) {
			for (Map.Entry<String,Set<String>> v2 : quantidadeVertices.entrySet()) {
				
				int qtdInterseccao =0;
				int qtdUniao =0;
				JaccardSimilaridade j = new JaccardSimilaridade();

				if(v1.getKey().equals(v2.getKey())){
					continue;
				}else{
					
					for (String vizinhosv1 : v1.getValue()) {
						for (String vizinhosv2 : v2.getValue()) {		
							j.setP1(v1.getKey());
							j.setP2(v2.getKey());
							
							if(vizinhosv1.equals(vizinhosv2)){
								qtdInterseccao++;
								System.out.println("interseccao "+qtdInterseccao);
							}
					   }
					}
				}
				qtdUniao = (v1.getValue().size() + v2.getValue().size()) - qtdInterseccao;
				j.setUniao(qtdUniao);
				j.setInterseccao(qtdInterseccao);

				j.realizaCalculo();
				coeficientesJaccard.add(j);
			}
		}
	}
	public void calculaCosineOld(){
		for (Map.Entry<String,Set<String>> v1 : quantidadeVertices.entrySet()) {
			for (Map.Entry<String,Set<String>> v2 : quantidadeVertices.entrySet()) {
				int qtdInterseccao=0;
				int qtdVizinhosV1=0;
				int qtdVizinhosV2=0;

				CosineSimilaridade c = new CosineSimilaridade();


				if(v1.getKey().equals(v2.getKey())){
					continue;
				}else{
					for (String vizinhosv1 : v1.getValue()) {
						qtdVizinhosV1++;
						for (String vizinhosv2 : v2.getValue()) {
							c.setP1(v1.getKey());
							c.setP2(v2.getKey());
							qtdVizinhosV2++;
							if(vizinhosv1.equals(vizinhosv2)){
								qtdInterseccao++;
							}
						}
						c.setInterseccao(qtdInterseccao);

					}
					c.setQtdV1(qtdVizinhosV1);
					c.setQtdV2(qtdVizinhosV2);
				}
				coeficientesCosine.add(c);
			}
		}
	}
	
	public void calculaOverlapOld(){
		for (Map.Entry<String,Set<String>> v1 : quantidadeVertices.entrySet()) {
			for (Map.Entry<String,Set<String>> v2 : quantidadeVertices.entrySet()) {
				int qtdInterseccao=0;
				int qtdVizinhosV1=0;
				int qtdVizinhosV2=0;

				OverlapSimilaridade o = new OverlapSimilaridade();


				if(v1.getKey().equals(v2.getKey())){
					continue;
				}else{
					for (String vizinhosv1 : v1.getValue()) {
						qtdVizinhosV1++;
						for (String vizinhosv2 : v2.getValue()) {
							qtdVizinhosV2++;
							o.setP1(v1.getKey());
							o.setP2(v2.getKey());
							if(vizinhosv1.equals(vizinhosv2)){
								qtdInterseccao++;
							}
						}
					}
					o.setInterseccao(qtdInterseccao);
					o.setQtdV1(qtdVizinhosV1);
					o.setQtdV2(qtdVizinhosV2);
				}
				coeficientesOverlap.add(o);
			}
		}
	}
}


