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

/**
 * Validator that checks to see if the given move is valid walk move
 * @author Nathan Bryant, Andrew Han
 *
 */
public class WalkValidator implements MoveValidatorStrategy{
	
	
	@Override
	public boolean canMove(HantoCoordinate from, HantoCoordinate to, HantoGameBoard board) {
		
		if (board.getPieceAt(to) != null)
		{
			return false;
		}
		
		if (HantoUtilities.getDistance(from, to) != 1)
		{
			return false;
		}
		
		if (!canSlideToDest(from, to, board))
		{
			return false;
		}
		
		// Fake moving the piece to check for contiguity after the move
		board.movePiece(from, to);
		
		boolean isContiguous;
		
		if (board.isContiguousFormation())
		{
			isContiguous = true;
		}
		else 
		{
			isContiguous = false;
		}
		
		// Move the piece back to its original location before exiting
		board.movePiece(to, from);
		
		return isContiguous;
	}

	/**
	 * Checks to see if the piece being moved can slide to its destination
	 * @param from - The location to move the piece from
	 * @param to - The location to move the piece to
	 * @param board - The game board
	 * @return - True if the piece can slide to the location, false otherwise
	 */
	private boolean canSlideToDest(HantoCoordinate from, HantoCoordinate to, HantoGameBoard board)
	{
		Set<HantoCoordinate> fromAdjacencies = HantoUtilities.getAdjacentPositions(from);
		Set<HantoCoordinate> toAdjacencies = HantoUtilities.getAdjacentPositions(to);
		fromAdjacencies.retainAll(toAdjacencies);

		for(HantoCoordinate coord:fromAdjacencies)
		{
			if (board.getPieceAt(coord) == null)
			{
				return true;
			}
		}
		
		return false;
	}
}
