package net.edrop.edrop_user.model.bean;

//邀请信息的bean类
public class InvitationInfo {
    private IMUserInfo user;   //联系人
    private String reason;    //邀请原因
    private InvitationStatus status;   //邀请的状态

    public InvitationInfo() {

    }

    public InvitationInfo(IMUserInfo user, String reason, InvitationStatus status) {
        this.user = user;
        this.reason = reason;
        this.status = status;
    }

    public IMUserInfo getUser() {
        return user;
    }

    public void setUser(IMUserInfo user) {
        this.user = user;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public InvitationStatus getStatus() {
        return status;
    }

    public void setStatus(InvitationStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "InvitationInfo{" +
                "user=" + user +
                ", reason='" + reason + '\'' +
                ", status=" + status +
                '}';
    }

    public enum InvitationStatus{
        //联系人邀请信息状态
        NEW_INVITE, //新邀请
        INVITE_ACCEPT,  //接受邀请
        INVITE_ACCEPT_BY_PEER, //接受被邀请
    }
}
