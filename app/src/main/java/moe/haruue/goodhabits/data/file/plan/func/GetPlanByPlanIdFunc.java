package moe.haruue.goodhabits.data.file.plan.func;

import moe.haruue.goodhabits.data.file.plan.PlanFileStorage;
import moe.haruue.goodhabits.model.Plan;
import rx.functions.Func1;

/**
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */

public class GetPlanByPlanIdFunc implements Func1<Plan, Plan> {
    @Override
    public Plan call(Plan plan) {
        return PlanFileStorage.getInstance().getPlan(plan.planId);
    }
}
