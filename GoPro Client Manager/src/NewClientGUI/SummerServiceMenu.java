/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package NewClientGUI;

import java.sql.Connection;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 *
 * @author purplesmurf
 */
public class SummerServiceMenu extends GridPane{
    
    private Connection conn;
    
    /*************************
     * SERVICES VARIABLES
     *************************/
    private final ToggleButton lawnBtn = new ToggleButton("Lawn");
    private final ToggleButton springBtn = new ToggleButton("Spring");
    private final ToggleButton fallBtn = new ToggleButton("Fall");
    private final ToggleButton weedTreatmentBtn = new ToggleButton("Weed Treatment");
    private final ToggleButton aerationSpringBtn = new ToggleButton("Aeration Spring");
    private final ToggleButton aerationFallBtn = new ToggleButton("Aeration Fall");
    private final ToggleButton spiderBtn = new ToggleButton("Spider");
    private final ToggleButton weedingBtn = new ToggleButton("Weeding");
    private final ToggleButton hedgesBtn = new ToggleButton("Hedges");
    private final ToggleButton fertilizerBtn = new ToggleButton("Fertilizer");
    private final ToggleButton wormsBtn = new ToggleButton("Worms");
    private final ToggleButton soilBtn = new ToggleButton("Soil");
    private final ToggleButton seedingBtn = new ToggleButton("Seeding");
    
    private final ToggleButton[] btnList = {lawnBtn, springBtn, fallBtn, weedTreatmentBtn, aerationSpringBtn, aerationFallBtn, 
        spiderBtn, weedingBtn, hedgesBtn, fertilizerBtn, wormsBtn, soilBtn, seedingBtn};
    
    private final TextField lawnFld = new TextField();
    private final TextField springFld = new TextField();
    private final TextField fallFld = new TextField();
    private final TextField weedTreatmentFld = new TextField();
    private final TextField aerationSpringFld = new TextField();
    private final TextField aerationFallFld = new TextField();
    private final TextField spiderFld = new TextField();
    private final TextField weedingFld = new TextField();
    private final TextField hedgesFld = new TextField();
    private final TextField fertilizerFld = new TextField();
    private final TextField wormsFld = new TextField();
    private final TextField soilFld = new TextField();
    private final TextField seedingFld = new TextField();
    
    private final TextField[] fldList = {lawnFld, springFld, fallFld, weedTreatmentFld, aerationSpringFld, aerationFallFld, 
        spiderFld, weedingFld, hedgesFld, fertilizerFld, wormsFld, soilFld, seedingFld};
    
    private final Button commentBtn = new Button("Comments");
    
    private final HBox totalPane = new HBox();
    private final CheckBox taxBtn = new CheckBox();
    private final Text totalText = new Text("TOTAL: $0.00");
    
    private final TextArea commentSct = new TextArea();
    private final Stage commentWindow = new Stage();
    private final GridPane commentPane = new GridPane();
    private Scene commentScene;
    private final Button saveCommentBtn = new Button("Save Comments");
    
    private double total = 0;
    private final double taxCst = 1.14972;
    private String comment = "";
    private double lawn = 0;
    private double spring = 0;
    private double fall = 0;
    private double weedTreatment = 0;
    private double aerationSpring = 0;
    private double aerationFall = 0;
    private double spider = 0;
    private double weeding = 0;
    private double hedges = 0;
    private double fertilizer = 0;
    private double worms = 0;
    private double soil = 0;
    private double seeding = 0;
    
    private final Alert errorAlert = new Alert(Alert.AlertType.ERROR);
    
    public SummerServiceMenu(Connection conn){
        this.conn = conn;
        
        setUpPane();
    }
    
