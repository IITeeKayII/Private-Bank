import com.fazecast.jSerialComm.*;

import java.util.concurrent.TimeUnit;

public class Serial {
	private String newData = "";
	public void setData(String data){
		if (data != "" && data != null){
			newData = data;
		} else {
			newData = "";
		}
	}

	public String getData(){
		return newData;
	}

	public void listenSerial() {
		//set serial port
		SerialPort comPort = SerialPort.getCommPort("COM9");
		
		//set the baud rate to 9600 (same as the Arduino)
		comPort.setBaudRate(9600);
		
		//open the port
		comPort.openPort();
		
		//create a listener and start listening
		comPort.addDataListener(new SerialPortDataListener() {
			@Override
			public int getListeningEvents() { 
				return SerialPort.LISTENING_EVENT_DATA_AVAILABLE; 
			}
			@Override
			public void serialEvent(SerialPortEvent event)
			{
				if (event.getEventType() != SerialPort.LISTENING_EVENT_DATA_AVAILABLE)
				return; //wait until we receive data

				try {
					TimeUnit.MILLISECONDS.sleep(50);
				} catch (Exception e){
					System.out.println(e);
				}
			
				byte[] newData = new byte[comPort.bytesAvailable()]; //receive incoming bytes
				comPort.readBytes(newData, newData.length); //read incoming bytes
				String serialData = new String(newData); //convert bytes to string
			  
				//print string received from the Arduino
				setData(serialData);
			}
		});
	}
}