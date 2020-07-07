package telega;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

import java.util.List;

public class BotTelega extends TelegramLongPollingBot {

    public static final String COMMAND_ADD = "/add ";
    public static final String COMMAND_HELP = "/help";
    public static final String COMMAND_DEL = "/del ";
    public static final String COMMAND_LIST = "/list";
    //public static final String COMMAND_START = "/start";

    @Override
    public void onUpdateReceived(Update update) {


        if (update.hasMessage() && update.getMessage().hasText()) {

            SendMessage message = new SendMessage();
            String str = update.getMessage().getText();

            NoteService operations = new DBNoteServiceImpl();

            Long userId = update.getMessage().getChatId();
            if (str.startsWith(COMMAND_ADD)) {
                String[] strings = str.split(" ", 3);

                if (strings.length < 3) return;
                String noteTitle = strings[1];
                String noteText = strings[2];

                if (noteTitle.isEmpty() || noteText.isEmpty()) return;

                Note note = new Note(noteTitle, noteText, userId);

                try {
                    operations.addNote(note);
                    message.setChatId(note.getUserId()).setText("Заметка добавлена!");
                } catch (Exception e) {
                    message.setChatId(note.getUserId()).setText("Что-то не так, " + e.getMessage());
                }

            } else if (str.equals(COMMAND_HELP)) {
                String helpText = "/add добавить заметку (шаблон: /add title text)\n" +
                        "/del удалить заметку (шаблон: /del id) где id номер заметки\n" +
                        "/list список заметок\n";
                message.setChatId(userId).setText(helpText);

            } else if (str.equals(COMMAND_LIST)) {

                try {
                    List<Note> userNoteList = operations.getUserNoteList(userId);

                    String messageText = "Список заметок: \n";
                    for (Note note : userNoteList) {
                        messageText += "ID: " + note.getId() + " Title: " + note.getTitle() + "\n";
                    }

                    message.setChatId(userId).setText(messageText);
                } catch (Exception e) {
                    message.setChatId(userId).setText("Что-то не так, " + e.getMessage());
                }

            } else if (str.startsWith(COMMAND_DEL)) {
                String[] strings = str.split(" ", 2);
                if (strings.length < 2) return;
                int noteId = Integer.parseInt(strings[1]);
                if (noteId == -1) return;

                try {
                    operations.deleteNote(noteId);
                    message.setChatId(userId).setText("Заметка удалена!");
                } catch (Exception e) {
                    message.setChatId(userId).setText("Что-то не так, " + e.getMessage());
                }

            }
            try {
                execute(message); // Call method to send the message

            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String getBotUsername() {

        return "Ne_ru_bot";
    }

    @Override
    public String getBotToken() {

        return "1079315483:AAEE_z3E8cYpTJDMcDTGCtypt4oHLXsnasc";
    }

    @Override
    public void clearWebhook() throws TelegramApiRequestException {

    }
}
