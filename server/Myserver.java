/**
 * 
 */
package socket;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * @author 罗先生
 *
 */
public class Myserver {

	public static void main(String[] args) {
		DataInputStream dis = null;
		Socket socket = null;
		FileOutputStream fos = null;
		InputStreamReader ir = null;
		BufferedReader br = null;
		try {
			int length = 0;
			byte[] getByte = new byte[1024];
			ServerSocket ss = new ServerSocket(7777);
			System.out.println("服务器创建完毕");
			socket = ss.accept();
			ir = new InputStreamReader(socket.getInputStream());
			br = new BufferedReader(ir);
			System.out.println("连接到客户端");
			dis = new DataInputStream(socket.getInputStream());
			File file = new File("D:\\workspace\\socket\\src\\socket\\result.json");//Select your local file to save directory
			fos = new FileOutputStream(file);
			String first = br.readLine();
			String second = br.readLine();
			System.out.println(first);
			System.out.println(second);
			System.out.println();
			System.out.println("准备接收文件");
			while ((length = dis.read(getByte)) > 0) {
				fos.write(getByte, 0, length);
				fos.flush();
			}
			System.out.println("文件接收完毕");
		} catch (IOException e) {
			e.getStackTrace();
		} finally {
			try {
				dis.close();
				fos.close();
				socket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		try {
            //通过传入File实例化Scanner类
            Scanner sc = new Scanner(new File("D:\\workspace\\socket\\src\\socket\\result.json"));
            //按行读取test.txt文件内容
            while (sc.hasNextLine()) {
                System.out.println(sc.nextLine());
            }
            sc.close();
        } catch (FileNotFoundException e) {

        }

	}

}
