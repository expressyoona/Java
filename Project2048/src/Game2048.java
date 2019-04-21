import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Random;
import java.awt.GridLayout;
import javax.swing.JLabel;

public class Game2048 extends JFrame {

	private JLabel[][] board = new JLabel[4][4];
	private int n = 4;
	private JPanel contentPane;
	private Random random = new Random();
	private int A[][] = new int[4][4];

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Game2048 frame = new Game2048();
					frame.setTitle("2048 Game");
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	private int getRandomValue(int min, int max) {
		int randomNum = random.nextInt((max - min) + 1) + min;
		return randomNum;
	}

	/**
	 * Create the frame.
	 */
	public Game2048() {
		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {

			}

			@Override
			public void keyReleased(KeyEvent e) {
				int keyCode = e.getKeyCode();
				switch (keyCode) {
				case KeyEvent.VK_UP:
					System.out.println("Up");
					moveUp();
					myRefresh();
					generate();
					break;
				case KeyEvent.VK_DOWN:
					System.out.println("Down");
					rotate180();
					moveUp();
					rotate180();
					myRefresh();
					generate();
					break;
				case KeyEvent.VK_LEFT:
					System.out.println("Left");
					rotate90();
					moveUp();
					rotate270();
					myRefresh();
					generate();
					break;
				case KeyEvent.VK_RIGHT:
					System.out.println("Right");
					rotate270();
					moveUp();
					rotate90();
					myRefresh();
					generate();
					break;
				}
			}
		});
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new GridLayout(4, 4, 0, 0));

		JLabel lblNewLabel = new JLabel("New label");
		contentPane.add(lblNewLabel);

		JLabel lblNewLabel_1 = new JLabel("New label");
		contentPane.add(lblNewLabel_1);

		JLabel lblNewLabel_2 = new JLabel("New label");
		contentPane.add(lblNewLabel_2);

		JLabel lblNewLabel_3 = new JLabel("New label");
		contentPane.add(lblNewLabel_3);

		JLabel lblNewLabel_4 = new JLabel("New label");
		contentPane.add(lblNewLabel_4);

		JLabel lblNewLabel_5 = new JLabel("New label");
		contentPane.add(lblNewLabel_5);

		JLabel lblNewLabel_6 = new JLabel("New label");
		contentPane.add(lblNewLabel_6);

		JLabel lblNewLabel_7 = new JLabel("New label");
		contentPane.add(lblNewLabel_7);

		JLabel lblNewLabel_8 = new JLabel("New label");
		contentPane.add(lblNewLabel_8);

		JLabel lblNewLabel_9 = new JLabel("New label");
		contentPane.add(lblNewLabel_9);

		JLabel lblNewLabel_10 = new JLabel("New label");
		contentPane.add(lblNewLabel_10);

		JLabel lblNewLabel_11 = new JLabel("New label");
		contentPane.add(lblNewLabel_11);

		JLabel lblNewLabel_12 = new JLabel("New label");
		contentPane.add(lblNewLabel_12);

		JLabel lblNewLabel_13 = new JLabel("New label");
		contentPane.add(lblNewLabel_13);

		JLabel lblNewLabel_14 = new JLabel("New label");
		contentPane.add(lblNewLabel_14);

		JLabel lblNewLabel_15 = new JLabel("New label");
		contentPane.add(lblNewLabel_15);

		board[0][0] = lblNewLabel;
		board[0][1] = lblNewLabel_1;
		board[0][2] = lblNewLabel_2;
		board[0][3] = lblNewLabel_3;

		board[1][0] = lblNewLabel_4;
		board[1][1] = lblNewLabel_5;
		board[1][2] = lblNewLabel_6;
		board[1][3] = lblNewLabel_7;

		board[2][0] = lblNewLabel_8;
		board[2][1] = lblNewLabel_9;
		board[2][2] = lblNewLabel_10;
		board[2][3] = lblNewLabel_11;

		board[3][0] = lblNewLabel_12;
		board[3][1] = lblNewLabel_13;
		board[3][2] = lblNewLabel_14;
		board[3][3] = lblNewLabel_15;

		Border border = BorderFactory.createBevelBorder(BevelBorder.RAISED);
		
		for (int i = 0; i < n; i++)
			for (int j = 0; j < n; j++) {
				board[i][j].setBorder(border);
				board[i][j].setText("");
				A[i][j] = 0;
			}

		for (int i = 0; i < 2;) {
			int r = getRandomValue(0, 3);
			int c = getRandomValue(0, 3);
			String s = "" + r + c;
			if (A[r][c] == 0) {
				int v = getRandomValue(1, 2) * 2;
				i++;
				board[r][c].setText("" + v);
				A[r][c] = v;
			}

		}

	}

	private void moveUp() {
		for (int j = 0; j < n; j++) {
			donlai(j);
			merge(j);
			donlai(j);
		}
	}

	void myRefresh() {
		for (int i = 0; i < n; i++)
			for (int j = 0; j < n; j++)
				if (A[i][j] == 0) {
					board[i][j].setText("");
				} else {
					board[i][j].setText("" + A[i][j]);
				}
	}

	void generate() {
		while (true) {
			int r = getRandomValue(0, 3);
			int c = getRandomValue(0, 3);
			if (A[r][c] == 0) {
				int v = getRandomValue(1, 2) * 2;
				board[r][c].setText("" + v);
				A[r][c] = v;
				return;
			}
		}
	}

	void donlai(int j) {
		for (int i = 1; i < n; i++) {
			if (A[i][j] > 0) {
				int k = findNotZero(i, j);
				if (k < (i - 1)) {
					A[k + 1][j] = A[i][j];
					A[i][j] = 0;
				}
			}
		}
	}

	int findNotZero(int i, int j) {
		for (int k = i - 1; k >= 0; k--) {
			if (A[k][j] > 0)
				return k;
		}
		return -1;
	}

	void merge(int j) {
		for (int i = 0; i < n - 1; i++) {
			if (A[i][j] == A[i + 1][j]) {
				A[i][j] *= 2;
				A[i + 1][j] = 0;
			}
		}
	}

	void rotate90() {
		int[][] B = new int[n][n];
		for (int i = 0; i < n; ++i)
			for (int j = 0; j < n; ++j) {
				B[i][j] = A[n - j - 1][i];
			}
		A = B;
	}
	
	void rotate270() {
		int[][] B = new int[n][n];
		for (int i = 0; i < n; ++i)
			for (int j = 0; j < n; ++j) {
				B[i][j] = A[j][n - i - 1];
			}
		A = B;
	}
	
	void rotate180() {
		int[][] B = new int[n][n];
		for (int i = 0; i < n; ++i)
			for (int j = 0; j < n; ++j) {
				B[i][j] = A[n - i - 1][n - j - 1];
			}
		A = B;
	}
	
}
