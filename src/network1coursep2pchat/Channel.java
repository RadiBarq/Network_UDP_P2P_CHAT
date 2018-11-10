/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package network1coursep2pchat;
/**
 *
 * @author radibarq
 */
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Channel implements Runnable
{
	private DatagramSocket socket;
	private boolean running;
	
	public void bind(int port, String ipAddress) throws SocketException
	{
            try {
                socket = new DatagramSocket(port, InetAddress.getByName(ipAddress));
            } catch (UnknownHostException ex) {
                Logger.getLogger(Channel.class.getName()).log(Level.SEVERE, null, ex);
            }
	}
	
	public void start()
	{
		Thread thread = new Thread(this);
		thread.start();
	}
	
	public void stop()
	{
		running = false;
		socket.close();
	}

	@Override
	public void run()
	{
		byte[] buffer = new byte[1024];
		DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                
		running = true;
		while(running)
		{
			try
			{
				socket.receive(packet);
				String msg = new String(buffer, 0, packet.getLength());
				Client.msg_area.setText( Client.msg_area.getText().trim() + "\n" + "user: " + msg);
			} 
			catch (IOException e)
			{
				break;
			}
		}
	}
    
       
	public void sendTo(SocketAddress address, String msg) throws IOException
	{
		byte[] buffer = msg.getBytes();
		
                if (socket == null)
                {
                    socket = new DatagramSocket();
                }
                
		DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
		packet.setSocketAddress(address);
		socket.send(packet);
	}
}