/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUIs;

import Application.Main;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Calendar;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

/**
 *
 * @author purplesmurf
 */
public class LoginPage extends GridPane{
    
    private Connection conn;
    private Main main;
    
    private Text title1 = new Text("Go Pro Paysagement");
    private Text subtitle1 = new Text("Client Manager");
    
    private Text promptSeasonYearText = new Text("Choose season and year:");
    
    private ComboBox<String> seasonPrompt = new ComboBox<>();
    private ComboBox<String> yearPrompt = new ComboBox<>();
    
    private Button enterBtn = new Button("Enter");
    
    private int yearSelected;
    private String seasonSelected;
    private String seasonID;
    
    public LoginPage(){
        
    }
    
    public LoginPage(Connection conn, Main main){
        this.conn = conn;
        this.main = main;
        
        fillComboBox();
        setUpPage();
        setBtnAction();
    }
    
    /***********************************
     *      SET-UP METHODS FOR CLASS
     ***********************************/
    
    private void setBtnAction(){
        this.enterBtn.setOnAction(e -> {
            this.yearSelected = Integer.parseInt(this.yearPrompt.getSelectionModel().getSelectedItem());
            this.seasonSelected = this.seasonPrompt.getSelectionModel().getSelectedItem();
            switch(this.seasonSelected){
                case "Winter": this.seasonID = "W" + this.yearSelected;break;
                case "Summer": this.seasonID = "S" + this.yearSelected;break;
                default: System.out.println("ERROR");
            }
            this.main.switchToClientFromLogin(this.seasonID);
        });
        
    }
    private void setUpPage(){
        this.add(title1, 1, 0);
        this.add(subtitle1, 1, 1);
        this.add(promptSeasonYearText, 1, 3);
        this.add(seasonPrompt, 0, 4);
        this.add(yearPrompt, 2, 4);
        this.add(enterBtn, 1, 5);
        
        this.setAlignment(Pos.CENTER);
        this.setPadding(new Insets(15, 15, 15, 15));
        this.setHgap(30);
        this.setVgap(30);
        
        GridPane.setHalignment(title1, HPos.CENTER);
        GridPane.setHalignment(subtitle1, HPos.CENTER);
        GridPane.setHalignment(promptSeasonYearText, HPos.CENTER);
        GridPane.setHalignment(seasonPrompt, HPos.RIGHT);
        GridPane.setHalignment(yearPrompt, HPos.LEFT);
        GridPane.setHalignment(enterBtn, HPos.CENTER);
        
        title1.setFont(Font.font("Rockwell", FontWeight.BOLD, 75));
        subtitle1.setFont(Font.font("Rockwell", FontWeight.LIGHT, 45));
        promptSeasonYearText.setFont(Font.font("Rockwell", FontWeight.THIN, 25));
        seasonPrompt.setMinSize(150, 30);
        seasonPrompt.setMaxSize(150, 30);
        yearPrompt.setMinSize(150, 30);
        yearPrompt.setMaxSize(150, 30);
        enterBtn.setFont(Font.font("Rockwell", 30));
        
    }
    private void fillComboBox(){
        
        for (int i = 2018; i < 2026; i++){
            yearPrompt.getItems().add("" + i);
        }
        
        int year = Calendar.getInstance().get(Calendar.YEAR);
        yearPrompt.getSelectionModel().select(year - 2018);
        
        seasonPrompt.getItems().add("Winter");
        seasonPrompt.getItems().add("Summer");
        
        seasonPrompt.getSelectionModel().select(0);
    }
    
    
    /*****************************************
     *          GET METHODS FOR CLASS
     *****************************************/
    
    
    public int getYearSelected(){
        return this.yearSelected;
    }
    
    public String getSeasonSelected(){
        return this.seasonSelected;
    }
    
    public String getSeasonID(){
        return this.seasonID;
    }
    
}
