package com.github.fiecher.turnforge.presentation.cli.output;

public class View {

    private static final String INFO_PREFIX = "[INFO]";
    private static final String ERROR_PREFIX = "[ERROR]";
    private static final String WARNING_PREFIX = "[WARN]";
    private static final String SUCCESS_PREFIX = "[SUCCESS]";

    public void showWelcome() {
        showMessage("+ ----------------------------------------- +");
        showMessage("|    DUNGEONS & DRAGONS: TURNFORGE CONSOLE   |");
        showMessage("+ ------------------------------------------ +");
    }

    public void showMessage(String message) {
        System.out.println(message);
    }

    public void showSuccess(String message) {
        System.out.println("\n" + SUCCESS_PREFIX + " " + message);
    }

    public void showError(String message) {
        System.err.println("\n" + ERROR_PREFIX + " " + message);
    }

    public void showWarning(String message) {
        System.out.println("\n" + WARNING_PREFIX + " " + message);
    }

    public void showInfo(String message) {
        System.out.println("\n" + INFO_PREFIX + " " + message);
    }

    public void showDivider() {
        System.out.println("\n----------------------------------------\n");
    }
}
