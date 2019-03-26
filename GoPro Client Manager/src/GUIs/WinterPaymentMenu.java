/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUIs;

import Objects.Payment;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 *
 * @author purplesmurf
 */
public class WinterPaymentMenu extends Stage{
    
    private Connection conn;
    private String seasonId;
    
    private Scene scene;
    private BorderPane pane = new BorderPane();
    
    private VBox leftPane = new VBox();
    private StackPane centerPane = new StackPane();
    
    private final ToggleButton allBtn = new ToggleButton("All");
    private final ToggleButton creditBtn = new ToggleButton("Credit");
    private final ToggleButton checkBtn = new ToggleButton("Check");
    private final ToggleButton interactBtn = new ToggleButton("Interact");
    private final ToggleButton otherBtn = new ToggleButton("Other");
    
    private final ToggleGroup methodGroup = new ToggleGroup();
    
    private Button statusBtn = new Button("All");
    private int status = 0;//0 = all, 1 = residential, 2 = commercial
    private int method = 0;//0 = all, 1 = check, 2 = credit, 3 = interact, 4 = other
    
    private final ToggleButton[] btnList = {allBtn, creditBtn, checkBtn, interactBtn, otherBtn};
    
    private final Text methodLbl = new Text("Method");
    private final Text statusLbl = new Text("Status");
    
    private final ObservableList<Payment> clientList = FXCollections.observableArrayList();
    private final TableView<Payment> table = new TableView<>();
    
    private final Insets insets = new Insets(5, 5, 5, 5);
    
    private double octTotal = 0;
    private double novTotal = 0;
    private double decTotal = 0;
    private double janTotal = 0;
    private double febTotal = 0;
    private double total = 0;
    
    private double octSubTotal = 0;
    private double novSubTotal = 0;
    private double decSubTotal = 0;
    private double janSubTotal = 0;
    private double febSubTotal = 0;
   
    private String query;
   
    public WinterPaymentMenu(Connection conn, String seasonId){
        this.conn = conn;
        this.seasonId = seasonId;
        
        this.query = "select client_information.address, client_information.name, "
                    + "winter_payment.total, winter_payment.plan, "
                    + "winter_payment.oct, winter_payment.nov, winter_payment.dec, winter_payment.jan, winter_payment.feb, "
                    + "winter_payment.method, client_information.id, winter_payment.comments "
                    + "from winter_payment inner join client_information "
                    + "on winter_payment.id = client_information.id and winter_payment.season = '" + this.seasonId + "' ";
        
        
        setUpLeftPane();
        setUpCenterPane();
        
        pane.setLeft(this.leftPane);
        pane.setCenter(this.centerPane);
        
        scene = new Scene(pane, 1200, 700);
        this.setScene(scene);
        this.setTitle("Payment Tab for Winter " + this.seasonId.substring(1));
        
    }
    
    private void setUpLeftPane(){
        this.leftPane.setPadding(insets);
        this.leftPane.setAlignment(Pos.CENTER);
        this.leftPane.setSpacing(20);
        
        this.leftPane.getChildren().addAll(this.methodLbl,this.allBtn, this.creditBtn, this.checkBtn, this.interactBtn, this.otherBtn, this.statusLbl, this.statusBtn);
        for (int i = 0; i < btnList.length; i++){
            btnList[i].setMinSize(125, 50);
            btnList[i].setMaxSize(125, 50);
            btnList[i].setFont(Font.font("Rockwell", 18));
            btnList[i].setFocusTraversable(false);
        }
        
        this.statusBtn.setMinSize(125, 50);
        this.statusBtn.setMaxSize(125, 50);
        this.statusBtn.setFont(Font.font("Rockwell", 18));
        this.statusBtn.setFocusTraversable(false);
        
        this.methodLbl.setFont(Font.font("Rockwell", 18));
        this.statusLbl.setFont(Font.font("Rockwell", 18));
        
        this.allBtn.setSelected(true);
        
        
        this.allBtn.setToggleGroup(this.methodGroup);
        this.creditBtn.setToggleGroup(this.methodGroup);
        this.checkBtn.setToggleGroup(this.methodGroup);
        this.interactBtn.setToggleGroup(this.methodGroup);
        this.otherBtn.setToggleGroup(this.methodGroup);
        
        this.allBtn.setOnAction(e -> {
            if (!this.allBtn.isSelected())
                this.allBtn.setSelected(true);
            
            this.method = 0;
            
            sortTable();
        });
        
        this.checkBtn.setOnAction(e -> {
            if (!this.checkBtn.isSelected())
                this.checkBtn.setSelected(true);
            
            this.method = 1;
            
            sortTable();
            
        });
        
        this.creditBtn.setOnAction(e -> {
            if (!this.creditBtn.isSelected())
                this.creditBtn.setSelected(true);
            
            this.method = 2;
            
            sortTable();
            
        });
        
        this.interactBtn.setOnAction(e -> {
            if (!this.interactBtn.isSelected())
                this.interactBtn.setSelected(true);
            
            this.method = 3;
            
            sortTable();
            
        });
        
        this.otherBtn.setOnAction(e -> {
            if (!this.otherBtn.isSelected())
                this.otherBtn.setSelected(true);
            
            this.method = 4;
            
            sortTable();
            
        });
        
        this.statusBtn.setOnAction(e -> {
            switch (this.status){
                case 0: {
                    this.status = 1;
                    this.statusBtn.setText("Residential");
                    sortTable();
                }break;
                case 1: {
                    this.status = 2;
                    this.statusBtn.setText("Commercial");
                    sortTable();
                }break;
                case 2: {
                    this.status = 0;
                    this.statusBtn.setText("All");
                    sortTable();
                }break;
            }
        });
        
        
    }
    
