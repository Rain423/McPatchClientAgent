package cc.sakurarain.mcpatchclientagent;


import java.io.*;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.net.HttpURLConnection;
import java.net.ServerSocket;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Tools {
    public static int getPID() {
        RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
        //System.out.println(runtimeMXBean.getName());
        return Integer.valueOf(runtimeMXBean.getName().split("@")[0]).intValue();
    }

    public static boolean checkPort(int port) {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            serverSocket.close();
        } catch (IOException e) {
            return false;
            //throw new RuntimeException(e);
        }
        return true;
    }

    public static HashMap<String, String> getFileList(String url) {
        HashMap<String, String> map = new HashMap<>();
        String request = doGET(url, null);
        String host = getHost(url);
        request = getMiddleValue(request, "<script type=\"text/javascript\">", "success:function(msg)");


        return map;
    }


    public static String getJSValue(String text, String target) {
        String jsValue;

        jsValue = getMiddleValue(text, "'" + target + "':", ",");
        //System.out.println("1"+jsValue);
        if ((jsValue.substring(0, 1).equals("'") && jsValue.substring(jsValue.length() - 1, jsValue.length()).equals("'")) || isnum(jsValue)) {
            if (jsValue.substring(jsValue.length() - 1, jsValue.length()).equals("'")) {
                jsValue = jsValue.substring(0, jsValue.length() - 1);
            }
            if ((jsValue.substring(0, 1).equals("'"))) {
                jsValue = jsValue.substring(1);
            }
            jsValue = jsValue.trim();
            return jsValue;
        }

        jsValue = getMiddleValue(text, jsValue + "=", ";");
        jsValue = jsValue.replaceAll("'", "");
        jsValue = jsValue.trim();
        if (!jsValue.equals("")) {
            return jsValue;
        }

        return "";
    }

    public static String getMiddleValue(String text, String start, String end) {
        int index1 = text.indexOf(start) + start.length();
        int index2 = text.substring(index1).indexOf(end) + index1;
        //System.out.println(index1+" "+index2);
        if (index1 >= index2 || index1 <= 0 || index2 < 0) {
            return "";
        }
        return text.substring(index1, index2);
    }

    public static boolean isnum(String text) {
        String target = text.substring(0, 1);
        for (int i = 0; i < 10; i++) {
            if (target.equals(String.valueOf(i))) {
                return true;
            }
        }
        return false;
    }

    public static String doGET(String httpurl, HashMap<String, String> header) {
        HttpURLConnection httpURLConnection = null;
        StringBuffer stringBuffer = new StringBuffer();
        try {
            URL url = new URL(httpurl);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setReadTimeout(5000);

            httpURLConnection.setRequestProperty("Accept", "*/*");
            httpURLConnection.setRequestProperty("Cache-Control", "no-cache");
            httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
            httpURLConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/109.0.0.0 Safari/537.36");

            if (header != null && !header.isEmpty()) {
                for (String key :
                        header.keySet()) {
                    httpURLConnection.setRequestProperty(key, header.get(key));
                }
            }
            httpURLConnection.connect();
            if (httpURLConnection.getResponseCode() == 200) {
                InputStream inputStream = httpURLConnection.getInputStream();
                if (inputStream != null) {

                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    String temp = null;
                    while ((temp = bufferedReader.readLine()) != null) {
                        stringBuffer.append(temp + "\r\n");
                    }
                }
            }


            httpURLConnection.disconnect();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //System.out.println(stringBuffer.toString());
        return stringBuffer.toString().replaceAll("(\\/\\/.*)|(\\/\\*([\\S\\s]+?)\\*\\/)", "").replaceAll("(\\<\\!\\-\\-([\\S\\s]+?)\\-\\-\\>)", "");
    }

    public static InputStream getInputStream(String httpurl, Map<String, String> header) {
        HttpURLConnection httpURLConnection = null;
        StringBuffer stringBuffer = new StringBuffer();
        try {
            URL url = new URL(httpurl);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setReadTimeout(5000);

            httpURLConnection.setRequestProperty("Accept", "*/*");
            httpURLConnection.setRequestProperty("Cache-Control", "no-cache");
            httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
            httpURLConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/109.0.0.0 Safari/537.36");

            if (header != null && !header.isEmpty()) {
                for (String key :
                        header.keySet()) {
                    httpURLConnection.setRequestProperty(key, header.get(key));
                }
            }
            httpURLConnection.connect();
            if (httpURLConnection.getResponseCode() == 200) {
                InputStream inputStream = httpURLConnection.getInputStream();
                return inputStream;
            }


            httpURLConnection.disconnect();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //System.out.println(stringBuffer.toString());
        return null;
    }

    public static String getLocation(String httpurl, HashMap<String, String> header) {
        HttpURLConnection httpURLConnection = null;
        String buffer = null;
        try {
            URL url = new URL(httpurl);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setReadTimeout(5000);

            httpURLConnection.setRequestProperty("Accept", "*/*");
            httpURLConnection.setRequestProperty("Cache-Control", "no-cache");
            httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
            httpURLConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/109.0.0.0 Safari/537.36");

            if (header != null && !header.isEmpty()) {
                for (String key :
                        header.keySet()) {
                    httpURLConnection.setRequestProperty(key, header.get(key));
                }
            }
            httpURLConnection.connect();
            if (httpURLConnection.getResponseCode() == 200) {
                buffer = httpURLConnection.getHeaderField("Location");
            }


            httpURLConnection.disconnect();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //System.out.println(stringBuffer.toString());
        return buffer;
    }

    public static String doPOST(String httpurl, String param, HashMap<String, String> header) {
        HttpURLConnection httpURLConnection = null;
        StringBuffer stringBuffer = new StringBuffer();
        try {
            URL url = new URL(httpurl);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setReadTimeout(5000);

            httpURLConnection.setRequestProperty("Accept", "*/*");
            httpURLConnection.setRequestProperty("Cache-Control", "no-cache");
            httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
            httpURLConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/109.0.0.0 Safari/537.36");

            if (header != null && !header.isEmpty()) {
                for (String key :
                        header.keySet()) {
                    httpURLConnection.setRequestProperty(key, header.get(key));
                }
            }


            if (param != null && !param.equals("")) {
                PrintWriter printWriter = new PrintWriter(httpURLConnection.getOutputStream());
                printWriter.write(param);
                printWriter.flush();
            }

            httpURLConnection.connect();
            if (httpURLConnection.getResponseCode() == 200) {
                InputStream inputStream = httpURLConnection.getInputStream();
                if (inputStream != null) {

                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    String temp = null;
                    while ((temp = bufferedReader.readLine()) != null) {
                        stringBuffer.append(temp + "\r\n");
                    }
                }
            }

            httpURLConnection.disconnect();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return stringBuffer.toString().replaceAll("(\\/\\/.*)|(\\/\\*([\\S\\s]+?)\\*\\/)", "").replaceAll("(\\<\\!\\-\\-([\\S\\s]+?)\\-\\-\\>)", "");
    }

    public static String getHost(String url) {
        String buffer = url.toLowerCase();
        buffer = buffer.replaceAll("//", "%~");
        buffer = buffer.replaceAll("(\\/.*)", "");
        buffer = buffer.replaceAll("%~", "//");
        return buffer;
    }

    public static String bin2hex(byte[] data) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < data.length; i++) {
            int v = data[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                sb.append(0);
            }
            sb.append(hv);
        }
        return sb.toString();
    }

    public static byte[] hex2bin(String data) {
        if (null == data || "".equals(data.trim())) {
            return new byte[0];
        }

        byte[] bytes = new byte[data.length() / 2];
        String hex;
        for (int i = 0; i < data.length() / 2; i++) {
            hex = data.substring(i * 2, i * 2 + 2);
            bytes[i] = (byte) Integer.parseInt(hex, 16);
        }

        return bytes;
    }

}
