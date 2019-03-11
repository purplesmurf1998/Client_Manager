/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUIs;

import Objects.Client;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 *
 * @author purplesmurf
 */
public class WinterFilterMenu extends Stage{
    
    private Connection conn;
    private ObservableList<Client> tableList;
    private String seasonId;
    
    private Scene scene;
    private final BorderPane pane = new BorderPane();
    private final GridPane centerPane = new GridPane();
    private final HBox topPane = new HBox();
    
    private int serviceSelected;//0 = all, 1 = single, 2 = double, 3 = triple, 4 = other
    private int shovelSelected;//-1 = null shows all clients, 0 = without shovel, 1 = with shovel
    private int statusSelected;//0 = null shows all clients, 1 = residential, 2 = commercial
    private int saltSelected;//-1 = null shows all client, 0 = without salt, 1 = with salt
    
    /****************************
     * FILTER BUTTONS
     ****************************/
    
    private final ToggleButton allClientsBtn = new ToggleButton("All Clients");
    
    private final ToggleButton singleBtn = new ToggleButton("Single");
    private final ToggleButton doubleBtn = new ToggleButton("Double");
    private final ToggleButton tripleBtn = new ToggleButton("Triple");
    private final ToggleButton otherBtn = new ToggleButton("Other");
    
    private final ToggleGroup serviceGroup = new ToggleGroup();
    
    private final ToggleButton shovelBtn = new ToggleButton("Shovel");
    private final ToggleButton noShovelBtn = new ToggleButton("No Shovel");
    
    private final ToggleGroup shovelGroup = new ToggleGroup();
    
    private final ToggleButton resBtn = new ToggleButton("Residential");
    private final ToggleButton comBtn = new ToggleButton("Commercial");
    
    private final ToggleGroup statusGroup = new ToggleGroup();
    
    private final ToggleButton saltBtn = new ToggleButton("Salt");
    private final ToggleButton noSaltBtn = new ToggleButton("No Salt");
    
    private final ToggleGroup saltGroup = new ToggleGroup();
    
    private final ToggleButton[] btnList = {allClientsBtn, singleBtn, doubleBtn, tripleBtn, otherBtn, shovelBtn, noShovelBtn, resBtn, comBtn, saltBtn, noSaltBtn};
    
    private final Button saveBtn = new Button("Save");
    
    private Text filterString = new Text();
    private String allClientsString = "";
    private String serviceString = "";
    private String shovelString = "";
    private String statusString = "";
    private String saltString = "";
    
    /*****************************
     * QUERY VARIABLES
     *****************************/
    private String query;
    
    public WinterFilterMenu(Connection conn, String seasonId, ObservableList<Client> tableList, int serviceSelected, int shovelSelected, int statusSelected, int saltSelected){
        this.conn = conn;
        this.tableList = tableList;
        this.seasonId = seasonId;
        this.serviceSelected = serviceSelected;
        this.shovelSelected = shovelSelected;
        this.statusSelected = statusSelected;
        this.saltSelected = saltSelected;
        
        query = "select winter_services.id, client_information.address, client_information.status, "
                    + "client_information.name, winter_services.total, client_information.phone, "
                    + "winter_services.comments ,client_information.city "
                    + "from winter_services inner join client_information "
                    + "on winter_services.id = client_information.id and winter_services.season = '" + this.seasonId + "' ";
        
        
        
        setStage();
        
    }
    
