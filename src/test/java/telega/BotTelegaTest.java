package telega;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ BotTelega.class })
public class BotTelegaTest
{
    @Mock
    BotTelega telegramBot;
    @Mock
    Update update;
    @Mock
    Chat chat;
    @Mock
    Message message;

    DBNoteServiceImpl noteService = PowerMockito.mock( DBNoteServiceImpl.class );

    @Before
    public void setUp() throws Exception
    {
        doCallRealMethod().when( telegramBot ).onUpdateReceived( any( Update.class ) );
        when( update.hasMessage() ).thenReturn( true );
        when( update.getMessage() ).thenReturn( message );
        when( message.hasText() ).thenReturn( true );
        when( telegramBot.execute( any( SendMessage.class ) ) ).thenReturn( null );
    }

    @Test
    public void command_start_test() throws TelegramApiException
    {
        String commandStart = "/start";
        String result = "/add добавить заметку (шаблон: /add title text)\n" +
                "/del удалить заметку (шаблон: /del id) где id номер заметки\n" +
                "/list список заметок\n";

        when( message.getText() ).thenReturn( commandStart );

        telegramBot.onUpdateReceived( update );

        verify( telegramBot ).execute( ArgumentMatchers.<SendMessage> argThat( message -> result.equals( message.getText() ) ) );
    }

    @Test
    public void command_add_default() throws Exception
    {
        String command = "/add title text";
        String result = "Заметка добавлена!";
        Note resultNote = new Note( "title", "text", 0 );

        when( message.getText() ).thenReturn( command );
        when( message.getChat() ).thenReturn( chat );
        when( chat.getId() ).thenReturn( 0L );

        PowerMockito.whenNew( DBNoteServiceImpl.class ).withNoArguments().thenReturn( noteService );
        doNothing().when( noteService ).addNote( any( Note.class ) );

        telegramBot.onUpdateReceived( update );

        verify( noteService ).addNote( argThat( note -> resultNote.getTitle().equals( note.getTitle() )
                && resultNote.getText().equals( note.getText() ) && resultNote.getUserId() == note.getUserId() ) );
        verify( telegramBot ).execute( ArgumentMatchers.<SendMessage> argThat( message -> result.equals( message.getText() ) ) );
    }

    @Test
    public void command_add_without_text() throws Exception
    {
        String command = "/add title";

        when( message.getText() ).thenReturn( command );
        when( message.getChat() ).thenReturn( chat );
        when( chat.getId() ).thenReturn( 0L );

        telegramBot.onUpdateReceived( update );

        verify( noteService, times( 0 ) ).addNote( any( Note.class ) );
        verify( telegramBot, times( 0 ) ).execute( any( SendMessage.class ) );
    }

    @Test
    public void command_add_without_title_and_text() throws Exception
    {
        String command = "/add";

        when( message.getText() ).thenReturn( command );
        when( message.getChat() ).thenReturn( chat );
        when( chat.getId() ).thenReturn( 0L );

        telegramBot.onUpdateReceived( update );

        verify( noteService, times( 0 ) ).addNote( any( Note.class ) );
        verify( telegramBot, times( 0 ) ).execute( any( SendMessage.class ) );
    }

    @Test
    public void command_add_and_text_with_whitespaces() throws Exception
    {
        String command = "/add title text with whitespaces";
        String result = "Заметка добавлена!";
        Note resultNote = new Note( "title", "text with whitespaces", 0 );

        when( message.getText() ).thenReturn( command );
        when( message.getChat() ).thenReturn( chat );
        when( chat.getId() ).thenReturn( 0L );

        PowerMockito.whenNew( DBNoteServiceImpl.class ).withNoArguments().thenReturn( noteService );
        doNothing().when( noteService ).addNote( any( Note.class ) );

        telegramBot.onUpdateReceived( update );

        verify( noteService ).addNote( argThat( note -> resultNote.getTitle().equals( note.getTitle() )
                && resultNote.getText().equals( note.getText() ) && resultNote.getUserId() == note.getUserId() ) );
        verify( telegramBot ).execute( ArgumentMatchers.<SendMessage> argThat( message -> result.equals( message.getText() ) ) );
    }

    @Test
    public void command_add_note_service_throw_exception() throws Exception
    {
        String command = "/add title text";
        String result = "Что-то не так, something wrong";
        Note resultNote = new Note( "title", "text", 0 );

        when( message.getText() ).thenReturn( command );
        when( message.getChat() ).thenReturn( chat );
        when( chat.getId() ).thenReturn( 0L );

        PowerMockito.whenNew( DBNoteServiceImpl.class ).withNoArguments().thenReturn( noteService );
        doThrow( new Exception( "something wrong" ) ).when( noteService ).addNote( any( Note.class ) );

        telegramBot.onUpdateReceived( update );

        verify( noteService ).addNote( argThat( note -> resultNote.getTitle().equals( note.getTitle() )
                && resultNote.getText().equals( note.getText() ) && resultNote.getUserId() == note.getUserId() ) );
        verify( telegramBot ).execute( ArgumentMatchers.<SendMessage> argThat( message -> result.equals( message.getText() ) ) );
    }