    private void setUpCenterPane(){
        this.centerPane.getChildren().add(this.table);
        
        this.centerPane.setAlignment(Pos.CENTER);
        this.centerPane.setPadding(this.insets);
        
        
        
        setUpTable();
    }
    private void setQuery(){
       
        switch (this.method){
            case 0: {
                //do nothing..want all clients
            }break;
            case 1: {
                this.query += "and winter_payment.method = 0 ";
            }break;
            case 2: {
                this.query += "and winter_payment.method = 1 ";
            }break;
            case 3: {
                this.query += "and winter_payment.method = 2 ";
            }break;
            case 4: {
                this.query += "and winter_payment.method = 3 ";
            }break;
        }
        
        switch (this.status){
            case 0: {
                //do nothing...want all clients
            }break;
            case 1: {
                this.query += "and client_information.status = 0 ";
            }break;
            case 2: {
                this.query += "and client_information.status = 1 ";
            }break;
        }
        
        this.query += "order by client_information.address asc";
    }
    private void resetQuery(){
        this.query = "select client_information.address, client_information.name, "
                    + "winter_payment.total, winter_payment.plan, "
                    + "winter_payment.oct, winter_payment.nov, winter_payment.dec, winter_payment.jan, winter_payment.feb, "
                    + "winter_payment.method, client_information.id, winter_payment.comments "
                    + "from winter_payment inner join client_information "
                    + "on winter_payment.id = client_information.id and winter_payment.season = '" + this.seasonId + "' ";
    }
    private void sortTable(){
        try {
            
            Statement st = this.conn.createStatement();
            setQuery();
            System.out.println(this.query);
            ResultSet rs = st.executeQuery(this.query);
            
            
            
            this.clientList.clear();
            resetTotals();
            while (rs.next()){
                
                this.clientList.add(new Payment(rs.getInt(11), rs.getString(1), rs.getString(2), rs.getInt(10), rs.getDouble(3), rs.getInt(4), rs.getInt(5), rs.getInt(6), rs.getInt(7), rs.getInt(8), rs.getInt(9), rs.getString(12)));
                
                this.total += rs.getDouble(3);
                
                if (rs.getInt(5) == 1 || rs.getInt(5) == 2){
                    this.octTotal += (rs.getDouble(3) / rs.getInt(4));
                }
                else if (rs.getInt(5) == 3){
                    this.octTotal += (rs.getDouble(3) / rs.getInt(4));
                    this.octSubTotal += (rs.getDouble(3) / rs.getInt(4));
                }
                
                if (rs.getInt(6) == 1 || rs.getInt(6) == 2){
                    this.novTotal += (rs.getDouble(3) / rs.getInt(4));
                }
                else if (rs.getInt(6) == 3){
                    this.novTotal += (rs.getDouble(3) / rs.getInt(4));
                    this.novSubTotal += (rs.getDouble(3) / rs.getInt(4));
                }
                
                if (rs.getInt(7) == 1 || rs.getInt(7) == 2){
                    this.decTotal += (rs.getDouble(3) / rs.getInt(4));
                }
                else if (rs.getInt(7) == 3){
                    this.decTotal += (rs.getDouble(3) / rs.getInt(4));
                    this.decSubTotal += (rs.getDouble(3) / rs.getInt(4));
                }
                
                if (rs.getInt(8) == 1 || rs.getInt(8) == 2){
                    this.janTotal += (rs.getDouble(3) / rs.getInt(4));
                }
                else if (rs.getInt(8) == 3){
                    this.janTotal += (rs.getDouble(3) / rs.getInt(4));
                    this.janSubTotal += (rs.getDouble(3) / rs.getInt(4));
                }
                
                if (rs.getInt(9) == 1 || rs.getInt(9) == 2){
                    this.febTotal += (rs.getDouble(3) / rs.getInt(4));
                }
                else if (rs.getInt(9) == 3){
                    this.febTotal += (rs.getDouble(3) / rs.getInt(4));
                    this.febSubTotal += (rs.getDouble(3) / rs.getInt(4));
                }
             
            }
            
            
            this.clientList.add(new Payment());
            this.clientList.add(new Payment(this.total, this.octTotal, this.novTotal, this.decTotal, this.janTotal, this.febTotal, 
                    this.octSubTotal, this.novSubTotal, this.decSubTotal, this.janSubTotal, this.febSubTotal));
            
            resetQuery();
            
        }
        catch (SQLException ex){
            
        }
    }
    
