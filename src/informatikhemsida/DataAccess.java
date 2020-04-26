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
    String fileUpload = "insert into Anslag(Filnamn, Fil, Filformat) values ( ?, ?, ?)";
    String getFile = "SELECT Fil, Filformat FROM Anslag WHERE Fil IS NOT NULL AND Filformat IS NOT NULL AND AnslagID = ?";

    /*
     * 1. Skapa ett nytt DataAccess object med dina inloggningsuppgifter: DataAccess
     * dataAccessObject = new DataAccess("Milky", "milkmaster");
     * 
     * 2. Kör metoder, ex: dataAccessObject.laggUppAnslag();
     */
    public DataAccess(String user, String pass) {
        connectionURL = "jdbc:sqlserver://localhost:1433;databaseName=InfoHemsida;user=" + user + ";password=" + pass;

        try {
            this.con = DriverManager.getConnection(connectionURL);
        } catch (SQLException ex) {
            Logger.getLogger(DataAccess.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public boolean verifieraInlogg(String inMejl, String inLösenord) throws SQLException, ClassNotFoundException {
        connectionURL = "jdbc:sqlserver://localhost:1433;databaseName=Informatik;user=admin;password=team15";
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

        try {
            ps = con.prepareStatement(insertAnslag);
            ps.setString(1, body);
            ps.setInt(2, category);
            ps.executeUpdate();
            laddaUppFil(file);

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

    public void laddaUppFil(File file) {

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
            System.out.println(extension); // parameter 3 not set
            fps.setString(3, extension);

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
    
    
        
    public ArrayList <String> getInläggIKategoriFrånBloggTabell (int kolumnIndex,  String kategori) throws ClassNotFoundException, SQLException{
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        this.con = DriverManager.getConnection(connectionURL);
        Statement st = con.createStatement();
        
        ResultSet inläggIKategoriTabell = st.executeQuery("Select * from blogginlägg where blogginlägg.Kategori = "
                + "(Select KategoriID from Kategori where kategori.Kategorinamn = ' "+ kategori + "')");
        
        ArrayList<String> inläggIKolumn= new ArrayList();
        
        while (inläggIKategoriTabell.next()){
            
          inläggIKolumn.add(inläggIKategoriTabell.getString(kolumnIndex));
        }   
        
                return inläggIKolumn;
    }
    
    
        public ArrayList <String> getKolumnFrånTabell (int kolumnIndex, String tabellNamn, String sorteraEfter) throws ClassNotFoundException, SQLException{
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        this.con = DriverManager.getConnection(connectionURL);
        Statement st = con.createStatement();
        
        ResultSet bloggVärdenTabell = st.executeQuery("Select top 5 * from " + tabellNamn + " order by " + sorteraEfter +  " desc");
        
        ArrayList<String> bloggVärden = new ArrayList();
        
        while (bloggVärdenTabell.next()){
            
          bloggVärden.add(bloggVärdenTabell.getString(kolumnIndex));
        }   
        
        return bloggVärden;
    }
    
    
    
    //en version
    public ArrayList <String> getInläggIKategori (int kolumnIndex, String tabellNamn, String kategori) throws ClassNotFoundException, SQLException{
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        this.con = DriverManager.getConnection(connectionURL);
        Statement st = con.createStatement();
        
        ResultSet bloggVärdenTabell = st.executeQuery("Select * from "+ tabellNamn+ " where blogginlägg.Kategori ="
                + "(Select KategoriID from Kategori where kategori.Kategorinamn = ' "+ kategori + "')");
        
        ArrayList<String> bloggVärden = new ArrayList();
        
        while (bloggVärdenTabell.next()){
            
          bloggVärden.add(bloggVärdenTabell.getString(kolumnIndex));
        }   
        
        return bloggVärden;
    }
    
    
    
        public ArrayList <String> getKolumn (int kolumnIndex, String tabellNamn) throws ClassNotFoundException, SQLException{
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        this.con = DriverManager.getConnection(connectionURL);
        Statement st = con.createStatement();
        
        ResultSet bloggVärdenTabell = st.executeQuery("Select * from " + tabellNamn);
        
        ArrayList<String> bloggVärden = new ArrayList();
        
        while (bloggVärdenTabell.next()){
            
          bloggVärden.add(bloggVärdenTabell.getString(kolumnIndex));
        }   
        
        return bloggVärden;
    }
    
    
    public boolean verifieraVärde(ArrayList kolumn, int indexPåListan){
        
        if(kolumn.size() <= indexPåListan){
            
            return true;
        }
        
        else return false;

    }

    public void skickaKommentar (String avsändare, String kommentar, String inlägg) throws SQLException, ClassNotFoundException{
        
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        this.con = DriverManager.getConnection(connectionURL);
        Statement st = con.createStatement();
        
        st.execute("Insert into kommentarer (avsändare, kommentar, inlägg) values('" + avsändare + "','" + kommentar + "'," + inlägg + ")");

    }
    
    public String getInläggID (String inläggRubrik) throws SQLException, ClassNotFoundException{
        
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        this.con = DriverManager.getConnection(connectionURL);
        Statement st = con.createStatement();
        
        ResultSet inläggIdTabell = st.executeQuery("Select InläggID from bloggInlägg where Rubrik ='" + inläggRubrik + "'");
        
         String inläggID = null;
        while(inläggIdTabell.next()){
        
           inläggID = inläggIdTabell.getString(1);
        
        }
        
        return inläggID;

    }
    

    public void skapaKategori (String nyKategori) throws SQLException, ClassNotFoundException{
        
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        this.con = DriverManager.getConnection(connectionURL);
        Statement st = con.createStatement();
        
        st.execute("Insert into kategori (Kategorinamn) values('" + nyKategori +")");
        
    }
    
        public void taBortKategori (String enKategori) throws SQLException, ClassNotFoundException{
        
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        this.con = DriverManager.getConnection(connectionURL);
        Statement st = con.createStatement();
        
        st.execute("Delete from kategori where Kategorinamn = '" + enKategori +")");
    }
        
    public void skapaBloggInlägg(String rubrik, String inläggInnehåll, String kategori) throws ClassNotFoundException, SQLException{
        
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        this.con = DriverManager.getConnection(connectionURL);
        Statement st = con.createStatement();
        
        
        
        st.execute("insert into Blogginlägg (Rubrik, BInnehåll) Values ('" + rubrik + "','" + inläggInnehåll + "', " + kategori + ")");

    }
}