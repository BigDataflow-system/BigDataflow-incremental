package alias_stmt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class ReturnAStmt extends AStmt
{
	private int length = 0;
	private int[] args = null;
	private int ret = -1;

	public ReturnAStmt()
	{
		this.stmt_value = TYPE.Return;
		this.length = 0;
		this.args = null;
		this.ret = -1;
	}

	public ReturnAStmt(Scanner sc)
	{
		this.stmt_value = TYPE.Return;

		String arg;
		Set<String> setString = new HashSet<>();

		while (sc.hasNext()) {
			arg = sc.next();
			if (arg.charAt(0) == 'a') {
				setString.add(arg.substring(2));
			}
			else if (arg.charAt(0) == 'r') {
				this.ret = Integer.parseInt(arg.substring(2));
			}
			else {
				System.out.println("return -> wrong arg type!!!");
				System.exit(1);
			}
		}
		this.length = setString.size();
		this.args = new int[this.length];
		int i = 0;
		for (String str : setString) {
			this.args[i] = Integer.parseInt(str);
			i++;
		}
	}
  
	public int getRet() {
		return ret;
	}

	public int getLength() {
		return length;
	}

	public int[] getArgs() {
		return args;
	}

	@Override
	public void toString_sub(StringBuilder str)
	{
		str.append("return, ").append(getRet()).append("<-");
		for (int i = 0; i < length; i++) {
			str.append(args[i]).append(',');
		}
	}

	@Override
	public AStmt decopy() {
		ReturnAStmt stmt = new ReturnAStmt();
		stmt.length = this.length;
		stmt.ret = this.ret;
		stmt.args = new int[length];
		System.arraycopy(this.args, 0, stmt.args, 0, length);
		return stmt;
	}

	@Override
	public int getSize(){
    return length+2;
  }

	@Override
	public String to_string(){
		StringBuilder strBuilder = new StringBuilder();
		strBuilder.append(length).append("\t");
		strBuilder.append(ret).append("\t");
		for (int i = 0; i < length; i++) {
			strBuilder.append(args[i]).append("\t");
		}
		return strBuilder.toString();
	}

	@Override
  public void readString(String[] token, int idx) {
		length = Integer.parseInt(token[idx]);
		ret = Integer.parseInt(token[idx + 1]);
		idx = idx + 2;
		args = new int[length];
		for (int i = 0; i < length; i++) {
			args[i] = Integer.parseInt(token[idx + i]);
		}
  }

	@Override
	public void write(DataOutput dataOutput) throws IOException {
		dataOutput.writeInt(length);
		dataOutput.writeInt(ret);
		for (int i = 0; i < length; i++) {
			dataOutput.writeInt(args[i]);
		}
	}

	@Override
	public void readFields(DataInput dataInput) throws IOException {
		length = dataInput.readInt();
		ret = dataInput.readInt();
		args = new int[length];
		for (int i = 0; i < length; i++) {
			args[i] = dataInput.readInt();
		}
	}
}
