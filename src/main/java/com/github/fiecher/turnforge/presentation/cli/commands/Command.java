package com.github.fiecher.turnforge.presentation.cli.commands;

public interface Command {
    String getName();

    void execute();
}