import com.fazecast.jSerialComm.*;

import java.util.Scanner;

public class Serial {
	private String key = "";
	private String RFID = "";
	private String receivedData;

	static SerialPort serialPort = SerialPort.getCommPort("COM9");
	private SerialPort[] portList;
	private Scanner scanner;


	Serial() {
		portList = SerialPort.getCommPorts();
		scanner = new Scanner(System.in);
	}

	public void openPort() {
		int i = 1;
//		for (SerialPort port : portList) {
//			System.out.println(i++ + ": " + port.getSystemPortName());
//		}

		int chosenPort = 0; // = scanner.nextInt();
		if (chosenPort < i) {
			//serialPort = portList[chosenPort - 1];
			serialPort.openPort();
			serialPort.setBaudRate(9600);
			if (serialPort.openPort()) {
				System.out.println("Port opened successfully.");
			} else {
				System.out.println("port opening failed.");
			}
		} else {
			System.out.println("This port doesn't exist.");
			openPort();
		}
	}

	public void listenSerial() {
		//create a listener and start listening
		serialPort.addDataListener(new SerialPortDataListener() {
			@Override
			public int getListeningEvents() {
				return SerialPort.LISTENING_EVENT_DATA_AVAILABLE;
			}

			@Override
			public void serialEvent(SerialPortEvent event) {
				if (event.getEventType() != SerialPort.LISTENING_EVENT_DATA_AVAILABLE)
					return; //wait until we receive data

				byte[] newData = new byte[serialPort.bytesAvailable()]; //receive incoming bytes
				serialPort.readBytes(newData, newData.length); //read incoming bytes
				String serialData = new String(newData); //convert bytes to string

				//print string received from the Arduino
				//System.out.println(serialData);
				receivedData = serialData;
				setValue(receivedData);
			}
		});
	}

	private void setValue(String data) {
		//System.out.println("setvalue method");
		if (data.length() > 1) {
			RFID = data;
			//System.out.println("in serial.java: " + RFID);
		} else if (data.length() == 1) {
			key = data;
//			System.out.println(key);
		}
	}

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