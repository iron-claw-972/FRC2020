package frc.robot.controllers;

import org.zeromq.ZMQ;
import org.zeromq.ZContext;

import frc.robot.util.RobotPacket;
import frc.robot.util.UnityPacket;

import com.google.gson.Gson;

public class ZMQServer extends Thread
{
    private Gson g;
    private ZMQ.Socket socket;
    private ZContext context;
    public RobotPacket robotPacket;
    public UnityPacket unityPacket;

    public ZMQServer()
    {
        g = new Gson();
        context = new ZContext();
        robotPacket = new RobotPacket();
        unityPacket = new UnityPacket();
        
        socket = context.createSocket(ZMQ.REP);
        socket.bind("tcp://*:5555");

        System.out.println("Sucessful Server Initialization");
    }

    public void run()
    {
        while(!Thread.currentThread().isInterrupted())
        {
            //Recieving data and putting it into object
            String replyString = new String(socket.recv(0), ZMQ.CHARSET);

            // System.out.println("Received: [" + replyString + "]");

            unityPacket = g.fromJson(replyString, UnityPacket.class);

            // System.out.println("Heartbeat = " + unityPacket.heartbeat);

            //Responding to unity server with data
            robotPacket.heartbeat = System.currentTimeMillis();

            String response = g.toJson(robotPacket);
            
            socket.send(response.getBytes(ZMQ.CHARSET), 0);
        }
    }
}