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


public class WaitlistEntry {
    
    private String faculty;
    private Date date;
    private int seats;
    private Object timestamp;
    
    public WaitlistEntry(String faculty, Date date, int seats, Object timestamp){
        this.faculty = faculty;
        this.date = date;
        this.seats = seats;
        this.timestamp = timestamp;
        
        //WaitlistQueries.addWaitlistEntry(faculty, date,  seats);
    }

    public Object getTimestamp() {
        return timestamp;
    }
    
    public String getFaculty(){
        return faculty;
    }
    
    public Date getDate(){
        return date;
    }
    
    public int getSeats(){
        return seats;
    }
    
    @Override
    public String toString(){
        String msg = String.format("%s \t %s \t %d ", faculty, date.toString(), seats);
        return msg;
    }
}


