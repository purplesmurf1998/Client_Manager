/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUIs;

import InformationGUI.WinterInformationMenu;
import NewClientGUI.NewClientMenu;
import Objects.Client;
import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
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
public class WinterMenu extends BorderPane{
    
    private Connection conn;
    private String seasonId;
    private NewClientMenu newClientMenu;
    private WinterPaymentMenu paymentMenu;
    private WinterFilterMenu filterMenu;
    
    /*************************
     * AVAILABLE OPTIONS
     *************************/
    
    private final Button paymentTabBtn = new Button("Payments");
    private final Button printBtn = new Button("Print");
    private final Button addBatchBtn = new Button("Add Batch");
    private final Button addSingleBtn = new Button("Add Single");
    private final Button filterBtn = new Button("Filter");
    
    private final Button[] optionBtnList = {filterBtn, printBtn, addBatchBtn, addSingleBtn, paymentTabBtn};
    
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
    
    //filter parameters...use these to search in search bar
    private int serviceSelected = 0;
    private int shovelSelected = -1;
    private int statusSelected = 0;
    private int saltSelected = -1;
    private String searchQuery;
    
    private boolean filterActive = false;
    
    ArrayList<Stage> informationList = new ArrayList<>();
    
    private ContextMenu deleteMenu = new ContextMenu();
    private MenuItem deleteClientBtn = new MenuItem("Delete");
    
    public WinterMenu(){
        
    }
    
