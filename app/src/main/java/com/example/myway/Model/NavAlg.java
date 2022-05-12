package com.example.myway.Model;

import android.util.Log;

import java.util.*;

public class NavAlg  {
    public static NavAlg instance = new NavAlg();

    public static RoomGraph g;
    private HashMap<StringKey,List<String >> paths = new HashMap<>();
    private ArrayList<String> arrayListOfInstruction;
    private ArrayList<String> arrayListOfRooms;

    private NavAlg(){
        g=new RoomGraph();

    }

    List<String> buildPath(String s, String d){
        HashMap<String,Boolean> isViseted = new HashMap<>();
        List<String> path=new LinkedList<>();
        for(RoomGraph.RoomRepresent r :g.getRoomList()){
            isViseted.put(r.getRoom(),false);
        }
        path.add(s);
        isViseted.put(s,true);
        Stack<String> neigh=new Stack<>();
        neigh.push(s);
        while (!neigh.empty()) {
            String room = neigh.pop();
            for (RoomGraph.Edge e : g.getEdges()) {
                if (e.getE1().matches(room)) {
                    if(!isViseted.get(e.getE2()))
                        neigh.push(e.getE2());
                    if (e.getE2().matches(d)) {
                        path.add(d);
                        return path;
                    }
                }
            }
        }

        return path;
    }

    public String Dijkstra(RoomGraph.RoomRepresent start, RoomGraph.RoomRepresent destination){
        arrayListOfRooms=new ArrayList<>();
        arrayListOfInstruction=new ArrayList<>();
        g.setAlldistance();
        String text = "Djikstra :\n";
        // d[start]=0 (other vertex's d_value is infinity by default), S={0} , Q = vertex
        LinkedList<RoomGraph.RoomRepresent> set = new LinkedList<RoomGraph.RoomRepresent>();
        PriorityQueue<RoomGraph.RoomRepresent> queue = new PriorityQueue<RoomGraph.RoomRepresent>();
        queue.add(start);
        start.d_value=0;

        //cycle until queue is empty or destination has been inserted into s
        while(!queue.isEmpty()){

            RoomGraph.RoomRepresent extracted = queue.poll();
            extracted.discovered=true;//kur eshte ne set dhe tashme d_value eshte percaktuar
            set.add(extracted);
            if(extracted.getRoom().matches(destination.getRoom())){
                break;
            }
            List<RoomGraph.Edge> edges = new LinkedList<>();
            for(RoomGraph.Edge e :g.getEdges()){
                if(e.getE1().matches(extracted.getRoom())){
                    edges.add(e);
                }
            }

            //for each vertex into dhe adj list of extracted -> relax.
            for(int i=0;i<edges.size();i++){
                //edge examined
                RoomGraph.Edge edge = edges.get(i);
                //get neighbor vertex and relax
                RoomGraph.RoomRepresent neighbor = g.getRoomByName(edge.getE2());
                if(neighbor.discovered==false){
                    //Relaxation
                    if(neighbor.d_value>extracted.d_value+edge.weight){
                        neighbor.d_value=extracted.d_value+edge.weight;
                        neighbor.parent=extracted;
                        //insert neighbors in queue so we can choose dhe min one
                        queue.remove(neighbor);
                        queue.add(neighbor);
                    }
                }//if discovered

            }
        }
        destination = g.getRoomByName(destination.getRoom());
        String text2="";
        String pathString="";
        if(destination.parent==null)
            text="This path does not exist";
        else{
            text+=" Vertex ne set: "+set.size();
            //Dijkstra process finished, now we will take our path and print it
            Stack<RoomGraph.RoomRepresent> stack = new Stack<RoomGraph.RoomRepresent>();
            RoomGraph.RoomRepresent current = destination;

            while(current!=null){
                stack.push(current);
                text2+=current.getRoom()+" ";
                if(current.parent!=null){
                    RoomGraph.Edge currentEdge=g.findEdgeByTwoRooms(current,current.parent);
                    arrayListOfInstructionAdd(pathString);
                    pathString+=currentEdge.getInstruction()+"\n";
                }
                arrayListOfRooms.add(current.getRoom());
                current = current.parent;

            }
            double path_length = destination.d_value;

            text+=" Nr.Hops:"+(stack.size()-1)+" Path length: "+String.format( "%.2f", path_length )+ "ns";
        }//fund else per ekzistencen e path
        Log.d("TAG",""+pathString);
        return text+"\n"+text2;
    }

    public void arrayListOfInstructionAdd(String instruction){
        if(instruction!=""){
            arrayListOfInstruction.add(instruction);
        }
    }

    public ArrayList<String> arrayListOfInstruction(){
        return arrayListOfInstruction;
    }
    public ArrayList<String> arrayListOfRooms(){
        return arrayListOfRooms;
    }




    private List<String> getRouteTo(String s,String e){
        StringKey k = new StringKey(s,e);
       return paths.get(k);
    }



    private class StringKey {
        private String str1;
        private String str2;

        public StringKey(String s1,String s2){
            str1=s1;
            str2=s2;
        }

        @Override
        public boolean equals(Object obj) {
            if(obj != null && obj instanceof StringKey) {
                StringKey s = (StringKey)obj;
                return str1.equals(s.str1) && str2.equals(s.str2);
            }
            return false;
        }

        @Override
        public int hashCode() {
            return (str1 + str2).hashCode();
        }
    }

}
