package application.util.html;

import java.util.*;


/**
 *
 * @author <a href="mailto:ghbpark@hanwha.co.kr">Sungsoo Park</a>
 */
final class SearchCache {
	private static final String START_TAG_PREFIX = "ST";
	private static final String END_TAG_PREFIX = "ET";
	private static final String ELEMENT_PREFIX = "E";
	private final HashMap cache = new HashMap();

	public StartTag getStartTag(String key) {
		return (StartTag) cache.get(key);
	}

	void setStartTag(String key, StartTag startTag) {
		cache.put(key, (startTag == null) ? StartTag.CACHED_NULL : startTag);
	}

	EndTag getEndTag(String key) {
		return (EndTag) cache.get(key);
	}

	void setEndTag(String key, EndTag endTag) {
		cache.put(key, (endTag == null) ? EndTag.CACHED_NULL : endTag);
	}

	Element getElement(String key) {
		return (Element) cache.get(key);
	}

	void setElement(String key, Element element) {
		cache.put(key, element);
	}

	static String getStartTagKey(int pos, String name, boolean previous) {
		// Note name may be null
		return getKey(START_TAG_PREFIX, pos, name, previous);
	}

	static String getEndTagKey(int pos, String name, boolean previous) {
		// Note name is never null so this can't conflict with getEndTagKey(int pos)
		return getKey(END_TAG_PREFIX, pos, name, previous);
	}

	static String getEndTagKey(int pos) {
		return ELEMENT_PREFIX + pos;
	}

	static String getElementKey(StartTag startTag) {
		return END_TAG_PREFIX + startTag.begin;
	}

	private static String getKey(String prefix, int pos, String name,
		boolean previous) {
		StringBuffer sb = new StringBuffer(prefix);
		sb.append(pos);

		if (name != null) {
			sb.append(name);
		}

		if (previous) {
			sb.append('<');
		}

		return sb.toString();
	}
}
/*
 * $Log: SearchCache.java,v $
 * Revision 1.2.4.1  2014/03/04 06:26:11  optimistic
 * @version �ּ�����.
 *
 * Revision 1.2  2012/06/18 14:16:31  gw025
 *
 *
 * Revision 1.1  2010/06/03 14:47:44  ghbpark
 * *** empty log message ***
 *
 * Revision 1.1  2006/07/06 14:17:55  ghbpark
 * *** empty log message ***
 *
 * Revision 1.1  2005/09/06 06:59:37  ghbpark
 * xcommons 2.0 start
 *
 * Revision 1.1  2005/04/11 10:24:20  ghbpark
 * *** empty log message ***
 *
 * Revision 1.1  2005/04/03 01:36:51  ghbpark
 * *** empty log message ***
 *
 * Revision 1.2  2004/05/11 03:48:47  ghbpark
 * html 파서 추가
 *
 */
