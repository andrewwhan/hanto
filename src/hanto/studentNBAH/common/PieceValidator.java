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

import java.util.Set;

import hanto.common.HantoCoordinate;
import hanto.tournament.HantoMoveRecord;

/**
 * Interface for object that contains a particular piece's validators
 * Checks all of the validators it has to see if a move is valid
 * @author Nathan Bryant, Andrew Han
 *
 */
public interface PieceValidator {
	
	/**
	 * Checks all of the piece's validators to see if the move is valid
	 * @param from - The location of the piece to move
	 * @param to - The destination of the piece to move
	 * @param board - The current board of the game
	 * @return - True if it is a valid move, false otherwise
	 */
	boolean canMove(HantoCoordinate from, HantoCoordinate to, HantoGameBoard board);
	
	/**
	 * Checks all of the piece's validators to see if it has a valid move
	 * @param from - The location of the piece to move
	 * @param board - The current board of the game
	 * @return - True if the piece has a valid move, false otherwise
	 */
	boolean hasValidMove(HantoCoordinate from, HantoGameBoard board);
	
	/**
	 * Gets a list of all the valid moves that the piece validator has
	 * @param from - The coordinate that the piece is located at
	 * @param board - The current game board
	 * @return - The set of all valid moves for the piece
	 */
	Set<HantoMoveRecord> getValidMoves(HantoCoordinate from, HantoGameBoard board);
}
