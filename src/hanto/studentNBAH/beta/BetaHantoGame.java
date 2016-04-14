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
import static hanto.common.HantoPlayerColor.BLUE;
import static hanto.common.HantoPlayerColor.RED;

import java.util.HashMap;
import java.util.Set;

import hanto.common.*;
import hanto.studentNBAH.common.BaseHantoGame;
import hanto.studentNBAH.common.HantoCoordinateImpl;
import hanto.studentNBAH.common.HantoGameBoard;
import hanto.studentNBAH.common.HantoPieceImpl;
import hanto.studentNBAH.common.HantoUtilities;

/**
 * <<Fill this in>>
 * @version Mar 16, 2016
 */
public class BetaHantoGame extends BaseHantoGame
{
	
	public BetaHantoGame(HantoPlayerColor firstPlayer){
		super(firstPlayer, 12);
		pieceMax.put(BUTTERFLY, 1);
		pieceMax.put(SPARROW, 5);
	}
	
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
}
