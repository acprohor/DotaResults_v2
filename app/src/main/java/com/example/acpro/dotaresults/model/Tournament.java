package com.example.acpro.dotaresults.model;

public class Tournament {
    private String championship;
    private String date;
    private String prize;

    public Tournament () {
    }

    public Tournament (String championship, String date, String prize){
        this.championship = championship;
        this.date = date;
        this.prize = prize;
    }

    public String getChampionship() {
        return championship;
    }

    public void setChampionship(String championship) {
        this.championship = championship;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPrize() {
        return prize;
    }

    public void setPrize(String prize) {
        this.prize = prize;
    }
}
