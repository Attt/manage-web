package com.dszj.thymeleaf;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dszj.manage.entity.Dict;
import com.dszj.manage.service.DictService;
import com.dszj.manage.utils.SpringContextUtil;

/**
 * 字典提取工具对象
 * @author yys
 */
public class DictUtil {

//    private static Cache dictCache = EhCacheUtil.getDictCache();
	//类型编码， 字典key，字典值 value
	private static Map<String,Map<String,String>> dictCache = new HashMap<>();
	
	private static Map<String,Map<String,String>> allDictsCache = new HashMap<>();

    /**
     * 获取字典值集合
     * @param label 字典标识
     */
    public static Map<String, String> value(String label){
        Map<String, String> value = dictCache.get(label);
        if(value == null){
            DictService dictService = SpringContextUtil.getBean(DictService.class);
            Dict dict = dictService.findByLabel(label);
            if(dict != null){
                String dictValue = dict.getValue();
                String[] outerSplit = dictValue.split(",");
                value = new HashMap<>();
                for (String osp : outerSplit) {
                    String[] split = osp.split(":");
                    if(split.length > 1){
                        value.put(split[0], split[1]);
                    }
                }
                dictCache.put(dict.getLabel(), value);
            }
        }
        return value;
    }
    
    public static Map<String, Map<String, String>> all(){
    	
    	if(allDictsCache == null || allDictsCache.size()<=0 ){
    		DictService dictService = SpringContextUtil.getBean(DictService.class);
    		List<Dict> allDicts = dictService.findAll();
    		
    		for (Dict dict : allDicts) {
    			Map<String,String> map = new HashMap<>();
    			String[] values = dict.getValue().split(",");
    			for (String value : values) {
    				String[] kv = value.split(":");
    				map.put(kv[0], kv[1]);
				}
    			
    			allDictsCache.put(dict.getLabel(),map);
			}
    	}
    	return allDictsCache;
    }

    /**
     * 根据选项编码获取选项值
     * @param label 字典标识
     * @param code 选项编码
     */
    public static String keyValue(String label, String code){
    	
        Map<String, String> map = DictUtil.value(label);
        if(map != null){
            return map.get(code);
        }else{
            return "";
        }
    }

    /**
     * 封装数据状态字典
     * @param status 状态
     */
    public static String dataStatus(Byte status){
        String label = "DATA_STATUS";
        return DictUtil.keyValue(label, String.valueOf(status));
    }

    /**
     * 清除缓存中指定的数据
     * @param label 字典标识
     */
    public static void clearCache(String label){
        if (dictCache.get(label) != null){
            dictCache.remove(label);
        }
    }
    
    /**
     * 清除缓存中指定的数据
     * @param label 字典标识
     */
    public static void clearAllCache(){
    	dictCache.clear();
    	allDictsCache.clear();
    	
    }
}
