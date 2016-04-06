/*******************************************************************************
 * This files was developed for CS4233: Object-Oriented Analysis & Design.
 * The course was taken at Worcester Polytechnic Institute.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Copyright ©2016 Gary F. Pollice
 *******************************************************************************/

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

/**
 * Gamma Hanto game version
 * Has all functionality of Beta Hanto, with the addition of walking
 * Can no longer place pieces next to opponent pieces
 * Game now goes to turn 20 (40 moves total)
 * @author Nathan Bryant, Andrew Han
 *
 */
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
	
	/**
	 * Constructor
	 * @param movesFirst - The color of the player that will make the first move
	 */
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
		
		if(!isButterflyPlacedInTime())
		{
			throw new HantoException("Player needs to place butterfly by turn 4");
		}
		
		final HantoCoordinateImpl destination = new HantoCoordinateImpl(to);
		
		// If from coordinate is null, then this is placement of a new piece
		if(from == null){
			isValidNewPiece(destination, pieceType);
			
			HantoPieceImpl newGamePiece = new HantoPieceImpl(getCurrentPlayerColor(), pieceType);
			
			gameBoard.placePiece(destination, newGamePiece);
		}
		// Otherwise, it is a piece being moved
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
		return "The board";
	}

	/**
	 * Gets the current player's color
	 * @return - The color of the current player
	 */
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

	/**
	 * Checks to see if placing a piece at the given location is valid
	 * @param placementPos - The location to place a piece
	 * @return - True if it is a valid placement, false otherwise
	 */
	private boolean isValidPlacement(HantoCoordinate placementPos)
	{	
		Set<HantoCoordinate> occupiedPositions = gameBoard.getListOfPiecePositions();
		
		// If it is the first placement of a piece and the given coordinate is the origin, it is a valid move
		if (occupiedPositions.size() == 0 && placementPos.equals(new HantoCoordinateImpl(0, 0)))
		{
			return true;
		}
		
		// If the given position is already occupied, it is not a valid move
		if (occupiedPositions.contains(placementPos))
		{
			return false;
		}
		
		boolean isPieceAdjacent = false;
		
		Set<HantoCoordinate> adjacentPositions = HantoUtilities.getAdjacentPositions(placementPos);
		
		// Check to make sure that the piece is being placed next to another piece
		// Not a valid move if placed next to piece of opposite color
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
	
	/**
	 * Checks to see if the given piece is valid to be placed for the given player
	 * @param color - The color of the player placing the piece
	 * @param type - The type of the piece being placed
	 * @return - True if the piece can be placed, false if the player has already placed the maximum number of pieces for that type
	 */
	private boolean isPieceMaxReachedForPlayer(HantoPlayerColor color, HantoPieceType type){
		Set<HantoPiece> pieces = gameBoard.getPiecesOfPlayer(color);
		int currentPiecesOfType = HantoUtilities.countPlayerPiecesOfType(color, type, pieces);
		return currentPiecesOfType < pieceMax.get(type);
	}
	
	/**
	 * Checks for end conditions of the game
	 * @return - The result of the move
	 */
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
		if(turnNumber == maxTurns + 1){
			gameOver = true;
			return MoveResult.DRAW;
		}
		return MoveResult.OK;
	}
	
	/**
	 * Checks to see if the piece placement is valid
	 * @param to - The location to place the piece
	 * @param pieceType - The type of piece being placed
	 * @return - True if it is a valid placement, false otherwise
	 * @throws HantoException
	 */
	private boolean isValidNewPiece(HantoCoordinate to, HantoPieceType pieceType) throws HantoException
	{	
		if (!isValidPlacement(to))
		{
			throw new HantoException("Destination is not a valid coordinate to place piece");
		}
	
		HantoPlayerColor currentPlayer = getCurrentPlayerColor();
		
		if(!isPieceMaxReachedForPlayer(currentPlayer, pieceType)){
			throw new HantoException("Maximum pieces of that type has been reached");
		}
		
		return true;
	}
	
	/**
	 * Checks to see if the piece movement is valid
	 * @param from - The location of the piece being moved
	 * @param to - The location to move the piece to
	 * @param pieceType - The type of piece being moved
	 * @return - True if the move is valid, false otherwise
	 * @throws HantoException
	 */
	private boolean isValidMove(HantoCoordinate from, HantoCoordinate to, HantoPieceType pieceType) throws HantoException
	{
		HantoPlayerColor currentPlayer = getCurrentPlayerColor();
		Set<HantoPiece> currentPlayerPieces = gameBoard.getPiecesOfPlayer(currentPlayer);
		if (HantoUtilities.countPlayerPiecesOfType(currentPlayer, BUTTERFLY, currentPlayerPieces) == 0)
		{
			throw new HantoException("Player must place butterfly before moving existing pieces");
		}
		
		if (gameBoard.getPieceAt(from).getType() != pieceType)
		{
			throw new HantoException("Given piece type does not match the piece at the from location on the board");
		}
		
		PieceValidator pv = factory.makePieceValidator(pieceType, HantoGameID.GAMMA_HANTO);
		boolean canMove = pv.canMove(from, to, gameBoard);
		if (!canMove)
		{
			throw new HantoException("Invalid move");
		}
		return canMove;
	}
	
	/**
	 * Checks to see if the butterfly has been placed before the last turn to place the butterfly
	 * @return - True if the player still has time to place butterfly or already placed it in time, false otherwise
	 */
	private boolean isButterflyPlacedInTime()
	{
		HantoPlayerColor currentPlayer = getCurrentPlayerColor();
		Set<HantoPiece> currentPlayerPieces = gameBoard.getPiecesOfPlayer(currentPlayer);
		if((HantoUtilities.countPlayerPiecesOfType(currentPlayer, BUTTERFLY, currentPlayerPieces) == 0)
				&& turnNumber >= 7){
			return false;
		}
		
		return true;
	}
}
