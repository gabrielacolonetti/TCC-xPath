package source;

public class CosineSimilaridade {

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
	
	public double realizaCalculo(){
		return calculo = interseccao/Math.sqrt(qtdV1*qtdV2);
	}
	
	public double getCalculo(){
		return calculo;
	}
}