    private void setUpTable(){
        //Address column
        TableColumn<Payment, String> addressCol = new TableColumn<>("ADDRESS");
        addressCol.setCellValueFactory(new PropertyValueFactory<>("addressString"));
        
        //Name column
        TableColumn<Payment, String> nameCol = new TableColumn<>("NAME");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("nameString"));
        
        //Method column
        TableColumn<Payment, String> methodCol = new TableColumn<>("METHOD");
        methodCol.setCellValueFactory(new PropertyValueFactory<>("methodString"));
        
        //Total column
        TableColumn<Payment, String> totalCol = new TableColumn<>("TOTAL");
        totalCol.setCellValueFactory(new PropertyValueFactory<>("totalString"));
        
        //October column
        TableColumn<Payment, Text> octoberCol = new TableColumn<>("OCTOBER");
        octoberCol.setCellValueFactory(new PropertyValueFactory<>("oct"));
        
        //November column
        TableColumn<Payment, Text> novemberCol = new TableColumn<>("NOVEMBER");
        novemberCol.setCellValueFactory(new PropertyValueFactory<>("nov"));
        
        //December column
        TableColumn<Payment, Text> decemberCol = new TableColumn<>("DECEMBER");
        decemberCol.setCellValueFactory(new PropertyValueFactory<>("dec"));
        
        //January column
        TableColumn<Payment, Text> januaryCol = new TableColumn<>("JANUARY");
        januaryCol.setCellValueFactory(new PropertyValueFactory<>("jan"));
        
        //February column
        TableColumn<Payment, Text> februaryCol = new TableColumn<>("FEBRUARY");
        februaryCol.setCellValueFactory(new PropertyValueFactory<>("feb"));
        
        //Comment column
        TableColumn<Payment, Text> commentCol = new TableColumn<>("COMMENTS");
        commentCol.setCellValueFactory(new PropertyValueFactory<>("comment"));
        
        
        this.table.getColumns().addAll(addressCol, nameCol, methodCol, totalCol, octoberCol, novemberCol, decemberCol, januaryCol, februaryCol, commentCol);
        
        //fill list
        try {
            
            
            Statement st = this.conn.createStatement();
            String query = "select client_information.address, client_information.name, "
                    + "winter_payment.total, winter_payment.plan, "
                    + "winter_payment.oct, winter_payment.nov, winter_payment.dec, winter_payment.jan, winter_payment.feb, "
                    + "winter_payment.method, client_information.id, winter_payment.comments "
                    + "from winter_payment inner join client_information "
                    + "on winter_payment.id = client_information.id and winter_payment.season = '" + this.seasonId + "' "
                    + "order by client_information.address asc";
            ResultSet rs = st.executeQuery(query);
            
            this.clientList.clear();
            resetTotals();
            while (rs.next()){
                
                this.clientList.add(new Payment(rs.getInt(11), rs.getString(1), rs.getString(2), rs.getInt(10), rs.getDouble(3), rs.getInt(4), rs.getInt(5), rs.getInt(6), rs.getInt(7), rs.getInt(8), rs.getInt(9), rs.getString(12)));
                
                this.total += rs.getDouble(3);
                
                if (rs.getInt(5) == 1 || rs.getInt(5) == 2){
                    this.octTotal += (rs.getDouble(3) / rs.getInt(4));
                }
                else if (rs.getInt(5) == 3){
                    this.octTotal += (rs.getDouble(3) / rs.getInt(4));
                    this.octSubTotal += (rs.getDouble(3) / rs.getInt(4));
                }
                
                if (rs.getInt(6) == 1 || rs.getInt(6) == 2){
                    this.novTotal += (rs.getDouble(3) / rs.getInt(4));
                }
                else if (rs.getInt(6) == 3){
                    this.novTotal += (rs.getDouble(3) / rs.getInt(4));
                    this.novSubTotal += (rs.getDouble(3) / rs.getInt(4));
                }
                
                if (rs.getInt(7) == 1 || rs.getInt(7) == 2){
                    this.decTotal += (rs.getDouble(3) / rs.getInt(4));
                }
                else if (rs.getInt(7) == 3){
                    this.decTotal += (rs.getDouble(3) / rs.getInt(4));
                    this.decSubTotal += (rs.getDouble(3) / rs.getInt(4));
                }
                
                if (rs.getInt(8) == 1 || rs.getInt(8) == 2){
                    this.janTotal += (rs.getDouble(3) / rs.getInt(4));
                }
                else if (rs.getInt(8) == 3){
                    this.janTotal += (rs.getDouble(3) / rs.getInt(4));
                    this.janSubTotal += (rs.getDouble(3) / rs.getInt(4));
                }
                
                if (rs.getInt(9) == 1 || rs.getInt(9) == 2){
                    this.febTotal += (rs.getDouble(3) / rs.getInt(4));
                }
                else if (rs.getInt(9) == 3){
                    this.febTotal += (rs.getDouble(3) / rs.getInt(4));
                    this.febSubTotal += (rs.getDouble(3) / rs.getInt(4));
                }
                
            }
            this.clientList.add(new Payment());
            this.clientList.add(new Payment(this.total, this.octTotal, this.novTotal, this.decTotal, this.janTotal, this.febTotal, 
                    this.octSubTotal, this.novSubTotal, this.decSubTotal, this.janSubTotal, this.febSubTotal));
            
            
        } catch(SQLException ex){
            System.out.println(ex.getMessage());
        }
        
