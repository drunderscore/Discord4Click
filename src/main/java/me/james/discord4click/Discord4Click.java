package me.james.discord4click;

import com.vdurmont.emoji.*;
import java.util.*;
import java.util.function.*;
import sx.blah.discord.api.events.*;
import sx.blah.discord.handle.impl.events.guild.channel.message.*;
import sx.blah.discord.handle.impl.events.guild.channel.message.reaction.*;
import sx.blah.discord.handle.obj.*;
import sx.blah.discord.util.*;

public class Discord4Click
{
    private static HashMap< IMessage, ArrayList< ClickEvent > > clickEvents = new HashMap<>();

    public static void addClickEvent( IMessage msg, String emoji, Function< IUser, Void > func, boolean block )
    {
        if ( !clickEvents.containsKey( msg ) )
            clickEvents.put( msg, new ArrayList<>() );
        clickEvents.get( msg ).add( new ClickEvent( msg, emoji, func ) );
        RequestBuffer.RequestFuture< Void > req = RequestBuffer.request( () -> msg.addReaction( EmojiManager.getByUnicode( emoji ) ) );
        if ( block )
            req.get();
    }

    public static void addClickEvent( IMessage msg, String emoji, Function< IUser, Void > func )
    {
        addClickEvent( msg, emoji, func, false );
    }

    public static void init()
    {
        System.out.println( "Discord4Click init" );
    }

    @EventSubscriber
    public void onMessageRemove( MessageDeleteEvent e )
    {
        clickEvents.remove( e.getMessage() );
    }

    @EventSubscriber
    public void onAddReact( ReactionAddEvent e )
    {
        if ( clickEvents.containsKey( e.getMessage() ) )
            for ( ClickEvent ce : clickEvents.get( e.getMessage() ) )
            {
                if ( ce.getEmoji().equals( e.getReaction().getEmoji().getName() ) && e.getUser() != e.getClient().getOurUser() )
                {
                    ce.onClick( e.getUser() );
                    RequestBuffer.request( () -> ce.getMessage().removeReaction( e.getUser(), e.getReaction() ) );
                }
            }
    }
}
