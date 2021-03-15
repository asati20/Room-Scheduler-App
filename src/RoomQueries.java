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
import java.util.ArrayList;

public class RoomQueries {
    
    private static Connection connection;
    
    private static ArrayList<String> roomsQueriesList = new ArrayList<String>();
    private static PreparedStatement getRoomsList;
    private static PreparedStatement checkRooms;
    private static ResultSet roomsList;
    private static ResultSet dateSet;
    private static PreparedStatement roomDrop;
    private static PreparedStatement addRooms;
    private static PreparedStatement getRooms;
    
    //getAllPossibleRooms
    public static ArrayList<String> getAllPossibleRooms(int seats, Date date)
    {
        connection = DBConnection.getConnection();
        try
        {
            getRoomsList = connection.prepareStatement("SELECT NAME, SEATS FROM ROOM ORDER BY SEATS ASC");
            roomsList = getRoomsList.executeQuery();
            
            outerloop:
            while(roomsList.next())
            {
            
                checkRooms = connection.prepareStatement("SELECT ROOM, DATE FROM RESERVATION");
                dateSet = checkRooms.executeQuery();
                while(dateSet.next())
                {
                    if(dateSet.getString(1).equals(roomsList.getString(1)) && dateSet.getDate(2).equals(date))
                        continue outerloop;                    
                }
                if (roomsList.getInt(2) >= seats)
                    roomsQueriesList.add(roomsList.getString(1));
            }
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
        
        return roomsQueriesList;        
    }
    
    
    
    public static ArrayList<RoomEntry> getAllRooms(){
        ResultSet roomEntry;
        connection = DBConnection.getConnection();
        ArrayList<RoomEntry> rooms = new ArrayList<RoomEntry>();
        try
        {           
            getRooms = connection.prepareStatement("SELECT NAME, SEATS FROM ROOM ORDER BY SEATS ASC");
            roomEntry = getRooms.executeQuery();
            
            while(roomEntry.next())
            {
                    rooms.add(new RoomEntry(roomEntry.getString(1), roomEntry.getInt(2)));
            }                      
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }       
        
        return rooms;
    }
    
    
    
    public static void addRoom(RoomEntry r){
        connection = DBConnection.getConnection();
        try
        {
            addRooms = connection.prepareStatement("INSERT INTO ROOM (NAME, SEATS) VALUES (?, ?)");
            addRooms.setString(1, r.getName());
            addRooms.setInt(2, r.getSize());
            addRooms.executeUpdate();
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
        WaitlistQueries.rebuildWaitlist();
    }
    
    
    
    public static void dropRoom(RoomEntry room){
        ArrayList<ReservationEntry> reservations = ReservationQueries.getReservationsByRoom(room.getName());        
        connection = DBConnection.getConnection();
      
        try
        {
            roomDrop = connection.prepareStatement("DELETE FROM ROOM WHERE NAME=(?)");
            roomDrop.setString(1,room.getName());
            roomDrop.executeUpdate();
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
        
        
        for(ReservationEntry e : reservations)
        {
            ReservationQueries.deleteReservationEntry(e);
        }
        
        for(ReservationEntry Rentry : reservations)
        {
            new ReservationEntry(Rentry.getFaculty(), Rentry.getDate(), Rentry.getSeats());
        }       
        
        WaitlistQueries.rebuildWaitlist();        
    }
}