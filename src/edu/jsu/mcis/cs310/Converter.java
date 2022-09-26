package edu.jsu.mcis.cs310;

import java.io.*;
import java.util.*;
import com.opencsv.*;
import org.json.simple.*;
import org.json.simple.parser.*;

public class Converter {

    /*
        
        Consider the following CSV data:
        
        "ID","Total","Assignment 1","Assignment 2","Exam 1"
        "111278","611","146","128","337"
        "111352","867","227","228","412"
        "111373","461","96","90","275"
        "111305","835","220","217","398"
        "111399","898","226","229","443"
        "111160","454","77","125","252"
        "111276","579","130","111","338"
        "111241","973","236","237","500"
        
        The corresponding JSON data would be similar to the following (tabs and
        other whitespace have been added for clarity).  Note the curly braces,
        square brackets, and double-quotes!  These indicate which values should
        be encoded as strings and which values should be encoded as integers, as
        well as the overall structure of the data!
        
        {
            "colHeaders":["ID","Total","Assignment 1","Assignment 2","Exam 1"],
            "rowHeaders":["111278","111352","111373","111305","111399","111160",
            "111276","111241"],
            "data":[[611,146,128,337],
                    [867,227,228,412],
                    [461,96,90,275],
                    [835,220,217,398],
                    [898,226,229,443],
                    [454,77,125,252],
                    [579,130,111,338],
                    [973,236,237,500]
            ]
        }
        
        Your task for this program is to complete the two conversion methods in
        this class, "csvToJson()" and "jsonToCsv()", so that the CSV data shown
        above can be converted to JSON format, and vice-versa.  Both methods
        should return the converted data as strings, but the strings do not need
        to include the newlines and whitespace shown in the examples; again,
        this whitespace has been added only for clarity.
        
        NOTE: YOU SHOULD NOT WRITE ANY CODE WHICH MANUALLY COMPOSES THE OUTPUT
        STRINGS!!!  Leave ALL string conversion to the two data conversion
        libraries we have discussed, OpenCSV and JSON.simple.  See the "Data
        Exchange" lecture notes for more details, including examples.
        
    */

    @SuppressWarnings({
        "unchecked",
        "ManualArrayToCollectionCopy"
    })
    public static String csvToJson(String csvString) {

        String results = "";

        try {
            // Initialize CSV Reader and Iterator
            CSVReader reader = new CSVReader(new StringReader(csvString));
            List < String[] > full = reader.readAll();
            Iterator < String[] > iterator = full.iterator();

            // init object
            JSONObject object = new JSONObject();

            // first read headers from the file
            String[] strings = iterator.next();
            List < String > headers = new ArrayList < > ();

            for (var string: strings) {
                headers.add(string);
            }
            object.put("colHeaders", headers);

            // init placeholders for rowHeaders and data
            List < String > rowHeaders = new ArrayList < > ();
            List < List < Integer >> data = new ArrayList < > ();

            // now parse rows
            while (iterator.hasNext()) {
                // create a new placeholder
                List < Integer > rowOfData = new ArrayList < > ();

                // read the whole line
                // "111278","611","146","128","337"
                strings = iterator.next();

                // add the id in the header
                rowHeaders.add(strings[0]);

                for (int i = 1; i < strings.length; i++) {
                    // add the remaining data in the row
                    rowOfData.add(Integer.parseInt(strings[i]));
                }

                // add the collected data in the data array
                data.add(rowOfData);
            }

            // add the parse data into object
            object.put("data", data);
            object.put("rowHeaders", rowHeaders);

            // convert object to json string
            results = object.toJSONString();

        } catch (Exception e) {
            e.printStackTrace();
        }

        // Return JSON String

        return results.trim();

    }

    public static String jsonToCsv(String jsonString) {

        String results = "";

        try {

            // Initialize JSON Parser and CSV Writer

            JSONParser parser = new JSONParser();
            StringWriter writer = new StringWriter();
            CSVWriter csvWriter = new CSVWriter(writer, ',', '"', '\\', "\n");

            // parse json string to object
            JSONObject object = (JSONObject) parser.parse(jsonString);
            
            

            // read colHeaders
            JSONArray colHeaders = (JSONArray) object.get("colHeaders");
            
            // to store array of strings
            String[] stringArray = new String[colHeaders.size()];
            
            for (int i = 0; i < colHeaders.size(); i++) {
                stringArray[i] = (String) colHeaders.get(i);
            }
            
            csvWriter.writeNext(stringArray);

            // now get the row and other data
            // rowHeader is array of ids
            JSONArray rowHeader = (JSONArray) object.get("rowHeaders");
            // data is array of int arrays
            JSONArray data = (JSONArray) object.get("data");

            // parsed row data holder
            List < String > dataList = new ArrayList < > ();

            for (int i = 0; i < rowHeader.size(); i++) {
                // i = each entry in the csv data
                //"111278","611","146","128","337"
                
                // add id first
                stringArray[0] = (String) rowHeader.get(i);
                          
                // this returns an array again
                JSONArray scores = (JSONArray) data.get(i);
                
                long temp;
                for (int j = 0; j < scores.size(); j++) {
                    temp = (long) scores.get(j);
                    stringArray[j+1] = (Long.toString(temp));
                }

               
                csvWriter.writeNext(stringArray);
                
                // reset the List that hold the parsed row data
                dataList = new ArrayList<>();
            }


            results = writer.toString();

        } catch (Exception e) {
            e.printStackTrace();
        }

        // Return CSV String

        return results.trim();

    }

}