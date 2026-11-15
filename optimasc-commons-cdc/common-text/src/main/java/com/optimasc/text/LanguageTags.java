package com.optimasc.text;

import java.text.ParseException;

/*
 * See License.txt for more information on the licensing terms
 * for this source code.
 */

/**
 * Parser of IETF BCP 47 (RFC 5646) language tags, reduced to the subset that
 * <code>java.util.Locale</code> can represent on all supported platforms:
 * 
 * <pre>
 *     langtag  = language
 *                 ["-" region]
 *
 *     language = 2ALPHA            ; shortest ISO 639 code
 *     region   = 2ALPHA            ; ISO 3166-1 code
 * </pre>
 * 
 * <p>
 * The tag is first verified to be a well-formed language tag (RFC 5646,
 * section 2.1). The subtags are then handled as follows:
 * </p>
 * 
 * <table border="1">
 * <tr><th>Subtag</th><th>Not lenient</th><th>Lenient</th></tr>
 * <tr><td>language of 2 letters</td><td>kept</td><td>kept</td></tr>
 * <tr><td>language of 3 letters with an ISO 639-1 code</td><td>replaced by the
 * ISO 639-1 code</td><td>replaced by the ISO 639-1 code</td></tr>
 * <tr><td>language of 3 letters without an ISO 639-1 code, or of 4 to 8
 * letters</td><td>rejected</td><td>rejected</td></tr>
 * <tr><td>extended language (such as <code>zh-yue</code>)</td><td>rejected</td>
 * <td>rejected, as removing it would change the language</td></tr>
 * <tr><td>script</td><td>rejected</td><td>removed</td></tr>
 * <tr><td>region of 2 letters</td><td>kept</td><td>kept</td></tr>
 * <tr><td>region of 3 digits (UN M.49)</td><td>rejected</td><td>removed</td></tr>
 * <tr><td>variants, extensions, private use subtags</td><td>rejected</td>
 * <td>removed</td></tr>
 * <tr><td>private use tag (<code>x-...</code>), grandfathered tag</td>
 * <td>rejected</td><td>rejected</td></tr>
 * </table>
 * 
 * <p>
 * In both modes the language is converted to lower case and the region to
 * upper case, since subtags are case-insensitive. Replacing a 3-letter code
 * and converting the case keep the value of the tag; removing a subtag does
 * not, which is why it happens only when lenient.
 * </p>
 */
final class LanguageTags
{
  /**
   * ISO 639-2 codes (terminology and bibliographic) that have an ISO 639-1
   * code: each entry is the 3-letter code followed by the 2-letter code,
   * sorted by 3-letter code and separated by a space.
   */
  private static final String ISO639_2_TO_1 =
      "aaraa abkab afraf akaak albsq amham araar argan armhy asmas avaav aveae "
      + "aymay azeaz bakba bambm baqeu belbe benbn bihbh bisbi bodbo bosbs brebr "
      + "bulbg burmy catca cescs chach chece chizh chucu chvcv corkw cosco crecr "
      + "cymcy czecs danda deude divdv dutnl dzodz ellel engen epoeo estet euseu "
      + "eweee faofo fasfa fijfj finfi frafr frefr fryfy fulff geoka gerde glagd "
      + "glega glggl glvgv greel grngn gujgu hatht hauha hebhe herhz hinhi hmoho "
      + "hrvhr hunhu hyehy iboig iceis idoio iiiii ikuiu ileie inaia indid ipkik "
      + "islis itait javjv jpnja kalkl kankn kasks katka kaukr kazkk khmkm kikki "
      + "kinrw kirky komkv konkg korko kuakj kurku laolo latla lavlv limli linln "
      + "litlt ltzlb lublu luglg macmk mahmh malml maomi marmr mayms mkdmk mlgmg "
      + "mltmt molmo monmn mrimi msams myamy nauna navnv nblnr ndend ndong nepne "
      + "nldnl nnonn nobnb norno nyany ocioc ojioj orior ormom ossos panpa perfa "
      + "plipi polpl porpt pusps quequ rohrm ronro rumro runrn rusru sagsg sansa "
      + "sinsi slksk slosk slvsl smese smosm snasn sndsd somso sotst spaes sqisq "
      + "srdsc srpsr sswss sunsu swasw swesv tahty tamta tattt telte tgktg tgltl "
      + "thath tibbo tirti tonto tsntn tsots tuktk turtr twitw uigug ukruk urdur "
      + "uzbuz venve vievi volvo welcy wlnwa wolwo xhoxh yidyi yoryo zhaza zhozh "
      + "zulzu";

  /** Length of an entry of {@link #ISO639_2_TO_1}, separator included. */
  private static final int ENTRY_LENGTH = 6;

  private LanguageTags()
  {
  }

  /**
   * Returns the ISO 639-1 code of an ISO 639-2 code in lower case, or
   * <code>null</code> if there is none.
   */
  static String toISO6391(String code)
  {
    int low = 0;
    int high = (ISO639_2_TO_1.length() + 1) / ENTRY_LENGTH - 1;
    while (low <= high)
    {
      int middle = (low + high) >>> 1;
      int offset = middle * ENTRY_LENGTH;
      int comparison = ISO639_2_TO_1.substring(offset, offset + 3).compareTo(code);
      if (comparison == 0)
      {
        return ISO639_2_TO_1.substring(offset + 3, offset + 5);
      }
      if (comparison < 0)
      {
        low = middle + 1;
      }
      else
      {
        high = middle - 1;
      }
    }
    return null;
  }

  private static boolean isAlpha(char c)
  {
    return ((c >= 'a') && (c <= 'z')) || ((c >= 'A') && (c <= 'Z'));
  }

