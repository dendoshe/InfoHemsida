/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package informatikhemsida;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javax.swing.JTextField;

public class DataAccess {
    
    JTextField textruta1;
    JTextField textruta2;
   
    boolean match;
    boolean godkant;
    String connectionURL = "jdbc:sqlserver://localhost:53158;databaseName=InfoHemsida;user=admin;password=team15";
    Connection con;
    
    public boolean verifieraInlogg(String inMejl, String inLösenord) throws SQLException, ClassNotFoundException{
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        this.con = DriverManager.getConnection(connectionURL);
        
        /*Klass för att göra en sql string exekverbar*/
        Statement st = con.createStatement();
        
        /*Spara alla användare som matchar värdena i en tabell*/
        ResultSet mejladresser = st.executeQuery("Select KontoID from konto where mejladress = " + 
                                                        "'" + inMejl + "'AND Lösenord = '" + inLösenord + "'");
        ArrayList <String> matchID = new ArrayList();
        
        /*Om loopen har ngt att iterera över*/
        while (mejladresser.next()){
            match = true;
        }

        return match;
    }
    
    public boolean verifieraAdmin(String inMejl) throws SQLException, ClassNotFoundException{
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        this.con = DriverManager.getConnection(connectionURL);
        
        /*Klass för att göra en sql string exekverbar*/
        Statement st = con.createStatement();
        
        /*Spara admins i en tabell*/
        ResultSet admins = st.executeQuery("Select KontoID, MejlAdress from konto where AdminFunktionalitet = 1 AND MejlAdress = '" + inMejl + "'");
        
        match = false;
        
       while(admins.next()){

           match = true;
       }
       
       return match;
    }
        
    
    public void skapaKonto(String mejladress, String losenord) throws ClassNotFoundException, SQLException{
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        this.con = DriverManager.getConnection(connectionURL);
        Statement st = con.createStatement();
        st.execute("INSERT INTO KONTO (Mejladress, Lösenord, Notis, AdminFunktionalitet) VALUES"
        + "('" + mejladress + "','" + losenord + "', 1,1)");
        
        
    }
    
    public void skapaAnslag(){
    
    
    
    }
    

        
        
    
    
}
