package sahil.iiitk_foundationday_app.model;


import android.content.Context;
import org.json.JSONException;
import org.json.JSONObject;

public class User {

	private String user_id,name,email,phone,department,college,collegeid, gender, year, mos;
	private int account_level;



	public User(){

	}
	public User(JSONObject author) throws JSONException {
		if(author.has("user_id"))this.user_id=author.getString("user_id");
		if(author.has("name"))this.name=author.getString("name");
		if(author.has("phone"))this.phone=author.getString("phone");
		if(author.has("email"))this.email=author.getString("email");
		if(author.has("account_level"))this.account_level=author.getInt("account_level");
		if(author.has("department"))this.department = author.getString("department");
		if(author.has("college"))this.college = author.getString("college");
		if(author.has("collegeid"))this.collegeid = author.getString("collegeid");
		if(author.has("gender"))this.gender = author.getString("gender");
		if(author.has("year"))this.year = author.getString("year");
		if(author.has("mos"))this.mos = author.getString("mos");
	}

	public User(Context context){


	}
	@Override
	public boolean equals(Object obj) {
		User user=(User)obj;
		if(user.getUser_id().equals(this.user_id))
			return true;
		else return false;
	}

	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return this.email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getYear(){ return year;}
	public void setYear(String year){this.year = year; }
	public String getGender(){ return gender;}
	public void setGender(String gender){this.gender = gender; }
	public String getMos(){ return mos;}
	public void setMos(String mos){this.mos = mos; }
	public String getDepartment(){return this.department;}
	public void setDepartment(String department){this.department = department;}

	public String getCollege(){return this.college;}
	public void setCollege(String college){this.college = college;}

	public String getCollegeid(){return this.collegeid;}
	public void setCollegeid(String collegeid){this.collegeid = collegeid;}

	public int getAccount_level() {
		return account_level;
	}

	public void setAccount_level(int account_level) {
		this.account_level = account_level;
	}
}
