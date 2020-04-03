package vn.com.irtech.eport.edo.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import vn.com.irtech.eport.common.annotation.Excel;
import vn.com.irtech.eport.common.core.domain.BaseEntity;

/**
 * Equipment attached document Object equipment_document
 * 
 * @author irtech
 * @date 2020-04-03
 */
public class EquipmentDocument extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** File Name */
    @Excel(name = "File Name")
    private String fileName;

    /** Original File Name */
    @Excel(name = "Original File Name")
    private String originalFileName;

    /** File Path */
    @Excel(name = "File Path")
    private String filePath;

    /** Mime Type */
    @Excel(name = "Mime Type")
    private String mimeType;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setFileName(String fileName) 
    {
        this.fileName = fileName;
    }

    public String getFileName() 
    {
        return fileName;
    }
    public void setOriginalFileName(String originalFileName) 
    {
        this.originalFileName = originalFileName;
    }

    public String getOriginalFileName() 
    {
        return originalFileName;
    }
    public void setFilePath(String filePath) 
    {
        this.filePath = filePath;
    }

    public String getFilePath() 
    {
        return filePath;
    }
    public void setMimeType(String mimeType) 
    {
        this.mimeType = mimeType;
    }

    public String getMimeType() 
    {
        return mimeType;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("fileName", getFileName())
            .append("originalFileName", getOriginalFileName())
            .append("filePath", getFilePath())
            .append("mimeType", getMimeType())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
