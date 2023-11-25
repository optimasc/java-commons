package com.optimasc.nio.charset;
/**
 * These are conversion tables to convert single-byte characters to UCS-2
 * characters. Currently the following tables are supported:
 *   CP437  (437)
 *   CP850  (850)
 *   Windows-1252 (1252)
 */
public final class CharSets
{

  /* We use the Private User Area to indicate a unmapped code point */
  public static final char UNKNOWN_CODEPOINT = (char)0xE000;
  /* Name: cp850_DOSLatin1 to Unicode table */
  /* Unicode version: 2.0 */
  /* Table version: 2.00 */
  /* Table format: Format A */
  /* Date: 04/24/96 */
  /* Authors: Lori Brownell <loribr@microsoft.com> */
  /* K.D. Chang <a-kchang@microsoft.com> */
  /* General notes: none */
  /*                                                                              */
  /* Format: Three tab-separated columns */
  /* Column #1 is the cp850_DOSLatin1 code (in hex) */
  /* Column #2 is the Unicode (in hex as (char)0xXXXX) */
  /* Column #3 is the Unicode name (follows a comment sign, '#') */
  /*                                                                              */
  /* The entries are in cp850_DOSLatin1 order */
  /*                                                                              */
  public static final char CodePage850ToUCS2[] = {
  /* 00 */(char) 0x0000,/* #NULL */
  /* 01 */(char) 0x0001,/* #START OF HEADING */
  /* 02 */(char) 0x0002,/* #START OF TEXT */
  /* 03 */(char) 0x0003,/* #END OF TEXT */
  /* 04 */(char) 0x0004,/* #END OF TRANSMISSION */
  /* 05 */(char) 0x0005,/* #ENQUIRY */
  /* 06 */(char) 0x0006,/* #ACKNOWLEDGE */
  /* 07 */(char) 0x0007,/* #BELL */
  /* 08 */(char) 0x0008,/* #BACKSPACE */
  /* 09 */(char) 0x0009,/* #HORIZONTAL TABULATION */
  /* 0A */(char) 0x000a,/* #LINE FEED */
  /* 0B */(char) 0x000b,/* #VERTICAL TABULATION */
  /* 0C */(char) 0x000c,/* #FORM FEED */
  /* 0D */(char) 0x000d,/* #CARRIAGE RETURN */
  /* 0E */(char) 0x000e,/* #SHIFT OUT */
  /* 0F */(char) 0x000f,/* #SHIFT IN */
  /* 10 */(char) 0x0010,/* #DATA LINK ESCAPE */
  /* 11 */(char) 0x0011,/* #DEVICE CONTROL ONE */
  /* 12 */(char) 0x0012,/* #DEVICE CONTROL TWO */
  /* 13 */(char) 0x0013,/* #DEVICE CONTROL THREE */
  /* 14 */(char) 0x0014,/* #DEVICE CONTROL FOUR */
  /* 15 */(char) 0x0015,/* #NEGATIVE ACKNOWLEDGE */
  /* 16 */(char) 0x0016,/* #SYNCHRONOUS IDLE */
  /* 17 */(char) 0x0017,/* #END OF TRANSMISSION BLOCK */
  /* 18 */(char) 0x0018,/* #CANCEL */
  /* 19 */(char) 0x0019,/* #END OF MEDIUM */
  /* 1A */(char) 0x001a,/* #SUBSTITUTE */
  /* 1B */(char) 0x001b,/* #ESCAPE */
  /* 1C */(char) 0x001c,/* #FILE SEPARATOR */
  /* 1D */(char) 0x001d,/* #GROUP SEPARATOR */
  /* 1E */(char) 0x001e,/* #RECORD SEPARATOR */
  /* 1F */(char) 0x001f,/* #UNIT SEPARATOR */
  /* 20 */(char) 0x0020,/* #SPACE */
  /* 21 */(char) 0x0021,/* #EXCLAMATION MARK */
  /* 22 */(char) 0x0022,/* #QUOTATION MARK */
  /* 23 */(char) 0x0023,/* #NUMBER SIGN */
  /* 24 */(char) 0x0024,/* #DOLLAR SIGN */
  /* 25 */(char) 0x0025,/* #PERCENT SIGN */
  /* 26 */(char) 0x0026,/* #AMPERSAND */
  /* 27 */(char) 0x0027,/* #APOSTROPHE */
  /* 28 */(char) 0x0028,/* #LEFT PARENTHESIS */
  /* 29 */(char) 0x0029,/* #RIGHT PARENTHESIS */
  /* 2A */(char) 0x002a,/* #ASTERISK */
  /* 2B */(char) 0x002b,/* #PLUS SIGN */
  /* 2C */(char) 0x002c,/* #COMMA */
  /* 2D */(char) 0x002d,/* #HYPHEN-MINUS */
  /* 2E */(char) 0x002e,/* #FULL STOP */
  /* 2F */(char) 0x002f,/* #SOLIDUS */
  /* 30 */(char) 0x0030,/* #DIGIT ZERO */
  /* 31 */(char) 0x0031,/* #DIGIT ONE */
  /* 32 */(char) 0x0032,/* #DIGIT TWO */
  /* 33 */(char) 0x0033,/* #DIGIT THREE */
  /* 34 */(char) 0x0034,/* #DIGIT FOUR */
  /* 35 */(char) 0x0035,/* #DIGIT FIVE */
  /* 36 */(char) 0x0036,/* #DIGIT SIX */
  /* 37 */(char) 0x0037,/* #DIGIT SEVEN */
  /* 38 */(char) 0x0038,/* #DIGIT EIGHT */
  /* 39 */(char) 0x0039,/* #DIGIT NINE */
  /* 3A */(char) 0x003a,/* #COLON */
  /* 3B */(char) 0x003b,/* #SEMICOLON */
  /* 3C */(char) 0x003c,/* #LESS-THAN SIGN */
  /* 3D */(char) 0x003d,/* #EQUALS SIGN */
  /* 3E */(char) 0x003e,/* #GREATER-THAN SIGN */
  /* 3F */(char) 0x003f,/* #QUESTION MARK */
  /* 40 */(char) 0x0040,/* #COMMERCIAL AT */
  /* 41 */(char) 0x0041,/* #LATIN CAPITAL LETTER A */
  /* 42 */(char) 0x0042,/* #LATIN CAPITAL LETTER B */
  /* 43 */(char) 0x0043,/* #LATIN CAPITAL LETTER C */
  /* 44 */(char) 0x0044,/* #LATIN CAPITAL LETTER D */
  /* 45 */(char) 0x0045,/* #LATIN CAPITAL LETTER E */
  /* 46 */(char) 0x0046,/* #LATIN CAPITAL LETTER F */
  /* 47 */(char) 0x0047,/* #LATIN CAPITAL LETTER G */
  /* 48 */(char) 0x0048,/* #LATIN CAPITAL LETTER H */
  /* 49 */(char) 0x0049,/* #LATIN CAPITAL LETTER I */
  /* 4A */(char) 0x004a,/* #LATIN CAPITAL LETTER J */
  /* 4B */(char) 0x004b,/* #LATIN CAPITAL LETTER K */
  /* 4C */(char) 0x004c,/* #LATIN CAPITAL LETTER L */
  /* 4D */(char) 0x004d,/* #LATIN CAPITAL LETTER M */
  /* 4E */(char) 0x004e,/* #LATIN CAPITAL LETTER N */
  /* 4F */(char) 0x004f,/* #LATIN CAPITAL LETTER O */
  /* 50 */(char) 0x0050,/* #LATIN CAPITAL LETTER P */
  /* 51 */(char) 0x0051,/* #LATIN CAPITAL LETTER Q */
  /* 52 */(char) 0x0052,/* #LATIN CAPITAL LETTER R */
  /* 53 */(char) 0x0053,/* #LATIN CAPITAL LETTER S */
  /* 54 */(char) 0x0054,/* #LATIN CAPITAL LETTER T */
  /* 55 */(char) 0x0055,/* #LATIN CAPITAL LETTER U */
  /* 56 */(char) 0x0056,/* #LATIN CAPITAL LETTER V */
  /* 57 */(char) 0x0057,/* #LATIN CAPITAL LETTER W */
  /* 58 */(char) 0x0058,/* #LATIN CAPITAL LETTER X */
  /* 59 */(char) 0x0059,/* #LATIN CAPITAL LETTER Y */
  /* 5A */(char) 0x005a,/* #LATIN CAPITAL LETTER Z */
  /* 5B */(char) 0x005b,/* #LEFT SQUARE BRACKET */
  /* 5C */(char) 0x005c,/* #REVERSE SOLIDUS */
  /* 5D */(char) 0x005d,/* #RIGHT SQUARE BRACKET */
  /* 5E */(char) 0x005e,/* #CIRCUMFLEX ACCENT */
  /* 5F */(char) 0x005f,/* #LOW LINE */
  /* 60 */(char) 0x0060,/* #GRAVE ACCENT */
  /* 61 */(char) 0x0061,/* #LATIN SMALL LETTER A */
  /* 62 */(char) 0x0062,/* #LATIN SMALL LETTER B */
  /* 63 */(char) 0x0063,/* #LATIN SMALL LETTER C */
  /* 64 */(char) 0x0064,/* #LATIN SMALL LETTER D */
  /* 65 */(char) 0x0065,/* #LATIN SMALL LETTER E */
  /* 66 */(char) 0x0066,/* #LATIN SMALL LETTER F */
  /* 67 */(char) 0x0067,/* #LATIN SMALL LETTER G */
  /* 68 */(char) 0x0068,/* #LATIN SMALL LETTER H */
  /* 69 */(char) 0x0069,/* #LATIN SMALL LETTER I */
  /* 6A */(char) 0x006a,/* #LATIN SMALL LETTER J */
  /* 6B */(char) 0x006b,/* #LATIN SMALL LETTER K */
  /* 6C */(char) 0x006c,/* #LATIN SMALL LETTER L */
  /* 6D */(char) 0x006d,/* #LATIN SMALL LETTER M */
  /* 6E */(char) 0x006e,/* #LATIN SMALL LETTER N */
  /* 6F */(char) 0x006f,/* #LATIN SMALL LETTER O */
  /* 70 */(char) 0x0070,/* #LATIN SMALL LETTER P */
  /* 71 */(char) 0x0071,/* #LATIN SMALL LETTER Q */
  /* 72 */(char) 0x0072,/* #LATIN SMALL LETTER R */
  /* 73 */(char) 0x0073,/* #LATIN SMALL LETTER S */
  /* 74 */(char) 0x0074,/* #LATIN SMALL LETTER T */
  /* 75 */(char) 0x0075,/* #LATIN SMALL LETTER U */
  /* 76 */(char) 0x0076,/* #LATIN SMALL LETTER V */
  /* 77 */(char) 0x0077,/* #LATIN SMALL LETTER W */
  /* 78 */(char) 0x0078,/* #LATIN SMALL LETTER X */
  /* 79 */(char) 0x0079,/* #LATIN SMALL LETTER Y */
  /* 7A */(char) 0x007a,/* #LATIN SMALL LETTER Z */
  /* 7B */(char) 0x007b,/* #LEFT CURLY BRACKET */
  /* 7C */(char) 0x007c,/* #VERTICAL LINE */
  /* 7D */(char) 0x007d,/* #RIGHT CURLY BRACKET */
  /* 7E */(char) 0x007e,/* #TILDE */
  /* 7F */(char) 0x007f,/* #DELETE */
  /* 80 */(char) 0x00c7,/* #LATIN CAPITAL LETTER C WITH CEDILLA */
  /* 81 */(char) 0x00fc,/* #LATIN SMALL LETTER U WITH DIAERESIS */
  /* 82 */(char) 0x00e9,/* #LATIN SMALL LETTER E WITH ACUTE */
  /* 83 */(char) 0x00e2,/* #LATIN SMALL LETTER A WITH CIRCUMFLEX */
  /* 84 */(char) 0x00e4,/* #LATIN SMALL LETTER A WITH DIAERESIS */
  /* 85 */(char) 0x00e0,/* #LATIN SMALL LETTER A WITH GRAVE */
  /* 86 */(char) 0x00e5,/* #LATIN SMALL LETTER A WITH RING ABOVE */
  /* 87 */(char) 0x00e7,/* #LATIN SMALL LETTER C WITH CEDILLA */
  /* 88 */(char) 0x00ea,/* #LATIN SMALL LETTER E WITH CIRCUMFLEX */
  /* 89 */(char) 0x00eb,/* #LATIN SMALL LETTER E WITH DIAERESIS */
  /* 8A */(char) 0x00e8,/* #LATIN SMALL LETTER E WITH GRAVE */
  /* 8B */(char) 0x00ef,/* #LATIN SMALL LETTER I WITH DIAERESIS */
  /* 8C */(char) 0x00ee,/* #LATIN SMALL LETTER I WITH CIRCUMFLEX */
  /* 8D */(char) 0x00ec,/* #LATIN SMALL LETTER I WITH GRAVE */
  /* 8E */(char) 0x00c4,/* #LATIN CAPITAL LETTER A WITH DIAERESIS */
  /* 8F */(char) 0x00c5,/* #LATIN CAPITAL LETTER A WITH RING ABOVE */
  /* 90 */(char) 0x00c9,/* #LATIN CAPITAL LETTER E WITH ACUTE */
  /* 91 */(char) 0x00e6,/* #LATIN SMALL LIGATURE AE */
  /* 92 */(char) 0x00c6,/* #LATIN CAPITAL LIGATURE AE */
  /* 93 */(char) 0x00f4,/* #LATIN SMALL LETTER O WITH CIRCUMFLEX */
  /* 94 */(char) 0x00f6,/* #LATIN SMALL LETTER O WITH DIAERESIS */
  /* 95 */(char) 0x00f2,/* #LATIN SMALL LETTER O WITH GRAVE */
  /* 96 */(char) 0x00fb,/* #LATIN SMALL LETTER U WITH CIRCUMFLEX */
  /* 97 */(char) 0x00f9,/* #LATIN SMALL LETTER U WITH GRAVE */
  /* 98 */(char) 0x00ff,/* #LATIN SMALL LETTER Y WITH DIAERESIS */
  /* 99 */(char) 0x00d6,/* #LATIN CAPITAL LETTER O WITH DIAERESIS */
  /* 9A */(char) 0x00dc,/* #LATIN CAPITAL LETTER U WITH DIAERESIS */
  /* 9B */(char) 0x00f8,/* #LATIN SMALL LETTER O WITH STROKE */
  /* 9C */(char) 0x00a3,/* #POUND SIGN */
  /* 9D */(char) 0x00d8,/* #LATIN CAPITAL LETTER O WITH STROKE */
  /* 9E */(char) 0x00d7,/* #MULTIPLICATION SIGN */
  /* 9F */(char) 0x0192,/* #LATIN SMALL LETTER F WITH HOOK */
  /* A0 */(char) 0x00e1,/* #LATIN SMALL LETTER A WITH ACUTE */
  /* A1 */(char) 0x00ed,/* #LATIN SMALL LETTER I WITH ACUTE */
  /* A2 */(char) 0x00f3,/* #LATIN SMALL LETTER O WITH ACUTE */
  /* A3 */(char) 0x00fa,/* #LATIN SMALL LETTER U WITH ACUTE */
  /* A4 */(char) 0x00f1,/* #LATIN SMALL LETTER N WITH TILDE */
  /* A5 */(char) 0x00d1,/* #LATIN CAPITAL LETTER N WITH TILDE */
  /* A6 */(char) 0x00aa,/* #FEMININE ORDINAL INDICATOR */
  /* A7 */(char) 0x00ba,/* #MASCULINE ORDINAL INDICATOR */
  /* A8 */(char) 0x00bf,/* #INVERTED QUESTION MARK */
  /* A9 */(char) 0x00ae,/* #REGISTERED SIGN */
  /* AA */(char) 0x00ac,/* #NOT SIGN */
  /* AB */(char) 0x00bd,/* #VULGAR FRACTION ONE HALF */
  /* AC */(char) 0x00bc,/* #VULGAR FRACTION ONE QUARTER */
  /* AD */(char) 0x00a1,/* #INVERTED EXCLAMATION MARK */
  /* AE */(char) 0x00ab,/* #LEFT-POINTING DOUBLE ANGLE QUOTATION MARK */
  /* AF */(char) 0x00bb,/* #RIGHT-POINTING DOUBLE ANGLE QUOTATION MARK */
  /* B0 */(char) 0x2591,/* #LIGHT SHADE */
  /* B1 */(char) 0x2592,/* #MEDIUM SHADE */
  /* B2 */(char) 0x2593,/* #DARK SHADE */
  /* B3 */(char) 0x2502,/* #BOX DRAWINGS LIGHT VERTICAL */
  /* B4 */(char) 0x2524,/* #BOX DRAWINGS LIGHT VERTICAL AND LEFT */
  /* B5 */(char) 0x00c1,/* #LATIN CAPITAL LETTER A WITH ACUTE */
  /* B6 */(char) 0x00c2,/* #LATIN CAPITAL LETTER A WITH CIRCUMFLEX */
  /* B7 */(char) 0x00c0,/* #LATIN CAPITAL LETTER A WITH GRAVE */
  /* B8 */(char) 0x00a9,/* #COPYRIGHT SIGN */
  /* B9 */(char) 0x2563,/* #BOX DRAWINGS DOUBLE VERTICAL AND LEFT */
  /* BA */(char) 0x2551,/* #BOX DRAWINGS DOUBLE VERTICAL */
  /* BB */(char) 0x2557,/* #BOX DRAWINGS DOUBLE DOWN AND LEFT */
  /* BC */(char) 0x255d,/* #BOX DRAWINGS DOUBLE UP AND LEFT */
  /* BD */(char) 0x00a2,/* #CENT SIGN */
  /* BE */(char) 0x00a5,/* #YEN SIGN */
  /* BF */(char) 0x2510,/* #BOX DRAWINGS LIGHT DOWN AND LEFT */
  /* C0 */(char) 0x2514,/* #BOX DRAWINGS LIGHT UP AND RIGHT */
  /* C1 */(char) 0x2534,/* #BOX DRAWINGS LIGHT UP AND HORIZONTAL */
  /* C2 */(char) 0x252c,/* #BOX DRAWINGS LIGHT DOWN AND HORIZONTAL */
  /* C3 */(char) 0x251c,/* #BOX DRAWINGS LIGHT VERTICAL AND RIGHT */
  /* C4 */(char) 0x2500,/* #BOX DRAWINGS LIGHT HORIZONTAL */
  /* C5 */(char) 0x253c,/* #BOX DRAWINGS LIGHT VERTICAL AND HORIZONTAL */
  /* C6 */(char) 0x00e3,/* #LATIN SMALL LETTER A WITH TILDE */
  /* C7 */(char) 0x00c3,/* #LATIN CAPITAL LETTER A WITH TILDE */
  /* C8 */(char) 0x255a,/* #BOX DRAWINGS DOUBLE UP AND RIGHT */
  /* C9 */(char) 0x2554,/* #BOX DRAWINGS DOUBLE DOWN AND RIGHT */
  /* CA */(char) 0x2569,/* #BOX DRAWINGS DOUBLE UP AND HORIZONTAL */
  /* CB */(char) 0x2566,/* #BOX DRAWINGS DOUBLE DOWN AND HORIZONTAL */
  /* CC */(char) 0x2560,/* #BOX DRAWINGS DOUBLE VERTICAL AND RIGHT */
  /* CD */(char) 0x2550,/* #BOX DRAWINGS DOUBLE HORIZONTAL */
  /* CE */(char) 0x256c,/* #BOX DRAWINGS DOUBLE VERTICAL AND HORIZONTAL */
  /* CF */(char) 0x00a4,/* #CURRENCY SIGN */
  /* D0 */(char) 0x00f0,/* #LATIN SMALL LETTER ETH */
  /* D1 */(char) 0x00d0,/* #LATIN CAPITAL LETTER ETH */
  /* D2 */(char) 0x00ca,/* #LATIN CAPITAL LETTER E WITH CIRCUMFLEX */
  /* D3 */(char) 0x00cb,/* #LATIN CAPITAL LETTER E WITH DIAERESIS */
  /* D4 */(char) 0x00c8,/* #LATIN CAPITAL LETTER E WITH GRAVE */
  /* D5 */(char) 0x0131,/* #LATIN SMALL LETTER DOTLESS I */
  /* D6 */(char) 0x00cd,/* #LATIN CAPITAL LETTER I WITH ACUTE */
  /* D7 */(char) 0x00ce,/* #LATIN CAPITAL LETTER I WITH CIRCUMFLEX */
  /* D8 */(char) 0x00cf,/* #LATIN CAPITAL LETTER I WITH DIAERESIS */
  /* D9 */(char) 0x2518,/* #BOX DRAWINGS LIGHT UP AND LEFT */
  /* DA */(char) 0x250c,/* #BOX DRAWINGS LIGHT DOWN AND RIGHT */
  /* DB */(char) 0x2588,/* #FULL BLOCK */
  /* DC */(char) 0x2584,/* #LOWER HALF BLOCK */
  /* DD */(char) 0x00a6,/* #BROKEN BAR */
  /* DE */(char) 0x00cc,/* #LATIN CAPITAL LETTER I WITH GRAVE */
  /* DF */(char) 0x2580,/* #UPPER HALF BLOCK */
  /* E0 */(char) 0x00d3,/* #LATIN CAPITAL LETTER O WITH ACUTE */
  /* E1 */(char) 0x00df,/* #LATIN SMALL LETTER SHARP S */
  /* E2 */(char) 0x00d4,/* #LATIN CAPITAL LETTER O WITH CIRCUMFLEX */
  /* E3 */(char) 0x00d2,/* #LATIN CAPITAL LETTER O WITH GRAVE */
  /* E4 */(char) 0x00f5,/* #LATIN SMALL LETTER O WITH TILDE */
  /* E5 */(char) 0x00d5,/* #LATIN CAPITAL LETTER O WITH TILDE */
  /* E6 */(char) 0x00b5,/* #MICRO SIGN */
  /* E7 */(char) 0x00fe,/* #LATIN SMALL LETTER THORN */
  /* E8 */(char) 0x00de,/* #LATIN CAPITAL LETTER THORN */
  /* E9 */(char) 0x00da,/* #LATIN CAPITAL LETTER U WITH ACUTE */
  /* EA */(char) 0x00db,/* #LATIN CAPITAL LETTER U WITH CIRCUMFLEX */
  /* EB */(char) 0x00d9,/* #LATIN CAPITAL LETTER U WITH GRAVE */
  /* EC */(char) 0x00fd,/* #LATIN SMALL LETTER Y WITH ACUTE */
  /* ED */(char) 0x00dd,/* #LATIN CAPITAL LETTER Y WITH ACUTE */
  /* EE */(char) 0x00af,/* #MACRON */
  /* EF */(char) 0x00b4,/* #ACUTE ACCENT */
  /* F0 */(char) 0x00ad,/* #SOFT HYPHEN */
  /* F1 */(char) 0x00b1,/* #PLUS-MINUS SIGN */
  /* F2 */(char) 0x2017,/* #DOUBLE LOW LINE */
  /* F3 */(char) 0x00be,/* #VULGAR FRACTION THREE QUARTERS */
  /* F4 */(char) 0x00b6,/* #PILCROW SIGN */
  /* F5 */(char) 0x00a7,/* #SECTION SIGN */
  /* F6 */(char) 0x00f7,/* #DIVISION SIGN */
  /* F7 */(char) 0x00b8,/* #CEDILLA */
  /* F8 */(char) 0x00b0,/* #DEGREE SIGN */
  /* F9 */(char) 0x00a8,/* #DIAERESIS */
  /* FA */(char) 0x00b7,/* #MIDDLE DOT */
  /* FB */(char) 0x00b9,/* #SUPERSCRIPT ONE */
  /* FC */(char) 0x00b3,/* #SUPERSCRIPT THREE */
  /* FD */(char) 0x00b2,/* #SUPERSCRIPT TWO */
  /* FE */(char) 0x25a0,/* #BLACK SQUARE */
  /* FF */(char) 0x00a0 /* #NO-BREAK SPACE */
  };

