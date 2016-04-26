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

package hanto.studentNBAH.common;

import static hanto.common.HantoPieceType.BUTTERFLY;
import static hanto.common.HantoPlayerColor.BLUE;
import static hanto.common.HantoPlayerColor.RED;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import hanto.common.HantoCoordinate;
import hanto.common.HantoException;
import hanto.common.HantoGame;
import hanto.common.HantoGameID;
import hanto.common.HantoPiece;
import hanto.common.HantoPieceType;
import hanto.common.HantoPlayerColor;
import hanto.common.HantoPrematureResignationException;
import hanto.common.MoveResult;

/**
 * Represents a HantoGame, using Template pattern.
 * @author Nathan Bryant, Andrew Han
 *
 */
public abstract class BaseHantoGame implements HantoGame {

	protected HantoPlayerColor firstPlayer;
	protected boolean gameOver = false;
	protected int turnNumber = 1;
	protected int maxTurns = Integer.MAX_VALUE;
	protected int latestButterflyTurn = 4;
	protected Map<HantoPieceType, Integer> pieceMax = new HashMap<HantoPieceType, Integer>();
	protected HantoGameBoard gameBoard;
	protected HantoGameID currentGameID;

	protected static PieceValidatorFactory pieceValidatorFactory = PieceValidatorFactory.getInstance();

	/**
	 * Creates a BaseHantoGame with given color to be the first player
	 * @param firstPlayer - The color of the player that starts the game
	 */
	public BaseHantoGame(HantoPlayerColor firstPlayer)
	{
		this.firstPlayer = firstPlayer;
		gameBoard = new HantoGameBoard();
	}

