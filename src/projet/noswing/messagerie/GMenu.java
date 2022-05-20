/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projet.noswing.messagerie;

import plum.console.Clavier;

/**
 *
 * @author thbogusz
 */
public class GMenu {

    public static int show(String leTitre, String[] menu) {
        String espace;
        int rep;
         int i;
        do {
            String titre = "MESSAGERIE -> " + leTitre;
            titre = Color.ANSI_BLACK_BACKGROUND + " "
                    + Color.ANSI_BLACK_BACKGROUND + Color.ANSI_WHITE
                    + titre
                    + new String(new char[40 - titre.length()]).replace("\0", " ")
                    + Color.ANSI_BLACK_BACKGROUND + " ";
            System.out.println(titre);

            
            for (i = 0; i < menu.length; i++) {
                String item = " " + (i + 1) + " - " + menu[i];
                espace = new String(new char[40 - item.length()]).replace("\0", " ");
                item += espace;
                System.out.println(Color.ANSI_BLACK_BACKGROUND + " "
                        + Color.ANSI_BLACK_BACKGROUND + Color.ANSI_WHITE + item
                        + Color.ANSI_BLACK_BACKGROUND + " ");
            }
            
            String fermetureMenu = Color.ANSI_BLACK_BACKGROUND + " "
                    + Color.ANSI_WHITE + Color.ANSI_BLACK_BACKGROUND
                    + new String(new char[40]).replace("\0", " ")
                    + Color.ANSI_BLACK_BACKGROUND + " ";
            System.out.println(fermetureMenu);

            rep = Clavier.lireInt(Color.ANSI_BLACK_BACKGROUND + Color.ANSI_WHITE
                    + "Votre Choix (Quitter = 0) ? ");

        } while (rep < 0 | rep > i);

        return rep;
    }

}
