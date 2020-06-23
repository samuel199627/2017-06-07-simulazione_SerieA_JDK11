package it.polito.tdp.seriea;

import java.net.URL;
import java.util.ResourceBundle;

import it.polito.tdp.seriea.model.Model;
import it.polito.tdp.seriea.model.Season;
import it.polito.tdp.seriea.model.Team;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;

public class FXMLController {
	
	private Model model;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ChoiceBox<Season> boxSeason;

    @FXML
    private ChoiceBox<Team> boxTeam;

    @FXML
    private TextArea txtResult;

    @FXML
    void handleCarica(ActionEvent event) {
    	txtResult.clear();
    	Season selezionata=boxSeason.getValue();
    	if(selezionata==null) {
    		txtResult.appendText("DEVI SELEZIONARE UNA STAGIONE");
    		return;
    	}
    	
    	txtResult.appendText(model.creaGrafo(selezionata));
    	
    	boxTeam.getItems().clear();
    	boxTeam.getItems().addAll(model.vertici());
    }

    @FXML
    void handleDomino(ActionEvent event) {
    	txtResult.clear();
    	Team selezionato=boxTeam.getValue();
    	if(selezionato==null) {
    		txtResult.appendText("DEVI SELEZIONARE UNA SQUADRA");
    		return;
    	}
    	txtResult.appendText(model.ricorsione(selezionato));
    	
    }

    @FXML
    void initialize() {
        assert boxSeason != null : "fx:id=\"boxSeason\" was not injected: check your FXML file 'SerieA.fxml'.";
        assert boxTeam != null : "fx:id=\"boxTeam\" was not injected: check your FXML file 'SerieA.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'SerieA.fxml'.";

    }

	public void setModel(Model model) {
		this.model = model;
		boxSeason.getItems().clear();
		boxSeason.getItems().addAll(model.caricaStagioni());
	}
}
