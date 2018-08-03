package com.revature.Dao;

import com.revature.Connectivity.DbConnection;
import com.revature.bean.LogingBean;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LogingDao {

    PreparedStatement pst = null;
    Connection conn = null;
    ResultSet rs = null;
    CallableStatement cst = null;

    // authenticate user credentials
    public String authenticateUser(LogingBean logingbean){


        String username = logingbean.getUsername();
        String password = logingbean.getPassword();

        String usernameFromDb ;
        String passwordFromDb ;
        String roleFromDb ;
        String fullnameFromDb;
        String emailfromDb;


        try(Connection conn = DbConnection.getConnection()){

            String sql = "select username, passwe, rolee from users" ;


            pst = conn.prepareStatement(sql);
           // pst.setString(1,username);
            rs = pst.executeQuery();

            while(rs.next()){
                usernameFromDb = rs.getString("username");
                passwordFromDb = rs.getString("passwe");
                roleFromDb = rs.getString("rolee");


                if((username.equals(usernameFromDb) && (password.equals(passwordFromDb)) && roleFromDb.equals("Host"))){
                    return "Host's Role";
                }else if((username.equals(usernameFromDb) && password.equals(passwordFromDb)) && roleFromDb.equals("User")) {
                    return "User's Role";
               }else if((!username.equals(usernameFromDb) && (!password.equals(passwordFromDb)) && roleFromDb.equals(""))){
                       // createUser(logingbean);
                }

            }

        }catch (SQLException ex){
            ex.printStackTrace();
        }catch (IOException ex){
            ex.printStackTrace();
        }
        return "invalid user credentials";
    }

// get guest info by username
   public LogingBean getUserLoginInfo(String username) {

        //String username = logingbean.getUsername();

       LogingBean lb = null;


       try(Connection conn = DbConnection.getConnection()) {


           String sql = "SELECT * FROM USERS  WHERE USERNAME = ?";

           pst = conn.prepareStatement(sql);
           pst.setString(1, username);
           ResultSet rs = pst.executeQuery();

           while(rs.next()) {

              /* Role role;

               if (rs.getString("Rolee") == "USER")
                   //role = Role.GUEST;
               {
                   role = new Role(2, "User");
               } else {
                   role = new Role(1, "Host");
               }*/

               int uid = rs.getInt("USERID");
               String name = rs.getString("FullNAME");
               String ema = rs.getString("EMAIL");
               String user = rs.getString("USERNAME");
               String pass = rs.getString("PASSWE");
               String r = rs.getString("rolee");

               lb = new LogingBean(uid, name, ema,user, pass,  r);

           }
       }catch (SQLException ex){
           ex.printStackTrace();
       }catch (IOException e){
           e.printStackTrace();
       }

       return lb;
   }
// create or insert a user in the users table
    public LogingBean createUser(LogingBean lb){


        //String username = lb.getUsername();
       // String password = lb.getPassword();



        try(Connection conn = DbConnection.getConnection()){

            String sql = "{CALL sp_insert_user (?,?,?,?,?,?)}";

            cst = conn.prepareCall(sql);

            cst.setInt(1,lb.getUserId());
            cst.setString(2,lb.getFullName());
            cst.setString(3,lb.getEmail());
            cst.setString(4,lb.getUsername());
            cst.setString(5,lb.getPassword());
            cst.setString(6,lb.getRole());

            lb = new LogingBean(lb.getUserId(),lb.getFullName(),lb.getEmail(),lb.getUsername(),lb.getPassword(),lb.getRole());

            int x = cst.executeUpdate();
            if(x == 1 ){ System.out.println("row inserted");}

           // System.out.println(" info for user ID: " + lb.getUserId());
            System.out.println(" your data has been inserted into users table ");


        }catch (SQLException ex){
            ex.printStackTrace();
        }catch(IOException ex){
            ex.printStackTrace();
        }
        return lb;
    }

// get all guest in the hotel
    public List<LogingBean> getAllguests() {

        List<LogingBean> guests = new ArrayList<LogingBean>();

        LogingBean lb = null;


        try(Connection conn = DbConnection.getConnection()) {


            String sql = "SELECT * FROM USERS";

            pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();

            while(rs.next()) {

               /* Role role;

                if (rs.getString("Rolee") == "USER") {
                    role = new Role(2, "Guest");
                } else {
                    role = new Role(1, "Host");
                }*/

                int uid = rs.getInt("USERID");
                String name = rs.getString("FullNAME");
                String ema = rs.getString("EMAIL");
                String user = rs.getString("USERNAME");
                String pass = rs.getString("PASSWE");
                String r = rs.getString("rolee");

                lb = new LogingBean(uid, name, ema,user, pass,  r);
                guests.add(lb);

            }
        }catch (SQLException ex){
            ex.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }

        return guests;
    }

    }
