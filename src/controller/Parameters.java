package controller;

import neuralnetwork.IllegalParameterException;
import neuralnetwork.Neuron;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Parameters
{

    // Size stuff
    public static int WIDTH; // Size of the board
    public static int HEIGHT;
    public static int BORDER_PADDING;

    public static int PANEL_PREFERRED_WIDTH; // Size of actual window
    public static int PANEL_PREFERRED_HEIGHT;

    // Timing stuff
    public static double TICKS_PER_SECOND;
    public static double MS_BETWEEN_TICKS;
    public static double TICK_MULTIPLIER;

    // Mine stuff
    public static int NUM_MINES;

    // Genetic algorithm stuff
    public static int POPULATION_SIZE; // how many genomes are in the population
    public static int NUM_ELITISM;
    public static int GENERATION_TIME; // length of generation in ticks
    public static double MUTATION_RATE;
    public static double CROSSOVER_RATE;
    public static double MUTATION_MULTIPLIER; // how big the mutation can be - value of 1.0 symbolizes that weight can move max of 1.0 either direction
    public static int NUM_WEIGHTS_PER_GENOME;
    public static double ROULETTE_MODIFIER; // how much the top fitnesses are valued over lower ones.

    // MineSweeper stuff
    public static int NUM_MINES_VISIBLE; // how many mines the MineSweepers can see - MUST BE LESS THAN NUM_MINES
    public static double MIN_SPEED;
    static int WHEEL_WIDTH;
    static int BODY_WIDTH;
    static int BODY_LENGTH;
    static int WHEEL_LENGTH;
    static int STEERING_MULTIPLIER;
    static int MAX_TURN_SPEED;

    // Neural network stuff
    public static int NUM_LAYERS; // How many layers we have that actually use neurons - essentially everything except the input.
    public static int NUM_INPUTS; // There is no `NUM_OUTPUTS` - instead, the final layer is considered the output.
    public static List<Integer> NUM_NEURONS_PER_LAYER; // One for each hidden layer & the output layer.
    public static List<Neuron.ActivationType> ACTIVATION_TYPE_LIST; // One for each hidden layer & the output layer.

    // Chart stuff
    public static int CHARTPANEL_WIDTH;
    public static int CHARTPANEL_HEIGHT;

    // Logging stuff
    public static File FITTEST_GENOMES_LOG;

    // Mine stuff
    public static List<MineType> MINE_TYPES;

    public static void initializeParameters(File XMLFile) throws ParserConfigurationException, SAXException
    {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser parser = factory.newSAXParser();

        DefaultHandler handler = new DefaultHandler()
        {
            public void startDocument()
            {
                NUM_NEURONS_PER_LAYER = new ArrayList<>();
                ACTIVATION_TYPE_LIST = new ArrayList<>();
                MINE_TYPES = new ArrayList<>();
            }

            public void startElement(String uri, String localName, String qName,
                                     Attributes attributes)
            {
                if (qName.equals("jFrame"))
                {
                    WIDTH = Integer.parseInt(attributes.getValue("width"));
                    System.out.println("Width: " + WIDTH);
                    HEIGHT = Integer.parseInt(attributes.getValue("height"));
                }
                else if (qName.equals("boardPanel"))
                {
                    BORDER_PADDING = Integer.parseInt(attributes.getValue("borderPadding"));
                    System.out.println("Height: " + HEIGHT);
                }
                else if (qName.equals("geneticAlgorithm"))
                {
                    POPULATION_SIZE = Integer.parseInt(attributes.getValue("populationSize"));
                    NUM_ELITISM = Integer.parseInt(attributes.getValue("numElitism"));
                    GENERATION_TIME = Integer.parseInt(attributes.getValue("generationTime"));
                    MUTATION_RATE = Double.parseDouble(attributes.getValue("mutationRate"));
                    CROSSOVER_RATE = Double.parseDouble(attributes.getValue("crossoverRate"));
                    MUTATION_MULTIPLIER = Double.parseDouble(attributes.getValue("mutationMultiplier"));
                    ROULETTE_MODIFIER = Double.parseDouble(attributes.getValue("rouletteModifier"));
                }
                else if (qName.equals("neuralNetwork"))
                {
                    NUM_INPUTS = Integer.parseInt(attributes.getValue("numInputs"));
                }
                else if (qName.equals("layer"))
                {
                    int numNeurons = Integer.parseInt(attributes.getValue("numNeurons"));
                    NUM_NEURONS_PER_LAYER.add(numNeurons);

                    String activationTypeString = attributes.getValue("activationType");
                    System.out.println("Activation type: " + activationTypeString);
                    Neuron.ActivationType activationType;
                    switch (activationTypeString)
                    {
                        case "relu":
                            activationType = Neuron.ActivationType.ACT_RELU;
                            break;
                        case "sigmoid":
                            activationType = Neuron.ActivationType.ACT_SIGMOID;
                            break;
                        default:
                            activationType = Neuron.ActivationType.ACT_UNDEF;
                    }
                    ACTIVATION_TYPE_LIST.add(activationType);
                }
                else if (qName.equals("mineSweeper"))
                {
                    MIN_SPEED = Double.parseDouble(attributes.getValue("minSpeed"));
                    NUM_MINES_VISIBLE = Integer.parseInt(attributes.getValue("numMinesVisible"));
                    BODY_LENGTH = Integer.parseInt(attributes.getValue("bodyLength"));
                    BODY_WIDTH = Integer.parseInt(attributes.getValue("bodyWidth"));
                    WHEEL_LENGTH = Integer.parseInt(attributes.getValue("wheelLength"));
                    WHEEL_WIDTH = Integer.parseInt(attributes.getValue("wheelWidth"));
                    STEERING_MULTIPLIER = Integer.parseInt(attributes.getValue("steeringMultiplier"));
                    MAX_TURN_SPEED = Integer.parseInt(attributes.getValue("maxTurnSpeed"));
                }
                else if (qName.equals("chart"))
                {
                    CHARTPANEL_WIDTH = Integer.parseInt(attributes.getValue("chartPanelWidth"));
                    CHARTPANEL_HEIGHT = Integer.parseInt(attributes.getValue("chartPanelHeight"));
                }
                else if (qName.equals("logging"))
                {
                    FITTEST_GENOMES_LOG = new File(attributes.getValue("fittestGenomeLogFile"));
                }
                else if (qName.equals("mines"))
                {
                    NUM_MINES = Integer.parseInt(attributes.getValue("numMines"));
                }
                else if (qName.equals("mineType"))
                {
                    MineType newMineType = new MineType(
                            Double.parseDouble(attributes.getValue("score")),
                            Double.parseDouble(attributes.getValue("probability")),
                            new Color(
                                    Integer.parseInt(attributes.getValue("color"), 16)
                            )
                    );

                    MINE_TYPES.add(newMineType);
                }
                else if (qName.equals("ticks"))
                {
                    TICKS_PER_SECOND = Double.parseDouble(attributes.getValue("ticksPerSecond"));
                    MS_BETWEEN_TICKS = (1 / TICKS_PER_SECOND) * 1000;
                    TICK_MULTIPLIER = Double.parseDouble(attributes.getValue("tickMultiplier"));
                }
            }

            public void endDocument()
            {
                NUM_LAYERS = NUM_NEURONS_PER_LAYER.size();
                NUM_WEIGHTS_PER_GENOME = getNumWeightsPerGenome();
                PANEL_PREFERRED_WIDTH = WIDTH + (2 * BORDER_PADDING);
                PANEL_PREFERRED_HEIGHT = HEIGHT + (2 * BORDER_PADDING);
            }

            private int getNumWeightsPerGenome()
            {
                int numWeights = 0;

                // Adds 1 weight to each layer (except the final one) to include bias.
                numWeights += (NUM_INPUTS + 1) * NUM_NEURONS_PER_LAYER.get(0);

                for (int i = 1; i < NUM_LAYERS; i++)
                {
                    numWeights += (NUM_NEURONS_PER_LAYER.get(i - 1) + 1) *
                            NUM_NEURONS_PER_LAYER.get(i);
                }

                return numWeights;
            }
        };

        try
        {
            parser.parse(XMLFile, handler);
        } catch (IOException e)
        {
            System.out.println();
            System.out.println("#####################################################################################");
            System.out.println("ERROR: Params.xml file must be present in the directory of the JARfile.");
            System.out.println("For a sample params.xml file, please see https://github.com/Bungee54/minesweeper-neuralnetwork/");
            System.out.println("#####################################################################################");
            System.out.println();
            e.printStackTrace();
            System.exit(1);
        }
    }
}
