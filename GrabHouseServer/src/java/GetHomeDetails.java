/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author gaurav
 */
public class GetHomeDetails extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ClassNotFoundException, SQLException, JSONException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            Class.forName("com.mysql.jdbc.Driver");
       
            String url = "jdbc:mysql://localhost:3306/grabhouse_db";
            String user = "root";
            String password = "admingaurav";
        
            Connection conn = null;
            try {
                conn = DriverManager.getConnection(url, user, password);
            } catch (SQLException ex) {
                Logger.getLogger(GetHomesJSON.class.getName()).log(Level.SEVERE, null, ex);
            }
            String homeID=request.getParameter("homeID");
            String sql = "SELECT * FROM HomeDetails where ListingID=?";
            PreparedStatement pss = conn.prepareStatement(sql);
            pss.setString(1, homeID);
           
            ResultSet rs = pss.executeQuery();
            //Map<String, ArrayList<String>> listMap;
            //listMap = new HashMap<String, ArrayList<String>>();
            JSONArray jsonArray = new JSONArray();
            while(rs.next()){
                JSONObject temp = new JSONObject();
                try {
                    temp.put("HOME_ID", Integer.toString(rs.getInt(1)));
                    temp.put("HOME_TITLE", rs.getString(2));
                    temp.put("HOME_LOCATION", rs.getString(3));
                    temp.put("HOME_LOCATION_URL", rs.getString(4));
                    temp.put("HOME_IMAGE_URL", rs.getString(5));
                    temp.put("HOME_RENT", rs.getString(6));
                    jsonArray.put(temp);

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            
            JSONObject homesObj = new JSONObject();
            homesObj.put("HOMES", jsonArray);



            String jsonStr = homesObj.toString();

            System.out.println("jsonString: "+jsonStr);
            
            
            
            System.out.println(jsonStr);
            response.getWriter().println(jsonStr);
            
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(GetHomeDetails.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(GetHomeDetails.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JSONException ex) {
            Logger.getLogger(GetHomeDetails.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(GetHomeDetails.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(GetHomeDetails.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JSONException ex) {
            Logger.getLogger(GetHomeDetails.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
