package br.ufrj.cos.prisma.BPMNAPI;




public class AssignmentBPMN extends BPMNCode {
	
	public static final String ASSIGNMENT = "Assignment";
	public static final String P_VAR_NAME = "VAR_NAME";
	public static final String P_EXPR = "EXPR";	

	public AssignmentBPMN(String varName, String expr) {
		
		this.name = AssignmentBPMN.ASSIGNMENT;
		this.addParam(AssignmentBPMN.P_VAR_NAME, varName);
		this.addParam(AssignmentBPMN.P_EXPR, expr);
	}
	



}
