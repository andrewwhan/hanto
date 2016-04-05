package hanto.studentNBAH.common;

import java.util.Set;

import hanto.common.HantoCoordinate;

public class PieceValidatorImpl implements PieceValidator {

	private final Set<MoveValidatorStrategy> validators;
	
	PieceValidatorImpl(Set<MoveValidatorStrategy> validators){
		this.validators = validators;
	}
	
	@Override
	public boolean canMove(HantoCoordinate from, HantoCoordinate to, HantoGameBoard board) {
		for(MoveValidatorStrategy mvs:validators){
			if(mvs.canMove(from, to, board)){
				return true;
			}
		}
		
		return false;
	}

}
