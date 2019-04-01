/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Objects;

import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 *
 * @author purplesmurf
 */
public class Printer extends Stage{
    
    private Pane pane = new Pane();
    private TextField fileNameField = new TextField();
    private String fileName = "";
    
    public Printer(){
        this.pane.getChildren().add(this.fileNameField);
        this.setScene(new Scene(this.pane));
    }
    
    public TextField getField(){
        return this.fileNameField;
    }
    
    public String getFileName(){
        return this.fileName;
    }
    
    public void setFileName(){
        this.fileName = this.fileNameField.getText();
    }
    
}
