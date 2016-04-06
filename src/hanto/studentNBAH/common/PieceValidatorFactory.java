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

import hanto.common.HantoGameID;
import hanto.common.HantoPieceType;

/**
 * Factory class that generates PieceValidators based on the piece type and game version
 * @author Nathan Bryant, Andrew Han
 *
 */
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
	
	/**
	 * Generates a PieceValidator based on the game version and piece type
	 * @param type - The type of the piece to make a validator for
	 * @param version - The version of Hanto game
	 * @return a PieceValidator for the given piece type and version
	 */
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
