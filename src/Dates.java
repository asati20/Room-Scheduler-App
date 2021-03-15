

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author nasat
 */
public class Dates {
    
    private static Connection connection;
    private static ArrayList<Dates> dates = new ArrayList<Dates>();
    private static PreparedStatement getDatesList;
    private static PreparedStatement addDate;
    
    
    public static ArrayList<Date> getDatesList()
    {
        connection = DBConnection.getConnection();
        ResultSet dateEntry;
        ArrayList<Date> dates = new ArrayList<Date>();
        try
        {
            getDatesList = connection.prepareStatement("SELECT DATE FROM DATE ORDER BY DATE");
            dateEntry = getDatesList.executeQuery();
            
            while(dateEntry.next())
            {
                dates.add(dateEntry.getDate(1));
            }
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
        return dates;
        
    }

    public static void addDate(String date)
    { 
        connection = DBConnection.getConnection();
        try{
            addDate = connection.prepareStatement("INSERT INTO DATE (DATE) VALUES (?)");
            addDate.setString(1, date);
            addDate.executeUpdate();
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
    }

}
