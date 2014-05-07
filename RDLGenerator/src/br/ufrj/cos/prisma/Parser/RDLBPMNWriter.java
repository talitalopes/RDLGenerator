package br.ufrj.cos.prisma.Parser;

import br.ufrj.cos.prisma.BPMNAPI.BPMNCodeGenerator;
import br.ufrj.cos.prisma.model.ModelNode;
import br.ufrj.cos.prisma.util.Constants;

public class RDLBPMNWriter {

	private BPMNCodeGenerator codegen;

	public RDLBPMNWriter() {
		codegen = new BPMNCodeGenerator();
	}

	public void prepareRDLFile() {
		codegen.addImportArtifact(Constants.MODEL_URL);
		codegen.addExportArtifact(Constants.MODEL_OUTPUT_URL);

		codegen.addVarDeclaration("void", Constants.PACKAGE_VAR_NAME);
		codegen.addNewPackage("appmodel", Constants.PACKAGE_NAME);

		codegen.addAssignment(Constants.PACKAGE_VAR_NAME,
				Constants.TEMP_VAR_ASSIGNMENT);
	}

	public void addClassExtensionOrMethodExtension(ModelNode mNode) {
		String name = mNode.getName();

		if (name.contains(Constants.CLASS_EXTENSION_PREFIX)) {
			String superName = name.split("_")[1];
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