package vn.com.irtech.eport.logistic.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.collections.CollectionUtils;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;

import vn.com.irtech.eport.common.config.Global;
import vn.com.irtech.eport.common.constant.EportConstants;
import vn.com.irtech.eport.common.core.text.Convert;
import vn.com.irtech.eport.common.utils.CacheUtils;
import vn.com.irtech.eport.common.utils.DateUtils;
import vn.com.irtech.eport.common.utils.StringUtils;
import vn.com.irtech.eport.logistic.domain.ProcessOrder;
import vn.com.irtech.eport.logistic.domain.Shipment;
import vn.com.irtech.eport.logistic.domain.ShipmentDetail;
import vn.com.irtech.eport.logistic.dto.ProcessJsonData;
import vn.com.irtech.eport.logistic.dto.ServiceSendFullRobotReq;
import vn.com.irtech.eport.logistic.dto.ShipmentWaitExec;
import vn.com.irtech.eport.logistic.form.BookingInfo;
import vn.com.irtech.eport.logistic.form.PickupAssignForm;
import vn.com.irtech.eport.logistic.mapper.ShipmentDetailMapper;
import vn.com.irtech.eport.logistic.service.ICatosApiService;
import vn.com.irtech.eport.logistic.service.IProcessOrderService;
import vn.com.irtech.eport.logistic.service.IShipmentDetailService;
import vn.com.irtech.eport.system.domain.SysDictData;
import vn.com.irtech.eport.system.dto.ContainerInfoDto;
import vn.com.irtech.eport.system.dto.PartnerInfoDto;
import vn.com.irtech.eport.system.service.ISysDictDataService;
import vn.com.irtech.eport.system.service.ISysDictTypeService;

/**
 * Shipment DetailsService Business Processing
 * 
 * @author admin
 * @date 2020-05-07
 */
@Service
public class ShipmentDetailServiceImpl implements IShipmentDetailService {

	private static final Logger logger = LoggerFactory.getLogger(ShipmentDetailServiceImpl.class);

	@Autowired
	private ShipmentDetailMapper shipmentDetailMapper;

	@Autowired
	private IProcessOrderService processOrderService;

	@Autowired
	private ISysDictTypeService dictTypeService;

	@Autowired
	private ICatosApiService catosApiService;

	@Autowired
	private ISysDictDataService sysDictDataService;

	// @Autowired
	// private IShipmentService shipmentService;

	class BayComparator implements Comparator<ShipmentDetail> {
		public int compare(ShipmentDetail shipmentDetail1, ShipmentDetail shipmentDetail2) {
			// In the following line you set the criterion,
			// which is the name of Contact in my example scenario
			return shipmentDetail1.getBay().compareTo(shipmentDetail2.getBay());
		}
	}

	class SztpComparator implements Comparator<ShipmentDetail> {
		public int compare(ShipmentDetail shipmentDetail1, ShipmentDetail shipmentDetail2) {
			// In the following line you set the criterion,
			// which is the name of Contact in my example scenario
			return shipmentDetail1.getSztp().compareTo(shipmentDetail2.getSztp());
		}
	}

	class OrderNoComparator implements Comparator<ShipmentDetail> {
		public int compare(ShipmentDetail shipmentDetail1, ShipmentDetail shipmentDetail2) {
			// In the following line you set the criterion,
			// which is the name of Contact in my example scenario
			return shipmentDetail1.getOrderNo().compareTo(shipmentDetail2.getOrderNo());
		}
	}

	/**
	 * Get trucker from tax code in dictionary defined before If defining trucker in
	 * dictionary is not exists then return tax code as default
	 * 
	 * @param taxCode
	 * @return
	 */
	private String getTruckerFromRegNoCatos(String taxCode) {
		String trucker = sysDictDataService.selectDictLabel("carrier_trucker_list", taxCode);
		if (StringUtils.isNotEmpty(trucker)) {
			return trucker;
		}
		// Convert taxCode to ptnr code
		String ptnrCode = catosApiService.getTruckerByTaxCode(taxCode);
		if (StringUtils.isNotEmpty(ptnrCode)) {
			return ptnrCode;
		}
		return taxCode;
	}

	/**
	 * Get Shipment Details
	 * 
	 * @param id Shipment DetailsID
	 * @return Shipment Details
	 */
	@Override
	public ShipmentDetail selectShipmentDetailById(Long id) {
		return shipmentDetailMapper.selectShipmentDetailById(id);
	}

	/**
	 * Get Shipment Details List
	 * 
	 * @param shipmentDetail Shipment Details
	 * @return Shipment Details
	 */
	@Override
	public List<ShipmentDetail> selectShipmentDetailList(ShipmentDetail shipmentDetail) {
		return shipmentDetailMapper.selectShipmentDetailList(shipmentDetail);
	}

	/**
	 * Add Shipment Details
	 * 
	 * @param shipmentDetail Shipment Details
	 * @return result
	 */
	@Override
	public int insertShipmentDetail(ShipmentDetail shipmentDetail) {
		shipmentDetail.setCreateTime(DateUtils.getNowDate());
		return shipmentDetailMapper.insertShipmentDetail(shipmentDetail);
	}

	/**
	 * Update Shipment Details
	 * 
	 * @param shipmentDetail Shipment Details
	 * @return result
	 */
	@Override
	public int updateShipmentDetail(ShipmentDetail shipmentDetail) {
		shipmentDetail.setUpdateTime(DateUtils.getNowDate());
		return shipmentDetailMapper.updateShipmentDetail(shipmentDetail);
	}

	/**
	 * Delete Shipment Details By ID
	 * 
	 * @param ids Entity ID
	 * @return result
	 */
	@Override
	public int deleteShipmentDetailByIds(Long shipmentId, String shipmentDetailIds, Long logisticGroupId) {
		return shipmentDetailMapper.deleteShipmentDetailByIds(shipmentId, Convert.toStrArray(shipmentDetailIds),
				logisticGroupId);
	}

	/**
	 * Delete Shipment Details
	 * 
	 * @param id Shipment DetailsID
	 * @return result
	 */
	@Override
	public int deleteShipmentDetailById(Long id) {
		return shipmentDetailMapper.deleteShipmentDetailById(id);
	}

	@Override
	public List<ShipmentDetail> selectShipmentDetailByIds(String ids, Long logisticGroupId) {
		return shipmentDetailMapper.selectShipmentDetailByIds(Convert.toStrArray(ids), logisticGroupId);
	}

	public long countShipmentDetailList(ShipmentDetail shipmentDetail) {
		return shipmentDetailMapper.countShipmentDetailList(shipmentDetail);
	}

	/**
	 * Select list shipment detail wait robot execute or robot can't be execute,
	 * group by shipment id
	 * 
	 * @return result
	 */
	@Override
	public List<ShipmentWaitExec> selectListShipmentWaitExec() {
		return shipmentDetailMapper.selectListShipmentWaitExec();
	}

