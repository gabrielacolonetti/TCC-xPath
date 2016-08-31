package source;

import java.awt.FontFormatException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
	public Matrix2D criaMatriz(List<JaccardSimilaridade> listaDeCoeficientes, int objectsCount, HashMap<String, Set<String>> quantidadeVertices) {
		 Matrix2D matrizDeSimilaridades = new Matrix2D(objectsCount);
		 List <String> autores = listaDeTodosAutores(quantidadeVertices);
		for (int i = 0; i < objectsCount; i++) {
			for (int j = i+1 ; j < objectsCount; j++) {
				 for (JaccardSimilaridade jac : listaDeCoeficientes) {
					 if(jac.getP1().equals(autores.get(i))&& jac.getP2().equals(autores.get(j)) || jac.getP1().equals(autores.get(j)) && jac.getP2().equals(autores.get(i))){
						// System.out.println(jac.getP1()+"-"+jac.getP2()+" "+jac.getCalculo());
						 matrizDeSimilaridades.set(i, j, jac.getCalculo() );
					 }
				}
			}
		}
		return matrizDeSimilaridades;
	}

	public List<String> listaDeTodosAutores (HashMap<String, Set<String>> quantidadeVertices){
		List<String> todosVizinhos = new ArrayList<String>();
		for (String vizinho: quantidadeVertices.keySet()) {
			todosVizinhos.add(vizinho);
		}
		return todosVizinhos;
		
	}
	  
}
