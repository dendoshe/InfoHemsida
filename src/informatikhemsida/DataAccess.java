/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package informatikhemsida;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
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
import java.sql.Blob;
import java.awt.Desktop;

public class DataAccess {

    JTextField textruta1;
    JTextField textruta2;

    boolean match;
    boolean godkant;
    String connectionURL = null;
    Connection con;
    Statement st = null;
    PreparedStatement ps = null;
    PreparedStatement fps = null;

    String insertAnslag = "INSERT INTO Anslag (AInnehåll, Kategori) VALUES (?, ?)";
    String fileUpload = "UPDATE Anslag SET Filnamn = ?, Fil = ?, Filformat = ? WHERE AnslagID = ?";
    String getFile = "SELECT Fil, Filformat FROM Anslag WHERE Fil IS NOT NULL AND Filformat IS NOT NULL AND AnslagID = ?";
    String getAdminStatus = "SELECT AdminFunktionalitet FROM Konto WHERE Mejladress = ?";
    String taBortAnslag = "DELETE FROM Anslag WHERE AnslagID = ?";

    // Sparar adminstatus vid inloggning
    int admin;

    /*
     * 1. Skapa ett nytt DataAccess object med dina inloggningsuppgifter: DataAccess
     * dataAccessObject = new DataAccess("Milky", "milkmaster");
     * 
     * 2. Kör metoder, ex: dataAccessObject.laggUppAnslag();
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
        connectionURL = "jdbc:sqlserver://localhost:1433;databaseName=Informatik;user=Milky;password=milkmaster";
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        this.con = DriverManager.getConnection(connectionURL);

        /* Klass för att göra en sql string exekverbar */
        st = con.createStatement();

        /* Spara i en tabell */
        ResultSet hamtaMejladresser = st.executeQuery("Select KontoID from konto where mejladress = " + "'" + inMejl
                + "'AND Lösenord = '" + inLösenord + "'");

        ArrayList<String> matchID = new ArrayList();

        while (hamtaMejladresser.next()) {
            match = true;
        }

