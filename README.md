# Padding Oracle Attack

This project implements a **Padding Oracle Attack** for decrypting a ciphertext using an oracle to check for valid padding, typically used in block cipher encryption schemes like AES in CBC mode.

The attack is based on a known ciphertext and initialization vector (IV). It attempts to guess the plaintext by exploiting the padding oracle, which verifies whether a given ciphertext has valid padding or not.

This project is part of the **Information Security** course at **Kyungpook National University**.

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
4. **Final Output**: After processing all blocks, the final decrypted message is XOR'ed with the original Initialization Vector to obtain the final plain text in both hexadecimal and ASCII formats.

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

```
### Compiling and executing

First, clone this repository to your local machine.

To compile the Java source code, navigate to the `src` directory and use the following command:

```bash
javac -cp pad_oracle.jar;bcprov-jdk15-130.jar p_SB202400516.java

```

Then, execute using the following command:
```bash
java -cp .;pad_oracle.jar;bcprov-jdk15-130.jar p_SB202400516 0xe584debd2abad5b3 0xcbd746544cdadf30

```
Replace the example hexadecimal values 0xe584debd2abad5b3 and 0xcbd746544cdadf30 with your own IV and encrypted ciphertext.

Ensure you have the following JAR files in your project:
- `pad_oracle.jar`
- `bcprov-jdk15-130.jar`

These JAR files are necessary for cryptographic operations and interacting with the oracle.

## Author

**Bruno Ramirez Ledesma**  
Information Security  
Kyungpook National University
