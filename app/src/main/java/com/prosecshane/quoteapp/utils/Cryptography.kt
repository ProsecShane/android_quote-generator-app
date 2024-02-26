package com.prosecshane.quoteapp.utils

/**
 * Utility class that deals with encrypting and decrypting information.
 */
class Cryptography {
    companion object {
        /**
         * Default value for the shift in Caesar's cypher
         */
        private const val builtInShift = 47

        /**
         * Default alphabet used for Caesar's cypher.
         */
        const val alphabet = "abcdefghijklmnopqrstuvwxyz"
        const val digits = "0123456789"
        val alphadigit = alphabet + alphabet.uppercase() + digits

        /**
         * Encrypts a string through Caesar's cypher.
         *
         * @param input A string to be encrypted.
         * @param shift A shift in the alphabet.
         * @param alphabet The alphabet used in the encryption.
         * @return The encrypted string.
         */
        fun encryptCaesar(
            input: String,
            shift: Int = builtInShift,
            alphabet: String = alphadigit
        ): String {
            val result = StringBuilder()
            for (char in input) {
                val index = alphabet.indexOf(char)
                if (index != -1) {
                    var shiftedIndex = (index + shift) % alphabet.length
                    if (shiftedIndex < 0) {
                        shiftedIndex += alphabet.length
                    }
                    result.append(alphabet[shiftedIndex])
                } else result.append(char)
            }
            return result.toString()
        }

        /**
         * Decrypts a string through Caesar's cypher.
         *
         * @param input A string to be decrypted.
         * @param shift A shift in the alphabet.
         * @param alphabet The alphabet used in the decryption.
         * @return The decrypted string.
         */
        fun decryptCaesar(
            input: String,
            shift: Int = builtInShift,
            alphabet: String = alphadigit
        ): String {
            return encryptCaesar(input, -shift, alphabet)
        }
    }
}
