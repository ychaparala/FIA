package lambda.fia.org;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.io.ByteArrayOutputStream;
import org.apache.commons.codec.binary.Base64;
import java.net.URL;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

//import org.apache.http.Header;
//import org.apache.http.HttpResponse;
//import org.apache.http.client.ClientProtocolException;
//import org.apache.http.client.HttpClient;
//import org.apache.http.client.methods.HttpPost;
//import org.apache.http.entity.StringEntity;
//import org.apache.http.impl.client.DefaultHttpClient;

public class rekognition implements RequestHandler < String, String > {
    public String handleRequest(String input, Context context) {
        String output = "";
        output = generateRekognitionJSON("name", getImageBytes(input));
        context.getLogger().log("Input: " + input);
        return output;
    }

    private static String getImageBytes(String urlText) {
        URL url = null;
        try {
            url = new URL(urlText);
        } catch (MalformedURLException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        InputStream inputStream = null;
        try {
            inputStream = url.openStream();
            int n = 0;
            byte[] buffer = new byte[1024];
            while (-1 != (n = inputStream.read(buffer))) {
                output.write(buffer, 0, n);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return encodeImage(output.toByteArray());
    }

    public static String encodeImage(byte[] imageByteArray) {
        return Base64.encodeBase64URLSafeString(imageByteArray);
    }

    private static String generateRekognitionJSON(String name, String encodedImage) {
        String JSON = "{\"Image\":{\"Bytes\":" + encodedImage + '}';

        return JSON;
    }
}