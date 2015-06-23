# quoridor

This is an implementation of the board game Quoridor.

The rules of the game are as follows:

    -The game is played on a 9x9 grid.
    -Each player controls a token, which starts on the middle of the top/bottom row.
    -Players also begin the game with ten walls to place.
    -The goal of the game is to move your token to the row opposite where it began.
    -On your turn, you can either move your token, or you can place a wall.
    -Tokens can move to adjacent squares - not diagonally (except see below).
    -Tokens cannot move through walls.
    -Tokens cannot move to squares occupied by the opponent's token.  
    -If your token is next to your opponent's token, you can jump over them to the next square.
    -If your token is next to your opponent's token, and jumping over them is blocked by a wall or the board's edge, you can move diagonally to land next to your opponent's token.
    -Walls are as long as two squares.  
    -Importantly, walls cannot be placed so that they totally block off a token from reaching the side it is aiming for.
    
Some information about the game will appear in the terminal after each move - whose turn it is, how many walls each side has left, and whether either player has won.
      

    
