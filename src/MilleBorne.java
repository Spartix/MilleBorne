import extensions.*;  
class MilleBorne extends Program {

    // les voitures en String car en Char ils n existent pas
    final String[] voitures_dispo = new String[]{"🚗","🚙","🚕","🚐","🚓"};
    // Delay d affichage de chaque carac
    final int DELAY = 25;
    // noms des cartes , leur associations en valeur et leur associations en nombre;
    final NameCards[] CARTES_BORNE = new NameCards[]{ NameCards.BORNES_50,  NameCards.BORNES_100 , NameCards.BORNES_150 , NameCards.BORNES_200};
    final NameCards[] MALUS = new NameCards[] { NameCards.CREVAISON , NameCards.FEU_ROUGE , NameCards.LIMIT_50  , NameCards.LIMIT_100 , NameCards.ACCIDENT};
    final NameCards[] BONUS = new NameCards[]{ NameCards.ROUES  , NameCards.FEU_VERT , NameCards.NO_LIMIT , NameCards.GARAGE};
    // nb de cartes
    final int nombre_CARTES_BORNEs = 35;
    final int nombre_cartes_malus = 100;


    void algorithm() {
        //enableKeyTypedInConsole(true);
        clearScreen();
        //welcome();
        start();
    }
    
    void start(){
        Plateau plateau = initJeu();
        int joueur_actuel = 0;
        while (!partieFinie(plateau)) {
            clearScreen();
            print(toString(plateau));
            println("Tour du joueur numéro "+(joueur_actuel + 1 )+ ' '+ plateau.liste_joueurs[joueur_actuel].pseudo +"\n");
            println("Les malus du joueur sont : "+toString(plateau.liste_joueurs[joueur_actuel].malus)+"\n");
            tourJoueur(plateau.liste_joueurs[joueur_actuel] , plateau);
            //delay(3000);
            joueur_actuel = (joueur_actuel+1) % length(plateau.liste_joueurs);
        }
    }


    void tourJoueur(Players joueur , Plateau plat){
        joueur.jeu[joueur.index_vide] =  piocher(plat);
        println(toString(joueur.jeu));
        //delay(3000);
        int choix = saisir("numéro de la carte a joué: ",1,7) -1;
        //delay(3000);
        //println("Le choix est "+ choix);
        //delay(3000);
        println("la carte joué est "+joueur.jeu[choix].nom);
        //delay(3000);
        if(jouerCarte(joueur.jeu[choix] , joueur , plat)){
            println("La carte est joué avec success");
        }else{
            println("la carte a été defaussé");
        }
        // vider la case de la carte
        joueur.index_vide = choix;
        joueur.jeu[choix] = null;
    }

