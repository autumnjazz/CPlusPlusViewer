package project.IO;
import java.util.*;

import project.GUI.ReadFileClass;

public class Parsing {
	public static KeyWord kw = new KeyWord();
	public String str; //���� ù �ؽ�Ʈ. 
	String raw;
	String[][] keyword = {
			{"class"}, //Ŭ����
			{"public","private","default","protected"}, //���������� 
			{"char", "double", "float", "int", "long", "short", "void", "bool"} //�ڷ���: �޼ҵ� �� ���� ����
	};

	ArrayList<String> temp1 = new ArrayList<String>();
	ArrayList<String> temp2 = new ArrayList<String>();
	
	public void algorithm() { //init() �� ���Ŀ� ��� ����
		//1�ܰ�: Ŭ���� �� ���� �����ͼ� temp1 �� ���� & Ŭ���� �̸� ����. Ŭ���� �ϳ��϶���!
		System.out.println(str);
		temp1 = doParsing(str, 0, "{","};");
		kw.clist.add(new Classes(doParsing(str, 0, " ","{").get(0)));
		
		//2�ܰ�: ���������ں� �з�. KeyWord�� alist �� ����
		ArrayList<String> tmp1 = new ArrayList<String>();
		ArrayList<String> tmp2 = new ArrayList<String>();
		ArrayList<Integer> intTmp = new ArrayList<Integer>();
		for(int i = 0; i<temp1.size(); i++) {
			String[] deli = {":",";"};
			temp2 = Token.finalResult(temp1.get(i), deli, false);
		}

		for(int i =0; i<temp2.size();i++) {
			for(int j= 0; j<keyword[1].length; j++) {
				if(temp2.get(i).contains(keyword[1][j])) {
					intTmp.add(i); //���������� Ű���尡 ����� �ε���
				}
			}
		}

		for(int i =0; i<intTmp.size();i++) {
			kw.alist.add(new Accesses(temp2.get(intTmp.get(i)).trim()));
			if(i == intTmp.size()-1) { //������������������ ���
				for(int j=intTmp.get(i)+1; j<temp2.size();j++) {
					kw.alist.get(i).allAccess.add(temp2.get(j));
				}
			}
			else {
				for(int j=intTmp.get(i)+1; j<intTmp.get(i+1);j++) {
					kw.alist.get(i).allAccess.add(temp2.get(j));
				}
			}
		}
		
		//3�ܰ�  �޼ҵ�� ���� ����
		tmp1.clear();
		for(int i = 0; i< kw.alist.size(); i++) {
			tmp1 = kw.alist.get(i).allAccess;
			for(int j = 0; j<tmp1.size(); j++) {
				String stmp = tmp1.get(j);
				if(tmp1.get(j).contains("(")) {		//�޼ҵ�
					kw.alist.get(i).mAccess.add(stmp.substring(0,stmp.length()));
				}
				else {		//����
					kw.alist.get(i).vAccess.add(stmp.substring(0,stmp.length()));
				}
			}
		}
		
		//4�ܰ� ���� info - �Ϸ�!
		tmp1.clear();	tmp2.clear();
		for(int i = 0; i< kw.alist.size(); i++) {
			tmp2 = kw.alist.get(i).vAccess;
			for(int j = 0; j<tmp2.size(); j++) { //tmp2 �� ����� 0 �̸� ���� �ȵ�
				//�̸� ����
				if(tmp2.get(j).contains("*")) { //�������� ���
					kw.vlist.add(new Variables(doParsing(tmp2.get(j), 2, "*","").get(0)));
				}
				else {
					kw.vlist.add(new Variables(doParsing(tmp2.get(j), 2, " ","").get(0)));
				}
				//�ڷ��� ����
				kw.vlist.get(j).type = doParsing(tmp2.get(j),2,""," ").get(0);
				//���������� ����
				kw.vlist.get(j).access = kw.alist.get(i).name;
			}
		}
		
		//5�ܰ� �޼ҵ� info -�Ϸ�!
		tmp1.clear();	tmp2.clear();	ArrayList<String> tmp3 = new ArrayList<String>();
		for(int i = 0; i< kw.alist.size(); i++) {
			tmp2 = kw.alist.get(i).mAccess; 
			for(int j = 0; j<tmp2.size(); j++) { //tmp2 �� ����� 0 �̸� ���� �ȵ�
				int from = tmp2.get(j).indexOf("("); //ù (
				int to = tmp2.get(j).indexOf(")"); //ù )
				tmp1.add(tmp2.get(j).substring(from+1, to)); //tmp1�� ��ȣ ���� ���� ����. ������ "" ����
				tmp3.add(tmp2.get(j).substring(0,from)); //��ȯ���� �̸�
				//���������� ����
				kw.mlist.add(new Methods(kw.alist.get(i).name));
			}
		}
		for(int j = 0; j<tmp3.size();j++) {
			//�̸� &��ȯ�� ����
			if(tmp3.get(j).contains("Stack")) { //������, �Ҹ���
				kw.mlist.get(j).name = tmp3.get(j).trim();
				kw.mlist.get(j).type = null;
			}
			else {
				kw.mlist.get(j).name = doParsing(tmp3.get(j),2," ","").get(0);
				kw.mlist.get(j).type = doParsing(tmp3.get(j), 2, ""," ").get(0);
			}
			//�Ű����� ���� 
			if(tmp1.get(j).equals("")) //�Ű����� ������
				kw.mlist.get(j).parameter = null;
			else
				kw.mlist.get(j).parameter = doParsing(tmp1.get(j), 2, ""," ").get(0);
		}
		
		//�޼ҵ� ���� ����
		tmp1.clear(); tmp2.clear();	
		String delete = doParsing(raw, 0, "","};").get(0);
		String dlt = raw.replace(delete, "");
		tmp1 = doParsing(dlt, "Stack::", "", "Stack::");
		for(int i =0; i<tmp1.size(); i++) {
			tmp2.add(tmp1.get(i)); //.replace("Stack::", "")
			int from = tmp2.get(i).indexOf("{");
			int to = tmp2.get(i).lastIndexOf("}");
			for(int j = 0; j< kw.mlist.size(); j++) {
				String n = kw.mlist.get(j).name;
				if(tmp2.get(i).contains(":"+n)) { //�Ҹ��ڰ� �ƴҰ��
					kw.mlist.get(j).allContents = tmp2.get(i).substring(from+1, to);
				}
			}
		}
		//�Ҹ����� �޼ҵ� ����
		for(int j = 0; j<kw.mlist.size(); j++) {
			if(kw.mlist.get(j).name.contains("~")) {
				for(int i =0; i<temp2.size(); i++) {
					if(temp2.get(i).contains(kw.mlist.get(j).name)) {
						int from = temp2.get(i).indexOf("{");
						kw.mlist.get(j).allContents = temp2.get(i).substring(from+1);;
					}
				}
			}
		}
		
		
		//6�ܰ�: �޼ҵ� ���� ���� vinm
		for(int i =0;i<kw.mlist.size();i++) {
			for(int j = 0; j<kw.vlist.size(); j++) {
				if(kw.mlist.get(i).allContents == null) { //������ ���� ���
					kw.mlist.get(i).vinm = null;
				}
				else if(kw.mlist.get(i).allContents.contains(kw.vlist.get(j).name)) {
					kw.mlist.get(i).vinm.add(kw.vlist.get(j));
				}
			}
		}
		
		//7�ܰ� ������ ����ϴ� �޼ҵ� musingv
		for(int i =0;i<kw.mlist.size();i++) {
			ArrayList<Variables> vtmp = kw.mlist.get(i).vinm;
				for(int k = 0; k<kw.vlist.size(); k++) {
					int w = vtmp.indexOf(kw.vlist.get(k));
					if(w != -1) {
						kw.vlist.get(k).musingv.add(kw.mlist.get(i));
					}
				}
		}
		
		
		//8�ܰ� Ŭ���� ���� �޼ҵ�� ���� minc, vinc
		for(Methods m : kw.mlist)
			kw.clist.get(0).minc.add(m);
		for(Variables v : kw.vlist)
			kw.clist.get(0).vinc.add(v);
		
		System.out.println("algorithm()����...!");
	} //algorithm
	
