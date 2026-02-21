package com.todokanban.application.ports.input;

import com.todokanban.domain.model.Board;

/** Input port for creating a new Column on a Board. */
public interface CreateColumnUseCase {
    Board createColumn(CreateColumnCommand command);
}
