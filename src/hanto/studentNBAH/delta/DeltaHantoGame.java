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

package hanto.studentNBAH.delta;

import static hanto.common.HantoPieceType.BUTTERFLY;
import static hanto.common.HantoPieceType.SPARROW;

import hanto.common.HantoGameID;

import static hanto.common.HantoPieceType.CRAB;

import hanto.common.HantoPlayerColor;
import hanto.studentNBAH.common.BaseHantoGame;

/**
 * Delta Hanto game version
 * Has all functionality of Gamma Hanto, with the addition of flying
 * Game now has no max turn limit
 * Players can now resign
 * @author Nathan Bryant, Andrew Han
 *
 */
public class DeltaHantoGame extends BaseHantoGame {

	public DeltaHantoGame(HantoPlayerColor firstPlayer) {
		super(firstPlayer);
		currentGameID = HantoGameID.DELTA_HANTO;
		pieceMax.put(BUTTERFLY, 1);
		pieceMax.put(SPARROW, 4);
		pieceMax.put(CRAB, 4);
	}

}
