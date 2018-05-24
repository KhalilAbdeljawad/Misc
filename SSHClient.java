/**
 * @desc A SSH Connectio class
 *
 * Developed by Khalil Abdeljawad
 * 
 */
import com.jcraft.jsch.*;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.UserInfo;
import MySQL;



import java.awt.*;
import javax.swing.*;
import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;



public class SSHClient {
    public  String passwdd;
    public static String user;
    public static String passwd;
    public static String Host, host ;
    boolean hostChecked = false;
    static int breakConnected = 0;
    String tryCommand = "ls";

    private Session session;
    private Channel channel;
    public  boolean sshIsConnected;


    public String getUser(){return this.user;}
    public String getPasswd(){return this.passwd;}

    public SSHClient(String host, String user, String passwd)
    {
        this.host = host;
        this.user = user;
        this.passwd = passwd;
        passwdd = passwd+"   "+user+"   "+host;
        Host = user+"@"+host;



        try {
            JSch jsch = new JSch();
            //   System.out.println(Host);

           //user = host.substring(0, host.indexOf('@'));
           // host = host.substring(host.indexOf('@') + 1);

            session = jsch.getSession(user, host, 22);

            UserInfo ui = new MyUserInfo();
            session.setUserInfo(ui);
            session.setTimeout(9500);
            session.connect();

            String command = tryCommand;

            channel = session.openChannel("exec");
            // man sudo
            //   -S  The -S (stdin) option causes sudo to read the password from the
            //       standard input instead of the terminal device.
            //   -p  The -p (prompt) option allows you to override the default
            //       password prompt and use a custom one.
            ((ChannelExec) channel).setCommand(command);


            InputStream in = channel.getInputStream();
            OutputStream out = channel.getOutputStream();
            ((ChannelExec) channel).setErrStream(System.err);

            channel.connect();

            //out.write((sudo_pass+"\n").getBytes());
            out.flush();
            String str = null;

            byte[] tmp = new byte[1024];
            while (true) {
                while (in.available() > 0) {
                    int i = in.read(tmp, 0, 1024);
                    if (i < 0) break;
                    //    System.out.print(new String(tmp, 0, i));
                    //     str = new String(tmp, 0, i);

                }
                if (channel.isClosed()) {
                    if(in.available()>0) continue;
                    //   System.out.println("exit-status: "+channel.getExitStatus());
                   // channel.connect(3*1000);
                    break;
                }
                try {
                    Thread.sleep(000);
                } catch (Exception ee) {
                }

            }
           // channel.disconnect();
          //  session.disconnect();

            //  System.out.println(str);
            breakConnected = 0;
            sshIsConnected = true;

        } catch (Exception e) {
            // boolean internet = Main.netIsAvailable();
            String ip = "";
            try {
                ip = InetAddress.getLocalHost().getHostAddress();
            } catch (UnknownHostException e1) {
                e1.printStackTrace();
            }

            /*
            if (!Host.equals("admin@192.168.10.1")) {//10.0.8.50
                Host = "admin@192.168.10.1";
                try {
                    return isConnected();

                } catch (Exception a) {
                    System.out.println("Error in isConnected");
                    return false;
                }
            } else if (!Host.equals("ubnt@192.168.1.20") && ip.equals("192.168.1.21")) {
                Host = "ubnt@192.168.1.20";
                try {
                    //        if(!hostChecked)return false;
                    // if(Main.netIsAvailable()) return false;
                    //hostChecked=true;
                    System.out.println("B C = "+breakConnected);
                    if (breakConnected++ == 4) {
                        // Main.gfaStatus.setText("لا يمكن الاتصال بالهوائي، قم بتغيير عنوان الانترنت للجهاز إلى عنوان ديناميكي");
                        breakConnected=0;
                        //  Main.changeIpFor10();
                        return false;
                    }

                    return isConnected();

                } catch (Exception a) {
                    System.out.println("Error in isConnected");
                    return false;
                }
            }
            */

          //  System.out.println("Error in connect with "+host);
            sshIsConnected = false;
            // System.out.println(e);
            //   return false;
        }
    }

    public boolean isConnected() {

     //   System.out.println("Error in connect with "+host);
return sshIsConnected;

    }

