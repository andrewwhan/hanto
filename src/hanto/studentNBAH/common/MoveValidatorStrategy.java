package hanto.studentNBAH.common;

import hanto.common.HantoCoordinate;

public interface MoveValidatorStrategy {
	public boolean canMove(HantoCoordinate from, HantoCoordinate to, HantoGameBoard board);
}
