/*******************************************************************************
 * This files was developed for CS4233: Object-Oriented Analysis & Design.
 * The course was taken at Worcester Polytechnic Institute.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package hanto.studentNBAH.tournament;

import java.util.HashSet;
import java.util.Set;

import hanto.common.*;
import hanto.studentNBAH.common.HantoGameBoard;
import hanto.studentNBAH.common.HantoPieceImpl;
import hanto.studentNBAH.common.HantoUtilities;
import hanto.studentNBAH.epsilon.EpsilonHantoGame;
import hanto.tournament.*;

/**
 * Description
 * @version Oct 13, 2014
 */
public class HantoPlayer implements HantoGamePlayer
{

	EpsilonHantoGame hantoGame;
	HantoPlayerColor myColor;
	
	/*
	 * @see hanto.tournament.HantoGamePlayer#startGame(hanto.common.HantoGameID, hanto.common.HantoPlayerColor, boolean)
	 */
	@Override
	public void startGame(HantoGameID version, HantoPlayerColor myColor,
			boolean doIMoveFirst)
	{
		this.myColor = myColor;
		if (doIMoveFirst) {
			hantoGame = new EpsilonHantoGame(myColor);
		}
		else {
			hantoGame = new EpsilonHantoGame(HantoUtilities.getOppositePlayerColor(myColor));
		}
	}

	/*
	 * @see hanto.tournament.HantoGamePlayer#makeMove(hanto.tournament.HantoMoveRecord)
	 */
	@Override
	public HantoMoveRecord makeMove(HantoMoveRecord opponentsMove)
	{
		// Register the previous opponent move in the game
		if (opponentsMove != null)
		{
			try {
				hantoGame.makeMove(opponentsMove.getPiece(), opponentsMove.getFrom(), opponentsMove.getTo());
			}
			catch (HantoException e) {
				System.out.println("Failed to make opponent's move");
				return null;
			}
		}
		
		Set<HantoMoveRecord> allPossibleMoves = hantoGame.getAllPossibleMoves();
		
		if (allPossibleMoves.size() == 0)
		{
			return new HantoMoveRecord(null, null, null);
		}
		else 
		{
			return chooseBestMove(allPossibleMoves);
		}
	}

	public void setCurrentGameState(EpsilonHantoGame currentGame)
	{
		hantoGame = currentGame;
	}
	
	/**
	 * Chooses the best move for the player
	 * Randomly chooses a move from all the possible moves
	 * Eliminates any moves that would cause the player to lose
	 * @param allPossibleMoves - All of the possible moves that the player has
	 * @return - The HantoMoveRecord that it has chosen for its turn
	 */
	public HantoMoveRecord chooseBestMove(Set<HantoMoveRecord> allPossibleMoves)
	{	
		Set<HantoMoveRecord> goodMoves = new HashSet<HantoMoveRecord>();
		
		for (HantoMoveRecord possibleMove : allPossibleMoves)
		{
			HantoGameBoard boardCopy = new HantoGameBoard(hantoGame.getPrintableBoard());
			if (possibleMove.getFrom() == null)
			{
				boardCopy.placePiece(possibleMove.getTo(), new HantoPieceImpl(myColor, possibleMove.getPiece()));
				if (!boardCopy.checkButterflySurrounded(myColor))
				{
					goodMoves.add(possibleMove);
				}
			}
			else
			{
				boardCopy.movePiece(possibleMove.getFrom(), possibleMove.getTo());
				if (!boardCopy.checkButterflySurrounded(myColor))
				{
					goodMoves.add(possibleMove);
				}
			}
		}
		
		if (goodMoves.size() == 0)
		{
			goodMoves = allPossibleMoves;
		}
		
		int randomIndex = (int) Math.floor(Math.random() * (goodMoves.size() - 1));
		return (HantoMoveRecord) goodMoves.toArray()[randomIndex];
	}
}
