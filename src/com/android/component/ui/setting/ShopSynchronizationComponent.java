package com.android.component.ui.setting;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.EditText;

import com.android.R;
import com.android.common.Constants;
import com.android.component.KeyboardComponent;
import com.android.component.SharedPreferencesComponent_;
import com.android.component.StringResComponent;
import com.android.component.ToastComponent;
import com.android.dialog.MyProcessDialog;
import com.android.mapping.CollectionMapping;
import com.android.mapping.ExpensesMapping;
import com.android.mapping.FoodAllMapping;
import com.android.mapping.ShopMapping;
import com.googlecode.androidannotations.annotations.AfterInject;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.Click;
import com.googlecode.androidannotations.annotations.EBean;
import com.googlecode.androidannotations.annotations.RootContext;
import com.googlecode.androidannotations.annotations.ViewById;
import com.googlecode.androidannotations.annotations.sharedpreferences.Pref;

/**
 * 店铺ID同步操作
 * 
 * @author superleo
 * 
 */
@EBean
public class ShopSynchronizationComponent {

	@RootContext
	Context context;

	@Pref
	SharedPreferencesComponent_ myPrefs;

	@ViewById(R.id.shop_set)
	EditText shopSet;

	@Bean
	StringResComponent stringResComponent;

	@Bean
	ToastComponent toastComponent;

	@Bean
	KeyboardComponent keyboardComponent;

	MyProcessDialog dialog;

	@ViewById(R.id.shop_set)
	EditText shopSset;

	@ViewById(R.id.shop_address_edit)
	EditText shopAddress;

	@ViewById(R.id.shop_contact_edit)
	EditText shopContact;

	@ViewById(R.id.shop_website_edit)
	EditText shopWebsite;

	@ViewById(R.id.shop_email_edit)
	EditText shopEmail;

	@AfterViews
	public void initFoodSynchronization() {
		shopSset.setText(myPrefs.shopId().get());
		shopAddress.setText(myPrefs.shopAddress().get());
		shopContact.setText(myPrefs.shopContact().get());
		shopWebsite.setText(myPrefs.shopWebsite().get());
		shopEmail.setText(myPrefs.shopEmail().get());
	}

	@AfterInject
	public void initLogin() {
		dialog = new MyProcessDialog(context, stringResComponent.dialogSet);
	}

	@Click(R.id.synchronization_shop_btn)
	public void synchronizeShopID() {
		String shopId = shopSet.getText().toString();

		ShopMapping mapping = ShopMapping.getJSON(Constants.URL_SHOP_PATH + shopId);
		if (mapping != null && mapping.code == Constants.STATUS_SUCCESS) {
			myPrefs.shopId().put(shopId);
			myPrefs.shopName().put(mapping.datas.get(0).name);
			myPrefs.shopAddress().put(mapping.datas.get(0).address);
			myPrefs.shopContact().put(mapping.datas.get(0).contact);
			myPrefs.shopWebsite().put(mapping.datas.get(0).website);
			myPrefs.shopEmail().put(mapping.datas.get(0).email);
			myPrefs.gstRate().put(mapping.datas.get(0).gstRate);
			myPrefs.gstRegNo().put(mapping.datas.get(0).gstRegNo);
			myPrefs.serviceRate().put(mapping.datas.get(0).serviceRate);
			myPrefs.weChat().put(mapping.datas.get(0).weChat);
			myPrefs.kichenPrinter().put(mapping.datas.get(0).kichenPrinter);
			myPrefs.openTime().put(mapping.datas.get(0).openTime);

			new ShopSynchronizationTask().execute(shopId);

			shopAddress.setText(myPrefs.shopAddress().get());
			shopContact.setText(myPrefs.shopContact().get());
			shopWebsite.setText(myPrefs.shopWebsite().get());
			shopEmail.setText(myPrefs.shopEmail().get());
		} else {
			toastComponent.show(stringResComponent.toastSettingShopFail);
		}
		dissmissKeyboard();
	}

	private class ShopSynchronizationTask extends AsyncTask<String, Void, Integer> {

		@Override
		protected Integer doInBackground(String... objs) {
			String url = Constants.URL_FOODSLIST_PATH + objs[0];
			FoodAllMapping foodMapping = FoodAllMapping.getJSONAndSave(url);
			if (foodMapping.code != Constants.STATUS_SUCCESS) {
				return foodMapping.code;
			}
			url = Constants.URL_PAY_DETAIL + objs[0];
			ExpensesMapping expensesMapping = ExpensesMapping.getJSONAndSave(url);
			if (expensesMapping.code != Constants.STATUS_SUCCESS) {
				return expensesMapping.code;
			}
			url = Constants.URL_TAKE_DNUM + objs[0];
			CollectionMapping collectionMapping = CollectionMapping.getJSONAndSave(url);
			if (collectionMapping.code != Constants.STATUS_SUCCESS) {
				return expensesMapping.code;
			}
			return Constants.STATUS_SUCCESS;
		}

		@Override
		protected void onPostExecute(Integer flag) {
			dialog.dismiss();
			switch (flag) {
			case Constants.STATUS_FAILED:
				toastComponent.show(stringResComponent.toastSettingErr);
				break;
			case Constants.STATUS_SUCCESS:
				toastComponent.show(stringResComponent.toastSettingSucc);
				break;
			case Constants.STATUS_SERVER_FAILED:
				toastComponent.show(stringResComponent.serviceErr);
				break;
			case Constants.STATUS_NETWORK_ERROR:
				toastComponent.show(stringResComponent.wifiErr);
				break;
			}
		}

		@Override
		protected void onPreExecute() {
			dialog.show();
		}

		@Override
		protected void onProgressUpdate(Void... values) {
		}
	}

	public void dissmissKeyboard() {
		keyboardComponent.dismissKeyboard(shopSet);
	}
}
