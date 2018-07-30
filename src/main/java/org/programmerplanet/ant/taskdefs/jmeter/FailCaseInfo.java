package org.programmerplanet.ant.taskdefs.jmeter;

import org.w3c.dom.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang.StringUtils;


public class FailCaseInfo {
	
    public static ArrayList<String> getFailCase(String jtlPath) {
        Document document = null;
        DocumentBuilder documentBuilder = null;
        File file = new File(jtlPath);
        try {
            DocumentBuilderFactory factory = null;
            factory = DocumentBuilderFactory.newInstance();
            documentBuilder = factory.newDocumentBuilder();
            document = documentBuilder.parse(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Element element = document.getDocumentElement();
        NodeList nodeList = element.getChildNodes();
        Map<String, String> map = new HashMap<String, String>();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            String nodeName = node.getNodeName();
            if (nodeName.contains("httpSample")) {
        		NamedNodeMap namedNodeMap = node.getAttributes();            		
            	try {
            		
                    Node tn = namedNodeMap.getNamedItem("tn");
					String labelName = tn.getNodeValue().split(" ")[0];
            		
					Node lb = namedNodeMap.getNamedItem("lb");
                    String caseName = lb.getNodeValue();
                    
                    Node rc = namedNodeMap.getNamedItem("rc");
                    String responseCode = rc.getNodeValue();
                    
                    Node urlNode = getNodeByName(node, "java.net.URL");
                    String caseUrl = urlNode.getTextContent().replace("&amp;", "&").replace("&#38;", "&");
                    //报告:接口名加上状态码
                    String reportCaseName = caseName + "(" + responseCode + ")";
                    
                    Node assertionNode = getNodeByName1(node);
                    if ( !isEmpty(assertionNode) || responseCode.startsWith("4") || responseCode.startsWith("5") ) {
                    	map.put(reportCaseName, caseUrl);
                    }
                    
                } catch (Exception e) {
                    e.printStackTrace();
                }
             	           
            }
        }
        Set<Map.Entry<String,String>> set=map.entrySet();  
        Iterator<Map.Entry<String,String>> iter=set.iterator();
        ArrayList<String> a1 = new ArrayList<String>();
        while(iter.hasNext()){  
            Map.Entry<String, String> entry=iter.next();  
            a1.add(entry.getKey() + ":" + entry.getValue()); 
        }
        return a1;
    }
    
    public static Node getNodeByName(Node node, String name) {
        NodeList msgNodeList = node.getChildNodes();
        Node node1 = null;
        for (int k = 0; k < msgNodeList.getLength(); k++) {
            Node childNode = msgNodeList.item(k);
            String childName = childNode.getNodeName();
            if (childName.equalsIgnoreCase(name)) {
                node1 = childNode;
                break;
            }
        }
        return node1;
    }
    
    public static Node getNodeByName1(Node node) {
        Node node1 = null;              
        NodeList assertionNodes = node.getChildNodes();
    	for (int j = 0; j < assertionNodes.getLength(); j++) {
    		String nodeNames = assertionNodes.item(j).getNodeName();
    		if (nodeNames.contains("assertionResult")) {
    			Node failureNode = getNodeByName(assertionNodes.item(j), "failure");
    			boolean failure = Boolean.parseBoolean(failureNode.getTextContent());
    			if (failure) {
    				node1 = assertionNodes.item(j);
    				break;
				}     		
			}           		
		}
    	return node1;
    }
    
    private static boolean isEmpty(Object o) {
        boolean f = false;
        if (o == null) {
            f = true;
        }
        return f;
    }
    
    /* 
     * 将时间戳转换为时间
     */
    public static String stampToDate(String s){
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long lt = new Long(s);
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }

    /* 
     * 将时间转换为时间戳
     */  
    public static String dateToStamp(String s) throws ParseException{
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = simpleDateFormat.parse(s);
        long ts = date.getTime();
        res = String.valueOf(ts);
        return res;
    }
    
    public static String listToString(List<String> list, String string) {    		
		return StringUtils.join(list.toArray(),string);  	
	}
}

