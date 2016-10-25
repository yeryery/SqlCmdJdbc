package ua.com.juja.yeryery.commands;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import ua.com.juja.yeryery.manager.DatabaseManager;
import ua.com.juja.yeryery.view.View;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class ClearTest {

    private DatabaseManager manager;
    private View view;
    private Command command;

    @Before
    public void setup() {
        manager = mock(DatabaseManager.class);
        view = mock(View.class);
        command = new Clear(view, manager);
    }

    @Test
    public void testClearSelectTableAndConfirm() {
        //given

        String[] tableNames = new String[]{"test", "table"};
        when(manager.getTableNames()).thenReturn(tableNames);
        when(view.read()).thenReturn("1").thenReturn("y").thenReturn("0");

        //when
        command.process("clear");

        //then
        shouldPrint("[Please enter the name or select number of table you need, " +
                "1. test, " +
                "2. table, " +
                "0. cancel (to go back), " +
                //Select table number 1
                "Are you sure you want to clear table 'test'? (y/n), " +
                //yes
                "Table 'test' successfully cleared!]");
    }

    @Test
    public void testClearSelectTableAndDontConfirm() {
        //given

        String[] tableNames = new String[]{"test", "table"};
        when(manager.getTableNames()).thenReturn(tableNames);
        when(view.read()).thenReturn("1").thenReturn("n").thenReturn("0");

        //when
        command.process("clear");

        //then
        shouldPrint("[Please enter the name or select number of table you need, " +
                "1. test, " +
                "2. table, " +
                "0. cancel (to go back), " +
                //Select table number 1
                "Are you sure you want to clear table 'test'? (y/n), " +
                //no
                "The clearing of table 'test' is cancelled, " +
                "Please enter the name or select number of table you need, " +
                "1. test, " +
                "2. table, " +
                "0. cancel (to go back), " +
                //cancel
                "Table clearing canceled]");
    }

    @Test
    public void testClearSelectTableAndNeitherInput() {
        //given

        String[] tableNames = new String[]{"test", "table"};
        when(manager.getTableNames()).thenReturn(tableNames);
        when(view.read()).thenReturn("1").thenReturn("neither").thenReturn("y");

        //when
        command.process("clear");

        //then
        shouldPrint("[Please enter the name or select number of table you need, " +
                "1. test, " +
                "2. table, " +
                "0. cancel (to go back), " +
                //Select table number 1
                "Are you sure you want to clear table 'test'? (y/n), " +
                //neither 'y' nor 'n'
                "Are you sure you want to clear table 'test'? (y/n), " +
                //yes
                "Table 'test' successfully cleared!]");
    }

    @Test
    public void testClearAndCancel() {
        //given

        String[] tableNames = new String[]{"test", "table"};
        when(manager.getTableNames()).thenReturn(tableNames);
        when(view.read()).thenReturn("0");

        //when
        command.process("clear");

        //then
        shouldPrint("[Please enter the name or select number of table you need, " +
                "1. test, " +
                "2. table, " +
                "0. cancel (to go back), " +
                //Select cancel
                "Table clearing canceled]");
    }

    private void shouldPrint(String expected) {
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(view, atLeastOnce()).write(captor.capture());
        assertEquals(expected, captor.getAllValues().toString());
    }

    @Test
    public void testCanProcessList() {
        //when
        boolean canProcess = command.canProcess("clear");

        //then
        assertTrue(canProcess);
    }

    @Test
    public void testCantProcessWrongInput() {
        //when
        boolean canProcess = command.canProcess("wrong");

        //then
        assertFalse(canProcess);
    }
}
