package BoardAndUI;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.Timer;

import Pieces.Piece;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class JChessBoard extends JPanel implements ActionListener 
{
    JTextArea textArea = new JTextArea();

    private int b_width = 600, b_height = 600;

    private Piece blackRook0 = new Piece(11);
    private Piece blackRook1 = new Piece(11);

    private Piece blackKnight0 = new Piece(9);
    private Piece blackKnight1 = new Piece(9);

    private Piece blackBishop0 = new Piece(10);
    private Piece blackBishop1 = new Piece(10);

    private Piece blackQueen = new Piece(12);
    private Piece blackKing = new Piece(13);

    private Piece[] blackPawns = 
        {new Piece(8), new Piece(8), new Piece(8), new Piece(8), 
        new Piece(8), new Piece(8), new Piece(8), new Piece(8)};

    private Piece whiteRook0 = new Piece(5);
    private Piece whiteRook1 = new Piece(5);

    private Piece whiteKnight0 = new Piece(3);
    private Piece whiteKnight1 = new Piece(3);

    private Piece whiteBishop0 = new Piece(4);
    private Piece whiteBishop1 = new Piece(4);

    private Piece whiteQueen = new Piece(6);
    private Piece whiteKing = new Piece(7);
    private Piece[] whitePawns =
        {new Piece(2), new Piece(2), new Piece(2), new Piece(2), 
        new Piece(2), new Piece(2), new Piece(2), new Piece(2)};
    
    private Piece[][] board = new Piece[8][8];
    private Piece[][] pieces = 
    {
        {blackRook0, blackKnight0, blackBishop0, blackQueen, blackKing, blackBishop1, blackKnight1, blackRook1},
        {blackPawns[0], blackPawns[1], blackPawns[2], blackPawns[3], blackPawns[4], blackPawns[5], blackPawns[6], blackPawns[7]},
        {null, null, null, null, null, null, null, null},
        {null, null, null, null, null, null, null, null},
        {null, null, null, null, null, null, null, null},
        {null, null, null, null, null, null, null, null},
        {whitePawns[0], whitePawns[1], whitePawns[2], whitePawns[3], whitePawns[4], whitePawns[5], whitePawns[6], whitePawns[7]},
        {whiteRook0, whiteKnight0, whiteBishop0, whiteQueen, whiteKing, whiteBishop1, whiteKnight1, whiteRook1}
    };
    private Piece[][] flippedPieces =
    {
        {null, null, null, null, null, null, null, null},
        {null, null, null, null, null, null, null, null},
        {null, null, null, null, null, null, null, null},
        {null, null, null, null, null, null, null, null},
        {null, null, null, null, null, null, null, null},
        {null, null, null, null, null, null, null, null},
        {null, null, null, null, null, null, null, null},
        {null, null, null, null, null, null, null, null}

    };


    private boolean inGame = true, isWhitesTurn = true, flip = true, whiteChecked = false, blackChecked = false;
    private Timer timer;
    private int delay = 10;
    private ArrayList<int[]> selectedTiles = new ArrayList<int[]>();
    private int[] selectedPiece = new int[2];

    public JChessBoard()
    {
        addMouseListener(new MAdapter());

        add(textArea);
        setBackground((Color.black));
        setFocusable(true);
        setPreferredSize(new Dimension(b_width, b_height));

        StartGame();
    }

    private void StartGame()
    {
        // Uploads the background checkard board. This will never change; pieces are overlayed.
        for (int i = 0; i < 8; i++)
        {
            for (int j = 0; j < 8; j++)
            {
                if ((i+j)%2==0)
                {
                    board[i][j] = new Piece(0);
                }
                else
                {
                    board[i][j] = new Piece(1);
                }
            }
        }

        SetPieceCoords();

        timer = new Timer(delay, this);
        timer.start();
    }

    // This method grabs coords based off the piece's position in the nested 'pieces' array.
    // The coords are also grabbed for the board
    private void SetPieceCoords()
    {
        for (int i = 0; i < 8; i++)
        {
            for (int j = 0; j < 8; j++)
            {
                board[i][j].SetX(j);
                board[i][j].SetY(i);
            }
        }

        for (int i = 0; i < 8; i++)
        {
            for (int j = 0; j < 8; j++)
            {
                if (pieces[i][j] != null)
                {
                    pieces[i][j].SetX(j);
                    pieces[i][j].SetY(i);
                }
            }
        }
    }

    // This method will highlight movement opportunities for the piece stored on the selected tile
    private void selectPiece(int x, int y)
    {
        //CheckChecked();
        //textArea.append(String.valueOf(whiteChecked));
        //textArea.append(String.valueOf(blackChecked));

        // These for loops is where the magic happens: the relative mouse positions is subtracted by the tile coordinates
        // the computation that results in a value >= 0 and < 75 is the tile to be selected and checked for movement options
        outerLoop:
        for (int i = 0; i < 8; i++)
        {
            for (int j = 0; j < 8; j++)
            {
                if ((((x-(75*j)) >= 0) && ((x-(75*j)) < 75)) && (((y-(75*i)) >= 0) && ((y-(75*i)) < 75)))
                {
                    if ((board[i][j].GetPiece() == 15 || board[i][j].GetPiece() == 14))
                    {
                        pieces[i][j] = pieces[selectedPiece[0]][selectedPiece[1]];
                        pieces[selectedPiece[0]][selectedPiece[1]] = null;
                        ClearSelected(selectedTiles);

                        if (isWhitesTurn)
                        {
                            isWhitesTurn = false;
                            FlipBoard();
                        }
                        else
                        {
                            isWhitesTurn = true;
                            FlipBoard();
                        }
                    }
                    else if (pieces[i][j] != null)
                    {
                        if ((isWhitesTurn && pieces[i][j].GetPiece() <= 7) || (!isWhitesTurn && pieces[i][j].GetPiece() > 7 && pieces[i][j].GetPiece() < 16))
                        {
                            ClearSelected(selectedTiles);

                            selectedPiece[0] = i;
                            selectedPiece[1] = j;
    
                            //textArea.append(String.valueOf(pieces[]));
                            for (int[] pos : GetPieceMovement(pieces[i][j].GetPiece(), i, j))
                            {
                                //textArea.append("asdfadf");
                                if (board[pos[0]][pos[1]].GetPiece() == 0)
                                {
                                    board[pos[0]][pos[1]] = new Piece(14);
                                }
                                else
                                {
                                    board[pos[0]][pos[1]] = new Piece(15);
                                }
                            }
                        }
                    }
                    else
                    {
                        ClearSelected(selectedTiles);
                    }
                    
                    selectedTiles = GetPieceMovement(pieces[i][j].GetPiece(), i, j);
                    break outerLoop;
                }
            }
        }
    }

    private void CheckChecked() 
    {
        int[] whiteKingCords = new int[2];//, blackKingCoords = new int[2];

        for (int i = 0; i < 8; i++)
        {
            for (int j = 0; j < 8; j++)
            {
                if (pieces[i][j].GetPiece() == 7)
                {
                    whiteKingCords[0] = i;
                    whiteKingCords[1] = j;
                }
            }
        }

        int row = whiteKingCords[0], column = whiteKingCords[1];
        //boolean limitCleared;
        //Checking for potential attacks on king
        for (int r = 1; r < 8; r++)
        {
            if (row-r >= 0)
            {
                if (pieces[row-r][column] != null)
                {
                    // Checks for rook/queen checks
                    if (pieces[row-r][column].GetPiece() == 11 || pieces[row-r][column].GetPiece() == 12)
                    {
                        whiteChecked = true;
                    }
                    break;
                }
            }
        } 
            /*
            // Checks for bishop/queen checks
            else if (Math.abs(r) == Math.abs(h))
            {
                if (pieces[row-r][column-h].GetPiece() == 10 || pieces[row-r][column-h].GetPiece() == 12)
                {
                    whiteChecked = true;
                    break outerLoop;
                }
                else
                {
                    break;
                }
            }
            */
        //Check separately for pawn checks
    }

    private void ClearSelected(ArrayList<int[]> selectedTiles) 
    {
        // This loop unselects the tiles
        for (int[] selectedTile : selectedTiles)
        {
            if (board[selectedTile[0]][selectedTile[1]].GetPiece() == 14)
            {
                board[selectedTile[0]][selectedTile[1]] = new Piece(0);
            }
            else if (board[selectedTile[0]][selectedTile[1]].GetPiece() == 15)
            {
                board[selectedTile[0]][selectedTile[1]] = new Piece(1);
            }
        }
    }

    public ArrayList<int[]> GetPieceMovement(int piece, int row, int column)
    {
        ArrayList<int[]> possibleMoves = new ArrayList<int[]>();

        switch (piece)
        {

            case 2:   
            case 8:
                if (row == 6)
                {
                    outerLoop:
                    for (int i = 1; i <= 2; i++)
                    {
                        if ((row - i >= 0))
                        {
                            if (pieces[row-i][column] == null)
                            {
                                possibleMoves.add(new int[] {row-i, column});
                            }
                            else
                            {
                                break outerLoop;
                            }
                        }
                    }
                    if (column - 1 >= 0)
                    {
                        if (pieces[row-1][column-1] != null)
                        {
                            if (isWhitesTurn)
                            {
                                if (pieces[row-1][column-1].GetPiece() > 7)
                                {
                                    possibleMoves.add(new int[] {row-1, column-1});
                                }
                            }
                            else
                            {
                                if (pieces[row-1][column-1].GetPiece() <= 7)
                                {
                                    possibleMoves.add(new int[] {row-1, column-1});
                                }
                            }
                        }
                    }
                    if (column + 1 < 8)
                    {
                        if (pieces[row-1][column+1] != null)
                        {
                            if (isWhitesTurn)
                            {
                                if (pieces[row-1][column+1].GetPiece() > 7)
                                {
                                    possibleMoves.add(new int[] {row-1, column+1});
                                }
                            }
                            else
                            {
                                if (pieces[row-1][column+1].GetPiece() <= 7)
                                {
                                    possibleMoves.add(new int[] {row-1, column+1});
                                }
                            }
                        }
                    }
                }
                else
                {
                    if (row - 1 >= 0)
                    {
                        if (pieces[row-1][column] == null)
                        {
                            possibleMoves.add(new int[] {row-1, column});
                        }
                        if (column - 1 >= 0)
                        {
                            if (pieces[row-1][column-1] != null)
                            {
                                if (isWhitesTurn)
                                {
                                    if (pieces[row-1][column-1].GetPiece() > 7)
                                    {
                                        possibleMoves.add(new int[] {row-1, column-1});
                                    }
                                }
                                else
                                {
                                    if (pieces[row-1][column-1].GetPiece() <= 7)
                                    {
                                        possibleMoves.add(new int[] {row-1, column-1});
                                    }
                                }
                            }
                        }
                        if (column + 1 < 8)
                        {
                            if (pieces[row-1][column+1] != null)
                            {
                                if (isWhitesTurn)
                                {
                                    if (pieces[row-1][column+1].GetPiece() > 7)
                                    {
                                        possibleMoves.add(new int[] {row-1, column+1});
                                    }
                                }
                                else
                                {
                                    if (pieces[row-1][column+1].GetPiece() <= 7)
                                    {
                                        possibleMoves.add(new int[] {row-1, column+1});
                                    }
                                }
                            }
                        }
                    }
                }; break;
            case 3:
            case 9:
                for (int i = -2; i < 3; i++)
                {
                    if ((row + i >= 0) && (row + i < 8))
                    {
                        for (int j = -2; j < 3; j++)
                        {
                            if ((column + j >= 0) && (column + j < 8))
                            {
                                if ((i == -2 && j == -1) || (i == -2 && j == 1))
                                {
                                    if (pieces[row+i][column+j] == null)
                                    {
                                        possibleMoves.add(new int[] {row+i, column+j});
                                    }
                                    else if (piece == 3)
                                    {
                                        if (pieces[row+i][column+j].GetPiece() > 7)
                                        {
                                            possibleMoves.add(new int[] {row+i, column+j});
                                        }
                                    }
                                    else if (piece == 9)
                                    {
                                        if (pieces[row+i][column+j].GetPiece() <= 7)
                                        {
                                            possibleMoves.add(new int[] {row+i, column+j});
                                        }
                                    }
                                }
                                else if ((i == -1 && j == -2) || (i == -1 && j == 2))
                                {
                                    if (pieces[row+i][column+j] == null)
                                    {
                                        possibleMoves.add(new int[] {row+i, column+j});
                                    }
                                    else if (piece == 3)
                                    {
                                        if (pieces[row+i][column+j].GetPiece() > 7)
                                        {
                                            possibleMoves.add(new int[] {row+i, column+j});
                                        }
                                    }
                                    else if (piece == 9)
                                    {
                                        if (pieces[row+i][column+j].GetPiece() <= 7)
                                        {
                                            possibleMoves.add(new int[] {row+i, column+j});
                                        }
                                    }
                                }
                                else if ((i == 1 && j == -2) || (i == 1 && j == 2))
                                {
                                    if (pieces[row+i][column+j] == null)
                                    {
                                        possibleMoves.add(new int[] {row+i, column+j});
                                    }
                                    else if (piece == 3)
                                    {
                                        if (pieces[row+i][column+j].GetPiece() > 7)
                                        {
                                            possibleMoves.add(new int[] {row+i, column+j});
                                        }
                                    }
                                    else if (piece == 9)
                                    {
                                        if (pieces[row+i][column+j].GetPiece() <= 7)
                                        {
                                            possibleMoves.add(new int[] {row+i, column+j});
                                        }
                                    }
                                }
                                else if ((i == 2 && j == -1) || (i == 2 && j == 1))
                                {
                                    if (pieces[row+i][column+j] == null)
                                    {
                                        possibleMoves.add(new int[] {row+i, column+j});
                                    }
                                    else if (piece == 3)
                                    {
                                        if (pieces[row+i][column+j].GetPiece() > 7)
                                        {
                                            possibleMoves.add(new int[] {row+i, column+j});
                                        }
                                    }
                                    else if (piece == 9)
                                    {
                                        if (pieces[row+i][column+j].GetPiece() <= 7)
                                        {
                                            possibleMoves.add(new int[] {row+i, column+j});
                                        }
                                    }
                                }
                            }
                        }
                    }
                }; break;
            case 4:
            case 10:
                possibleMoves = BishopMovement(row, column, piece); break;
            case 5:
            case 11:
                possibleMoves = RookMovement(row, column, piece); break;
            case 6:
            case 12:
                possibleMoves.addAll(BishopMovement(row, column, piece));
                possibleMoves.addAll(RookMovement(row, column, piece)); break;
            case 7:
            case 13:
                for (int i = -1; i < 2; i++)
                {
                    if ((row + i >= 0) && (row + i < 8))
                    {
                        for (int j = -1; j < 2; j++)
                        {
                            if ((column + j >= 0) && (column + j < 8))
                            {
                                if (i != 0 || j != 0)
                                {
                                    if (pieces[row+i][column+j] == null)
                                    {
                                        possibleMoves.add(new int[] {row+i, column+j});
                                    }
                                    else if (piece == 7)
                                    {
                                        if (pieces[row+i][column+j].GetPiece() > 7)
                                        {
                                            possibleMoves.add(new int[] {row+i, column+j});
                                        }
                                    }
                                    else if (piece == 13)
                                    {
                                        if (pieces[row+i][column+j].GetPiece() <= 7)
                                        {
                                            possibleMoves.add(new int[] {row+i, column+j});
                                        }
                                    }
                                }
                            }
                        }
                    }
                }; break;
            default: break; 
        }


        return possibleMoves;
    }

    private ArrayList<int[]> BishopMovement(int row, int column, int piece)
    {
        ArrayList<int[]> possibleMoves = new ArrayList<int[]>();

        for (int dir = 0; dir < 4; dir++)
        {
            outerLoop:
            for (int i = -1; i > -8; i--)
            {
                if (dir == 0)
                {
                    if ((row + i >= 0) && (row + i < 8))
                    {
                        for (int j = -1; j > -8; j--)
                        {
                            if ((column + j >= 0) && (column + j < 8))
                            {
                                if ((i == j))
                                {
                                    if (pieces[row+i][column+j] == null)
                                    {
                                        possibleMoves.add(new int[] {row+i, column+j});
                                    }
                                    else if (piece == 4 || piece == 6)
                                    {
                                        if (pieces[row+i][column+j].GetPiece() > 7)
                                        {
                                            possibleMoves.add(new int[] {row+i, column+j});
                                        }
                                        break outerLoop;
                                    }
                                    else if (piece == 10 || piece == 12)
                                    {
                                        if (pieces[row+i][column+j].GetPiece() <= 7)
                                        {
                                            possibleMoves.add(new int[] {row+i, column+j});
                                        }
                                        break outerLoop;
                                    }
                                    else
                                    {
                                        break outerLoop;
                                    }
                                }
                            }
                        }
                    }
                }
                else if (dir == 1)
                {
                    if ((row + i >= 0) && (row + i < 8))
                    {
                        for (int j = -1; j > -8; j--)
                        {
                            if ((column - j >= 0) && (column - j < 8))
                            {
                                if ((i == j))
                                {
                                    if (pieces[row+i][column-j] == null)
                                    {
                                        possibleMoves.add(new int[] {row+i, column-j});
                                    }
                                    else if (piece == 4 || piece == 6)
                                    {
                                        if (pieces[row+i][column-j].GetPiece() > 7)
                                        {
                                            possibleMoves.add(new int[] {row+i, column-j});
                                        }
                                        break outerLoop;
                                    }
                                    else if (piece == 10 || piece == 12)
                                    {
                                        if (pieces[row+i][column-j].GetPiece() <= 7)
                                        {
                                            possibleMoves.add(new int[] {row+i, column-j});
                                        }
                                        break outerLoop;
                                    }
                                    else
                                    {
                                        break outerLoop;
                                    }
                                }
                            }
                        }
                    }
                }
                else if (dir == 2)
                {
                    if ((row - i >= 0) && (row - i < 8))
                    {
                        for (int j = -1; j > -8; j--)
                        {
                            if ((column + j >= 0) && (column + j < 8))
                            {
                                if ((i == j))
                                {
                                    if (pieces[row-i][column+j] == null)
                                    {
                                        possibleMoves.add(new int[] {row-i, column+j});
                                    }
                                    else if (piece == 4 || piece == 6)
                                    {
                                        if (pieces[row-i][column+j].GetPiece() > 7)
                                        {
                                            possibleMoves.add(new int[] {row-i, column+j});
                                        }
                                        break outerLoop;
                                    }
                                    else if (piece == 10 || piece == 12)
                                    {
                                        if (pieces[row-i][column+j].GetPiece() <= 7)
                                        {
                                            possibleMoves.add(new int[] {row-i, column+j});
                                        }
                                        break outerLoop;
                                    }
                                    else
                                    {
                                        break outerLoop;
                                    }
                                }
                            }
                        }
                    }
                }
                else
                {
                    if ((row - i >= 0) && (row - i < 8))
                    {
                        for (int j = -1; j > -8; j--)
                        {
                            if ((column - j >= 0) && (column - j < 8))
                            {
                                if ((i == j))
                                {
                                    if (pieces[row-i][column-j] == null)
                                    {
                                        possibleMoves.add(new int[] {row-i, column-j});
                                    }
                                    else if (piece == 4 || piece == 6)
                                    {
                                        if (pieces[row-i][column-j].GetPiece() > 7)
                                        {
                                            possibleMoves.add(new int[] {row-i, column-j});
                                        }
                                        break outerLoop;
                                    }
                                    else if (piece == 10 || piece == 12)
                                    {
                                        if (pieces[row-i][column-j].GetPiece() <= 7)
                                        {
                                            possibleMoves.add(new int[] {row-i, column-j});
                                        }
                                        break outerLoop;
                                    }
                                    else
                                    {
                                        break outerLoop;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        return possibleMoves;
    }

    private ArrayList<int[]> RookMovement(int row, int column, int piece)
    {
        ArrayList<int[]> possibleMoves = new ArrayList<int[]>();

        for (int vert = 0; vert < 2; vert++)
        {
            outerLoop:
            for (int i = -1; i > -8; i--)
            {
                if (vert == 0)
                {
                    if ((row + i >= 0) && (row + i < 8))
                    {
                        if (pieces[row+i][column] == null)
                        {
                            possibleMoves.add(new int[] {row+i, column});
                        }
                        else if (piece == 5 || piece == 6)
                        {
                            if (pieces[row+i][column].GetPiece() > 7)
                            {
                                possibleMoves.add(new int[] {row+i, column});
                            }
                            break outerLoop;
                        }
                        else if (piece == 11 || piece == 12)
                        {
                            if (pieces[row+i][column].GetPiece() <= 7)
                            {
                                possibleMoves.add(new int[] {row+i, column});
                            }
                            break outerLoop;
                        }
                        else
                        {
                            break outerLoop;
                        }
                    }
                }
                else
                {
                    if ((row - i >= 0) && (row - i < 8))
                    {
                        if (pieces[row-i][column] == null)
                        {
                            possibleMoves.add(new int[] {row-i, column});
                        }
                        else if (piece == 5 || piece == 6)
                        {
                            if (pieces[row-i][column].GetPiece() > 7)
                            {
                                possibleMoves.add(new int[] {row-i, column});
                            }
                            break outerLoop;
                        }
                        else if (piece == 11 || piece == 12)
                        {
                            if (pieces[row-i][column].GetPiece() <= 7)
                            {
                                possibleMoves.add(new int[] {row-i, column});
                            }
                            break outerLoop;
                        }
                        else
                        {
                            break outerLoop;
                        }
                    }
                }
            }
        }
        for (int horiz = 0; horiz < 2; horiz++)
        {
            outerLoop:
            for (int j = -1; j > -8; j--)
            {
                if (horiz == 0)
                {
                    if ((column + j >= 0) && (column + j < 8))
                    {
                        if (pieces[row][column+j] == null)
                        {
                            possibleMoves.add(new int[] {row, column+j});
                        }
                        else if (piece == 5 || piece == 6)
                        {
                            if (pieces[row][column+j].GetPiece() > 7)
                            {
                                possibleMoves.add(new int[] {row, column+j});
                            }
                            break outerLoop;
                        }
                        else if (piece == 11 || piece == 12)
                        {
                            if (pieces[row][column+j].GetPiece() <= 7)
                            {
                                possibleMoves.add(new int[] {row, column+j});
                            }
                            break outerLoop;
                        }
                        else
                        {
                            break outerLoop;
                        }
                    }
                }
                else
                {
                    if ((column - j >= 0) && (column - j < 8))
                    {
                        if (pieces[row][column-j] == null)
                        {
                            possibleMoves.add(new int[] {row, column-j});
                        }
                        else if (piece == 5 || piece == 6)
                        {
                            if (pieces[row][column-j].GetPiece() > 7)
                            {
                                possibleMoves.add(new int[] {row, column-j});
                            }
                            break outerLoop;
                        }
                        else if (piece == 11 || piece == 12)
                        {
                            if (pieces[row][column-j].GetPiece() <= 7)
                            {
                                possibleMoves.add(new int[] {row, column-j});
                            }
                            break outerLoop;
                        }
                        else
                        {
                            break outerLoop;
                        }
                    }
                }
            }
        }
        return possibleMoves;
    }

    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);

        doDrawings(g);
    }

    private void doDrawings(Graphics g)
    {
        Graphics2D g2d = (Graphics2D) g;
        if (inGame)
        {
            for (int i = 0; i < 8; i++)
            {
                for (int j = 0; j < 8; j++)
                {
                    g2d.drawImage(board[i][j].GetImage(), board[i][j].GetX(), board[i][j].GetY(), this);

                    if (pieces[i][j] != null)
                    {
                        g2d.drawImage(pieces[i][j].GetImage(), pieces[i][j].GetX()+12,pieces[i][j].GetY()+12, this);
                    }
                }
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        SetPieceCoords();

        repaint();
    }

    private void FlipBoard() 
    {
        for (int i = 0; i < 8; i++)
        {
            for (int j = 0; j < 8; j++)
            {
                if (pieces[i][j] != null)
                {
                    
                    if (flip)
                    {
                        flippedPieces[7-i][7-j] = new Piece(pieces[i][j].GetPiece()+14);
                        flippedPieces[7-i][7-j].SetPiece(flippedPieces[7-i][7-j].GetPiece()-14);
                    }
                    else
                    {
                        flippedPieces[7-i][7-j] = new Piece(pieces[i][j].GetPiece());
                    }                    
                    pieces[i][j] = null;
                }
            }
        }
        for (int k = 0; k < 8; k++)
        {
            for (int m = 0; m < 8; m++)
            {
                pieces[k][m] = flippedPieces[k][m];
                flippedPieces[k][m] = null;
            }
        }

        if (flip)
        {
            flip = false;
        }
        else
        {
            flip = true;
        }
    }

    // TODO :: Checking / Checkmate system
    // TODO :: Castling
    // TODO :: Upgrading pawns
    // TODO :: Onpesant system

    private class MAdapter extends MouseAdapter
    {
        public void mouseClicked(MouseEvent e)
        {
            selectPiece(e.getX(), e.getY());
        }
    }
}
