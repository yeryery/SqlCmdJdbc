package ua.com.juja.yeryery.commands;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import ua.com.juja.yeryery.manager.DatabaseManager;
import ua.com.juja.yeryery.view.View;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class DropTest {

    private DatabaseManager manager;
    private View view;
    private Command command;

    @Before
    public void setup() {
        manager = mock(DatabaseManager.class);
        view = mock(View.class);
        command = new Drop(view, manager);
    }

    @Test
    public void testDropSelectTableAndConfirm() {
        //given

        String[] tableNames = new String[]{"test", "table"};
        when(manager.getTableNames()).thenReturn(tableNames);
        when(view.read()).thenReturn("1").thenReturn("y").thenReturn("0");

        //when
        command.process("drop");

        //then
        shouldPrint("[Please enter the name or select number of table you need, " +
                "1. test, " +
                "2. table, " +
                "0. cancel (to go back), " +
                //Select table number 1
                "Are you sure you want to drop table 'test'? (y/n), " +
                //yes
                "Table 'test' successfully dropped!]");
    }

    @Test
    public void testDropSelectTableAndDontConfirm() {
        //given

        String[] tableNames = new String[]{"test", "table"};
        when(manager.getTableNames()).thenReturn(tableNames);
        when(view.read()).thenReturn("1").thenReturn("n").thenReturn("0");

        //when
        command.process("drop");

        //then
        shouldPrint("[Please enter the name or select number of table you need, " +
                "1. test, " +
                "2. table, " +
                "0. cancel (to go back), " +
                //Select table number 1
                "Are you sure you want to drop table 'test'? (y/n), " +
                //no
                "The dropping of table 'test' is cancelled, " +
                "Please enter the name or select number of table you need, " +
                "1. test, " +
                "2. table, " +
                "0. cancel (to go back), " +
                //cancel
                "Table dropping canceled]");
    }

    @Test
    public void testDropSelectTableAndNeitherInput() {
        //given

        String[] tableNames = new String[]{"test", "table"};
        when(manager.getTableNames()).thenReturn(tableNames);
        when(view.read()).thenReturn("1").thenReturn("neither").thenReturn("y");

        //when
        command.process("drop");

        //then
        shouldPrint("[Please enter the name or select number of table you need, " +
                "1. test, " +
                "2. table, " +
                "0. cancel (to go back), " +
                //Select table number 1
                "Are you sure you want to drop table 'test'? (y/n), " +
                //neither 'y' nor 'n'
                "Are you sure you want to drop table 'test'? (y/n), " +
                //yes
                "Table 'test' successfully dropped!]");
    }

    @Test
    public void testDropAndCancel() {
        //given

        String[] tableNames = new String[]{"test", "table"};
        when(manager.getTableNames()).thenReturn(tableNames);
        when(view.read()).thenReturn("0");

        //when
        command.process("drop");

        //then
        shouldPrint("[Please enter the name or select number of table you need, " +
                "1. test, " +
                "2. table, " +
                "0. cancel (to go back), " +
                //Select cancel
                "Table dropping canceled]");
    }

    private void shouldPrint(String expected) {
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(view, atLeastOnce()).write(captor.capture());
        assertEquals(expected, captor.getAllValues().toString());
    }

    @Test
    public void testCanProcessList() {
        //when
        boolean canProcess = command.canProcess("drop");

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
