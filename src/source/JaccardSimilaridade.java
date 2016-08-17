package source;

import java.util.List;

import datastructures.core.Matrix2D;
import utility.similarity.ParDocumento;

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
	public Matrix2D criaMatriz(Matrix2D matrizDeSimilaridades,List<JaccardSimilaridade> listaDeCoeficientes, int objectsCount) {
		
		for (int i = 0; i < objectsCount; i++) {
			double similaridade = listaDeCoeficientes.get(i).getCalculo();
			for (int j = i+1 ; j < objectsCount; j++) {
				matrizDeSimilaridades.set(i, j, similaridade );
				System.out.println(matrizDeSimilaridades);

			}
		}
		return matrizDeSimilaridades;
	}
	
	
	}
