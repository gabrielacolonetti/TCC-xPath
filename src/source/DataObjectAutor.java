package source;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import datastructures.core.DataFeature;
import datastructures.core.DataObject;
import datastructures.core.MetaData;
import main.ClusteringProcess;
import model.Grafo;

public class DataObjectAutor extends DataObject {
	MetaData autor;
	List<MetaData> vizinhos;
	
	public DataObjectAutor(MetaData autor, List<MetaData> vizinhos){
		this.autor = autor;
		this.vizinhos = vizinhos;
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return null;
	}

	public  void addObject(ClusteringProcess primeiroProcesso, Grafo g, HashMap<String, Set<String>> quantidadeVertices) {
		for (Map.Entry<String,Set<String>> v1 : quantidadeVertices.entrySet()) {	   
		//	String id = v1.getKey();
		//	List<String> vizinhos = (List<String>) v1.getValue();
			autor = ("autor"+v1.getKey());
			vizinhos = v1.getValue();
			primeiroProcesso.addDataObject(dataobject);
       }		
	}
	
//	public static  void addObject(ClusteringProcess primeiroProcesso, Grafo g, HashMap<String, Set<String>> quantidadeVertices) {
//		for (Map.Entry<String,Set<String>> v1 : quantidadeVertices.entrySet()) {	   
//			String id = v1.getKey();
//			List<String> vizinhos = (List<String>) v1.getValue();
//			MetaDataTCC dataobject = new MetaDataTCC("autor", id);
//			dataobject.setVizinhos(vizinhos);
//			primeiroProcesso.addDataObject(dataobject);
//        }		
//	}
	 
	
//	public static  void addObject(ClusteringProcess primeiroProcesso, Grafo g, HashMap<String, Set<String>> quantidadeVertices) {
//		for (Map.Entry<String,Set<String>> v1 : quantidadeVertices.entrySet()) {	   
//			String id = v1.getKey();
//			List<String> vizinhos = (List<String>) v1.getValue();
//			MetaDataTCC dataobject = new MetaDataTCC("autor", id);
//			dataobject.setVizinhos(vizinhos);
//			primeiroProcesso.addDataObject(dataobject);
//        }		
//	}
	
//	public  void addObject(ClusteringProcess primeiroProcesso, Grafo g, HashMap<String, Set<String>> quantidadeVertices) {
//		for (Map.Entry<String,Set<String>> v1 : quantidadeVertices.entrySet()) {	   
//			List<String> vizinhos1 = (List<String>) v1.getValue();
//			String id = "autor";
//			String value = v1.getKey();
//			MetaDataTCC dataobject = new MetaDataTCC(id, value);
//			dataobject.setVizinhos(vizinhos1);
//			//autor = ("autor"+v1.getKey());
//			//vizinhos = v1.getValue();
//			primeiroProcesso.addDataObject(dataobject);
//        }		
//	}
	

	


	

	
}