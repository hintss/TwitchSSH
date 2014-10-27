package tk.hintss.twitchssh;

import com.jcraft.jsch.*;

import java.io.*;

/**
 * Created by Henry on 7/20/2014.
 */
public class TwitchSsh {
    static String twitchuser = "hintss";
    static String oauthKey = "<censored>";

    static String sshHost = "localhost";
    static String sshUser = "twitch";
    static String sshPw = "asdf";

    public static void main(String[] args) {


        ConcatInputStream keyInput = new ConcatInputStream();

        keyInput.addInputStream(new ByteArrayInputStream("screen -x twitch\r".getBytes()));

        IrcThread irc = new IrcThread(oauthKey, twitchuser, keyInput);

        new Thread(irc).start();

        // begin jsch code stuff here vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv

        try{
            JSch jsch=new JSch();

            Session session=jsch.getSession(sshUser, sshHost, 22);

            session.setPassword(sshPw);

            // It must not be recommended, but if you want to skip host-key check,
            // invoke following,
             session.setConfig("StrictHostKeyChecking", "no");

            //session.connect();
            session.connect(30000);   // making a connection with timeout.

            Channel channel=session.openChannel("shell");

            // Enable agent-forwarding.
            //((ChannelShell)channel).setAgentForwarding(true);

            channel.setInputStream(keyInput);
      /*
      // a hack for MS-DOS prompt on Windows.
      channel.setInputStream(new FilterInputStream(System.in){
          public int read(byte[] b, int off, int len)throws IOException{
            return in.read(b, off, (len>1024?1024:len));
          }
        });
       */

            //channel.setOutputStream(System.out);
            channel.setOutputStream(null);

      /*
      // Choose the pty-type "vt102".
      ((ChannelShell)channel).setPtyType("vt102");
      */

      /*
      // Set environment variable "LANG" as "ja_JP.eucJP".
      ((ChannelShell)channel).setEnv("LANG", "ja_JP.eucJP");
      */

            //channel.connect();
            channel.connect(3*1000);
        }
        catch(Exception e){
            System.out.println(e);
        }
    }

    public static void sendCmd(String cmd) {
        try{
            JSch jsch=new JSch();

            Session session=jsch.getSession(sshUser, sshHost, 22);

            session.setPassword(sshPw);

            // It must not be recommended, but if you want to skip host-key check,
            // invoke following,
            session.setConfig("StrictHostKeyChecking", "no");

            //session.connect();
            session.connect(30000);   // making a connection with timeout.

            Channel channel=session.openChannel("shell");

            // Enable agent-forwarding.
            //((ChannelShell)channel).setAgentForwarding(true);

            channel.setInputStream(keyInput);
            channel.setOutputStream(null);
            channel.connect(3*1000);
        }
        catch(Exception e){
            System.out.println(e);
        }
    }
}
