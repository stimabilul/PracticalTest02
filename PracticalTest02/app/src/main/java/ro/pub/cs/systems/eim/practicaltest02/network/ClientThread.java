package ro.pub.cs.systems.eim.practicaltest02.network;

import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import ro.pub.cs.systems.eim.practicaltest02.general.Constants;
import ro.pub.cs.systems.eim.practicaltest02.general.Utilities;

public class ClientThread extends Thread {

    private String address;
    private int port;
    private String hour;
    private String minute;
    private TextView view_results;
    private String whatToDo;
    private Socket socket;

    public ClientThread(String address, int port, String hour, String minute, TextView view_results, String whatToDo) {
        this.address = address;
        this.port = port;
        this.hour = hour;
        this.minute = minute;
        this.view_results = view_results;
        this.whatToDo = whatToDo;
    }

    @Override
    public void run() {
        try {
            socket = new Socket(address, port);

            BufferedReader bufferedReader = Utilities.getReader(socket);
            PrintWriter printWriter = Utilities.getWriter(socket);

            printWriter.println(whatToDo);
            printWriter.println(hour);
            printWriter.println(minute);
            printWriter.flush();
            String alarmInformation;
            while ((alarmInformation = bufferedReader.readLine()) != null) {
                final String finalizedAlarmInformation = alarmInformation;
                view_results.post(new Runnable() {
                    @Override
                    public void run() {
                        view_results.setText(finalizedAlarmInformation);
                    }
                });
            }
        } catch (IOException ioException) {
            Log.e(Constants.TAG, "[CLIENT THREAD] An exception has occurred: " + ioException.getMessage());
            if (Constants.DEBUG) {
                ioException.printStackTrace();
            }
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException ioException) {
                    Log.e(Constants.TAG, "[CLIENT THREAD] An exception has occurred: " + ioException.getMessage());
                    if (Constants.DEBUG) {
                        ioException.printStackTrace();
                    }
                }
            }
        }
    }
}
