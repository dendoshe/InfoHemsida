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
    String getFile = "SELECT Fil, Filformat FROM Anslag WHERE Fil IS NOT NULL AND Filformat IS NOT NULL";

    private static final int BUFFER_SIZE = 4096; // 4KB

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
        connectionURL = "jdbc:sqlserver://localhost:53158;databaseName=Informatik;user=admin;password=team15";
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

    public void hamtaFil(int AnslagID) {
        try {
            this.con = DriverManager.getConnection(connectionURL);
            ps = con.prepareStatement(getFile);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Blob blob = rs.getBlob(1);
                String fileExtension = rs.getString("Filformat");
                File file;
                try {
                    InputStream in = blob.getBinaryStream();
                    file = File.createTempFile("GenericFile-", fileExtension,
                            new File(System.getProperty("java.io.tmpdir")));
                    OutputStream out = new FileOutputStream(file);
                    byte[] buff = new byte[4096]; // how much of the blob to read/write at a time
                    int len = 0;

                    while ((len = in.read(buff)) != -1) {
                        out.write(buff, 0, len);
                    }

                    out.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            int[] pngSignature = { 137, 80, 78, 71, 13, 10, 26, 10 };

            // Path path =
            // Paths.get("%USERPROFILE%/AppData/Local/Temp/Skolapp/GenericFile");

            // Files.createDirectories(path.getParent());

            // try {
            // Files.createFile(path);
            // } catch (FileAlreadyExistsException e) {
            // System.err.println("already exists: " + e.getMessage());
            // }

            // try (InputStream inputStream = new
            // FileInputStream("%USERPROFILE%/AppData/Local/Temp/GenericFile");
            // OutputStream outputStream = new FileOutputStream(outputFile);) {

            // byte[] buffer = new byte[BUFFER_SIZE];
            // byte[] contentInBytes = inputFile.getBytes();

            // while (inputStream.read(buffer) != -1) {
            // outputStream.write(buffer);
            // }

            // outputStream.flush();
            // outputStream.close();

            // } catch (IOException ex) {
            // ex.printStackTrace();
            // }

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

    public void bjudInDeltagareTillMöte(String möte, String deltagare) throws ClassNotFoundException, SQLException {
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        this.con = DriverManager.getConnection(connectionURL);
        Statement st = con.createStatement();

        st.execute("insert into komö (möte,deltagare) values (" + möte + "," + deltagare + ")");
    }

}
