class Question{
    String question;
    String reponse;
    int niveau;
    String sujet;

    Question(String question , String reponse , int niveau , String sujet){
        this.question = question;
        this.reponse = reponse;
        this.niveau = niveau;
        this.sujet = sujet;
        return;
    }
    String getQuestion(){
        return this.question;
    }
    String getReponse(){
        return this.reponse;
    }
    int getNiveau(){
        return niveau;
    }
    String getSujet(){
        return this.sujet;
    }
    boolean isNiveau(int value){
        return value == niveau;
    }
    boolean goodAnswer(String message){
        return this.reponse == message;
    }
}