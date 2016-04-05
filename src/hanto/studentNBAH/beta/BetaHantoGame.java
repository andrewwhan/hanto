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

package hanto.studentNBAH.beta;

import static hanto.common.HantoPieceType.BUTTERFLY;
import static hanto.common.HantoPieceType.SPARROW;
import static hanto.common.HantoPlayerColor.BLUE;
import static hanto.common.HantoPlayerColor.RED;

import java.util.HashMap;
import java.util.Set;

import hanto.common.*;
import hanto.studentNBAH.common.HantoCoordinateImpl;
import hanto.studentNBAH.common.HantoGameBoard;
import hanto.studentNBAH.common.HantoPieceImpl;
import hanto.studentNBAH.common.HantoUtilities;

/**
 * <<Fill this in>>
 * @version Mar 16, 2016
 */
public class BetaHantoGame implements HantoGame
{

	private HantoCoordinateImpl blueButterflyHex = null, redButterflyHex = null;
	private final HantoPiece blueButterfly = new HantoPieceImpl(BLUE, BUTTERFLY);
	private final HantoPiece redButterfly = new HantoPieceImpl(RED, BUTTERFLY);
	private boolean gameOver = false;
	private int turnNumber = 1;
	private HashMap<HantoPieceType, Integer> pieceMax = new HashMap<HantoPieceType, Integer>();

	private HantoGameBoard gameBoard = new HantoGameBoard();
	
	public BetaHantoGame(){
		pieceMax.put(BUTTERFLY, 1);
		pieceMax.put(SPARROW, 5);
	}
	
	/*
	 * @see hanto.common.HantoGame#makeMove(hanto.common.HantoPieceType, hanto.common.HantoCoordinate, hanto.common.HantoCoordinate)
	 */
	@Override
	public MoveResult makeMove(HantoPieceType pieceType, HantoCoordinate from,
			HantoCoordinate to) throws HantoException
	{
		if (gameOver) 
		{
			throw new HantoException("You cannot move after the game is finished");
		}
		
		final HantoCoordinateImpl destination = new HantoCoordinateImpl(to);
		
		if (!isValidPlacement(destination))
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
		
		HantoPieceImpl newGamePiece = new HantoPieceImpl(currentPlayer, pieceType);
		
		gameBoard.placePiece(destination, newGamePiece);
		
		if(gameBoard.checkButterflySurrounded(RED)){
			gameOver = true;
			return MoveResult.BLUE_WINS;
		}
		
		if(gameBoard.checkButterflySurrounded(BLUE)){
			gameOver = true;
			return MoveResult.RED_WINS;
		}
		
		turnNumber++;
		if(turnNumber == 13){
			gameOver = true;
			return MoveResult.DRAW;
		}
		return MoveResult.OK;
	}

	/*
	 * @see hanto.common.HantoGame#getPieceAt(hanto.common.HantoCoordinate)
	 */
	@Override
	public HantoPiece getPieceAt(HantoCoordinate where)
	{
		HantoCoordinateImpl whereImpl = new HantoCoordinateImpl(where);
		return gameBoard.getPieceAt(whereImpl);
	}

	/*
	 * @see hanto.common.HantoGame#getPrintableBoard()
	 */
	@Override
	public String getPrintableBoard()
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	private HantoPlayerColor getCurrentPlayerColor()
	{
		if (turnNumber % 2 == 0)
		{
			return HantoPlayerColor.RED;
		}
		else 
		{
			return HantoPlayerColor.BLUE;
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
		
		Set<HantoCoordinate> adjacentPositions = HantoUtilities.getAdjacentPositions(placementPos);
		for(HantoCoordinate occupiedPos : occupiedPositions)
		{
			for(HantoCoordinate adjacentPos : adjacentPositions)
			{
				if (occupiedPos.equals(adjacentPos))
				{
					return true;
				}
			}
		}
		return false;
	}
	
	private boolean isValidPiece(HantoPlayerColor color, HantoPieceType type){
		Set<HantoPiece> pieces = gameBoard.getPiecesOfPlayer(color);
		int currentPiecesOfType = HantoUtilities.countPlayerPiecesOfType(color, type, pieces);
		return currentPiecesOfType < pieceMax.get(type);
	}
}
