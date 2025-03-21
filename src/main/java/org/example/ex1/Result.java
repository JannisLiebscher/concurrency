package org.example.ex1;

import lombok.Data;

@Data
public class Result {

    final Param param;
    final double timeSpent;
    final double speedupCalculated;
    final double speedupMeasured;
}
