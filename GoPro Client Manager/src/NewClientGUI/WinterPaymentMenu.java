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
public class WinterPaymentMenu extends GridPane{
    
    private Connection conn;
    
    //months: oct, nov, dec, jan, feb
    
    private final ToggleButton octBtn = new ToggleButton("October");
    private final ToggleButton novBtn = new ToggleButton("November");
    private final ToggleButton decBtn = new ToggleButton("December");
    private final ToggleButton janBtn = new ToggleButton("January");
    private final ToggleButton febBtn = new ToggleButton("February");
    
    private final ToggleButton[] btnList = {octBtn, novBtn, decBtn, janBtn, febBtn};
    
    private final TextField octField = new TextField();
    private final TextField novField = new TextField();
    private final TextField decField = new TextField();
    private final TextField janField = new TextField();
    private final TextField febField = new TextField();
    
    private final TextField[] fieldList = {octField, novField, decField, janField, febField};
    
    private final CheckBox octBox = new CheckBox();
    private final CheckBox novBox = new CheckBox();
    private final CheckBox decBox = new CheckBox();
    private final CheckBox janBox = new CheckBox();
    private final CheckBox febBox = new CheckBox();
    
    private final CheckBox[] boxList = {octBox, novBox, decBox, janBox, febBox};
    
    private final ToggleButton creditMethod = new ToggleButton("Credit");
    private final ToggleButton checkMethod = new ToggleButton("Check");
    private final ToggleButton interactMethod = new ToggleButton("Interact");
    private final ToggleButton otherMethod = new ToggleButton("Misc.");
    
    private final ToggleButton[] methodList = {creditMethod, checkMethod, interactMethod, otherMethod};
    private final ToggleGroup methodGroup = new ToggleGroup();
    
    private final Text totalText = new Text("Total: ");
    private final Text planText = new Text("Payments: ");
    
    private final TextField totalField = new TextField();
    private final TextField planField = new TextField();
    
    private final Button allMonthsBtn = new Button("Select All");
    
    /*
    0 = month not selected
    1 = payment not recieved
    2 = payment recieved
    3 = payment deposited
    */
    
    private int oct = 0;
    private int nov = 0;
    private int dec = 0;
    private int jan = 0;
    private int feb = 0;
    private String comment = "";
    private double total;
    private int plan = 0;
    private int method = 0;
    
    private final Button commentBtn = new Button("Comments");
    private final Stage commentStage = new Stage();
    private Scene commentScene;
    private final GridPane commentPane = new GridPane();
    private final TextArea commentSection = new TextArea();
    private final Button saveCommentBtn = new Button("Save Comments");
    
    
    public WinterPaymentMenu(){
        
    }
    
