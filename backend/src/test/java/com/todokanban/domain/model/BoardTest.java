package com.todokanban.domain.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link Board#moveCard(CardId, ColumnId, ColumnId)}.
 * Pure JUnit 5 – no Spring context needed.
 */
@DisplayName("Board aggregate root")
class BoardTest {

    private WorkspaceId workspaceId;
    private Board board;
    private Column todoColumn;
    private Column inProgressColumn;
    private Column doneColumn;
    private Card card;

    @BeforeEach
    void setUp() {
        workspaceId  = WorkspaceId.generate();
        board        = Board.create(workspaceId, "Sprint Board", "Q1 Sprint");

        todoColumn       = Column.create("To Do",       0);
        inProgressColumn = Column.create("In Progress", 1);
        doneColumn       = Column.create("Done",        2);

        card = Card.create("Implement login", "OAuth2 login flow", 0);
        todoColumn.addCard(card);

        board.addColumn(todoColumn);
        board.addColumn(inProgressColumn);
        board.addColumn(doneColumn);
    }

    @Nested
    @DisplayName("moveCard()")
    class MoveCardTests {

        @Test
        @DisplayName("moves a card from source column to target column")
        void moveCard_happyPath() {
            board.moveCard(card.getId(), todoColumn.getId(), inProgressColumn.getId());

            assertFalse(todoColumn.containsCard(card.getId()),
                    "Card should no longer be in the source column");
            assertTrue(inProgressColumn.containsCard(card.getId()),
                    "Card should be present in the target column");
        }

        @Test
        @DisplayName("throws when source column does not exist in this board")
        void moveCard_sourceColumnNotFound() {
            ColumnId unknownColumn = ColumnId.generate();

            IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                    () -> board.moveCard(card.getId(), unknownColumn, doneColumn.getId()));

            assertTrue(ex.getMessage().contains("Source column"),
                    "Error message should mention 'Source column'");
        }

        @Test
        @DisplayName("throws when card does not exist in source column")
        void moveCard_cardNotFoundInSourceColumn() {
            CardId unknownCard = CardId.generate();

            IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                    () -> board.moveCard(unknownCard, todoColumn.getId(), inProgressColumn.getId()));

            assertTrue(ex.getMessage().contains("not found in column"),
                    "Error message should mention 'not found in column'");
        }

        @Test
        @DisplayName("throws when target column does not exist in this board")
        void moveCard_targetColumnNotFound() {
            ColumnId unknownColumn = ColumnId.generate();

            IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                    () -> board.moveCard(card.getId(), todoColumn.getId(), unknownColumn));

            assertTrue(ex.getMessage().contains("Target column"),
                    "Error message should mention 'Target column'");
        }

        @Test
        @DisplayName("card is appended to the end of the target column")
        void moveCard_cardPositionedAtEnd() {
            Card existingCard = Card.create("Existing task", "Already in progress", 0);
            inProgressColumn.addCard(existingCard);

            board.moveCard(card.getId(), todoColumn.getId(), inProgressColumn.getId());

            // moved card should be positioned after the existing card
            int movedCardIndex = inProgressColumn.getCards().indexOf(
                    inProgressColumn.findCard(card.getId()).orElseThrow());
            assertEquals(1, movedCardIndex,
                    "Moved card should be appended at the end of the target column");
        }

        @Test
        @DisplayName("board updatedAt timestamp is refreshed after a move")
        void moveCard_updatesTimestamp() throws InterruptedException {
            var before = board.getUpdatedAt();
            Thread.sleep(10); // ensure a measurable time difference
            board.moveCard(card.getId(), todoColumn.getId(), inProgressColumn.getId());

            assertTrue(board.getUpdatedAt().isAfter(before),
                    "updatedAt should be refreshed after moveCard");
        }
    }

    @Nested
    @DisplayName("Board creation")
    class BoardCreationTests {

        @Test
        @DisplayName("throws when name is blank")
        void create_blankName() {
            assertThrows(IllegalArgumentException.class,
                    () -> Board.create(workspaceId, "  ", "desc"));
        }

        @Test
        @DisplayName("throws when workspaceId is null")
        void create_nullWorkspaceId() {
            assertThrows(IllegalArgumentException.class,
                    () -> Board.create(null, "Valid Name", "desc"));
        }

        @Test
        @DisplayName("new board starts with empty columns list")
        void create_emptyColumns() {
            Board newBoard = Board.create(workspaceId, "New Board", null);
            assertTrue(newBoard.getColumns().isEmpty());
        }
    }
}
