package com.demohouse.walletcore.exception;

public class AddressFormatException extends IllegalArgumentException {
    public AddressFormatException() {
    }

    public AddressFormatException(String message) {
        super(message);
    }

    public static class WrongNetwork extends AddressFormatException.InvalidPrefix {
        public WrongNetwork(int versionHeader) {
            super("Version code of address did not match acceptable versions for network: " + versionHeader);
        }

        public WrongNetwork(String hrp) {
            super("Human readable part of address did not match acceptable HRPs for network: " + hrp);
        }
    }

    public static class InvalidPrefix extends AddressFormatException {
        public InvalidPrefix() {
        }

        public InvalidPrefix(String message) {
            super(message);
        }
    }

    public static class InvalidChecksum extends AddressFormatException {
        public InvalidChecksum() {
            super("Checksum does not validate");
        }

        public InvalidChecksum(String message) {
            super(message);
        }
    }

    public static class InvalidDataLength extends AddressFormatException {
        public InvalidDataLength() {
        }

        public InvalidDataLength(String message) {
            super(message);
        }
    }

    public static class InvalidCharacter extends AddressFormatException {
        public final char character;
        public final int position;

        public InvalidCharacter(char character, int position) {
            super("Invalid character '" + Character.toString(character) + "' at position " + position);
            this.character = character;
            this.position = position;
        }
    }
}
