package com.optimasc.text;

import java.text.ParseException;

import com.optimasc.lang.IntegerSelectItems;
import com.optimasc.lang.SelectItem;

/**
 * Routines for decomposition and composition according to different Unicode
 * normalization algorithms.
 * 
 * <p>
 * It supports the following algorithms:
 * </p>
 * 
 * <ul>
 * <li>String Normalization Form C (NFC) which composes base characters and
 * diactrical characters into its equivalent unique codepoint. May be used to
 * normalize a string before it is stored.</li>
 * <li>String Normalization Form KC (NFKC) which composes base characters,
 * diactrical characters and compatible characters into its equivalent unique
 * codepoint. May be used to normalize a string before doing an aggressive
 * search. This method replaces some characters with their equivalents, hence
 * this algorithm is lossy.</li>
 * <li>String Normalization Form D (NFD) which decomposes characters into their
 * base characters and diactrical characters. May be used as part of an
 * algorithm to search for characters without diactrical marks.</li>
 * <li>String search with all diacritical marks removed.</li>
 * </ul>
 * 
 * <p>
 * This code is compatible with Java 1.4, hence only supports decomposition of
 * unicode BMP codepoints 0000 to 04FF which covers latin/greek/cyrillic
 * languages.. Use Java 6 or higher to support all BMP codepoint decomposition.
 * </p>
 * 
 * 
 * @author Carl Eric Codere
 *
 */
public class StringNormalizer
{

  // Lookup table for NFD decompositions (U+0000 to U+04FF)
  private static final String[] DECOMPOSITION_TABLE = new String[0x0500];

  // Pre-calculated fully decomposed forms
  private static final String[] FULL_DECOMPOSITION_TABLE = new String[0x0500];

  //Add this new table at the top with the other static fields
  //Compatibility decomposition table (NFKD) - includes formatting/presentation variants
  private static final String[] COMPATIBILITY_DECOMPOSITION_TABLE = new String[0x0500];

  // Composition lookup: maps "base+combining" to composed character
  private static final java.util.Hashtable COMPOSITION_MAP = new java.util.Hashtable();

  // Combining class for canonical ordering
  // Key: combining character, Value: combining class (0-254)
  private static final byte[] COMBINING_CLASS = new byte[0x0400]; // Covers U+0000 to U+03FF

  /** Non character, bidirectional and deprecated characters */
  /**
   * From Unicode 4.0 Proplist.txt -> # Cn property for non-characters the rest
   * is defined in IETF RFC 4518.
   */
  public static final SelectItem UNICODE_NON_CHARACTERS[] = {
  /* Non character */
  new IntegerSelectItems.IntegerSelectRange(0xFDD0, 0xFDEF),
  /* Non character */
  new IntegerSelectItems.IntegerSelectRange(0xFFFE, 0xFFFF),
  /* Combining characters */
  new IntegerSelectItems.IntegerSelectRange(0x0340, 0x341),
  /* Direction of text mark */
  new IntegerSelectItems.IntegerSelectRange(0x200E, 0x200F),
  /* Bidirectional text markers */
  new IntegerSelectItems.IntegerSelectRange(0x202A, 0x202E),
  /* Swapping and shares */
  new IntegerSelectItems.IntegerSelectRange(0x206A, 0x206F)
  /*    1FFFE..1FFFF  ; Noncharacter_Code_Point # Cn   [2] <noncharacter-1FFFE>..<noncharacter-1FFFF>
      2FFFE..2FFFF  ; Noncharacter_Code_Point # Cn   [2] <noncharacter-2FFFE>..<noncharacter-2FFFF>
      3FFFE..3FFFF  ; Noncharacter_Code_Point # Cn   [2] <noncharacter-3FFFE>..<noncharacter-3FFFF>
      4FFFE..4FFFF  ; Noncharacter_Code_Point # Cn   [2] <noncharacter-4FFFE>..<noncharacter-4FFFF>
      5FFFE..5FFFF  ; Noncharacter_Code_Point # Cn   [2] <noncharacter-5FFFE>..<noncharacter-5FFFF>
      6FFFE..6FFFF  ; Noncharacter_Code_Point # Cn   [2] <noncharacter-6FFFE>..<noncharacter-6FFFF>
      7FFFE..7FFFF  ; Noncharacter_Code_Point # Cn   [2] <noncharacter-7FFFE>..<noncharacter-7FFFF>
      8FFFE..8FFFF  ; Noncharacter_Code_Point # Cn   [2] <noncharacter-8FFFE>..<noncharacter-8FFFF>
      9FFFE..9FFFF  ; Noncharacter_Code_Point # Cn   [2] <noncharacter-9FFFE>..<noncharacter-9FFFF>
      AFFFE..AFFFF  ; Noncharacter_Code_Point # Cn   [2] <noncharacter-AFFFE>..<noncharacter-AFFFF>
      BFFFE..BFFFF  ; Noncharacter_Code_Point # Cn   [2] <noncharacter-BFFFE>..<noncharacter-BFFFF>
      CFFFE..CFFFF  ; Noncharacter_Code_Point # Cn   [2] <noncharacter-CFFFE>..<noncharacter-CFFFF>
      DFFFE..DFFFF  ; Noncharacter_Code_Point # Cn   [2] <noncharacter-DFFFE>..<noncharacter-DFFFF>
      EFFFE..EFFFF  ; Noncharacter_Code_Point # Cn   [2] <noncharacter-EFFFE>..<noncharacter-EFFFF>
      FFFFE..FFFFF  ; Noncharacter_Code_Point # Cn   [2] <noncharacter-FFFFE>..<noncharacter-FFFFF>*/
  };

  /**
   * Whitespace characters according to IETF RFC 4518 and according to Unicode
   * standard in PropList with category : White_Space
   */
  public static final SelectItem UNICODE_WHITESPACE_CODEPOINTS[] = {
      new IntegerSelectItems.IntegerSelectRange(0x0009, 0x000D),
      new IntegerSelectItems.IntegerSelectValue(0x0020),
      new IntegerSelectItems.IntegerSelectValue(0x0085),
      new IntegerSelectItems.IntegerSelectValue(0x00A0),
      new IntegerSelectItems.IntegerSelectValue(0x1680),
      new IntegerSelectItems.IntegerSelectRange(0x2000, 0x200A),
      new IntegerSelectItems.IntegerSelectRange(0x2028, 0x2029),
      new IntegerSelectItems.IntegerSelectValue(0x202F),
      new IntegerSelectItems.IntegerSelectValue(0x205F),
      new IntegerSelectItems.IntegerSelectValue(0x3000) };

  static
  {
    initializeCombiningClasses();
    initializeDecompositionTable();
    initializeCompatibilityDecompositions();
    precomputeFullDecompositions();
    buildCompositionMap();
  }

