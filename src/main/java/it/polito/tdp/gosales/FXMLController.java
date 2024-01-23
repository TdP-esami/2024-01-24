package it.polito.tdp.gosales;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.gosales.model.Methods;
import it.polito.tdp.gosales.model.Model;
import it.polito.tdp.gosales.model.ProductsExt;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {
	
	private Model model;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button btnCreaGrafo;

    @FXML
    private Button btnProdotti;

    @FXML
    private Button btnRicorsione;

    @FXML
    private ComboBox<Integer> cmbAnno;

    @FXML
    private ComboBox<Methods> cmbMetodo;

    @FXML
    private TextArea txtResult;

    @FXML
    private TextField txtS;

    @FXML
    void doCalcolaProdotti(ActionEvent event) {
    	List<ProductsExt> prodottiRicavi = this.model.calcolaProdotti();
    	this.txtResult.appendText("I prodotti più redditizi sono:\n ");
    	int N = Integer.min(5, prodottiRicavi.size());
    	for (int i = 0; i<N; i++) {
    		this.txtResult.appendText(prodottiRicavi.get(i) + "\n");
    	}
    	this.txtResult.appendText("\n\n");
    }
    

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	//controllo input
    	Integer anno = this.cmbAnno.getValue();
    	if (anno==null) {
    		this.txtResult.setText("Selezionare un anno\n");
    		return;
    	}
    	Methods metodo = this.cmbMetodo.getValue();
    	if (metodo==null) {
    		this.txtResult.setText("Selezionare una metodo\n");
    		return;
    	}
    	
    	//Controllo input
    	double S =0;
    	try {
    		S = Double.parseDouble(this.txtS.getText());
    	}catch(NumberFormatException e) {
    		this.txtResult.setText("S deve essere un double.");
    		return;
    	}
    	if (S<=0) {
    		this.txtResult.setText("S deve essere un numero positivo.");
    		return;
    	}
    	
    	//creazione grafo
    	this.model.creaGrafo(metodo, anno, S);
    	
    	//stampa informazioni
    	this.txtResult.setText("Grafo creato.\n");
    	this.txtResult.appendText("Ci sono " + this.model.getNVertici() + " vertici.\n");
    	this.txtResult.appendText("Ci sono " + this.model.getNArchi() + " archi.\n\n");	
    	
    	this.btnProdotti.setDisable(false);
        this.btnRicorsione.setDisable(false);
    }
    
    
    
    

    @FXML
    void doRicorsione(ActionEvent event) {
    	List<ProductsExt> cammino = this.model.calcolaCammino();
    	
    	if (cammino.size()<2) {
    		this.txtResult.appendText("Nessun cammino trovato con le specifiche richieste\n\n");
    	} else {
    		this.txtResult.appendText("Il miglior cammino trovato è:\n");
    		for( int i = 0; i<cammino.size(); i++) {
    			this.txtResult.appendText(cammino.get(i).toStringReduced() + "\n");
    		}
    		this.txtResult.appendText("\n\n");
    	}

    }

    @FXML
    void initialize() {
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnProdotti != null : "fx:id=\"btnProdotti\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnRicorsione != null : "fx:id=\"btnRicorsione\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbAnno != null : "fx:id=\"cmbAnno\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbMetodo != null : "fx:id=\"cmbMetodo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtS != null : "fx:id=\"txtS\" was not injected: check your FXML file 'Scene.fxml'.";
        this.btnProdotti.setDisable(true);
        this.btnRicorsione.setDisable(true);
    }
    
    
    public void setModel(Model model) {
    	this.model = model;
    	this.cmbMetodo.getItems().setAll(this.model.getMethods());
    	this.cmbAnno.getItems().setAll(this.model.getYears());
    	
    }
    

}
