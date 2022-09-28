package com.fartburger.fartcheat.modules.hacks.HiveMind;

import com.fartburger.fartcheat.util.Utils;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class HiveConnection extends Thread {

    public final Socket socket;
    public String message;

    public HiveConnection(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        //ChatUtils.info("Swarm", "New worker connected on %s.", getIp(socket.getInetAddress().getHostAddress()));
        Utils.chatLog("New worker connected on "+getIp(socket.getInetAddress().getHostAddress()));

        try {
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());

            while (!isInterrupted()) {
                if (message != null) {
                    try {
                        out.writeUTF(message);
                        out.flush();
                    } catch (Exception e) {
                        //ChatUtils.error("Swarm", "Encountered error when sending command.");
                        Utils.chatError("Ran into an error while sending command");
                        e.printStackTrace();
                    }

                    message = null;
                }
            }

            out.close();
        } catch (IOException e) {
            //ChatUtils.info("Swarm", "Error creating a connection with %s on port %s.", getIp(socket.getInetAddress().getHostAddress()), socket.getPort());
            Utils.chatError("Could not create a connection with "+getIp(socket.getInetAddress().getHostAddress())+" on port 420.");
            e.printStackTrace();
        }
    }

    public void disconnect() {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //ChatUtils.info("Swarm", "Worker disconnected on ip: %s.", socket.getInetAddress().getHostAddress());
        Utils.chatLog("Disconnected from ip "+socket.getInetAddress().getHostAddress());

        interrupt();
    }

    public String getConnection() {
        return getIp(socket.getInetAddress().getHostAddress()) + ":" + socket.getPort();
    }

    private String getIp(String ip) {
        return ip.equals("127.0.0.1") ? "localhost" : ip;
    }
}
