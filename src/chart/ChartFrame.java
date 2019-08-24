package chart;

import java.awt.Dimension;

import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;

import controller.Parameters;

public class ChartFrame extends JFrame
{

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public DefaultCategoryDataset dataset = new DefaultCategoryDataset();

    public ChartFrame(String windowTitle)
    {
        super();

        //dataset = new DefaultCategoryDataset();

        JFreeChart lineChart = ChartFactory.createLineChart("Fitness over time", "Generations", "Fitness", dataset);

        ChartPanel chartPanel = new ChartPanel(lineChart);
        chartPanel.setPreferredSize(new Dimension(Parameters.CHARTPANEL_WIDTH, Parameters.CHARTPANEL_HEIGHT));
        setContentPane(chartPanel);
    }

}
