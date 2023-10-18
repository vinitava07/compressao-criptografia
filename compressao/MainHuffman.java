
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class HuffNode {

    int p;
    char element;
    HuffNode left;
    HuffNode right;

    public HuffNode(int prob, char s) {

        this.p = prob;
        this.element = s;
        this.left = null;
        this.right = null;

    }

    public HuffNode(HuffNode left, HuffNode right) {
        this.left = left;
        this.right = right;
        this.p = left.p + right.p;
        this.element = '\u001b';
    }

    public int getP() {
        return this.p;
    }

}

class Tuple {

    char c;
    int ocorruences;

    Tuple() {
        this.c = '0';
        this.ocorruences = 0;
    }

    public int getOcurrences() {
        return ocorruences;
    }

}

class Huffman {

    HuffNode root;
    String rawText;
    StringBuilder compressedText;

    Map<Character, String> huffTable;

    Huffman(String txt) {
        this.root = null;
        this.rawText = txt;
        huffTable = new HashMap<>();
    }

    public void buildTree() {
        ArrayList<Tuple> arrayList = new ArrayList<>();
        countOcurrences(arrayList);
        arrayList.sort(Comparator.comparingInt(Tuple::getOcurrences));
        ArrayList<HuffNode> main = new ArrayList<>();
        ArrayList<HuffNode> aux = new ArrayList<>();
        HuffNode auxNode;

        for (int i = 0; i < arrayList.size(); i++) {
            auxNode = new HuffNode(arrayList.get(i).ocorruences, arrayList.get(i).c);
            main.add(i, auxNode);
        }
        main.sort(Comparator.comparingInt(HuffNode::getP));
        mergeNodes(main, aux);
        if (main.size() == 0) {
            this.root = aux.get(0);
        } else {
            this.root = main.get(0);
        }
        createHashMap(this.root, "");
        // printTree(this.root, "");
    }

    public void createCompressedText() {
        this.compressedText = new StringBuilder();
        for (int i = 0; i < rawText.length(); i++) {
            compressedText.append(huffTable.get(rawText.charAt(i)));
        }
    }

    private void createHashMap(HuffNode n, String s) {
        if (n != null) {
            if (n.element != '\u001b') {
                huffTable.put(n.element, s);
            }
            createHashMap(n.left, s + "0");
            createHashMap(n.right, s + "1");
        }
    }

    public void printTree(HuffNode n, String s) {
        if (n != null) {
            if (n.element != '\u001b') {
                System.out.println(s + " " + n.element);
            }
            printTree(n.left, s + "0");
            printTree(n.right, s + "1");
        }

    }

    public void mergeNodes(ArrayList<HuffNode> main, ArrayList<HuffNode> aux) {

        HuffNode auxNode;
        if (main.size() == 1) {
            return;
        }
        auxNode = new HuffNode(main.get(0), main.get(1));
        // aux.add(auxNode);
        for (int i = 2; i < main.size(); i++) {
            aux.add(main.get(i));
        }
        sortStack(aux, auxNode);
        main.clear();
        mergeNodes(aux, main);

    }

    private void sortStack(ArrayList<HuffNode> aux, HuffNode huffNode) {

        for (int i = 0; i < aux.size(); i++) {
            if (aux.get(i).p > huffNode.p) {
                aux.add(i, huffNode);
                return;
            }
        }
        aux.add(huffNode);

    }

    public void countOcurrences(List<Tuple> al) {

        Tuple tuple = new Tuple();
        tuple.c = rawText.charAt(0);
        tuple.ocorruences++;
        al.add(tuple);
        boolean found = false;

        for (int index = 1; index < rawText.length(); index++) {

            for (int i = 0; i < al.size() && !found; i++) {
                if (al.get(i).c == rawText.charAt(index)) {
                    al.get(i).ocorruences++;
                    found = true;

                }

            }
            if (!found) {
                tuple = new Tuple();
                tuple.c = rawText.charAt(index);
                tuple.ocorruences++;
                al.add(tuple);
            }

            found = false;

        }

    }

}

public class MainHuffman {

    public static void main(String[] args) {
        // System.out.println("Hello");
        try (RandomAccessFile raf = new RandomAccessFile("ListaAnime.csv", "rw")) {

            StringBuilder sb = new StringBuilder();
            while (raf.getFilePointer() < raf.length() / 5) {
                sb.append(raf.readLine());
                sb.append('\n');
            }
            // System.out.println(sb);
            Huffman huffman = new Huffman(sb.toString());
            // Huffman huffman = new Huffman("cavalo");
            huffman.buildTree();
            huffman.createCompressedText();
            System.out.println(huffman.compressedText);
            System.out.println(
                    "COMPRESSIONNNN: " + (((double) huffman.compressedText.toString().getBytes().length / 8)
                            / (sb.toString().getBytes().length)));
            // huffman.printTree(huffman.root, "");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
