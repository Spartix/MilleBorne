class main extends Program {

    // les voitures en String car en Char ils n existent pas
    final String[] voitures_dispo = new String[]{"🚗","🚙","🚕","🚐","🚓"};

    // noms des cartes , leur associations en valeur et leur associations en nombre;

    // METTRE EN FINAL ET METTRE EN MAJ
    NameCards[] cartes_borne = new NameCards[]{ NameCards.BORNES_50,  NameCards.BORNES_100 , NameCards.BORNES_150 , NameCards.BORNES_200 , NameCards.JOKER};
    NameCards[] malus = new NameCards[] { NameCards.CREVAISON , NameCards.FEU_ROUGE , NameCards.LIMIT_50  , NameCards.LIMIT_100 , NameCards.ACCIDENT};
    NameCards[] bonus = new NameCards[]{ NameCards.ROUES  , NameCards.FEU_VERT , NameCards.NO_LIMIT , NameCards.GARAGE};
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
        melanger(p.pioche);
    }

    void melanger(Cards[] paquet){
        // fonction qui permet de mélanger un paquet de carte donné
        for (int i = 0; i < length(paquet); i++) {
            int index_choix = (int) (random() * length(paquet));
            Cards carte_choisi = paquet[index_choix];
            paquet[index_choix] = paquet[i];
            paquet[i] = carte_choisi;
        }
    }

    void initJoueurs(Plateau plat){
        // fonction qui demande les infos de chaque joueurs
        for (int i = 1; i <= length(plat.liste_joueurs); i++) {
            print("Bonjour joueur "+ (i) + "! Veuillez entrer votre pseudo :");
            String pseudo = readString(); // ICI FAIRE UN CONTROLE DE SASIE;
            println(toString(voitures_dispo));
            int nb_Voiture = saisir("Veuillez choisir un vehicule :",1,length(voitures_dispo));
            plat.liste_joueurs[i] = newPlayers(i, pseudo , voitures_dispo[nb_Voiture-1]);
        }
    }

    void distribuerCartes(Plateau plat){
        for (int nbCartesDonne = 0; nbCartesDonne < 6; nbCartesDonne++) {
            for (int joueur_actuel = 0; joueur_actuel < length(plat.liste_joueurs); joueur_actuel++) {
                plat.liste_joueurs[joueur_actuel].jeu[nbCartesDonne] = piocher(plat);
            }
        }
    }

    Cards piocher(Plateau p){
        p.nb_cartes_pioche -= 1;
        return p.pioche[p.nb_cartes_pioche];
    }

    void testGetVoiture(){
        assertEquals("🚗 🚙 🚕 🚐 🚓 " , toString(voitures_dispo));
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

    boolean avancement(Cards carte , Players joueur){
        /*
            Fonction avancement qui regarde si un joueur peut jouer sa carte , la joue ou la défausse le cas écheant;
            carte (Cards) : Carte joué par le joueur;
            joueur (Players) : Object joueur qui joue la carte;
            return (boolean) : la carte a été jouer ou non (défaussé si non joué);
         */
        if(!estBloquer(joueur)){
            return jouerCarte(carte, joueur);
        }else{
            return contrerMalus(carte,joueur);
        }
    }

    boolean estBloquer(Players joueur){
        int i = 0;
        while (i < length(joueur.malus) && joueur.malus[i] == null) {
            i ++;
        }
        return i != length(joueur.malus);
    }



        // a faire
    boolean jouerCarte(Cards carte , Players joueur){
        if(reponseBonne(carte)){
            if(carte == Cards.BORNES_50 || carte == Cards.BORNES_50 || carte == Cards.BORNES_100 || carte == Cards.BORNES_150 || carte == Cards.BORNES_200){
                avancerDe(carte , joueur);
            }
        }
        return true;
    }
    boolean reponseBonne(Cards carte){
        //question(carte);

        return true;
    }
    boolean contrerMalus(Cards carte,Players joueur){
        return true;
    }
}