	/**
	 * Creates a BaseHantoGame with given color to be the first player and specified
	 * maximum number of turns
	 * @param firstPlayer - The color of the player that starts the game
	 * @param maxTurns - Maximum number of turns for the game before draw
	 */
	public BaseHantoGame(HantoPlayerColor firstPlayer, int maxTurns)
	{
		this.firstPlayer = firstPlayer;
		this.maxTurns = maxTurns;
		gameBoard = new HantoGameBoard();
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

	@Override
	public MoveResult makeMove(HantoPieceType pieceType, HantoCoordinate source,
			HantoCoordinate destination) throws HantoException
	{		
		if (gameOver) {
			throw new HantoException("You cannot move after the game is finished");
		}

		if(checkForResignation(pieceType, source, destination)){
			gameOver = true;
			if(getCurrentPlayerColor() == HantoPlayerColor.BLUE){
				return MoveResult.RED_WINS;
			}
			else{
				return MoveResult.BLUE_WINS;
			}
		}

		if(!checkButterflyPlacedInTimeRule())
		{
			throw new HantoException("Player needs to place butterfly by turn 4");
		}

		final HantoCoordinateImpl destinationImpl = new HantoCoordinateImpl(destination);

		// If from coordinate is null, then this is placement of a new piece
		if(source == null){
			isValidNewPiece(destinationImpl, pieceType);

			HantoPieceImpl newGamePiece = new HantoPieceImpl(getCurrentPlayerColor(), pieceType);

			gameBoard.placePiece(destinationImpl, newGamePiece);
		}
		// Otherwise, it is a piece being moved
		else{
			final HantoCoordinateImpl fromImpl = new HantoCoordinateImpl(source);

			isValidMove(fromImpl, destinationImpl, pieceType);

			gameBoard.movePiece(fromImpl, destinationImpl);
		}

		return getMoveResult();
	}

	/**
	 * Gets the current player's color
	 * @return - The color of the current player
	 */
	protected HantoPlayerColor getCurrentPlayerColor()
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
	 * Checks to see if the given piece is valid to be placed for the given player
	 * @param color - The color of the player placing the piece
	 * @param type - The type of the piece being placed
	 * @return - True if the piece can be placed, false if the player has already placed the maximum number of pieces for that type
	 */
	protected boolean isPieceMaxReachedForPlayer(HantoPlayerColor color, HantoPieceType type){
		if (!pieceMax.containsKey(type))
		{
			return false;
		}

		Set<HantoPiece> pieces = gameBoard.getPiecesOfPlayer(color);
		int currentPiecesOfType = HantoUtilities.countPlayerPiecesOfType(color, type, pieces);
		return currentPiecesOfType < pieceMax.get(type);
	}

	/**
	 * Checks for end conditions of the game
	 * @return - The result of the move
	 */
	protected MoveResult getMoveResult()
	{
		if (gameBoard.checkButterflySurrounded(RED) && gameBoard.checkButterflySurrounded(BLUE))
		{
			gameOver = true;
			return MoveResult.DRAW;
		}

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
	 * Checks whether the given position is a valid place to put a new piece
	 * @param placementPos - the position to place the piece
	 * @return True if the position is valid, false otherwise
	 */
	protected boolean isValidPlacement(HantoCoordinate placementPos)
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

					if (!checkAdjacentOpponentRule(placementPos, occupiedPos))
					{
						return false;
					}
				}
			}
		}
		return isPieceAdjacent;
	}

	/**
	 * Checks to see if the piece movement is valid
	 * @param from - The location of the piece being moved
	 * @param to - The location to move the piece to
	 * @param pieceType - The type of piece being moved
	 * @return - True if the move is valid, false otherwise
	 * @throws HantoException
	 */
	protected boolean isValidMove(HantoCoordinate from, HantoCoordinate to, HantoPieceType pieceType) throws HantoException
	{
		HantoPlayerColor currentPlayer = getCurrentPlayerColor();
		Set<HantoPiece> currentPlayerPieces = gameBoard.getPiecesOfPlayer(currentPlayer);
		if (HantoUtilities.countPlayerPiecesOfType(currentPlayer, BUTTERFLY, currentPlayerPieces) == 0)
		{
			throw new HantoException("Player must place butterfly before moving existing pieces");
		}

		HantoPiece movingPiece = gameBoard.getPieceAt(from);

		if (movingPiece == null)
		{
			throw new HantoException("No piece located at given location");
		}

		if (movingPiece.getType() != pieceType)
		{
			throw new HantoException("Given piece type does not match the piece at the from location on the board");
		}

		if (movingPiece.getColor() != currentPlayer)
		{
			throw new HantoException("Can't move an opponent's piece");
		}

		PieceValidator pv = pieceValidatorFactory.makePieceValidator(pieceType, currentGameID);
		boolean canMove = pv.canMove(from, to, gameBoard);
		if (!canMove)
		{
			throw new HantoException("Invalid move");
		}
		return canMove;
	}

	/**
	 * Checks to see if the piece placement is valid
	 * @param to - The location to place the piece
	 * @param pieceType - The type of piece being placed
	 * @return - True if it is a valid placement, false otherwise
	 * @throws HantoException
	 */
	protected boolean isValidNewPiece(HantoCoordinate to, HantoPieceType pieceType) throws HantoException
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
	 * Rule for checking whether a piece will be placed next to an opponent
	 * @param placementPos - Position to place the new piece
	 * @param occupiedPos - Position adjacent to the placement position
	 * @return false if the occupiedPos is occupied by an opponent, true otherwise
	 */
	protected boolean checkAdjacentOpponentRule(HantoCoordinate placementPos, HantoCoordinate occupiedPos)
	{
		// In turns 1 and 2, don't check if piece is placed next to an opponent's piece
		if (turnNumber != 1 && turnNumber != 2)
		{
			// If any of the adjacent positions are occupied by a piece of the opposite color, return false
			if (gameBoard.getPieceAt(occupiedPos).getColor() == HantoUtilities.getOppositePlayerColor(getCurrentPlayerColor()))
			{
				return false;
			}
		}

		return true;
	}

	/**
	 * Checks to see if the butterfly has been placed before the last turn to place the butterfly
	 * @return - True if the player still has time to place butterfly or already placed it in time, false otherwise
	 */
	protected boolean checkButterflyPlacedInTimeRule()
	{
		int latestMoveNumber = (latestButterflyTurn * 2) - 1;

		HantoPlayerColor currentPlayer = getCurrentPlayerColor();
		Set<HantoPiece> currentPlayerPieces = gameBoard.getPiecesOfPlayer(currentPlayer);
		if((HantoUtilities.countPlayerPiecesOfType(currentPlayer, BUTTERFLY, currentPlayerPieces) == 0)
				&& turnNumber >= latestMoveNumber){
			return false;
		}

		return true;
	}

	/**
	 * Checks the inputs to makeMove and determines whether the player has resigned
	 * @param pieceType - The piecetype of the move
	 * @param source - The source of the move
	 * @param destination - The destination for the move
	 * @return True if all parameters are null, false otherwise
	 * @throws HantoPrematureResignationException
	 */
	protected boolean checkForResignation(HantoPieceType pieceType, HantoCoordinate source,
			HantoCoordinate destination) throws HantoPrematureResignationException
	{
		return pieceType == null && source == null && destination == null;
	}
	
	/**
	 * Checks to see if current player has a valid move
	 * @return True if player has valid move, false otherwise
	 */
	protected boolean validMoveExists()
	{
		HantoPlayerColor currentPlayer = getCurrentPlayerColor();
		
		Set<HantoPiece> currentPlayerPieces = gameBoard.getPiecesOfPlayer(currentPlayer);
		
		for(HantoPiece currentPiece : currentPlayerPieces)
		{
			PieceValidator currentPieceValidator = PieceValidatorFactory.getInstance()
					.makePieceValidator(currentPiece.getType(), currentGameID);
			if (currentPieceValidator.hasValidMove(gameBoard.getPosByPiece(currentPiece), gameBoard))
			{
				return true;
			}
		}
		
		for (HantoPieceType pieceType : pieceMax.keySet())
		{
			int currentTypeMax = pieceMax.get(pieceType);
			int currentTypeCount = HantoUtilities.countPlayerPiecesOfType(currentPlayer, pieceType, 
					gameBoard.getPiecesOfPlayer(currentPlayer));
			if (currentTypeCount < currentTypeMax)
			{
				return currentPlayerHasValidPlacement();
			}
		}
		
		return false;
	}
	
	/**
	 * Checks to see if player can place a piece on the board
	 * @return True if player can put a piece on the board, false otherwise
	 */
	protected boolean currentPlayerHasValidPlacement()
	{
		for (HantoCoordinate coord : gameBoard.getAllAdjacentEmptyCoords())
		{
			if (isValidPlacement(coord))
			{
				return true;
			}
		}
		
		return false;
	}
}
