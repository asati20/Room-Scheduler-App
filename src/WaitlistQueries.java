/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author nasat
 */
import java.sql.SQLException;
import java.sql.Connection;
import java.util.ArrayList;
import java.sql.Timestamp;
import java.util.Calendar;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class WaitlistQueries {
    
    private static Connection connection;
    private static PreparedStatement addWaitlist;
    private static PreparedStatement getWaitlistByDate;
    private static PreparedStatement getWaitlistByFaculty;
    private static PreparedStatement deleteEntry;
    
    public static void addWaitlistEntry(String name, Date date, int seats)
    {
        connection = DBConnection.getConnection();
        try
        {
            java.sql.Timestamp currentTimestamp = new java.sql.Timestamp(Calendar.getInstance().getTime().getTime());
            
            addWaitlist = connection.prepareStatement("INSERT INTO WAITLIST (FACULTY, DATE, SEATS, TIMESTAMP) VALUES (?,?,?,?)");
            addWaitlist.setString(1, name);
            addWaitlist.setDate(2, date);
            addWaitlist.setInt(3, seats);
            addWaitlist.setTimestamp(4, currentTimestamp);
            addWaitlist.executeUpdate();          
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }        
    }
    
    public static ArrayList<WaitlistEntry> getWaitlist(){
        ResultSet waitListEntry;
        connection = DBConnection.getConnection();
        ArrayList<WaitlistEntry> waitList = new ArrayList<WaitlistEntry>();
        try{
                       
            getWaitlistByDate = connection.prepareStatement("SELECT FACULTY, DATE, SEATS, TIMESTAMP FROM WAITLIST ORDER BY DATE, TIMESTAMP DESC");
            waitListEntry = getWaitlistByDate.executeQuery();
            
            while(waitListEntry.next()){
                waitList.add(new WaitlistEntry(waitListEntry.getString(1), waitListEntry.getDate(2), waitListEntry.getInt(3), waitListEntry.getTimestamp(4)));                
            }                      
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
        
        return waitList;
    }
    
    public static ArrayList<WaitlistEntry> getWaitlistByFaculty(String faculty){
        ResultSet waitListEntry;
        connection = DBConnection.getConnection();
        ArrayList<WaitlistEntry> waitlist = new ArrayList<>();
        try{
                       
            getWaitlistByFaculty = connection.prepareStatement("SELECT FACULTY, DATE, SEATS, TIMESTAMP FROM WAITLIST ORDER BY DATE, TIMESTAMP DESC");
            waitListEntry = getWaitlistByFaculty.executeQuery();
            
            while(waitListEntry.next()){
                if(waitListEntry.getString(1).equals(faculty)){
                    waitlist.add(new WaitlistEntry(waitListEntry.getString(1), waitListEntry.getDate(2), waitListEntry.getInt(3), waitListEntry.getTimestamp(4)));      
                }
            }                      
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
        
        return waitlist;
    }
    
    
    public static void deleteWaitlistEntry(WaitlistEntry e){
        
        connection = DBConnection.getConnection();
        try{
            deleteEntry = connection.prepareStatement("DELETE FROM WAITLIST WHERE FACULTY=(?) AND DATE=(?)");
            deleteEntry.setString(1,e.getFaculty());
            deleteEntry.setDate(2,e.getDate());            
            deleteEntry.executeUpdate();            
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
    }
    
    
    // takes the waitlist array and tries to get a reservation for each entry if there is any space, if not, the entry goes back to waitlist
    public static void rebuildWaitlist(){
        
        ArrayList<WaitlistEntry> waitlist = new ArrayList<WaitlistEntry>();
        waitlist = WaitlistQueries.getWaitlist();
        
        if (!waitlist.isEmpty()){
            
            for(WaitlistEntry e : waitlist){
                deleteWaitlistEntry(e);
                new ReservationEntry(e.getFaculty(), e.getDate(), e.getSeats());
            }  
        }
    }
}

