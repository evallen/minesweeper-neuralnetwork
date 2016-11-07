package tests;

import org.junit.Before;
import org.junit.Test;

import controller.Mine;
import controller.MineSweeper;

public class MineSweeperTest {

	Mine mine;
	MineSweeper sweeper;
	
	@Before
	public void setUp() throws Exception {
		mine = new Mine(100, 100);
		sweeper = new MineSweeper(50, 150, 226, null);
	}

	@Test
	public void testGetMineRelativeBearing() {
		System.out.println();
	}

}
