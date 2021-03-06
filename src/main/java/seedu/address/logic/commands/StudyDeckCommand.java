package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.DecksView;
import seedu.address.model.Model;
import seedu.address.model.deck.Deck;

/**
 * Selects a deck identified using its displayed index.
 */
public class StudyDeckCommand extends Command {

    public static final String COMMAND_WORD = "study";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Enters the session using a deck.\n"
            + "Parameters: INDEX (must be a positive integer)\n" + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_STUDY_DECK_SUCCESS = "Studying: %s";

    private Index targetIndex;
    private DecksView decksView;
    private Deck targetDeck;

    public StudyDeckCommand(DecksView decksView, Index targetIndex) {
        this.targetIndex = targetIndex;
        this.decksView = decksView;
    }

    public StudyDeckCommand(Deck targetDeck) {
        this.targetDeck = targetDeck;
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        requireNonNull(model);

        if (targetIndex != null) { //if OpenDeckCommand is based on target index
            List<Deck> filteredDeckList = decksView.filteredDecks;

            if (targetIndex.getZeroBased() >= filteredDeckList.size()) {
                throw new CommandException(Messages.MESSAGE_INVALID_DISPLAYED_INDEX);
            }
            targetDeck = filteredDeckList.get(targetIndex.getZeroBased());
        }

        if (targetDeck.isEmpty()) {
            throw new CommandException(Messages.MESSAGE_EMPTY_DECK);
        }

        model.studyDeck(targetDeck);

        return new UpdatePanelCommandResult(String.format(MESSAGE_STUDY_DECK_SUCCESS, targetDeck.getName()));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof StudyDeckCommand // instanceof handles nulls
                && targetIndex.equals(((StudyDeckCommand) other).targetIndex)); // state check
    }
}
