package cn.xjbpm.common.util.language;

import cn.hutool.core.util.StrUtil;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

/**
 * @author 黄川 huchuc@vip.qq.com
 * https://blog.csdn.net/weixin_42248745/article/details/82992869
 */
public class PingYinUtil {

	/**
	 * 取得拼音
	 * <p>
	 * 秋水共长天一色 => qiushuigongzhangtianyise
	 * @param inputString
	 * @return
	 */
	public static String getPingYin(String inputString) {
		if (StrUtil.isEmpty(inputString)) {
			return "";
		}
		HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
		format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
		format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		// WITH_V：用v表示ü
		format.setVCharType(HanyuPinyinVCharType.WITH_V);
		char[] input = inputString.trim().toCharArray();
		StringBuffer stringBuffer = new StringBuffer();
		try {
			for (int i = 0; i < input.length; ++i) {
				if (Character.toString(input[i]).matches("[\\u4E00-\\u9FA5]+")) {
					String[] temp = PinyinHelper.toHanyuPinyinStringArray(input[i], format);
					stringBuffer.append(temp[0]);
				}
				else {
					stringBuffer.append(input[i]);
				}
			}
		}
		catch (BadHanyuPinyinOutputFormatCombination e) {
			e.printStackTrace();
		}
		return stringBuffer.toString();
	}

	/**
	 * 取得全拼
	 * <p>
	 * 秋水共长天一色 => qiu shui gong zhang tian yi se
	 * @param hanzi
	 * @return
	 */
	public static String getFullSpell(String hanzi) {
		if (StrUtil.isEmpty(hanzi)) {
			return "";
		}
		HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
		format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
		/**
		 * 输出音标设置
		 *
		 * WITH_TONE_MARK:直接用音标符（必须设置WITH_U_UNICODE，否则会抛出异常） WITH_TONE_NUMBER：1-4数字表示音标
		 * WITHOUT_TONE：没有音标
		 */
		format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		/**
		 * 特殊音标ü设置
		 *
		 * WITH_V：用v表示ü WITH_U_AND_COLON：用"u:"表示ü WITH_U_UNICODE：直接用ü
		 * format.setVCharType(HanyuPinyinVCharType.WITH_U_UNICODE);
		 */
		char[] hanYuArr = hanzi.trim().toCharArray();
		StringBuilder pinYin = new StringBuilder();
		try {
			for (int i = 0, len = hanYuArr.length; i < len; i++) {
				// 匹配是否是汉字
				if (Character.toString(hanYuArr[i]).matches("[\\u4E00-\\u9FA5]+")) {
					// 如果是多音字，返回多个拼音，这里只取第一个
					String[] pys = PinyinHelper.toHanyuPinyinStringArray(hanYuArr[i], format);
					pinYin.append(pys[0]).append(" ");
				}
				else {
					pinYin.append(hanYuArr[i]).append(" ");
				}
			}
		}
		catch (BadHanyuPinyinOutputFormatCombination e) {
			e.printStackTrace();
		}
		return pinYin.toString();
	}

	/**
	 * 获得首字母拼写
	 * <p>
	 * 秋水共长天一色 => qsgztys
	 * @param hanyu
	 * @return
	 */
	public static String getFirstSpell(String hanyu) {
		if (StrUtil.isEmpty(hanyu)) {
			return "";
		}
		HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
		format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
		format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		StringBuilder firstPinyin = new StringBuilder();
		char[] hanyuArr = hanyu.trim().toCharArray();
		try {
			for (int i = 0, len = hanyuArr.length; i < len; i++) {
				if (Character.toString(hanyuArr[i]).matches("[\\u4E00-\\u9FA5]+")) {
					String[] pys = PinyinHelper.toHanyuPinyinStringArray(hanyuArr[i], format);
					firstPinyin.append(pys[0].charAt(0));
				}
				else {
					firstPinyin.append(hanyuArr[i]);
				}
			}
		}
		catch (BadHanyuPinyinOutputFormatCombination badHanyuPinyinOutputFormatCombination) {
			badHanyuPinyinOutputFormatCombination.printStackTrace();
		}
		return firstPinyin.toString();
	}

}
