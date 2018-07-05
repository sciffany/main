package seedu.address.model;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.address.model.person.UniquePersonList;

public class UniquePersonListTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void asUnmodifiableObservableList_modifyList_throwsUnsupportedOperationException() {
        UniquePersonList uniquePersonList = new UniquePersonList();
        thrown.expect(UnsupportedOperationException.class);
        uniquePersonList.asUnmodifiableObservableList().remove(0);
    }
}