    private void setStage(){
        this.scene = new Scene(this.pane, 450, 200);
        this.setScene(this.scene);
        
        this.pane.setTop(this.topPane);
        
        this.topPane.setPadding(new Insets(10, 10, 10, 10));
        this.topPane.setSpacing(10);
        this.topPane.setAlignment(Pos.CENTER_LEFT);
        this.topPane.getChildren().add(this.filterString);
        
        this.filterString.setFont(Font.font("Gotham", 18));
        
        this.pane.setCenter(this.centerPane);
        
        this.centerPane.setPadding(new Insets(10, 10, 10, 10));
        this.centerPane.setAlignment(Pos.CENTER);
        this.centerPane.setVgap(10);
        this.centerPane.setHgap(10);
        
        this.centerPane.add(this.allClientsBtn, 0, 0);
        this.centerPane.add(this.singleBtn, 1, 0);
        this.centerPane.add(this.doubleBtn, 1, 1);
        this.centerPane.add(this.tripleBtn, 1, 2);
        this.centerPane.add(this.otherBtn, 1, 3);
        this.centerPane.add(this.shovelBtn, 2, 0);
        this.centerPane.add(this.noShovelBtn, 2, 1);
        this.centerPane.add(this.saltBtn, 3, 0);
        this.centerPane.add(this.noSaltBtn, 3, 1);
        this.centerPane.add(this.resBtn, 4, 0);
        this.centerPane.add(this.comBtn, 4, 1);
        this.centerPane.add(this.saveBtn, 4, 3);
        
        for (int i = 0; i < this.btnList.length; i++){
            this.btnList[i].setFocusTraversable(false);
        }
        
        this.singleBtn.setToggleGroup(this.serviceGroup);
        this.doubleBtn.setToggleGroup(this.serviceGroup);
        this.tripleBtn.setToggleGroup(this.serviceGroup);
        this.otherBtn.setToggleGroup(this.serviceGroup);
        
        this.shovelBtn.setToggleGroup(this.shovelGroup);
        this.noShovelBtn.setToggleGroup(this.shovelGroup);
        
        this.resBtn.setToggleGroup(this.statusGroup);
        this.comBtn.setToggleGroup(this.statusGroup);
        
        this.saltBtn.setToggleGroup(this.saltGroup);
        this.noSaltBtn.setToggleGroup(this.saltGroup);
        
        setButtons();
        
        this.allClientsBtn.setOnMouseReleased(e -> {
            if (this.allClientsBtn.isSelected()){
                this.singleBtn.setSelected(false);
                this.doubleBtn.setSelected(false);
                this.tripleBtn.setSelected(false);
                this.otherBtn.setSelected(false);
                
                this.shovelBtn.setSelected(false);
                this.noShovelBtn.setSelected(false);
                
                this.resBtn.setSelected(false);
                this.comBtn.setSelected(false);
                
                this.saltBtn.setSelected(false);
                this.noSaltBtn.setSelected(false);
                
                this.allClientsString = "All Clients ";
                this.serviceString = "";
                this.shovelString = "";
                this.statusString = "";
                this.saltString = "";
                this.serviceSelected = 0;
                this.shovelSelected = -1;
                this.statusSelected = 0;
                this.saltSelected = -1;
            }
            else {
                this.allClientsBtn.setSelected(true);
                 this.singleBtn.setSelected(false);
                this.doubleBtn.setSelected(false);
                this.tripleBtn.setSelected(false);
                this.otherBtn.setSelected(false);
                
                this.shovelBtn.setSelected(false);
                this.noShovelBtn.setSelected(false);
                
                this.resBtn.setSelected(false);
                this.comBtn.setSelected(false);
                
                this.saltBtn.setSelected(false);
                this.noSaltBtn.setSelected(false);
                
                this.allClientsString = "All Clients ";
                this.serviceString = "";
                this.shovelString = "";
                this.statusString = "";
                this.saltString = "";
                this.serviceSelected = 0;
                this.shovelSelected = -1;
                this.statusSelected = 0;
                this.saltSelected = -1;
            }
            this.filterString.setText(this.allClientsString + this.serviceString + this.shovelString + this.saltString + this.statusString);
            System.out.println(this.scene.getWidth() + " " + this.scene.getHeight());
            System.out.println(this.serviceSelected + ", " + this.shovelSelected + ", " + this.statusSelected);
        });
        
        this.singleBtn.setOnMouseReleased(e -> {
            if (this.singleBtn.isSelected()){
                this.allClientsBtn.setSelected(false);
                this.allClientsString = "";
                this.serviceString = "| Single ";
                this.serviceSelected = 1;
                this.resBtn.setSelected(false);
                this.comBtn.setSelected(false);
                this.statusString = "";
                this.statusSelected = 0;
            }
            else{
                this.allClientsBtn.setSelected(true);
                this.allClientsString = "All Clients";
                this.serviceString = "";
                this.serviceSelected = 0;
            }
            this.filterString.setText(this.allClientsString + this.serviceString + this.shovelString + this.saltString + this.statusString);
            System.out.println(this.serviceSelected + ", " + this.shovelSelected + ", " + this.statusSelected);
        });
        this.doubleBtn.setOnMouseReleased(e -> {
            if (this.doubleBtn.isSelected()){
                this.allClientsBtn.setSelected(false);
                this.allClientsString = "";
                this.serviceString = "| Double ";
                this.serviceSelected = 2;
                this.resBtn.setSelected(false);
                this.comBtn.setSelected(false);
                this.statusString = "";
                this.statusSelected = 0;
            }
            else{
                this.allClientsBtn.setSelected(true);
                this.allClientsString = "All Clients ";
                this.serviceString = "";
                this.serviceSelected = 0;
            }
            this.filterString.setText(this.allClientsString + this.serviceString + this.shovelString + this.saltString + this.statusString);
            System.out.println(this.serviceSelected + ", " + this.shovelSelected + ", " + this.statusSelected);
        });
        this.tripleBtn.setOnMouseReleased(e -> {
            if (this.tripleBtn.isSelected()){
                this.allClientsBtn.setSelected(false);
                this.allClientsString = "";
                this.serviceString = "| Triple ";
                this.serviceSelected = 3;
                this.resBtn.setSelected(false);
                this.comBtn.setSelected(false);
                this.statusString = "";
                this.statusSelected = 0;
            }
            else{
                this.allClientsBtn.setSelected(true);
                this.allClientsString = "All Clients ";
                this.serviceString = "";
                this.serviceSelected = 0;
            }
            this.filterString.setText(this.allClientsString + this.serviceString + this.shovelString + this.saltString + this.statusString);
            System.out.println(this.serviceSelected + ", " + this.shovelSelected + ", " + this.statusSelected);
        });
        this.otherBtn.setOnMouseReleased(e -> {
            if (this.otherBtn.isSelected()){
                this.allClientsBtn.setSelected(false);
                this.allClientsString = "";
                this.serviceString = "| Other ";
                this.serviceSelected = 4;
            }
            else{
                this.allClientsBtn.setSelected(true);
                this.allClientsString = "All Clients ";
                this.serviceString = "";
                this.serviceSelected = 0;
            }
            this.filterString.setText(this.allClientsString + this.serviceString + this.shovelString + this.saltString + this.statusString);
            System.out.println(this.serviceSelected + ", " + this.shovelSelected + ", " + this.statusSelected);
        });
        
        this.shovelBtn.setOnMouseReleased(e -> {
            if (this.shovelBtn.isSelected()){
                this.shovelString = "| With Shovel ";
                this.shovelSelected = 1;
                
            }
            else {
                this.shovelString = "";
                this.shovelSelected = -1;
            }
            this.filterString.setText(this.allClientsString + this.serviceString + this.shovelString + this.saltString + this.statusString);
            System.out.println(this.serviceSelected + ", " + this.shovelSelected + ", " + this.statusSelected);
        });
        this.noShovelBtn.setOnMouseReleased(e -> {
            if (this.noShovelBtn.isSelected()){
                this.shovelString = "| Without Shovel ";
                this.shovelSelected = 0;
                
            }
            else {
                this.shovelString = "";
                this.shovelSelected = -1;
            }
            this.filterString.setText(this.allClientsString + this.serviceString + this.shovelString + this.saltString + this.statusString);
            System.out.println(this.serviceSelected + ", " + this.shovelSelected + ", " + this.statusSelected);
        });
        
        this.saltBtn.setOnMouseReleased(e -> {
            if (this.saltBtn.isSelected()){
                this.saltString = "| With Salt ";
                this.saltSelected = 1;
            }
            else {
                this.saltString = "";
                this.saltSelected = -1;
            }
            this.filterString.setText(this.allClientsString + this.serviceString + this.shovelString + this.saltString + this.statusString);
            System.out.println(this.serviceSelected + ", " + this.shovelSelected + ", " + this.statusSelected);
        });
        this.noSaltBtn.setOnMouseReleased(e -> {
            if (this.noSaltBtn.isSelected()){
                this.saltString = "| Without Salt ";
                this.saltSelected = 0;
            }
            else {
                this.saltString = "";
                this.saltSelected = -1;
            }
            this.filterString.setText(this.allClientsString + this.serviceString + this.shovelString + this.saltString + this.statusString);
            System.out.println(this.serviceSelected + ", " + this.shovelSelected + ", " + this.statusSelected);
        });
        
        this.resBtn.setOnMouseReleased(e -> {
            if (this.resBtn.isSelected()){
                this.statusString = "| Residential ";
                this.statusSelected = 1;
                if (!this.otherBtn.isSelected()){
                    this.singleBtn.setSelected(false);
                    this.doubleBtn.setSelected(false);
                    this.tripleBtn.setSelected(false);
                    this.otherBtn.setSelected(false);
                    this.allClientsBtn.setSelected(true);
                    this.allClientsString = "All Clients ";
                    this.serviceSelected = 0;
                    this.serviceString = "";
                }
            }
            else {
                this.statusString = "";
                this.statusSelected = 0;
            }
            this.filterString.setText(this.allClientsString + this.serviceString + this.shovelString + this.saltString + this.statusString);
            System.out.println(this.serviceSelected + ", " + this.shovelSelected + ", " + this.statusSelected);
        });
        this.comBtn.setOnMouseReleased(e -> {
            if (this.comBtn.isSelected()){
                this.statusString = "| Commercial ";
                this.statusSelected = 2;
                if (!this.otherBtn.isSelected()){
                    this.singleBtn.setSelected(false);
                    this.doubleBtn.setSelected(false);
                    this.tripleBtn.setSelected(false);
                    this.otherBtn.setSelected(false);
                    this.allClientsBtn.setSelected(true);
                    this.allClientsString = "All Clients ";
                    this.serviceSelected = 0;
                    this.serviceString = "";
                }
            }
            else {
                this.statusString = "";
                this.statusSelected = 0;
            }
            this.filterString.setText(this.allClientsString + this.serviceString + this.shovelString + this.saltString + this.statusString);
            System.out.println(this.serviceSelected + ", " + this.shovelSelected + ", " + this.statusSelected);
        });
        
    }
    
