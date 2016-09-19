package source;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import datastructures.core.Matrix2D;

public class JaccardSimilaridade {

	public String p1;
	public String p2;
	public double interseccao;
	public double uniao;
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
	public double getUniao() {
		return uniao;
	}
	public void setUniao(double uniao) {
		this.uniao = uniao;
	}

	public double realizaCalculo(){
		return calculo = (double)interseccao/uniao;
	}

	public double getCalculo(){
		return calculo;
	}

	//	public Matrix2D criaMatriz(HashMap<String, HashMap<String, Double>> hashCoeficientes, int objectsCount, HashMap<String, Set<String>> quantidadeVertices) {
	//		Matrix2D matrizDeSimilaridades = new Matrix2D(objectsCount);
	//		List <String> autores = listaDeTodosAutores(quantidadeVertices);
	//		for (int i = 0; i < objectsCount; i++) {
	//
	//			int j = i+1;
	//			//obter segundo hash
	//			//iterar no segundo hash
	//			//a cada linha do segundo hash : setar na matriz o value
	//			HashMap<String, Double> a = hashCoeficientes.get(autores.get(i));								 
	//			for (Entry<String, Double> b : a.entrySet()) {
	//				matrizDeSimilaridades.set(i, j, b.getValue());
	//				matrizDeSimilaridades.set(j, i, b.getValue());
	//				j++;
	//			}
	//
	//		}
	//		return matrizDeSimilaridades;
	//	}


	//	public Matrix2D criaMatrizLenta(List<JaccardSimilaridade> listaDeCoeficientes, int objectsCount, HashMap<String, Set<String>> quantidadeVertices) {
	//		Matrix2D matrizDeSimilaridades = new Matrix2D(objectsCount);
	//		List <String> autores = listaDeTodosAutores(quantidadeVertices);
	//		for (int i = 0; i < objectsCount; i++) {
	//			for (int j = i+1 ; j < objectsCount; j++) {
	//				for (Entry<String, Set<String>> verticeVizinhos : quantidadeVertices.entrySet()) {
	//					for (JaccardSimilaridade jac : listaDeCoeficientes) {
	//						for (String vizinho : verticeVizinhos.getValue()) {
	//							if(jac.getP1().equals(verticeVizinhos.getKey())&& jac.getP2().equals(vizinho) || jac.getP1().equals(vizinho) && jac.getP2().equals(verticeVizinhos.getKey())){
	//								matrizDeSimilaridades.set(i, j, jac.getCalculo() );
	//
	//							}
	//
	//						}
	//					}
	//					}}
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


	public HashMap<String, HashMap<String, Double>> criaHashMap(List<JaccardSimilaridade> listaCoeficientesJaccard) {
		HashMap<String, HashMap<String,Double>> hashCoeficientes = new HashMap<String, HashMap<String,Double>>();
		for (JaccardSimilaridade jaccardSimilaridade : listaCoeficientesJaccard) {
			if(!hashCoeficientes.containsKey(jaccardSimilaridade.getP1())){
				HashMap<String,Double> inner = new HashMap<String,Double>();
				inner.put(jaccardSimilaridade.getP2(), jaccardSimilaridade.getCalculo());
				hashCoeficientes.put(jaccardSimilaridade.getP1(),inner);
			}else{
				hashCoeficientes.get(jaccardSimilaridade.getP1()).put(jaccardSimilaridade.getP2(), jaccardSimilaridade.getCalculo());
			}

		}
		return hashCoeficientes;
	}




}
