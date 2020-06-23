package it.polito.tdp.seriea.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.polito.tdp.seriea.model.Adiacenza;
import it.polito.tdp.seriea.model.Season;
import it.polito.tdp.seriea.model.Team;

public class SerieADAO {
	
	public List<Season> listSeasons() {
		String sql = "SELECT season, description FROM seasons" ;
		
		List<Season> result = new ArrayList<>() ;
		
		Connection conn = DBConnect.getConnection() ;
		
		try {
			PreparedStatement st = conn.prepareStatement(sql) ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				result.add( new Season(res.getInt("season"), res.getString("description"))) ;
			}
			
			conn.close();
			return result ;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}
	
	public Map<String,Team> listTeams() {
		String sql = "SELECT team FROM teams" ;
		
		Map<String,Team> result = new HashMap<>() ;
		
		Connection conn = DBConnect.getConnection() ;
		
		try {
			PreparedStatement st = conn.prepareStatement(sql) ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				result.put( res.getString("team"),new Team(res.getString("team"))) ;
			}
			
			conn.close();
			return result ;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}
	
	/*
	 	select hometeam, awayteam, ftr
		from matches
		where season=2003
	 */
	public List<Adiacenza> listAdiacenze(Season s, Map<String,Team> squadre) {
		String sql = "select hometeam, awayteam, ftr " + 
				"		from matches " + 
				"		where season=? " ;
		
		List<Adiacenza> result = new ArrayList<>() ;
		
		Connection conn = DBConnect.getConnection() ;
		
		try {
			PreparedStatement st = conn.prepareStatement(sql) ;
			st.setInt(1, s.getSeason());
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				int risultato=0;
				switch(res.getString("ftr").toLowerCase()) {
				case "h":
					risultato=1;
					break;
				case "d":
					risultato=0;
					break;
				case "a":
					risultato=-1;
					break;
				}
				if(squadre.containsKey(res.getString("hometeam"))&&squadre.containsKey(res.getString("awayteam"))) {
					result.add( new Adiacenza(squadre.get(res.getString("hometeam")),squadre.get(res.getString("awayteam")),risultato)) ;
				}
				
			}
			
			conn.close();
			return result ;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}


}
