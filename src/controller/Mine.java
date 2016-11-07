package controller;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Arrays;
import java.util.Random;

public class Mine {
	
	int x;
	int y;
	final int sideLength = 5;
	private int scoreIndex;
	
	public Mine (int x, int y) throws Exception {
		if (x < Parameters.BORDER_PADDING || x >= Parameters.BORDER_PADDING + Parameters.BOARD_WIDTH
				|| y < Parameters.BORDER_PADDING || y >= Parameters.BORDER_PADDING + Parameters.BOARD_HEIGHT) {
			throw new Exception("Initial coordinates of mine are out of bounds! ( " + x + ", " + y + ")");
		}
		
		this.x = x; 
		this.y = y;
		this.scoreIndex = getRandomScoreIndex();
	}
	
	public void paintMine (Graphics g) {
		Graphics2D g2d = (Graphics2D) g.create();
		
		Color color = getScoreColor();
		g2d.setColor(color);
		
		g2d.translate(x, y);
		g2d.drawRect(-(sideLength / 2), -(sideLength / 2), sideLength, sideLength);
		
		g2d.dispose();
	}
	
	public Color getScoreColor() {
		return (Color) Parameters.MINE_SCORE_INFO[scoreIndex][2];
		
	}
	
	public int getRandomScoreIndex() {
		double[] cumulativeScoreProbs = new double[Parameters.MINE_SCORE_INFO.length];
		cumulativeScoreProbs[0] = (double) Parameters.MINE_SCORE_INFO[0][1];
		for (int i = 1; i < cumulativeScoreProbs.length; i++) {
			cumulativeScoreProbs[i] = cumulativeScoreProbs[i-1] + (double) Parameters.MINE_SCORE_INFO[i][1];
		}
		Random rng = new Random();
		
		double randomNum = rng.nextDouble() * cumulativeScoreProbs[cumulativeScoreProbs.length - 1];
		
		int index = Arrays.binarySearch(cumulativeScoreProbs, randomNum);
		
		if (index < 0) {
			index = Math.abs(index + 1);
		}
				
		return index;
	}
	
	public double getScore() {
		return (double) Parameters.MINE_SCORE_INFO[scoreIndex][0];
	}
	
	public void setScore(double score) {
		for (int i = 0; i < Parameters.MINE_SCORE_INFO.length; i++) {
			if ((double) Parameters.MINE_SCORE_INFO[i][0] == score) {
				scoreIndex = i;
				return;
			}
		}
		
		throw new IllegalArgumentException("Couldn't set mine's score - not in Parameters.MINE_SCORE_INFO!");
	}
}
