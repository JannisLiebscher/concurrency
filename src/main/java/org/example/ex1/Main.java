package org.example.ex1;

public class Main {

    private static final int DEFAULT_TIME = 10;
    private static final int MILLIS = 1000;
    private static final Param[] PARAMS = {
            new Param(0.5, 2),
            new Param(0.5, 4),
            new Param(0.5, 8),
            new Param(0.5, 16),
            new Param(0.5, 32),
            new Param(0.05, 8),
            new Param(0.05, 1024),
            new Param(0.1, 8),
            new Param(0.1, 100),
            new Param(0.25, 8),
            new Param(0.25, 64),
            new Param(0.99, 8),
            new Param(0.99, 1000)
    };
    private static final Result[] RESULTS = new Result[PARAMS.length];

    public static void main(String[] args) throws InterruptedException {

          // Für Manuelle Tests
//        Scanner scanner = new Scanner(System.in);
//
//        System.out.print("Geben Sie den seriellen Anteil F (0 bis 1) ein: ");
//        double F = scanner.nextDouble();
//
//        System.out.print("Geben Sie die Anzahl der Prozessoren P ein: ");
//        int P = scanner.nextInt();
//
//        scanner.close();

        for (int i = 0; i < PARAMS.length; i++) {
            double F = PARAMS[i].getF();
            int P = PARAMS[i].getP();

            double speedupCalculated = 1 / (F + (1 - F) / P);


            System.out.println("Parameter: F = " + F + ", P = " + P);
            System.out.println("Starte serialisierbaren Teil...");
            long start = System.currentTimeMillis();

            Thread.sleep((int) Math.round(F * DEFAULT_TIME * MILLIS));

            System.out.println("Starte parallelisierbaren Teil...");

            Thread.sleep((int) Math.round(((1 - F) * DEFAULT_TIME * MILLIS) / P));

            long stop = System.currentTimeMillis();

            double timeSpent = (double) (stop - start) / (double) MILLIS;
            double speedupMeasured = (double) DEFAULT_TIME / timeSpent;
            System.out.println("Aufgabe abgeschlossen");

            Result result = new Result(PARAMS[i], timeSpent, speedupCalculated, speedupMeasured);
            RESULTS[i] = result;
        }
        System.out.println();
        System.out.println("----- Ergebnisse -----\n");
        for (Result result : RESULTS) {
            System.out.println("Serial Portion F: " + result.param.getF());
            System.out.println("Cores P: " + result.param.getP());
            System.out.println("Time spent: " + result.getTimeSpent() + "S");
            System.out.println("Speed-Up Calculated: " + result.getSpeedupCalculated());
            System.out.println("Speed-Up Measured: " + result.getSpeedupMeasured());
            System.out.println();
        }


    }
}