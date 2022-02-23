/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package software1project;

/**
 *
 * @author Nathan
 */
public class IdGenerator {
   private static int getNextPartID = 1;
    private static int getNextProductID = 1;
    
    public static int getNextPartID(){
if (getNextPartID == 1){
getNextPartID++;
return 1;
}
else if (getNextPartID >= 2){
getNextPartID++;
return getNextPartID - 1;
}
return -1;
    }
    
     public static int getNextProductID(){
if (getNextProductID == 1){
getNextProductID++;
return 1;
}
else if (getNextProductID >= 2){
getNextProductID++;
return getNextProductID - 1;
}
return -1;
    }
     
     public static int handleAddPartError(){
     getNextPartID--;
     return getNextPartID;
     }
     public static int handleAddProductError(){
     getNextProductID--;
     return getNextProductID;
     }
   }
   
    

