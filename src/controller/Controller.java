package controller;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.KeyStroke;
import javax.xml.parsers.ParserConfigurationException;

import chart.ChartFrame;
import genalgorithm.GeneticAlgorithm;
import neuralnetwork.IllegalParameterException;
import org.xml.sax.SAXException;

public class Controller
{

    private static File XMLFile = new File("src\\controller\\params.xml");
    static GeneticAlgorithm ga = new GeneticAlgorithm();

    public static MineSweeper[] sweepers;
    static Mine[] mines;

    private static double deltaTime = 0; // in seconds

    private static BoardPanel panel;

    private static int totalTicks = 0;
    private static int epochTicks = 0;

    public static ChartFrame cFrame;

    public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException, IllegalParameterException
    {

        Parameters.initializeParameters(XMLFile);

        sweepers = new MineSweeper[Parameters.POPULATION_SIZE];
        mines = new Mine[Parameters.NUM_MINES];

        cFrame = new ChartFrame("Chart");
        cFrame.pack();
        cFrame.setVisible(true);

        JFrame frame = new JFrame("Mine Sweeper Neural Network");
        panel = new BoardPanel();

        panel.addMouseListener(panel);
        panel.getInputMap().put(KeyStroke.getKeyStroke('j'), "slow");
        panel.getInputMap().put(KeyStroke.getKeyStroke('k'), "fast");
        panel.getInputMap().put(KeyStroke.getKeyStroke('n'), "smallSlow");
        panel.getInputMap().put(KeyStroke.getKeyStroke('m'), "smallFast");
        panel.getInputMap().put(KeyStroke.getKeyStroke('f'), "paintToggle");
        panel.getActionMap().put("slow", new AbstractAction()
        {

            /**
             *
             */
            private static final long serialVersionUID = 1L;

            @Override
            public void actionPerformed(ActionEvent e)
            {
                Parameters.TICK_MULTIPLIER -= 20;
                Parameters.TICK_MULTIPLIER = Math.max(1.0, Parameters.TICK_MULTIPLIER);
            }

        });
        panel.getActionMap().put("fast", new AbstractAction()
        {

            /**
             *
             */
            private static final long serialVersionUID = 1L;

            @Override
            public void actionPerformed(ActionEvent e)
            {
                Parameters.TICK_MULTIPLIER += 20;

            }

        });
        panel.getActionMap().put("smallSlow", new AbstractAction()
        {

            /**
             *
             */
            private static final long serialVersionUID = 1L;

            @Override
            public void actionPerformed(ActionEvent e)
            {
                Parameters.TICK_MULTIPLIER -= 0.1;
                Parameters.TICK_MULTIPLIER = Math.max(Parameters.TICK_MULTIPLIER, 0.1);
            }

        });
        panel.getActionMap().put("smallFast", new AbstractAction()
        {

            /**
             *
             */
            private static final long serialVersionUID = 1L;

            @Override
            public void actionPerformed(ActionEvent e)
            {
                Parameters.TICK_MULTIPLIER += 0.1;
            }
        });
        panel.getActionMap().put("paintToggle", new AbstractAction()
        {

            /**
             *
             */
            private static final long serialVersionUID = 1L;

            @Override
            public void actionPerformed(ActionEvent e)
            {
                panel.painting = !panel.painting;
            }
        });
        BorderLayout layout = new BorderLayout();

        frame.getContentPane().setLayout(layout);
        frame.getContentPane().add(panel, BorderLayout.CENTER);
        frame.pack();

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //frame.setSize(WIDTH, HEIGHT);

        frame.setFocusable(true);

        frame.setVisible(true);

        panel.requestFocus();

        ga.start();

    }

    public static void startLoop()
    {
        epochTicks = 0;
        Random rand = new Random();
        clearAndCreateMines(rand);
        runGameLoop();

        for (MineSweeper sweeper : sweepers)
        {
            sweeper.epochEnd();
        }
    }

    private static void runGameLoop()
    {
        // in milliseconds
        double lastUpdateTime = System.currentTimeMillis();
        while (true)
        {
            deltaTime = System.currentTimeMillis() - lastUpdateTime;

            if (deltaTime < Parameters.MS_BETWEEN_TICKS / Parameters.TICK_MULTIPLIER)
                continue;

            lastUpdateTime = System.currentTimeMillis();
            tick();
            totalTicks++;
            epochTicks++;

            if (epochTicks >= Parameters.GENERATION_TIME)
            {
                return;
            }
        }
    }

    private static void tick()
    {
        for (MineSweeper sweeper : sweepers)
        {
            sweeper.doTurn();
        }
        checkMineCollisions();
        if (panel.painting)
        {
            panel.repaint();
        }
    }

    public static double getDeltaTimeSec()
    {
        return (deltaTime * Parameters.TICK_MULTIPLIER) / 1000;
    }

    public static double getDeltaTimeMs()
    {
        return deltaTime * Parameters.TICK_MULTIPLIER;
    }

    /**
     * Checks to see if any mine sweepers have hit a mine - if they have, increment the minesweeper's
     * mine counter, delete the mine, and put a new one somewhere.
     */
    private static void checkMineCollisions()
    {
        for (int i = 0; i < mines.length; i++)
        {
            MineSweeper closestSweeper = null;
            double closestDistance = Double.POSITIVE_INFINITY;
            for (MineSweeper sweeper : sweepers)
            {
                double distance = getDistance(sweeper.x, sweeper.y, mines[i].x, mines[i].y);
                if (distance <= MineSweeper.collectDistance && distance < closestDistance)
                {
                    closestDistance = distance;
                    closestSweeper = sweeper;
                }
            }
            if (closestSweeper != null)
            {
                closestSweeper.collectedMine(mines[i]);
                Random rand = new Random();
                int x = rand.nextInt(Parameters.WIDTH) + Parameters.BORDER_PADDING;
                int y = rand.nextInt(Parameters.HEIGHT) + Parameters.BORDER_PADDING;
                try
                {
                    mines[i] = new Mine(x, y);
                } catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

    static double getDistance(double x1, double y1, double x2, double y2)
    {
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }

    private static void clearAndCreateMines(Random rand)
    {
        for (int i = 0; i < mines.length; i++)
        {
            int x = rand.nextInt(Parameters.WIDTH) + Parameters.BORDER_PADDING;
            int y = rand.nextInt(Parameters.HEIGHT) + Parameters.BORDER_PADDING;
            try
            {
                mines[i] = new Mine(x, y);
            } catch (IllegalArgumentException e)
            {
                e.printStackTrace();
            }
        }
    }

}
