package org.example.ex5.problem3;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Arrays;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@EqualsAndHashCode(callSuper = true)
@Data
public class Cache extends ReentrantReadWriteLock {
    private int lastNumber;
    private int[] lastFactors;

    @Override
    public String toString() {
        return "Cache{" +
                "lastNumber=" + lastNumber +
                ", lastFactors=" + Arrays.toString(lastFactors) +
                '}';
    }
}
