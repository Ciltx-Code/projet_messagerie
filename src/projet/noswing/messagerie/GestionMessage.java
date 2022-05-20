/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projet.noswing.messagerie;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 *
 * @author thbogusz
 */
public class GestionMessage {

    public GestionMessage(String identifiant) throws SQLException {
        // Création de l'objet jdbc permettant d'administrer les utilisateurs
        JdbcMessage jdbc = new JdbcMessage();
        JdbcUsers jdbc2 = new JdbcUsers();

        String titre = "MESSAGES";

        String[] menu = {"Voir les messages", "Envoyer un message",
            "Supprimer un message", "Répondre à un message"};

        String[] reponse;
        ResultSet rs;
        int rep;
        do {
            rep = GMenu.show(titre, menu);

            switch (rep) {
                case 1:
                    int c = 0;
                    rs = jdbc.getAllMessageOfCurrentUser();
                    //System.out.println(rs);
                    while(rs.next()){
                        int id = -1;
                        String isLu = rs.getString("etat");
                        if(isLu.equals("non lu")){
                            System.out.println("---------------------");
                            c++;
                            id = rs.getInt("origineUsers");
                            Date date = rs.getDate("dateEnvoi");
                            String objet = rs.getString("objet");
                            String message = rs.getString("message");

                            Format format = new SimpleDateFormat("dd/MM/yyyy");
                            String _identifiant = jdbc2.getIdentifiantById(id);
                            System.out.println("De : "+_identifiant + ", le : "+format.format(date));
                            System.out.println("Objet : "+objet);
                            System.out.println("Message : "+message);
                            jdbc.setLu(rs.getInt("id"));
                        }


                    }
                    if(c==0){
                        System.out.println("Aucun nouveau message");
                    }else{
                        System.out.println("---------------------");
                    }
                    break;

                case 2:
                    while (true) {
                        String[] champ = {"Destinataire", "Objet", "Message"};
                        reponse = GForm.show("ENVOYER UN MESSAGE", champ, null);
                        rs = jdbc2.getUtilisateur(reponse[0]);
                        try {
                            if (reponse == null){
                                continue; // on reste dans la boucle sans examiner le reste
                            }

                            if (!rs.next()) {
                                GForm.message("Identifiant inconnu...");
                                continue;
                            }
                        } catch (SQLException e) {
                            System.out.println(e);
                        }
                        break;
                    }

                    jdbc.sendMessage(reponse[0], reponse[1], reponse[2]);
                    break;

                case 3:
                    ArrayList<Integer> liste= new ArrayList<Integer>();

                    rs = jdbc.getAllMessageOfCurrentUser();
                    while(rs.next()){
                        int id = rs.getInt("origineUsers");
                        Date date = rs.getDate("dateEnvoi");
                        String objet = rs.getString("objet");
                        String message = rs.getString("message");

                        Format format = new SimpleDateFormat("dd/MM/yyyy");
                        String _identifiant = jdbc2.getIdentifiantById(id);
                        int idMessage = rs.getInt("id");
                        System.out.println("Message n°" + idMessage +" : " + "De "+ _identifiant + " le "+format.format(date) + "; objet : " + objet + "; message : " + message);

                        liste.add(idMessage);
                    }
                    System.out.println( "0 ---> Annuler");
                    while (true) {
                        String[] champ = {"Numéro de message"};
                        reponse = GForm.show("SUPPRIMER UN MESSAGE", champ, null);
                        rs = jdbc2.getUtilisateur(reponse[0]);
                        try {
                            if (reponse == null){
                                continue; // on reste dans la boucle sans examiner le reste
                            }

                            if (!rs.next()) {
                                //GForm.message("Numéro inconnu...");
                                //continue;
                            }
                            if(Integer.parseInt(reponse[0]) == 0){
                                break;
                            }
                            if(liste.contains(Integer.parseInt(reponse[0]))){
                                jdbc.deleteMessageOfUser(Integer.parseInt(reponse[0]));
                            }else{
                                System.out.println("Vous ne pouvez pas supprimer un message qui ne vous est pas destiné ou qui n'existe pas.");
                            }

                        } catch (SQLException e) {
                            System.out.println(e);
                        }

                        break;
                    }
                    break;

                case 4:
                    int idMessage = -1;
                    ArrayList<Integer> liste1= new ArrayList<Integer>();

                    rs = jdbc.getAllMessageOfCurrentUser();
                    while(rs.next()){
                        int id = rs.getInt("origineUsers");
                        Date date = rs.getDate("dateEnvoi");
                        String objet = rs.getString("objet");
                        String message = rs.getString("message");

                        Format format = new SimpleDateFormat("dd/MM/yyyy");
                        String _identifiant = jdbc2.getIdentifiantById(id);
                        idMessage = rs.getInt("id");
                        System.out.println("Message n°" + idMessage +" : " + "De "+ _identifiant + " le "+format.format(date) + "; objet : " + objet + "; message : " + message);

                        liste1.add(idMessage);
                    }
                    System.out.println( "0 ---> Annuler");

                    while (true) {
                        String[] champ = {"Numéro de message", "Message"};
                        reponse = GForm.show("RÉPONDRE À UN MESSAGE", champ, null);
                        rs = jdbc.getMail(idMessage);
                        try {
                            if (reponse == null){
                                continue; // on reste dans la boucle sans examiner le reste
                            }

                            if (!rs.next()) {
                                GForm.message("Numéro inconnu...");
                                continue;
                            }
                            if(Integer.parseInt(reponse[0]) == 0){
                                break;
                            }
                            if(liste1.contains(Integer.parseInt(reponse[0]))){
                                jdbc.sendMessage(jdbc2.getIdentifiantById(rs.getInt("origineUsers")), rs.getString("objet"), reponse[1]);
                            }else{
                                System.out.println("Vous ne pouvez pas répondre à un message qui n'est pas dans la liste.");
                                break;
                            }


                        } catch (SQLException e) {
                            System.out.println(e);
                        }

                        break;
                    }
                    break;

            }
        } while (rep != 0);

    }
}
