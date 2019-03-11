/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Objects;

import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

/**
 *
 * @author purplesmurf
 */
public class Payment {
    
    private int id;
    private String addressString;
    private String nameString;
    private String methodString;
    private String totalString;
    private String comment;
    private Text mar = new Text();
    private Text apr = new Text();
    private Text may = new Text();
    private Text jun = new Text();
    private Text jul = new Text();
    private Text aug = new Text();
    private Text sep = new Text();
    private Text oct = new Text();
    private Text nov = new Text();
    private Text dec = new Text();
    private Text jan = new Text();
    private Text feb = new Text();
    private Text commentText = new Text();
    
    
    public Payment(){
        this.addressString = "";
        this.nameString = "";
        this.methodString = "";
        this.totalString = "";
        this.comment = "";
    }
    public Payment(double oct, double nov, double dec, double jan, double feb, double octSub, double novSub, double decSub, double janSub, double febSub){
        this.addressString = "";
        this.nameString = "TOTAL: ";
        this.methodString = "";
        this.totalString = "";
        this.commentText.setText("");
        
        this.oct.setText(String.format("%.2f", octSub) + " / " + String.format("%.2f", oct));
        this.oct.setFont(Font.font("Gotham"));
        
        this.nov.setText(String.format("%.2f", novSub) + " / " + String.format("%.2f", nov));
        this.nov.setFont(Font.font("Gotham"));
        
        this.dec.setText(String.format("%.2f", decSub) + " / " + String.format("%.2f", dec));
        this.dec.setFont(Font.font("Gotham"));
        
        this.jan.setText(String.format("%.2f", janSub) + " / " + String.format("%.2f", jan));
        this.jan.setFont(Font.font("Gotham"));
        
        this.feb.setText(String.format("%.2f", febSub) + " / " + String.format("%.2f", feb));
        this.feb.setFont(Font.font("Gotham"));
    }
    
    public Payment(double mar, double apr, double may, double jun, double jul, double aug, double sep, double oct, double marSub, double aprSub, double maySub, double junSub, double julSub, double augSub, double sepSub, double octSub){
        this.addressString = "";
        this.nameString = "TOTAL: ";
        this.methodString = "";
        this.totalString = "";
        this.commentText.setText("");
        
        this.mar.setText(String.format("%.2f", marSub) + " / " + String.format("%.2f", mar));
        this.mar.setFont(Font.font("Gotham"));
        
        this.apr.setText(String.format("%.2f", aprSub) + " / " + String.format("%.2f", apr));
        this.apr.setFont(Font.font("Gotham"));
        
        this.may.setText(String.format("%.2f", maySub) + " / " + String.format("%.2f", may));
        this.may.setFont(Font.font("Gotham"));
        
        this.jun.setText(String.format("%.2f", junSub) + " / " + String.format("%.2f", jun));
        this.jun.setFont(Font.font("Gotham"));
        
        this.jul.setText(String.format("%.2f", julSub) + " / " + String.format("%.2f", jul));
        this.jul.setFont(Font.font("Gotham"));
        
        this.aug.setText(String.format("%.2f", augSub) + " / " + String.format("%.2f", aug));
        this.aug.setFont(Font.font("Gotham"));
        
        this.sep.setText(String.format("%.2f", sepSub) + " / " + String.format("%.2f", sep));
        this.sep.setFont(Font.font("Gotham"));
        
        this.oct.setText(String.format("%.2f", octSub) + " / " + String.format("%.2f", oct));
        this.oct.setFont(Font.font("Gotham"));
        
    }
    
