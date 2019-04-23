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
public class SummerFilterMenu extends Stage{
    
    private Connection conn;//connection to the database
    private ObservableList<Client> tableList;//observable list that is used for the tableview in the summer menu
    private String seasonId;//seasonId of the current session
    
    private Scene scene;
    private final BorderPane pane = new BorderPane();
    private final GridPane centerPane = new GridPane();
    private final HBox topPane = new HBox();
    
    /**
     * HOW THE FILTERING WORKS
     * 
     * The status can be either everyone, residential or commercial. 
     * By default the status is set to all clients.
     * The user can choose 1 of the three status, with a combination of all the services provided.
     * Pressing on a status when it is already selected will deactivate all the services selected.
     */
    private int status; //0 = all clients, 1 = residential, 2 = commercial
    
    private boolean lawn;
    private boolean spring;
    private boolean fall;
    private boolean weedTreatment;
    private boolean aerationSpring;
    private boolean aerationFall;
    private boolean spider;
    private boolean weeding;
    private boolean hedges;
    private boolean fertilizer;
    private boolean worms;
    private boolean soil;
    private boolean seeding;
    
    /****************************
     * FILTER BUTTONS
     * width = 100
     ****************************/
    
    private ToggleButton allClientsBtn = new ToggleButton("All Clients");
    private ToggleButton residentialBtn = new ToggleButton("Residential");
    private ToggleButton commercialBtn = new ToggleButton("Commercial");
    
    private ToggleButton lawnBtn = new ToggleButton("Lawn");
    private ToggleButton springBtn = new ToggleButton("Spring");
    private ToggleButton fallBtn = new ToggleButton("Fall");
    private ToggleButton weedTreatmentBtn = new ToggleButton("Weed Treatment");
    
    private ToggleButton aerationSpringBtn = new ToggleButton("Aeration Spring");
    private ToggleButton aerationFallBtn = new ToggleButton("Aeration Fall");
    private ToggleButton spiderBtn = new ToggleButton("Spiders");
    private ToggleButton weedingBtn = new ToggleButton("Weeding");
    
    private ToggleButton hedgesBtn = new ToggleButton("Hedges");
    private ToggleButton fertilizerBtn = new ToggleButton("Fertilizer");
    private ToggleButton wormsBtn = new ToggleButton("Worms");
    private ToggleButton soilBtn = new ToggleButton("Soil");
    
    private ToggleButton seedingBtn = new ToggleButton("Seeding");
    
    private Button saveBtn = new Button("Save");
    
    //list of all the buttons
    private ToggleButton[] btnList = {allClientsBtn, residentialBtn, commercialBtn, lawnBtn, springBtn, fallBtn, weedTreatmentBtn, aerationSpringBtn, 
        aerationFallBtn, spiderBtn, weedingBtn, hedgesBtn, fertilizerBtn, wormsBtn, soilBtn, seedingBtn};
    
    //toggle group for the status buttons
    private ToggleGroup statusGroup = new ToggleGroup();
    
    //the string that displays what the user is currently filtering
    private Text filterString = new Text();
    private String statusString = "";
    
    private String lawnStr = "";
    private String springStr = "";
    private String fallStr = "";
    private String weedTreatmentStr = "";
    private String aerationSpringStr = "";
    private String aerationFallStr = "";
    private String spiderStr = "";
    private String weedingStr = "";
    private String hedgesStr = "";
    private String fertilizerStr = "";
    private String wormsStr = "";
    private String soilStr = "";
    private String seedingStr = "";
    
    private String[] stringList = {lawnStr, springStr, fallStr, weedTreatmentStr, aerationSpringStr, aerationFallStr, spiderStr, weedingStr, 
                                 hedgesStr, fertilizerStr, wormsStr, soilStr, seedingStr};
    
    private String query = "";
    
