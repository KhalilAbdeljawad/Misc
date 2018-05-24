

import MySQL;
import SSHClient;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import scala.Array;
import scala.reflect.io.File;

import javax.net.ssl.*;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.HttpURLConnection;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Khalil on 6/2/2017.
 */
public class Sys {

    public static String runSystemCommand(String command) {

        try {
            Process p = Runtime.getRuntime().exec(command);
            BufferedReader inputStream = new BufferedReader(
                    new InputStreamReader(p.getInputStream()));

            String s = "";
            StringBuilder str = new StringBuilder();
            // reading output stream of the command
            while ((s = inputStream.readLine()) != null) {
             //   System.out.println(s);
                str.append(s).append("\n");

            }


           // System.out.println(str.toString());
            return str.toString().length()>5 ?  str.toString().substring(0, str.toString().length()-1).trim() : str.toString().trim();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * // call windows ping command
     * @param ip
     * @return Ping result
     */
    public static String winPing(String ip){
        String str[]= runSystemCommand("ping "+ip).split("\n");
       // System.out.println(str[str.length-1]);
        if(str[str.length-1].indexOf("Average")>0)
            return (str[str.length-1].substring(str[str.length-1].indexOf("Average")).split("=")[1].trim());
        else
            return "Unreachable";
    }

    public static String getTime(){
        DateTimeFormatter tf = DateTimeFormatter.ofPattern("HH:mm:ss");
        LocalTime localTime = LocalTime.now();
        return tf.format(localTime);
    }

    public static String getDate(){
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.now();
        return df.format(localDate);
    }


    public static String sendPost(String url, String paramerterName, String parameterValue) throws Exception {


        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        //add reuqest header
        String USER_AGENT = "Mozilla/5.0";
        con.setRequestMethod("POST");
        con.setRequestProperty("User-Agent", USER_AGENT);
        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

        String urlParameters = paramerterName+"="+parameterValue;// "sn=C02G8416DRJM&cn=&locale=&caller=&num=12345";

        // Send post request
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(urlParameters);
        wr.flush();
        wr.close();

        int responseCode = con.getResponseCode();
   //     System.out.println("\nSending 'POST' request to URL : " + url);
    //    System.out.println("Post parameters : " + urlParameters);
    //    System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        //print result
        String str = response.toString();
        return (str.substring(1, str.length()-1));

    }


    private static void disableSslVerification() {
        try
        {
            // Create a trust manager that does not validate certificate chains
            TrustManager[] trustAllCerts = new TrustManager[] {new X509TrustManager() {
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }
                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }
            };

            // Install the all-trusting trust manager
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

            // Create all-trusting host name verifier
            HostnameVerifier allHostsValid = new HostnameVerifier() {
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            };

            // Install the all-trusting host verifier
            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
    }

    static {
        disableSslVerification();
    }

    public static String sendPost(String url, String ... params) throws Exception {

        disableSslVerification();
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
       // HttpsURLConnection.setDefaultHostnameVerifier(
      //          SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

        //add reuqest header
        String USER_AGENT = "Mozilla/5.0";
        con.setRequestMethod("POST");
        con.setRequestProperty("User-Agent", USER_AGENT);
        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

        String paramsStr = "";
        for (int i = 0; i < params.length; i+=2) {
            paramsStr+=params[i]+"="+params[i+1]+"&";
        }
        String urlParameters = paramsStr;// paramerterName+"="+parameterValue;// "sn=C02G8416DRJM&cn=&locale=&caller=&num=12345";

        // Send post request
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(urlParameters);
        wr.flush();
        wr.close();

        int responseCode = con.getResponseCode();
        //     System.out.println("\nSending 'POST' request to URL : " + url);
        //    System.out.println("Post parameters : " + urlParameters);
        //    System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        //print result
        String str = response.toString();
        return (str.substring(1, str.length()-1));

    }

/*
    public static String sendGet(String no) throws Exception {

        String url = "https://www.shopandship.com/api/tracking/"+no;

        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        // optional default is GET
        con.setRequestMethod("GET");

        //add request header
     //   con.setRequestProperty("User-Agent", USER_AGENT);

        int responseCode = con.getResponseCode();
     //   System.out.println("\nSending 'GET' request to URL : " + url);
     //   System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        //print result
        return(response.toString());

    }
*/

    public static String sendGet(String url) throws Exception {

      //  String url = "https://www.shopandship.com/api/tracking/"+no;

        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        // optional default is GET
        con.setRequestMethod("GET");

        //add request header
        //   con.setRequestProperty("User-Agent", USER_AGENT);

        int responseCode = con.getResponseCode();
        //   System.out.println("\nSending 'GET' request to URL : " + url);
        //   System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        //print result
        return(response.toString());

    }




       // System.out.println(sendPost("https://www.shopandship.com/api/tracking/9331842004"));


        String response = (Sys.sendPost("https://10.100.28.119/cgi-bin/luci", "username", "admin", "password", "Gig@Cli3nt" ));

        System.out.println(response);
        JSONParser parser = new JSONParser();
        JSONObject json = (JSONObject) parser.parse("{"+response+"}");
        System.out.println(json.get("stok"));

       Sys.sendGet("https://10.100.28.119/cgi-bin/luci/;stok="+json.get("stok")+"/admin/test_connect");

        //System.out.println(runSystemCommand("snmpwalk -v 1 -c gigacore 10.100.40.62  .1.3.6.1.4.1.41112.1"));

        if(true)return;

        long a=9332025743l;
      //  long b=9331912105l;

        String line="";
        //String alla[]=new String[100000], []=new String[10000];

        ArrayList<String> alla, lib, allab, libb;
        alla=new ArrayList<>();
        lib = new ArrayList<>();

        allab=new ArrayList<>();
        libb = new ArrayList<>();
        int alli=0, libi=0, allib=0, libib=0;
        for (int i = 0; i < 100000; i++) {
            try {

                line = sendGet("" + a);
                System.out.println(alli);
             //   System.out.println(line);
                alla.add("\n"+line);
                if(line.contains("Libya"))
                    lib.add("\n"+line);

                if(libi%100==0){
                    Files.writeFile("D:\\ara\\aralib"+libi+".txt", lib.toString());
                }
                if(alli%1000==0) {
                    Files.writeFile("D:\\ara\\ara" + alli + ".txt", alla.toString());
                }

                libi++;
                alli++;



/*

                line = sendGet("" + b);
                System.out.println(allib);
              //    System.out.println(line);
                allab.add("\n"+line);
                if(line.contains("Libya"))
                    libb.add("\n"+line);

                if(libib%100==0){
                    Files.writeFile("D:\\ara\\aralibb"+libib+".txt", libb.toString());
                }
                if(allib%1000==0) {
                    Files.writeFile("D:\\ara\\arab" + allib + ".txt", allab.toString());
                }

                libib++;
                allib++;

                */
            }catch (Exception e){
               // System.out.println(e.getMessage());
            }
            a++;
           // b++;
        }


        if(true)return;
        String staCount = runSystemCommand("snmpwalk -v 1 -c gigacore 10.100.84.152 UBNT-AirMAX-MIB::ubntStaTable").concat(runSystemCommand("snmpwalk -v 1 -c gigacore 10.100.84.152 UBNT-AirMAX-MIB::ubntWlStatTable"));

       // System.out.println(staCount);
//        System.exit(0);


        staCount = runSystemCommand("snmpget -v 1 -c gigacore 10.0.3.33 UBNT-AirMAX-MIB::ubntWlStatStaCount.1");

        staCount = staCount.split("=")[1].split(":")[1].trim();
        Integer nStats = 0;

        if(staCount.equals("") == false){
            nStats = Integer.parseInt(staCount);
            long aa=System.currentTimeMillis();
            String all = runSystemCommand("snmpwalk -v 1 -c gigacore 10.0.3.33 UBNT-AirMAX-MIB::ubntStaTable");
            System.out.println(System.currentTimeMillis()-aa);
           // System.out.printf(all);
            System.exit(0);
            String[] starr = all.split("\n");


            String data = "";

            for (int i = 0; i < starr.length ; i++) {


                if(i%nStats == 0) {
                    System.out.println(data);
                //    System.out.println("\n\n\n\n\n");
                    data="";
                    data+=starr[i]+"\n";
                }



            }

        }
        else{
            System.out.println("null");
        }
        System.out.println(staCount);

        //System.out.println(sendPost("http://localhost/user_data2.php","username","KhlilGiga"));
        //updatePasswords2();


        System.exit(0);
        String str = null;
        try {
            str = sendPost("http://localhost/updateAradialPassword.php",
                    "username", "mansour_muhamad1", "password", "10033", "update", "true");

        } catch (Exception e) {

            e.printStackTrace();

        }
        System.out.println(str);


    }
}
