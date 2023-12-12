package it.unibo.oop.reactivegui03;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.TimeUnit;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * Third experiment with reactive gui.
 */
@SuppressWarnings("PMD.AvoidPrintStackTrace")
public final class AnotherConcurrentGUI extends JFrame {

    private static final double WIDTH_PERC = 0.2;
    private static final double HEIGHT_PERC = 0.1;
    boolean myflag = true;
    long startTime;
    final JLabel display = new JLabel();
    JButton up = new JButton("up");
    JButton down = new JButton("down");
    JButton stop = new JButton("stop");
    Agent myagent = new Agent();

    public AnotherConcurrentGUI() {
        super();
        final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setSize((int) (screenSize.getWidth() * WIDTH_PERC), (int) (screenSize.getHeight() * HEIGHT_PERC));
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        JPanel panel = new JPanel(new FlowLayout());
        this.getContentPane().add(panel);
        panel.add(display);
        up.addActionListener(e -> myflag = true);
        panel.add(up);
        panel.add(down);
        down.addActionListener(e -> myflag = false);
        panel.add(stop);

        new Thread(myagent).start();
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    Thread.sleep(TimeUnit.SECONDS.toMillis(10));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                AnotherConcurrentGUI.this.stopTheCount();
            }
             
        }).start();
        stop.addActionListener(e -> myagent.stopCounting());
        this.setVisible(true);
    }

    private void stopTheCount() {
        myagent.stopCounting();
    }
    


    private class Agent implements Runnable {

        private volatile boolean stop;
        private int counter;

        @Override
        public void run() {
            while (!this.stop) {
                try {
                    // The EDT doesn't access `counter` anymore, it doesn't need to be volatile 
                    final var nextText = Integer.toString(this.counter);
                    SwingUtilities.invokeAndWait(() -> AnotherConcurrentGUI.this.display.setText(nextText));
                    if (myflag) {
                        this.counter++;
                    } else {
                        this.counter--;
                    }
                    Thread.sleep(100);
                    
                } catch (InvocationTargetException | InterruptedException ex) {
                    /*
                     * This is just a stack trace print, in a real program there
                     * should be some logging and decent error reporting
                     */
                    ex.printStackTrace();
                }
            }
        }

        public void stopCounting() {
            this.stop = true;
        }

    }

}
