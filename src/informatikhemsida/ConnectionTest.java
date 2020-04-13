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

/**
 *
 * @author Diana Jumaili
 */
public class ConnectionTest {
    
    
    /*För att köra detta behöver du högerklicka på klassen i menyn till höger och trycka run file*/
     public static void main(String[] args) throws ClassNotFoundException, SQLException  {
    
        /*detta hämtar en JDBC (Java Database Connection) interface*/
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

        /*detta är vad som upprättar en connection. Om det blir en error med något med TCP/IP, fråga Diana*/
        String connectionURL = "jdbc:sqlserver://localhost:53158;databaseName=InfoHemsida;user=admin;password=team15";
        
        /*Detta är en variabel för att komma åt data via connection*/
        Connection con = DriverManager.getConnection(connectionURL);
        
        /*testa att det fungerar*/
        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery("Select * from Konto");
        while (rs.next()){
        int ID = rs.getInt(1);
        String Name = rs.getString(2);
        System.out.println(ID + " " + Name);}
        System.out.println("Succé!");
    }
        
    

            
    
        
}
