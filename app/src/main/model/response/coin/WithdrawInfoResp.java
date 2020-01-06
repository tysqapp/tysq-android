package response.coin;

import com.google.gson.annotations.SerializedName;

/**
 * author       : frog
 * time         : 2019-08-15 11:35
 * desc         : 提现信息
 * version      : 1.3.0
 */
public class WithdrawInfoResp {

    /**
     * withdraw_amount : 11.28858233
     * min_fee : 0.001
     * income_amount : 11.28758233
     */

    @SerializedName("withdraw_amount")
    private double withdrawAmount;
    @SerializedName("min_fee")
    private double minFee;
    @SerializedName("income_amount")
    private double incomeAmount;

    public double getWithdrawAmount() {
        return withdrawAmount;
    }

    public void setWithdrawAmount(double withdrawAmount) {
        this.withdrawAmount = withdrawAmount;
    }

    public double getMinFee() {
        return minFee;
    }

    public void setMinFee(double minFee) {
        this.minFee = minFee;
    }

    public double getIncomeAmount() {
        return incomeAmount;
    }

    public void setIncomeAmount(double incomeAmount) {
        this.incomeAmount = incomeAmount;
    }
}
