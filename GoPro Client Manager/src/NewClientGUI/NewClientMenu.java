/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package NewClientGUI;

import GUIs.SummerMenu;
import GUIs.WinterMenu;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 *
 * @author purplesmurf
 */
public class NewClientMenu extends Stage{
    
    private Connection conn;
    private String seasonId;
    private boolean batchAdd;//specified if clients are added as batch or individually
    private WinterMenu winterMenu;
    private SummerMenu summerMenu;
    private int id;
    
    private int centerPaneCounter = 0;
    
    private Scene scene;
    
    private BorderPane mainPane = new BorderPane();
    
    private ClientInfoMenu infoPage;
    private WinterServiceMenu winterServiceMenu;
    private WinterPaymentMenu winterPaymentMenu;
    private SummerServiceMenu summerServiceMenu;
    private SummerPaymentMenu summerPaymentMenu;
    
    private final HBox topPane = new HBox();
    private final Button backBtn = new Button("Cancel");
    private final Button nextBtn = new Button("Save");
    
    /***************************
     * INFO PAGE NODES
     ***************************/
    
    private boolean errorDontSave = false;
    
    private final Alert saveAlert = new Alert(AlertType.CONFIRMATION);
    
    
    public NewClientMenu(){
        
    }
    
    public NewClientMenu(WinterMenu winterMenu, Connection conn, String seasonId, boolean batchAdd){
        this.conn = conn;
        this.seasonId = seasonId;
        this.batchAdd = batchAdd;
        this.winterMenu = winterMenu;
        
        setUpTopPane();
        
        this.infoPage = new ClientInfoMenu(this.conn);
        this.winterServiceMenu = new WinterServiceMenu(this.conn);
        this.winterPaymentMenu = new WinterPaymentMenu(this.conn);
        
        mainPane.setTop(this.topPane);
        mainPane.setCenter(this.infoPage);
        
        this.scene = new Scene(mainPane, 800, 500);
        this.setScene(this.scene);
        
    }
    
    public NewClientMenu(SummerMenu summerMenu, Connection conn, String seasonId, boolean batchAdd){
        this.conn = conn;
        this.seasonId = seasonId;
        this.batchAdd = batchAdd;
        this.summerMenu = summerMenu;
        
        setUpTopPane();
        
        this.infoPage = new ClientInfoMenu(this.conn);
        this.summerServiceMenu = new SummerServiceMenu(this.conn);
        this.summerPaymentMenu = new SummerPaymentMenu(this.conn);
        
        mainPane.setTop(this.topPane);
        mainPane.setCenter(this.infoPage);
        
        this.scene = new Scene(mainPane, 800, 500);
        this.setScene(this.scene);
    }
    
    private void setUpTopPane(){
        this.topPane.setPadding(new Insets(10, 10, 10, 10));
        this.topPane.setAlignment(Pos.CENTER);
        this.topPane.setSpacing(500);
        
        this.topPane.getChildren().addAll(this.backBtn, this.nextBtn);
        
        this.backBtn.setMinSize(125, 50);
        this.backBtn.setMaxSize(125, 50);
        this.backBtn.setFont(Font.font("Rockwell", 18));
        this.backBtn.setFocusTraversable(false);
        
        this.nextBtn.setMinSize(125, 50);
        this.nextBtn.setMaxSize(125, 50);
        this.nextBtn.setFont(Font.font("Rockwell", 18));
        this.nextBtn.setFocusTraversable(false);
        
        this.nextBtn.setOnAction(e -> {
            if (this.centerPaneCounter == 0){
                this.saveAlert.setTitle("Saving Client");
                this.saveAlert.setHeaderText("Is the information correct?");
                this.saveAlert.setContentText("You are about to update an existing client\n"
                        + "or create a new one. Make sure the\n"
                        + "information entered is correct, and press 'Ok'.");
                Optional<ButtonType> button = this.saveAlert.showAndWait();
                
                if (button.get() == ButtonType.OK){
                    saveClient();
                }
            }
            else
                switchPage(1);
        });
        
        this.backBtn.setOnAction(e -> {
            switchPage(-1);
        });
    }
    
