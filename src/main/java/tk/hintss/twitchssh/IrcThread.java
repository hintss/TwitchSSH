package tk.hintss.twitchssh;

import org.jibble.pircbot.*;

import java.io.*;

/**
 * Created by Henry on 7/20/2014.
 */
public class IrcThread implements Runnable {
    String oauthKey;
    String channel;
    ConcatInputStream keyInput;

    public IrcThread(String oauthKey, String channel, ConcatInputStream keyInput) {
        this.oauthKey = oauthKey;
        this.channel = channel;
        this.keyInput = keyInput;
    }

    @Override
    public void run() {
        IrcBot bot = new IrcBot(oauthKey, channel, keyInput);
        try {
            bot.connect("irc.twitch.tv", 6667, oauthKey);
            bot.joinChannel("#" + channel);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IrcException e) {
            e.printStackTrace();
        }
    }
}