    /**
     *
     * @param conn
     * @param seasonId
     * @param tableList
     * @param status
     * @param lawn
     * @param spring
     * @param fall
     * @param weedTreatment
     * @param aerationSpring
     * @param aerationFall
     * @param spider
     * @param weeding
     * @param hedges
     * @param fertilizer
     * @param worms
     * @param soil
     * @param seeding
     */
    public SummerFilterMenu(Connection conn, String seasonId, ObservableList<Client> tableList, int status, boolean lawn, 
                            boolean spring, boolean fall, boolean weedTreatment, boolean aerationSpring, boolean aerationFall, boolean spider, 
                            boolean weeding, boolean hedges, boolean fertilizer, boolean worms, boolean soil, boolean seeding){
        this.conn = conn;
        this.seasonId = seasonId;
        this.tableList = tableList;
        
        this.status = status;
        this.lawn = lawn;
        this.spring = spring;
        this.fall = fall;
        this.weedTreatment = weedTreatment;
        this.aerationSpring = aerationSpring;
        this.aerationFall = aerationFall;
        this.spider = spider;
        this.weeding = weeding;
        this.hedges = hedges;
        this.fertilizer = fertilizer;
        this.worms = worms;
        this.soil = soil;
        this.seeding = seeding;
        
        boolean[] serviceList = {lawn, spring, fall, weedTreatment, aerationSpring, aerationFall, spider, weeding, hedges, fertilizer, worms, soil, seeding};
        
        query = "select summer_services.id, client_information.address, client_information.status, "
                    + "client_information.name, summer_services.total, client_information.phone, "
                    + "summer_services.comments ,client_information.city "
                    + "from summer_services inner join client_information "
                    + "on summer_services.id = client_information.id and summer_services.season = '" + this.seasonId + "' ";
        
        setStage();
        setStatusButtonAction(serviceList);
        
        updateFilterString();
    }
    
    private void setStage(){
        
        this.scene = new Scene(this.pane);
        this.setScene(this.scene);
        
        this.pane.setTop(this.topPane);
        
        this.topPane.setPadding(new Insets(10, 10, 10, 10));
        this.topPane.setSpacing(10);
        this.topPane.setAlignment(Pos.CENTER_LEFT);
        this.topPane.getChildren().add(this.filterString);
        
        this.pane.setCenter(this.centerPane);
        
        this.centerPane.setPadding(new Insets(10, 10, 10, 10));
        this.centerPane.setAlignment(Pos.CENTER);
        this.centerPane.setVgap(10);
        this.centerPane.setHgap(10);
        
        this.centerPane.add(this.allClientsBtn, 0, 0);
        this.centerPane.add(this.residentialBtn, 0, 2);
        this.centerPane.add(this.commercialBtn, 0, 3);
        
        this.centerPane.add(this.lawnBtn, 1, 0);
        this.centerPane.add(this.springBtn, 1, 1);
        this.centerPane.add(this.fallBtn, 1, 2);
        this.centerPane.add(this.weedTreatmentBtn, 1, 3);
        
        this.centerPane.add(this.aerationSpringBtn, 2, 0);
        this.centerPane.add(this.aerationFallBtn, 2, 1);
        this.centerPane.add(this.spiderBtn, 2, 2);
        this.centerPane.add(this.weedingBtn, 2, 3);
        
        this.centerPane.add(this.hedgesBtn, 3, 0);
        this.centerPane.add(this.fertilizerBtn, 3, 1);
        this.centerPane.add(this.wormsBtn, 3, 2);
        this.centerPane.add(this.soilBtn, 3, 3);
        
        this.centerPane.add(this.seedingBtn, 4, 0);
        this.centerPane.add(this.saveBtn, 4, 3);
        
        for (int i = 0; i < btnList.length; i++){
            this.btnList[i].setMinWidth(100);
            this.btnList[i].setMaxWidth(100);
            this.btnList[i].setFocusTraversable(false);
        }
        
        this.saveBtn.setMinWidth(100);
        this.saveBtn.setMaxWidth(100);
        this.saveBtn.setFocusTraversable(false);
        
        this.residentialBtn.setToggleGroup(this.statusGroup);
        this.commercialBtn.setToggleGroup(this.statusGroup);
        this.allClientsBtn.setToggleGroup(this.statusGroup);
        
        setServicesButtonAction();
        
    }
    
