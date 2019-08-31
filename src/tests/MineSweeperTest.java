package tests;

import org.junit.Before;
import org.junit.Test;

import controller.Mine;
import controller.MineSweeper;

public class MineSweeperTest
{

    @Before
    public void setUp() throws Exception
    {
        Mine mine = new Mine(100, 100);
        MineSweeper sweeper = new MineSweeper(50, 150, 226, null);
    }

    @Test
    public void testGetMineRelativeBearing()
    {
        System.out.println();
    }

}