  /**
   * Initialize combining class values for canonical ordering These values are
   * from the Unicode Character Database
   */
  private static void initializeCombiningClasses()
  {
    // Combining Diacritical Marks (U+0300 to U+036F)
    COMBINING_CLASS[0x0300] = (byte) 230; // Combining Grave Accent
    COMBINING_CLASS[0x0301] = (byte) 230; // Combining Acute Accent
    COMBINING_CLASS[0x0302] = (byte) 230; // Combining Circumflex Accent
    COMBINING_CLASS[0x0303] = (byte) 230; // Combining Tilde
    COMBINING_CLASS[0x0304] = (byte) 230; // Combining Macron
    COMBINING_CLASS[0x0305] = (byte) 230; // Combining Overline
    COMBINING_CLASS[0x0306] = (byte) 230; // Combining Breve
    COMBINING_CLASS[0x0307] = (byte) 230; // Combining Dot Above
    COMBINING_CLASS[0x0308] = (byte) 230; // Combining Diaeresis
    COMBINING_CLASS[0x0309] = (byte) 230; // Combining Hook Above
    COMBINING_CLASS[0x030A] = (byte) 230; // Combining Ring Above
    COMBINING_CLASS[0x030B] = (byte) 230; // Combining Double Acute Accent
    COMBINING_CLASS[0x030C] = (byte) 230; // Combining Caron
    COMBINING_CLASS[0x030D] = (byte) 230; // Combining Vertical Line Above
    COMBINING_CLASS[0x030E] = (byte) 230; // Combining Double Vertical Line Above
    COMBINING_CLASS[0x030F] = (byte) 230; // Combining Double Grave Accent
    COMBINING_CLASS[0x0310] = (byte) 230; // Combining Candrabindu
    COMBINING_CLASS[0x0311] = (byte) 230; // Combining Inverted Breve
    COMBINING_CLASS[0x0312] = (byte) 230; // Combining Turned Comma Above
    COMBINING_CLASS[0x0313] = (byte) 230; // Combining Comma Above
    COMBINING_CLASS[0x0314] = (byte) 230; // Combining Reversed Comma Above
    COMBINING_CLASS[0x0315] = (byte) 232; // Combining Comma Above Right
    COMBINING_CLASS[0x0316] = (byte) 220; // Combining Grave Accent Below
    COMBINING_CLASS[0x0317] = (byte) 220; // Combining Acute Accent Below
    COMBINING_CLASS[0x0318] = (byte) 220; // Combining Left Tack Below
    COMBINING_CLASS[0x0319] = (byte) 220; // Combining Right Tack Below
    COMBINING_CLASS[0x031A] = (byte) 232; // Combining Left Angle Above
    COMBINING_CLASS[0x031B] = (byte) 216; // Combining Horn
    COMBINING_CLASS[0x031C] = (byte) 220; // Combining Left Half Ring Below
    COMBINING_CLASS[0x031D] = (byte) 220; // Combining Up Tack Below
    COMBINING_CLASS[0x031E] = (byte) 220; // Combining Down Tack Below
    COMBINING_CLASS[0x031F] = (byte) 220; // Combining Plus Sign Below
    COMBINING_CLASS[0x0320] = (byte) 220; // Combining Minus Sign Below
    COMBINING_CLASS[0x0321] = (byte) 202; // Combining Palatalized Hook Below
    COMBINING_CLASS[0x0322] = (byte) 202; // Combining Retroflex Hook Below
    COMBINING_CLASS[0x0323] = (byte) 220; // Combining Dot Below
    COMBINING_CLASS[0x0324] = (byte) 220; // Combining Diaeresis Below
    COMBINING_CLASS[0x0325] = (byte) 220; // Combining Ring Below
    COMBINING_CLASS[0x0326] = (byte) 220; // Combining Comma Below
    COMBINING_CLASS[0x0327] = (byte) 202; // Combining Cedilla
    COMBINING_CLASS[0x0328] = (byte) 202; // Combining Ogonek
    COMBINING_CLASS[0x0329] = (byte) 220; // Combining Vertical Line Below
    COMBINING_CLASS[0x032A] = (byte) 220; // Combining Bridge Below
    COMBINING_CLASS[0x032B] = (byte) 220; // Combining Inverted Double Arch Below
    COMBINING_CLASS[0x032C] = (byte) 220; // Combining Caron Below
    COMBINING_CLASS[0x032D] = (byte) 220; // Combining Circumflex Accent Below
    COMBINING_CLASS[0x032E] = (byte) 220; // Combining Breve Below
    COMBINING_CLASS[0x032F] = (byte) 220; // Combining Inverted Breve Below
    COMBINING_CLASS[0x0330] = (byte) 220; // Combining Tilde Below
    COMBINING_CLASS[0x0331] = (byte) 220; // Combining Macron Below
    COMBINING_CLASS[0x0332] = (byte) 220; // Combining Low Line
    COMBINING_CLASS[0x0333] = (byte) 220; // Combining Double Low Line
    COMBINING_CLASS[0x0334] = (byte) 1; // Combining Tilde Overlay
    COMBINING_CLASS[0x0335] = (byte) 1; // Combining Short Stroke Overlay
    COMBINING_CLASS[0x0336] = (byte) 1; // Combining Long Stroke Overlay
    COMBINING_CLASS[0x0337] = (byte) 1; // Combining Short Solidus Overlay
    COMBINING_CLASS[0x0338] = (byte) 1; // Combining Long Solidus Overlay
    COMBINING_CLASS[0x0339] = (byte) 220; // Combining Right Half Ring Below
    COMBINING_CLASS[0x033A] = (byte) 220; // Combining Inverted Bridge Below
    COMBINING_CLASS[0x033B] = (byte) 220; // Combining Square Below
    COMBINING_CLASS[0x033C] = (byte) 220; // Combining Seagull Below
    COMBINING_CLASS[0x033D] = (byte) 230; // Combining X Above
    COMBINING_CLASS[0x033E] = (byte) 230; // Combining Vertical Tilde
    COMBINING_CLASS[0x033F] = (byte) 230; // Combining Double Overline
    COMBINING_CLASS[0x0340] = (byte) 230; // Combining Grave Tone Mark
    COMBINING_CLASS[0x0341] = (byte) 230; // Combining Acute Tone Mark
    COMBINING_CLASS[0x0342] = (byte) 230; // Combining Greek Perispomeni
    COMBINING_CLASS[0x0343] = (byte) 230; // Combining Greek Koronis
    COMBINING_CLASS[0x0344] = (byte) 230; // Combining Greek Dialytika Tonos
    COMBINING_CLASS[0x0345] = (byte) 240; // Combining Greek Ypogegrammeni (iota subscript)
    COMBINING_CLASS[0x0346] = (byte) 230; // Combining Bridge Above
    COMBINING_CLASS[0x0347] = (byte) 220; // Combining Equals Sign Below
    COMBINING_CLASS[0x0348] = (byte) 220; // Combining Double Vertical Line Below
    COMBINING_CLASS[0x0349] = (byte) 220; // Combining Left Angle Below
    COMBINING_CLASS[0x034A] = (byte) 230; // Combining Not Tilde Above
    COMBINING_CLASS[0x034B] = (byte) 230; // Combining Homothetic Above
    COMBINING_CLASS[0x034C] = (byte) 230; // Combining Almost Equal To Above
    COMBINING_CLASS[0x034D] = (byte) 220; // Combining Left Right Arrow Below
    COMBINING_CLASS[0x034E] = (byte) 0; // Combining Upwards Arrow Below

    // Additional ranges would be added here for completeness
    // For now, we cover the main combining marks used in our decomposition table
  }

