package br.ufrj.cos.prisma.BPMNAPI;

public class ExternalTaskBPMN extends BPMNCode {
	
	public static final String EXT_TASK = "ExternalTask";
	public static final String MESSAGE = "MESSAGE";

	public ExternalTaskBPMN(String message) {

		this.name = ExternalTaskBPMN.EXT_TASK;
		this.addParam(ExternalTaskBPMN.MESSAGE, message);
	}
	

}