    public WinterMenu(Connection conn, String seasonId){
        this.conn = conn;
        this.seasonId = seasonId;
        
        this.deleteMenu.getItems().add(this.deleteClientBtn);
        this.deleteMenu.setAutoHide(true);
        
        this.searchQuery = "select winter_services.id, client_information.address, client_information.status, "
                    + "client_information.name, winter_services.total, client_information.phone, "
                    + "winter_services.comments, client_information.city "
                    + "from winter_services inner join client_information "
                    + "on winter_services.id = client_information.id and winter_services.season = '" + this.seasonId + "' ";
        
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
        
        for (int i = 0; i < optionBtnList.length; i++){
            optionBtnList[i].setMinSize(125, 50);
            optionBtnList[i].setMaxSize(125, 50);
            optionBtnList[i].setFont(Font.font("Rockwell", 18));
            optionBtnList[i].setFocusTraversable(false);
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
                    this.searchQuery += "order by client_information.door_number asc";
                    
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
                    this.searchQuery += "order by client_information.door_number asc";
                    
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
            String query = "select winter_services.id, client_information.address, client_information.status, "
                    + "client_information.name, winter_services.total, client_information.phone, "
                    + "winter_services.comments, client_information.city "
                    + "from winter_services inner join client_information "
                    + "on winter_services.id = client_information.id and winter_services.season = '" + this.seasonId + "' "
                    + "order by client_information.door_number asc";
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
        
        this.table.setOnMouseClicked(e -> {
            this.deleteMenu.hide();
            if (e.getClickCount() == 2){
                try {
                    TablePosition pos = this.table.getSelectionModel().getSelectedCells().get(0);
                    int row = pos.getRow();
                    Client client = this.table.getItems().get(row);
                
                    WinterInformationMenu menu = new WinterInformationMenu(this.conn, this.seasonId, client.getId(), this);
                    this.informationList.add(menu);
                    final int index = this.informationList.size() - 1;
                    menu.setOnCloseRequest(ex -> {
                       this.informationList.remove(index);
                    });
                    menu.show();
                }catch (IndexOutOfBoundsException ex){
                    System.out.println("Double clicked on no client");
                }
            }
            else if (e.getButton() == MouseButton.SECONDARY){
                try {    
                    TablePosition pos = this.table.getSelectionModel().getSelectedCells().get(0);
                    int row = pos.getRow();
                    Client client = this.table.getItems().get(row);
                
                    this.deleteMenu.show(this.table, e.getScreenX(), e.getScreenY());
                    this.deleteMenu.requestFocus();
                    this.deleteClientBtn.setOnAction(a -> {
                        try {
                            deleteClient(client);
                        }catch (SQLException ex2){
                            System.out.println(ex2.getMessage());
                        }
                    });
                    
                }catch (IndexOutOfBoundsException ex){
                    System.out.println("Right clicked on no client");
                }
            }
        });
    }
    
    private void deleteClient(Client client) throws SQLException{
        Statement st = this.conn.createStatement();
        
        String deleteString = "delete from winter_services where seasonid = '" + client.getId() + "" + this.seasonId + "'";
        
        st.executeUpdate(deleteString);
        
        deleteString = "delete from winter_payment where id = " + client.getId() + " and season = '" + this.seasonId + "'";
        
        st.executeUpdate(deleteString);
        
        st.close();
        this.refreshTable();
    }
    
    private void setBtnAction(){
        
        
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
        
        this.paymentTabBtn.setOnAction(e -> {
            
            if (!this.paymentMenuOpen){
                this.paymentMenu = new WinterPaymentMenu(this.conn, this.seasonId);
                this.paymentMenu.setOnCloseRequest(ex -> {this.paymentMenuOpen = false;});
                this.paymentMenuOpen = true;
                this.paymentMenu.show();
            }
            else
                this.paymentMenu.requestFocus();
        });
        
        this.filterBtn.setOnAction(e -> {
            if (!this.filterActive){
                this.filterActive = true;
                this.filterMenu = new WinterFilterMenu(this.conn, this.seasonId, this.tableList, this.serviceSelected, this.shovelSelected, this.statusSelected, this.saltSelected);
                this.filterMenu.show();
                
                this.filterMenu.getSaveBtn().setOnAction(a -> {
                    //updateFilterSettings()
                    this.serviceSelected = this.filterMenu.getServiceSelected();
                    this.shovelSelected = this.filterMenu.getShovelSelected();
                    this.statusSelected = this.filterMenu.getStatusSelected();
                    this.saltSelected = this.filterMenu.getSaltSelected();
                    
                    updateSearchQuery();
                    
                    this.searchBar.setText("");
                    this.filterMenu.filterList();
                    this.filterMenu.close();
                    this.filterActive = false;
                });
                
                this.filterMenu.setOnCloseRequest(b -> {
                    this.filterActive = false;
                });
            }
            else
                this.filterMenu.requestFocus();
        });
        
        this.printBtn.setOnAction(e -> {
            try {
                PrintWriter writer = new PrintWriter(new FileOutputStream("Winter_Client_List.txt"));
                for (int i = 0; i < this.tableList.size(); i++){
                    writer.println(this.tableList.get(i).getAddress().getText() + " " + this.tableList.get(i).getCity() + "\n" + this.tableList.get(i).getComment().getText() + "\n");
                    
                }
                System.out.println("Addresses printed");
                writer.close();
                
                File file = new File("Winter_Client_List.txt");
                Desktop desktop = Desktop.getDesktop();
                desktop.open(file);
                
            } catch (IOException ex) {
                Logger.getLogger(WinterMenu.class.getName()).log(Level.SEVERE, null, ex);
            } 
        });
        
    }
    private void updateSearchQuery(){
        switch (this.serviceSelected){
                case 0: {
                    //don't do anything...show all services
                }break;
                case 1: {
                    this.searchQuery += "and winter_services.single > 0 ";
                }break;
                case 2: {
                    this.searchQuery += "and winter_services.double > 0 ";
                }break;
                case 3: {
                    this.searchQuery += "and winter_services.triple > 0 ";
                }break;
                case 4: {
                    this.searchQuery += "and winter_services.other > 0 ";
                }break;
            }
        
            switch (this.shovelSelected){
                case -1: {
                    //don't add anything...want with shovel AND without shovel
                }break;
                case 0: {
                    this.searchQuery += "and winter_services.shovel = 0 ";
                }break;
                case 1: {
                    this.searchQuery += "and winter_services.shovel > 0 ";
                }break;
            }
            
            switch (this.saltSelected){
                case -1: {
                    //don't add anything...want with salt AND without salt
                }break;
                case 0: {
                    this.searchQuery += "and winter_services.salt = 0 ";
                }break;
                case 1: {
                    this.searchQuery += "and winter_services.salt > 0 ";
                }break;
            }
        
            switch (this.statusSelected){
                case 0: {
                    //don't add anything...want residential AND commercial
                }break;
                case 1: {
                    this.searchQuery += "and client_information.status = 0 ";
                }break;
                case 2: {
                    this.searchQuery += "and client_information.status = 1 ";
                }break;
            }
            
            
    }
    private void resetSearchQuery(){
        this.searchQuery = "select winter_services.id, client_information.address, client_information.status, "
                    + "client_information.name, winter_services.total, client_information.phone, "
                    + "winter_services.comments, client_information.city "
                    + "from winter_services inner join client_information "
                    + "on winter_services.id = client_information.id and winter_services.season = '" + this.seasonId + "' "
                    + "order by client_information.door_number asc";
        
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
    
    public void closeAllStages(){
        if (this.filterActive)
            this.filterMenu.close();
        
        if (this.paymentMenuOpen)
            this.paymentMenu.close();
        
        if (this.addingNewClient)
            this.newClientMenu.close();
        
        if (this.informationList.size() > 0)
            for (int i = 0; i < this.informationList.size(); i++)
                this.informationList.get(i).close();
        
        this.informationList.clear();
    }
    
    public void refreshTable(){
        resetSearchQuery();
        updateSearchQuery();
        
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
