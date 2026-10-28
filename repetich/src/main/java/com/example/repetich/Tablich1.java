package com.example.repetich;

public class Tablich1 {
    private int id;
    private String name;
    private String chen;   // VARCHAR в БД

    public Tablich1(int id, String name, String chen) {
        this.id = id;
        this.name = name;
        this.chen = chen;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getChen() { return chen; }
    public void setChen(String chen) { this.chen = chen; }

    // Вспомогательный метод для числовой сортировки (можно использовать в comparator)
    public double getChenAsDouble() {
        try { return Double.parseDouble(chen); }
        catch (Exception e) { return 0; }
    }
}