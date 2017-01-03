package application.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class MimeHelperNode {

	private String tagName;
	private MimeHelperNode parent;
	private Object reference;
	private Map<String,String> attrMap = new HashMap<String,String>();
	private List<MimeHelperNode> childList = new ArrayList<MimeHelperNode>(5);

	public MimeHelperNode() {
	}

	public void setParent(MimeHelperNode parent) {
		this.parent = parent;
	}

	public MimeHelperNode getParent() {
		return parent;
	}

	public void setTagName(String tagname) {
//		tagName = tagname.intern();
		this.tagName = tagname;
	}

	public String getTagName() {
		return tagName;
	}

	public void putAttribute(String name, String value) {
		attrMap.put(name.toLowerCase(), value);
	}

	public String getAttribute(String name) {
		String lName = name.toLowerCase();
		if (attrMap.containsKey(lName)) {
			return attrMap.get(lName);
		}
		return null;
	}

	public Map<String,String> getAttributes() {
		return attrMap;
	}

	public void setReference(Object value) {
		reference = value;
	}

	public Object getReference() {
		return reference;
	}

	public void appendChild(MimeHelperNode child) {
		childList.add(child);
	}

	public int getChildLength() {
		return childList.size();
	}

	public MimeHelperNode getChildAt(int index) {
		return childList.get(index);
	}

	public void removeChild(MimeHelperNode child) {
		int index = childList.indexOf(child);

		if (index > -1) {
			childList.remove(index);
		}
	}

	public Iterator<MimeHelperNode> getChildren() {
		return childList.iterator();
	}

	public String toString() {
		return tagName;
	}

	public void clear() {
		tagName = null;
		parent = null;
		reference = null;
		attrMap.clear();
		childList.clear();
	}
	
	/**
	 * Mime 파싱 과정에서 return 되는 Content-Type 사례
	 * - text/html; charset="utf-8"
	 * - text/html; charset="euc-kr"
	 * - multipart/related; boundary="=_NamoWEC-6lktobsu2i"
	 * @return
	 */
	public String getContentType() {
		return attrMap.get("content-type");	//Content-Type
	}
	/**
	 * Mime 파싱 과정에서  return 되는 Content-Transfer-Encoding 사례
	 * - base64
	 * - quoted-printable

	 * 실제로 매핑될 수 있는 값: 7bit, base64, quoted-printable, 8bit, binary
	 * @return
	 */
	public String getContentTransferEncoding() {
		return attrMap.get("content-transfer-encoding");	//Content-Transfer-Encoding
	}
	/**
	 * 이미지 있을 경우 해당 CID 값 리턴됨
	 * @return
	 */
	public String getContentId() {
		return attrMap.get("content-id");	//Content-ID
	}
}