    //SUMMER
    public Payment(int id, String address, String name, int method, double total, int plan, int mar, int apr, int may, int jun, int jul, int aug, int sep, int oct, String comment){
        this.id = id;
        this.addressString = address;
        this.nameString = name;
        this.commentText.setFont(Font.font("Gotham"));
        this.comment = comment;
        if (comment.length() > 0)
            this.commentText.setText("Comment");
        
        switch (method){
            case 0: this.methodString = "CHECK";break;
            case 1: this.methodString = "CREDIT";break;
            case 2: this.methodString = "INTERACT";break;
            case 3: this.methodString = "MISC";break;
            default: this.methodString = "WTF...SOMETHING WENT WRONG";
        }
        
        this.totalString = "$" + String.format("%.2f", total);
        
        switch (mar){
            case 0: {
                this.mar.setText("");
            }break;
            case 1: {
                this.mar.setText("$" + String.format("%.2f", (total / plan)));
                this.mar.setFont(Font.font("Gotham"));
                this.mar.setFill(Color.RED);
            }break;
            case 2: {
                this.mar.setText("$" + String.format("%.2f", (total / plan)));
                this.mar.setFont(Font.font("Gotham"));
            }break;
            case 3: {
                this.mar.setText("$" + String.format("%.2f", (total / plan)));
                this.mar.setFont(Font.font("Gotham"));
                this.mar.setFill(Color.GREEN);
            }break;
        }
        
        switch (apr){
            case 0: {
                this.apr.setText("");
            }break;
            case 1: {
                this.apr.setText("$" + String.format("%.2f", (total / plan)));
                this.apr.setFont(Font.font("Gotham"));
                this.apr.setFill(Color.RED);
            }break;
            case 2: {
                this.apr.setText("$" + String.format("%.2f", (total / plan)));
                this.apr.setFont(Font.font("Gotham"));
            }break;
            case 3: {
                this.apr.setText("$" + String.format("%.2f", (total / plan)));
                this.apr.setFont(Font.font("Gotham"));
                this.apr.setFill(Color.GREEN);
            }break;
        }
        
        switch (may){
            case 0: {
                this.may.setText("");
            }break;
            case 1: {
                this.may.setText("$" + String.format("%.2f", (total / plan)));
                this.may.setFont(Font.font("Gotham"));
                this.may.setFill(Color.RED);
            }break;
            case 2: {
                this.may.setText("$" + String.format("%.2f", (total / plan)));
                this.may.setFont(Font.font("Gotham"));
            }break;
            case 3: {
                this.may.setText("$" + String.format("%.2f", (total / plan)));
                this.may.setFont(Font.font("Gotham"));
                this.may.setFill(Color.GREEN);
            }break;
        }
        
        switch (jun){
            case 0: {
                this.jun.setText("");
            }break;
            case 1: {
                this.jun.setText("$" + String.format("%.2f", (total / plan)));
                this.jun.setFont(Font.font("Gotham"));
                this.jun.setFill(Color.RED);
            }break;
            case 2: {
                this.jun.setText("$" + String.format("%.2f", (total / plan)));
                this.jun.setFont(Font.font("Gotham"));
            }break;
            case 3: {
                this.jun.setText("$" + String.format("%.2f", (total / plan)));
                this.jun.setFont(Font.font("Gotham"));
                this.jun.setFill(Color.GREEN);
            }break;
        }
        
        switch (jul){
            case 0: {
                this.jul.setText("");
            }break;
            case 1: {
                this.jul.setText("$" + String.format("%.2f", (total / plan)));
                this.jul.setFont(Font.font("Gotham"));
                this.jul.setFill(Color.RED);
            }break;
            case 2: {
                this.jul.setText("$" + String.format("%.2f", (total / plan)));
                this.jul.setFont(Font.font("Gotham"));
            }break;
            case 3: {
                this.jul.setText("$" + String.format("%.2f", (total / plan)));
                this.jul.setFont(Font.font("Gotham"));
                this.jul.setFill(Color.GREEN);
            }break;
        }
        
        switch (aug){
            case 0: {
                this.aug.setText("");
            }break;
            case 1: {
                this.aug.setText("$" + String.format("%.2f", (total / plan)));
                this.aug.setFont(Font.font("Gotham"));
                this.aug.setFill(Color.RED);
            }break;
            case 2: {
                this.aug.setText("$" + String.format("%.2f", (total / plan)));
                this.aug.setFont(Font.font("Gotham"));
            }break;
            case 3: {
                this.aug.setText("$" + String.format("%.2f", (total / plan)));
                this.aug.setFont(Font.font("Gotham"));
                this.aug.setFill(Color.GREEN);
            }break;
        }
        
        switch (sep){
            case 0: {
                this.sep.setText("");
            }break;
            case 1: {
                this.sep.setText("$" + String.format("%.2f", (total / plan)));
                this.sep.setFont(Font.font("Gotham"));
                this.sep.setFill(Color.RED);
            }break;
            case 2: {
                this.sep.setText("$" + String.format("%.2f", (total / plan)));
                this.sep.setFont(Font.font("Gotham"));
            }break;
            case 3: {
                this.sep.setText("$" + String.format("%.2f", (total / plan)));
                this.sep.setFont(Font.font("Gotham"));
                this.sep.setFill(Color.GREEN);
            }break;
        }
        
        switch (oct){
            case 0: {
                this.oct.setText("");
            }break;
            case 1: {
                this.oct.setText("$" + String.format("%.2f", (total / plan)));
                this.oct.setFont(Font.font("Gotham"));
                this.oct.setFill(Color.RED);
            }break;
            case 2: {
                this.oct.setText("$" + String.format("%.2f", (total / plan)));
                this.oct.setFont(Font.font("Gotham"));
            }break;
            case 3: {
                this.oct.setText("$" + String.format("%.2f", (total / plan)));
                this.oct.setFont(Font.font("Gotham"));
                this.oct.setFill(Color.GREEN);
            }break;
        }
    }
    