  /**
   * Initialize the NFD decomposition table
   */
  private static void initializeDecompositionTable()
  {
    // Latin-1 Supplement (U+0080 to U+00FF)
    DECOMPOSITION_TABLE[0x00C0] = "A\u0300"; // À
    DECOMPOSITION_TABLE[0x00C1] = "A\u0301"; // Á
    DECOMPOSITION_TABLE[0x00C2] = "A\u0302"; // Â
    DECOMPOSITION_TABLE[0x00C3] = "A\u0303"; // Ã
    DECOMPOSITION_TABLE[0x00C4] = "A\u0308"; // Ä
    DECOMPOSITION_TABLE[0x00C5] = "A\u030A"; // Å
    DECOMPOSITION_TABLE[0x00C7] = "C\u0327"; // Ç
    DECOMPOSITION_TABLE[0x00C8] = "E\u0300"; // È
    DECOMPOSITION_TABLE[0x00C9] = "E\u0301"; // É
    DECOMPOSITION_TABLE[0x00CA] = "E\u0302"; // Ê
    DECOMPOSITION_TABLE[0x00CB] = "E\u0308"; // Ë
    DECOMPOSITION_TABLE[0x00CC] = "I\u0300"; // Ì
    DECOMPOSITION_TABLE[0x00CD] = "I\u0301"; // Í
    DECOMPOSITION_TABLE[0x00CE] = "I\u0302"; // Î
    DECOMPOSITION_TABLE[0x00CF] = "I\u0308"; // Ï
    DECOMPOSITION_TABLE[0x00D1] = "N\u0303"; // Ñ
    DECOMPOSITION_TABLE[0x00D2] = "O\u0300"; // Ò
    DECOMPOSITION_TABLE[0x00D3] = "O\u0301"; // Ó
    DECOMPOSITION_TABLE[0x00D4] = "O\u0302"; // Ô
    DECOMPOSITION_TABLE[0x00D5] = "O\u0303"; // Õ
    DECOMPOSITION_TABLE[0x00D6] = "O\u0308"; // Ö
    DECOMPOSITION_TABLE[0x00D9] = "U\u0300"; // Ù
    DECOMPOSITION_TABLE[0x00DA] = "U\u0301"; // Ú
    DECOMPOSITION_TABLE[0x00DB] = "U\u0302"; // Û
    DECOMPOSITION_TABLE[0x00DC] = "U\u0308"; // Ü
    DECOMPOSITION_TABLE[0x00DD] = "Y\u0301"; // Ý
    DECOMPOSITION_TABLE[0x00E0] = "a\u0300"; // à
    DECOMPOSITION_TABLE[0x00E1] = "a\u0301"; // á
    DECOMPOSITION_TABLE[0x00E2] = "a\u0302"; // â
    DECOMPOSITION_TABLE[0x00E3] = "a\u0303"; // ã
    DECOMPOSITION_TABLE[0x00E4] = "a\u0308"; // ä
    DECOMPOSITION_TABLE[0x00E5] = "a\u030A"; // å
    DECOMPOSITION_TABLE[0x00E7] = "c\u0327"; // ç
    DECOMPOSITION_TABLE[0x00E8] = "e\u0300"; // è
    DECOMPOSITION_TABLE[0x00E9] = "e\u0301"; // é
    DECOMPOSITION_TABLE[0x00EA] = "e\u0302"; // ê
    DECOMPOSITION_TABLE[0x00EB] = "e\u0308"; // ë
    DECOMPOSITION_TABLE[0x00EC] = "i\u0300"; // ì
    DECOMPOSITION_TABLE[0x00ED] = "i\u0301"; // í
    DECOMPOSITION_TABLE[0x00EE] = "i\u0302"; // î
    DECOMPOSITION_TABLE[0x00EF] = "i\u0308"; // ï
    DECOMPOSITION_TABLE[0x00F1] = "n\u0303"; // ñ
    DECOMPOSITION_TABLE[0x00F2] = "o\u0300"; // ò
    DECOMPOSITION_TABLE[0x00F3] = "o\u0301"; // ó
    DECOMPOSITION_TABLE[0x00F4] = "o\u0302"; // ô
    DECOMPOSITION_TABLE[0x00F5] = "o\u0303"; // õ
    DECOMPOSITION_TABLE[0x00F6] = "o\u0308"; // ö
    DECOMPOSITION_TABLE[0x00F9] = "u\u0300"; // ù
    DECOMPOSITION_TABLE[0x00FA] = "u\u0301"; // ú
    DECOMPOSITION_TABLE[0x00FB] = "u\u0302"; // û
    DECOMPOSITION_TABLE[0x00FC] = "u\u0308"; // ü
    DECOMPOSITION_TABLE[0x00FD] = "y\u0301"; // ý
    DECOMPOSITION_TABLE[0x00FF] = "y\u0308"; // ÿ

    // Latin Extended-A (U+0100 to U+017F)
    DECOMPOSITION_TABLE[0x0100] = "A\u0304"; // Ā
    DECOMPOSITION_TABLE[0x0101] = "a\u0304"; // ā
    DECOMPOSITION_TABLE[0x0102] = "A\u0306"; // Ă
    DECOMPOSITION_TABLE[0x0103] = "a\u0306"; // ă
    DECOMPOSITION_TABLE[0x0104] = "A\u0328"; // Ą
    DECOMPOSITION_TABLE[0x0105] = "a\u0328"; // ą
    DECOMPOSITION_TABLE[0x0106] = "C\u0301"; // Ć
    DECOMPOSITION_TABLE[0x0107] = "c\u0301"; // ć
    DECOMPOSITION_TABLE[0x0108] = "C\u0302"; // Ĉ
    DECOMPOSITION_TABLE[0x0109] = "c\u0302"; // ĉ
    DECOMPOSITION_TABLE[0x010A] = "C\u0307"; // Ċ
    DECOMPOSITION_TABLE[0x010B] = "c\u0307"; // ċ
    DECOMPOSITION_TABLE[0x010C] = "C\u030C"; // Č
    DECOMPOSITION_TABLE[0x010D] = "c\u030C"; // č
    DECOMPOSITION_TABLE[0x010E] = "D\u030C"; // Ď
    DECOMPOSITION_TABLE[0x010F] = "d\u030C"; // ď
    DECOMPOSITION_TABLE[0x0112] = "E\u0304"; // Ē
    DECOMPOSITION_TABLE[0x0113] = "e\u0304"; // ē
    DECOMPOSITION_TABLE[0x0114] = "E\u0306"; // Ĕ
    DECOMPOSITION_TABLE[0x0115] = "e\u0306"; // ĕ
    DECOMPOSITION_TABLE[0x0116] = "E\u0307"; // Ė
    DECOMPOSITION_TABLE[0x0117] = "e\u0307"; // ė
    DECOMPOSITION_TABLE[0x0118] = "E\u0328"; // Ę
    DECOMPOSITION_TABLE[0x0119] = "e\u0328"; // ę
    DECOMPOSITION_TABLE[0x011A] = "E\u030C"; // Ě
    DECOMPOSITION_TABLE[0x011B] = "e\u030C"; // ě
    DECOMPOSITION_TABLE[0x011C] = "G\u0302"; // Ĝ
    DECOMPOSITION_TABLE[0x011D] = "g\u0302"; // ĝ
    DECOMPOSITION_TABLE[0x011E] = "G\u0306"; // Ğ
    DECOMPOSITION_TABLE[0x011F] = "g\u0306"; // ğ
    DECOMPOSITION_TABLE[0x0120] = "G\u0307"; // Ġ
    DECOMPOSITION_TABLE[0x0121] = "g\u0307"; // ġ
    DECOMPOSITION_TABLE[0x0122] = "G\u0327"; // Ģ
    DECOMPOSITION_TABLE[0x0123] = "g\u0327"; // ģ
    DECOMPOSITION_TABLE[0x0124] = "H\u0302"; // Ĥ
    DECOMPOSITION_TABLE[0x0125] = "h\u0302"; // ĥ
    DECOMPOSITION_TABLE[0x0128] = "I\u0303"; // Ĩ
    DECOMPOSITION_TABLE[0x0129] = "i\u0303"; // ĩ
    DECOMPOSITION_TABLE[0x012A] = "I\u0304"; // Ī
    DECOMPOSITION_TABLE[0x012B] = "i\u0304"; // ī
    DECOMPOSITION_TABLE[0x012C] = "I\u0306"; // Ĭ
    DECOMPOSITION_TABLE[0x012D] = "i\u0306"; // ĭ
    DECOMPOSITION_TABLE[0x012E] = "I\u0328"; // Į
    DECOMPOSITION_TABLE[0x012F] = "i\u0328"; // į
    DECOMPOSITION_TABLE[0x0130] = "I\u0307"; // İ
    DECOMPOSITION_TABLE[0x0134] = "J\u0302"; // Ĵ
    DECOMPOSITION_TABLE[0x0135] = "j\u0302"; // ĵ
    DECOMPOSITION_TABLE[0x0136] = "K\u0327"; // Ķ
    DECOMPOSITION_TABLE[0x0137] = "k\u0327"; // ķ
    DECOMPOSITION_TABLE[0x0139] = "L\u0301"; // Ĺ
    DECOMPOSITION_TABLE[0x013A] = "l\u0301"; // ĺ
    DECOMPOSITION_TABLE[0x013B] = "L\u0327"; // Ļ
    DECOMPOSITION_TABLE[0x013C] = "l\u0327"; // ļ
    DECOMPOSITION_TABLE[0x013D] = "L\u030C"; // Ľ
    DECOMPOSITION_TABLE[0x013E] = "l\u030C"; // ľ
    DECOMPOSITION_TABLE[0x0143] = "N\u0301"; // Ń
    DECOMPOSITION_TABLE[0x0144] = "n\u0301"; // ń
    DECOMPOSITION_TABLE[0x0145] = "N\u0327"; // Ņ
    DECOMPOSITION_TABLE[0x0146] = "n\u0327"; // ņ
    DECOMPOSITION_TABLE[0x0147] = "N\u030C"; // Ň
    DECOMPOSITION_TABLE[0x0148] = "n\u030C"; // ň
    DECOMPOSITION_TABLE[0x014C] = "O\u0304"; // Ō
    DECOMPOSITION_TABLE[0x014D] = "o\u0304"; // ō
    DECOMPOSITION_TABLE[0x014E] = "O\u0306"; // Ŏ
    DECOMPOSITION_TABLE[0x014F] = "o\u0306"; // ŏ
    DECOMPOSITION_TABLE[0x0150] = "O\u030B"; // Ő
    DECOMPOSITION_TABLE[0x0151] = "o\u030B"; // ő
    DECOMPOSITION_TABLE[0x0154] = "R\u0301"; // Ŕ
    DECOMPOSITION_TABLE[0x0155] = "r\u0301"; // ŕ
    DECOMPOSITION_TABLE[0x0156] = "R\u0327"; // Ŗ
    DECOMPOSITION_TABLE[0x0157] = "r\u0327"; // ŗ
    DECOMPOSITION_TABLE[0x0158] = "R\u030C"; // Ř
    DECOMPOSITION_TABLE[0x0159] = "r\u030C"; // ř
    DECOMPOSITION_TABLE[0x015A] = "S\u0301"; // Ś
    DECOMPOSITION_TABLE[0x015B] = "s\u0301"; // ś
    DECOMPOSITION_TABLE[0x015C] = "S\u0302"; // Ŝ
    DECOMPOSITION_TABLE[0x015D] = "s\u0302"; // ŝ
    DECOMPOSITION_TABLE[0x015E] = "S\u0327"; // Ş
    DECOMPOSITION_TABLE[0x015F] = "s\u0327"; // ş
    DECOMPOSITION_TABLE[0x0160] = "S\u030C"; // Š
    DECOMPOSITION_TABLE[0x0161] = "s\u030C"; // š
    DECOMPOSITION_TABLE[0x0162] = "T\u0327"; // Ţ
    DECOMPOSITION_TABLE[0x0163] = "t\u0327"; // ţ
    DECOMPOSITION_TABLE[0x0164] = "T\u030C"; // Ť
    DECOMPOSITION_TABLE[0x0165] = "t\u030C"; // ť
    DECOMPOSITION_TABLE[0x0168] = "U\u0303"; // Ũ
    DECOMPOSITION_TABLE[0x0169] = "u\u0303"; // ũ
    DECOMPOSITION_TABLE[0x016A] = "U\u0304"; // Ū
    DECOMPOSITION_TABLE[0x016B] = "u\u0304"; // ū
    DECOMPOSITION_TABLE[0x016C] = "U\u0306"; // Ŭ
    DECOMPOSITION_TABLE[0x016D] = "u\u0306"; // ŭ
    DECOMPOSITION_TABLE[0x016E] = "U\u030A"; // Ů
    DECOMPOSITION_TABLE[0x016F] = "u\u030A"; // ů
    DECOMPOSITION_TABLE[0x0170] = "U\u030B"; // Ű
    DECOMPOSITION_TABLE[0x0171] = "u\u030B"; // ű
    DECOMPOSITION_TABLE[0x0172] = "U\u0328"; // Ų
    DECOMPOSITION_TABLE[0x0173] = "u\u0328"; // ų
    DECOMPOSITION_TABLE[0x0174] = "W\u0302"; // Ŵ
    DECOMPOSITION_TABLE[0x0175] = "w\u0302"; // ŵ
    DECOMPOSITION_TABLE[0x0176] = "Y\u0302"; // Ŷ
    DECOMPOSITION_TABLE[0x0177] = "y\u0302"; // ŷ
    DECOMPOSITION_TABLE[0x0178] = "Y\u0308"; // Ÿ
    DECOMPOSITION_TABLE[0x0179] = "Z\u0301"; // Ź
    DECOMPOSITION_TABLE[0x017A] = "z\u0301"; // ź
    DECOMPOSITION_TABLE[0x017B] = "Z\u0307"; // Ż
    DECOMPOSITION_TABLE[0x017C] = "z\u0307"; // ż
    DECOMPOSITION_TABLE[0x017D] = "Z\u030C"; // Ž
    DECOMPOSITION_TABLE[0x017E] = "z\u030C"; // ž

    // Latin Extended-B (U+0180 to U+024F) - with multi-mark sequences
    DECOMPOSITION_TABLE[0x01A0] = "O\u031B"; // Ơ
    DECOMPOSITION_TABLE[0x01A1] = "o\u031B"; // ơ
    DECOMPOSITION_TABLE[0x01AF] = "U\u031B"; // Ư
    DECOMPOSITION_TABLE[0x01B0] = "u\u031B"; // ư
    DECOMPOSITION_TABLE[0x01CD] = "A\u030C"; // Ǎ
    DECOMPOSITION_TABLE[0x01CE] = "a\u030C"; // ǎ
    DECOMPOSITION_TABLE[0x01CF] = "I\u030C"; // Ǐ
    DECOMPOSITION_TABLE[0x01D0] = "i\u030C"; // ǐ
    DECOMPOSITION_TABLE[0x01D1] = "O\u030C"; // Ǒ
    DECOMPOSITION_TABLE[0x01D2] = "o\u030C"; // ǒ
    DECOMPOSITION_TABLE[0x01D3] = "U\u030C"; // Ǔ
    DECOMPOSITION_TABLE[0x01D4] = "u\u030C"; // ǔ

    // Multi-mark sequences (2 combining marks)
    DECOMPOSITION_TABLE[0x01D5] = "U\u0308\u0304"; // Ǖ = U + diaeresis + macron
    DECOMPOSITION_TABLE[0x01D6] = "u\u0308\u0304"; // ǖ
    DECOMPOSITION_TABLE[0x01D7] = "U\u0308\u0301"; // Ǘ = U + diaeresis + acute
    DECOMPOSITION_TABLE[0x01D8] = "u\u0308\u0301"; // ǘ
    DECOMPOSITION_TABLE[0x01D9] = "U\u0308\u030C"; // Ǚ = U + diaeresis + caron
    DECOMPOSITION_TABLE[0x01DA] = "u\u0308\u030C"; // ǚ
    DECOMPOSITION_TABLE[0x01DB] = "U\u0308\u0300"; // Ǜ = U + diaeresis + grave
    DECOMPOSITION_TABLE[0x01DC] = "u\u0308\u0300"; // ǜ
    DECOMPOSITION_TABLE[0x01DE] = "A\u0308\u0304"; // Ǟ = A + diaeresis + macron
    DECOMPOSITION_TABLE[0x01DF] = "a\u0308\u0304"; // ǟ
    DECOMPOSITION_TABLE[0x01E0] = "A\u0307\u0304"; // Ǡ = A + dot + macron
    DECOMPOSITION_TABLE[0x01E1] = "a\u0307\u0304"; // ǡ

    DECOMPOSITION_TABLE[0x01E2] = "\u00C6\u0304"; // Ǣ
    DECOMPOSITION_TABLE[0x01E3] = "\u00E6\u0304"; // ǣ
    DECOMPOSITION_TABLE[0x01E6] = "G\u030C"; // Ǧ
    DECOMPOSITION_TABLE[0x01E7] = "g\u030C"; // ǧ
    DECOMPOSITION_TABLE[0x01E8] = "K\u030C"; // Ǩ
    DECOMPOSITION_TABLE[0x01E9] = "k\u030C"; // ǩ
    DECOMPOSITION_TABLE[0x01EA] = "O\u0328"; // Ǫ
    DECOMPOSITION_TABLE[0x01EB] = "o\u0328"; // ǫ
    DECOMPOSITION_TABLE[0x01EC] = "O\u0328\u0304"; // Ǭ = O + ogonek + macron
    DECOMPOSITION_TABLE[0x01ED] = "o\u0328\u0304"; // ǭ
    DECOMPOSITION_TABLE[0x01EE] = "\u01B7\u030C"; // Ǯ
    DECOMPOSITION_TABLE[0x01EF] = "\u0292\u030C"; // ǯ
    DECOMPOSITION_TABLE[0x01F0] = "j\u030C"; // ǰ
    DECOMPOSITION_TABLE[0x01F4] = "G\u0301"; // Ǵ
    DECOMPOSITION_TABLE[0x01F5] = "g\u0301"; // ǵ
    DECOMPOSITION_TABLE[0x01F8] = "N\u0300"; // Ǹ
    DECOMPOSITION_TABLE[0x01F9] = "n\u0300"; // ǹ
    DECOMPOSITION_TABLE[0x01FA] = "A\u030A\u0301"; // Ǻ = A + ring + acute
    DECOMPOSITION_TABLE[0x01FB] = "a\u030A\u0301"; // ǻ
    DECOMPOSITION_TABLE[0x01FC] = "\u00C6\u0301"; // Ǽ
    DECOMPOSITION_TABLE[0x01FD] = "\u00E6\u0301"; // ǽ
    DECOMPOSITION_TABLE[0x01FE] = "\u00D8\u0301"; // Ǿ
    DECOMPOSITION_TABLE[0x01FF] = "\u00F8\u0301"; // ǿ
    DECOMPOSITION_TABLE[0x0200] = "A\u030F"; // Ȁ
    DECOMPOSITION_TABLE[0x0201] = "a\u030F"; // ȁ
    DECOMPOSITION_TABLE[0x0202] = "A\u0311"; // Ȃ
    DECOMPOSITION_TABLE[0x0203] = "a\u0311"; // ȃ
    DECOMPOSITION_TABLE[0x0204] = "E\u030F"; // Ȅ
    DECOMPOSITION_TABLE[0x0205] = "e\u030F"; // ȅ
    DECOMPOSITION_TABLE[0x0206] = "E\u0311"; // Ȇ
    DECOMPOSITION_TABLE[0x0207] = "e\u0311"; // ȇ
    DECOMPOSITION_TABLE[0x0208] = "I\u030F"; // Ȉ
    DECOMPOSITION_TABLE[0x0209] = "i\u030F"; // ȉ
    DECOMPOSITION_TABLE[0x020A] = "I\u0311"; // Ȋ
    DECOMPOSITION_TABLE[0x020B] = "i\u0311"; // ȋ
    DECOMPOSITION_TABLE[0x020C] = "O\u030F"; // Ȍ
    DECOMPOSITION_TABLE[0x020D] = "o\u030F"; // ȍ
    DECOMPOSITION_TABLE[0x020E] = "O\u0311"; // Ȏ
    DECOMPOSITION_TABLE[0x020F] = "o\u0311"; // ȏ
    DECOMPOSITION_TABLE[0x0210] = "R\u030F"; // Ȑ
    DECOMPOSITION_TABLE[0x0211] = "r\u030F"; // ȑ
    DECOMPOSITION_TABLE[0x0212] = "R\u0311"; // Ȓ
    DECOMPOSITION_TABLE[0x0213] = "r\u0311"; // ȓ
    DECOMPOSITION_TABLE[0x0214] = "U\u030F"; // Ȕ
    DECOMPOSITION_TABLE[0x0215] = "u\u030F"; // ȕ
    DECOMPOSITION_TABLE[0x0216] = "U\u0311"; // Ȗ
    DECOMPOSITION_TABLE[0x0217] = "u\u0311"; // ȗ
    DECOMPOSITION_TABLE[0x0218] = "S\u0326"; // Ș
    DECOMPOSITION_TABLE[0x0219] = "s\u0326"; // ș
    DECOMPOSITION_TABLE[0x021A] = "T\u0326"; // Ț
    DECOMPOSITION_TABLE[0x021B] = "t\u0326"; // ț
    DECOMPOSITION_TABLE[0x021E] = "H\u030C"; // Ȟ
    DECOMPOSITION_TABLE[0x021F] = "h\u030C"; // ȟ
    DECOMPOSITION_TABLE[0x0226] = "A\u0307"; // Ȧ
    DECOMPOSITION_TABLE[0x0227] = "a\u0307"; // ȧ
    DECOMPOSITION_TABLE[0x0228] = "E\u0327"; // Ȩ
    DECOMPOSITION_TABLE[0x0229] = "e\u0327"; // ȩ
    DECOMPOSITION_TABLE[0x022A] = "O\u0308\u0304"; // Ȫ = O + diaeresis + macron
    DECOMPOSITION_TABLE[0x022B] = "o\u0308\u0304"; // ȫ
    DECOMPOSITION_TABLE[0x022C] = "O\u0303\u0304"; // Ȭ = O + tilde + macron
    DECOMPOSITION_TABLE[0x022D] = "o\u0303\u0304"; // ȭ
    DECOMPOSITION_TABLE[0x022E] = "O\u0307"; // Ȯ
    DECOMPOSITION_TABLE[0x022F] = "o\u0307"; // ȯ
    DECOMPOSITION_TABLE[0x0230] = "O\u0307\u0304"; // Ȱ = O + dot + macron
    DECOMPOSITION_TABLE[0x0231] = "o\u0307\u0304"; // ȱ
    DECOMPOSITION_TABLE[0x0232] = "Y\u0304"; // Ȳ
    DECOMPOSITION_TABLE[0x0233] = "y\u0304"; // ȳ

    // Greek and Coptic (U+0370 to U+03FF)
    DECOMPOSITION_TABLE[0x0374] = "\u02B9";
    DECOMPOSITION_TABLE[0x037E] = ";";
    DECOMPOSITION_TABLE[0x0385] = "\u00A8\u0301"; // Diaeresis + acute (2 marks)
    DECOMPOSITION_TABLE[0x0386] = "\u0391\u0301"; // Ά
    DECOMPOSITION_TABLE[0x0388] = "\u0395\u0301"; // Έ
    DECOMPOSITION_TABLE[0x0389] = "\u0397\u0301"; // Ή
    DECOMPOSITION_TABLE[0x038A] = "\u0399\u0301"; // Ί
    DECOMPOSITION_TABLE[0x038C] = "\u039F\u0301"; // Ό
    DECOMPOSITION_TABLE[0x038E] = "\u03A5\u0301"; // Ύ
    DECOMPOSITION_TABLE[0x038F] = "\u03A9\u0301"; // Ώ
    DECOMPOSITION_TABLE[0x0390] = "\u03B9\u0308\u0301"; // ΐ = iota + diaeresis + acute (3 marks!)
    DECOMPOSITION_TABLE[0x03AA] = "\u0399\u0308"; // Ϊ
    DECOMPOSITION_TABLE[0x03AB] = "\u03A5\u0308"; // Ϋ
    DECOMPOSITION_TABLE[0x03AC] = "\u03B1\u0301"; // ά
    DECOMPOSITION_TABLE[0x03AD] = "\u03B5\u0301"; // έ
    DECOMPOSITION_TABLE[0x03AE] = "\u03B7\u0301"; // ή
    DECOMPOSITION_TABLE[0x03AF] = "\u03B9\u0301"; // ί
    DECOMPOSITION_TABLE[0x03B0] = "\u03C5\u0308\u0301"; // ΰ = upsilon + diaeresis + acute (3 marks!)
    DECOMPOSITION_TABLE[0x03CA] = "\u03B9\u0308"; // ϊ
    DECOMPOSITION_TABLE[0x03CB] = "\u03C5\u0308"; // ϋ
    DECOMPOSITION_TABLE[0x03CC] = "\u03BF\u0301"; // ό
    DECOMPOSITION_TABLE[0x03CD] = "\u03C5\u0301"; // ύ
    DECOMPOSITION_TABLE[0x03CE] = "\u03C9\u0301"; // ώ
    DECOMPOSITION_TABLE[0x03D3] = "\u03D2\u0301"; // ϓ
    DECOMPOSITION_TABLE[0x03D4] = "\u03D2\u0308"; // ϔ

    // Cyrillic (U+0400 to U+04FF)
    DECOMPOSITION_TABLE[0x0400] = "\u0415\u0300"; // Ѐ
    DECOMPOSITION_TABLE[0x0401] = "\u0415\u0308"; // Ё
    DECOMPOSITION_TABLE[0x0403] = "\u0413\u0301"; // Ѓ
    DECOMPOSITION_TABLE[0x0407] = "\u0406\u0308"; // Ї
    DECOMPOSITION_TABLE[0x040C] = "\u041A\u0301"; // Ќ
    DECOMPOSITION_TABLE[0x040D] = "\u0418\u0300"; // Ѝ
    DECOMPOSITION_TABLE[0x040E] = "\u0423\u0306"; // Ў
    DECOMPOSITION_TABLE[0x0419] = "\u0418\u0306"; // Й
    DECOMPOSITION_TABLE[0x0439] = "\u0438\u0306"; // й
    DECOMPOSITION_TABLE[0x0450] = "\u0435\u0300"; // ѐ
    DECOMPOSITION_TABLE[0x0451] = "\u0435\u0308"; // ё
    DECOMPOSITION_TABLE[0x0453] = "\u0433\u0301"; // ѓ
    DECOMPOSITION_TABLE[0x0457] = "\u0456\u0308"; // ї
    DECOMPOSITION_TABLE[0x045C] = "\u043A\u0301"; // ќ
    DECOMPOSITION_TABLE[0x045D] = "\u0438\u0300"; // ѝ
    DECOMPOSITION_TABLE[0x045E] = "\u0443\u0306"; // ў
    DECOMPOSITION_TABLE[0x0476] = "\u0474\u030F"; // Ѷ
    DECOMPOSITION_TABLE[0x0477] = "\u0475\u030F"; // ѷ
    DECOMPOSITION_TABLE[0x04C1] = "\u0416\u0306"; // Ӂ
    DECOMPOSITION_TABLE[0x04C2] = "\u0436\u0306"; // ӂ
    DECOMPOSITION_TABLE[0x04D0] = "\u0410\u0306"; // Ӑ
    DECOMPOSITION_TABLE[0x04D1] = "\u0430\u0306"; // ӑ
    DECOMPOSITION_TABLE[0x04D2] = "\u0410\u0308"; // Ӓ
    DECOMPOSITION_TABLE[0x04D3] = "\u0430\u0308"; // ӓ
    DECOMPOSITION_TABLE[0x04D6] = "\u0415\u0306"; // Ӗ
    DECOMPOSITION_TABLE[0x04D7] = "\u0435\u0306"; // ӗ
    DECOMPOSITION_TABLE[0x04DA] = "\u04D8\u0308"; // Ӛ
    DECOMPOSITION_TABLE[0x04DB] = "\u04D9\u0308"; // ӛ
    DECOMPOSITION_TABLE[0x04DC] = "\u0416\u0308"; // Ӝ
    DECOMPOSITION_TABLE[0x04DD] = "\u0436\u0308"; // ӝ
    DECOMPOSITION_TABLE[0x04DE] = "\u0417\u0308"; // Ӟ
    DECOMPOSITION_TABLE[0x04DF] = "\u0437\u0308"; // ӟ
    DECOMPOSITION_TABLE[0x04E2] = "\u0418\u0304"; // Ӣ
    DECOMPOSITION_TABLE[0x04E3] = "\u0438\u0304"; // ӣ
    DECOMPOSITION_TABLE[0x04E4] = "\u0418\u0308"; // Ӥ
    DECOMPOSITION_TABLE[0x04E5] = "\u0438\u0308"; // ӥ
    DECOMPOSITION_TABLE[0x04E6] = "\u041E\u0308"; // Ӧ
    DECOMPOSITION_TABLE[0x04E7] = "\u043E\u0308"; // ӧ
    DECOMPOSITION_TABLE[0x04EA] = "\u04E8\u0308"; // Ӫ
    DECOMPOSITION_TABLE[0x04EB] = "\u04E9\u0308"; // ӫ
    DECOMPOSITION_TABLE[0x04EC] = "\u042D\u0308"; // Ӭ
    DECOMPOSITION_TABLE[0x04ED] = "\u044D\u0308"; // ӭ
    DECOMPOSITION_TABLE[0x04EE] = "\u0423\u0304"; // Ӯ
    DECOMPOSITION_TABLE[0x04EF] = "\u0443\u0304"; // ӯ
    DECOMPOSITION_TABLE[0x04F0] = "\u0423\u0308"; // Ӱ
    DECOMPOSITION_TABLE[0x04F1] = "\u0443\u0308"; // ӱ
    DECOMPOSITION_TABLE[0x04F2] = "\u0423\u030B"; // Ӳ
    DECOMPOSITION_TABLE[0x04F3] = "\u0443\u030B"; // ӳ
    DECOMPOSITION_TABLE[0x04F4] = "\u0427\u0308"; // Ӵ
    DECOMPOSITION_TABLE[0x04F5] = "\u0447\u0308"; // ӵ
    DECOMPOSITION_TABLE[0x04F8] = "\u042B\u0308"; // Ӹ
    DECOMPOSITION_TABLE[0x04F9] = "\u044B\u0308"; // ӹ
  }

