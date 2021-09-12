package cn.xjbpm.ultron.groovy.repository;

import cn.xjbpm.ultron.groovy.entity.ScriptCode;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * @author 黄川 huchuc@vip.qq.com date: 2021/7/31
 */
public interface DataBaseScriptSourceRepository {

	/**
	 * 根据脚本名称和容器中的最后一次注册时间查询脚本
	 * @param scriptName
	 * @param localDateTime
	 * @return
	 */
	String findScriptContent(String scriptName, LocalDateTime localDateTime);

	/**
	 * 检查脚本是否有变化
	 * @param scriptName
	 * @param lastModifiedLocalDateTime
	 * @return
	 */
	boolean checkModified(String scriptName, LocalDateTime lastModifiedLocalDateTime);

	/**
	 * 查找全部脚本
	 * @return
	 */
	Set<ScriptCode> findAllScriptCodeSet();

}
