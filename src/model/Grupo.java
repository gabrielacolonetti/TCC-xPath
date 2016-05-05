package model;

import java.util.ArrayList;
import java.util.List;


import org.jgrapht.graph.DefaultWeightedEdge;

public class Grupo {

	List<DefaultWeightedEdge> grupos;
	
	public Grupo(){
		grupos = new ArrayList<DefaultWeightedEdge>();
	}

	public List<DefaultWeightedEdge> getGrupos() {
		return grupos;
	}

	public void setGrupos(List<DefaultWeightedEdge> grupos) {
		this.grupos = grupos;
	}


	
}
