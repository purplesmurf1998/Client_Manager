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
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 *
 * @author purplesmurf
 */
public class WinterServiceMenu extends GridPane{
    
    private Connection conn;
    
    private final ToggleButton singleBtn = new ToggleButton("Single");
    private final ToggleButton doubleBtn = new ToggleButton("Double");
    private final ToggleButton tripleBtn = new ToggleButton("Triple");
    private final ToggleButton otherBtn = new ToggleButton("Other");
    private final ToggleButton shovelBtn = new ToggleButton("Shovel");
    private final ToggleButton saltBtn = new ToggleButton("Salt");
    
    private final ToggleButton[] btnList = {singleBtn, doubleBtn, tripleBtn, otherBtn, shovelBtn, saltBtn};
    
    private final ToggleGroup serviceGroup = new ToggleGroup();
    
    private final TextField singleFld = new TextField();
    private final TextField doubleFld = new TextField();
    private final TextField tripleFld = new TextField();
    private final TextField otherFld = new TextField();
    private final TextField shovelFld = new TextField();
    private final TextField saltFld = new TextField();
    
    private final TextField[] fldList = {singleFld, doubleFld, tripleFld, otherFld, shovelFld, saltFld};
    
    private final HBox totalPane = new HBox();
    private final Text totalText = new Text("TOTAL: ");
    private final CheckBox taxBox = new CheckBox();
    
    private final Button commentBtn = new Button("Comments");
    
    private final TextArea commentSct = new TextArea();
    private final Stage commentWindow = new Stage();
    private final GridPane commentPane = new GridPane();
    private Scene commentScene;
    private final Button saveCommentBtn = new Button("Save Comments");
    
    private final Alert errorAlert = new Alert(AlertType.ERROR);
    
    private double total = 0;
    private final double taxCst = 1.14972;
    private double serviceCost = 215;
    private double shovelCost = 0;
    private double saltCost = 0;
    private String comment = "";
    
    
    public WinterServiceMenu(){
        
    }
    
