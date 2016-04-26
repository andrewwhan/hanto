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

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import hanto.common.HantoCoordinate;

/**
 * Validator that checks to see if the given move is valid walk move
 * @author Nathan Bryant, Andrew Han
 *
 */
public class WalkValidator implements MoveValidatorStrategy{
	
	int maxWalkDistance;
	
	public WalkValidator() {
		maxWalkDistance = 1;
	}
	
	/**
	 * Creates a walk validator with the specified maximum walking distance
	 * @param maxWalkDistance - The max distance a piece with this walk validator can walk
	 */
	public WalkValidator(int maxWalkDistance) {
		this.maxWalkDistance = maxWalkDistance;
	}

	@Override
	public boolean canMove(HantoCoordinate from, HantoCoordinate to, HantoGameBoard board) {
		if (board.getPieceAt(to) != null)
		{
			return false;
		}
	
		List<HantoCoordinate> currentPath = new LinkedList<HantoCoordinate>();
		return findPath(from, to, new HantoGameBoard(board), currentPath);
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
		if (board.getPieceAt(to) != null)
		{
			return false;
		}
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
	
	private boolean findPath(HantoCoordinate from, HantoCoordinate to, HantoGameBoard board,
			List<HantoCoordinate> path)
	{
		if(from.equals(to)){
			path.add(to);
			return true;
		}
		if(path.contains(from)){
			return false;
		}
		if(path.size() >= maxWalkDistance){
			return false;
		}
		if (HantoUtilities.getDistance(from, to) + path.size() > maxWalkDistance)
		{
			return false;
		}
		path.add(from);
		Set<HantoCoordinate> fromAdjacencies = HantoUtilities.getAdjacentPositions(from);
		for(HantoCoordinate coord:fromAdjacencies){
			if(canSlideToDest(from, coord, board)){
				
				// Fake moving the piece to check for contiguity after the move
				board.movePiece(from, coord);
				
				if (!board.isContiguousFormation())
				{
					board.movePiece(coord, from);
					continue;
				}
				
				// Move the piece back to its original location before exiting				
				
				if(findPath(coord, to, board, path)){
					board.movePiece(coord, from);
					return true;
				}
				board.movePiece(coord, from);
			}
		}
		path.remove(from);
		return false;
	}
}
