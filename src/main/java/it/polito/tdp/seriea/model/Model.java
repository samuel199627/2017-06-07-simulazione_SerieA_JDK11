package it.polito.tdp.seriea.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.seriea.db.SerieADAO;

public class Model {
	
	SimpleDirectedWeightedGraph<Team,DefaultWeightedEdge> grafo=null;
	
	SerieADAO dao;
	Map<String,Team> squadre;
	
	public List<Season> caricaStagioni(){
		dao=new SerieADAO();
		return dao.listSeasons();
	}
	
	public String creaGrafo(Season stagione) {
		String ritornare="";
		dao=new SerieADAO();
		squadre=new HashMap<>();
		squadre=dao.listTeams();
		
		List<Adiacenza> adiacenze=new ArrayList<>();
		adiacenze=dao.listAdiacenze(stagione,squadre);
		
		
		grafo=new SimpleDirectedWeightedGraph<Team,DefaultWeightedEdge>(DefaultWeightedEdge.class);
		
		for(Adiacenza a: adiacenze) {
			if(!grafo.containsVertex(a.getT1())) {
				grafo.addVertex(a.getT1());
			}
			if(!grafo.containsVertex(a.getT2())) {
				grafo.addVertex(a.getT2());
			}
			//l'arco e' orientato sempre da chi e' in casa verso chi va fuori
			Graphs.addEdgeWithVertices(grafo, a.getT1(), a.getT2(), a.getRisultato());
			//metto i punteggi per la classifica
			switch(a.getRisultato()) {
			case 1:
				//System.out.println("VITTORIA IN CASA "+a.getT1().getPunteggio());
				a.getT1().setPunteggio(a.getT1().getPunteggio()+3);
				//System.out.println("dopo "+a.getT1().getPunteggio());
				break;
			case -1:
				a.getT2().setPunteggio(a.getT2().getPunteggio()+3);
				break;
			case 0:
				a.getT1().setPunteggio(a.getT1().getPunteggio()+1);
				a.getT2().setPunteggio(a.getT2().getPunteggio()+1);
				break;
			}
		}
		
		ritornare=ritornare+"GRAFO CREATO CON "+grafo.vertexSet().size()+" vertici e "+grafo.edgeSet().size()+" archi\ne la classifica e' cosi' composta:\n\n";
		
		//scrivo la classifica
		List<Team> classifica=new ArrayList<>();
		for(Team t: grafo.vertexSet()) {
			classifica.add(t);
		}
		classifica.sort(null);
		for(Team t: classifica) {
			ritornare=ritornare+t.getTeam()+" con punti "+t.getPunteggio()+"\n";
		}
		
		return ritornare;
	}
	
	List<Partita> soluzione;
	int bestLunghezza;
	Team partenza;
	//devo ricercare una sequenza di partite e non una sequenza di team
	//cerchiamo solo sequenze di vittorie in casa ( il testo era da interpretare)
	public String ricorsione(Team selezionato) {
		String ritornare="";
		partenza=selezionato;
		soluzione=new ArrayList<>();
		bestLunghezza=0;
		cerca(new ArrayList<Partita>(), 0);
		ritornare=ritornare+"RICORSIONE: \n\n";
		for(Partita p: soluzione) {
			ritornare=ritornare+""+p.getCasa().getTeam()+" - "+p.getTrasfera().getTeam()+"\n";
		}
		
		
		return ritornare;
	}
	
	public List<Team> vertici(){
		List<Team> vertici=new ArrayList<>();
		
		for(Team t: grafo.vertexSet()) {
			vertici.add(t);
		}
		return vertici;
	}
	
	public void cerca(List<Partita> parziale, int livello) {
		
		if(livello==0) {
			//provo a partire con un qualunque vertice dei grafo e prendere una partita in cui ha vinto
			/*
			for(Team t2: Graphs.neighborListOf(grafo,partenza)) {
				//la squadra in casa deve avere vinto
				if(grafo.getEdgeWeight(grafo.getEdge(partenza, t2))==1) {
					//allora esploro la soluzione
					parziale.add(new Partita(partenza,t2));
					cerca(parziale,livello+1);
					parziale.remove(parziale.size()-1);
				}
			}
			*/
			for(DefaultWeightedEdge e: grafo.outgoingEdgesOf(partenza)) {
				if(grafo.getEdgeWeight(e)==1) {
					parziale.add(new Partita(partenza,grafo.getEdgeTarget(e)));
					cerca(parziale,livello+1);
					parziale.remove(parziale.size()-1);
				}
			}
			
			
				
			
		}
		else {
			//scorro i vicini del team in trasferta dell'ultima partita inserita che diventa la squadra in casa in cui cercare
			/*
			for(Team t: Graphs.neighborListOf(grafo, parziale.get(parziale.size()-1).getTrasfera())) {
				//la squadra in casa deve avere vinto, ma non ci deve ancora essere la partita gia' in parziale
				if(grafo.getEdgeWeight(grafo.getEdge(parziale.get(parziale.size()-1).getTrasfera(),t))==1&&!parzialeContiene(parziale,parziale.get(parziale.size()-1).getTrasfera(),t)) {
					parziale.add(new Partita(parziale.get(parziale.size()-1).getTrasfera(),t));
					cerca(parziale,livello+1);
					parziale.remove(parziale.size()-1);
				}
			}
			*/
			Team nuovaPartenza=parziale.get(parziale.size()-1).getTrasfera();
			
			for(DefaultWeightedEdge e: grafo.outgoingEdgesOf(nuovaPartenza)) {
				if(grafo.getEdgeWeight(e)==1 && !parziale.contains(new Partita(nuovaPartenza,grafo.getEdgeTarget(e)))) {
					parziale.add(new Partita(nuovaPartenza,grafo.getEdgeTarget(e)));
					cerca(parziale,livello+1);
					parziale.remove(parziale.size()-1);
				}
			}
		}
		if(parziale.size()>bestLunghezza) {
			System.out.println("TROVATO MASSSIMO "+bestLunghezza);
			bestLunghezza=parziale.size();
			soluzione=new ArrayList<>(parziale);
			
		}
	}
	
	public boolean parzialeContiene(List<Partita> parziale,Team cas, Team trasf) {
		for(Partita p: parziale) {
			if(p.getCasa().getTeam().equals(cas.getTeam())&&p.getTrasfera().getTeam().equals(trasf.getTeam())) {
				return true;
			}
		}
		return false;
	}

}
