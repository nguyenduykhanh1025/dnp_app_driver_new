package vn.com.irtech.eport.carrier.service.impl;

import java.io.File;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vn.com.irtech.eport.carrier.domain.Edo;
import vn.com.irtech.eport.carrier.dto.EdoWithoutHouseBillReq;
import vn.com.irtech.eport.carrier.mapper.EdoMapper;
import vn.com.irtech.eport.carrier.service.IEdoService;
import vn.com.irtech.eport.common.core.text.Convert;
import vn.com.irtech.eport.common.exception.BusinessException;
import vn.com.irtech.eport.common.utils.DateUtils;


/**
 * Exchange Delivery OrderService Business Processing
 * 
 * @author irtech
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
		Edo edoCheck = new Edo();
		edoCheck.setContainerNumber(edo.getContainerNumber());
		edoCheck.setBillOfLading(edo.getBillOfLading());
		edoCheck.setDelFlg(0);
		 if (selectFirstEdo(edo) != null) {
			 throw new BusinessException(String.format("Edo to insert was existed"));
		 }
		edo.setStatus("1");
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
		if(edo.getExpiredDem() != null)
		{
			Date setTimeUpdatExpicedDem = edo.getExpiredDem();
			setTimeUpdatExpicedDem.setHours(23);
			setTimeUpdatExpicedDem.setMinutes(59);
			setTimeUpdatExpicedDem.setSeconds(59);
			edo.setExpiredDem(setTimeUpdatExpicedDem);
		}
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
		List<Edo> listEdi = new ArrayList<>();
		String business = "";
		Date fileCreateTime = new Date();
		boolean checkBgm = true;
		for(String s : text)
		{
			//businessUnit and createTime
			if(s.contains("UNB+UNOA"))
			{
				String[] businessUnit = s.split("\\+");
				if(businessUnit.length > 2)
				{
					business = businessUnit[2];
				}
				if(businessUnit.length > 4)
				{
					String [] timeInfo = businessUnit[4].split("\\:");
					if(timeInfo.length > 1)
					{
						fileCreateTime.setYear(Integer.parseInt("20"+timeInfo[0].substring(0,2))-1900);
						fileCreateTime.setMonth(Integer.parseInt(timeInfo[0].substring(2,4))-1);
						fileCreateTime.setDate(Integer.parseInt(timeInfo[0].substring(4,6)));
						fileCreateTime.setHours(Integer.parseInt(timeInfo[1].substring(0,2)));
						fileCreateTime.setMinutes(Integer.parseInt(timeInfo[1].substring(2,4)));
						fileCreateTime.setSeconds(00);
					}
				}
				continue;
			}
			
			if(s.contains("UNH"))
			{
				edi = new Edo();
				edi.setBusinessUnit(business);
				edi.setFileCreateTime(fileCreateTime);
				continue;
			}

			if(s.contains("BGM+12"))
			{
				String[] checkMess = s.split("\\+");
				if(checkMess.length == 5 && checkMess[3].equals("3"))
				{
					checkBgm = false;
				}
				continue;
			}
			//Bill Of Lading
			if(s.contains("RFF+BM"))
			{
				if(!s.isEmpty() && s.length() > 7)
				{
					s = s.substring(7,s.length());
					edi.setBillOfLading(s);
				}
				continue;
				
			}
		
			//contNo
			if(s.contains("EQD+CN"))
			{
			
				String[] contNo = s.split("\\+");
				if(contNo.length >= 2)
				{
                	edi.setContainerNumber(contNo[2]);
				}
				if(contNo.length >= 3)
				{
					String[] sztp = contNo[3].split("\\:");
                	edi.setSztp(sztp[0]);
				}
				continue;
			}
			//orderNo
			if(s.contains("RFF+AAJ"))	
			{
				if(!s.isEmpty() && s.length() > 8)
				{
					s = s.substring(8,s.length());
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
					String rs = releaseTo[3].replace(":", "");
					edi.setConsignee(rs);
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
					edi.setEmptyContainerDepot(emptyContDepot[0]);
				}
				continue;		
			}
			//Unloading port
			if(s.contains("LOC+170"))
			{
				String[] unloadingPorts = s.split("\\+");
				if(unloadingPorts.length >= 4){
					// Split POL: LOC+170+VNDAD:139:6+DA NANG:TER:ZZZ
					String pol = unloadingPorts[2].split(":")[0]; // VNDAD
					String polName = unloadingPorts[3].split(":")[0];
					edi.setPol(pol);
					edi.setPolName(polName);
					//String[] unloadingPort = unloadingPorts[3].split(":");
					//edi.setPol(unloadingPort[0]);
				}
				continue;		
			}
			// Pick-up location
			if(s.contains("LOC+176"))
			{
				String[] pickUpLocations = s.split("\\+");
				if(pickUpLocations.length >= 4){
					// LOC+176+VNDAD:139:6+DA NANG:TER:ZZZ
					String pod = pickUpLocations[2].split(":")[0];  // VNDAD
					String podName = pickUpLocations[3].split(":")[0];
					edi.setPod(pod);
					edi.setPodName(podName);
					//String[] pickUpLocation = pickUpLocations[3].split(":");
					//edi.setPod(pickUpLocation[0]);
				}
				continue;		
			}
			//haulage
			if(s.contains("FTX+AAI"))
			{
				String[] haulage = s.split("\\+");
				// TODO check null
				haulage[4] = haulage[4].substring(0, haulage[4].length());
				if (!haulage[4].isEmpty()) {
					try{
//						int i = Integer.parseInt(haulage[4]);
//						edi.setDetFreeTime(i);
						edi.setDetFreeTime(haulage[4]);
					}catch (Exception e) {
						e.printStackTrace();
					}
				}
				continue; 
			}

			//Voy and vessel
			if(s.contains("TDT+20"))
			{
				String[] infoDTD20 = s.split("\\+");
				if(infoDTD20.length > 8)
				{
					String [] vessel = infoDTD20[8].split("\\:");
					String [] carrierCode = infoDTD20[5].split("\\:");
					edi.setCarrierCode(carrierCode[0]);
					edi.setVoyNo(infoDTD20[2]);
					edi.setVesselNo(vessel[0]);
					edi.setVessel(vessel[3]); 
				}
				continue; 
			}

			if(s.contains("UNT"))
			{
				if(checkBgm)
				{
					listEdi.add(edi);
				}
				checkBgm = true;
				
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


	public List<String> selectVesselNo(Edo edo)
	{
		return edoMapper.selectVesselNo(edo);
	}

	public List<String> selectOprCode(Edo edo)
	{
		return edoMapper.selectOprCode(edo);
	} 

	public List<String> selectVoyNos(Edo edo)
	{
		return edoMapper.selectVoyNos(edo);
	} 

	public List<String> selectVessels(Edo edo)
	{
		return edoMapper.selectVessels(edo);
	}
	
    /**
     * Update edo by bill of lading and container no
     * 
     * @param edo
     * @return
     */
	@Override
    public int updateEdoByBlCont(Edo edo) {
		return edoMapper.updateEdoByBlCont(edo);
	}

	/**
	 * Get list Edo without house bill id
	 * @param edo
	 * @return
	 */
	@Override
	public List<Edo> selectListEdoWithoutHouseBillId(EdoWithoutHouseBillReq edo)
	{
		return edoMapper.selectListEdoWithoutHouseBillId(edo);
	}

	/**
     * Get container amount with order number
     * 
     * @param blNo
     * @param orderNumber
     * @return int
     */
	@Override
    public int getContainerAmountWithOrderNumber(String blNo, String orderNumber) {
		return edoMapper.getContainerAmountWithOrderNumber(blNo, orderNumber);
	}
	
	/**
     * Get bill of lading by house bill id
     * 
     * @param houseBillId
     * @return String
     */
	@Override
    public String getBlNoByHouseBillId(Long houseBillId) {
		return edoMapper.getBlNoByHouseBillId(houseBillId);
	}

    public List<Edo> selectEdoListForReport(Edo edo)
    {
        return edoMapper.selectEdoListForReport(edo);
    }

    /**
	 * Select list edo with house bill req
	 * 
	 * @param edo
	 * @return List<Edo>
	 */
    @Override
	public List<Edo> selectListEdoWithHouseBill(EdoWithoutHouseBillReq edo) {
		return edoMapper.selectListEdoWithHouseBill(edo);
	}

	public Map<String, String> getReportByCarrierGroup(String[] codes) {
		return edoMapper.getReportByCarrierGroup(codes);
	}

	public Edo getBillOfLadingInfo(String blNo) {
		return edoMapper.getBillOfLadingInfo(blNo);
	}
	
}
