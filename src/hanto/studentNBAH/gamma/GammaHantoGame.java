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

package hanto.studentNBAH.gamma;

import static hanto.common.HantoPieceType.BUTTERFLY;
import static hanto.common.HantoPieceType.SPARROW;
import hanto.common.HantoCoordinate;
import hanto.common.HantoGameID;
import hanto.common.HantoPieceType;
import hanto.common.HantoPlayerColor;
import hanto.studentNBAH.common.BaseHantoGame;

/**
 * Gamma Hanto game version
 * Has all functionality of Beta Hanto, with the addition of walking
 * Can no longer place pieces next to opponent pieces
 * Game now goes to turn 20 (40 moves total)
 * @author Nathan Bryant, Andrew Han
 *
 */
public class GammaHantoGame extends BaseHantoGame{

	/**
	 * Constructor
	 * @param movesFirst - The color of the player that will make the first move
	 */
	public GammaHantoGame(HantoPlayerColor movesFirst){
		super(movesFirst, 40);
		currentGameID = HantoGameID.GAMMA_HANTO;
		pieceMax.put(BUTTERFLY, 1);
		pieceMax.put(SPARROW, 5);
		firstPlayer = movesFirst;
	}
	
	/**
	 * Returns false because resignation is not supported in Gamma
	 */
	protected boolean checkForResignation(HantoPieceType pieceType, HantoCoordinate source,
			HantoCoordinate destination){
		return false;
	}
}
