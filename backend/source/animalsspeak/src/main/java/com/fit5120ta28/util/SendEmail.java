package com.fit5120ta28.util;

import com.sendgrid.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
//import java.io.InputStreamReader;
//import java.io.LineNumberReader;

import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Component;

@Component
public class SendEmail {
	//define the email destination
	private static final String DEST = "yuantianyi0302@hotmail.com";
	private static final String GOV = "tyua0003@student.monash.edu";
	private String apiKey;
	public SendEmail() throws IOException {
		//read the api key from the file
		File file = new File("sendgridAPI");
		FileReader fileReader = new FileReader(file);
		BufferedReader reader = new BufferedReader(fileReader);
		apiKey = reader.readLine();
		reader.close();
		fileReader.close();
		System.out.println("SendEmail init key:"+apiKey);
	}
	
	// send report to the official rescue email
	public int send(String pdf) throws IOException {
		//get pdf file path
		pdf = "reportPdf/"+pdf+".pdf";
		Email from = new Email(DEST);
		//setup the title
	    String subject = "Injury Report";
	    Email to = new Email(GOV);
	    //setup the content
	    Content content = new Content("text/plain", "A user sent an injury report.");
	    Mail mail = new Mail(from, subject, to, content);

	    
	    //get the pdf file
	    File file = new File(pdf);
	    byte[] filedata=org.apache.commons.io.IOUtils.toByteArray(new FileInputStream(file));
    	Base64 x = new Base64();
    	//encode the pdf file data
		String imageDataString = x.encodeAsString(filedata);
		//add pdf data stream to the email
		Attachments attachments3 = new Attachments();
		//set pdf information
		attachments3.setContent(imageDataString);
		attachments3.setType("application/pdf");
		attachments3.setFilename("InjuryReport.pdf");
		attachments3.setDisposition("attachment");
		attachments3.setContentId("Banner");
		mail.addAttachments(attachments3);


	    //invoke api key
	    SendGrid sg = new SendGrid(apiKey);
	    //define a request
	    Request request = new Request();
	    try {
	      request.setMethod(Method.POST);
	      request.setEndpoint("mail/send");
	      request.setBody(mail.build());
	      Response response = sg.api(request);
	      System.out.println(response.getStatusCode());
//	      System.out.println(response.getBody());
//	      System.out.println(response.getHeaders());
	      return response.getStatusCode();
	    } catch (IOException ex) {
	      throw ex;
	    }
	    
	   
	}
	
	// cc the report to the sender
	public int ccMail(String pdf,String ccAdress) throws IOException {
		System.out.println(ccAdress);
		pdf = "reportPdf/"+pdf+".pdf";
		Email from = new Email(DEST);
	    String subject = "Animals Speak-Your Report Has Been Delivered Successfully!";
	    Email to = new Email(ccAdress);
	    Content content = new Content("text/plain", "Dear,\n Your report has been delivered to the government department.\nHere is the pdf.");
	    Mail mail = new Mail(from, subject, to, content);

	    
	    
	    File file = new File(pdf);
	    byte[] filedata=org.apache.commons.io.IOUtils.toByteArray(new FileInputStream(file));
    	Base64 x = new Base64();
		String imageDataString = x.encodeAsString(filedata);
		Attachments attachments3 = new Attachments();
		attachments3.setContent(imageDataString);
		attachments3.setType("application/pdf");
		attachments3.setFilename("InjuryReport.pdf");
		attachments3.setDisposition("attachment");
		attachments3.setContentId("Banner");
		mail.addAttachments(attachments3);


	    
	    SendGrid sg = new SendGrid(apiKey);
	    Request request = new Request();
	    try {
	      request.setMethod(Method.POST);
	      request.setEndpoint("mail/send");
	      request.setBody(mail.build());
	      Response response = sg.api(request);
	      System.out.println(response.getStatusCode());
//	      System.out.println(response.getBody());
//	      System.out.println(response.getHeaders());
	      return response.getStatusCode();
	    } catch (IOException ex) {
	      throw ex;
	    }
	}
}
