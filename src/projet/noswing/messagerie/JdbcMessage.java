/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projet.noswing.messagerie;

import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

/**
 *
 * @author thbogusz
 */
public class JdbcMessage {
    private Connection conn;
    public JdbcMessage() {
        try {
            // create our mysql database connection
            String myDriver = "com.mysql.cj.jdbc.Driver";
            Class.forName(myDriver);

            // déclaration URL base de données... règlage du TIMESTAMP
            String myUrl = "jdbc:mysql://localhost/bdmessage"
                    + "?useUnicode=true&useJDBCCompliantTimezoneShift=true"
                    + "&useLegacyDatetimeCode=false&serverTimezone=UTC";

            //Connection à la base bdmessage sous MYSQL
            conn = DriverManager.getConnection(myUrl, "root", "");

        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
    }

    public ResultSet getAllMessage() {
        String query = "SELECT * FROM message";
        ResultSet rs = null;
        try {
            // create the java statement
            Statement st = conn.createStatement();

            // execute the query, and get a java resultset
            rs = st.executeQuery(query);
        } catch (SQLException e) {
            System.err.print(e);
        }

        return rs;
    }

    public ResultSet getAllMessageOfCurrentUser() {
        int user = Connexion.getIdConnecte();
        String query = "SELECT * FROM message where destinataireUsers='"+user+"'";
        ResultSet rs = null;
        try {
            // create the java statement
            Statement st = conn.createStatement();

            // execute the query, and get a java resultset
            rs = st.executeQuery(query);
        } catch (SQLException e) {
            System.err.print(e);
        }

        return rs;
    }

    public ResultSet sendMessage(String dest, String objet, String message) {
        ResultSet rs = null;
        JdbcUsers jdbc = new JdbcUsers();
        int idConnecte = Connexion.getIdConnecte();
        int idDest =-1;
        rs = jdbc.getUtilisateur(dest);

        LocalDate dateActuelle = LocalDate.now();
        String sql = "INSERT into message (origineUsers,destinataireUsers,objet,message,DateEnvoi) VALUES(?,?,?,?,?)";

        try {
            if (!rs.next()) {
                GForm.message("ERREUR...");

            }
            idDest = rs.getInt("id");
            PreparedStatement prepare;
            prepare = conn.prepareStatement(sql);
            prepare.setInt(1, idConnecte);
            prepare.setInt(2, idDest);
            prepare.setString(3, objet);
            prepare.setString(4, message);
            prepare.setDate(5, java.sql.Date.valueOf(dateActuelle));
            int r = prepare.executeUpdate();
            prepare.close();
        } catch (SQLException e) {
            System.err.println(e);
        }


        return rs;
    }


    public void deleteMessageOfUser(int id) {

        String sql = "DELETE FROM message where id=?";

        try {
            conn.setAutoCommit(false);

            PreparedStatement prepare = conn.prepareStatement(sql);

            prepare.setInt(1, id);

            prepare.executeUpdate();
            prepare.close();

            conn.commit();

        } catch (SQLException e) {
            System.err.print(e);
        }
    }

    public void setLu(int id){
        String sql = "UPDATE message SET etat = 'lu' WHERE id =?";

        try{
            conn.setAutoCommit(false);

            PreparedStatement prepare = conn.prepareStatement(sql);
            prepare.setInt(1, id);
            prepare.executeUpdate();
            prepare.close();

            conn.commit();
        } catch (SQLException e) {
            System.err.print(e);
        }
    }

    public ResultSet getMail(int id){
        String sql = "SELECT * from message where id='"+id+"'";
        ResultSet rs =null;
        try {

            Statement st = conn.createStatement();

            // execute the query, and get a java resultset
            rs = st.executeQuery(sql);
        } catch (SQLException e) {
            System.err.println(e);
        }

        return rs;
    }
}
