class main extends Program {
    // les voitures en String car en Char ils n existent pas
    final String[] voitures_dispo = new String[]{"🚗","🚙","🚕","🚐","🚓"};
    // taille de la pioche au début du jeu;
    final int taille_pioche = 100;
    // noms des cartes , leur associations en valeur et leur associations en nombre;
    final String[] cards_name = new String[]{"+50 KM", "+100 KM"};
    final int[] cards_value = new int[]{50,100};
    final int[] cards_nb = new int[]{90,10};
    void algorithm() {
        // players P = new players();
        //println("🚗");
        Players p = newPlayers(1,"TEST","🚗");
        p.position_Plateau = 100;
        println(generateRoute(p));
    }
    void start(){
        int nbJoueurs = saisir("Entrez un nome de joueurs :",2,5);
        Plateau plateau = newPlateau(100,nbJoueurs);
        initJoueurs(plateau);
    }

    Cards[] initPaquet(int total, String name[] , int[] value){
        // fonction pour initialiser une pioche 
        Cards[] paquet  = new Cards[total];
        for (int i = 0; i < total; i++) {
            paquet[i] = newCards(cards_name[i],cards_value[i]);
        }
        return paquet;
    }
    Cards newCards(String name, int value){
        Cards carte = new Cards();
        carte.name = name;
        carte.value = value;
        return carte;
    }
    void initJoueurs(Plateau plat){
        // fonction qui demande les infos de chaque joueurs
        for (int i = 1; i <= length(plat.liste_joueurs); i++) {
            print("Bonjour joueur "+ (i) + "! Veuillez entrer votre pseudo :");
            String pseudo = readString(); // ICI FAIRE UN CONTROLE DE SASIE;
            println(getVoiture());
            int nb_Voiture = saisir("Veuillez choisir un vehicule :",1,length(voitures_dispo));
            plat.liste_joueurs[i] = newPlayers(i, pseudo , voitures_dispo[nb_Voiture-1]);
        }
    }

    String getVoiture(){
        /* function qui retourne la liste des voiture en string;*/
        String msg = "";
        for (int i = 0; i < length(voitures_dispo); i++) {
            msg = msg + voitures_dispo[i] + " ";
        }
        return msg;
    }
    void testGetVoiture(){
        assertEquals("🚗 🚙 🚕 🚐 🚓 " , getVoiture());
    }

    Plateau newPlateau(int nbCartes ,int  nbJoueurs){
        // fonction pour gen le plateau
            Plateau plat = new Plateau();
            plat.pioche = new Cards[nbCartes];
            plat.liste_joueurs = new Players[nbJoueurs];
            return plat;
    }


    Players newPlayers(int numero , String pseudo , String voiture ){
        /* Fonction de construction du joueur */
        Players joueur = new Players();
        joueur.numero = numero;
        joueur.pseudo = pseudo;
        joueur.voiture = voiture;
        return joueur;
    }
    int saisir(String message ,int min , int max){
        /*
        fonction de controle de saisie;
        message (String): Message à demandé à l'utilisateur;
        min (int): Nombre minimum que l'utilisateur doit entré (inclus);
        max (int): Nombre maximum que l'utilisateur peut entré (inclus);
        return (int) : nombre saisie par l'utilisateur;
        */
            print(message);
            int saisie = readInt();
            while (saisie < min || saisie > max) {
                print("Nombre entré invalide (doit être entre" + min + "et" + max+")");
                saisie = readInt();
            }
            return saisie;
    }
    String generateRoute( Players joueur){
        String msg = " ";
        int position = joueur.position_Plateau;
        // les tiret en haut
        for (int i = 0; i < 100; i++) {
            msg = msg + "_";
        }
        msg = msg + " \n|" ;
        // la ligne de la voiture
        for (int i = 0; i < 100; i++) {
            msg = msg + " ";
        }
        msg += "|\n|";
        for (int i = 0; i < 100 - position; i++) {
            msg = msg + " ";
        }
        msg = msg + joueur.voiture;
        for (int i = 0; i < position; i++) {
            msg = msg + " ";
        }
        msg = msg + "|\n|";
        for (int i = 0; i < 100; i++) {
            msg = msg + "_";
        };
        msg = msg + "|";
        return msg;
    }
}