	@Override
	public List<ShipmentDetail[][]> getContPosition(String blNo, List<ShipmentDetail> shipmentDetails) {
		List<ContainerInfoDto> containerInfoDtos = catosApiService.selectShipmentDetailsByBLNo(blNo);

		// simulating the location of container in da nang port, mapping to matrix
		// Update yard position for list container
		for (ShipmentDetail shipmentDetail : shipmentDetails) {
			for (int i = 0; i < containerInfoDtos.size(); i++) {
				if (shipmentDetail.getContainerNo().equals(containerInfoDtos.get(i).getCntrNo())) {
					String[] positionArr = containerInfoDtos.get(i).getLocation().split("-");
					if (positionArr.length == 4) {
						shipmentDetail.setBlock(positionArr[0]);
						shipmentDetail.setBay(positionArr[1]);
						shipmentDetail.setRow(Integer.parseInt(positionArr[2]));
						shipmentDetail.setTier(Integer.parseInt(positionArr[3]));
						containerInfoDtos.remove(i);
						i--;
					}
				}
			}
		}

		// Add the rest unknown container in list
		for (int i = 0; i < containerInfoDtos.size(); i++) {
			String[] positionArr = containerInfoDtos.get(i).getLocation().split("-");
			if (positionArr.length == 4) {
				ShipmentDetail shipmentDetail2 = new ShipmentDetail();
				shipmentDetail2.setBlock(positionArr[0]);
				shipmentDetail2.setBay(positionArr[1]);
				shipmentDetail2.setRow(Integer.parseInt(positionArr[2]));
				shipmentDetail2.setTier(Integer.parseInt(positionArr[3]));
				containerInfoDtos.remove(i);
				i--;
			}
		}

		// Sorting list container by bay
		Collections.sort(shipmentDetails, new BayComparator());

		// List that store the max value index row.
		List<Integer> maxRowList = new ArrayList<>();

		// Set the max value of each bay into maxRowList
		String currentBay = shipmentDetails.get(0).getBay();
		Integer maxRow = shipmentDetails.get(0).getRow();
		for (ShipmentDetail shipmentDetail : shipmentDetails) {
			if (currentBay.equals(shipmentDetail.getBay())) {
				if (maxRow < shipmentDetail.getRow()) {
					maxRow = shipmentDetail.getRow();
				}
			} else {
				maxRowList.add(maxRow);
				currentBay = shipmentDetail.getBay();
				maxRow = shipmentDetail.getRow();
			}
		}
		maxRowList.add(maxRow);

		// Declare bayList to store multiple matrix of position for each bay
		List<ShipmentDetail[][]> bayList = new ArrayList<>();
		ShipmentDetail[][] shipmentDetailMatrix = new ShipmentDetail[5][maxRowList.get(0)];

		int i = 0;
		currentBay = shipmentDetails.get(0).getBay();
		for (ShipmentDetail shipmentDetail : shipmentDetails) {
			if (currentBay.equals(shipmentDetail.getBay())) {
				shipmentDetailMatrix[shipmentDetail.getTier() - 1][shipmentDetail.getRow() - 1] = shipmentDetail;
			} else {
				bayList.add(shipmentDetailMatrix);
				currentBay = shipmentDetail.getBay();
				shipmentDetailMatrix = new ShipmentDetail[5][maxRowList.get(++i)];
				shipmentDetailMatrix[shipmentDetail.getTier() - 1][shipmentDetail.getRow() - 1] = shipmentDetail;
			}
		}
		bayList.add(shipmentDetailMatrix);

		return bayList;
	}

