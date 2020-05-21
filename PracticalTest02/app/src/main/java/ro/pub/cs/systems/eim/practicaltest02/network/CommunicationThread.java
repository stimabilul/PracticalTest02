package ro.pub.cs.systems.eim.practicaltest02.network;

import android.util.Log;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;

import ro.pub.cs.systems.eim.practicaltest02.general.Constants;
import ro.pub.cs.systems.eim.practicaltest02.general.Utilities;
import ro.pub.cs.systems.eim.practicaltest02.model.AlarmInformation;

public class CommunicationThread extends Thread {

    private ServerThread serverThread;
    private Socket socket;

    public CommunicationThread(ServerThread serverThread, Socket socket) {
        this.serverThread = serverThread;
        this.socket = socket;
    }

    @Override
    public void run() {
        if (socket == null) {
            Log.e(Constants.TAG, "[COMMUNICATION THREAD] Socket is null!");
            return;
        }

        try {
            BufferedReader bufferedReader = Utilities.getReader(socket);
            PrintWriter printWriter = Utilities.getWriter(socket);
            if (bufferedReader == null || printWriter == null) {
                Log.e(Constants.TAG, "[COMMUNICATION THREAD] Buffered Reader / Print Writer are null!");
                return;
            }
            Log.i(Constants.TAG, "[COMMUNICATION THREAD] Waiting for parameters from client (hour / minute!");
            String whatToDo = bufferedReader.readLine();

            if (whatToDo.equals("set")) {
                String hour = bufferedReader.readLine();
                String minute = bufferedReader.readLine();
                if (hour == null || hour.isEmpty() || minute == null || minute.isEmpty()) {
                    Log.e(Constants.TAG, "[COMMUNICATION THREAD] Error receiving parameters from client (hour / minute!");
                    return;
                }
                serverThread.setData("ON", new AlarmInformation(hour, minute));
                Log.i(Constants.TAG, "[COMMUNICATION THREAD] Alarm set for " + hour + ":" + minute);
                printWriter.println("Alarm set for " + hour + ":" + minute);

            } else if (whatToDo.equals("reset")) {
                serverThread.setData("OFF", new AlarmInformation(null, null));
                Log.i(Constants.TAG, "[COMMUNICATION THREAD] Alarm canceled");
                printWriter.println("Alarm canceled.");
            }

        } catch (IOException ioException) {
            Log.e(Constants.TAG, "[COMMUNICATION THREAD] An exception has occurred: " + ioException.getMessage());
            if (Constants.DEBUG) {
                ioException.printStackTrace();
            }
//        } catch (JSONException jsonException) {
//            Log.e(Constants.TAG, "[COMMUNICATION THREAD] An exception has occurred: " + jsonException.getMessage());
//            if (Constants.DEBUG) {
//                jsonException.printStackTrace();
//            }
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException ioException) {
                    Log.e(Constants.TAG, "[COMMUNICATION THREAD] An exception has occurred: " + ioException.getMessage());
                    if (Constants.DEBUG) {
                        ioException.printStackTrace();
                    }
                }
            }
        }
    }

}
