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
 * Creates a validator for jumping
 * @author Nathan Bryant, Andrew Han
 *
 */
public class JumpValidator implements MoveValidatorStrategy {

	@Override
	public boolean canMove(HantoCoordinate from, HantoCoordinate to, HantoGameBoard board) {
		HantoCoordinate jumpDirection = lineDirection(from, to);
		if(jumpDirection == null){
			System.out.println("Not straight line");
			return false;
		}
		if (board.getPieceAt(to) != null)
		{
			System.out.println("Space not empty");

			return false;
		}
		if(HantoUtilities.getDistance(from, to) < 2){
			return false;
		}
		System.out.println(jumpDirection.getX() + " , " + jumpDirection.getY());
		HantoCoordinate alongLine = new HantoCoordinateImpl(from.getX() + jumpDirection.getX(),
				from.getY() + jumpDirection.getY());
		while(!alongLine.equals(to)){
			System.out.println("loop");
			if(board.getPieceAt(alongLine) == null){
				System.out.println("Gap in jump");

				return false;
			}
			alongLine = new HantoCoordinateImpl(alongLine.getX() + jumpDirection.getX(),
					alongLine.getY() + jumpDirection.getY());

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
	 * Determines the straight-line direction between two coordinates
	 * @param from The source of the straight line
	 * @param to The destination of the straight line
	 * @return A single coordinate indicating the direction of the straight line, null if not a straight line
	 */
	public HantoCoordinate lineDirection(HantoCoordinate from, HantoCoordinate to) {
		int xDiff = to.getX() - from.getX();
		int yDiff = to.getY() - from.getY();
		if(xDiff == 0 || yDiff == 0 || xDiff + yDiff == 0){
			int xDir = xDiff == 0 ? 0 : xDiff/Math.abs(xDiff);
			int yDir = yDiff == 0 ? 0 : yDiff/Math.abs(yDiff);
			HantoCoordinate direction = new HantoCoordinateImpl(xDir, yDir);
			return direction;
		}
		return null;
	}



}
