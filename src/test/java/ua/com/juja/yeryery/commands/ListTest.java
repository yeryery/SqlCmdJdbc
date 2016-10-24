package ua.com.juja.yeryery.commands;

import ua.com.juja.yeryery.manager.DatabaseManager;
import ua.com.juja.yeryery.view.View;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class ListTest {

    private DatabaseManager manager;
    private View view;
    private Command command;

    @Before
    public void setup() {
        manager = mock(DatabaseManager.class);
        view = mock(View.class);
        command = new List(view, manager);
    }

    @Test
    public void TestList() {
        //given
        String[] tableNames = new String[] {"test", "ttable"};
        when(manager.getTableNames()).thenReturn(tableNames);

        //when
        command.process("list");

        //then
        verify(view).write("[test, ttable]");
    }

    @Test
    public void TestCanProcessList() {
        //when
        boolean canProcess = command.canProcess("list");

        //then
        assertTrue(canProcess);
    }

    @Test
    public void TestCantProcessWrongInput() {
        //when
        boolean canProcess = command.canProcess("wrong");

        //then
        assertFalse(canProcess);
    }
}