package commands;

import ui.FinanceException;

public interface Command {

    void execute() throws FinanceException;

    /**
     * Returns true if this command should exit the application.
     * Default is false.
     */
    default boolean shouldExit() {
        return false;
    }
}
