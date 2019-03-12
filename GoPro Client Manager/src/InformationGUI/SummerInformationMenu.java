/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InformationGUI;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 *
 * @author purplesmurf
 */
public class SummerInformationMenu extends Stage{
    
    private Connection conn;
    private String seasonId;
    private int clientId;
    
    private BorderPane pane = new BorderPane();
    private HBox topPane = new HBox();
    private GridPane leftPane = new GridPane();
    private GridPane centerPane = new GridPane();
    private VBox rightPane = new VBox();
    private HBox bottomPane = new HBox();
    
    private Scene scene;
    
    private Insets insets = new Insets(5, 5, 5, 5);
    
    /****************************
     * CLIENT VARIABLES
     ****************************/
    
    private String address;
    private String city;
    private String name;
    private String phone;
    private String email;
    private int status;
    
    /****************************
     * WINTER VARIABLES
     ****************************/
    
    private double lawn;
    private double spring;
    private double fall;
    private double weedTreatment;
    private double aerationSpring;
    private double aerationFall;
    private double spider;
    private double weeding;
    private double hedges;
    private double fertilizer;
    private double worms;
    private double soil;
    private double seeding;
    
    private double[] serviceList = new double[13];
    
    private LocalDate timestamp;
    private String serviceComment;
    private int method;
    private String paymentComment;
    private double total;
    
    private final Button saveBtn = new Button("Save");
    
    public SummerInformationMenu(Connection conn, String seasonId, int clientId){
        this.conn = conn;
        this.seasonId = seasonId;
        this.clientId = clientId;
        
        try {
            setUpSummer();
        }
        catch (SQLException ex){
            System.out.println(ex.getMessage());
        }
    }
    
    private void getSummerInfo() throws SQLException{
        Statement st = this.conn.createStatement();
            
        String query = "select * from client_information where id = " + this.clientId;
            
        ResultSet rs = st.executeQuery(query);
        
        while (rs.next()){
            this.address = rs.getString(2);
            this.city = rs.getString(3);
            this.name = rs.getString(4);
            this.phone = rs.getString(5);
            this.email = rs.getString(6);
            this.status = rs.getInt(7);
        }
        rs.close();
        
        //get summer services
        query = "select * from summer_services where id = " + this.clientId + " and season = '" + this.seasonId + "'";
            
        rs = st.executeQuery(query);
        while (rs.next()){
            for (int i = 3, j = 0; i < 16; i++, j++)
                this.serviceList[j] = rs.getDouble(i);
                
                
            this.timestamp = rs.getObject(19, LocalDate.class);
                
            this.serviceComment = rs.getString(17);
        }
        rs.close();
        
        //get summer payment
        query = "select method, comments, total from summer_payment where id = " + this.clientId + " and season = '" + this.seasonId + "'";
            
        rs = st.executeQuery(query);
        while (rs.next()){
            this.method = rs.getInt(1);
            this.paymentComment = rs.getString(2);
            this.total = rs.getDouble(3);
        }
        rs.close();
            
            
    }
    
