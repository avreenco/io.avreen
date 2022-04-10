package org.avreen.security.module.api;

/*
 00 No error
 01 DES Fault (system disabled)
 02 Illegal Function Code. PIN mailing not enabled
 03 Incorrect message length
 04 Invalid data in message: Character not in range (0-9, A-F)
 05 Invalid key index: Index not defined, key with this Index not stored or incorrect key length
 06 Invalid PIN format specifier: only AS/ANSI = 1 & PIN/PAD = 3 specified
 07 PIN format error: PIN does not comply with the AS2805.3 1985 specification, is in an invalid PIN/PAD
 format, or is in an invalid Docutel format
 08 Verification failure
 09 Contents of key memory destroyed: e.g. the Luna EFT (SafeNet Luna EFT) was tampered or all Keys deleted
 0A Uninitiated key accessed. Key or decimalization table (DT) is not stored in the Luna EFT (SafeNet Luna EFT).
 0B Checklength Error. Customer PIN length is less than the minimum PVK length or less than Checklen in
 function.
 0C Inconsistent Request Fields: inconsistent field size.
 0F Invalid VISA Index. Invalid VISA PIN verification key indicator.
 10 Internal Error
 11 Errlog file does not exist
 12 Errlog internal error
 13 Errlog request length invalid
 14 Errlog file number invalid
 15 Errlog index number invalid
 16 Errlog date time invalid
 17 Errlog before/after flag invalid
 19 Unsupported key type
 1A Duplicate key or record
 20 Invalid key specifier length
 21 Unsupported key specifier
 22 Invalid key specifier content
 23 Invalid key specifier format
 24 Invalid Function Modifier. Invalid=00
 25 Invalid key attributes
 27 Hash process failed
 28 Invalid Key Type
 29 Unsupported Triple Des Index
 30 Invalid administrator signature
 32 No administration session
 33 Invalid file type
 34 Invalid signature
 35 KKL disabled
 36 No PIN pad
 37 Pin pad timeout
 39 Public key pair not available
  SafeNet, Inc. 551
 Luna EFT (PH-EFT) Programmers Guide Appendix J
 Error Codes
  SafeNet, Inc. 552
 Error
 Code
 Meaning
 3A Public key pair generating
 3B RSA cipher error
 40 Unsupported HSM stored SEED key
 50 Invalid Variant Scheme
 50 Invalid SDF
 51 Invalid hash indicator
 52 Invalid public key algorithm
 53 Public key pair incompatible
 54 RSA key length error
 60 Software already Loaded
 61 Software being loaded from CD ROM
 62 Software data segment too large
 63 Invalid offset value
 64 Software loading not initiated
 65 Unsupported file id
 66 Unsupported control id
 67 Software image is being verified
 70 Invalid PIN Block flag
 71 Invalid PIN Block random padding
 72 Invalid PIN Block delimiter
 73 Invalid PIN Block RB
 74 Invalid PIN Block. Random number invalid
 75 Invalid PIN Block RA
 76 Invalid PIN Block PIN
 77 Invalid PIN Block PIN length
 78 PIN Block format disabled or requested reformatting not allowed
 79 Validation data check failed
 7F Invalid Print Token
 80 OAEP Decode Error
 81 OAEP Invalid Header Byte
 82 OAEP Invalid PIN Block
 83 OAEP Invalid Random Number
 90 General Printer Error
 F0 Zero length PIN
 91 Invalid Key Block version Id
 92 Key Block Key authentication failure
 93 Invalid Key Usage
 94 Invalid Algorithms
 95 Invalid Mode of use
 96 Invalid Version number
 97 Invalid Export Flag
 98 Invalid Key length
 99 Invalid Reserve Field
 9A Invalid Number of optional block
 9B Invalid Optional block header
 9C Repeated Optional block
 9D Invalid Key Block
 9E Invalid Padding Indicator
 */

/**
 * The interface Error codes.
 */
public interface IErrorCodes {

}
