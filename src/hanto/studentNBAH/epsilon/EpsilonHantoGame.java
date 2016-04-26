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

package hanto.studentNBAH.epsilon;

import static hanto.common.HantoPieceType.BUTTERFLY;
import static hanto.common.HantoPieceType.CRAB;
import static hanto.common.HantoPieceType.SPARROW;

import hanto.common.HantoCoordinate;

import static hanto.common.HantoPieceType.HORSE;

import hanto.common.HantoGameID;
import hanto.common.HantoPieceType;
import hanto.common.HantoPlayerColor;
import hanto.common.HantoPrematureResignationException;
import hanto.studentNBAH.common.BaseHantoGame;

/**
 * Epsilon Hanto Game Version
 * Implement jumping pieces
 * Players can no longer resign if they still have valid moves
 * @author Nathan Bryant, Andrew Han
 *
 */
public class EpsilonHantoGame extends BaseHantoGame {

	public EpsilonHantoGame(HantoPlayerColor firstPlayer) {
		super(firstPlayer);
		currentGameID = HantoGameID.EPSILON_HANTO;
		pieceMax.put(BUTTERFLY, 1);
		pieceMax.put(SPARROW, 2);
		pieceMax.put(CRAB, 6);
		pieceMax.put(HORSE, 4);
	}
	
	@Override
	protected boolean checkForResignation(HantoPieceType pieceType, HantoCoordinate source,
			HantoCoordinate destination) throws HantoPrematureResignationException
	{
		if(pieceType == null && source == null && destination == null)
		{
			if (validMoveExists())
			{
				throw new HantoPrematureResignationException();
			}
			
			return true;
		}
		
		return false;
	}
}
