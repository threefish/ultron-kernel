package cn.xjbpm.ultron.id.generator.service;

/**
 * @author 黄川 huchuc@vip.qq.com date: 2021/6/19
 */
public interface GlobalIdService {

	/**
	 * 生产数字类型唯一ID
	 * @return
	 */
	long nextLongId();

	/**
	 * 生产字符串类型唯一ID
	 * @return
	 */
	String nextStringId();

}