    Plateau initJeu(){
        int nbJoueurs = saisir("Entrez un nombre de joueurs : ", 2, 4);
        Plateau plateau = newPlateau(nbJoueurs);
        initPioche(plateau);
        initQuestions(plateau);
        //println(toString(plateau.questions));
        initJoueurs(plateau);
        distribuerCartes(plateau);
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
        plateau.nb_cartes_pioche = nombre_CARTES_BORNEs + (nombre_cartes_malus * 3);
        plateau.pioche = new Cards[plateau.nb_cartes_pioche];
        int i = 0;
        while (i < plateau.nb_cartes_pioche) {
            if (i < nombre_cartes_malus) {
                plateau.pioche[i] = carteAleatoire(creerPaquetNom(MALUS));
                i++;
                plateau.pioche[i] = carteAleatoire(creerPaquetNom(BONUS));
                i++;
                plateau.pioche[i] = carteAleatoire(creerPaquetNom(BONUS));
                i++;
            }else{
                plateau.pioche[i] = carteAleatoire(creerPaquetNom(CARTES_BORNE));
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
            paquet[i] = newCards(noms[i],valeurCarte(noms[i]),estCarteBorne(noms[i]),valeurDifficulte(noms[i]));
        }
        return paquet;
    }

    int valeurCarte(NameCards nom){
        for (int i = 0; i < length(CARTES_BORNE); i++) {
            if(nom == CARTES_BORNE[i]){
                return (i+1) * 50;
            }
        }
        return 0;
    }
    int valeurDifficulte(NameCards nom){
        for (int i = 0; i < length(CARTES_BORNE); i++) {
            if(nom == CARTES_BORNE[i]){
                return i+1;
            }
        }
        return 0;
    }
    boolean estCarteBorne(NameCards nom){
        return valeurCarte(nom) > 0;
    }
    boolean isBorne(Cards carte){
        return carte.borne_carte;
    }
    void initJoueurs(Plateau plat){
        // fonction qui demande les infos de chaque joueurs
        for (int i = 0; i < length(plat.liste_joueurs); i++) {
            clearScreen();
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

    Cards newCards(NameCards name , int value, boolean borne_carte, int difficulte){
        Cards carte = new Cards();
        carte.nom = name;
        carte.valeurDeDéplacement = value;
        carte.borne_carte = borne_carte;
        carte.difficulte = difficulte;
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
            String saisie = readString();
            while (length(saisie) <= 0 || !onlyNumbers(saisie) || stringToInt(saisie) < min || stringToInt(saisie) > max){
                print("Nombre entré invalide (doit être entre " + min + " et " + max+")");
                saisie = readString();
            }
            return stringToInt(saisie);
    }

    boolean onlyNumbers(String msg){
        int i = 0;
        while ( i < length(msg) && charAt(msg,i) >= '0' && charAt(msg,i) <= '9') {
            i++;
        }
        return i == length(msg);
    }

    void testGenerateRoute(){
        Players p = newPlayers(1,"J1","🚗");
        p.position_Plateau = 1000;
        assertEquals("   __________________________________________________\n  |                                                  |\n  |                                                🚗|\n  |__________________________________________________|",generateRoute(p));
        Players p2 = newPlayers(1,"J2","🚙");
        p2.position_Plateau = 0;
        assertEquals("   __________________________________________________\n  |                                                  |\n  |🚙                                                |\n  |__________________________________________________|",generateRoute(p2));
        Players p3 = newPlayers(1,"J3","🚐");
        p3.position_Plateau = 485;
        assertEquals("   __________________________________________________\n  |                                                  |\n  |                      🚐                          |\n  |__________________________________________________|",generateRoute(p3));
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
            msg += joueur.voiture +"|                                                  | \n  |__________________________________________________|";
            return msg;
        }else if(position == 1){
            msg += "  " + joueur.voiture +"                                                 | \n  |__________________________________________________|";
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
   
    Question newQuestion(String question , String reponse , int niveau , String sujet){
        Question quest = new Question();
        quest.question = question;
        quest.reponse = reponse;
        quest.niveau = niveau;
        quest.sujet = sujet;
        return quest;
    }

    void initQuestions(Plateau P){
        //saveCSV( new String[][]{{"OUOU"},{"AA"}} , "./ressources/caca.csv");
        CSVFile File = loadCSV("./ressources/questionv1.csv");
        Question[] tabquestion = new Question[rowCount(File) -1];
        println("Il y a "+length(tabquestion));
        for (int i = 0; i < length(tabquestion) ; i++) {
            tabquestion[i] = newQuestion(getCell(File,i+1,0) , getCell(File,i+1,1) , 1 , getCell(File,i+1,2));
            println(getCell(File,i+1,0));
        }
        P.questions = tabquestion;

    }



    // Les fonctions toString des diffenrents nouveaux type

    String toString(Cards[] paquet){
        String[][] msg = new String[7][8]; // 7 cartes qui prennent 7 lignes;
        //String msg2 = "";
        for (int i = 0; i < length(paquet); i++) {
            if(paquet[i] != null){
                //msg[i] = stringToArray(toString(paquet[i],i+1));
                msg[i] = stringToArray(toString(paquet[i],i+1));
            }
        }
        return toString(msg);
    }
    void testStringToArray(){
        String[][] tab1 = new String[][]{{"1erl","2emel"},{"2emecarte","2emecarte"}};
        String[][] tab2 = new String[][]{stringToArray("1erl\n2emel"),stringToArray("2emecarte\n2emecarte")};
        assertArrayEquals(tab1[1],tab2[1]);
    }
    String toString(String[][] tab_double) {
        String msg = "";
        for (int u = 0; u < length(tab_double[0]); u++) {
            for (int i = 0; i < length(tab_double); i++) { 
                msg += tab_double[i][u] + " "; 
            }
            msg += "\n";
        }
        return msg;
    }

    void testToString(){

    }

    String[] stringToArray(String msg){
        //fonction qui transforme un de string en un tab de String. Chaque entrée est une ligne de la chaine
        int nb_retour = 0;
        String[] tab = new String[length(msg)];
        for (int i = 0; i < length(msg); i++) {
            if(charAt(msg,i) == '\n'){
                nb_retour += 1;
            }else{
                if (tab[nb_retour] == null) {
                    tab[nb_retour] = charAt(msg,i) +"";
                }else{
                    tab[nb_retour] = tab[nb_retour] + charAt(msg,i);
                }
                
            }
        }
        return slice(tab);
    }
    void testSlice(){
        String[] tab = new String[]{"Bjr","Arv","Bg",null,null,null};
        tab = slice(tab);
        assertArrayEquals(tab, new String[]{"Bjr","Arv","Bg"});
    }
    String[] slice(String[] tableau){
        /* Fonction qui renvoi un tableau sans les valeur null en fin de tab;
            (String[]) tableau: tableau à decouper;
            (int) start: debut du decoupage;
            (int) stop: fin du decoupage (exclu);
        */
        int nb = 0;
        for (int i = 0; i < length(tableau); i++) {
            if (tableau[i] == null){
                nb ++;
            }
        }
        String[] tab = new String[length(tableau)-nb];
        for (int i = 0; i < length(tableau)-nb; i++) {
            tab[i] = tableau[i];
        }
        return tab;
    }




    String toString(Cards carte, int numero){
        String msg = "+-------------------+\n";
        if(isBorne(carte)){
            msg = msg + "|      Borne        |\n";
        }else if(estUneCarteBonus(carte)){
            msg = msg + "|      Bonus        |\n";
        }else {
           msg = msg + "|      Malus        |\n";
        }
        msg = msg + "|    +---------+    |\n|    |   n°"+numero+"   |    |\n|    +---------+    |\n|       Carte       |\n|";
        int space = 19 - length(carte.nom +"");
        for (int i = 0; i <= space; i++) {
            if (space/2 == i) {
                msg += carte.nom;
            }else {
                msg += " ";
            }
        }
        return msg + "|\n+-------------------+";
    }


    String toString(String[] voitures){
        /* function qui retourne la liste des voiture en string*/
        String msg = "";
        for (int i = 0; i < length(voitures); i++) {
            msg = msg + voitures[i] + " ";
        }
        return msg;
    }

    String toString(Plateau plat){
        String msg = "";
        for (int i = 0; i < length(plat.liste_joueurs); i++) {
            msg +=  generateRoute(plat.liste_joueurs[i]) + "\n";
        }
        return msg;
    }

    String toString(Question[] quest){
        String msg = "";
        for (int i = 0; i < length(quest); i++) {
            msg += quest[i].question + "\n";
        }
        return msg;
    }

    String toString(Malus malus){
        String msg = "";
        boolean feu=true;
        boolean crever=false;
        boolean accident=false;
        int limit=200;
        if (malus.feu) {
            msg += "Feu Rouge, ";
        }
        if (malus.crever) {
            msg += "Roue Crevée, ";
        }
        if (malus.accident) {
            msg += "Accident, ";
        }
        msg += "Limité à "+ malus.limit + "KM";
        return msg ;
    }


    boolean estBloquer(Players joueur){
        return joueur.malus.feu || joueur.malus.crever || joueur.malus.accident;
    }


    boolean jouerCarte(Cards cartejoué , Players joueur , Plateau plat){

        if(estCarteBorne(cartejoué.nom)){
            if(!estBloquer(joueur) && !estLimite(joueur,cartejoué)){
                if(reponseBonne(cartejoué , plat)){
                        avancerDe(joueur , valeurCarte(cartejoué.nom));
                }else{
                    println("Réponse est fausse");
                    return false;
                }
            }else{
                return false;
            }
        }else{
            jouerCarteSpecial(cartejoué, joueur , plat);
        }
        return true;
    }
    void avancerDe(Players joueur , int value){
        //fonction pour faire avancer le joueur de value KM
        joueur.position_Plateau += value;
    }
    boolean reponseBonne(Cards carte , Plateau plat){
        Question question = plat.questions[(int)(random() * length(plat.questions))];
        println("La diffculté pour la carte "+carte.nom +" est de "+ carte.difficulte);
        println(question.question);
        String input = removeUnChar(toLowerCase(readString()));
        return equals(input,question.reponse);
    }
    boolean estLimite(Players joueur , Cards carte){
        return carte.valeurDeDéplacement > joueur.malus.limit;
    }

    String removeUnChar(String msg){
        String rep = "";
        for (int i = 0; i < length(msg); i++) {
            if ((charAt(msg,i) >= 'a' && charAt(msg,i) <= 'z') || (charAt(msg,i) >= '0' && charAt(msg,i) <= '9')) {
                rep += charAt(msg,i);
            }
        }
        return rep;
    }

    void jouerCarteSpecial(Cards carte , Players joueur , Plateau plat){
        if (estUneCarteBonus(carte)) {
            if (carte.nom == NameCards.ROUES) {
                joueur.malus.crever = false;
            }
            else if (carte.nom == NameCards.FEU_VERT) {
                joueur.malus.feu = false;
            }
            else if (carte.nom == NameCards.NO_LIMIT) {
                joueur.malus.limit = 200;
            }
            else if (carte.nom == NameCards.GARAGE) {
                joueur.malus.accident = false;
            }
        }else{
            int choix = saisir("Quel joueur veut tu ciblé avec ta carte malus ?\n >",1,length(plat.liste_joueurs));
            if (carte.nom == NameCards.CREVAISON) {
                plat.liste_joueurs[choix-1].malus.crever = true;
            }
            else if (carte.nom == NameCards.FEU_ROUGE) {
                plat.liste_joueurs[choix-1].malus.feu = true;
            }
            else if (carte.nom == NameCards.LIMIT_50) {
                plat.liste_joueurs[choix-1].malus.limit = 50;
            }
            else if (carte.nom == NameCards.LIMIT_100) {
                plat.liste_joueurs[choix-1].malus.limit = 100;
            }
            else if (carte.nom == NameCards.ACCIDENT) {
                plat.liste_joueurs[choix-1].malus.accident = true;
            }else{
                println("La carte est pas trouvé "+carte.nom);
            }
        }
    }

    boolean estUneCarteBonus(Cards carte){
        int i = 0;
        while (i < length(BONUS) && BONUS[i] != carte.nom) {
            i ++;
        }
        return i != length(BONUS);
    }

    String[] questionReponse(int niveau , String sujet){
        return new String[5];
    }

    boolean partieFinie(Plateau p){
        int i = 0;
        while (i < length(p.liste_joueurs) && p.liste_joueurs[i].position_Plateau <= 1000) {
            i++;
        }
        return i != length(p.liste_joueurs);
    }
    void welcome() {
        delayPrint("Bienvenue dans le jeu du Mille Bornes !\n",DELAY);
        delayPrint("C'est un jeu de cartes où le but est de parcourir 1000 kilomètres en premier 🚗.\n",DELAY);
        delayPrint("Vous pouvez utiliser des malus pour mettre des bâtons dans les roues de vos adversaires 🚧, comme des crevaisons ou des accidents.\n",DELAY);
        delayPrint("Les bonus vous permettent de contrer les malus que vous avez subis 💪.\n",DELAY);
        delayPrint("Attention, il y a aussi une limite de vitesse qui peut être imposée et qui restreint votre progression 🚦.\n",DELAY);
        delayPrint("Planifiez bien vos coups et que le meilleur gagne ! 🎉\n",DELAY);
    }
    void delayPrint(String msg, int delay){
        for (int i = 0; i < length(msg); i++) {
            print(charAt(msg,i));
            delay(delay);
        }
    }
}