  private static boolean isDigit(char c)
  {
    return (c >= '0') && (c <= '9');
  }

  private static boolean isAlpha(String s)
  {
    for (int i = 0; i < s.length(); i++)
    {
      if (isAlpha(s.charAt(i)) == false)
      {
        return false;
      }
    }
    return true;
  }

  private static boolean isDigits(String s)
  {
    for (int i = 0; i < s.length(); i++)
    {
      if (isDigit(s.charAt(i)) == false)
      {
        return false;
      }
    }
    return true;
  }

  /** Variant: 5 to 8 alphanumeric characters, or a digit and 3 alphanumerics. */
  private static boolean isVariant(String s)
  {
    return ((s.length() >= 5) && (s.length() <= 8))
        || ((s.length() == 4) && isDigit(s.charAt(0)));
  }

  /**
   * Parses a language tag and converts it to a normalized form. Among others,
   * it  converts all ISO 639-2 codes to 639-1 codes and throws an error
   * if there is no equivalent. It also rejects all other language codes.
   * It drops all other subtabs except the ISO 3166-1 region code. 
   * 
   * @param value
   *          [in] The language tag according to IETF BCP 47.
   * @param lenient
   *          [in] <code>true</code> to remove the subtags that cannot be
   *          represented (script, UN M.49 region, variants, extensions and
   *          private use) instead of rejecting the tag. Removing them loses
   *          information. Changes that keep the value, such as converting the
   *          case or replacing an ISO 639-2 code by its ISO 639-1 code, are
   *          made in both modes.
   * @return The language (2 lower-case letters) and the region (2 upper-case
   *         letters, or the empty string)
   * @throws ParseException
   *           if the tag is not well-formed, or cannot be represented
   */
  public static String[] parse(CharSequence value, boolean lenient) throws ParseException
  {
    int length = value.length();
    // Split into subtags, remembering where each one starts.
    java.util.Vector subtags = new java.util.Vector();
    java.util.Vector starts = new java.util.Vector();
    int start = 0;
    for (int i = 0; i <= length; i++)
    {
      if ((i == length) || (value.charAt(i) == '-'))
      {
        if (i == start)
        {
          throw new ParseException("Empty subtag in language tag.", i);
        }
        subtags.addElement(value.subSequence(start, i).toString());
        starts.addElement(new Integer(start));
        start = i + 1;
        continue;
      }
      char c = value.charAt(i);
      if ((isAlpha(c) == false) && (isDigit(c) == false))
      {
        throw new ParseException("Invalid character in language tag.", i);
      }
      if (i - start >= 8)
      {
        throw new ParseException("Subtags have at most 8 characters.", i);
      }
    }

    // Language
    String language = (String) subtags.elementAt(0);
    if ((isAlpha(language) == false) || (language.length() < 2))
    {
      throw new ParseException(
          "Private use and grandfathered language tags are not supported.", 0);
    }
    if (language.length() > 3)
    {
      throw new ParseException("Language subtags of 4 to 8 letters are not supported.", 0);
    }
    // Subtags are case-insensitive (RFC 5646, Section 2.1.1), so changing
    // the case does not change the value.
    language = language.toLowerCase();
    if (language.length() == 3)
    {
      String code = toISO6391(language);
      if (code == null)
      {
        throw new ParseException("Language '" + language
            + "' has no ISO 639-1 code and cannot be represented.", 0);
      }
      // The ISO 639-1 code denotes the same language.
      language = code;
    }

    // Other subtags, in the order defined by RFC 5646.
    final int SCRIPT = 1;
    final int REGION = 2;
    final int VARIANT = 3;
    final int EXTENSION = 4;
    int state = SCRIPT;
    String region = "";
    int count = subtags.size();
    for (int index = 1; index < count; index++)
    {
      String subtag = (String) subtags.elementAt(index);
      int position = ((Integer) starts.elementAt(index)).intValue();
      int size = subtag.length();
      boolean removed = false;
      if ((index == 1) && (size == 3) && isAlpha(subtag))
      {
        throw new ParseException("Extended language subtags are not supported, as removing "
            + "them would change the language.", position);
      }
      if ((state <= SCRIPT) && (size == 4) && isAlpha(subtag))
      {
        // Script
        removed = true;
        state = REGION;
      }
      else if ((state <= REGION) && (size == 2) && isAlpha(subtag))
      {
        // ISO 3166-1 region
        region = subtag.toUpperCase();
        state = VARIANT;
      }
      else if ((state <= REGION) && (size == 3) && isDigits(subtag))
      {
        // UN M.49 region
        removed = true;
        state = VARIANT;
      }
      else if ((state <= VARIANT) && isVariant(subtag))
      {
        removed = true;
        state = VARIANT;
      }
      else if (size == 1)
      {
        // Extension or private use: a singleton followed by at least one subtag.
        boolean privateUse = (subtag.charAt(0) == 'x') || (subtag.charAt(0) == 'X');
        int minimum = privateUse ? 1 : 2;
        int next = index + 1;
        while ((next < count) && (((String) subtags.elementAt(next)).length() >= minimum)
            && (privateUse || ((String) subtags.elementAt(next)).length() > 1))
        {
          next++;
        }
        if (next == index + 1)
        {
          throw new ParseException("Expecting a subtag after the singleton.", position);
        }
        removed = true;
        index = next - 1;
        if (privateUse && (next != count))
        {
          throw new ParseException("Private use subtags must be the last subtags.", position);
        }
        state = EXTENSION;
      }
      else
      {
        throw new ParseException("Invalid or misplaced subtag in language tag.", position);
      }
      if (removed && (lenient == false))
      {
        throw new ParseException("Only a language and an ISO 3166-1 region are supported.",
            position);
      }
    }
    return new String[] { language, region };
  }
}
