package vn.com.irtech.eport.carrier.task;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Collection;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.RegexFileFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author GiapHD
 *
 */
@Component("edoTask")
public class CarrierEdoTask {

	private static final Logger logger = LoggerFactory.getLogger(CarrierEdoTask.class); 
	
	@Autowired
	private CarrierEdoFolderMonitorTask carrierEdoMonitorTask;

	@Value("${eport.edi.rootPath}")
	private String edoRootPath;

	public void checkEdiFile(Integer createDiffTimeInMinutes) throws IOException {
		Collection<File> files = FileUtils.listFiles(new File(edoRootPath), new RegexFileFilter("^(.*?)"), DirectoryFileFilter.DIRECTORY);
		for(File f : files) {
			if(f.isDirectory()) {
				//skip
				continue;
			}
			// Get create time
			BasicFileAttributes attr = Files.readAttributes(f.toPath(), BasicFileAttributes.class);
			long fileTimeInMs = attr.creationTime().toMillis();
			long currentTimeInMs = System.currentTimeMillis();
			long createDiffTimeInMs = createDiffTimeInMinutes * 60 * 1000;
			if(currentTimeInMs - fileTimeInMs > createDiffTimeInMs) {
				logger.debug("Found file :{} , create at {}", f.getAbsolutePath(), attr.creationTime().toString());
				carrierEdoMonitorTask.offerQueue(f.getAbsolutePath());
			}
		}
	}
}