  /*                                                                              */
  /* Name: cp437_DOSLatinUS to Unicode table */
  /* Unicode version: 2.0 */
  /* Table version: 2.00 */
  /* Table format: Format A */
  /* Date: 04/24/96 */
  /* Authors: Lori Brownell <loribr@microsoft.com> */
  /* K.D. Chang <a-kchang@microsoft.com> */
  /* General notes: none */
  /*                                                                              */
  /* Format: Three tab-separated columns */
  /* Column #1 is the cp437_DOSLatinUS code (in hex) */
  /* Column #2 is the Unicode (in hex as XXXX) */
  /* Column #3 is the Unicode name (follows a comment sign, '#') */
  /*                                                                              */
  /* The entries are in cp437_DOSLatinUS order */
  /*                                                                              */
  public static final char CodePage437ToUCS2[] = {
  /* 00 */(char) 0x0000,/* #NULL */
  /* 01 */(char) 0x0001,/* #START OF HEADING */
  /* 02 */(char) 0x0002,/* #START OF TEXT */
  /* 03 */(char) 0x0003,/* #END OF TEXT */
  /* 04 */(char) 0x0004,/* #END OF TRANSMISSION */
  /* 05 */(char) 0x0005,/* #ENQUIRY */
  /* 06 */(char) 0x0006,/* #ACKNOWLEDGE */
  /* 07 */(char) 0x0007,/* #BELL */
  /* 08 */(char) 0x0008,/* #BACKSPACE */
  /* 09 */(char) 0x0009,/* #HORIZONTAL TABULATION */
  /* 0A */(char) 0x000a,/* #LINE FEED */
  /* 0B */(char) 0x000b,/* #VERTICAL TABULATION */
  /* 0C */(char) 0x000c,/* #FORM FEED */
  /* 0D */(char) 0x000d,/* #CARRIAGE RETURN */
  /* 0E */(char) 0x000e,/* #SHIFT OUT */
  /* 0F */(char) 0x000f,/* #SHIFT IN */
  /* 10 */(char) 0x0010,/* #DATA LINK ESCAPE */
  /* 11 */(char) 0x0011,/* #DEVICE CONTROL ONE */
  /* 12 */(char) 0x0012,/* #DEVICE CONTROL TWO */
  /* 13 */(char) 0x0013,/* #DEVICE CONTROL THREE */
  /* 14 */(char) 0x0014,/* #DEVICE CONTROL FOUR */
  /* 15 */(char) 0x0015,/* #NEGATIVE ACKNOWLEDGE */
  /* 16 */(char) 0x0016,/* #SYNCHRONOUS IDLE */
  /* 17 */(char) 0x0017,/* #END OF TRANSMISSION BLOCK */
  /* 18 */(char) 0x0018,/* #CANCEL */
  /* 19 */(char) 0x0019,/* #END OF MEDIUM */
  /* 1A */(char) 0x001a,/* #SUBSTITUTE */
  /* 1B */(char) 0x001b,/* #ESCAPE */
  /* 1C */(char) 0x001c,/* #FILE SEPARATOR */
  /* 1D */(char) 0x001d,/* #GROUP SEPARATOR */
  /* 1E */(char) 0x001e,/* #RECORD SEPARATOR */
  /* 1F */(char) 0x001f,/* #UNIT SEPARATOR */
  /* 20 */(char) 0x0020,/* #SPACE */
  /* 21 */(char) 0x0021,/* #EXCLAMATION MARK */
  /* 22 */(char) 0x0022,/* #QUOTATION MARK */
  /* 23 */(char) 0x0023,/* #NUMBER SIGN */
  /* 24 */(char) 0x0024,/* #DOLLAR SIGN */
  /* 25 */(char) 0x0025,/* #PERCENT SIGN */
  /* 26 */(char) 0x0026,/* #AMPERSAND */
  /* 27 */(char) 0x0027,/* #APOSTROPHE */
  /* 28 */(char) 0x0028,/* #LEFT PARENTHESIS */
  /* 29 */(char) 0x0029,/* #RIGHT PARENTHESIS */
  /* 2A */(char) 0x002a,/* #ASTERISK */
  /* 2B */(char) 0x002b,/* #PLUS SIGN */
  /* 2C */(char) 0x002c,/* #COMMA */
  /* 2D */(char) 0x002d,/* #HYPHEN-MINUS */
  /* 2E */(char) 0x002e,/* #FULL STOP */
  /* 2F */(char) 0x002f,/* #SOLIDUS */
  /* 30 */(char) 0x0030,/* #DIGIT ZERO */
  /* 31 */(char) 0x0031,/* #DIGIT ONE */
  /* 32 */(char) 0x0032,/* #DIGIT TWO */
  /* 33 */(char) 0x0033,/* #DIGIT THREE */
  /* 34 */(char) 0x0034,/* #DIGIT FOUR */
  /* 35 */(char) 0x0035,/* #DIGIT FIVE */
  /* 36 */(char) 0x0036,/* #DIGIT SIX */
  /* 37 */(char) 0x0037,/* #DIGIT SEVEN */
  /* 38 */(char) 0x0038,/* #DIGIT EIGHT */
  /* 39 */(char) 0x0039,/* #DIGIT NINE */
  /* 3A */(char) 0x003a,/* #COLON */
  /* 3B */(char) 0x003b,/* #SEMICOLON */
  /* 3C */(char) 0x003c,/* #LESS-THAN SIGN */
  /* 3D */(char) 0x003d,/* #EQUALS SIGN */
  /* 3E */(char) 0x003e,/* #GREATER-THAN SIGN */
  /* 3F */(char) 0x003f,/* #QUESTION MARK */
  /* 40 */(char) 0x0040,/* #COMMERCIAL AT */
  /* 41 */(char) 0x0041,/* #LATIN CAPITAL LETTER A */
  /* 42 */(char) 0x0042,/* #LATIN CAPITAL LETTER B */
  /* 43 */(char) 0x0043,/* #LATIN CAPITAL LETTER C */
  /* 44 */(char) 0x0044,/* #LATIN CAPITAL LETTER D */
  /* 45 */(char) 0x0045,/* #LATIN CAPITAL LETTER E */
  /* 46 */(char) 0x0046,/* #LATIN CAPITAL LETTER F */
  /* 47 */(char) 0x0047,/* #LATIN CAPITAL LETTER G */
  /* 48 */(char) 0x0048,/* #LATIN CAPITAL LETTER H */
  /* 49 */(char) 0x0049,/* #LATIN CAPITAL LETTER I */
  /* 4A */(char) 0x004a,/* #LATIN CAPITAL LETTER J */
  /* 4B */(char) 0x004b,/* #LATIN CAPITAL LETTER K */
  /* 4C */(char) 0x004c,/* #LATIN CAPITAL LETTER L */
  /* 4D */(char) 0x004d,/* #LATIN CAPITAL LETTER M */
  /* 4E */(char) 0x004e,/* #LATIN CAPITAL LETTER N */
  /* 4F */(char) 0x004f,/* #LATIN CAPITAL LETTER O */
  /* 50 */(char) 0x0050,/* #LATIN CAPITAL LETTER P */
  /* 51 */(char) 0x0051,/* #LATIN CAPITAL LETTER Q */
  /* 52 */(char) 0x0052,/* #LATIN CAPITAL LETTER R */
  /* 53 */(char) 0x0053,/* #LATIN CAPITAL LETTER S */
  /* 54 */(char) 0x0054,/* #LATIN CAPITAL LETTER T */
  /* 55 */(char) 0x0055,/* #LATIN CAPITAL LETTER U */
  /* 56 */(char) 0x0056,/* #LATIN CAPITAL LETTER V */
  /* 57 */(char) 0x0057,/* #LATIN CAPITAL LETTER W */
  /* 58 */(char) 0x0058,/* #LATIN CAPITAL LETTER X */
  /* 59 */(char) 0x0059,/* #LATIN CAPITAL LETTER Y */
  /* 5A */(char) 0x005a,/* #LATIN CAPITAL LETTER Z */
  /* 5B */(char) 0x005b,/* #LEFT SQUARE BRACKET */
  /* 5C */(char) 0x005c,/* #REVERSE SOLIDUS */
  /* 5D */(char) 0x005d,/* #RIGHT SQUARE BRACKET */
  /* 5E */(char) 0x005e,/* #CIRCUMFLEX ACCENT */
  /* 5F */(char) 0x005f,/* #LOW LINE */
  /* 60 */(char) 0x0060,/* #GRAVE ACCENT */
  /* 61 */(char) 0x0061,/* #LATIN SMALL LETTER A */
  /* 62 */(char) 0x0062,/* #LATIN SMALL LETTER B */
  /* 63 */(char) 0x0063,/* #LATIN SMALL LETTER C */
  /* 64 */(char) 0x0064,/* #LATIN SMALL LETTER D */
  /* 65 */(char) 0x0065,/* #LATIN SMALL LETTER E */
  /* 66 */(char) 0x0066,/* #LATIN SMALL LETTER F */
  /* 67 */(char) 0x0067,/* #LATIN SMALL LETTER G */
  /* 68 */(char) 0x0068,/* #LATIN SMALL LETTER H */
  /* 69 */(char) 0x0069,/* #LATIN SMALL LETTER I */
  /* 6A */(char) 0x006a,/* #LATIN SMALL LETTER J */
  /* 6B */(char) 0x006b,/* #LATIN SMALL LETTER K */
  /* 6C */(char) 0x006c,/* #LATIN SMALL LETTER L */
  /* 6D */(char) 0x006d,/* #LATIN SMALL LETTER M */
  /* 6E */(char) 0x006e,/* #LATIN SMALL LETTER N */
  /* 6F */(char) 0x006f,/* #LATIN SMALL LETTER O */
  /* 70 */(char) 0x0070,/* #LATIN SMALL LETTER P */
  /* 71 */(char) 0x0071,/* #LATIN SMALL LETTER Q */
  /* 72 */(char) 0x0072,/* #LATIN SMALL LETTER R */
  /* 73 */(char) 0x0073,/* #LATIN SMALL LETTER S */
  /* 74 */(char) 0x0074,/* #LATIN SMALL LETTER T */
  /* 75 */(char) 0x0075,/* #LATIN SMALL LETTER U */
  /* 76 */(char) 0x0076,/* #LATIN SMALL LETTER V */
  /* 77 */(char) 0x0077,/* #LATIN SMALL LETTER W */
  /* 78 */(char) 0x0078,/* #LATIN SMALL LETTER X */
  /* 79 */(char) 0x0079,/* #LATIN SMALL LETTER Y */
  /* 7A */(char) 0x007a,/* #LATIN SMALL LETTER Z */
  /* 7B */(char) 0x007b,/* #LEFT CURLY BRACKET */
  /* 7C */(char) 0x007c,/* #VERTICAL LINE */
  /* 7D */(char) 0x007d,/* #RIGHT CURLY BRACKET */
  /* 7E */(char) 0x007e,/* #TILDE */
  /* 7F */(char) 0x007f,/* #DELETE */
  /* 80 */(char) 0x00c7,/* #LATIN CAPITAL LETTER C WITH CEDILLA */
  /* 81 */(char) 0x00fc,/* #LATIN SMALL LETTER U WITH DIAERESIS */
  /* 82 */(char) 0x00e9,/* #LATIN SMALL LETTER E WITH ACUTE */
  /* 83 */(char) 0x00e2,/* #LATIN SMALL LETTER A WITH CIRCUMFLEX */
  /* 84 */(char) 0x00e4,/* #LATIN SMALL LETTER A WITH DIAERESIS */
  /* 85 */(char) 0x00e0,/* #LATIN SMALL LETTER A WITH GRAVE */
  /* 86 */(char) 0x00e5,/* #LATIN SMALL LETTER A WITH RING ABOVE */
  /* 87 */(char) 0x00e7,/* #LATIN SMALL LETTER C WITH CEDILLA */
  /* 88 */(char) 0x00ea,/* #LATIN SMALL LETTER E WITH CIRCUMFLEX */
  /* 89 */(char) 0x00eb,/* #LATIN SMALL LETTER E WITH DIAERESIS */
  /* 8A */(char) 0x00e8,/* #LATIN SMALL LETTER E WITH GRAVE */
  /* 8B */(char) 0x00ef,/* #LATIN SMALL LETTER I WITH DIAERESIS */
  /* 8C */(char) 0x00ee,/* #LATIN SMALL LETTER I WITH CIRCUMFLEX */
  /* 8D */(char) 0x00ec,/* #LATIN SMALL LETTER I WITH GRAVE */
  /* 8E */(char) 0x00c4,/* #LATIN CAPITAL LETTER A WITH DIAERESIS */
  /* 8F */(char) 0x00c5,/* #LATIN CAPITAL LETTER A WITH RING ABOVE */
  /* 90 */(char) 0x00c9,/* #LATIN CAPITAL LETTER E WITH ACUTE */
  /* 91 */(char) 0x00e6,/* #LATIN SMALL LIGATURE AE */
  /* 92 */(char) 0x00c6,/* #LATIN CAPITAL LIGATURE AE */
  /* 93 */(char) 0x00f4,/* #LATIN SMALL LETTER O WITH CIRCUMFLEX */
  /* 94 */(char) 0x00f6,/* #LATIN SMALL LETTER O WITH DIAERESIS */
  /* 95 */(char) 0x00f2,/* #LATIN SMALL LETTER O WITH GRAVE */
  /* 96 */(char) 0x00fb,/* #LATIN SMALL LETTER U WITH CIRCUMFLEX */
  /* 97 */(char) 0x00f9,/* #LATIN SMALL LETTER U WITH GRAVE */
  /* 98 */(char) 0x00ff,/* #LATIN SMALL LETTER Y WITH DIAERESIS */
  /* 99 */(char) 0x00d6,/* #LATIN CAPITAL LETTER O WITH DIAERESIS */
  /* 9A */(char) 0x00dc,/* #LATIN CAPITAL LETTER U WITH DIAERESIS */
  /* 9B */(char) 0x00a2,/* #CENT SIGN */
  /* 9C */(char) 0x00a3,/* #POUND SIGN */
  /* 9D */(char) 0x00a5,/* #YEN SIGN */
  /* 9E */(char) 0x20a7,/* #PESETA SIGN */
  /* 9F */(char) 0x0192,/* #LATIN SMALL LETTER F WITH HOOK */
  /* A0 */(char) 0x00e1,/* #LATIN SMALL LETTER A WITH ACUTE */
  /* A1 */(char) 0x00ed,/* #LATIN SMALL LETTER I WITH ACUTE */
  /* A2 */(char) 0x00f3,/* #LATIN SMALL LETTER O WITH ACUTE */
  /* A3 */(char) 0x00fa,/* #LATIN SMALL LETTER U WITH ACUTE */
  /* A4 */(char) 0x00f1,/* #LATIN SMALL LETTER N WITH TILDE */
  /* A5 */(char) 0x00d1,/* #LATIN CAPITAL LETTER N WITH TILDE */
  /* A6 */(char) 0x00aa,/* #FEMININE ORDINAL INDICATOR */
  /* A7 */(char) 0x00ba,/* #MASCULINE ORDINAL INDICATOR */
  /* A8 */(char) 0x00bf,/* #INVERTED QUESTION MARK */
  /* A9 */(char) 0x2310,/* #REVERSED NOT SIGN */
  /* AA */(char) 0x00ac,/* #NOT SIGN */
  /* AB */(char) 0x00bd,/* #VULGAR FRACTION ONE HALF */
  /* AC */(char) 0x00bc,/* #VULGAR FRACTION ONE QUARTER */
  /* AD */(char) 0x00a1,/* #INVERTED EXCLAMATION MARK */
  /* AE */(char) 0x00ab,/* #LEFT-POINTING DOUBLE ANGLE QUOTATION MARK */
  /* AF */(char) 0x00bb,/* #RIGHT-POINTING DOUBLE ANGLE QUOTATION MARK */
  /* B0 */(char) 0x2591,/* #LIGHT SHADE */
  /* B1 */(char) 0x2592,/* #MEDIUM SHADE */
  /* B2 */(char) 0x2593,/* #DARK SHADE */
  /* B3 */(char) 0x2502,/* #BOX DRAWINGS LIGHT VERTICAL */
  /* B4 */(char) 0x2524,/* #BOX DRAWINGS LIGHT VERTICAL AND LEFT */
  /* B5 */(char) 0x2561,/* #BOX DRAWINGS VERTICAL SINGLE AND LEFT DOUBLE */
  /* B6 */(char) 0x2562,/* #BOX DRAWINGS VERTICAL DOUBLE AND LEFT SINGLE */
  /* B7 */(char) 0x2556,/* #BOX DRAWINGS DOWN DOUBLE AND LEFT SINGLE */
  /* B8 */(char) 0x2555,/* #BOX DRAWINGS DOWN SINGLE AND LEFT DOUBLE */
  /* B9 */(char) 0x2563,/* #BOX DRAWINGS DOUBLE VERTICAL AND LEFT */
  /* BA */(char) 0x2551,/* #BOX DRAWINGS DOUBLE VERTICAL */
  /* BB */(char) 0x2557,/* #BOX DRAWINGS DOUBLE DOWN AND LEFT */
  /* BC */(char) 0x255d,/* #BOX DRAWINGS DOUBLE UP AND LEFT */
  /* BD */(char) 0x255c,/* #BOX DRAWINGS UP DOUBLE AND LEFT SINGLE */
  /* BE */(char) 0x255b,/* #BOX DRAWINGS UP SINGLE AND LEFT DOUBLE */
  /* BF */(char) 0x2510,/* #BOX DRAWINGS LIGHT DOWN AND LEFT */
  /* C0 */(char) 0x2514,/* #BOX DRAWINGS LIGHT UP AND RIGHT */
  /* C1 */(char) 0x2534,/* #BOX DRAWINGS LIGHT UP AND HORIZONTAL */
  /* C2 */(char) 0x252c,/* #BOX DRAWINGS LIGHT DOWN AND HORIZONTAL */
  /* C3 */(char) 0x251c,/* #BOX DRAWINGS LIGHT VERTICAL AND RIGHT */
  /* C4 */(char) 0x2500,/* #BOX DRAWINGS LIGHT HORIZONTAL */
  /* C5 */(char) 0x253c,/* #BOX DRAWINGS LIGHT VERTICAL AND HORIZONTAL */
  /* C6 */(char) 0x255e,/* #BOX DRAWINGS VERTICAL SINGLE AND RIGHT DOUBLE */
  /* C7 */(char) 0x255f,/* #BOX DRAWINGS VERTICAL DOUBLE AND RIGHT SINGLE */
  /* C8 */(char) 0x255a,/* #BOX DRAWINGS DOUBLE UP AND RIGHT */
  /* C9 */(char) 0x2554,/* #BOX DRAWINGS DOUBLE DOWN AND RIGHT */
  /* CA */(char) 0x2569,/* #BOX DRAWINGS DOUBLE UP AND HORIZONTAL */
  /* CB */(char) 0x2566,/* #BOX DRAWINGS DOUBLE DOWN AND HORIZONTAL */
  /* CC */(char) 0x2560,/* #BOX DRAWINGS DOUBLE VERTICAL AND RIGHT */
  /* CD */(char) 0x2550,/* #BOX DRAWINGS DOUBLE HORIZONTAL */
  /* CE */(char) 0x256c,/* #BOX DRAWINGS DOUBLE VERTICAL AND HORIZONTAL */
  /* CF */(char) 0x2567,/* #BOX DRAWINGS UP SINGLE AND HORIZONTAL DOUBLE */
  /* D0 */(char) 0x2568,/* #BOX DRAWINGS UP DOUBLE AND HORIZONTAL SINGLE */
  /* D1 */(char) 0x2564,/* #BOX DRAWINGS DOWN SINGLE AND HORIZONTAL DOUBLE */
  /* D2 */(char) 0x2565,/* #BOX DRAWINGS DOWN DOUBLE AND HORIZONTAL SINGLE */
  /* D3 */(char) 0x2559,/* #BOX DRAWINGS UP DOUBLE AND RIGHT SINGLE */
  /* D4 */(char) 0x2558,/* #BOX DRAWINGS UP SINGLE AND RIGHT DOUBLE */
  /* D5 */(char) 0x2552,/* #BOX DRAWINGS DOWN SINGLE AND RIGHT DOUBLE */
  /* D6 */(char) 0x2553,/* #BOX DRAWINGS DOWN DOUBLE AND RIGHT SINGLE */
  /* D7 */(char) 0x256b,/* #BOX DRAWINGS VERTICAL DOUBLE AND HORIZONTAL SINGLE */
  /* D8 */(char) 0x256a,/* #BOX DRAWINGS VERTICAL SINGLE AND HORIZONTAL DOUBLE */
  /* D9 */(char) 0x2518,/* #BOX DRAWINGS LIGHT UP AND LEFT */
  /* DA */(char) 0x250c,/* #BOX DRAWINGS LIGHT DOWN AND RIGHT */
  /* DB */(char) 0x2588,/* #FULL BLOCK */
  /* DC */(char) 0x2584,/* #LOWER HALF BLOCK */
  /* DD */(char) 0x258c,/* #LEFT HALF BLOCK */
  /* DE */(char) 0x2590,/* #RIGHT HALF BLOCK */
  /* DF */(char) 0x2580,/* #UPPER HALF BLOCK */
  /* E0 */(char) 0x03b1,/* #GREEK SMALL LETTER ALPHA */
  /* E1 */(char) 0x00df,/* #LATIN SMALL LETTER SHARP S */
  /* E2 */(char) 0x0393,/* #GREEK CAPITAL LETTER GAMMA */
  /* E3 */(char) 0x03c0,/* #GREEK SMALL LETTER PI */
  /* E4 */(char) 0x03a3,/* #GREEK CAPITAL LETTER SIGMA */
  /* E5 */(char) 0x03c3,/* #GREEK SMALL LETTER SIGMA */
  /* E6 */(char) 0x00b5,/* #MICRO SIGN */
  /* E7 */(char) 0x03c4,/* #GREEK SMALL LETTER TAU */
  /* E8 */(char) 0x03a6,/* #GREEK CAPITAL LETTER PHI */
  /* E9 */(char) 0x0398,/* #GREEK CAPITAL LETTER THETA */
  /* EA */(char) 0x03a9,/* #GREEK CAPITAL LETTER OMEGA */
  /* EB */(char) 0x03b4,/* #GREEK SMALL LETTER DELTA */
  /* EC */(char) 0x221e,/* #INFINITY */
  /* ED */(char) 0x03c6,/* #GREEK SMALL LETTER PHI */
  /* EE */(char) 0x03b5,/* #GREEK SMALL LETTER EPSILON */
  /* EF */(char) 0x2229,/* #INTERSECTION */
  /* F0 */(char) 0x2261,/* #IDENTICAL TO */
  /* F1 */(char) 0x00b1,/* #PLUS-MINUS SIGN */
  /* F2 */(char) 0x2265,/* #GREATER-THAN OR EQUAL TO */
  /* F3 */(char) 0x2264,/* #LESS-THAN OR EQUAL TO */
  /* F4 */(char) 0x2320,/* #TOP HALF INTEGRAL */
  /* F5 */(char) 0x2321,/* #BOTTOM HALF INTEGRAL */
  /* F6 */(char) 0x00f7,/* #DIVISION SIGN */
  /* F7 */(char) 0x2248,/* #ALMOST EQUAL TO */
  /* F8 */(char) 0x00b0,/* #DEGREE SIGN */
  /* F9 */(char) 0x2219,/* #BULLET OPERATOR */
  /* FA */(char) 0x00b7,/* #MIDDLE DOT */
  /* FB */(char) 0x221a,/* #SQUARE ROOT */
  /* FC */(char) 0x207f,/* #SUPERSCRIPT LATIN SMALL LETTER N */
  /* FD */(char) 0x00b2,/* #SUPERSCRIPT TWO */
  /* FE */(char) 0x25a0,/* #BLACK SQUARE */
  /* FF */(char) 0x00a0 /* #NO-BREAK SPACE */
  };