    public WinterPaymentMenu(Connection conn){
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
        this.setVgap(30);
        this.setHgap(30);
        
        this.add(this.octBox, 0, 0);
        this.add(this.novBox, 0, 1);
        this.add(this.decBox, 0, 2);
        this.add(this.janBox, 0, 3);
        this.add(this.febBox, 0, 4);
        
        this.add(this.octBtn, 1, 0);
        this.add(this.novBtn, 1, 1);
        this.add(this.decBtn, 1, 2);
        this.add(this.janBtn, 1, 3);
        this.add(this.febBtn, 1, 4);
        
        this.add(this.octField, 2, 0);
        this.add(this.novField, 2, 1);
        this.add(this.decField, 2, 2);
        this.add(this.janField, 2, 3);
        this.add(this.febField, 2, 4);
        
        this.add(this.checkMethod, 3, 0);
        this.add(this.creditMethod, 3, 1);
        this.add(this.interactMethod, 3, 2);
        this.add(this.otherMethod, 3, 3);
        
        this.add(this.totalText, 4, 0);
        this.add(this.totalField, 5, 0);
        this.add(this.planText, 4, 1);
        this.add(this.planField, 5, 1);
        this.add(this.allMonthsBtn, 5, 2);
        this.add(this.commentBtn, 5, 3);
        
        for (int i = 0; i < this.boxList.length; i++){
            GridPane.setHalignment(this.boxList[i], HPos.RIGHT);
            this.boxList[i].setSelected(false);
        }
        
        for (int i = 0; i < this.btnList.length; i++){
            this.btnList[i].setMinSize(125, 50);
            this.btnList[i].setMaxSize(125, 50);
            this.btnList[i].setFont(Font.font("Rockwell", 18));
            this.btnList[i].setFocusTraversable(false);
        }
        
        for (int i = 0; i < this.fieldList.length; i++){
            this.fieldList[i].setMinSize(80, 30);
            this.fieldList[i].setMaxSize(80, 30);
            this.fieldList[i].setFont(Font.font("Rockwell", 12));
            this.fieldList[i].setEditable(false);
        }
        
        for (int i = 0; i < this.methodList.length; i++){
            this.methodList[i].setMinSize(125, 50);
            this.methodList[i].setMaxSize(125, 50);
            this.methodList[i].setFont(Font.font("Rockwell", 18));
            this.methodList[i].setFocusTraversable(false);
        }
        
        this.totalText.setFont(Font.font("Rockwell", 18));
        this.totalField.setMinSize(80, 30);
        this.totalField.setMaxSize(80, 30);
        this.totalField.setEditable(false);
        this.totalField.setFont(Font.font("Rockwell", 12));
        
        this.planText.setFont(Font.font("Rockwell", 18));
        this.planField.setMinSize(80, 30);
        this.planField.setMaxSize(80, 30);
        this.planField.setEditable(false);
        this.planField.setFont(Font.font("Rockwell", 12));
        
        this.commentBtn.setMinSize(125, 30);
        this.commentBtn.setMaxSize(125, 30);
        this.commentBtn.setFont(Font.font("Rockwell", 12));
        this.commentBtn.setFocusTraversable(false);
        
        this.allMonthsBtn.setMinSize(125, 30);
        this.allMonthsBtn.setMaxSize(125, 30);
        this.allMonthsBtn.setFont(Font.font("Rockwell", 12));
        this.allMonthsBtn.setFocusTraversable(false);
        
        this.checkMethod.setToggleGroup(methodGroup);
        this.creditMethod.setToggleGroup(methodGroup);
        this.interactMethod.setToggleGroup(methodGroup);
        this.otherMethod.setToggleGroup(methodGroup);
        
        this.checkMethod.setSelected(true);
        
        this.planField.setText("" + plan);
        
        setUpButtons();
        
    }
    private void updateFields(){
        for (int i = 0; i < btnList.length; i++){
            if (btnList[i].isSelected())
                fieldList[i].setText("$" + String.format("%.2f", this.total / this.plan));
        }
    }
    