    private void switchPage(int value){
        switch(this.centerPaneCounter){
            case 0: {
                
                if (value > 0){//switch to services page
                    this.centerPaneCounter++;
                    if (seasonId.charAt(0) == 'W'){
                        //switch to winter service
                        this.nextBtn.setText("Next");
                        this.mainPane.setCenter(this.winterServiceMenu);
                    }
                    else if (seasonId.charAt(0) == 'S'){
                        //switch to summer service
                        this.nextBtn.setText("Next");
                        this.mainPane.setCenter(this.summerServiceMenu);
                    }
                    this.backBtn.setText("Back");
                }
                else{//close add new client page
                    if (seasonId.charAt(0) == 'W'){
                        this.winterMenu.setAddingNewClient(false);
                        this.close();
                    }
                    else if (seasonId.charAt(0) == 'S'){
                        this.summerMenu.setAddingNewClient(false);
                        this.close();
                    }
                }
                
            }break;
            case 1: {
                
                if (value > 0){//switch to payment page
                    this.centerPaneCounter++;
                    if (seasonId.charAt(0) == 'W'){
                        //switch to winter payment
                        this.mainPane.setCenter(this.winterPaymentMenu);
                        this.winterPaymentMenu.refreshPane();
                        this.winterPaymentMenu.setTotal(this.winterServiceMenu.getTotal());
                    }
                    else if (seasonId.charAt(0) == 'S'){
                        //switch to summer payment
                        this.mainPane.setCenter(this.summerPaymentMenu);
                        this.summerPaymentMenu.refreshPane();
                        this.summerPaymentMenu.setTotal(this.summerServiceMenu.getTotal());
                        
                    }
                    this.nextBtn.setText("Save");
                }
                else {//switch to info page
                    this.centerPaneCounter--;
                    this.mainPane.setCenter(this.infoPage);
                    this.backBtn.setText("Cancel");
                    this.nextBtn.setText("Save");
                }
                
            }break;
            case 2: {
                
                if (value > 0){//save and close add new client page
                    /*if (this.batchAdd && this.seasonId.charAt(0) == 'W'){
                        //save new client method('W');
                        
                        this.infoPage = new ClientInfoMenu(this.conn);
                        this.winterServiceMenu = new WinterServiceMenu(this.conn);
                        this.winterPaymentMenu = new WinterPaymentMenu(this.conn);
                        this.centerPaneCounter = 0;
                        this.mainPane.setCenter(this.infoPage);
                    }*/
                    
                    //Get All Information
                    if (seasonId.charAt(0) == 'W')
                        updateWinter();
                    else if (seasonId.charAt(0) == 'S')
                        updateSummer();
                    
                }
                else {//switch to services page
                    if (seasonId.charAt(0) == 'W'){
                        this.centerPaneCounter--;
                        this.mainPane.setCenter(this.winterServiceMenu);
                        this.nextBtn.setText("Next");
                    }
                    else if (seasonId.charAt(0) == 'S'){
                        this.centerPaneCounter--;
                        this.mainPane.setCenter(this.summerServiceMenu);
                        this.nextBtn.setText("Next");
                    }
                }
                
            }break;
            default:
        }
    }
    private void saveClient(){
        String address = this.infoPage.getClientAddress();
        String name = this.infoPage.getClientName();
        String phone = this.infoPage.getClientPhone();
        String email = this.infoPage.getClientEmail();
        String city = this.infoPage.getClientCity();
        int status = this.infoPage.getClientStatus();
        int id = this.infoPage.getClientId();
        String season_id = this.id + this.seasonId;
        
        //Check if already added for season
        
        
        if (address.length() <= 0){
            this.saveAlert.setTitle("Client Save Error");
            this.saveAlert.setHeaderText("Address field is empty.");
            this.saveAlert.setContentText("Address field must have a valid address\n"
                    + "for a client to be saved");
            this.saveAlert.show();
        }
        else{
            try {
                
                for (int i = 0; i < address.length(); i++){
                        if (address.charAt(i) == '\''){
                            address = address.substring(0, i) + "'" + address.substring(i, address.length());
                            i++;
                        }
                }
                            
                Statement st = this.conn.createStatement();
                            
                String update;
                            
                //Update client_information
                if (id < 0){//add new client
                    update = "insert into client_information(address, city, name, phone, email, status)"
                            + " values ('" + address.toUpperCase() + "', '" + city.toUpperCase() + "', '" + name.toUpperCase() + "', '" + phone.toUpperCase() + "', '" + email.toUpperCase() + "', " + status + ")";
                                
                    st.executeUpdate(update);
                    update = "select id from client_information where LOWER(address) = '" + address.toLowerCase() + "'";
                    ResultSet rs = st.executeQuery(update);
                    
                    while (rs.next())
                        this.id = rs.getInt(1);
                    
                    System.out.println(this.id);
                    switchPage(1);
                }
                else{//update pre-existing client
                    update = "update client_information "
                            + "set (address, city, name, phone, email, status)"
                            + " = ('" + address.toUpperCase() + "', '" + city.toUpperCase() + "', '" + name.toUpperCase() + "', '" + phone.toUpperCase() + "', '" + email.toUpperCase() + "', " + status + ") "
                            + "where id = " + id;
                                
                    st.executeUpdate(update);
                    this.id = id;
                    System.out.println(this.id);
                    switchPage(1);
                }
            } catch(SQLException ex){
                System.out.println(ex.getMessage());
                this.saveAlert.setTitle("Client Save Error");
                this.saveAlert.setHeaderText("Client already exists.");
                this.saveAlert.setContentText("You have entered an existing address.\n"
                        + "Please select or delete existing client before saving.");
                this.saveAlert.show();
            }
            
        }
    }
    private void updateSummer(){
        double lawn = this.summerServiceMenu.getLawn();
        double spring = this.summerServiceMenu.getSpring();
        double fall = this.summerServiceMenu.getFall();
        double weedTreatment = this.summerServiceMenu.getWeedTreatment();
        double aerationSpring = this.summerServiceMenu.getAerationSpring();
        double aerationFall = this.summerServiceMenu.getAerationFall();
        double spider = this.summerServiceMenu.getSpider();
        double weeding = this.summerServiceMenu.getWeeding();
        double hedges = this.summerServiceMenu.getHedges();
        double fertilizer = this.summerServiceMenu.getFertilizer();
        double worms = this.summerServiceMenu.getWorms();
        double soil = this.summerServiceMenu.getSoil();
        double seeding = this.summerServiceMenu.getSeeding();
        String serviceComment = this.summerServiceMenu.getComment();
        String season_id = this.id + this.seasonId;
        
        int plan = this.summerPaymentMenu.getPlan();
        int mar = this.summerPaymentMenu.getMar();
        int apr = this.summerPaymentMenu.getApr();
        int may = this.summerPaymentMenu.getMay();
        int jun = this.summerPaymentMenu.getJun();
        int jul = this.summerPaymentMenu.getJul();
        int aug = this.summerPaymentMenu.getAug();
        int sep = this.summerPaymentMenu.getSep();
        int oct = this.summerPaymentMenu.getOct();
        int method = this.summerPaymentMenu.getMethod();
        double total = this.summerPaymentMenu.getTotal();
        String paymentComment = this.summerPaymentMenu.getComments();
        double save = this.summerPaymentMenu.getSave();
        
        try {
            
            Statement st = this.conn.createStatement();
            
            String update;
            
            for (int i = 0; i < serviceComment.length(); i++){
                if (serviceComment.charAt(i) == '\''){
                    serviceComment = serviceComment.substring(0, i) + "'" + serviceComment.substring(i, serviceComment.length());
                    i++;
                }
            }
            
            for (int i = 0; i < paymentComment.length(); i++){
                if (paymentComment.charAt(i) == '\''){
                    paymentComment = paymentComment.substring(0, i) + "'" + paymentComment.substring(i, paymentComment.length());
                    i++;
                }
            }
            
            if (save > 0) {
                paymentComment += "\t--Client saved " + save + "%";
            }
            
            //Update summer services
            update = "INSERT INTO summer_services "
                    + "VALUES (" + this.id + ", '" + this.seasonId + "', " 
                    + lawn + ", " + spring + ", " + fall + ", " + weedTreatment + ", " 
                    + aerationSpring + ", " + aerationFall + ", " + spider + ", "  + weeding + ", "  + hedges + ", " 
                    + fertilizer + ", " + worms + ", "  + soil + ", "  + seeding + ", "    
                    + total + ", '" + serviceComment + "', '" + season_id.toUpperCase() + "')";
            System.out.println(update);
            
            st.executeUpdate(update);
            
            //update summer payment
            update = "INSERT INTO summer_payment "
                    + "VALUES (" + this.id + ", '" + this.seasonId + "', " + total + ", " + plan + ", " 
                    + mar + ", " + apr + ", " + may + ", " + jun + ", " + jul + ", " + aug + ", " + sep + ", " + oct + ", "
                    + method + ", '" + paymentComment + "')";
            System.out.println(update);
            
            st.executeUpdate(update);
            this.errorDontSave = false;
        }
        catch (SQLException ex){
            this.errorDontSave = true;
            System.out.println(ex.getMessage());
            
            this.saveAlert.setTitle("Client Error");
            this.saveAlert.setHeaderText("Client already registered.");
            this.saveAlert.setContentText("This client has already been registered for the \n"
                    + "season " + this.seasonId + ". Modify or choose new client.");
            this.saveAlert.show();
        }
        
        if (!this.errorDontSave){
            if (this.batchAdd)
                this.summerMenu.addNewClient(true);
            else
                this.summerMenu.setAddingNewClient(false);
            
            this.summerMenu.refreshTable();
            this.close();
            
        }
    }
    private void updateWinter(){
                    
        double single = this.winterServiceMenu.getSingle();
        double doubleW = this.winterServiceMenu.getDouble();
        double triple = this.winterServiceMenu.getTriple();
        double other = this.winterServiceMenu.getOther();
        double shovel = this.winterServiceMenu.getShovel();
        double salt = this.winterServiceMenu.getSalt();
        double total = this.winterServiceMenu.getTotal();
        String serviceComment = this.winterServiceMenu.getComment();
        String season_id = this.id + this.seasonId;
                    
        int plan = this.winterPaymentMenu.getPlan();
        int oct = this.winterPaymentMenu.getOct();
        int nov = this.winterPaymentMenu.getNov();
        int dec = this.winterPaymentMenu.getDec();
        int jan = this.winterPaymentMenu.getJan();
        int feb = this.winterPaymentMenu.getFeb();
        int method = this.winterPaymentMenu.getMethod();
        String paymentComment = this.winterPaymentMenu.getComment();               
        
        try {
            
            Statement st = this.conn.createStatement();
            
            String update;
            
            for (int i = 0; i < serviceComment.length(); i++){
                if (serviceComment.charAt(i) == '\''){
                    serviceComment = serviceComment.substring(0, i) + "'" + serviceComment.substring(i, serviceComment.length());
                    i++;
                }
            }
            
            for (int i = 0; i < paymentComment.length(); i++){
                if (paymentComment.charAt(i) == '\''){
                    paymentComment = paymentComment.substring(0, i) + "'" + paymentComment.substring(i, paymentComment.length());
                    i++;
                }
            }
            
            //Update winter services
            update = "INSERT INTO winter_services "
                    + "VALUES (" + this.id + ", '" + this.seasonId + "', " 
                    + single + ", " + doubleW + ", " + triple + ", " + other + ", " 
                    + shovel + ", " + salt + ", " + total + ", '" + serviceComment + "', '" + season_id.toUpperCase() + "')";
            System.out.println(update);
            
            st.executeUpdate(update);
            
            //update winter payment
            update = "INSERT INTO winter_payment "
                    + "VALUES (" + this.id + ", '" + this.seasonId + "', " + total + ", " + plan + ", " 
                    + oct + ", " + nov + ", " + dec + ", " + jan + ", " + feb + ", " + method + ", '" + paymentComment + "')";
            System.out.println(update);
            
            st.executeUpdate(update);
            this.errorDontSave = false;
        }
        catch (SQLException ex){
            this.errorDontSave = true;
            System.out.println(ex.getMessage());
            
            this.saveAlert.setTitle("Client Error");
            this.saveAlert.setHeaderText("Client already registered.");
            this.saveAlert.setContentText("This client has already been registered for the \n"
                    + "season " + this.seasonId + ". Modify or choose new client.");
            this.saveAlert.show();
        }
            
        if (!this.errorDontSave){
            if (this.batchAdd)
                this.winterMenu.addNewClient(true);
            else
                this.winterMenu.setAddingNewClient(false);
            
            this.winterMenu.refreshTable();
            this.close();
            
        }                
    }
    
    
    public void setBatchAdd(boolean batchAdd){
        this.batchAdd = batchAdd;
        
    }
    
    
    
}
