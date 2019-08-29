package controller;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;

public class BoardPanel extends JPanel implements MouseListener
{
    // Sometimes, we want to disable screen painting to speed up the simulation.
    boolean painting = true;

    BoardPanel()
    {
        super();
        System.out.println("Board dimensions are: (" + Parameters.PANEL_PREFERRED_WIDTH + ", " + Parameters.PANEL_PREFERRED_HEIGHT + ")");
        this.setPreferredSize(new Dimension(Parameters.PANEL_PREFERRED_WIDTH, Parameters.PANEL_PREFERRED_HEIGHT));
        this.setFocusable(true);
    }

    @Override
    public Dimension getPreferredSize()
    {
        return new Dimension(Parameters.PANEL_PREFERRED_WIDTH, Parameters.PANEL_PREFERRED_HEIGHT);
    }

    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);

        // Draw border
        g.drawRect(Parameters.BORDER_PADDING, Parameters.BORDER_PADDING, Parameters.WIDTH, Parameters.HEIGHT);

        // Without the second condition, the first frame throws an exception because it tries to draw the
        // screen before the mines have been generated.
        if (painting && Controller.mines[0] != null)
        {
            for (Mine mine : Controller.mines)
            {
                mine.paintMine(g);
            }

            for (MineSweeper sweeper : Controller.sweepers)
            {
                sweeper.paintMineSweeper(g);
            }
        }

        Graphics2D g2d = (Graphics2D) g.create();
        // Here we draw the text; we use g2d.getFontMetrics().getAscent to factor in the height of the text.
        // In the second string of text, we simply add 2 pixels to the y-coordinate of the previous drawn string
        // for spacing and then continue.
        g2d.drawString("Generation: " + Controller.ga.generations, Parameters.BORDER_PADDING + 10,
                    Parameters.BORDER_PADDING + 10 + g2d.getFontMetrics().getAscent());
        g2d.drawString("Tick Multiplier: " + Parameters.TICK_MULTIPLIER + "x", Parameters.BORDER_PADDING + 10,
                    Parameters.BORDER_PADDING + 10 + g2d.getFontMetrics().getAscent() + 2 +
                        g2d.getFontMetrics().getAscent());
        g2d.dispose();
    }


    @Override
    public void mouseClicked(MouseEvent e)
    {
        // Nothing

    }

    @Override
    public void mousePressed(MouseEvent e)
    {
        // Nothing

    }

    @Override
    public void mouseReleased(MouseEvent e)
    {
        this.requestFocus();

    }

    @Override
    public void mouseEntered(MouseEvent e)
    {
        // Nothing

    }

    @Override
    public void mouseExited(MouseEvent e)
    {
        // Nothing

    }
}