    private void setUpButtons(){
        this.octBtn.setOnAction(e -> {
            if (this.octBtn.isSelected()){
                this.octBox.setSelected(true);
                this.plan++;
                this.planField.setText("" + this.plan);
                this.oct = 2;
                updateFields();
            }
            else {
                this.octBox.setSelected(false);
                this.plan--;
                this.planField.setText("" + this.plan);
                this.oct = 0;
                this.octField.setText("");
                updateFields();
            }
        });
        
        this.novBtn.setOnAction(e -> {
            if (this.novBtn.isSelected()){
                this.novBox.setSelected(true);
                this.plan++;
                this.planField.setText("" + this.plan);
                this.nov = 2;
                updateFields();
            }
            else {
                this.novBox.setSelected(false);
                this.plan--;
                this.planField.setText("" + this.plan);
                this.nov = 0;
                this.novField.setText("");
                updateFields();
            }
        });
        
        this.decBtn.setOnAction(e -> {
            if (this.decBtn.isSelected()){
                this.decBox.setSelected(true);
                this.plan++;
                this.planField.setText("" + this.plan);
                this.dec = 2;
                updateFields();
            }
            else {
                this.decBox.setSelected(false);
                this.plan--;
                this.planField.setText("" + this.plan);
                this.dec = 0;
                this.decField.setText("");
                updateFields();
            }
        });
        
        this.janBtn.setOnAction(e -> {
            if (this.janBtn.isSelected()){
                this.janBox.setSelected(true);
                this.plan++;
                this.planField.setText("" + this.plan);
                this.jan = 2;
                updateFields();
            }
            else {
                this.janBox.setSelected(false);
                this.plan--;
                this.planField.setText("" + this.plan);
                this.jan = 0;
                this.janField.setText("");
                updateFields();
            }
        });
        
        this.febBtn.setOnAction(e -> {
            if (this.febBtn.isSelected()){
                this.febBox.setSelected(true);
                this.plan++;
                this.planField.setText("" + this.plan);
                this.feb = 2;
                updateFields();
            }
            else {
                this.febBox.setSelected(false);
                this.plan--;
                this.planField.setText("" + this.plan);
                this.feb = 0;
                this.febField.setText("");
                updateFields();
            }
        });
        
        this.allMonthsBtn.setOnAction(e -> {
            for (int i = 0; i < btnList.length; i++)
                this.btnList[i].setSelected(true);
            
            for (int i = 0; i < boxList.length; i++)
                this.boxList[i].setSelected(true);
            
            this.oct = 2;
            this.nov = 2;
            this.dec = 2;
            this.jan = 2;
            this.feb = 2;
            
            this.plan = 5;
            this.planField.setText("" + 5);
            
            updateFields();
        });
        
        this.checkMethod.setOnAction(e -> {this.method = 0;});
        this.creditMethod.setOnAction(e -> {this.method = 1;});
        this.interactMethod.setOnAction(e -> {this.method = 2;});
        this.otherMethod.setOnAction(e -> {this.method = 3;});
        
        this.commentBtn.setOnAction(e -> {
            this.commentStage.show();
        });
        
        this.octBox.setOnAction(e -> {
            if (this.octBox.isSelected())
                this.oct = 2;
            else
                this.oct = 1;
        });
        
        this.novBox.setOnAction(e -> {
            if (this.novBox.isSelected())
                this.nov = 2;
            else 
                this.nov = 1;
        });
        
        this.decBox.setOnAction(e -> {
            if (this.decBox.isSelected())
                this.dec = 2;
            else 
                this.dec = 1;
        });
        
        this.janBox.setOnAction(e -> {
            if (this.janBox.isSelected())
                this.jan = 2;
            else 
                this.jan = 1;
        });
        
        this.febBox.setOnAction(e -> {
            if (this.febBox.isSelected())
                this.feb = 2;
            else 
                this.feb = 1;
        });
    }
    
    public void setTotal(double total){
        this.total = total;
        
        this.totalField.setText("$" + String.format("%.2f", this.total));
    }
    
    public void refreshPane(){
        for (int i = 0; i < btnList.length; i++)
            this.btnList[i].setSelected(false);
        
        for (int i = 0; i < boxList.length; i++)
            this.boxList[i].setSelected(false);
        
        for(int i = 0; i < fieldList.length; i++)
            this.fieldList[i].setText("");
        
        this.oct = 0;
        this.nov = 0;
        this.dec = 0;
        this.jan = 0;
        this.feb = 0;
        
        this.plan = 0;
        
        this.planField.setText("");
    }
    
    public int getPlan(){
        return this.plan;
    }
    
    public int getOct(){
        return this.oct;
    }
    public int getNov(){
        return this.nov;
    }
    public int getDec(){
        return this.dec;
    }
    public int getJan(){
        return this.jan;
    }
    public int getFeb(){
        return this.feb;
    }
    public int getMethod(){
        return this.method;
    }
    public String getComment(){
        return this.comment;
    }
    
    
}
