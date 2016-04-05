package hanto.studentNBAH.common;

import java.util.HashSet;
import java.util.Set;

import hanto.common.HantoGameID;
import hanto.common.HantoPieceType;
import hanto.studentNBAH.HantoGameFactory;

public class PieceValidatorFactory {
private static final PieceValidatorFactory instance = new PieceValidatorFactory();
	
	/**
	 * Default private descriptor.
	 */
	private PieceValidatorFactory()
	{
		// Empty, but the private constructor is necessary for the singleton.
	}

	/**
	 * @return the instance
	 */
	public static PieceValidatorFactory getInstance()
	{
		return instance;
	}
	
	public PieceValidator makePieceValidator(HantoPieceType type, HantoGameID version){
		PieceValidator newImpl = null;
		switch(version){
			case GAMMA_HANTO:
				newImpl = new PieceValidatorImpl(gammaFactory(type));
				break;
			default:
				break;
		}
		
		return newImpl;
	}
	
	private Set<MoveValidatorStrategy> gammaFactory(HantoPieceType type){
		Set<MoveValidatorStrategy> validators = new HashSet<MoveValidatorStrategy>();
		switch(type){
			case BUTTERFLY:
				validators.add(new WalkValidator());
				break;
			case SPARROW:
				validators.add(new WalkValidator());
				break;
		}
		return validators;
	}
}
