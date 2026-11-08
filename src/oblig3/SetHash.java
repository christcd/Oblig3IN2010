package oblig3;

import java.io.*;

class SetHash {

	// Node i lenkelisten i en bucket
	static class Node {
		int key;
		Node next;

		Node(int key, Node next) {
			this.key = key;
			this.next = next;
		}
	}

	
	private Node[] table = new Node[16]; // Buckets, N = table.length
	private int size = 0; // Antall elementer i settet


    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int N = Integer.parseInt(br.readLine());
		SetHash set = new SetHash();

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

	// Hashfunksjon fra forelesning, h(k, N) = k mod N
	private int hash(int x){
		return x % table.length;
	}

	// Sjekker gjennom listen i bucketen til x
	boolean contains(int x) {
		Node n = table[hash(x)];
		while(n != null) {
			if(n.key == x){
				return true;
			}
			n = n.next;
		}
		return false; // Hvis ingen x
	}

	// Sett inn x i settet, ignorerer duplikate
	void insert(int x) {
		if(contains(x)){
			return;
		}

		// Holder load factor under 0.75 (size / N), rehasher hvis den blir for høy
		int i = hash(x);
		table[i] = new Node(x, table[i]);
		size++;

		if(size * 4 > table.length * 3) {
			resize();
		}
	}

	// Fjern x fra settet
	void remove(int x) {
		int i = hash(x);
		Node prev = null;
		Node n = table[i];

		while(n != null) {
			if(n.key == x) {
				if(prev == null) {
					table[i] = n.next;
				} else {
					prev.next = n.next;
				}
				size--;
				return;
			}
			prev = n;
			n = n.next;
		}
		// Hvis ikke x finnes skjer ingenting
	}

	//Antall elementer i settet
	int size() {
		return size;
	}

	// Rehashing, lager et dobbelt så stort array og setter inn alle elementene på nytt
	// siden plassen til et element avhenger av N
	private void resize() { 
		Node[] old = table;
		table = new Node[old.length * 2];

		for(Node bucket : old){
			Node n = bucket;
			while(n != null) {
				int i = hash(n.key);
				table[i] = new Node(n.key, table[i]);
				n = n.next;
			}
		}
		// Størrelse endres ikke, samme elementer bare større array
	}
}
