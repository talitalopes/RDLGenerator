package br.ufrj.cos.prisma.BPMNAPI;

public class EvalConditionBPMN extends BPMNCode {
	
	
	public static final String EVAL_CONDITION = "EvalConditionBPMN";
	public static final String P_CURRENT_ADDRESS = "CURRENT_ADDRESS";
	public static final String P_CONDITION = "CONDITION";
	
	public EvalConditionBPMN(int curAddress, String cond) {
		this.name = EVAL_CONDITION;
		this.addParam(P_CURRENT_ADDRESS,String.valueOf(curAddress));
		this.addParam(P_CONDITION,cond);
	}

}
