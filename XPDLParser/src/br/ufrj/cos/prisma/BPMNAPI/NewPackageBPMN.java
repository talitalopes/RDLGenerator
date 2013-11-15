package br.ufrj.cos.prisma.BPMNAPI;

public class NewPackageBPMN extends BPMNCode {

	public static final String NEW_PACKAGE= "NewPackage";
	public static final String P_CONTAINER_NAME = "CONTAINER_NAME";
	public static final String P_PACKAGE_NAME = "PACKAGE_NAME";

	public NewPackageBPMN(String containerName, String packageName) {
		this.name = NEW_PACKAGE;
		this.addParam(P_CONTAINER_NAME, containerName);
		this.addParam(P_PACKAGE_NAME, packageName);
	}
}
