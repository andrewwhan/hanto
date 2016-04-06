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

import hanto.common.HantoCoordinate;

/**
 * Interface for validators that check if a move is valid
 * Uses Strategy Pattern
 * @author Nathan Bryant, Andrew Han
 *
 */
public interface MoveValidatorStrategy {
	
	/**
	 * Checks to see if the given move given the current board is valid 
	 * @param from - The location of the piece to move
	 * @param to - The destination of the piece being moved
	 * @param board - The current board of the game
	 * @return - True if it can move, false otherwise
	 */
	boolean canMove(HantoCoordinate from, HantoCoordinate to, HantoGameBoard board);
}