	@Override
	@Transactional
	public List<ServiceSendFullRobotReq> calculateMovingCont(List<ContainerInfoDto> containerInfoDtos,
			List<ShipmentDetail> preorderPickupConts, List<ShipmentDetail> shipmentDetails, Shipment shipment,
			Boolean isCredit) {
		try {

			// simulating the location of container in da nang port, mapping to matrix
			// Update yard position for list container
			for (ShipmentDetail shipmentDetail : shipmentDetails) {
				for (int i = 0; i < containerInfoDtos.size(); i++) {
					if (shipmentDetail.getContainerNo().equals(containerInfoDtos.get(i).getCntrNo())) {
						String[] positionArr = containerInfoDtos.get(i).getLocation().split("-");
						if (positionArr.length == 4) {
							shipmentDetail.setBlock(positionArr[0]);
							shipmentDetail.setBay(positionArr[1]);
							shipmentDetail.setRow(Integer.parseInt(positionArr[2]));
							shipmentDetail.setTier(Integer.parseInt(positionArr[3]));
							containerInfoDtos.remove(i);
							i--;
						}
					}
				}
			}

			// Add the rest unknown container in list
			for (int i = 0; i < containerInfoDtos.size(); i++) {
				String[] positionArr = containerInfoDtos.get(i).getLocation().split("-");
				if (positionArr.length == 4) {
					ShipmentDetail shipmentDetail2 = new ShipmentDetail();
					shipmentDetail2.setBlock(positionArr[0]);
					shipmentDetail2.setBay(positionArr[1]);
					shipmentDetail2.setRow(Integer.parseInt(positionArr[2]));
					shipmentDetail2.setTier(Integer.parseInt(positionArr[3]));
					containerInfoDtos.remove(i);
					i--;
				}
			}

			// Sorting list container by bay
			Collections.sort(shipmentDetails, new BayComparator());

			// List that store the max value index row.
			List<Integer> maxRowList = new ArrayList<>();

			// Set the max value of each bay into maxRowList
			String currentBay = shipmentDetails.get(0).getBay();
			Integer maxRow = shipmentDetails.get(0).getRow();
			for (ShipmentDetail shipmentDetail : shipmentDetails) {
				if (currentBay.equals(shipmentDetail.getBay())) {
					if (maxRow < shipmentDetail.getRow()) {
						maxRow = shipmentDetail.getRow();
					}
				} else {
					maxRowList.add(maxRow);
					currentBay = shipmentDetail.getBay();
					maxRow = shipmentDetail.getRow();
				}
			}
			maxRowList.add(maxRow);

			// Declare bayList to store multiple matrix of position for each bay
			List<ShipmentDetail[][]> bayList = new ArrayList<>();
			ShipmentDetail[][] shipmentDetailMatrix = new ShipmentDetail[5][maxRowList.get(0)];

			int i = 0;
			currentBay = shipmentDetails.get(0).getBay();
			for (ShipmentDetail shipmentDetail : shipmentDetails) {
				if (currentBay.equals(shipmentDetail.getBay())) {
					shipmentDetailMatrix[shipmentDetail.getTier() - 1][shipmentDetail.getRow() - 1] = shipmentDetail;
				} else {
					bayList.add(shipmentDetailMatrix);
					currentBay = shipmentDetail.getBay();
					shipmentDetailMatrix = new ShipmentDetail[5][maxRowList.get(++i)];
					shipmentDetailMatrix[shipmentDetail.getTier() - 1][shipmentDetail.getRow() - 1] = shipmentDetail;
				}
			}
			bayList.add(shipmentDetailMatrix);

			// Collect list cont need to shifting
			List<Long> prePickupContIds = new ArrayList<>();
			List<ShipmentDetail> shiftingContList = new ArrayList<>();
			for (int b = 0; b < bayList.size(); b++) {
				List<ShipmentDetail> tempShiftingContList = new ArrayList<>();
				int level = 1;
				for (int row = 0; row < bayList.get(b)[0].length; row++) {
					if (row > 5 && level == 1) {
						tempShiftingContList = new ArrayList<>();
					}
					for (int tier = 4; tier >= 0; tier--) {
						if (bayList.get(b)[tier][row] != null) {
							Boolean needMoving = true;
							for (ShipmentDetail shipmentDetail : preorderPickupConts) {
								if (Objects.equals(bayList.get(b)[tier][row].getId(), shipmentDetail.getId())) {
									shipmentDetail.setPreorderPickup("Y");
									shipmentDetail.setPrePickupPaymentStatus("N");
									prePickupContIds.add(shipmentDetail.getId());
									shipmentDetailMapper.updateShipmentDetail(shipmentDetail);
									shiftingContList.addAll(tempShiftingContList);
									tempShiftingContList.clear();
									needMoving = false;
									break;
								}
							}
							if ("N".equals(bayList.get(b)[tier][row].getPreorderPickup()) && needMoving) {
								tempShiftingContList.add(bayList.get(b)[tier][row]);
							}
						}
					}
					tempShiftingContList.clear();
				}
			}

			// Seperate cont with different sztp
			List<ServiceSendFullRobotReq> serviceRobotReq = new ArrayList<>();
			Collections.sort(shiftingContList, new SztpComparator());
			String sztp = shiftingContList.get(0).getSztp();
			List<ShipmentDetail> shipmentOrderList = new ArrayList<>();
			for (ShipmentDetail shipmentDetail : shiftingContList) {
				if (!sztp.equals(shipmentDetail.getSztp())) {
					serviceRobotReq.add(groupShipmentDetailByShiftingContOrder(shipmentOrderList, shipment, isCredit,
							prePickupContIds));
					shipmentOrderList = new ArrayList<>();
				}
				Integer index = catosApiService.getIndexContForSsrByContainerNo(shipmentDetail.getContainerNo());
				if (index == null) {
					index = 1;
				}
				shipmentDetail.setIndex(index);
				shipmentOrderList.add(shipmentDetail);
			}
			serviceRobotReq.add(
					groupShipmentDetailByShiftingContOrder(shipmentOrderList, shipment, isCredit, prePickupContIds));

			return serviceRobotReq;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public ServiceSendFullRobotReq groupShipmentDetailByShiftingContOrder(List<ShipmentDetail> shipmentDetails,
			Shipment shipment, Boolean isCredit, List<Long> prePickupContIds) {
		ProcessOrder processOrder = new ProcessOrder();
		// FIXME
		processOrder.setTaxCode(shipmentDetails.get(0).getPayer());
		processOrder.setShipmentId(shipment.getId());
		processOrder.setLogisticGroupId(shipment.getLogisticGroupId());
		processOrder.setServiceType(5);
		if (isCredit) {
			processOrder.setPayType("Credit");
		} else {
			processOrder.setPayType("Cash");
		}
		processOrder.setContNumber(shipmentDetails.size());
		processOrder.setSsrCode(getSsrCodeBySztp(shipmentDetails.get(0).getSztp()));
		List<Long> shipmentDetailIds = new ArrayList<>();
		for (ShipmentDetail shipmentDetail : shipmentDetails) {
			shipmentDetailIds.add(shipmentDetail.getId());
		}
		Map<String, Object> map = new HashMap<>();
		map.put("shipmentDetailIds", shipmentDetailIds);
		map.put("prePickupContIds", prePickupContIds);
		processOrder.setProcessData(new Gson().toJson(map));
		processOrderService.insertProcessOrder(processOrder);
		return new ServiceSendFullRobotReq(processOrder, shipmentDetails);
	}

	public List<ServiceSendFullRobotReq> makeOrderReceiveContFull(List<ShipmentDetail> shipmentDetails,
			Shipment shipment, String taxCode, boolean creditFlag) {
		if (shipmentDetails.size() > 0) {
			List<ServiceSendFullRobotReq> serviceRobotReq = new ArrayList<>();
			if (checkMakeOrderByBl(shipment.getBlNo(), shipmentDetails.size(),
					"1".equalsIgnoreCase(shipment.getEdoFlg()))) {
				serviceRobotReq.add(groupShipmentDetailByReceiveContFullOrder(shipmentDetails.get(0).getId(),
						shipmentDetails, shipment, taxCode, creditFlag, true));
			} else {
				Collections.sort(shipmentDetails, new SztpComparator());
				String sztp = shipmentDetails.get(0).getSztp();
				List<ShipmentDetail> shipmentOrderList = new ArrayList<>();
				for (ShipmentDetail shipmentDetail : shipmentDetails) {
					if (!sztp.equals(shipmentDetail.getSztp())) {
						serviceRobotReq.add(groupShipmentDetailByReceiveContFullOrder(shipmentDetails.get(0).getId(),
								shipmentOrderList, shipment, taxCode, creditFlag, false));
						sztp = shipmentDetail.getSztp();
						shipmentOrderList = new ArrayList<>();
					}
					shipmentOrderList.add(shipmentDetail);
				}
				serviceRobotReq.add(groupShipmentDetailByReceiveContFullOrder(shipmentDetails.get(0).getId(),
						shipmentOrderList, shipment, taxCode, creditFlag, false));
			}
			return serviceRobotReq;
		}
		return null;
	}

	@Transactional
	private ServiceSendFullRobotReq groupShipmentDetailByReceiveContFullOrder(Long registerNo,
			List<ShipmentDetail> shipmentDetails, Shipment shipment, String taxCode, boolean creditFlag,
			boolean orderByBl) {
		ShipmentDetail detail = shipmentDetails.get(0);
		ProcessOrder processOrder = new ProcessOrder();
		if (orderByBl) {
			processOrder.setModee("Pickup Order By BL");
		} else {
			processOrder.setModee("Truck Out");
		}
		processOrder.setConsignee(detail.getConsignee());
		processOrder.setLogisticGroupId(shipment.getLogisticGroupId());
		try {
			processOrder.setTruckCo(
					getTruckerFromRegNoCatos(taxCode) + " : " + getGroupNameByTaxCode(taxCode).getGroupName());
		} catch (Exception e) {
			logger.error("Error when get company name with tax code: " + e);
		}
		processOrder.setTaxCode(taxCode);
		if (creditFlag) {
			processOrder.setPayType("Credit");
		} else {
			processOrder.setPayType("Cash");
		}
		ProcessOrder tempProcessOrder = getYearBeforeAfter(detail.getVslNm(), detail.getVoyNo());
		if (tempProcessOrder != null) {
			processOrder.setYear(tempProcessOrder.getYear());
			processOrder.setBeforeAfter(tempProcessOrder.getBeforeAfter());
		} else {
			processOrder.setYear(Integer.toString(Calendar.getInstance().get(Calendar.YEAR)));
			processOrder.setBeforeAfter("Before");
		}
		processOrder.setTrucker(getTruckerFromRegNoCatos(taxCode));
		processOrder.setBlNo(detail.getBlNo());
		processOrder.setPickupDate(detail.getExpiredDem());
		processOrder.setVessel(detail.getVslNm());
		processOrder.setVoyage(detail.getVoyNo());
		processOrder.setSztp(detail.getSztp());
		processOrder.setContNumber(shipmentDetails.size());
		processOrder.setShipmentId(shipment.getId());
		processOrder.setServiceType(1);
		processOrderService.insertProcessOrder(processOrder);
		String payer = taxCode;
		String payerName = "";
		try {
			logger.debug("Get payer name for shipment detail by tax code: " + taxCode);
			payerName = getGroupNameByTaxCode(taxCode).getGroupName();
		} catch (Exception e) {
			logger.error("Error when get payer name for " + payer + ": " + e);
		}
		for (ShipmentDetail shipmentDetail : shipmentDetails) {
			shipmentDetail.setProcessOrderId(processOrder.getId());
			shipmentDetail.setRegisterNo(registerNo.toString());
			shipmentDetail.setPayer(payer);
			shipmentDetail.setPayerName(payerName);
			if (creditFlag) {
				shipmentDetail.setPayType("Credit");
			} else {
				shipmentDetail.setPayType("Cash");
			}
			shipmentDetail.setUserVerifyStatus("Y");
			shipmentDetailMapper.updateShipmentDetail(shipmentDetail);
		}
		return new ServiceSendFullRobotReq(processOrder, shipmentDetails);
	}

	private boolean checkMakeOrderByBl(String blNo, int size, boolean edoFlag) {
		if (edoFlag) {
			if (getCountContByBlNo(blNo) == size) {
				return true;
			}
		} else {
			if (getCountContByBlNo(blNo) == size) {
				return true;
			}
		}
		return false;
	}

	@Transactional
	public List<ServiceSendFullRobotReq> makeOrderReceiveContEmpty(List<ShipmentDetail> shipmentDetails,
			Shipment shipment, String taxCode, boolean creditFlag) {
		if (shipmentDetails.size() > 0) {
			List<ServiceSendFullRobotReq> serviceRobotReq = new ArrayList<>();
			Collections.sort(shipmentDetails, new SztpComparator());
			String sztp = shipmentDetails.get(0).getSztp();
			List<ShipmentDetail> shipmentOrderList = new ArrayList<>();
			for (ShipmentDetail shipmentDetail : shipmentDetails) {
				if (!sztp.equals(shipmentDetail.getSztp())) {
					serviceRobotReq.add(groupShipmentDetailByReceiveContEmptyOrder(shipmentDetails.get(0).getId(),
							shipmentOrderList, shipment, taxCode, creditFlag));
					sztp = shipmentDetail.getSztp();
					shipmentOrderList = new ArrayList<>();
				}
				shipmentOrderList.add(shipmentDetail);
			}
			serviceRobotReq.add(groupShipmentDetailByReceiveContEmptyOrder(shipmentDetails.get(0).getId(),
					shipmentOrderList, shipment, taxCode, creditFlag));
			return serviceRobotReq;
		}
		return null;
	}

	@Transactional
	private ServiceSendFullRobotReq groupShipmentDetailByReceiveContEmptyOrder(Long registerNo,
			List<ShipmentDetail> shipmentDetails, Shipment shipment, String taxCode, boolean creditFlag) {
		ShipmentDetail detail = shipmentDetails.get(0);
		ProcessOrder processOrder = new ProcessOrder();
		processOrder.setModee("Pickup By Booking");
		processOrder.setConsignee(detail.getConsignee());
		processOrder.setLogisticGroupId(shipment.getLogisticGroupId());
		try {
			processOrder.setTruckCo(
					getTruckerFromRegNoCatos(taxCode) + " : " + getGroupNameByTaxCode(taxCode).getGroupName());
		} catch (Exception e) {
			logger.error("Error when get company name with tax code: " + e);
		}
		processOrder.setTaxCode(taxCode);
		if (creditFlag) {
			processOrder.setPayType("Credit");
		} else {
			processOrder.setPayType("Cash");
		}
		ProcessOrder tempProcessOrder = getYearBeforeAfter(detail.getVslNm(), detail.getVoyNo());
		if (tempProcessOrder != null) {
			processOrder.setYear(tempProcessOrder.getYear());
			processOrder.setBeforeAfter(tempProcessOrder.getBeforeAfter());
		} else {
			processOrder.setYear(Integer.toString(Calendar.getInstance().get(Calendar.YEAR)));
			processOrder.setBeforeAfter("Before");
		}
		processOrder.setTrucker(getTruckerFromRegNoCatos(taxCode));
		// Set booking no with booking removed all non word character
		processOrder.setBookingNo(detail.getBookingNo().replaceAll("\\W", ""));
		processOrder.setPickupDate(detail.getExpiredDem());
		processOrder.setVessel(detail.getVslNm());
		processOrder.setVoyage(detail.getVoyNo());
		processOrder.setSztp(detail.getSztp());
		processOrder.setContNumber(shipmentDetails.size());
		processOrder.setShipmentId(shipment.getId());
		processOrder.setCargoType("MT");
		processOrder.setPod(detail.getDischargePort());
		processOrder.setOpr(detail.getOpeCode());
		processOrder.setPol("VNDAD");
		processOrder.setRunnable(false);
		processOrder.setServiceType(EportConstants.SERVICE_PICKUP_EMPTY);
		processOrderService.insertProcessOrder(processOrder);
		String payer = taxCode;
		String payerName = "";
		try {
			logger.debug("Get payer name for shipment detail by tax code: " + taxCode);
			payerName = getGroupNameByTaxCode(taxCode).getGroupName();
		} catch (Exception e) {
			logger.error("Error when get payer name for " + payer + ": " + e);
		}
		for (ShipmentDetail shipmentDetail : shipmentDetails) {
			shipmentDetail.setProcessOrderId(processOrder.getId());
			shipmentDetail.setRegisterNo(registerNo.toString());
			shipmentDetail.setPayer(payer);
			shipmentDetail.setPayerName(payerName);
			if (creditFlag) {
				shipmentDetail.setPayType("Credit");
			} else {
				shipmentDetail.setPayType("Cash");
			}
			shipmentDetail.setUserVerifyStatus("Y");
			shipmentDetailMapper.updateShipmentDetail(shipmentDetail);
		}
		return new ServiceSendFullRobotReq(processOrder, shipmentDetails);
	}

	@Override
	public ProcessOrder makeOrderSendCont(List<ShipmentDetail> shipmentDetails, Shipment shipment, String taxCode,
			boolean creditFlag) {
		ShipmentDetail detail = shipmentDetails.get(0);
		ProcessOrder processOrder = new ProcessOrder();
		processOrder.setTaxCode(taxCode);
		processOrder.setContNumber(shipmentDetails.size());
		processOrder.setVessel(detail.getVslNm());
		processOrder.setVoyage(detail.getVoyNo());
		processOrder.setConsignee(detail.getConsignee());
		processOrder.setLogisticGroupId(shipment.getLogisticGroupId());
		ProcessOrder tempProcessOrder = getYearBeforeAfter(detail.getVslNm(), detail.getVoyNo());
		if (tempProcessOrder != null) {
			processOrder.setYear(tempProcessOrder.getYear());
			processOrder.setBeforeAfter(tempProcessOrder.getBeforeAfter());
		} else {
			processOrder.setYear(Integer.toString(Calendar.getInstance().get(Calendar.YEAR)));
			processOrder.setBeforeAfter("Before");
		}
		// Set trucker from dictionary if null get tax code as default
		processOrder.setTrucker(getTruckerFromRegNoCatos(taxCode));
		processOrder.setShipmentId(shipment.getId());
		if ("F".equalsIgnoreCase(detail.getFe())) {
			processOrder.setServiceType(EportConstants.SERVICE_DROP_FULL);
			if (StringUtils.isNotEmpty(detail.getDischargePort())
					&& "VN".equalsIgnoreCase(detail.getDischargePort().substring(0, 2))) {
				processOrder.setDomesticFlg(true);
			} else {
				processOrder.setDomesticFlg(false);
			}
		} else {
			processOrder.setDomesticFlg(false);
			processOrder.setServiceType(EportConstants.SERVICE_DROP_EMPTY);
		}
		if (creditFlag) {
			processOrder.setPayType("Credit");
		} else {
			processOrder.setPayType("Cash");
		}
		processOrderService.insertProcessOrder(processOrder);

		String payer = taxCode;
		String payerName = "";
		try {
			logger.debug("Get payer name for shipment detail by tax code: " + taxCode);
			payerName = getGroupNameByTaxCode(taxCode).getGroupName();
		} catch (Exception e) {
			logger.error("Error when get payer name for " + payer + ": " + e);
		}
		for (ShipmentDetail shipmentDetail : shipmentDetails) {
			shipmentDetail.setProcessOrderId(processOrder.getId());
			shipmentDetail.setRegisterNo(detail.getId().toString());
			shipmentDetail.setUserVerifyStatus("Y");
			shipmentDetail.setPayer(payer);
			shipmentDetail.setPayerName(payerName);
			if (creditFlag) {
				shipmentDetail.setPayType("Credit");
			} else {
				shipmentDetail.setPayType("Cash");
			}
			// Them remark han lenh khi ha vo
			String remark = "";
			// Case drop empty need remark empty depot location and det free time
			if (processOrder.getServiceType() == EportConstants.SERVICE_DROP_EMPTY
					&& EportConstants.DROP_EMPTY_TO_DEPORT.equals(shipment.getSendContEmptyType())) {
				if (StringUtils.isNotEmpty(shipmentDetail.getEmptyDepotLocation())) {
					remark += "Ha vo " + shipmentDetail.getEmptyDepotLocation();
				}
				if (shipmentDetail.getDetFreeTime() != null && !"GLS".equalsIgnoreCase(shipmentDetail.getOpeCode())) {
					if (StringUtils.isNotEmpty(remark)) {
						remark += ", ";
					}
					remark += "free " + shipmentDetail.getDetFreeTime() + " days";
				}
				shipmentDetail.setContainerRemark(remark);
			}
			shipmentDetailMapper.updateShipmentDetail(shipmentDetail);

			// Set remark has saved on process order for robot can mapping data easier
			shipmentDetail.setRemark(remark);
		}
		return processOrder;
	}

	@Override
	@Transactional
	public void updateProcessStatus(List<ShipmentDetail> shipmentDetails, String status, String invoiceNo,
			ProcessOrder processOrder) {
		for (ShipmentDetail shipmentDetail : shipmentDetails) {
			shipmentDetail.setProcessStatus(status);
			shipmentDetail.setOrderNo(processOrder.getOrderNo());
			shipmentDetail.setUpdateBy(processOrder.getUpdateBy());
			if ("Y".equalsIgnoreCase(status)) {
				if ("Credit".equalsIgnoreCase(processOrder.getPayType())) {
					shipmentDetail.setStatus(shipmentDetail.getStatus() + 1);
					shipmentDetail.setPaymentStatus("Y");
					if (4 == processOrder.getServiceType()
							&& "VN".equals(shipmentDetail.getDischargePort().substring(0, 2))) {
						shipmentDetail.setStatus(shipmentDetail.getStatus() + 1);
						shipmentDetail.setCustomStatus("R");
					}
				}
				shipmentDetail.setStatus(shipmentDetail.getStatus() + 1);
			}
			shipmentDetailMapper.updateShipmentDetail(shipmentDetail);
		}
	}

	@Override
	public PartnerInfoDto getGroupNameByTaxCode(String taxCode) throws Exception {
		// String apiUrl = "https://thongtindoanhnghiep.co/api/company/";
		// String methodName = "GET";
		// String readLine = null;
		// HttpURLConnection connection = (HttpURLConnection) new URL(apiUrl + "/" +
		// taxCode).openConnection();
		// connection.setRequestMethod(methodName);
		// connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1;
		// WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95
		// Safari/537.11");
		//
		// int responseCode = connection.getResponseCode();
		// StringBuffer response = new StringBuffer();
		// if (responseCode == HttpURLConnection.HTTP_OK) {
		// BufferedReader in = new BufferedReader(new
		// InputStreamReader(connection.getInputStream()));
		// while ((readLine = in.readLine()) != null) {
		// response.append(readLine);
		// };
		// in.close();
		// } else {
		// String error = responseCode + " : " + methodName + " NOT WORKED";
		// return error;
		// }
		// String str = response.toString();
		// JsonObject convertedObject = new Gson().fromJson(str, JsonObject.class);
		// if(convertedObject.get("Title").toString().equals("null"))
		// {
		// return null;
		// }
		// return convertedObject.get("Title").toString().replace("\"", "");
		String url = Global.getApiUrl() + "/shipmentDetail/getGroupNameByTaxCode/" + taxCode;
		RestTemplate restTemplate = new RestTemplate();
		PartnerInfoDto partnerInfoDto = restTemplate.getForObject(url, PartnerInfoDto.class);
		return partnerInfoDto;
	}

	@Override
	public ProcessOrder getYearBeforeAfter(String vessel, String voyage) {
		try {
			String url = Global.getApiUrl() + "/processOrder/getYearBeforeAfter/" + vessel + "/" + voyage;
			logger.debug("Call CATOS API :{}", url);
			RestTemplate restTemplate = new RestTemplate();
			return restTemplate.getForObject(url, ProcessOrder.class);
		} catch (Exception e) {
			logger.error("Error while call CATOS Api", e);
			e.printStackTrace();
			return null;
		}

	}

	@Override
	public List<String> checkContainerReserved(String containerNos) {
		String url = Global.getApiUrl() + "/shipmentDetail/checkContReserved/" + containerNos;
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<List<String>> response = restTemplate.exchange(url, HttpMethod.GET, null,
				new ParameterizedTypeReference<List<String>>() {
				});
		List<String> listCont = response.getBody();
		return listCont;
	}

	@Override
	public List<String> getVesselCodeList() {
		List<String> vslNms = (List<String>) CacheUtils.get("vslNmList");
		if (vslNms == null) {
			String url = Global.getApiUrl() + "/shipmentDetail/getVesselCodeList";
			RestTemplate restTemplate = new RestTemplate();
			ResponseEntity<List<String>> response = restTemplate.exchange(url, HttpMethod.GET, null,
					new ParameterizedTypeReference<List<String>>() {
					});
			vslNms = response.getBody();
			CacheUtils.put("vslNmList", vslNms);
		}
		return vslNms;
	}

	@Override
	public List<String> getConsigneeList() {
		// List<String> consignees = (List<String>)
		// CacheUtils.get("consigneeListTaxCode");
		// if (consignees == null) {
		String url = Global.getApiUrl() + "/shipmentDetail/getConsigneeList";
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<List<String>> response = restTemplate.exchange(url, HttpMethod.GET, null,
				new ParameterizedTypeReference<List<String>>() {
				});
		List<String> consignees = response.getBody();
		// CacheUtils.put("consigneeListTaxCode", consignees);
		// }
		return consignees;
	}

	@Override
	public List<String> getConsigneeListWithoutTaxCode() {
		String url = Global.getApiUrl() + "/shipmentDetail/getConsigneeListWithoutTaxCode";
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<List<String>> response = restTemplate.exchange(url, HttpMethod.GET, null,
				new ParameterizedTypeReference<List<String>>() {
				});
		List<String> consignees = response.getBody();
		return consignees;
	}

	@Override
	public List<String> getOpeCodeList() {
		List<String> opeCodes = (List<String>) CacheUtils.get("opeCodeList");
		if (opeCodes == null) {
			String url = Global.getApiUrl() + "/shipmentDetail/getOpeCodeList";
			RestTemplate restTemplate = new RestTemplate();
			ResponseEntity<List<String>> response = restTemplate.exchange(url, HttpMethod.GET, null,
					new ParameterizedTypeReference<List<String>>() {
					});
			opeCodes = response.getBody();
			CacheUtils.put("opeCodeList", opeCodes);
		}
		return opeCodes;
	}

	@Override
	public List<String> getVoyageNoList(String vesselCode) {
		String url = Global.getApiUrl() + "/shipmentDetail/getVoyageNoList/" + vesselCode;
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<List<String>> response = restTemplate.exchange(url, HttpMethod.GET, null,
				new ParameterizedTypeReference<List<String>>() {
				});
		List<String> voyages = response.getBody();
		return voyages;
	}

	@Override
	public int getCountContByBlNo(String blNo) {
		String url = Global.getApiUrl() + "/shipmentDetail/getCountContByBlNo/" + blNo;
		RestTemplate restTemplate = new RestTemplate();
		Integer count = restTemplate.getForObject(url, Integer.class);
		return count.intValue();
	}

	@Override
	public List<ShipmentDetail> selectShipmentDetailByProcessIds(String processOrderIds) {
		return shipmentDetailMapper.selectShipmentDetailByProcessIds(Convert.toStrArray(processOrderIds));
	}

	@Override
	public List<ShipmentDetail> getShipmentDetailListForAssign(ShipmentDetail shipmentDetail) {
		return shipmentDetailMapper.getShipmentDetailListForAssign(shipmentDetail);
	}

	// @Override
	// public List<ShipmentDetail>
	// selectSendEmptyShipmentDetailByListCont(@Param("conts") String conts,
	// @Param("shipmentId") Long shipmentId) {
	// return
	// shipmentDetailMapper.selectSendEmptyShipmentDetailByListCont(Convert.toStrArray(conts),
	// shipmentId);
	// }

	@Override
	public List<ShipmentDetail> selectContainerStatusList(ShipmentDetail shipmentDetail) {
		return shipmentDetailMapper.selectContainerStatusList(shipmentDetail);
	}

	@Override
	public List<ShipmentDetail> getShipmentDetailList(ShipmentDetail shipmentDetail) {
		return shipmentDetailMapper.getShipmentDetailList(shipmentDetail);
	}

	/**
	 * Count number of legal container
	 * 
	 * @param shipmentDetails
	 * @param logisticGroupId
	 * @return Integer
	 */
	@Override
	public Integer countNumberOfLegalCont(List<ShipmentDetail> shipmentDetails, Long logisticGroupId) {
		return shipmentDetailMapper.countNumberOfLegalCont(shipmentDetails, logisticGroupId);
	}

	@Override
	public String getSsrCodeBySztp(String sztp) {
		List<SysDictData> dictDatas = dictTypeService.selectDictDataByType("sys_special_service_request");
		for (SysDictData i : dictDatas) {
			if (i.getDictValue().equals(sztp)) {
				return i.getDictLabel();
			}
		}
		return null;
	}

	@Override
	public List<ShipmentDetail> getShipmentDetailListForSendFReceiveE(ShipmentDetail shipmentDetail) {
		return shipmentDetailMapper.getShipmentDetailListForSendFReceiveE(shipmentDetail);
	}

	@Override
	public List<Long> getProcessOrderIdListByShipment(ShipmentDetail shipmentDetail) {
		return shipmentDetailMapper.getCommandListInBatch(shipmentDetail);
	}

	@Override
	public List<ShipmentDetail> getShipmentDetailForPrint(ShipmentDetail shipmentDetail) {
		return shipmentDetailMapper.getShipmentDetailForPrint(shipmentDetail);
	}

	// /**
	// * Get container with yard position
	// *
	// * @param shipmentId
	// * @return ShipmentDetail
	// */
	// @Override
	// public ShipmentDetail getContainerWithYardPosition(Long shipmentId) {
	// // Get Shipment
	//// Shipment shipment = shipmentService.selectShipmentById(shipmentId);
	//
	// // Get shipment detail by shipment id
	// ShipmentDetail shipmentDetail = new ShipmentDetail();
	// shipmentDetail.setShipmentId(shipmentId);
	// List<ShipmentDetail> shipmentDetails =
	// shipmentDetailMapper.selectShipmentDetailList(shipmentDetail);
	//
	// // Get coordinate by bill
	// List<ShipmentDetail> coordinateList =
	// catosApiService.getCoordinateOfContainers(shipmentDetails.get(0).getBlNo());
	// List<ShipmentDetail[][]> bayList = new ArrayList<>();
	// bayList = getContPosition(coordinateList, shipmentDetails);
	//
	// // Get container from top to bottom
	// for (int b = 0; b < bayList.size(); b++) {
	// int rowMax = bayList.get(b)[0].length;
	// int rowTemp = 0;
	// if (rowMax%2 == 1) {
	// rowMax++;
	// rowTemp++;
	// }
	// for (int row = 0; row < rowMax/2; row++) {
	// boolean stack1 = false;
	// boolean stack2 = false;
	// for (int tier = 4; tier >= 0; tier--) {
	// ShipmentDetail shipmentDetail1 = bayList.get(b)[tier][row];
	// // validate
	// if (shipmentDetail1 != null) {
	// if (validateAutoPickupCont(shipmentDetail1, stack1)) {
	// return shipmentDetail1;
	// }
	// stack1 = true;
	// }
	//
	// ShipmentDetail shipmentDetail2 = bayList.get(b)[tier][rowMax-row-1-rowTemp];
	// if (shipmentDetail2 != null) {
	// if (validateAutoPickupCont(shipmentDetail2, stack2)) {
	// return shipmentDetail2;
	// }
	// stack2 = true;
	// }
	// }
	// }
	// }
	// return null;
	// }

	// private Boolean validateAutoPickupCont (ShipmentDetail shipmentDetail,
	// boolean stack) {
	// if (shipmentDetail.getId() == null) {
	// return false;
	// }
	//
	// // Not received DO
	// if (!"Y".equals(shipmentDetail.getDoStatus())) {
	// return false;
	// }
	//
	// // Exceed expired dem
	// Date now = new Date();
	// Calendar calendar = Calendar.getInstance();
	// calendar.setTime(now);
	// calendar.set(Calendar.HOUR_OF_DAY, 0);
	// calendar.set(Calendar.MINUTE, 0);
	// calendar.set(Calendar.SECOND, 0);
	// calendar.set(Calendar.MILLISECOND, 0);
	// if (shipmentDetail.getExpiredDem().compareTo(now) < 0) {
	// return false;
	// }
	//
	// if ("N".equals(shipmentDetail.getPaymentStatus())) {
	// return false;
	// }
	//
	// if (stack && "N".equals(shipmentDetail.getPrePickupPaymentStatus())) {
	// return false;
	// }
	//
	// if (!"N".equals(shipmentDetail.getFinishStatus())) {
	// return false;
	// }
	//
	// return true;
	// }

	/**
	 * Select shipment detail for driver shipment assign
	 * 
	 * @param shipmentId
	 * @param driverId
	 * @return List<PickupAssignForm>
	 */
	public List<PickupAssignForm> selectShipmentDetailForDriverShipmentAssign(Long shipmentId, Long driverId) {
		return shipmentDetailMapper.selectShipmentDetailForDriverShipmentAssign(shipmentId, driverId);
	}

	/**
	 * Make change vessel order
	 * 
	 * @param shipmentDetails
	 * @param vessel
	 * @param voyage
	 * @param groupId
	 * @return ServiceSendFullRobotReq
	 */
	@Override
	public ServiceSendFullRobotReq makeChangeVesselOrder(List<ShipmentDetail> shipmentDetails, String vslNm,
			String voyNo, String vslName, String voyCarrier, Long groupId) {
		ProcessOrder processOrder = new ProcessOrder();
		if (CollectionUtils.isNotEmpty(shipmentDetails)) {
			ShipmentDetail shipmentDt = shipmentDetails.get(0);
			processOrder.setServiceType(EportConstants.SERVICE_CHANGE_VESSEL);
			processOrder.setShipmentId(shipmentDt.getShipmentId());
			processOrder.setBookingNo(shipmentDt.getBookingNo());
			processOrder.setStatus(0);
			ProcessOrder tempProcessOrder = getYearBeforeAfter(shipmentDt.getVslNm(), shipmentDt.getVoyNo());
			if (tempProcessOrder != null) {
				processOrder.setYear(tempProcessOrder.getYear());
				processOrder.setBeforeAfter(tempProcessOrder.getBeforeAfter());
			} else {
				processOrder.setYear(Integer.toString(Calendar.getInstance().get(Calendar.YEAR)));
				processOrder.setBeforeAfter("Before");
			}
			processOrder.setOldVessel(shipmentDt.getVslNm());
			processOrder.setOldVoyAge(shipmentDt.getVoyNo());
			processOrder.setVessel(vslNm);
			processOrder.setVoyage(voyNo);
			processOrder.setContNumber(shipmentDetails.size());
			List<Long> shipmentDetailIds = new ArrayList<>();
			for (ShipmentDetail shipmentDetail : shipmentDetails) {
				shipmentDetail.setVslNm(vslNm);
				shipmentDetail.setVoyNo(voyNo);
				shipmentDetailIds.add(shipmentDetail.getId());
			}
			Map<String, Object> map = new HashMap<>();
			map.put("shipmentDetailIds", shipmentDetailIds);
			map.put("vslName", vslName);
			map.put("voyCarrier", voyCarrier);
			processOrder.setProcessData(new Gson().toJson(map));
			processOrder.setLogisticGroupId(groupId);
			processOrderService.insertProcessOrder(processOrder);
			ServiceSendFullRobotReq serviceRobotReq = new ServiceSendFullRobotReq(processOrder, shipmentDetails);
			return serviceRobotReq;
		}
		return null;
	}

	/**
	 * Make extension date order
	 * 
	 * @param shipmentDetails
	 * @param expiredDem
	 * @param groupId
	 * @return ServiceSendFullRobotReq
	 */
	@Override
	public List<ServiceSendFullRobotReq> makeExtensionDateOrder(List<ShipmentDetail> shipmentDetails, Date expiredDem,
			Long groupId) {
		Collections.sort(shipmentDetails, new OrderNoComparator());
		List<ServiceSendFullRobotReq> serviceRobotReqs = new ArrayList<>();
		String currentOrderNo = shipmentDetails.get(0).getOrderNo();
		List<ShipmentDetail> shipmentDetailList = new ArrayList<>();
		for (ShipmentDetail shipmentDetail : shipmentDetails) {
			if (currentOrderNo.equals(shipmentDetail.getOrderNo())) {
				shipmentDetailList.add(shipmentDetail);
			} else {
				serviceRobotReqs.add(separateExtensionOrderByOrderNo(shipmentDetailList, expiredDem, groupId));
				shipmentDetailList = new ArrayList<>();
				shipmentDetailList.add(shipmentDetail);
				currentOrderNo = shipmentDetail.getOrderNo();
			}
		}
		serviceRobotReqs.add(separateExtensionOrderByOrderNo(shipmentDetailList, expiredDem, groupId));
		return serviceRobotReqs;
	}

	private ServiceSendFullRobotReq separateExtensionOrderByOrderNo(List<ShipmentDetail> shipmentDetails,
			Date expiredDem, Long groupId) {
		ProcessOrder processOrder = new ProcessOrder();
		processOrder.setPickupDate(expiredDem);
		processOrder.setServiceType(EportConstants.SERVICE_EXTEND_DATE);
		processOrder.setLogisticGroupId(groupId);
		processOrder.setShipmentId(shipmentDetails.get(0).getShipmentId());
		processOrder.setOrderNo(shipmentDetails.get(0).getOrderNo());
		processOrder.setContNumber(shipmentDetails.size());
		String containers = "";
		for (ShipmentDetail shipmentDetail : shipmentDetails) {
			containers += shipmentDetail.getContainerNo() + ",";
		}
		ProcessJsonData processJsonData = new ProcessJsonData();
		processJsonData.setContainers(containers.substring(0, containers.length() - 1));
		processOrder.setProcessData(new Gson().toJson(processJsonData));
		processOrderService.insertProcessOrder(processOrder);
		ServiceSendFullRobotReq serviceRobotReq = new ServiceSendFullRobotReq(processOrder, shipmentDetails);
		return serviceRobotReq;
	}

	/**
	 * Select consignee tax code by shipment id
	 * 
	 * @param shipmentId
	 * @return String
	 */
	public String selectConsigneeTaxCodeByShipmentId(Long shipmentId) {
		return shipmentDetailMapper.selectConsigneeTaxCodeByShipmentId(shipmentId);
	}

	/**
	 * Get Shipment detail from house bill
	 * 
	 * @param houseBill
	 * @return List<ShipmentDetail>
	 */
	public List<ShipmentDetail> getShipmentDetailFromHouseBill(String houseBill) {
		return shipmentDetailMapper.selectHouseBillForShipment(houseBill);
	}

	/**
	 * Get shipment detail from edi by blNo
	 * 
	 * @param blNo
	 * @return List<ShipmentDetail>
	 */
	public List<ShipmentDetail> getShipmentDetailsFromEDIByBlNo(String blNo) {
		return shipmentDetailMapper.selectEdoListByBlNo(blNo);
	}

	/**
	 * Check and create booking if need
	 * 
	 * @param receiveEmptyReqs
	 * @return List<ProcessOrder>
	 */
	@Override
	public List<ProcessOrder> createBookingIfNeed(List<ServiceSendFullRobotReq> receiveEmptyReqs) {
		List<ProcessOrder> processOrders = new ArrayList<>();
		for (ServiceSendFullRobotReq receiveEmptyReq : receiveEmptyReqs) {
			ProcessOrder processOrder = receiveEmptyReq.processOrder;
			boolean hasBooking = false;
			// Get booking info from catos
			List<BookingInfo> bookingInfos = catosApiService.getBookingInfo(processOrder.getBookingNo(),
					processOrder.getVessel() + processOrder.getVoyage());
			if (CollectionUtils.isNotEmpty(bookingInfos)) {
				for (BookingInfo bookingInfo : bookingInfos) {
					// check in list booking info has same sztp with process order of receive empty
					// req
					if (processOrder.getSztp().equals(bookingInfo.getSztp())) {
						hasBooking = true;

						// Update booking to increase slot for container empty
						ProcessOrder bookingOrder = new ProcessOrder();

						// Set booking mode ( update or create new)
						bookingOrder.setBookingCreateMode(EportConstants.BOOKING_UPDATE);

						// Set new cont number = quantity need + current quantity
						bookingOrder.setContNumber(bookingInfo.getBookQty() + processOrder.getContNumber());

						// Set the other data mapping from receive cont empty order
						bookingOrder.setBookingNo(processOrder.getBookingNo());
						bookingOrder.setSztp(processOrder.getSztp());
						bookingOrder.setPostProcessId(processOrder.getId());
						bookingOrder.setVessel(processOrder.getVessel());
						bookingOrder.setVoyage(processOrder.getVoyage());
						bookingOrder.setBeforeAfter(processOrder.getBeforeAfter());
						bookingOrder.setShipmentId(processOrder.getShipmentId());
						bookingOrder.setYear(processOrder.getYear());
						bookingOrder.setServiceType(EportConstants.SERVICE_CREATE_BOOKING);
						bookingOrder.setLogisticGroupId(processOrder.getLogisticGroupId());
						bookingOrder.setFe("E");
						bookingOrder.setOpr(processOrder.getOpr());
						bookingOrder.setPol(processOrder.getPol());
						bookingOrder.setPod(processOrder.getPod());
						bookingOrder.setCargoType(processOrder.getCargoType());

						// Get index line of booking in catos when update for robot
						// Robot using index to determine which line need to be update
						// Differentiate number of booking
						ShipmentDetail shipmentDetail = new ShipmentDetail();
						shipmentDetail.setVslNm(processOrder.getVessel());
						shipmentDetail.setVoyNo(processOrder.getVoyage());
						shipmentDetail.setYear(processOrder.getYear());
						shipmentDetail.setBookingNo(processOrder.getBookingNo());
						shipmentDetail.setSztp(processOrder.getSztp());
						bookingOrder.setBookingIndex(catosApiService.getIndexBooking(shipmentDetail));
						processOrderService.insertProcessOrder(bookingOrder);
						processOrders.add(bookingOrder);

						// // Check if sztp has enough quantity for order receive empty
						// // If not then update booking with extra amount for the number lacking of
						// sztp
						// // first check if used booking quantity is null then set to 0 to make it
						// calculatable
						// if (bookingInfo.getUsedQty() == null) bookingInfo.setUsedQty(0);
						//
						// // Calculate booking quantity need exclude the booking available currently
						// // If bookingQuantityNeed > 0 then need more booking to make order, need to
						// update
						// Integer bookingQuantityNeed = processOrder.getContNumber() -
						// (bookingInfo.getBookQty() - bookingInfo.getUsedQty());
						// if (bookingQuantityNeed > 0) {
						//
						// } else {
						// // No need to update booking, current booking has enought quantity to make
						// order
						// processOrder.setRunnable(true);
						// processOrderService.updateProcessOrder(processOrder);
						// }
					}
				}
			}
			// If has booking = false then there is no booking, need to create new booking
			// with this sztp
			if (!hasBooking) {
				ProcessOrder bookingOrder = new ProcessOrder();

				// Set booking mode create new
				bookingOrder.setBookingCreateMode(EportConstants.BOOKING_CREATE);
				bookingOrder.setContNumber(receiveEmptyReq.containers.size());
				bookingOrder.setBookingNo(processOrder.getBookingNo());
				bookingOrder.setVessel(processOrder.getVessel());
				bookingOrder.setVoyage(processOrder.getVoyage());
				bookingOrder.setShipmentId(processOrder.getShipmentId());
				bookingOrder.setBeforeAfter(processOrder.getBeforeAfter());
				bookingOrder.setYear(processOrder.getYear());
				bookingOrder.setServiceType(EportConstants.SERVICE_CREATE_BOOKING);
				bookingOrder.setLogisticGroupId(processOrder.getLogisticGroupId());
				bookingOrder.setSztp(processOrder.getSztp());
				bookingOrder.setBookingIndex(0);
				bookingOrder.setPostProcessId(processOrder.getId());
				bookingOrder.setFe("E");
				bookingOrder.setOpr(processOrder.getOpr());
				bookingOrder.setPol(processOrder.getPol());
				bookingOrder.setPod(processOrder.getPod());
				bookingOrder.setCargoType(processOrder.getCargoType());
				processOrderService.insertProcessOrder(bookingOrder);
				processOrders.add(bookingOrder);
			}
		}
		return processOrders;
	}

	@Override
	public List<ShipmentDetail> selectShipmentDetailListReport(ShipmentDetail shipmentDetail) {
		return shipmentDetailMapper.selectShipmentDetailListReport(shipmentDetail);
	}

	/**
	 * updateShipmentDetailForOMSupport is used for OM reset process status. Not use
	 * with another purpose
	 */
	@Override
	public int resetShipmentDetailProcessStatus(ShipmentDetail shipmentDetail) {
		return shipmentDetailMapper.resetShipmentDetailProcessStatus(shipmentDetail);
	}

	/**
	 * Get List container with coordinate for carrier
	 * 
	 * @param ShipmentDetail
	 * @return List shipment detail with container, sztp, yard position include
	 */
	@Override
	public ShipmentDetail[][] getListContainerForCarrier(ShipmentDetail shipmentDetail) {
		List<ShipmentDetail> shipmentDetails = catosApiService
				.selectCoordinateOfContainersByShipmentDetail(shipmentDetail);

		ShipmentDetail[][] shipmentDetailMatrix = new ShipmentDetail[5][12];

		for (ShipmentDetail shipmentDt : shipmentDetails) {
			shipmentDetailMatrix[shipmentDt.getTier() - 1][shipmentDt.getRow() - 1] = shipmentDt;
		}
		return shipmentDetailMatrix;
	}

	/**
	 * Select shipment detail for driver send cont on app mobile
	 * 
	 * @param logisticGroupId
	 * @param pickupAssignForm
	 * @return list pickup assign form object
	 */
	@Override
	public List<PickupAssignForm> selectShipmentDetailForDriverSendCont(@Param("logisticGroupId") Long logisticGroupId,
			@Param("pickUp") PickupAssignForm pickupAssignForm, @Param("serviceType") Integer serviceType) {
		return shipmentDetailMapper.selectShipmentDetailForDriverSendCont(logisticGroupId, pickupAssignForm,
				serviceType);
	}

	/**
	 * Update shipment detail by shipment detail id
	 * 
	 * @param shipmentDetailIds
	 * @param shipmentDetail
	 * @return int
	 */
	@Override
	public int updateShipmentDetailByIds(String shipmentDetailIds, ShipmentDetail shipmentDetail) {
		return shipmentDetailMapper.updateShipmentDetailByIds(Convert.toStrArray(shipmentDetailIds), shipmentDetail);
	}

	@Override
	public void resetCustomStatus(Long shipmentId) {
		shipmentDetailMapper.resetCustomStatus(shipmentId);
	}

	/**
	 * Delete shipment detail by condition
	 * 
	 * @param shipmentDetail
	 * @return int
	 */
	@Override
	public int deleteShipmentDetailByCondition(ShipmentDetail shipmentDetail) {
		return shipmentDetailMapper.deleteShipmentDetailByCondition(shipmentDetail);
	}

	/**
	 * Update shipment detail by condition
	 * 
	 * @param shipmentDetail
	 * @return int
	 */
	@Override
	public int updateShipmentDetailByCondition(ShipmentDetail shipmentDetail) {
		return shipmentDetailMapper.updateShipmentDetailByCondition(shipmentDetail);
	}

	/**
	 * Check cont have request status for cont special
	 * 
	 * @param shipmentDetail
	 * @return boolean
	 */
	@Override
	public Boolean isHaveContSpacialRequest(ShipmentDetail shipmentDetail) {
		Boolean result = false;
		if (!StringUtils.isEmpty(shipmentDetail.getFrozenStatus())
				&& shipmentDetail.getFrozenStatus().equals(EportConstants.CONT_SPECIAL_STATUS_REQ)) {
			result = true;
		}
		if (!StringUtils.isEmpty(shipmentDetail.getDangerous())
				&& shipmentDetail.getDangerous().equals(EportConstants.CONT_SPECIAL_STATUS_REQ)) {
			result = true;
		}
		if (!StringUtils.isEmpty(shipmentDetail.getOversize())
				&& shipmentDetail.getOversize().equals(EportConstants.CONT_SPECIAL_STATUS_REQ)) {
			result = true;
		}
		return result;
	}

	/**
	 * Check cont have done status for cont special
	 * 
	 * @param shipmentDetail
	 * @return boolean
	 */
	@Override
	public Boolean isHaveContSpacialYes(ShipmentDetail shipmentDetail) {
		Boolean result = false;
		if (!StringUtils.isEmpty(shipmentDetail.getFrozenStatus())
				&& shipmentDetail.getFrozenStatus().equals(EportConstants.CONT_SPECIAL_STATUS_YES)) {
			result = true;
		}
		if (!StringUtils.isEmpty(shipmentDetail.getDangerous())
				&& shipmentDetail.getDangerous().equals(EportConstants.CONT_SPECIAL_STATUS_YES)) {
			result = true;
		}
		if (!StringUtils.isEmpty(shipmentDetail.getOversize())
				&& shipmentDetail.getOversize().equals(EportConstants.CONT_SPECIAL_STATUS_YES)) {
			result = true;
		}
		return result;
	}
}
