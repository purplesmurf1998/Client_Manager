/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUIs;

import Application.Main;
import NewClientGUI.NewClientMenu;
import java.sql.Connection;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 *
 * @author purplesmurf
 */
public class GoProMenu extends BorderPane{
    
    private Connection conn;//connection to the database
    private Main main;//pointer to the main class for when user logs out
    private String seasonId;//seasonId for the session
    
    //Attributes for menu display
    private Insets insets = new Insets(10, 10, 10, 10);
    private HBox topPane = new HBox();//Pane for the top pane of the borderpane. Contains the logout button and the title
    private Button logoutBtn = new Button("Logout");//logout button for when user wants to go back to login page
    private Text title = new Text();
    
    //The different menus that can be displayed
    private SummerMenu summerMenu;//menu for summer season
    private WinterMenu winterMenu;//menu for winter season
    
    //Property to know which menu is currently active
    private boolean activeMenu; //false = winter, true = summer
    
    /**
     * Main constructor
     * @param conn
     * @param main
     */
    public GoProMenu(Connection conn, Main main){
        this.conn = conn;
        this.main = main;
    }
    
    //sets the layout of the top pane of the border pane
    private void setTopPane(){
        this.setTop(this.topPane);
        
        this.topPane.getChildren().addAll(this.logoutBtn, this.title);
        this.topPane.setPadding(insets);
        this.topPane.setSpacing(15);
        this.topPane.setAlignment(Pos.CENTER_LEFT);
        
        this.logoutBtn.setFont(Font.font("Rockwell"));
        
        switch(this.seasonId.charAt(0)){
            case 'W': this.title.setText("Winter " + seasonId.substring(1));break;
            case 'S': this.title.setText("Summer " + seasonId.substring(1));break;
            default: this.title.setText("Uh Oh...something happenned...call Cedrik!!");
        }
        this.title.setFont(Font.font("Rockwell"));
        
    }
    
    //sets the action of all the buttons in the pane
    private void setButtonAction(){
        //Logout and switch to login menu
        this.logoutBtn.setOnAction(e -> {
            this.main.switchToLoginPageFromClient();
            if (!this.activeMenu) 
                this.winterMenu.closeAllStages();
            else{
                //close all stages for summerCenterPane
            }
        });
    }
    
    /*
    * Creates the menu that will be used as the center pane.
    * Depending on the seasonId provided by the setSeasonId() method, the summer or winter menu will be created
    */
    private void createMenus(){
        switch(this.seasonId.charAt(0)){
            case 'W':
            {
                winterMenu = new WinterMenu(this.conn, this.seasonId);
                this.setCenter(winterMenu);
                this.activeMenu = false;
            }break;
            case 'S':
            {
                summerMenu = new SummerMenu(this.conn, this.seasonId);
                this.setCenter(summerMenu);
                this.activeMenu = true;
            }break;
            default:System.out.println("Error in creatMenus GoProMenu line 66");
        }
    }
    
    /**
     * Prompt for the seasonId that will be used throughout the session
     * set it, set the top pane, create the menu and activate the button action to logout
     * @param seasonId
     */
    public void setSeasonId(String seasonId){
        this.seasonId = seasonId;
        setTopPane();
        createMenus();
        setButtonAction();
    }
    
    
    
}
