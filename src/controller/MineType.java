package controller;

import java.awt.*;

public class MineType
{
    public double score; // How much the mine is worth when picked up.
    public double probability; // Chance that any given mine is of this type.
    // Must be between 0.0 and 1.0; together, all
    // MineInfo probabilities must add to 1.0.
    public Color color; // Color of the mine as displayed on screen.

    public MineType(double score, double probability, Color color)
    {
        this.score = score;
        this.probability = probability;
        this.color = color;
    }
}
