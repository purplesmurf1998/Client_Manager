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
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 *
 * @author purplesmurf
 */
public class SummerPaymentMenu extends GridPane{
    
    private Connection conn;
    
    //months: mar, apr, may, jun, jul, aug, sep, oct
    
    private final ToggleButton marBtn = new ToggleButton("March");
    private final ToggleButton aprBtn = new ToggleButton("April");
    private final ToggleButton mayBtn = new ToggleButton("May");
    private final ToggleButton junBtn = new ToggleButton("June");
    private final ToggleButton julBtn = new ToggleButton("July");
    private final ToggleButton augBtn = new ToggleButton("August");
    private final ToggleButton sepBtn = new ToggleButton("September");
    private final ToggleButton octBtn = new ToggleButton("October");
    
    private final ToggleButton saveBtn = new ToggleButton("Save %");
    
    private final ToggleButton[] btnList = {marBtn, aprBtn, mayBtn, junBtn, julBtn, augBtn, sepBtn, octBtn, saveBtn};
    
    private final TextField marFld = new TextField();
    private final TextField aprFld = new TextField();
    private final TextField mayFld = new TextField();
    private final TextField junFld = new TextField();
    private final TextField julFld = new TextField();
    private final TextField augFld = new TextField();
    private final TextField sepFld = new TextField();
    private final TextField octFld = new TextField();
    
    private final TextField saveFld = new TextField();
    
    private final TextField totalFld = new TextField();
    
    private final TextField[] fldList = {marFld, aprFld, mayFld, junFld, julFld, augFld, sepFld, octFld, saveFld, totalFld};
    
    private final CheckBox marBox = new CheckBox();
    private final CheckBox aprBox = new CheckBox();
    private final CheckBox mayBox = new CheckBox();
    private final CheckBox junBox = new CheckBox();
    private final CheckBox julBox = new CheckBox();
    private final CheckBox augBox = new CheckBox();
    private final CheckBox sepBox = new CheckBox();
    private final CheckBox octBox = new CheckBox();
    
    private final CheckBox[] boxList = {marBox, aprBox, mayBox, junBox, julBox, augBox, sepBox, octBox};
    
    private final ToggleButton creditMethod = new ToggleButton("Credit");
    private final ToggleButton checkMethod = new ToggleButton("Check");
    private final ToggleButton interactMethod = new ToggleButton("Interact");
    private final ToggleButton otherMethod = new ToggleButton("Misc.");
    
    private final ToggleButton[] methodList = {creditMethod, checkMethod, interactMethod, otherMethod};
    private final ToggleGroup methodGroup = new ToggleGroup();
    
    private final Text totalText = new Text("Total: ");
    private final Text planText = new Text("Payments: ");
    
    private final Button allMonthsBtn = new Button("Select All");
    
    private final Button commentBtn = new Button("Comments");
    private final Stage commentStage = new Stage();
    private Scene commentScene;
    private final GridPane commentPane = new GridPane();
    private final TextArea commentSection = new TextArea();
    private final Button saveCommentBtn = new Button("Save Comments");
    
    /*
    0 = month not selected
    1 = payment not recieved
    2 = payment recieved
    3 = payment deposited
    */
    
    private int mar = 0;
    private int apr = 0;
    private int may = 0;
    private int jun = 0;
    private int jul = 0;
    private int aug = 0;
    private int sep = 0;
    private int oct = 0;
    private String comment = "";
    private double total;
    private int plan = 0;
    private int method = 0;
    
    public SummerPaymentMenu(Connection conn){
        this.conn = conn;
        
        setUpPane();
        setUpCommentWindow();
        
    }
    
    private void setUpCommentWindow(){
        this.commentScene = new Scene(this.commentPane, 300, 200);
        
        this.commentPane.setPadding(new Insets(5, 5, 5, 5));
        this.commentPane.setVgap(5);
        this.commentPane.setHgap(5);
        this.commentPane.setAlignment(Pos.CENTER);
        
        GridPane.setHalignment(this.saveCommentBtn, HPos.RIGHT);
        
        this.commentPane.add(this.commentSection, 0, 0);
        this.commentPane.add(this.saveCommentBtn, 0, 1);
        
        this.saveCommentBtn.setOnAction(e -> {
            this.comment = this.commentSection.getText();
            this.commentStage.close();
        });
        
        this.commentStage.setScene(this.commentScene);
        this.commentStage.setTitle("Comments Section");
        
    }
    