    public void filterList(){
        try {
            
            Statement st = this.conn.createStatement();
            setQuery();
            ResultSet rs = st.executeQuery(this.query);
            
            this.tableList.clear();
            while (rs.next()){
                this.tableList.add(new Client(rs.getInt(1), rs.getString(2), rs.getInt(3), rs.getString(4), rs.getDouble(5), rs.getString(6), rs.getString(7), rs.getString(8)));
            }
            
        }
        catch (SQLException ex){
            System.out.println(ex.getMessage());
        }
    }
    
    private void setButtons(){
        
            
            
            switch (this.serviceSelected){
                case 0: {
                    this.allClientsBtn.setSelected(true);
                    this.singleBtn.setSelected(false);
                    this.doubleBtn.setSelected(false);
                    this.tripleBtn.setSelected(false);
                    this.otherBtn.setSelected(false);
                    
                    this.allClientsString = "All Clients";
                    this.serviceString = "";
                }break;
                case 1: {
                    this.singleBtn.setSelected(true);
                    this.serviceString = "| Single ";
                }break;
                case 2: {
                    this.doubleBtn.setSelected(true);
                    this.serviceString = "| Double ";
                }break;
                case 3: {
                    this.tripleBtn.setSelected(true);
                    this.serviceString = "| Triple ";
                }break;
                case 4: {
                    this.otherBtn.setSelected(true);
                    this.serviceString = "| Other ";
                }break;
            }
            
            switch (this.shovelSelected){
                case -1: {
                    this.shovelBtn.setSelected(false);
                    this.noShovelBtn.setSelected(false);
                    this.shovelString = "";
                }break;
                case 0: {
                    this.noShovelBtn.setSelected(true);
                    this.shovelString = "| Without Shovel ";
                }break;
                case 1: {
                    this.shovelBtn.setSelected(true);
                    this.shovelString = "| With Shovel ";
                }break;
            }
            
            switch (this.saltSelected){
                case -1: {
                    this.saltBtn.setSelected(false);
                    this.noSaltBtn.setSelected(false);
                    this.saltString = "";
                }break;
                case 0: {
                    this.noSaltBtn.setSelected(true);
                    this.saltString = "| Without Salt ";
                }break;
                case 1: {
                    this.saltBtn.setSelected(true);
                    this.saltString = "| With Salt";
                }break;
            }
            
            switch (this.statusSelected){
                case 0: {
                    this.resBtn.setSelected(false);
                    this.comBtn.setSelected(false);
                    this.statusString = "";
                }break;
                case 1: {
                    this.resBtn.setSelected(true);
                    this.statusString = "| Residential ";
                }break;
                case 2: {
                    this.comBtn.setSelected(true);
                    this.statusString = "| Commercial ";
                }break;
            }
        
        this.filterString.setText(this.allClientsString + this.serviceString + this.shovelString + this.saltString + this.statusString);
        System.out.println(this.serviceSelected + ", " + this.shovelSelected + ", " + this.statusSelected + ", " + this.saltSelected);
        
        
    }
    
