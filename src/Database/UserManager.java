/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mysqlconnection;

import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Connection;
import SharedModels.*;
import java.util.*;

/**
 *
 * @author Yhellosaud
 */
public class UserManager {
    

    private ResultSet rs1, rs2, rs3;
    private ResultSetMetaData rsmd;
    private PreparedStatement ps;
    private final Connection c;
    private final Statement s1, s2, s3;
    private final String str1, str2, str3, str4, str5, str6, str7, str8, str9, str10, str11, str12;
    
    public UserManager(Connection c) throws SQLException {

        this.c = c;
        
        s1 = c.createStatement();
        s2 = c.createStatement();
        s3 = c.createStatement();
        
        c.setAutoCommit(true);
        
        str1 = "SELECT * FROM user";
        str2 = "SELECT * FROM user_playeddebateid";
        str3 = "SELECT * FROM user_voteddebateid";
        str4 = "INSERT INTO user (userID, username, password, avatarID, titleID, playedDebateNumber, votedDebateNumber) VALUES (?, ?, ?, ?, ?, ?, ?)";
        str5 = "INSERT INTO user_playeddebateid (userID, debateID) VALUES (?, ?)";
        str6 = "INSERT INTO user_voteddebateid (userID, debateID) VALUES (?, ?)";
        str7 = "UPDATE user_playeddebateid SET debateID = ? WHERE userID = ?";
        str8 = "UPDATE user_voteddebateid SET debateID = ? WHERE userID = ?";
        str9 = "UPDATE user SET playedDebateNumber = ? WHERE userID = ?";
        str10 = "UPDATE user SET votedDebateNumber = ? WHERE userID = ?";
        str11 = "UPDATE user SET avatarID = ? WHERE userID = ?";
        str12 = "UPDATE user SET titleID = ? WHERE userID = ?";
    }
    
    public boolean signUp(String username, String password) throws SQLException {
                
        rs1 = s1.executeQuery(str1);
        
        int userID = 0;
        
        while(rs1.next()) {

            if(username.equals(rs1.getString(2))) {
                
                return false;
            }
            
            userID = rs1.getInt(1);
        }
        
        insertUser(new User(username, password, ++userID, new ArrayList<>(), new ArrayList<>(), new Avatar(), new Title()));
        System.out.println("User '" + username + "' has been successfully signed up!");
        return true;
    }
    
    public User signIn(String username, String password) throws SQLException {
        
        rs1 = s1.executeQuery(str1);

        while(rs1.next()) {

            if(username.equals(rs1.getString(2)) && password.equals(rs1.getString(3))) {
                
                System.out.println("User '" + username + "' has been successfully signed in!");
                return getUser(username);
            }
        }

        return null;
    }
    
    public boolean addPlayedDebateID(String username, int debateID) throws SQLException {
        
        rs1 = s1.executeQuery(str1);
        String ids = "";
        int userID = 0;
        int playedDebateNumber = 0;
        
        while(rs1.next()) {
            if(username.equals(rs1.getString(2))) {
                userID = rs1.getInt(1);
                playedDebateNumber = rs1.getInt(6);
                break;
            }
        }
        String[] pdids = new String[playedDebateNumber];
        // Played debateID's of user are retrieved
        rs2 = s2.executeQuery(str2);
        while(rs2.next()) {
            if(rs2.getInt(1) == userID) {   
                ids = rs2.getString(2);
                pdids = ids.split(", ");
            }
        }
        for(int i = 0; i < pdids.length; i++) {  
            if(pdids[i].equals("" + debateID)) {
                return false;
            }
        }

        ids += "" + debateID + ", ";
        
        ps = c.prepareStatement(str7);
        ps.setString(1, ids);
        ps.setInt(2, userID);
        ps.executeBatch();
        ps.executeUpdate();
        
        ps = c.prepareStatement(str9);
        ps.setInt(1, playedDebateNumber + 1);
        ps.setInt(2, userID);
        ps.executeBatch();
        ps.executeUpdate();
                
        System.out.println("Played debate with the id '" + debateID + "' has been successfully inserted to user '" + username + "'!");
        return true;
    }
    
    public boolean addVotedDebateID(String username, int debateID) throws SQLException {
        
        rs1 = s1.executeQuery(str1);
        String ids = "";
        int userID = 0;
        int votedDebateNumber = 0;
        
        while(rs1.next()) {
            if(username.equals(rs1.getString(2))) {
                userID = rs1.getInt(1);
                votedDebateNumber = rs1.getInt(7);
                break;
            }
        }
        String[] vdids = new String[votedDebateNumber];
        // Voted debateID's of user are retrieved
        rs3 = s3.executeQuery(str3);
        while(rs3.next()) {
            if(rs3.getInt(1) == userID) {   
                ids = rs3.getString(2);
                vdids = ids.split(", ");
            }
        }
        for(int i = 0; i < vdids.length; i++) {  
            if(vdids[i].equals("" + debateID)) {
                return false;
            }
        }

        ids += "" + debateID + ", ";

        ps = c.prepareStatement(str8);
        ps.setString(1, ids);
        ps.setInt(2, userID);
        ps.executeBatch();
        ps.executeUpdate();
        
        ps = c.prepareStatement(str10);
        ps.setInt(1, votedDebateNumber + 1);
        ps.setInt(2, userID);
        ps.executeBatch();
        ps.executeUpdate();
                
        System.out.println("Played debate with the id '" + debateID + "' has been successfully inserted to user '" + username + "'!");
        return true;
    }
    
