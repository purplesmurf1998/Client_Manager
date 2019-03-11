/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package NewClientGUI;

import com.sun.javafx.scene.control.skin.ComboBoxListViewSkin;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 *
 * @author purplesmurf
 */
public class ClientInfoMenu extends GridPane{
    
    private Connection conn;
    
    private final Text addressTxt = new Text("Address: ");
    private final Text nameTxt = new Text("Full Name: ");
    private final Text phoneTxt = new Text("Phone: ");
    private final Text emailTxt = new Text("Email: ");
    private final Text cityTxt = new Text("City: ");
    
    private final Text[] textList = {addressTxt, nameTxt, phoneTxt, emailTxt, cityTxt};
    
    private final ComboBox<String> addressFld = new ComboBox<>();
    private final TextField nameFld = new TextField();
    private final TextField phoneFld = new TextField();
    private final TextField emailFld = new TextField();
    private final TextField cityFld = new TextField();
    private final ToggleButton resBtn = new ToggleButton("Residential");
    private final ToggleButton comBtn = new ToggleButton("Commercial");
    
    private final ToggleGroup statusGroup = new ToggleGroup();
    
    private int clientId;//id of client that was selected in infoPage
    private boolean editable = false;
    
    public ClientInfoMenu(){
        
    }
    
    public ClientInfoMenu(Connection conn){
        this.conn = conn;
        
        setPane();
    }
    