    public String exec(String command) {
        if (!sshIsConnected) return null;
        try {
          //  JSch jsch = new JSch();


            // see for the host and admin and password change between reset mode and configured mode
       //     String host = Host;


          //  String user = host.substring(0, host.indexOf('@'));
          //  host = host.substring(host.indexOf('@') + 1);

        //    Session session = jsch.getSession(user, host, 22);

        //    UserInfo ui = new MyUserInfo();
       //     session.setUserInfo(ui);
       //    session.setTimeout(2500);
        //    session.connect();


         //   Channel channel = session.openChannel("exec");

            // man sudo
            //   -S  The -S (stdin) option causes sudo to read the password from the
            //       standard input instead of the terminal device.
            //   -p  The -p (prompt) option allows you to override the default
            //       password prompt and use a custom one.
           // session.connect();
            channel = session.openChannel("exec");
            // man sudo
            //   -S  The -S (stdin) option causes sudo to read the password from the
            //       standard input instead of the terminal device.
            //   -p  The -p (prompt) option allows you to override the default
            //       password prompt and use a custom one.
            ((ChannelExec) channel).setCommand(command);
//


            InputStream in = channel.getInputStream();
            OutputStream out = channel.getOutputStream();
            ((ChannelExec) channel).setErrStream(System.err);

            channel.connect();

            //out.write((sudo_pass+"\n").getBytes());
            out.flush();
            String str = "";

            byte[] tmp = new byte[1024];
            while (true) {
                while (in.available() > 0) {
                    int i = in.read(tmp, 0, 1024);
                    if (i < 0) break;
                   // System.out.print(new String(tmp, 0, i));
                    str += new String(tmp, 0, i);

                }
                if (channel.isClosed()) {
                    //     System.out.println("exit-status: "+channel.getExitStatus());
                    break;
                }
                try {
                    Thread.sleep(000);
                } catch (Exception ee) {
                }
            }
           // channel.disconnect();
           // session.disconnect();

            if(str.equals("")) str = null;
            return str;
        } catch (Exception e) {
            System.out.println("EXEC Error");
            System.out.println(e);
            System.out.println(e.getMessage());
            e.printStackTrace();


            // return null;
        }
        return null;
    }

