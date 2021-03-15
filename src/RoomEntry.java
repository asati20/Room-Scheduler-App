/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author nasat
 */
public class RoomEntry {
    private int roomSeats;
    private String name;
    public RoomEntry(String room, int seats){
        this.name = room;
        this.roomSeats = seats;
    }
    
    public String getName(){return name;}
    
    public int getSize(){return roomSeats;}
    
    @Override
    public String toString()
    {
       //          m  
        return name + " (" + roomSeats + " seats)";
    }
}