package it.polito.tdp.seriea.model;

public class Adiacenza {
	
	Team t1;
	Team t2;
	//+1 vince in casa, 0 pareggio, -1 vince in trasferta
	int risultato;
	
	public Adiacenza(Team t1, Team t2, int risultato) {
		super();
		this.t1 = t1;
		this.t2 = t2;
		this.risultato = risultato;
	}

	public Team getT1() {
		return t1;
	}

	public void setT1(Team t1) {
		this.t1 = t1;
	}

	public Team getT2() {
		return t2;
	}

	public void setT2(Team t2) {
		this.t2 = t2;
	}

	public int getRisultato() {
		return risultato;
	}

	public void setRisultato(int risultato) {
		this.risultato = risultato;
	}
	
	
	

}
