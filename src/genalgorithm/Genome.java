package genalgorithm;

import java.util.List;

class Genome
{
    List<Double> weights;
    double fitness = 0;

    Genome(List<Double> weights)
    {
        this.weights = weights;
    }

    int compareTo(Genome g2)
    {
        return Double.compare(fitness, g2.fitness);
    }
}
