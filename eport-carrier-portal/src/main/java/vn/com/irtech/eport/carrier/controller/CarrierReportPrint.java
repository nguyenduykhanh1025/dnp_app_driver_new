package vn.com.irtech.eport.carrier.controller;

import java.io.OutputStream;
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
import org.springframework.web.bind.annotation.RequestMapping;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import vn.com.irtech.eport.carrier.domain.Edo;
import vn.com.irtech.eport.carrier.service.IEdoService;


@Controller
@RequestMapping("edo/print")
public class CarrierReportPrint extends CarrierBaseController{
	private static final Logger logger = LoggerFactory.getLogger(CarrierReportPrint.class);
	
	private String prefix = "edo/print";
	
	@Autowired
	private IEdoService edoService;
	
	/**
	 * Print EDO
	 */
	@GetMapping("/bill/{billOfLading}")
	public String edit(@PathVariable("billOfLading") String billOfLading, ModelMap mmap) {
		mmap.put("billOfLading", billOfLading);
		return prefix + "/printEdo";
	}
	
	@GetMapping("/report/{billOfLading}")
	public void jasperReport(@PathVariable("billOfLading") String billOfLading, HttpServletResponse response) {
		// First check permission for this shipmentId
		if(billOfLading != null) {
			Edo edo = new Edo();
			edo.setBillOfLading(billOfLading);
			List<Edo> edoList = edoService.selectEdoList(edo);
			if(edoList.size() != 0 && getUser().getCarrierCode().equals(edoList.get(0).getCarrierCode())) {
				try {
					response.setContentType("application/pdf");
					createPdfReport(edoList, response.getOutputStream());
				} catch (final Exception e) {
					logger.debug(e.getMessage());
					e.printStackTrace();
				}
			}

		}
	}
	
	private void createPdfReport(final List<Edo> edoList, OutputStream out) throws JRException {
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
}
