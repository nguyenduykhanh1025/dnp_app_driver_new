package vn.com.irtech.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("tb_token")
public class TokenEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@TableId(type= IdType.INPUT)
	private Long userId;
	
	private String token;

	private Date expireTime;

	private Date updateTime;
	
}
