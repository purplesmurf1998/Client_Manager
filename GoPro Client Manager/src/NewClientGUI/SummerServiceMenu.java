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
    private final ToggleButton lawnBtn = new ToggleButton("Lawn ($)");
    private final ToggleButton springBtn = new ToggleButton("Spring ($)");
    private final ToggleButton fallBtn = new ToggleButton("Fall ($)");
    private final ToggleButton weedBtn = new ToggleButton("Weeds ($)");
    private final ToggleButton areationBtn = new ToggleButton("Aeration ($)");
    private final ToggleButton spiderBtn = new ToggleButton("Spider ($)");
    private final ToggleButton flowerbedBtn = new ToggleButton("Flowerbed (Hr)");
    private final ToggleButton hedgeBtn = new ToggleButton("Hedge (Hr)");
    private final ToggleButton fertilizerBtn = new ToggleButton("Fertilizer ($)");
    private final ToggleButton wormsBtn = new ToggleButton("Worms ($)");
    private final ToggleButton soilBtn = new ToggleButton("Soil (Yd)");
    
    private final ToggleButton[] btnList = {lawnBtn, springBtn, fallBtn, weedBtn, areationBtn, spiderBtn, flowerbedBtn, hedgeBtn, fertilizerBtn, wormsBtn, soilBtn};
    
    private final TextField lawnFld = new TextField();
    private final TextField springFld = new TextField();
    private final TextField fallFld = new TextField();
    private final TextField weedFld = new TextField();
    private final TextField areationFld = new TextField();
    private final TextField spiderFld = new TextField();
    private final TextField flowerbedFld = new TextField();
    private final TextField hedgeFld = new TextField();
    private final TextField fertilizerFld = new TextField();
    private final TextField wormsFld = new TextField();
    private final TextField soilFld = new TextField();
    
    private final TextField[] fldList = {lawnFld, springFld, fallFld, weedFld, areationFld, spiderFld, flowerbedFld, hedgeFld, fertilizerFld, wormsFld, soilFld};
    
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
    private double weed = 0;
    private double areation = 0;
    private double spider = 0;
    private double flowerbed = 0;
    private double hedge = 0;
    private double fertilizer = 0;
    private double worms = 0;
    private double soil = 0;
    
    private final Alert errorAlert = new Alert(Alert.AlertType.ERROR);
    
    public SummerServiceMenu(Connection conn){
        this.conn = conn;
        
        setUpPane();
    }
    
    private void setUpPane(){
        this.setPadding(new Insets(10, 10, 10, 10));
        this.setAlignment(Pos.CENTER);
        this.setVgap(15);
        this.setHgap(30);
        
        this.add(this.lawnBtn, 0, 0);
        this.add(this.springBtn, 0, 1);
        this.add(this.fallBtn, 0, 2);
        this.add(this.weedBtn, 0, 3);
        this.add(this.areationBtn, 0, 4);
        this.add(this.spiderBtn, 0, 5);
        this.add(this.flowerbedBtn, 2, 0);
        this.add(this.hedgeBtn, 2, 1);
        this.add(this.fertilizerBtn, 2, 2);
        this.add(this.wormsBtn, 2, 3);
        this.add(this.soilBtn, 2, 4);
        
        this.add(this.lawnFld, 1, 0);
        this.add(this.springFld, 1, 1);
        this.add(this.fallFld, 1, 2);
        this.add(this.weedFld, 1, 3);
        this.add(this.areationFld, 1, 4);
        this.add(this.spiderFld, 1, 5);
        this.add(this.flowerbedFld, 3, 0);
        this.add(this.hedgeFld, 3, 1);
        this.add(this.fertilizerFld, 3, 2);
        this.add(this.wormsFld, 3, 3);
        this.add(this.soilFld, 3, 4);
        
        this.add(this.totalPane, 3, 5);
        this.add(this.commentBtn, 3, 6);
        
        setUpTotalPane();
        
        
        for (int i = 0; i < btnList.length; i++){
            btnList[i].setMinSize(175, 50);
            btnList[i].setMaxSize(175, 50);
            btnList[i].setFont(Font.font("Rockwell", 18));
            btnList[i].setFocusTraversable(false);
        }
       
        for (int i = 0; i < fldList.length; i++){
            fldList[i].setMinSize(80, 30);
            fldList[i].setMaxSize(80, 30);
            fldList[i].setFont(Font.font("Rockwell", 12));
            fldList[i].setEditable(false);
            fldList[i].setFocusTraversable(false);
           
        }
        
        this.totalText.setFont(Font.font("Rockwell", 23));
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
        this.total = this.lawn + this.spring + this.fall + this.weed + this.areation + this.spider + this.flowerbed + this.hedge + this.fertilizer + this.worms + this.soil;
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
        
        this.weedBtn.setOnAction(e -> {
            if (this.weedBtn.isSelected()){
                this.weedFld.setEditable(true);
                this.weedFld.requestFocus();
                this.weedFld.setText("65");
                this.weedFld.selectAll();
                this.weed = 65;
                this.updateTotals();
            }
            else{
                this.weedFld.setText("");
                this.weedFld.setEditable(false);
                this.weed = 0;
                this.updateTotals();
            }
            
        });
        
        this.areationBtn.setOnAction(e -> {
            if (this.areationBtn.isSelected()){
                this.areationFld.setEditable(true);
                this.areationFld.requestFocus();
                this.areationFld.setText("65");
                this.areationFld.selectAll();
                this.areation = 65;
                this.updateTotals();
            }
            else{
                this.areationFld.setText("");
                this.areationFld.setEditable(false);
                this.areation = 0;
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
        
        this.flowerbedBtn.setOnAction(e -> {
            if (this.flowerbedBtn.isSelected()){
                this.flowerbedFld.setEditable(true);
                this.flowerbedFld.requestFocus();
                this.flowerbedFld.setText("0");
                this.flowerbedFld.selectAll();
                this.updateTotals();
            }
            else{
                this.flowerbedFld.setText("");
                this.flowerbedFld.setEditable(false);
                this.flowerbed = 0;
                this.updateTotals();
            }
            
        });
        
        this.hedgeBtn.setOnAction(e -> {
            if (this.hedgeBtn.isSelected()){
                this.hedgeFld.setEditable(true);
                this.hedgeFld.requestFocus();
                this.hedgeFld.setText("0");
                this.hedgeFld.selectAll();
                this.updateTotals();
            }
            else{
                this.hedgeFld.setText("");
                this.hedgeFld.setEditable(false);
                this.hedge = 0;
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
        
        this.weedFld.setOnKeyReleased(e -> {
            if (this.weedFld.isEditable() && this.weedFld.getText().length() > 0){    
                try {
                    this.weed = Double.parseDouble(this.weedFld.getText());
                    this.updateTotals();
                }catch(NumberFormatException ex){
                    this.errorAlert.setTitle("Weed Field Error");
                    this.errorAlert.setHeaderText("Input error in text field for 'Weed' service");
                    this.errorAlert.setContentText("Hint: There should be no symbols or characters for price\n"
                            + "Only input numbers and decimal point if needed.");
                    this.errorAlert.show();
                }    
            }
            else if (this.weedFld.isEditable() && this.weedFld.getText().length() == 0){
                this.weed = 0;
                this.updateTotals();
            }
        });
        
        this.areationFld.setOnKeyReleased(e -> {
            if (this.areationFld.isEditable() && this.areationFld.getText().length() > 0){    
                try {
                    this.areation = Double.parseDouble(this.areationFld.getText());
                    this.updateTotals();
                }catch(NumberFormatException ex){
                    this.errorAlert.setTitle("Areation Field Error");
                    this.errorAlert.setHeaderText("Input error in text field for 'Areation' service");
                    this.errorAlert.setContentText("Hint: There should be no symbols or characters for price\n"
                            + "Only input numbers and decimal point if needed.");
                    this.errorAlert.show();
                }    
            }
            else if (this.areationFld.isEditable() && this.areationFld.getText().length() == 0){
                this.areation = 0;
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
        
        this.flowerbedFld.setOnKeyReleased(e -> {
            if (this.flowerbedFld.isEditable() && this.flowerbedFld.getText().length() > 0){    
                try {
                    double hours = Double.parseDouble(this.flowerbedFld.getText());
                    this.flowerbed = 45 * hours;
                    this.updateTotals();
                }catch(NumberFormatException ex){
                    this.errorAlert.setTitle("Flowerbed Field Error");
                    this.errorAlert.setHeaderText("Input error in text field for 'Flowerbed' service");
                    this.errorAlert.setContentText("Hint: There should be no symbols or characters for price\n"
                            + "Only input numbers and decimal point if needed.");
                    this.errorAlert.show();
                }    
            }
            else if (this.flowerbedFld.isEditable() && this.flowerbedFld.getText().length() == 0){
                this.flowerbed = 0;
                this.updateTotals();
            }
        });
        
        this.hedgeFld.setOnKeyReleased(e -> {
            if (this.hedgeFld.isEditable() && this.hedgeFld.getText().length() > 0){    
                try {
                    double hours = Double.parseDouble(this.hedgeFld.getText());
                    this.hedge = 40 * hours;
                    this.updateTotals();
                }catch(NumberFormatException ex){
                    this.errorAlert.setTitle("Hedge Field Error");
                    this.errorAlert.setHeaderText("Input error in text field for 'Hedge' service");
                    this.errorAlert.setContentText("Hint: There should be no symbols or characters for price\n"
                            + "Only input numbers and decimal point if needed.");
                    this.errorAlert.show();
                }    
            }
            else if (this.hedgeFld.isEditable() && this.hedgeFld.getText().length() == 0){
                this.hedge = 0;
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
                    double yards = Double.parseDouble(this.soilFld.getText());
                    this.soil = (40 * yards) + (80 * yards);
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
    public double getWeed(){
        return this.weed;
    }
    public double getAreation(){
        return this.areation;
    }
    public double getSpider(){
        return this.spider;
    }
    public double getFlowerbed(){
        return this.flowerbed;
    }
    public double getHedge(){
        return this.hedge;
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
    public String getComment(){
        return this.comment;
    }
    
}
