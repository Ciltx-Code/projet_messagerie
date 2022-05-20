/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projet.noswing.messagerie;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author thbogusz
 */
public class Connexion {

    private static int idConnecte;

    public Connexion() throws SQLException {
// Création de l'objet jdbc permettant d'administrer les utilisateurs
        JdbcUsers jdbc = new JdbcUsers();
        int tempId =-1;
        String[] champ = {"Identifiant", "Mot de passe"};
        ResultSet rs;
        
        while (true) {
            String[] reponse = GForm.show("CONNEXION", champ, null);

            if (reponse == null){
                continue; // on reste dans la boucle sans examiner le reste
            }
            
            rs = jdbc.getUtilisateur(reponse[0]);

            if (!rs.next()) {
                GForm.message("Identifiant inconnu...");
                continue;
            }
            if (!rs.getString("MotDePasse").equals(reponse[1])) {
                GForm.message("Mot de passe incorrect...");
                continue;
            }
            tempId = rs.getInt("id");
            break; //on sort de la boucle car tout est ok

        }

        //--- identifiant et mot de passe correct : accéder à la messagerie
        try {
            idConnecte = tempId;
            if ( rs.getString("role").equals("admin")){
            new GestionUsers(rs.getString("identifiant"));
            }else{
               new GestionMessage (rs.getString("identifiant"));

            }
        } catch (SQLException e) {
            System.err.print(e);
        }
    }

    public static int getIdConnecte(){
        return idConnecte;
    }

}
