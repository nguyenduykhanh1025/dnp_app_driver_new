package vn.com.irtech.eport.web.controller.edo;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.alibaba.fastjson.JSONObject;
import vn.com.irtech.eport.carrier.domain.Edo;




public class EdoCommon {
	 
	
	
    public  List<Edo> readEdi(String[] text)
    {
        Edo edi = new Edo();
		ZoneId defaultZoneId = ZoneId.systemDefault();
		JSONObject obj = new JSONObject();
		List<Edo> listEdi = new ArrayList<>();
		System.out.print(text.toString());
		String business = "";
		int num = 1;
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
			}
			// //buildNo
			if(s.contains("UNH+"+num))
			{
				edi = new Edo();
				edi.setBusinessUnit(business);
			}
			if(s.contains("RFF+BM"))
			{
				if(!s.isEmpty())
				{
					s = s.substring(9,s.length());
					obj.put("buildNo", s);
					edi.setBillOfLading(s);
				}
				
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
				
				
			}
			//orderNo
			if(s.contains("RFF+AAJ"))	
			{
				if(!s.isEmpty())
				{
					s = s.substring(10,s.length());
					obj.put("orderNo", s);
					edi.setOrderNumber(s);
				}
			}
			//releaseTo
			if(s.contains("NAD+BJ"))
			{
				String[] releaseTo = s.split("\\+");
				if(!releaseTo[3].isEmpty())
				{
					releaseTo[3] = releaseTo[3].substring(0, releaseTo[3].length() - 1);
					obj.put("releaseTo", releaseTo[3]);
					edi.setConsignee(releaseTo[3]);
				}
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
					obj.put("validToDay", releaseDate);
					edi.setExpiredDem(releaseDate);
				}
			}                                                                                                                                                                                                                                                                                                                                                                                           
			//emptyContDepot
			if(s.contains("LOC+99"))
			{
				String[] emptyContDepotA = s.split("\\+");
				if(emptyContDepotA.length > 4){
					String[] emptyContDepot = emptyContDepotA[3].split(":");
					obj.put("emptyContDepot", emptyContDepot[0]);
					edi.setEmptyContainerDepot(emptyContDepot[0]);
				}
						
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
			}

			if(s.contains("UNT+20+"+num))
			{
				listEdi.add(edi);
				num++;
			}
			
		}
		return listEdi;
	}

	

}