package application.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class MimeDecodeUtil {
	
	private static final int BUFFER_SIZE = 1024*4;
	
	public static String getString(InputStream inputStream, String encoding)
			throws IOException {
			if (inputStream == null) {
				return null;
			}
		
			BufferedReader in = new BufferedReader(new InputStreamReader(
						inputStream, encoding), BUFFER_SIZE);
				int charsRead;
				char[] copyBuffer = new char[BUFFER_SIZE];
				StringBuffer sb = new StringBuffer();
			
				while ((charsRead = in.read(copyBuffer, 0, BUFFER_SIZE)) != -1) {
					sb.append(copyBuffer, 0, charsRead);
				}
			
				in.close();
			
				return sb.toString();
			}
	
	public static String getExtension(String filePath) {
		String strRet = "";
		if ( filePath == null ) return strRet;
		if ( !filePath.startsWith(".") && filePath.lastIndexOf(".") < 1 ) {
			return "";
		}
		int index = filePath.lastIndexOf(".") + 1;

		if (index > -1) {
			strRet = filePath.substring(index);
		}
		return strRet;
	}
	
	/**
	 * charet 참조
	 * @param contentType contentType 문자열
	 * @param isConvert 문자열 자동 변환 여부
	 * @return charset
	 */
	public static String getValue(String contentType, String paramKey) {
		String value = null;
		
		// 대문자로 변환
		contentType = contentType.toLowerCase();
		int charsetPosi = contentType.indexOf(paramKey);
		if (contentType.indexOf(paramKey) >= 0) {
			// StringTonkenizer 처리 ";="
			StringTokenizer st = new StringTokenizer(contentType.substring(charsetPosi), "=");
			String name = "";
			while (st.hasMoreTokens()) {
				name = (String)st.nextToken();
				if ( paramKey.equals(name) && st.hasMoreTokens() ) {
					value = (String)st.nextToken();
					value = value.replace('"', '\0');
					break;
				}
			}
		}
		return value;
	}
	
	/**
	 * charet 참조
	 * @param contentType contentType 문자열
	 * @param isConvert 문자열 자동 변환 여부
	 * @return charset
	 */
	public static String getCharset(String contentType) {
		String charset = null;
		
		// 대문자로 변환
		contentType = contentType.toUpperCase();
		int charsetPosi = contentType.indexOf("CHARSET");
		if (contentType.indexOf("CHARSET") >= 0) {
			// StringTonkenizer 처리 ";="
			StringTokenizer st = new StringTokenizer(contentType.substring(charsetPosi), "=");
			String name = "";
			while (st.hasMoreTokens()) {
				name = (String)st.nextToken();
				if ( "CHARSET".equals(name) && st.hasMoreTokens() ) {
					charset = (String)st.nextToken();
					break;
				}
			}
		}
		return charset;
	}
	
	/**
	 * Charset 교정
	 * @param strCharset 교정할 Charset 
	 * @return
	 */
    public static String selectCharset(String strCharset) {

        if ( strCharset.startsWith("\"")) {
             int mylength = strCharset.length();
             strCharset = strCharset.substring(1, mylength);
        }

        if ( strCharset.endsWith("\"")) {
             int mylength = strCharset.length();
             strCharset = strCharset.substring(0, mylength-1);
        }

        strCharset = strCharset.toUpperCase();
        String resultCharset = strCharset;

        ////////// 한국어 EUC-KR을 지원하는 인코딩 방식입니다 //////////////////////////////////////////
        if ( strCharset.indexOf("EUC-KR") != -1) {
                resultCharset = "EUC-KR";
        }

        else if ( strCharset.indexOf("EUCKR") != -1) {
                resultCharset = "EUC-KR";
        }

        else if ( strCharset.indexOf("KS_C_5601") != -1) {
                resultCharset = "EUC-KR";
        }

        else if ( strCharset.indexOf("EDU-KR") != -1) {
                resultCharset = "EUC-KR";
        }

        else if ( strCharset.indexOf("EUR-KR") != -1) {
                resultCharset = "EUC-KR";
        }

        else if ( strCharset.indexOf("KOREAN") != -1) {
                resultCharset = "EUC-KR";
        }

        else if ( strCharset.indexOf("DEFAULT_CHARSET") != -1) {
                resultCharset = "EUC-KR";
        }

        else if ( strCharset.indexOf("_AUTODETECT_KR") != -1) {
                resultCharset = "EUC-KR";
        }

        else if ( strCharset.indexOf("KS_C_5601-1987") != -1) {
                resultCharset = "EUC-KR";
        }

        else if ( strCharset.indexOf("KOR-ASCII") != -1) {
                resultCharset = "EUC-KR";
        }

        else if ( strCharset.indexOf("EUC_KR") != -1) {
                resultCharset = "EUC-KR";
        }

        else if ( strCharset.indexOf("EUC-KOR") != -1) {
                resultCharset = "EUC-KR";
        }

        //////// 여기 까지가 EUC-KR 인코딩 방식입니다 //////////////////////////////////////////////////

        //////// 일본어 방식의 처리는 이곳에서 합니다 ////////////////////////////////////////////////////
        else if ( strCharset.indexOf("JA_JP.ISO2022-7")!= -1) {
                resultCharset = "JIS";
        }

        else if ( strCharset.indexOf("JA_JP.EUCJP") != -1) {
                resultCharset = "EUCJIS";
        }

        else if ( strCharset.indexOf("ISO-2022-JP") != -1) {
                resultCharset = "JIS";
        }

        else if ( strCharset.indexOf("EUC-JP") != -1) {
                resultCharset = "EUCJIS";
        }

        ///////// 중국어 지원은 이곳에서 합니다. ///////////////////////////////////////////////////////

        else if ( strCharset.indexOf("ISO-2022-CN") != -1) {
                resultCharset = "CNS11643";
        }

        ///////// ISO 계열의 지원은 이곳에서 합니다 ////////////////////////////////////////////////////
        else if ( strCharset.indexOf("US-ASCII") != -1) {
                resultCharset = "EUC-KR";
        }


        else if ( strCharset.indexOf("ISO-2022-KR") != -1) {
                resultCharset = "ISO2022KR";
        }

        else if ( strCharset.indexOf("UTF-8") != -1) {
                resultCharset = "UTF-88";
        }

        else if ( strCharset.indexOf("UTF8") != -1) {
                resultCharset = "UTF-8";
        }

		else if ( strCharset.indexOf("UTF-16") != -1) {
				resultCharset = "UTF16";
		}

		else if ( strCharset.indexOf("UTF16") != -1) {
				resultCharset = "UTF16";
		}

        else if ( strCharset.indexOf("JA_JP.ISO2022-7") != -1) {
                resultCharset = "JIS";
        }

        else if ( strCharset.indexOf("JA_JP.EUCJP") != -1) {
                resultCharset = "EUCJIS";
        }

        ///////// 여기 까지가 ISO 계열의 인코딩 방식입니다 //////////////////////////////////////////
        else {
                resultCharset = "EUC-KR";
        }

        return resultCharset;
    }
	
    
    /**
     * text 문자열을 HTML 형식으로 변환
     * @param s 변경할 문자열
     * @return 변경된 문자열
     */
    public static String textToHtml(String s) {
        if (s.equals("") || s.equals(" ") || s.equals("null"))
            return "";
        //if (s.length() == 0 || s.length() == 1)
        if (s.length() == 0)
            return "";

        int len = s.length();
        int i = 0;

	    StringBuffer strBuffer = new StringBuffer();
	    strBuffer.append("<html><head>");
	    strBuffer.append("<STYLE>BODY{FONT-FAMILY: Gulim;FONT-SIZE: 10pt;}</STYLE>");
	    strBuffer.append("</head><body>");
        for (i = 0; i < len; i++) {
        	if (s.charAt(i) == ' ')
        		strBuffer.append("&nbsp;");
        	else if (s.charAt(i) == '<')
				strBuffer.append("&lt;");
            else if (s.charAt(i) == '>')
				strBuffer.append("&gt;");
            else if (s.charAt(i) == '&') {
            	if( i+1 < len &&  s.charAt(i+1) != '#') // unicode 를 text/plain 코드로 보여주어 발생한 문제처리..
            		strBuffer.append("&amp;");
            	else
            		strBuffer.append(s.charAt(i));
            }
            else if (s.charAt(i) == '\''){
				strBuffer.append("");}
            else if (s.charAt(i) == '"')
				strBuffer.append("&quot;");
          	else if (s.charAt(i) == '"')
				strBuffer.append("&apos;");
			else if (s.charAt(i) == '\n')
			{
				if( i==0 || ( i > 0 && s.charAt(i-1) != '\r'))
					strBuffer.append("<BR>");
			}
			else if ( s.charAt(i) == '\r')
			{
				if( i==0 || ( i > 0 && s.charAt(i-1) != '\n'))
					strBuffer.append("<BR>");
			}
			else
				strBuffer.append(s.charAt(i));
        }
        strBuffer.append("</body></html>");
        return strBuffer.toString();
    }
    
    

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
	}

}
