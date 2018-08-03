package com.revature.controller;

import com.revature.Connectivity.DbConnection;
import com.revature.Dao.LogingDao;
import com.revature.bean.LogingBean;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/LogingServlet")
public class LogingServlet extends HttpServlet {


    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
       // super.doGet(req, resp);//in case you put the path /LogingServlet in the address bar
        resp.setContentType("text/html");
        PrintWriter pw = resp.getWriter();

        LogingDao dao = new LogingDao();

       /* List<LogingBean> users = dao.getAllguests();

        for (LogingBean user : users) {
            pw.println( );
            pw.println(user.toString());
        }*/
        pw.print("<html>");
        pw.print("<head><title>All Guests </title></head>");
        pw.print("</body>");
        pw.print("<center>");

        PreparedStatement pst = null;
        CallableStatement cst = null;


        List<LogingBean> guests = null ;

        LogingBean lb = null;


        try(Connection conn = DbConnection.getConnection()) {

            guests  = new ArrayList<LogingBean>();

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

                int userid = rs.getInt("USERID");
                String name = rs.getString("FullNAME");
                String email = rs.getString("EMAIL");
                String username = rs.getString("USERNAME");
                String password = rs.getString("PASSWE");
                String role = rs.getString("rolee");

                pw.print(userid + " :: ");
                pw.print(name + " :: ");
                pw.print(email + " :: ");
                pw.print(username + " :: ");
                pw.print(password + " :: ");
                pw.print(role + " :: ");
                pw.print("");

                    /*lb = new LogingBean(uid, name, ema,user, pass,  r);

                    guests.add(lb);*/

            }
        }catch (SQLException ex){
            ex.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
        pw.print("</center>");
        pw.println("</body>");
        pw.println("</html>");

        pw.close();



    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {


        response.setContentType("text/html charset=utf-8");
        PrintWriter pw = response.getWriter();

        String username = request.getParameter("username");//ce qui est en guillemet(paranthese) est ce que je recois du client
        String password = request.getParameter("password");

        LogingBean logingbean = new LogingBean();

        logingbean.setUsername(username);
        logingbean.setPassword(password);



        LogingDao dao = new LogingDao();


        try {

            String userValidation = dao.authenticateUser(logingbean);

            //was userValidation
            switch (userValidation) {
                case "Host's Role": {

                    HttpSession session = request.getSession();
                    session.setAttribute("Host", username);//garder les info du client ds une session
                    request.setAttribute("username", username);

                    request.getRequestDispatcher("Host.html").forward(request, response);


                    break;
                }
                case "User's Role": {

                    HttpSession session = request.getSession();
                    session.setAttribute("User", username);
                    request.setAttribute("username", username);

                    request.getRequestDispatcher("User.html").forward(request, response);
                    break;
                }
                default:
                    pw.println("Error Message:  " + userValidation);
                    request.setAttribute("error message", userValidation);

                    break;
               }

            } catch (IOException e) {
                 e.printStackTrace();
        }
        System.out.println("username " + username);


        System.out.println("password " +password);

        // if host want to see a guest info
        //pw.println(dao.getUserLoginInfo(username));//this works

        //if host want to create/register a guest
        pw.println(dao.createUser(logingbean));//not working now

        //if host want to see all guests
       // pw.println(dao.getAllguests());

        }


    }

