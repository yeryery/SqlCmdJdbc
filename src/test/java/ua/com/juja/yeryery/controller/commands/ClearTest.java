package ua.com.juja.yeryery.controller.commands;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import ua.com.juja.yeryery.controller.commands.Utility.Dialog;
import ua.com.juja.yeryery.model.DatabaseManager;
import ua.com.juja.yeryery.view.View;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Clear.class)
public class ClearTest {

    private View view;
    private DatabaseManager manager;
    private Dialog dialog;
    private Command command;
    private static final String TEST_TABLE = "test";
    private static final String ACTION = "clear";

    @Before
    public void setup() {
        view = mock(View.class);
        manager = mock(DatabaseManager.class);
        dialog = mock(Dialog.class);
        command = new Clear(view, manager);
    }

    @Test
    public void testClearCommand() throws Exception {
        //given
        PowerMockito.whenNew(Dialog.class).withArguments(view, manager).thenReturn(dialog);
        when(dialog.selectTable(ACTION)).thenReturn(TEST_TABLE);
        doNothing().when(dialog).confirmAction(ACTION, TEST_TABLE);

        //when
        command.process(ACTION);

        //then
        verify(view).write("Table 'test' successfully cleared!");
    }

    @Test
    public void testCanProcessClear() {
        //when
        boolean canProcess = command.canProcess(ACTION);

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
