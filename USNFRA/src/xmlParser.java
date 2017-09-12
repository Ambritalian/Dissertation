import java.io.File;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
//A class that reads an XML file and outputs a list of key, value pairs.
public class xmlParser {
	public ArrayList<String> parse(String XML_File_Name) throws Exception {
		String sequence = "";
		// Retrieve and parse XML document
	    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	    DocumentBuilder db = dbf.newDocumentBuilder();
	    Document document = db.parse(new File(XML_File_Name));
	    // Retrieve all element tags
	    NodeList nodeList = document.getElementsByTagName("*");
        for (int i=0; i<nodeList.getLength(); i++) {
    		String letter = "";
        	Element element = (Element)nodeList.item(i);
        	NamedNodeMap attrs = element.getAttributes();
        	for (int k = 0; k < attrs.getLength(); k++) {
        		// For each element
        		// Get attributes and create letters of the form (attribute_name, attribute_value) out of them
        		// N.B. Attributes may be returned in a different order to which they have been read
        		Attr attr = (Attr)attrs.item(k);
        		letter = letter + "(" + attr.getName() + "," + attr.getValue() + ")";
	        }
        	// Create words of the form <Element, open> (letters) <Element,close>
        	if (letter.equals("")) ;
        	else sequence = sequence + "<" + element.getNodeName() + "," + "open" + ">" + letter + "<" + element.getNodeName() + "," + "close" + ">";
        }
		ArrayList<Integer> index = new ArrayList<Integer>();
		// Convert sequence to array of characters
		char[] in = sequence.toCharArray();
		// Add to list of indexes every time a certain case is triggered
		for (int i=0;i<in.length;i++) {
			// First data letter
			if (in[i] == '>') {
				if (i+1 < in.length && in[i+1] == '(') index.add(i+1);
			}
			// For # of data letters > 2
			// Add to index twice, first time to show the end of the data letter
			// Second to show the start of the next data letter
			if (in[i] == ')') {
				if (in[i+1] == '(') {
					index.add(i+1);
					index.add(i+1);
				}
			}
			// Last data letter
			if (in[i] == ')') {
				if (in[i+1] == '<') index.add(i+1);
			}
		}
		// Get substring between each pair of indices in arraylist 
		ArrayList<String> letters = new ArrayList<String>();
		for (int i=0;i<index.size();i+=2) {
			String word = sequence.substring(index.get(i), index.get(i+1));
			letters.add(word);
		}
		return letters;
	}
}
//<CD,open>(Genre,Rock)(Rating,***)<CD,close><CD,open>(Genre,Rock)(Rating,****)<CD,close><CD,open>(Genre,Pop)(Rating,****)<CD,close><CD,open>(Genre,Blues)(Rating,***)<CD,close>
//<book,open>(cat,action)(id,bk101)(type,hardback)<book,close><title,open>(lang,en)<title,close><book,open>(id,bk101)(type,hardback)<book,close><book,open>(id,bk103)<book,close><author,open>(status,alive)<author,close>