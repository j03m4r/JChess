package Pieces;

import java.awt.Image;

import javax.swing.ImageIcon;

public class Piece {
    protected int x, y, piece = 0;
    protected boolean alive;
    protected Image image;

    private String[] pieceNamePaths = 
        {"Images/BlankSpace(Dark).png", "Images/BlankSpace(Light).png", "Images/Pawn(White).png", "Images/Knight(White).png",
        "Images/Bishop(White).png", "Images/Rook(White).png", "Images/Queen(White).png", "Images/King(White).png",
        "Images/Pawn(Black).png", "Images/Knight(Black).png", "Images/Bishop(Black).png", "Images/Rook(Black).png",
        "Images/Queen(Black).png", "Images/King(Black).png", "Images/SelectedSpace(Light).png", "Images/WhitePawn(Flipped).png",
        "Images/WhiteKnight(Flipped).png", "Images/WhiteBishop(Flipped).png", "Images/WhiteRook(Flipped).png", "Images/WhiteQueen(Flipped).png",
        "Images/WhiteKing(Flipped).png", "Images/BlackPawn(Upright).png", "Images/BlackKnight(Upright).png", "Images/BlackBishop(Upright).png",
        "Images/BlackRook(Upright).png", "Images/BlackQueen(Upright).png", "Images/BlackKing(Upright).png"};
    /*
     * Pawns: 2 (white), 8 (black)
     * Knights: 3 (white), 9 (black)
     * Bishops: 4 (white), 10 (black)
     * Rooks: 5 (white), 11 (black)
     * Queens: 6 (white), 12 (black)
     * Kings: 7 (white), 13 (black)
     * Selected Space: 14 (dark) 15 (light)
     * Pawns (Flipped): 16 (white), 22 (black)
     * Knights (Flipped): 17 (white), 23 (black)
     * Bishops (Flipped): 18 (white), 24 (black)
     * Rooks (Flipped): 19 (white), 25 (black)
     * Queens (Flipped): 20 (white), 26 (black)
     * Kings (Flipped): 21 (white), 27 (black)
     */
    
    public Piece(int piece)
    {
        alive = true;
        initPiece(piece);
    }

    private void initPiece(int piece)
    {
        SetPiece(piece);
        if (piece < 15)
        {
            LoadImage(pieceNamePaths[piece]);
        }
        else
        {
            LoadImage(pieceNamePaths[piece-1]);
        }
    }

    protected void LoadImage(String imageName)
    {
        ImageIcon ii = new ImageIcon(imageName);
        image = ii.getImage();
    }

    public void ChangeImage()
    {
        ImageIcon ii = new ImageIcon(pieceNamePaths[piece+14]);
        image = ii.getImage();
    }

    public void ChangeImageBack()
    {
        LoadImage(pieceNamePaths[piece-14]);
    }

    public Image GetImage()
    {
        return image;
    }

    public void SetX(int x)
    {
        this.x = x*75;
    }

    public void SetY(int y)
    {
        this.y = y*75;
    }

    public int GetX()
    {
        return x;
    }

    public int GetY()
    {
        return y;
    }

    public void SetPiece(int piece)
    {
        this.piece = piece;
    }

    public int GetPiece()
    {
        return piece;
    }

    public void SetAlive(boolean isAlive)
    {
        alive = isAlive;
    }

    public boolean IsAlive()
    {
        return alive;
    }

}
