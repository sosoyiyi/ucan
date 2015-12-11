package com.ucan.app.common.model;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

import com.yuntongxun.ecsdk.ECInitParams;

public class ClientUser implements Parcelable {

	public static final Parcelable.Creator<ClientUser> CREATOR = new Parcelable.Creator<ClientUser>() {
		public ClientUser createFromParcel(Parcel in) {
			return new ClientUser(in);
		}

		public ClientUser[] newArray(int size) {
			return new ClientUser[size];
		}
	};
	/**
	 * 用户唯一Id码<h1>32位uuid</h1>
	 */
	private String id;
	/**
	 * 用户账号:<h1>mobile</h1>
	 */
	private String accountId;
	/**
	 * 密码
	 */
	private String password;

	/**
	 * 昵称
	 */
	private String nickName = "";
	/**
	 * 0-Female <br/>
	 * 1-Male
	 */
	private int sex;
	
	
	/**
	 * 出生日期
	 */
	private long birth;
	
	
	/**
	 * 用户类别：<br/>
	 * 0-普通 <br/>
	 * 1-月费<br/>
	 * 2-季费<br/>
	 * 3-半年<br/>
	 * 4-年费<br/>
	 */
	private int pType;
	
	
	/**
	 * 用户属性：<br/>
	 * 0-初次使用<br/>
	 * 1-已注册(在Account	 
	 *  
	 */
	private int pVersion;

	/**
	 * 
	 * 用户签名
	 * 
	 */
	private String signature;

	private ECInitParams.LoginAuthType loginAuthType;

	public ECInitParams.LoginAuthType getLoginAuthType() {
		return loginAuthType;
	}

	public void setLoginAuthpType(ECInitParams.LoginAuthType loginAuthpType) {
		this.loginAuthType = loginAuthpType;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public long getBirth() {
		return birth;
	}

	public void setBirth(long birth) {
		this.birth = birth;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}

	public int getpType() {
		return pType;
	}

	public void setpType(int pType) {
		this.pType = pType;
	}

	public int getpVersion() {
		return pVersion;
	}

	public void setpVersion(int pVersion) {
		this.pVersion = pVersion;
	}

	public ClientUser(String accountId) {
		this.accountId = accountId;
	}

	private ClientUser(Parcel in) {

		this.accountId = in.readString();
		this.nickName = in.readString();
		this.password = in.readString();
		this.signature = in.readString();
		this.pVersion = in.readInt();
		this.sex = in.readInt();
		this.birth = in.readLong();
		this.pType = in.readInt();
		this.loginAuthType = ECInitParams.LoginAuthType.fromId(in.readInt());
	}

	@Override
	public String toString() {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("accountId", accountId);
			jsonObject.put("password", password);
			jsonObject.put("nickName", nickName);
			jsonObject.put("sex", sex);
			jsonObject.put("signature", signature);
			jsonObject.put("birth", birth);
			jsonObject.put("pType", pType);
			jsonObject.put("pVersion", pVersion);
			return jsonObject.toString();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return "ClientUser{" + "accountId='"
				+ accountId + '\'' + ", password='" + password + '\'' + '}';
	}

	public ClientUser from(String input) {
		JSONObject object = null;
		try {
			object = new JSONObject(input);
			if (object.has("accountId")) {
				this.accountId = object.getString("accountId");
			}
			if (object.has("nickName")) {
				this.nickName = object.getString("nickName");
			}
			if (object.has("password")) {
				this.password = object.getString("password");
			}
			if (object.has("sex")) {
				this.sex = object.getInt("sex");
			}
			if (object.has("birth")) {
				this.birth = object.getLong("birth");
			}
			if (object.has("personSign")) {
				this.signature = object.getString("personSign");
			}
			if (object.has("pType")) {
				this.pType = object.getInt("pType");
			}
			if (object.has("pVersion")) {
				this.pVersion = object.getInt("pVersion");
			}
			if (object.has("loginAuthpType")) {
				this.loginAuthType = ECInitParams.LoginAuthType.fromId(object
						.getInt("loginAuthpType"));
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return this;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.accountId);
		dest.writeString(this.nickName);
		dest.writeString(this.signature);
		dest.writeString(this.password);
		dest.writeInt(this.sex);
		dest.writeLong(this.birth);
		dest.writeInt(this.pType);
		dest.writeInt(this.pVersion);
		dest.writeInt(this.loginAuthType.getAuthTypeValue());
	}

}
