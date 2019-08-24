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
    double heading;
    double nearestMine;
    final int bodyLength = 14;
    final int bodyWidth = 5;
    final int wheelLength = 8;
    final int wheelWidth = 4;
    final double maxTurnSpeed = 80; // in degrees
    int debugPaints = 0;
    public int score = 0;
    static double collectDistance = 5;
    static double speedMultiplier = 4.0;

    double forwardSpeed = 0;
    double lSpeed = 0;
    double rSpeed = 0;

    NeuralNet net;

    public MineSweeper(double x, double y, double heading, NeuralNet net) throws Exception
    {
        if (x < Parameters.BORDER_PADDING || x >= Parameters.BORDER_PADDING + Parameters.WIDTH
                || y < Parameters.BORDER_PADDING || y >= Parameters.BORDER_PADDING + Parameters.HEIGHT) {
            throw new Exception("Initial coordinates of minesweeper are out of bounds! (" + x + ", " + y + "); (" + Parameters.BORDER_PADDING + "-" + (Parameters.BORDER_PADDING + Parameters.WIDTH) + ", " + Parameters.BORDER_PADDING + "-" + (Parameters.BORDER_PADDING + Parameters.HEIGHT) + ")");
        }

        this.x = x;
        this.y = y;
        this.heading = heading;
        this.net = net;

    }

    public void paintMineSweeper(Graphics g)
    {
        Graphics2D g2d = (Graphics2D) g.create();

        g2d.setColor(Color.black);
        //g2d.drawLine((int) getClosestMine().x, (int) getClosestMine().y, (int) x, (int) y);

        g2d.translate(x, y);
        g2d.rotate(Math.toRadians(-heading));

        int x1 = -(bodyWidth / 2) - wheelWidth;
        int x2 = -(bodyWidth / 2);
        int x3 = (bodyWidth / 2);
        int x4 = (bodyWidth / 2) + wheelWidth;

        int y1 = -(bodyLength / 2);
        int y2 = y1 + wheelLength;
        int y3 = bodyLength / 2;

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
        g2d.rotate(-Math.toRadians(-heading));
        g2d.drawString(String.valueOf(score), 15, 15);
        //g2d.drawString(String.valueOf((int) getRelativeBearing(getClosestMine())), 15, 15 + g2d.getFontMetrics().getAscent() + 2);
        //g2d.drawString(String.valueOf((int) (360 - ((heading + 180) % 360))) , 15, 15 + 2 * (g2d.getFontMetrics().getAscent() + 2));

        g2d.dispose();

        //System.out.println("Paints: " + ++debugPaints);
    }

    public void doTurn()
    {
        Mine[] closestMines = getClosestMines();

        List<Double> inputs = new ArrayList<Double>();
		/*inputs.add(Math.sin(Math.toRadians(heading)));
		inputs.add(Math.cos(Math.toRadians(heading)));
		inputs.add((closestMine.x - x) / Controller.getDistance(x, y, closestMine.x, closestMine.y));
		inputs.add((closestMine.y - y) / Controller.getDistance(x, y, closestMine.x, closestMine.y));*/

        for (int i = 0; i < closestMines.length; i++) {
            inputs.add(getRelativeBearing(closestMines[i]));
            inputs.add(closestMines[i].getScore());
        }

        List<Double> outputs = net.run(inputs);

        lSpeed = outputs.get(0) * speedMultiplier;
        rSpeed = outputs.get(1) * speedMultiplier;

        double steeringForce = rSpeed - lSpeed;
        steeringForce *= 4;
        steeringForce = clamp(steeringForce, -maxTurnSpeed, maxTurnSpeed);

        //forwardSpeed = lSpeed + rSpeed;
        forwardSpeed = Parameters.MIN_SPEED;

        heading = (heading + steeringForce) % 360; // modulo to keep it in the [0, 360) range

        x = clampWrap(x + (((Math.sin(Math.toRadians(heading)) * forwardSpeed))), Parameters.BORDER_PADDING, Parameters.BORDER_PADDING + Parameters.WIDTH);
        y = clampWrap(y + (((Math.cos(Math.toRadians(heading)) * forwardSpeed))), Parameters.BORDER_PADDING, Parameters.BORDER_PADDING + Parameters.HEIGHT);
		
		/*double deltaX = 10 * Main.getDeltaTimeSec();
		double deltaY = 10 * Main.getDeltaTimeSec();
		x += deltaX;
		y += deltaY;
		
		System.out.println("dX: " + deltaX);
		System.out.println("dY: " + deltaY);*/
        //System.out.println("deltaTime = " + Main.getDeltaTimeSec());
    }

    public double clamp(double value, double min, double max)
    {
        return Math.max(min, Math.min(value, max));
    }

    public double clampWrap(double value, double min, double max)
    {
        if (value > max) {
            value = min;
        } else if (value < min) {
            value = max;
        }

        return value;
    }

    /**
     * Use when it finds a mine.
     */
    public void collectedMine(Mine mine)
    {
        score += mine.getScore();
    }

    public void epochEnd()
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
        for (Mine mine : Controller.mines) {
            for (int i = 0; i < closestMines.length; i++) {
                if (closestMines[i] == null || Controller.getDistance(mine.x, mine.y, x, y) < Controller.getDistance(closestMines[i].x, closestMines[i].y, x, y)) {
                    closestMines[i] = mine;
                    continue;
                }
            }
        }
        return closestMines;
    }

    public double getRelativeBearing(Mine mine)
    {
        double xDist = x - mine.x;
        double yDist = y - mine.y;
        double absoluteBearing = Math.toDegrees(Math.atan(xDist / yDist));
        if (xDist < 0 && yDist > 0) {
            absoluteBearing += 180;
        } else if (xDist > 0 && yDist < 0) {
            //absoluteBearing += 180;
        } else if (xDist > 0 && yDist > 0) {
            absoluteBearing += 180;
        }

        double relativeBearing = (heading - absoluteBearing);
        while (relativeBearing < 0)
            relativeBearing += 360;
        if (relativeBearing >= 180) {
            relativeBearing = 360 - relativeBearing;
            relativeBearing *= -1;
        }
        return relativeBearing;
    }

}
