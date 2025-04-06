package com.bushnell;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.bushnell.Part;

public class Database {

    public static String DBName = "jdbc:sqlite:VR-Factory.db";

    public static boolean checkConnection() {
      try {
        Class.forName("org.sqlite.JDBC");
        return true;
    }
    catch(Exception e)
    {
        e.printStackTrace(System.err);
        return false;  
    }
    }

    public static String[] getSkuList() 
    {
      try
      (
        Connection connection = DriverManager.getConnection(DBName);
        Statement statement = connection.createStatement();
      )
      {
        ResultSet rs = statement.executeQuery("select sku from part");
        List<String> skuList = new ArrayList<>();
        while(rs.next()) {
          skuList.add(rs.getString("sku"));
        }
        String[] skuArray = skuList.toArray(new String[0]);
        return skuArray;          
      }
      catch(SQLException e)
      {
        e.printStackTrace(System.err);
        return new String[0];
      }
    }

    public static Part getSkuData(String sku) 
    {
      Part result = new Part();
      try
      (
        Connection connection = DriverManager.getConnection(DBName);
        Statement statement = connection.createStatement();
      )
      {
        ResultSet rs = statement.executeQuery("select * from part where sku = \"" + sku + "\"");
        while(rs.next()) {
          result.sku = rs.getString("sku");
          result.description = rs.getString("description");
          result.price = rs.getDouble("price");
          result.stock = rs.getInt("stock");
        }
        return result;          
      }
      catch(SQLException e)
      {
        e.printStackTrace(System.err);
        result.sku = "";
        result.description = "";
        result.price = 0.0;
        result.stock = 0;
        return result;
      }
    }

    public static boolean updateSku(Part part) 
    {
      try
      (
        Connection connection = DriverManager.getConnection(DBName);
        Statement statement = connection.createStatement();
      )
      {
        String statementTxt =   "update part\n" +
                                "set price = " + Double.toString(part.price) + ",\n" +
                                "stock = " + Integer.toString(part.stock) + "\n" +
                                "where sku = \"" + part.sku + "\"";
        statement.executeUpdate(statementTxt);
        return true;         
      }
      catch(SQLException e)
      {
        e.printStackTrace(System.err);
        return false;
      }
    }

    public static List<Part> getAllSkuData() 
    {
      List<Part> allSkuList = new ArrayList<Part>();
      try
      (
        Connection connection = DriverManager.getConnection(DBName);
        Statement statement = connection.createStatement();
      )
      {
        ResultSet rs = statement.executeQuery("select * from part");
        while(rs.next()) {
          Part part = new Part();
          part.sku = rs.getString("sku");
          part.description = rs.getString("description");
          part.price = rs.getDouble("price");
          part.stock = rs.getInt("stock");
          System.out.println(" sku=" + part.sku + " description=\"" + part.description + "\" " +
            "price=" + Double.toString(part.price) + " stock=" + Integer.toString(part.stock));
          allSkuList.add(part);
        }
        return allSkuList;          
      }
      catch(SQLException e)
      {
        e.printStackTrace(System.err);
        return allSkuList;
      }
    }    
}
