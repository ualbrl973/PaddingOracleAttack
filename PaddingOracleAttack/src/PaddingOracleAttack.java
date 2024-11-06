/**
 * The PaddingOracleAttack class implements a Padding Oracle attack on ciphertext encrypted with a block cipher 
 * (e.g., AES in CBC mode). It uses a padding oracle to iteratively guess the plaintext by testing different 
 * attack vectors for each block of ciphertext. The attack proceeds block by block, exploiting the oracle's 
 * feedback on padding validity.
 * 
 * <p><b>Usage:</b></p>
 * <pre>
 *  pad_oracle oracle = new pad_oracle(); // Example oracle implementation
 *  PaddingOracleAttack attack = new PaddingOracleAttack(oracle);
 *  String decryptedText = attack.paddingOracleAttack(cipherText, oracle);
 * </pre>
 * 
 * <p><b>Methods:</b></p>
 * <ul>
 *   <li>{@link #paddingOracleAttack(String, pad_oracle)} - Main method for performing the attack.</li>
 *   <li>{@link #getDecryptedValue(long, int)} - Computes the decrypted value for a block.</li>
 *   <li>{@link #buildAttackVector(long, String, int)} - Builds the attack vector for the oracle query.</li>
 *   <li>{@link #xorAndRebuild(String, int)} - A helper method used by {@link #buildAttackVector(long, String, int)} 
 *       to rebuild the attack vector by XOR-ing the decrypted text with the expected block padding.</li>
 * </ul>
 * 
 * <p><b>Author:</b> Bruno Ramirez Ledesma</p>
 */
public class PaddingOracleAttack {

	  private pad_oracle oracle;

	    public PaddingOracleAttack(pad_oracle oracle) {
	        this.oracle = oracle;
	    }
	    
	    /**
	     * Performs a Padding Oracle attack on the given cipher text using the provided oracle.
	     * It attempts to decrypt the cipher text by making iterative guesses for each block.
	     *
	     * @param cipherText the cipher text to be decrypted
	     * @param oracle the Padding Oracle instance used to check the validity of padding
	     * @return the decrypted text after all blocks have been processed
	     */
	    public static String paddingOracleAttack(String cipherText, pad_oracle oracle) {
	        
	        // Block size for each iteration (64 bits)
	        int blockSize = 8;
	        
	        // Initialize the attack with the IV (initialization vector)
	        String initializationVector = "0x0000000000000000";
	        String decryptedText = "";

	        // Convert the IV string to a long value
	        long attackValue = Long.parseUnsignedLong(initializationVector.substring(2), 16);
	        
	        // Process each block independently
	        for (int blockIndex = 1; blockIndex <= blockSize; blockIndex++) {

	        	System.out.println("=".repeat(50));
	            System.out.println("\nProcessing Block " + blockIndex);
	            System.out.println("=".repeat(50));

	            // Try different attack values (brute-force over 256 possible values)
	            for (int i = 0; i < 256; i++) {
	                
	                // Build the attack vector using the current attack value and the already decrypted text
	                String currentAttackVector = buildAttackVector(attackValue, decryptedText, blockIndex);
	                
	                // Print attack vector
	                System.out.println("Attack Vector (Block " + blockIndex + ", Attack value " + Long.toHexString(attackValue) + "): " + currentAttackVector);
	                
	                // Ask the oracle to check if the attack vector produces valid padding
	                boolean isValidPadding = oracle.doOracle(currentAttackVector, cipherText);
	                
	                if (isValidPadding) {
	                	
	                	System.out.println("[VALID] Attack Vector (Block " + blockIndex + ", Attack value " + Long.toHexString(attackValue) + "): " + currentAttackVector);
	                    // If the padding is valid, we obtain the decrypted value for this block
	                    String decryptedBlockValue = getDecryptedValue(attackValue, blockIndex);

	                    // Prepend the decryptedBlockValue to the decryptedText to maintain correct order
	                    decryptedText = decryptedBlockValue + decryptedText;
	                    System.out.println("Decrypted Block Value (Block " + blockIndex + "): " + decryptedBlockValue);
	                    
	                    // Reset attackValue for the next block
	                    attackValue = Long.parseUnsignedLong(initializationVector.substring(2), 16);
	                    break; // Move on to the next block
	                }

	                // Increment the attack value for the next guess
	                attackValue++;
	            }

	          
	        }
	        
	        // Print final decrypted text
	        System.out.println("\nFinal Decrypted Text: 0x" + decryptedText);
	        return decryptedText;
	    }
	    
	    /**
	     * Calculates the decrypted value for a given attack value and block index.
	     * The decrypted value is obtained by XOR-ing the attack value with the expected padding for the block.
	     *
	     * @param attackValue the current attack value (XOR-ed with block padding)
	     * @param blockIndex the current block index being processed
	     * @return the decrypted value for the given block
	     */
	    private static String getDecryptedValue(long attackValue, int blockIndex) {
	    	
	    	//Convert the attack value to hex and apply the block padding
	    	String hexAttackValue = String.format("%02x", attackValue);
	        String blockPadding = "0" + blockIndex;
	        
	        //XOR the attack value with the block padding to get the decrypted value
	        return HexUtil.xorHex(hexAttackValue, blockPadding).substring(2).toUpperCase();
		}


	    /**
	     * Builds the attack vector used in the Padding Oracle attack.
	     * This vector includes the current attack value and the previously decrypted text, XOR-ed with the expected padding for the block.
	     *
	     * @param attackValue the current attack value for the block
	     * @param decryptedText the already decrypted text (used for constructing the attack vector)
	     * @param blockIndex the current block index being processed
	     * @return the formatted attack vector used to query the oracle
	     */
	    private static String buildAttackVector(long attackValue, String decryptedText, int blockIndex) {
	        //Calculate the necessary padding length for the attack vector formatting
	        int paddingLength = 18 - (blockIndex * 2);
	        
	        //Format the attack value with leading zeros for the correct attack vector size
	        String formattedAttack = String.format("0x%0" + paddingLength + "X", attackValue);
	        
	        //Build the full attack vector by adding the XOR of decrypted text with the expected padding
	        return formattedAttack + xorAndRebuild(decryptedText, blockIndex).toUpperCase();
	    }


	    /**
	     * Rebuilds the attack vector by XOR-ing the decrypted text with the expected block padding.
	     * Each byte of the decrypted text is XOR-ed with the padding for the current block.
	     *
	     * @param decryptedText the decrypted text up to the current block
	     * @param blockIndex the current block index being processed
	     * @return the rebuilt string used to generate the attack vector
	     */
		public static String xorAndRebuild(String decryptedText, int blockIndex) {
			String blockPadding = "0" + blockIndex;
	        StringBuilder result = new StringBuilder();

	        // Iterate through each decryptedValue from the decryptedText string (each 2 characters) and XOR it with the blockPadding padding
	        // then reconstruct the string in order to be used by the attackVector
	        for (int i = 0; i < decryptedText.length(); i += 2) {

	            String substring = decryptedText.substring(i, Math.min(i + 2, decryptedText.length()));
	            String xorResult = HexUtil.xorHex(substring, blockPadding);
	            result.append(xorResult.substring(2));
	        }

	        return result.toString().toUpperCase();
	    }
	
}
