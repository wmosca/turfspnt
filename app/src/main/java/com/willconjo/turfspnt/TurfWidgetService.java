package com.willconjo.turfspnt;

import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.os.StrictMode;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class TurfWidgetService extends Service {
    public TurfWidgetService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

//        if(intent.getStringExtra("dailyLimit") != null)
//        {
//            String limits = intent.getStringExtra("dailyLimit") + " " + intent.getStringExtra("weeklyLimit") + " " + intent.getStringExtra("monthlyLimit");
//            System.out.println("LIMITS: " + limits);
//            FileOutputStream outputStream;
//            try {
//                outputStream = openFileOutput("limits.txt", Context.MODE_PRIVATE);
//                outputStream.write(limits.getBytes());
//                outputStream.close();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }

        Double newExpenditures = 0.0;
        // Enable the worlds shittiest ThreadPolicy
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        // Do the main service function
        try {
            // Contact web server to get TUM
            String tumString = getTUM();
            // Convert to XML tree
            DocumentBuilder newDocumentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document turfUpdateMessageDoc = newDocumentBuilder.parse(new ByteArrayInputStream(tumString.getBytes()));
            Node turfUpdateMessage = turfUpdateMessageDoc.getFirstChild();
            NodeList transactionStream = turfUpdateMessage.getLastChild().getChildNodes();
            String transactionsString = "";
            for(int i=0;i<transactionStream.getLength();i++){
                System.out.println(nodeToString(transactionStream.item(i)));
                transactionsString = transactionsString + nodeToString(transactionStream.item(i));
            }

            System.out.println("TRANS STRING: " + transactionsString);

            FileOutputStream outputStream;
            try {
                outputStream = openFileOutput("turfTransactions.xml", Context.MODE_PRIVATE);
                outputStream.write(transactionsString.getBytes());
                outputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            FileInputStream inputStream;
            String s = "";
            try {
                inputStream = openFileInput("turfTransactions.xml");
                char c;
                int byt;
                while ((byt = inputStream.read()) != -1){
                    c = (char) byt;
                    s = s + c;
                }
            }catch(Exception e){
                System.out.println("Failed to READ");
                System.err.println(e);
                e.printStackTrace();
            }
            System.out.println("THIS IS S YOU MONGOLOID: " + s);
            s = "<Transactions>" + s + "</Transactions>";

            DocumentBuilder newDocBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document currentTransactions = newDocBuilder.parse(new ByteArrayInputStream(s.getBytes()));
            System.out.println(currentTransactions.getNodeName());
            Node transactionRoot = currentTransactions.getFirstChild();
            System.out.println(transactionRoot.getTextContent());
            NodeList transStream= transactionRoot.getChildNodes();

            System.out.println("NODE TO STRING OF ROOT: " + nodeToString(transactionRoot));

            for(int i=0; i<transStream.getLength(); i++) {
                System.out.println(transStream.toString());
                System.out.println(transStream.item(i).getTextContent());
                if (transStream.item(i).getFirstChild().getTextContent().equals("1")) {
                    newExpenditures = newExpenditures + Double.parseDouble(transactionStream.item(i).getChildNodes().item(1).getTextContent());
                }
            }


        }catch(Exception e) {
            System.out.println("Failed to get TUM");
            System.err.println(e);
            e.printStackTrace();
        }

//        FileInputStream inputStream;
//        String s = "";
//        try {
//            inputStream = openFileInput("limits.txt");
//            char c;
//            int byt;
//            while ((byt = inputStream.read()) != -1){
//                c = (char) byt;
//                s = s + c;
//            }
//        }catch(Exception e){
//            System.out.println("Failed to READ2");
//            System.err.println(e);
//            e.printStackTrace();
//        }
//
//        System.out.println(s);
//        String[] moneyLimits = s.split(" ");

        System.out.println(newExpenditures);
        String dString =  newExpenditures + " " + "00" + " " + "100";
        String wString =  newExpenditures + " " + "00" + " " + "750";
        String mString =  newExpenditures + " " + "00" + " " + "3000";

        // Broadcast intents to the three widgets
        updateWidgets(getApplicationContext(), dString, wString, mString);

        return START_STICKY;
    }

    private String nodeToString(Node node) {
        StringWriter sw = new StringWriter();
        try {
            Transformer t = TransformerFactory.newInstance().newTransformer();
            t.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            t.transform(new DOMSource(node), new StreamResult(sw));
        } catch (TransformerException te) {
            System.out.println("nodeToString Transformer Exception");
        }
        return sw.toString();
    }

    public static void updateWidgets(Context context, String dString, String wString, String mString) {
        String[] dParts = dString.split(" ");
        String[] wParts = wString.split(" ");
        String[] mParts = mString.split(" ");
        Intent dIntent = new Intent(context.getApplicationContext(), TurfWidgetDaily.class);
        Intent wIntent = new Intent(context.getApplicationContext(), TurfWidgetWeekly.class);
        Intent mIntent = new Intent(context.getApplicationContext(), TurfWidgetMonthly.class);
        System.out.println("dParts[0] = "+ dParts[0]);
        System.out.println("dParts[1] = "+ dParts[1]);
        System.out.println("dParts[2] = "+ dParts[2]);
        dIntent.putExtra("dollars", dParts[0]);
        dIntent.putExtra("cents", dParts[1]);
        dIntent.putExtra("total", dParts[2]);
        wIntent.putExtra("dollars", wParts[0]);
        wIntent.putExtra("cents", wParts[1]);
        wIntent.putExtra("total", wParts[2]);
        mIntent.putExtra("dollars", mParts[0]);
        mIntent.putExtra("cents", mParts[1]);
        mIntent.putExtra("total", mParts[2]);
        dIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        wIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        mIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        context.sendBroadcast(dIntent);
        context.sendBroadcast(wIntent);
        context.sendBroadcast(mIntent);
    }

    // HTTP POST request
    private String getTUM() throws Exception {

        String url = "http://155.246.204.59:55555/tum";
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        //add request header
        con.setRequestMethod("POST");
        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

        String urlParameters = "sn=C02G8416DRJM&cn=&locale=&caller=&num=12345";

        // Send post request
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(urlParameters);
        wr.flush();
        wr.close();

        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'POST' request to URL : " + url);
        System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        return response.toString();
    }
}
