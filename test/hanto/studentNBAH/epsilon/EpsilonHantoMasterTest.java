package hanto.studentNBAH.epsilon;

import static hanto.common.HantoPlayerColor.BLUE;
import static hanto.common.HantoPlayerColor.RED;
import static hanto.common.HantoPieceType.*;
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import hanto.common.HantoCoordinate;
import hanto.common.HantoException;
import hanto.common.HantoGame;
import hanto.common.HantoGameID;
import hanto.common.HantoPrematureResignationException;
import hanto.common.MoveResult;
import hanto.studentNBAH.HantoGameFactory;

public class EpsilonHantoMasterTest {
	/**
	 * Internal class for these test cases.
	 * @version Sep 13, 2014
	 */
	class TestHantoCoordinate implements HantoCoordinate
	{
		private final int x, y;
		
		public TestHantoCoordinate(int x, int y)
		{
			this.x = x;
			this.y = y;
		}
		/*
		 * @see hanto.common.HantoCoordinate#getX()
		 */
		@Override
		public int getX()
		{
			return x;
		}

		/*
		 * @see hanto.common.HantoCoordinate#getY()
		 */
		@Override
		public int getY()
		{
			return y;
		}

	}
	
	private static HantoGameFactory factory;
	private HantoGame blueFirstGame;
	private HantoGame redFirstGame;
	
	@BeforeClass
	public static void initializeClass()
	{
		factory = HantoGameFactory.getInstance();
	}
	
	@Before
	public void setup()
	{
		blueFirstGame = factory.makeHantoGame(HantoGameID.EPSILON_HANTO, BLUE);
		redFirstGame = factory.makeHantoGame(HantoGameID.EPSILON_HANTO, RED);
	}
	
	// Helper methods
	private HantoCoordinate makeCoordinate(int x, int y)
	{
		return new TestHantoCoordinate(x, y);
	}
	
	@Test (expected = HantoPrematureResignationException.class) //1
	public void blueResignsOnFirstTurn() throws HantoException{
		blueFirstGame.makeMove(null, null, null);
	}
	