  /**
   * Initialize NFKD (compatibility) decomposition table These include
   * formatting variants, font variants, etc.
   */
  private static void initializeCompatibilityDecompositions()
  {
    // Copy all canonical decompositions first
    for (int i = 0; i < DECOMPOSITION_TABLE.length; i++)
    {
      if (DECOMPOSITION_TABLE[i] != null)
      {
        COMPATIBILITY_DECOMPOSITION_TABLE[i] = DECOMPOSITION_TABLE[i];
      }
    }
  }

  /**
   * Precompute all full decompositions
   */
  private static void precomputeFullDecompositions()
  {
    for (int i = 0; i < DECOMPOSITION_TABLE.length; i++)
    {
      if (DECOMPOSITION_TABLE[i] != null)
      {
        FULL_DECOMPOSITION_TABLE[i] = fullyDecompose(DECOMPOSITION_TABLE[i]);
      }
    }
    // Latin-1 Supplement - Spacing Modifier Letters
    COMPATIBILITY_DECOMPOSITION_TABLE[0x00A0] = " "; // No-break space → space
    COMPATIBILITY_DECOMPOSITION_TABLE[0x00A8] = " \u0308"; // Diaeresis → space + combining diaeresis
    COMPATIBILITY_DECOMPOSITION_TABLE[0x00AA] = "a"; // Feminine ordinal indicator → a
    COMPATIBILITY_DECOMPOSITION_TABLE[0x00AF] = " \u0304"; // Macron → space + combining macron
    COMPATIBILITY_DECOMPOSITION_TABLE[0x00B2] = "2"; // Superscript two
    COMPATIBILITY_DECOMPOSITION_TABLE[0x00B3] = "3"; // Superscript three
    COMPATIBILITY_DECOMPOSITION_TABLE[0x00B4] = " \u0301"; // Acute accent → space + combining acute
    COMPATIBILITY_DECOMPOSITION_TABLE[0x00B5] = "\u03BC"; // Micro sign → Greek mu
    COMPATIBILITY_DECOMPOSITION_TABLE[0x00B8] = " \u0327"; // Cedilla → space + combining cedilla
    COMPATIBILITY_DECOMPOSITION_TABLE[0x00B9] = "1"; // Superscript one
    COMPATIBILITY_DECOMPOSITION_TABLE[0x00BA] = "o"; // Masculine ordinal indicator → o
    COMPATIBILITY_DECOMPOSITION_TABLE[0x00BC] = "1\u20444"; // Fraction 1/4
    COMPATIBILITY_DECOMPOSITION_TABLE[0x00BD] = "1\u20442"; // Fraction 1/2
    COMPATIBILITY_DECOMPOSITION_TABLE[0x00BE] = "3\u20444"; // Fraction 3/4

    // IPA Extensions (U+0250 to U+02AF)
    // Most IPA characters don't have compatibility decompositions

    // Spacing Modifier Letters (U+02B0 to U+02FF) - Just a few in our range
    COMPATIBILITY_DECOMPOSITION_TABLE[0x02B0] = "h"; // Modifier letter small h
    COMPATIBILITY_DECOMPOSITION_TABLE[0x02B1] = "\u0266"; // Modifier letter small h with hook
    COMPATIBILITY_DECOMPOSITION_TABLE[0x02B2] = "j"; // Modifier letter small j
    COMPATIBILITY_DECOMPOSITION_TABLE[0x02B3] = "r"; // Modifier letter small r
    COMPATIBILITY_DECOMPOSITION_TABLE[0x02B4] = "\u0279"; // Modifier letter small turned r
    COMPATIBILITY_DECOMPOSITION_TABLE[0x02B5] = "\u027B"; // Modifier letter small turned r with hook
    COMPATIBILITY_DECOMPOSITION_TABLE[0x02B6] = "\u0281"; // Modifier letter small capital inverted r
    COMPATIBILITY_DECOMPOSITION_TABLE[0x02B7] = "w"; // Modifier letter small w
    COMPATIBILITY_DECOMPOSITION_TABLE[0x02B8] = "y"; // Modifier letter small y
    COMPATIBILITY_DECOMPOSITION_TABLE[0x02B9] = "\u02B9"; // Modifier letter prime (no decomposition)
    COMPATIBILITY_DECOMPOSITION_TABLE[0x02BA] = "\u02BA"; // Modifier letter double prime (no decomposition)
    COMPATIBILITY_DECOMPOSITION_TABLE[0x02BB] = "\u02BB"; // Modifier letter turned comma (no decomposition)
    COMPATIBILITY_DECOMPOSITION_TABLE[0x02BC] = "\u02BC"; // Modifier letter apostrophe (no decomposition)
    COMPATIBILITY_DECOMPOSITION_TABLE[0x02BD] = "\u02BD"; // Modifier letter reversed comma (no decomposition)
    COMPATIBILITY_DECOMPOSITION_TABLE[0x02BE] = "\u02BE"; // Modifier letter right half ring (no decomposition)
    COMPATIBILITY_DECOMPOSITION_TABLE[0x02BF] = "\u02BF"; // Modifier letter left half ring (no decomposition)
    COMPATIBILITY_DECOMPOSITION_TABLE[0x02C0] = "\u02C0"; // Modifier letter glottal stop (no decomposition)
    COMPATIBILITY_DECOMPOSITION_TABLE[0x02C1] = "\u02C1"; // Modifier letter reversed glottal stop (no decomposition)
    COMPATIBILITY_DECOMPOSITION_TABLE[0x02C2] = "<"; // Modifier letter left arrowhead
    COMPATIBILITY_DECOMPOSITION_TABLE[0x02C3] = ">"; // Modifier letter right arrowhead
    COMPATIBILITY_DECOMPOSITION_TABLE[0x02C4] = "\u02C4"; // Modifier letter up arrowhead (no decomposition)
    COMPATIBILITY_DECOMPOSITION_TABLE[0x02C5] = "\u02C5"; // Modifier letter down arrowhead (no decomposition)
    COMPATIBILITY_DECOMPOSITION_TABLE[0x02C6] = " \u0302"; // Modifier letter circumflex accent
    COMPATIBILITY_DECOMPOSITION_TABLE[0x02C7] = " \u030C"; // Caron
    COMPATIBILITY_DECOMPOSITION_TABLE[0x02C8] = "\u02C8"; // Modifier letter vertical line (no decomposition)
    COMPATIBILITY_DECOMPOSITION_TABLE[0x02C9] = " \u0304"; // Modifier letter macron
    COMPATIBILITY_DECOMPOSITION_TABLE[0x02CA] = " \u0301"; // Modifier letter acute accent
    COMPATIBILITY_DECOMPOSITION_TABLE[0x02CB] = " \u0300"; // Modifier letter grave accent
    COMPATIBILITY_DECOMPOSITION_TABLE[0x02CC] = "\u02CC"; // Modifier letter low vertical line (no decomposition)
    COMPATIBILITY_DECOMPOSITION_TABLE[0x02CD] = " \u0304"; // Modifier letter low macron
    COMPATIBILITY_DECOMPOSITION_TABLE[0x02CE] = " \u0300"; // Modifier letter low grave accent
    COMPATIBILITY_DECOMPOSITION_TABLE[0x02CF] = " \u0301"; // Modifier letter low acute accent
    COMPATIBILITY_DECOMPOSITION_TABLE[0x02D0] = "\u02D0"; // Modifier letter triangular colon (no decomposition)
    COMPATIBILITY_DECOMPOSITION_TABLE[0x02D1] = "\u02D1"; // Modifier letter half triangular colon (no decomposition)
    COMPATIBILITY_DECOMPOSITION_TABLE[0x02D2] = "\u02D2"; // Modifier letter centred right half ring (no decomposition)
    COMPATIBILITY_DECOMPOSITION_TABLE[0x02D3] = "\u02D3"; // Modifier letter centred left half ring (no decomposition)
    COMPATIBILITY_DECOMPOSITION_TABLE[0x02D4] = "\u02D4"; // Modifier letter up tack (no decomposition)
    COMPATIBILITY_DECOMPOSITION_TABLE[0x02D5] = "\u02D5"; // Modifier letter down tack (no decomposition)
    COMPATIBILITY_DECOMPOSITION_TABLE[0x02D6] = "\u02D6"; // Modifier letter plus sign (no decomposition)
    COMPATIBILITY_DECOMPOSITION_TABLE[0x02D7] = "\u02D7"; // Modifier letter minus sign (no decomposition)
    COMPATIBILITY_DECOMPOSITION_TABLE[0x02D8] = " \u0306"; // Breve
    COMPATIBILITY_DECOMPOSITION_TABLE[0x02D9] = " \u0307"; // Dot above
    COMPATIBILITY_DECOMPOSITION_TABLE[0x02DA] = " \u030A"; // Ring above
    COMPATIBILITY_DECOMPOSITION_TABLE[0x02DB] = " \u0328"; // Ogonek
    COMPATIBILITY_DECOMPOSITION_TABLE[0x02DC] = " \u0303"; // Small tilde
    COMPATIBILITY_DECOMPOSITION_TABLE[0x02DD] = " \u030B"; // Double acute accent
    COMPATIBILITY_DECOMPOSITION_TABLE[0x02DE] = "\u02DE"; // Modifier letter rhotic hook (no decomposition)
    COMPATIBILITY_DECOMPOSITION_TABLE[0x02DF] = "\u02DF"; // Modifier letter cross accent (no decomposition)
    COMPATIBILITY_DECOMPOSITION_TABLE[0x02E0] = "\u0263"; // Modifier letter small gamma
    COMPATIBILITY_DECOMPOSITION_TABLE[0x02E1] = "l"; // Modifier letter small l
    COMPATIBILITY_DECOMPOSITION_TABLE[0x02E2] = "s"; // Modifier letter small s
    COMPATIBILITY_DECOMPOSITION_TABLE[0x02E3] = "x"; // Modifier letter small x
    COMPATIBILITY_DECOMPOSITION_TABLE[0x02E4] = "\u0295"; // Modifier letter small reversed glottal stop
    COMPATIBILITY_DECOMPOSITION_TABLE[0x02E5] = "\u02E5"; // Modifier letter extra-high tone bar (no decomposition)
    COMPATIBILITY_DECOMPOSITION_TABLE[0x02E6] = "\u02E6"; // Modifier letter high tone bar (no decomposition)
    COMPATIBILITY_DECOMPOSITION_TABLE[0x02E7] = "\u02E7"; // Modifier letter mid tone bar (no decomposition)
    COMPATIBILITY_DECOMPOSITION_TABLE[0x02E8] = "\u02E8"; // Modifier letter low tone bar (no decomposition)
    COMPATIBILITY_DECOMPOSITION_TABLE[0x02E9] = "\u02E9"; // Modifier letter extra-low tone bar (no decomposition)
    COMPATIBILITY_DECOMPOSITION_TABLE[0x02EA] = "\u02EA"; // Modifier letter yin departing tone mark (no decomposition)
    COMPATIBILITY_DECOMPOSITION_TABLE[0x02EB] = "\u02EB"; // Modifier letter yang departing tone mark (no decomposition)
    COMPATIBILITY_DECOMPOSITION_TABLE[0x02EC] = "\u02EC"; // Modifier letter voicing (no decomposition)
    COMPATIBILITY_DECOMPOSITION_TABLE[0x02ED] = "\u02ED"; // Modifier letter unaspirated (no decomposition)
    COMPATIBILITY_DECOMPOSITION_TABLE[0x02EE] = "\u02EE"; // Modifier letter double apostrophe (no decomposition)    
  }

