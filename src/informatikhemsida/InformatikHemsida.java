
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
import java.util.logging.Level;
import java.util.logging.Logger;

public class InformatikHemsida {

    public static void main(String[] args) throws SQLException, ClassNotFoundException {

        /*detta vet jag inte vad det är*/
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

        /*detta är vad som upprättar en connection. Om det blir en error med något med TCP/IP, fråga Diana*/
        String connectionURL = "jdbc:sqlserver://localhost:1434;databaseName=Informatik;user=admin;password=team15";
        
        /*Detta är en variabel för att komma åt data via connection*/
        Connection con = DriverManager.getConnection(connectionURL);
      
        
        System.out.println("Succé!");
        
        /*testa att det fungerar*/
        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery("Select * from Konto");
        while (rs.next()){
        int ID = rs.getInt(1);
        String Name = rs.getString(2);
        System.out.println(ID + " " + Name);}
        
        
    }
    
    
    
}

