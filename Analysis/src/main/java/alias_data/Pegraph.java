 

   
    
      
      
    
    
    
     
      
    
    
    
    
    
    
    
    
      
      
      
      
        
         
        
        
        
        
        
        
            
            
            

           
          
      
      
      
        
      
        
       
       

package alias_data;

import incre_data.Fact;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Pegraph extends Fact{

  protected final Map<Integer, EdgeArray> graph;
  
  public Pegraph(){
    this.graph = new HashMap<>();
  }

  public String graphtoString(){
      StringBuilder strBuilder = new StringBuilder();
      strBuilder.append(graph.size()).append("\t");
      for (Map.Entry<Integer, EdgeArray> entry : graph.entrySet()) {
          strBuilder.append(entry.getKey()).append("\t");
          strBuilder.append(entry.getValue().toString());
      }
      return strBuilder.toString();
  }

  public Pegraph(String[] tokens, int idx){
    this.graph = new HashMap<>();

    int k = idx;
      k++;
      int size = Integer.parseInt(tokens[k]);
//      CommonWrite.method2("PEG size:\t"+ String.valueOf(size));
      k++;
      while(size > 0){
        Integer key = Integer.parseInt(tokens[k]);
        k++;
        EdgeArray edgeArray = new EdgeArray(tokens, k);
        graph.put(key, edgeArray);
        k = k + 2 + edgeArray.getSize() + edgeArray.getSize();
//        CommonWrite.method2("key:\t"+ String.valueOf(key) + "\t"+ edgeArray.getSize());
        size--;
      }
  }

  public Map<Integer, EdgeArray> getGraph() {
    return graph;
  }

  public int getNumEdges(int src) {
      if (!graph.containsKey(src)) {
          graph.put(src, new EdgeArray());
          return 0;
      }
      return graph.get(src).getSize();
  }

  public int getNumEdges() {
      int size = 0;
      for(EdgeArray edgeArray : graph.values()) {
          size += edgeArray.getSize();
      }
      return size;
  }

  public int getSize() {
    return graph.size()*3 + getNumEdges()*2;
  }


  public int[] getEdges(int src) {
      return graph.get(src).getEdges();
  }

  public byte[] getLabels(int src) {
      return graph.get(src).getLabels();
  }

  public void setEdgeArray(int index, int numEdges, int[] edges, byte[] labels) {
      EdgeArray tmp = new EdgeArray();
      tmp.set(numEdges, edges, labels);
      graph.put(index, tmp);
  }

  public void setEdgeArray(int index, EdgeArray array) {
      this.graph.put(index, array);
  }

  public void setDeep(Pegraph pegraph) {
    for (Map.Entry<Integer, EdgeArray> entry : pegraph.graph.entrySet()) {
        Integer oldId = entry.getKey();
        EdgeArray oldEdgeArray = entry.getValue();
        EdgeArray edgeArray = new EdgeArray();
        edgeArray.set(oldEdgeArray.getSize(), oldEdgeArray.getEdges(), oldEdgeArray.getLabels());
        this.graph.put(oldId, edgeArray);
    }
  }

  @Override
  public void merge(Fact fact){
    Pegraph mergeGraph = (Pegraph)fact;

    for (Map.Entry<Integer, EdgeArray> entry : mergeGraph.getGraph().entrySet()) {
      Integer mergeId = entry.getKey();
      EdgeArray mergeEdgeArray = entry.getValue();
      if (!this.graph.containsKey(mergeId)) {
          this.graph.put(mergeId, mergeEdgeArray);
      }
      else {
          // merge the edgeArray with the same src in graph_1 and graph_2
          int n1 = mergeEdgeArray.getSize();
          int n2 = this.getNumEdges(mergeId);
          int[] edges = new int[n1 + n2];
          byte[] labels = new byte[n1 + n2];
          int len = AliasTool.unionTwoArray(edges, labels, n1,
                  mergeEdgeArray.getEdges(), mergeEdgeArray.getLabels(), n2,
                  this.getEdges(mergeId),
                  this.getLabels(mergeId));

          this.graph.get(mergeId).set(len, edges, labels);
      }
    }
  }

  @Override
  public Fact getNew(){
    Pegraph tmp = new Pegraph();
    tmp.setDeep(this);
    return tmp;
  }

  @Override
  public boolean consistent(Fact fact){

    if(fact == null)  return false;

    Pegraph another = (Pegraph)fact;
    if(this == another){
      return true;
    }

    if(this.graph.size() != another.graph.size()) {
        return false;
    }

    for (Map.Entry<Integer, EdgeArray> entry : graph.entrySet()) {
        Integer id = entry.getKey();
        if (!another.graph.containsKey(id)) {
            return false;
        } else if (!another.graph.get(id).equals(this.graph.get(id))) {
            return false;
        }
    }
    return true;
  }


  // @ new collection function added by szw
  public String getAliasNumEdges(Grammar grammar){
    int size_mem_graphitems = 0;
    int size_val_graphitems = 0;
    
    for(Map.Entry<Integer, EdgeArray> entry : graph.entrySet()){
      byte[] prt_labels = entry.getValue().getLabels();
      for(int i = 0; i < entry.getValue().getSize(); i++){
        if(grammar.isMemoryAlias(prt_labels[i]))
          size_mem_graphitems++;
        if(grammar.isValueAlias(prt_labels[i]))
          size_val_graphitems++;
      }
    }

    StringBuilder mv_num = new StringBuilder();
    mv_num.append(size_mem_graphitems).append("\t").append(size_val_graphitems);
    return mv_num.toString();
  }

  @Override
  public void write(DataOutput dataOutput) throws IOException {
    int size = graph.size();
    dataOutput.writeInt(size);
    for (Map.Entry<Integer, EdgeArray> entry : graph.entrySet()) {
      dataOutput.writeInt(entry.getKey());
      entry.getValue().write(dataOutput);
    }
  }

  @Override
  public void readFields(DataInput dataInput) throws IOException {
    this.graph.clear();
    int size = dataInput.readInt();
    for (int i = 0; i < size; i++) {
      Integer key = dataInput.readInt();
      EdgeArray edgeArray = new EdgeArray();
      edgeArray.readFields(dataInput);
      graph.put(key, edgeArray);
    }
  }

  public String toString(){
    return String.valueOf(getNumEdges());
  }
}