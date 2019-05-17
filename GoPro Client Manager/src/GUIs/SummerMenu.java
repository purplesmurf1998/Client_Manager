/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUIs;

import InformationGUI.SummerInformationMenu;
import NewClientGUI.NewClientMenu;
import Objects.Client;
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
    
    private Connection conn;//Connection to the database
    private String seasonId;//seasonId chosen by user
    private NewClientMenu newClientMenu;//menu when adding new client
    private SummerPaymentMenu paymentMenu;//menu displaing payments of every client
    private SummerFilterMenu filterMenu;//menu for filtering the client services
    private PrintingMenu printingMenu;//menu for printing addresses
    
    /***********************
     AVAILABLE OPTIONS (BUTTONS ON THE RIGHT)
     ***********************/
    
    private Button filterBtn = new Button("Filter");//button to open the filter menu
    private Button printBtn = new Button("Print");//buton to open to printing menu
    private Button addBatchBtn = new Button("Add Batch");//button to open the new batch client menu
    private Button addSingleBtn = new Button("Add Single");//button to open the new single client menu
    private Button paymentMenuBtn = new Button("Payment");//button to open the payment menu
    
    private Button[] btnList = {filterBtn, printBtn, addBatchBtn, addSingleBtn, paymentMenuBtn};//list to hold all the menu buttons on the right pane
    
    /*************************
     * PANES FOR THE MENU
     *************************/
    
    private final VBox rightPane = new VBox();//this pane contains the option buttons
    private final BorderPane centerPane = new BorderPane();//this pane contains the tableview of clients and the search function
    
    /*************************
     * CLASS VARIABLES
     *************************/
    private final Insets insets = new Insets(5, 5, 5, 5);
    
    //values controlling when the different menus are open or not
    private boolean addingNewClient = false;
    private boolean paymentMenuActive = false;
    private boolean filterMenuActive = false;
    private boolean printMenuActive = false;
    
    private String filterQuery;//string that is used when querying through the database and filling up the tableList
    
    /*************************
     * CENTER PANE PROPERTIES
     *************************/
    private final HBox centerTopPane = new HBox();
    private final StackPane centerCenterPane = new StackPane();
    
    private final TextField searchBar = new TextField();//search bar to search for clients by name or by address
    private final ToggleButton searchAddressBtn = new ToggleButton("Address");//toggle button to toggle by address
    private final ToggleButton searchNameBtn = new ToggleButton("Name");//toggle button to toggle by name
    private final ToggleGroup searchGroup = new ToggleGroup();//toggle group for the toggle buttons so both can't be active simultaneously
    
