package vn.com.irtech.eport.edo.controller;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.export.ExporterInputItem;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleExporterInputItem;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import vn.com.irtech.eport.carrier.domain.Edo;
import vn.com.irtech.eport.carrier.domain.EdoHouseBill;
import vn.com.irtech.eport.carrier.service.IEdoHouseBillService;
import vn.com.irtech.eport.carrier.service.IEdoService;
import vn.com.irtech.eport.common.core.controller.BaseController;
import vn.com.irtech.eport.common.core.domain.AjaxResult;
import vn.com.irtech.eport.common.utils.CacheUtils;
import vn.com.irtech.eport.common.utils.DateUtils;
import vn.com.irtech.eport.framework.util.ShiroUtils;

@Controller
@RequestMapping("/edo/print")
public class EdoPrintController extends BaseController{
	private static final Logger logger = LoggerFactory.getLogger(EdoPrintController.class);
	private String prefix = "edo/print";
	
	@Autowired
	private IEdoService edoService;

	@Autowired
	private IEdoHouseBillService edoHouseBillService;

	/**
	 * Print Delivery Order
	 */
	@GetMapping("/view/{key}")
	public String view(@PathVariable("key") String key, ModelMap mmap) {
		mmap.put("key", key);
		return prefix + "/deliveryOrder";
	}
	/**
	 * Print EDO
	 */
	@GetMapping("/bill/{billOfLading}")
	public String edit(@PathVariable("billOfLading") String billOfLading, ModelMap mmap) {
		mmap.put("billOfLading", billOfLading);
		return prefix + "/printEdo";
	}

	/**
	 * Print House Bill
	 */
	@GetMapping("/house-bill/{houseBillNo}")
	public String viewHouseBill(@PathVariable("houseBillNo") String houseBillNo, ModelMap mmap) {
		mmap.put("houseBillNo", houseBillNo);
		return prefix + "/houseBill";
	}

