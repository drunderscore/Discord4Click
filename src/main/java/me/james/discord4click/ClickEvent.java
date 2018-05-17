package me.james.discord4click;

import java.io.*;
import java.util.function.*;
import sx.blah.discord.handle.obj.*;

public class ClickEvent implements Serializable
{
    private IMessage msg;
    private String emoji;
    private Function< IUser, Void > func;
    private int clicks;

    public ClickEvent( IMessage msg, String emoji, Function< IUser, Void > func )
    {
        this.msg = msg;
        this.emoji = emoji;
        this.func = func;
    }

    public void onClick( IUser user )
    {
        clicks++;
        func.apply( user );
    }

    public String getEmoji()
    {
        return emoji;
    }

    public int getClicks()
    {
        return clicks;
    }

    public IMessage getMessage()
    {
        return msg;
    }
}
