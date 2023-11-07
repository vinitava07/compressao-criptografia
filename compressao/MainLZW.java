import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashMap;

class LZW {

    HashMap<String, Integer> compressDictionary;
    HashMap<Integer, String> decompressDictionary;
    int position;
    int positionDecompress;
    StringBuilder compressedTxt;
    ArrayList<Integer> compressedList;
    String rawText;
    StringBuilder decompressedText;

    LZW(String txt) {
        rawText = txt;
        position = 0;
        positionDecompress = 0;
        compressDictionary = new HashMap<>();
        decompressDictionary = new HashMap<>();
        compressedTxt = new StringBuilder();
        decompressedText = new StringBuilder();
        compressedList = new ArrayList<>();
        createDictionary();
    }

    public void createDictionary() {
        for (int i = 0; i <= 255; i++) { // Letras maiúsculas (A-Z)
            compressDictionary.put(String.valueOf((char) i), position);
            decompressDictionary.put(positionDecompress, String.valueOf((char) i));
            positionDecompress++;
            position++;
        }
        // for (int i = 97; i <= 122; i++) { // Letras minúsculas (a-z)
        // compressDictionary.put(String.valueOf((char) i), position);
        // position++;
        // }
        // for (int i = 48; i <= 57; i++) { // Números (0-9)
        // compressDictionary.put(String.valueOf((char) i), position);
        // position++;
        // }
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
            compressedTxt.append(String.valueOf(compressDictionary.get(toAdd) + " "));
            compressedList.add(compressDictionary.get(toAdd));
            compressDictionary.put(pattern.toString(), position++);
            pattern.setLength(0);

        }
        // System.out.println("raw: " + rawText);
        // System.out.println("final: " + compressedTxt);
        // System.out.println(compressDictionary.keySet());

    }

    public void decompress() {

        StringBuilder newPat = new StringBuilder();
        for (int i = 0; i < compressedList.size(); i++) {
            newPat.append(decompressDictionary.get(compressedList.get(i)));
            if (decompressDictionary.containsKey(compressedList.get(i))) {
                if (i == compressedList.size() - 1 || (compressedList.get(i + 1)) >= positionDecompress) {
                    newPat.append(decompressDictionary.get(compressedList.get(i)).charAt(0));
                    decompressDictionary.put(positionDecompress++, newPat.toString());
                } else {
                    newPat.append(decompressDictionary.get(compressedList.get(i + 1)).charAt(0));
                    decompressDictionary.put(positionDecompress++, newPat.toString());

                }
                newPat.setLength(0);
            }
            decompressedText.append(decompressDictionary.get(compressedList.get(i)));

        }
        // decompressedText.append(decompressDictionary.get(compressedList.get(compressedList.size()
        // - 1)));
        // System.out.println("decomp " + decompressedText);
        // System.out.println("raw " + rawText);
    }

}

/**
 * Main
 */
public class MainLZW {

    public static void main(String[] args) {

        // System.out.println("Hello");
        try (RandomAccessFile raf = new RandomAccessFile("ListaAnime.csv", "rw")) {
            // String texto = "sadsaaaaaaaaaaadfaa";
            String texto = "wabbawabba";

            // System.out.println(texto.length());

            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < 18000; i++) {
                sb.append(raf.readLine());
                sb.append('\n');
            }
            // String texto = "abab";
            LZW lzw = new LZW(sb.toString());
            // LZW lzw = new LZW(texto);
            lzw.compression();
            lzw.decompress();
            System.out.println("size: " + lzw.compressedList.size());
            double log2 = Math.ceil(Math.log(lzw.positionDecompress) / Math.log(2));
            System.out.println("binário necessário: " + log2);
            double originalBits = sb.toString().getBytes().length * 8;
            // double originalBits = texto.getBytes().length * 8;  
            double compressedBits = log2 * lzw.compressedList.size();
            System.out.println("compressed bits: " + compressedBits);
            System.out.println("original bits: " + originalBits);
            System.out.println("compression rate: "
                    + (1 - (compressedBits / originalBits)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}