package alias_stmt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class CallfptrAStmt extends AStmt {
    private int dst = -1;
    private int auxiliary = -1;
    private int length = 0;
    private int[] args;
    private int ret = -1;

    public CallfptrAStmt()
    {
        this.stmt_value = TYPE.Callfptr;
        this.dst = -1;
        this.auxiliary = -1;

        this.length = 0;
        this.args = null;
        this.ret = -1;
    }

    public CallfptrAStmt(Scanner sc)
    {
        this.stmt_value = TYPE.Callfptr;
        this.dst = sc.nextInt();

        String aux_tmp;
        Set<String> setString = new HashSet<>();

        if(sc.hasNext()){
            aux_tmp = sc.next();
            if (aux_tmp.charAt(0) == 'a') {
                setString.add(aux_tmp.substring(2));
                this.auxiliary = this.dst;
            }
            else if (aux_tmp.charAt(0) == 'r') {
                this.ret = Integer.parseInt(aux_tmp.substring(2));
                this.auxiliary = this.dst;
            }
            else if (aux_tmp.charAt(0) >= '1' && aux_tmp.charAt(0) <= '9'){
                this.auxiliary = Integer.parseInt(aux_tmp);
            }
            else{
                System.out.println("callfptr -> wrong arg type!!!");
                System.exit(1);
            }
        }
        //this.auxiliary = sc.nextInt();

        String arg;
        while (sc.hasNext()) {
            arg = sc.next();
            if (arg.charAt(0) == 'a') {
                setString.add(arg.substring(2));
            }
            else if (arg.charAt(0) == 'r') {
                this.ret = Integer.parseInt(arg.substring(2));
            }
            else {
                System.out.println("callfptr -> wrong arg type!!!");
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

    public int getDst()
    {
        return dst;
    }

    public int getAuxiliary()
    {
        return auxiliary;
    }

    @Override
    public void toString_sub(StringBuilder str)
    {
        str.append("callfptr, ").append(getDst()).append(", ").append(getAuxiliary()).append(", ").append(getRet()).append("<-");
        for (int i = 0; i < length; i++) {
            str.append(args[i]).append(',');
        }
    }

    @Override
    public AStmt decopy() {
        CallfptrAStmt stmt = new CallfptrAStmt();
        stmt.dst = this.dst;
        stmt.auxiliary = this.auxiliary;
        stmt.length = this.length;
        stmt.ret = this.ret;
        stmt.args = new int[length];
        System.arraycopy(this.args, 0, stmt.args, 0, length);
        return stmt;
    }

    @Override
	public int getSize(){
    return 4+length;
  }

  @Override
  public String toString() {
      StringBuilder strBuilder = new StringBuilder();
      // strBuilder.append(dst).append("\t");
      // strBuilder.append(auxiliary).append("\t");
      // strBuilder.append(length).append("\t");
      // strBuilder.append(ret).append("\t");
      // for (int i = 0; i < length; i++) {
      // 	strBuilder.append(args[i]).append("\t");
      // }

      /// this.dst = sc.nextInt();
      strBuilder.append(dst).append("\t");
      strBuilder.append(auxiliary).append("\t");
      if(ret != -1) {
          strBuilder.append("r_" + ret).append("\t");
      }
      for (int i = 0; i < length; i++) {
          strBuilder.append("a_" + args[i]).append("\t");
      }
      return strBuilder.toString();
  }
  
  @Override
  public String to_string(){
      StringBuilder strBuilder = new StringBuilder();
      strBuilder.append(dst).append("\t");
      strBuilder.append(auxiliary).append("\t");
      strBuilder.append(length).append("\t");
      strBuilder.append(ret).append("\t");
      for (int i = 0; i < length; i++) {
          strBuilder.append(args[i]).append("\t");
      }
      return strBuilder.toString();	
  }


	@Override
    public void readString(String[] token, int idx) {
        dst = Integer.parseInt(token[idx]);
        auxiliary = Integer.parseInt(token[idx + 1]);
		length = Integer.parseInt(token[idx + 2]);
		ret = Integer.parseInt(token[idx + 3]);
		args = new int[length];
		idx = idx+4;
		for (int i = 0; i < length; i++) {
			args[i] = Integer.parseInt(token[idx + i]);
		}
    }

    @Override
    public void write(DataOutput dataOutput) throws IOException {
        dataOutput.writeInt(dst);
        dataOutput.writeInt(auxiliary);
        dataOutput.writeInt(length);
        dataOutput.writeInt(ret);
        for (int i = 0; i < length; i++) {
            dataOutput.writeInt(args[i]);
        }
    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {
        dst = dataInput.readInt();
        auxiliary = dataInput.readInt();
        length = dataInput.readInt();
        ret = dataInput.readInt();
        args = new int[length];
        for (int i = 0; i < length; i++) {
            args[i] = dataInput.readInt();
        }
    }
}
