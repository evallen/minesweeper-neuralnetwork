package controller;

import org.jetbrains.annotations.NotNull;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Arrays;
import java.util.Random;

public class Mine
{

    int x;
    int y;
    private final int sideLength = 5;
    private int scoreIndex;

    public Mine(int x, int y) throws IllegalArgumentException
    {
        if (x < Parameters.BORDER_PADDING || x >= Parameters.BORDER_PADDING + Parameters.WIDTH
                || y < Parameters.BORDER_PADDING || y >= Parameters.BORDER_PADDING + Parameters.HEIGHT)
        {
            throw new IllegalArgumentException("Initial coordinates of mine are out of bounds! ( " + x + ", " + y + ")");
        }

        this.x = x;
        this.y = y;
        this.scoreIndex = getRandomScoreIndex();
    }

    void paintMine(@NotNull Graphics g)
    {
        Graphics2D g2d = (Graphics2D) g.create();

        Color color = getScoreColor();
        g2d.setColor(color);

        g2d.translate(x, y);
        g2d.drawRect(-(sideLength / 2), -(sideLength / 2), sideLength, sideLength);

        g2d.dispose();
    }

    private Color getScoreColor()
    {
        return Parameters.MINE_TYPES.get(scoreIndex).color;

    }

    /**
     * This method outputs a random index, representing a MineType.
     * This works because the MineTypes are stored in a List (Parameters.MINE_TYPES).
     * The output of this function can then be used to retrieve further data about that MineType (for example,
     * if this function sets the Mine's scoreIndex as 0, the Mine's MineType will be set to the first MineType
     * in the list.
     * @return The index of this Mine's MineType in the Parameters.MINE_TYPES list.
     */
    private int getRandomScoreIndex()
    {
        double[] cumulativeScoreProbs = new double[Parameters.MINE_TYPES.size()];
        cumulativeScoreProbs[0] = Parameters.MINE_TYPES.get(0).probability;
        for (int i = 1; i < cumulativeScoreProbs.length; i++)
        {
            cumulativeScoreProbs[i] = cumulativeScoreProbs[i - 1] + Parameters.MINE_TYPES.get(i).probability;
        }
        Random rng = new Random();

        double randomNum = rng.nextDouble() * cumulativeScoreProbs[cumulativeScoreProbs.length - 1];

        int index = Arrays.binarySearch(cumulativeScoreProbs, randomNum);

        if (index < 0)
        {
            index = Math.abs(index + 1);
        }

        return index;
    }

    double getScore()
    {
        return Parameters.MINE_TYPES.get(scoreIndex).score;
    }
}
