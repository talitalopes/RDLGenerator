package br.ufrj.cos.prisma.BPMNAPI;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Stack;

public class BPMNCodeGenerator {

	public static final String CONDITIONAL_VAR_NAME = "condStatus"; 
	private int iiAddress = 0;
	ArrayList<BPMNCode> intermediateCode = new ArrayList<BPMNCode>();

	// Stack<BPMNCode> loopStack = new Stack<BPMNCode>();
	private Stack<BPMNCode> forkStack = new Stack<BPMNCode>();
	private Stack<BPMNCode> beginThreadStack = new Stack<BPMNCode>();
	private Stack<BPMNCode> endThreadStack = new Stack<BPMNCode>();

	// stores statement if-then-else blocks to control code generation
	private Stack<BeginThenBPMN> ifStack = new Stack<BeginThenBPMN>();
    // holds the all conditions in the code to create proper BPMN variables	
	private ArrayList<EvalConditionBPMN> condStatusList = new ArrayList<EvalConditionBPMN>();
	// contains a pointer to the last diverging gateway
	private BPMNCode lastDiverging;
	
	// stores the loop-endloop blocks to control code generation 
	private Stack<BPMNCode> loopStack = new Stack<BPMNCode>();
	
	private void addCode(BPMNCode iiCode) {
		System.out.println("Adding Code...." + iiCode);
		iiCode.setAddress(String.valueOf(iiAddress));
		iiCode.setAddressNext(String.valueOf(iiAddress + 1));
		intermediateCode.add(iiCode);
		iiAddress++;
	}

	public void addExportArtifact(String modelURL) {
		BPMNCode bpmn_ii = null;
		bpmn_ii = new ExportModelBPMN(modelURL);
		this.addCode(bpmn_ii);
	}

	public void addImportArtifact(String modelURL) {
		BPMNCode bpmn_ii = null;
		bpmn_ii = new ImportModelBPMN(modelURL);
		this.addCode(bpmn_ii);
	}

	// BEGIN LOOP
	public void addBeginLoopBlock(String condition) {
		BeginLoopBPMN begin_ii = null;
		begin_ii = new BeginLoopBPMN(iiAddress);
		this.addCode(begin_ii);
		loopStack.push(begin_ii);

		EvalConditionBPMN eval_ii = null;
		eval_ii = new EvalConditionBPMN(iiAddress, condition);
		this.addCode(eval_ii);
		this.condStatusList.add(eval_ii);	
				
		EndLoopBPMN end_ii = null;
		String evalAddress = condStatusList.get(condStatusList.size()-1).getAddress();
		end_ii = new EndLoopBPMN(iiAddress,-1,CONDITIONAL_VAR_NAME + evalAddress);
		this.addCode(end_ii);
		loopStack.push(end_ii);
	}

	public void addEndLoopBlock() {

		EndLoopBPMN end_ii = (EndLoopBPMN) loopStack.pop();
		BeginLoopBPMN begin_ii = (BeginLoopBPMN) loopStack.pop();
		
		// binding the blockStack addresses
		end_ii.setEscapeAddress(String.valueOf(iiAddress));
		BPMNCode last_ii = intermediateCode.get(iiAddress-1);
		last_ii.setAddressNext(begin_ii.getAddress());
	}
	// END_LOOP

	
	public void addEvalCondition(String cond){ 
		EvalConditionBPMN bpmn_ii = null;
		bpmn_ii = new EvalConditionBPMN(iiAddress, cond);
		this.addCode(bpmn_ii);		
	}
	
	
	// ++++++++BEGIN IF 
	public void addBeginIfBlock(String cond) {
		EvalConditionBPMN bpmn_ii = null;
		bpmn_ii = new EvalConditionBPMN(iiAddress, cond);
		this.addCode(bpmn_ii);
		this.condStatusList.add(bpmn_ii);		
	}
	public void addBeginThenBlock() {
		BeginThenBPMN bpmn_ii = null;
		// AS PER RDL SYNTAX, THE BEGINTHENBLOCK STARTS AFTER AN BEGINIF (WHICH INTANTIATES AN EVALCOND Instruction). 
		// SO THE LAST EVALCOND HAS THE PROPOER ADDRESS FOR THE CONDSTATUS VARIABLE
		String evalAddress = condStatusList.get(condStatusList.size()-1).getAddress();
        bpmn_ii = new BeginThenBPMN(iiAddress, -1, CONDITIONAL_VAR_NAME + evalAddress);
		this.addCode(bpmn_ii);
		ifStack.push(bpmn_ii);
	}

