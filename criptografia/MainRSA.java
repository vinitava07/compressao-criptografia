package criptografia;

import java.io.RandomAccessFile;
import java.math.BigInteger;

/**
 * RSA
 */
class RSA {

    private BigInteger publicKey; // E (e * d) mod z == 1; algoritimo de euclides
    private BigInteger privateKey; // D (primo em relação a z)
    private BigInteger p; // primo grande n1
    private BigInteger q; // primo grande n2
    private BigInteger n; // p * q
    private BigInteger z; // (p-1) * (q-1)
    private String message;
    private BigInteger[] cMessage;
    // 329999
    // 300043

    RSA(String uMessage) {
        publicKey = new BigInteger("0");
        privateKey = new BigInteger("0");
        message = uMessage;
        cMessage = new BigInteger[uMessage.length()];
        calculateNumbers();
    }

    public void cipherMessage() {

        for (int i = 0; i < message.length(); i++) {
            cMessage[i] = new BigInteger(String.valueOf(Integer.valueOf(message.charAt(i)))).modPow(publicKey, n);
        }
        System.out.println("cripto: ");
        for (BigInteger bigInteger : cMessage) {
            System.out.print(bigInteger);
        }
        System.out.println();

    }

    public void uncipherMessage() {

        StringBuilder uncMessage = new StringBuilder();
        for (int i = 0; i < cMessage.length; i++) {
            uncMessage.append((char) (cMessage[i].modPow(privateKey, n).intValue()));
        }

        System.out.println("descriptografado: " + uncMessage);

    }

    private void calculateNumbers() {
        p = new BigInteger("300043");
        q = new BigInteger("329999");
        n = p.multiply(q);
        z = (p.subtract(BigInteger.ONE)).multiply(q.subtract(BigInteger.ONE));
        privateKey = createPrivateKey();
        System.out.println("z: " + z.toString(0));
        System.out.println("PrivateK: " + privateKey.toString(0));
        publicKey = createPublicKey();
        System.out.println("public key: " + publicKey);

    }

    private BigInteger createPublicKey() {

        BigInteger e = null;
        e = privateKey.modInverse(z);
        return e;
    }

    private BigInteger createPrivateKey() {

        BigInteger coprime = null;
        BigInteger aux = z;
        int cont = 0;
        BigInteger b = new BigInteger(z.toString(0));

        for (BigInteger i = new BigInteger("2"); cont != 3 && i.compareTo(aux) < 1; i = i.add(BigInteger.ONE)) {

            if (isCoprime(i, b)) {
                cont += 1;
                coprime = new BigInteger(i.toString(0));
            }

        }
        return coprime;
    }

    private boolean isCoprime(BigInteger a, BigInteger b) {

        while (!b.equals(BigInteger.ZERO)) {
            BigInteger temp = b;
            b = a.mod(b);
            a = temp;
        }
        if (a.equals(BigInteger.ONE)) {
            return true;
        }
        return false;
    }

}

public class MainRSA {

    public static void main(String[] args) {

        try (RandomAccessFile raf = new RandomAccessFile("ListaAnime.csv", "rw")) {
            StringBuilder sb = new StringBuilder();
            while (raf.getFilePointer() < raf.length()/100) {
                sb.append(raf.readLine());
                sb.append('\n');
            }
            RSA rsa = new RSA(sb.toString());
            rsa.cipherMessage();
            rsa.uncipherMessage();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