    public Payment(int id, String address, String name, int method, double total, int plan, int oct, int nov, int dec, int jan, int feb, String comment){
        this.id = id;
        this.addressString = address;
        this.nameString = name;
        this.commentText.setFont(Font.font("Gotham"));
        this.comment = comment;
        if (comment.length() > 0)
            this.commentText.setText("Comment");
        
        switch (method){
            case 0: this.methodString = "CHECK";break;
            case 1: this.methodString = "CREDIT";break;
            case 2: this.methodString = "INTERACT";break;
            case 3: this.methodString = "MISC";break;
            default: this.methodString = "WTF...SOMETHING WENT WRONG";
        }
        
        this.totalString = "$" + String.format("%.2f", total);
        
        switch (oct){
            case 0: {
                this.oct.setText("");
            }break;
            case 1: {
                this.oct.setText("$" + String.format("%.2f", (total / plan)));
                this.oct.setFont(Font.font("Gotham"));
                this.oct.setFill(Color.RED);
            }break;
            case 2: {
                this.oct.setText("$" + String.format("%.2f", (total / plan)));
                this.oct.setFont(Font.font("Gotham"));
            }break;
            case 3: {
                this.oct.setText("$" + String.format("%.2f", (total / plan)));
                this.oct.setFont(Font.font("Gotham"));
                this.oct.setFill(Color.GREEN);
            }break;
        }
        
        switch (nov){
            case 0: {
                this.nov.setText("");
            }break;
            case 1: {
                this.nov.setText("$" + String.format("%.2f", (total / plan)));
                this.nov.setFont(Font.font("Gotham"));
                this.nov.setFill(Color.RED);
            }break;
            case 2: {
                this.nov.setText("$" + String.format("%.2f", (total / plan)));
                this.nov.setFont(Font.font("Gotham"));
            }break;
            case 3: {
                this.nov.setText("$" + String.format("%.2f", (total / plan)));
                this.nov.setFont(Font.font("Gotham"));
                this.nov.setFill(Color.GREEN);
            }break;
        }
        
        switch (dec){
            case 0: {
                this.dec.setText("");
            }break;
            case 1: {
                this.dec.setText("$" + String.format("%.2f", (total / plan)));
                this.dec.setFont(Font.font("Gotham"));
                this.dec.setFill(Color.RED);
            }break;
            case 2: {
                this.dec.setText("$" + String.format("%.2f", (total / plan)));
                this.dec.setFont(Font.font("Gotham"));
            }break;
            case 3: {
                this.dec.setText("$" + String.format("%.2f", (total / plan)));
                this.dec.setFont(Font.font("Gotham"));
                this.dec.setFill(Color.GREEN);
            }break;
        }
        
        switch (jan){
            case 0: {
                this.jan.setText("");
            }break;
            case 1: {
                this.jan.setText("$" + String.format("%.2f", (total / plan)));
                this.jan.setFont(Font.font("Gotham"));
                this.jan.setFill(Color.RED);
            }break;
            case 2: {
                this.jan.setText("$" + String.format("%.2f", (total / plan)));
                this.jan.setFont(Font.font("Gotham"));
            }break;
            case 3: {
                this.jan.setText("$" + String.format("%.2f", (total / plan)));
                this.jan.setFont(Font.font("Gotham"));
                this.jan.setFill(Color.GREEN);
            }break;
        }
        
        switch (feb){
            case 0: {
                this.feb.setText("");
            }break;
            case 1: {
                this.feb.setText("$" + String.format("%.2f", (total / plan)));
                this.feb.setFont(Font.font("Gotham"));
                this.feb.setFill(Color.RED);
            }break;
            case 2: {
                this.feb.setText("$" + String.format("%.2f", (total / plan)));
                this.feb.setFont(Font.font("Gotham"));
            }break;
            case 3: {
                this.feb.setText("$" + String.format("%.2f", (total / plan)));
                this.feb.setFont(Font.font("Gotham"));
                this.feb.setFill(Color.GREEN);
            }break;
        }
        
        
    }
    
