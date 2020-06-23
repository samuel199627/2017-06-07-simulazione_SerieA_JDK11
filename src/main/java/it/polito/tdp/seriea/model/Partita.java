package it.polito.tdp.seriea.model;

public class Partita {
	
	Team casa;
	Team trasfera;
	
	public Partita(Team casa, Team trasfera) {
		super();
		this.casa = casa;
		this.trasfera = trasfera;
	}

	public Team getCasa() {
		return casa;
	}

	public void setCasa(Team casa) {
		this.casa = casa;
	}

	public Team getTrasfera() {
		return trasfera;
	}

	public void setTrasfera(Team trasfera) {
		this.trasfera = trasfera;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((casa == null) ? 0 : casa.hashCode());
		result = prime * result + ((trasfera == null) ? 0 : trasfera.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Partita other = (Partita) obj;
		if (casa == null) {
			if (other.casa != null)
				return false;
		} else if (!casa.equals(other.casa))
			return false;
		if (trasfera == null) {
			if (other.trasfera != null)
				return false;
		} else if (!trasfera.equals(other.trasfera))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Partita [casa=" + casa.getTeam() + ", trasfera=" + trasfera.getTeam() + "]";
	}
	
	
	

}
