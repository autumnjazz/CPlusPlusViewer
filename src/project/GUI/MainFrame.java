package project.GUI;
import java.awt.*;
import javax.swing.*;


public class MainFrame extends JFrame{ 

	public JPanel panel1 = new JPanel();		//Ʈ��
	public JPanel panel2 = new JPanel();		//���
	public JPanel panel3 = new JPanel();		//cardLayout
	private JPanel leftpanel = new JPanel(); 	//Ʈ��+��� �г�
	
	private CardLayout cards1 = new CardLayout();
	private CardLayout cards2 = new CardLayout();
	private CardLayout cards3 = new CardLayout();

//	private MethodMemberClass m;
//	private TableClass t;
//	private EditablePageClass e;
//	private DataField d;
//	private TreeClass tree;

	
	public MainFrame(){
		
		panel1.setLayout(cards1);
		panel2.setLayout(cards2);
		panel3.setLayout(cards3);
		
		leftpanel.setLayout(new GridLayout(2, 1));
		setLayout(new BorderLayout());
		
//		m = new MethodMemberClass();
//		t = new TableClass();
//		e = new EditablePageClass();
//		d = new DataField();
//		tree = new TreeClass(this);
				
		panel1.add("empty", new JPanel());					//�� ȭ��
//		panel1.add("tree", tree.panel);						//open file��
		
		panel2.add("empty", new JPanel());					//��ȭ��
//		panel2.add("use", m.Panel);							//������� field		
		
		panel3.add("empty", new JPanel());					//ó�� ȭ��
//		panel3.add("table", new JScrollPane(t.table));		//Ŭ���� ���ý�
//		panel3.add("method", e.panel);						//�޼��� ���ý�
//		panel3.add("data", d.mainPanel);					//�ڷ� ���ý�
		
		leftpanel.add(panel1);
		leftpanel.add(panel2);
		
		add(leftpanel, BorderLayout.WEST);
		add(panel3, BorderLayout.CENTER);
		
	}
	
	public void changePanel(int i) {
		if(i==1) cards1.next(this.getContentPane());
		if(i==2) cards2.next(this.getContentPane());
		if(i==3) cards3.next(this.getContentPane());
	}
	
	public CardLayout getCardLayout(int i) {
		if(i==1) return cards1;
		if(i==2) return cards2;
		if(i==3) return cards3;
		else return null;
	}
  
	
}