    private void setUpPane(){
        this.setPadding(new Insets(10, 10, 10, 10));
        this.setAlignment(Pos.CENTER);
        this.setVgap(10);
        this.setHgap(30);
        
        this.add(this.lawnBtn, 0, 0);
        this.add(this.springBtn, 0, 1);
        this.add(this.fallBtn, 0, 2);
        this.add(this.weedTreatmentBtn, 0, 3);
        this.add(this.aerationSpringBtn, 0, 4);
        this.add(this.aerationFallBtn, 0, 5);
        this.add(this.spiderBtn, 0, 6);
        this.add(this.weedingBtn, 0, 7);
        this.add(this.hedgesBtn, 2, 0);
        this.add(this.fertilizerBtn, 2, 1);
        this.add(this.wormsBtn, 2, 2);
        this.add(this.soilBtn, 2, 3);
        this.add(this.seedingBtn, 2, 4);
        
        this.add(this.lawnFld, 1, 0);
        this.add(this.springFld, 1, 1);
        this.add(this.fallFld, 1, 2);
        this.add(this.weedTreatmentFld, 1, 3);
        this.add(this.aerationSpringFld, 1, 4);
        this.add(this.aerationFallFld, 1, 5);
        this.add(this.spiderFld, 1, 6);
        this.add(this.weedingFld, 1, 7);
        this.add(this.hedgesFld, 4, 0);
        this.add(this.fertilizerFld, 4, 1);
        this.add(this.wormsFld, 4, 2);
        this.add(this.soilFld, 4, 3);
        this.add(this.seedingFld, 4, 4);
        
        this.add(this.totalPane, 2, 6);
        this.add(this.commentBtn, 2, 7);
        
        setUpTotalPane();
        
        
        for (int i = 0; i < btnList.length; i++){
            btnList[i].setMinSize(200, 30);
            btnList[i].setMaxSize(200, 30);
            btnList[i].setFont(Font.font("Rockwell"));
            btnList[i].setFocusTraversable(false);
        }
       
        for (int i = 0; i < fldList.length; i++){
            fldList[i].setMinSize(80, 30);
            fldList[i].setMaxSize(80, 30);
            fldList[i].setFont(Font.font("Rockwell"));
            fldList[i].setEditable(false);
            fldList[i].setFocusTraversable(false);
           
        }
        
        this.totalText.setFont(Font.font("Rockwell"));
        this.taxBtn.setSelected(true);
        
        setUpButtons();
        setUpFields();
        setUpComments();
    }
    
    private void setUpTotalPane(){
        this.totalPane.setSpacing(5);
        this.totalPane.getChildren().addAll(this.taxBtn, this.totalText);
    }
    
    private void updateTotals(){
        this.total = this.lawn + this.spring + this.fall + this.weedTreatment + this.aerationSpring + this.aerationFall + 
                this.spider + this.weeding + this.hedges + this.fertilizer + this.worms + this.soil + this.seeding;
        if (this.taxBtn.isSelected())
            this.total = this.total * this.taxCst;
        
        this.totalText.setText("TOTAL: $" + String.format("%.2f", this.total));
    }
    
    private void setUpComments(){
        this.commentPane.add(this.commentSct, 0, 0);
        this.commentPane.add(this.saveCommentBtn, 0, 1);
        this.commentScene = new Scene(this.commentPane, 300, 200);
        this.commentWindow.setScene(this.commentScene);
        this.commentWindow.setTitle("Comments");
        
        GridPane.setHalignment(this.saveCommentBtn, HPos.RIGHT);
        
        this.commentPane.setPadding(new Insets(5, 5, 5, 5));
        this.commentPane.setVgap(5);
        this.commentPane.setHgap(5);
        this.commentPane.setAlignment(Pos.CENTER);
        
        this.saveCommentBtn.setOnAction(e -> {
            this.comment = this.commentSct.getText();
            this.commentWindow.close();
        });
        
    }
    
