package com.github.fiecher.turnforge.app.usecase;

public interface UseCase<Input, Output> {
    Output execute(Input input);
}
