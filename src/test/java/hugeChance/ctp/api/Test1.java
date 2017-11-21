package hugeChance.ctp.api;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.BeanUtils;
import org.bridj.util.StringUtils;
import org.hraink.futures.ctp.thostftdcuserapistruct.CThostFtdcRspUserLoginField;
import org.junit.Test;

import com.alibaba.fastjson.JSON;

public class Test1 {

    
    @Test
    public void test(){
        CThostFtdcRspUserLoginField field = new CThostFtdcRspUserLoginField();
        field.setBrokerID("123");
        
        String s = JSON.toJSONString(field);
        
        JSON json = JSON.parseObject(s);
        
        System.out.println(s);
        
        CThostFtdcRspUserLoginField field2 = new CThostFtdcRspUserLoginField();
        
        field2 = JSON.toJavaObject(json, CThostFtdcRspUserLoginField.class);
        
        System.out.println("sss"+true);
        
        try {
            BeanUtils.copyProperties(field2, json);
            
            System.out.println(field2.getBrokerID());
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
