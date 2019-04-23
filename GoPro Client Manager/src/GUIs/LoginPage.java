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
    
    private Connection conn; //Connection to the database
    private Main main; //pointer to the main class for the two public methods
    
    private Text title1 = new Text("Go Pro Paysagement");
    private Text subtitle1 = new Text("Client Manager");
    
    private Text promptSeasonYearText = new Text("Choose season and year:");
    
    private ComboBox<String> seasonPrompt = new ComboBox<>();//combo box for the seasons
    private ComboBox<String> yearPrompt = new ComboBox<>();//combo box for the years
    
    private Button enterBtn = new Button("Enter");//enter button when season and year have been chosen
    
    private int yearSelected;//year selected by user
    private String seasonSelected;//season selected by user
    private String seasonID;//seasonId from the season and year
    
    /**
     * Main constructor
     * @param conn
     * @param main
     */
    public LoginPage(Connection conn, Main main){
        this.conn = conn;
        this.main = main;
        
        fillComboBox();
        setUpPage();
        setButtonAction();
    }
    
    /***********************************
     *      SET-UP METHODS FOR CLASS
     ***********************************/
    
    //set the action of every button in the pane
    private void setButtonAction(){
        /*
        ENTER button:
        When pressed, the year selected and season selected is recorded into the attributes of the class
        The seasonId is then created using the two attributes, and passed as a parameter for the
        switchToClientFromLogin() method
        */
        this.enterBtn.setOnAction(e -> {
            //Record inputs selected
            this.yearSelected = Integer.parseInt(this.yearPrompt.getSelectionModel().getSelectedItem());
            this.seasonSelected = this.seasonPrompt.getSelectionModel().getSelectedItem();
            
            //Create seasonId
            switch(this.seasonSelected){
                case "Winter": this.seasonID = "W" + this.yearSelected;break;
                case "Summer": this.seasonID = "S" + this.yearSelected;break;
                default: System.out.println("ERROR");
            }
            
            //Switch to the main menu
            this.main.switchToClientFromLogin(this.seasonID);
        });
        
    }
   
    //Set up the page layout
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
    
    //Fill the combo boxes used to hold the options season and year
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
    
    
    /**
     * Returns the year selected by user
     * @return 
     */
    public int getYearSelected(){
        return this.yearSelected;
    }

    /**
     * Returns the season selected by user
     * @return
     */
    public String getSeasonSelected(){
        return this.seasonSelected;
    }

    /**
     * Returns the seasonId constructed from the season and year selected
     * @return
     */
    public String getSeasonID(){
        return this.seasonID;
    }
    
}
