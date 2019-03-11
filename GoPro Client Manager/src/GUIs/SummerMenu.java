/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUIs;

import InformationGUI.InformationMenu;
import NewClientGUI.NewClientMenu;
import Objects.Client;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 *
 * @author purplesmurf
 */
public class SummerMenu extends BorderPane{
    
    private Connection conn;
    private String seasonId;
    private NewClientMenu newClientMenu;
    private SummerPaymentMenu paymentMenu;
    private SummerFilterMenu filterMenu;
    private InformationMenu clientMenu;
    
    /***********************
     AVAILABLE OPTIONS
     ***********************/
    
    private Button filterBtn = new Button("Filter");
    private Button printBtn = new Button("Print");
    private Button addBatchBtn = new Button("Add Batch");
    private Button addSingleBtn = new Button("Add Single");
    private Button paymentTabBtn = new Button("Payment");
    
    private Button[] btnList = {filterBtn, printBtn, addBatchBtn, addSingleBtn, paymentTabBtn};
    
    /*************************
     * PANES FOR THE MENU
     *************************/
    
    private final VBox rightPane = new VBox();
    private final BorderPane centerPane = new BorderPane();
    
    /*************************
     * CLASS VARIABLES
     *************************/
    private final Insets insets = new Insets(5, 5, 5, 5);
    
    private boolean addingNewClient = false;
    private boolean paymentMenuOpen = false;
    
    /*************************
     * CENTER PANE NODES
     *************************/
    private final HBox centerTopPane = new HBox();
    private final StackPane centerCenterPane = new StackPane();
    
    private final TextField searchBar = new TextField();
    private final ToggleButton searchAddressBtn = new ToggleButton("Address");
    private final ToggleButton searchNameBtn = new ToggleButton("Name");
    private final ToggleGroup searchGroup = new ToggleGroup();
    private boolean searchValue = false;//false = search, true = name 
    
    private final TableView<Client> table = new TableView<>();
    private final ObservableList<Client> tableList = FXCollections.observableArrayList();
    
    /**************************
     * VALUES FOR TABLE FILTER
     **************************/
    
    private boolean all = true;
    private boolean spring = false;
    private boolean fall = false;
    private boolean weed = false;
    private boolean aeration = false;
    private boolean spiders = false;
    private boolean hedge = false;
    private boolean fertilizer = false;
    private boolean worms = false;
    private boolean soil = false;
    private int status = 0;//0 = all, 1 = residential, 2 = commercial
    
    private String searchQuery;
    
    private boolean filterActive = false;
    
    
    
    public SummerMenu(){
        
    }
    
    public SummerMenu(Connection conn, String seasonId){
        this.conn = conn;
        this.seasonId = seasonId;
        
        this.searchQuery = "select summer_services.id, client_information.address, client_information.status, "
                    + "client_information.name, summer_services.total, client_information.phone, "
                    + "summer_services.comments, client_information.city "
                    + "from summer_services inner join client_information "
                    + "on summer_services.id = client_information.id and summer_services.season = '" + this.seasonId + "' ";
        
        setRightPane();
        setCenterPane();
        
        setBtnAction();
        
        
        this.setRight(this.rightPane);
        this.setCenter(this.centerPane);
    }
    
    private void setRightPane(){
        rightPane.setPadding(insets);
        rightPane.setAlignment(Pos.CENTER);
        rightPane.setSpacing(25);
        
        rightPane.getChildren().addAll(this.filterBtn, this.printBtn, this.addBatchBtn, this.addSingleBtn, this.paymentTabBtn);
        
        for (int i = 0; i < btnList.length; i++){
            btnList[i].setMinSize(125, 50);
            btnList[i].setMaxSize(125, 50);
            btnList[i].setFont(Font.font("Rockwell", 18));
            btnList[i].setFocusTraversable(false);
        }
        
        
    }
    
