package source;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import datastructures.core.Matrix2D;

public class OverlapSimilaridade {
	public String p1;
	public String p2;
	public double interseccao;
	public double qtdV1;
	public double qtdV2;
	public double calculo;
	
	public String getP1() {
		return p1;
	}
	public void setP1(String p1) {
		this.p1 = p1;
	}
	public String getP2() {
		return p2;
	}
	public void setP2(String p2) {
		this.p2 = p2;
	}
	public double getInterseccao() {
		return interseccao;
	}
	public void setInterseccao(double interseccao) {
		this.interseccao = interseccao;
	}
	public double getQtdV1() {
		return qtdV1;
	}
	public void setQtdV1(double qtdV1) {
		this.qtdV1 = qtdV1;
	}
	public double getQtdV2() {
		return qtdV2;
	}
	public void setQtdV2(double qtdV2) {
		this.qtdV2 = qtdV2;
	}
	
	public double retornaMin(){
		return Math.min(qtdV1, qtdV2);
	}
	
	public double realizaCalculo(){
		double min = retornaMin();
		return calculo = interseccao/min;
	}
	
	public double getCalculo(){
		return calculo;
	}
//	public Matrix2D criaMatriz(List<OverlapSimilaridade> listaDeCoeficientes, int objectsCount, HashMap<String, Set<String>> quantidadeVertices) {
//		 Matrix2D matrizDeSimilaridades = new Matrix2D(objectsCount);
//		 List <String> autores = listaDeTodosAutores(quantidadeVertices);
//		for (int i = 0; i < objectsCount; i++) {
//			for (int j = i+1 ; j < objectsCount; j++) {
//				 for (OverlapSimilaridade o : listaDeCoeficientes) {
//					 if(o.getP1().equals(autores.get(i))&& o.getP2().equals(autores.get(j)) || o.getP1().equals(autores.get(j)) && o.getP2().equals(autores.get(i))){
//						// System.out.println(o.getP1()+"-"+o.getP2()+" "+o.getCalculo());
//						 matrizDeSimilaridades.set(i, j, o.getCalculo() );
//					 }
//				}
//			}
//		}
//		return matrizDeSimilaridades;
//	}

	
	public Matrix2D criaMatriz(HashMap<String, HashMap<String, Double>> hashCoeficientes, int objectsCount, List<String> autores) {
		Matrix2D matrizDeSimilaridades = new Matrix2D(objectsCount);

		for (int i = 0; i < objectsCount; i++) {
			for (int j = i+1 ; j < objectsCount; j++) {
				//	System.out.println(hashCoeficientes.get(autores.get(i)).get(autores.get(j)));
					double valor = hashCoeficientes.get(autores.get(i)).get(autores.get(j));
					matrizDeSimilaridades.set(i, j, valor);
				//	matrizDeSimilaridades.set(j, i, 8);
			}
		}
		//System.out.println(matrizDeSimilaridades);
		return matrizDeSimilaridades;
	}


	public List<String> listaDeTodosAutores (HashMap<String, Set<String>> quantidadeVertices){
		List<String> todosVizinhos = new ArrayList<String>();
		for (String vizinho: quantidadeVertices.keySet()) {
			todosVizinhos.add(vizinho);
		}
		return todosVizinhos;

	}


	public HashMap<String, HashMap<String, Double>> criaHashMap(List<OverlapSimilaridade> listaCoeficientes) {
		HashMap<String, HashMap<String,Double>> hashCoeficientes = new HashMap<String, HashMap<String,Double>>();
		for (OverlapSimilaridade overlapSimilaridade : listaCoeficientes) {
			if(!hashCoeficientes.containsKey(overlapSimilaridade.getP1())){
				HashMap<String,Double> inner = new HashMap<String,Double>();
				inner.put(overlapSimilaridade.getP2(), overlapSimilaridade.getCalculo());
				hashCoeficientes.put(overlapSimilaridade.getP1(),inner);
			}else{
				hashCoeficientes.get(overlapSimilaridade.getP1()).put(overlapSimilaridade.getP2(), overlapSimilaridade.getCalculo());
			}

		}
		return hashCoeficientes;
	}


	
}