    public boolean setSelectedAvatar(String username, Avatar selectedAvatar) throws SQLException {
        int avatarID = selectedAvatar.getAvatarID();
        rs1 = s1.executeQuery(str1);
        while(rs1.next()) {
            if(username.equals(rs1.getString(2))) {
                ps = c.prepareStatement(str11);
                ps.setInt(1, avatarID);
                ps.setInt(2, rs1.getInt(1));
                ps.executeBatch();
                ps.executeUpdate();
                System.out.println("Avatar with ID '" + avatarID + "' of the user '" + username + "' has been successfully updated!");
                return true;
            }
        }
        return false;
    }
    public boolean setSelectedTitle(String username, Title selectedTitle) throws SQLException {
        int titleID = selectedTitle.getTitleID();
        rs1 = s1.executeQuery(str1);
        while(rs1.next()) {
            if(username.equals(rs1.getString(2))) {
                ps = c.prepareStatement(str12);
                ps.setInt(1, titleID);
                ps.setInt(2, rs1.getInt(1));
                ps.executeBatch();
                ps.executeUpdate();
                System.out.println("Title with ID '" + titleID + "' of the user '" + username + "' has been successfully updated!");
                return true;
            }
        }
        return false;
    }
    public int getNumberOfUsers() throws SQLException {
        
        rs1 = s1.executeQuery(str1);
        
        int numberOfUsers = 0;

        while(rs1.next()) {

            numberOfUsers++;
        }
        
        return numberOfUsers;
    }
    
    public void displayAllUsers() throws SQLException {
        
        try {

            rsmd = rs1.getMetaData();

            rs1 = s1.executeQuery(str1);
            rs2 = s2.executeQuery(str2);
            rs3 = s3.executeQuery(str3);
            
            while (rs1.next()) {
                rs2.next();
                rs3.next();
                //Table display
                System.out.println("\n------------------------");
                for(int i = 1; i <= rsmd.getColumnCount(); i++) {

                    if (i > 1) { 
                        System.out.print("->  ");
                    }
                    
                    System.out.println(rsmd.getColumnName(i) + ": " + rs1.getString(i));
                }
                
                System.out.print("-> played debates: " + rs2.getString(2));
                System.out.println("");

                System.out.print("-> voted debates: " + rs3.getString(2));
                System.out.println("\n------------------------\n");
                

            }
        }
        
        catch (NullPointerException e) {
            
        }
    }
    
    public User getUser(String username) throws SQLException {
        
        int userID = 0;
        int playedDebateNumber = 0;
        int votedDebateNumber = 0;
        String password = " ";
        Avatar selectedAvatar = null;
        Title selectedTitle = null;
        ArrayList<Integer> playedDebateIDs = new ArrayList<>();
        ArrayList<Integer> votedDebateIDs = new ArrayList<>();
        String[] pdids = new String[playedDebateIDs.size()];
        String[] vdids = new String[votedDebateIDs.size()];
        // User details are retrieved
        rs1 = s1.executeQuery(str1);
        while (rs1.next()) {
            if(rs1.getString(2).equals(username)) {
                userID = rs1.getInt(1);
                password = rs1.getString(3);
                selectedAvatar = new Avatar(rs1.getInt(4));
                selectedTitle = new Title(rs1.getInt(5));
                playedDebateNumber = rs1.getInt(5);
                votedDebateNumber = rs2.getInt(6);
            }
        }
        // Played debateID's of user are retrieved
        rs2 = s2.executeQuery(str2);
        while(rs2.next()) {
            if(rs2.getInt(1) == userID) {   
                String ids = rs2.getString(2);
                pdids = ids.split(", ");
            }
        }
        for(int i = 0; i < playedDebateNumber; i++) {
            playedDebateIDs.add(i, Integer.parseInt(pdids[i]));
        }
        // Voted debateID's of user are retrieved
        rs3 = s3.executeQuery(str3);
        while(rs3.next()) {
            if(rs3.getInt(1) == userID) {   
                String ids = rs3.getString(2);
                vdids = ids.split(", ");
            }
        }
        for(int i = 0; i < votedDebateNumber; i++) {
            votedDebateIDs.add(i, Integer.parseInt(vdids[i]));
        }
        
        return (new User(username, password, userID, playedDebateIDs, votedDebateIDs, selectedAvatar, selectedTitle));
    }
    
    public void insertUser(User user) throws SQLException {
        
        String pdids = "";
        String vdids = "";
        
        for(int i = 0; i < user.getPlayedDebateIDs().size(); i++) {
            
            pdids += "" + user.getPlayedDebateIDs().get(i) + ", ";
        }
        for(int i = 0; i < user.getVotedDebateIDs().size(); i++) {
        
            vdids += "" + user.getVotedDebateIDs().get(i) + ", ";  
        }
        //Related database tables are prepared in order to perform insertion
        //The table is transfered into a prepared statement
        //Statement variants are loaded with user information
        ps = c.prepareStatement(str4);
        ps.setInt(1, 0);
        ps.setString(2, user.getUsername());
        ps.setString(3, user.getPassword());
        ps.setInt(4, 0);
        ps.setInt(5, 0);
        ps.setInt(6, 0);
        ps.setInt(7, 0);
        ps.executeBatch();
        ps.executeUpdate();
        //////////////////////////////////////////////////////////////////////
        ps = c.prepareStatement(str5); 
        ps.setInt(1, 0);
        ps.setString(2, pdids);
        ps.executeBatch();
        ps.executeUpdate();
        //////////////////////////////////////////////////////////////////////
        ps = c.prepareStatement(str6);
        ps.setInt(1, 0);
        ps.setString(2, vdids);
        ps.executeBatch();
        ps.executeUpdate();
    }
}
