/**
 * Copyright 2009 by HANWHA S&C Corp.,
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of HANWHA S&C Corp. ("Confidential Information").  You
 * shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with HANWHA S&C Corp.
 */
package application.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import application.util.html.Attribute;
import application.util.html.Attributes;
import application.util.html.Element;
import application.util.html.FormControlType;
import application.util.html.FormField;
import application.util.html.FormFields;
import application.util.html.OutputDocument;
import application.util.html.Segment;
import application.util.html.Source;
import application.util.html.StringOutputSegment;

/**
 * @author : ParkSeongSoo
 * 
 */
public class ApFormHtmlFixer {

	private static final String[] resevedKeyword =
			"`,~,!,@,#,%,^,&,*,(,),-,+,|,<,>,[,],{,},',\",.,:,;"
			.split(",");

		/**
		 * 폼필드가 동작하기 위한 최소 입력값이 없을 때 최소 입력값을 자동으로 생성 
		 * @param htmlContent
		 * @return
		 * @throws Exception
		 */
		public static String fixFormFieldName(String htmlContent) throws Exception {
			Source formFieldsSource = new Source(htmlContent);
			OutputDocument outputDocument = new OutputDocument(htmlContent);
			FormFields formFields = formFieldsSource.findFormFields();
			for (Iterator i = formFields.iterator(); i.hasNext();) {
				FormField formField = (FormField) i.next();
				
				String orgFormFieldName = formField.getName();
				
				List formFieldList = formFields.getConflictList(formField.getName());
				if ( !(formFieldList != null && formFieldList.size() > 0) ) {
					formFieldList = new ArrayList();
					formFieldList.add(formField);
				}
				
				Iterator formFieldIter=formFieldList.iterator();
				for(int j=0; formFieldIter.hasNext(); j++) {
					FormField currentFormField = (FormField)formFieldIter.next();
//					FormControlType type = currentFormField.getFormControlType();
					
					
					String formFieldName = currentFormField.getName();
					
//					String fixedFieldName = null;
					boolean isFixed = false;
					
					if ( formFieldName != null ) {
						if ( j > 0 ) {	// 중복이 있다는 것임
							formFieldName = currentFormField.getName()+"_"+j;
						}
						
						if ( formFieldName.indexOf(' ') > 0 ) {
							formFieldName = formFieldName.replace(' ', '_');
							isFixed = true;
						}
						
						String compareFieldName = " " + formFieldName;
						
						if ( compareFieldName.indexOf(',') > 0 ) {
							formFieldName = formFieldName.replace(',', '_');
							isFixed = true;
						}
						
						for(int k=0; k<resevedKeyword.length; k++) {
							if ( compareFieldName.indexOf(resevedKeyword[k]) > 0 ) {
								formFieldName = formFieldName.replace(resevedKeyword[k].toCharArray()[0], '_');
								isFixed = true;
							}
						}
//						else if ( formFieldName.indexOf('-') > 0 ) {
//								fixedFiendName = formFieldName.replace('-', '_');
//								isFixed = true;
//						}
						char startChar = formFieldName.charAt(0);
						if ( !Character.isJavaIdentifierStart(startChar) ) {	// Java Program 문법에서 시작할 수 없는 문자열일 경우
							formFieldName = "FIX_" + formFieldName;
							isFixed = true;
						}
					}
					if ( isFixed ) {
						currentFormField.setName(formFieldName);
						List attrList = currentFormField.getAttributeList();
						for(Iterator attrIter=attrList.iterator(); attrIter.hasNext(); ) {
							Attribute attribute = (Attribute)attrIter.next();
							if ( attribute.getName().equalsIgnoreCase("name") ) {
								outputDocument.add(new StringOutputSegment(attribute,"name=\""+formFieldName+"\""));
							}
						}
//						outputDocument.add(new StringOutputSegment(currentFormField.getSegment(),
//								currentFormField.getSegment().getSourceText().replaceAll(orgFormFieldName, formFieldName)));
					}
					
				}
				
				
				
			}
			return outputDocument.toString();
		}
		
		public static String fixDocument(String legacyHTML, boolean delScript) throws Exception {
			return fixDocument(legacyHTML, true, delScript);
		}

