

import java.io.*;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Khalil on 5/22/2017.
 */
public class Files {

    public static boolean downloadFile(String url, String downloadFileName) {

        ////// Download file
        try {
            URL website = new URL(url);
            ReadableByteChannel rbc = Channels.newChannel(website.openStream());
            FileOutputStream fos = new FileOutputStream(downloadFileName);
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
            fos.close();
            rbc.close();
            return true;
        } catch (Exception ex) {

            System.out.println(ex);
            return false;
        }
        ////////////////////////////////////

    }

    public static String readFile(String filepath) {
        String text = "";
        try (BufferedReader reader = new BufferedReader(new FileReader(filepath))) {
            String line = null;
            while ((line = reader.readLine()) != null) {
                //if(line.contains("radio.1.chanbw=20") || line.contains("radio.1.chanbw=40"))
                //    line = "radio.1.chanbw=10";
                text += line + "\n";
            }
        } catch (IOException x) {
          
            return null;
        }

        return text;
    }



    public static Boolean writeFile(String filepath, String text) {
        BufferedWriter writer = null;
        try {
            //create a temporary file
            String timeLog = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
         //   System.out.println(timeLog);
            File file = new File(filepath);

            // This will output the full path where the file will be written to...
          //  System.out.println(file.getCanonicalPath());

            writer = new BufferedWriter(new FileWriter(file));
            writer.write(text);
            writer.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                // Close the writer regardless of what happens...
                writer.close();
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }


    public Boolean writeFile(String filepath, String[][] text) {
        BufferedWriter writer = null;
        try {
            //create a temporary file
            String timeLog = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
            System.out.println(timeLog);
            File file = new File(filepath);

            // This will output the full path where the file will be written to...
            System.out.println(file.getCanonicalPath());

            // writer = new BufferedWriter(new FileWriter(file));

            writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(file), "UTF-8"));


            String fileText = "";
            for (String sar[] : text) {
                for (String str : sar) {
                    fileText += str + ",";
                }
                fileText += "\n";
            }
            writer.write(fileText);
            writer.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                // Close the writer regardless of what happens...
                writer.close();
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }


}
