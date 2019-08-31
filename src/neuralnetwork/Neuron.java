package neuralnetwork;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Neuron
{

    private int numInputs;
    public List<Double> weights = new ArrayList<>(); // First weight is bias

    public enum ActivationType
    {
        ACT_SIGMOID,
        ACT_RELU,
        ACT_UNDEF
    }

    private ActivationType activationType;

    private static final double sigmoidModifier = 1; // Usually stays one, can change shape of sigmoid

    Neuron(int numInputs, ActivationType actType)
    {
        this.numInputs = numInputs;
        // Makes one extra weight for bias
        for (int i = 0; i < numInputs + 1; i++)
        {
            weights.add(getRandomClampedWeight());
        }

        this.activationType = actType;
    }

    Neuron(List<Double> weights, ActivationType actType)
    {
        this.numInputs = weights.size();
        this.weights = weights;
        this.activationType = actType;
    }

    private double getRandomClampedWeight()
    {
        Random rand = new Random();
        return rand.nextDouble() * 2 - 1;
    }

    double run(List<Double> inputs)
    {
        if (inputs.size() + 1 != numInputs)
            throw new IllegalArgumentException("Input list must match numInputs. (inputs.size() + 1 -> " + (inputs.size() + 1) + " and numInputs -> " + numInputs);

        return activationFunction(getWeightedSum(inputs), activationType);
    }

    /**
     * The neuron's activation function.
     * @param weightedSum The weighted sum of all of the neuron's inputs (for each input, add (input * weight)).
     * @param activationType What kind of activation function the neuron uses.
     * @return The output of the neuron.
     */
    private double activationFunction(double weightedSum, ActivationType activationType)
    {
        // Sigmoid
        if (activationType == ActivationType.ACT_SIGMOID)
            return (1 / (1 + Math.exp(-weightedSum / Neuron.sigmoidModifier)));

        // ReLU
        else if (activationType == ActivationType.ACT_RELU)
        {
            if (weightedSum < 0)
                return 0;
            else
                return weightedSum;
        }
        else throw new IllegalArgumentException("Wrong ActivationType!");
    }

    private double getWeightedSum(List<Double> inputs)
    {
        double sum = -weights.get(0); // Add bias
        for (int i = 1; i < numInputs; i++)
        {
            sum += weights.get(i) * inputs.get(i - 1);
        }
        return sum;
    }
}
