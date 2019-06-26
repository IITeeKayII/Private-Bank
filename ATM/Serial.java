/*
    Coen Schutte
    0976553
    TI1A
    21/06/2019
*/

import com.fazecast.jSerialComm.*;
import java.io.IOException;

public class Serial {
	private String key = "";
	private String RFID = "";
	private String receivedData;

	static SerialPort pinModule = SerialPort.getCommPort("COM9");
	static SerialPort dispenser = SerialPort.getCommPort("COM4");



	public void openPort() {
		pinModule.openPort();
		pinModule.setBaudRate(9600);
		if (pinModule.openPort()) {
			System.out.println("Port opened successfully.");
		} else {
			System.out.println("port opening failed.");
		}
//		dispenser.openPort();
//		dispenser.setBaudRate(9600);
//		if (dispenser.openPort()) {
//			System.out.println("Dispenser port opened successfully.");
//		} else {
//			System.out.println("Dispenser port opening failed.");
//		}
	}

	public void listenSerial() {
		//create a listener and start listening
		pinModule.addDataListener(new SerialPortDataListener() {
			@Override
			public int getListeningEvents() {
				return SerialPort.LISTENING_EVENT_DATA_AVAILABLE;
			}

			@Override
			public void serialEvent(SerialPortEvent event) {
				if (event.getEventType() != SerialPort.LISTENING_EVENT_DATA_AVAILABLE)
					return; //wait until we receive data

				byte[] newData = new byte[pinModule.bytesAvailable()]; //receive incoming bytes
				pinModule.readBytes(newData, newData.length); //read incoming bytes
				String serialData = new String(newData); //convert bytes to string

				//print string received from the Arduino
				System.out.println(serialData);
				receivedData = serialData;
				setValue(receivedData);
			}
		});
	}

	//Seperates IBAN from number input
	private void setValue(String data) {
		if (data.length() > 1) {
			RFID = data;
		} else if (data.length() == 1) {
			key = data;
		}
	}

	//Sends receipt for to the printer
	public void sendDispenser(String i) {
		try {
			dispenser.getOutputStream().write(i.getBytes());
			dispenser.getOutputStream().flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/* getters and setters */
	public String getKey() {
		return key;
	}

	public String getRFID() {
		return RFID;
	}

	public void resetRFID() {
		RFID = "";
	}

	public void resetKey() {
		key = "";
	}
}