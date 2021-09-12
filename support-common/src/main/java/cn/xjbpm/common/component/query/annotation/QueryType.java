package cn.xjbpm.common.component.query.annotation;

/**
 * @author 黄川 huchuc@vip.qq.com date: 2021/6/27
 */
public enum QueryType {

	/**
	 * 等于
	 */
	EQ,
	/**
	 * 不等于
	 */
	NE,
	/**
	 * 小于等于
	 */
	LE,
	/**
	 * 大于等于
	 */
	GE,
	/**
	 * 小于
	 */
	LT,
	/**
	 * 大于
	 */
	GT,
	/**
	 * LIKE '%值%'
	 */
	LIKE,
	/**
	 * LIKE '%值'
	 */
	LEFT_LIKE,
	/**
	 * LIKE '值%'
	 */
	RIGHT_LIKE,
	/**
	 * NOT LIKE '%值%'
	 */
	NOT_LIKE,
	/**
	 * 字段 IN
	 * <p>
	 * 例: IN ('a', 'b', 'c')
	 * </p>
	 */
	IN,
	/**
	 * 字段 IN
	 * <p>
	 * 例:NOT IN ('a', 'b', 'c')
	 * </p>
	 */
	NOT_IN;

}
