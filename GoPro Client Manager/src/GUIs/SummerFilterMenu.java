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
    
    private Connection conn;
    private ObservableList<Client> tableList;
    private String seasonId;
    
    private Scene scene;
    private final BorderPane pane = new BorderPane();
    private final GridPane centerPane = new GridPane();
    private final HBox topPane = new HBox();
    
    /*
    if the allClients option is true, then ignore every other option
    show all clients, and set everything else to false, as well as
    setSelected() to false
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
    
    private ToggleButton[] btnList = {allClientsBtn, residentialBtn, commercialBtn, lawnBtn, springBtn, fallBtn, weedTreatmentBtn, aerationSpringBtn, 
        aerationFallBtn, spiderBtn, weedingBtn, hedgesBtn, fertilizerBtn, wormsBtn, soilBtn, seedingBtn};
    
    private ToggleGroup statusGroup = new ToggleGroup();
    
    private Text filterString = new Text();
    
    private String statusString = "";
    
    private Text lawnStr = new Text();
    private Text springStr = new Text();
    private Text fallStr = new Text();
    private Text weedTreatmentStr = new Text();
    private Text aerationSpringStr = new Text();
    private Text aerationFallStr = new Text();
    private Text spiderStr = new Text();
    private Text weedingStr = new Text();
    private Text hedgesStr = new Text();
    private Text fertilizerStr = new Text();
    private Text wormsStr = new Text();
    private Text soilStr = new Text();
    private Text seedingStr = new Text();
    
    private Text[] stringList = {lawnStr, springStr, fallStr, weedTreatmentStr, aerationSpringStr, aerationFallStr, spiderStr, weedingStr, 
                                 hedgesStr, fertilizerStr, wormsStr, soilStr, seedingStr};
    
    private String query = "";
    
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
        setButtons(serviceList);
        
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
        
        setButtonAction();
        
    }
    
    private void setButtonAction(){
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
                this.lawnStr.setText("| Lawn ");
                updateFilterString();
            }
            else {
                this.lawn = false;
                this.lawnStr.setText("");
                this.updateFilterString();
            }
        });
        
        this.springBtn.setOnAction(e -> {
            if (this.springBtn.isSelected()){
                this.spring = true;
                this.springStr.setText("| Spring ");
                updateFilterString();
            }
            else {
                this.spring = false;
                this.springStr.setText("");
                this.updateFilterString();
            }
        });
        
        this.fallBtn.setOnAction(e -> {
            if (this.fallBtn.isSelected()){
                this.fall = true;
                this.fallStr.setText("| Fall ");
                updateFilterString();
            }
            else {
                this.fall = false;
                this.fallStr.setText("");
                this.updateFilterString();
            }
        });
        
        this.weedTreatmentBtn.setOnAction(e -> {
            if (this.weedTreatmentBtn.isSelected()){
                this.weedTreatment = true;
                this.weedTreatmentStr.setText("| Weed Treatment ");
                updateFilterString();
            }
            else {
                this.weedTreatment = false;
                this.weedTreatmentStr.setText("");
                this.updateFilterString();
            }
        });
        
        this.aerationSpringBtn.setOnAction(e -> {
            if (this.aerationSpringBtn.isSelected()){
                this.aerationSpring = true;
                this.aerationSpringStr.setText("| Aeration Spring ");
                updateFilterString();
            }
            else {
                this.aerationSpring = false;
                this.aerationSpringStr.setText("");
                this.updateFilterString();
            }
        });
        
        this.aerationFallBtn.setOnAction(e -> {
            if (this.aerationFallBtn.isSelected()){
                this.aerationFall = true;
                this.aerationFallStr.setText("| Aeration Fall ");
                updateFilterString();
            }
            else {
                this.aerationFall = false;
                this.aerationFallStr.setText("");
                this.updateFilterString();
            }
        });
        
        this.spiderBtn.setOnAction(e -> {
            if (this.spiderBtn.isSelected()){
                this.spider = true;
                this.spiderStr.setText("| Spiders ");
                updateFilterString();
            }
            else {
                this.spider = false;
                this.spiderStr.setText("");
                this.updateFilterString();
            }
        });
        
        this.weedingBtn.setOnAction(e -> {
            if (this.weedingBtn.isSelected()){
                this.weeding = true;
                this.weedingStr.setText("| Weeding ");
                updateFilterString();
            }
            else {
                this.weeding = false;
                this.weedingStr.setText("");
                this.updateFilterString();
            }
        });
        
        this.hedgesBtn.setOnAction(e -> {
            if (this.hedgesBtn.isSelected()){
                this.hedges = true;
                this.hedgesStr.setText("| Hedges ");
                updateFilterString();
            }
            else {
                this.hedges = false;
                this.hedgesStr.setText("");
                this.updateFilterString();
            }
        });
        
        this.fertilizerBtn.setOnAction(e -> {
            if (this.fertilizerBtn.isSelected()){
                this.fertilizer = true;
                this.fertilizerStr.setText("| Fertilizer ");
                updateFilterString();
            }
            else {
                this.fertilizer = false;
                this.fertilizerStr.setText("");
                this.updateFilterString();
            }
        });
        
        this.wormsBtn.setOnAction(e -> {
            if (this.wormsBtn.isSelected()){
                this.worms = true;
                this.wormsStr.setText("| Worms ");
                updateFilterString();
            }
            else {
                this.worms = false;
                this.wormsStr.setText("");
                this.updateFilterString();
            }
        });
        
        this.soilBtn.setOnAction(e -> {
            if (this.soilBtn.isSelected()){
                this.soil = true;
                this.soilStr.setText("| Soil ");
                updateFilterString();
            }
            else {
                this.soil = false;
                this.soilStr.setText("");
                this.updateFilterString();
            }
        });
        
        this.seedingBtn.setOnAction(e -> {
            if (this.seedingBtn.isSelected()){
                this.seeding = true;
                this.seedingStr.setText("| Seeding ");
                updateFilterString();
            }
            else {
                this.seeding = false;
                this.seedingStr.setText("");
                this.updateFilterString();
            }
        });
        
    }
    
    private void setServicesToFalse(){
        this.lawn = false;
        this.lawnBtn.setSelected(false);
        this.lawnStr.setText("");
                
        this.spring = false;
        this.springBtn.setSelected(false);
        this.springStr.setText("");
                
        this.fall = false;
        this.fallBtn.setSelected(false);
        this.fallStr.setText("");
                
        this.weedTreatment = false;
        this.weedTreatmentBtn.setSelected(false);
        this.weedTreatmentStr.setText("");
                
        this.aerationSpring = false;
        this.aerationSpringBtn.setSelected(false);
        this.aerationSpringStr.setText("");
                
        this.spider = false;
        this.spiderBtn.setSelected(false);
        this.spiderStr.setText("");
                
        this.weeding = false;
        this.weedingBtn.setSelected(false);
        this.weedingStr.setText("");
                
        this.hedges = false;
        this.hedgesBtn.setSelected(false);
        this.hedgesStr.setText("");
                
        this.fertilizer = false;
        this.fertilizerBtn.setSelected(false);
        this.fertilizerStr.setText("");
                
        this.worms = false;
        this.wormsBtn.setSelected(false);
        this.wormsStr.setText("");
                
        this.soil = false;
        this.soilBtn.setSelected(false);
        this.soilStr.setText("");
                
        this.aerationFall = false;
        this.aerationFallBtn.setSelected(false);
        this.aerationFallStr.setText("");
                
        this.seeding = false;
        this.seedingBtn.setSelected(false);
        this.seedingStr.setText("");
    }
    
    private void setButtons(boolean[] serviceList){
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
                        this.stringList[i].setText("| " + this.btnList[j].getText() + " ");
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
                        this.stringList[i].setText("| " + this.btnList[j].getText() + " ");
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
                        this.stringList[i].setText("| " + this.btnList[j].getText() + " ");
                    }
                }
            }break;
        }
    }
    
    private void updateFilterString(){
        this.filterString.setText(this.statusString + this.lawnStr.getText() + this.springStr.getText() + this.fallStr.getText() 
                + this.weedTreatmentStr.getText() + this.aerationSpringStr.getText() + this.aerationFallStr.getText() + this.spiderStr.getText() 
                + this.weedingStr.getText() + this.hedgesStr.getText() + this.fertilizerStr.getText() + this.wormsStr.getText() 
                + this.soilStr.getText() + this.seedingStr.getText());
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
        
        this.query += "order by client_information.address asc";
            
        System.out.println(query);
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
    
    public int getStatus(){
        return this.status;
    }
    public boolean getLawn(){
        return this.lawn;
    }
    public boolean getSpring(){
        return this.spring;
    }
    public boolean getFall(){
        return this.fall;
    }
    public boolean getWeedTreatment(){
        return this.weedTreatment;
    }
    public boolean getAerationSpring(){
        return this.aerationSpring;
    }
    public boolean getAerationFall(){
        return this.aerationFall;
    }
    public boolean getSpider(){
        return this.spider;
    }
    public boolean getWeeding(){
        return this.weeding;
    }
    public boolean getHedges(){
        return this.hedges;
    }
    public boolean getFertilizer(){
        return this.fertilizer;
    }
    public boolean getWorms(){
        return this.worms;
    }
    public boolean getSoil(){
        return this.soil;
    }
    public boolean getSeeding(){
        return this.seeding;
    }
    public Button getSaveBtn(){
        return this.saveBtn;
    }
}