    private void setUpButtons(){
        this.lawnBtn.setOnAction(e -> {
            if (this.lawnBtn.isSelected()){
                this.lawnFld.setEditable(true);
                this.lawnFld.requestFocus();
                this.lawnFld.setText("475");
                this.lawnFld.selectAll();
                this.lawn = 475;
                this.updateTotals();
            }
            else{
                this.lawnFld.setText("");
                this.lawnFld.setEditable(false);
                this.lawn = 0;
                this.updateTotals();
            }
            
        });
        
        this.springBtn.setOnAction(e -> {
            if (this.springBtn.isSelected()){
                this.springFld.setEditable(true);
                this.springFld.requestFocus();
                this.springFld.setText("120");
                this.springFld.selectAll();
                this.spring = 120;
                this.updateTotals();
            }
            else{
                this.springFld.setText("");
                this.springFld.setEditable(false);
                this.spring = 0;
                this.updateTotals();
            }
            
        });
        
        this.fallBtn.setOnAction(e -> {
            if (this.fallBtn.isSelected()){
                this.fallFld.setEditable(true);
                this.fallFld.requestFocus();
                this.fallFld.setText("195");
                this.fallFld.selectAll();
                this.fall = 195;
                this.updateTotals();
            }
            else{
                this.fallFld.setText("");
                this.fallFld.setEditable(false);
                this.fall = 0;
                this.updateTotals();
            }
            
        });
        
        this.weedTreatmentBtn.setOnAction(e -> {
            if (this.weedTreatmentBtn.isSelected()){
                this.weedTreatmentFld.setEditable(true);
                this.weedTreatmentFld.requestFocus();
                this.weedTreatmentFld.setText("65");
                this.weedTreatmentFld.selectAll();
                this.weedTreatment = 65;
                this.updateTotals();
            }
            else{
                this.weedTreatmentFld.setText("");
                this.weedTreatmentFld.setEditable(false);
                this.weedTreatment = 0;
                this.updateTotals();
            }
            
        });
        
        this.aerationSpringBtn.setOnAction(e -> {
            if (this.aerationSpringBtn.isSelected()){
                this.aerationSpringFld.setEditable(true);
                this.aerationSpringFld.requestFocus();
                this.aerationSpringFld.setText("65");
                this.aerationSpringFld.selectAll();
                this.aerationSpring = 65;
                this.updateTotals();
            }
            else{
                this.aerationSpringFld.setText("");
                this.aerationSpringFld.setEditable(false);
                this.aerationSpring = 0;
                this.updateTotals();
            }
            
        });
        
        this.aerationFallBtn.setOnAction(e -> {
            if (this.aerationFallBtn.isSelected()){
                this.aerationFallFld.setEditable(true);
                this.aerationFallFld.requestFocus();
                this.aerationFallFld.setText("65");
                this.aerationFallFld.selectAll();
                this.aerationFall = 65;
                this.updateTotals();
            }
            else{
                this.aerationFallFld.setText("");
                this.aerationFallFld.setEditable(false);
                this.aerationFall = 0;
                this.updateTotals();
            }
            
        });
        
        this.spiderBtn.setOnAction(e -> {
            if (this.spiderBtn.isSelected()){
                this.spiderFld.setEditable(true);
                this.spiderFld.requestFocus();
                this.spiderFld.setText("65");
                this.spiderFld.selectAll();
                this.spider = 65;
                this.updateTotals();
            }
            else{
                this.spiderFld.setText("");
                this.spiderFld.setEditable(false);
                this.spider = 0;
                this.updateTotals();
            }
            
        });
        
        this.weedingBtn.setOnAction(e -> {
            if (this.weedingBtn.isSelected()){
                this.weedingFld.setEditable(true);
                this.weedingFld.requestFocus();
                this.weedingFld.setText("0");
                this.weedingFld.selectAll();
                this.updateTotals();
            }
            else{
                this.weedingFld.setText("");
                this.weedingFld.setEditable(false);
                this.weeding = 0;
                this.updateTotals();
            }
            
        });
        
        this.hedgesBtn.setOnAction(e -> {
            if (this.hedgesBtn.isSelected()){
                this.hedgesFld.setEditable(true);
                this.hedgesFld.requestFocus();
                this.hedgesFld.setText("0");
                this.hedgesFld.selectAll();
                this.updateTotals();
            }
            else{
                this.hedgesFld.setText("");
                this.hedgesFld.setEditable(false);
                this.hedges = 0;
                this.updateTotals();
            }
            
        });
        
        this.fertilizerBtn.setOnAction(e -> {
            if (this.fertilizerBtn.isSelected()){
                this.fertilizerFld.setEditable(true);
                this.fertilizerFld.requestFocus();
                this.fertilizerFld.setText("165");
                this.fertilizerFld.selectAll();
                this.fertilizer = 165;
                this.updateTotals();
            }
            else{
                this.fertilizerFld.setText("");
                this.fertilizerFld.setEditable(false);
                this.fertilizer = 0;
                this.updateTotals();
            }
            
        });
        
        this.wormsBtn.setOnAction(e -> {
            if (this.wormsBtn.isSelected()){
                this.wormsFld.setEditable(true);
                this.wormsFld.requestFocus();
                this.wormsFld.setText("95");
                this.wormsFld.selectAll();
                this.worms = 95;
                this.updateTotals();
            }
            else{
                this.wormsFld.setText("");
                this.wormsFld.setEditable(false);
                this.worms = 0;
                this.updateTotals();
            }
            
        });
        
        this.soilBtn.setOnAction(e -> {
            if (this.soilBtn.isSelected()){
                this.soilFld.setEditable(true);
                this.soilFld.requestFocus();
                this.soilFld.setText("0");
                this.soilFld.selectAll();
                this.updateTotals();
            }
            else{
                this.soilFld.setText("");
                this.soilFld.setEditable(false);
                this.soil = 0;
                this.updateTotals();
            }
            
        });
        
        this.seedingBtn.setOnAction(e -> {
            if (this.seedingBtn.isSelected()){
                this.seedingFld.setEditable(true);
                this.seedingFld.requestFocus();
                this.seedingFld.setText("0");
                this.seedingFld.selectAll();
                this.updateTotals();
            }
            else{
                this.seedingFld.setText("");
                this.seedingFld.setEditable(false);
                this.seeding = 0;
                this.updateTotals();
            }
            
        });
        
        this.taxBtn.setOnAction(e -> {this.updateTotals();});
        
        this.commentBtn.setOnAction(e -> {
            this.commentWindow.show();
        });
        
    }
    
