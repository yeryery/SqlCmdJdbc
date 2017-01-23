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

import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Drop.class)
public class DropTest {

    private View view;
    private DatabaseManager manager;
    private Dialog dialog;
    private Command command;
    private static final String TABLE = "test";
    private static final String ACTION = "drop";

    @Before
    public void setup() {
        view = mock(View.class);
        manager = mock(DatabaseManager.class);
        dialog = mock(Dialog.class);
        command = new Drop(view, manager);
    }

    @Test
    public void testDropCommand() throws Exception {
        //given
        mockMethods();

        //when
        command.process(ACTION);

        //then
        verify(view).write("Table 'test' successfully dropped!");
    }

    private void mockMethods() throws Exception {
        PowerMockito.whenNew(Dialog.class).withArguments(view, manager).thenReturn(dialog);
        when(dialog.selectTable(ACTION)).thenReturn(TABLE);
        doNothing().when(dialog).confirmAction(ACTION, TABLE);
    }
}
