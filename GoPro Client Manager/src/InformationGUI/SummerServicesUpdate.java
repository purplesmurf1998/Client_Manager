/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InformationGUI;

import NewClientGUI.SummerPaymentMenu;
import NewClientGUI.SummerServiceMenu;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

/**
 *
 * @author purplesmurf
 */
public class SummerServicesUpdate extends Stage{
    
    private Connection conn;
    private int clientId;
    private String seasonId;
    
    private Scene scene;
    private BorderPane pane = new BorderPane();
    
    private SummerServiceMenu serviceMenu;
    private HBox bottomPane = new HBox();
    private Button saveBtn = new Button("Save");
    
    public SummerServicesUpdate(Connection conn, int clientId, String seasonId){
        this.conn = conn;
        this.clientId = clientId;
        this.seasonId = seasonId;
        
        this.serviceMenu = new SummerServiceMenu(this.conn);
        
        this.scene = new Scene(this.pane);
        this.setScene(scene);
        
        this.pane.setCenter(this.serviceMenu);
        this.pane.setBottom(this.bottomPane);
        
        this.bottomPane.getChildren().add(this.saveBtn);
        
        setUpServiceMenu();
        
    }
    
    public void saveServicesAndModifyPayments(){
        double lawn = this.serviceMenu.getLawn();
        double spring = this.serviceMenu.getSpring();
        double fall = this.serviceMenu.getFall();
        double weedTreatment = this.serviceMenu.getWeedTreatment();
        double aerationSpring = this.serviceMenu.getAerationSpring();
        double aerationFall = this.serviceMenu.getAerationFall();
        double spider = this.serviceMenu.getSpider();
        double weeding = this.serviceMenu.getWeeding();
        double hedges = this.serviceMenu.getHedges();
        double fertilizer = this.serviceMenu.getFertilizer();
        double worms = this.serviceMenu.getWorms();
        double soil = this.serviceMenu.getSoil();
        double seeding = this.serviceMenu.getSeeding();
        double total = this.serviceMenu.getTotal();
        double saved = 0;
            
        try {
                
            Statement st = this.conn.createStatement();
                
            String query = "select saved from summer_payment where id = " + this.clientId + " and season = '" + this.seasonId + "'";
                
            ResultSet rs = st.executeQuery(query);
                
            while (rs.next()){
                if (rs.getDouble(1) > 0)
                    saved = rs.getDouble(1);
            }
                
            rs.close();
            
            query = "update summer_services "
                    + "set (lawn, spring, fall, weed_treatment, aeration_spring, aeration_fall, spider, weeding, hedges, fertilizer, worms, soil, seeding, total) = "
                    + "(" + lawn + ", " + spring + ", " + fall + ", " + weedTreatment + ", " + aerationSpring + ", " + aerationFall + ", " 
                    + spider + ", " + weeding + ", " + hedges + ", " + fertilizer + ", " + worms + ", " + soil + ", " + seeding + ", " + total + ") "
                    + "where seasonId = '" + this.clientId + this.seasonId + "'";
            
            st.executeUpdate(query);
            
            total  = total - (total * (saved / 100));
            
            query = "update summer_payment "
                    + "set total = " + total + " where id = " + this.clientId + " and season = '" + this.seasonId + "'";
            
            st.executeUpdate(query);
                
        }
        catch (SQLException ex){
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
    }
    
    private void setUpServiceMenu(){
        try {
            Statement st = conn.createStatement();
            
            String query = "select lawn, spring, fall, weed_treatment, aeration_spring, aeration_fall, spider, weeding, hedges, fertilizer, worms, soil, seeding, comments "
                    + "from summer_services where seasonid = '" + this.clientId + "" + this.seasonId + "'";
            
            ResultSet rs = st.executeQuery(query);
            
            while (rs.next()){
                this.serviceMenu.changeServices(rs.getDouble(1), rs.getDouble(2), rs.getDouble(3), rs.getDouble(4), rs.getDouble(5), rs.getDouble(6), 
                        rs.getDouble(7), rs.getDouble(8), rs.getDouble(9), rs.getDouble(10), rs.getDouble(11), rs.getDouble(12), rs.getDouble(13), rs.getString(14));
            }
            
            
        }
        catch (SQLException ex){
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
        
        
    }
    
    public Button getSaveBtn(){
        return this.saveBtn;
    }
    
}
