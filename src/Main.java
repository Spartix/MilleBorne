import extensions.*;
import Collections;
class Main extends Program {

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




    // int position_fleche = 0;
    // void keyTypedInConsole(char key) {
    //     //println("Vous avez appuyé sur : " + key+ " (pressez 'q' pour quitter)");
    //     switch (key) {
    //         case ANSI_UP:
    //             position_fleche += 1;
    //             printFleche(position_fleche);
    //             break;
    //         case 'q' :
    //             println("Ok, au revoir ...");
    //             break;
    //     }
        
    // }
    // void printFleche(int pos){
    //     for (int i = 0; i < pos; i++) {
    //         print(" ");
    //     }
    //     print("^");
    // }



    void algorithm() {
        //enableKeyTypedInConsole(true);
        start();
    }
    
    void start(){
        Plateau plateau = initJeu();
        int joueur_actuel = 0;
        while (true) {
            tourJoueur(plateau.liste_joueurs[joueur_actuel] , plateau);
            println("Tour du joueur "+(joueur_actuel + 1 ));
            joueur_actuel = (joueur_actuel+1) % length(plateau.liste_joueurs);
        }
    }

    void tourJoueur(Players joueur , Plateau plat){
        piocher(plat);
        println(toString(joueur.jeu));
        print("numéro de la carte a joué: ");
        int choix = readInt();
        println("la carte joué est "+joueur.jeu[choix-1].nom);
        if(jouerCarte(joueur.jeu[choix-1] , joueur)){
            println("La carte est joué avec success");
        }else{
            println("la carte a été defaussé");
        }
    }

    Plateau initJeu(){
        int nbJoueurs = saisir("Entrez un nombre de joueurs : ", 2, 4);
        Plateau plateau = newPlateau(nbJoueurs);
        initPioche(plateau);
        //println(toString(plateau.pioche));
        initJoueurs(plateau);
        distribuerCartes(plateau);
        //println(generateRoute(plateau.liste_joueurs[0]));
        println(plateau.liste_joueurs[0].pseudo+" a "+toString(plateau.liste_joueurs[0].jeu));
        piocherCarte(plateau, 0);
        println(plateau.liste_joueurs[0].pseudo+" a "+toString(plateau.liste_joueurs[0].jeu));
        return plateau;
    }

