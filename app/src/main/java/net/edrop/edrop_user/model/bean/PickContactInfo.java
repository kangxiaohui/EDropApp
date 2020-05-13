package net.edrop.edrop_user.model.bean;

//选择联系人的bean类
public class PickContactInfo {
    private IMUserInfo user;// 联系人
    private boolean isChecked;//是否被选择的标记

    public PickContactInfo(IMUserInfo user, boolean isChecked) {
        this.user = user;
        this.isChecked = isChecked;
    }

    public PickContactInfo() {
    }

    public IMUserInfo getUser() {
        return user;
    }

    public void setUser(IMUserInfo user) {
        this.user = user;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
