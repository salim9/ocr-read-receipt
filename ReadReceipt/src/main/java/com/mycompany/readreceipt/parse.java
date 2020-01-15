/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.readreceipt;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Salim
 */
public class parse {
     public static String deleteDoubleSpace(String input){
      
      input = input.replace("\0", " ");
         
      for(int j = 0; j< input.length(); j++){
          if(input.charAt(j) == ' ' && input.charAt(j+1) == ' '){
              input = input.substring(0,j) + input.substring(j+1);
              j = 0;
          }
      }
      
      return input;
  }
   public static String getDate(String input) {
    String allMatches = new String();
    Matcher m = Pattern.compile("(0[1-9]|[12][0-9]|3[01])[- /.](0[1-9]|1[012])[- /.](19|20)\\d\\d").matcher(input);
    if (m.find()) {
        allMatches = m.group();
    }else{
        allMatches = "01/01/2000";
    }
    
    allMatches = allMatches.replace(".","/");
    allMatches = allMatches.replace("-","/");
    
    return allMatches;
}
  
  public static String getPrice(String input){
        String regexType1 = "TOP\\s\\*([0-9]+[, .][0-9]+)";
        String regexType2 = "TOPLAM\\s\\*([0-9]+[, .][0-9]+)";
        String regexType3 = "TOP\\s([0-9]+[, .][0-9]+)";
        String regexType4 = "TOPLAM\\s([0-9]+[, .][0-9]+)";
        String regexType5 = "TUTAR\\s\\*([0-9]+[, .][0-9]+)";
        String regexType6 = "TUTAR\\s([0-9]+[, .][0-9]+)";
        
        List<String> regexType = new ArrayList<>();
        
        regexType.add(regexType1);
        regexType.add(regexType2);
        regexType.add(regexType3);
        regexType.add(regexType4);
        regexType.add(regexType5);
        regexType.add(regexType6);
        
        String price = null;
        
        int i = 0;
        for(String regex : regexType){
            
            Pattern pattern = Pattern.compile(regexType.get(i));
            Matcher matcher = pattern.matcher(input);
            
            if(matcher.find()){
                price = matcher.group();
                break;
            }
            
            i++;
        }
        
        if(price != null){
            int j;
            
            for (j = price.length()-1; j>= 0; j--){
                if(price.charAt(j) == ' ' || price.charAt(j) == '*'){
                    price = price.substring(j+1,price.length());
                    break;
                }
            }
        }
        
        return price;
  }
  
  public static String getKDV(String input){
        String regexType1 = "TOPKDV\\s\\*([0-9]+[, .][0-9]+)";
        String regexType2 = "KDV\\s\\*([0-9]+[, .][0-9]+)";
        String regexType3 = "TOPKDV\\s([0-9]+[, .][0-9]+)";
        String regexType4 = "KDV\\s([0-9]+[, .][0-9]+)";
        
        List<String> regexType = new ArrayList<>();
        
        regexType.add(regexType1);
        regexType.add(regexType2);
        regexType.add(regexType3);
        regexType.add(regexType4);
        
        String KDV = null;
        
        int i = 0;
        for(String regex : regexType){
            
            Pattern pattern = Pattern.compile(regexType.get(i));
            Matcher matcher = pattern.matcher(input);
            
            if(matcher.find()){
                KDV = matcher.group();
                break;
            }
            
            i++;
        }
        
        if(KDV != null){
            int j;
            
            for (j = KDV.length()-1; j>= 0; j--){
                if(KDV.charAt(j) == ' ' || KDV.charAt(j) == '*'){
                    KDV = KDV.substring(j+1,KDV.length());
                    break;
                }
            }
        }
        
        return KDV;  
      
  }
  
  public static String getReceiptNo(String input){
        
        String regexType1 = "F..\\s..:\\s([0-9])";
        String regexType2 = "F..\\s..\\s:\\s([0-9]{1,20})";
        
        List<String> regexType = new ArrayList<>();
        
        regexType.add(regexType1);
        regexType.add(regexType2);
        
        String ReceiptNo = null;
        
        int i = 0;
        for(String regex : regexType){
            
            Pattern pattern = Pattern.compile(regexType.get(i));
            Matcher matcher = pattern.matcher(input);
            
            if(matcher.find()){
                ReceiptNo = matcher.group();
                break;
            }
            
            i++;
        }
        
        if(ReceiptNo != null){
            int j;
            
            for (j = ReceiptNo.length()-1; j>= 0; j--){
                if(ReceiptNo.charAt(j) == ' '){
                    ReceiptNo = ReceiptNo.substring(j+1,ReceiptNo.length());
                    break;
                }
            }
        }
        
        
        
        return ReceiptNo;
  }
  
  public static String getName(String input){
      
      String name = null;
      
      for(int i = 0; i < input.length(); i++){
          if(input.charAt(i) == '\n'){
              name = input.substring(0,i);
              break;
          }
      }
      
      for(int i = 0; i < name.length(); i++){
          if(name.charAt(i) == '*'){
              name = name.substring(0, i-1);
          }
      }
      
      int j = 0;
      
      for(j = 0; j< name.length(); j++){
          if(name.charAt(j) == ' ' && name.charAt(j+1) == ' '){
              name = name.substring(0,j-1);
              j = 0;
          }
      }
      
      return name;
  }
    
    
}
