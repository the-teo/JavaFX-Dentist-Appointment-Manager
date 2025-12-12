package command;

public interface Command {
    void executeRedo() throws Exception;
    void executeUndo() throws Exception;
}