/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hospitalmain;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
/**
 * A class to connect to the database-MaliziaHp,
 * execute query in the program ,produce  a result set,generate update query.
 * @author p.kitheka
 */
public class DatabaseManager {
      String connectionUrl = "jdbc:sqlserver://SQLSVR:1433;user=sa;password=Edulink2015;databaseName=MaliziaHp";
      Connection conn = null;
    
      /**
       * This method connect() connects to the database.
       * @throws SQLException - if there is a failure in connecting to the database.
       */
    DatabaseManager()throws SQLException {
        connect();
    }
    
    /**
     * Connects to the database.
     * @throws SQLException- if there is a failure in connecting to the database.
     * Does not capture the exception.
     */
    private void connect()throws SQLException {
        try {
            conn = DriverManager.getConnection(connectionUrl);
        } catch (SQLException ex) {
            throw ex;
        }
    }
    
    /**
     * Executes a query which does not produce a result set.
     * @param query-the query provided to execute in the program.
     * @param successMessage-shows the success message after a successful execution.
     * @param failureMessage-shows the success message after a successful execution. 
     */
    public void execute(String query, String successMessage, String failureMessage) {
        try {
            Statement statement = this.conn.createStatement();
            statement.execute(query);
            
            if (!successMessage.equals("")) {
                System.out.println(successMessage);//Prints a success message if executed successfilly.
            }
            
        } catch (SQLException ex) {
            System.err.println(failureMessage + ": " + ex.getMessage());//Prints a failure message if an error occured.
        }
    }
    
    /**
     * Executes a query that produces a result set.
     * @param query-the query provided to execute in the program.
     * @return ResultSet  
     */
    public ResultSet executeQuery(String query) {
        try {
            /**
             * May throw an exception.
             */
            Statement statement = this.conn.createStatement();
            return statement.executeQuery(query);
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());//Prints a failure message if an error occured.
        }
        
        return null;
    }
    /**
     * Updates data in the specified table.
     * @param table-table specified in the query
     * @param columns-columns specified from the  table.
     * @param values-values of data to be updated by the query.
     * @return String -value of sql.
     */
    public String generateUpdateQuery(String table, String[] columns, String[] values) {
        
        String sql = "UPDATE " + table + " SET ";
        
        for(int i = 0, count = 0; i < values.length; i++) {
            
            String value = values[i];//values put in an array
            
            if (value.equals("")) {
                continue;//if the specified columns are empty continue sql.
            }
            
            String column = columns[i];
            
            if (count > 0) {//if the count is greater than 0 adds a ","at the end.
                sql += ", ";
            }
            
            sql += column + " = " + "'" + value + "'";
            
            count++;
            
        }
        
        return sql;
        
    }
    
}
