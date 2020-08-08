package vn.com.irtech.eport.web.controller.edo;

import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import vn.com.irtech.eport.carrier.domain.Edo;
import vn.com.irtech.eport.common.core.controller.BaseController;

@Controller
@RequestMapping("/edo/print")
public class EdoPrintController extends BaseController{
	private static final Logger logger = LoggerFactory.getLogger(EdoPrintController.class);
	private String prefix = "edo/print";
	
	/**
	 * Print Delivery Order
	 */
	@GetMapping("/view")
	public String edit() {
		//mmap.put("edoList", dataObj);
		return prefix + "/deliveryOrder";
	}
	@PostMapping("/deliveryOrder")
	@ResponseBody
	public void jasperReport(@RequestBody List<Edo> edoList, HttpServletResponse response) {
		try {
			response.setContentType("application/pdf");
			createPdfReport(edoList, response.getOutputStream());
		} catch (final Exception e) {
			logger.debug(e.getMessage());
			e.printStackTrace();
		}
	}
	private void createPdfReport(final List<Edo> edoList, OutputStream out) throws JRException {
		// Fetching the report file from the resources folder.
		final JasperReport report = (JasperReport) JRLoader
				.loadObject(this.getClass().getResourceAsStream("/report/deliver_order.jasper"));

		// Fetching the shipmentDetails from the data source.
		final JRBeanCollectionDataSource params = new JRBeanCollectionDataSource(edoList);

		// Adding the additional parameters to the pdf.
        final Map<String, Object> parameters = new HashMap<>();
        //parameters.put("user", getGroup().getGroupName());

		// Filling the report with the shipmentDetail data and additional parameters
		// information.
		final JasperPrint print = JasperFillManager.fillReport(report, parameters, params);
		
		// Export DPF to output stream
		JasperExportManager.exportReportToPdfStream(print, out);
	}

}