    private void setPane(){
        setAddressFld();
        
        this.setPadding(new Insets(15, 15, 15, 15));
        this.setAlignment(Pos.CENTER);
        this.setHgap(30);
        this.setVgap(30);
        
        this.add(this.addressTxt, 0, 0);
        this.add(this.addressFld, 1, 0);
        this.add(this.nameTxt, 0, 1);
        this.add(this.nameFld, 1, 1);
        this.add(this.phoneTxt, 0, 2);
        this.add(this.phoneFld, 1, 2);
        this.add(this.emailTxt, 0, 3);
        this.add(this.emailFld, 1, 3);
        this.add(this.cityTxt, 0, 4);
        this.add(this.cityFld, 1, 4);
        this.add(this.resBtn, 0, 5);
        this.add(this.comBtn, 1, 5);
        
        for (int i = 0; i < textList.length; i++){
            textList[i].setFont(Font.font("Rockwell", 18));
        }
        
        
        
        this.resBtn.setToggleGroup(statusGroup);
        this.comBtn.setToggleGroup(statusGroup);
        
        this.resBtn.setFont(Font.font("Rockwell"));
        this.comBtn.setFont(Font.font(("Rockwell")));
        
        this.resBtn.setFocusTraversable(false);
        this.comBtn.setFocusTraversable(false);
        this.resBtn.setSelected(true);
        
        this.resBtn.setOnAction(e -> {
            if (!this.resBtn.isSelected())
                this.resBtn.setSelected(true);
        });
        
        this.comBtn.setOnAction(e -> {
            if (!this.comBtn.isSelected())
                this.comBtn.setSelected(true);
        });
        
        
    }
    
    
    private void setAddressFld(){
        this.addressFld.setEditable(true);
        
        ComboBoxListViewSkin comboBoxListViewSkin = new ComboBoxListViewSkin(this.addressFld);
        comboBoxListViewSkin.getPopupContent().addEventFilter(KeyEvent.ANY, (event) -> {
            if( event.getCode() == KeyCode.SPACE ) {
                event.consume();
            }
        });
        this.addressFld.setSkin(comboBoxListViewSkin);
        
        try{
            Statement st = conn.createStatement();
            String query = "SELECT address FROM client_information";
            ResultSet rs = st.executeQuery(query);
        
            while (rs.next()){
                this.addressFld.getItems().add(rs.getString(1));
            }
        } catch (SQLException ex) {
            Logger.getLogger(NewClientMenu.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        this.addressFld.setOnKeyReleased(e -> {
            if (this.addressFld.getSelectionModel().isEmpty()){
                if (e.getCode().isDigitKey() || 
                        e.getCode().isLetterKey() || 
                        e.getCode().equals(KeyCode.BACK_SPACE) || 
                        e.getCode().equals(KeyCode.SUBTRACT) || 
                        e.getCode().equals(KeyCode.SPACE)){
                    
                    this.addressFld.hide();
                    this.addressFld.setVisibleRowCount(this.addressFld.getItems().size());
                    this.addressFld.show();
                    this.addressFld.getItems().clear();
                    try {
                        String addressSearch = this.addressFld.getEditor().getText().toLowerCase();
                        
                        for (int i = 0; i < addressSearch.length(); i++){
                            if (addressSearch.charAt(i) == '\''){
                                addressSearch = addressSearch.substring(0, i) + "'" + addressSearch.substring(i, addressSearch.length());
                                i++;
                            }
                        }
                        
                        Statement st = conn.createStatement();
                        String query = "SELECT address FROM client_information WHERE LOWER(address) LIKE '" + addressSearch + "%'";
                        ResultSet rs = st.executeQuery(query);
                        
                        while (rs.next()){
                            this.addressFld.getItems().add(rs.getString(1));
                            
                        }
                        
                        
                    } catch(SQLException ex){
                        Logger.getLogger(NewClientMenu.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                }
            }
            else if (!this.addressFld.getSelectionModel().isEmpty() && e.getCode().equals(KeyCode.BACK_SPACE)){
                this.addressFld.getSelectionModel().clearSelection();
                this.addressFld.getItems().clear();
                this.addressFld.hide();
                try {
                    Statement st = conn.createStatement();
                    String query = "SELECT address FROM client_information";
                    ResultSet rs = st.executeQuery(query);
                    
                    while (rs.next()){
                        this.addressFld.getItems().add(rs.getString(1));
                    }
                    
                   
                    
                } catch (SQLException ex) {
                    Logger.getLogger(NewClientMenu.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                this.addressFld.show();
            }
           
            
            
        });
        
        this.addressFld.setOnShown(e -> {
            if (this.addressFld.getEditor().getText().length() > 0){
                this.addressFld.getItems().clear();
                try {
                    
                    String addressSearch = this.addressFld.getEditor().getText().toLowerCase();
                    
                    for (int i = 0; i < addressSearch.length(); i++){
                        if (addressSearch.charAt(i) == '\''){
                            addressSearch = addressSearch.substring(0, i) + "'" + addressSearch.substring(i, addressSearch.length());
                            i++;
                        }
                    }
                    
                    Statement st = this.conn.createStatement();
                    String query = "SELECT address FROM client_information "
                    + "WHERE LOWER(address) LIKE '" + addressSearch + "'";
                    ResultSet rs = st.executeQuery(query);
                    
                    while (rs.next()){
                        this.addressFld.getItems().add(rs.getString(1));
                    }
                    
                }
                catch (SQLException ex){
                    ex.printStackTrace();
                }
            }
        });
        
        this.addressFld.setOnAction(e -> {
            
            if (!this.addressFld.getSelectionModel().isEmpty()){//item was selected
                
                try {
                    String address = this.addressFld.getSelectionModel().getSelectedItem().toLowerCase();
                    
                    for (int i = 0; i < address.length(); i++){
                        if (address.charAt(i) == '\''){
                            address = address.substring(0, i) + "'" + address.substring(i, address.length());
                            i++;
                        }
                    }
                    
                    Statement st = conn.createStatement();
                    String query = "SELECT * FROM client_information WHERE LOWER(address) = '" + address + "'";
                    ResultSet rs = st.executeQuery(query);
                    
                    while (rs.next()){
                        this.clientId = rs.getInt(1);
                        this.nameFld.setText(rs.getString(4));
                        this.phoneFld.setText(rs.getString(5));
                        this.emailFld.setText(rs.getString(6));
                        this.cityFld.setText(rs.getString(3));
                        
                        if (rs.getInt(7) == 0)
                            this.resBtn.setSelected(true);
                        else if (rs.getInt(7) == 1)
                            this.comBtn.setSelected(true);
                    }
                } catch(SQLException ex){
                    Logger.getLogger(NewClientMenu.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                
                
            }
            else if (this.addressFld.getSelectionModel().isEmpty()){//item was deleted
                this.addressFld.getItems().clear();
                try {
                    Statement st = conn.createStatement();
                    String query = "SELECT address FROM client_information";
                    ResultSet rs = st.executeQuery(query);
                    
                    
                    while (rs.next()){
                        this.addressFld.getItems().add(rs.getString(1));
                    }
                    
                } catch (SQLException ex){
                    Logger.getLogger(NewClientMenu.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                this.nameFld.setText("");
                this.phoneFld.setText("");
                this.emailFld.setText("");
                this.cityFld.setText("");
                this.resBtn.setSelected(true);
                
                
            }
            
            
        });
        
    }
    
    public void resetMenu(){
        //clear address field
        if (!this.addressFld.getSelectionModel().isEmpty())//item selected
            this.addressFld.getSelectionModel().clearSelection();
        else
            this.addressFld.getEditor().clear();
        
        this.addressFld.getItems().clear();
        try {
            Statement st = conn.createStatement();
            String query = "SELECT address FROM client_information";
            ResultSet rs = st.executeQuery(query);
                    
                    
            while (rs.next()){
                this.addressFld.getItems().add(rs.getString(1));
            }
        } catch (SQLException ex){
            Logger.getLogger(NewClientMenu.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        //clear name field
        this.nameFld.clear();
        
        //clear phone field
        this.phoneFld.clear();
        
        //clear backup phone field
        this.emailFld.clear();
        
        //clear city field
        this.cityFld.clear();
        
        //clear status options
        this.resBtn.setSelected(true);
    }
    
    public int getClientId(){
        return this.clientId;
    }
    
    public String getClientAddress(){
        if (!this.addressFld.getSelectionModel().isEmpty())
            return this.addressFld.getSelectionModel().getSelectedItem();
        else{
            this.clientId = -1;
            return this.addressFld.getEditor().getText();
        }
    }
    
    public String getClientName(){
        return this.nameFld.getText();
    }
    
    public String getClientPhone(){
        return this.phoneFld.getText();
    }
    
    public String getClientEmail(){
        return this.emailFld.getText();
    }
    
    public String getClientCity(){
        return this.cityFld.getText();
    }
    
    public int getClientStatus(){
        if (this.resBtn.isSelected())
            return 0;
        else if (this.comBtn.isSelected())
            return 1;
        else return -1;
    }
    
}
