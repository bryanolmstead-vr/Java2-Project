package com.bushnell;

import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.sql.PreparedStatement;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.net.URISyntaxException;

import com.bushnell.Part;

public class Database {

    public static String DBName = "jdbc:sqlite:VR-Factory.db";

    public static boolean setDBDirectory(String directory) {
      try {
        Path fullPath = Paths.get(directory, "VR-Factory.db");
        DBName = "jdbc:sqlite:" + fullPath.toString();  
        return true;
      } catch(Exception e) {
        return false;
      }
    }

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

    public static String[] getSkuList(String filter) 
    {
      try
      (
        Connection connection = DriverManager.getConnection(DBName);
        Statement statement = connection.createStatement();
      )
      {
        ResultSet rs = statement.executeQuery("select sku from part where sku like '" + filter + "'");
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

    public static List<Part> getAllSkuChildrenData(String sku) 
    {
      List<Part> allSkuList = new ArrayList<Part>();
      try
      (
        Connection connection = DriverManager.getConnection(DBName);
        Statement statement = connection.createStatement();
      )
      {
        ResultSet rs = statement.executeQuery(
            "SELECT bom.sku, bom.quantity, part.stock, part.description " +
            "FROM bom JOIN part " +
            "ON bom.sku = part.sku " +
            "WHERE parent_sku = \"" + sku + "\";");
        while(rs.next()) {
          Part part = new Part();
          part.sku = rs.getString("sku");
          part.description = rs.getString("description");
          part.stock = rs.getInt("stock");
          part.quantity = rs.getInt("quantity");
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

    public static Boolean bundle(String sku) 
    {
      try
      (
        Connection connection = DriverManager.getConnection(DBName);
        Statement queryStatement = connection.createStatement();
        Statement updateStatement = connection.createStatement();
      )
      {
        System.out.println("Bundling " + sku);
        ResultSet rs = queryStatement.executeQuery(
            "SELECT bom.sku, bom.quantity, part.stock " +
            "FROM bom JOIN part " +
            "ON bom.sku = part.sku " +
            "WHERE parent_sku = \"" + sku + "\";");
        while(rs.next()) {
          Part part = new Part();
          part.sku = rs.getString("sku");
          part.stock = rs.getInt("stock");
          part.quantity = rs.getInt("quantity");
          System.out.println("  Decrementing sku " + part.sku + " by qty " + part.quantity + " with existing stock " + part.stock);
          String s = "UPDATE part SET stock = stock - " + part.quantity + " WHERE sku = \"" + part.sku + "\"";
          int x = updateStatement.executeUpdate(s);
          System.out.println("  updated " + x + " records");
        }   
        System.out.println("  Incrementing sku " + sku + " by qty 1");
        String s = "UPDATE part SET stock = stock + 1 WHERE sku = \"" + sku + "\"";
        int x = updateStatement.executeUpdate(s);
        System.out.println("  updated " + x + " records");
        return true;       
      }
      catch(SQLException e)
      {
        e.printStackTrace(System.err);
        return false;
      }
    }   

    public static String loadStringFromFile(String fileName) throws IOException, URISyntaxException {
      ClassLoader classLoader = Database.class.getClassLoader();
      InputStream inputStream = classLoader.getResourceAsStream(fileName);
      if (classLoader.getResource(fileName) == null) {
          throw new IOException("File not found: " + fileName);
      }
      StringBuilder content = new StringBuilder();
      try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
          String line;
          while ((line = reader.readLine()) != null) {
              content.append(line).append("\n");
          }
      }
      return content.toString(); 
    }

    private static String replaceNamedParameters(String query, List<Object> parameterValues, String namedParam, Object value) {
      // Replace all instances of the named parameter with "?" and track the values
      while (query.contains(namedParam)) {
          query = query.replaceFirst(namedParam, "?");
          parameterValues.add(value);
      }
      return query;
    }

    public static List<Part> getRequiredStock(String sku, int desiredQty) {
      List<Part> requiredStockList = new ArrayList<Part>();
      // get sql query string for demand analysis
      String queryString = "";
      try {
        queryString = Database.loadStringFromFile("DemandQuery.sql");
        } catch(URISyntaxException | IOException e) {
            e.printStackTrace(System.err);
        }
      // replace :demand_qty and :demand_sku appropriately
      List<Object> parameterValues = new ArrayList<>();
      queryString = replaceNamedParameters(queryString, parameterValues, ":demand_qty", desiredQty);
      queryString = replaceNamedParameters(queryString, parameterValues, ":demand_sku", sku);
      // attempt to execute the query
      try
      (
        Connection connection = DriverManager.getConnection(DBName);
        PreparedStatement statement = connection.prepareStatement(queryString);
      )
      {
        // replace each ? with appropriate values in the statement
        for (int i = 0; i < parameterValues.size(); i++) {
          statement.setObject(i + 1, parameterValues.get(i));
        }
        // Execute the query and process the results
        ResultSet rs = statement.executeQuery();
        while(rs.next()) {
          Part part = new Part();
          part.sku = rs.getString("raw_material_sku");
          part.description = rs.getString("raw_material_description");
          part.stock = 0;
          part.quantity = rs.getInt("total_required_qty");
          requiredStockList.add(part);
        }
        return requiredStockList;          
      }
      catch(SQLException e)
      {
        e.printStackTrace(System.err);
        return requiredStockList;
      }
    }

}
