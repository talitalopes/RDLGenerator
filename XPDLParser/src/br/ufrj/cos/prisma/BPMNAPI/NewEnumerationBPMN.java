package br.ufrj.cos.prisma.BPMNAPI;

public class NewEnumerationBPMN extends BPMNCode {

	public static final String NEW_ENUMERATION = "NewEnumeration";
	public static final String P_PACKAGE_NAME = "PACKAGE_NAME";
	public static final String P_ENUM_NAME = "ENUM_NAME";
	
	public NewEnumerationBPMN(String packageName, String enumName) {
		this.name = NEW_ENUMERATION;
		this.addParam(P_PACKAGE_NAME, packageName);
		this.addParam(P_ENUM_NAME, enumName);
	}

}