		/**
		 * 문서의 잘못된 부분을 교정해 줌
		 * @param legacyHTML
		 * @return
		 */
		public static String fixDocument(String legacyHTML, boolean replaceViewWriter, boolean delScript) throws Exception {
			Source formFieldsSource = new Source(legacyHTML);
			OutputDocument outputDocument = new OutputDocument(legacyHTML);
			
			// script 삭제
			// <script src='script/approve.js'></script>
			// <script language="JavaScript" src="script/approve.js"></script>
			if ( delScript ) {
				List scriptElements = formFieldsSource.findAllElements("script");
				if ( scriptElements != null ) {
					for (Iterator i = scriptElements.iterator(); i.hasNext();) {
						Element element = (Element)i.next();
						outputDocument.add((new StringOutputSegment(element,"")));
					}
				}
			}
			
//				log("translate.2");
			
			// form 태그 삭제
			// <form name='upform' method='post' action='reportDoc.jsp'>
			// </form>
			List formElements = formFieldsSource.findAllElements("form");
			if ( formElements != null ) {
				for (Iterator i = formElements.iterator(); i.hasNext();) {
					Element element = (Element)i.next();
					outputDocument.add((new StringOutputSegment(element.getStartTag(),"")));
					if ( element.getEndTag() != null ) {
						outputDocument.add((new StringOutputSegment(element.getEndTag(),"")));
					}
				}
			}

			
			List headElements = formFieldsSource.findAllElements("head");
			if ( headElements != null ) {
				for (Iterator i = headElements.iterator(); i.hasNext();) {
					Element element = (Element)i.next();
					String endText = element.getStartTag().getSourceText() + "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />";
					outputDocument.add((new StringOutputSegment(element.getStartTag(),endText)));
				}
			}
			
//				log("translate.3");
			
			Map hasNotValueRadio = new HashMap();
			Map hasNotValueCheckbox = new HashMap();
			
			Map coflictValueRadio = new HashMap();
			Map coflictValueCheckbox = new HashMap();
			
			FormFields formFields = formFieldsSource.findFormFields();
			for (Iterator i = formFields.iterator(); i.hasNext();) {
				FormField formField = (FormField) i.next();
				FormControlType type = formField.getFormControlType();
				if ( type.equals(FormControlType.TEXT) ) {
					if ( formField.getName().equalsIgnoreCase("view_writer") && replaceViewWriter ) {
//						String inputSource = formField.getSegment().getSourceText();
//						inputSource = inputSource.replaceAll("view_writer", "기안자정보");
//						inputSource = inputSource.replaceAll(" disabled", "");
//						inputSource = inputSource.replaceAll(" readonly", "");
						List attrList = formField.getAttributeList();
						for(Iterator attrIter=attrList.iterator(); attrIter.hasNext(); ) {
							Attribute attribute = (Attribute)attrIter.next();
							if ( attribute.getName().equalsIgnoreCase("name") ) {
								outputDocument.add(new StringOutputSegment(attribute,"name=\""+"기안자정보"+"\"" + " " + "value=\""+"기안자정보"+"\""));
							} else if ( attribute.getName().equalsIgnoreCase("value") ) {
								outputDocument.add(new StringOutputSegment(attribute,""));
							} else if ( attribute.getName().equalsIgnoreCase("disabled") ) {
								outputDocument.add(new StringOutputSegment(attribute,""));
							} else if ( attribute.getName().equalsIgnoreCase("readonly") ) {
								outputDocument.add(new StringOutputSegment(attribute,""));
							}
						}
//						outputDocument.add(new StringOutputSegment(formField.getSegment(),inputSource));
					} else if ( formField.getName().equalsIgnoreCase("view_title") ) {
						List attrList = formField.getAttributeList();
						for(Iterator attrIter=attrList.iterator(); attrIter.hasNext(); ) {
							Attribute attribute = (Attribute)attrIter.next();
							if ( attribute.getName().equalsIgnoreCase("name") ) {
								outputDocument.add(new StringOutputSegment(attribute,"name=\""+"bodyDocTitle"+"\"" + " " + "value=\""+"제목"+"\""));
							} else if ( attribute.getName().equalsIgnoreCase("value") ) {
								outputDocument.add(new StringOutputSegment(attribute,""));
							}
						}
//						String inputSource = formField.getSegment().getSourceText();
//						inputSource = inputSource.replaceAll("view_title", "bodyDocTitle");
//						outputDocument.add(new StringOutputSegment(formField.getSegment(),inputSource));
					} else if ( formField.getName().equalsIgnoreCase("bodyDocTitle") ) {
//						List attrList = formField.getAttributeList();
//						for(Iterator attrIter=attrList.iterator(); attrIter.hasNext(); ) {
//							Attribute attribute = (Attribute)attrIter.next();
//							if ( attribute.getName().equalsIgnoreCase("name") ) {
//								outputDocument.add(new StringOutputSegment(attribute,"name=\""+"bodyDocTitle"+"\"" + " " + "value=\""+"제목"+"\""));
//							} else if ( attribute.getName().equalsIgnoreCase("value") ) {
//								outputDocument.add(new StringOutputSegment(attribute,""));
//							}
//						}
					}
				} else if ( type.equals(FormControlType.RADIO) || type.equals(FormControlType.CHECKBOX) ) {
					fixMultiComponent(outputDocument, 
							(type.equals(FormControlType.RADIO) ? hasNotValueRadio : hasNotValueCheckbox), 
							(type.equals(FormControlType.RADIO) ? coflictValueRadio : coflictValueCheckbox), 
							formField);
				} else if ( type.equals(FormControlType.SELECT_MULTIPLE) || type.equals(FormControlType.SELECT_SINGLE) ) {
					Segment segment = formField.getSegment();
					List optionList = segment.findAllElements("option");
					if( optionList != null ) {
						for(Iterator optionIter=optionList.iterator(); optionIter.hasNext(); ) {
							Element optionEl = (Element)optionIter.next();
							String optionCotentText = optionEl.getContentText();
							Attributes attribute = optionEl.getStartTag().getAttributes();
							if ( attribute.get("value") == null ) {	// select에 있는 option에서 value가 없을 경우 이를 보정함
								String selected = ( attribute.get("selected") != null ) ? "selected" : "";
								String fixedValue = "<option value=\""+optionCotentText+"\" "+selected+">";
								outputDocument.add(new StringOutputSegment(optionEl.getStartTag(),fixedValue));
							}
						}
					}
				}
			}
			return outputDocument.toString();
		}

