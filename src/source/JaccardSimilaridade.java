package source;

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
	
	
	}
