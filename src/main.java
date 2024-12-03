class main extends Program {

    // les voitures en String car en Char ils n existent pas
    final String[] voitures_dispo = new String[]{"🚗","🚙","🚕","🚐","🚓"};

    // noms des cartes , leur associations en valeur et leur associations en nombre;
    final String[] cards_name = new String[]{"+50 KM", "+100 KM"};
    final int[] cards_value = new int[]{50,100};
    final int[] cards_nb = new int[]{90,10};


    void algorithm() {
        Cards[] pioche = initPaquet(cards_name,cards_value,cards_nb);
        println(toString(pioche));
    }

    
    void start(){
        int nbJoueurs = saisir("Entrez un nombre de joueurs : ", 1, 4);
        Plateau plateau = newPlateau(100,nbJoueurs);
        initJoueurs(plateau);
    }

    int taillePaquet(int[] nombre){
        int total = 0;
        for (int i = 0; i < length(nombre); i++) {
            total += nombre[i];
        }
        return total;
    }

    Cards[] initPaquet(String name[] , int[] value , int[] nombre){
        // fonction pour initialiser une pioche 
        Cards[] paquet  = new Cards[taillePaquet(nombre)];
        int positionDansLePaquet = 0;
        for (int typeDeCarte = 0; typeDeCarte < length(nombre); typeDeCarte++) {
            for (int nbDeCarte = 0; nbDeCarte < nombre[typeDeCarte] ; nbDeCarte++) {
                paquet[positionDansLePaquet] = newCards(name[typeDeCarte],value[typeDeCarte]);
                positionDansLePaquet += 1;
            }
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
    /*
     Fonction generateRoute qui genere la route d un joueur ainsi que sa position.
     joueur (Players) : objet Joueur.
     return (String): La route avec la voiture à la bonne position;
    */
    String generateRoute(Players joueur) {
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
    
    String toString(Cards[] paquet){
        String msg = "";
        for (int i = 0; i < length(paquet); i++) {
            msg += paquet[i].name + " " + paquet[i].value +"\n";
        }
        return msg;
    }
}
