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
import java.util.Date;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 *
 * @author purplesmurf
 */
public class WinterInformationMenu extends Stage{
    
    private Connection conn;
    private String seasonId;
    private int clientId;
    
    private BorderPane pane = new BorderPane();
    private GridPane centerPane = new GridPane();
    private HBox topPane = new HBox();
    private HBox bottomPane = new HBox();
    private VBox rightPane = new VBox();
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
    
    private double winterService;
    private double winterShovel;
    private double winterSalt;
    private double winterTotal;
    private LocalDate timestamp;
    private String winterServiceComments;
    
    private int winterMethod;
    private String winterPaymentComments;
    
    /****************************
     * CLASS VARIABLES
     ****************************/
    
    private final Button saveBtn = new Button("Save");
    
    public WinterInformationMenu(Connection conn, String seasonId, int clientId){
        this.conn = conn;
        this.seasonId = seasonId;
        this.clientId = clientId;
        
        
        setUpWinter();
        
    }
    
    private void setUpWinter(){
        getWinterInfo();
        
        Text addressText = new Text(this.address);
        
        TextField nameText = new TextField(this.name);
        TextField cityText = new TextField(this.city);
        TextField phoneText = new TextField(this.phone);
        TextField emailText = new TextField(this.email);
        Text statusText = new Text();
        if (this.status == 0)
            statusText.setText("Residential");
        else if (this.status == 1)
            statusText.setText("Commercial");
        
        Text serviceText = new Text("$" + String.format("%.2f", this.winterService));
        Text shovelText = new Text("$" + String.format("%.2f", this.winterShovel));
        Text saltText = new Text("$" + String.format("%.2f", this.winterSalt));
        Text totalText = new Text("$" + String.format("%.2f", this.winterTotal));
        Text timestampText = new Text(this.timestamp.toString());
        
        
        Text methodText = new Text();
        switch (this.winterMethod){
            case 0: methodText.setText("Check");break;
            case 1: methodText.setText("Credit");break;
            case 2: methodText.setText("Interact");break;
            case 3: methodText.setText("Misc");break;
            default: methodText.setText("ERROR");
        }
        
        Text[] textList = {statusText, serviceText, shovelText, saltText, totalText, timestampText, methodText};
        
        TextArea serviceCommentText = new TextArea(this.winterServiceComments);
        TextArea paymentCommentText = new TextArea(this.winterPaymentComments);
        
        serviceCommentText.setWrapText(true);
        paymentCommentText.setWrapText(true);
        
        this.scene = new Scene(this.pane);
        
        this.pane.setPadding(this.insets);
        this.pane.setTop(this.topPane);
        this.pane.setCenter(this.centerPane);
        this.pane.setBottom(this.bottomPane);
        this.pane.setRight(this.rightPane);
        
        this.topPane.setPadding(this.insets);
        this.topPane.setAlignment(Pos.CENTER);
        this.topPane.getChildren().add(addressText);
        addressText.setFont(Font.font("Rockwell", FontWeight.BOLD, 23));
        
        Text nameLbl = new Text("Name: ");
        Text cityLbl = new Text("City: ");
        Text phoneLbl = new Text("Phone: ");
        Text emailLbl = new Text("Email: ");
        Text statusLbl = new Text("Status: ");
        
        Text serviceLbl = new Text("Service: ");
        Text shovelLbl = new Text("Shovel: ");
        Text saltLbl = new Text("Salt: ");
        Text totalLbl = new Text("TOTAL: ");
        
        Text timestampLbl = new Text("Registered on the: ");
        Text methodLbl = new Text("Payment method: ");
        
        Text serviceCommentLbl = new Text("Service Comments: ");
        Text paymentCommentLbl = new Text("Payment Comments: ");
        
        
        Text[] labelList = {nameLbl, cityLbl, phoneLbl, emailLbl, statusLbl, serviceLbl, 
            shovelLbl, saltLbl, timestampLbl, methodLbl, totalLbl, serviceCommentLbl, paymentCommentLbl};
        
        for (int i = 0; i < labelList.length; i++){
            labelList[i].setFont(Font.font("Gotham"));
        }
        
        for (int i = 0; i < textList.length; i++){
            textList[i].setFont(Font.font("Gotham"));
        }
        
        this.centerPane.add(nameLbl, 0, 0);
        this.centerPane.add(nameText, 1, 0);
        this.centerPane.add(cityLbl, 0, 1);
        this.centerPane.add(cityText, 1, 1);
        this.centerPane.add(phoneLbl, 0, 2);
        this.centerPane.add(phoneText, 1, 2);
        this.centerPane.add(emailLbl, 0, 3);
        this.centerPane.add(emailText, 1, 3);
        this.centerPane.add(statusLbl, 0, 4);
        this.centerPane.add(statusText, 1, 4);
        
        this.centerPane.add(serviceLbl, 2, 0);
        this.centerPane.add(serviceText, 3, 0);
        this.centerPane.add(shovelLbl, 2, 1);
        this.centerPane.add(shovelText, 3, 1);
        this.centerPane.add(saltLbl, 2, 2);
        this.centerPane.add(saltText, 3, 2);
        this.centerPane.add(totalLbl, 2, 3);
        this.centerPane.add(totalText, 3, 3);
        this.centerPane.add(methodLbl, 2, 4);
        this.centerPane.add(methodText, 3, 4);
        this.centerPane.add(timestampLbl, 2, 5);
        this.centerPane.add(timestampText, 3, 5);
        
        this.centerPane.setAlignment(Pos.CENTER);
        this.centerPane.setVgap(10);
        this.centerPane.setHgap(10);
        this.centerPane.setPadding(this.insets);
        
        this.rightPane.setSpacing(10);
        this.rightPane.setAlignment(Pos.TOP_CENTER);
        this.rightPane.setPadding(this.insets);
        
        this.rightPane.getChildren().addAll(serviceCommentLbl, serviceCommentText, paymentCommentLbl, paymentCommentText);
        
        serviceCommentText.setMinSize(200, 100);
        serviceCommentText.setMaxSize(200, 100);
        paymentCommentText.setMinSize(200, 100);
        paymentCommentText.setMaxSize(200, 100);
        
        this.bottomPane.setPadding(this.insets);
        this.bottomPane.setAlignment(Pos.CENTER_RIGHT);
        
        this.bottomPane.getChildren().add(this.saveBtn);
        
        this.saveBtn.setOnAction(e -> {
            this.name = nameText.getText();
            this.city = cityText.getText();
            this.phone = phoneText.getText();
            this.email = emailText.getText();
            
            this.winterPaymentComments = paymentCommentText.getText();
            this.winterServiceComments = serviceCommentText.getText();
            
            saveWinterInfo();
            this.close();
        });
        
        this.setScene(this.scene);
    }
    