	public void print() {
		//Ŭ����
		System.out.println("===============CLASS===============");
		System.out.println("kw.clist.get(0).name: "+kw.clist.get(0).name);
		System.out.print("kw.clist.get(0).minc: ");
		for(Methods m : kw.clist.get(0).minc)
			System.out.print(m.name+"   ");
		System.out.println();
		System.out.print("kw.clist.get(0).vinc: ");
		for(Variables v : kw.clist.get(0).vinc)
			System.out.print(v.name+"   ");
		System.out.println();
		
		//����
		System.out.println("===============VARIABLE===============");
		for(int j = 0; j<kw.vlist.size(); j++) {
			System.out.println("kw.vlist.get("+j+").name: "+kw.vlist.get(j).name);
			System.out.println("kw.vlist.get("+j+").access: "+kw.vlist.get(j).access);
			System.out.println("kw.vlist.get("+j+").type: "+kw.vlist.get(j).type);
			System.out.print("kw.vlist.get("+j+").musingv: ");
			for(Methods m : kw.vlist.get(j).musingv)
				System.out.print(m.name+"   ");
			System.out.println("\n");
			
		}
		//�޼ҵ�
		System.out.println("===============METHOD===============");
		for(int i = 0; i<kw.mlist.size(); i++) {
			System.out.println("kw.mlist.get("+i+").name: "+kw.mlist.get(i).name);
			System.out.println("kw.mlist.get("+i+").access: "+kw.mlist.get(i).access);
			System.out.println("kw.mlist.get("+i+").type: "+kw.mlist.get(i).type);
			System.out.println("kw.mlist.get("+i+").parameter: "+kw.mlist.get(i).parameter);
			System.out.print("kw.mlist.get("+i+").vinm.get(j).name: ");
			for(int j = 0; j<kw.mlist.get(i).vinm.size(); j++) {
				System.out.print(kw.mlist.get(i).vinm.get(j).name+"   ");
			}
			System.out.println("\nkw.mlist.get("+i+").allContents: "+kw.mlist.get(i).allContents);
			System.out.println();
		}
		
		
	}
	
