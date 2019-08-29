package controller;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import neuralnetwork.NeuralNet;

public class MineSweeper
{

    double x, y;
    private double heading;
    double nearestMine;
    int debugPaints = 0;
    public int score = 0;
    static double collectDistance = 5;

    private NeuralNet net;

    public MineSweeper(double x, double y, double heading, NeuralNet net) throws Exception
    {
        if (x < Parameters.BORDER_PADDING || x >= Parameters.BORDER_PADDING + Parameters.WIDTH
                || y < Parameters.BORDER_PADDING || y >= Parameters.BORDER_PADDING + Parameters.HEIGHT)
        {
            throw new Exception("Initial coordinates of minesweeper are out of bounds! (" + x +
                                ", " + y + "); (" + Parameters.BORDER_PADDING + "-" +
                                (Parameters.BORDER_PADDING + Parameters.WIDTH) + ", " + Parameters.BORDER_PADDING +
                                "-" + (Parameters.BORDER_PADDING + Parameters.HEIGHT) + ")");
        }

        this.x = x;
        this.y = y;
        this.heading = heading;
        this.net = net;

    }

    void paintMineSweeper(Graphics g)
    {
        Graphics2D g2d = (Graphics2D) g.create();

        g2d.setColor(Color.black);

        g2d.translate(x, y);
        g2d.rotate(Math.toRadians(-heading));

        // Internal parameters for drawing the minesweeperS
        int x1 = -(Parameters.BODY_WIDTH / 2) - Parameters.WHEEL_WIDTH;
        int x2 = -(Parameters.BODY_WIDTH / 2);
        int x3 = (Parameters.BODY_WIDTH / 2);
        int x4 = (Parameters.BODY_WIDTH / 2) + Parameters.WHEEL_WIDTH;

        int y1 = -(Parameters.BODY_LENGTH / 2);
        int y2 = y1 + Parameters.WHEEL_LENGTH;
        int y3 = Parameters.BODY_LENGTH / 2;

        // Draw body + base of wheels
        g2d.drawLine(x1, y1, x4, y1);
        g2d.drawLine(x2, y1, x2, y3);
        g2d.drawLine(x2, y3, x3, y3);
        g2d.drawLine(x3, y3, x3, y1);

        // Draw left wheel
        g2d.drawLine(x1, y1, x1, y2);
        g2d.drawLine(x1, y2, x2, y2);

        // Draw right wheel
        g2d.drawLine(x4, y1, x4, y2);
        g2d.drawLine(x4, y2, x3, y2);

        // Draw debug data
        g2d.rotate(-Math.toRadians(-heading)); // Back to standard orientation so that we can read it!
        g2d.drawString(String.valueOf(score), 15, 15);

        g2d.dispose();
    }

    void doTurn()
    {
        // Get inputs
        Mine[] closestMines = getClosestMines();

        List<Double> inputs = new ArrayList<>();

        for (Mine closestMine : closestMines)
        {
            inputs.add(getRelativeBearing(closestMine));
            inputs.add(closestMine.getScore());
            inputs.add(Controller.getDistance(closestMine.x, closestMine.y, x, y));
        }

        // Run the neural network and set the speed of each wheel accordingly
        List<Double> outputs = net.run(inputs);

        double lSpeed = outputs.get(0);
        double rSpeed = outputs.get(1);

        double steeringForce = rSpeed - lSpeed;
        steeringForce *= Parameters.STEERING_MULTIPLIER;
        // in degrees per tick
        steeringForce = clamp(steeringForce, -Parameters.MAX_TURN_SPEED, Parameters.MAX_TURN_SPEED);

        // We use a MIN_SPEED parameter in case they're just idling forever (boring!)
        double forwardSpeed = Math.max(lSpeed + rSpeed, Parameters.MIN_SPEED);

        // Update heading & position
        heading = (heading + steeringForce) % 360; // modulo to keep it in the [0, 360) range

        double tentativeX = x + (((Math.sin(Math.toRadians(heading)) * forwardSpeed)));
        double tentativeY = y + (((Math.cos(Math.toRadians(heading)) * forwardSpeed)));
        x = clampWrap(tentativeX, Parameters.BORDER_PADDING, Parameters.BORDER_PADDING + Parameters.WIDTH);
        y = clampWrap(tentativeY, Parameters.BORDER_PADDING, Parameters.BORDER_PADDING + Parameters.HEIGHT);
    }

    /**
     * Takes an input `value` and, if it's out of the range [`min`, `max`], sets it to the closest extreme.
     * @param value
     * @param min
     * @param max
     * @return The clamped value.
     */
    private double clamp(double value, double min, double max)
    {
        return Math.max(min, Math.min(value, max));
    }

    /**
     * Similar to `clamp` in that this function takes in a `value` and returns a version within the range
     * [`min`, `max`] - except now, if `value` is out of range in one direction, the function returns the _opposite_
     * extreme. Example: `clamp(4.0, 1.0, 2.0) == 1.0`. This is useful so the minesweepers can drive off one end of
     * the board and come back on the other side.
     * @param value
     * @param min
     * @param max
     * @return The clamped value, wrapped to the other side of the [`min`, `max`] range.
     */
    private double clampWrap(double value, double min, double max)
    {
        if (value > max)
        {
            value = min;
        }
        else if (value < min)
        {
            value = max;
        }

        return value;
    }

    /**
     * Use when it finds a mine.
     */
    void collectedMine(Mine mine)
    {
        score += mine.getScore();
    }

    void epochEnd()
    {
        // nothing right now
    }

    /**
     * Use when you want to bring the counter back to zero.
     */
    public void resetMineCounter()
    {
        score = 0;
    }

    private Mine[] getClosestMines()
    {
        Mine[] closestMines = new Mine[Parameters.NUM_MINES_VISIBLE];
        for (Mine mine : Controller.mines)
        {
            for (int i = 0; i < closestMines.length; i++)
            {
                if (closestMines[i] == null ||
                        Controller.getDistance(mine.x, mine.y, x, y) < Controller.getDistance(closestMines[i].x, closestMines[i].y, x, y))
                {
                    closestMines[i] = mine;
                }
            }
        }
        return closestMines;
    }

    private double getRelativeBearing(Mine mine)
    {
        double xDist = x - mine.x;
        double yDist = y - mine.y;
        double absoluteBearing = Math.toDegrees(Math.atan(xDist / yDist));
        if (xDist < 0 && yDist > 0)
        {
            absoluteBearing += 180;
        }
        else if (xDist > 0 && yDist < 0)
        {
            //absoluteBearing += 180;
        }
        else if (xDist > 0 && yDist > 0)
        {
            absoluteBearing += 180;
        }

        double relativeBearing = (heading - absoluteBearing);
        while (relativeBearing < 0)
            relativeBearing += 360;
        if (relativeBearing >= 180)
        {
            relativeBearing = 360 - relativeBearing;
            relativeBearing *= -1;
        }
        return relativeBearing;
    }

}
