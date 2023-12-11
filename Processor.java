import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class Processor {
	
	short [] instructionMemory= new short[1024]; 
	byte [] dataMemory= new byte[2048];
	Register[] Register=new Register[65];
	short PC=0;
	int clockCycles=1;
	int totalCycles=0;
	short Fetched=0;
	short toBeDecoded=0;
	boolean isFetched=true;
	boolean isDecoded=true;
	boolean isExecuted=true;
	int opcode=0;
	int op1=0;
	int op2=0;
	int operand1=0;
	int operand2=0;
	int opcodex=0;
	int op1x=0;
	int op2x=0;
	int operand1x=0;
	int operand2x=0;
	int c=0;
	int v=0;
	int n=0;
	int s=0;
	int z=0;
	int printDecode=1;
	public Processor(String filename) throws FileNotFoundException {
		this.read(filename);
		int n=0;
		while(this.instructionMemory[n]!=0) 
			n++;
		System.out.println("****************************************************");
		System.out.println("The processor has started executing the program!!!");
		System.out.println("****************************************************");
		//totalCycles=3+((n-1)*1);
		//System.out.println("Total cycles ="+totalCycles);
		
		//Inserting data to registers
		Register[0]=new Register("Status",(byte)0);
		for(byte i=1;i<Register.length;i++) {
			Register[i]=new Register("R"+i,(byte)i);
		}
		//Inserting data to data memory
		byte c=0;
		for(int i=0;i<dataMemory.length;i++) {
			dataMemory[i]=c;
			c++;
		}
		while(isExecuted) {
			System.out.println();
			System.out.println();
			System.out.println("At clockcycle : "+clockCycles);
			if(clockCycles==1) {
				fetch();
				System.out.println("At Decode stage : none");
				System.out.println("At Execute stage : none");
			}
			else if(clockCycles==2){
				fetch();
				decode();
				System.out.println("At Execute stage : none");
			}
			else if(!isDecoded){
				isExecuted=false;
				System.out.println("At Fetch stage : none");
				System.out.println("At Decode stage : none");
				execute();
				break;
			}
			else if(!isFetched){
				isDecoded=false;
				System.out.println("At Fetch stage : none");
				decode();
				execute();
			}
			else {
				fetch();
				decode();
				execute();
			}
			
			clockCycles++;
		}
		System.out.println();
		System.out.println();
		System.out.println("After Last clock cycle :-");
		System.out.println("---------------------------");
		System.out.println("1-Registers:-");
		System.out.println("--------------");
		System.out.println("Register Name: PC   Register value = "+PC);
		System.out.println("Register Name: Status   Register value = 00"+fromDecimalToBinary(Register[0].value+""));
		
		for(int i=1;i<Register.length;i++) {
			System.out.println("Register Name: "+Register[i].name+"   Register value = "+Register[i].value);
		}
		
		System.out.println("-----------------------------------------------------------------------------");
		System.out.println("-----------------------------------------------------------------------------");
		System.out.println("2-Instruction Memory:-");
		System.out.println("------------------------");
		for(int i=0;i<instructionMemory.length;i++) {
			System.out.println(i+" ["+fromDecimalToBinary16(instructionMemory[i]+"")+"]");
		}
		System.out.println("-----------------------------------------------------------------------------");
		System.out.println("-----------------------------------------------------------------------------");
		System.out.println("3-Data Memory:-");
		System.out.println("------------------------");
		for(int i=0;i<dataMemory.length;i++) {
			System.out.println(i+" ["+dataMemory[i]+"]");
		}
		System.out.println();
		System.out.println("****************************************************");
		System.out.println("The processor has finished executing the entire program!!!");
		System.out.println("****************************************************");
	}
		
	
	
	public void fetch() {
		System.out.println("At Fetch stage : Instruction "+(PC+1));
		System.out.println("Input Parameters :-");
		System.out.println("PC = "+PC);
		toBeDecoded=Fetched;
		Fetched=instructionMemory[PC];
		PC++;
		System.out.println("PC register value has changed to : "+PC);
		if(instructionMemory[PC]==0)
			isFetched=false;
	
		
	}
	
	public void decode() {
		
		int print=0;
		for(int i=0;i<instructionMemory.length;i++) {
			if(instructionMemory[i]==toBeDecoded) {
				print=i+1;
				break;
			}
		}
		System.out.println("At Decode stage : Instruction "+print);
		System.out.println("Input Parameters :-");
		System.out.println(toBeDecoded);
		System.out.println("Instruction to be decoded : "+fromDecimalToBinary16(toBeDecoded+""));
		opcode= opcodex;
		op1= op1x;
		op2= op2x;
		operand1= operand1x;
		operand2= operand2x;
		opcodex=(toBeDecoded & 0b1111000000000000)>>12;
		op1x=(toBeDecoded & 0b0000111111000000)>>>6;
		op2x=toBeDecoded & 0b0000000000111111;
		operand1x=Register[op1x].value;
		operand2x=op2x;
		if(opcodex==0||opcodex==1||opcodex==2||opcodex==6||opcodex==7)
			operand2x=Register[op2x].value;
		if(!isFetched) {
			toBeDecoded=Fetched;
		}
		
		
		}
	
	public void execute() {
		if(!isDecoded&&!isExecuted) {  //--------------------------????-----------------------------------
			opcode= opcodex;
			op1= op1x;
			op2= op2x;
			operand1= operand1x;
			operand2= operand2x;
		}
		int print=0;
		String toBeExecuted=fromDecimalToBinary(opcode+"")+fromDecimalToBinary(op1+"")+fromDecimalToBinary(op2+"");
		short toBeExecutedDecimal=fromBinaryToDecimal(toBeExecuted);
		for(int i=0;i<instructionMemory.length;i++) {
			if(instructionMemory[i]==toBeExecutedDecimal) {
				print=i+1;
				break;
			}
		}
		System.out.println("At Execute stage : Instruction "+print);
		System.out.println("Input Parameters :-");
		System.out.print("opcode = ");
		for(int i=5;i>=2;i--)   //------------------------------------????--------------------------------------------------//
			System.out.print(fromDecimalToBinary(opcode+"").charAt(i));
		System.out.println();
		
		System.out.println("Operand1 = "+operand1);
		System.out.println("Operand2 = "+operand2);
		
		int outputC=0;
		int outputN=0;
		int op1Sign=0;
		int op2Sign=0;
		int outputSign=0;
		//add
		if(opcode==0) {
			outputC=((operand1+operand2)&0b00000000000000000000000100000000)>>8;
			outputN=((operand1+operand2)&0b00000000000000000000000010000000)>>7;
			op1Sign=((operand1)&0b00000000000000000000000010000000)>>7;
			op2Sign=((operand2)&0b00000000000000000000000010000000)>>7;
			outputSign=((operand1+operand2)&0b00000000000000000000000010000000)>>7;
			
			if(outputC==1) {
				c=1;
			}
			else {
				c=0;
			}
			if(op1Sign==op2Sign) {
				if(outputSign!=op1Sign) {
					v=1;
				}
				v=0;
			}
			if(outputN==1) {
				n=1;
			}
			else {
				n=0;
			}
			s=n^v;
			if((operand1+operand2)==0){
				z=1;
			}
			else {
				z=0;
			}
			Register[op1].value=(byte)(operand1+operand2);
			System.out.println("Register "+op1+" value changed to : "+Register[op1].value);
			String statusStr="000"+c+v+n+s+z;
			Register[0].value=(byte)fromBinaryToDecimal(statusStr);
			System.out.println("Status value Register has changed to : "+statusStr);
		}
		//sub
		else if(opcode== 1) {
			op1Sign=((operand1)&0b00000000000000000000000010000000)>>7;
			op2Sign=((operand2)&0b00000000000000000000000010000000)>>7;
			outputSign=((operand1-operand2)&0b00000000000000000000000010000000)>>7;
			outputN=((operand1-operand2)&0b00000000000000000000000010000000)>>7;
			if(op1Sign!=op2Sign) {
				if(outputSign==op2Sign) {
					v=1;
				}
				v=0;
			}
			
			if(outputN==1) {
				n=1;
			}
			else {
				n=0;
			}
			s=n^v;
			if((operand1-operand2)==0){
				z=1;
			}
			else {
				z=0;
			}
			Register[op1].value=(byte)(operand1-operand2);
			System.out.println("Register "+op1+" value changed to : "+Register[op1].value);
			String statusStr="000"+c+v+n+s+z;
			Register[0].value=(byte)fromBinaryToDecimal(statusStr);
			System.out.println("Status Register value has changed to : "+statusStr);
		
		}
		//mul
		else if(opcode== 2) {
			outputN=((operand1*operand2)&0b00000000000000000000000010000000)>>7;
			if(outputN==1) {
				n=1;
			}
			else {
				n=0;
			}
			if((operand1*operand2)==0){
				z=1;
			}
			else {
				z=0;
			}
			Register[op1].value=(byte)(operand1*operand2);
			System.out.println("Register "+op1+" value changed to : "+Register[op1].value);
			String statusStr="000"+c+v+n+s+z;
			Register[0].value=(byte)fromBinaryToDecimal(statusStr);
			System.out.println("Status Register value has changed to : "+statusStr);
		}
		//movi
		else if(opcode== 3) {
			Register[op1].value=(byte)operand2;
			System.out.println("Register "+op1+" value changed to : "+Register[op1].value);
		}
		//beqz
		else if(opcode== 4) {
			if(operand1 == 0) {
				PC = (short)(PC+operand2) ;
				}
			System.out.println("PC value has changed to "+PC);
		}
		//andi
		else if(opcode== 5) {
			outputN=((operand1&operand2)&0b00000000000000000000000010000000)>>7;
			if(outputN==1) {
				n=1;
			}
			else {
				n=0;
			}
			if((operand1&operand2)==0){
				z=1;
			}
			else {
				z=0;
			}
			Register[op1].value=(byte)(operand1&operand2);
			System.out.println("Register "+op1+" value changed to : "+Register[op1].value);
			String statusStr="000"+c+v+n+s+z;
			Register[0].value=(byte)fromBinaryToDecimal(statusStr);
			System.out.println("Status Register value has changed to : "+statusStr);
		}
		//eor
		else if(opcode== 6) {
			outputN=((operand1^operand2)&0b00000000000000000000000010000000)>>7;
			if(outputN==1) {
				n=1;
			}
			else {
				n=0;
			}
			if((operand1^operand2)==0){
				z=1;
			}
			else {
				z=0;
			}
			Register[op1].value=(byte)(operand1^operand2);
			System.out.println("Register "+op1+" value changed to : "+Register[op1].value);
			String statusStr="000"+c+v+n+s+z;
			Register[0].value=(byte)fromBinaryToDecimal(statusStr);
			System.out.println("Status Register value has changed to : "+statusStr);
		}
		//br
		else if(opcode== 7) {
			String ss=operand1+operand2+"";
			
			PC=(short)(fromBinaryToDecimal(ss));
			System.out.println("PC value has changed to : "+PC);
		}
		//sal
		else if(opcode== 8) {
			outputN=((operand1<<operand2)&0b00000000000000000000000010000000)>>7;
			if(outputN==1) {
				n=1;
			}
			else {
				n=0;
			}
			if((operand1<<operand2)==0){
				z=1;
			}
			else {
				z=0;
			}
			Register[op1].value =(byte)(operand1 << operand2);
			System.out.println("Register "+op1+" value changed to : "+Register[op1].value);
			String statusStr="000"+c+v+n+s+z;
			Register[0].value=(byte)fromBinaryToDecimal(statusStr);
			System.out.println("Status Register value has changed to : "+statusStr);
		}
		//sar
		else if(opcode== 9) {
			outputN=((operand1>>operand2)&0b00000000000000000000000010000000)>>7;
			if(outputN==1) {
				n=1;
			}
			else {
				n=0;
			}
			if((operand1>>operand2)==0){
				z=1;
			}
			else {
				z=0;
			}
			Register[op1].value = (byte)(operand1 >> operand2);
			System.out.println("Register "+op1+" value changed to : "+Register[op1].value);
			String statusStr="000"+c+v+n+s+z;
			Register[0].value=(byte)fromBinaryToDecimal(statusStr);
			System.out.println("Status Register value has changed to : "+statusStr);
		}
		//ldr
		else if(opcode== 10) {
			Register[op1].value = dataMemory[operand2];
			System.out.println("Register "+op1+" value changed to : "+Register[op1].value);
			
		}
		//str
		else if(opcode== 11) {
			dataMemory[operand2] = (byte)operand1;
			System.out.println("Data value of address " + operand2+" has changed to "+dataMemory[operand2]);
		}
		

		
		
		
		
	}
	
	
	
	
	
	
	
	
	
	public  void read(String fileName) throws FileNotFoundException{
    	
	     File file = new File(fileName);
	     Scanner sc = new Scanner(file);
	     int mempointer=0;
	     while (sc.hasNextLine()) {
	    	String [] instruction= sc.nextLine().split(" ");
	    	String opcode="";
	    	String snum1="";
    		String snum2="";
	    	String r1="";
	    	String r2="";
	    	String inst="";
	    	short instnum=0;
	    	switch(instruction[0]) {
	    	case "ADD":opcode="0000";break;//0 R
	    	case "SUB":opcode="0001";break;//1 R
	    	case "MUL":opcode="0010";break;//2 R
	    	case "MOVI":opcode="0011";break;//3 I
	    	case "BEQZ":opcode="0100";break;//4 I
	    	case "ANDI":opcode="0101";break;//5 I
	    	case "EOR":opcode="0110";break;//6 R
	    	case "BR":opcode="0111";break;//7 R
	    	case "SAL":opcode="1000";break;//8 I 
	    	case "SAR":opcode="1001";break;//9 I
	    	case "LDR":opcode="1010";break;//10 I
	    	case "STR":opcode="1011";break;//11 I
	    		default:opcode="";
	    	}
	    	
	    	
	    	if(!(instruction[2].charAt(0)=='R')){					// I-type
	    		
	    		for(int i=1;i<instruction[1].length();i++) {
	    			snum1+=instruction[1].charAt(i);
	    		}
	    		snum2=instruction[2];
	    		
	    	}
	    	else {													//R-type
	    		
	    		for(int i=1;i<instruction[1].length();i++) {
	    			snum1+=instruction[1].charAt(i);
	    		}
	    		for(int i=1;i<instruction[2].length();i++) {
	    			snum2+=instruction[2].charAt(i);
	    		}
	    		
	    		
	    	}
	    	r1=fromDecimalToBinary(snum1);
    		r2=fromDecimalToBinary(snum2);
	    	inst=opcode+r1+r2;
	    	instnum=fromBinaryToDecimal(inst);
	    	this.instructionMemory[mempointer]=instnum;
	    	mempointer++;
	     }
	     	
	    	
		}
	
	public short fromBinaryToDecimal(String s) {
		short output=0;
		int c=0;
		for(int i=s.length()-1;i>=0;i--) {
			int x= Integer.parseInt(s.charAt(i)+"");
			output+=Math.pow(2,c)*x;
			c++;
		}
		return output;
	}
	
	public String fromDecimalToBinary(String s) {
		int snum=Integer.parseInt(s);
		String output="";
		while(snum!=0) {
			if(snum%2==0) {
				output=0+output;
			}
			else {
				output=1+output;
			}
			snum=snum/2;
		}
		for(int i=output.length();i<6;i++) {
			output=0+output;
		}
		return output;
	}
	
	public String fromDecimalToBinary16(String s) {
		int snum=Integer.parseInt(s);
		String output="";
		while(snum!=0) {
			if(snum%2==0) {
				output=0+output;
			}
			else {
				output=1+output;
			}
			snum=snum/2;
		}
		for(int i=output.length();i<16;i++) {
			output=0+output;
		}
		return output;
	}
	
	public static void main(String[] args) throws FileNotFoundException {
		Processor p = new Processor("Program.txt");
		
	
	}
	
	
	
}