//searchValue is for searching specific clients either by name or by address
    private boolean searchValue = false;//false = search, true = name 
    
    private final TableView<Client> table = new TableView<>();//tableview for displaying the clients
    private final ObservableList<Client> tableList = FXCollections.observableArrayList();//list that contains the clients to be displayed
    
    private Text numOfClientsText = new Text();//text displaying the number of clients being displayed in the tableview
    
    /**************************
     * VALUES FOR TABLE FILTER
     **************************/
    
    /*
    * These values are used for filtering through the services. When the filter menu is opened, these values
    * are passed as arguments so that the options can be initialized in the menu.
    * After the filtering the new values are saved here
    */
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
    
    ArrayList<Stage> informationList = new ArrayList<>();//Array of all the summerInformationMenus active so that all may be closed at the same time
    
    //Context menu for deleting a client
    private ContextMenu deleteMenu = new ContextMenu();//context menu to hold the delete client option of right click of a client
    private MenuItem deleteClientBtn = new MenuItem("Delete");//delete client option button
    
    //Alert for any errors
    private Alert menuAlert = new Alert(AlertType.CONFIRMATION);//alert popup for any user errors caught
    
    /**
     * Main constructor
     * @param conn
     * @param seasonId
     */
    public SummerMenu(Connection conn, String seasonId){
        this.conn = conn;
        this.seasonId = seasonId;
        
        this.filterQuery = "select summer_services.id, client_information.address, client_information.status, "
                    + "client_information.name, summer_payment.total, client_information.phone, "
                    + "summer_services.comments, client_information.city "
                    + "from summer_services "
                    + "inner join client_information on summer_services.id = client_information.id and summer_services.season = '" + this.seasonId + "' "
                    + "inner join summer_payment on summer_services.id = summer_payment.id and summer_payment.season = '" + this.seasonId + "' ";
        
        setRightPane();
        setCenterPane();
        
        setButtonAction();
        
        this.deleteMenu.getItems().add(this.deleteClientBtn);
        this.deleteMenu.setAutoHide(true);
        
        this.setRight(this.rightPane);
        this.setCenter(this.centerPane);
    }
    
    //set up the right pane containing the different menus
    private void setRightPane(){
        rightPane.setPadding(insets);
        rightPane.setAlignment(Pos.CENTER);
        rightPane.setSpacing(25);
        
        rightPane.getChildren().addAll(this.filterBtn, this.printBtn, this.addBatchBtn, this.addSingleBtn, this.paymentMenuBtn);
        
        for (int i = 0; i < btnList.length; i++){
            btnList[i].setMinSize(125, 50);
            btnList[i].setMaxSize(125, 50);
            btnList[i].setFont(Font.font("Rockwell", 18));
            btnList[i].setFocusTraversable(false);
        }
    }
    
    //set up the center pane containing the client search and tableview
    private void setCenterPane(){
        this.centerPane.setPadding(insets);
        
        this.centerPane.setTop(this.centerTopPane);
        this.centerPane.setCenter(this.centerCenterPane);
        
        //Set up the center top pane
        this.centerTopPane.setPadding(insets);
        this.centerTopPane.setSpacing(10);
        this.centerTopPane.setAlignment(Pos.CENTER_RIGHT);
        this.centerTopPane.getChildren().addAll(this.numOfClientsText, this.searchAddressBtn, this.searchNameBtn, this.searchBar);
        
        this.centerCenterPane.getChildren().add(this.table);
        setTableView();
        
        this.searchAddressBtn.setToggleGroup(this.searchGroup);
        this.searchNameBtn.setToggleGroup(this.searchGroup);
        this.searchAddressBtn.setSelected(true);
        
        setSearchBar();
    }
    
    //set the behavior of the search bar
    private void setSearchBar(){
        this.searchBar.setOnKeyReleased(e -> {
            if (!this.searchValue){//by address
                try {
                    resetFilterQuery();
                    
                    Statement st = this.conn.createStatement();
                    String search = this.searchBar.getText().toUpperCase();
                    
                    for (int i = 0; i < search.length(); i++){
                        if (search.charAt(i) == '\''){
                            search = search.substring(0, i) + "'" + search.substring(i, search.length());
                            i++;
                        }
                    }
                    
                    this.filterQuery += "and client_information.address LIKE '" + search + "%' ";
                    this.filterQuery += "order by client_information.door_number asc ";
                    
                    System.out.println(this.filterQuery);
                    
                    ResultSet rs = st.executeQuery(this.filterQuery);
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
                    resetFilterQuery();
                    
                    Statement st = this.conn.createStatement();
                    String search = this.searchBar.getText().toUpperCase();
                    
                    for (int i = 0; i < search.length(); i++){
                        if (search.charAt(i) == '\''){
                            search = search.substring(0, i) + "'" + search.substring(i, search.length());
                            i++;
                        }
                    }
                    
                    this.filterQuery += "and client_information.name LIKE '" + search + "%' ";
                    this.filterQuery += "order by client_information.door_number asc ";
                    
                    System.out.println(this.filterQuery);

                    
                    ResultSet rs = st.executeQuery(this.filterQuery);
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
    }
    
    //print the client's address that are being displayed in the tableview on a text file
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
    
    //set up the tableview that displays the client information
    private void setTableView(){
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
            this.filterQuery += "order by client_information.door_number asc";
            ResultSet rs = st.executeQuery(this.filterQuery);
            
            this.tableList.clear();
            while (rs.next()){
                this.tableList.add(new Client(rs.getInt(1), rs.getString(2), rs.getInt(3), rs.getString(4), rs.getDouble(5), rs.getString(6), rs.getString(7), rs.getString(8)));
                
            }
            
            resetFilterQuery();
            
        }
        catch (SQLException ex){
            System.out.println(ex.getMessage());
        }
        
        countClients();
        
        this.table.setItems(this.tableList);
        
        /*
        * Two things may happen on a mouse click
        * 1. Double click:
        *   On a double click, the information menu of the client selected is shown
        * 2. Right click:
        *   On right click, the context menu is shown with the option to delete a client
        */
        this.table.setOnMouseClicked(e -> {
            this.deleteMenu.hide();
            if (e.getClickCount() == 2){//check for double click
                try {
                    //get the client that was clicked on
                    TablePosition pos = this.table.getSelectionModel().getSelectedCells().get(0);
                    int row = pos.getRow();
                    Client client = this.table.getItems().get(row);
                    
                    //create the new information menu by passing the connection, seasonId, clientId selected and this class as arguments
                    SummerInformationMenu menu = new SummerInformationMenu(this.conn, this.seasonId, client.getId(), this);
                    
                    /*
                    * Add the newly created menu to the array so that if more than one has been opened at the same time,
                    * all of them can be closed simultaneously
                    */
                    this.informationList.add(menu);
                    final int index = this.informationList.size() - 1;
                    menu.setOnCloseRequest(ex -> {
                       this.informationList.remove(index);
                    });
                    //show the menu
                    menu.show();
                    
                }catch (IndexOutOfBoundsException ex){
                    System.out.println("Double clicked on no client");
                }
            }
            else if (e.getButton() == MouseButton.SECONDARY){//check for right click
                try {
                    //get the client that was clicked on
                    TablePosition pos = this.table.getSelectionModel().getSelectedCells().get(0);
                    int row = pos.getRow();
                    Client client = this.table.getItems().get(row);
                    
                    //display the context menu where the mouse click on the screen
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
    
    //delete the client passed as the argument from the database
    private void deleteClient(Client client) throws SQLException{
        Statement st = this.conn.createStatement();
        
        this.menuAlert.setTitle("Deleting Client");
        this.menuAlert.setHeaderText("You are about to delete a client, continue?");
        this.menuAlert.setContentText("By deleting this client, you are removing it from every list.\n"
                + "This includes past seasons, summer and winter. Are you sure to continue?");
        Optional<ButtonType> button = this.menuAlert.showAndWait();
        
        if (button.get() == ButtonType.OK){
        
            String deleteString = "delete from summer_services where id = " + client.getId();
        
            st.executeUpdate(deleteString);
        
            deleteString = "delete from summer_payment where id = " + client.getId();
        
            st.executeUpdate(deleteString);
            
            deleteString = "delete from winter_services where id = " + client.getId();
            
            st.executeUpdate(deleteString);
            
            deleteString = "delete from winter_payment where id = " + client.getId();
            
            st.executeUpdate(deleteString);
        
            deleteString = "delete from client_information where id = " + client.getId();
        
            st.executeUpdate(deleteString);
        
            st.close();
            this.refreshTable();
        }
    }
    
    //set up the action of every button on the pane
    private void setButtonAction(){
        
        /*
        PAYMENT MENU button:
        create a new payment menu and set paymentMenuActive to true.
        on close request, set paymentMenuActive to false.
        if payment menu already open, request focus to the current one.
        */
        this.paymentMenuBtn.setOnAction(e -> {
            
            if (!this.paymentMenuActive){
                this.paymentMenu = new SummerPaymentMenu(this.conn, this.seasonId);
                this.paymentMenu.setOnCloseRequest(ex -> {this.paymentMenuActive = false;});
                this.paymentMenuActive = true;
                this.paymentMenu.show();
            }
            else
                this.paymentMenu.requestFocus();
        });
        
        /*
        ADD BATCH button:
        add batch is used for adding multiple clients at once.
        when one client is entered, it continues prompting for more clients until user cancels.
        pass the value true as the argument to signify adding batch
        */
        this.addBatchBtn.setOnAction(e -> {
            if (!this.addingNewClient){  
                addNewClient(true);
            }
            else
                this.newClientMenu.requestFocus();
        });
        
        /*
        ADD SINGLE button:
        add single is used for adding one client at a time.
        when the client is entered, the menu closes.
        pass the value false as the argument to signify adding single
        */
        this.addSingleBtn.setOnAction(e -> {
            if (!this.addingNewClient){
                addNewClient(false);
            }
            else
                this.newClientMenu.requestFocus();
        });
        
        /*
        FILTER button:
        create a new SummerFilterMenu and pass the values of every service as arguments to initialize the stage.
        set the filterMenuActive to true.
        get the save button from the filter menu and set the action so that when activated, the list is filtered according to the
        settings activated
        the tablelist is passed as an argument so that the menu can filter the list when the saved button is pressed.
        */
        this.filterBtn.setOnAction(a -> {
            if (!this.filterMenuActive){
                this.filterMenuActive = true;
                this.filterMenu = new SummerFilterMenu(this.conn, this.seasonId, this.tableList, this.status, this.lawn, 
                                                       this.spring, this.fall, this.weedTreatment, this.aerationSpring, this.aerationFall, 
                                                       this.spiders, this.weeding, this.hedges, this.fertilizer, this.worms, this.soil, this.seeding);
                this.filterMenu.show();
                this.filterMenu.setOnCloseRequest(e -> {
                    this.filterMenuActive = false;
                });
                
                this.filterMenu.getSaveBtn().setOnAction(b -> {
                    //filter the list
                    this.filterMenu.filterList();
                    //update the filter setting for later searches
                    updateFilterSettings();
                    //update the query for later searches
                    updateFilterQuery();
                    
                    //reset the search bar text, close filter menu and update the number of clients displayed
                    this.searchBar.setText("");
                    this.filterMenu.close();
                    this.filterMenuActive = false;
                    countClients();
                });
            }
            else {
                this.filterMenu.requestFocus();
            }
        });
        
        /*
        PRINT button:
        create a new printing menu that prompts the user to enter the name of the new file.
        if a file with the same name already exists, the user is asked if he/she wishes to overwrite the exisiting file
        */
        this.printBtn.setOnAction(e -> {
            if (this.printMenuActive)
                this.printingMenu.requestFocus();
            else{
                this.printingMenu = new PrintingMenu();
                this.printMenuActive = true;
                this.printingMenu.show();
                
                this.printingMenu.getField().setOnKeyPressed(a -> {
                    if (a.getCode() == KeyCode.ENTER){
                        final String userHomeFolder = System.getProperty("user.home") + "/Desktop";
                        final String fileName = this.printingMenu.getField().getText();
                        
                        //create a new file with the name entered in the printing menu
                        File file = new File(userHomeFolder, fileName + ".txt");
                        //check if a file with the same name already exists 
                        if (file.exists()){
                            this.menuAlert.setTitle("File Already Exists");
                            this.menuAlert.setHeaderText("The file " + file.getName() + " already exists on the desktop");
                            this.menuAlert.setContentText("Pressing 'Ok' will overwrite the current text files saved on the computer.\n"
                                    + "If you do not wish for this to happen, press Cancel and enter a different name.");
                            Optional<ButtonType> button = this.menuAlert.showAndWait();
                            
                            //If the user presses the ok button, the existing file will be overwritten with the new information
                            if (button.get() == ButtonType.OK){
                                try{
                                    printAddresses(this.printingMenu.getField().getText());
                                }
                                catch (IOException ex){
                                    System.out.println(ex.getMessage());
                                    ex.printStackTrace();
                                }
                                this.printingMenu.close();
                                this.printMenuActive = false;
                            }
                        }
                        else {
                            try{
                                printAddresses(this.printingMenu.getField().getText());
                            }
                            catch (IOException ex){
                                System.out.println(ex.getMessage());
                                ex.printStackTrace();
                            }
                            this.printingMenu.close();
                            this.printMenuActive = false;
                        }
                        
                    }
                });
            }
        });
        
        /*
        SEARCH CLIENTS buttons:
        each button simply sets the search value so that the search bar may search accordingly
        */
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
    }
    
    //update the client filter settings when finsihed filtering
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
    
    //update the filter query to match the one used to filter the clients so that you can search the displayed clients only
    private void updateFilterQuery(){
        switch (this.status){
            case 0:{
                //do nothing, want all clients
            }break;
            case 1:{//search for residential
                this.filterQuery += "and client_information.status = 0 ";
            }break;
            case 2:{//serach for commercial
                this.filterQuery += "and client_information.status = 1 ";
            }break;
        }
        
        if (this.lawn)
            this.filterQuery += "and summer_services.lawn > 0 ";
        
        if (this.spring)
            this.filterQuery += "and summer_services.spring > 0 ";
        
        if (this.fall)
            this.filterQuery += "and summer_services.fall > 0 ";
        
        if (this.weedTreatment)
            this.filterQuery += "and summer_services.weed_treatment > 0 ";
        
        if (this.aerationSpring)
            this.filterQuery += "and summer_services.aeration_spring > 0 ";
        
        if (this.aerationFall)
            this.filterQuery += "and summer_services.aeration_fall > 0 ";
        
        if (this.spiders)
            this.filterQuery += "and summer_services.spider > 0 ";
        
        if (this.weeding)
            this.filterQuery += "and summer_services.weeding > 0 ";
        
        if (this.hedges)
            this.filterQuery += "and summer_services.hedges > 0 ";
        
        if (this.fertilizer)
            this.filterQuery += "and summer_services.fertilizer > 0 ";
        
        if (this.worms)
            this.filterQuery += "and summer_services.worms > 0 ";
        
        if (this.soil)
            this.filterQuery += "and summer_services.soil > 0 ";
        
        if (this.seeding)
            this.filterQuery += "and summer_services.seeding > 0 ";
        
    }
    
    //reset the filter query
    private void resetFilterQuery(){
        this.filterQuery = "select summer_services.id, client_information.address, client_information.status, "
                    + "client_information.name, summer_payment.total, client_information.phone, "
                    + "summer_services.comments, client_information.city "
                    + "from summer_services "
                    + "inner join client_information on summer_services.id = client_information.id and summer_services.season = '" + this.seasonId + "' "
                    + "inner join summer_payment on summer_services.id = summer_payment.id and summer_payment.season = '" + this.seasonId + "' ";
        updateFilterQuery();
    }
    
    //update the number of clients being displayed in the tableview
    private void countClients(){
        this.numOfClientsText.setText("Total Clients Displayed: " + this.tableList.size());
    }
    
    /**
     * method to set whether a client is being added or not. Used by the NewClientMenu class
     * @param addingNewClient
     */
    public void setAddingNewClient(boolean addingNewClient){
        this.addingNewClient = addingNewClient;
    }
    
    /**
     * creates a new NewClientMenu and passes as an argument whether it's by batch or single
     * @param batch
     */
    public void addNewClient(boolean batch){
        this.addingNewClient = true;
        this.newClientMenu = new NewClientMenu(this, this.conn, this.seasonId, batch);
        this.newClientMenu.setOnCloseRequest(a -> {
            this.addingNewClient = false;
            refreshTable();
        });
        this.newClientMenu.show();
        this.newClientMenu.requestFocus();
    }
    
    /**
     * refreshes the table so that database updates can be viewed
     */
    public void refreshTable(){
        resetFilterQuery();
        //setSearchQuery();
        this.filterQuery += "order by client_information.door_number asc ";
        
        this.tableList.clear();
        
        try {
            
            Statement st = this.conn.createStatement();
            ResultSet rs = st.executeQuery(this.filterQuery);
            
            this.tableList.clear();
            while (rs.next()){
                this.tableList.add(new Client(rs.getInt(1), rs.getString(2), rs.getInt(3), rs.getString(4), rs.getDouble(5), rs.getString(6), rs.getString(7), rs.getString(8)));
            }
            
        }catch (SQLException ex){
            System.out.println(ex.getMessage());
        }
        
        resetFilterQuery();
        countClients();
    }
    
}
