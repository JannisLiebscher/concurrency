package org.example.ex2.problem2;

public class Request {
    private int number;
    private String origin;

    public Request(int number, String origin) {
        this.number = number;
        this.origin = origin;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }
}