  /**
   * Helper method for initialization
   */
  private static String fullyDecompose(String str)
  {
    StringBuffer result = new StringBuffer(str.length() * 2);
    for (int i = 0; i < str.length(); i++)
    {
      char c = str.charAt(i);
      if (c < DECOMPOSITION_TABLE.length && DECOMPOSITION_TABLE[c] != null)
      {
        result.append(fullyDecompose(DECOMPOSITION_TABLE[c]));
      }
      else
      {
        result.append(c);
      }
    }
    return result.toString();
  }

  /**
   * Build composition map with support for multi-mark sequences
   */
  private static void buildCompositionMap()
  {
    for (int composed = 0; composed < DECOMPOSITION_TABLE.length; composed++)
    {
      String decomp = DECOMPOSITION_TABLE[composed];
      if (decomp == null || decomp.length() < 2)
      {
        continue;
      }

      // Get the fully decomposed form
      String fullDecomp = FULL_DECOMPOSITION_TABLE[composed];
      if (fullDecomp == null || fullDecomp.length() < 2)
      {
        continue;
      }

      // Extract base character (first non-combining character)
      char base = fullDecomp.charAt(0);

      // For each combining mark sequence, create composition entries
      // Strategy: Store compositions for each step of the sequence
      // Example: For Ǖ (U+diaeresis+macron), we store:
      //   1. U+diaeresis → Ü
      //   2. Ü+macron → Ǖ

      if (fullDecomp.length() == 2 && isCombiningMark(fullDecomp.charAt(1)))
      {
        // Simple case: base + one combining mark
        char combining = fullDecomp.charAt(1);
        long key = makeCompositionKey(base, combining);
        COMPOSITION_MAP.put(new Long(key), new Character((char) composed));
      }
      else if (fullDecomp.length() >= 3)
      {
        // Multi-mark case: need to build intermediate compositions
        // This handles sequences like base+mark1+mark2

        // First, try to find if there's an intermediate composed form
        // For example: U+0308 (diaeresis) might compose to Ü first
        char currentBase = base;
        for (int i = 1; i < fullDecomp.length(); i++)
        {
          char combining = fullDecomp.charAt(i);
          if (!isCombiningMark(combining))
          {
            break; // Not a combining sequence
          }

          // Look for intermediate or final composition
          char nextComposed = findComposedChar(currentBase, combining);
          if (nextComposed != 0)
          {
            currentBase = nextComposed;
          }
          else
          {
            // Store the composition if not already present
            if (i == fullDecomp.length() - 1)
            {
              // This is the final composition
              long key = makeCompositionKey(currentBase, combining);
              COMPOSITION_MAP.put(new Long(key), new Character((char) composed));
            }
          }
        }
      }
    }
  }