    private void setQuery(){
        
        
            switch (this.serviceSelected){
                case 0: {
                    //don't do anything...show all services
                }break;
                case 1: {
                    this.query += "and winter_services.single > 0 ";
                }break;
                case 2: {
                    this.query += "and winter_services.double > 0 ";
                }break;
                case 3: {
                    this.query += "and winter_services.triple > 0 ";
                }break;
                case 4: {
                    this.query += "and winter_services.other > 0 ";
                }break;
            }
        
            switch (this.shovelSelected){
                case -1: {
                    //don't add anything...want with shovel AND without shovel
                }break;
                case 0: {
                    this.query += "and winter_services.shovel = 0 ";
                }break;
                case 1: {
                    this.query += "and winter_services.shovel > 0 ";
                }break;
            }
            
            switch (this.saltSelected){
                case -1: {
                    //don't add anything...want with salt AND without salt
                }break;
                case 0: {
                    this.query += "and winter_services.salt = 0 ";
                }break;
                case 1: {
                    this.query += "and winter_services.salt > 0 ";
                }break;
            }
        
            switch (this.statusSelected){
                case 0: {
                    //don't add anything...want residential AND commercial
                }break;
                case 1: {
                    this.query += "and client_information.status = 0 ";
                }break;
                case 2: {
                    this.query += "and client_information.status = 1 ";
                }break;
            }
            
            this.query += "order by client_information.address asc";
            
            System.out.println(query);
        
    }
    
    public Button getSaveBtn(){
        return this.saveBtn;
    }
    
    public int getServiceSelected(){
        return this.serviceSelected;
    }
    
    public int getShovelSelected(){
        return this.shovelSelected;
    }
    
    public int getStatusSelected(){
        return this.statusSelected;
    }
    
    public int getSaltSelected(){
        return this.saltSelected;
    }
    
}
