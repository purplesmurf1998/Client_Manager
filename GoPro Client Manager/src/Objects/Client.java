/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Objects;

import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 *
 * @author purplesmurf
 */
public class Client {
    
    private int id;
    private Text address = new Text();
    private Text status = new Text();
    private Text name = new Text();
    private Text total = new Text();
    private Text phone = new Text();
    private Text comment = new Text();
    private String city;
    
    
    public Client(int id, String address, int status, String name, double total, String phone, String comment, String city){
        this.city = city;
        this.id = id;
        this.address.setText(address);
        if (status == 0)
            this.status.setText("RESIDENTIAL");
        else
            this.status.setText("COMMERCIAL");
        this.name.setText(name);
        this.total.setText("$" + String.format("%.2f", total));
        this.phone.setText(phone);
        this.comment.setText(comment);
        /*
        this.address.setFont(Font.font("Gotham"));
        this.status.setFont(Font.font("Gotham"));
        this.name.setFont(Font.font("Gotham"));
        this.total.setFont(Font.font("Gotham"));
        this.phone.setFont(Font.font("Gotham"));
        this.comment.setFont(Font.font("Gotham"));
        */
    }
    
    //Set methods
    public void setId(int id){
        this.id = id;
    }
    public void setAddress(Text address){
        this.address = address;
    }
    public void setStatus(Text status){
        this.status = status;
    }
    public void setName(Text name){
        this.name = name;
    }
    public void setTotal(Text total){
        this.total = total;
    }
    public void setPhone(Text phone){
        this.phone = phone;
    }
    public void setComment(Text comment){
        this.comment = comment;
    }
    public void setCity(String city){
        this.city = city;
    }
    
    //Get methods
    public int getId(){
        return this.id;
    }
    public Text getAddress(){
        return this.address;
    }
    public Text getStatus(){
        return this.status;
    }
    public Text getName(){
        return this.name;
    }
    public Text getTotal(){
        return this.total;
    }
    public Text getPhone(){
        return this.phone;
    }
    public Text getComment(){
        return this.comment;
    }
    public String getCity(){
        return this.city;
    }
    
}
