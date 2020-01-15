package com.mycompany.readreceipt;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SQLQuery {
    
    public static String checkCompNameSQLQuery(String compName){
        
        String query = "SELECT companyID FROM company WHERE companyName = '" + compName + "';"; 
        
        
        
        return query;
    }
    
    
    
    public static Object checkCompID(String query, String compName) throws SQLException{
        
        Object ID[] = new Object[1];
        
        ResultSet myRs = Main.getData(query);
        
        if(myRs.next()){
            ID[0] = myRs.getInt(1);
        }
        
        Object id = ID[0];
        
        if(id == null){
            query = "SELECT Max(companyID) FROM company;";
            
            myRs = Main.getData(query);
            
            if(myRs.next()){
                ID[0] = myRs.getInt(1) + 1;
                
                id = ID[0];
                
                if(id == null){
                    id = 1;
                }
            }
        }

        Main.add(getCompSQLQuery(compName,id));
        
        return id;
    }
    
    public static String getCompSQLQuery(String compName,Object ID){
        String query = "INSERT INTO company (companyID, companyName) VALUES (" + ID + ", '" + compName +"');";
        
        return query;
    }
    
    public static String addReceiptSQLQuery(String compName,Object ID,String date,String receiptNo,String kdv,String total){
        String query;
        
        query = "INSERT INTO receipt (companyID,companyName,date,receiptNo,kdv,total) VALUES (" + ID + ",'" + compName + "','" + date + "','" + receiptNo + "','" + kdv 
                + "','" + total + "');";
    
        return query;
    }
    
}