    private void setUpFields(){
        this.lawnFld.setOnKeyReleased(e -> {
            if (this.lawnFld.isEditable() && this.lawnFld.getText().length() > 0){    
                try {
                    this.lawn = Double.parseDouble(this.lawnFld.getText());
                    this.updateTotals();
                }catch(NumberFormatException ex){
                    this.errorAlert.setTitle("Lawn Field Error");
                    this.errorAlert.setHeaderText("Input error in text field for 'Lawn' service");
                    this.errorAlert.setContentText("Hint: There should be no symbols or characters for price\n"
                            + "Only input numbers and decimal point if needed.");
                    this.errorAlert.show();
                }    
            }
            else if (this.lawnFld.isEditable() && this.lawnFld.getText().length() == 0){
                this.lawn = 0;
                this.updateTotals();
            }
        });
        
        this.springFld.setOnKeyReleased(e -> {
            if (this.springFld.isEditable() && this.springFld.getText().length() > 0){    
                try {
                    this.spring = Double.parseDouble(this.springFld.getText());
                    this.updateTotals();
                }catch(NumberFormatException ex){
                    this.errorAlert.setTitle("Spring Field Error");
                    this.errorAlert.setHeaderText("Input error in text field for 'Spring' service");
                    this.errorAlert.setContentText("Hint: There should be no symbols or characters for price\n"
                            + "Only input numbers and decimal point if needed.");
                    this.errorAlert.show();
                }    
            }
            else if (this.springFld.isEditable() && this.springFld.getText().length() == 0){
                this.spring = 0;
                this.updateTotals();
            }
        });
        
        this.fallFld.setOnKeyReleased(e -> {
            if (this.fallFld.isEditable() && this.fallFld.getText().length() > 0){    
                try {
                    this.fall = Double.parseDouble(this.fallFld.getText());
                    this.updateTotals();
                }catch(NumberFormatException ex){
                    this.errorAlert.setTitle("Fall Field Error");
                    this.errorAlert.setHeaderText("Input error in text field for 'Fall' service");
                    this.errorAlert.setContentText("Hint: There should be no symbols or characters for price\n"
                            + "Only input numbers and decimal point if needed.");
                    this.errorAlert.show();
                }    
            }
            else if (this.fallFld.isEditable() && this.fallFld.getText().length() == 0){
                this.fall = 0;
                this.updateTotals();
            }
        });
        
        this.weedTreatmentFld.setOnKeyReleased(e -> {
            if (this.weedTreatmentFld.isEditable() && this.weedTreatmentFld.getText().length() > 0){    
                try {
                    this.weedTreatment = Double.parseDouble(this.weedTreatmentFld.getText());
                    this.updateTotals();
                }catch(NumberFormatException ex){
                    this.errorAlert.setTitle("Weed Field Error");
                    this.errorAlert.setHeaderText("Input error in text field for 'Weed' service");
                    this.errorAlert.setContentText("Hint: There should be no symbols or characters for price\n"
                            + "Only input numbers and decimal point if needed.");
                    this.errorAlert.show();
                }    
            }
            else if (this.weedTreatmentFld.isEditable() && this.weedTreatmentFld.getText().length() == 0){
                this.weedTreatment = 0;
                this.updateTotals();
            }
        });
        
        this.aerationSpringFld.setOnKeyReleased(e -> {
            if (this.aerationSpringFld.isEditable() && this.aerationSpringFld.getText().length() > 0){    
                try {
                    this.aerationSpring = Double.parseDouble(this.aerationSpringFld.getText());
                    this.updateTotals();
                }catch(NumberFormatException ex){
                    this.errorAlert.setTitle("Areation Field Error");
                    this.errorAlert.setHeaderText("Input error in text field for 'Areation' service");
                    this.errorAlert.setContentText("Hint: There should be no symbols or characters for price\n"
                            + "Only input numbers and decimal point if needed.");
                    this.errorAlert.show();
                }    
            }
            else if (this.aerationSpringFld.isEditable() && this.aerationSpringFld.getText().length() == 0){
                this.aerationSpring = 0;
                this.updateTotals();
            }
        });
        
        this.aerationFallFld.setOnKeyReleased(e -> {
            if (this.aerationFallFld.isEditable() && this.aerationFallFld.getText().length() > 0){    
                try {
                    this.aerationFall = Double.parseDouble(this.aerationFallFld.getText());
                    this.updateTotals();
                }catch(NumberFormatException ex){
                    this.errorAlert.setTitle("Areation Field Error");
                    this.errorAlert.setHeaderText("Input error in text field for 'Areation' service");
                    this.errorAlert.setContentText("Hint: There should be no symbols or characters for price\n"
                            + "Only input numbers and decimal point if needed.");
                    this.errorAlert.show();
                }    
            }
            else if (this.aerationFallFld.isEditable() && this.aerationFallFld.getText().length() == 0){
                this.aerationFall = 0;
                this.updateTotals();
            }
        });
        
        this.spiderFld.setOnKeyReleased(e -> {
            if (this.spiderFld.isEditable() && this.spiderFld.getText().length() > 0){    
                try {
                    this.spider = Double.parseDouble(this.spiderFld.getText());
                    this.updateTotals();
                }catch(NumberFormatException ex){
                    this.errorAlert.setTitle("Spider Field Error");
                    this.errorAlert.setHeaderText("Input error in text field for 'Spider' service");
                    this.errorAlert.setContentText("Hint: There should be no symbols or characters for price\n"
                            + "Only input numbers and decimal point if needed.");
                    this.errorAlert.show();
                }    
            }
            else if (this.spiderFld.isEditable() && this.spiderFld.getText().length() == 0){
                this.spider = 0;
                this.updateTotals();
            }
        });
        
        this.weedingFld.setOnKeyReleased(e -> {
            if (this.weedingFld.isEditable() && this.weedingFld.getText().length() > 0){    
                try {
                    this.weeding = Double.parseDouble(this.weedingFld.getText());
                    this.updateTotals();
                }catch(NumberFormatException ex){
                    this.errorAlert.setTitle("Flowerbed Field Error");
                    this.errorAlert.setHeaderText("Input error in text field for 'Flowerbed' service");
                    this.errorAlert.setContentText("Hint: There should be no symbols or characters for price\n"
                            + "Only input numbers and decimal point if needed.");
                    this.errorAlert.show();
                }    
            }
            else if (this.weedingFld.isEditable() && this.weedingFld.getText().length() == 0){
                this.weeding = 0;
                this.updateTotals();
            }
        });
        
        this.hedgesFld.setOnKeyReleased(e -> {
            if (this.hedgesFld.isEditable() && this.hedgesFld.getText().length() > 0){    
                try {
                    this.hedges = Double.parseDouble(this.hedgesFld.getText());
                    this.updateTotals();
                }catch(NumberFormatException ex){
                    this.errorAlert.setTitle("Hedge Field Error");
                    this.errorAlert.setHeaderText("Input error in text field for 'Hedge' service");
                    this.errorAlert.setContentText("Hint: There should be no symbols or characters for price\n"
                            + "Only input numbers and decimal point if needed.");
                    this.errorAlert.show();
                }    
            }
            else if (this.hedgesFld.isEditable() && this.hedgesFld.getText().length() == 0){
                this.hedges = 0;
                this.updateTotals();
            }
        });
        
        this.fertilizerFld.setOnKeyReleased(e -> {
            if (this.fertilizerFld.isEditable() && this.fertilizerFld.getText().length() > 0){    
                try {
                    this.fertilizer = Double.parseDouble(this.fertilizerFld.getText());
                    this.updateTotals();
                }catch(NumberFormatException ex){
                    this.errorAlert.setTitle("Fertilizer Field Error");
                    this.errorAlert.setHeaderText("Input error in text field for 'Fertilizer' service");
                    this.errorAlert.setContentText("Hint: There should be no symbols or characters for price\n"
                            + "Only input numbers and decimal point if needed.");
                    this.errorAlert.show();
                }    
            }
            else if (this.fertilizerFld.isEditable() && this.fertilizerFld.getText().length() == 0){
                this.fertilizer = 0;
                this.updateTotals();
            }
        });
        
        this.wormsFld.setOnKeyReleased(e -> {
            if (this.wormsFld.isEditable() && this.wormsFld.getText().length() > 0){    
                try {
                    this.worms = Double.parseDouble(this.wormsFld.getText());
                    this.updateTotals();
                }catch(NumberFormatException ex){
                    this.errorAlert.setTitle("Worms Field Error");
                    this.errorAlert.setHeaderText("Input error in text field for 'Worms' service");
                    this.errorAlert.setContentText("Hint: There should be no symbols or characters for price\n"
                            + "Only input numbers and decimal point if needed.");
                    this.errorAlert.show();
                }    
            }
            else if (this.wormsFld.isEditable() && this.wormsFld.getText().length() == 0){
                this.worms = 0;
                this.updateTotals();
            }
        });
        
        this.soilFld.setOnKeyReleased(e -> {
            if (this.soilFld.isEditable() && this.soilFld.getText().length() > 0){    
                try {
                    this.soil = Double.parseDouble(this.soilFld.getText());
                    this.updateTotals();
                }catch(NumberFormatException ex){
                    this.errorAlert.setTitle("Soil Field Error");
                    this.errorAlert.setHeaderText("Input error in text field for 'Soil' service");
                    this.errorAlert.setContentText("Hint: There should be no symbols or characters for price\n"
                            + "Only input numbers and decimal point if needed.");
                    this.errorAlert.show();
                }    
            }
            else if (this.soilFld.isEditable() && this.soilFld.getText().length() == 0){
                this.soil = 0;
                this.updateTotals();
            }
        });
        
        this.seedingFld.setOnKeyReleased(e -> {
            if (this.seedingFld.isEditable() && this.seedingFld.getText().length() > 0){    
                try {
                    this.seeding = Double.parseDouble(this.seedingFld.getText());
                    this.updateTotals();
                }catch(NumberFormatException ex){
                    this.errorAlert.setTitle("Soil Field Error");
                    this.errorAlert.setHeaderText("Input error in text field for 'Soil' service");
                    this.errorAlert.setContentText("Hint: There should be no symbols or characters for price\n"
                            + "Only input numbers and decimal point if needed.");
                    this.errorAlert.show();
                }    
            }
            else if (this.seedingFld.isEditable() && this.seedingFld.getText().length() == 0){
                this.seeding = 0;
                this.updateTotals();
            }
        });
    }
    
    
    public double getTotal(){
        return this.total;
    }
    public double getLawn(){
        return this.lawn;
    }
    public double getSpring(){
        return this.spring;
    }
    public double getFall(){
        return this.fall;
    }
    public double getWeedTreatment(){
        return this.weedTreatment;
    }
    public double getAerationSpring(){
        return this.aerationSpring;
    }
    public double getAerationFall(){
        return this.aerationFall;
    }
    public double getSpider(){
        return this.spider;
    }
    public double getWeeding(){
        return this.weeding;
    }
    public double getHedges(){
        return this.hedges;
    }
    public double getFertilizer(){
        return this.fertilizer;
    }
    public double getWorms(){
        return this.worms;
    }
    public double getSoil(){
        return this.soil;
    }
    public double getSeeding(){
        return this.seeding;
    }
    public String getComment(){
        return this.comment;
    }
    
