package fileProcessing; 
  
import java.io.BufferedReader; 
import java.io.FileNotFoundException; 
import java.io.IOException; 
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList; 
import java.util.HashMap;
import java.util.Map;
  
//class to simplify reading from file. 
public class MyFileReader { 
      
    private BufferedReader inputStream; 
      
    public MyFileReader(URL filename) { 
        try { 
            BufferedReader br = new BufferedReader(new InputStreamReader(filename.openStream())); 
            inputStream = br; 
        } catch (FileNotFoundException e) { 
            System.out.println(filename); 
            throw new RuntimeException("File not found"); 
        } catch (IOException e) {
			e.printStackTrace();
		} 
          
    } 
      
    public String readLine() { 
        if (inputStream != null) { 
            try { 
                return inputStream.readLine(); 
            } catch (IOException e) { 
                e.printStackTrace(); 
                throw new RuntimeException("Problem reading from file"); 
            } 
        } else { 
            throw new RuntimeException("Problem opening the file"); 
        } 
    } 
      
    public ArrayList<String> readRemainingLines() { 
        if (inputStream != null) { 
            ArrayList<String> lines = new ArrayList<String>(); 
            String line = readLine(); 
            while (line != null) { 
                lines.add(line); 
                line = readLine(); 
            } 
            return lines; 
        } else { 
            throw new RuntimeException("Problem opening the file"); 
        } 
    } 
    
    /**
     * Splits an array of strings into "tags", where tags look like <TAGNAME></TAGNAME>
     * (but each on its own line...)
     * @param linesToSplit
     * @return map from the tag name to the list of strings contained within that tag
     */
    public static Map<String,ArrayList<String>> splitByTags(ArrayList<String> linesToSplit) {    	
    	Map<String, ArrayList<String>> mapToReturn = new HashMap<String, ArrayList<String>>();
    	int i = 0;
    	ArrayList<String> currentTagContents = null;
    	String tagName = null;
    	while (i < linesToSplit.size()) {
    		String aString = linesToSplit.get(i);
    		if (tagName == null) {
	    		if (aString.matches("^<.*>$")) {
	    			tagName = aString.substring(1, aString.length()-1);
	    			currentTagContents = new ArrayList<String>();
	    		}
    		} else {
    			if (aString.matches("^</"+tagName+">$")) {
	    			mapToReturn.put(tagName, currentTagContents);
	    			tagName = null;
	    			currentTagContents = null;
	    		} else {
	    			currentTagContents.add(aString);
	    		}
    		}
    		++i;
    	}
    	if (tagName != null) {
    		throw new RuntimeException("Did not find closing tag for: "+tagName);
    	}
    	return mapToReturn;
    }
      
    public void close() { 
        try { 
            inputStream.close(); 
        } catch (IOException e) { 
            e.printStackTrace(); 
        } 
    } 
      
} 