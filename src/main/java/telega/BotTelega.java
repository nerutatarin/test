package telega;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

public class BotTelega extends TelegramLongPollingBot {

    public static final String COMMAND_ADD = "/add ";
    public static final String COMMAND_HELP = "/help";
    public static final String COMMAND_DEL = "/del ";
    public static final String COMMAND_LIST = "/list";
    public static final String COMMAND_START = "/start";

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {

            SendMessage message = new SendMessage();
            String text = update.getMessage().getText();
            if (text.startsWith(COMMAND_ADD)) {
                String[] strings = text.split(" ", 3);

                if (strings.length < 3) return;
                String noteTitle = strings[1];
                String noteText = strings[2];

                if (noteTitle.isEmpty() || noteText.isEmpty()) return;

                Note zametka = new Note();
                zametka.title = noteTitle;
                zametka.text = noteText;
                zametka.userId = update.getMessage().getChatId();

                NoteOperations operations = new NoteOperations();
                try {
                    operations.addNote(zametka);
                    message.setChatId(zametka.userId).setText("It's Ok!");
                } catch (Exception e) {
                    message.setChatId(zametka.userId).setText("Something wrong, " + e.getMessage());
                }

            } else if (text.equals(COMMAND_HELP)) {
                String helpText = "/add добавить заметку (шаблон: /add title text)\n" +
                        "/del удалить заметку (шаблон: /del id) где id номер заметки\n" +
                        "/list список заметок\n";
                message.setChatId(update.getMessage().getChatId()).setText(helpText);

            } else if (text.equals(COMMAND_LIST)) {
                NoteOperations operations = new NoteOperations();
                Long userId = update.getMessage().getChatId();
                try {
                    operations.listNote(userId);
                    message.setChatId(update.getMessage().getChatId()).setText("Список заметок:");
                } catch (Exception e) {
                    message.setChatId(update.getMessage().getChatId()).setText("Something wrong, " + e.getMessage());
                }

            } else if (text.startsWith(COMMAND_DEL)) {
                String[] strings = text.split(" ", 2);
                if (strings.length < 2) return;
                int noteId = Integer.valueOf(strings[1]);
                if (noteId == -1) return;

                NoteOperations operations = new NoteOperations();
                try {
                    operations.deleteNote(noteId);
                    message.setChatId(update.getMessage().getChatId()).setText("Заметка удалена!");
                } catch (Exception e) {
                    message.setChatId(update.getMessage().getChatId()).setText("Something wrong, " + e.getMessage());
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