    public WinterServiceMenu(Connection conn){
        this.conn = conn;
        
        setUpPane();
        setUpComments();
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
    
    private void setUpPane(){
        this.setPadding(new Insets(10, 10, 10, 10));
        this.setAlignment(Pos.CENTER);
        this.setVgap(30);
        this.setHgap(60);
        
        this.add(this.singleBtn, 0, 0);
        this.add(this.doubleBtn, 1, 0);
        this.add(this.tripleBtn, 2, 0);
        this.add(this.otherBtn, 3, 0);
       
        this.add(this.singleFld, 0, 1);
        this.add(this.doubleFld, 1, 1);
        this.add(this.tripleFld, 2, 1);
        this.add(this.otherFld, 3, 1);
       
        this.add(this.shovelBtn, 0, 2);
        this.add(this.shovelFld, 1, 2);
        this.add(this.saltBtn, 2, 2);
        this.add(this.saltFld, 3, 2);
       
        this.add(this.totalPane, 3, 3);
        
        setUpTotalPane();
        
        this.add(this.commentBtn, 3, 4);
       
        for (int i = 0; i < btnList.length; i++){
            btnList[i].setMinSize(125, 50);
            btnList[i].setMaxSize(125, 50);
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
       
        this.singleBtn.setToggleGroup(serviceGroup);
        this.doubleBtn.setToggleGroup(serviceGroup);
        this.tripleBtn.setToggleGroup(serviceGroup);
        this.otherBtn.setToggleGroup(serviceGroup);
       
        this.singleBtn.setSelected(true);
        this.total = 215;
        this.singleFld.setText("$215");
        this.taxBox.setSelected(true);
        this.updateTotals();
       
        this.singleBtn.setOnAction(e -> {
            if (!this.singleBtn.isSelected())
                this.singleBtn.setSelected(true);
            
            this.singleFld.setText("$215");
            this.doubleFld.setText("");
            this.tripleFld.setText("");
            this.otherFld.setText("");
            this.otherFld.setEditable(false);
            this.serviceCost = 215;
            this.updateTotals();
           
        });
       
        this.doubleBtn.setOnAction(e -> {
            if (!this.doubleBtn.isSelected())
                this.doubleBtn.setSelected(true);
            
            this.doubleFld.setText("$235");
            this.singleFld.setText("");
            this.tripleFld.setText("");
            this.otherFld.setText("");
            this.otherFld.setEditable(false);
            this.serviceCost = 235;
            this.updateTotals();
           
        });
       
        this.tripleBtn.setOnAction(e -> {
            if (!this.tripleBtn.isSelected())
                this.tripleBtn.setSelected(true);
            
            this.tripleFld.setText("$285");
            this.singleFld.setText("");
            this.doubleFld.setText("");
            this.otherFld.setText("");
            this.otherFld.setEditable(false);
            this.serviceCost = 285;
            this.updateTotals();
           
        });
       
        this.otherBtn.setOnAction(e -> {
            if (!this.otherBtn.isSelected()){
                this.otherBtn.setSelected(true);
                
                if (this.otherFld.getText().length() <= 0){//aplies for when the button is pressed twice
                    this.serviceCost = 0;
                    this.updateTotals();
                }
                else{
                    try{
                        this.serviceCost = Double.parseDouble(this.otherFld.getText());
                        this.updateTotals();
                    }catch(NumberFormatException ex){
                    
                    }
                }
            }
            
            this.otherFld.setEditable(true);
            this.otherFld.requestFocus();
            this.singleFld.setText("");
            this.doubleFld.setText("");
            this.tripleFld.setText("");
            
            if (this.otherFld.getText().length() <= 0){
                this.serviceCost = 0;
                this.updateTotals();
            }
            else{
                try{    
                    this.serviceCost = Double.parseDouble(this.otherFld.getText());
                    this.updateTotals();
                }catch(NumberFormatException ex){
                    this.errorAlert.setTitle("Other Field Error");
                    this.errorAlert.setHeaderText("Input error in text field for 'Other' service");
                    this.errorAlert.setContentText("Hint: There should be no symbols or characters for price\n"
                            + "Only input numbers and decimal point if needed.");
                    this.errorAlert.show();
                }
            }
            
        });
        
        this.otherFld.setOnKeyReleased(e -> {
            if (this.otherFld.isEditable() && this.otherFld.getText().length() > 0){
                try{
                    this.serviceCost = Double.parseDouble(this.otherFld.getText());
                    this.updateTotals();
                }catch(NumberFormatException ex){
                    this.errorAlert.setTitle("Other Field Error");
                    this.errorAlert.setHeaderText("Input error in text field for 'Other' service");
                    this.errorAlert.setContentText("Hint: There should be no symbols or characters for price\n"
                            + "Only input numbers and decimal point if needed.");
                    this.errorAlert.show();
                }
            }
            else if (this.otherFld.isEditable() && this.otherFld.getText().length() == 0){
                this.serviceCost = 0;
                this.updateTotals();
            }
        });
       
        this.shovelBtn.setOnAction(e -> {
            if (this.shovelBtn.isSelected()){
                this.shovelFld.setEditable(true);
                this.shovelFld.requestFocus();
            }
            else{
                this.shovelFld.setText("");
                this.shovelFld.setEditable(false);
                this.shovelCost = 0;
                this.updateTotals();
            }
            
        });
        
        this.shovelFld.setOnKeyReleased(e -> {
            if (this.shovelFld.isEditable() && this.shovelFld.getText().length() > 0){    
                try {
                    this.shovelCost = Double.parseDouble(this.shovelFld.getText());
                    this.updateTotals();
                }catch(NumberFormatException ex){
                    this.errorAlert.setTitle("Shovel Field Error");
                    this.errorAlert.setHeaderText("Input error in text field for 'Shovel' service");
                    this.errorAlert.setContentText("Hint: There should be no symbols or characters for price\n"
                            + "Only input numbers and decimal point if needed.");
                    this.errorAlert.show();
                }    
            }
            else if (this.shovelFld.isEditable() && this.shovelFld.getText().length() == 0){
                this.shovelCost = 0;
                this.updateTotals();
            }
        });
        
        this.taxBox.setOnAction(e -> {this.updateTotals();});
        
        this.commentBtn.setOnAction(e -> {
            this.commentWindow.show();
        });
       
        this.saltFld.setOnAction(e -> {
            if (this.saltFld.isEditable() && this.saltFld.getText().length() > 0){
                try{
                    this.saltCost = Double.parseDouble(this.saltFld.getText());
                    this.updateTotals();
                }catch(NumberFormatException ex){
                    this.errorAlert.setTitle("SaltField Error");
                    this.errorAlert.setHeaderText("Input error in text field for 'Salt' service");
                    this.errorAlert.setContentText("Hint: There should be no symbols or characters for price\n"
                            + "Only input numbers and decimal point if needed.");
                    this.errorAlert.show();
                }
            }
            else if (this.saltFld.isEditable() && this.saltFld.getText().length() == 0){
                this.saltCost = 0;
                this.updateTotals();
            }
        });
        
        this.saltBtn.setOnAction(e -> {
            if (this.saltBtn.isSelected()){
                this.saltFld.setEditable(true);
                this.saltFld.requestFocus();
            }
            else{
                this.saltFld.setText("");
                this.saltFld.setEditable(false);
                this.saltCost = 0;
                this.updateTotals();
            }
            
        });
        
    }
    
    private void updateTotals(){
        this.total = this.serviceCost + this.shovelCost + this.saltCost;
        if (this.taxBox.isSelected())
            this.total = this.total * this.taxCst;
        
        this.totalText.setText("TOTAL: $" + String.format("%.2f", this.total));
    }
    
    private void setUpTotalPane(){
        this.totalPane.setSpacing(5);
        this.totalPane.getChildren().addAll(this.taxBox, this.totalText);
    }
    
    public double getTotal(){
        return this.total;
    }
    public double getSingle(){
        if (this.singleFld.getText().length() > 0)    
            return Double.parseDouble(this.singleFld.getText().substring(1));
        else 
            return 0;
    }
    public double getDouble(){
        if (this.doubleFld.getText().length() > 0)
            return Double.parseDouble(this.doubleFld.getText().substring(1));
        else
            return 0;
    }
    public double getTriple(){
        if (this.tripleFld.getText().length() > 0)    
            return Double.parseDouble(this.tripleFld.getText().substring(1));
        else
            return 0;
    }
    public double getOther(){
        if (this.otherFld.getText().length() > 0)
            return Double.parseDouble(this.otherFld.getText());
        else
            return 0;
    }
    public double getShovel(){
        return this.shovelCost;
    }
    public double getSalt(){
        return this.saltCost;
    }
    public String getComment(){
        return this.comment;
    }
    
}
