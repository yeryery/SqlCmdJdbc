package ua.com.juja.yeryery.controller.commands;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import ua.com.juja.yeryery.controller.commands.Exceptions.CancelException;
import ua.com.juja.yeryery.controller.commands.Utility.Dialog;
import ua.com.juja.yeryery.controller.commands.Utility.TablePrinter;
import ua.com.juja.yeryery.model.*;
import ua.com.juja.yeryery.view.View;

import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Update.class)
public class UpdateTest {

    private View view;
    private DatabaseManager manager;
    private Dialog dialog;
    private TablePrinter tablePrinter;
    private DataEntry definingEntry;
    private DataSet updatingEntries;
    private Command command;
    private static final String TEST_TABLE = "test";
    private static final String ACTION = "update";

    @Before
    public void setup() {
        view = mock(View.class);
        manager = mock(DatabaseManager.class);
        dialog = mock(Dialog.class);
        tablePrinter = mock(TablePrinter.class);
        definingEntry = new DataEntryImpl();
        updatingEntries = new DataSetImpl();
        command = new Update(view, manager);
    }

    @Test
    public void testUpdateStringValue() throws Exception {
        //given
        definingEntry.setEntry("id", 1);
        updatingEntries.put("name", "Bob");
        mockMethods();

        //when
        command.process(ACTION);

        //then
        verify(tablePrinter, atLeastOnce()).print();
        verify(view).write("You have successfully updated table 'test'");
    }

    @Test
    public void testUpdateIntValue() throws Exception {
        //given
        definingEntry.setEntry("name", "John");
        updatingEntries.put("id", "10");
        mockMethods();

        //when
        command.process(ACTION);

        //then
        verify(tablePrinter, atLeastOnce()).print();
        verify(view).write("You have successfully updated table 'test'");
    }

    @Test
    public void testUpdateValueEqualsToUpdated() throws Exception {
        //given
        definingEntry.setEntry("id", 1);
        updatingEntries.put("name", "John");
        mockMethods();

        //when
        try {
            command.process(ACTION);
        } catch (CancelException e) {
            //do nothing
        }

        //then
        verify(tablePrinter, never()).print();
        verify(view).write("Your entries are equivalent to the updated");
    }

    private void mockMethods() throws Exception {
        PowerMockito.whenNew(Dialog.class).withArguments(view, manager).thenReturn(dialog);
        when(dialog.selectTable(ACTION)).thenReturn(TEST_TABLE);
        when(dialog.findRow(TEST_TABLE, ACTION)).thenReturn(definingEntry);
        when(dialog.getNewEntries(TEST_TABLE, ACTION)).thenReturn(updatingEntries);
        when(manager.getDataContent(TEST_TABLE)).thenReturn(TestTable.getTableContent());
        PowerMockito.whenNew(TablePrinter.class).withArguments(view, manager, TEST_TABLE).thenReturn(tablePrinter);
    }
}