    private void setUpSummer() throws SQLException{
        getSummerInfo();
        
        this.scene = new Scene(this.pane);
        
        this.pane.setPadding(this.insets);
        this.pane.setTop(this.topPane);
        this.pane.setLeft(this.leftPane);
        this.pane.setCenter(this.centerPane);
        this.pane.setBottom(this.bottomPane);
        this.pane.setRight(this.rightPane);
        
        Text addressText = new Text(this.address);
        
        //LEFT PANE SET UP
        TextField nameText = new TextField(this.name);
        TextField cityText = new TextField(this.city);
        TextField phoneText = new TextField(this.phone);
        TextField emailText = new TextField(this.email);
        Text statusText = new Text();
        if (this.status == 0)
            statusText.setText("Residential");
        else if (this.status == 1)
            statusText.setText("Commercial");
        
        Text nameLbl = new Text("Name: ");
        Text cityLbl = new Text("City: ");
        Text phoneLbl = new Text("Phone: ");
        Text emailLbl = new Text("Email: ");
        Text statusLbl = new Text("Status: ");
        
        this.leftPane.add(nameLbl, 0, 0);
        this.leftPane.add(nameText, 1, 0);
        this.leftPane.add(cityLbl, 0, 1);
        this.leftPane.add(cityText, 1, 1);
        this.leftPane.add(phoneLbl, 0, 2);
        this.leftPane.add(phoneText, 1, 2);
        this.leftPane.add(emailLbl, 0, 3);
        this.leftPane.add(emailText, 1, 3);
        this.leftPane.add(statusLbl, 0, 4);
        this.leftPane.add(statusText, 1, 4);
        
        this.leftPane.setAlignment(Pos.CENTER);
        this.leftPane.setVgap(10);
        this.leftPane.setHgap(10);
        this.leftPane.setPadding(this.insets);
        
        //CENTER PANE SET UP
        int row = 0;
        for (int i = 0, col = 0; i < this.serviceList.length; i++){
            if (this.serviceList[i] > 0){
                switch (i){
                    case 0: {//lawn
                        this.centerPane.add(new Text("Lawn: "), col, row);
                        this.centerPane.add(new Text("$" + String.format("%.2f", this.serviceList[i])), col + 1, row);
                        row++;
                    }break;
                    case 1: {//spring
                        this.centerPane.add(new Text("Spring: "), col, row);
                        this.centerPane.add(new Text("$" + String.format("%.2f", this.serviceList[i])), col + 1, row);
                        row++;
                    }break;
                    case 2: {//fall
                        this.centerPane.add(new Text("Fall: "), col, row);
                        this.centerPane.add(new Text("$" + String.format("%.2f", this.serviceList[i])), col + 1, row);
                        row++;
                    }break;
                    case 3: {//weedTreatment
                        this.centerPane.add(new Text("Weed Treatment: "), col, row);
                        this.centerPane.add(new Text("$" + String.format("%.2f", this.serviceList[i])), col + 1, row);
                        row++;
                    }break;
                    case 4: {//aerationSpring
                        this.centerPane.add(new Text("Aeration Spring: "), col, row);
                        this.centerPane.add(new Text("$" + String.format("%.2f", this.serviceList[i])), col + 1, row);
                        row++;
                    }break;
                    case 5: {//aerationFall
                        this.centerPane.add(new Text("Aeration Fall: "), col, row);
                        this.centerPane.add(new Text("$" + String.format("%.2f", this.serviceList[i])), col + 1, row);
                        row++;
                    }break;
                    case 6: {//spider
                        this.centerPane.add(new Text("Spider: "), col, row);
                        this.centerPane.add(new Text("$" + String.format("%.2f", this.serviceList[i])), col + 1, row);
                        row++;
                    }break;
                    case 7: {//weeding
                        this.centerPane.add(new Text("Weeding: "), col, row);
                        this.centerPane.add(new Text("$" + String.format("%.2f", this.serviceList[i])), col + 1, row);
                        row++;
                    }break;
                    case 8: {//hedges
                        this.centerPane.add(new Text("Hedges: "), col, row);
                        this.centerPane.add(new Text("$" + String.format("%.2f", this.serviceList[i])), col + 1, row);
                        row++;
                    }break;
                    case 9: {//fertilizer
                        this.centerPane.add(new Text("Fertilizer: "), col, row);
                        this.centerPane.add(new Text("$" + String.format("%.2f", this.serviceList[i])), col + 1, row);
                        row++;
                    }break;
                    case 10: {//worms
                        this.centerPane.add(new Text("Worms: "), col, row);
                        this.centerPane.add(new Text("$" + String.format("%.2f", this.serviceList[i])), col + 1, row);
                        row++;
                    }break;
                    case 11: {//soil
                        this.centerPane.add(new Text("Soil: "), col, row);
                        this.centerPane.add(new Text("$" + String.format("%.2f", this.serviceList[i])), col + 1, row);
                        row++;
                    }break;
                    case 12: {//seeding
                        this.centerPane.add(new Text("Seeding: "), col, row);
                        this.centerPane.add(new Text("$" + String.format("%.2f", this.serviceList[i])), col + 1, row);
                        row++;
                    }break;
                    
                }
            }
        }
        
        this.centerPane.setAlignment(Pos.CENTER);
        this.centerPane.setVgap(10);
        this.centerPane.setHgap(10);
        this.centerPane.setPadding(this.insets);
        
        row++;
        this.centerPane.add(new Text("TOTAL: "), 0, row);
        this.centerPane.add(new Text("$" + String.format("%.2f", this.total)), 1, row);
        row++;
        
        this.centerPane.add(new Text("Payment Method: "), 0, row);
        switch (this.method){
            case 0: {
                this.centerPane.add(new Text("Check"), 1, row);
            }break;
            case 1: {
                this.centerPane.add(new Text("Credit"), 1, row);
            }break;
            case 2: {
                this.centerPane.add(new Text("Interact"), 1, row);
            }break;
            case 3: {
                this.centerPane.add(new Text("Misc"), 1, row);
            }break;
        }
        row++;
        
        this.centerPane.add(new Text("Registered on the: "), 0, row);
        this.centerPane.add(new Text(this.timestamp.toString()), 1, row);
        row++;
        
        //RIGHT PANE SET UP
        this.rightPane.setSpacing(10);
        this.rightPane.setAlignment(Pos.TOP_CENTER);
        this.rightPane.setPadding(this.insets);
        
        TextArea serviceCommentText = new TextArea(this.serviceComment);
        TextArea paymentCommentText = new TextArea(this.paymentComment);
        
        serviceCommentText.setWrapText(true);
        paymentCommentText.setWrapText(true);
        
        Text serviceCommentLbl = new Text("Service Comments: ");
        Text paymentCommentLbl = new Text("Payment Comments: ");
        
        this.rightPane.getChildren().addAll(serviceCommentLbl, serviceCommentText, paymentCommentLbl, paymentCommentText);
        
        serviceCommentText.setMinSize(200, 100);
        serviceCommentText.setMaxSize(200, 100);
        paymentCommentText.setMinSize(200, 100);
        paymentCommentText.setMaxSize(200, 100);
        
        
        this.setScene(this.scene);
    }
    
}
