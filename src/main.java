class main extends Program {

    // les voitures en String car en Char ils n existent pas
    final String[] voitures_dispo = new String[]{"🚗","🚙","🚕","🚐","🚓"};

    // noms des cartes , leur associations en valeur et leur associations en nombre;
    Cards[] cartes_borne = new Cards[]{ Cards.BORNES_50,  Cards.BORNES_100 , Cards.BORNES_150 , Cards.BORNES_200 , Cards.JOKER};
    Cards[] malus = new Cards[] { Cards.CREVAISON , Cards.FEU_ROUGE , Cards.LIMIT_50  , Cards.LIMIT_100 , Cards.ACCIDENT};
    Cards[] bonus = new Cards[]{ Cards.ROUES  , Cards.FEU_VERT , Cards.NO_LIMIT , Cards.GARAGE};
    // nb de cartes
    final int nombre_cartes_bornes = 60;
    final int nombre_cartes_malus = 30;


    void algorithm() {
        start();
    }
    
    void start(){
        int nbJoueurs = saisir("Entrez un nombre de joueurs : ", 2, 4);
        Plateau plateau = newPlateau(100,nbJoueurs);
        initPioche(plateau);
        println(toString(plateau.pioche));
        //initJoueurs(plateau);
    }


    int taillePaquet(int[] nombre){
        int total = 0;
        for (int i = 0; i < length(nombre); i++) {
            total += nombre[i];
        }
        return total;
    }


    Cards carteAleatoire(Cards[] paquet){
        // donne une carte aleatoire parmis une liste de carte (Cards)
        return paquet[(int) (random() * length(paquet))];
    }


    //initialisations 

    void initPioche(Plateau p){
        // fonction pour initialiser une pioche pour le plateau p , aleatoire
        p.nb_cartes_pioche = nombre_cartes_bornes + (nombre_cartes_malus * 2);
        p.pioche = new Cards[p.nb_cartes_pioche];
        int i = 0;
        while (i < p.nb_cartes_pioche) {
            if (i < nombre_cartes_malus) {
                p.pioche[i] = carteAleatoire(malus);
                i++;
                p.pioche[i] = carteAleatoire(bonus);
                i++;
            }else{
                p.pioche[i] = carteAleatoire(cartes_borne);
                i++;
            }
        }
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
                print("Nombre entré invalide (doit être entre " + min + " et " + max+")");
                saisie = readInt();
            }
            return saisie;
    }

    void testGenerateRoute(){
        Players p = newPlayers(1,"J1","🚗");
        p.position_Plateau = 1000;
        assertEquals(" __________________________________________________\n|                                                  |\n|                                                🚗|\n|__________________________________________________|",generateRoute(p));
        Players p2 = newPlayers(1,"J2","🚙");
        p2.position_Plateau = 0;
        assertEquals(" __________________________________________________\n|                                                  |\n|🚙                                                |\n|__________________________________________________|",generateRoute(p2));
        Players p3 = newPlayers(1,"J3","🚐");
        p3.position_Plateau = 485;
        assertEquals(" __________________________________________________\n|                                                  |\n|                      🚐                          |\n|__________________________________________________|",generateRoute(p3));
    }


    String generateRoute(Players joueur) {
    /*
     Fonction generateRoute qui genere la route d un joueur ainsi que sa position.
     joueur (Players) : objet Joueur.
     return (String): La route avec la voiture à la bonne position;
    */
        String msg = " ";
        int position = joueur.position_Plateau / 20; // Conversion en "blocs" de 10 km;
        int voitureWidth = 2;
    
        // Ligne du haut
        for (int i = 0; i < 50; i++) {
            msg += "_";
        }
        msg += "\n|";
    
        // Ligne vide
        for (int i = 0; i < 50; i++) {
            msg += " ";
        }
        msg += "|\n|";
    
        // Espaces avant la voiture
        for (int i = 0; i < position - voitureWidth; i++) {
            msg += " ";
        }
    
        // Ajouter la voiture
        msg += joueur.voiture;
    
        // Espaces après la voiture (ajusté pour que la voiture ne dépasse pas)
        int espace = 50 - position;
        if(position == 0){
            espace = 48;
        }
        for (int i = 0; i < espace; i++) { // Ajustement de l espace après la voiture;
            msg += " ";
        }
    
        msg += "|\n|";
    
        // Ligne du bas
        for (int i = 0; i < 50; i++) {
            msg += "_";
        }
        msg += "|";
    
        return msg;
    }


    // Les fonctions toString des diffenrents nouveaux type

    String toString(Cards[] paquet){
        String msg = "";
        for (int i = 0; i < length(paquet); i++) {
            msg += paquet[i] +" ";
        }
        return msg;
    }
    String toString(String[] voitures){
        /* function qui retourne la liste des voiture en string;*/
        String msg = "";
        for (int i = 0; i < length(voitures); i++) {
            msg = msg + voitures[i] + " ";
        }
        return msg;
    }
    // String toString(Cards[] paquet){
    //     String msg = "";
    //     for (int i = 0; i < length(paquet); i++) {
    //         msg += paquet[i].name + " " + paquet[i].value +"\n";
    //     }
    //     return msg;
    // }
}
