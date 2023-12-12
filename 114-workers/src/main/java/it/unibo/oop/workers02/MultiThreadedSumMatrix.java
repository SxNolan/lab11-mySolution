package it.unibo.oop.workers02;

import java.util.ArrayList;
import java.util.List;

public class MultiThreadedSumMatrix implements SumMatrix{

    /**
     *
     */
    final int nthreads;
    public List<MatrixWorker> myWorkers = new ArrayList<>();
    /**
     * @param nthreads
     */
    MultiThreadedSumMatrix(final int nthreads) {
        this.nthreads = nthreads;
    }

    private static class MatrixWorker extends Thread {

        private final double[][] myMatrix;
        private final int startpos;
        private final int nelem;
        private double res;

        MatrixWorker(final double[][] myMatrix, final int startpos, final int nelem) {
            super();
            this.myMatrix = myMatrix;
            this.startpos = startpos;
            this.nelem = nelem;
        }

        @Override
        @SuppressWarnings("PMD.SystemPrintln")
        public void run() {
            System.out.println("Working from position " + startpos + " to position " + (startpos + nelem - 1));
            for (int i = startpos; i < nelem; i++) {
                for (double elem : myMatrix[i]) {
                    res += elem;
                }
            }
        }

        public double getResult() {
            return this.res;
        }

    }

    @Override
    public double sum(double[][] matrix) {
        int rowsPerThread = matrix.length / nthreads;
        for (int i = 0; i < nthreads; i++) {
            int startRow = i * rowsPerThread;
            int endRow = (i + 1) * rowsPerThread;
            if (i == nthreads - 1) {
                endRow = matrix.length;
            } 
            MatrixWorker myWorker = new MatrixWorker(matrix, startRow, endRow);
            myWorkers.add(myWorker);
            myWorker.start();
        }

        for (MatrixWorker elem : myWorkers) {
            try {
              elem.join();  
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        double finalSum = 0.0;
        for (MatrixWorker elem : myWorkers) {
            finalSum += elem.getResult();
        }
        return finalSum;
    }
}
