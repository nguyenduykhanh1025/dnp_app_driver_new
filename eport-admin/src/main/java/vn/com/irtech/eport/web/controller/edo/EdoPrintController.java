package vn.com.irtech.eport.web.controller.edo;

import java.io.OutputStream;
import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.PathVariable;
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
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.export.ExporterInputItem;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleExporterInputItem;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import vn.com.irtech.eport.carrier.domain.Edo;
import vn.com.irtech.eport.common.core.controller.BaseController;
import vn.com.irtech.eport.common.core.domain.AjaxResult;
import vn.com.irtech.eport.common.utils.CacheUtils;
import vn.com.irtech.eport.common.utils.DateUtils;
import vn.com.irtech.eport.framework.util.ShiroUtils;
import vn.com.irtech.eport.web.controller.AdminBaseController;

@Controller
@RequestMapping("/edo/print")
public class EdoPrintController extends BaseController{
	private static final Logger logger = LoggerFactory.getLogger(EdoPrintController.class);
	private String prefix = "edo/print";
	
	/**
	 * Print Delivery Order
	 */
	@GetMapping("/view/{key}")
	public String view(@PathVariable("key") String key, ModelMap mmap) {
		mmap.put("key", key);
		return prefix + "/deliveryOrder";
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
				if(!i.getBillOfLading().equals(blNo)) {
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
						final JRBeanCollectionDataSource params = new JRBeanCollectionDataSource(list);
						final Map<String, Object> parameters = new HashMap<>();
						final JasperPrint print = JasperFillManager.fillReport(report, parameters, params);
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

}
