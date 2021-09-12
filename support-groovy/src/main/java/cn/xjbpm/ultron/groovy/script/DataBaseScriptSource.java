package cn.xjbpm.ultron.groovy.script;

import cn.xjbpm.ultron.groovy.repository.DataBaseScriptSourceRepository;
import org.springframework.scripting.ScriptSource;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

/**
 * @author 黄川 huchuc@vip.qq.com date: 2021/7/31
 */
public class DataBaseScriptSource implements ScriptSource {

	private final String scriptName;

	private final DataBaseScriptSourceRepository dataBaseScriptSourceRepository;

	private final Object lastModifiedMonitor = new Object();

	private LocalDateTime lastModifiedLocalDateTime;

	public DataBaseScriptSource(String scriptName, DataBaseScriptSourceRepository dataBaseScriptSourceRepository) {
		this.scriptName = scriptName;
		this.dataBaseScriptSourceRepository = dataBaseScriptSourceRepository;
	}

	@Override
	public String getScriptAsString() {
		synchronized (this.lastModifiedMonitor) {
			this.lastModifiedLocalDateTime = LocalDateTime.now();
		}
		return dataBaseScriptSourceRepository.findScriptContent(this.scriptName, lastModifiedLocalDateTime);
	}

	@Override
	public boolean isModified() {
		synchronized (this.lastModifiedMonitor) {
			return dataBaseScriptSourceRepository.checkModified(this.scriptName, lastModifiedLocalDateTime);
		}
	}

	@Override
	public String suggestedClassName() {
		return StringUtils.stripFilenameExtension(this.scriptName);
	}

}
