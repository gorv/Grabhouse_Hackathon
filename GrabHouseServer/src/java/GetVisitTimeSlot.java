/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Raghav
 */
public class GetVisitTimeSlot extends HttpServlet {

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
            /* TODO output your page here. You may use following sample code. */
            Class.forName("com.mysql.jdbc.Driver");
       
            
            String url = "jdbc:mysql://localhost:3306/grabhouse_db";
            String user = "root";
            String password = "admingaurav";
        
            Connection conn = null;
            try {
                conn = DriverManager.getConnection(url, user, password);
            } catch (SQLException ex) {
                Logger.getLogger(GetVisitTimeSlot.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            String home_id = request.getParameter("home_id");
            String startDate = request.getParameter("startDate");
            String fromtime = request.getParameter("fromtime");
            String totime = request.getParameter("totime");
            String username = request.getParameter("username");
            
            String [] splittedInputString=fromtime.split(":");
            int startInputMinutes = Integer.parseInt(splittedInputString[0])*60 + Integer.parseInt(splittedInputString[1]);
            
            String [] splittedInputStringEnd=totime.split(":");
            int endInputMinutes = Integer.parseInt(splittedInputStringEnd[0])*60 + Integer.parseInt(splittedInputStringEnd[1]);
                
            
                    /*
            startDate="2015-11-05";
            username="ebc@gmail.com";
            home_id="5";
            */
            
            String sql="SELECT COUNT(*) as count FROM scheduleTable WHERE ScheduleDate  BETWEEN DATE_SUB("+"'"+startDate+"'"+", INTERVAL 7 DAY) AND DATE_ADD("+"'"+startDate+"'"+", INTERVAL 7 DAY) and UserEmail="+"'"+username+"'"+" and ListingID="+"'"+home_id+"'"+"";
            System.out.println(sql);
            PreparedStatement statement=conn.prepareStatement(sql);  
            ResultSet rs = statement.executeQuery(sql);
      
            while(rs.next()){
                int count  = rs.getInt("count");
                System.out.print("count: " + count);
                JSONObject temp = new JSONObject();
                if(count<1){ 
                    temp.put("RESPONSE_CODE", "100");
                    
                }
                else{
                    temp.put("RESPONSE_CODE", "200");
                    String sql1="SELECT ScheduleFromTime,ScheduleToTime from scheduleTable where ListingID="+"'"+home_id+"'"+" and ScheduleFromTime>="+"'"+fromtime+"'"+" and ScheduleToTime<="+"'"+totime+"'"+"";
                    System.out.println(sql1);
                    statement=conn.prepareStatement(sql1);  
                    rs = statement.executeQuery(sql1);
                    boolean totalNumberLine[]=new boolean[1441];
                    for(int i=0;i<=1440;i++){
                        totalNumberLine[i]=false;
                    }
                    while(rs.next()){
                        //System.out.println("FROM "+rs.getString("ScheduleFromTime"));
                        //System.out.println("TO "+rs.getString("ScheduleToTime"));
                        String [] splittedString=rs.getString("ScheduleFromTime").split(":");
                        int startTimeMinutes = Integer.parseInt(splittedString[0])*60 + Integer.parseInt(splittedString[1]);
                        String [] splittedEndString=rs.getString("ScheduleToTime").split(":");
                        int endTimeMinutes = Integer.parseInt(splittedEndString[0])*60 + Integer.parseInt(splittedEndString[1]);
                        System.out.println("QUERY LEFT "+startTimeMinutes);
                        System.out.println("QUERY RIGHT "+endTimeMinutes);
                        for(int i=startTimeMinutes;i<=endTimeMinutes;i++){
                            totalNumberLine[i]=true;
                        }
                    }
                    System.out.println("INPUT LEFT "+startInputMinutes);
                    System.out.println("INPUT RIGHT "+endInputMinutes);
                    ArrayList<String> rangesLeft=new ArrayList<>();
                    ArrayList<String> rangesRight = new ArrayList<>();
                    int currentLeft=0;
                    int currentRight=0;
                    boolean gotLeft=false;
                    for(int i=startInputMinutes;i<=endInputMinutes;i++){
                        if(totalNumberLine[i-1]==true && !gotLeft){
                            gotLeft=true;
                            currentLeft=i;
                        }
                        if(gotLeft && i!=currentLeft && 
                             totalNumberLine[i-1]==false && totalNumberLine[i+1]==false)
                        {
                            continue;
                        }
                        if(gotLeft && totalNumberLine[i+1]==true){
                            currentRight=i;
                            gotLeft=false;
                            rangesLeft.add(currentLeft/60 + ":"+currentLeft%60);
                            rangesRight.add(currentRight/60 + ":" + currentRight%60);
                        }
                    }
                    
                    for(int i=0;i<rangesLeft.size();i++){
                        System.out.println(rangesLeft.get(i)+"-"+rangesRight.get(i));
                    }
                    
            
                }
                
         
            }
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
            Logger.getLogger(GetVisitTimeSlot.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(GetVisitTimeSlot.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JSONException ex) {
            Logger.getLogger(GetVisitTimeSlot.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(GetVisitTimeSlot.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(GetVisitTimeSlot.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JSONException ex) {
            Logger.getLogger(GetVisitTimeSlot.class.getName()).log(Level.SEVERE, null, ex);
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
