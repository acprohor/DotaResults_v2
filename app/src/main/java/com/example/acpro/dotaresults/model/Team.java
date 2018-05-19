package com.example.acpro.dotaresults.model;

public class Team {
    private String name;
    private String prize;
    private String gold;
    private String silver;
    private String bronze;
    private String logo;

    public Team (){
    }

    public Team (String name, String prize, String gold, String silver, String bronze, String logo){
        this.name = name;
        this.prize = prize;
        this.gold = gold;
        this.silver = silver;
        this.bronze = bronze;
        this.logo = logo;
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

    public String getLogo(){
        return logo;
    }

    public void setLogo(String logo){
        this.logo = logo;
    }
}
