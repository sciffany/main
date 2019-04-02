package seedu.address.logic;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;

import java.util.Objects;
import java.util.function.Predicate;

import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import seedu.address.logic.commands.AddDeckCommand;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.DeleteDeckCommand;
import seedu.address.logic.commands.EditDeckCommand;
import seedu.address.logic.commands.FindDeckCommand;
import seedu.address.logic.commands.OpenDeckCommand;
import seedu.address.logic.commands.SelectCommand;
import seedu.address.logic.commands.StudyDeckCommand;
import seedu.address.logic.parser.AddDeckCommandParser;
import seedu.address.logic.parser.DeleteDeckCommandParser;
import seedu.address.logic.parser.EditDeckCommandParser;
import seedu.address.logic.parser.FindDeckCommandParser;
import seedu.address.logic.parser.SelectCommandParser;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.deck.Deck;

/**
 * Stores the state of the Deck's view.
 */
public class DecksView implements ListViewState<Deck> {

    public final FilteredList<Deck> filteredDecks;

    public final SimpleObjectProperty<Deck> selectedDeck = new SimpleObjectProperty<>();

    public DecksView(FilteredList<Deck> deckList) {
        filteredDecks = deckList;
        filteredDecks.addListener(this::ensureSelectedItemIsValid);
    }

    public DecksView(DecksView decksView) {
        this(new FilteredList<>(decksView.getFilteredList()));
    }

    @Override
    public Command parse(String commandWord, String arguments) throws ParseException {
        switch (commandWord) {
            case AddDeckCommand.COMMAND_WORD:
                return new AddDeckCommandParser(this).parse(arguments);
            case SelectCommand.COMMAND_WORD:
                return new SelectCommandParser(this).parse(arguments);
            case OpenDeckCommand.COMMAND_WORD:
                return new OpenDeckCommandParser(this).parse(arguments);
            case StudyDeckCommand.COMMAND_WORD:
                return new StudyDeckCommandParser(this).parse(arguments);
            case DeleteDeckCommand.COMMAND_WORD:
                return new DeleteDeckCommandParser(this).parse(arguments);
            case EditDeckCommand.COMMAND_WORD:
                return new EditDeckCommandParser(this).parse(arguments);
            case FindDeckCommand.COMMAND_WORD:
                return new FindDeckCommandParser(this).parse(arguments);
            default:
                throw new ParseException(MESSAGE_UNKNOWN_COMMAND);
        }
    }

    /**
     * Ensures {@code selectedItem} is a valid card in {@code filteredItems}.
     */
    private void ensureSelectedItemIsValid(ListChangeListener.Change<? extends Deck> change) {
        while (change.next()) {
            if (selectedDeck.getValue() == null) {
                // null is always a valid selected card, so we do not need to check that it is valid anymore.
                return;
            }

            boolean wasSelectedItemReplaced =
                    change.wasReplaced() && change.getAddedSize() == change.getRemovedSize() && change
                            .getRemoved().contains(selectedDeck.getValue());
            if (wasSelectedItemReplaced) {
                // Update selectedDeck to its new value.
                int index = change.getRemoved().indexOf(selectedDeck.getValue());
                selectedDeck.setValue(change.getAddedSubList().get(index));
                continue;
            }

            boolean wasSelectedItemRemoved = change.getRemoved().stream().anyMatch(
                removedItem -> selectedDeck.getValue().equals(removedItem));
            if (wasSelectedItemRemoved) {
                // Select the card that came before it in the list,
                // or clear the selection if there is no such card.
                selectedDeck.setValue(change.getFrom() > 0 ? change.getList().get(change.getFrom() - 1) : null);
            }
        }
    }

    /**
     * Updates the filtered list in DecksView.
     */
    public void updateFilteredList(Predicate<Deck> predicate) {
        requireNonNull(predicate);
        filteredDecks.setPredicate(predicate);
    }

    public ObservableList<Deck> getFilteredList() {
        return filteredDecks;
    }

    @Override
    public boolean equals(Object obj) {
        // short circuit if same object
        if (obj == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(obj instanceof DecksView)) {
            return false;
        }

        // state check
        DecksView other = (DecksView) obj;
        return filteredDecks.equals(other.filteredDecks) &&
                Objects.equals(selectedDeck.getValue(), other.selectedDeck.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(filteredDecks, selectedDeck.getValue());
    }
}
