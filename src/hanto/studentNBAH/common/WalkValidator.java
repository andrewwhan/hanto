package hanto.studentNBAH.common;

import java.util.Set;

import hanto.common.HantoCoordinate;

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
		
		board.movePiece(to, from);
		
		return isContiguous;
	}

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
