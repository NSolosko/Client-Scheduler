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
public class User {
    private int userId;
    private String userName;
  
    
   public User(int userId, String userName){
    this.userId = userId;
    this.userName = userName;
   
    }
   public void setUserId(int userId){
   this.userId = userId;
   }
   
   public int getUserId(){
  return this.userId;
   }

   public void setUserName(String userName){
   this.userName = userName;
   }
   public String getUserName(){
  return this.userName; 
   }
}
