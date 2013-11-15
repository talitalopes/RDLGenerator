package br.ufrj.cos.prisma.Parser;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.jgrapht.DirectedGraph;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XPDLGraph {

	private static String ACTIVITY_TAG = "Activity";
	private static String TRANSITION_TAG = "Transition";
	
	private static Map<String, Node> nodesIds = new HashMap<String, Node>();
	private static DirectedGraph<Node, DefaultEdge> g = new DefaultDirectedGraph<Node, DefaultEdge>(
			DefaultEdge.class);

	public static DirectedGraph<Node, DefaultEdge> getGraph(String f) {
		if (f == null) {
			log("You must provide a valid url for a XPDL model");
			return null;
		}
		
		createGraph(f);
		return g;
	}

	private static void createGraph(String fileUrl) {
		// read XPDL file
		Document doc = getDomObject(fileUrl);

		// get activity elements
		NodeList activitiesNodes = getNodesWithType(doc, ACTIVITY_TAG);
		createNodesForActivities(activitiesNodes);

		// get transition elements
		NodeList transitions = getNodesWithType(doc, TRANSITION_TAG);

		// create nodes for activities
		createNodesForActivities(activitiesNodes);
		
		// create edges for transitions
		createEdges(transitions);
	}

	private static void createNodesForActivities(NodeList nodes) {
		for (int temp = 0; temp < nodes.getLength(); temp++) {
			Node node = nodes.item(temp);
			if (node == null || node.getNodeType() != Node.ELEMENT_NODE) {
				continue;
			}

			Element element = (Element) node;
			String id = element.getAttribute("Id");

			g.addVertex(node);
			nodesIds.put(id, node);
		}
	}
	
	private static void createEdges(NodeList transitions) {
		for (int temp = 0; temp < transitions.getLength(); temp++) {
			Node sequenceNode = transitions.item(temp);
			if (sequenceNode == null
					|| sequenceNode.getNodeType() != Node.ELEMENT_NODE) {
				continue;
			}

			Element sequenceElement = (Element) sequenceNode;
			String sourceId = sequenceElement.getAttribute("From");
			String targetId = sequenceElement.getAttribute("To");

			log(String.format(
					"Source: %s |  Target: %s | existskey: %b", sourceId,
					targetId, nodesIds.containsKey(sourceId)));

			if (nodesIds.get(sourceId) != null
					&& nodesIds.get(targetId) != null) {
				g.addEdge(nodesIds.get(sourceId), nodesIds.get(targetId));
			}
		}
	}

	private static Document getDomObject(String file) {
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

	private static NodeList getNodesWithType(Document doc, String type) {
		return doc.getElementsByTagName(type);
	}
	
	private static void log(String message) {
		System.out.println(message);
	}

}
