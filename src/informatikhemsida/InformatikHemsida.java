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

        /*Detta är vad som upprättar en connection. 
        Inför detta ska du ha: 
        1. Microsoft Management Studio
        2. Skapat ett nytt login med user = admin och password = team15
        3. Skapat en databas som heter Informatik
        4. Kört in våra tabeller (finns i wordpad dokument som heter "databas")
        5. Kört in vår exempeldata (finns i wordpad dokument som heter "exempeldata") 
        Om det blir en error med något med TCP/IP, fråga Diana
        */
        String connectionURL = "jdbc:sqlserver://localhost:1433;databaseName=Informatik;user=admin;password=team15";
        
        /*Detta är en variabel för att komma åt data via connection. Denna använder du i alla metoder som vill komma åt databasen*/
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