	@Test (expected = HantoPrematureResignationException.class) //2
	public void noValidMovesButValidPlacementResignation() throws HantoException{
		blueFirstGame.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));
		blueFirstGame.makeMove(BUTTERFLY, null, makeCoordinate(1, 0));
		blueFirstGame.makeMove(CRAB, null, makeCoordinate(-1, 1));
		blueFirstGame.makeMove(BUTTERFLY, makeCoordinate(1, 0), makeCoordinate(0, 1));
		blueFirstGame.makeMove(CRAB, makeCoordinate(-1, 1), makeCoordinate(-1, 2));
		blueFirstGame.makeMove(null, null, null);
	}
	
	@Test (expected = HantoPrematureResignationException.class) //3
	public void validMoveExistsResignation() throws HantoException
	{
		blueFirstGame.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));
		blueFirstGame.makeMove(BUTTERFLY, null, makeCoordinate(1,0));
		blueFirstGame.makeMove(null, null, null);
	}
	
	@Test //4
	public void validResignation() throws HantoException{
		blueFirstGame.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));
		blueFirstGame.makeMove(BUTTERFLY, null, makeCoordinate(1, -1));
		blueFirstGame.makeMove(CRAB, null, makeCoordinate(-1, 1));
		blueFirstGame.makeMove(BUTTERFLY, makeCoordinate(1, -1), makeCoordinate(1, 0));
		blueFirstGame.makeMove(CRAB, null, makeCoordinate(-1, 2));
		blueFirstGame.makeMove(BUTTERFLY, makeCoordinate(1, 0), makeCoordinate(0, 1));
		blueFirstGame.makeMove(CRAB, makeCoordinate(-1, 2), makeCoordinate(0, 2));
		MoveResult mr = blueFirstGame.makeMove(null, null, null);
		assertEquals(MoveResult.BLUE_WINS, mr);
	}
	
	@Test (expected = HantoException.class) //5
	public void flyPastMaxDist() throws HantoException{
		blueFirstGame.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));
		blueFirstGame.makeMove(BUTTERFLY, null, makeCoordinate(1, 0));
		blueFirstGame.makeMove(CRAB, null, makeCoordinate(-1, 0));
		blueFirstGame.makeMove(CRAB, null, makeCoordinate(2, 0));
		blueFirstGame.makeMove(SPARROW, null, makeCoordinate(-2, 0));
		blueFirstGame.makeMove(CRAB, null, makeCoordinate(3, 0));
		blueFirstGame.makeMove(SPARROW, makeCoordinate(-2, 0), makeCoordinate(4, 0));
	}
	
	@Test //6
	public void successfulJump() throws HantoException{
		blueFirstGame.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));
		blueFirstGame.makeMove(BUTTERFLY, null, makeCoordinate(1, 0));
		blueFirstGame.makeMove(CRAB, null, makeCoordinate(-1, 0));
		blueFirstGame.makeMove(CRAB, null, makeCoordinate(2, 0));
		blueFirstGame.makeMove(HORSE, null, makeCoordinate(-2, 0));
		blueFirstGame.makeMove(CRAB, null, makeCoordinate(3, 0));
		MoveResult mr = blueFirstGame.makeMove(HORSE, makeCoordinate(-2, 0), makeCoordinate(4, 0));
		assertEquals(MoveResult.OK, mr);
	}
	
	@Test (expected = HantoException.class)//7
	public void notStraightLineJump() throws HantoException{
		blueFirstGame.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));
		blueFirstGame.makeMove(BUTTERFLY, null, makeCoordinate(1, 0));
		blueFirstGame.makeMove(CRAB, null, makeCoordinate(-1, 0));
		blueFirstGame.makeMove(CRAB, null, makeCoordinate(2, 0));
		blueFirstGame.makeMove(HORSE, null, makeCoordinate(-2, 0));
		blueFirstGame.makeMove(CRAB, null, makeCoordinate(3, 0));
		blueFirstGame.makeMove(HORSE, makeCoordinate(-2, 0), makeCoordinate(1,-1));
	}
	
	@Test (expected = HantoException.class)//8
	public void destOccupiedJump() throws HantoException{
		blueFirstGame.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));
		blueFirstGame.makeMove(BUTTERFLY, null, makeCoordinate(1, 0));
		blueFirstGame.makeMove(CRAB, null, makeCoordinate(-1, 0));
		blueFirstGame.makeMove(CRAB, null, makeCoordinate(2, 0));
		blueFirstGame.makeMove(HORSE, null, makeCoordinate(-2, 0));
		blueFirstGame.makeMove(CRAB, null, makeCoordinate(3, 0));
		blueFirstGame.makeMove(HORSE, makeCoordinate(-2, 0), makeCoordinate(3,0));
	}
	
	@Test (expected = HantoException.class)//9
	public void jumpOverNoPieces() throws HantoException{
		blueFirstGame.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));
		blueFirstGame.makeMove(BUTTERFLY, null, makeCoordinate(1, 0));
		blueFirstGame.makeMove(CRAB, null, makeCoordinate(-1, 0));
		blueFirstGame.makeMove(CRAB, null, makeCoordinate(2, 0));
		blueFirstGame.makeMove(HORSE, null, makeCoordinate(-2, 0));
		blueFirstGame.makeMove(CRAB, null, makeCoordinate(3, 0));
		blueFirstGame.makeMove(HORSE, makeCoordinate(-2, 0), makeCoordinate(-1, -1));
	}
	
	@Test (expected = HantoException.class)//10
	public void gapInJump() throws HantoException{
		blueFirstGame.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));
		blueFirstGame.makeMove(BUTTERFLY, null, makeCoordinate(1, -1));
		blueFirstGame.makeMove(CRAB, null, makeCoordinate(-1, 0));
		blueFirstGame.makeMove(CRAB, null, makeCoordinate(2, -1));
		blueFirstGame.makeMove(HORSE, null, makeCoordinate(-2, 0));
		blueFirstGame.makeMove(CRAB, null, makeCoordinate(2, 0));
		blueFirstGame.makeMove(HORSE, makeCoordinate(-2, 0), makeCoordinate(3, 0));
	}
	
	@Test (expected = HantoException.class)//11
	public void jumpBreaksContiguity() throws HantoException{
		blueFirstGame.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));
		blueFirstGame.makeMove(BUTTERFLY, null, makeCoordinate(1, 0));
		blueFirstGame.makeMove(CRAB, null, makeCoordinate(-1, 0));
		blueFirstGame.makeMove(CRAB, null, makeCoordinate(2, 0));
		blueFirstGame.makeMove(HORSE, null, makeCoordinate(-2, 0));
		blueFirstGame.makeMove(CRAB, null, makeCoordinate(3, 0));
		blueFirstGame.makeMove(CRAB, null, makeCoordinate(-3, 0));
		blueFirstGame.makeMove(CRAB, null, makeCoordinate(4, 0));
		blueFirstGame.makeMove(HORSE, makeCoordinate(-2, 0), makeCoordinate(5, 0));
	}
}
