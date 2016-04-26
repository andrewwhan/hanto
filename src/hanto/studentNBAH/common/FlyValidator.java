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
 * Creates a validator for flying
 * @author Nathan Bryant, Andrew Han
 *
 */
public class FlyValidator implements MoveValidatorStrategy {

	int maxFlyDistance;
	
	public FlyValidator()
	{
		maxFlyDistance = Integer.MAX_VALUE;
	}
	
	/**
	 * Instantiates a fly validator with a maximum fly distance
	 * @param maxFlyDistance The max distance this piece can fly
	 */
	public FlyValidator(int maxFlyDistance)
	{
		this.maxFlyDistance = maxFlyDistance;
	}
	
	@Override
	public boolean canMove(HantoCoordinate from, HantoCoordinate to, HantoGameBoard board) {
		if (board.getPieceAt(to) != null)
		{
			return false;
		}
		
		if (HantoUtilities.getDistance(from, to) > maxFlyDistance)
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

}
