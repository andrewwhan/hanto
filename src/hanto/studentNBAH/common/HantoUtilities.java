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

import java.util.HashSet;
import java.util.Set;

import hanto.common.*;

/**
 * HantoUtilities is a collection of helper functions that are used throughout the Hanto game code
 * @author Nathan Bryant, Andrew Han
 *
 */
public class HantoUtilities {

	/**
	 * Counts the number of pieces of the given type that the given player has and returns the count
	 * @param color - The color of the player to count pieces for
	 * @param type - The piece type to count
	 * @param pieces - The set of all the pieces of the specified player
	 * @return - The number of the given piece type that the given player has
	 */
	public static int countPlayerPiecesOfType(HantoPlayerColor color, HantoPieceType type, Set<HantoPiece> pieces){
		int count = 0;
		for(HantoPiece piece : pieces){
			if(piece.getColor().equals(color) && piece.getType().equals(type)){
				count++;
			}
		}
		return count;
	}

	/**
	 * Gets the adjacent coordinates of the given position
	 * @param pos - The position to get the adjacent pieces for
	 * @return - The set of all the adjacent coordinates of the given coordinate
	 */
	public static Set<HantoCoordinate> getAdjacentPositions(HantoCoordinate pos)
	{
		Set<HantoCoordinate> adjacentPositions = new HashSet<HantoCoordinate>();
		adjacentPositions.add(new HantoCoordinateImpl(pos.getX(), pos.getY() + 1));
		adjacentPositions.add(new HantoCoordinateImpl(pos.getX() + 1, pos.getY()));
		adjacentPositions.add(new HantoCoordinateImpl(pos.getX() + 1, pos.getY() - 1));
		adjacentPositions.add(new HantoCoordinateImpl(pos.getX(), pos.getY() - 1));
		adjacentPositions.add(new HantoCoordinateImpl(pos.getX() - 1, pos.getY()));
		adjacentPositions.add(new HantoCoordinateImpl(pos.getX() - 1, pos.getY() + 1));
		return adjacentPositions;
	}

	public static int getDistance(HantoCoordinate from, HantoCoordinate to){
		int normX = to.getX() - from.getX(); 
		int normY = to.getY() - from.getY();
		if((normX >= 0 && normY >= 0) || 
				(normX <= 0 && normY <= 0)){
			return Math.abs(normX) + Math.abs(normY);
		}
		else{
			int count = 0;
			while(normX != 0 && normY != 0){
				//X is positive, Y is negative
				if(normX > 0){
					normX -= 1;
					normY += 1;
				}
				//X is negative, Y is positive
				else{
					normX += 1;
					normY -= 1;
				}
				count++;
			}
			return count + Math.abs(normX) + Math.abs(normY);
		}
	}

	/**
	 * Returns the color that the given player is playing against
	 * @param color - The color of the player to get its opponent's color
	 * @return - The color of the given player's opponent
	 */
	public static HantoPlayerColor getOppositePlayerColor(HantoPlayerColor color)
	{
		if (color == HantoPlayerColor.BLUE)
		{
			return HantoPlayerColor.RED;
		}
		else
		{
			return HantoPlayerColor.BLUE;
		}
	}
}
