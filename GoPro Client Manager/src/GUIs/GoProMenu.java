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
    
    private Connection conn;
    private Main main;
    private String seasonId;
    
    private Insets insets = new Insets(10, 10, 10, 10);
    
    private HBox topPane = new HBox();
    private Button logoutBtn = new Button("Logout");
    private Text title = new Text();
    
    private SummerMenu summerCenterPane;
    private WinterMenu winterCenterPane;
    
    private boolean activeMenu; //0 = winter, 1 = summer
    
    public GoProMenu(){
        
    }//Default constructor
    
    public GoProMenu(Connection conn, Main main){
        this.conn = conn;
        this.main = main;
        
        
    }
    
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
        
        this.logoutBtn.setOnAction(e -> {
            this.main.switchToLoginPage();
            if (!this.activeMenu) 
                this.winterCenterPane.closeAllStages();
            
        });
        
    }
    
    private void createMenus(){
        switch(this.seasonId.charAt(0)){
            case 'W':
            {
                winterCenterPane = new WinterMenu(this.conn, this.seasonId);
                this.setCenter(winterCenterPane);
                this.activeMenu = false;
            }break;
            case 'S':
            {
                summerCenterPane = new SummerMenu(this.conn, this.seasonId);
                this.setCenter(summerCenterPane);
                this.activeMenu = true;
            }break;
            default:System.out.println("Error in creatMenus GoProMenu line 66");
        }
    }
    
    public void setSeasonId(String seasonId){
        this.seasonId = seasonId;
        setTopPane();
        createMenus();
    }
    
    
    
}
