import java.io.*;
import org.json.simple.JSONArray;  
import org.json.simple.JSONObject;
import java.util.ArrayList;

public class CSVtoJSON{
   public static void convert(String fileIn, String fileOut){
      try{
         // ------------------- FILE SETUP -------------------
         File input = new File(fileIn);
         File output = new File(fileOut);
         output.createNewFile();
         
         BufferedReader fileReader = new BufferedReader(new FileReader(input)); // file reader
         FileWriter fileWriter = new FileWriter(output); // file writer
         System.out.println("Filepaths are valid!");
         
         String[] columnLabels = new String[0];
         boolean firstLineFlag = true;
         
         // ------------------- FILE READ -------------------
         JSONArray allObjects = new JSONArray();
         JSONObject thisObject = new JSONObject();
         System.out.println("Reading data from CSV file...");
         int lineNo = 0;
         while(fileReader.ready()){
            String[] thisLine = fileReader.readLine().split(",");
            if(firstLineFlag){ // are we reading the first row of the csv file?
               // if so, set up our column labels
               // System.out.println("Parsing column labels.");
               columnLabels = thisLine;
               firstLineFlag = false; // unflag
            } else { // for every other line, go label by label
               // System.out.println("Parsing a line of our source. Line number: " + lineNo);
               thisObject = new JSONObject();
               for(int i = 0; i < columnLabels.length; i++){
                  // System.out.println("Parsing " + columnLabels[i] + " for this line.");
                  if(i >= thisLine.length){ // empty space at end of row
                     thisObject.put(columnLabels[i],"");
                     continue;
                  }
                  if(thisLine[i].contains(";")){ // parse as array
                     JSONArray tempArr = new JSONArray();
                     String[] stringArr = thisLine[i].split(";");
                     for(String s : stringArr){
                        tempArr.add(s.trim()); // add each smaller string to the temp array (remove whtiespace too)
                     }
                     thisObject.put(columnLabels[i], tempArr);
                  } else { // parse as single item
                     thisObject.put(columnLabels[i], thisLine[i].trim());
                  }
               }
               allObjects.add(thisObject); // throw our object in the list
            }
            lineNo++;
         }
         fileReader.close();
         // -------------------- FILE WRITE --------------------
         System.out.println("Writing JSON object to file...");
         fileWriter.write(allObjects.toJSONString()); // write to json file
         fileWriter.flush();
         fileWriter.close();
         System.out.println("File write successful!");
      } catch(IOException e){
         e.printStackTrace();
      }
   }
   public static void main(String[] args){
      convert(args[0],args[1]);
   }
}