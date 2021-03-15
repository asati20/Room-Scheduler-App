/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author nasat
 */
    
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;

public class ReservationQueries {
    
    private static Connection connection;
    private static PreparedStatement addReservation;    
    private static PreparedStatement getReservationByDate;
    private static PreparedStatement getReservationByRoom;
    private static PreparedStatement deleteEntry;
    private static PreparedStatement getReservationByFaculty;
    
    
    public static void addReservationEntry(ReservationEntry entry)
    {
        connection = DBConnection.getConnection();
        try
        {           
            if(entry.getRoom() == null){
                WaitlistQueries.addWaitlistEntry(entry.getFaculty(), entry.getDate(), entry.getSeats());
            } else {
                java.sql.Timestamp currentTimestamp = new java.sql.Timestamp(Calendar.getInstance().getTime().getTime());
            
                addReservation = connection.prepareStatement("INSERT INTO RESERVATION (FACULTY, ROOM, DATE, SEATS, TIMESTAMP) VALUES (?,?,?,?,?)");
                addReservation.setString(1, entry.getFaculty());
                addReservation.setString(2, entry.getRoom());
                addReservation.setDate(3, entry.getDate());
                addReservation.setInt(4, entry.getSeats());
                addReservation.setTimestamp(5, currentTimestamp);
                addReservation.executeUpdate();
            }          
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
    }
    
    public static ArrayList<ReservationEntry> getReservationsByDate(Date date){
        // Obtain reservations by date
        ResultSet reservationEntry;
        connection = DBConnection.getConnection();
        ArrayList<ReservationEntry> reservationList = new ArrayList<>();
        try{
                       
            getReservationByDate = connection.prepareStatement("SELECT FACULTY, ROOM, DATE, SEATS, TIMESTAMP FROM RESERVATION ORDER BY DATE DESC");
            reservationEntry = getReservationByDate.executeQuery();
            
            while(reservationEntry.next()){
                if(reservationEntry.getDate(3).equals(date)){
                    reservationList.add(new ReservationEntry(reservationEntry.getString(1), reservationEntry.getDate(3), reservationEntry.getInt(4), reservationEntry.getString(2), reservationEntry.getTimestamp(5)));
                }
            }                      
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
        
        return reservationList;        
    }
    
    
    public static ArrayList<ReservationEntry> getReservationsByRoom(String room){
        // Obtain reservations by room
        ResultSet reservationEntry;
        connection = DBConnection.getConnection();
        ArrayList<ReservationEntry> reservations = new ArrayList<ReservationEntry>();
        try{
                       
            getReservationByRoom = connection.prepareStatement("SELECT FACULTY, ROOM, DATE, SEATS, TIMESTAMP FROM RESERVATION ORDER BY DATE, TIMESTAMP ASC");
            reservationEntry = getReservationByRoom.executeQuery();
            
            while(reservationEntry.next()){
                if(reservationEntry.getString(2).equals(room)){
                    reservations.add(new ReservationEntry(reservationEntry.getString(1), reservationEntry.getDate(3), reservationEntry.getInt(4), reservationEntry.getString(2), reservationEntry.getTimestamp(5)));
                }
            }                      
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }       
        
        return reservations;
    }
    
    
    public static ArrayList<ReservationEntry> getReservationsByFaculty(String faculty){
        // Obtain reservations by faculty
        ResultSet reservationEntry;
        connection = DBConnection.getConnection();
        ArrayList<ReservationEntry> reservations = new ArrayList<ReservationEntry>();
        try{
                       
            getReservationByFaculty = connection.prepareStatement("SELECT FACULTY, ROOM, DATE, SEATS, TIMESTAMP FROM RESERVATION ORDER BY DATE, TIMESTAMP ASC");
            reservationEntry = getReservationByFaculty.executeQuery();
            
            while(reservationEntry.next()){
                if(reservationEntry.getString(1).equals(faculty)){
                    reservations.add(new ReservationEntry(reservationEntry.getString(1), reservationEntry.getDate(3), reservationEntry.getInt(4), reservationEntry.getString(2), reservationEntry.getTimestamp(5)));
                }
            }                      
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }       
        
        return reservations;
    }
    
    
    public static void deleteReservationEntry(ReservationEntry e){
        
        connection = DBConnection.getConnection();
        try{
            deleteEntry = connection.prepareStatement("DELETE FROM RESERVATION WHERE FACULTY=(?) AND ROOM=(?) AND DATE=(?)");
            deleteEntry.setString(1,e.getFaculty());
            deleteEntry.setString(2,e.getRoom());
            deleteEntry.setDate(3,e.getDate());
            deleteEntry.executeUpdate();            
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
    }
    
}

