package command;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class CommandRegistry {

	private final ObservableList<Command> commandStack = FXCollections
			.observableArrayList();

	private final ObservableList<Command> undoStack = FXCollections
			.observableArrayList();


	public void executeCommand(Command command) {
		command.execute();
		commandStack.add(command);
		undoStack.clear(); //Stos cofniętych wywołań powinien być czyszczony w każdym wywołaniu metody executeCommand()!
	}

	public void redo() {
		if (!undoStack.isEmpty()){
			Command lastUndoneCommand = undoStack.removeLast();
			lastUndoneCommand.redo();
			commandStack.add(lastUndoneCommand);
		}
	}

	public void undo() {
		if (!commandStack.isEmpty()){
			Command lastCommand = commandStack.removeLast();
			lastCommand.undo();
			undoStack.add(lastCommand);
		}
	}

	public ObservableList<Command> getCommandStack() {
		return commandStack;
	}
}
