/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Application;

import GUIs.GoProMenu;
import GUIs.LoginPage;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Date;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author purplesmurf
 */
public class Main extends Application {
    
    private final String local_url = "jdbc:postgresql://localhost/clientmanager";
    private final String local_user = "alexanedubois";
    private final String local_password = "aloha";
    
    private final String amazon_url = "jdbc:postgresql://clientmanager.cpnxyhp5tfh2.us-east-2.rds.amazonaws.com/client_manager";
    private final String amazon_user = "purplesmurf";
    private final String amazon_password = "Linken14";
    
    private Connection local_connection = null;
    private Connection amazon_connection = null;
    
    private LoginPage loginPage;
    private GoProMenu mainMenu;
    
    private Scene scene;
        
    @Override
    public void start(Stage primaryStage) {
        
        
        connectDB();
        System.out.println("Checking for backup");
        if (checkForBackup()){
            System.out.println("Check returned true, backup database");
            backupDatabase();
        }
        
        loginPage = new LoginPage(local_connection, this);
        
        setUpScene(primaryStage);
        primaryStage.show();
        
    }
    
    //Set up the scene for the main stage
    private void setUpScene(Stage stage){
        scene = new Scene(loginPage, 1200, 700);
        stage.setScene(scene);
        stage.setTitle("GoPro Client Manager");
    }
    
    //Connect to the database
    private void connectDB(){
        try {
            this.local_connection = DriverManager.getConnection(local_url, local_user, local_password);
            System.out.println("Is this even doing anything?");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    private void backupDatabase(){
        
        try {
            
            this.amazon_connection = DriverManager.getConnection(amazon_url, amazon_user, amazon_password);
            System.out.println("Connected to backup database");
            
            //Clear backupdatabase
            Statement amazon_statement = this.amazon_connection.createStatement();
            
            String clear = "truncate client_information cascade";
            amazon_statement.executeUpdate(clear);
            
            amazon_statement.close();
            
            Statement local_statement = this.local_connection.createStatement();
            PreparedStatement st_2; 
            ResultSet rs;
            String query = "SELECT * FROM client_information";
            
            //"INSERT INTO client_information values (?, ?, ?, ?, ?, ?, ?, ?)"
            
            rs = local_statement.executeQuery(query);
            System.out.println("Copying client_information records...");
            while (rs.next()){
                st_2 = this.amazon_connection.prepareStatement("INSERT INTO client_information values (?, ?, ?, ?, ?, ?, ?, ?)");
                
                st_2.setInt(1, rs.getInt(1));
                st_2.setString(2, rs.getString(2));
                st_2.setString(3, rs.getString(3));
                st_2.setString(4, rs.getString(4));
                st_2.setString(5, rs.getString(5));
                st_2.setString(6, rs.getString(6));
                st_2.setInt(7, rs.getInt(7));
                st_2.setInt(8, rs.getInt(8));
                
                st_2.executeUpdate();
            }
            
            rs.close();
            System.out.println("Copying completed.");
            
            
            query = "SELECT * FROM summer_payment";
            
            //"INSERT INTO summer_payment values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"
            
            rs = local_statement.executeQuery(query);
            System.out.println("Copying summer_payment records...");
            while (rs.next()){
                st_2 = this.amazon_connection.prepareStatement("INSERT INTO summer_payment values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
                
                st_2.setInt(1, rs.getInt(1));
                st_2.setString(2, rs.getString(2));
                st_2.setDouble(3, rs.getDouble(3));
                st_2.setInt(4, rs.getInt(4));
                st_2.setInt(5, rs.getInt(5));
                st_2.setInt(6, rs.getInt(6));
                st_2.setInt(7, rs.getInt(7));
                st_2.setInt(8, rs.getInt(8));
                st_2.setInt(9, rs.getInt(9));
                st_2.setInt(10, rs.getInt(10));
                st_2.setInt(11, rs.getInt(11));
                st_2.setInt(12, rs.getInt(12));
                st_2.setInt(13, rs.getInt(13));
                st_2.setString(14, rs.getString(14));
                st_2.setDouble(15, rs.getDouble(15));
                
                st_2.executeUpdate();
            }
            
            rs.close();
            System.out.println("Copying complete.");
            
            
            query = "SELECT * FROM summer_services";
            
            //"INSERT INTO summer_services values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"
            
            rs = local_statement.executeQuery(query);
            System.out.println("Copying summer_services records...");
            while (rs.next()){
                st_2 = this.amazon_connection.prepareStatement("INSERT INTO summer_services values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
                
                st_2.setInt(1, rs.getInt(1));
                st_2.setString(2, rs.getString(2));
                st_2.setDouble(3, rs.getDouble(3));
                st_2.setDouble(4, rs.getDouble(4));
                st_2.setDouble(5, rs.getDouble(5));
                st_2.setDouble(6, rs.getDouble(6));
                st_2.setDouble(7, rs.getDouble(7));
                st_2.setDouble(8, rs.getDouble(8));
                st_2.setDouble(9, rs.getDouble(9));
                st_2.setDouble(10, rs.getDouble(10));
                st_2.setDouble(11, rs.getDouble(11));
                st_2.setDouble(12, rs.getDouble(12));
                st_2.setDouble(13, rs.getDouble(13));
                st_2.setDouble(14, rs.getDouble(14));
                st_2.setDouble(15, rs.getDouble(15));
                st_2.setDouble(16, rs.getDouble(16));
                st_2.setString(17, rs.getString(17));
                st_2.setString(18, rs.getString(18));
                st_2.setDate(19, rs.getDate(19));
                
                st_2.executeUpdate();
            }
            
            rs.close();
            System.out.println("Copying complete.");
            
            local_statement.executeUpdate("update backup_timestamp set last_backup = current_timestamp");
            
        }catch (SQLException ex){
            ex.printStackTrace();
        }
        
    }
    private boolean checkForBackup(){
        Calendar calendar = Calendar.getInstance();
        Date date_now = new Date();
        Date date_backup = null;
        
        calendar.setTime(date_now);
        
        int week_now = calendar.get(Calendar.WEEK_OF_YEAR);
        int week_backup = week_now;
        
        try{
            String query = "select last_backup from backup_timestamp";
            Statement st = this.local_connection.createStatement();
            ResultSet rs = st.executeQuery(query);
            
            while (rs.next()){
                date_backup = rs.getDate(1);
            }
            
            calendar.setTime(date_backup);
            week_backup = calendar.get(Calendar.WEEK_OF_YEAR);
            System.out.println();
            /*
            if (week_now != week_backup)
                return true;
            else
                return false;
            */
            return true;
            
        }catch (SQLException ex){
            System.out.println("Something went wrong in try catch");
            ex.printStackTrace();
        }
        System.out.println("returning false for some reason");
        return false;
    }
    /**
     * Switch back to the login page when the logout button is pressed
     */
    public void switchToLoginPageFromClient(){
        this.scene.setRoot(this.loginPage);
    }
    /**
     * Switch the scene from the login menu to the main menu, where the whole application resides
     * @param seasonId
     */
    public void switchToClientFromLogin(String seasonId){
        mainMenu = new GoProMenu(local_connection, this);
        this.mainMenu.setSeasonId(seasonId);
        this.scene.setRoot(this.mainMenu);
    }

    /**
     * @param args the command line arguments
     */
    static void main(String[] args) {
        launch(args);
    }
    
}