        ps = con.prepareStatement(getAdminStatus);
        ps.setString(1, inMejl);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            admin = rs.getInt("AdminFunktionalitet");
        }
        System.out.println("Adminstatus: " + admin);

        return match;
    }

    public boolean verifieraAdmin(String inMejl) throws SQLException, ClassNotFoundException {
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        this.con = DriverManager.getConnection(connectionURL);

        /* Klass för att göra en sql string exekverbar */
        Statement st = con.createStatement();

        /* Spara admins i en tabell */
        ResultSet admins = st
                .executeQuery("Select KontoID, MejlAdress from konto where AdminFunktionalitet = 1 AND MejlAdress = '"
                        + inMejl + "'");

        match = false;

        while (admins.next()) {

            match = true;
        }

        return match;
    }

    public void skapaKonto(String mejladress, String losenord) throws ClassNotFoundException, SQLException {
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        this.con = DriverManager.getConnection(connectionURL);
        Statement st = con.createStatement();
        st.execute("INSERT INTO KONTO (Mejladress, Lösenord, Notis, AdminFunktionalitet) VALUES" + "('" + mejladress
                + "','" + losenord + "', 1,0)");

    }

    public void tilldelaAdmin(String inMejl) throws ClassNotFoundException, SQLException {
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        this.con = DriverManager.getConnection(connectionURL);
        Statement st = con.createStatement();

        st.execute("Update Konto set AdminFunktionalitet = 1 where Mejladress = '" + inMejl + "'");
    }

    public void taBortAdmin(String inMejl) throws ClassNotFoundException, SQLException {
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        this.con = DriverManager.getConnection(connectionURL);
        Statement st = con.createStatement();

        st.execute("Update Konto set AdminFunktionalitet = 0 where Mejladress = '" + inMejl + "'");

    }

    public void skapaAnslag() {

    }

    public void laggUppAnslag(Map<Object, Object> anslag) {
        String header = anslag.get(" Rubrik").toString();
        String body = anslag.get("AInnehåll").toString();
        int category = (int) anslag.get("Kategori");
        File file = (File) anslag.get("Fil");
        BigDecimal anslagID = null;

        try {
            //"new String[]{"AnslagID"} gör så man kan returnera primärnyckel i samma SQL-fråga
            ps = con.prepareStatement(insertAnslag, new String[]{"AnslagID"});
            ps.setString(1, body);
            ps.setInt(2, category);
            ps.executeUpdate();

            //Hämta primärnyckel av det som precis exekverades i SQL
            ResultSet rs = ps.getGeneratedKeys();
            while (rs.next()){
                anslagID = (BigDecimal) rs.getObject(1);
            }

            laddaUppFil(file, anslagID);

        } catch (SQLException ex) {
            Logger.getLogger(DataAccess.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println(ex.getMessage());
        } finally { // stänger ps och connection för att undvika memory leakage
            try {
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(DataAccess.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    /*
    * Tar bort anslag med angivet anslagID
    */
    public void taBortAnslag (int anslagID){
        try {
            ps = con.prepareStatement(taBortAnslag);
            ps.setInt(1, anslagID);
            ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DataAccess.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println(ex.getMessage());
        } finally { // stänger ps och connection för att undvika memory leakage
            try {
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(DataAccess.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void laddaUppFil(File file, BigDecimal anslagID) {

        try {
            this.con = DriverManager.getConnection(connectionURL);
            try {
                FileInputStream fis = new FileInputStream(file);
                fps = con.prepareStatement(fileUpload);

                // Sätt in filnamn i SQL-kommandot i parameter 1
                fps.setString(1, file.getName());

                // Sätt in fil i SQL-kommandot i parameter 2
                fps.setBinaryStream(2, fis, (int) file.length());
            } catch (FileNotFoundException e) {
                System.out.println(e.getMessage());
            }

            // Hitta filtyp och sätt in det i SQL-kommandot i parameter 3
            String s = file.getPath();
            System.out.println(s);
            String filename = s.substring(s.lastIndexOf("\\"));
            String extension = filename.substring(filename.indexOf("."));
            fps.setString(3, extension);

            fps.setBigDecimal(4, anslagID);

            // Exekvera SQL-kommandot
            fps.executeUpdate();
            
        } catch (SQLException ex) {
            Logger.getLogger(DataAccess.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println(ex.getMessage());
        } finally { // Stänger ps och connection för att undvika memory leakage
            try {
                if (con != null) {
                    con.close();
                }
                if (fps != null) {
                    fps.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(DataAccess.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    /*
     * Hämtar fil, med anslagID, till klientens temporary files
     *
     * Returnerar fil
     */
    public File hamtaFil(int anslagID) {

        // Skapa ny fil för att skriva i
        File file = null;

        try {
            this.con = DriverManager.getConnection(connectionURL);

            // SQL-kommando getFile, deklareras globalt i klassen längre upp
            ps = con.prepareStatement(getFile);

            // Hämta fil beroende på anslagsID
            ps.setInt(1, anslagID);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                // Spara binärdata
                Blob blob = rs.getBlob(1);

                // Spara filformat
                String fileExtension = rs.getString("Filformat");
                System.out.println(fileExtension);

                try {
                    // Öppna fil
                    InputStream in = blob.getBinaryStream();

                    // Gör filen till temporär fil i användarens temporary files mapp, och döper
                    // den utefter anslagsID
                    file = File.createTempFile("GenericFile" + anslagID + "-", fileExtension,
                            new File(System.getProperty("java.io.tmpdir")));

                    // Skriv data i den nya filen utifrån binärdatan som hämtats från databasen
                    OutputStream out = new FileOutputStream(file);

                    // Hur många bits som skrivs per iteration (4kB)
                    byte[] buff = new byte[4096];
                    int len = 0;
                    while ((len = in.read(buff)) != -1) {
                        out.write(buff, 0, len);
                    }
                    // Stäng fil
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        } catch (SQLException ex) {
            Logger.getLogger(DataAccess.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println(ex.getMessage());
        } finally { // stänger ps och connection för att undvika memory leakage
            try {
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(DataAccess.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        oppnaFil(file);
        return file;
    }

    /*
     * Öppnar en fil (file) med klientens förvalda applikation
     */
    public void oppnaFil(File file) {
        Desktop desktop = Desktop.getDesktop();
        if (file.exists()) {
            try {
                desktop.open(file);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public void bjudInDeltagareTillMöte(String möte, String deltagare) throws ClassNotFoundException, SQLException {
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        this.con = DriverManager.getConnection(connectionURL);
        Statement st = con.createStatement();

        st.execute("insert into komö (möte,deltagare) values (" + möte + "," + deltagare + ")");
    }

}
