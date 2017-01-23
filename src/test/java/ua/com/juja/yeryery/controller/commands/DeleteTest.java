package ua.com.juja.yeryery.controller.commands;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import ua.com.juja.yeryery.controller.commands.Utility.Dialog;
import ua.com.juja.yeryery.controller.commands.Utility.TablePrinter;
import ua.com.juja.yeryery.model.DataEntry;
import ua.com.juja.yeryery.model.DataEntryImpl;
import ua.com.juja.yeryery.model.DatabaseManager;
import ua.com.juja.yeryery.view.View;

import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Delete.class)
public class DeleteTest {

    private View view;
    private DatabaseManager manager;
    private Dialog dialog;
    private TablePrinter tablePrinter;
    private DataEntry definingEntry;
    private Command command;
    private static final String TABLE = "test";
    private static final String ACTION = "delete";

    @Before
    public void setup() {
        view = mock(View.class);
        manager = mock(DatabaseManager.class);
        dialog = mock(Dialog.class);
        tablePrinter = mock(TablePrinter.class);
        definingEntry = new DataEntryImpl();
        command = new Delete(view, manager);
    }

    @Test
    public void testDeleteCommand() throws Exception {
        //given
        mockMethods();

        //when
        command.process(ACTION);

        //then
        verify(tablePrinter, atLeastOnce()).print();
        verify(view).write("You have successfully deleted data from 'test'");
    }

    private void mockMethods() throws Exception {
        PowerMockito.whenNew(Dialog.class).withArguments(view, manager).thenReturn(dialog);
        when(dialog.selectTable(ACTION)).thenReturn(TABLE);
        when(dialog.findRow(TABLE, ACTION)).thenReturn(definingEntry);
        PowerMockito.whenNew(TablePrinter.class).withArguments(view, manager, TABLE).thenReturn(tablePrinter);
    }
}
