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
        //System.out.println("connected to " + DBName);
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
}
