package com.example.flappybird;

public class Player
{
    private String name;
    private int score;  // Firebase stores numbers in Long format

    public Player() {}
    public Player(String name, int score){this.name = name;this.score = score;}
    public String getName(){return name;}
    public void setName(String name) {this.name = name;}
    public int getScore() {return score;}
    public void setScore(int score) {this.score = score;}
    public String toString() {return name + " : " + score; }
}
