package it.polito.tdp.itunes.model;

import java.util.List;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import it.polito.tdp.itunes.db.ItunesDAO;

public class Model {
	
	private Graph<Album, DefaultEdge> grafo ;
	private List<Album> albums;
	private ItunesDAO dao = new ItunesDAO();
	private List<Album> archi;

	public void creaGrafo(double durata) {
		
		this.albums= dao.getVertici(durata);
		
		
		this.grafo = new SimpleGraph<>(DefaultEdge.class);
		Graphs.addAllVertices(this.grafo, this.albums);
		this.archi = dao.getArchi();
		
		for(Album a1 : this.archi) {
			for(Album a2 : this.archi) {
				if(a1.compareTo(a2)!=0 && albums.contains(a1) && albums.contains(a2)) {
					grafo.addEdge(a1, a2);
				}
			}
		}
		
	}
	
	public List<Album> restituisciVertici(double durata){
		return dao.getVertici(durata);
	}
	
	public String componentiConnesse(Album a) {
		
		String risultato ="";
		double durataTot=0.0;
		
		ConnectivityInspector<Album, DefaultEdge> connectivity = new ConnectivityInspector<>(this.grafo);
		
		Set<Album> connessi= connectivity.connectedSetOf(a);
		
		for(Album a1 : connessi) {
			for(Album a2 : this.albums) {
				if(a1.compareTo(a2)==0) {
					durataTot = durataTot+a1.getDurata();
				}
			}
		}
		
		risultato = "L'album "+a.getTitle()+" è connesso a "+connessi.size()+" albums. \n Ha una durata totale di: "+durataTot;
		
		return risultato;
	}
}
