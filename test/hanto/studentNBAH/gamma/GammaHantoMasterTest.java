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
	private HantoGame blueGame;
	private HantoGame redGame;
	
	@BeforeClass
	public static void initializeClass()
	{
		factory = HantoGameFactory.getInstance();
	}
	
	@Before
	public void setup()
	{
		// By default, blue moves first.
		blueGame = factory.makeHantoGame(HantoGameID.GAMMA_HANTO, BLUE);
		redGame = factory.makeHantoGame(HantoGameID.GAMMA_HANTO, RED);
	}
	
	@Test	// 1
	public void bluePlacesInitialButterflyAtOrigin() throws HantoException
	{
		final MoveResult mr = blueGame.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));
		assertEquals(OK, mr);
		final HantoPiece p = blueGame.getPieceAt(makeCoordinate(0, 0));
		assertEquals(BLUE, p.getColor());
		assertEquals(BUTTERFLY, p.getType());
	}
	
	@Test // 2
	public void redPlacesInitialButterflyAtOrigin() throws HantoException
	{
		final MoveResult mr = redGame.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));
		assertEquals(OK, mr);
		final HantoPiece p = redGame.getPieceAt(makeCoordinate(0, 0));
		assertEquals(RED, p.getColor());
		assertEquals(BUTTERFLY, p.getType());
	}
	
	@Test // 3
	public void bluePlacesInitialPieceOutsideOrigin() throws HantoException
	{
		try
		{
			final MoveResult mr = blueGame.makeMove(SPARROW, null, makeCoordinate(1, 0));
			
			// If no exception is caught, then the test has failed
			assertTrue(false);
		}
		catch (HantoException e)
		{
			assertEquals("Destination is not a valid coordinate to place piece", e.getMessage());
		}
	}
	
	@Test // 4
	public void redPlacesInitialPieceAdjacentToBluePiece() throws HantoException
	{
		final MoveResult blueMove = blueGame.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));
		final MoveResult redMove = blueGame.makeMove(BUTTERFLY, null, makeCoordinate(1, 0));
		assertEquals(redMove, MoveResult.OK);
		final HantoPiece p = blueGame.getPieceAt(makeCoordinate(1, 0));
		assertEquals(RED, p.getColor());
		assertEquals(BUTTERFLY, p.getType());
	}
	
	@Test // 5
	public void bluePlacesPieceAdjacentToRedPiece() throws HantoException
	{
		try
		{
			blueGame.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));
			blueGame.makeMove(BUTTERFLY, null, makeCoordinate(1, 0));
			blueGame.makeMove(SPARROW, null, makeCoordinate(0, 1));
		}
		catch (HantoException e)
		{
			assertEquals("Destination is not a valid coordinate to place piece", e.getMessage());
		}
	}
	
	@Test // 6
	public void blueWalksFirstPiece() throws HantoException
	{
		blueGame.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));
		blueGame.makeMove(BUTTERFLY, null, makeCoordinate(1, 0));
		final MoveResult mr = blueGame.makeMove(BUTTERFLY, makeCoordinate(0, 0), makeCoordinate(0, 1));
		assertEquals(MoveResult.OK, mr);
	}
	
	@Test // 7
	public void distanceCalculatorTest()
	{
		assertEquals(1, HantoUtilities.getDistance(makeCoordinate(0,0), makeCoordinate(1,-1)));
		assertEquals(0, HantoUtilities.getDistance(makeCoordinate(0,0), makeCoordinate(0, 0)));
		assertEquals(2, HantoUtilities.getDistance(makeCoordinate(0,0), makeCoordinate(2,-1)));
		assertEquals(2, HantoUtilities.getDistance(makeCoordinate(0,0), makeCoordinate(1, 1)));
		assertEquals(2, HantoUtilities.getDistance(makeCoordinate(0,0), makeCoordinate(-1,-1)));
		assertEquals(1, HantoUtilities.getDistance(makeCoordinate(0,0), makeCoordinate(-1, 1)));
	}
	
	@Test // 8
	public void walkingBreaksContiguity() throws HantoException
	{
		try
		{
			blueGame.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));
			blueGame.makeMove(BUTTERFLY, null, makeCoordinate(1, 0));
			final MoveResult mr = blueGame.makeMove(BUTTERFLY, makeCoordinate(0, 0), makeCoordinate(-1, 0));
			assertTrue(false);
		}
		catch(HantoException e)
		{
			assertEquals("Invalid move", e.getMessage());
		}
	}
	
	// Helper methods
	private HantoCoordinate makeCoordinate(int x, int y)
	{
		return new TestHantoCoordinate(x, y);
	}
}