    private void setServicesButtonAction(){
        this.allClientsBtn.setOnAction(e -> {
            if (this.allClientsBtn.isSelected()){
                this.status = 0;
                this.statusString = "All Clients ";
                updateFilterString();
            }
            else{
                this.allClientsBtn.setSelected(true);
                
                setServicesToFalse();
                
                updateFilterString();
            }
        });
        
        this.residentialBtn.setOnAction(e -> {
            if (this.residentialBtn.isSelected()){    
                this.status = 1;
                this.statusString = "Residential ";
                updateFilterString();
            }
            else{
                this.residentialBtn.setSelected(true);
                
                setServicesToFalse();
                
                updateFilterString();
            }
        });
        
        this.commercialBtn.setOnAction(e -> {
            if (this.commercialBtn.isSelected()){
                this.status = 2;
                this.statusString = "Commercial ";
                updateFilterString();
            }
            else{
                this.commercialBtn.setSelected(true);
                
                setServicesToFalse();
                
                updateFilterString();
            }
        });
        
        this.lawnBtn.setOnAction(e -> {
            if (this.lawnBtn.isSelected()){
                this.lawn = true;
                this.lawnStr = "| Lawn ";
                updateFilterString();
            }
            else {
                this.lawn = false;
                this.lawnStr = "";
                this.updateFilterString();
            }
        });
        
        this.springBtn.setOnAction(e -> {
            if (this.springBtn.isSelected()){
                this.spring = true;
                this.springStr = "| Spring ";
                updateFilterString();
            }
            else {
                this.spring = false;
                this.springStr = "";
                this.updateFilterString();
            }
        });
        
        this.fallBtn.setOnAction(e -> {
            if (this.fallBtn.isSelected()){
                this.fall = true;
                this.fallStr = "| Fall ";
                updateFilterString();
            }
            else {
                this.fall = false;
                this.fallStr = "";
                this.updateFilterString();
            }
        });
        
        this.weedTreatmentBtn.setOnAction(e -> {
            if (this.weedTreatmentBtn.isSelected()){
                this.weedTreatment = true;
                this.weedTreatmentStr = "| Weed Treatment ";
                updateFilterString();
            }
            else {
                this.weedTreatment = false;
                this.weedTreatmentStr = "";
                this.updateFilterString();
            }
        });
        
        this.aerationSpringBtn.setOnAction(e -> {
            if (this.aerationSpringBtn.isSelected()){
                this.aerationSpring = true;
                this.aerationSpringStr = "| Aeration Spring ";
                updateFilterString();
            }
            else {
                this.aerationSpring = false;
                this.aerationSpringStr = "";
                this.updateFilterString();
            }
        });
        
        this.aerationFallBtn.setOnAction(e -> {
            if (this.aerationFallBtn.isSelected()){
                this.aerationFall = true;
                this.aerationFallStr = "| Aeration Fall ";
                updateFilterString();
            }
            else {
                this.aerationFall = false;
                this.aerationFallStr = "";
                this.updateFilterString();
            }
        });
        
        this.spiderBtn.setOnAction(e -> {
            if (this.spiderBtn.isSelected()){
                this.spider = true;
                this.spiderStr = "| Spiders ";
                updateFilterString();
            }
            else {
                this.spider = false;
                this.spiderStr = "";
                this.updateFilterString();
            }
        });
        
        this.weedingBtn.setOnAction(e -> {
            if (this.weedingBtn.isSelected()){
                this.weeding = true;
                this.weedingStr = "| Weeding ";
                updateFilterString();
            }
            else {
                this.weeding = false;
                this.weedingStr = "";
                this.updateFilterString();
            }
        });
        
        this.hedgesBtn.setOnAction(e -> {
            if (this.hedgesBtn.isSelected()){
                this.hedges = true;
                this.hedgesStr = "| Hedges ";
                updateFilterString();
            }
            else {
                this.hedges = false;
                this.hedgesStr = "";
                this.updateFilterString();
            }
        });
        
        this.fertilizerBtn.setOnAction(e -> {
            if (this.fertilizerBtn.isSelected()){
                this.fertilizer = true;
                this.fertilizerStr = "| Fertilizer ";
                updateFilterString();
            }
            else {
                this.fertilizer = false;
                this.fertilizerStr = "";
                this.updateFilterString();
            }
        });
        
        this.wormsBtn.setOnAction(e -> {
            if (this.wormsBtn.isSelected()){
                this.worms = true;
                this.wormsStr = "| Worms ";
                updateFilterString();
            }
            else {
                this.worms = false;
                this.wormsStr = "";
                this.updateFilterString();
            }
        });
        
        this.soilBtn.setOnAction(e -> {
            if (this.soilBtn.isSelected()){
                this.soil = true;
                this.soilStr = "| Soil ";
                updateFilterString();
            }
            else {
                this.soil = false;
                this.soilStr = "";
                this.updateFilterString();
            }
        });
        
        this.seedingBtn.setOnAction(e -> {
            if (this.seedingBtn.isSelected()){
                this.seeding = true;
                this.seedingStr = "| Seeding ";
                updateFilterString();
            }
            else {
                this.seeding = false;
                this.seedingStr = "";
                this.updateFilterString();
            }
        });
        
    }
    