    private void getWinterInfo(){
        
        try {
            //get client information
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
            //get winter services
            query = "select * from winter_services where id = " + this.clientId + " and season = '" + this.seasonId + "'";
            
            rs = st.executeQuery(query);
            while (rs.next()){
                this.winterTotal = rs.getDouble(9);
                this.winterShovel = rs.getDouble(7);
                this.winterSalt = rs.getDouble(8);
                this.timestamp = rs.getObject(12, LocalDate.class);
                
                if (rs.getDouble(3) > 0)
                    this.winterService = rs.getDouble(3);
                else if (rs.getDouble(4) > 0)
                    this.winterService = rs.getDouble(4);
                else if (rs.getDouble(5) > 0)
                    this.winterService = rs.getDouble(5);
                else if (rs.getDouble(6) > 0)
                    this.winterService = rs.getDouble(6);
                else
                    this.winterService = -1;
                
                this.winterServiceComments = rs.getString(10);
            }
            rs.close();
            
            //get winter payment
            query = "select method, comments from winter_payment where id = " + this.clientId + " and season = '" + this.seasonId + "'";
            
            rs = st.executeQuery(query);
            while (rs.next()){
                this.winterMethod = rs.getInt(1);
                this.winterPaymentComments = rs.getString(2);
            }
            rs.close();
            
            
        }catch (SQLException ex){
            System.out.println(ex.getMessage());
        }
    }
    
    private void saveWinterInfo(){
        try {
            
            Statement st = this.conn.createStatement();
            
            
            String update = "update client_information "
                    + "set (name, city, phone, email) "
                    + "= ('" + this.name + "', '" + this.city + "', '" + this.phone + "', '" + this.email + "') "
                    + "where id = " + this.clientId;
            
            st.executeUpdate(update);
            
            for (int i = 0; i < this.winterPaymentComments.length(); i++){
                if (this.winterPaymentComments.charAt(i) == '\''){
                    this.winterPaymentComments = this.winterPaymentComments.substring(0, i) + "'" + this.winterPaymentComments.substring(i, this.winterPaymentComments.length());
                    i++;
                }
            }
            
            for (int i = 0; i < this.winterServiceComments.length(); i++){
                if (this.winterServiceComments.charAt(i) == '\''){
                    this.winterServiceComments = this.winterServiceComments.substring(0, i) + "'" + this.winterServiceComments.substring(i, this.winterServiceComments.length());
                    i++;
                }
            }
            
            update = "update winter_services "
                    + "set comments = '" + this.winterServiceComments + "' "
                    + "where id = " + this.clientId + " and season = '" + this.seasonId + "'";
            
            st.executeUpdate(update);
            
            update = "update winter_payment "
                    + "set comments = '" + this.winterPaymentComments + "' "
                    + "where id = " + this.clientId + " and season = '" + this.seasonId + "'";
            
            st.executeUpdate(update);
            
            st.close();
            
        }catch (SQLException ex){
            System.out.println(ex.getMessage());
        }
    }
}
