package com.todokanban.application.ports.input;

import com.todokanban.domain.model.Board;

/** Input port for creating a new Card inside a Column. */
public interface CreateCardUseCase {
    Board createCard(CreateCardCommand command);
}
