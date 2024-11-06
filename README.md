# Padding Oracle Attack

This project implements a Padding Oracle Attack in Java for the class Information Security from Kyungpook National University. The attack targets a block cipher encrypted message and uses an oracle to verify padding correctness, enabling the decryption of the ciphertext without needing the secret key.

## Description

The `PaddingOracleAttack` class performs the attack by iterating through potential values for each block of the encrypted message and interacting with a `pad_oracle` to identify valid padding. Once the valid padding is found, the corresponding block is decrypted. This process is repeated for each block in the ciphertext.

### Main Components

- **PaddingOracleAttack**: Contains the logic for performing the padding oracle attack on the ciphertext.
- **pad_oracle**: A mock oracle class that simulates the validation of padding. In real scenarios, this would interact with an actual system that can provide padding validation.
- **HexUtil**: A utility class for performing hexadecimal string operations, including XOR, byte array to hex conversion, and removing padding.

## How the Attack Works

1. **Initialization**: The attack begins with a known Initialization Vector (IV) and the ciphertext.
2. **Brute Force**: For each block, the attack iterates through all 256 possible byte values to identify the correct padding.
3. **Decryption**: Upon finding the correct padding, the decrypted byte for that block is determined and prepended to the decrypted message.
4. **Final Output**: After processing all blocks, the final decrypted message is displayed in both hexadecimal and ASCII formats.

## Usage

### Example Code

```java
String iv = "0xf3e7a7327f1fc500";  // Initialization Vector in hexadecimal format
String cipherText = "0xd2f8a91f2b61b980";  // Encrypted Ciphertext in hexadecimal format
pad_oracle oracle = new pad_oracle(); 

// Perform the attack and decrypt the ciphertext
String decryptedHex = PaddingOracleAttack.paddingOracleAttack(cipherText, oracle);
decryptedHex = "0x" + decryptedHex;  // Add 0x prefix to match original format

// Output the results
printDecryptedText(decryptedHex, iv);
