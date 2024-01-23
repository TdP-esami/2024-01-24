package it.polito.tdp.gosales.model;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleDirectedGraph;

import it.polito.tdp.gosales.dao.GOsalesDAO;

public class Model {
	
	private GOsalesDAO dao;
	private SimpleDirectedGraph<Products,DefaultEdge> grafo;
	private Map<Integer, Double> mappaRicavi;
	private List<ProductsExt> cammino;
	private int bestLengthCammino;
	
	public Model() {
		this.dao = new GOsalesDAO();
		mappaRicavi = new HashMap<Integer, Double>();
	}
	
	
	public List<Methods> getMethods(){
		return this.dao.getMethods();
	}
	
	public List<Integer> getYears(){
		return this.dao.getYears();
	}
	
	
	public void creaGrafo(Methods metodo, int anno, double S) {
		this.grafo = new SimpleDirectedGraph<Products, DefaultEdge>(DefaultEdge.class);
	
		//Vertici
		List<Products> vertici= this.dao.getVertici(metodo, anno);
		mappaRicavi = this.dao.getRicavoProdotti(metodo, anno);
		Graphs.addAllVertices(this.grafo, vertici);
		
		//Archi
		for(Products p1 : vertici) {
			for(Products p2 : vertici) {
				if (!p1.equals(p2)) {
					if(this.mappaRicavi.get(p1.getNumber())>= (1+S)*this.mappaRicavi.get(p2.getNumber())) {
						this.grafo.addEdge(p2, p1);
					} else if(this.mappaRicavi.get(p2.getNumber())>= (1+S)*this.mappaRicavi.get(p1.getNumber())) {
						this.grafo.addEdge(p1, p2);
					}
				}
			}
		}
	}
	
	public int getNVertici() {
		return this.grafo.vertexSet().size();
	}
	
	public Set<Products> getVertici() {
		return this.grafo.vertexSet();
	}
	
	public int getNArchi() {
		return this.grafo.edgeSet().size();
	}
	
	
	
	public List<ProductsExt> calcolaProdotti() {
		List<ProductsExt> result = new ArrayList<ProductsExt>();
		
		for (Products v : this.grafo.vertexSet()) {
			int outgoingEdges = this.grafo.outgoingEdgesOf(v).size();
			int incomingEdges = this.grafo.incomingEdgesOf(v).size();
			if (outgoingEdges == 0 ) {
				result.add(new ProductsExt(v, this.mappaRicavi.get(v.getNumber()), incomingEdges  )  );
			}
		}
		
		Collections.sort(result);
		return result;
	}
	
	
	
	public List<ProductsExt> calcolaCammino(){
		this.cammino = new ArrayList<ProductsExt>();
		this.bestLengthCammino = 0;
		
		for (Products p : this.grafo.vertexSet()) {
			if (this.grafo.inDegreeOf(p)==0){
				List<ProductsExt> parziale = new ArrayList<ProductsExt>();
				parziale.add(new ProductsExt(p, this.mappaRicavi.get(p.getNumber()), this.grafo.outDegreeOf(p)) );
				doRicorsione(parziale);
			}			
		}
		return this.cammino;
	}
	
	
	
	private void doRicorsione(List<ProductsExt> parziale) {		

		// caso terminale
		ProductsExt last = parziale.get(parziale.size()-1);
		if ( this.grafo.outDegreeOf(last.getProdotto())  == 0 ) {
			int lengthCammino = parziale.size()-1;
			if (lengthCammino > this.bestLengthCammino) {
				this.bestLengthCammino = lengthCammino;
				this.cammino = new ArrayList<ProductsExt>(parziale);
			}
			return;
		}

		// caso normale
		Set<DefaultEdge> archiUscenti = this.grafo.outgoingEdgesOf(last.getProdotto());
		for (DefaultEdge arco : archiUscenti) {
			//nodo target
			Products next = this.grafo.getEdgeTarget(arco);
			//aggiorna parziale
			parziale.add(  new ProductsExt(next, this.mappaRicavi.get(next.getNumber()), 0)  );
				
			//fai un'altro step della ricorsione
			doRicorsione(parziale);
				
			//backtracking
			parziale.remove(parziale.size()-1);
			
		}
	}
		
		
}
	

