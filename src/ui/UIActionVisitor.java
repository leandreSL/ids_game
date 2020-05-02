package ui;

import core.player.ClientData;
import javafx.application.Platform;
import javafx.scene.paint.Color;
import share.action.ActionVisitor;
import share.action.ChangeZone;
import share.action.PlayerMoves;
import share.action.UpdateBoard;
import share.action.UpdateTile;
import share.board.Tile;
import share.board.TileLand;

public class UIActionVisitor implements ActionVisitor {
	PlateauController ui;
	ClientData data;
	public UIActionVisitor (PlateauController ui_c,ClientData dt) {
		this.ui = ui_c;
		this.data = dt;
	}
	
	@Override
	public void visit(ChangeZone changeZoneMessage) {
		this.ui.setNode( changeZoneMessage.getNodeName());
		Platform.runLater(() -> {
			this.ui.rootPane.getChildren().clear();
			this.ui.adjustNodes();
			this.ui.initPlateau();
		});

		
	}

	@Override
	public void visit(PlayerMoves playerMovesMessage) {
		Platform.runLater(()->{
			if (playerMovesMessage.sayHi()){
				this.ui.score.setText("Score : " + playerMovesMessage.getScore());
				this.ui.message.setText(playerMovesMessage.getMessage());
			}
			for (int i=0; i<7;i++) {
				for (int j = 0; j < 7; j++) {

					if(i == playerMovesMessage.getSourceTile().getX() && j == playerMovesMessage.getSourceTile().getY()){
						this.ui.grille[j][i].setFill(this.ui.getColorNode());
					}
					if(i == playerMovesMessage.getDestinationTile().getX() && j == playerMovesMessage.getDestinationTile().getY()){
						System.out.println(this.data.getPlayer() + " == " + ((TileLand)playerMovesMessage.getDestinationTile()).getPlayer());
						if (this.data.getPlayer().equals(((TileLand)playerMovesMessage.getDestinationTile()).getPlayer())){
							this.ui.grille[j][i].setFill(this.ui.getColorPlayer());
						}else {
							this.ui.grille[j][i].setFill(this.ui.getColorOthersPlayers());
						}

					}

				}
			}
		});

		
	}

	@Override
	public void visit(UpdateBoard updateBoardMessage) {
		Tile [][] board = updateBoardMessage.getBoard().getTiles();
		Platform.runLater(()->{
			this.ui.initPlateau();
			for (int i=0; i<board.length;i++){
				for (int j=0; j<board.length;j++){
					if (board[j][i].getClass().getName() == "share.board.TileLand"){
						if (((TileLand)board[j][i]).getPlayer() == null){
							ui.grille[j][i].setFill(this.ui.getColorNode());
						}else if (((TileLand)board[j][i]).getPlayer().equals(this.data.getPlayer())){
							ui.grille[j][i].setFill(this.ui.getColorPlayer());
						}else{
							ui.grille[j][i].setFill(this.ui.getColorOthersPlayers());
						}
					}
					if(board[j][i].getClass().getName() == "share.board.TileWall"){
						ui.grille[j][i].setFill(Color.DARKGRAY);
					}
					if(board[j][i].getClass().getName() == "share.board.TileChangeZone"){
						ui.grille[j][i].setFill(Color.SANDYBROWN);
					}
				}
			}
		});

		
	}

	@Override
	public void visit(UpdateTile updateTileMessage) {

		updateTile(updateTileMessage.getTile());
	}

	public void updateTile(Tile t) {
		Platform.runLater(() -> {
			if (t.getClass().getName() == "share.board.TileLand") {
				if (((TileLand) t).getPlayer() == null) {
					ui.grille[t.getY()][t.getX()].setFill(this.ui.getColorNode());
				} else if (((TileLand) t).getPlayer().equals(this.data.getPlayer())) {
					ui.grille[t.getY()][t.getX()].setFill(this.ui.getColorPlayer());
				} else {
					ui.grille[t.getY()][t.getX()].setFill(this.ui.getColorOthersPlayers());
				}
			}
			if (t.getClass().getName() == "share.board.TileWall") {
				ui.grille[t.getY()][t.getX()].setFill(Color.DARKGRAY);
			}
			if (t.getClass().getName() == "share.board.TileChangeZone") {
				ui.grille[t.getY()][t.getX()].setFill(Color.SANDYBROWN);
			}


		});
	}

}
