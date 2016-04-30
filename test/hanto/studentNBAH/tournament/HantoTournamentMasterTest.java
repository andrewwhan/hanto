package hanto.studentNBAH.tournament;

import static hanto.common.HantoPlayerColor.BLUE;
import static hanto.common.HantoPlayerColor.RED;
import static hanto.common.HantoPieceType.*;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import hanto.common.HantoCoordinate;
import hanto.common.HantoException;
import hanto.common.HantoGame;
import hanto.common.HantoGameID;
import hanto.studentNBAH.HantoGameFactory;
import hanto.studentNBAH.epsilon.EpsilonHantoGame;
import hanto.tournament.HantoMoveRecord;

public class HantoTournamentMasterTest {

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
	private EpsilonHantoGame blueFirstGame;
	private EpsilonHantoGame redFirstGame;
	private HantoPlayer bluePlayer;
	private HantoPlayer redPlayer;
	
	@BeforeClass
	public static void initializeClass()
	{
		factory = HantoGameFactory.getInstance();
	}
	
	@Before
	public void setup()
	{
		blueFirstGame = new EpsilonHantoGame(BLUE);
		redFirstGame = new EpsilonHantoGame(RED);
		
		bluePlayer = new HantoPlayer();
		bluePlayer.startGame(HantoGameID.EPSILON_HANTO, BLUE, true);
		
		redPlayer = new HantoPlayer();
		redPlayer.startGame(HantoGameID.EPSILON_HANTO, RED, false);
	}
	
	// Helper methods
	private HantoCoordinate makeCoordinate(int x, int y)
	{
		return new TestHantoCoordinate(x, y);
	}
	
	@Test // 1
	public void hantoPlayerMakesValidStartMove()
	{
		HantoMoveRecord previousMove = new HantoMoveRecord(BUTTERFLY, null, makeCoordinate(0, 0));
		HantoMoveRecord redMove = redPlayer.makeMove(previousMove);
		assertNotNull(redMove.getPiece());
	}
	
	@Test // 2
	public void hantoPlayerNeedsToResign() throws HantoException
	{
		blueFirstGame.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));
		blueFirstGame.makeMove(BUTTERFLY, null, makeCoordinate(1, -1));
		blueFirstGame.makeMove(CRAB, null, makeCoordinate(-1, 1));
		blueFirstGame.makeMove(BUTTERFLY, makeCoordinate(1, -1), makeCoordinate(1, 0));
		blueFirstGame.makeMove(CRAB, null, makeCoordinate(-1, 2));
		blueFirstGame.makeMove(BUTTERFLY, makeCoordinate(1, 0), makeCoordinate(0, 1));
		blueFirstGame.makeMove(CRAB, makeCoordinate(-1, 2), makeCoordinate(0, 2));
		redPlayer.setCurrentGameState(blueFirstGame);
		HantoMoveRecord redMove = redPlayer.makeMove(null);
		assertNull(redMove.getPiece());
		assertNull(redMove.getFrom());
		assertNull(redMove.getTo());
	}
	
	@Test // 3
	public void hantoPlayerMakesMoveInMiddleOfGame() throws HantoException
	{
		blueFirstGame.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));
		blueFirstGame.makeMove(BUTTERFLY, null, makeCoordinate(1, 0));
		blueFirstGame.makeMove(CRAB, null, makeCoordinate(-1, 0));
		blueFirstGame.makeMove(CRAB, null, makeCoordinate(2, 0));
		bluePlayer.setCurrentGameState(blueFirstGame);
		HantoMoveRecord blueMove = bluePlayer.makeMove(null);
		assertNotNull(blueMove.getPiece());
	}
	
	@Test // 4
	public void hantoPlayerUpdatesBoardWithInput() throws HantoException
	{
		blueFirstGame.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));
		blueFirstGame.makeMove(BUTTERFLY, null, makeCoordinate(1, 0));
		blueFirstGame.makeMove(CRAB, null, makeCoordinate(-1, 0));
		
		HantoMoveRecord redMove = new HantoMoveRecord(CRAB, null, makeCoordinate(2, 0));
		bluePlayer.setCurrentGameState(blueFirstGame);
		HantoMoveRecord blueMove = bluePlayer.makeMove(redMove);
		assertNotEquals(makeCoordinate(2, 0), blueMove.getFrom());
	}
	
	@Test // 5
	public void hantoPlayerOpponentMakesInvalidMove() throws HantoException
	{
		blueFirstGame.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));
		HantoMoveRecord duplicateMove = new HantoMoveRecord(BUTTERFLY, null, makeCoordinate(0, 0));
		redPlayer.setCurrentGameState(blueFirstGame);
		HantoMoveRecord redMove = redPlayer.makeMove(duplicateMove);
		assertNull(redMove);
	}
	
	@Test // 6
	public void hantoPlayerDoesNotSurroundOwnButterfly() throws HantoException
	{
		blueFirstGame.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));
		blueFirstGame.makeMove(BUTTERFLY, null, makeCoordinate(1, 0));
		blueFirstGame.makeMove(CRAB, null, makeCoordinate(-1, 0));
		blueFirstGame.makeMove(CRAB, null, makeCoordinate(2, -1));
		blueFirstGame.makeMove(CRAB, null, makeCoordinate(-1, -1));
		blueFirstGame.makeMove(CRAB, null, makeCoordinate(1, 1));
		blueFirstGame.makeMove(CRAB, null, makeCoordinate(-2, 1));
		blueFirstGame.makeMove(CRAB, makeCoordinate(2, -1), makeCoordinate(1, -1));
		blueFirstGame.makeMove(CRAB, makeCoordinate(-2, 1), makeCoordinate(-1, 1));
		blueFirstGame.makeMove(CRAB, makeCoordinate(1, 1), makeCoordinate(0, 1));
		
		bluePlayer.setCurrentGameState(blueFirstGame);
		HantoMoveRecord blueMove = bluePlayer.makeMove(null);
		assertNotEquals(makeCoordinate(0, -1), blueMove.getTo());
	}
}