    public void setId(int id){
        this.id = id;
    }
    public void setAddressString(String addressString){
        this.addressString = addressString;
    }
    public void setNameString(String nameString){
        this.nameString = nameString;
    }
    public void setMethodString(String methodString){
        this.methodString = methodString;
    }
    public void setTotalString(String totalString){
        this.totalString = totalString;
    }
    public void setMar(Text mar){
        this.mar = mar;
    }
    public void setApr(Text apr){
        this.apr = apr;
    }
    public void setMay(Text may){
        this.may = may;
    }
    public void setJun(Text jun){
        this.jun = jun;
    }
    public void setJul(Text jul){
        this.jul = jul;
    }
    public void setAug(Text aug){
        this.aug = aug;
    }
    public void setSep(Text sep){
        this.sep = sep;
    }
    public void setOct(Text oct){
        this.oct = oct;
    }
    public void setNov(Text nov){
        this.nov = nov;
    }
    public void setDec(Text dec){
        this.dec = dec;
    }
    public void setJan(Text jan){
        this.jan = jan;
    }
    public void setFeb(Text feb){
        this.feb = feb;
    }
    public void setCommentText(Text commentText){
        this.commentText = commentText;
    }
    public void setComment(String comment){
        this.comment = comment;
    }
    
    public int getId(){
        return this.id;
    }
    public String getAddressString(){
        return this.addressString;
    }
    public String getNameString(){
        return this.nameString;
    }
    public String getMethodString(){
        return this.methodString;
    }
    public String getTotalString(){
        return this.totalString;
    }
    public Text getMar(){
        return this.mar;
    }
    public Text getApr(){
        return this.apr;
    }
    public Text getMay(){
        return this.may;
    }
    public Text getJun(){
        return this.jun;
    }
    public Text getJul(){
        return this.jul;
    }
    public Text getAug(){
        return this.aug;
    }
    public Text getSep(){
        return this.sep;
    }
    public Text getOct(){
        return this.oct;
    }
    public Text getNov(){
        return this.nov;
    }
    public Text getDec(){
        return this.dec;
    }
    public Text getJan(){
        return this.jan;
    }
    public Text getFeb(){
        return this.feb;
    }
    public Text getCommentText(){
        return this.commentText;
    }
    public String getComment(){
        return this.comment;
    }
    
}