    public boolean downloadFile(String sshFile, String distination){

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(distination))) {

            String content = this.exec("cat " + sshFile);

            bw.write(content);

            // no need to close it.
            //bw.close();

            System.out.println("Done");

        } catch (IOException e) {

            e.printStackTrace();
            return false;
        }
        return true;
    }
    public void close(){
        try {
            channel.disconnect();
            session.disconnect();

        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }



    public boolean uploadFile(String file, String fileInDevice) {

        if (fileInDevice == null) fileInDevice = "system.cfg";
        String hoststr = Host + ":/tmp/" + fileInDevice;

        FileInputStream fis = null;
        try {

            String lfile = file;
            String user = hoststr.substring(0, hoststr.indexOf('@'));
            hoststr = hoststr.substring(hoststr.indexOf('@') + 1);
            String host = hoststr.substring(0, hoststr.indexOf(':'));
            String rfile = hoststr.substring(hoststr.indexOf(':') + 1);

            JSch jsch = new JSch();
            Session session = jsch.getSession(user, host, 22);

            // username and password will be given via UserInfo interface.
            UserInfo ui = new MyUserInfo();
            session.setUserInfo(ui);
            session.setTimeout(5000);
            session.connect();

            boolean ptimestamp = true;

            // exec 'scp -t rfile' remotely
            String command = "scp " + (ptimestamp ? "-p" : "") + " -t " + rfile;
            Channel channel = session.openChannel("exec");
            ((ChannelExec) channel).setCommand(command);

            // get I/O streams for remote scp
            OutputStream out = channel.getOutputStream();
            InputStream in = channel.getInputStream();

            channel.connect();

            if (checkAck(in) != 0) {
                System.exit(0);
            }

            File _lfile = new File(lfile);

            if (ptimestamp) {
                command = "T " + (_lfile.lastModified() / 1000) + " 0";
                // The access time should be sent here,
                // but it is not accessible with JavaAPI ;-<
                command += (" " + (_lfile.lastModified() / 1000) + " 0\n");
                out.write(command.getBytes());
                out.flush();
                if (checkAck(in) != 0) {
                    System.exit(0);
                }
            }

            // send "C0644 filesize filename", where filename should not include '/'
            long filesize = _lfile.length();
            command = "C0644 " + filesize + " ";
            if (lfile.lastIndexOf('/') > 0) {
                command += lfile.substring(lfile.lastIndexOf('/') + 1);
            } else {
                command += lfile;
            }
            command += "\n";
            out.write(command.getBytes());
            out.flush();
            if (checkAck(in) != 0) {
                System.exit(0);
            }

            // send a content of lfile
            fis = new FileInputStream(lfile);
            byte[] buf = new byte[1024];
            while (true) {
                int len = fis.read(buf, 0, buf.length);
                if (len <= 0) break;
                out.write(buf, 0, len); //out.flush();
            }
            fis.close();
            fis = null;
            // send '\0'
            buf[0] = 0;
            out.write(buf, 0, 1);
            out.flush();
            if (checkAck(in) != 0) {
                System.exit(0);
            }
            out.close();

            channel.disconnect();
            session.disconnect();

            System.out.println("Uploaded");
            //System.exit(0);
            this.exec("cfgmtd -f /tmp/system.cfg -w ");//save

            this.exec("reboot");//rebot
            System.out.println("Rebooting");
            return true;
        } catch (Exception e) {

            System.out.println("Error in ssh upload file");
            System.out.println(e);
            e.printStackTrace();
            try {
                if (fis != null) fis.close();
            } catch (Exception ee) {
                return false;
            }
            return false;
        }

    }


    public String getCCode(){
        return exec("cat /tmp/system.cfg | grep radio.countrycode").split("=")[1].trim();
    }
    public String getFirmwareInfo(){
        String str = exec("mca-status | head -n 1");
        if(str==null) return null;
      //  System.out.println("THIS IS "+str);
        str = str.split(",")[2].split("=")[1];
        /*for (int i = 0; i < arr.length; i++) {
            System.out.println(arr[i]);
        }*/

        return str;
    }
    public boolean upgradeFirmware(String file) {


        String hoststr = Host + ":/tmp/fwupdate.bin";

        FileInputStream fis = null;
        try {

           // String lfile = file;
            String user = hoststr.substring(0, hoststr.indexOf('@'));
            hoststr = hoststr.substring(hoststr.indexOf('@') + 1);
            String host = hoststr.substring(0, hoststr.indexOf(':'));
            String rfile = hoststr.substring(hoststr.indexOf(':') + 1);

            JSch jsch = new JSch();
            Session session = jsch.getSession(user, host, 22);

            // username and password will be given via UserInfo interface.
            UserInfo ui = new MyUserInfo();
            session.setUserInfo(ui);
            session.setTimeout(5000);
            session.connect();



            boolean ptimestamp = true;

            // exec 'scp -t rfile' remotely
            String command = "scp " + (ptimestamp ? "-p" : "") + " -t " + rfile;
            Channel channel = session.openChannel("exec");
            ((ChannelExec) channel).setCommand(command);

            // get I/O streams for remote scp
            OutputStream out = channel.getOutputStream();
            InputStream in = channel.getInputStream();

            channel.connect();

            if (checkAck(in) != 0) {
                System.exit(0);
            }

            File _lfile = new File(file);

            if (ptimestamp) {
                command = "T " + (_lfile.lastModified() / 1000) + " 0";
                // The access time should be sent here,
                // but it is not accessible with JavaAPI ;-<
                command += (" " + (_lfile.lastModified() / 1000) + " 0\n");
                out.write(command.getBytes());
                out.flush();
                if (checkAck(in) != 0) {
                    System.exit(0);
                }
            }

            // send "C0644 filesize filename", where filename should not include '/'
            long filesize = _lfile.length();
            command = "C0644 " + filesize + " ";
            if (file.lastIndexOf('/') > 0) {
                command += file.substring(file.lastIndexOf('/') + 1);
            } else {
                command += file;
            }
            command += "\n";
            out.write(command.getBytes());
            out.flush();
            if (checkAck(in) != 0) {
                System.exit(0);
            }

            // send a content of lfile
            fis = new FileInputStream(file);
            byte[] buf = new byte[1024];
            while (true) {
                int len = fis.read(buf, 0, buf.length);
                if (len <= 0) break;
                out.write(buf, 0, len); //out.flush();
            }
            fis.close();
            fis = null;
            // send '\0'
            buf[0] = 0;
            out.write(buf, 0, 1);
            out.flush();
            if (checkAck(in) != 0) {
                System.exit(0);
            }
            out.close();

            channel.disconnect();
            session.disconnect();

            //System.exit(0);
            this.exec("/sbin/fwupdate -m");//upgrade command
            //  this.exec("reboot");//rebot
            return true;
        } catch (Exception e) {

            System.out.println(e);
            e.printStackTrace();
            try {
                if (fis != null) fis.close();
            } catch (Exception ee) {
                return false;
            }
            return false;
        }

    }


    static int checkAck(InputStream in) throws IOException {
        int b = in.read();
        // b may be 0 for success,
        //          1 for error,
        //          2 for fatal error,
        //          -1
        if (b == 0) return b;
        if (b == -1) return b;

        if (b == 1 || b == 2) {
            StringBuffer sb = new StringBuffer();
            int c;
            do {
                c = in.read();
                sb.append((char) c);
            }
            while (c != '\n');
            if (b == 1) { // error
                System.out.print(sb.toString());
            }
            if (b == 2) { // fatal error
                System.out.print(sb.toString());
            }
        }
        return b;
    }


    public static class MyUserInfo implements UserInfo, UIKeyboardInteractive {
        public String getPassword() {
            return passwd;
        }

        public boolean promptYesNo(String str) {

            return true;
        }

        String passwd;
        JTextField passwordField = (JTextField) new JPasswordField(20);

        public String getPassphrase() {
            return null;
        }

        public boolean promptPassphrase(String message) {
            return true;
        }

        public boolean promptPassword(String message) {
           /* if (Host.startsWith("admin"))
                passwd = "admin";// "Gig@Cli3nt";
            else if (Host.startsWith("ubnt"))
                passwd = "ubnt";
            */
            passwd = SSHClient.passwd;
            return true;
        }

        public void showMessage(String message) {
            JOptionPane.showMessageDialog(null, message);
        }

        final GridBagConstraints gbc =
                new GridBagConstraints(0, 0, 1, 1, 1, 1,
                        GridBagConstraints.NORTHWEST,
                        GridBagConstraints.NONE,
                        new Insets(0, 0, 0, 0), 0, 0);
        private Container panel;

        public String[] promptKeyboardInteractive(String destination,
                                                  String name,
                                                  String instruction,
                                                  String[] prompt,
                                                  boolean[] echo) {
            panel = new JPanel();
            panel.setLayout(new GridBagLayout());

            gbc.weightx = 1.0;
            gbc.gridwidth = GridBagConstraints.REMAINDER;
            gbc.gridx = 0;
            panel.add(new JLabel(instruction), gbc);
            gbc.gridy++;

            gbc.gridwidth = GridBagConstraints.RELATIVE;

            JTextField[] texts = new JTextField[prompt.length];
            for (int i = 0; i < prompt.length; i++) {
                gbc.fill = GridBagConstraints.NONE;
                gbc.gridx = 0;
                gbc.weightx = 1;
                panel.add(new JLabel(prompt[i]), gbc);

                gbc.gridx = 1;
                gbc.fill = GridBagConstraints.HORIZONTAL;
                gbc.weighty = 1;
                if (echo[i]) {
                    texts[i] = new JTextField(20);
                } else {
                    texts[i] = new JPasswordField(20);
                }
                panel.add(texts[i], gbc);
                gbc.gridy++;
            }

            if (JOptionPane.showConfirmDialog(null, panel,
                    destination + ": " + name,
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE)
                    == JOptionPane.OK_OPTION) {
                String[] response = new String[prompt.length];
                for (int i = 0; i < prompt.length; i++) {
                    response[i] = texts[i].getText();
                }
                return response;
            } else {
                return null;  // cancel
            }
        }
    }
}