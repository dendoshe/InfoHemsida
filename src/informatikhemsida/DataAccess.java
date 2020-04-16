/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package informatikhemsida;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTextField;

public class DataAccess {

    JTextField textruta1;
    JTextField textruta2;

    boolean match;
    boolean godkant;
    String connectionURL = null;
    Connection con;
    Statement st = null;
    PreparedStatement ps = null;

    String insertAnslag = "INSERT INTO Anslag (AInnehåll, Kategori) VALUES (?, ?)";

    /*
    * 1. Skapa ett nytt DataAccess object med dina inloggningsuppgifter:
    *   DataAccess dataAccessObject = new DataAccess("Milky", "milkmaster");
    * 
    * 2. Kör metoder, ex:
    *   dataAccessObject.laggUppAnslag();
     */
    public DataAccess(String user, String pass) {
        connectionURL = "jdbc:sqlserver://localhost:1433;databaseName=Informatik;user=" + user + ";password=" + pass;

        try {
            this.con = DriverManager.getConnection(connectionURL);
        } catch (SQLException ex) {
            Logger.getLogger(DataAccess.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public boolean verifieraInlogg(String inMejl, String inLösenord) throws SQLException, ClassNotFoundException {
        connectionURL = "jdbc:sqlserver://localhost:53158;databaseName=Informatik;user=admin;password=team15";
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        this.con = DriverManager.getConnection(connectionURL);

        /*Klass för att göra en sql string exekverbar*/
        st = con.createStatement();

        /*Spara i en tabell*/
        ResultSet hamtaMejladresser = st.executeQuery("Select KontoID from konto where mejladress = "
                + "'" + inMejl + "'AND Lösenord = '" + inLösenord + "'");

        ArrayList<String> matchID = new ArrayList();

        while (hamtaMejladresser.next()) {
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
        + "('" + mejladress + "','" + losenord + "', 1,0)");
        
        
    }
    
    
    public void tilldelaAdmin(String inMejl) throws ClassNotFoundException, SQLException{
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        this.con = DriverManager.getConnection(connectionURL);
        Statement st = con.createStatement();
        
        st.execute("Update Konto set AdminFunktionalitet = 1 where Mejladress = '" + inMejl + "'");
    }
    
    public void taBortAdmin(String inMejl) throws ClassNotFoundException, SQLException{
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        this.con = DriverManager.getConnection(connectionURL);
        Statement st = con.createStatement();
        
        st.execute("Update Konto set AdminFunktionalitet = 0 where Mejladress = '" + inMejl + "'");
    
    }
    
    public void skapaAnslag(){
    
    
    
    }
    

        
        
    
    
}
