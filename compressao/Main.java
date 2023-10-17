import java.util.HashMap;

class LZW {

    HashMap<String, Integer> dictionary;
    int position;
    StringBuffer compressedTxt;
    String rawText;

    LZW(String txt) {
        rawText = txt;
        position = 0;
        dictionary = new HashMap<>();
        compressedTxt = new StringBuffer();
        createDictionary();
    }

    public void createDictionary() {
        // for (int i = 65; i <= 90; i++) { // Letras maiúsculas (A-Z)
        // dictionary.put(String.valueOf((char) i), position);
        // position++;
        // }
        // for (int i = 97; i <= 122; i++) { // Letras minúsculas (a-z)
        // dictionary.put(String.valueOf((char) i), position);
        // position++;
        // }
        // for (int i = 48; i <= 57; i++) { // Números (0-9)
        // dictionary.put(String.valueOf((char) i), position);
        // position++;
        // }
        dictionary.put("a", 0);
        position++;
        dictionary.put("b", 1);
        position++;
        dictionary.put("w", 2);
        position++;

    }

    public void compression() {

        char k;
        boolean append = false;
        int cont = 0;
        StringBuffer pattern = new StringBuffer();
        for (int i = 0; i < rawText.length()-1;) {
            pattern.append(rawText.charAt(i));
            cont = i;
            while (dictionary.containsKey(pattern.toString()) && i+1 < rawText.length()) {
                i++;
                pattern.append(rawText.charAt(i));
            }
            System.out.println("pre append: " + pattern.substring(0, pattern.length() - 1));
            System.out.println(compressedTxt
                    .append(String.valueOf(dictionary.get(pattern.substring(0, pattern.length() - 1))) + " "));
            System.out.println("patter len: " + pattern.length());
            System.out.println("i: " + cont);
            System.out.println("pos append: " + pattern);
            dictionary.put(pattern.toString(), position++);
            pattern.setLength(0);

        }
        System.out.println(compressedTxt);
        System.out.println(dictionary.keySet());

    }

}

/**
 * Main
 */
public class Main {

    public static void main(String[] args) {

        String texto = "wabbawabba";
        LZW lzw = new LZW(texto);
        StringBuilder str = new StringBuilder();
        lzw.compression();
        // for (int i = 0; i < lzw.position; i++) {
        // System.out.println(lzw.dictionary.keySet());
        // }

    }
}