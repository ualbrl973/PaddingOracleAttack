/**
 * This class executes the Padding Oracle attack on a given ciphertext using a provided oracle.
 * It decrypts the ciphertext and prints the result in both hexadecimal and ASCII formats.
 * Author: Bruno Ramirez Ledesma
 */


import java.math.BigInteger;
import java.util.Arrays;

public class p_SB202400516 {


    public static void main(String[] args) {
         if (args.length != 2) {
             System.out.println("Usage: java p_SB202400516 <C0> <C1>");
             return;
         }

         String iv = args[0]; // IV in hexadecimal format
         String cipherText = args[1]; // Encrypted Ciphertext in hexadecimal format
    	
     //   String iv = "0xf3e7a7327f1fc500"; 
     //   String cipherText = "0xd2f8a91f2b61b980"; 
        pad_oracle oracle = new pad_oracle(); 
        
        

        PaddingOracleAttack attack = new PaddingOracleAttack(oracle);
        String decryptedHex = attack.paddingOracleAttack(cipherText, oracle);
        decryptedHex = "0x" + decryptedHex;  // Add 0x prefix to match original format
        
        // Output the results
        printDecryptedText(decryptedHex, iv);
       
    }
	
	  /**
     * Prints the final decrypted message in both hex and ASCII format.
     * @param decryptedText the decrypted text in hexadecimal format
     * @param iv the original initialization vector
     */
    private static void printDecryptedText(String decryptedText, String iv) {
        System.out.println("=".repeat(50));
        System.out.println("		    RESULT");
        System.out.println("=".repeat(50));

        String plainTextHex = HexUtil.xorHex(decryptedText, iv);
        System.out.print("Plain Text (HEX): " + plainTextHex);
        String plainText = HexUtil.removePadding(plainTextHex);
        System.out.println("Plain Text (ASCII): " + HexUtil.hexToAscii(plainText));
    }
  }


    
    
    
    
    
    

 