    private void setUpPane(){
        this.setAlignment(Pos.CENTER);
        this.setVgap(5);
        this.setHgap(30);
        
        this.add(this.marBox, 0, 0);
        this.add(this.aprBox, 0, 1);
        this.add(this.mayBox, 0, 2);
        this.add(this.junBox, 0, 3);
        this.add(this.julBox, 0, 4);
        this.add(this.augBox, 0, 5);
        this.add(this.sepBox, 0, 6);
        this.add(this.octBox, 0, 7);
        
        this.add(this.marBtn, 1, 0);
        this.add(this.aprBtn, 1, 1);
        this.add(this.mayBtn, 1, 2);
        this.add(this.junBtn, 1, 3);
        this.add(this.julBtn, 1, 4);
        this.add(this.augBtn, 1, 5);
        this.add(this.sepBtn, 1, 6);
        this.add(this.octBtn, 1, 7);
        
        this.add(this.marFld, 2, 0);
        this.add(this.aprFld, 2, 1);
        this.add(this.mayFld, 2, 2);
        this.add(this.junFld, 2, 3);
        this.add(this.julFld, 2, 4);
        this.add(this.augFld, 2, 5);
        this.add(this.sepFld, 2, 6);
        this.add(this.octFld, 2, 7);
        
        this.add(this.saveBtn, 3, 0);
        this.add(this.checkMethod, 3, 2);
        this.add(this.creditMethod, 3, 3);
        this.add(this.interactMethod, 3, 4);
        this.add(this.otherMethod, 3, 5);
        this.add(this.totalText, 3, 7);
        
        this.add(this.saveFld, 4, 0);
        this.add(this.planText, 4, 2);
        this.add(this.allMonthsBtn, 4, 4);
        this.add(this.commentBtn, 4, 5);
        this.add(this.totalFld, 4, 7);
        
        for (int i = 0; i < this.boxList.length; i++){
            GridPane.setHalignment(this.boxList[i], HPos.RIGHT);
            this.boxList[i].setSelected(false);
        }
        
        for (int i = 0; i < this.btnList.length; i++){
            this.btnList[i].setMinSize(125, 40);
            this.btnList[i].setMaxSize(125, 40);
            this.btnList[i].setFont(Font.font("Rockwell", 18));
            this.btnList[i].setFocusTraversable(false);
        }
        
        for (int i = 0; i < this.fldList.length; i++){
            this.fldList[i].setMinSize(80, 30);
            this.fldList[i].setMaxSize(80, 30);
            this.fldList[i].setFont(Font.font("Rockwell", 12));
            this.fldList[i].setEditable(false);
        }
        
        for (int i = 0; i < this.methodList.length; i++){
            this.methodList[i].setMinSize(125, 40);
            this.methodList[i].setMaxSize(125, 40);
            this.methodList[i].setFont(Font.font("Rockwell", 18));
            this.methodList[i].setFocusTraversable(false);
        }
        
        this.totalText.setFont(Font.font("Rockwell", 18));
        this.planText.setFont(Font.font("Rockwell", 18));
        
        this.commentBtn.setMinSize(125, 40);
        this.commentBtn.setMaxSize(125, 40);
        this.commentBtn.setFont(Font.font("Rockwell", 18));
        this.commentBtn.setFocusTraversable(false);
        
        
        this.allMonthsBtn.setMinSize(125, 40);
        this.allMonthsBtn.setMaxSize(125, 4);
        this.allMonthsBtn.setFont(Font.font("Rockwell", 18));
        this.allMonthsBtn.setFocusTraversable(false);
        
        this.checkMethod.setToggleGroup(methodGroup);
        this.creditMethod.setToggleGroup(methodGroup);
        this.interactMethod.setToggleGroup(methodGroup);
        this.otherMethod.setToggleGroup(methodGroup);
        
        this.checkMethod.setSelected(true);
        
        this.planText.setText("Payments: " + this.plan);
        
        GridPane.setHalignment(this.totalText, HPos.RIGHT);
    }
    
    
}
