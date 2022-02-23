/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package software2project;

/**
 *
 * @author Nathan
 */
public class Customer {
    private int customerId;
    private String name;
    private String phoneNumber;
    private String address;
    private String address2;
    private String postalCode;
    private String city;
    private String country;
    
    public Customer (int customerId, String name, String phoneNumber, String address, String address2, String postalCode, String city, String country){
    this.customerId = customerId;
    this.name = name;
    this.phoneNumber = phoneNumber;
    this.address = address;
    this.address2 = address2;
    this.postalCode = postalCode;
    this.city = city;
    this.country = country;
    }
    
    public int getCustomerId(){
    return this.customerId;
    }
    public String getName(){
    return this.name;
    }
    public String getPhoneNumber(){
    return this.phoneNumber;
    }
    public String getAddress(){
    return this.address;
    }
    public String getAddress2(){
    return this.address2;
    }
    public String getPostalCode(){
    return this.postalCode;
    }
    public String getCity(){
    return this.city;
    }
    public String getCountry(){
    return this.country;
    }
    
    public void setName(String name){
    this.name = name;
    }
    public void setPhoneNumber(String phoneNumber){
    this.phoneNumber = phoneNumber;
    }
    public void setAddress(String Address){
    this.address = Address;
    }
    public void setAddress2(String Address2){
    this.address2 = Address2;
    }
    public void setPostalCode(String postalCode){
    this.postalCode = postalCode;
    }
    public void setCity (String city){
    this.city = city;
    }
    public void setCountry (String Country){
    this.country = Country;
    }
}
