package com.optimasc.utils;

/** Table for converting accented characters to their canonical representations.
 *  This table is a subset of the CanonicalChars class and represents only
 *  Latin characters of the BMP up to 0x02FF.
 *
 * 
 * @author Carl Eric Codere
 *
 */
public final class LatinCanonicalChars
{
  /** The first column is the actual non-canonical character code while
   *  the second one is the canonical character code. This has been
   *  implemented so it work in J2ME which limits each method to 32K in
   *  bytecode.
   */
  public static final char[][] canonicalMap =
  {
   { (char)0x00A8,  (char)0x0020},
   { (char)0x00AF,  (char)0x0020},
   { (char)0x00B4,  (char)0x0020},
   { (char)0x00B5,  (char)0x03BC},
   { (char)0x00B8,  (char)0x0020},
   { (char)0x00C0,  (char)0x0041},
   { (char)0x00C1,  (char)0x0041},
   { (char)0x00C2,  (char)0x0041},
   { (char)0x00C3,  (char)0x0041},
   { (char)0x00C4,  (char)0x0041},
   { (char)0x00C5,  (char)0x0041},
   { (char)0x00C7,  (char)0x0043},
   { (char)0x00C8,  (char)0x0045},
   { (char)0x00C9,  (char)0x0045},
   { (char)0x00CA,  (char)0x0045},
   { (char)0x00CB,  (char)0x0045},
   { (char)0x00CC,  (char)0x0049},
   { (char)0x00CD,  (char)0x0049},
   { (char)0x00CE,  (char)0x0049},
   { (char)0x00CF,  (char)0x0049},
   { (char)0x00D1,  (char)0x004E},
   { (char)0x00D2,  (char)0x004F},
   { (char)0x00D3,  (char)0x004F},
   { (char)0x00D4,  (char)0x004F},
   { (char)0x00D5,  (char)0x004F},
   { (char)0x00D6,  (char)0x004F},
   { (char)0x00D9,  (char)0x0055},
   { (char)0x00DA,  (char)0x0055},
   { (char)0x00DB,  (char)0x0055},
   { (char)0x00DC,  (char)0x0055},
   { (char)0x00DD,  (char)0x0059},
   { (char)0x00E0,  (char)0x0061},
   { (char)0x00E1,  (char)0x0061},
   { (char)0x00E2,  (char)0x0061},
   { (char)0x00E3,  (char)0x0061},
   { (char)0x00E4,  (char)0x0061},
   { (char)0x00E5,  (char)0x0061},
   { (char)0x00E7,  (char)0x0063},
   { (char)0x00E8,  (char)0x0065},
   { (char)0x00E9,  (char)0x0065},
   { (char)0x00EA,  (char)0x0065},
   { (char)0x00EB,  (char)0x0065},
   { (char)0x00EC,  (char)0x0069},
   { (char)0x00ED,  (char)0x0069},
   { (char)0x00EE,  (char)0x0069},
   { (char)0x00EF,  (char)0x0069},
   { (char)0x00F1,  (char)0x006E},
   { (char)0x00F2,  (char)0x006F},
   { (char)0x00F3,  (char)0x006F},
   { (char)0x00F4,  (char)0x006F},
   { (char)0x00F5,  (char)0x006F},
   { (char)0x00F6,  (char)0x006F},
   { (char)0x00F9,  (char)0x0075},
   { (char)0x00FA,  (char)0x0075},
   { (char)0x00FB,  (char)0x0075},
   { (char)0x00FC,  (char)0x0075},
   { (char)0x00FD,  (char)0x0079},
   { (char)0x00FF,  (char)0x0079},
   { (char)0x0100,  (char)0x0041},
   { (char)0x0101,  (char)0x0061},
   { (char)0x0102,  (char)0x0041},
   { (char)0x0103,  (char)0x0061},
   { (char)0x0104,  (char)0x0041},
   { (char)0x0105,  (char)0x0061},
   { (char)0x0106,  (char)0x0043},
   { (char)0x0107,  (char)0x0063},
   { (char)0x0108,  (char)0x0043},
   { (char)0x0109,  (char)0x0063},
   { (char)0x010A,  (char)0x0043},
   { (char)0x010B,  (char)0x0063},
   { (char)0x010C,  (char)0x0043},
   { (char)0x010D,  (char)0x0063},
   { (char)0x010E,  (char)0x0044},
   { (char)0x010F,  (char)0x0064},
   { (char)0x0112,  (char)0x0045},
   { (char)0x0113,  (char)0x0065},
   { (char)0x0114,  (char)0x0045},
   { (char)0x0115,  (char)0x0065},
   { (char)0x0116,  (char)0x0045},
   { (char)0x0117,  (char)0x0065},
   { (char)0x0118,  (char)0x0045},
   { (char)0x0119,  (char)0x0065},
   { (char)0x011A,  (char)0x0045},
   { (char)0x011B,  (char)0x0065},
   { (char)0x011C,  (char)0x0047},
   { (char)0x011D,  (char)0x0067},
   { (char)0x011E,  (char)0x0047},
   { (char)0x011F,  (char)0x0067},
   { (char)0x0120,  (char)0x0047},
   { (char)0x0121,  (char)0x0067},
   { (char)0x0122,  (char)0x0047},
   { (char)0x0123,  (char)0x0067},
   { (char)0x0124,  (char)0x0048},
   { (char)0x0125,  (char)0x0068},
   { (char)0x0128,  (char)0x0049},
   { (char)0x0129,  (char)0x0069},
   { (char)0x012A,  (char)0x0049},
   { (char)0x012B,  (char)0x0069},
   { (char)0x012C,  (char)0x0049},
   { (char)0x012D,  (char)0x0069},
   { (char)0x012E,  (char)0x0049},
   { (char)0x012F,  (char)0x0069},
   { (char)0x0130,  (char)0x0049},
   { (char)0x0132,  (char)0x0049},
   { (char)0x0133,  (char)0x0069},
   { (char)0x0134,  (char)0x004A},
   { (char)0x0135,  (char)0x006A},
   { (char)0x0136,  (char)0x004B},
   { (char)0x0137,  (char)0x006B},
   { (char)0x0139,  (char)0x004C},
   { (char)0x013A,  (char)0x006C},
   { (char)0x013B,  (char)0x004C},
   { (char)0x013C,  (char)0x006C},
   { (char)0x013D,  (char)0x004C},
   { (char)0x013E,  (char)0x006C},
   { (char)0x013F,  (char)0x004C},
   { (char)0x0140,  (char)0x006C},
   { (char)0x0143,  (char)0x004E},
   { (char)0x0144,  (char)0x006E},
   { (char)0x0145,  (char)0x004E},
   { (char)0x0146,  (char)0x006E},
   { (char)0x0147,  (char)0x004E},
   { (char)0x0148,  (char)0x006E},
   { (char)0x0149,  (char)0x02BC},
   { (char)0x014C,  (char)0x004F},
   { (char)0x014D,  (char)0x006F},
   { (char)0x014E,  (char)0x004F},
   { (char)0x014F,  (char)0x006F},
   { (char)0x0150,  (char)0x004F},
   { (char)0x0151,  (char)0x006F},
   { (char)0x0154,  (char)0x0052},
   { (char)0x0155,  (char)0x0072},
   { (char)0x0156,  (char)0x0052},
   { (char)0x0157,  (char)0x0072},
   { (char)0x0158,  (char)0x0052},
   { (char)0x0159,  (char)0x0072},
   { (char)0x015A,  (char)0x0053},
   { (char)0x015B,  (char)0x0073},
   { (char)0x015C,  (char)0x0053},
   { (char)0x015D,  (char)0x0073},
   { (char)0x015E,  (char)0x0053},
   { (char)0x015F,  (char)0x0073},
   { (char)0x0160,  (char)0x0053},
   { (char)0x0161,  (char)0x0073},
   { (char)0x0162,  (char)0x0054},
   { (char)0x0163,  (char)0x0074},
   { (char)0x0164,  (char)0x0054},
   { (char)0x0165,  (char)0x0074},
   { (char)0x0168,  (char)0x0055},
   { (char)0x0169,  (char)0x0075},
   { (char)0x016A,  (char)0x0055},
   { (char)0x016B,  (char)0x0075},
   { (char)0x016C,  (char)0x0055},
   { (char)0x016D,  (char)0x0075},
   { (char)0x016E,  (char)0x0055},
   { (char)0x016F,  (char)0x0075},
   { (char)0x0170,  (char)0x0055},
   { (char)0x0171,  (char)0x0075},
   { (char)0x0172,  (char)0x0055},
   { (char)0x0173,  (char)0x0075},
   { (char)0x0174,  (char)0x0057},
   { (char)0x0175,  (char)0x0077},
   { (char)0x0176,  (char)0x0059},
   { (char)0x0177,  (char)0x0079},
   { (char)0x0178,  (char)0x0059},
   { (char)0x0179,  (char)0x005A},
   { (char)0x017A,  (char)0x007A},
   { (char)0x017B,  (char)0x005A},
   { (char)0x017C,  (char)0x007A},
   { (char)0x017D,  (char)0x005A},
   { (char)0x017E,  (char)0x007A},
   { (char)0x017F,  (char)0x0073},
   { (char)0x01A0,  (char)0x004F},
   { (char)0x01A1,  (char)0x006F},
   { (char)0x01AF,  (char)0x0055},
   { (char)0x01B0,  (char)0x0075},
   { (char)0x01C4,  (char)0x0044},
   { (char)0x01C5,  (char)0x0044},
   { (char)0x01C6,  (char)0x0064},
   { (char)0x01C7,  (char)0x004C},
   { (char)0x01C8,  (char)0x004C},
   { (char)0x01C9,  (char)0x006C},
   { (char)0x01CA,  (char)0x004E},
   { (char)0x01CB,  (char)0x004E},
   { (char)0x01CC,  (char)0x006E},
   { (char)0x01CD,  (char)0x0041},
   { (char)0x01CE,  (char)0x0061},
   { (char)0x01CF,  (char)0x0049},
   { (char)0x01D0,  (char)0x0069},
   { (char)0x01D1,  (char)0x004F},
   { (char)0x01D2,  (char)0x006F},
   { (char)0x01D3,  (char)0x0055},
   { (char)0x01D4,  (char)0x0075},
   { (char)0x01D5,  (char)0x00DC},
   { (char)0x01D6,  (char)0x00FC},
   { (char)0x01D7,  (char)0x00DC},
   { (char)0x01D8,  (char)0x00FC},
   { (char)0x01D9,  (char)0x00DC},
   { (char)0x01DA,  (char)0x00FC},
   { (char)0x01DB,  (char)0x00DC},
   { (char)0x01DC,  (char)0x00FC},
   { (char)0x01DE,  (char)0x00C4},
   { (char)0x01DF,  (char)0x00E4},
   { (char)0x01E0,  (char)0x0226},
   { (char)0x01E1,  (char)0x0227},
   { (char)0x01E2,  (char)0x00C6},
   { (char)0x01E3,  (char)0x00E6},
   { (char)0x01E6,  (char)0x0047},
   { (char)0x01E7,  (char)0x0067},
   { (char)0x01E8,  (char)0x004B},
   { (char)0x01E9,  (char)0x006B},
   { (char)0x01EA,  (char)0x004F},
   { (char)0x01EB,  (char)0x006F},
   { (char)0x01EC,  (char)0x01EA},
   { (char)0x01ED,  (char)0x01EB},
   { (char)0x01EE,  (char)0x01B7},
   { (char)0x01EF,  (char)0x0292},
   { (char)0x01F0,  (char)0x006A},
   { (char)0x01F1,  (char)0x0044},
   { (char)0x01F2,  (char)0x0044},
   { (char)0x01F3,  (char)0x0064},
   { (char)0x01F4,  (char)0x0047},
   { (char)0x01F5,  (char)0x0067},
   { (char)0x01F8,  (char)0x004E},
   { (char)0x01F9,  (char)0x006E},
   { (char)0x01FA,  (char)0x00C5},
   { (char)0x01FB,  (char)0x00E5},
   { (char)0x01FC,  (char)0x00C6},
   { (char)0x01FD,  (char)0x00E6},
   { (char)0x01FE,  (char)0x00D8},
   { (char)0x01FF,  (char)0x00F8},
   { (char)0x0200,  (char)0x0041},
   { (char)0x0201,  (char)0x0061},
   { (char)0x0202,  (char)0x0041},
   { (char)0x0203,  (char)0x0061},
   { (char)0x0204,  (char)0x0045},
   { (char)0x0205,  (char)0x0065},
   { (char)0x0206,  (char)0x0045},
   { (char)0x0207,  (char)0x0065},
   { (char)0x0208,  (char)0x0049},
   { (char)0x0209,  (char)0x0069},
   { (char)0x020A,  (char)0x0049},
   { (char)0x020B,  (char)0x0069},
   { (char)0x020C,  (char)0x004F},
   { (char)0x020D,  (char)0x006F},
   { (char)0x020E,  (char)0x004F},
   { (char)0x020F,  (char)0x006F},
   { (char)0x0210,  (char)0x0052},
   { (char)0x0211,  (char)0x0072},
   { (char)0x0212,  (char)0x0052},
   { (char)0x0213,  (char)0x0072},
   { (char)0x0214,  (char)0x0055},
   { (char)0x0215,  (char)0x0075},
   { (char)0x0216,  (char)0x0055},
   { (char)0x0217,  (char)0x0075},
   { (char)0x0218,  (char)0x0053},
   { (char)0x0219,  (char)0x0073},
   { (char)0x021A,  (char)0x0054},
   { (char)0x021B,  (char)0x0074},
   { (char)0x021E,  (char)0x0048},
   { (char)0x021F,  (char)0x0068},
   { (char)0x0226,  (char)0x0041},
   { (char)0x0227,  (char)0x0061},
   { (char)0x0228,  (char)0x0045},
   { (char)0x0229,  (char)0x0065},
   { (char)0x022A,  (char)0x00D6},
   { (char)0x022B,  (char)0x00F6},
   { (char)0x022C,  (char)0x00D5},
   { (char)0x022D,  (char)0x00F5},
   { (char)0x022E,  (char)0x004F},
   { (char)0x022F,  (char)0x006F},
   { (char)0x0230,  (char)0x022E},
   { (char)0x0231,  (char)0x022F},
   { (char)0x0232,  (char)0x0059},
   { (char)0x0233,  (char)0x0079},
   { (char)0x02D8,  (char)0x0020},
   { (char)0x02D9,  (char)0x0020},
   { (char)0x02DA,  (char)0x0020},
   { (char)0x02DB,  (char)0x0020},
   { (char)0x02DC,  (char)0x0020},
   { (char)0x02DD,  (char)0x0020}
  };


