package web;

import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * This class provides methods for manipulating XML documents.
 * 
 * @author Abraham Lin
 */
public class XMLUtil {
	/**
	 * Parses an XML string into an XML document.
	 * 
	 * @param xmlString
	 *            the XML document in <code>String</code> form
	 * @return the XML document in <code>Document</code> form
	 */
	public static Document parseXML( String xmlString ) {
		Document document = null;
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance( );
		try {
			DocumentBuilder builder = factory.newDocumentBuilder( );
			document = builder.parse( new InputSource( new StringReader(
					xmlString ) ) );
		} catch( ParserConfigurationException e ) {
			// TODO: Handle exception
		} catch( SAXException e ) {
			// TODO: Handle exception
		} catch( IOException e ) {
			// TODO: Handle exception
		}
		
		return document;
	}
}