    public void changeServices(double lawn, double spring, double fall, double weedTreatment, double aerationSpring, double aerationFall, 
            double spider, double weeding, double hedges, double fertilizer, double worms, double soil, double seeding, String comment){
        
        this.lawn = lawn;
        if (this.lawn > 0){
            this.total += this.lawn;
            this.lawnBtn.setSelected(true);
            this.lawnFld.setText(this.lawn + "");
            this.lawnFld.setEditable(true);
        }
        
        this.spring = spring;
        if (this.spring > 0){
            this.total += this.spring;
            this.springBtn.setSelected(true);
            this.springFld.setText(this.spring + "");
            this.springFld.setEditable(true);
        }
        
        this.fall = fall;
        if (this.fall > 0){
            this.total += this.fall;
            this.fallBtn.setSelected(true);
            this.fallFld.setText(this.fall + "");
            this.fallFld.setEditable(true);
        }
        
        this.weedTreatment = weedTreatment;
        if (this.weedTreatment > 0){
            this.total += this.weedTreatment;
            this.weedTreatmentBtn.setSelected(true);
            this.weedTreatmentFld.setText(this.weedTreatment + "");
            this.weedTreatmentFld.setEditable(true);
        }
        
        this.aerationSpring = aerationSpring;
        if (this.aerationSpring > 0){
            this.total += this.aerationSpring;
            this.aerationSpringBtn.setSelected(true);
            this.aerationSpringFld.setText(this.aerationSpring + "");
            this.aerationSpringFld.setEditable(true);
        }
        
        this.aerationFall = aerationFall;
        if (this.aerationFall > 0){
            this.total += this.aerationFall;
            this.aerationFallBtn.setSelected(true);
            this.aerationFallFld.setText(this.aerationFall + "");
            this.aerationFallFld.setEditable(true);
        }
        
        this.spider = spider;
        if (this.spider > 0){
            this.total += this.spider;
            this.spiderBtn.setSelected(true);
            this.spiderFld.setText(this.spider + "");
            this.spiderFld.setEditable(true);
        }
        
        this.weeding = weeding;
        if (this.weeding > 0){
            this.total += this.weeding;
            this.weedingBtn.setSelected(true);
            this.weedingFld.setText(this.weeding + "");
            this.weedingFld.setEditable(true);
        }
        
        this.hedges = hedges;
        if (this.hedges > 0){
            this.total += this.hedges;
            this.hedgesBtn.setSelected(true);
            this.hedgesFld.setText(this.hedges + "");
            this.hedgesFld.setEditable(true);
        }
        
        this.fertilizer = fertilizer;
        if (this.fertilizer > 0){
            this.total += this.fertilizer;
            this.fertilizerBtn.setSelected(true);
            this.fertilizerFld.setText(this.fertilizer + "");
            this.fertilizerFld.setEditable(true);
        }
        
        this.worms = worms;
        if (this.worms > 0){
            this.total += this.worms;
            this.wormsBtn.setSelected(true);
            this.wormsFld.setText(this.worms + "");
            this.wormsFld.setEditable(true);
        }
        
        this.soil = soil;
        if (this.soil > 0){
            this.total += this.soil;
            this.soilBtn.setSelected(true);
            this.soilFld.setText(this.soil + "");
            this.soilFld.setEditable(true);
        }
        
        this.seeding = seeding;
        if (this.seeding > 0){
            this.total += this.seeding;
            this.seedingBtn.setSelected(true);
            this.seedingFld.setText(this.seeding + "");
            this.seedingFld.setEditable(true);
        }
        
        this.updateTotals();
        
    }
    
}
