package com.example.myway.Model;

import android.util.Log;

import java.util.List;

public class RoomGraph {
    private List<RoomRepresent> roomList;
    private List<Edge> edges;

    /**
     * Represents the vertex in the graph
     */
    private class RoomRepresent {
        private double doorX;
        private double doorY;
        private String room;

        public RoomRepresent(double dx,double dy,String r){
            doorX=dx;
            doorY=dy;
            room=r;
        }

        public double getDoorX() {
            return doorX;
        }

        public double getDoorY() {
            return doorY;
        }

        public String getRoom() {
            return room;
        }

        public void setDoorX(int doorX) {
            this.doorX = doorX;
        }

        public void setDoorY(int doorY) {
            this.doorY = doorY;
        }

        public void setRoom(String room) {
            this.room = room;
        }
    }

    /**
     * Represents edges (e1,e2)
     */
    private class Edge{
        private String e1;
        private String e2;
        private String instruction;

        public Edge(String e1,String e2,String ins){
            this.e1=e1;
            this.e2=e2;
            this.instruction=ins;
        }

        public String getInstruction() {
            return instruction;
        }

        public String getE1() {
            return e1;
        }

        public String getE2() {
            return e2;
        }

        public void setInstruction(String instruction) {
            this.instruction = instruction;
        }

        public void setE1(String e1) {
            this.e1 = e1;
        }

        public void setE2(String e2) {
            this.e2 = e2;
        }
    }

    public RoomGraph(){
        roomList.add(new RoomRepresent(34.65844,31.80684,"168"));
        roomList.add(new RoomRepresent(34.65832,31.80689,"167"));
        roomList.add(new RoomRepresent(34.65828,31.807,"166C"));
        roomList.add(new RoomRepresent(34.65829,31.80706,"166A"));
        roomList.add(new RoomRepresent(34.6582,31.80704,"166B"));
        roomList.add(new RoomRepresent(34.65814,31.80714,"165"));
        roomList.add(new RoomRepresent(34.65805,31.80712,"164"));
        roomList.add(new RoomRepresent(34.65797,31.80718,"163"));
        roomList.add(new RoomRepresent(34.65791,31.8072,"162"));
        roomList.add(new RoomRepresent(34.65785,31.80721,"161"));
        roomList.add(new RoomRepresent(34.65783,31.80729,"160"));

        edges.add(new Edge("168","167",""));
        edges.add(new Edge("167","168",""));

        edges.add(new Edge("167","J1",""));
        edges.add(new Edge("J1","167",""));

        edges.add(new Edge("166C","J1",""));
        edges.add(new Edge("J1","166C",""));

        edges.add(new Edge("J1","J2",""));
        edges.add(new Edge("J2","J1",""));

        edges.add(new Edge("J2","166A",""));
        edges.add(new Edge("166A","J2",""));

        edges.add(new Edge("J2","160",""));
        edges.add(new Edge("160","J2",""));

        edges.add(new Edge("166A","166B",""));
        edges.add(new Edge("166B","166A",""));

        edges.add(new Edge("166A","166C",""));
        edges.add(new Edge("166C","166A",""));

        edges.add(new Edge("166A","165",""));
        edges.add(new Edge("165","166A",""));

        edges.add(new Edge("166B","165",""));
        edges.add(new Edge("165","166B",""));

        edges.add(new Edge("165","164",""));
        edges.add(new Edge("164","165",""));

        edges.add(new Edge("166B","164",""));
        edges.add(new Edge("164","166B",""));

        edges.add(new Edge("165","163",""));
        edges.add(new Edge("163","165",""));

        edges.add(new Edge("165","162",""));
        edges.add(new Edge("162","165",""));

        edges.add(new Edge("165","161",""));
        edges.add(new Edge("161","165",""));

        edges.add(new Edge("164","160",""));
        edges.add(new Edge("160","164",""));

        edges.add(new Edge("163","160",""));
        edges.add(new Edge("160","163",""));

        edges.add(new Edge("163","162",""));
        edges.add(new Edge("162","163",""));

        edges.add(new Edge("163","164",""));
        edges.add(new Edge("164","163",""));

        edges.add(new Edge("163","165",""));
        edges.add(new Edge("165","163",""));

        edges.add(new Edge("162","160",""));
        edges.add(new Edge("160","162",""));

        edges.add(new Edge("162","161",""));
        edges.add(new Edge("161","162",""));

        edges.add(new Edge("161","160",""));
        edges.add(new Edge("160","161",""));


    }
}