    @Test
    public void command_del_default() throws Exception
    {
        String command = "/del 1";
        String result = "Заметка удалена!";
        int deleteNoteId = 1;

        when( message.getText() ).thenReturn( command );
        when( message.getChat() ).thenReturn( chat );
        when( chat.getId() ).thenReturn( 0L );

        PowerMockito.whenNew( DBNoteServiceImpl.class ).withNoArguments().thenReturn( noteService );
        doNothing().when( noteService ).deleteNote( anyInt() );

        telegramBot.onUpdateReceived( update );

        verify( noteService ).deleteNote( deleteNoteId );
        verify( telegramBot ).execute( ArgumentMatchers.<SendMessage> argThat( message -> result.equals( message.getText() ) ) );
    }

    @Test
    public void command_del_with_id_text() throws Exception
    {
        String command = "/del qwe";

        when( message.getText() ).thenReturn( command );
        when( message.getChat() ).thenReturn( chat );
        when( chat.getId() ).thenReturn( 0L );

        telegramBot.onUpdateReceived( update );

        verify( noteService, times( 0 ) ).deleteNote( anyInt() );
        verify( telegramBot, times( 0 ) ).execute( any( SendMessage.class ) );
    }

    @Test
    public void command_del_without_id() throws Exception
    {
        String command = "/del";

        when( message.getText() ).thenReturn( command );
        when( message.getChat() ).thenReturn( chat );
        when( chat.getId() ).thenReturn( 0L );

        telegramBot.onUpdateReceived( update );

        verify( noteService, times( 0 ) ).deleteNote( anyInt() );
        verify( telegramBot, times( 0 ) ).execute( any( SendMessage.class ) );
    }

    @Test
    public void command_del_note_service_throw_exception() throws Exception
    {
        String command = "/del 1";
        String result = "Что-то не так, something wrong";
        int deleteNoteId = 1;

        when( message.getText() ).thenReturn( command );
        when( message.getChat() ).thenReturn( chat );
        when( chat.getId() ).thenReturn( 0L );

        PowerMockito.whenNew( DBNoteServiceImpl.class ).withNoArguments().thenReturn( noteService );
        doThrow( new Exception( "something wrong" ) ).when( noteService ).deleteNote( anyInt() );

        telegramBot.onUpdateReceived( update );

        verify( noteService ).deleteNote( deleteNoteId );
        verify( telegramBot ).execute( ArgumentMatchers.<SendMessage> argThat( message -> result.equals( message.getText() ) ) );
    }

    @Test
    public void command_list_default() throws Exception
    {
        String command = "/list";
        String result = "Список заметок: \nID: 0 Title: title\n";
        List<Note> notes = Arrays.asList( new Note( 0, "title", "text", 0 ) );

        when( message.getText() ).thenReturn( command );
        when( message.getChat() ).thenReturn( chat );
        when( chat.getId() ).thenReturn( 0L );

        PowerMockito.whenNew( DBNoteServiceImpl.class ).withNoArguments().thenReturn( noteService );
        doReturn( notes ).when( noteService ).getUserNoteList( anyLong() );

        telegramBot.onUpdateReceived( update );

        verify( noteService ).getUserNoteList( anyLong() );
        verify( telegramBot ).execute( ArgumentMatchers.<SendMessage> argThat( message -> result.equals( message.getText() ) ) );
    }

    @Test
    public void command_list_note_service_throw_exception() throws Exception
    {
        String command = "/list";
        String result = "Что-то не так, something wrong";

        when( message.getText() ).thenReturn( command );
        when( message.getChat() ).thenReturn( chat );
        when( chat.getId() ).thenReturn( 0L );

        PowerMockito.whenNew( DBNoteServiceImpl.class ).withNoArguments().thenReturn( noteService );
        doThrow( new Exception( "something wrong" ) ).when( noteService ).getUserNoteList( anyLong() );

        telegramBot.onUpdateReceived( update );

        verify( noteService ).getUserNoteList( anyLong() );
        verify( telegramBot ).execute( ArgumentMatchers.<SendMessage> argThat( message -> result.equals( message.getText() ) ) );
    }

    @Test
    public void unknown_command() throws Exception
    {
        String command = "/qwerty";
        String result = "/qwerty";

        when( message.getText() ).thenReturn( command );
        when( message.getChat() ).thenReturn( chat );
        when( chat.getId() ).thenReturn( 0L );

        telegramBot.onUpdateReceived( update );

        verify( telegramBot ).execute( ArgumentMatchers.<SendMessage> argThat( message -> result.equals( message.getText() ) ) );
    }
}