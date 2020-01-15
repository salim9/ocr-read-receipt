package com.mycompany.readreceipt;

import static com.mycompany.readreceipt.parse.getDate;
import static com.mycompany.readreceipt.parse.*;
import static com.mycompany.readreceipt.parse.getPrice;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;

public class Main {
    static Connection myConn;
    static Statement myStat;

    public static void main(String[] args) {
        
       
        
        try{
        Class.forName("com.mysql.cj.jdbc.Driver");
    }
    catch(ClassNotFoundException e){
        System.out.println("Driver can not found.");
        return;
    }
    
        System.out.println("Driver Succes");
        Connection connection = null;
        
     try {
            myConn = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/DATABASENAME?useUnicode=true&useLegacyDatetimeCode=false&serverTimezone=Turkey", "root", "password");
            System.out.println("Connection to database");
            
 
        } catch (SQLException e) {
            System.out.println("Could not connect to database.");
            return;
        } 

    }

    public static ResultSet getData(String query){
        try{
            myConn = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/DATABASENAME?useUnicode=true&useLegacyDatetimeCode=false&serverTimezone=Turkey", "root", "password");
            myStat = myConn.createStatement();
            ResultSet myRs = myStat.executeQuery(query);


            return myRs;
        } catch (Exception e){
            e.printStackTrace();
        }
        return null; 
    }
    
    public static void add (String query){
        try{
            myConn = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/DATABASENAME?useUnicode=true&useLegacyDatetimeCode=false&serverTimezone=Turkey", "root", "password");
            myStat = myConn.createStatement();
            myStat.executeUpdate(query);
        }catch (SQLException e){
            e.printStackTrace();
        }

    }
}
