class Players{
    /* 
    int numéro représente le numéro du joueur (useless for moment);
    String voiture est la voiture utilisé par le joueur;
    int position_Plateau représente la poisition actuel du joueur sur le plateau (1 = 1KM);
    Cards[] jeu représente la main d un joueur (les cartes qu il a actuellement dans sa main);
    nombre_cartes représente le nombre de carte total que a un joueur dans sa main (max 6) (surement ultra useless car max 6 cartes);
    */
    int numero;
    String voiture;
    int position_Plateau=0;
    Cards[] jeu;
    int nombre_cartes=0;
    String pseudo;
    int index_vide=6;
}