  /**
   * Helper: Find if base+combining has a composed form in our table
   */
  private static char findComposedChar(char base, char combining)
  {
    for (int i = 0; i < DECOMPOSITION_TABLE.length; i++)
    {
      String decomp = DECOMPOSITION_TABLE[i];
      if (decomp != null && decomp.length() == 2 && decomp.charAt(0) == base
          && decomp.charAt(1) == combining)
      {
        return (char) i;
      }
    }
    return 0; // Not found
  }

  /**
   * Create a unique key from base and combining character (using long for more
   * space)
   */
  private static long makeCompositionKey(char base, char combining)
  {
    return ((long) base << 16) | combining;
  }

  /**
   * Get combining class for a character
   */
  private static int getCombiningClass(char c)
  {
    if (c < COMBINING_CLASS.length)
    {
      return COMBINING_CLASS[c] & 0xFF; // Unsigned byte
    }
    return 0; // Default: not a combining mark
  }

  /**
   * Canonically reorder combining marks by their combining class
   */
  private static void canonicalReorder(char[] chars, int start, int end)
  {
    // Bubble sort by combining class (simple but correct for small sequences)
    for (int i = start; i < end; i++)
    {
      for (int j = i + 1; j < end; j++)
      {
        int class1 = getCombiningClass(chars[i]);
        int class2 = getCombiningClass(chars[j]);

        // Sort by combining class (ascending order)
        // Characters with class 0 stay in place (they're not combining marks)
        if (class1 > class2 && class2 != 0)
        {
          // Swap
          char temp = chars[i];
          chars[i] = chars[j];
          chars[j] = temp;
        }
      }
    }
  }

