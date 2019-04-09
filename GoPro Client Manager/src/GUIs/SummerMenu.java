/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUIs;

import InformationGUI.SummerInformationMenu;
import NewClientGUI.NewClientMenu;
import Objects.Client;
import Objects.Printer;
import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Optional;
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
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
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
public class SummerMenu extends BorderPane{
    
    private Connection conn;
    private String seasonId;
    private NewClientMenu newClientMenu;
    private SummerPaymentMenu paymentMenu;
    private SummerFilterMenu filterMenu;
    private SummerInformationMenu clientMenu;
    
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
    
    private boolean lawn = false;
    private boolean spring = false;
    private boolean fall = false;
    private boolean weedTreatment = false;
    private boolean aerationSpring = false;
    private boolean aerationFall = false;
    private boolean spiders = false;
    private boolean weeding = false;
    private boolean hedges = false;
    private boolean fertilizer = false;
    private boolean worms = false;
    private boolean soil = false;
    private boolean seeding = false;
    private int status = 0;//0 = all, 1 = residential, 2 = commercial
    
    private int numOfClients = 0;
    private Text numOfClientsText = new Text();
    
    private String searchQuery;
    
    private boolean filterActive = false;
    
    ArrayList<Stage> informationList = new ArrayList<>();
    
    private ContextMenu deleteMenu = new ContextMenu();
    private MenuItem deleteClientBtn = new MenuItem("Delete");
    
    private Alert fileExistsAlert = new Alert(AlertType.CONFIRMATION);
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
        
        this.deleteMenu.getItems().add(this.deleteClientBtn);
        this.deleteMenu.setAutoHide(true);
        
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
        this.centerTopPane.getChildren().addAll(this.numOfClientsText, this.searchAddressBtn, this.searchNameBtn, this.searchBar);
        
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
                    resetSearchQuery();
                    
                    Statement st = this.conn.createStatement();
                    String search = this.searchBar.getText().toUpperCase();
                    
                    for (int i = 0; i < search.length(); i++){
                        if (search.charAt(i) == '\''){
                            search = search.substring(0, i) + "'" + search.substring(i, search.length());
                            i++;
                        }
                    }
                    
                    this.searchQuery += "and client_information.address LIKE '" + search + "%' ";
                    this.searchQuery += "order by client_information.door_number asc ";
                    
                    System.out.println(this.searchQuery);
                    