		/**
		 * option, checkbox의 경우 value가 필수 필드이나 이 값이 없는 경우가 있어 보정함.
		 * @param outputDocument
		 * @param hasNotValueMap
		 * @param formField
		 */
		public static void fixMultiComponent(OutputDocument outputDocument, Map hasNotValueMap, Map coflictValueMap, FormField formField) throws Exception {
			List attrList = formField.getAttributeList();
			String compnentName = formField.getName();
			List valueList = null;
			if ( coflictValueMap.containsKey(compnentName) ) {
				valueList = (List)coflictValueMap.get(compnentName);
			} else {
				valueList = new ArrayList();
				coflictValueMap.put(compnentName, valueList);
			}
			boolean hasValue = false;
			for(Iterator attrIter=attrList.iterator(); attrIter.hasNext(); ) {
				Attribute attribute = (Attribute)attrIter.next();
				if ( attribute.getName().equalsIgnoreCase("value") ) {
					String attrValue = attribute.getValue();
					hasValue = (attrValue!=null && attrValue.trim().length()!=0);
					if ( !hasValue ) {
						int fixedValue = 1;
						if ( hasNotValueMap.containsKey(compnentName) ) {
							fixedValue = ((Integer)hasNotValueMap.get(compnentName)).intValue() + 1;
						}
						hasNotValueMap.put(compnentName, new Integer(fixedValue));
						outputDocument.add(new StringOutputSegment(attribute,"value=\"FIX_"+fixedValue+"\""));
						valueList.add("FIX_"+fixedValue);
						hasValue = true;
					} else {
						// 동일 값이 있을 경우
						if ( valueList.contains(attrValue.trim()) ) {
							int fixedValue = 1;
							if ( hasNotValueMap.containsKey(compnentName) ) {
								fixedValue = ((Integer)hasNotValueMap.get(compnentName)).intValue() + 1;
							}
							hasNotValueMap.put(compnentName, new Integer(fixedValue));
							outputDocument.add(new StringOutputSegment(attribute,"value=\"FIX_"+fixedValue+"_"+attrValue.trim()+"\""));
							valueList.add("FIX_"+fixedValue+"_"+attrValue.trim());
							//if ( attrValue.substring(attrValue.length()-1);
						} else {
							valueList.add(attrValue.trim());
						}
					}
				}
			}
			if ( !hasValue ) {
				int fixedValue = 1;
				if ( hasNotValueMap.containsKey(compnentName) ) {
					fixedValue = ((Integer)hasNotValueMap.get(compnentName)).intValue() + 1;
				}
				hasNotValueMap.put(compnentName, new Integer(fixedValue));
				String compnentSourceText = formField.getSegment().getSourceText();
				compnentSourceText = compnentSourceText.substring(0, compnentSourceText.length()-1) + " value=\"FIX_"+fixedValue+"\">";
				valueList.add("FIX_"+fixedValue);
				outputDocument.add(new StringOutputSegment(formField.getSegment(),compnentSourceText));
			}
		}	
	
}
