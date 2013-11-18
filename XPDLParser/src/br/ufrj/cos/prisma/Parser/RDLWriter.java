package br.ufrj.cos.prisma.Parser;

import org.w3c.dom.Element;

import br.ufrj.cos.prisma.BPMNAPI.BPMNCodeGenerator;
import br.ufrj.cos.prisma.model.ModelNode;
import br.ufrj.cos.prisma.util.Constants;

public class RDLWriter {

	private BPMNCodeGenerator codegen;
	
	public RDLWriter() {
		codegen = new BPMNCodeGenerator();
	}
	
	public void prepareRDLFile() {
		codegen.addImportArtifact(Constants.MODEL_URL);
		codegen.addExportArtifact(Constants.MODEL_OUTPUT_URL);

		codegen.addVarDeclaration("void", Constants.PACKAGE_VAR_NAME);
		
		// TODO: confirm container value.
		codegen.addNewPackage("appmodel", Constants.PACKAGE_NAME);
		
		codegen.addAssignment(Constants.PACKAGE_VAR_NAME,
				Constants.TEMP_VAR_ASSIGNMENT);
	}
	
	public void addClassExtensionOrMethodExtension(ModelNode mNode) {
		Element el = (Element) mNode.getNode();

		if (el.getAttribute("Name").contains(Constants.CLASS_EXTENSION_PREFIX)) {
			String superName = el.getAttribute("Name").split("_")[1];
			// The user provides the name of the subclass
			codegen.addClassExtension(superName, Constants.PACKAGE_VAR_NAME,
					"?");
		}
	}

	public void addBeginIf() {
		codegen.addBeginIfBlock("Loop?");
		codegen.addBeginThenBlock();
	}

	public void addEndIf() {
		codegen.addEndThenBlock();
		codegen.addEndIfBlock();
	}
	
	public void addBeginLoop() {
		codegen.addBeginLoopBlock("Loop?");
	}

	public void addEndLoop() {
		codegen.addEndLoopBlock();
	}

	public void addFork() {
		codegen.addFork();
	}

	public void addJoin() {
		codegen.addJoin();
	}

	public void generateRDLFile() {
		codegen.generateToFile(Constants.RDL_OUTPUT);
	}

}