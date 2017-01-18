package ua.com.juja.yeryery.controller.commands;

import org.junit.Before;
import org.junit.Test;
import ua.com.juja.yeryery.model.DatabaseManager;
import ua.com.juja.yeryery.view.View;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class ContentTest {

    private DatabaseManager manager;
    private View view;
    private Command command;

    @Before
    public void setup() {
        manager = mock(DatabaseManager.class);
        view = mock(View.class);
        command = new Content(view, manager);
    }

    @Test
    public void testList() {
        //given
        Set<String> tableNames = new LinkedHashSet<>(Arrays.asList("test", "users"));
        when(manager.getTableNames()).thenReturn(tableNames);

        //when
        command.process("content");

        //then
        verify(view).write("[test, users]");
    }

    @Test
    public void testCanProcessList() {
        //when
        boolean canProcess = command.canProcess("content");

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
