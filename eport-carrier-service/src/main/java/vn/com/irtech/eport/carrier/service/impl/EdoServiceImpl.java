package vn.com.irtech.eport.carrier.service.impl;

import java.io.File;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

import vn.com.irtech.eport.carrier.domain.Edo;
import vn.com.irtech.eport.carrier.mapper.EdoMapper;
import vn.com.irtech.eport.carrier.service.IEdoService;
import vn.com.irtech.eport.common.core.text.Convert;
import vn.com.irtech.eport.common.utils.DateUtils;


/**
 * Exchange Delivery OrderService Business Processing
 * 
 * @author ruoyi
 * @date 2020-06-26
 */
@Service
public class EdoServiceImpl implements IEdoService 
{
    @Autowired
    private EdoMapper edoMapper;

    /**
     * Get Exchange Delivery Order
     * 
     * @param id Exchange Delivery OrderID
     * @return Exchange Delivery Order
     */
    @Override
    public Edo selectEdoById(Long id)
    {
        return edoMapper.selectEdoById(id);
    }

    /**
     * Get Exchange Delivery Order List
     * 
     * @param edo Exchange Delivery Order
     * @return Exchange Delivery Order
     */
    @Override
    public List<Edo> selectEdoList(Edo edo)
    {
        return edoMapper.selectEdoList(edo);
    }

    /**
     * Add Exchange Delivery Order
     * 
     * @param edo Exchange Delivery Order
     * @return result
     */
    @Override
    public int insertEdo(Edo edo)
    {
        edo.setCreateTime(DateUtils.getNowDate());
        return edoMapper.insertEdo(edo);
    }

    /**
     * Update Exchange Delivery Order
     * 
     * @param edo Exchange Delivery Order
     * @return result
     */
    @Override
    public int updateEdo(Edo edo)
    {
        edo.setUpdateTime(DateUtils.getNowDate());
        return edoMapper.updateEdo(edo);
    }

    /**
     * Delete Exchange Delivery Order By ID
     * 
     * @param ids Entity ID
     * @return result
     */
    @Override
    public int deleteEdoByIds(String ids)
    {
        return edoMapper.deleteEdoByIds(Convert.toStrArray(ids));
    }

    /**
     * Delete Exchange Delivery Order
     * 
     * @param id Exchange Delivery OrderID
     * @return result
     */
    @Override
    public int deleteEdoById(Long id)
    {
        return edoMapper.deleteEdoById(id);
    }

    @Override
    public Edo checkContainerAvailable(@Param("container") String cont,@Param("billNo") String billNo)
    {
        return edoMapper.checkContainerAvailable(cont,billNo);
	}
	
	@Override
	public List<Edo> selectEdoListByBillNo(Edo edo)
	{
		return edoMapper.selectEdoListByBillNo(edo);
	}

