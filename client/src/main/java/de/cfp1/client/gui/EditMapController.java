package de.cfp1.client.gui;

import de.cfp1.client.Client;
import de.cfp1.server.entities.Map;
import de.cfp1.server.game.BoardTheme;
import de.cfp1.server.game.GameBoardHelper;
import de.cfp1.server.game.GameTeamHelper;
import de.cfp1.server.game.map.*;
import de.cfp1.server.game.state.GameState;
import de.cfp1.server.game.state.Piece;
import de.cfp1.server.game.state.Team;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author virgil.baclanov, juan.steppacher, gabriel.victor.arthur.himmelein
 */

public class EditMapController implements Stageable, Initializable {

  private Stage stage;

  private final SceneController sceneController = new SceneController();

  @FXML
  TextField mapName, mapColumns, mapRows, mapTotalGameTime, mapMoveTime;

  @FXML
  TextField flagsCount, blocksCount, pawnCount, knightCount, bishopCount, rookCount, queenCount, kingCount;

  @FXML
  ToggleGroup playersNumber, placementType;
  @FXML
  RadioButton playersNumber2, playersNumber3, playersNumber4;
  @FXML
  RadioButton placementTypeSymmetrical, placementTypeDefensive, placementTypeSpacedOut;
  @FXML
  ToggleButton mapPublicToggle;
  @FXML
  GridPane boardPreview;

  @FXML
  Label mapSizeExceptionLabel, playersNumberExceptionLabel, gameTimeExceptionLabel, moveTimeExceptionLabel, placementExceptionLabel, flagsExceptionLabel, blocksExceptionLabel, pawnExceptionLabel, knightExceptionLabel, bishopExceptionLabel, rookExceptionLabel, queenExceptionLabel, kingExceptionLabel;

  private Map currentMap = null;
  private boolean isNewMap = true;

  /**
   * This method is called after the scene is loaded and initializes the map details and the map
   * preview. It comes from the Initializable interface.
   *
   * @param location  the location
   * @param resources the resources
   * @author virgil.baclanov, gabriel.victor.arthur.himmelein
   */
  @Override
  public void initialize(URL location, ResourceBundle resources) {
    Platform.runLater(() -> {
      try {
        if (this.currentMap != null) {
          this.isNewMap = false;
          populateMapDetails();
        }
        displayMapPreview();
      } catch (NullPointerException e) {
        System.out.println("Map not found or properties not found");
      } catch (NumberFormatException e) {
        System.out.println("Please enter a valid number");
      }
    });
  }

  /**
   * If there is a map, import the current map details into the fields
   *
   * @author virgil.baclanov, gabriel.victor.arthur.himmelein
   */
  public void populateMapDetails() {
    mapName.setText(this.currentMap.getName());
    mapColumns.setText(String.valueOf(this.currentMap.getMapTemplate().getGridSize()[1]));
    mapRows.setText(String.valueOf(this.currentMap.getMapTemplate().getGridSize()[0]));
    mapTotalGameTime.setText(
        String.valueOf((this.currentMap.getMapTemplate().getTotalTimeLimitInSeconds() / 60)));
    mapMoveTime.setText(
        String.valueOf(this.currentMap.getMapTemplate().getMoveTimeLimitInSeconds()));
    flagsCount.setText(String.valueOf(this.currentMap.getMapTemplate().getFlags()));
    blocksCount.setText(String.valueOf(this.currentMap.getMapTemplate().getBlocks()));
    pawnCount.setText(String.valueOf(this.currentMap.getMapTemplate().getPieces()[0].getCount()));
    knightCount.setText(String.valueOf(this.currentMap.getMapTemplate().getPieces()[1].getCount()));
    bishopCount.setText(String.valueOf(this.currentMap.getMapTemplate().getPieces()[2].getCount()));
    rookCount.setText(String.valueOf(this.currentMap.getMapTemplate().getPieces()[3].getCount()));
    queenCount.setText(String.valueOf(this.currentMap.getMapTemplate().getPieces()[4].getCount()));
    kingCount.setText(String.valueOf(this.currentMap.getMapTemplate().getPieces()[5].getCount()));

    switch (this.currentMap.getMapTemplate().getTeams()) {
      case 2:
        playersNumber.selectToggle(playersNumber2);
        break;
      case 3:
        playersNumber.selectToggle(playersNumber3);
        break;
      case 4:
        playersNumber.selectToggle(playersNumber4);
        break;
    }

    switch (this.currentMap.getMapTemplate().getPlacement()) {
      case symmetrical:
        placementType.selectToggle(placementTypeSymmetrical);
        break;
      case defensive:
        placementType.selectToggle(placementTypeDefensive);
        break;
      case spaced_out:
        placementType.selectToggle(placementTypeSpacedOut);
        break;
    }

    if (this.currentMap.isPublic()) {
      mapPublicToggle.setSelected(true);
      mapPublicToggle.setText("Public map: YES");
    } else {
      mapPublicToggle.setSelected(false);
      mapPublicToggle.setText("Public map: NO");
    }
  }

