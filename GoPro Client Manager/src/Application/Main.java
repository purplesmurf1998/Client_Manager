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
import java.sql.SQLException;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author purplesmurf
 */
public class Main extends Application {
    
    private final String url = "jdbc:postgresql://localhost/clientmanager";
    private final String user = "alexanedubois";
    private final String password = "aloha";
    
    private Connection conn = null;
    
    private LoginPage loginPage;
    private GoProMenu mainMenu;
    
    private Scene scene;
        
    @Override
    public void start(Stage primaryStage) {
        
        
        connectDB();
        
        loginPage = new LoginPage(conn, this);
        
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
            this.conn = DriverManager.getConnection(url, user, password);
            System.out.println("Connected to the PostgreSQL server successfully.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
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
        mainMenu = new GoProMenu(conn, this);
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
