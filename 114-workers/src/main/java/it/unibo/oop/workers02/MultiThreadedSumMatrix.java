package it.unibo.oop.workers02;

import java.util.List;

public class MultiThreadedSumMatrix implements SumMatrix{

    /**
     *
     */
    final int nthreads;

    /**
     * @param nthreads
     */
    MultiThreadedSumMatrix(final int nthreads) {
        this.nthreads = nthreads;
    }

    private static class MatrixWorker extends Thread {

        private final List<Integer> list;
        private final int startpos;
        private final int nelem;
        private long res;

        MatrixWorker(final List<Integer> list, final int startpos, final int nelem) {
            super();
            this.list = list;
            this.startpos = startpos;
            this.nelem = nelem;
        }

        @Override
        @SuppressWarnings("PMD.SystemPrintln")
        public void run() {
            System.out.println("Working from position " + startpos + " to position " + (startpos + nelem - 1));
            for (int i = startpos; i < list.size() && i < startpos + nelem; i++) {
                this.res += this.list.get(i);
            }
        }

    }

    @Override
    public double sum(double[][] matrix) {
        
    }
    
}
