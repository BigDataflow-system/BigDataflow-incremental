package alias_stmt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Scanner;

public class AssignAStmt extends AStmt
{
	private int src;
	private int dst;
	
	public AssignAStmt()
	{
		this.stmt_value = TYPE.Assign;
		this.src = -1;
		this.dst = -1;
	}
	
	public AssignAStmt(Scanner sc)
	{
		this.stmt_value = TYPE.Assign;
		this.dst = sc.nextInt();
		this.src = sc.nextInt();
	}
	
	public int getSrc() 
	{
		return src;
	}

	public int getDst() 
	{
		return dst;
	}
	
	@Override
	public  void toString_sub(StringBuilder str)
	{
		str.append("assign, ").append(getDst()).append("<-").append(getSrc());
	}

	@Override
	public AStmt decopy() {
		AssignAStmt stmt = new AssignAStmt();
		stmt.src = this.src;
		stmt.dst = this.dst;
		return stmt;
	}

	@Override
	public int getSize(){
    return 2;
  }

	@Override
	public String to_string(){
		StringBuilder strBuilder = new StringBuilder();
		strBuilder.append(src).append("\t");
		strBuilder.append(dst).append("\t");
		return strBuilder.toString();
	}

	@Override
  public void readString(String[] token, int idx) {
		src = Integer.parseInt(token[idx]);
		dst = Integer.parseInt(token[idx + 1]);
  }

	@Override
	public void write(DataOutput dataOutput) throws IOException {
		dataOutput.writeInt(src);
		dataOutput.writeInt(dst);
	}

	@Override
	public void readFields(DataInput dataInput) throws IOException {
		src = dataInput.readInt();
		dst = dataInput.readInt();
	}
}