    private void setServicesToFalse(){
        this.lawn = false;
        this.lawnBtn.setSelected(false);
        this.lawnStr = "";
                
        this.spring = false;
        this.springBtn.setSelected(false);
        this.springStr = "";
                
        this.fall = false;
        this.fallBtn.setSelected(false);
        this.fallStr = "";
                
        this.weedTreatment = false;
        this.weedTreatmentBtn.setSelected(false);
        this.weedTreatmentStr = "";
                
        this.aerationSpring = false;
        this.aerationSpringBtn.setSelected(false);
        this.aerationSpringStr = "";
                
        this.spider = false;
        this.spiderBtn.setSelected(false);
        this.spiderStr = "";
                
        this.weeding = false;
        this.weedingBtn.setSelected(false);
        this.weedingStr = "";
                
        this.hedges = false;
        this.hedgesBtn.setSelected(false);
        this.hedgesStr = "";
                
        this.fertilizer = false;
        this.fertilizerBtn.setSelected(false);
        this.fertilizerStr = "";
                
        this.worms = false;
        this.wormsBtn.setSelected(false);
        this.wormsStr = "";
                
        this.soil = false;
        this.soilBtn.setSelected(false);
        this.soilStr = "";
                
        this.aerationFall = false;
        this.aerationFallBtn.setSelected(false);
        this.aerationFallStr = "";
                
        this.seeding = false;
        this.seedingBtn.setSelected(false);
        this.seedingStr = "";
    }
    
    private void setStatusButtonAction(boolean[] serviceList){
        switch (this.status){
            case 0: {//All clients selected
                this.allClientsBtn.setSelected(true);
                this.residentialBtn.setSelected(false);
                this.commercialBtn.setSelected(false);
                
                this.statusString = "All Clients ";
                
                System.out.println(serviceList[0]);
                for (int i = 0, j = 3; i < serviceList.length; i++, j++){
                    if (serviceList[i]){
                        this.btnList[j].setSelected(true);
                        this.stringList[i] = "| " + this.btnList[j].getText() + " ";
                    }
                }
            }break;
            case 1: {//Residential selected
                this.allClientsBtn.setSelected(false);
                this.residentialBtn.setSelected(true);
                this.commercialBtn.setSelected(false);
                
                this.statusString = "Residential ";
                
                for (int i = 0, j = 3; i < serviceList.length; i++, j++){
                    if (serviceList[i]){
                        this.btnList[j].setSelected(true);
                        this.stringList[i] = "| " + this.btnList[j].getText() + " ";
                    }
                }
            }break;
            case 2: {//Commercial selected
                this.allClientsBtn.setSelected(false);
                this.residentialBtn.setSelected(false);
                this.commercialBtn.setSelected(true);
                
                this.statusString = "Commercial ";
                
                for (int i = 0, j = 3; i < serviceList.length; i++, j++){
                    if (serviceList[i]){
                        this.btnList[j].setSelected(true);
                        this.stringList[i] = "| " + this.btnList[j].getText() + " ";
                    }
                }
            }break;
        }
    }
    
