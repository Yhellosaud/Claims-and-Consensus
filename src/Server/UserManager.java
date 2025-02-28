/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

//import com.mysql.jdbc.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Array;
import java.sql.Connection;

import SharedModels.*;
import java.util.*;
import java.lang.Integer;

/**
 *
 * @author Yhellosaud
 */
public class UserManager {
    
    private Statement s;
    private ResultSet rs;
    private String userData;
    private ResultSetMetaData rsmd;
    private PreparedStatement statement;
    private final Connection c;
    
    public UserManager(Connection c) throws SQLException {

        this.c = c;
        s = c.createStatement();
        c.setAutoCommit(true);
    }
    
    /*public boolean signUp(String username, String password) {
        
        
        return true;
    }
    
    public User signIn(String username, String password) {
        
        
    }*/
    
    public boolean checkUserName(String username) {
        
    
        return true;
    }
    
    public User getUserDetails(String username) throws SQLException {
        
        int userID = 0;
        String password = " ";
        ArrayList<Integer> pastDebateIDs = new ArrayList<Integer>();
        ArrayList<Integer> votedDebateIDs = new ArrayList<Integer>();

        userData = "SELECT * FROM user";
        s = c.createStatement();
        rs = s.executeQuery(userData);
        rsmd = rs.getMetaData();

        while (rs.next()) {
            //Table display
            for(int i = 1; i <= rsmd.getColumnCount(); i++) {

                if (i > 1) { 
                    System.out.print(",  ");
                }

                System.out.print(rsmd.getColumnName(i) + ": " + rs.getString(i));
            }
            System.out.println("");
            //Searched user data is found and retrieved respectively
            if(rs.getString(2).equals(username)) {

                userID = rs.getInt(1);
                password = rs.getString(3);
            }
        }
            
        userData = "SELECT * FROM user_pastdebateid";
        s = c.createStatement();
        rs = s.executeQuery(userData);
        rsmd = rs.getMetaData();

        String[] dids = new String[20];

        while(rs.next()) {

            System.out.print(",  ");
            System.out.println(rsmd.getColumnName(2) + " " + rs.getString(2));
            String ids = rs.getString(2);
            dids = ids.split(",");
        }

        for(int i = 0; i < dids.length; i++) {

            int id = Integer.parseInt(dids[i]);
            pastDebateIDs.add(i, id);

        }

        userData = "SELECT * FROM user_voteddebateid";
        s = c.createStatement();
        rs = s.executeQuery(userData);
        rsmd = rs.getMetaData();

        while(rs.next()) {

            System.out.print(",  ");
            System.out.println(rsmd.getColumnName(2) + " " + rs.getString(2));
            String ids = rs.getString(2);
            dids = ids.split(",");
        }

        for(int i = 0; i < dids.length; i++) {

            int id = Integer.parseInt(dids[i]);
            votedDebateIDs.add(i, id);

        }
        
        return (new User(username, password, userID, pastDebateIDs, votedDebateIDs));
    }
    
    public void InsertUser(User user) throws SQLException {
        
        //Each single information data of user is attained one by one
        int id = user.getUserID();
        String username = user.getUsername();
        String password = user.getPassword();
        ArrayList<Integer> pastDebateIDs = user.getPastDebateIDs();
        ArrayList<Integer> votedDebateIDs = user.getVotedDebateIDs();
        
        String pdids = "";
        String vdids = "";
        
        for(int i = 0; i < pastDebateIDs.size(); i++) {
            
            pdids += "" + pastDebateIDs.get(i) + ",";
        }
        for(int i = 0; i < votedDebateIDs.size(); i++) {
        
            vdids += "" + votedDebateIDs.get(i) + ",";
        }
        
        //Related database tables are prepared in order to perform insertion
        //The table is transfered into a prepared statement
        //Statement variants are loaded with user information
        userData = "INSERT INTO user (id, username, password) VALUES (?, ?, ?)";
        statement = c.prepareStatement(userData); 
        statement.setInt(1, id);
        statement.setString(2, username);
        statement.setString(3, password);
        statement.executeBatch();
        statement.executeUpdate();
        //////////////////////////////////////////////////////////////////////
        userData = "INSERT INTO user_pastdebateid (userID, debateID) VALUES (?, ?)";
        statement = c.prepareStatement(userData); 
        statement.setInt(1, id);
        statement.setString(2, pdids);
        statement.executeBatch();
        statement.executeUpdate();
        //////////////////////////////////////////////////////////////////////
        userData = "INSERT INTO user_voteddebateid (userID, debateID) VALUES (?, ?)";
        statement = c.prepareStatement(userData);
        statement.setInt(1, id);
        statement.setString(2, vdids);
        statement.executeBatch();
        statement.executeUpdate();

        /*Iterator<Integer> it = pastDebateIDs.iterator();
        
        while(it.hasNext()){
            
            int pastDebateID = it.next();
            statement.setInt(4, pastDebateID);
            statement.addBatch();                      
        }
        int[] numUpdates = statement.executeBatch();
        
        for (int i=0; i < numUpdates.length; i++) {
            if (numUpdates[i] == -2) {
                System.out.println("Execution " + i + 
                  ": unknown number of rows updated");
            }
            
            else {
                System.out.println("Execution " + i + 
                  "successful: " + numUpdates[i] + " rows updated");
            }
        }
        
        int rowsInserted = statement.executeUpdate();
        
        if (rowsInserted > 0) {
            System.out.println("A new user was inserted successfully!");
        }*/
    }
}