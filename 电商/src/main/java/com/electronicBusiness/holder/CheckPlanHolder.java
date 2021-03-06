package com.electronicBusiness.holder;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.electronicBusiness.R;
import com.electronicBusiness.activity.TaskDetailActivity;
import com.electronicBusiness.base.BaseHolder;
import com.electronicBusiness.domain.MyPlanBean;
import com.electronicBusiness.manager.OkHttpClientManager;
import com.electronicBusiness.manager.OkHttpClientManager.Param;
import com.electronicBusiness.manager.OkHttpClientManager.ResultCallback;
import com.electronicBusiness.utils.ToastUtils;
import com.electronicBusiness.utils.UIUtils;
import com.squareup.okhttp.Request;

public abstract class CheckPlanHolder extends BaseHolder<MyPlanBean> {

	private TextView mTv_plan_name;
	private TextView mTv_plan_type;
	private TextView mTv_plan_time;
	private TextView mTv_receive_plan;
	private TextView mTv_see_detail;

	@Override
	public void changeViewStyle(final MyPlanBean data) {
		mTv_plan_name.setText(data.getName());
		mTv_plan_type.setText(UIUtils.getType(data.getIsBlind()));
		mTv_plan_time.setText(UIUtils.getFormatterTime(data.getAddTime()));
		mTv_receive_plan.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Param param1 = new Param("planId", data.getId() + "");
				OkHttpClientManager.postAsyn("http://" + mIp + ":" + mPort
						+ "/"+ com.electronicBusiness.manager.ConfigurationManager.project+"/check/receive_plan",
						new ResultCallback<String>() {

							@Override
							public void onError(Request request, Exception e) {
								ToastUtils.showToast("领取任务失败" + e.getMessage());
							}

							@Override
							public void onResponse(String response) {
								if (response.equals("200")) {
									ToastUtils.showToast("领取成功");
									receiveSuccess(mRootView, data);
								} else {
									ToastUtils.showToast("领取任务失败");
								}
							}

						}, param1);
			}
		});
		mTv_see_detail.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(data.getIsBlind()==1)
				{
					ToastUtils.showToast("盲盘无详情");
					return ;
				}
				Intent intent = new Intent(UIUtils.getContext(),TaskDetailActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.putExtra("planId", data.getId());
				UIUtils.getContext().startActivity(intent);
			}
		});
	}

	@Override
	public View initView() {
		View v = View.inflate(UIUtils.getContext(),
				R.layout.item_checkplan_list, null);
		mTv_plan_name = (TextView) v.findViewById(R.id.tv_plan_name);
		mTv_plan_type = (TextView) v.findViewById(R.id.tv_plan_type);
		mTv_plan_time = (TextView) v.findViewById(R.id.tv_plan_time);
		mTv_receive_plan = (TextView) v.findViewById(R.id.tv_receive_plan);
		mTv_see_detail = (TextView) v.findViewById(R.id.tv_see_detail);
		return v;
	}

	public abstract void receiveSuccess(View mRootView, MyPlanBean data);
}
