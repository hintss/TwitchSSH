package tk.hintss.twitchssh;

import org.jibble.pircbot.*;

import java.io.*;

/**
 * Created by Henry on 7/20/2014.
 */
public class IrcBot extends PircBot {
    ConcatInputStream keyInput;

    boolean control = false;

    public IrcBot(String key, String user, ConcatInputStream keyInput) {
        this.keyInput = keyInput;
        setName(user);
    }

    @Override
    public void onMessage(String channel, String sender, String login, String hostname, String message) {
        System.out.println(sender + ": " + message);

        if (message.startsWith("!type ")) {
            String[] split = message.split(" ");
            if (split.length > 1) {
                String key = split[1].toLowerCase().replaceAll("[^\\x00-\\x7F]", "");
                Character outputKey = null;

                if (key.equals("enter")) {
                    outputKey = '\r';
                } else if (key.equals("backspace")) {
                    outputKey = 8;
                } else if (key.equals("delete")) {
                    outputKey = 127;
                } else if (key.equals("control") || key.equals("ctrl")) {
                    control = !control;
                } else if (key.equals("space")) {
                    outputKey = ' ';
                } else if (key.equals("bell")) {
                    outputKey = 0x07;
                } else if (key.equals("up")) {
                    keyInput.addInputStreams(new ByteArrayInputStream(new char[] {0x1B, '[', 'A'}.toString().getBytes()));
                } else if (key.equals("down")) {
                    keyInput.addInputStreams(new ByteArrayInputStream(new char[] {0x1B, '[', 'B'}.toString().getBytes()));
                } else if (key.equals("left")) {
                    keyInput.addInputStreams(new ByteArrayInputStream(new char[] {0x1B, '[', 'D'}.toString().getBytes()));
                } else if (key.equals("right")) {
                    keyInput.addInputStreams(new ByteArrayInputStream(new char[] {0x1B, '[', 'C'}.toString().getBytes()));
                } else if (key.length() == 1) {
                    char inputKey = key.charAt(0);

                    if (inputKey > 31 && inputKey < 127) {
                        if (control) {
                            control = false;

                            if (inputKey < 96 && inputKey > 63) {
                                outputKey = (char) (inputKey - 64);
                            } else if (inputKey < 123 && inputKey > 96) {
                                outputKey = (char) (inputKey - 96);
                            } else {
                                outputKey = (char) 7;
                            }
                        } else {
                            outputKey = inputKey;
                        }
                    }
                }

                if (outputKey != null) {
                    keyInput.addInputStream(new ByteArrayInputStream(outputKey.toString().getBytes()));
                    if (outputKey == 1) {
                        keyInput.addInputStream(new ByteArrayInputStream("a".getBytes()));
                    }
                }
            }
        }
    }
}
