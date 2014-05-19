package br.ufrj.cos.prisma.util;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Util {

	public static void log(String message) {
		if (Constants.VERBOSE) {
			System.out.println(message);
		}
	}

	public static void log(String format, String message) {
		if (Constants.VERBOSE) {
			System.out.println(String.format(format, message));
		}
	}
	
	/**
	 * Returns the DOM object from the given XPDL file.
	 * **/
	public static Document getDomObject(String file) {
		File fXmlFile = new File(file);

		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder;
		Document doc = null;
		try {
			dBuilder = dbFactory.newDocumentBuilder();
			doc = dBuilder.parse(fXmlFile);
			doc.getDocumentElement().normalize();

		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return doc;
	}
	
	public static NodeList getNodesWithType(Document doc, String type) {
		return doc.getElementsByTagName(type);
	}

}
