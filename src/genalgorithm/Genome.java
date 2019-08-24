package genalgorithm;

import java.util.List;

public class Genome
{
    public List<Double> weights;
    double fitness = 0;

    public Genome(List<Double> weights)
    {
        this.weights = weights;
    }

    public int compareTo(Genome g2)
    {
        return Double.compare(fitness, g2.fitness);
    }
}
