package vn.com.irtech.eport.framework.web.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.com.irtech.eport.system.domain.SysDictData;
import vn.com.irtech.eport.system.service.ISysDictDataService;
import vn.com.irtech.eport.system.service.ISysDictTypeService;

/**
 * Html calling thymeleaf for dictionary reading
 * 
 * @author admin
 */
@Service("dict")
public class DictService
{
    @Autowired
    private ISysDictTypeService dictTypeService;

    @Autowired
    private ISysDictDataService dictDataService;

    /**
     * Query dictionary data information according to dictionary type
     * 
     * @param dictType Dictionary type
     * @return Parameter key
     */
    public List<SysDictData> getType(String dictType)
    {
        return dictTypeService.selectDictDataByType(dictType);
    }

    /**
     * Query dictionary data information according to dictionary type and dictionary key value
     * 
     * @param dictType Dictionary type
     * @param dictValue Dictionary key
     * @return Dictionary tag
     */
    public String getLabel(String dictType, String dictValue)
    {
        return dictDataService.selectDictLabel(dictType, dictValue);
    }
}
