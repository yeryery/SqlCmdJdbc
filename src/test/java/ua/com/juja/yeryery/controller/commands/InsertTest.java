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
import ua.com.juja.yeryery.model.DataSet;
import ua.com.juja.yeryery.model.DataSetImpl;
import ua.com.juja.yeryery.model.DatabaseManager;
import ua.com.juja.yeryery.view.View;

import java.sql.SQLException;

import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Insert.class)
public class InsertTest {

    private View view;
    private DatabaseManager manager;
    private Dialog dialog;
    private TablePrinter tablePrinter;
    private DataSet insertedRow;
    private Command command;
    private static final String TEST_TABLE = "test";
    private static final String ACTION = "insert";

    @Before
    public void setup() {
        view = mock(View.class);
        manager = mock(DatabaseManager.class);
        dialog = mock(Dialog.class);
        tablePrinter = mock(TablePrinter.class);
        insertedRow = new DataSetImpl();
        command = new Insert(view, manager);
    }

    @Test
    public void testInsertCommand() throws Exception {
        //given
        mockMethods();

        //when
        command.process(ACTION);

        //then
        verify(tablePrinter, atLeastOnce()).print();
        verify(view).write("You have successfully entered new data into the table 'test'");
    }

    @Test
    public void testInsertThrowingSqlException() throws Exception {
        //given
        mockMethods();
        SQLException exception = new SQLException("SQL exception message");
        doThrow(exception).when(manager).insert(TEST_TABLE, insertedRow);

        //when
        try {
            command.process(ACTION);
        } catch (CancelException e) {
            //do nothing
        }

        //then
        verify(tablePrinter, never()).print();
        verify(view).write("SQL exception message");
    }

    private void mockMethods() throws Exception {
        PowerMockito.whenNew(Dialog.class).withArguments(view, manager).thenReturn(dialog).thenThrow(new CancelException());
        when(dialog.selectTable(ACTION)).thenReturn(TEST_TABLE);
        when(dialog.getNewEntries(TEST_TABLE, ACTION)).thenReturn(insertedRow);
        PowerMockito.whenNew(TablePrinter.class).withArguments(view, manager, TEST_TABLE).thenReturn(tablePrinter);
    }
}