  /*                                                                              */
  /* Name: cp1252 to Unicode table */
  /* Unicode version: 2.0 */
  /* Table version: 2.01 */
  /* Table format: Format A */
  /* Date: 04/15/98 */
  /*                                                                              */
  /* Contact: cpxlate@microsoft.com */
  /*                                                                              */
  /* General notes: none */
  /*                                                                              */
  /* Format: Three tab-separated columns */
  /* Column #1 is the cp1252 code (in hex) */
  /* Column #2 is the Unicode (in hex as XXXX) */
  /* Column #3 is the Unicode name (follows a comment sign, '#') */
  /*                                                                              */
  /* The entries are in cp1252 order */
  /*                                                                              */
  public static final char CodePage1252ToUCS2[] = {
  /* 00 */(char) 0x0000,/* #NULL */
  /* 01 */(char) 0x0001,/* #START OF HEADING */
  /* 02 */(char) 0x0002,/* #START OF TEXT */
  /* 03 */(char) 0x0003,/* #END OF TEXT */
  /* 04 */(char) 0x0004,/* #END OF TRANSMISSION */
  /* 05 */(char) 0x0005,/* #ENQUIRY */
  /* 06 */(char) 0x0006,/* #ACKNOWLEDGE */
  /* 07 */(char) 0x0007,/* #BELL */
  /* 08 */(char) 0x0008,/* #BACKSPACE */
  /* 09 */(char) 0x0009,/* #HORIZONTAL TABULATION */
  /* 0A */(char) 0x000A,/* #LINE FEED */
  /* 0B */(char) 0x000B,/* #VERTICAL TABULATION */
  /* 0C */(char) 0x000C,/* #FORM FEED */
  /* 0D */(char) 0x000D,/* #CARRIAGE RETURN */
  /* 0E */(char) 0x000E,/* #SHIFT OUT */
  /* 0F */(char) 0x000F,/* #SHIFT IN */
  /* 10 */(char) 0x0010,/* #DATA LINK ESCAPE */
  /* 11 */(char) 0x0011,/* #DEVICE CONTROL ONE */
  /* 12 */(char) 0x0012,/* #DEVICE CONTROL TWO */
  /* 13 */(char) 0x0013,/* #DEVICE CONTROL THREE */
  /* 14 */(char) 0x0014,/* #DEVICE CONTROL FOUR */
  /* 15 */(char) 0x0015,/* #NEGATIVE ACKNOWLEDGE */
  /* 16 */(char) 0x0016,/* #SYNCHRONOUS IDLE */
  /* 17 */(char) 0x0017,/* #END OF TRANSMISSION BLOCK */
  /* 18 */(char) 0x0018,/* #CANCEL */
  /* 19 */(char) 0x0019,/* #END OF MEDIUM */
  /* 1A */(char) 0x001A,/* #SUBSTITUTE */
  /* 1B */(char) 0x001B,/* #ESCAPE */
  /* 1C */(char) 0x001C,/* #FILE SEPARATOR */
  /* 1D */(char) 0x001D,/* #GROUP SEPARATOR */
  /* 1E */(char) 0x001E,/* #RECORD SEPARATOR */
  /* 1F */(char) 0x001F,/* #UNIT SEPARATOR */
  /* 20 */(char) 0x0020,/* #SPACE */
  /* 21 */(char) 0x0021,/* #EXCLAMATION MARK */
  /* 22 */(char) 0x0022,/* #QUOTATION MARK */
  /* 23 */(char) 0x0023,/* #NUMBER SIGN */
  /* 24 */(char) 0x0024,/* #DOLLAR SIGN */
  /* 25 */(char) 0x0025,/* #PERCENT SIGN */
  /* 26 */(char) 0x0026,/* #AMPERSAND */
  /* 27 */(char) 0x0027,/* #APOSTROPHE */
  /* 28 */(char) 0x0028,/* #LEFT PARENTHESIS */
  /* 29 */(char) 0x0029,/* #RIGHT PARENTHESIS */
  /* 2A */(char) 0x002A,/* #ASTERISK */
  /* 2B */(char) 0x002B,/* #PLUS SIGN */
  /* 2C */(char) 0x002C,/* #COMMA */
  /* 2D */(char) 0x002D,/* #HYPHEN-MINUS */
  /* 2E */(char) 0x002E,/* #FULL STOP */
  /* 2F */(char) 0x002F,/* #SOLIDUS */
  /* 30 */(char) 0x0030,/* #DIGIT ZERO */
  /* 31 */(char) 0x0031,/* #DIGIT ONE */
  /* 32 */(char) 0x0032,/* #DIGIT TWO */
  /* 33 */(char) 0x0033,/* #DIGIT THREE */
  /* 34 */(char) 0x0034,/* #DIGIT FOUR */
  /* 35 */(char) 0x0035,/* #DIGIT FIVE */
  /* 36 */(char) 0x0036,/* #DIGIT SIX */
  /* 37 */(char) 0x0037,/* #DIGIT SEVEN */
  /* 38 */(char) 0x0038,/* #DIGIT EIGHT */
  /* 39 */(char) 0x0039,/* #DIGIT NINE */
  /* 3A */(char) 0x003A,/* #COLON */
  /* 3B */(char) 0x003B,/* #SEMICOLON */
  /* 3C */(char) 0x003C,/* #LESS-THAN SIGN */
  /* 3D */(char) 0x003D,/* #EQUALS SIGN */
  /* 3E */(char) 0x003E,/* #GREATER-THAN SIGN */
  /* 3F */(char) 0x003F,/* #QUESTION MARK */
  /* 40 */(char) 0x0040,/* #COMMERCIAL AT */
  /* 41 */(char) 0x0041,/* #LATIN CAPITAL LETTER A */
  /* 42 */(char) 0x0042,/* #LATIN CAPITAL LETTER B */
  /* 43 */(char) 0x0043,/* #LATIN CAPITAL LETTER C */
  /* 44 */(char) 0x0044,/* #LATIN CAPITAL LETTER D */
  /* 45 */(char) 0x0045,/* #LATIN CAPITAL LETTER E */
  /* 46 */(char) 0x0046,/* #LATIN CAPITAL LETTER F */
  /* 47 */(char) 0x0047,/* #LATIN CAPITAL LETTER G */
  /* 48 */(char) 0x0048,/* #LATIN CAPITAL LETTER H */
  /* 49 */(char) 0x0049,/* #LATIN CAPITAL LETTER I */
  /* 4A */(char) 0x004A,/* #LATIN CAPITAL LETTER J */
  /* 4B */(char) 0x004B,/* #LATIN CAPITAL LETTER K */
  /* 4C */(char) 0x004C,/* #LATIN CAPITAL LETTER L */
  /* 4D */(char) 0x004D,/* #LATIN CAPITAL LETTER M */
  /* 4E */(char) 0x004E,/* #LATIN CAPITAL LETTER N */
  /* 4F */(char) 0x004F,/* #LATIN CAPITAL LETTER O */
  /* 50 */(char) 0x0050,/* #LATIN CAPITAL LETTER P */
  /* 51 */(char) 0x0051,/* #LATIN CAPITAL LETTER Q */
  /* 52 */(char) 0x0052,/* #LATIN CAPITAL LETTER R */
  /* 53 */(char) 0x0053,/* #LATIN CAPITAL LETTER S */
  /* 54 */(char) 0x0054,/* #LATIN CAPITAL LETTER T */
  /* 55 */(char) 0x0055,/* #LATIN CAPITAL LETTER U */
  /* 56 */(char) 0x0056,/* #LATIN CAPITAL LETTER V */
  /* 57 */(char) 0x0057,/* #LATIN CAPITAL LETTER W */
  /* 58 */(char) 0x0058,/* #LATIN CAPITAL LETTER X */
  /* 59 */(char) 0x0059,/* #LATIN CAPITAL LETTER Y */
  /* 5A */(char) 0x005A,/* #LATIN CAPITAL LETTER Z */
  /* 5B */(char) 0x005B,/* #LEFT SQUARE BRACKET */
  /* 5C */(char) 0x005C,/* #REVERSE SOLIDUS */
  /* 5D */(char) 0x005D,/* #RIGHT SQUARE BRACKET */
  /* 5E */(char) 0x005E,/* #CIRCUMFLEX ACCENT */
  /* 5F */(char) 0x005F,/* #LOW LINE */
  /* 60 */(char) 0x0060,/* #GRAVE ACCENT */
  /* 61 */(char) 0x0061,/* #LATIN SMALL LETTER A */
  /* 62 */(char) 0x0062,/* #LATIN SMALL LETTER B */
  /* 63 */(char) 0x0063,/* #LATIN SMALL LETTER C */
  /* 64 */(char) 0x0064,/* #LATIN SMALL LETTER D */
  /* 65 */(char) 0x0065,/* #LATIN SMALL LETTER E */
  /* 66 */(char) 0x0066,/* #LATIN SMALL LETTER F */
  /* 67 */(char) 0x0067,/* #LATIN SMALL LETTER G */
  /* 68 */(char) 0x0068,/* #LATIN SMALL LETTER H */
  /* 69 */(char) 0x0069,/* #LATIN SMALL LETTER I */
  /* 6A */(char) 0x006A,/* #LATIN SMALL LETTER J */
  /* 6B */(char) 0x006B,/* #LATIN SMALL LETTER K */
  /* 6C */(char) 0x006C,/* #LATIN SMALL LETTER L */
  /* 6D */(char) 0x006D,/* #LATIN SMALL LETTER M */
  /* 6E */(char) 0x006E,/* #LATIN SMALL LETTER N */
  /* 6F */(char) 0x006F,/* #LATIN SMALL LETTER O */
  /* 70 */(char) 0x0070,/* #LATIN SMALL LETTER P */
  /* 71 */(char) 0x0071,/* #LATIN SMALL LETTER Q */
  /* 72 */(char) 0x0072,/* #LATIN SMALL LETTER R */
  /* 73 */(char) 0x0073,/* #LATIN SMALL LETTER S */
  /* 74 */(char) 0x0074,/* #LATIN SMALL LETTER T */
  /* 75 */(char) 0x0075,/* #LATIN SMALL LETTER U */
  /* 76 */(char) 0x0076,/* #LATIN SMALL LETTER V */
  /* 77 */(char) 0x0077,/* #LATIN SMALL LETTER W */
  /* 78 */(char) 0x0078,/* #LATIN SMALL LETTER X */
  /* 79 */(char) 0x0079,/* #LATIN SMALL LETTER Y */
  /* 7A */(char) 0x007A,/* #LATIN SMALL LETTER Z */
  /* 7B */(char) 0x007B,/* #LEFT CURLY BRACKET */
  /* 7C */(char) 0x007C,/* #VERTICAL LINE */
  /* 7D */(char) 0x007D,/* #RIGHT CURLY BRACKET */
  /* 7E */(char) 0x007E,/* #TILDE */
  /* 7F */(char) 0x007F,/* #DELETE */
  /* 80 */(char) 0x20AC,/* #EURO SIGN */
  /* 81 */UNKNOWN_CODEPOINT,
  /* 82 */(char) 0x201A,/* #SINGLE LOW-9 QUOTATION MARK */
  /* 83 */(char) 0x0192,/* #LATIN SMALL LETTER F WITH HOOK */
  /* 84 */(char) 0x201E,/* #DOUBLE LOW-9 QUOTATION MARK */
  /* 85 */(char) 0x2026,/* #HORIZONTAL ELLIPSIS */
  /* 86 */(char) 0x2020,/* #DAGGER */
  /* 87 */(char) 0x2021,/* #DOUBLE DAGGER */
  /* 88 */(char) 0x02C6,/* #MODIFIER LETTER CIRCUMFLEX ACCENT */
  /* 89 */(char) 0x2030,/* #PER MILLE SIGN */
  /* 8A */(char) 0x0160,/* #LATIN CAPITAL LETTER S WITH CARON */
  /* 8B */(char) 0x2039,/* #SINGLE LEFT-POINTING ANGLE QUOTATION MARK */
  /* 8C */(char) 0x0152,/* #LATIN CAPITAL LIGATURE OE */
  /* 8D */UNKNOWN_CODEPOINT,
  /* 8E */(char) 0x017D,/* #LATIN CAPITAL LETTER Z WITH CARON */
  /* 8F */UNKNOWN_CODEPOINT,
  /* 90 */UNKNOWN_CODEPOINT,
  /* 91 */(char) 0x2018,/* #LEFT SINGLE QUOTATION MARK */
  /* 92 */(char) 0x2019,/* #RIGHT SINGLE QUOTATION MARK */
  /* 93 */(char) 0x201C,/* #LEFT DOUBLE QUOTATION MARK */
  /* 94 */(char) 0x201D,/* #RIGHT DOUBLE QUOTATION MARK */
  /* 95 */(char) 0x2022,/* #BULLET */
  /* 96 */(char) 0x2013,/* #EN DASH */
  /* 97 */(char) 0x2014,/* #EM DASH */
  /* 98 */(char) 0x02DC,/* #SMALL TILDE */
  /* 99 */(char) 0x2122,/* #TRADE MARK SIGN */
  /* 9A */(char) 0x0161,/* #LATIN SMALL LETTER S WITH CARON */
  /* 9B */(char) 0x203A,/* #SINGLE RIGHT-POINTING ANGLE QUOTATION MARK */
  /* 9C */(char) 0x0153,/* #LATIN SMALL LIGATURE OE */
  /* 9D */UNKNOWN_CODEPOINT,
  /* 9E */(char) 0x017E,/* #LATIN SMALL LETTER Z WITH CARON */
  /* 9F */(char) 0x0178,/* #LATIN CAPITAL LETTER Y WITH DIAERESIS */
  /* A0 */(char) 0x00A0,/* #NO-BREAK SPACE */
  /* A1 */(char) 0x00A1,/* #INVERTED EXCLAMATION MARK */
  /* A2 */(char) 0x00A2,/* #CENT SIGN */
  /* A3 */(char) 0x00A3,/* #POUND SIGN */
  /* A4 */(char) 0x00A4,/* #CURRENCY SIGN */
  /* A5 */(char) 0x00A5,/* #YEN SIGN */
  /* A6 */(char) 0x00A6,/* #BROKEN BAR */
  /* A7 */(char) 0x00A7,/* #SECTION SIGN */
  /* A8 */(char) 0x00A8,/* #DIAERESIS */
  /* A9 */(char) 0x00A9,/* #COPYRIGHT SIGN */
  /* AA */(char) 0x00AA,/* #FEMININE ORDINAL INDICATOR */
  /* AB */(char) 0x00AB,/* #LEFT-POINTING DOUBLE ANGLE QUOTATION MARK */
  /* AC */(char) 0x00AC,/* #NOT SIGN */
  /* AD */(char) 0x00AD,/* #SOFT HYPHEN */
  /* AE */(char) 0x00AE,/* #REGISTERED SIGN */
  /* AF */(char) 0x00AF,/* #MACRON */
  /* B0 */(char) 0x00B0,/* #DEGREE SIGN */
  /* B1 */(char) 0x00B1,/* #PLUS-MINUS SIGN */
  /* B2 */(char) 0x00B2,/* #SUPERSCRIPT TWO */
  /* B3 */(char) 0x00B3,/* #SUPERSCRIPT THREE */
  /* B4 */(char) 0x00B4,/* #ACUTE ACCENT */
  /* B5 */(char) 0x00B5,/* #MICRO SIGN */
  /* B6 */(char) 0x00B6,/* #PILCROW SIGN */
  /* B7 */(char) 0x00B7,/* #MIDDLE DOT */
  /* B8 */(char) 0x00B8,/* #CEDILLA */
  /* B9 */(char) 0x00B9,/* #SUPERSCRIPT ONE */
  /* BA */(char) 0x00BA,/* #MASCULINE ORDINAL INDICATOR */
  /* BB */(char) 0x00BB,/* #RIGHT-POINTING DOUBLE ANGLE QUOTATION MARK */
  /* BC */(char) 0x00BC,/* #VULGAR FRACTION ONE QUARTER */
  /* BD */(char) 0x00BD,/* #VULGAR FRACTION ONE HALF */
  /* BE */(char) 0x00BE,/* #VULGAR FRACTION THREE QUARTERS */
  /* BF */(char) 0x00BF,/* #INVERTED QUESTION MARK */
  /* C0 */(char) 0x00C0,/* #LATIN CAPITAL LETTER A WITH GRAVE */
  /* C1 */(char) 0x00C1,/* #LATIN CAPITAL LETTER A WITH ACUTE */
  /* C2 */(char) 0x00C2,/* #LATIN CAPITAL LETTER A WITH CIRCUMFLEX */
  /* C3 */(char) 0x00C3,/* #LATIN CAPITAL LETTER A WITH TILDE */
  /* C4 */(char) 0x00C4,/* #LATIN CAPITAL LETTER A WITH DIAERESIS */
  /* C5 */(char) 0x00C5,/* #LATIN CAPITAL LETTER A WITH RING ABOVE */
  /* C6 */(char) 0x00C6,/* #LATIN CAPITAL LETTER AE */
  /* C7 */(char) 0x00C7,/* #LATIN CAPITAL LETTER C WITH CEDILLA */
  /* C8 */(char) 0x00C8,/* #LATIN CAPITAL LETTER E WITH GRAVE */
  /* C9 */(char) 0x00C9,/* #LATIN CAPITAL LETTER E WITH ACUTE */
  /* CA */(char) 0x00CA,/* #LATIN CAPITAL LETTER E WITH CIRCUMFLEX */
  /* CB */(char) 0x00CB,/* #LATIN CAPITAL LETTER E WITH DIAERESIS */
  /* CC */(char) 0x00CC,/* #LATIN CAPITAL LETTER I WITH GRAVE */
  /* CD */(char) 0x00CD,/* #LATIN CAPITAL LETTER I WITH ACUTE */
  /* CE */(char) 0x00CE,/* #LATIN CAPITAL LETTER I WITH CIRCUMFLEX */
  /* CF */(char) 0x00CF,/* #LATIN CAPITAL LETTER I WITH DIAERESIS */
  /* D0 */(char) 0x00D0,/* #LATIN CAPITAL LETTER ETH */
  /* D1 */(char) 0x00D1,/* #LATIN CAPITAL LETTER N WITH TILDE */
  /* D2 */(char) 0x00D2,/* #LATIN CAPITAL LETTER O WITH GRAVE */
  /* D3 */(char) 0x00D3,/* #LATIN CAPITAL LETTER O WITH ACUTE */
  /* D4 */(char) 0x00D4,/* #LATIN CAPITAL LETTER O WITH CIRCUMFLEX */
  /* D5 */(char) 0x00D5,/* #LATIN CAPITAL LETTER O WITH TILDE */
  /* D6 */(char) 0x00D6,/* #LATIN CAPITAL LETTER O WITH DIAERESIS */
  /* D7 */(char) 0x00D7,/* #MULTIPLICATION SIGN */
  /* D8 */(char) 0x00D8,/* #LATIN CAPITAL LETTER O WITH STROKE */
  /* D9 */(char) 0x00D9,/* #LATIN CAPITAL LETTER U WITH GRAVE */
  /* DA */(char) 0x00DA,/* #LATIN CAPITAL LETTER U WITH ACUTE */
  /* DB */(char) 0x00DB,/* #LATIN CAPITAL LETTER U WITH CIRCUMFLEX */
  /* DC */(char) 0x00DC,/* #LATIN CAPITAL LETTER U WITH DIAERESIS */
  /* DD */(char) 0x00DD,/* #LATIN CAPITAL LETTER Y WITH ACUTE */
  /* DE */(char) 0x00DE,/* #LATIN CAPITAL LETTER THORN */
  /* DF */(char) 0x00DF,/* #LATIN SMALL LETTER SHARP S */
  /* E0 */(char) 0x00E0,/* #LATIN SMALL LETTER A WITH GRAVE */
  /* E1 */(char) 0x00E1,/* #LATIN SMALL LETTER A WITH ACUTE */
  /* E2 */(char) 0x00E2,/* #LATIN SMALL LETTER A WITH CIRCUMFLEX */
  /* E3 */(char) 0x00E3,/* #LATIN SMALL LETTER A WITH TILDE */
  /* E4 */(char) 0x00E4,/* #LATIN SMALL LETTER A WITH DIAERESIS */
  /* E5 */(char) 0x00E5,/* #LATIN SMALL LETTER A WITH RING ABOVE */
  /* E6 */(char) 0x00E6,/* #LATIN SMALL LETTER AE */
  /* E7 */(char) 0x00E7,/* #LATIN SMALL LETTER C WITH CEDILLA */
  /* E8 */(char) 0x00E8,/* #LATIN SMALL LETTER E WITH GRAVE */
  /* E9 */(char) 0x00E9,/* #LATIN SMALL LETTER E WITH ACUTE */
  /* EA */(char) 0x00EA,/* #LATIN SMALL LETTER E WITH CIRCUMFLEX */
  /* EB */(char) 0x00EB,/* #LATIN SMALL LETTER E WITH DIAERESIS */
  /* EC */(char) 0x00EC,/* #LATIN SMALL LETTER I WITH GRAVE */
  /* ED */(char) 0x00ED,/* #LATIN SMALL LETTER I WITH ACUTE */
  /* EE */(char) 0x00EE,/* #LATIN SMALL LETTER I WITH CIRCUMFLEX */
  /* EF */(char) 0x00EF,/* #LATIN SMALL LETTER I WITH DIAERESIS */
  /* F0 */(char) 0x00F0,/* #LATIN SMALL LETTER ETH */
  /* F1 */(char) 0x00F1,/* #LATIN SMALL LETTER N WITH TILDE */
  /* F2 */(char) 0x00F2,/* #LATIN SMALL LETTER O WITH GRAVE */
  /* F3 */(char) 0x00F3,/* #LATIN SMALL LETTER O WITH ACUTE */
  /* F4 */(char) 0x00F4,/* #LATIN SMALL LETTER O WITH CIRCUMFLEX */
  /* F5 */(char) 0x00F5,/* #LATIN SMALL LETTER O WITH TILDE */
  /* F6 */(char) 0x00F6,/* #LATIN SMALL LETTER O WITH DIAERESIS */
  /* F7 */(char) 0x00F7,/* #DIVISION SIGN */
  /* F8 */(char) 0x00F8,/* #LATIN SMALL LETTER O WITH STROKE */
  /* F9 */(char) 0x00F9,/* #LATIN SMALL LETTER U WITH GRAVE */
  /* FA */(char) 0x00FA,/* #LATIN SMALL LETTER U WITH ACUTE */
  /* FB */(char) 0x00FB,/* #LATIN SMALL LETTER U WITH CIRCUMFLEX */
  /* FC */(char) 0x00FC,/* #LATIN SMALL LETTER U WITH DIAERESIS */
  /* FD */(char) 0x00FD,/* #LATIN SMALL LETTER Y WITH ACUTE */
  /* FE */(char) 0x00FE,/* #LATIN SMALL LETTER THORN */
  /* FF */(char) 0x00FF /* #LATIN SMALL LETTER Y WITH DIAERESIS */
  };


  public static final int UCS2ToCodePage850(char c)
  {
      int i;
      for (i = 0; i < CodePage850ToUCS2.length; i++)
      {
          if (c == (char)CodePage850ToUCS2[i])
          {
              return i;
          }
      }
      return UNKNOWN_CODEPOINT;
  }

  public static final int UCS2ToCodePage437(char c)
  {
      int i;
      for (i = 0; i < CodePage437ToUCS2.length; i++)
      {
          if (c == (char)CodePage437ToUCS2[i])
          {
              return i;
          }
      }
      return UNKNOWN_CODEPOINT;
  }

  public static final int UCS2ToCodePageWin1252(char c)
  {
      int i;
      for (i = 0; i < CodePage1252ToUCS2.length; i++)
      {
          if (c == (char)CodePage1252ToUCS2[i])
          {
              return i;
          }
      }
      return UNKNOWN_CODEPOINT;
  }




}


