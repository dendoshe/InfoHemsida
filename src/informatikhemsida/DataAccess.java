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
    String connectionURL = "jdbc:sqlserver://localhost:1434;databaseName=Informatik;user=admin;password=team15";
    Connection con;
    
    public boolean verifieraInlogg(String inMejl, String inLösenord) throws SQLException, ClassNotFoundException{
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        this.con = DriverManager.getConnection(connectionURL);
        
        /*Klass för att göra en sql string exekverbar*/
        Statement st = con.createStatement();
        
        /*Spara i en tabell*/
        ResultSet hamtaMejladresser = st.executeQuery("Select KontoID from konto where mejladress = " + 
                                                        "'" + inMejl + "'AND Lösenord = '" + inLösenord + "'");
                
        ArrayList <String> matchID = new ArrayList();
        
        while (hamtaMejladresser.next()){
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
    
}
