package com.example.acpro.dotaresults.model;

public class Player {

    private String photo;
    private String nickname;
    private String info;
    private String role;
    private String winRate;

    public Player (){
    }

    public Player (String photo, String nickname, String info, String role, String winRate){
        this.photo = photo;
        this.nickname = nickname;
        this.info = info;
        this.role = role;
        this.winRate = winRate;
    }

    public String getPhoto() {
        return photo;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getWinRate() {
        return winRate;
    }

    public void setWinRate(String winRate) {
        this.winRate = winRate;
    }
}
