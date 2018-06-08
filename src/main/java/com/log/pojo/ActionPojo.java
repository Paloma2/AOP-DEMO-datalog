package com.log.pojo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class ActionPojo {

    private String id;

    private String objectId;

    private String objectClass;

    private String operator;

    private Date operateTime;

    private ActionType actionType;

    private List<ChangeItem> changes = new ArrayList<ChangeItem>();
    
    public ActionPojo(){
    	UUID uuid = UUID.randomUUID();
    	this.id = uuid.toString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getObjectClass() {
        return objectClass;
    }

    public void setObjectClass(String objectClass) {
        this.objectClass = objectClass;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public Date getOperateTime() {
        return operateTime;
    }

    public void setOperateTime(Date operateTime) {
        this.operateTime = operateTime;
    }

    public ActionType getActionType() {
        return actionType;
    }

    public void setActionType(ActionType actionType) {
        this.actionType = actionType;
    }

    public List<ChangeItem> getChanges() {
        return changes;
    }

    public void setChanges(List<ChangeItem> changes) {
        this.changes = changes;
    }
}