    int taillePaquet(int[] nombre){
        //pas utile
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

    void initPioche(Plateau plateau){
        // fonction pour initialiser une pioche pour le plateau p , aleatoire
        plateau.nb_cartes_pioche = nombre_cartes_bornes + (nombre_cartes_malus * 3);
        plateau.pioche = new Cards[plateau.nb_cartes_pioche];
        int i = 0;
        while (i < plateau.nb_cartes_pioche) {
            if (i < nombre_cartes_malus) {
                plateau.pioche[i] = carteAleatoire(creerPaquetNom(malus));
                i++;
                plateau.pioche[i] = carteAleatoire(creerPaquetNom(bonus));
                i++;
                plateau.pioche[i] = carteAleatoire(creerPaquetNom(bonus));
                i++;
            }else{
                plateau.pioche[i] = carteAleatoire(creerPaquetNom(cartes_borne));
                i++;
            }
        }
        melanger(plateau.pioche);
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
    Cards[] creerPaquetNom(NameCards[] noms){
        //fonction pour creer un paquet de carte Cards[] à partir d un tableau de NameCards (nom des cartes)
        Cards[] paquet = new Cards[length(noms)];
        for (int i = 0; i < length(noms); i++) {
            paquet[i] = newCards(noms[i],valeurCarte(noms[i]),estCarteBorne(noms[i]));
        }
        return paquet;
    }

    int valeurCarte(NameCards nom){
        for (int i = 0; i < length(cartes_borne); i++) {
            if(nom == cartes_borne[i]){
                return (i+1) * 50;
            }
        }
        return 0;
    }
    boolean estCarteBorne(NameCards nom){
        return valeurCarte(nom) > 0;
    }
    void initJoueurs(Plateau plat){
        // fonction qui demande les infos de chaque joueurs
        for (int i = 0; i < length(plat.liste_joueurs); i++) {
            print("Bonjour joueur "+ (i+1) + " ! Veuillez entrer votre pseudo : ");
            String pseudo = readString(); // ICI FAIRE UN CONTROLE DE SASIE;
            println("1  2  3  4  5");
            println(toString(voitures_dispo));
            int nb_Voiture = saisir("Veuillez choisir un vehicule : ",1,length(voitures_dispo));
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

    Plateau newPlateau(int  nbJoueurs){
        // fonction pour gen le plateau
            Plateau plat = new Plateau();
            plat.liste_joueurs = new Players[nbJoueurs];
            return plat;
    }

    Cards newCards(NameCards name , int value, boolean borne_carte){
        Cards carte = new Cards();
        carte.nom = name;
        carte.valeurDeDéplacement = value;
        carte.borne_carte = borne_carte;
        return carte;
    }

    Players newPlayers(int numero , String pseudo , String voiture ){
        /* Fonction de construction du joueur */
        Players joueur = new Players();
        joueur.numero = numero;
        joueur.pseudo = pseudo;
        joueur.voiture = voiture;
        joueur.malus = new Malus();
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
        int position = joueur.position_Plateau / 20;
        String msg = "   __________________________________________________ \n  |                                                  |\n";
    
        // Espaces avant la voiture
        if(position == 0){
            msg += "🚗|                                                  | \n  |__________________________________________________|";
            return msg;
        }else if(position == 1){
            msg += "  🚗                                                 | \n  |__________________________________________________|";
            return msg;
        }else{
            msg += "  |";
            for (int i = 0; i < position - 2; i++) {
                msg += " ";
            }
        
            // Ajouter la voiture
            msg += joueur.voiture;
        
            // Espaces après la voiture (ajusté pour que la voiture ne dépasse pas)
            int espace = 50 - position;
            for (int i = 0; i < espace; i++) { // Ajustement de l espace après la voiture;
                msg += " ";
            }
        
            msg += "|\n  |__________________________________________________|";
        }
        return msg;
    }
   


    Question[] initQuestions(){
        CSVFile File = loadCSV("../ressources/questionv1.csv");
        Question[] tabquestion = new Question[rowCount(File)-1];
        for (int i = 1; i < length(tabquestion) ; i++) {
            tabquestion[i] = new Question(getCell(File,i,0) , getCell(File,i,1) , 1 , getCell(File,i,2));
        }
        return tabquestion;
    }



    // Les fonctions toString des diffenrents nouveaux type

    String toString(Cards[] paquet){
        String msg = "";
        for (int i = 0; i < length(paquet); i++) {
            if(paquet[i] != null){
                msg += paquet[i].nom +" ";
            }
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
        return joueur.malus.feu || joueur.malus.crever || joueur.malus.accident;
    }


        // a faire
    boolean jouerCarte(Cards cartejoué , Players joueur){
        if(estCarteBorne(cartejoué.nom)){
        if(reponseBonne(cartejoué)){
                avancerDe(joueur , valeurCarte(cartejoué.nom));
            }
        }
        return true;
    }
    void avancerDe(Players joueur , int value){
        //fonction pour faire avancer le joueur de value KM
        joueur.position_Plateau += value;
    }
    boolean reponseBonne(Cards carte){
        //question(carte);

        return true;
    }

    String[] questionReponse(int niveau , String sujet){
        return new String[5];
    }

    boolean contrerMalus(Cards carte,Players joueur){
        return true;
    }

    void piocherCarte(Plateau p, int idx){
        p.liste_joueurs[idx].jeu[p.liste_joueurs[idx].index_vide] = piocher(p);
    }
}
