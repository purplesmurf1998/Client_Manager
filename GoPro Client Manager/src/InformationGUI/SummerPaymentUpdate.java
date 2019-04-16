/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InformationGUI;

import NewClientGUI.SummerPaymentMenu;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

/**
 *
 * @author purplesmurf
 */
public class SummerPaymentUpdate extends Stage{
    
    private Connection conn;
    private int clientId;
    private String seasonId;
    
    private Scene scene;
    private BorderPane pane = new BorderPane();
    
    private SummerPaymentMenu paymentMenu;
    private HBox bottomPane = new HBox();
    private Button saveBtn = new Button("Save");
    
    public SummerPaymentUpdate(Connection conn, int clientId, String seasonId){
        this.conn = conn;
        this.clientId = clientId;
        this.seasonId = seasonId;
        
        this.paymentMenu = new SummerPaymentMenu(this.conn);
        this.paymentMenu.disableCommentsBtn();
        
        this.scene = new Scene(this.pane);
        this.setScene(scene);
        
        this.pane.setCenter(this.paymentMenu);
        this.pane.setBottom(this.bottomPane);
        
        this.bottomPane.getChildren().add(this.saveBtn);
        this.bottomPane.setAlignment(Pos.CENTER_RIGHT);
        this.bottomPane.setPadding(new Insets(5, 5, 5, 5));
        
        setUpServiceMenu();
        
    }
    
    public void savePaymentModification(){
        int mar = this.paymentMenu.getMar();
        int apr = this.paymentMenu.getApr();
        int may = this.paymentMenu.getMay();
        int jun = this.paymentMenu.getJun();
        int jul = this.paymentMenu.getJul();
        int aug = this.paymentMenu.getAug();
        int sep = this.paymentMenu.getSep();
        int oct = this.paymentMenu.getOct();
        int method = this.paymentMenu.getMethod();
        double finalTotal = this.paymentMenu.getTotal();
        double saved = this.paymentMenu.getSave();
        double plan = this.paymentMenu.getPlan();
        
        try {
            Statement st = this.conn.createStatement();
            
            String update = "update summer_payment set (total, plan, mar, apr, may, jun, jul, aug, sep, oct, method, saved) "
                    + "= (" + finalTotal + ", " + plan + ", " + mar + ", " + apr + ", " + may + ", " + jun + ", " + jul + ", " + aug + ", "
                    + sep + ", " + oct + ", " + method + ", " + saved + ") "
                    + "where id = " + this.clientId + " and season = '" + this.seasonId + "'";
            
            st.executeUpdate(update);
            
            
        }catch (SQLException ex){
            ex.printStackTrace();
        }
    }
    
    private void setUpServiceMenu(){
        try {
            Statement st = conn.createStatement();
            
            String query = "select summer_payment.mar, summer_payment.apr, summer_payment.may, summer_payment.jun, "
                    + "summer_payment.jul, summer_payment.aug, summer_payment.sep, summer_payment.oct, "
                    + "summer_payment.method, summer_services.total, summer_payment.saved, summer_payment.plan "
                    + "from summer_payment "
                    + "inner join summer_services on summer_services.id = " + this.clientId + " and summer_payment.id = " + this.clientId + " "
                    + "and summer_services.season = '" + this.seasonId + "' and summer_payment.season = '" + this.seasonId + "'";
            
            ResultSet rs = st.executeQuery(query);
            
            while (rs.next()){
                this.paymentMenu.displayServices(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getInt(4), rs.getInt(5), rs.getInt(6), rs.getInt(7), 
                        rs.getInt(8), rs.getInt(9), rs.getDouble(10), rs.getDouble(11), rs.getInt(12));
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
