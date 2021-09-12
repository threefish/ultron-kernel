package cn.xjbpm.ultron.mybaitsplus.mapper;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.UpdateChainWrapper;
import com.baomidou.mybatisplus.extension.toolkit.ChainWrappers;

/**
 * @author 黄川 huchuc@vip.qq.com date: 2021/6/20
 */
public interface BaseMapper<DO> extends com.baomidou.mybatisplus.core.mapper.BaseMapper<DO> {

	default QueryChainWrapper<DO> query() {
		return ChainWrappers.queryChain(this);
	}

	default LambdaQueryChainWrapper<DO> lambdaQuery() {
		return ChainWrappers.lambdaQueryChain(this);
	}

	default UpdateChainWrapper<DO> update() {
		return ChainWrappers.updateChain(this);
	}

	default LambdaUpdateChainWrapper<DO> lambdaUpdate() {
		return ChainWrappers.lambdaUpdateChain(this);
	}

}
