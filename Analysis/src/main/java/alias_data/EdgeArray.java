package alias_data;

import org.apache.hadoop.io.Writable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class EdgeArray implements Writable
{
    private int[] edges;
    private byte[] labels;
    private int size;
    private int capacity;

    public EdgeArray() {
        edges = new int[0];
        labels = new byte[0];
        size = capacity = 0;
    }

    public EdgeArray(String[] tokens, int idx) {
        // edges = new int[0];
        // labels = new byte[0];
        // size = capacity = 0;

        // size = in.readInt();
        // capacity = in.readInt();
        // int[] tmpEdges = new int[size];
        // byte[] tmpLabels = new byte[size];
        // for(int i = 0; i < size; i++)
        // {
        //     tmpEdges[i] = in.readInt();
        // }
        // for(int i = 0; i < size; i++)
        // {
        //     tmpLabels[i] = in.readByte();
        // }
        // edges = tmpEdges;
        // labels = tmpLabels;

        size = Integer.parseInt(tokens[idx]);
        capacity = Integer.parseInt(tokens[idx+1]);
        int[] tmpEdges = new int[size];
        byte[] tmpLabels = new byte[size];
        idx = idx + 2;
        for(int i = 0; i < size; i++)
        {
            tmpEdges[i] = Integer.parseInt(tokens[idx+i]);
        }
        idx = idx + size;
        for(int i = 0; i < size; i++)
        {
            tmpLabels[i] = Byte.parseByte(tokens[idx+i]);
        }
        edges = tmpEdges;
        labels = tmpLabels;
    }

    public int[] getEdges()
    {
        return this.edges;
    }

    public byte[] getLabels()
    {
        return this.labels;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public void readFields(DataInput in) throws IOException {
        size = in.readInt();
        capacity = in.readInt();
        int[] tmpEdges = new int[size];
        byte[] tmpLabels = new byte[size];
        for(int i = 0; i < size; i++)
        {
            tmpEdges[i] = in.readInt();
        }
        for(int i = 0; i < size; i++)
        {
            tmpLabels[i] = in.readByte();
        }
        edges = tmpEdges;
        labels = tmpLabels;
    }

    @Override
    public void write(DataOutput out) throws IOException {
        out.writeInt(size);
        out.writeInt(capacity);

        for (int tmp : edges) {
            out.writeInt(tmp);
        }
        for (byte tmp : labels) {
            out.writeByte(tmp);
        }
    }

    @Override
    public boolean equals(Object another) {
        if(another == null)
			return false;
        EdgeArray obj = (EdgeArray)another;
        if(this == obj)
            return true;
        if(this.getSize() != obj.getSize())
            return false;
        for(int i = 0; i < this.getSize(); i++)
        {
            if(this.edges[i] != obj.edges[i] || this.labels[i] != obj.labels[i])
                return false;
        }
        return true;
    }

    public int getSize() {
        return size;
    }

    public void addOneEdge(int edge, byte label) {
        if(size == 0) {
            capacity = 8;
            edges = new int[capacity];
            labels = new byte[capacity];
            //initial
            for(int i = 0;i < capacity; ++i) {
                edges[i] = -1;
                labels[i] = 127;
            }
        }
        else {
            if(size >= capacity) {
                capacity *= 2;
                edges = AliasTool.myrealloc(edges,size,capacity);
                labels = AliasTool.myrealloc(labels,size,capacity);
                for(int i = size;i < capacity;++i) {
                    edges[i] = -1;
                    labels[i] = 127;
                }
            }
        }
        // add edge
        edges[size] = edge;
        labels[size] = label;
        ++size;
    }

    public void set(int size, int[] edges, byte[] labels) {
        if(size == 0)
            return;
        this.size = size;
        this.edges = new int[size];
        this.labels = new byte[size];
        System.arraycopy(edges, 0, this.edges, 0, size);
        System.arraycopy(labels, 0, this.labels, 0, size);
    }

    public void merge() {
        // sort edges
        AliasTool.quickSort(edges, labels, 0, size - 1);
        // remove duplicate edges
        int[] _edges = new int[size];
        byte[] _labels = new byte[size];
        int _numEdges = 0;
        _numEdges = AliasTool.removeDuple(_numEdges, _edges, _labels, size, edges, labels);
        size = _numEdges;
        System.arraycopy(_edges, 0, edges, 0, _numEdges);
        System.arraycopy(_labels, 0, labels, 0, _numEdges);
    }

    public int print() {
        System.out.print("size=" + size + ", ");
        for (int i = 0; i < size; i++) {
            System.out.print("(" + edges[i] + ", " + labels[i] + ") ");
        }
        return size;
    }

    public String toString(){
        StringBuilder strBuilder = new StringBuilder();

        strBuilder.append(size).append("\t");
        strBuilder.append(capacity).append("\t");
//        for (int tmp : edges) {
//            strBuilder.append(tmp).append("\t");
//        }
//        for (byte tmp : labels) {
//            strBuilder.append(tmp).append("\t");
//        }
        for(int i = 0; i < size; i++){
            strBuilder.append(edges[i]).append("\t");
        }
        for(int i = 0; i < size; i++){
            strBuilder.append(labels[i]).append("\t");
        }

        return strBuilder.toString();
    }

}
