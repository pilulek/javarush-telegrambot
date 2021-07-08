package com.github.javarushcommunity.jrtb.bot;

import com.github.javarushcommunity.jrtb.command.CommandContainer;
import com.github.javarushcommunity.jrtb.service.SendBotMessageServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import static com.github.javarushcommunity.jrtb.command.CommandName.NO;

@Component
public class JavaRushTelegramBot extends TelegramLongPollingBot {

  public static String COMMAND_PREFIX = "/";

//  @Value("${bot.username")
  @Value("javarush_project_bot")
  private String username;

//  @Value("${bot.token")
  @Value("1752666951:AAE2CLrGf4g7Z_6X2vabVP4-D051rItuXjs")
  private String token;

  private final CommandContainer commandContainer;

  public JavaRushTelegramBot() {
    this.commandContainer = new CommandContainer(new SendBotMessageServiceImpl(this));
  }

  @Override
  public String getBotUsername() {
    return username;
  }

  @Override
  public String getBotToken() {
    return token;
  }

  @Override
  public void onUpdateReceived(Update update) {
    if(update.hasMessage() && update.getMessage().hasText()) {
      String message = update.getMessage().getText().trim();
      if(message.startsWith(COMMAND_PREFIX)) {
        String commandIdentifier = message.split(" ")[0].toLowerCase();
        commandContainer.retrieveCommand(commandIdentifier).execute(update);
      } else {
        commandContainer.retrieveCommand(NO.getCommandName()).execute(update);
      }
    }
  }
}