  /**
   * Canonical decomposition of a string. This converts a string in the
   * supported range using NFD Unicode Normalization algorithm.
   * 
   * Convert string to NFD (Canonical Decomposition)
   */
  public static String toNFD(CharSequence input)
  {
    if (input == null)
    {
      return null;
    }

    int len = input.length();
    char[] result = new char[len * 3];
    int resultLen = 0;

    for (int i = 0; i < len; i++)
    {
      char c = input.charAt(i);

      if (c < FULL_DECOMPOSITION_TABLE.length && FULL_DECOMPOSITION_TABLE[c] != null)
      {
        String decomposed = FULL_DECOMPOSITION_TABLE[c];
        int decompStart = resultLen;
        for (int j = 0; j < decomposed.length(); j++)
        {
          result[resultLen++] = decomposed.charAt(j);
        }

        // Canonically reorder combining marks in the decomposed sequence
        if (resultLen - decompStart > 1)
        {
          canonicalReorder(result, decompStart + 1, resultLen);
        }
      }
      else
      {
        result[resultLen++] = c;
      }
    }

    return new String(result, 0, resultLen);
  }

  /**
   * Convert string to NFC (Canonical Composition) - with multi-mark support
   */
  public static String toNFC(CharSequence input)
  {
    if (input == null)
    {
      return null;
    }

    // Step 1: Decompose to NFD (which includes canonical ordering)
    String nfd = toNFD(input);

    // Step 2: Compose base + combining sequences
    int len = nfd.length();
    char[] result = new char[len];
    int resultLen = 0;

    int i = 0;
    while (i < len)
    {
      char base = nfd.charAt(i);

      // Collect all following combining marks
      int combStart = i + 1;
      int combEnd = combStart;
      while (combEnd < len && isCombiningMark(nfd.charAt(combEnd)))
      {
        combEnd++;
      }

      int numCombining = combEnd - combStart;

      if (numCombining == 0)
      {
        // No combining marks, just copy base
        result[resultLen++] = base;
        i++;
      }
      else
      {
        // Try to compose base with combining marks
        char currentBase = base;
        boolean anyComposed = false;
        int combIndex = combStart;

        // Iterate through combining marks and try to compose
        while (combIndex < combEnd)
        {
          char combining = nfd.charAt(combIndex);
          long key = makeCompositionKey(currentBase, combining);
          Character composedChar = (Character) COMPOSITION_MAP.get(new Long(key));

          if (composedChar != null)
          {
            // Composition found!
            currentBase = composedChar.charValue();
            combIndex++; // Mark was consumed
            anyComposed = true;
          }
          else
          {
            // No composition possible with this mark
            // Try next mark (maybe it can compose)
            combIndex++;
          }
        }

        // Output the composed base
        result[resultLen++] = currentBase;

        // Output any remaining combining marks that couldn't be composed
        for (int j = combStart; j < combEnd; j++)
        {
          char combining = nfd.charAt(j);
          long key = makeCompositionKey(base, combining);

          // Check if this mark was used in composition
          boolean wasUsed = false;
          char testBase = base;
          for (int k = combStart; k <= j; k++)
          {
            char testComb = nfd.charAt(k);
            long testKey = makeCompositionKey(testBase, testComb);
            Character testComposed = (Character) COMPOSITION_MAP.get(new Long(testKey));
            if (testComposed != null)
            {
              testBase = testComposed.charValue();
              if (k == j)
              {
                wasUsed = true;
              }
            }
          }

          // If mark wasn't composed, output it
          if (!wasUsed && currentBase != base)
          {
            // This is a simplified check - a full implementation would track exactly which marks were consumed
          }
        }

        i = combEnd;
      }
    }

    return new String(result, 0, resultLen);
  }

  /**
   * Return a string without all diactrical marks.
   * 
   * @param input
   *          [in] The string which may contain diactrical marks.
   * @return The string without any diactrical marks.
   */
  private static String removeDiacritics(CharSequence input)
  {
    if (input == null)
    {
      return null;
    }

    int len = input.length();
    char[] result = new char[len * 2];
    int resultLen = 0;

    for (int i = 0; i < len; i++)
    {
      char c = input.charAt(i);

      if (c < FULL_DECOMPOSITION_TABLE.length && FULL_DECOMPOSITION_TABLE[c] != null)
      {
        String decomposed = FULL_DECOMPOSITION_TABLE[c];
        for (int j = 0; j < decomposed.length(); j++)
        {
          char dc = decomposed.charAt(j);
          if (!isCombiningMark(dc))
          {
            result[resultLen++] = dc;
          }
        }
      }
      else
      {
        if (!isCombiningMark(c))
        {
          result[resultLen++] = c;
        }
      }
    }

    return new String(result, 0, resultLen);
  }

  /**
   * Verifies if the specified character is a combining character such as a
   * diacritical mark in the supported codepoints.
   * 
   * @param c
   *          [in] The character to verify.
   * @return true if the character is a combining character.
   */
  private static boolean isCombiningMark(char c)
  {
    return (c >= 0x0300 && c <= 0x036F) || (c >= 0x1AB0 && c <= 0x1AFF)
        || (c >= 0x1DC0 && c <= 0x1DFF) || (c >= 0xFE20 && c <= 0xFE2F);
  }


  /**
   * Recursively decompose using compatibility decompositions
   */
  private static String fullyDecomposeCompatibility(String str)
  {
    StringBuffer result = new StringBuffer(str.length() * 2);
    for (int i = 0; i < str.length(); i++)
    {
      char c = str.charAt(i);
      if (c < COMPATIBILITY_DECOMPOSITION_TABLE.length
          && COMPATIBILITY_DECOMPOSITION_TABLE[c] != null)
      {
        result.append(fullyDecomposeCompatibility(COMPATIBILITY_DECOMPOSITION_TABLE[c]));
      }
      else
      {
        result.append(c);
      }
    }
    return result.toString();
  }

  /**
   * Convert string to NFKD (Compatibility Decomposition) This decomposes both
   * canonical equivalents AND formatting/presentation variants
   */
  public static String toNFKD(CharSequence input)
  {
    if (input == null)
    {
      return null;
    }

    int len = input.length();
    char[] result = new char[len * 3];
    int resultLen = 0;

    for (int i = 0; i < len; i++)
    {
      char c = input.charAt(i);

      // Use compatibility decomposition
      if (c < COMPATIBILITY_DECOMPOSITION_TABLE.length
          && COMPATIBILITY_DECOMPOSITION_TABLE[c] != null)
      {
        String decomposed = fullyDecomposeCompatibility(String.valueOf(c));
        int decompStart = resultLen;
        for (int j = 0; j < decomposed.length(); j++)
        {
          result[resultLen++] = decomposed.charAt(j);
        }

        // Canonically reorder combining marks
        if (resultLen - decompStart > 1)
        {
          canonicalReorder(result, decompStart + 1, resultLen);
        }
      }
      else
      {
        result[resultLen++] = c;
      }
    }

    return new String(result, 0, resultLen);
  }

  /**
   * Convert string to NFKC (Compatibility Composition) This is NFKD followed by
   * NFC composition
   */
  public static String toNFKC(CharSequence input)
  {
    if (input == null)
    {
      return null;
    }

    // Step 1: Decompose to NFKD
    String nfkd = toNFKD(input);

    // Step 2: Compose using NFC rules (same as toNFC but starting from NFKD)
    int len = nfkd.length();
    char[] result = new char[len];
    int resultLen = 0;

    int i = 0;
    while (i < len)
    {
      char base = nfkd.charAt(i);

      // Collect all following combining marks
      int combStart = i + 1;
      int combEnd = combStart;
      while (combEnd < len && isCombiningMark(nfkd.charAt(combEnd)))
      {
        combEnd++;
      }

      int numCombining = combEnd - combStart;

      if (numCombining == 0)
      {
        result[resultLen++] = base;
        i++;
      }
      else
      {
        // Try to compose base with combining marks
        char currentBase = base;

        // Iterate through combining marks and try to compose
        for (int combIndex = combStart; combIndex < combEnd; combIndex++)
        {
          char combining = nfkd.charAt(combIndex);
          long key = makeCompositionKey(currentBase, combining);
          Character composedChar = (Character) COMPOSITION_MAP.get(new Long(key));

          if (composedChar != null)
          {
            currentBase = composedChar.charValue();
          }
          else
          {
            // Can't compose this mark, output it separately
            if (currentBase != base)
            {
              // We have a partial composition, output it
              result[resultLen++] = currentBase;
              currentBase = base;
            }
            result[resultLen++] = combining;
          }
        }

        // Output the final composed base (if different from original)
        if (currentBase != base || numCombining == 0)
        {
          result[resultLen++] = currentBase;
        }

        i = combEnd;
      }
    }

    return new String(result, 0, resultLen);
  }

