package ro.pub.cs.systems.eim.practicaltest02;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import ro.pub.cs.systems.eim.practicaltest02.general.Constants;
import ro.pub.cs.systems.eim.practicaltest02.network.ClientThread;
import ro.pub.cs.systems.eim.practicaltest02.network.ServerThread;

public class PracticalTest02MainActivity extends AppCompatActivity {

    // Server widgets
    private EditText serverPortEditText = null;
    private Button connectButton = null;

    // Client widgets
    private EditText clientAddressEditText = null;
    private EditText clientPortEditText = null;
    private EditText clientHourEditText = null;
    private EditText clientMinuteEditText = null;

    private Button setTimeButton = null;
    private Button resetButton = null;
    private Button pollButton = null;

    private TextView resultTextView = null;

    private ServerThread serverThread = null;
    private ClientThread clientThread = null;

    private ConnectButtonClickListener connectButtonClickListener = new ConnectButtonClickListener();
    private class ConnectButtonClickListener implements Button.OnClickListener {

        @Override
        public void onClick(View view) {
            String serverPort = serverPortEditText.getText().toString();
            if (serverPort == null || serverPort.isEmpty()) {
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] Server port should be filled!", Toast.LENGTH_SHORT).show();
                return;
            }
            serverThread = new ServerThread(Integer.parseInt(serverPort));
            if (serverThread.getServerSocket() == null) {
                Log.e(Constants.TAG, "[MAIN ACTIVITY] Could not create server thread!");
                return;
            }
            serverThread.start();
            Log.i(Constants.TAG, "[MAIN ACTIVITY] Server started");
        }
    }

    private GeneralButtonListener generalButtonListener = new GeneralButtonListener();
    private class GeneralButtonListener implements Button.OnClickListener {

        @Override
        public void onClick(View view) {

            String clientAddress = clientAddressEditText.getText().toString();
            String clientPort = clientPortEditText.getText().toString();
            if (clientAddress == null || clientAddress.isEmpty()
                    || clientPort == null || clientPort.isEmpty()) {
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] Client connection parameters should be filled!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (serverThread == null || !serverThread.isAlive()) {
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] There is no server to connect to!", Toast.LENGTH_SHORT).show();
                return;
            }


            String hour = clientHourEditText.getText().toString();
            String minute = clientMinuteEditText.getText().toString();
            if (hour == null || hour.isEmpty() || minute == null || minute.isEmpty()) {
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] Parameters from client (city / information type) should be filled", Toast.LENGTH_SHORT).show();
                return;
            }

            resultTextView.setText(Constants.EMPTY_STRING);

            switch(view.getId()) {
                case R.id.set_button:
                    Log.i(Constants.TAG, "[MAIN ACTIVITY] set button invoked");
                    clientThread = new ClientThread(
                            clientAddress, Integer.parseInt(clientPort), hour, minute, resultTextView, "set"
                    );
                    clientThread.start();

                    break;
                case R.id.reset_button:
                    Log.i(Constants.TAG, "[MAIN ACTIVITY] reset button invoked");
                    clientThread = new ClientThread(
                            clientAddress, Integer.parseInt(clientPort), hour, minute, resultTextView, "reset"
                    );
                    clientThread.start();
                    break;
                case R.id.poll_button:
                    Log.i(Constants.TAG, "[MAIN ACTIVITY] poll button invoked");
                    clientThread = new ClientThread(
                            clientAddress, Integer.parseInt(clientPort), hour, minute, resultTextView, "poll"
                    );
                    clientThread.start();
                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(Constants.TAG, "[MAIN ACTIVITY] onCreate() callback method has been invoked");
        setContentView(R.layout.activity_practical_test02_main);

        serverPortEditText = (EditText)findViewById(R.id.server_port_edit_text);
        connectButton = (Button)findViewById(R.id.connect_button);
        connectButton.setOnClickListener(connectButtonClickListener);

        clientAddressEditText = (EditText)findViewById(R.id.client_address_edit_text);
        clientPortEditText = (EditText)findViewById(R.id.client_port_edit_text);
        clientHourEditText = (EditText)findViewById(R.id.client_hour);
        clientMinuteEditText = (EditText)findViewById(R.id.client_minute);

        setTimeButton = (Button)findViewById(R.id.set_button);
        setTimeButton.setOnClickListener(generalButtonListener);
        resetButton = (Button)findViewById(R.id.reset_button);
        resetButton.setOnClickListener(generalButtonListener);
        pollButton = (Button)findViewById(R.id.poll_button);
        pollButton.setOnClickListener(generalButtonListener);

        resultTextView = (TextView)findViewById(R.id.response_text_view);
    }

    @Override
    protected void onDestroy() {
        Log.i(Constants.TAG, "[MAIN ACTIVITY] onDestroy() callback method has been invoked");
//        if (serverThread != null) {
//            serverThread.stopThread();
//        }
        super.onDestroy();
    }
}
