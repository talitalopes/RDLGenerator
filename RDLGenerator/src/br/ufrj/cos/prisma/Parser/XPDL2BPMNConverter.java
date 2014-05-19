package br.ufrj.cos.prisma.Parser;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import br.ufrj.cos.prisma.BPMNAPI.BPMNCodeGenerator;
import br.ufrj.cos.prisma.util.Constants;
import br.ufrj.cos.prisma.util.Util;

public class XPDL2BPMNConverter {

	private static XPDL2BPMNConverter instance = new XPDL2BPMNConverter();

	protected Document doc;

	public static XPDL2BPMNConverter getInstance() {
		return instance;
	}

	private XPDL2BPMNConverter() {
		this.doc = Util.getDomObject(Constants.XPDLFile);
	}

	public void convert() {
		NodeList nodes = Util
				.getNodesWithType(doc, Constants.XPDL_ACTIVITY_TAG);

		BPMNCodeGenerator codeGen = new BPMNCodeGenerator();

		for (int temp = 0; temp < nodes.getLength(); temp++) {
			Node node = nodes.item(temp);
			if (node == null || node.getNodeType() != Node.ELEMENT_NODE) {
				continue;
			}

			Element element = (Element) node;
			String name = element.getAttribute("Name");
			String id = element.getAttribute("Id");
			System.out.println(name);

			if (name.contains("GATEWAY")) {
				Node splitNode = element.getElementsByTagName("Split").item(0);
				if (splitNode != null && splitNode.getNodeType() == Node.ELEMENT_NODE) {
					Element elementSplit = (Element) splitNode;
					String type = elementSplit.getAttribute("Type");
					codeGen.addFork();
				}
				
				Node joinNode = element.getElementsByTagName("Join").item(0);
				if (joinNode != null && joinNode.getNodeType() == Node.ELEMENT_NODE) {
					Element elementJoin = (Element) joinNode;
					String type = elementJoin.getAttribute("Type");
					System.out.println("Join " + id + " " + type);
					codeGen.addJoin();
				}
				
			} else {
				codeGen.addClassExtension(name, "?", "test");
			}
			
		}

		codeGen.generateToFile("output/rdl.test.txt");
	}

}