                    ResultSet rs = st.executeQuery(this.searchQuery);
                    this.tableList.clear();
                    while (rs.next()){
                        this.tableList.add(new Client(rs.getInt(1), rs.getString(2), rs.getInt(3), rs.getString(4), rs.getDouble(5), rs.getString(6), rs.getString(7), rs.getString(8)));
                        
                    }
                    
                    
                }catch (SQLException ex){
                    System.out.println(ex.getMessage());
                }
            }
            else {//by name
                try {
                    resetSearchQuery();
                    
                    Statement st = this.conn.createStatement();
                    String search = this.searchBar.getText().toUpperCase();
                    
                    for (int i = 0; i < search.length(); i++){
                        if (search.charAt(i) == '\''){
                            search = search.substring(0, i) + "'" + search.substring(i, search.length());
                            i++;
                        }
                    }
                    
                    this.searchQuery += "and client_information.name LIKE '" + search + "%' ";
                    this.searchQuery += "order by client_information.door_number asc ";
                    
                    System.out.println(this.searchQuery);

                    
                    ResultSet rs = st.executeQuery(this.searchQuery);
                    this.tableList.clear();
                    while (rs.next()){
                        this.tableList.add(new Client(rs.getInt(1), rs.getString(2), rs.getInt(3), rs.getString(4), rs.getDouble(5), rs.getString(6), rs.getString(7), rs.getString(8)));
                        
                    }
                    
                    
                }catch (SQLException ex){
                    System.out.println(ex.getMessage());
                }
            }
            countClients();
        });
        
        this.printBtn.setOnAction(e -> {
            final Printer printer = new Printer();
            printer.show();
                
            printer.getField().setOnKeyPressed(a -> {
                if (a.getCode() == KeyCode.ENTER){
                    final String userHomeFolder = System.getProperty("user.home") + "/Desktop";
                    final String fileName = printer.getField().getText();
                    
                    File file = new File(userHomeFolder, fileName + ".txt");
                    
                    if (file.exists()){
                        this.fileExistsAlert.setTitle("File Already Exists");
                        this.fileExistsAlert.setHeaderText("The file " + file.getName() + " already exists on the desktop");
                        this.fileExistsAlert.setContentText("Pressing 'Ok' will overwrite the current text files saved on the computer.\n"
                                + "If you do not wish for this to happen, press Cancel and enter a different name.");
                        Optional<ButtonType> button = this.fileExistsAlert.showAndWait();
                        
                        if (button.get() == ButtonType.OK){
                            printer.close();
                            try{
                                printAddresses(printer.getField().getText());
                            }
                            catch (IOException ex){
                                System.out.println(ex.getMessage());
                                ex.printStackTrace();
                            }
                        }
                    }
                    else {
                        printer.close();
                        try{
                            printAddresses(printer.getField().getText());
                        }
                        catch (IOException ex){
                            System.out.println(ex.getMessage());
                            ex.printStackTrace();
                        }
                    }
                        
                }
            }); 
        });
        
    }
    
    private void printAddresses(String fileName) throws IOException{
        
        String userHomeFolder = System.getProperty("user.home") + "/Desktop";
        File file = new File(userHomeFolder, fileName + ".txt");
        PrintWriter output = new PrintWriter(new FileOutputStream(file));
        
        String address = "";
        String city = "";
        String province = "QC";
        String country = "Canada";
        
        for (int i = 0; i < this.tableList.size(); i++){
            address = tableList.get(i).getAddress().getText();
            city = tableList.get(i).getCity();
            
            output.println(address + ", " + city + ", " + province + ", " + country);
        }
        
        output.close();
        
        Desktop.getDesktop().open(file);
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
        
        countClients();
        
        this.table.setItems(this.tableList);
        
        this.table.setOnMouseClicked(e -> {
            this.deleteMenu.hide();
            if (e.getClickCount() == 2){
                try {
                    TablePosition pos = this.table.getSelectionModel().getSelectedCells().get(0);
                    int row = pos.getRow();
                    Client client = this.table.getItems().get(row);
                
                    SummerInformationMenu menu = new SummerInformationMenu(this.conn, this.seasonId, client.getId(), this);
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
        
        String deleteString = "delete from summer_services where seasonid = '" + client.getId() + "" + this.seasonId + "'";
        
        st.executeUpdate(deleteString);
        
        deleteString = "delete from summer_payment where id = " + client.getId() + " and season = '" + this.seasonId + "'";
        
        st.executeUpdate(deleteString);
        
        st.close();
        this.refreshTable();
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
        
        this.filterBtn.setOnAction(a -> {
            if (!this.filterActive){
                this.filterActive = true;
                this.filterMenu = new SummerFilterMenu(this.conn, this.seasonId, this.tableList, this.status, this.lawn, 
                                                       this.spring, this.fall, this.weedTreatment, this.aerationSpring, this.aerationFall, 
                                                       this.spiders, this.weeding, this.hedges, this.fertilizer, this.worms, this.soil, this.seeding);
                this.filterMenu.show();
                this.filterMenu.setOnCloseRequest(e -> {
                    this.filterActive = false;
                });
                
                this.filterMenu.getSaveBtn().setOnAction(b -> {
                    updateFilterSettings();
                    
                    updateQuery();
                    
                    this.searchBar.setText("");
                    this.filterMenu.filterList();
                    this.filterMenu.close();
                    this.filterActive = false;
                    countClients();
                });
            }
            else {
                this.filterMenu.requestFocus();
            }
        });
        
    }
    
    private void updateFilterSettings(){
        this.status = this.filterMenu.getStatus();
        this.lawn = this.filterMenu.getLawn();
        this.spring = this.filterMenu.getSpring();
        this.fall = this.filterMenu.getFall();
        this.weedTreatment = this.filterMenu.getWeedTreatment();
        this.aerationSpring = this.filterMenu.getAerationSpring();
        this.aerationFall = this.filterMenu.getAerationFall();
        this.spiders = this.filterMenu.getSpider();
        this.weeding = this.filterMenu.getWeeding();
        this.hedges = this.filterMenu.getHedges();
        this.fertilizer = this.filterMenu.getFertilizer();
        this.worms = this.filterMenu.getWorms();
        this.soil = this.filterMenu.getSoil();
        this.seeding = this.filterMenu.getSeeding();
    }
    
    private void updateQuery(){
        switch (this.status){
            case 0:{
                //do nothing, want all clients
            }break;
            case 1:{//search for residential
                this.searchQuery += "and client_information.status = 0 ";
            }break;
            case 2:{//serach for commercial
                this.searchQuery += "and client_information.status = 1 ";
            }break;
        }
        
        if (this.lawn)
            this.searchQuery += "and summer_services.lawn > 0 ";
        
        if (this.spring)
            this.searchQuery += "and summer_services.spring > 0 ";
        
        if (this.fall)
            this.searchQuery += "and summer_services.fall > 0 ";
        
        if (this.weedTreatment)
            this.searchQuery += "and summer_services.weed_treatment > 0 ";
        
        if (this.aerationSpring)
            this.searchQuery += "and summer_services.aeration_spring > 0 ";
        
        if (this.aerationFall)
            this.searchQuery += "and summer_services.aeration_fall > 0 ";
        
        if (this.spiders)
            this.searchQuery += "and summer_services.spider > 0 ";
        
        if (this.weeding)
            this.searchQuery += "and summer_services.weeding > 0 ";
        
        if (this.hedges)
            this.searchQuery += "and summer_services.hedges > 0 ";
        
        if (this.fertilizer)
            this.searchQuery += "and summer_services.fertilizer > 0 ";
        
        if (this.worms)
            this.searchQuery += "and summer_services.worms > 0 ";
        
        if (this.soil)
            this.searchQuery += "and summer_services.soil > 0 ";
        
        if (this.seeding)
            this.searchQuery += "and summer_services.seeding > 0 ";
        
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
                    //+ "order by client_information.door_number asc";
        updateQuery();
    }
    
    public void refreshTable(){
        resetSearchQuery();
        //setSearchQuery();
        this.searchQuery += "order by client_information.door_number asc ";
        
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
        
        resetSearchQuery();
        countClients();
    }
    
    private void countClients(){
        this.numOfClients = 0;
        for (int i = 0; i < this.tableList.size(); i++){
            this.numOfClients++;
        }
        this.numOfClientsText.setText("Total Clients Displayed: " + this.numOfClients);
    }
    
}