    @Override
    public  List<Edo> readEdi(String[] text)
    {
        Edo edi = new Edo();
		ZoneId defaultZoneId = ZoneId.systemDefault();
		JSONObject obj = new JSONObject();
		List<Edo> listEdi = new ArrayList<>();
		System.out.print(text.toString());
		String business = "";
		for(String s : text)
		{
			
			//businessUnit
			if(s.contains("UNB+UNOA"))
			{
				String[] businessUnit = s.split("\\+");
				if(!s.isEmpty())
				{
					obj.put("businessUnit", businessUnit[2]);
					business = businessUnit[2];
				}
				continue;
			}
			if(s.contains("UNH"))
			{
				edi = new Edo();
				edi.setBusinessUnit(business);
				edi.setCarrierCode(business);
				continue;
			}

			//Bill Of Lading
			if(s.contains("RFF+BM"))
			{
				if(!s.isEmpty() && s.length() > 7)
				{
					s = s.substring(7,s.length());
					obj.put("buildNo", s);
					edi.setBillOfLading(s);
				}
				continue;
				
			}
		
			//contNo
			if(s.contains("EQD+CN"))
			{
			
				String[] contNo = s.split("\\+");
				if(!s.isEmpty())
				{
					obj.put("contNo",contNo[2]);
                	edi.setContainerNumber(contNo[2]);
				}
				continue;
			}
			//orderNo
			if(s.contains("RFF+AAJ"))	
			{
				if(!s.isEmpty() && s.length() > 8)
				{
					s = s.substring(8,s.length());
					obj.put("orderNo", s);
					edi.setOrderNumber(s);
				}
				continue;
			}
			//releaseTo
			if(s.contains("NAD+BJ"))
			{
				String[] releaseTo = s.split("\\+");
				if(!releaseTo[3].isEmpty())
				{
					releaseTo[3] = releaseTo[3].substring(0, releaseTo[3].length());
					obj.put("releaseTo", releaseTo[3]);
					edi.setConsignee(releaseTo[3]);
				}
				continue;
			}
			//validToDay
			if(s.contains("DTM+400"))
			{
				String[] validToDay = s.split("\\:");
				if(!validToDay[1].isEmpty())
				{
					validToDay[1] = validToDay[1].substring(0, validToDay[1].length() - 4);
					LocalDate date = LocalDate.parse(validToDay[1], DateTimeFormatter.BASIC_ISO_DATE);
					Date releaseDate = Date.from(date.atStartOfDay(defaultZoneId).toInstant());
					releaseDate.setHours(23);
					releaseDate.setMinutes(59);
					releaseDate.setSeconds(59);
					obj.put("validToDay", releaseDate);
					edi.setExpiredDem(releaseDate);
				}
				continue;
			}                                                                                                                                                                                                                                                                                                                                                                                           
			//emptyContDepot
			if(s.contains("LOC+99"))
			{
				String[] emptyContDepotA = s.split("\\+");
				if(emptyContDepotA.length >= 4){
					String[] emptyContDepot = emptyContDepotA[3].split(":");
					obj.put("emptyContDepot", emptyContDepot[0]);
					edi.setEmptyContainerDepot(emptyContDepot[0]);
				}
				continue;
						
			}
			//haulage
			if(s.contains("FTX+AAI"))
			{
				String[] haulage = s.split("\\+");
				haulage[4] = haulage[4].substring(0, haulage[4].length());
				
                if(!haulage[4].isEmpty()){
                    int i = Integer.parseInt(haulage[4]);
					edi.setDetFreeTime(i);
					obj.put("haulage", haulage[4]);
				}
				continue; 
			}

			//haulage
			if(s.contains("TDT+20"))
			{
				String[] infoDTD20 = s.split("\\:");
				if(infoDTD20.length > 5)
				{
					String [] voyNo = infoDTD20[0].split("\\+");
					String [] vesselNo = infoDTD20[2].split("\\+");
					edi.setVoyNo(voyNo[2]);
					edi.setVesselNo(vesselNo[3]);
					obj.put("voyNo", voyNo[2]);
					obj.put("vesselNo", vesselNo[3]);
				}
				
				continue; 
			}

			if(s.contains("UNT"))
			{
				listEdi.add(edi);
			}

			
		}
		return listEdi;
	}
	@Override
	public String getOpeCodeByBlNo(String blNo) {
		return edoMapper.getOpeCodeByBlNo(blNo);
	}

	@Override
	public Long getCountContainerAmountByBlNo(String blNo) {
		return edoMapper.getCountContainerAmountByBlNo(blNo);
	}


	@Override
	public File getFolderUploadByTime(String folderLoad) {
		LocalDate toDay = LocalDate.now();
		String year = Integer.toString(toDay.getYear());
		String month = Integer.toString(toDay.getMonthValue());
		String day = Integer.toString(toDay.getDayOfMonth());
        File folderUpload = new File(folderLoad  + File.separator +  year + File.separator +  month + File.separator +  day + File.separator);
        if (!folderUpload.exists()) {
          folderUpload.mkdirs();
        }
        return folderUpload;
	}

	@Override
	public Edo selectFirstEdo(Edo edo) {
		return edoMapper.selectFirstEdo(edo);
	}


	public List<String> selectVesselNo()
	{
		return edoMapper.selectVesselNo();
	}

	public List<String> selectCarrierCode()
	{
		return edoMapper.selectCarrierCode();
	} 

	public List<String> selectVoyNo()
	{
		return edoMapper.selectVoyNo();
	} 
    

}
