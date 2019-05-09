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
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 *
 * @author purplesmurf
 */
public class SummerPaymentMenu extends Stage{
    
    private Connection conn;//connection to database
    private String seasonId;//seasonId for the session
    
    private Scene scene;
    private BorderPane pane = new BorderPane();
    private VBox leftPane = new VBox();
    private StackPane centerPane = new StackPane();
    
    //PAYMENT METHOD BUTTONS
    private final ToggleButton allBtn = new ToggleButton("All");//button for displaying all clients
    private final ToggleButton creditBtn = new ToggleButton("Credit");//button for displaying credit paying clients
    private final ToggleButton checkBtn = new ToggleButton("Check");//button for displaying check paying clients
    private final ToggleButton interactBtn = new ToggleButton("Interact");//button for displaying interact paying clients
    private final ToggleButton otherBtn = new ToggleButton("Other");//button for displaying misc playing clients
    
    private final ToggleGroup methodGroup = new ToggleGroup();//toggle group for payment method buttons
    
    private Button statusBtn = new Button("All");//button for displaying clients according to the status selected
    private int status = 0;//0 = all, 1 = residential, 2 = commercial
    private int method = 0;//0 = all, 1 = check, 2 = credit, 3 = interact, 4 = other
    
    private final ToggleButton[] btnList = {allBtn, creditBtn, checkBtn, interactBtn, otherBtn};
    
    private final Text methodLbl = new Text("Method");
    private final Text statusLbl = new Text("Status");
    
    private final ObservableList<Payment> clientList = FXCollections.observableArrayList();//payment tableview
    private final TableView<Payment> table = new TableView<>();//list containing the payment information of the clients
    
    private final Insets insets = new Insets(5, 5, 5, 5);
    
    //TOTALS FOR EACH MONTH
    private double marTotal = 0;
    private double aprTotal = 0;
    private double mayTotal = 0;
    private double junTotal = 0;
    private double julTotal = 0;
    private double augTotal = 0;
    private double sepTotal = 0;
    private double octTotal = 0;
    private double total = 0;
    
    //SUBTOTALS FOR EACH MONTH
    private double marSubTotal = 0;
    private double aprSubTotal = 0;
    private double maySubTotal = 0;
    private double junSubTotal = 0;
    private double julSubTotal = 0;
    private double augSubTotal = 0;
    private double sepSubTotal = 0;
    private double octSubTotal = 0;
   
    private String query;
    
    public SummerPaymentMenu(Connection conn, String seasonId){
        
        this.conn = conn;
        this.seasonId = seasonId;
        
        this.query = "select client_information.address, client_information.name, "
                    + "summer_payment.total, summer_payment.plan, "
                    + "summer_payment.mar, summer_payment.apr, summer_payment.may, summer_payment.jun, summer_payment.jul, "
                    + "summer_payment.aug, summer_payment.sep, summer_payment.oct, "
                    + "summer_payment.method, client_information.id, summer_payment.comments "
                    + "from summer_payment inner join client_information "
                    + "on summer_payment.id = client_information.id and summer_payment.season = '" + this.seasonId + "' ";
        
        setUpLeftPane();
        setUpCenterPane();
        
        pane.setLeft(this.leftPane);
        pane.setCenter(this.centerPane);
        
        scene = new Scene(pane, 1200, 700);
        this.setScene(scene);
        this.setTitle("Payment Tab for Summer " + this.seasonId.substring(1));
        
    }
    
    //sets up the left pane with the filter options
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
        
