import java.io.RandomAccessFile;
import java.util.HashMap;

class LZW {

    HashMap<String, Integer> compressDictionary;
    HashMap<String, Integer> decompressDictionary;
    int position;
    StringBuilder compressedTxt;
    String rawText;

    LZW(String txt) {
        rawText = txt;
        position = 0;
        compressDictionary = new HashMap<>();
        decompressDictionary = new HashMap<>();
        compressedTxt = new StringBuilder();
        createDictionary();
    }

    public void createDictionary() {
        for (int i = 65; i <= 90; i++) { // Letras maiúsculas (A-Z)
            compressDictionary.put(String.valueOf((char) i), position);
            position++;
        }
        for (int i = 97; i <= 122; i++) { // Letras minúsculas (a-z)
            compressDictionary.put(String.valueOf((char) i), position);
            position++;
        }
        for (int i = 48; i <= 57; i++) { // Números (0-9)
            compressDictionary.put(String.valueOf((char) i), position);
            position++;
        }
        // compressDictionary.put("a", 0);
        // position++;
        // compressDictionary.put("b", 1);
        // position++;
        // compressDictionary.put("w", 2);
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
            if (!compressDictionary.containsKey(pattern.toString())) {
                compressDictionary.put(pattern.toString(), position++);
            }
            while (compressDictionary.containsKey(pattern.toString()) && ++i < rawText.length()) {
                pattern.append(rawText.charAt(i));
            }
            // i < rawText.length()
            // System.out.println(pattern);
            if (i >= rawText.length()) {
                toAdd = (pattern.substring(0, pattern.length()));

            } else if (pattern.length() > 1) {
                toAdd = (pattern.substring(0, pattern.length() - 1));
            } else {
                toAdd = (pattern.substring(0, pattern.length()));
            }
            System.out.println("toadd: " + toAdd);

            compressedTxt.append(String.valueOf(compressDictionary.get(toAdd) + " "));
            compressDictionary.put(pattern.toString(), position++);
            pattern.setLength(0);

        }
        System.out.println("raw: " + rawText);
        System.out.println("final: " + compressedTxt);
        System.out.println(compressDictionary.keySet());
        System.out.println("SIZEEEEE: " + compressDictionary.keySet().size());

    }

}

/**
 * Main
 */
public class Main {

    public static void main(String[] args) {

        String texto = "desgraçaaaa";
        // System.out.println("Hello");
        System.out.println(texto.length());
        try (RandomAccessFile raf = new RandomAccessFile("ListaAnime.csv", "rw")) {

            // StringBuilder sb = new StringBuilder();
            // for (int i = 0; i < 10000; i++) {
            //     sb.append(raf.readLine());
            // }
            // String texto = "abab";
            LZW lzw = new LZW(texto);
            lzw.compression();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}