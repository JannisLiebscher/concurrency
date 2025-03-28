package org.example.ex2.problem2;

public class Response {
    private int[] factors;
    private String destination;

    public Response(int[] factors, String destination) {
        this.factors = factors;
        this.destination = destination;
    }

    public int[] getFactors() {
        return factors;
    }

    public void setFactors(int[] factors) {
        this.factors = factors;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }
}
