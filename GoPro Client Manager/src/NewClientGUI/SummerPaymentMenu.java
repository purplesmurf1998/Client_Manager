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
    
    private final ToggleButton[] btnList = {marBtn, aprBtn, mayBtn, junBtn, julBtn, augBtn, sepBtn, octBtn};
    
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
    
    private final TextField[] fldList = {marFld, aprFld, mayFld, junFld, julFld, augFld, sepFld, octFld};
    
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
    private double initialTotal = 0;
    private double finalTotal = 0;
    private int plan = 0;
    private int method = 0;
    private double save = 0;
    
    private final Alert errorAlert = new Alert(Alert.AlertType.ERROR);
    
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
        
        this.saveBtn.setMinSize(125, 40);
        this.saveBtn.setMaxSize(125, 40);
        this.saveBtn.setFont(Font.font("Rockwell", 18));
        this.saveBtn.setFocusTraversable(false);
        
        for (int i = 0; i < this.fldList.length; i++){
            this.fldList[i].setMinSize(80, 30);
            this.fldList[i].setMaxSize(80, 30);
            this.fldList[i].setFont(Font.font("Rockwell", 12));
            this.fldList[i].setEditable(false);
        }
        
        this.saveFld.setMinSize(80, 30);
        this.saveFld.setMaxSize(80, 30);
        this.saveFld.setFont(Font.font("Rockwell", 12));
        this.saveFld.setEditable(false);
        
        this.totalFld.setMinSize(80, 30);
        this.totalFld.setMaxSize(80, 30);
        this.totalFld.setFont(Font.font("Rockwell", 12));
        this.totalFld.setEditable(false);
        
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
        
        setUpButtons();
    }
    
    private void updateFields(){
        for (int i = 0; i < btnList.length; i++){
            if (btnList[i].isSelected()){
                fldList[i].setText("$" + String.format("%.2f", this.finalTotal / this.plan));
            }
        }
    }
    
    private void updateTotal(){
        if (this.saveBtn.isSelected()){    
            double savings = (this.save / 100.0) * this.initialTotal;
            this.finalTotal = this.initialTotal - savings;
            this.totalFld.setText("$" + String.format("%.2f", this.finalTotal));
        }
        else {
            this.finalTotal = this.initialTotal;
            this.totalFld.setText("$" + String.format("%.2f", this.finalTotal));
        }
        updateFields();
    }
    
    private void setUpButtons(){
        this.marBtn.setOnAction(e -> {
            if (this.marBtn.isSelected()){
                this.marBox.setSelected(true);
                this.plan++;
                this.planText.setText("Payments: " + this.plan);
                this.mar = 2;
                updateFields();
            }
            else {
                this.marBox.setSelected(false);
                this.plan--;
                this.planText.setText("Payments: " + this.plan);
                this.mar = 0;
                this.marFld.setText("");
                updateFields();
            }
        });
        
        this.aprBtn.setOnAction(e -> {
            if (this.aprBtn.isSelected()){
                this.aprBox.setSelected(true);
                this.plan++;
                this.planText.setText("Payments: " + this.plan);
                this.apr = 2;
                updateFields();
            }
            else {
                this.aprBox.setSelected(false);
                this.plan--;
                this.planText.setText("Payments: " + this.plan);
                this.apr = 0;
                this.aprFld.setText("");
                updateFields();
            }
        });
        
        this.mayBtn.setOnAction(e -> {
            if (this.mayBtn.isSelected()){
                this.mayBox.setSelected(true);
                this.plan++;
                this.planText.setText("Payments: " + this.plan);
                this.may = 2;
                updateFields();
            }
            else {
                this.mayBox.setSelected(false);
                this.plan--;
                this.planText.setText("Payments: " + this.plan);
                this.may = 0;
                this.mayFld.setText("");
                updateFields();
            }
        });
        
        this.junBtn.setOnAction(e -> {
            if (this.junBtn.isSelected()){
                this.junBox.setSelected(true);
                this.plan++;
                this.planText.setText("Payments: " + this.plan);
                this.jun = 2;
                updateFields();
            }
            else {
                this.junBox.setSelected(false);
                this.plan--;
                this.planText.setText("Payments: " + this.plan);
                this.jun = 0;
                this.junFld.setText("");
                updateFields();
            }
        });
        
        this.julBtn.setOnAction(e -> {
            if (this.julBtn.isSelected()){
                this.julBox.setSelected(true);
                this.plan++;
                this.planText.setText("Payments: " + this.plan);
                this.jul = 2;
                updateFields();
            }
            else {
                this.julBox.setSelected(false);
                this.plan--;
                this.planText.setText("Payments: " + this.plan);
                this.jul = 0;
                this.julFld.setText("");
                updateFields();
            }
        });
        
        this.augBtn.setOnAction(e -> {
            if (this.augBtn.isSelected()){
                this.augBox.setSelected(true);
                this.plan++;
                this.planText.setText("Payments: " + this.plan);
                this.aug = 2;
                updateFields();
            }
            else {
                this.augBox.setSelected(false);
                this.plan--;
                this.planText.setText("Payments: " + this.plan);
                this.aug = 0;
                this.augFld.setText("");
                updateFields();
            }
        });
        
        this.sepBtn.setOnAction(e -> {
            if (this.sepBtn.isSelected()){
                this.sepBox.setSelected(true);
                this.plan++;
                this.planText.setText("Payments: " + this.plan);
                this.sep = 2;
                updateFields();
            }
            else {
                this.sepBox.setSelected(false);
                this.plan--;
                this.planText.setText("Payments: " + this.plan);
                this.sep = 0;
                this.sepFld.setText("");
                updateFields();
            }
        });
        
        this.octBtn.setOnAction(e -> {
            if (this.octBtn.isSelected()){
                this.octBox.setSelected(true);
                this.plan++;
                this.planText.setText("Payments: " + this.plan);
                this.oct = 2;
                updateFields();
            }
            else {
                this.octBox.setSelected(false);
                this.plan--;
                this.planText.setText("Payments: " + this.plan);
                this.oct = 0;
                this.octFld.setText("");
                updateFields();
            }
        });
        
        this.saveBtn.setOnAction(e -> {
            if (this.saveBtn.isSelected()){
                this.saveFld.setEditable(true);
                this.save = 5;
                this.saveFld.setText("5");
                updateTotal();
            }
            else {
                this.saveFld.setEditable(false);
                this.save = 0;
                this.saveFld.setText("");
                updateTotal();
            }
        });
        
        this.saveFld.setOnKeyReleased(e -> {
            if (this.saveFld.isEditable() && this.saveFld.getText().length() > 0){    
                try {
                    this.save = Double.parseDouble(this.saveFld.getText());
                    updateTotal();
                }catch(NumberFormatException ex){
                    this.errorAlert.setTitle("Save Field Error");
                    this.errorAlert.setHeaderText("Input error in text field for 'Save'");
                    this.errorAlert.setContentText("Hint: There should be no symbols or characters for savings\n"
                            + "Only input numbers and decimal point if needed.");
                    this.errorAlert.show();
                }    
            }
            else if (this.saveFld.isEditable() && this.saveFld.getText().length() == 0){
                this.save = 0;
                updateTotal();
            }
        });
        
        this.allMonthsBtn.setOnAction(e -> {
            for (int i = 0; i < btnList.length; i++)
                this.btnList[i].setSelected(true);
            
            for (int i = 0; i < boxList.length; i++)
                this.boxList[i].setSelected(true);
            
            this.mar = 2;
            this.apr = 2;
            this.may = 2;
            this.jun = 2;
            this.jul = 2;
            this.aug = 2;
            this.sep = 2;
            this.oct = 2;
            
            this.plan = 8;
            this.planText.setText("Payment: " + 8);
            
            updateFields();
        });
        
        this.checkMethod.setOnAction(e -> {this.method = 0;});
        this.creditMethod.setOnAction(e -> {this.method = 1;});
        this.interactMethod.setOnAction(e -> {this.method = 2;});
        this.otherMethod.setOnAction(e -> {this.method = 3;});
        
        this.commentBtn.setOnAction(e -> {
            this.commentStage.show();
        });
        
        this.marBox.setOnAction(e -> {
            if (this.marBox.isSelected())
                this.mar = 2;
            else
                this.mar = 1;
        });
        
        this.aprBox.setOnAction(e -> {
            if (this.aprBox.isSelected())
                this.apr = 2;
            else
                this.apr = 1;
        });
        
        this.mayBox.setOnAction(e -> {
            if (this.mayBox.isSelected())
                this.may = 2;
            else
                this.may = 1;
        });
        
        this.junBox.setOnAction(e -> {
            if (this.junBox.isSelected())
                this.jun = 2;
            else
                this.jun = 1;
        });
        
        this.julBox.setOnAction(e -> {
            if (this.julBox.isSelected())
                this.jul = 2;
            else
                this.jul = 1;
        });
        
        this.augBox.setOnAction(e -> {
            if (this.augBox.isSelected())
                this.aug = 2;
            else
                this.aug = 1;
        });
        
        this.sepBox.setOnAction(e -> {
            if (this.sepBox.isSelected())
                this.sep = 2;
            else
                this.sep = 1;
        });
        
        this.octBox.setOnAction(e -> {
            if (this.octBox.isSelected())
                this.oct = 2;
            else
                this.oct = 1;
        });
    }
    
    public void setTotal(double total){
        this.initialTotal = total;
        this.finalTotal = total;
        
        this.totalFld.setText("$" + String.format("%.2f", this.initialTotal));
    }
    
    public void refreshPane(){
        for (int i = 0; i < btnList.length; i++)
            this.btnList[i].setSelected(false);
        
        for (int i = 0; i < boxList.length; i++)
            this.boxList[i].setSelected(false);
        
        for(int i = 0; i < fldList.length; i++)
            this.fldList[i].setText("");
        
        this.mar = 0;
        this.apr = 0;
        this.may = 0;
        this.jun = 0;
        this.jul = 0;
        this.aug = 0;
        this.sep = 0;
        this.oct = 0;
        
        this.plan = 0;
        
        this.planText.setText("");
    }
    
    
}