        this.table.setItems(this.clientList);
        this.table.setFocusTraversable(true);
        this.table.setEditable(true);
        this.table.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        this.table.getSelectionModel().setCellSelectionEnabled(true);
        
        this.table.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2){
                TablePosition pos = this.table.getSelectionModel().getSelectedCells().get(0);
                int row = pos.getRow();
                Payment payment = this.table.getItems().get(row);
                TableColumn col = pos.getTableColumn();
                
                if (col.equals(octoberCol)){
                    updatePayment(payment.getId(), "oct");
                }
                else if (col.equals(novemberCol)){
                    updatePayment(payment.getId(), "nov");
                }
                else if (col.equals(decemberCol)){
                    updatePayment(payment.getId(), "dec");
                }
                else if (col.equals(januaryCol)){
                    updatePayment(payment.getId(), "jan");
                }
                else if (col.equals(februaryCol)){
                    updatePayment(payment.getId(), "feb");
                }
            }
        });
        
        this.table.setOnKeyReleased(e -> {
            if (e.getCode().equals(KeyCode.ENTER)){
                TablePosition pos = this.table.getSelectionModel().getSelectedCells().get(0);
                int row = pos.getRow();
                Payment payment = this.table.getItems().get(row);
                TableColumn col = pos.getTableColumn();
                
                if (col.equals(octoberCol)){
                    updatePayment(payment.getId(), "oct");
                    this.table.getSelectionModel().select(row, octoberCol);
                }
                else if (col.equals(novemberCol)){
                    updatePayment(payment.getId(), "nov");
                    this.table.getSelectionModel().select(row, novemberCol);
                }
                else if (col.equals(decemberCol)){
                    updatePayment(payment.getId(), "dec");
                    this.table.getSelectionModel().select(row, decemberCol);
                }
                else if (col.equals(januaryCol)){
                    updatePayment(payment.getId(), "jan");
                    this.table.getSelectionModel().select(row, januaryCol);
                }
                else if (col.equals(februaryCol)){
                    updatePayment(payment.getId(), "feb");
                    this.table.getSelectionModel().select(row, februaryCol);
                }
                else if (col.equals(commentCol)){
                    //store string into payment directly instead of boolean
                }
            }
        });
    }
    
    private void updatePayment(int id, String month){
        try {
            int monthCurrentValue = -1;
            
            Statement st = this.conn.createStatement();
            String query = "select " + month + " from winter_payment where id = " + id;
            ResultSet rs = st.executeQuery(query);
            
            while (rs.next()){
                monthCurrentValue = rs.getInt(1);
            }
            
            switch (monthCurrentValue){
                case 0: {//do nothing
                    
                }break;
                case 1: {//set to recieved
                    query = "update winter_payment set " + month + " = " + 2 + " where id = " + id;
                }break;
                case 2: {//set to deposited
                    query = "update winter_payment set " + month + " = " + 3 + " where id = " + id;
                }break;
                case 3: {//set to not recieved
                    query = "update winter_payment set " + month + " = " + 1 + " where id = " + id;
                }break;
                case -1: {
                    
                }break;
            }
            
            st.executeUpdate(query);
            sortTable();
            
        }
        catch (SQLException ex){
            
        }
    }
    
    private void resetTotals(){
        this.octTotal = 0;
        this.novTotal = 0;
        this.decTotal = 0;
        this.janTotal = 0;
        this.febTotal = 0;
        this.total = 0;
        
        this.octSubTotal = 0;
        this.novSubTotal = 0;
        this.decSubTotal = 0;
        this.janSubTotal = 0;
        this.febSubTotal = 0;
        
    }
    
    
}
