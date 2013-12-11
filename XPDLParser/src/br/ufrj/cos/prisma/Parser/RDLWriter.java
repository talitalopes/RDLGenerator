package br.ufrj.cos.prisma.Parser;

import org.w3c.dom.Element;

import br.ufrj.cos.prisma.model.ModelNode;
import br.ufrj.cos.prisma.util.Constants;

public class RDLWriter {

	public RDLWriter() {
	}

	public void prepareRDLFile() {
		System.out.println(String.format("import '%s';", Constants.MODEL_URL));
		System.out.println(String.format("export '%s';", Constants.MODEL_OUTPUT_URL));
		System.out.println("\n");
		System.out.println("COOKBOOK GEFProducts");		 
		System.out.println("\n");
		System.out.println("RECIPE main() {");
		System.out.println("\n");
		System.out.println(String.format("%s = NEW_PACKAGE(appmodel,\"?\");", Constants.PACKAGE_VAR_NAME));		
	}

	public void addClassExtensionOrMethodExtension(ModelNode mNode) {
		Element el = (Element) mNode.getNode();

		if (el.getAttribute("Name").contains(Constants.CLASS_EXTENSION_PREFIX)) {
			String superName = el.getAttribute("Name").split("_")[1];
			System.out.println(String.format("CLASS_EXTENSION(%s, %s, \"?\");",
					superName, Constants.PACKAGE_VAR_NAME));
		}
	}

	public void addBeginIf() {
		System.out.println("IF (Condition) {");
	}

	public void addEndIf() {
		System.out.println("}");
	}

	public void addBeginLoop() {
		System.out.println("LOOP (Condition) {");
	}

	public void addEndLoop() {
		System.out.println("}");
	}

	public void generateRDLFile() {
//		codegen.generateToFile(Constants.RDL_OUTPUT);
	}

	public void closeScript() {
		 System.out.println("}");
	}

}