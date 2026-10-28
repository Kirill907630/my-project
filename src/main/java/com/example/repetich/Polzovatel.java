package com.example.repetich;

public class Polzovatel {
    private int id;
    private String name;
    private String family;
    private String login;
    private String parol;

    public Polzovatel(int id, String name, String family, String login, String parol) {
        this.id = id;
        this.name = name;
        this.family = family;
        this.login = login;
        this.parol = parol;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getFamily() { return family; }
    public void setFamily(String family) { this.family = family; }

    public String getLogin() { return login; }
    public void setLogin(String login) { this.login = login; }

    public String getParol() { return parol; }
    public void setParol(String parol) { this.parol = parol; }
}