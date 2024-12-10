class Players{
    /* 
    int numéro représente le numéro du joueur (useless for moment);
    String voiture est la voiture utilisé par le joueur;
    int position_Plateau représente la poisition actuel du joueur sur le plateau (1 = 1KM);
    Cards[] jeu représente la main d un joueur (les cartes qu il a actuellement dans sa main);
    nombre_cartes représente le nombre de carte total que a un joueur dans sa main (max 7) (surement ultra useless car max 7 cartes);
    String pseudo représente le pseudo du joueur choisi au début du jeu;
    index_vide représente l index du tableau jeu où le joueur n a pas de carte (positionnement de la derniere carte joué);
    Malus malus représente les malus que le joueur subis ou non;
    */

    int numero;
    String voiture;
    int position_Plateau=0;
    Cards[] jeu = new Cards[7];
    int nombre_cartes=6;
    String pseudo;
    int index_vide=6;
    Malus malus;
}