   /** Converts a string representation to its canonical representation,
    *  that is removing all accented characters and replacing ligatured
    *  characters with non-ligatured characters. Currently only supports
    *  Latin character sets regarding replacing ligatured characters.
    *
    * @param str The string to convert
    * @return
    */
    public static String convertCanonical(String str)
    {
        final StringBuffer buffer = new StringBuffer(256);
        convertCanonical(str, buffer);
        return buffer.toString();
    }

   /** Converts a string representation to its canonical representation,
    *  that is removing all accented characters and replacing ligatured
    *  characters with non-ligatured characters. Currently only supports
    *  Latin character sets regarding replacing ligatured characters.
    *
    * @param outBuffer The buffer that will receive the resulting string.
    * @return outBuffer
    */
    public static void convertCanonical(String str, StringBuffer outBuffer)
    {
        int i;
        int j;
        char c;
        outBuffer.setLength(0);
        outBuffer.append(str);
        for (i = 0; i < outBuffer.length(); i++)
        {
            c = outBuffer.charAt(i);
            if (c > (char)0x7F)
            {
               // AE
               if (c == (char)0xC6)
               {
                   outBuffer.setCharAt(i,'A');
                   outBuffer.insert(i+1,'E');
                   break;
               }
               // ae
               if (c == (char)0xE6)
               {
                 // Canonical is always upper case
                   outBuffer.setCharAt(i,'A');
                   outBuffer.insert(i+1,'E');
                   break;
               }
               // OE
               if (c == (char)0x152)
               {
                   outBuffer.setCharAt(i,'O');
                   outBuffer.insert(i+1,'E');
                   break;
               }
               // oe
               if (c == (char)0x153)
               {
                   // Canonical is always upper case
                   outBuffer.setCharAt(i,'O');
                   outBuffer.insert(i+1,'E');
                   break;
               }
               for (j = 0; j < LatinCanonicalChars.canonicalMap.length; j++)
               {
                  if (c == LatinCanonicalChars.canonicalMap[j][0])
                  {
                      outBuffer.setCharAt(i,LatinCanonicalChars.canonicalMap[j][1]);
                      break;
                  }
               }
            } else
            {
                outBuffer.setCharAt(i,c);
            }
        }
    }


}
