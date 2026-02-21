package com.todokanban.application.ports.input;

import com.todokanban.domain.model.Board;

/** Input port for updating an existing Card's text fields. */
public interface UpdateCardUseCase {
    Board updateCard(UpdateCardCommand command);
}
