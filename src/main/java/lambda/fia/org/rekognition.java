package lambda.fia.org;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.io.ByteArrayOutputStream;

//import org.apache.commons.codec.binary.Base64;

import java.net.URL;
import java.nio.ByteBuffer;
import java.util.List;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.AmazonRekognitionClientBuilder;
import com.amazonaws.services.rekognition.model.DetectLabelsRequest;
import com.amazonaws.services.rekognition.model.DetectLabelsResult;
import com.amazonaws.services.rekognition.model.Image;
import com.amazonaws.services.rekognition.model.Label;

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
//        output = generateRekognitionJSON("name", getImageBytes(input));
        context.getLogger().log("Input: " + input);
        AmazonRekognition amazonRekognition = AmazonRekognitionClientBuilder
            .standard()
            .withRegion(Regions.US_WEST_2)
            .build();
            
        DetectLabelsRequest request = new DetectLabelsRequest()
            .withImage(new Image()
            .withBytes(ByteBuffer.wrap(getImageBytes(input))));

         DetectLabelsResult result=amazonRekognition.detectLabels(request);
         List<Label> labels = result.getLabels();
         for (Label label : labels)
             {
                output+="Label: " + label.getName().toString()+ "\n Confidence: " + label.getConfidence().toString();
            }

        return output;
    }

    private static byte[] getImageBytes(String urlText) {
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
        return output.toByteArray();
    }

//    public static String encodeImage(byte[] imageByteArray) {
//        return Base64.encodeBase64URLSafeString(imageByteArray);
//    }

//    private static String generateRekognitionJSON(String name, String encodedImage) {
//        String JSON = "{"+(char)34+"Image"+(char)34+":{"+(char)34+"Bytes"+(char)34+":"+(char)34+ encodedImage +(char)34+"}}";
//        return JSON;
//    }
}