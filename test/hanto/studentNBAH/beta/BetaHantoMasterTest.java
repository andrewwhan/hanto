/*******************************************************************************
 * This files was developed for CS4233: Object-Oriented Analysis & Design.
 * The course was taken at Worcester Polytechnic Institute.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package hanto.studentNBAH.beta;

import static hanto.common.HantoPieceType.*;
import static hanto.common.MoveResult.*;
import static hanto.common.HantoPlayerColor.*;
import static org.junit.Assert.*;
import hanto.common.*;
import hanto.studentNBAH.HantoGameFactory;

import org.junit.*;

/**
 * Test cases for Beta Hanto.
 * @version Sep 14, 2014
 */
public class BetaHantoMasterTest
{
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
	private HantoGame game;
	
	@BeforeClass
	public static void initializeClass()
	{
		factory = HantoGameFactory.getInstance();
	}
	
	@Before
	public void setup()
	{
		// By default, blue moves first.
		game = factory.makeHantoGame(HantoGameID.BETA_HANTO, BLUE);
	}
	
	@Test	// 1
	public void bluePlacesInitialButterflyAtOrigin() throws HantoException
	{
		final MoveResult mr = game.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));
		assertEquals(OK, mr);
		final HantoPiece p = game.getPieceAt(makeCoordinate(0, 0));
		assertEquals(BLUE, p.getColor());
		assertEquals(BUTTERFLY, p.getType());
	}
	
	@Test // 2
	public void bluePlacesInitialSparrowAtOrigin() throws HantoException
	{
		final MoveResult mr = game.makeMove(SPARROW, null, makeCoordinate(0, 0));
		assertEquals(OK, mr);
		final HantoPiece p = game.getPieceAt(makeCoordinate(0, 0));
		assertEquals(BLUE, p.getColor());
		assertEquals(SPARROW, p.getType());
	}
	
	@Test // 3
	public void bluePlacesInitialPieceOutsideOrigin() throws HantoException
	{
		try
		{
			final MoveResult mr = game.makeMove(SPARROW, null, makeCoordinate(1, 0));
			
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
		final MoveResult blueMove = game.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));
		final MoveResult redMove = game.makeMove(BUTTERFLY, null, makeCoordinate(1, 0));
		assertEquals(redMove, MoveResult.OK);
		final HantoPiece p = game.getPieceAt(makeCoordinate(1, 0));
		assertEquals(RED, p.getColor());
		assertEquals(BUTTERFLY, p.getType());
	}

	@Test // 5
	public void redPlacesInitialPieceNotAdjacentToBluePiece() throws HantoException
	{
		try
		{
			game.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));
			game.makeMove(BUTTERFLY, null, makeCoordinate(2, 0));
			
			// If no exception is caught, then the test has failed
			assertTrue(false);
		}
		catch (HantoException e)
		{
			assertEquals("Destination is not a valid coordinate to place piece", e.getMessage());
		}
	}
	
	@Test // 6
	public void bluePlacesTwoButterflys() throws HantoException
	{
		try
		{
			game.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));
			game.makeMove(BUTTERFLY, null, makeCoordinate(1, 0));
			game.makeMove(BUTTERFLY, null, makeCoordinate(0, 1));
			
			// If no exception is caught, then the test has failed
			assertTrue(false);
		}
		catch (HantoException e)
		{
			assertEquals("Maximum pieces of that type has been reached", e.getMessage());
		}
	}
	
	@Test // 7
	public void playToTurnThirteen() throws HantoException
	{
		game.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));
		game.makeMove(BUTTERFLY, null, makeCoordinate(1, 0));
		game.makeMove(SPARROW, null, makeCoordinate(2, 0));
		game.makeMove(SPARROW, null, makeCoordinate(3, 0));
		game.makeMove(SPARROW, null, makeCoordinate(4, 0));
		game.makeMove(SPARROW, null, makeCoordinate(5, 0));
		game.makeMove(SPARROW, null, makeCoordinate(6, 0));
		game.makeMove(SPARROW, null, makeCoordinate(7, 0));
		game.makeMove(SPARROW, null, makeCoordinate(8, 0));
		game.makeMove(SPARROW, null, makeCoordinate(9, 0));
		game.makeMove(SPARROW, null, makeCoordinate(10, 0));
		final MoveResult mr = game.makeMove(SPARROW, null, makeCoordinate(11, 0));
		assertEquals(DRAW, mr);
	}
	
	@Test // 8
	public void surroundBlueButterfly() throws HantoException
	{
		game.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));
		game.makeMove(BUTTERFLY, null, makeCoordinate(1, 0));
		game.makeMove(SPARROW, null, makeCoordinate(1, -1));
		game.makeMove(SPARROW, null, makeCoordinate(0, -1));
		game.makeMove(SPARROW, null, makeCoordinate(-1, 0));
		game.makeMove(SPARROW, null, makeCoordinate(-1, 1));
		final MoveResult mr = game.makeMove(SPARROW, null, makeCoordinate(0, 1));
		assertEquals(MoveResult.RED_WINS, mr);
	}
	
	@Test // 9
	public void surroundRedButterfly() throws HantoException
	{
		game.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));
		game.makeMove(BUTTERFLY, null, makeCoordinate(1, 0));
		game.makeMove(SPARROW, null, makeCoordinate(0, 1));
		game.makeMove(SPARROW, null, makeCoordinate(1, 1));
		game.makeMove(SPARROW, null, makeCoordinate(2, 0));
		game.makeMove(SPARROW, null, makeCoordinate(2, -1));
		final MoveResult mr = game.makeMove(SPARROW, null, makeCoordinate(1, -1));
		assertEquals(MoveResult.BLUE_WINS, mr);
	}
	
	@Test //10
	public void noButterflyBeforeTurn4() throws HantoException
	{
		try
		{
			game.makeMove(SPARROW, null, makeCoordinate(0, 0));
			game.makeMove(SPARROW, null, makeCoordinate(1, 0));
			game.makeMove(SPARROW, null, makeCoordinate(2, 0));
			game.makeMove(SPARROW, null, makeCoordinate(3, 0));
			game.makeMove(SPARROW, null, makeCoordinate(4, 0));
			game.makeMove(SPARROW, null, makeCoordinate(5, 0));
			game.makeMove(SPARROW, null, makeCoordinate(6, 0));
			
			// If no exception is caught, then the test has failed
			assertTrue(false);
		}
		catch (HantoException e)
		{
			assertEquals("Player needs to place butterfly by turn 4", e.getMessage());
		}
	}
	
	@Test //11
	public void piecesCantMoveInBeta() throws HantoException
	{
		try
		{
			game.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));
			game.makeMove(BUTTERFLY, null, makeCoordinate(1, 0));
			game.makeMove(BUTTERFLY, makeCoordinate(0, 0), makeCoordinate(1, -1));

			// If no exception is caught, then the test has failed
			assertTrue(false);
		}
		catch (HantoException e)
		{
			assertEquals("Pieces can't move in beta Hanto", e.getMessage());
		}
	}
	
	// Helper methods
	private HantoCoordinate makeCoordinate(int x, int y)
	{
		return new TestHantoCoordinate(x, y);
	}
}
