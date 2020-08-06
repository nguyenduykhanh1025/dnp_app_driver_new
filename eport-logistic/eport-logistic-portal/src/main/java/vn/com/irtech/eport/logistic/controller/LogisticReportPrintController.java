package vn.com.irtech.eport.logistic.controller;

import java.io.InputStream;
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

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import vn.com.irtech.eport.logistic.domain.Shipment;
import vn.com.irtech.eport.logistic.domain.ShipmentDetail;
import vn.com.irtech.eport.logistic.service.IShipmentDetailService;
import vn.com.irtech.eport.logistic.service.IShipmentService;

/**
 * Logistic Report Print Controller
 * 
 * @author admin
 * @date 2020-06-16
 */
@Controller
@RequestMapping("/logistic/print")
public class LogisticReportPrintController extends LogisticBaseController {
	
	private static final Logger logger = LoggerFactory.getLogger(LogisticReportPrintController.class);
	
	private String prefix = "logistic/print";

	@Autowired
	private IShipmentService shipmentService;

	@Autowired
	private IShipmentDetailService shipmentDetailService;

	/**
	 * Print Process Order
	 */
	@GetMapping("/shipment/{id}")
	public String edit(@PathVariable("id") Long id, ModelMap mmap) {
		mmap.put("shipmentId", id);
		return prefix + "/processOrder";
	}

	@GetMapping("/processOrder/{shipmentId}")
	public void jasperReport(@PathVariable("shipmentId") Long shipmentId, HttpServletResponse response) {
		// First check permission for this shipmentId
		Shipment shipment = shipmentService.selectShipmentById(shipmentId);
		if(shipment == null || shipment.getLogisticGroupId() == null || shipment.getLogisticGroupId().equals(getUser().getLogisticGroup().getId()))
		{
			logger.error("Error when print Order for shipment: " + shipmentId);
			return;
		}
		// get shipment detail list
		ShipmentDetail shipmentDetail = new ShipmentDetail();
		shipmentDetail.setShipmentId(shipmentId);
		shipmentDetail.setPaymentStatus("Y");
		List<ShipmentDetail> shipmentDetails = shipmentDetailService.selectShipmentDetailList(shipmentDetail);
		try {
			response.setContentType("application/pdf");
			createPdfReport(shipmentDetails, response.getOutputStream());
		} catch (final Exception e) {
			logger.debug(e.getMessage());
			e.printStackTrace();
		}
	}

	private void createPdfReport(final List<ShipmentDetail> shipmentDetails, OutputStream out) throws JRException {
		// Fetching the report file from the resources folder.
		final JasperReport report = (JasperReport) JRLoader
				.loadObject(this.getClass().getResourceAsStream("/report/equipment_interchange_order.jasper"));

		// Fetching the shipmentDetails from the data source.
		final JRBeanCollectionDataSource params = new JRBeanCollectionDataSource(shipmentDetails);

		// Adding the additional parameters to the pdf.
        final Map<String, Object> parameters = new HashMap<>();
        parameters.put("user", getGroup().getGroupName());

		// Filling the report with the shipmentDetail data and additional parameters
		// information.
		final JasperPrint print = JasperFillManager.fillReport(report, parameters, params);

		// Export DPF to output stream
		JasperExportManager.exportReportToPdfStream(print, out);
	}
}
