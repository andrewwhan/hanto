package hanto.studentNBAH.delta;

import static hanto.common.HantoPieceType.BUTTERFLY;
import static hanto.common.HantoPieceType.SPARROW;
import static hanto.common.HantoPieceType.CRAB;
import static hanto.common.HantoPlayerColor.BLUE;
import static hanto.common.HantoPlayerColor.RED;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import hanto.common.HantoCoordinate;
import hanto.common.HantoException;
import hanto.common.HantoGame;
import hanto.common.HantoGameID;
import hanto.common.MoveResult;
import hanto.studentNBAH.HantoGameFactory;

public class DeltaHantoMasterTest {
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
		blueFirstGame = factory.makeHantoGame(HantoGameID.DELTA_HANTO, BLUE);
		redFirstGame = factory.makeHantoGame(HantoGameID.DELTA_HANTO, RED);
	}
	
	// Helper methods
	private HantoCoordinate makeCoordinate(int x, int y)
	{
		return new TestHantoCoordinate(x, y);
	}
	
	@Test //1
	public void blueResignsOnFirstTurn() throws HantoException{
		MoveResult mr = blueFirstGame.makeMove(null, null, null);
		assertEquals(MoveResult.RED_WINS, mr);
	}
	
	@Test //2
	public void redResignsOnMoveFour() throws HantoException{
		blueFirstGame.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));
		blueFirstGame.makeMove(BUTTERFLY, null, makeCoordinate(1, 0));
		blueFirstGame.makeMove(SPARROW, null, makeCoordinate(0, -1));
		MoveResult mr = blueFirstGame.makeMove(null, null, null);
		assertEquals(MoveResult.BLUE_WINS, mr);
	}
	
	@Test (expected = HantoException.class) //3
	public void moveAfterResignation() throws HantoException{
		blueFirstGame.makeMove(null, null, null);
		MoveResult mr = blueFirstGame.makeMove(BUTTERFLY, null, makeCoordinate(0,0));
	}
	
	@Test
	public void walkButterflyOneSpace() throws HantoException{
		blueFirstGame.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));
		blueFirstGame.makeMove(BUTTERFLY, null, makeCoordinate(1, 0));
		MoveResult mr = blueFirstGame.makeMove(BUTTERFLY, makeCoordinate(0, 0), makeCoordinate(0, 1));
		assertEquals(MoveResult.OK, mr);
	}
	
	@Test //4
	public void flyBlueSparrowOneSpace() throws HantoException{
		blueFirstGame.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));
		blueFirstGame.makeMove(BUTTERFLY, null, makeCoordinate(1, 0));
		blueFirstGame.makeMove(SPARROW, null, makeCoordinate(0, -1));
		blueFirstGame.makeMove(SPARROW, null, makeCoordinate(1, 1));
		MoveResult mr = blueFirstGame.makeMove(SPARROW, makeCoordinate(0, -1), makeCoordinate(-1, 0));
		assertEquals(MoveResult.OK, mr);
	}
	
	@Test //5
	public void flyBlueSparrowTwoSpaces() throws HantoException{
		blueFirstGame.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));
		blueFirstGame.makeMove(BUTTERFLY, null, makeCoordinate(1, 0));
		blueFirstGame.makeMove(SPARROW, null, makeCoordinate(0, -1));
		blueFirstGame.makeMove(SPARROW, null, makeCoordinate(1, 1));
		MoveResult mr = blueFirstGame.makeMove(SPARROW, makeCoordinate(0, -1), makeCoordinate(-1, 1));
		assertEquals(MoveResult.OK, mr);
	}
	
	@Test //6
	public void flyBlueSparrowOverOtherPieces() throws HantoException{
		blueFirstGame.makeMove(SPARROW, null, makeCoordinate(0, 0));
		blueFirstGame.makeMove(BUTTERFLY, null, makeCoordinate(1, -1));
		blueFirstGame.makeMove(BUTTERFLY, null, makeCoordinate(0, 1));
		blueFirstGame.makeMove(SPARROW, null, makeCoordinate(2, -1));
		MoveResult mr = blueFirstGame.makeMove(SPARROW, makeCoordinate(0, 0), makeCoordinate(1, 0));
		assertEquals(MoveResult.OK, mr);
	}
	
	@Test (expected = HantoException.class)//4
	public void cantFlyOntoOccupiedSpace() throws HantoException{
		blueFirstGame.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));
		blueFirstGame.makeMove(BUTTERFLY, null, makeCoordinate(1, 0));
		blueFirstGame.makeMove(SPARROW, null, makeCoordinate(0, -1));
		blueFirstGame.makeMove(SPARROW, null, makeCoordinate(1, 1));
		blueFirstGame.makeMove(SPARROW, makeCoordinate(0, -1), makeCoordinate(1, 0));
	}
	
	@Test (expected = HantoException.class) //7
	public void cantFlyWhenBreakingContiguity() throws HantoException{
		blueFirstGame.makeMove(SPARROW, null, makeCoordinate(0, 0));
		blueFirstGame.makeMove(BUTTERFLY, null, makeCoordinate(1, -1));
		blueFirstGame.makeMove(BUTTERFLY, null, makeCoordinate(0, 1));
		blueFirstGame.makeMove(SPARROW, null, makeCoordinate(2, -1));
		blueFirstGame.makeMove(SPARROW, makeCoordinate(0, 0), makeCoordinate(-1, 0));
	}
	
	@Test //8
	public void crabWalksOneSpace() throws HantoException{
		blueFirstGame.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));
		blueFirstGame.makeMove(BUTTERFLY, null, makeCoordinate(1, 0));
		blueFirstGame.makeMove(CRAB, null, makeCoordinate(0, -1));
		blueFirstGame.makeMove(SPARROW, null, makeCoordinate(1, 1));
		MoveResult mr = blueFirstGame.makeMove(CRAB, makeCoordinate(0, -1), makeCoordinate(-1, 0));
		assertEquals(MoveResult.OK, mr);
	}
	
	@Test //9
	public void crabWalksTwoSpaces() throws HantoException{
		blueFirstGame.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));
		blueFirstGame.makeMove(BUTTERFLY, null, makeCoordinate(1, 0));
		blueFirstGame.makeMove(CRAB, null, makeCoordinate(0, -1));
		blueFirstGame.makeMove(SPARROW, null, makeCoordinate(1, 1));
		MoveResult mr = blueFirstGame.makeMove(CRAB, makeCoordinate(0, -1), makeCoordinate(-1, 1));
		assertEquals(MoveResult.OK, mr);
	}
	
	@Test //10
	public void crabWalksThreeSpaces() throws HantoException{
		blueFirstGame.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));
		blueFirstGame.makeMove(BUTTERFLY, null, makeCoordinate(1, 0));
		blueFirstGame.makeMove(CRAB, null, makeCoordinate(-1, 0));
		blueFirstGame.makeMove(SPARROW, null, makeCoordinate(2, 0));
		MoveResult mr = blueFirstGame.makeMove(CRAB, makeCoordinate(-1, 0), makeCoordinate(1, 1));
		assertEquals(MoveResult.OK, mr);
	}
	
	@Test(expected = HantoException.class) //11
	public void crabCantWalkFourSpaces() throws HantoException{
		blueFirstGame.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));
		blueFirstGame.makeMove(BUTTERFLY, null, makeCoordinate(1, 0));
		blueFirstGame.makeMove(CRAB, null, makeCoordinate(-1, 0));
		blueFirstGame.makeMove(SPARROW, null, makeCoordinate(2, 0));
		blueFirstGame.makeMove(CRAB, makeCoordinate(-1, 0), makeCoordinate(2, 1));
	}
	
	@Test(expected = HantoException.class) //12
	public void crabCantSlideOneSpace() throws HantoException{
		blueFirstGame.makeMove(CRAB, null, makeCoordinate(0, 0));
		blueFirstGame.makeMove(BUTTERFLY, null, makeCoordinate(1, -1));
		blueFirstGame.makeMove(BUTTERFLY, null, makeCoordinate(0, 1));
		blueFirstGame.makeMove(SPARROW, null, makeCoordinate(2, -1));
		blueFirstGame.makeMove(CRAB, makeCoordinate(0, 0), makeCoordinate(1, 0));
	}

	@Test(expected = HantoException.class) //13
	public void crabCantWalkThroughWall() throws HantoException{
		blueFirstGame.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));
		blueFirstGame.makeMove(BUTTERFLY, null, makeCoordinate(1, 0));
		blueFirstGame.makeMove(SPARROW, null, makeCoordinate(-1, 0));
		blueFirstGame.makeMove(SPARROW, null, makeCoordinate(1, 1));
		blueFirstGame.makeMove(CRAB, null, makeCoordinate(0, -1));
		blueFirstGame.makeMove(SPARROW, null, makeCoordinate(2, 0));
		blueFirstGame.makeMove(CRAB, makeCoordinate(0, -1), makeCoordinate(0, 1));
	}
	
	@Test(expected = HantoException.class) //14
	public void crabCantBreakContiguityInWalk() throws HantoException{
		blueFirstGame.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));
		blueFirstGame.makeMove(BUTTERFLY, null, makeCoordinate(1, -1));
		blueFirstGame.makeMove(SPARROW, null, makeCoordinate(-1, 0));
		blueFirstGame.makeMove(SPARROW, null, makeCoordinate(2, -2));
		blueFirstGame.makeMove(SPARROW, null, makeCoordinate(-2, 0));
		blueFirstGame.makeMove(SPARROW, null, makeCoordinate(2, -3));
		blueFirstGame.makeMove(CRAB, null, makeCoordinate(-2, -1));
		blueFirstGame.makeMove(SPARROW, null, makeCoordinate(2, -4));
		blueFirstGame.makeMove(CRAB, makeCoordinate(-2, -1), makeCoordinate(1, -3));
	}
	
}
