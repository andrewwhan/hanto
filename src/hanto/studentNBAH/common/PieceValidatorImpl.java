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
 * Implementation of piece validator, checks to see if a piece can move by checking the given move with
 * its validators
 * @author Nathan Bryant, Andrew Han
 *
 */
public class PieceValidatorImpl implements PieceValidator {

	private final Set<MoveValidatorStrategy> validators;
	
	/**
	 * Constructor
	 * @param validators - The set of MoveValidatorStrategy that the piece has associated with it
	 */
	PieceValidatorImpl(Set<MoveValidatorStrategy> validators){
		this.validators = validators;
	}
	
	@Override
	public boolean canMove(HantoCoordinate from, HantoCoordinate to, HantoGameBoard board) {
		
		// If any of the move validators accept the move, then it is a valid move
		for(MoveValidatorStrategy mvs:validators){
			if(mvs.canMove(from, to, board)){
				return true;
			}
		}
		
		return false;
	}

	@Override
	public boolean hasValidMove(HantoCoordinate from, HantoGameBoard board)
	{
		Set<HantoCoordinate> adjEmptyCoords = board.getAllAdjacentEmptyCoords();
		for (HantoCoordinate coord : adjEmptyCoords)
		{
			for(MoveValidatorStrategy mvs:validators){
				if(mvs.canMove(from, coord, board)){
					return true;
				}
			}
		}
		return false;
	}
}
