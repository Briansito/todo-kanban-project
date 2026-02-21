package com.todokanban.application.ports.input;

/** Input port for deleting a Card from a Column. */
public interface DeleteCardUseCase {
    void deleteCard(DeleteCardCommand command);
}
