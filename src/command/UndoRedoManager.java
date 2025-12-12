package command;

import java.util.Stack;

public class UndoRedoManager {
    private final Stack<Command> undoStack = new Stack<>();
    private final Stack<Command> redoStack = new Stack<>();

    /** Executes a new command and resets the redo history. */
    public void execute(Command command) throws Exception {
        command.executeRedo();
        undoStack.push(command);
        redoStack.clear();
    }

    /** Reverts the last executed command. */
    public void undo() throws Exception {
        if (undoStack.isEmpty()) {
            throw new IllegalStateException("Nothing to undo.");
        }

        Command command = undoStack.pop();
        command.executeUndo();
        redoStack.push(command);
    }

    /** Re-executes the last undone command. */
    public void redo() throws Exception {
        if (redoStack.isEmpty()) {
            throw new IllegalStateException("Nothing to redo.");
        }

        Command command = redoStack.pop();
        command.executeRedo();
        undoStack.push(command);
    }

    public boolean canUndo() {
        return !undoStack.isEmpty();
    }

    public boolean canRedo() {
        return !redoStack.isEmpty();
    }
}
