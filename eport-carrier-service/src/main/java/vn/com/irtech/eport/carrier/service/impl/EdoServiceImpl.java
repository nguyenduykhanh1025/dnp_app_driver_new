package vn.com.irtech.eport.carrier.service.impl;

import java.io.File;
import java.text.SimpleDateFormat;
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
import vn.com.irtech.eport.common.utils.StringUtils;


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
		if(edo.getDetFreeTime() == null || edo.getDetFreeTime() == "")
		{
			edo.setDetFreeTime("0");
		}
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
		String carrierCode = "";
		Date fileCreateTime = new Date();
		boolean checkBgm = true;
		for(String s : text)
		{
			//carrierCode and createTime
			if(s.contains("UNB+UNOA")) {
				String[] carrierCodes = s.split("\\+");
				if(carrierCodes.length > 2) {
					carrierCode = carrierCodes[2];
				}
				if(carrierCodes.length > 4) {
					String strTime = carrierCodes[4].replaceAll(":", "");
					try {
						fileCreateTime = new SimpleDateFormat("yyMMddhhmm").parse(strTime);
					} catch(Exception ex) {
						ex.printStackTrace();
					}
				}
				continue;
			}
			// Header
			if(s.contains("UNH")) {
				edi = new Edo();
				edi.setCarrierCode(carrierCode);
				edi.setFileCreateTime(fileCreateTime);
				continue;
			}
			// Begin message
			if(s.contains("BGM+12")) {
				String[] checkMess = s.split("\\+");
				if(checkMess.length == 5 && checkMess[3].equals("3")) {
					checkBgm = false;
				}
				continue;
			}
			//Bill Of Lading: RFF+BM:<BL_NO>
			if(s.contains("RFF+BM")) {
				String[] blNo = s.split("\\+");
				if(blNo.length > 1 && StringUtils.isNotEmpty(blNo[1]) && blNo[1].split("\\:").length > 1) {
					edi.setBillOfLading(blNo[1].split("\\:")[1]);
				}
				continue;
			}
			//contNo
			if(s.contains("EQD+CN")) {
				String[] contNo = s.split("\\+");
				if(contNo.length > 2) {
					edi.setContainerNumber(contNo[2]);
				}
				if(contNo.length > 3) {
					edi.setSztp(contNo[3].split("\\:")[0]);
				}
				continue;
			}
			//orderNo
			if(s.contains("RFF+AAJ")) {
				String[] orderNo = s.split("\\+");
				if(StringUtils.isNotEmpty(orderNo[1]) && orderNo[1].split("\\:").length > 1) {
					edi.setOrderNumber(orderNo[1].split("\\:")[1]);
				}
				continue;
			}
			//releaseTo
			if(s.contains("NAD+BJ")) {
				String[] releaseTo = s.split("\\+");
				if(releaseTo.length > 3 && StringUtils.isNotEmpty(releaseTo[3])) {
					edi.setConsignee(releaseTo[3].replace(":", ""));
				}
				continue;
			}
			//validToDay// DTM+400:202007122359:203
			if(s.contains("DTM+400")) {
				String[] validToDay = s.split("\\:");
				if(validToDay.length > 1 && !validToDay[1].isEmpty()) {
					validToDay[1] = validToDay[1].substring(0, 8);
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
			if(s.contains("LOC+99")) {
				String[] emptyContDepotA = s.split("\\+");
				if(emptyContDepotA.length > 3){
					edi.setEmptyContainerDepot(emptyContDepotA[3].split(":")[0]);
				}
				continue;		
			}
			//Unloading port
			if(s.contains("LOC+170")) {
				String[] unloadingPorts = s.split("\\+");
				if(unloadingPorts.length > 3){
					// POL: LOC+170+VNDAD:139:6+DA NANG:TER:ZZZ
					String pol = unloadingPorts[2].split(":")[0]; // VNDAD
					String polName = unloadingPorts[3].split(":")[0];
					edi.setPol(pol);
					edi.setPolName(polName);
				}
				continue;		
			}
			// Pick-up location
			if(s.contains("LOC+176")) {
				String[] pickUpLocations = s.split("\\+");
				if(pickUpLocations.length > 3){
					// POD: LOC+176+VNDAD:139:6+DA NANG:TER:ZZZ
					String pod = pickUpLocations[2].split(":")[0];  // VNDAD
					String podName = pickUpLocations[3].split(":")[0];
					edi.setPod(pod);
					edi.setPodName(podName);
				}
				continue;		
			}
			//haulage // DET FREE TIME // FTX+AAI+++7
			if(s.contains("FTX+AAI")) {
				String[] haulage = s.split("\\+");
				if(haulage.length > 4 && StringUtils.isNotEmpty(haulage[4])) {
					edi.setDetFreeTime(haulage[4]);
				}
				continue; 
			}
			//Voy and vessel
			if(s.contains("TDT+20")) {
				String[] infoDTD20 = s.split("\\+");
				if(infoDTD20.length > 2) {
					edi.setVoyNo(infoDTD20[2]);
				}
				if(infoDTD20.length > 5) {
					String [] businessUnit = infoDTD20[5].split("\\:");
					edi.setBusinessUnit(businessUnit[0]);
				}
				if(infoDTD20.length > 8) {
					String [] vessel = infoDTD20[8].split("\\:");
					edi.setVesselNo(vessel[0]);
					if(vessel.length > 3) {
						edi.setVessel(vessel[3]);
					}
				}
				continue; 
			}
			// Tail
			if(s.contains("UNT")) {
				if(checkBgm) {
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

	public List<Edo> selectOprCode(Edo edo)
	{
		return edoMapper.selectOprCode(edo);
	} 

	public List<Edo> selectVoyNos(Edo edo)
	{
		return edoMapper.selectVoyNos(edo);
	} 

	public List<Edo> selectVessels(Edo edo)
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

	public Edo getBillOfLadingInfo(Edo edo) {
		return edoMapper.getBillOfLadingInfo(edo);
	}
	
	/**
     * Select list edo by ids
     * 
     * @param ids
     * @return List<Edo>
     */
	@Override
    public List<Edo> selectEdoByIds(String ids) {
		return edoMapper.selectEdoByIds(Convert.toStrArray(ids));
	}
	
	/**
     * Update edo by condition
     * 
     * @param edo
     * @return int
     */
	@Override
    public int updateEdoByCondition(Edo edo) {
		return edoMapper.updateEdoByCondition(edo);
	}
}