	public void addEndThenBlock() {
		BPMNCode bpmn_ii = null;
		bpmn_ii = new EndIfBPMN(iiAddress, -1);
		this.addCode(bpmn_ii);
		// binding the blockStack addresses
		BeginThenBPMN begin_ii = (BeginThenBPMN) ifStack.lastElement();
		// fixes the escape address for the associated Diverging Gateway
		begin_ii.setEscapeAddress(String.valueOf(bpmn_ii.address));
		// sets the last Diverging Gateway to EndIf
		lastDiverging  = bpmn_ii; 
	}
	
	
	public void addEndIfBlock() {
		// remove last If from stack
		ifStack.pop();		
	}

	public void addBeginElseBlock() {
		// fixing associated Diverging Gateway escape address to the next ii
		BeginThenBPMN begin_ii = (BeginThenBPMN) ifStack.lastElement();
		begin_ii.setEscapeAddress(String.valueOf(iiAddress));
	}

	public void addEndElseBlock() {	
		// fixing the  addresses of the last instruction to point to 
		// the associated Converging Gateway.		
		BPMNCode last_ii = intermediateCode.get(intermediateCode.size()-1);
		last_ii.setAddressNext(String.valueOf(lastDiverging.getAddress()));
		// fixing the address of the last Converging Gateway
		// to the next available instruction
		lastDiverging.setAddressNext(String.valueOf(intermediateCode.size()));
	}
	// ++++++++END IF 
	

	public void addAssignment(String varName, String expr) {
		this.addCode(new AssignmentBPMN(varName, expr));
	}

	public void addVarDeclaration(String typeName, String varName) {
		this.addCode(new DeclarationBPMN(typeName, varName));
	}

	public void addNewClass(String packageName, String className) {
		this.addCode(new NewClassBPMN(packageName, className));
	}

	public void addClassExtension(String superName, String subPName,
			String subName) {
		this.addCode(new ClassExtensionBPMN(superName, subPName, subName));
	}

	public void addNewEnumeration(String packageName, String enumName) {
		this.addCode(new NewEnumerationBPMN(packageName, enumName));

	}

	public void addMethodExtension(String superName, String subName,
			String methName) {
		this.addCode(new MethodExtensionBPMN(superName, subName, methName));

	}

	public void addNewInheritance(String subClassName, String superClassName) {
		this.addCode(new NewGeneralizationBPMN(subClassName, superClassName));
	}

	public void addNewMethod(String className, String methodName) {
		this.addCode(new NewMethodBPMN(className, methodName));

	}

	public void addNewAttribute(String className, String attribName,
			String typeName) {
		this.addCode(new NewAttributeBPMN(className, attribName, typeName));
	}

	public void addNewPackage(String containerName, String packName) {
		this.addCode(new NewPackageBPMN(containerName, packName));

	}

	public void addRetrieveClass(String packageName) {
		this.addCode(new RetrieveClassBPMN(packageName));

	}

	public void addRetrievePackage(String string) {
		// TODO Complete

	}

	public void addNewRealization(String className, String interfName) {
		this.addCode(new NewRealizationBPMN(className, interfName));

	}

	public void addNewInterface(String packName, String interfName) {
		this.addCode(new NewInterfaceBPMN(packName, interfName));

	}

	public void addAddCode(String className, String operName, String code) {
		this.addCode(new AddCodeBPMN(className, operName, code));

	}

	public void addExternalTask(String message) {
		this.addCode(new ExternalTaskBPMN(message));

	}

	public void addFork() {
		BPMNCode bpmn_ii = null;
		bpmn_ii = new ForkBPMN(intermediateCode.size());
		this.addCode(bpmn_ii);
		forkStack.push(bpmn_ii);
	}

	public void addJoin() {
		BPMNCode bpmn_ii = null;
		bpmn_ii = new JoinBPMN(intermediateCode.size());
		this.addCode(bpmn_ii);
		// forkStack.push(bpmn_ii);
		while (!endThreadStack.isEmpty()) {
			EndThreadBPMN endThread = (EndThreadBPMN) endThreadStack.pop();
			endThread.setAddressNext(bpmn_ii.getAddress());
		}
	}

