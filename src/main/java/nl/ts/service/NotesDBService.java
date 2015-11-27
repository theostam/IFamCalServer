package nl.ts.service;

import nl.ts.domain.Note;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by a132552 on 3-7-2015.
 */
public class NotesDBService {
    private Connection connection;

    public Connection getConnection(){
        if (connection == null){
            try {
                InitialContext ic = new InitialContext();
                Context initialContext = (Context) ic.lookup("java:/comp/env");
                DataSource datasource = (DataSource) initialContext.lookup("jdbc/PostgreSQLDS");
                connection = datasource.getConnection();

            } catch (Exception ex) {
                System.out.println("Exception: " + ex + ex.getMessage());
            }
        }
        return connection;
    }

    public String findLastChangeDate( String user){
        String datefound = null;
        Connection conn = getConnection();
        try {
            Statement stmt = conn.createStatement() ;
            String query = "SELECT max(n.change_date) FROM Note n where n.name != '" + user + "'";
            ResultSet rs = stmt.executeQuery(query) ;
            if (rs.next()) {
                datefound = rs.getString(1);
            }
        } catch (Exception ex) {
            System.out.println("Exception: " + ex + ex.getMessage());
        }
        return datefound;
    }
    private List<Note> findByDate( String date){
        List<Note> result = new ArrayList<Note>();
        Connection conn = getConnection();
        try {
            Statement stmt = conn.createStatement() ;

            String query = "SELECT * FROM Note n where n.date = '" + date + "'";
            ResultSet rs = stmt.executeQuery(query) ;
            while (rs.next()) {
                Note note = new Note(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4));
                result.add(note);
            }
        } catch (Exception ex) {
            System.out.println("Exception: " + ex + ex.getMessage());
        }
        return result;
    }

    public List<Note> getNotesSince( String user,  String date) {
        // get all notes modified after param 'date'.
        // Except the notes of param 'user', since 'user' was the changer/updater
        List<Note> result = new ArrayList<Note>();
        Connection conn = getConnection();
        try {
            Statement stmt = conn.createStatement() ;

            String query = "SELECT * FROM Note n where n.date > '" + date + "' and n.name != '" + user + "'";
            ResultSet rs = stmt.executeQuery(query) ;
            while (rs.next()) {
//                Note note = new Note(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4));
                Note note = new Note(rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5));
                System.out.println("found: " + note);
                result.add(note);
            }
        } catch (Exception ex) {
            System.out.println("Exception: " + ex + ex.getMessage());
        }
        return result;
    }

    public void saveOrUpdate( Note note){
        String query = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String today = sdf.format(new Date());

        if (findByNameAndDate(note.getName(), note.getDate()) != null){
            query = "UPDATE Note SET " +
                    "change_date='" + today + "', " +
                    "text='" + note.getText() + "' "+
                    "WHERE "+
                    "date='" + note.getDate() + "' AND " +
                    "name='" + note.getName() + "'"
            ;
        } else{
            query = "INSERT INTO Note (change_date,date,name,text) VALUES ("+
                    today + "," +
                    note.getDate()+",'"+
                    note.getName()+"','"+
                    note.getText()+ "');"
            ;
        }
        Connection conn = getConnection();
        try {
            Statement stmt = conn.createStatement() ;
 System.out.println("query="+query);
            stmt.executeQuery(query) ;
        } catch (Exception ex) {
            System.out.println("Exception: " + ex + ex.getMessage());
        }
    }

    private Note findByNameAndDate(String name, String date){
        Note result = null;
        Connection conn = getConnection();
        try {
            Statement stmt = conn.createStatement() ;

            String query = "SELECT * FROM Note n where n.name = '" + name + "' and n.date = '" + date + "'";
            ResultSet rs = stmt.executeQuery(query) ;
            if (rs.next()) {
                result = new Note(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4));
            }
        } catch (Exception ex) {
            System.out.println("Exception: " + ex + ex.getMessage());
        }
        return result;
    }

}
