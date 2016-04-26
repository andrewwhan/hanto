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

package hanto.studentNBAH.beta;

import static hanto.common.HantoPieceType.BUTTERFLY;
import static hanto.common.HantoPieceType.SPARROW;
import hanto.common.*;
import hanto.studentNBAH.common.BaseHantoGame;

/**
 * <<Fill this in>>
 * @version Mar 16, 2016
 */
public class BetaHantoGame extends BaseHantoGame
{
	
	public BetaHantoGame(HantoPlayerColor firstPlayer){
		super(firstPlayer, 12);
		currentGameID = HantoGameID.BETA_HANTO;
		pieceMax.put(BUTTERFLY, 1);
		pieceMax.put(SPARROW, 5);
	}
	
	/**
	 * Returns true because we don't care about placing next to opponents in Beta
	 */
	@Override
	protected boolean checkAdjacentOpponentRule(HantoCoordinate placementPos, HantoCoordinate occupiedPos)
	{
		return true;
	}
	
	@Override
	protected boolean isValidMove(HantoCoordinate from, HantoCoordinate to, HantoPieceType pieceType) throws HantoException
	{
		throw new HantoException("Pieces can't move in beta Hanto");
	}
	
	/**
	 * Returns false because resignation is not supported in Beta
	 */
	protected boolean checkForResignation(HantoPieceType pieceType, HantoCoordinate source,
			HantoCoordinate destination){
		return false;
	}
}