	public void addBeginThread() {
		BPMNCode bpmn_ii = null;
		bpmn_ii = new BeginThreadBPMN(intermediateCode.size());
		this.addCode(bpmn_ii);
		beginThreadStack.push(bpmn_ii);
		ForkBPMN fork_ii = (ForkBPMN) forkStack.peek();
		fork_ii.addTransitionAddress(bpmn_ii.getAddress());

	}

	public void addEndThread() {
		BPMNCode bpmn_ii = null;
		bpmn_ii = new EndThreadBPMN(intermediateCode.size());
		endThreadStack.push(bpmn_ii);
		this.addCode(bpmn_ii);

	}

	public void generateToFile(String fileName) {
		BPMNCode bpmn_ii = null;
		int codeIndex = 0;

		// this.iiCodeStub();
		// this.iiForkCodeStub();

		// link and save code to file
		try {
			FileOutputStream out = new FileOutputStream(fileName);
			PrintStream processFile = new PrintStream(out);

			processFile.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
			processFile.println("<definitions id=\"Definition\"");
			processFile.println("   targetNamespace=\"http://www.jboss.org/drools\"");
			processFile.println("   typeLanguage=\"http://www.java.com/javaTypes\"");
			processFile.println("   expressionLanguage=\"http://www.mvel.org/2.0\"");
			processFile.println("   xmlns=\"http://www.omg.org/spec/BPMN/20100524/MODEL\"");
			processFile.println("   xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"");
			processFile.println("   xsi:schemaLocation=\"http://www.omg.org/spec/BPMN/20100524/MODEL BPMN20.xsd\"");
			processFile.println("   xmlns:g=\"http://www.jboss.org/drools/flow/gpd\"");
			processFile.println("   xmlns:bpmndi=\"http://www.omg.org/spec/BPMN/20100524/DI\"");
			processFile.println("   xmlns:dc=\"http://www.omg.org/spec/DD/20100524/DC\"");
			processFile.println("   xmlns:di=\"http://www.omg.org/spec/DD/20100524/DI\"");
			processFile.println("   xmlns:tns=\"http://www.jboss.org/drools\">");

			//TODO: USE THE PROPER STATUS
			processFile.println("   <itemDefinition id=\"_condStatus\" structureRef=\"Boolean\" />");
			
			processFile.println("<process processType=\"Private\" isExecutable=\"true\" id=\"org.reusefusion.reuse\" name=\"Reuse Process\" >");

			processFile.println("<!-- process variables -->");
			
			//TODO: USE THE PROPER STATUS
			for (int i=0; i <= condStatusList.size()-1;i++){
				EvalConditionBPMN status = condStatusList.get(i);				
				processFile.println("<property id=\"condStatus"+ status.getAddress() +"\" itemSubjectRef=\"_condStatus\"/>");
			}
			//processFile.println("<property id=\"condStatus3\" itemSubjectRef=\"_condStatus\"/>");			
			//processFile.println("<property id=\"condStatus4\" itemSubjectRef=\"_condStatus\"/>");			
			
			codeIndex = 0;
//TODO: Check why the BPMN NODE IDs can only be numbers!!!!! Then remove the _10000000 limit!!!
			processFile.println("<!-- Start Process-->");
			processFile.println("<startEvent id=\"_100000\" name=\"StartProcess\" />");
			processFile.println("<sequenceFlow id=\"_-1-_0\" sourceRef=\"_100000\" targetRef=\"_0\" />");

			while (codeIndex <= intermediateCode.size() - 1) {
				bpmn_ii = intermediateCode.get(codeIndex);
				processFile.println(bpmn_ii.getCode());
				codeIndex++;
			}

			processFile.println("<!-- End Process -->");
			processFile.println("<endEvent id=\"_"+ codeIndex +"\" name=\"EndProcess\" >");
			processFile.println("    <terminateEventDefinition/>");
			processFile.println("</endEvent>");

			//processFile.println("<sequenceFlow id=\"_"+ (codeIndex-1)+"-_3\" sourceRef=\"_2\" targetRef=\"_3\" />	");

			processFile.println("</process>");
			processFile.println("</definitions>");

			processFile.close();
			out.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}




}
