package com.example.acpro.dotaresults.model;

public class PlayerRate {
    private String photo;
    private String nick;
    private String name;
    private String prize;
    private String gold;
    private String silver;
    private String bronze;

    public PlayerRate (){
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrize() {
        return prize;
    }

    public void setPrize(String prize) {
        this.prize = prize;
    }

    public String getGold() {
        return gold;
    }

    public void setGold(String gold) {
        this.gold = gold;
    }

    public String getSilver() {
        return silver;
    }

    public void setSilver(String silver) {
        this.silver = silver;
    }

    public String getBronze() {
        return bronze;
    }

    public void setBronze(String bronze) {
        this.bronze = bronze;
    }

    public PlayerRate (String photo, String nick, String name, String prize, String gold, String silver, String bronze){
        this.photo = photo;
        this.nick = nick;
        this.name = name;
        this.prize = prize;
        this.gold = gold;
        this.silver = silver;
        this.bronze = bronze;
    }
}
