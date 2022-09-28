package com.fartburger.fartcheat.modules.hacks.HiveMind;

import baritone.api.BaritoneAPI;
import com.fartburger.fartcheat.FCRMain;
import com.fartburger.fartcheat.util.Utils;
import net.minecraft.block.Block;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

public class HiveWorker extends Thread {
    private Socket socket;
    public Block target;

    public HiveWorker(String ip, int port) {
        try {
            socket = new Socket(ip, port);
        } catch (Exception e) {
            socket = null;
            //ChatUtils.warning("Swarm", "Server not found at %s on port %s.", ip, port);
            Utils.chatError("Server not found at "+ip+"on port 420.");
            e.printStackTrace();
        }

        if (socket != null) start();
    }

    @Override
    public void run() {
        //ChatUtils.info("Swarm", "Connected to Swarm host on at %s on port %s.", getIp(socket.getInetAddress().getHostAddress()), socket.getPort());
        Utils.chatLog("Connected to Hive server successfully.");

        try {
            DataInputStream in = new DataInputStream(socket.getInputStream());


            while (!isInterrupted()) {
                String read = in.readUTF();

                if (!read.equals("")) {
                    //ChatUtils.info("Swarm", "Received command: (highlight)%s", read);
                    FCRMain.client.player.sendMessage(Text.of(Formatting.GRAY+read));
                }
            }

            in.close();
        } catch (IOException e) {
            //ChatUtils.error("Swarm", "Error in connection to host.");
            Utils.chatError("Encountered error in connection to host. Disconnecting..");
            e.printStackTrace();
            disconnect();
        }
    }

    public void disconnect() {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        BaritoneAPI.getProvider().getPrimaryBaritone().getPathingBehavior().cancelEverything();

       // ChatUtils.info("Swarm", "Disconnected from host.");
        Utils.chatLog("Disconnected from Hive.");

        interrupt();
    }

    public void tick() {

    }

    public String getConnection() {
        return getIp(socket.getInetAddress().getHostAddress()) + ":" + socket.getPort();
    }

    private String getIp(String ip) {
        return ip.equals("127.0.0.1") ? "localhost" : ip;
    }
}
