package project.GUI;
import java.io.*;

public class ReadFileClass{

	private StringBuffer buffer = new StringBuffer();
	private FileInputStream file = null;
	private int b = 0;
	
	public ReadFileClass(String sourcefile) {
		
		try {
			file = new FileInputStream(sourcefile); //project ���� �ȿ� txt ���� �ֱ�. �Ǵ� ���� ��� ���
			b = file.read();
			while(b!=-1) {
				buffer.append((char)b);
				b = file.read();
			}
//			System.out.println(buffer);
			}catch(FileNotFoundException e) {
				System.out.println("Oops: FileNotFoundException");
			}catch(IOException e) {
				System.out.println("Input error");
			}
	}
   
   public StringBuffer getBuffer() {
	   return buffer;
   }
  
}