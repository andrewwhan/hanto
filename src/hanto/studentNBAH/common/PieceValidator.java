package hanto.studentNBAH.common;

import hanto.common.HantoCoordinate;

public interface PieceValidator {
	public boolean canMove(HantoCoordinate from, HantoCoordinate to, HantoGameBoard board);
}
