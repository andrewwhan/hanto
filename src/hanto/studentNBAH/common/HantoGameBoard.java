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

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import hanto.common.*;

/**
 * Stores the board state of the Hanto game
 * @author Nathan Bryant, Andrew Han
 *
 */
public class HantoGameBoard implements Serializable {

	private Map<HantoCoordinate, HantoPiece> board = new HashMap<HantoCoordinate, HantoPiece>();
	
	public HantoGameBoard() {
		
	}
	
	/**
	 * Copy constructor for HantoGameBoards
	 * @param source - The source object to copy from
	 */
	public HantoGameBoard(HantoGameBoard source){
		board = new HashMap<HantoCoordinate, HantoPiece>(source.board);
	}
	
	/**
	 * Constructor that takes the string representation of a game board and creates a new game board from it
	 * @param boardString - the string representation of a game board
	 */
	public HantoGameBoard(String boardString)
	{
		board = new HashMap<HantoCoordinate, HantoPiece>();
		
		if (boardString.equals(""))
		{
			return;
		}
		
		String[] initialStrings = boardString.split("&");
		for (String s : initialStrings)
		{
			if (s.equals(""))
			{
				continue;
			}			
			String[] split = s.split(";");		
			int coordX = Integer.parseInt(split[0]);
			int coordY = Integer.parseInt(split[1]);
			HantoPieceType pieceType = HantoPieceType.valueOf(split[2]);
			HantoPlayerColor playerColor = HantoPlayerColor.valueOf(split[3]);
			board.put(new HantoCoordinateImpl(coordX, coordY), new HantoPieceImpl(playerColor, pieceType));
		}
	}

	/**
	 * Returns the HantoPiece that is at the given coordinate on the board
	 * @param boardPos - The position to grab the piece from
	 * @return the HantoPiece at the given position
	 */
	public HantoPiece getPieceAt(HantoCoordinate boardPos)
	{
		if (board.containsKey(boardPos))
		{
			return board.get(boardPos);
		}
		else
		{
			return null;
		}
	}
	
	/**
	 * Places the given piece at the given coordinate on the game board
	 * This function assumes that it has been given a valid move
	 * @param boardPos - The coordinate to place the piece at
	 * @param pieceToPlace - The piece to place
	 */
	public void placePiece(HantoCoordinate boardPos, HantoPiece pieceToPlace)
	{
		board.put(boardPos, pieceToPlace);
	}
	
	/**
	 * Moves the piece at the given location to the given destination on the game board
	 * This function assumes that it has been given a valid move
	 * @param from - The location of the piece to move
	 * @param to - The destination of the piece
	 */
	public void movePiece(HantoCoordinate from, HantoCoordinate to)
	{
		HantoPiece pieceToMove = board.get(from);
		
		HantoPieceImpl copyPiece = new HantoPieceImpl(pieceToMove.getColor(), pieceToMove.getType());
		board.remove(from);
		board.put(to, copyPiece);
	}
	
	/**
	 * Returns the coordinate of the given HantoPiece on the board
	 * @param piece - The piece to retrieve the coordinate for
	 * @return - The coordinate that the piece is located at
	 */
	public HantoCoordinate getPosByPiece(HantoPiece piece)
	{
		for(HantoCoordinate hc : board.keySet()){
			if(board.get(hc).equals(piece)){
				return hc;
			}
		}
		return null;
	}
	
	/**
	 * @return - Retrieves the list of positions that are currently occupied by pieces
	 */
	public Set<HantoCoordinate> getListOfPiecePositions()
	{
		return board.keySet();
	}
	
	/**
	 * Gets all the pieces of the given player
	 * @param color - The color of the player to retrieve pieces for
	 * @return - The set of all the given player's pieces
	 */
	public Set<HantoPiece> getPiecesOfPlayer(HantoPlayerColor color)
	{
		Collection<HantoPiece> allPieces = board.values();
		Set<HantoPiece> playerPieces = new HashSet<HantoPiece>();
		for(HantoPiece piece : allPieces){
			if(piece.getColor().equals(color)){
				playerPieces.add(piece);
			}
		}
		return playerPieces;
	}
	
	/**
	 * Checks to see if the butterfly of the given player is surrounded
	 * @param color - The color of the butterfly to check
	 * @return - True if the given player's butterfly is surrounded, false otherwise
	 */
	public boolean checkButterflySurrounded(HantoPlayerColor color){
		HantoPiece butterfly = null;
		for(HantoPiece piece : getPiecesOfPlayer(color)){
			if(piece.getType().equals(HantoPieceType.BUTTERFLY)){
				butterfly = piece;
			}
		}
		if(butterfly == null){
			return false;
		}
		else{
			Set<HantoCoordinate> adjacentPos = HantoUtilities.getAdjacentPositions(getPosByPiece(butterfly));
			for(HantoCoordinate hc : adjacentPos){
				if(!board.keySet().contains(hc)){
					return false;
				}
			}
			return true;
		}
	}
	
	/**
	 * Checks to see if the pieces on the game board are in a contiguous formation
	 * @return - True if pieces are in contiguous formation, false otherwise
	 */
	public boolean isContiguousFormation()
	{
		Set<HantoCoordinate> occupiedPositions = board.keySet();
		HantoCoordinate[] occupiedPositionsArray = new HantoCoordinate[occupiedPositions.size()];
		occupiedPositions.toArray(occupiedPositionsArray);
		Set<HantoCoordinate> connectedPositions = visitAdjacent(new HashSet<HantoCoordinate>(), occupiedPositionsArray[0]);
		
		if (connectedPositions.size() == occupiedPositions.size())
		{
			return true;
		}
		
		return false;
	}
	
	private Set<HantoCoordinate> visitAdjacent(Set<HantoCoordinate> visited, HantoCoordinate position)
	{
		visited.add(position);
		
		for (HantoCoordinate coord:HantoUtilities.getAdjacentPositions(position))
		{
			if (getPieceAt(coord) != null && !visited.contains(coord))
			{
				visited = visitAdjacent(visited, coord);
			}
		}
		
		return visited;
	}
	
	/**
	 * Returns all spaces on the board that are empty and adjacent to other pieces
	 * @return A Set of spaces that are empty and adjacent to pieces
	 */
	public Set<HantoCoordinate> getAllAdjacentEmptyCoords()
	{
		Set<HantoCoordinate> piecePositions = getListOfPiecePositions();
		Set<HantoCoordinate> adjacentEmptyPositions = new HashSet<HantoCoordinate>();
		
		if (piecePositions.size() == 0)
		{
			adjacentEmptyPositions.add(new HantoCoordinateImpl(0, 0));
			return adjacentEmptyPositions;
		}
		
		for (HantoCoordinate coord : piecePositions)
		{
			Set<HantoCoordinate> adjacentPositions = HantoUtilities.getAdjacentPositions(coord);
			for (HantoCoordinate adjCoord : adjacentPositions)
			{
				if (getPieceAt(adjCoord) == null)
				{
					adjacentEmptyPositions.add(adjCoord);
				}
			}
		}
		return adjacentEmptyPositions;
	}
	
	@Override
	public String toString()
	{
		String boardString = "";
		Set<HantoCoordinate> piecePositions = getListOfPiecePositions();
		for (HantoCoordinate coord : piecePositions)
		{
			boardString += "&";
			boardString += coord.getX() + ";" + coord.getY();
			boardString += ";" + getPieceAt(coord).getType().toString().toUpperCase();
			boardString += ";" + getPieceAt(coord).getColor().toString().toUpperCase();
		}
		return boardString;
	}
}