    private void setCenterPane(){
        this.centerPane.setPadding(insets);
        
        this.centerPane.setTop(this.centerTopPane);
        this.centerPane.setCenter(this.centerCenterPane);
        
        this.centerTopPane.setPadding(insets);
        this.centerTopPane.setSpacing(10);
        this.centerTopPane.setAlignment(Pos.CENTER_RIGHT);
        this.centerTopPane.getChildren().addAll(this.searchAddressBtn, this.searchNameBtn, this.searchBar);
        
        this.centerCenterPane.getChildren().add(this.table);
        setCenterTable();
        
        this.searchAddressBtn.setToggleGroup(this.searchGroup);
        this.searchNameBtn.setToggleGroup(this.searchGroup);
        this.searchAddressBtn.setSelected(true);
        
        this.searchAddressBtn.setOnAction(e -> {
            if (!this.searchAddressBtn.isSelected()){
                this.searchAddressBtn.setSelected(true);
                this.searchValue = false;
            }
            else
                this.searchValue = false;
        });
        
        this.searchNameBtn.setOnAction(e -> {
            if (!this.searchNameBtn.isSelected()){
                this.searchNameBtn.setSelected(true);
                this.searchValue = true;
            }
            else
                this.searchValue = true;
        });
        
        this.searchBar.setOnKeyReleased(e -> {
            if (!this.searchValue){//by address
                try {
                    
                    Statement st = this.conn.createStatement();
                    String search = this.searchBar.getText().toUpperCase();
                    
                    for (int i = 0; i < search.length(); i++){
                        if (search.charAt(i) == '\''){
                            search = search.substring(0, i) + "'" + search.substring(i, search.length());
                            i++;
                        }
                    }
                    
                    this.searchQuery += "and client_information.address LIKE '" + search + "%' ";
                    this.searchQuery += "order by client_information.address asc";
                    
                    System.out.println(this.searchQuery);
                    
                    ResultSet rs = st.executeQuery(this.searchQuery);
                    this.tableList.clear();
                    while (rs.next()){
                        this.tableList.add(new Client(rs.getInt(1), rs.getString(2), rs.getInt(3), rs.getString(4), rs.getDouble(5), rs.getString(6), rs.getString(7), rs.getString(8)));
                        
                    }
                    
                    resetSearchQuery();
                }catch (SQLException ex){
                    System.out.println(ex.getMessage());
                }
            }
            else {//by name
                try {
                    
                    Statement st = this.conn.createStatement();
                    String search = this.searchBar.getText().toUpperCase();
                    
                    for (int i = 0; i < search.length(); i++){
                        if (search.charAt(i) == '\''){
                            search = search.substring(0, i) + "'" + search.substring(i, search.length());
                            i++;
                        }
                    }
                    
                    this.searchQuery += "and client_information.name LIKE '" + search + "%' ";
                    this.searchQuery += "order by client_information.address asc";
                    
                    ResultSet rs = st.executeQuery(this.searchQuery);
                    this.tableList.clear();
                    while (rs.next()){
                        this.tableList.add(new Client(rs.getInt(1), rs.getString(2), rs.getInt(3), rs.getString(4), rs.getDouble(5), rs.getString(6), rs.getString(7), rs.getString(8)));
                        
                    }
                    
                    resetSearchQuery();
                }catch (SQLException ex){
                    System.out.println(ex.getMessage());
                }
            }
        });
        
    }
    