  /**
   * Method to display the map preview based on the current map configuration, visually
   *
   * @author virgil.baclanov, gabriel.victor.arthur.himmelein
   */
  public void displayMapPreview() {
    if (this.currentMap == null) {
      this.currentMap = new Map();
    }
    GameBoardHelper gameBoardHelper = new GameBoardHelper();
    System.out.println("Setting map template...");
    this.currentMap.setMapTemplate(getMapTemplateFromFields());
    System.out.println("Map template set.");
    updateMapPreview(gameBoardHelper);

    //listeners with handlers and visual tips to input only valid values
    mapRows.setOnKeyPressed(event -> {
      if (event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.TAB) {
        this.sceneController.playAudio("selectMenuSound.wav");

        int prevRows = this.currentMap.getMapTemplate().getGridSize()[0];
        try {
          if ((Integer.parseInt(mapRows.getText()) < 6) || (Integer.parseInt(mapRows.getText())
              > 20)) {
            mapRows.setText(String.valueOf(prevRows));
            mapSizeExceptionLabel.setVisible(true);
            mapSizeExceptionLabel.setText("Between 6 and 20 rows please");
            throw new IndexOutOfBoundsException("GridState too small.");
          } else {
            this.currentMap.getMapTemplate().setGridSize(
                new int[]{Integer.parseInt(mapRows.getText()),
                    this.currentMap.getMapTemplate().getGridSize()[1]});
            boolean isEnoughPlace = isEnoughPlace();
            if (!isEnoughPlace) {
              //save the previous value back in the template
              this.currentMap.getMapTemplate().setGridSize(
                  new int[]{prevRows, this.currentMap.getMapTemplate().getGridSize()[1]});
              mapRows.setText(String.valueOf(prevRows));
              mapSizeExceptionLabel.setVisible(true);
              mapSizeExceptionLabel.setText("Not enough place for pieces.");
              throw new IndexOutOfBoundsException("Not enough place for pieces");
            }
          }
          updateMapPreview(gameBoardHelper);
          mapSizeExceptionLabel.setVisible(false);
        } catch (NumberFormatException e) {
          this.currentMap.getMapTemplate()
              .setGridSize(new int[]{prevRows, this.currentMap.getMapTemplate().getGridSize()[1]});
          mapRows.setText(String.valueOf(prevRows));
          mapSizeExceptionLabel.setVisible(true);
          mapSizeExceptionLabel.setText("Please enter a valid number");
        } catch (IndexOutOfBoundsException e) {
          this.currentMap.getMapTemplate()
              .setGridSize(new int[]{prevRows, this.currentMap.getMapTemplate().getGridSize()[1]});
          mapRows.setText(String.valueOf(prevRows));
          mapSizeExceptionLabel.setVisible(true);
          mapSizeExceptionLabel.setText("Increase grid size until < 20");
        }
      }
    });

    mapColumns.setOnKeyPressed(event -> {
      if (event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.TAB) {
        this.sceneController.playAudio("selectMenuSound.wav");

        int prevColumns = this.currentMap.getMapTemplate().getGridSize()[1];
        try {
          if ((Integer.parseInt(mapColumns.getText()) < 6) || (
              Integer.parseInt(mapColumns.getText()) > 20)) {
            mapColumns.setText(String.valueOf(prevColumns));
            mapSizeExceptionLabel.setVisible(true);
            mapSizeExceptionLabel.setText("Between 6 and 20 columns please");
            throw new IndexOutOfBoundsException("GridState too small.");
          } else {
            this.currentMap.getMapTemplate().setGridSize(
                new int[]{currentMap.getMapTemplate().getGridSize()[0],
                    Integer.parseInt(mapColumns.getText())});
            boolean isEnoughPlace = isEnoughPlace();
            if (!isEnoughPlace) {
              //save the previous value back in the template
              this.currentMap.getMapTemplate().setGridSize(
                  new int[]{this.currentMap.getMapTemplate().getGridSize()[0], prevColumns});
              mapColumns.setText(String.valueOf(prevColumns));
              mapSizeExceptionLabel.setVisible(true);
              mapSizeExceptionLabel.setText("Not enough place for pieces.");
              throw new IndexOutOfBoundsException("Not enough place for pieces");
            }
          }
          updateMapPreview(gameBoardHelper);
          mapSizeExceptionLabel.setVisible(false);
        } catch (NumberFormatException e) {
          this.currentMap.getMapTemplate().setGridSize(
              new int[]{this.currentMap.getMapTemplate().getGridSize()[0], prevColumns});
          mapColumns.setText(String.valueOf(prevColumns));
          mapSizeExceptionLabel.setVisible(true);
          mapSizeExceptionLabel.setText("Please enter a valid number");
        } catch (IndexOutOfBoundsException e) {
          this.currentMap.getMapTemplate().setGridSize(
              new int[]{this.currentMap.getMapTemplate().getGridSize()[0], prevColumns});
          mapColumns.setText(String.valueOf(prevColumns));
          mapSizeExceptionLabel.setVisible(true);
          mapSizeExceptionLabel.setText("Increase grid size until < 20");
        }
      }
    });

    playersNumber.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
      try {
        this.sceneController.playAudio("selectMenuSound.wav");

        this.currentMap.getMapTemplate()
            .setTeams(Integer.parseInt(((RadioButton) newValue).getText()));
        boolean isEnoughPlace = isEnoughPlace();
        if (!isEnoughPlace) {
          this.currentMap.getMapTemplate()
              .setTeams(Integer.parseInt(((RadioButton) oldValue).getText()));
          playersNumber.selectToggle(oldValue);
          playersNumberExceptionLabel.setVisible(true);
          playersNumberExceptionLabel.setText("Not enough place for pieces.");
          throw new IndexOutOfBoundsException("Not enough place for pieces");
        }
        updateMapPreview(gameBoardHelper);
        playersNumberExceptionLabel.setVisible(false);
      } catch (NumberFormatException e) {
        this.currentMap.getMapTemplate()
            .setTeams(Integer.parseInt(((RadioButton) oldValue).getText()));
        playersNumber.selectToggle(oldValue);
        playersNumberExceptionLabel.setVisible(true);
        playersNumberExceptionLabel.setText("Please enter a valid number");
      } catch (IndexOutOfBoundsException e) {
        this.currentMap.getMapTemplate()
            .setTeams(Integer.parseInt(((RadioButton) oldValue).getText()));
        playersNumber.selectToggle(oldValue);
        playersNumberExceptionLabel.setVisible(true);
        playersNumberExceptionLabel.setText("Not enough place for pieces.");
      }
    });

    mapTotalGameTime.setOnKeyPressed(event -> {
      if (event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.TAB) {
        this.sceneController.playAudio("selectMenuSound.wav");

        int prevTotalGameTime = this.currentMap.getMapTemplate().getTotalTimeLimitInSeconds() / 60;
        try {
          if (Integer.parseInt(mapTotalGameTime.getText()) <= 2) {
            mapTotalGameTime.setText(String.valueOf(prevTotalGameTime));
            gameTimeExceptionLabel.setVisible(true);
            gameTimeExceptionLabel.setText("Please enter a number > 2");
            throw new IndexOutOfBoundsException("Game time too short.");
          }
          currentMap.getMapTemplate()
              .setTotalTimeLimitInSeconds(Integer.parseInt(mapTotalGameTime.getText()) * 60);
          gameTimeExceptionLabel.setVisible(false);
        } catch (NumberFormatException e) {
          mapTotalGameTime.setText(String.valueOf(prevTotalGameTime));
          gameTimeExceptionLabel.setVisible(true);
          gameTimeExceptionLabel.setText("Please enter a valid number");
        }
      }
    });

    mapMoveTime.setOnKeyPressed(event -> {
      if (event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.TAB) {
        this.sceneController.playAudio("selectMenuSound.wav");

        int prevMoveTime = this.currentMap.getMapTemplate().getMoveTimeLimitInSeconds();
        try {
          if (Integer.parseInt(mapMoveTime.getText()) <= 2) {
            mapMoveTime.setText(String.valueOf(prevMoveTime));
            moveTimeExceptionLabel.setVisible(true);
            moveTimeExceptionLabel.setText("Please enter a number > 2");
            throw new IndexOutOfBoundsException("Move time too short.");
          }
          currentMap.getMapTemplate()
              .setMoveTimeLimitInSeconds(Integer.parseInt(mapMoveTime.getText()));
          moveTimeExceptionLabel.setVisible(false);
        } catch (NumberFormatException e) {
          mapMoveTime.setText(String.valueOf(prevMoveTime));
          moveTimeExceptionLabel.setVisible(true);
          moveTimeExceptionLabel.setText("Please enter a valid number");
        }
      }
    });

    flagsCount.setOnKeyPressed(event -> {
      if (event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.TAB) {
        this.sceneController.playAudio("selectMenuSound.wav");

        int prevFlagsCount = this.currentMap.getMapTemplate().getFlags();
        try {
          if (Integer.parseInt(flagsCount.getText()) <= 0) {
            flagsCount.setText(String.valueOf(prevFlagsCount));
            flagsExceptionLabel.setVisible(true);
            flagsExceptionLabel.setText("At least one flag.");
            throw new IndexOutOfBoundsException("Too few flags.");
          } else if (Integer.parseInt(flagsCount.getText()) > this.currentMap.getMapTemplate()
              .getTeams()) {
            flagsCount.setText(String.valueOf(prevFlagsCount));
            flagsExceptionLabel.setVisible(true);
            flagsExceptionLabel.setText("Max flags = nr of teams.");
            throw new IndexOutOfBoundsException("Too many flags.");
          }
          currentMap.getMapTemplate().setFlags(Integer.parseInt(flagsCount.getText()));
          flagsExceptionLabel.setVisible(false);
        } catch (NumberFormatException e) {
          this.currentMap.getMapTemplate().setFlags(prevFlagsCount);
          flagsCount.setText(String.valueOf(prevFlagsCount));
          flagsExceptionLabel.setVisible(true);
          flagsExceptionLabel.setText("Please enter a valid number");
        } catch (IndexOutOfBoundsException e) {
          this.currentMap.getMapTemplate().setFlags(prevFlagsCount);
          flagsCount.setText(String.valueOf(prevFlagsCount));
          flagsExceptionLabel.setVisible(true);
          flagsExceptionLabel.setText("0 < flags <=  nr of teams");
        }
      }

    });

    blocksCount.setOnKeyPressed(event -> {
      if (event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.TAB) {
        this.sceneController.playAudio("selectMenuSound.wav");

        int prevBlocksCount = this.currentMap.getMapTemplate().getBlocks();
        try {
          if (Integer.parseInt(blocksCount.getText()) < 0) {
            blocksCount.setText(String.valueOf(prevBlocksCount));
            blocksExceptionLabel.setVisible(true);
            blocksExceptionLabel.setText("Not a valid number of blocks");
            throw new IndexOutOfBoundsException("GridState too small.");
          } else {
            this.currentMap.getMapTemplate().setBlocks(Integer.parseInt(blocksCount.getText()));
            boolean isEnoughPlace = isEnoughPlace();
            if (!isEnoughPlace) {
              //save the previous value back in the template
              this.currentMap.getMapTemplate().setBlocks(prevBlocksCount);
              blocksCount.setText(String.valueOf(prevBlocksCount));
              blocksExceptionLabel.setVisible(true);
              blocksExceptionLabel.setText("Not enough place for pieces.");
              throw new IndexOutOfBoundsException("Not enough place for pieces");
            }
          }
          updateMapPreview(gameBoardHelper);
          blocksExceptionLabel.setVisible(false);
        } catch (NumberFormatException e) {
          this.currentMap.getMapTemplate().setBlocks(prevBlocksCount);
          blocksCount.setText(String.valueOf(prevBlocksCount));
          blocksExceptionLabel.setVisible(true);
          blocksExceptionLabel.setText("Please enter a valid number");
        } catch (IndexOutOfBoundsException e) {
          this.currentMap.getMapTemplate().setBlocks(prevBlocksCount);
          blocksCount.setText(String.valueOf(prevBlocksCount));
          blocksExceptionLabel.setVisible(true);
          blocksExceptionLabel.setText("More space for pieces");

        }
      }
    });

    pawnCount.setOnKeyPressed(event -> {
      if (event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.TAB) {
        this.sceneController.playAudio("selectMenuSound.wav");

        int prevPawnCount = this.currentMap.getMapTemplate().getPieces()[0].getCount();
        try {
          if (Integer.parseInt(pawnCount.getText()) < 0) {
            pawnCount.setText(String.valueOf(prevPawnCount));
            pawnExceptionLabel.setVisible(true);
            pawnExceptionLabel.setText("Not a valid number of pawns");
            throw new IndexOutOfBoundsException("GridState too small.");
          } else {
            this.currentMap.getMapTemplate().getPieces()[0].setCount(
                Integer.parseInt(pawnCount.getText()));
            boolean isEnoughPlace = isEnoughPlace();
            if (!isEnoughPlace) {
              //save the previous value back in the template
              this.currentMap.getMapTemplate().getPieces()[0].setCount(prevPawnCount);
              pawnCount.setText(String.valueOf(prevPawnCount));
              pawnExceptionLabel.setVisible(true);
              pawnExceptionLabel.setText("Increment min grid dimension");
              throw new IndexOutOfBoundsException("Not enough place for pieces");
            }
          }
          updateMapPreview(gameBoardHelper);
          pawnExceptionLabel.setVisible(false);
        } catch (NumberFormatException e) {
          this.currentMap.getMapTemplate().getPieces()[0].setCount(prevPawnCount);
          pawnCount.setText(String.valueOf(prevPawnCount));
          pawnExceptionLabel.setVisible(true);
          pawnExceptionLabel.setText("Please enter a valid number");
        } catch (IndexOutOfBoundsException e) {
          this.currentMap.getMapTemplate().getPieces()[0].setCount(prevPawnCount);
          pawnCount.setText(String.valueOf(prevPawnCount));
          pawnExceptionLabel.setVisible(true);
          pawnExceptionLabel.setText("No space for pieces");
        } catch (IllegalArgumentException e) {
          this.currentMap.getMapTemplate().getPieces()[0].setCount(prevPawnCount);
          pawnCount.setText(String.valueOf(prevPawnCount));
          pawnExceptionLabel.setVisible(true);
          pawnExceptionLabel.setText("At least 1 piece required!");
        }
      }
    });

    knightCount.setOnKeyPressed(event -> {
      if (event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.TAB) {
        this.sceneController.playAudio("selectMenuSound.wav");

        int prevKnightCount = this.currentMap.getMapTemplate().getPieces()[1].getCount();
        try {
          if (Integer.parseInt(knightCount.getText()) < 0) {
            knightCount.setText(String.valueOf(prevKnightCount));
            knightExceptionLabel.setVisible(true);
            knightExceptionLabel.setText("Not a valid number of knights");
            throw new IndexOutOfBoundsException("GridState too small.");
          } else {
            this.currentMap.getMapTemplate().getPieces()[1].setCount(
                Integer.parseInt(knightCount.getText()));
            boolean isEnoughPlace = isEnoughPlace();
            if (!isEnoughPlace) {
              //save the previous value back in the template
              this.currentMap.getMapTemplate().getPieces()[1].setCount(prevKnightCount);
              knightCount.setText(String.valueOf(prevKnightCount));
              knightExceptionLabel.setVisible(true);
              knightExceptionLabel.setText("Increment grid size or decrease number of pieces.");
              throw new IndexOutOfBoundsException("Not enough place for pieces");
            }
          }
          updateMapPreview(gameBoardHelper);
          knightExceptionLabel.setVisible(false);
        } catch (NumberFormatException e) {
          this.currentMap.getMapTemplate().getPieces()[1].setCount(prevKnightCount);
          knightCount.setText(String.valueOf(prevKnightCount));
          knightExceptionLabel.setVisible(true);
          knightExceptionLabel.setText("Please enter a valid number");
        } catch (IndexOutOfBoundsException e) {
          this.currentMap.getMapTemplate().getPieces()[1].setCount(prevKnightCount);
          knightCount.setText(String.valueOf(prevKnightCount));
          knightExceptionLabel.setVisible(true);
          knightExceptionLabel.setText("No space for pieces");
        } catch (IllegalArgumentException e) {
          this.currentMap.getMapTemplate().getPieces()[1].setCount(prevKnightCount);
          knightCount.setText(String.valueOf(prevKnightCount));
          knightExceptionLabel.setVisible(true);
          knightExceptionLabel.setText("At least 1 piece required!");
        }
      }
    });

    bishopCount.setOnKeyPressed(event -> {
      if (event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.TAB) {
        this.sceneController.playAudio("selectMenuSound.wav");

        int prevBishopCount = this.currentMap.getMapTemplate().getPieces()[2].getCount();
        try {
          if (Integer.parseInt(bishopCount.getText()) < 0) {
            bishopCount.setText(String.valueOf(prevBishopCount));
            bishopExceptionLabel.setVisible(true);
            bishopExceptionLabel.setText("Not a valid number of bishops");
            throw new IndexOutOfBoundsException("GridState too small.");
          } else {
            this.currentMap.getMapTemplate().getPieces()[2].setCount(
                Integer.parseInt(bishopCount.getText()));
            boolean isEnoughPlace = isEnoughPlace();
            if (!isEnoughPlace) {
              //save the previous value back in the template
              this.currentMap.getMapTemplate().getPieces()[2].setCount(prevBishopCount);
              bishopCount.setText(String.valueOf(prevBishopCount));
              bishopExceptionLabel.setVisible(true);
              bishopExceptionLabel.setText("Increment grid size or decrease number of pieces.");
              throw new IndexOutOfBoundsException("Not enough place for pieces");
            }
          }
          updateMapPreview(gameBoardHelper);
          bishopExceptionLabel.setVisible(false);
        } catch (NumberFormatException e) {
          this.currentMap.getMapTemplate().getPieces()[2].setCount(prevBishopCount);
          bishopCount.setText(String.valueOf(prevBishopCount));
          bishopExceptionLabel.setVisible(true);
          bishopExceptionLabel.setText("Please enter a valid number");
        } catch (IndexOutOfBoundsException e) {
          this.currentMap.getMapTemplate().getPieces()[2].setCount(prevBishopCount);
          bishopCount.setText(String.valueOf(prevBishopCount));
          bishopExceptionLabel.setVisible(true);
          bishopExceptionLabel.setText("No space for pieces");
        } catch (IllegalArgumentException e) {
          this.currentMap.getMapTemplate().getPieces()[2].setCount(prevBishopCount);
          bishopCount.setText(String.valueOf(prevBishopCount));
          bishopExceptionLabel.setVisible(true);
          bishopExceptionLabel.setText("At least 1 piece required!");
        }
      }
    });

    rookCount.setOnKeyPressed(event -> {
      if (event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.TAB) {
        this.sceneController.playAudio("selectMenuSound.wav");

        int prevRookCount = this.currentMap.getMapTemplate().getPieces()[3].getCount();
        try {
          if (Integer.parseInt(rookCount.getText()) < 0) {
            rookCount.setText(String.valueOf(prevRookCount));
            rookExceptionLabel.setVisible(true);
            rookExceptionLabel.setText("Not a valid number of rooks");
            throw new IndexOutOfBoundsException("GridState too small.");
          } else {
            this.currentMap.getMapTemplate().getPieces()[3].setCount(
                Integer.parseInt(rookCount.getText()));
            boolean isEnoughPlace = isEnoughPlace();
            if (!isEnoughPlace) {
              //save the previous value back in the template
              this.currentMap.getMapTemplate().getPieces()[3].setCount(prevRookCount);
              rookCount.setText(String.valueOf(prevRookCount));
              rookExceptionLabel.setVisible(true);
              rookExceptionLabel.setText("Increment grid size or decrease number of pieces.");
              throw new IndexOutOfBoundsException("Not enough place for pieces");
            }
          }
          updateMapPreview(gameBoardHelper);
          rookExceptionLabel.setVisible(false);
        } catch (NumberFormatException e) {
          this.currentMap.getMapTemplate().getPieces()[3].setCount(prevRookCount);
          rookCount.setText(String.valueOf(prevRookCount));
          rookExceptionLabel.setVisible(true);
          rookExceptionLabel.setText("Please enter a valid number");
        } catch (IndexOutOfBoundsException e) {
          this.currentMap.getMapTemplate().getPieces()[3].setCount(prevRookCount);
          rookCount.setText(String.valueOf(prevRookCount));
          rookExceptionLabel.setVisible(true);
          rookExceptionLabel.setText("No space for pieces");
        } catch (IllegalArgumentException e) {
          this.currentMap.getMapTemplate().getPieces()[3].setCount(prevRookCount);
          rookCount.setText(String.valueOf(prevRookCount));
          rookExceptionLabel.setVisible(true);
          rookExceptionLabel.setText("At least 1 piece required!");
        }
      }
    });

    queenCount.setOnKeyPressed(event -> {
      if (event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.TAB) {
        this.sceneController.playAudio("selectMenuSound.wav");

        int prevQueenCount = this.currentMap.getMapTemplate().getPieces()[4].getCount();
        try {
          if (Integer.parseInt(queenCount.getText()) < 0) {
            queenCount.setText(String.valueOf(prevQueenCount));
            queenExceptionLabel.setVisible(true);
            queenExceptionLabel.setText("Not a valid number of queens");
            throw new IndexOutOfBoundsException("GridState too small.");
          } else {
            this.currentMap.getMapTemplate().getPieces()[4].setCount(
                Integer.parseInt(queenCount.getText()));
            boolean isEnoughPlace = isEnoughPlace();
            if (!isEnoughPlace) {
              //save the previous value back in the template
              this.currentMap.getMapTemplate().getPieces()[4].setCount(prevQueenCount);
              queenCount.setText(String.valueOf(prevQueenCount));
              queenExceptionLabel.setVisible(true);
              queenExceptionLabel.setText("Increment grid size or decrease number of pieces.");
              throw new IndexOutOfBoundsException("Not enough place for pieces");
            }
          }
          updateMapPreview(gameBoardHelper);
          queenExceptionLabel.setVisible(false);
        } catch (NumberFormatException e) {
          this.currentMap.getMapTemplate().getPieces()[4].setCount(prevQueenCount);
          queenCount.setText(String.valueOf(prevQueenCount));
          queenExceptionLabel.setVisible(true);
          queenExceptionLabel.setText("Please enter a valid number");
        } catch (IndexOutOfBoundsException e) {
          this.currentMap.getMapTemplate().getPieces()[4].setCount(prevQueenCount);
          queenCount.setText(String.valueOf(prevQueenCount));
          queenExceptionLabel.setVisible(true);
          queenExceptionLabel.setText("No space for pieces");
        } catch (IllegalArgumentException e) {
          this.currentMap.getMapTemplate().getPieces()[4].setCount(prevQueenCount);
          queenCount.setText(String.valueOf(prevQueenCount));
          queenExceptionLabel.setVisible(true);
          queenExceptionLabel.setText("At least 1 piece required!");
        }
      }
    });

    kingCount.setOnKeyPressed(event -> {
      if (event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.TAB) {
        this.sceneController.playAudio("selectMenuSound.wav");

        int prevKingCount = this.currentMap.getMapTemplate().getPieces()[5].getCount();
        try {
          if (Integer.parseInt(kingCount.getText()) < 0) {
            kingCount.setText(String.valueOf(prevKingCount));
            kingExceptionLabel.setVisible(true);
            kingExceptionLabel.setText("Not a valid number of kings");
            throw new IndexOutOfBoundsException("GridState too small.");
          } else {
            this.currentMap.getMapTemplate().getPieces()[5].setCount(
                Integer.parseInt(kingCount.getText()));
            boolean isEnoughPlace = isEnoughPlace();
            if (!isEnoughPlace) {
              //save the previous value back in the template
              this.currentMap.getMapTemplate().getPieces()[5].setCount(prevKingCount);
              kingCount.setText(String.valueOf(prevKingCount));
              kingExceptionLabel.setVisible(true);
              kingExceptionLabel.setText("Increment grid size or decrease number of pieces.");
              throw new IndexOutOfBoundsException("Not enough place for pieces");
            }
          }
          updateMapPreview(gameBoardHelper);
          kingExceptionLabel.setVisible(false);
        } catch (NumberFormatException e) {
          this.currentMap.getMapTemplate().getPieces()[5].setCount(prevKingCount);
          kingCount.setText(String.valueOf(prevKingCount));
          kingExceptionLabel.setVisible(true);
          kingExceptionLabel.setText("Please enter a valid number");
        } catch (IndexOutOfBoundsException e) {
          this.currentMap.getMapTemplate().getPieces()[5].setCount(prevKingCount);
          kingCount.setText(String.valueOf(prevKingCount));
          kingExceptionLabel.setVisible(true);
          kingExceptionLabel.setText("No space for pieces");
        } catch (IllegalArgumentException e) {
          this.currentMap.getMapTemplate().getPieces()[5].setCount(prevKingCount);
          kingCount.setText(String.valueOf(prevKingCount));
          kingExceptionLabel.setVisible(true);
          kingExceptionLabel.setText("At least 1 piece required!");
        }
      }
    });

    placementType.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
      PlacementType prevSelectedPlacement = this.currentMap.getMapTemplate().getPlacement();
      RadioButton selectedRadioButton = (RadioButton) newValue;
      try {
        this.sceneController.playAudio("selectMenuSound.wav");

        switch (selectedRadioButton.getText().toUpperCase()) {
          case "SYMMETRICAL":
            currentMap.getMapTemplate().setPlacement(PlacementType.symmetrical);
            break;
          case "DEFENSIVE":
            currentMap.getMapTemplate().setPlacement(PlacementType.defensive);
            break;
          case "SPACED OUT":
            currentMap.getMapTemplate().setPlacement(PlacementType.spaced_out);
            break;
        }
        updateMapPreview(gameBoardHelper);
        placementExceptionLabel.setVisible(false);
      } catch (IndexOutOfBoundsException e) {
        currentMap.getMapTemplate().setPlacement(prevSelectedPlacement);
        placementType.selectToggle(oldValue);
        placementExceptionLabel.setVisible(true);
        placementExceptionLabel.setText("Not enough place for pieces.");
      }
    });
  }

  /**
   * Gets the Map Template from the current field configuration
   *
   * @return MapTemplate the MapTemplate
   * @author gabriel.victor.arthur.himmelein, virgil.baclanov
   */
  public MapTemplate getMapTemplateFromFields() {
    try {
      MapTemplate mapTemplate = new MapTemplate();
      mapTemplate.setGridSize(new int[]{Integer.parseInt(this.mapRows.getText()),
          Integer.parseInt(this.mapColumns.getText())});
      mapTemplate.setTeams(
          Integer.parseInt(((RadioButton) this.playersNumber.getSelectedToggle()).getText()));
      mapTemplate.setTotalTimeLimitInSeconds(
          Integer.parseInt(this.mapTotalGameTime.getText()) * 60);
      mapTemplate.setMoveTimeLimitInSeconds(Integer.parseInt(this.mapMoveTime.getText()));
      mapTemplate.setFlags(Integer.parseInt(this.flagsCount.getText()));
      mapTemplate.setBlocks(Integer.parseInt(this.blocksCount.getText()));

      switch (((RadioButton) this.placementType.getSelectedToggle()).getText().toUpperCase()) {
        case "SYMMETRICAL":
          mapTemplate.setPlacement(PlacementType.symmetrical);
          break;
        case "DEFENSIVE":
          mapTemplate.setPlacement(PlacementType.defensive);
          break;
        case "SPACED OUT":
          mapTemplate.setPlacement(PlacementType.spaced_out);
          break;
      }

      //initialize all pieces
      Directions directionsPawn = new Directions(1, 1, 1, 1, 0, 0, 0, 0);
      Directions directionsBishop = new Directions(0, 0, 0, 0, 2, 2, 2, 2);
      Directions directionsRook = new Directions(2, 2, 2, 2, 0, 0, 0, 0);
      Directions directionsQueen = new Directions(2, 2, 2, 2, 2, 2, 2, 2);
      Directions directionsKing = new Directions(1, 1, 1, 1, 1, 1, 1, 1);
      Movement movementPawn = new Movement(directionsPawn);
      Movement movementKnight = new Movement(new Shape());
      Movement movementBishop = new Movement(directionsBishop);
      Movement movementRook = new Movement(directionsRook);
      Movement movementQueen = new Movement(directionsQueen);
      Movement movementKing = new Movement(directionsKing);
      PieceDescription pawnDescription = new PieceDescription("Pawn", 1,
          Integer.parseInt(this.pawnCount.getText()), movementPawn);
      PieceDescription knightDescription = new PieceDescription("Knight", 3,
          Integer.parseInt(this.knightCount.getText()), movementKnight);
      PieceDescription bishopDescription = new PieceDescription("Bishop", 3,
          Integer.parseInt(this.bishopCount.getText()), movementBishop);
      PieceDescription rookDescription = new PieceDescription("Rook", 4,
          Integer.parseInt(this.rookCount.getText()), movementRook);
      PieceDescription queenDescription = new PieceDescription("Queen", 5,
          Integer.parseInt(this.queenCount.getText()), movementQueen);
      PieceDescription kingDescription = new PieceDescription("King", 5,
          Integer.parseInt(this.kingCount.getText()), movementKing);

      mapTemplate.setPieces(new PieceDescription[]{
          pawnDescription, knightDescription, bishopDescription, rookDescription, queenDescription,
          kingDescription
      });

      return mapTemplate;

    } catch (NullPointerException e) {
      System.out.println("Field is empty.");
    }
    return null;
  }

  /**
   * Updates the map preview with the current map configuration
   *
   * @param gameBoardHelper the gameBoardHelper
   * @author virgil.baclanov
   */
  public void updateMapPreview(GameBoardHelper gameBoardHelper) {
    Team[] updatedTeam = getUpdatedTeam(this.currentMap);

    GameState gameState = new GameState(this.currentMap.getMapTemplate().getGridSize()[0],
        this.currentMap.getMapTemplate().getGridSize()[1],
        this.currentMap.getMapTemplate().getTeams());
    String[][] updatedFilledGrid = getUpdatedGameStateGrid(this.currentMap, updatedTeam);
    updatedFilledGrid = gameBoardHelper.placeBlocks(updatedFilledGrid,
        this.currentMap.getMapTemplate().getBlocks());
    gameState.setGrid(updatedFilledGrid);
    gameState.setTeams(updatedTeam);
    this.currentMap.setGameState(gameState);

    boardPreview.getChildren().clear();
    boardPreview.getChildren()
        .add(new GameBoard(this.currentMap.getGameState(), BoardTheme.CLASSIC, false));
  }

  /**
   * Method to get the updated team object from the current map configuration
   *
   * @param map the current map
   * @return Team[] the updated team object
   * @author virgil.baclanov
   */
  public Team[] getUpdatedTeam(Map map) {
    GameTeamHelper gameTeamHelper = new GameTeamHelper(0, 0);
    gameTeamHelper.setTotalPiecesPerTeam(map.getMapTemplate());
    int totalPiecesPerTeam = gameTeamHelper.getTotalPiecesPerTeam();

    Team[] teams = new Team[map.getMapTemplate().getTeams()];
    for (int i = 1; i <= map.getMapTemplate().getTeams(); i++) {
      teams[i - 1] = new Team();
      Piece[] teamPieces = new Piece[totalPiecesPerTeam];
      int pieceId = 0;
      for (PieceDescription pieceDescription : map.getMapTemplate().getPieces()) {
        int pieceTypeCountPerTeam = pieceDescription.getCount();
        while (pieceTypeCountPerTeam > 0) {
          Piece piece = new Piece();
          piece.setId("p:" + i + "_" + pieceId);
          piece.setDescription(pieceDescription);
          piece.setTeamId(String.valueOf(i));
          piece.setPosition(new int[]{0, 0});
          teamPieces[pieceId++] = piece;
          pieceTypeCountPerTeam--;
        }
      }
      teams[i - 1].setPieces(teamPieces);
    }
    return teams;
  }

  /**
   * Method to get the updated game state grid with placement types from the current map
   * configuration
   *
   * @param map   the current map
   * @param teams the teams objects
   * @return String[][] the updated game state grid
   * @author virgil.baclanov
   */
  public String[][] getUpdatedGameStateGrid(Map map, Team[] teams) {
    GameBoardHelper gameBoardHelper = new GameBoardHelper();
    String[][] emptyGrid = gameBoardHelper.createEmptyGrid(map.getMapTemplate());
    return gameBoardHelper.placePiecesAndBase(new GameState(), emptyGrid, teams,
        map.getMapTemplate());
  }

  /**
   * Method to check if there is enough place for the current map configuration. We take the square
   * grid with dimensions 3/4 x 3/4 from the minimum dimension of the given map. There can only be
   * as much as pieces as this square grid can hold.
   *
   * @return boolean true if there is enough place, false otherwise
   * @author virgil.baclanov
   */
  public boolean isEnoughPlace() {
    try {
      int totalObjects = 0;
      for (PieceDescription pieceDescription : this.currentMap.getMapTemplate().getPieces()) {
        totalObjects += pieceDescription.getCount();
      }
      totalObjects *= this.currentMap.getMapTemplate().getTeams();
      //add flags and then blocks
      totalObjects += this.currentMap.getMapTemplate().getTeams();
      totalObjects += this.currentMap.getMapTemplate().getBlocks() * 2;

      int lowerBoundary = 3 * (Math.min(this.currentMap.getMapTemplate().getGridSize()[0],
          this.currentMap.getMapTemplate().getGridSize()[1])) / 4;
      return !(Math.ceil(Math.sqrt(totalObjects)) >= lowerBoundary);
    } catch (NullPointerException e) {
      System.out.println("No map or maptemplate found.");
    }
    return false;
  }

  /**
   * Method to save created maps in the database.
   *
   * @author gabriel.victor.arthur.himmelein, juan.steppacher
   */
  @FXML
  public boolean saveMap() {
    try {
      if (this.isNewMap) {
        this.currentMap = Client.networkHandler.getMapHandler().createNewMap();
      }

      if (this.mapName.getText().isEmpty()) {
        this.currentMap.setName("Remember to name your map!");
      } else {
        this.currentMap.setName(this.mapName.getText());
      }

      this.currentMap.setMapTemplate(getMapTemplateFromFields());
      this.currentMap.setPublic(mapPublicToggle.isSelected());

      Client.networkHandler.getMapHandler()
          .updateMap(this.currentMap.getId(), this.currentMap.name, this.currentMap.mapTemplate,
              mapPublicToggle.isSelected());

      this.currentMap = null;

      stage.setScene(GUI.getScenes().get(SceneName.MAP_EDITOR).getScene());
      this.sceneController.playAudio("selectMenuSound.wav");
      return true;
    } catch (NullPointerException e) {
      System.out.println("Network handler or map not found.");
      e.printStackTrace();
      return false;
    } catch (NumberFormatException e) {
      System.out.println("Please enter a valid number");
      return false;
    }
  }

  /**
   * Method to change text of the public map toggle button.
   *
   * @author gabriel.victor.arthur.himmelein, juan.steppacher
   */
  @FXML
  private void chooseMapPublic() throws Exception {
    this.sceneController.playAudio("selectMenuSound.wav");

    if (mapPublicToggle.isSelected()) {
      mapPublicToggle.setText("Public map: YES");
    } else {
      mapPublicToggle.setText("Public map: NO");
    }
  }

  /**
   * This method switches back to the map editor frame.
   *
   * @author juan.steppacher
   */
  @FXML
  private void navigateToMapEditor() throws Exception {
    stage.setScene(GUI.getScenes().get(SceneName.MAP_EDITOR).getScene(this.currentMap));
    this.sceneController.playAudio("selectMenuSound.wav");
  }

  public void setCurrentMap(Map currentMap) {
    this.currentMap = currentMap;
  }

  /**
   * Set the primary stage of the application
   *
   * @param stage primary stage to set
   */
  @Override
  public void setStage(Stage stage) {
    this.stage = stage;
  }
}
