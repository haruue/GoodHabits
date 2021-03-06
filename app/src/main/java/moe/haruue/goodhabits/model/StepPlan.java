package moe.haruue.goodhabits.model;

import java.util.List;

/**
 * 自动安排时间的 Plan
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */

public class StepPlan extends Plan<Step> {

    // 此 Plan 中拥有的 Steps，兼容性考虑
    public List<Step> steps = array;

    public final static String PLAN_TYPE_STEP = "plan_type_step";

    public StepPlan() {
        super();
        planType = PLAN_TYPE_STEP;
    }

}
