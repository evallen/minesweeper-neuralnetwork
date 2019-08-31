package tests;

import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import neuralnetwork.IllegalParameterException;
import neuralnetwork.Neuron;
import org.junit.Before;
import org.junit.Test;

import neuralnetwork.NeuralNet;

public class NeuralNetTest
{

    private List<Double> weights = new ArrayList<>();

    @Before
    public void setUp()
    {
        weights.add(0.0);
        weights.add(-.3);
        weights.add(.7);

        weights.add(0.0);
        weights.add(3.3);
        weights.add(-2.4);

        weights.add(0.0);
        weights.add(.4);
        weights.add(-.7);

        weights.add(1.0);
        weights.add(1.3);
        weights.add(-1.9);
    }

    @Test
    public void testNeuralNetIntIntIntIntArray()
    {
        fail("Not yet implemented");
    }

    @Test
    public void testNeuralNetIntIntIntIntArrayListOfDouble() throws IllegalParameterException
    {
        ArrayList<Integer> neuronsPerLayer = new ArrayList<>()
        {{
            add(2);
            add(2);
        }};

        ArrayList<Neuron.ActivationType> activationTypes = new ArrayList<>()
        {{
           add(Neuron.ActivationType.ACT_RELU);
           add(Neuron.ActivationType.ACT_SIGMOID);
        }};
        NeuralNet net = new NeuralNet(2, 2, neuronsPerLayer, activationTypes, weights);
        for (int i = 0; i < net.layers.size(); i++)
        {
            System.out.println("Layer " + i);
            for (int j = 0; j < net.layers.get(i).neurons.size(); j++)
            {
                System.out.println("\tNeuron " + j);
                for (int k = 0; k < net.layers.get(i).neurons.get(j).weights.size(); k++)
                {
                    System.out.println("\t\t" + net.layers.get(i).neurons.get(j).weights.get(k));
                }
            }
        }

        List<Double> inputs = new ArrayList<>();
        inputs.add(3.2);
        inputs.add(2.4);
        System.out.println(net.run(inputs));
    }

}
