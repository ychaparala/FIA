package lambda.fia.org;


import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.AmazonRekognitionClientBuilder;
import com.amazonaws.services.rekognition.model.AmazonRekognitionException;
import com.amazonaws.services.rekognition.model.DetectLabelsRequest;
import com.amazonaws.services.rekognition.model.DetectLabelsResult;
import com.amazonaws.services.rekognition.model.Image;
import com.amazonaws.services.rekognition.model.Label;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;

import javax.imageio.ImageIO;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.lambda.runtime.Context;

public class rekognition implements RequestHandler < String, String > {
    
	public String handleRequest(String input, Context context) {
    	
		//Access Key ID Secret Access Key
    	BasicAWSCredentials awsCreds = new BasicAWSCredentials("Access Key ID", "Secret Access Key");
    	
    	context.getLogger().log("Input: " + input);
    	AmazonRekognition amazonRekognition = AmazonRekognitionClientBuilder
    				.standard()
                .withRegion(Regions.US_WEST_2)
                .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
                .build();
    	Image imageInput = new Image();
    	String output ="";
    	try {
			imageInput = getAWSFormatImage(input);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    	DetectLabelsRequest request = new DetectLabelsRequest()
                .withImage(imageInput)
                .withMaxLabels(10)
                .withMinConfidence(77F);
    	try {
            DetectLabelsResult result = amazonRekognition.detectLabels(request);
            List<Label> labels = result.getLabels();
          //Object to JSON in file
            ObjectMapper mapper = new ObjectMapper();
            try {
				output=mapper.writeValueAsString(labels);
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        } catch (AmazonRekognitionException e) {
            e.printStackTrace();
        }
    	
    	return output;
}

	private static Image getAWSFormatImage(String ImageBytes) throws IOException{
		byte[] imageInByte; 
		String encodedImg = ImageBytes.split(",")[1];
	    byte[] bytes = Base64.getDecoder().decode(encodedImg.getBytes(StandardCharsets.UTF_8));

		InputStream in = new ByteArrayInputStream(bytes);
		BufferedImage bImageFromConvert = ImageIO.read(in);
		// convert BufferedImage to byte array
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write(bImageFromConvert, "jpg", baos);
		baos.flush();
		imageInByte = baos.toByteArray();
		baos.close();
		ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
		
      return new Image().withBytes(byteBuffer);
	}
}
