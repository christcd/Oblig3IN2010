package oblig3;

import java.io.*;

class SetAvl {
	//Node i avl treet
	static class Node {
		int key;
		Node left, right;
		int height;
		
		Node(int key) {
			this.key = key;
			this.height = 0;
		}
	}

	private Node root = null; // Treets rot, når roten er null er treet tomt
	private int size = 0; 		// Antall elementer i settet

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int N = Integer.parseInt(br.readLine());
		SetAvl set = new SetAvl();		

        for (int i = 0; i < N; i++) {
            String[] line = br.readLine().split(" ");
            String cmd = line[0];

            if (cmd.equals("contains")) {
				System.out.println(set.contains(Integer.parseInt(line[1])));
            } else if (cmd.equals("insert")) {
				set.insert(Integer.parseInt(line[1]));
            } else if (cmd.equals("remove")) {
				set.remove(Integer.parseInt(line[1]));
            } else if (cmd.equals("size")) {
				System.out.println(set.size());
            }
		}
    }

	boolean contains(int x) {
		Node n = root;
		while(n != null){
			if(x < n.key) {
				n= n.left;
			} else if (x > n.key) {
				n = n.right;
			} else {
				return true;
			}
		}
		return false;
	}

	// Setter inn x, ignorerer duplikate
	void insert(int x) {
		root = insert(root, x);
	}

	// Fjerne x fra settet, gjør ingenting om x ikke finnes
	void remove(int x) {
		root = remove(root, x);
	}

	// Antall elementer i settet
	int size() {
		return size;
	}

	// Rekursive hjelpere

	private Node insert(Node n, int x) {
		if(n == null) {
			size++;		// Finner en tom plass, legger til x som nytt element
			return new Node(x);
		}

		if(x < n.key) {
			n.left = insert(n.left, x);
		} else if(x > n.key) {
			n.right = insert(n.right, x);
		} else {
			return n; 		// Hvis x allerede er der skjer ingenting
		}

		return rebalance(n);	// Fikser balanse på vei opp i treet
	}

	private Node remove(Node n, int x){
		if(n == null) {
			return null;
		}

		if(x < n.key){
			n.left = remove(n.left, x);
		} else if (x > n.key) {
			n.right = remove(n.right, x);
		} else {
			// Finner noden som skal fjernes
			// Hvis 1 eller 0 under
			if(n.left == null || n.right == null) {
				size--;
				return(n.left != null) ? n.left : n.right;
			}

			// Hvis to under må den finne den riktige å bytte med, altså den minste
			Node s = n.right;
			while(s.left != null) {
				s = s.left;
			}
			n.key = s.key;
			n.right = remove(n.right, s.key);
		}

		return rebalance(n);
	}

	// Høyden på et subtre hvor høyden er -1
	private int height(Node n) {
		return(n == null) ? -1 : n.height;
	}

	// Gjør om n sin høyde fra nodene under
	private void updateHeight(Node n) {
		n.height = 1 + Math.max(height(n.left), height(n.right));
	}

	// Positiv betyr at venstre side er lengre, negativ betyr høyre er lengre
	private int balance(Node n) {
		return height(n.left) - height(n.right);
	}

	// Høyre rotering, Y sin venstre node x blir nye roten av subtreet
	private Node rotateRight(Node y){
		Node x = y.left;
		y.left = x.right;
		x.right = y;
		updateHeight(y);
		updateHeight(x);
		return x;
	}

	// Venstre rotering
	private Node rotateLeft(Node x){
		Node y = x.right;
		x.right = y.left;
		y.left = x;
		updateHeight(x);
		updateHeight(y);
		return y;
	}

	// Balanserer ut treet og returnerer roten av subtreet
	private Node rebalance(Node n){
		updateHeight(n);
		int b = balance(n);

		if(b > 1){
			// Venstre side for lang
			if(balance(n.left) < 0) {
				n.left = rotateLeft(n.left);
			}
			return rotateRight(n);
		}

		if(b < -1) {
			// Høyre side for lang
			if(balance(n.right) > 0){
				n.right = rotateRight(n.right);
			}
			return rotateLeft(n);
		}
		return n; // Allerede balansert her
	}
}
