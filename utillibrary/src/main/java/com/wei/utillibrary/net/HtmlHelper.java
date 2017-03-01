package com.wei.utillibrary.net;

import java.util.regex.Pattern;

public class HtmlHelper {
	
	//定义script的正则表达式{或<script[^>]*?>[\\s\\S]*?<\\/script>
	public static final String regEx_script = "<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>";

	//定义style的正则表达式{或<style[^>]*?>[\\s\\S]*?<\\/style>
	public static final String regEx_style = "<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>";

	// 定义HTML标签的正则表达式
	public static final String regEx_html = "<[^>]+>";
	
	//图片
	public static final String regEx_img = "<img[^>]*>";
	
	/**
	 * 删除Html标签
	 * 
	 * @param inputString
	 * @return
	 */
	public static String htmlRemoveTag(String inputString) {
		if (inputString == null)
			return null;
		String htmlStr = inputString; // 含html标签的字符串
		String textStr = "";
		java.util.regex.Pattern p_script;
		java.util.regex.Matcher m_script;
		java.util.regex.Pattern p_style;
		java.util.regex.Matcher m_style;
		java.util.regex.Pattern p_html;
		java.util.regex.Matcher m_html;
		java.util.regex.Pattern p_img;
		java.util.regex.Matcher m_img;
		try {
			p_img = Pattern.compile(regEx_img, Pattern.CASE_INSENSITIVE);
			m_img = p_img.matcher(htmlStr);
			htmlStr = m_img.replaceAll("[图片]"); // 过滤img标签 add
			
			p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
			m_script = p_script.matcher(htmlStr);
			htmlStr = m_script.replaceAll(""); // 过滤script标签
			p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
			m_style = p_style.matcher(htmlStr);
			htmlStr = m_style.replaceAll(""); // 过滤style标签
			p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
			m_html = p_html.matcher(htmlStr);
			htmlStr = m_html.replaceAll(""); // 过滤html标签
		    htmlStr=htmlStr.replaceAll("\\&nbsp;", " ");  
		    htmlStr=htmlStr.replaceAll("\\&lt;", "<");  
		    htmlStr=htmlStr.replaceAll("\\&gt;", ">");  
			textStr = htmlStr;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return textStr;// 返回文本字符串
	}
	
	/**
	 * 获取指定url中的指定参数字段对应的值
	 * @param url
	 * @param spec
	 * @return
	 */
	public static String getSpeFild(String url, String spec)
	{
        String specField = "", content = "";
        if (url.contains("?"))
        {
            String[] strArr = url.split("\\?");
            content = strArr[1].trim(); 
            if (content.contains("&"))
            { // 有多个参数
                String[] strs = content.split("&");
                for (int i = 0; i < strs.length; i ++)
                {
                    if (strs[i].contains(spec))
                    {
                        String[] infos = strs[i].split("=");
                        specField = infos[1].trim();
                        break;
                    }
                }
            }
            else
            { // 只有一个参数
                if (content.contains(spec))
                {
                    String[] strs = content.split("=");
                    specField = strs[1].trim();
                }
            }
        }
        return specField;
    }
}
