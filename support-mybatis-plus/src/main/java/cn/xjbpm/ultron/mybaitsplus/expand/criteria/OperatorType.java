/*
 * Copyright © 2021  黄川 Rights Reserved.
 * 版权声明：黄川保留所有权利。
 * 免责声明：本规范是初步的，随时可能更改，恕不另行通知。黄川对此处包含的任何错误不承担任何责任。
 * 最后修改时间：2021/04/04 23:12:04
 *
 */

package cn.xjbpm.ultron.mybaitsplus.expand.criteria;

import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;

import java.util.Collection;

/**
 * @author 黄川 huchuc@vip.qq.com
 */
@SuppressWarnings("all")
public enum OperatorType {

	/**
	 * 等于
	 */
	EQ {
		@Override
		public void build(QueryChainWrapper queryWrapper, String column, Object value) {
			queryWrapper.eq(column, value);
		}
	},
	/**
	 * 不等于
	 */
	NE {
		@Override
		public void build(QueryChainWrapper queryWrapper, String column, Object value) {
			queryWrapper.ne(column, value);
		}
	},
	/**
	 * 小于等于
	 */
	LE {
		@Override
		public void build(QueryChainWrapper queryWrapper, String column, Object value) {
			queryWrapper.le(column, value);
		}
	},
	/**
	 * 大于等于
	 */
	GE {
		@Override
		public void build(QueryChainWrapper queryWrapper, String column, Object value) {
			queryWrapper.ge(column, value);
		}
	},
	/**
	 * 小于
	 */
	LT {
		@Override
		public void build(QueryChainWrapper queryWrapper, String column, Object value) {
			queryWrapper.lt(column, value);
		}
	},
	/**
	 * 大于
	 */
	GT {
		@Override
		public void build(QueryChainWrapper queryWrapper, String column, Object value) {
			queryWrapper.gt(column, value);
		}
	},
	/**
	 * LIKE '%值%'
	 */
	LIKE {
		@Override
		public void build(QueryChainWrapper queryWrapper, String column, Object value) {
			queryWrapper.like(column, value);
		}
	},
	/**
	 * LIKE '%值'
	 */
	LEFT_LIKE {
		@Override
		public void build(QueryChainWrapper queryWrapper, String column, Object value) {
			queryWrapper.likeLeft(column, value);
		}
	},
	/**
	 * LIKE '值%'
	 */
	RIGHT_LIKE {
		@Override
		public void build(QueryChainWrapper queryWrapper, String column, Object value) {
			queryWrapper.likeRight(column, value);
		}
	},
	/**
	 * NOT LIKE '%值%'
	 */
	NOT_LIKE {
		@Override
		public void build(QueryChainWrapper queryWrapper, String column, Object value) {
			queryWrapper.notLike(column, value);
		}
	},
	/**
	 * 字段 IN
	 * <p>
	 * 例: IN ('a', 'b', 'c')
	 * </p>
	 */
	IN {
		@Override
		public void build(QueryChainWrapper queryWrapper, String column, Object value) {
			if (value instanceof Collection) {
				queryWrapper.in(column, ((Collection) value));
			}
			else if (value instanceof Object[]) {
				queryWrapper.in(column, ((Object[]) value));
			}
			else {
				throw new RuntimeException("IN 查询只支持数组和集合！");
			}
		}
	},
	/**
	 * 字段 IN
	 * <p>
	 * 例:NOT IN ('a', 'b', 'c')
	 * </p>
	 */
	NOT_IN {
		@Override
		public void build(QueryChainWrapper queryWrapper, String column, Object value) {
			if (value instanceof Collection) {
				queryWrapper.notIn(column, ((Collection) value));
			}
			else if (value instanceof Object[]) {
				queryWrapper.notIn(column, ((Object[]) value));
			}
			else {
				throw new RuntimeException("NOT IN 查询只支持数组和集合！");
			}
		}
	};

	public abstract void build(QueryChainWrapper queryWrapper, String column, Object value);

}
