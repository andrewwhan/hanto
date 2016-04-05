package hanto.studentNBAH.gamma;

import static hanto.common.HantoPieceType.BUTTERFLY;
import static hanto.common.HantoPieceType.SPARROW;
import static hanto.common.HantoPlayerColor.BLUE;
import static hanto.common.HantoPlayerColor.RED;

import java.util.HashMap;
import java.util.Set;

import hanto.common.HantoCoordinate;
import hanto.common.HantoException;
import hanto.common.HantoGame;
import hanto.common.HantoGameID;
import hanto.common.HantoPiece;
import hanto.common.HantoPieceType;
import hanto.common.HantoPlayerColor;
import hanto.common.MoveResult;
import hanto.studentNBAH.common.HantoCoordinateImpl;
import hanto.studentNBAH.common.HantoGameBoard;
import hanto.studentNBAH.common.HantoPieceImpl;
import hanto.studentNBAH.common.HantoUtilities;
import hanto.studentNBAH.common.PieceValidator;
import hanto.studentNBAH.common.PieceValidatorFactory;

public class GammaHantoGame implements HantoGame{

	private HantoCoordinateImpl blueButterflyHex = null, redButterflyHex = null;
	private final HantoPiece blueButterfly = new HantoPieceImpl(BLUE, BUTTERFLY);
	private final HantoPiece redButterfly = new HantoPieceImpl(RED, BUTTERFLY);
	private boolean gameOver = false;
	private int turnNumber = 1;
	private HashMap<HantoPieceType, Integer> pieceMax = new HashMap<HantoPieceType, Integer>();
	
	private HantoGameBoard gameBoard = new HantoGameBoard();
	
	private static PieceValidatorFactory factory = PieceValidatorFactory.getInstance();
	
	private final int maxTurns = 40;
	
	private final HantoPlayerColor firstPlayer;
	
	public GammaHantoGame(HantoPlayerColor movesFirst){
		pieceMax.put(BUTTERFLY, 1);
		pieceMax.put(SPARROW, 5);
		firstPlayer = movesFirst;
	}
	
	@Override
	public MoveResult makeMove(HantoPieceType pieceType, HantoCoordinate from, HantoCoordinate to)
			throws HantoException {
		
		if (gameOver) 
		{
			throw new HantoException("You cannot move after the game is finished");
		}
		
		final HantoCoordinateImpl destination = new HantoCoordinateImpl(to);
		
		if(from == null){
			isValidNewPiece(destination, pieceType);
			
			HantoPieceImpl newGamePiece = new HantoPieceImpl(getCurrentPlayerColor(), pieceType);
			
			gameBoard.placePiece(destination, newGamePiece);
		}
		else{
			final HantoCoordinateImpl fromImpl = new HantoCoordinateImpl(from);
			
			isValidMove(fromImpl, destination, pieceType);
			
			gameBoard.movePiece(fromImpl, destination);
		}
		
		return getMoveResult();
	}

	@Override
	public HantoPiece getPieceAt(HantoCoordinate where) {
		HantoCoordinateImpl whereImpl = new HantoCoordinateImpl(where);
		return gameBoard.getPieceAt(whereImpl);
	}

	@Override
	public String getPrintableBoard() {
		// TODO Auto-generated method stub
		return null;
	}

	private HantoPlayerColor getCurrentPlayerColor()
	{
		if (turnNumber % 2 == 0)
		{
			return HantoUtilities.getOppositePlayerColor(firstPlayer);
		}
		else 
		{
			return firstPlayer;
		}
	}

	private boolean isValidPlacement(HantoCoordinate placementPos)
	{	
		Set<HantoCoordinate> occupiedPositions = gameBoard.getListOfPiecePositions();
		
		if (occupiedPositions.size() == 0 && placementPos.equals(new HantoCoordinateImpl(0, 0)))
		{
			return true;
		}
		
		if (occupiedPositions.contains(placementPos))
		{
			return false;
		}
		
		boolean isPieceAdjacent = false;
		
		Set<HantoCoordinate> adjacentPositions = HantoUtilities.getAdjacentPositions(placementPos);
		for(HantoCoordinate occupiedPos : occupiedPositions)
		{
			for(HantoCoordinate adjacentPos : adjacentPositions)
			{
				if (occupiedPos.equals(adjacentPos))
				{
					isPieceAdjacent = true;
					
					// In turns 1 and 2, don't check if piece is placed next to an opponent's piece
					if (turnNumber != 1 && turnNumber != 2)
					{
						// If any of the adjacent positions are occupied by a piece of the opposite color, return false
						if (gameBoard.getPieceAt(occupiedPos).getColor() == HantoUtilities.getOppositePlayerColor(getCurrentPlayerColor()))
						{
							return false;
						}
					}
				}
			}
		}
		return isPieceAdjacent;
	}
	
	private boolean isValidPiece(HantoPlayerColor color, HantoPieceType type){
		Set<HantoPiece> pieces = gameBoard.getPiecesOfPlayer(color);
		int currentPiecesOfType = HantoUtilities.countPlayerPiecesOfType(color, type, pieces);
		return currentPiecesOfType < pieceMax.get(type);
	}
	
	private MoveResult getMoveResult()
	{
		if(gameBoard.checkButterflySurrounded(RED)){
			gameOver = true;
			return MoveResult.BLUE_WINS;
		}
		
		if(gameBoard.checkButterflySurrounded(BLUE)){
			gameOver = true;
			return MoveResult.RED_WINS;
		}
		
		turnNumber++;
		if(turnNumber == maxTurns){
			gameOver = true;
			return MoveResult.DRAW;
		}
		return MoveResult.OK;
	}
	
	private boolean isValidNewPiece(HantoCoordinate to, HantoPieceType pieceType) throws HantoException
	{	
		if (!isValidPlacement(to))
		{
			throw new HantoException("Destination is not a valid coordinate to place piece");
		}
	
		HantoPlayerColor currentPlayer = getCurrentPlayerColor();
		
		if(!isValidPiece(currentPlayer, pieceType)){
			throw new HantoException("Maximum pieces of that type has been reached");
		}
		
		Set<HantoPiece> currentPlayerPieces = gameBoard.getPiecesOfPlayer(currentPlayer);
		if((HantoUtilities.countPlayerPiecesOfType(currentPlayer, BUTTERFLY, currentPlayerPieces) == 0)
				&& turnNumber >= 7){
			throw new HantoException("Player needs to place butterfly by turn 4");
		}
		
		return true;
	}
	
	private boolean isValidMove(HantoCoordinate from, HantoCoordinate to, HantoPieceType pieceType) throws HantoException
	{
		PieceValidator pv = factory.makePieceValidator(pieceType, HantoGameID.GAMMA_HANTO);
		boolean canMove = pv.canMove(from, to, gameBoard);
		if (!canMove)
		{
			throw new HantoException("Invalid move");
		}
		return canMove;
	}
}