	public Parsing(ReadFileClass rd, String sourcefile){ 
		if(rd.getBuffer() != null) { //������ �ȳ���
			raw = sourcefile;
			Token tkn = new Token(sourcefile);
			str = tkn.contents;
		}
	}
	
/*	void init(){ //�׽�Ʈ�� �ʱ�ȭ
		ReadFileData rd = new ReadFileData("Stack.h_text.txt");
		if(rd.buffer != null) { //������ �ȳ���
			raw = rd.buffer.toString();
			Token tkn = new Token(rd);
			tkn.doAll();
			str = tkn.contents;
		}
		else System.out.println(rd.errmessage);
	}
*/	
	public ArrayList<String> doParsing(String str,int arrNum, String f, String t) {
		if(str == null || str.equals("")) {
			return null;
		}
		else {
			ArrayList<String> list = new ArrayList<String>();
			String adder;
				if(arrNum == 1) adder=":"; //���������� �ڿ��� ":"
				else adder = " "; //�������� " "
			int s = 0;
			int e = str.length();
			
			for(int i = 0; i<keyword[arrNum].length ; i++){
				int r = str.indexOf(keyword[arrNum][i]+adder);
				while(s<e) {
					if(r!=-1) {
						s = r+((keyword[arrNum][i]).length());
						int from;
							if(f.equals("")) from = r-1; //Ű������ ���ۺ���
							else from = str.indexOf(f, s);
						int to;
							if(t.equals("")) to = str.length(); //str�� ������
							else to = str.indexOf(t, s);
						list.add(str.substring(from+1, to));
					}
					else {
						s++;
					}
					r = str.indexOf(keyword[arrNum][i]+adder, s);
				}
				s=0;
			}
			return list;
		}
	}
	public ArrayList<String> doParsing(String str, String deli, String f, String t) {
		if(str == null || str.equals("")) {	return null; }
		else {
			ArrayList<String> list = new ArrayList<String>();
			int s = 0;
			int e = str.length();
			int p = 0;
			while(p<str.length()){
				int r = str.indexOf(deli);
				while(s<e) {
					if(r!=-1) {
						s = r+(deli.length());
						int from;
							if(f.equals("")) from = r-1; //Ű������ ���ۺ���
							else from = str.indexOf(f, s);
						int to;
							if(t.equals("")) to = str.length(); //str�� ������
							else to = str.indexOf(t, s);
							if(to == -1) to = str.length(); 
						list.add(str.substring(from+1, to));
					}
					else {
						s++;
					}
					r = str.indexOf(deli, s);
				}
				p=s;
				s=0;
			}
			return list;
		}
	}

}
