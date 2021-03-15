/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author nasat
 */

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
public class ReservationEntry {
    
    // If no reservation is made, place on waitlist
    private String faculty;
    private String room;
    private Date date;
    private int seats;
    private Object timestamp;
    
    //Making new reservation entry
    public ReservationEntry(String faculty, Date date, int seats){
        this.faculty = faculty;       
        this.date = date;
        this.seats = seats;
        this.room = getPossibleRoom();
        
        ReservationQueries.addReservationEntry(this);
    }
    
    //Inputting existing reservation entry
    public ReservationEntry(String faculty, Date date, int seats, String room, Object timestamp){
        this.faculty = faculty;       
        this.date = date;
        this.seats = seats;
        this.room = room;
        this.timestamp = timestamp;
    }

    
    //getting rooms availabe for reservations and returning availabe rooms
    public String getPossibleRoom(){
        ArrayList <String> rooms = RoomQueries.getAllPossibleRooms(seats, date);  

        if(rooms.isEmpty()){
            return null;
            
        } else {
            room = rooms.get(0);
            rooms.clear();
        }
        return room;
    }   
    
    public Object getTimestamp() {
        return timestamp;
    }
    
    public String getFaculty(){
        return faculty;
    }
    
    public String getRoom(){
        return room;
    }
    
    public Date getDate(){
        return date;
    }
    
    public int getSeats(){
        return seats;
    }

    @Override
    public String toString(){
        String table = String.format("%s \t %s \t %s \t %d ", faculty, room, date.toString(), seats);
        return table;
    }
}