    private void updateFilterString(){
        this.filterString.setText(this.statusString + this.lawnStr + this.springStr + this.fallStr 
                + this.weedTreatmentStr + this.aerationSpringStr + this.aerationFallStr + this.spiderStr 
                + this.weedingStr + this.hedgesStr + this.fertilizerStr + this.wormsStr 
                + this.soilStr + this.seedingStr);
    }
    
    private void setQuery(){
        
        switch (this.status){
            case 0:{
                //do nothing, want all clients
            }break;
            case 1:{//search for residential
                this.query += "and client_information.status = 0 ";
            }break;
            case 2:{//serach for commercial
                this.query += "and client_information.status = 1 ";
            }break;
        }
        
        if (this.lawn)
            this.query += "and summer_services.lawn > 0 ";
        
        if (this.spring)
            this.query += "and summer_services.spring > 0 ";
        
        if (this.fall)
            this.query += "and summer_services.fall > 0 ";
        
        if (this.weedTreatment)
            this.query += "and summer_services.weed_treatment > 0 ";
        
        if (this.aerationSpring)
            this.query += "and summer_services.aeration_spring > 0 ";
        
        if (this.aerationFall)
            this.query += "and summer_services.aeration_fall > 0 ";
        
        if (this.spider)
            this.query += "and summer_services.spider > 0 ";
        
        if (this.weeding)
            this.query += "and summer_services.weeding > 0 ";
        
        if (this.hedges)
            this.query += "and summer_services.hedges > 0 ";
        
        if (this.fertilizer)
            this.query += "and summer_services.fertilizer > 0 ";
        
        if (this.worms)
            this.query += "and summer_services.worms > 0 ";
        
        if (this.soil)
            this.query += "and summer_services.soil > 0 ";
        
        if (this.seeding)
            this.query += "and summer_services.seeding > 0 ";
        
        this.query += "order by client_information.door_number asc";
            
        System.out.println(query);
    }
    
    /**
     * Updates the observable list by querying database with selected filter parameters
     */
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
    
    /**
     * Return the status selected
     * @return
     */
    public int getStatus(){
        return this.status;
    }

    /**
     * Return the lawn value
     * @return
     */
    public boolean getLawn(){
        return this.lawn;
    }

    /**
     * Return the spring value
     * @return
     */
    public boolean getSpring(){
        return this.spring;
    }

    /**
     * Return the fall value
     * @return
     */
    public boolean getFall(){
        return this.fall;
    }

    /**
     * Return the weed treatment value
     * @return
     */
    public boolean getWeedTreatment(){
        return this.weedTreatment;
    }

    /**
     * Return the aeration spring value
     * @return
     */
    public boolean getAerationSpring(){
        return this.aerationSpring;
    }

    /**
     * Return the aeration fall value
     * @return
     */
    public boolean getAerationFall(){
        return this.aerationFall;
    }

    /**
     * Return the spider value
     * @return
     */
    public boolean getSpider(){
        return this.spider;
    }

    /**
     * Return the weeding value
     * @return
     */
    public boolean getWeeding(){
        return this.weeding;
    }

    /**
     * Return the hedges value
     * @return
     */
    public boolean getHedges(){
        return this.hedges;
    }

    /**
     * Return the fertilizer value
     * @return
     */
    public boolean getFertilizer(){
        return this.fertilizer;
    }

    /**
     * Return the worms value
     * @return
     */
    public boolean getWorms(){
        return this.worms;
    }

    /**
     * Return the soil value
     * @return
     */
    public boolean getSoil(){
        return this.soil;
    }

    /**
     * Return the seeding value
     * @return
     */
    public boolean getSeeding(){
        return this.seeding;
    }

    /**
     * Return the save button
     * @return
     */
    public Button getSaveBtn(){
        return this.saveBtn;
    }
}