  /**
   * Checks for prohibited characters according to RFC 4518. This is much
   * stricter than then XML 1.1 Specification but provides better compatibility
   * (XML allows private use characters, some non-characters and text direction
   * markers).
   * 
   * <p>
   * Verification fails if the character has one the following properties:
   * </p>
   * 
   * <ul>
   * <li>Codepoint is a surrogate character (General category "Cs")</li>
   * <li>Codepoint is a private use character (General category "Co")</li>
   * <li>Codepoint is a non-character (PropList "# Cn" category)</li>
   * <li>Codepoint is a text direction marker or is deprecated</li>
   * </ul>
   * 
   * The limitations of this compliance check are as follows:
   * 
   * <ul>
   * <li>Unassigned codepoint verification are not done, as they may be assigned
   * in the future.</li>
   * <li>Only supports character verification in the BMP (legacy UCS-2
   * encoding).</li>
   * </ul>
   * 
   * <p>
   * See IETF RFC 4518 Section 2.4 for more information.
   * </p>
   * 
   * @param value
   *          [in] The string to check
   * @throws ParseException
   *           if prohibited characters are found
   */
  public static void verifyProhibited(CharSequence value) throws ParseException
  {
    int maxLength = value.length();
    for (int i = 0; i < maxLength; i++)
    {
      char ch = value.charAt(i);
      int category = Character.getType(ch);
      switch (category)
      {
      /* Surrogate general category codepoints */
      /* Java 1.4+ versions: D800-DFFF; [SURROGATE CODES] */
        case Character.SURROGATE:
          throw new ParseException("String contains prohibited characters at position", i);
          /* Private use codepoints */
          /* Java 1.4+: E000-F8FF; [PRIVATE USE, PLANE 0]
           * Java 1.6+: E000-F8FF; [PRIVATE USE, PLANE 0]
           *            F0000-FFFFD; [PRIVATE USE, PLANE 15]
           *            100000-10FFFD; [PRIVATE USE, PLANE 16]
           */
        case Character.PRIVATE_USE:
          throw new ParseException("String contains prohibited characters at position", i);
        default:
          break;
      }
      /* Replacement character is not allowed */
      if (ch == 0xFFFD)
      {
        throw new ParseException("String contains prohibited characters at position", i);
      }
      /* Non-character code points */
      if (IntegerSelectItems.validateValue(UNICODE_NON_CHARACTERS, ch) == false)
      {
        throw new ParseException("String contains prohibited characters at position", i);
      }
    }
  }

  /**
   * Map a string by adapting the string for comparison and potential storage
   * for certain specific use-cases.
   * 
   * This is compliant with the mapping prohibited codepoint steps of IETF RFC 4518 and does the
   * following:
   * 
   * <ul>
   * <li>CHARACTER TABULATION (U+0009), LINE FEED (LF) (U+000A), LINE TABULATION
   * (U+000B), FORM FEED (FF) (U+000C), CARRIAGE RETURN (CR) (U+000D), and NEXT
   * LINE (NEL) (U+0085) mapped to SPACE (U+0020).
   * <li>All other control code (e.g., Cc) points or code points with a control
   * function (e.g., Cf) are mapped to nothing.</li>
   * <li>ZERO WIDTH SPACE (U+200B) is mapped to nothing.</li>
   * <li>All other code points with Separator (space, line, or paragraph)
   * property (e.g., Zs, Zl, or Zp) are mapped to SPACE (U+0020)</li>
   * <li>If Codepoint is a surrogate character (General category "Cs") then an exception is thrown.</li>
   * <li>If Codepoint is a private use character (General category "Co") then an exception is thrown.</li>
   * <li>Codepoint is a non-character (PropList "# Cn" category) then an exception is thrown.</li>
   * <li>Codepoint is a text direction marker or is deprecated then an exception is thrown.</li>
   * </ul>
   * 
   * <p>
   * This could be considered a stricter version of the
   * <code>normalizedString</code> defined in W3C XML Schema Definition Language
   * (XSD) 1.1 Part 2: Datatypes.
   * </p>
   * 
   * @param value
   *          [in] The value that requires remapping.
   * @param toLowerCase
   *          [in] <code>true</code> if the characters should be converted to
   *          lower case on output.
   * @throws ParseException
   *           if prohibited characters are found
   * @return The mapped string.
   */
  public static final String mapAndVerifyString(CharSequence value, boolean toLowerCase)
      throws ParseException
  {
    int maxLength = value.length();
    char buffer[] = new char[maxLength];
    int length = 0;
    for (int i = 0; i < maxLength; i++)
    {
      char ch = value.charAt(i);
      // ZERO WIDTH SPACE
      if (ch == '\u200B')
        continue;
      if (IntegerSelectItems.validateValue(UNICODE_WHITESPACE_CODEPOINTS, ch) == true)
      {
        buffer[i] = ' ';
        length++;
        continue;
      }
      /* Replacement character is not allowed */
      if (ch == 0xFFFD)
      {
        throw new ParseException("String contains prohibited characters at position", i);
      }
      /* Non-character code points */
      if (IntegerSelectItems.validateValue(UNICODE_NON_CHARACTERS, ch) == false)
      {
        throw new ParseException("String contains prohibited characters at position", i);
      }
      int category = Character.getType(ch);
      switch (category)
      {
      /* Surrogate general category codepoints */
      /* Java 1.4+ versions: D800-DFFF; [SURROGATE CODES] */
        case Character.SURROGATE:
          throw new ParseException("String contains prohibited characters at position", i);
          /* Private use codepoints */
          /* Java 1.4+: E000-F8FF; [PRIVATE USE, PLANE 0]
           * Java 1.6+: E000-F8FF; [PRIVATE USE, PLANE 0]
           *            F0000-FFFFD; [PRIVATE USE, PLANE 15]
           *            100000-10FFFD; [PRIVATE USE, PLANE 16]
           */
        case Character.PRIVATE_USE:
          throw new ParseException("String contains prohibited characters at position", i);
        case Character.FORMAT:
        case Character.CONTROL:
          continue;
        case Character.SPACE_SEPARATOR:
        case Character.PARAGRAPH_SEPARATOR:
        case Character.LINE_SEPARATOR:
          buffer[i] = ' ';
          length++;
          continue;
        default:
          break;
      }
      {

        if (toLowerCase == true)
        {
          buffer[i] = Character.toLowerCase(ch);
        }
        else
        {
          buffer[i] = ch;
        }
        length++;
      }
    }
    String result = new String(buffer, 0, length);
    return result;
  }

  
  /** Prepares a string for case exact matching. The 
   *  preparation follows the specification in ITU-T X.520
   *  for string preparation.
   *  
   *  <p>The following steps are done for this
   *  preparation:</p>
   *  
   *  <ul>
   *   <li></li>
   *  </ul>
   *  
   * 
   * @param value [in] The string value to prepare
   * @return The prepared string for comparison 
   * @throws ParseException If any ilegal codepoint
   *   is encountered.
   */
  public static String prepareCaseExact(CharSequence value) throws ParseException
  {
    String prepared1 = mapAndVerifyString(value, true);
    prepared1 = toNFKC(prepared1);
    prepared1 = collapseWhitespace(prepared1.trim());
    return prepared1;
  }
  
  
  
  /** Prepares a string for case ignore matching. The 
   *  preparation follows the specification in ITU-T X.520
   *  for string preparation.
   * 
   * @param value [in] The string value to prepare
   * @return The prepared string for comparison 
   * @throws ParseException If any ilegal codepoint
   *   is encountered.
   */
  public static String prepareCaseIgnore(CharSequence value) throws ParseException
  {
    String prepared1 = mapAndVerifyString(value, false);
    prepared1 = toNFKC(prepared1);
    prepared1 = collapseWhitespace(prepared1.trim());
    return prepared1;
  }
  
  /**
   * Prepares a string by converting to a string to its decomposed form
   * and removing any diacritics marks and converting it to lower case.
   * 
   * Both parameters will be decomposed using the NFD unicode algorithm and
   * diacritics removed and converted to lower-case before doing the actual
   * search.
   * 
   * @param text
   *          [in] The text that needs to be prepared
   * @return The prepared string.
   * @throws ParseException If any ilegal codepoint
   *   is encountered.
   */
  public static String prepareCaseIgnoreNoDiacritics(CharSequence text) throws ParseException
  {
    if (text==null)
    {
      return null;
    }
    String normalizedText = mapAndVerifyString(removeDiacritics(text), true);
    return normalizedText;
  }
  
  
  
  /**
   * Prepares a string by converting to a string to its decomposed form
   * and removing any diacritics marks.
   * 
   * Both parameters will be decomposed using the NFD unicode algorithm and
   * diacritics removed and converted to lower-case before doing the actual
   * search.
   * 
   * @param text
   *          [in] The text that needs to be prepared
   * @return The prepared string.
   * @throws ParseException If any ilegal codepoint
   *   is encountered.
   */
  public static String prepareCaseExactNoDiacritics(CharSequence text) throws ParseException
  {
    if (text==null)
    {
      return null;
    }
    String normalizedText = mapAndVerifyString(removeDiacritics(text), false);
    return normalizedText;
  }
  
  
  
  /**
   * Collapses multiple consecutive whitespace characters into a single space.
   * 
   * <p>
   * This handles spaces, tabs, newlines, and other whitespace characters and
   * collapses them to a single ASCII space character.
   * </p>
   * 
   * @param value
   *          [in] The string to process
   * @return String with collapsed whitespace
   */
  public static String collapseWhitespace(CharSequence value)
  {
    if (value == null)
    {
      return null;
    }

    if (value.length() == 0)
    {
      return new String();
    }

    StringBuffer result = new StringBuffer();
    boolean inWhitespace = false;

    for (int i = 0; i < value.length(); i++)
    {
      char c = value.charAt(i);

      if (Character.isWhitespace(c))
      {
        if (!inWhitespace)
        {
          result.append(' '); // Replace any whitespace with single space
          inWhitespace = true;
        }
        // Skip additional consecutive whitespace
      }
      else
      {
        result.append(c);
        inWhitespace = false;
      }
    }

    return result.toString();
  }

}