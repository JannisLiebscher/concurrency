package org.example.ex2.problem2;

import java.util.Arrays;

public class Cache {
    private int lastNumber;
    private int[] lastFactors;

    public int getLastNumber() {
        return lastNumber;
    }

    public void setLastNumber(int lastNumber) {
        this.lastNumber = lastNumber;
    }

    public int[] getLastFactors() {
        return lastFactors;
    }

    public void setLastFactors(int[] lastFactors) {
        this.lastFactors = lastFactors;
    }

    @Override
    public String toString() {
        return "Cache: " + lastNumber + " → " + Arrays.toString(lastFactors);
    }
}
