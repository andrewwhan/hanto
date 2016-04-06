package hanto.studentNBAH.gamma;

import static hanto.common.HantoPieceType.BUTTERFLY;
import static hanto.common.HantoPieceType.SPARROW;
import static hanto.common.HantoPlayerColor.BLUE;
import static hanto.common.HantoPlayerColor.RED;
import static hanto.common.MoveResult.OK;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import hanto.common.HantoCoordinate;
import hanto.common.HantoException;
import hanto.common.HantoGame;
import hanto.common.HantoGameID;
import hanto.common.HantoPiece;
import hanto.common.MoveResult;
import hanto.studentNBAH.HantoGameFactory;
import hanto.studentNBAH.common.HantoUtilities;

public class GammaHantoMasterTest {

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
		blueFirstGame = factory.makeHantoGame(HantoGameID.GAMMA_HANTO, BLUE);
		redFirstGame = factory.makeHantoGame(HantoGameID.GAMMA_HANTO, RED);
	}
	
	@Test // 1
	public void getPrintableBoard()
	{
		assertEquals("The board", blueFirstGame.getPrintableBoard());
	}
	
	@Test	// 2
	public void bluePlacesInitialButterflyAtOrigin() throws HantoException
	{
		final MoveResult mr = blueFirstGame.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));
		assertEquals(OK, mr);
		final HantoPiece p = blueFirstGame.getPieceAt(makeCoordinate(0, 0));
		assertEquals(BLUE, p.getColor());
		assertEquals(BUTTERFLY, p.getType());
	}
	
	@Test // 3
	public void redPlacesInitialButterflyAtOrigin() throws HantoException
	{
		final MoveResult mr = redFirstGame.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));
		assertEquals(OK, mr);
		final HantoPiece p = redFirstGame.getPieceAt(makeCoordinate(0, 0));
		assertEquals(RED, p.getColor());
		assertEquals(BUTTERFLY, p.getType());
	}
	
	@Test // 4
	public void bluePlacesInitialPieceOutsideOrigin() throws HantoException
	{
		try
		{
			final MoveResult mr = blueFirstGame.makeMove(SPARROW, null, makeCoordinate(1, 0));
			
			// If no exception is caught, then the test has failed
			assertTrue(false);
		}
		catch (HantoException e)
		{
			assertEquals("Destination is not a valid coordinate to place piece", e.getMessage());
		}
	}
	
	@Test // 5
	public void redPlacesInitialPieceAdjacentToBluePiece() throws HantoException
	{
		final MoveResult blueMove = blueFirstGame.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));
		final MoveResult redMove = blueFirstGame.makeMove(BUTTERFLY, null, makeCoordinate(1, 0));
		assertEquals(redMove, MoveResult.OK);
		final HantoPiece p = blueFirstGame.getPieceAt(makeCoordinate(1, 0));
		assertEquals(RED, p.getColor());
		assertEquals(BUTTERFLY, p.getType());
	}
	
	@Test // 6
	public void bluePlacesPieceAdjacentToRedPiece() throws HantoException
	{
		try
		{
			blueFirstGame.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));
			blueFirstGame.makeMove(BUTTERFLY, null, makeCoordinate(1, 0));
			blueFirstGame.makeMove(SPARROW, null, makeCoordinate(0, 1));
			assertTrue(false);
		}
		catch (HantoException e)
		{
			assertEquals("Destination is not a valid coordinate to place piece", e.getMessage());
		}
	}
	
	@Test // 7
	public void bluePlacesTooManyButterflies() throws HantoException
	{
		try
		{
			blueFirstGame.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));
			blueFirstGame.makeMove(BUTTERFLY, null, makeCoordinate(1, -1));
			blueFirstGame.makeMove(BUTTERFLY, null, makeCoordinate(-1, 1));
			assertTrue(false);
		}
		catch (HantoException e)
		{
			assertEquals("Maximum pieces of that type has been reached", e.getMessage());
		}
	}
	
	@Test (expected = HantoException.class) // 8
	public void placePieceAtOccupiedPosition() throws HantoException
	{
		blueFirstGame.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));
		blueFirstGame.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));
	}
	
	@Test // 9
	public void blueWalksFirstPiece() throws HantoException
	{
		blueFirstGame.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));
		blueFirstGame.makeMove(BUTTERFLY, null, makeCoordinate(1, 0));
		final MoveResult mr = blueFirstGame.makeMove(BUTTERFLY, makeCoordinate(0, 0), makeCoordinate(0, 1));
		assertEquals(MoveResult.OK, mr);
	}
	
	@Test // 10
	public void movePieceDoesNotMatchType() throws HantoException
	{
		try
		{
			blueFirstGame.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));
			blueFirstGame.makeMove(BUTTERFLY, null, makeCoordinate(1, 0));
			blueFirstGame.makeMove(SPARROW, makeCoordinate(0,0), makeCoordinate(1, -1));
			assertTrue(false);
		}
		catch(HantoException e)
		{
			assertEquals("Given piece type does not match the piece at the from location on the board", e.getMessage());
		}
	}
	
	@Test (expected = HantoException.class) // 11
	public void movePieceToOccupied() throws HantoException
	{
		blueFirstGame.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));
		blueFirstGame.makeMove(BUTTERFLY, null, makeCoordinate(1, 0));
		blueFirstGame.makeMove(BUTTERFLY, makeCoordinate(0, 0), makeCoordinate(1, 0));
	}
	
	@Test (expected = HantoException.class) // 12
	public void movePieceFartherThan1Hex() throws HantoException
	{
		blueFirstGame.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));
		blueFirstGame.makeMove(BUTTERFLY, null, makeCoordinate(1, -1));
		blueFirstGame.makeMove(SPARROW, null, makeCoordinate(-1, 1));
		blueFirstGame.makeMove(SPARROW, null, makeCoordinate(1, -2));
		blueFirstGame.makeMove(SPARROW, makeCoordinate(-1, 1), makeCoordinate(1, 0));
	}
	
	@Test // 13
	public void distanceCalculatorTest()
	{
		assertEquals(1, HantoUtilities.getDistance(makeCoordinate(0,0), makeCoordinate(1,-1)));
		assertEquals(0, HantoUtilities.getDistance(makeCoordinate(0,0), makeCoordinate(0, 0)));
		assertEquals(2, HantoUtilities.getDistance(makeCoordinate(0,0), makeCoordinate(2,-1)));
		assertEquals(2, HantoUtilities.getDistance(makeCoordinate(0,0), makeCoordinate(1, 1)));
		assertEquals(2, HantoUtilities.getDistance(makeCoordinate(0,0), makeCoordinate(-1,-1)));
		assertEquals(1, HantoUtilities.getDistance(makeCoordinate(0,0), makeCoordinate(-1, 1)));
	}
	
	@Test (expected = HantoException.class) // 14
	public void movePieceThatCannotSlide() throws HantoException
	{
		blueFirstGame.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));
		blueFirstGame.makeMove(BUTTERFLY, null, makeCoordinate(1, -1));
		blueFirstGame.makeMove(SPARROW, null, makeCoordinate(0, 1));
		blueFirstGame.makeMove(SPARROW, null, makeCoordinate(2, -1));
		blueFirstGame.makeMove(BUTTERFLY, makeCoordinate(0, 0), makeCoordinate(1, 0));
	}
	
	@Test // 15
	public void walkingBreaksContiguity() throws HantoException
	{
		try
		{
			blueFirstGame.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));
			blueFirstGame.makeMove(BUTTERFLY, null, makeCoordinate(1, 0));
			final MoveResult mr = blueFirstGame.makeMove(BUTTERFLY, makeCoordinate(0, 0), makeCoordinate(-1, 0));
			assertTrue(false);
		}
		catch(HantoException e)
		{
			assertEquals("Invalid move", e.getMessage());
		}
	}
	
	@Test // 16
	public void surroundRedButterfly() throws HantoException
	{
		blueFirstGame.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));
		blueFirstGame.makeMove(BUTTERFLY, null, makeCoordinate(1, -1));
		blueFirstGame.makeMove(SPARROW, null, makeCoordinate(0, 1));
		blueFirstGame.makeMove(SPARROW, null, makeCoordinate(2, -1));
		blueFirstGame.makeMove(SPARROW, null, makeCoordinate(-1, 0));
		blueFirstGame.makeMove(SPARROW, null, makeCoordinate(1, -2));
		blueFirstGame.makeMove(SPARROW, makeCoordinate(-1, 0), makeCoordinate(0, -1));
		blueFirstGame.makeMove(SPARROW, null, makeCoordinate(2, -2));
		MoveResult mr = blueFirstGame.makeMove(SPARROW, makeCoordinate(0, 1), makeCoordinate(1, 0));
		assertEquals(MoveResult.BLUE_WINS, mr);
	}
	
	@Test // 17
	public void surroundBlueButterfly() throws HantoException
	{
		redFirstGame.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));
		redFirstGame.makeMove(BUTTERFLY, null, makeCoordinate(1, -1));
		redFirstGame.makeMove(SPARROW, null, makeCoordinate(0, 1));
		redFirstGame.makeMove(SPARROW, null, makeCoordinate(2, -1));
		redFirstGame.makeMove(SPARROW, null, makeCoordinate(-1, 0));
		redFirstGame.makeMove(SPARROW, null, makeCoordinate(1, -2));
		redFirstGame.makeMove(SPARROW, makeCoordinate(-1, 0), makeCoordinate(0, -1));
		redFirstGame.makeMove(SPARROW, null, makeCoordinate(2, -2));
		MoveResult mr = redFirstGame.makeMove(SPARROW, makeCoordinate(0, 1), makeCoordinate(1, 0));
		assertEquals(MoveResult.RED_WINS, mr);
	}
	
	@Test // 18
	public void blueFailsToPlaceButterflyByFourthTurn() throws HantoException
	{
		try
		{
			blueFirstGame.makeMove(SPARROW, null, makeCoordinate(0, 0));
			blueFirstGame.makeMove(BUTTERFLY, null, makeCoordinate(1, -1));
			blueFirstGame.makeMove(SPARROW, null, makeCoordinate(-1, 1));
			blueFirstGame.makeMove(BUTTERFLY, makeCoordinate(1, -1), makeCoordinate(1, 0));
			blueFirstGame.makeMove(SPARROW, null, makeCoordinate(-2, 2));
			blueFirstGame.makeMove(BUTTERFLY, makeCoordinate(1, 0), makeCoordinate(1, -1));
			blueFirstGame.makeMove(SPARROW, null, makeCoordinate(-3, 3));
			assertTrue(false);
		}
		catch(HantoException e)
		{
			assertEquals("Player needs to place butterfly by turn 4", e.getMessage());
		}
	}
	
	@Test // 19
	public void makeMoveAfterGameOver() throws HantoException
	{
		int turnCount = 4;
		try
		{
			blueFirstGame.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));
			blueFirstGame.makeMove(BUTTERFLY, null, makeCoordinate(1, -1));
			blueFirstGame.makeMove(SPARROW, null, makeCoordinate(-1, 1));
			
			for (int i = 0; i < 10; i++)
			{
				blueFirstGame.makeMove(BUTTERFLY, makeCoordinate(1, -1), makeCoordinate(0, -1));
				turnCount++;
				
				blueFirstGame.makeMove(SPARROW, makeCoordinate(-1, 1), makeCoordinate(0, 1));
				turnCount++;
				
				blueFirstGame.makeMove(BUTTERFLY, makeCoordinate(0, -1), makeCoordinate(1, -1));
				turnCount++;
				
				blueFirstGame.makeMove(SPARROW, makeCoordinate(0, 1), makeCoordinate(-1, 1));
				turnCount++;
			}
			assertTrue(false);
		}
		catch(HantoException e)
		{
			assertEquals("You cannot move after the game is finished", e.getMessage());
			assertEquals(41, turnCount);
		}
	}
	
	@Test // 20
	public void attemptingWalkBeforePlacingButterfly() throws HantoException
	{
		try
		{
			blueFirstGame.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));
			blueFirstGame.makeMove(SPARROW, null, makeCoordinate(1, -1));
			blueFirstGame.makeMove(SPARROW, null, makeCoordinate(-1, 1));
			blueFirstGame.makeMove(SPARROW, makeCoordinate(1, -1), makeCoordinate(1, 0));
			assertTrue(false);
		}
		catch (HantoException e)
		{
			assertEquals("Player must place butterfly before moving existing pieces", e.getMessage());
		}
	}
	
	// Helper methods
	private HantoCoordinate makeCoordinate(int x, int y)
	{
		return new TestHantoCoordinate(x, y);
	}
}