    private void setCenterTable(){
        //create columns
        TableColumn<Client, Text> addressCol = new TableColumn<>("ADDRESS");
        addressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        
        TableColumn<Client, Text> statusCol = new TableColumn<>("STATUS");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        
        TableColumn<Client, Text> nameCol = new TableColumn<>("NAME");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        
        TableColumn<Client, Text> totalCol = new TableColumn<>("REVENUE");
        totalCol.setCellValueFactory(new PropertyValueFactory<>("total"));
        
        TableColumn<Client, Text> phoneCol = new TableColumn<>("PHONE");
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));
        
        TableColumn<Client, Text> commentCol = new TableColumn<>("COMMENT");
        commentCol.setCellValueFactory(new PropertyValueFactory<>("comment"));
        
        this.table.getColumns().addAll(addressCol, statusCol, nameCol, totalCol, phoneCol, commentCol);
        
        //fill List
        try {
            
            Statement st = conn.createStatement();
            String query = "select summer_services.id, client_information.address, client_information.status, "
                    + "client_information.name, summer_services.total, client_information.phone, "
                    + "summer_services.comments, client_information.city "
                    + "from summer_services inner join client_information "
                    + "on summer_services.id = client_information.id and summer_services.season = '" + this.seasonId + "' "
                    + "order by client_information.address asc";
            ResultSet rs = st.executeQuery(query);
            
            this.tableList.clear();
            while (rs.next()){
                this.tableList.add(new Client(rs.getInt(1), rs.getString(2), rs.getInt(3), rs.getString(4), rs.getDouble(5), rs.getString(6), rs.getString(7), rs.getString(8)));
                
            }
            
        }
        catch (SQLException ex){
            System.out.println(ex.getMessage());
        }
        
        this.table.setItems(this.tableList);
        
        /*
        this.table.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2){
                TablePosition pos = this.table.getSelectionModel().getSelectedCells().get(0);
                int row = pos.getRow();
                Client client = this.table.getItems().get(row);
                
                this.clientMenu = new InformationMenu(this.conn, this.seasonId, client.getId());
                this.clientMenu.show();
            }
        });
        */
    }
    
    private void setBtnAction(){
        
        this.paymentTabBtn.setOnAction(e -> {
            
            if (!this.paymentMenuOpen){
                this.paymentMenu = new SummerPaymentMenu(this.conn, this.seasonId);
                this.paymentMenu.setOnCloseRequest(ex -> {this.paymentMenuOpen = false;});
                this.paymentMenuOpen = true;
                this.paymentMenu.show();
            }
            else
                this.paymentMenu.requestFocus();
        });
        
        this.addBatchBtn.setOnAction(e -> {
            System.out.println(this.addingNewClient);
            if (!this.addingNewClient){  
                addNewClient(true);
            }
            else
                this.newClientMenu.requestFocus();
        });
        
        this.addSingleBtn.setOnAction(e -> {
            System.out.println(this.addingNewClient);
            if (!this.addingNewClient){
                addNewClient(false);
            }
            else
                this.newClientMenu.requestFocus();
        });
        
    }
    
    public void setAddingNewClient(boolean addingNewClient){
        this.addingNewClient = addingNewClient;
    }
    
    public void addNewClient(boolean batch){
        if (batch){
            this.addingNewClient = true;
            this.newClientMenu = new NewClientMenu(this, this.conn, this.seasonId, true);
            this.newClientMenu.setOnCloseRequest(a -> {
                this.addingNewClient = false;
                System.out.println("Refresh Table");
                refreshTable();
            });
            this.newClientMenu.show();
            this.newClientMenu.requestFocus();
        }
        else{
            this.addingNewClient = true;
            this.newClientMenu = new NewClientMenu(this, this.conn, this.seasonId, false);
            this.newClientMenu.setOnCloseRequest(a -> {
                this.addingNewClient = false;
                System.out.println("Refresh Table");
                refreshTable();
            });
            this.newClientMenu.show();
            this.newClientMenu.requestFocus();
        }
    }
    
    private void resetSearchQuery(){
        this.searchQuery = "select summer_services.id, client_information.address, client_information.status, "
                    + "client_information.name, summer_services.total, client_information.phone, "
                    + "summer_services.comments, client_information.city "
                    + "from summer_services inner join client_information "
                    + "on summer_services.id = client_information.id and summer_services.season = '" + this.seasonId + "' ";
        
    }
    
    public void refreshTable(){
        resetSearchQuery();
        //setSearchQuery();
        
        this.tableList.clear();
        
        try {
            
            Statement st = this.conn.createStatement();
            ResultSet rs = st.executeQuery(this.searchQuery);
            
            this.tableList.clear();
            while (rs.next()){
                this.tableList.add(new Client(rs.getInt(1), rs.getString(2), rs.getInt(3), rs.getString(4), rs.getDouble(5), rs.getString(6), rs.getString(7), rs.getString(8)));
            }
            
        }catch (SQLException ex){
            System.out.println(ex.getMessage());
        }
    }
    
}
