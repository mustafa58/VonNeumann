
public enum Command {
	HLT(000), ADD(100), SUB(200), STO(300), NOP(400), LDA(500), BR(600), BRZ(700), BRP(800), IN(901), OUT(902);
	private int arg = 0;
	private int objCode;
	
	Command(int objCode) {
		this.objCode = objCode;
	}
	
	public int getArg() {
		return arg;
	}
	public void setArg(int arg) {
		objCode = objCode-this.arg;
		this.arg = arg;
		objCode = objCode + arg;
	}
	
	public int getObjCode() {
		return objCode;
	}
}
