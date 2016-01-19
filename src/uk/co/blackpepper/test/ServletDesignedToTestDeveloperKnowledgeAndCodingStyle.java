package uk.co.blackpepper.test;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.google.gson.Gson;

public class ServletDesignedToTestDeveloperKnowledgeAndCodingStyle  extends javax.servlet.http.HttpServlet {

	private static final String PATH_TO_FILE = "c:\\my output test\\test\\mypostfile.txt";
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(ServletDesignedToTestDeveloperKnowledgeAndCodingStyle.class);

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Map<String, String[]> parameterMap = request.getParameterMap();
		ParameterMapWithChecksum mapWithChecksum = new ParameterMapWithChecksum(parameterMap);
		logRequestParameters(parameterMap);
		logger.info("checksum = " + mapWithChecksum.getCheckSum());

		writeToFile(mapWithChecksum);
	}

	private void logRequestParameters(Map<String, String[]> parameterMap) {
		for (String key : parameterMap.keySet()) {
			logger.info(key  + " = " + Arrays.toString(parameterMap.get(key)));
		}
	}
	
	private void writeToFile(ParameterMapWithChecksum mapWithChecksum) throws FileNotFoundException {
		try (PrintStream p = new PrintStream(new FileOutputStream(PATH_TO_FILE))){
			p.println(mapWithChecksum.toJson());
		} catch (FileNotFoundException e) {
			logger.error("Error writing to file", e);
			// TODO Create the file if it doesn't exist.
			throw e;
		}
	}
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String fileContents = readFromFile();
		ParameterMapWithChecksum mapWithChecksum = new Gson().fromJson(fileContents, ParameterMapWithChecksum.class);
		testCheckSum(mapWithChecksum);
		
		// We might want to do something with the map, but let's just put it into the response as JSON.
		response.getWriter().println("Stored map is \n" + new Gson().toJson(mapWithChecksum.getParameterMap()));
	}
	
	private String readFromFile() throws IOException {
		try {
			return new String(Files.readAllBytes(Paths.get(PATH_TO_FILE)));
		} catch (IOException e) {
			logger.error("Failed to read from file " + PATH_TO_FILE, e);
			throw e;
		}
	}

	private void testCheckSum(ParameterMapWithChecksum mapWithChecksum) throws IOException {
		int recordedCheckSum = mapWithChecksum.getCheckSum();
		int newCheckSum = ParameterMapWithChecksum.betterHashCode(mapWithChecksum.getParameterMap());
		if  (newCheckSum == recordedCheckSum) {
			logger.info("Checksum value of " + newCheckSum + " tested and valid.");
			
		} else {
			// I guess we can call this an IOException.
			IOException e =  new IOException("Checksum value of " + newCheckSum + " tested and invalid. It should be " + recordedCheckSum);
			logger.error(e.getMessage(), e);
			throw e;
		}
	}
}

/*
 * 10:10 Create new dynamic web project in eclipse. 
 * 10:12 Paste the java file into a new package in the project. 
 * 10:12 Format the white-space.
 * 10:12 Change the file name to match the class name.
 * 10:12 Convert to Maven project.
 * 10:13 Add log4j dependency.
 * 10:14 Organise imports.
 * 10:15 Make variables private (static final if appropriate).
 * 10:15 Don't use the root logger.
 * 10:16 The logger isn't used. Replace System.out with logger.info
 * 10:17 Add serialVersionUID.
 * 10:18 In listObjects(), add missing braces to the second if block.
 * 10:19 Log the exception from the catch block.
 * 10:21 Make the HashMap generic.
 * 10:29 What is the point of the check-sum? 
 * 	Maybe we should create a check-sum on post, and write the file.
 *  Then on get, we read the file, test the check-sum, and return the Map in the response.
 *  Obviously, this is not what it does at present. Then again, at present, it just falls over.
 * 10:32 Don't create a new long object to return from the check-sum method.
 * 10:39 I don't know what the check-sum method is attempting to do, and I don't have time to figure it out. I'll just use the hashcode.
 * 10:41 Make the outputMe string local to the method.
 * 10:50 Use GSON to write JSON to the file.
 * 10:54 Make new class ParameterMapWithChecksum.
 * 11:00 Test the post method using Chrome plugin - Advanced Rest Client
 * 11:22 Add the servlet mapping in web.xml - I thought eclipse did this for me automatically.
 * 11:22 Configure log4j
 * 11:22 Create the file we will write to.
 * 11:36 Eventually got the post working.
 * 11:39 Extract writeToFile method and use try-with-resources.
 * 12:02 The checksum fails. I guess it's because of the arrays. Our hashcode will have to be a bit cleverer.
 * 12:16 Done. Could do with some tests, but there's no time left.
 */