	/**
	 * Print Delivery Order
	 */
	@PostMapping("/post-data")
	@ResponseBody
	public AjaxResult postData(@RequestBody List<Edo> dataObj) {
		AjaxResult ajaxResult = success();
		if(dataObj != null) {
			String key = ShiroUtils.getUserId() + "-edo-" + DateUtils.dateTimeNow();
			CacheUtils.put(key , dataObj);
			ajaxResult.put("key", key);
			return ajaxResult;
		}
		return error();
	}
	@GetMapping("/deliveryOrder/{key}")
	@ResponseBody
	public void jasperReport(@PathVariable("key") String key, HttpServletResponse response) {
		try {
			List<Edo> edos = (List<Edo>) CacheUtils.get(key);
			if(edos.size() > 0) {
				response.setContentType("application/pdf");
				createPdfReport(edos, response.getOutputStream());
			}
		} catch (final Exception e) {
			logger.debug(e.getMessage());
			e.printStackTrace();
		} finally {
			CacheUtils.remove(key);
		}
	}
	private void createPdfReport(final List<Edo> edoList, OutputStream out) throws JRException {
		// Fetching the report file from the resources folder.
		final JasperReport report = (JasperReport) JRLoader
				.loadObject(this.getClass().getResourceAsStream("/report/delivery_order.jasper"));
		List<ExporterInputItem> jpList = new ArrayList<>();
		//tach bill 
		if(edoList.size() > 0) {
			//get ds so bill
			List<String> blNoList = new ArrayList<String>();
			String blNo = edoList.get(0).getBillOfLading();
			blNoList.add(blNo);
			for(Edo i: edoList) {
				//i.getBill != pre blNo  && i.getBill not contains in blNoList
				if(!i.getBillOfLading().equals(blNo) && blNoList.indexOf(i.getBillOfLading()) == -1 ) {
					blNoList.add(i.getBillOfLading());
					blNo = i.getBillOfLading();
				}
			}
			//tach thanh nhieu bill
			if(blNoList.size() > 0) {
				for(String i: blNoList) {
					List<Edo> list = new ArrayList<Edo>();
					for(Edo j : edoList) {
						if(j.getBillOfLading().equals(i)) {
							list.add(j);
						}
					}
					if(list.size() > 0) {
						//final JRBeanCollectionDataSource params = new JRBeanCollectionDataSource(list);
						final Map<String, Object> parameters = new HashMap<>();
						parameters.put("consignee", list.get(0).getConsignee());
						parameters.put("businessUnit", list.get(0).getBusinessUnit());
						parameters.put("vessel/voy", list.get(0).getVessel() + " / " + list.get(0).getVoyNo());
						parameters.put("orderNumber", list.get(0).getOrderNumber());
						parameters.put("pol", list.get(0).getPol());
						parameters.put("pod", list.get(0).getPod());
						parameters.put("billOfLading", list.get(0).getBillOfLading());
						parameters.put("fileCreateTime", list.get(0).getFileCreateTime());
						parameters.put("list", list);
						final JasperPrint print = JasperFillManager.fillReport(report, parameters, new JREmptyDataSource());
						jpList.add(new SimpleExporterInputItem(print));
					}
				}
			}
		}
		// Export DPF to output stream
		JRPdfExporter exporter = new JRPdfExporter(); 
		exporter.setExporterInput(new SimpleExporterInput(jpList));
		exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(out));
		exporter.exportReport();
	}
	@GetMapping("/report/{billOfLading}")
	public void report(@PathVariable("billOfLading") String billOfLading, HttpServletResponse response) {
		// First check permission for this shipmentId
		if(billOfLading != null) {
			Edo edo = new Edo();
			edo.setBillOfLading(billOfLading);
			List<Edo> edoList = edoService.selectEdoList(edo);
			if(edoList.size() != 0) {
				try {
					response.setContentType("application/pdf");
					createEdoReport(edoList, response.getOutputStream());
				} catch (final Exception e) {
					logger.debug(e.getMessage());
					e.printStackTrace();
				}
			}

		}
	}
	
	private void createEdoReport(final List<Edo> edoList, OutputStream out) throws JRException {
		// Fetching the report file from the resources folder.
		final JasperReport report = (JasperReport) JRLoader
				.loadObject(this.getClass().getResourceAsStream("/report/edo.jasper"));

		// Fetching the shipmentDetails from the data source.
		//final JRBeanCollectionDataSource params = new JRBeanCollectionDataSource(shipmentDetails);


		// Adding the additional parameters to the pdf.
		final Map<String, Object> parameters = new HashMap<>();
		parameters.put("consignee", edoList.get(0).getConsignee());
		parameters.put("businessUnit", edoList.get(0).getBusinessUnit());
		parameters.put("vessel/voy", edoList.get(0).getVessel() + " / " + edoList.get(0).getVoyNo());
		parameters.put("orderNumber", edoList.get(0).getOrderNumber());
		parameters.put("pol", edoList.get(0).getPol());
		parameters.put("pod", edoList.get(0).getPod());
		parameters.put("billOfLading", edoList.get(0).getBillOfLading());
		parameters.put("fileCreateTime", edoList.get(0).getFileCreateTime());
		parameters.put("list", edoList);
		final JasperPrint print = JasperFillManager.fillReport(report, parameters, new JREmptyDataSource());
//		final JasperPrint print = JasperFillManager.fillReport(report, parameters, params);
		// Export DPF to output stream
		JasperExportManager.exportReportToPdfStream(print, out);
	}

	@GetMapping("create-house-bill/{houseBillNo}")
	public void jasperReportHouseBill(@PathVariable("houseBillNo") String houseBillNo, HttpServletResponse response) {
		EdoHouseBill edoHouseBill = new EdoHouseBill();
		edoHouseBill.setHouseBillNo(houseBillNo);
		// edoHouseBill.setLogisticAccountId(getUser().getId());
		List<EdoHouseBill> houseBillList = edoHouseBillService.selectEdoHouseBillList(edoHouseBill);
		if (houseBillList.isEmpty()) {
			logger.error("Error when print HouseBill: " + houseBillNo);
			return;
		}
		try {
			response.setContentType("application/pdf");
			createHouseBillReport(houseBillList, response.getOutputStream());
		} catch (final Exception e) {
			logger.debug(e.getMessage());
			e.printStackTrace();
		}
	}
	
	private void createHouseBillReport(final List<EdoHouseBill> houseBillList, OutputStream out) throws JRException {
		// Fetching the report file from the resources folder.
		final JasperReport report = (JasperReport) JRLoader
				.loadObject(this.getClass().getResourceAsStream("/report/house_bill.jasper"));

		// Fetching the shipmentDetails from the data source.
		// final JRBeanCollectionDataSource params = new
		// JRBeanCollectionDataSource(shipmentDetails);

		// Adding the additional parameters to the pdf.
		final Map<String, Object> parameters = new HashMap<>();
		parameters.put("consignee", houseBillList.get(0).getConsignee2());
		parameters.put("businessUnit", houseBillList.get(0).getEdo().getBusinessUnit());
		parameters.put("vessel/voy", houseBillList.get(0).getVessel() + " / " + houseBillList.get(0).getVoyNo());
		parameters.put("orderNumber", houseBillList.get(0).getOrderNumber());
		parameters.put("pol", houseBillList.get(0).getEdo().getPol());
		parameters.put("pod", houseBillList.get(0).getEdo().getPod());
		parameters.put("masterBillNo", houseBillList.get(0).getMasterBillNo());
		parameters.put("houseBillNo", houseBillList.get(0).getHouseBillNo());
		parameters.put("fileCreateTime", houseBillList.get(0).getEdo().getFileCreateTime());
//		try {
//			File file = new File("target/classes/static/img/logo_gray.jpeg");
//			parameters.put("pathBackground", file.getPath());
//		} catch (Exception e) {
//			logger.error("Path background report error",e.getMessage());
//		}
		List<Edo> edoList = new ArrayList<Edo>();
		for (EdoHouseBill i : houseBillList) {
			edoList.add(i.getEdo());
		}
		parameters.put("list", edoList);
		final JasperPrint print = JasperFillManager.fillReport(report, parameters, new JREmptyDataSource());
//		final JasperPrint print = JasperFillManager.fillReport(report, parameters, params);
		// Export DPF to output stream
		JasperExportManager.exportReportToPdfStream(print, out);
	}
}
