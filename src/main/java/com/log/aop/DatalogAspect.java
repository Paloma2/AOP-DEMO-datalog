package com.log.aop;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.beanutils.PropertyUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.log.dao.DataLogDao;
import com.log.pojo.ActionPojo;
import com.log.pojo.ActionType;
import com.log.pojo.ChangeItem;
import com.log.util.DiffUtil;

@Aspect
@Component
public class DatalogAspect {

    private static final Logger logger = LoggerFactory.getLogger(DatalogAspect.class);

    @Resource
    DataLogDao dataLogDao;
    
    public DatalogAspect(){
    	System.out.println("****************DatalogAspect****************");
    }
    @Pointcut("execution(public * com.product.dao.*.insert*(..))")
    public void insert(){}

    @Pointcut("execution(public * com.product.dao.*.delete*(..))")
    public void delete(){}
    
    @Pointcut("execution(public * com.product.dao.*.update*(..))")
    public void update(){}

    /**
     * 1\判断是什么类型的操作,增加\删除\还是更新
     *  增加insert(Product)
     *  更新 update(Product)
     *  删除delete(id)
     * 2\获取changeitem
     *   (1)新增操作,before直接获取,after记录下新增之后的id
     *   (2)更新操作,before获取操作之前的记录,after获取操作之后的记录,然后diff
     *   (3)删除操作,before根据id取记录
     * 3\保存操作记录
     *    actionType
     *    objectId
     *    objectClass
     * @param pjp
     * @return
     * @throws Throwable
     */
   @Around("insert() || delete() || update()")
   // @Around("insert()  || update()")
    public Object addDataLog(ProceedingJoinPoint pjp) throws Throwable {
    	System.out.println("****************addDataLog****************");
        Object returnObj = null;

        String method = pjp.getSignature().getName();
        ActionType actionType = null;
        ActionPojo dataLogPojo = new ActionPojo();
        String id = null;
        Object oldObj = null;
        try{

            if("insert".equals(method)){
                Object obj = pjp.getArgs()[0];
                id = PropertyUtils.getProperty(obj,"id").toString();
                actionType = ActionType.INSERT;
                List<ChangeItem> changeItems = DiffUtil.getInsertChangeItems(obj);
                dataLogPojo.getChanges().addAll(changeItems);
                dataLogPojo.setObjectClass(obj.getClass().getName());
                dataLogPojo.setObjectId(id);

            }else if("delete".equals(method)){
                id = pjp.getArgs()[0].toString();
                actionType = ActionType.DELETE;
                oldObj = DiffUtil.getObjectById(pjp.getTarget(),id);
                ChangeItem changeItem = DiffUtil.getDeleteChangeItem(oldObj);
                dataLogPojo.getChanges().add(changeItem);
                dataLogPojo.setObjectId(id);
                dataLogPojo.setObjectClass(oldObj.getClass().getName());
            }
            else if("update".equals(method)){
                actionType = ActionType.UPDATE;
                Object obj = pjp.getArgs()[0];
                id = PropertyUtils.getProperty(obj,"id").toString();
                dataLogPojo.setObjectId(id);
                oldObj = DiffUtil.getObjectById(pjp.getTarget(),id);
                dataLogPojo.setObjectClass(oldObj.getClass().getName());
            }

            returnObj = pjp.proceed(pjp.getArgs());

            dataLogPojo.setActionType(actionType);
            if(ActionType.UPDATE == actionType){
                Object newObj = DiffUtil.getObjectById(pjp.getTarget(),id);
                List<ChangeItem> changeItems = DiffUtil.getChangeItems(oldObj,newObj);
                dataLogPojo.getChanges().addAll(changeItems);
            }

            dataLogPojo.setOperator("admin"); //dynamic get from threadlocal/session
            dataLogPojo.setOperateTime(new Date());

            dataLogDao.insert(dataLogPojo);

        }catch (Exception e){
            logger.error(e.getMessage(),e);
        }

        return returnObj;
    }
}
