package com.example.acpro.dotaresults.model;

public class TeamRate {
    private String teamName;
    private String matches;
    private String wins;
    private String draw;
    private String lose;
    private String points;

    public TeamRate (){
    }

    public TeamRate(String teamName, String matches, String wins, String draw, String lose, String points){
        this.teamName = teamName;
        this.matches = matches;
        this.wins = wins;
        this.draw = draw;
        this.lose = lose;
        this.points = points;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getMatches() {
        return matches;
    }

    public void setMatches(String matches) {
        this.matches = matches;
    }

    public String getWins() {
        return wins;
    }

    public void setWins(String wins) {
        this.wins = wins;
    }

    public String getDraw() {
        return draw;
    }

    public void setDraw(String draw) {
        this.draw = draw;
    }

    public String getLose() {
        return lose;
    }

    public void setLose(String lose) {
        this.lose = lose;
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }
}