        setButtonAction();
        
    }
    
    private void setButtonAction(){
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
                this.query += "and summer_payment.method = 0 ";
            }break;
            case 2: {
                this.query += "and summer_payment.method = 1 ";
            }break;
            case 3: {
                this.query += "and summer_payment.method = 2 ";
            }break;
            case 4: {
                this.query += "and summer_payment.method = 3 ";
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
        
        this.query += "order by client_information.door_number asc";
    }
    
    private void resetQuery(){
        this.query = "select client_information.address, client_information.name, "
                    + "summer_payment.total, summer_payment.plan, "
                    + "summer_payment.mar, summer_payment.apr, summer_payment.may, summer_payment.jun, summer_payment.jul, "
                    + "summer_payment.aug, summer_payment.sep, summer_payment.oct, "
                    + "summer_payment.method, client_information.id, summer_payment.comments "
                    + "from summer_payment inner join client_information "
                    + "on summer_payment.id = client_information.id and summer_payment.season = '" + this.seasonId + "' ";
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
                
                this.clientList.add(new Payment(rs.getInt(14), rs.getString(1), rs.getString(2), rs.getInt(13), rs.getDouble(3), rs.getInt(4), rs.getInt(5), rs.getInt(6), rs.getInt(7), rs.getInt(8), rs.getInt(9), rs.getInt(10), rs.getInt(11), rs.getInt(12), rs.getString(15)));
                
                this.total += rs.getDouble(3);
                
                //mar
                if (rs.getInt(5) == 1 || rs.getInt(5) == 2){
                    this.marTotal += (rs.getDouble(3) / rs.getInt(4));
                }
                else if (rs.getInt(5) == 3){
                    this.marTotal += (rs.getDouble(3) / rs.getInt(4));
                    this.marSubTotal += (rs.getDouble(3) / rs.getInt(4));
                }
                
                //apr
                if (rs.getInt(6) == 1 || rs.getInt(6) == 2){
                    this.aprTotal += (rs.getDouble(3) / rs.getInt(4));
                }
                else if (rs.getInt(6) == 3){
                    this.aprTotal += (rs.getDouble(3) / rs.getInt(4));
                    this.aprSubTotal += (rs.getDouble(3) / rs.getInt(4));
                }
                
                //may
                if (rs.getInt(7) == 1 || rs.getInt(7) == 2){
                    this.mayTotal += (rs.getDouble(3) / rs.getInt(4));
                }
                else if (rs.getInt(7) == 3){
                    this.mayTotal += (rs.getDouble(3) / rs.getInt(4));
                    this.maySubTotal += (rs.getDouble(3) / rs.getInt(4));
                }
                
                //jun
                if (rs.getInt(8) == 1 || rs.getInt(8) == 2){
                    this.junTotal += (rs.getDouble(3) / rs.getInt(4));
                }
                else if (rs.getInt(8) == 3){
                    this.junTotal += (rs.getDouble(3) / rs.getInt(4));
                    this.junSubTotal += (rs.getDouble(3) / rs.getInt(4));
                }
                
                //jul
                if (rs.getInt(9) == 1 || rs.getInt(9) == 2){
                    this.julTotal += (rs.getDouble(3) / rs.getInt(4));
                }
                else if (rs.getInt(9) == 3){
                    this.julTotal += (rs.getDouble(3) / rs.getInt(4));
                    this.julSubTotal += (rs.getDouble(3) / rs.getInt(4));
                }
                
                //aug
                if (rs.getInt(10) == 1 || rs.getInt(10) == 2){
                    this.augTotal += (rs.getDouble(3) / rs.getInt(4));
                }
                else if (rs.getInt(10) == 3){
                    this.augTotal += (rs.getDouble(3) / rs.getInt(4));
                    this.augSubTotal += (rs.getDouble(3) / rs.getInt(4));
                }
                
                //sep
                if (rs.getInt(11) == 1 || rs.getInt(11) == 2){
                    this.sepTotal += (rs.getDouble(3) / rs.getInt(4));
                }
                else if (rs.getInt(11) == 3){
                    this.sepTotal += (rs.getDouble(3) / rs.getInt(4));
                    this.sepSubTotal += (rs.getDouble(3) / rs.getInt(4));
                }
                
                //oct
                if (rs.getInt(12) == 1 || rs.getInt(12) == 2){
                    this.octTotal += (rs.getDouble(3) / rs.getInt(4));
                }
                else if (rs.getInt(12) == 3){
                    this.octTotal += (rs.getDouble(3) / rs.getInt(4));
                    this.octSubTotal += (rs.getDouble(3) / rs.getInt(4));
                }
             
            }
            
            
            this.clientList.add(new Payment());
            this.clientList.add(new Payment(this.total, this.marTotal, this.aprTotal, this.mayTotal, this.junTotal, this.julTotal, this.augTotal, this.sepTotal, this.octTotal, 
                    this.marSubTotal, this.aprSubTotal, this.maySubTotal, this.junSubTotal, this.julSubTotal, this.augSubTotal, this.sepSubTotal, this.octSubTotal));
            
            resetQuery();
            
        }
        catch (SQLException ex){
            ex.printStackTrace();
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
        
        //March column
        TableColumn<Payment, Text> marchCol = new TableColumn<>("MARCH");
        marchCol.setCellValueFactory(new PropertyValueFactory<>("mar"));
        
        //April column
        TableColumn<Payment, Text> aprilCol = new TableColumn<>("APRIL");
        aprilCol.setCellValueFactory(new PropertyValueFactory<>("apr"));
        
        //May column
        TableColumn<Payment, Text> mayCol = new TableColumn<>("MAY");
        mayCol.setCellValueFactory(new PropertyValueFactory<>("may"));
        
        //June column
        TableColumn<Payment, Text> juneCol = new TableColumn<>("JUNE");
        juneCol.setCellValueFactory(new PropertyValueFactory<>("jun"));
        
        //July column
        TableColumn<Payment, Text> julyCol = new TableColumn<>("JULY");
        julyCol.setCellValueFactory(new PropertyValueFactory<>("jul"));
        
        //August column
        TableColumn<Payment, Text> augustCol = new TableColumn<>("AUGUST");
        augustCol.setCellValueFactory(new PropertyValueFactory<>("aug"));
        
        //Septembre column
        TableColumn<Payment, Text> septembreCol = new TableColumn<>("SEPTEMBRE");
        septembreCol.setCellValueFactory(new PropertyValueFactory<>("sep"));
        
        //October column
        TableColumn<Payment, Text> octoberCol = new TableColumn<>("OCTOBER");
        octoberCol.setCellValueFactory(new PropertyValueFactory<>("oct"));
        
        //Comment column
        TableColumn<Payment, Text> commentCol = new TableColumn<>("COMMENTS");
        commentCol.setCellValueFactory(new PropertyValueFactory<>("comment"));
        
        
        this.table.getColumns().addAll(addressCol, nameCol, methodCol, totalCol, marchCol, aprilCol, mayCol, juneCol, julyCol, augustCol, septembreCol, octoberCol, commentCol);
        
        //fill list
        try {
            
            
            Statement st = this.conn.createStatement();
            String //                                     1                         2
                    query = "select client_information.address, client_information.name, "
                    //                  3                     4
                    + "summer_payment.total, summer_payment.plan, "
                    //                 5                   6                   7                   8                   9
                    + "summer_payment.mar, summer_payment.apr, summer_payment.may, summer_payment.jun, summer_payment.jul, "
                    //                 10                  11                  12
                    + "summer_payment.aug, summer_payment.sep, summer_payment.oct, "
                    //                  13                       14                    15
                    + "summer_payment.method, client_information.id, summer_payment.comments "
                    
                    + "from summer_payment inner join client_information "
                    + "on summer_payment.id = client_information.id and summer_payment.season = '" + this.seasonId + "' "
                    + "order by client_information.door_number asc";
            ResultSet rs = st.executeQuery(query);
            
            this.clientList.clear();
            resetTotals();
            while (rs.next()){
                
                this.clientList.add(new Payment(rs.getInt(14), rs.getString(1), rs.getString(2), rs.getInt(13), rs.getDouble(3), rs.getInt(4), rs.getInt(5), rs.getInt(6), rs.getInt(7), rs.getInt(8), rs.getInt(9), rs.getInt(10), rs.getInt(11), rs.getInt(12), rs.getString(15)));
                
                this.total += rs.getDouble(3);
                
                if (rs.getInt(5) == 1 || rs.getInt(5) == 2){
                    this.marTotal += (rs.getDouble(3) / rs.getInt(4));
                }
                else if (rs.getInt(5) == 3){
                    this.marTotal += (rs.getDouble(3) / rs.getInt(4));
                    this.marSubTotal += (rs.getDouble(3) / rs.getInt(4));
                }
                
                if (rs.getInt(6) == 1 || rs.getInt(6) == 2){
                    this.aprTotal += (rs.getDouble(3) / rs.getInt(4));
                }
                else if (rs.getInt(6) == 3){
                    this.aprTotal += (rs.getDouble(3) / rs.getInt(4));
                    this.aprSubTotal += (rs.getDouble(3) / rs.getInt(4));
                }
                
                if (rs.getInt(7) == 1 || rs.getInt(7) == 2){
                    this.mayTotal += (rs.getDouble(3) / rs.getInt(4));
                }
                else if (rs.getInt(7) == 3){
                    this.mayTotal += (rs.getDouble(3) / rs.getInt(4));
                    this.maySubTotal += (rs.getDouble(3) / rs.getInt(4));
                }
                
                if (rs.getInt(8) == 1 || rs.getInt(8) == 2){
                    this.junTotal += (rs.getDouble(3) / rs.getInt(4));
                }
                else if (rs.getInt(8) == 3){
                    this.junTotal += (rs.getDouble(3) / rs.getInt(4));
                    this.junSubTotal += (rs.getDouble(3) / rs.getInt(4));
                }
                
                if (rs.getInt(9) == 1 || rs.getInt(9) == 2){
                    this.julTotal += (rs.getDouble(3) / rs.getInt(4));
                }
                else if (rs.getInt(9) == 3){
                    this.julTotal += (rs.getDouble(3) / rs.getInt(4));
                    this.julSubTotal += (rs.getDouble(3) / rs.getInt(4));
                }
                
                if (rs.getInt(10) == 1 || rs.getInt(10) == 2){
                    this.augTotal += (rs.getDouble(3) / rs.getInt(4));
                }
                else if (rs.getInt(10) == 3){
                    this.augTotal += (rs.getDouble(3) / rs.getInt(4));
                    this.augSubTotal += (rs.getDouble(3) / rs.getInt(4));
                }
                
                if (rs.getInt(11) == 1 || rs.getInt(11) == 2){
                    this.sepTotal += (rs.getDouble(3) / rs.getInt(4));
                }
                else if (rs.getInt(11) == 3){
                    this.sepTotal += (rs.getDouble(3) / rs.getInt(4));
                    this.sepSubTotal += (rs.getDouble(3) / rs.getInt(4));
                }
                
                if (rs.getInt(12) == 1 || rs.getInt(12) == 2){
                    this.octTotal += (rs.getDouble(3) / rs.getInt(4));
                }
                else if (rs.getInt(12) == 3){
                    this.octTotal += (rs.getDouble(3) / rs.getInt(4));
                    this.octSubTotal += (rs.getDouble(3) / rs.getInt(4));
                }
                
            }
            this.clientList.add(new Payment());
            this.clientList.add(new Payment(this.total, this.marTotal, this.aprTotal, this.mayTotal, this.junTotal, this.julTotal, this.augTotal, this.sepTotal, this.octTotal, 
                    this.marSubTotal, this.aprSubTotal, this.maySubTotal, this.junSubTotal, this.julSubTotal, this.augSubTotal, this.sepSubTotal, this.octSubTotal));
            
            
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
                
                if (col.equals(marchCol)){
                    updatePayment(payment.getId(), "mar");
                }
                else if (col.equals(aprilCol)){
                    updatePayment(payment.getId(), "apr");
                }
                else if (col.equals(mayCol)){
                    updatePayment(payment.getId(), "may");
                }
                else if (col.equals(juneCol)){
                    updatePayment(payment.getId(), "june");
                }
                else if (col.equals(julyCol)){
                    updatePayment(payment.getId(), "july");
                }
                else if (col.equals(augustCol)){
                    updatePayment(payment.getId(), "aug");
                }
                else if (col.equals(septembreCol)){
                    updatePayment(payment.getId(), "sep");
                }
                else if (col.equals(octoberCol)){
                    updatePayment(payment.getId(), "oct");
                }
            }
        });
        
        this.table.setOnKeyReleased(e -> {
            if (e.getCode().equals(KeyCode.ENTER)){
                TablePosition pos = this.table.getSelectionModel().getSelectedCells().get(0);
                int row = pos.getRow();
                Payment payment = this.table.getItems().get(row);
                TableColumn col = pos.getTableColumn();
                
                if (col.equals(marchCol)){
                    updatePayment(payment.getId(), "mar");
                    this.table.getSelectionModel().select(row, marchCol);
                }
                else if (col.equals(aprilCol)){
                    updatePayment(payment.getId(), "apr");
                    this.table.getSelectionModel().select(row, aprilCol);
                }
                else if (col.equals(mayCol)){
                    updatePayment(payment.getId(), "may");
                    this.table.getSelectionModel().select(row, mayCol);
                }
                else if (col.equals(juneCol)){
                    updatePayment(payment.getId(), "jun");
                    this.table.getSelectionModel().select(row, juneCol);
                }
                else if (col.equals(julyCol)){
                    updatePayment(payment.getId(), "jul");
                    this.table.getSelectionModel().select(row, julyCol);
                }
                else if (col.equals(augustCol)){
                    updatePayment(payment.getId(), "aug");
                    this.table.getSelectionModel().select(row, augustCol);
                }
                else if (col.equals(septembreCol)){
                    updatePayment(payment.getId(), "sep");
                    this.table.getSelectionModel().select(row, septembreCol);
                }
                else if (col.equals(octoberCol)){
                    updatePayment(payment.getId(), "oct");
                    this.table.getSelectionModel().select(row, octoberCol);
                }
            }
        });
        
    }
    
    private void updatePayment(int id, String month){
        try {
            int monthCurrentValue = -1;
            
            Statement st = this.conn.createStatement();
            String query = "select " + month + " from summer_payment where id = " + id;
            ResultSet rs = st.executeQuery(query);
            
            while (rs.next()){
                monthCurrentValue = rs.getInt(1);
            }
            
            switch (monthCurrentValue){
                case 0: {//do nothing
                    
                }break;
                case 1: {//set to recieved
                    query = "update summer_payment set " + month + " = " + 2 + " where id = " + id;
                }break;
                case 2: {//set to deposited
                    query = "update summer_payment set " + month + " = " + 3 + " where id = " + id;
                }break;
                case 3: {//set to not recieved
                    query = "update summer_payment set " + month + " = " + 1 + " where id = " + id;
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
        this.marTotal = 0;
        this.aprTotal = 0;
        this.mayTotal = 0;
        this.junTotal = 0;
        this.julTotal = 0;
        this.augTotal = 0;
        this.sepTotal = 0;
        this.octTotal = 0;
        this.total = 0;
        
        this.marSubTotal = 0;
        this.aprSubTotal = 0;
        this.maySubTotal = 0;
        this.junSubTotal = 0;
        this.julSubTotal = 0;
        this.augSubTotal = 0;
        this.sepSubTotal = 0;
        this.octSubTotal = 0;
        
    }
    
}
