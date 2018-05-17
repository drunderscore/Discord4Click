# Discord4Click
A Discord library to allow click events on messages.

## What does this library allow?
This library allows for a user to click a Discord message and have an event be triggered, all without typing.

Discord currently does not have an easy solution to a simple click, so this library helps this by letting a user react to a message with a specific reaction and run a lambda.

## How do I use this library?
**Simple!**

### In order to use this library, you need to do a few things first:
* Call the init method. (`Discord4Click#init`)
* Register a new instance of the class with your bot. (`IDiscordClient#getDispatcher#registerListener`)
* Add a click event to a IMessage. (`Discord4Click#addClickEvent(IMessage msg, String emojiUnicodeCode, boolean shouldSerialize, boolean addBotReaction, Function<String, Void> func`)

Now you are ready to add clickable buttons to Discord messages!

```java
import me.james.messageclick.*;
import sx.blah.discord.api.*;
import sx.blah.discord.api.events.*;
import sx.blah.discord.handle.impl.events.*;
import sx.blah.discord.util.*;

public class Test {
    public static IDiscordClient bot;

    public static void main(String[] args) {
        try {
            bot = new ClientBuilder().withToken("xxx").login();
        } catch (DiscordException e) {
            //TODO: Catch this a better way maybe?
            e.printStackTrace();
        }
        bot.getDispatcher().registerListener(new Test());
    }

    @EventSubscriber
    public void onReady(ReadyEvent e) {
        Discord4Click.init();  //Init the bot only on the ready event.
        Test.bot.getDispatcher().registerListener(new Discord4Click()); //Register it as a listener only on the ready event.
    }

    @EventSubscriber
    public void onMessageReceived(MessageReceivedEvent e) {
        if (e.getMessage().getAuthor().getLongID().equals(bot.getOurUser().getLongID())) {
            try {
                Discord4Click.addClickEvent(e.getMessage(), "\u2767", true, true, clickerUserid -> {
                    System.out.println(String.format("LOL! %s actually clicked the button.", clickerUserid));
                    return null;
                });
            } catch (RateLimitException | DiscordException | MissingPermissionsException e1) {
                //TODO: Catch this a better way maybe?
                e1.printStackTrace();
            }
        }
    }
}
```
This code will add the ❗ reaction to every message this bot sends. When other users click this reaction, it will apply the Function provided. **(NOTICE: The Function can be ran multiple times by removing and adding the reaction)**
##I'm a server owner! What do I do.
**Nothing!**

Evidently enough, this isn't for server owner, this is for Discord Bot's and their programmers to use. Yell at the owner of your favorite bot to incorperate this into the bot they made. *(don't actually yell at them to do it, programmers -- well people in general -- don't like in when you yell at them :crying_cat_face:)*
