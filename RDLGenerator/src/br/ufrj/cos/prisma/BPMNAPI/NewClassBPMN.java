package br.ufrj.cos.prisma.BPMNAPI;

public class NewClassBPMN extends BPMNCode {
	
	public static final String NEW_CLASS= "NewClass";
	public static final String P_PACKAGE_NAME = "PACKAGE_NAME";
	public static final String P_CLASS_NAME = "CLASS_NAME";

	public NewClassBPMN(String packageName, String className) {
		this.name = NEW_CLASS;
		this.addParam(P_PACKAGE_NAME, packageName);
		this.addParam(P_CLASS_NAME, className);
	}

}
