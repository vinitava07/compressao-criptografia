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
        for (int i = 65; i <= 90; i++) { // Letras maiúsculas (A-Z)
            dictionary.put(String.valueOf((char) i), position);
            position++;
        }
        for (int i = 97; i <= 122; i++) { // Letras minúsculas (a-z)
            dictionary.put(String.valueOf((char) i), position);
            position++;
        }
        for (int i = 48; i <= 57; i++) { // Números (0-9)
            dictionary.put(String.valueOf((char) i), position);
            position++;
        }
        // dictionary.put("a", 0);
        // position++;
        // dictionary.put("b", 1);
        // position++;
        // dictionary.put("w", 2);
        // position++;

    }

    public void compression() {

        char k;
        boolean append = false;
        int cont = 0;
        StringBuilder pattern = new StringBuilder();
        String toAdd;
        for (int i = 0; i < rawText.length();) {
            pattern.append(rawText.charAt(i));
            cont = i;
            while (dictionary.containsKey(pattern.toString()) && ++i < rawText.length()) {
                pattern.append(rawText.charAt(i));
            }
            if (i < rawText.length()) {
                toAdd = (pattern.substring(0, pattern.length() - 1));
            } else {
                toAdd = (pattern.substring(0, pattern.length()));
            }
            compressedTxt.append(String.valueOf(dictionary.get(toAdd) + " "));
            dictionary.put(pattern.toString(), position++);
            pattern.setLength(0);

        }
        System.out.println("raw: " + rawText);
        System.out.println("final " + compressedTxt);
        System.out.println(dictionary.keySet());

    }

}

/**
 * Main
 */
public class Main {

    public static void main(String[] args) {

        String texto = "wabbawabba";
        // String texto = "abab";
        LZW lzw = new LZW(texto);
        lzw.compression();

    }
}