package vn.com.irtech.eport.web.controller.tool;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.com.irtech.eport.common.core.controller.BaseController;
import vn.com.irtech.eport.common.core.domain.AjaxResult;
import vn.com.irtech.eport.common.utils.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;

/**
 * Swagger test controller
 * 
 * @author admin
 */
@Api("User information management")
@RestController
@RequestMapping("/test/user")
public class TestController extends BaseController
{
    private final static Map<Integer, UserEntity> users = new LinkedHashMap<Integer, UserEntity>();
    {
        users.put(1, new UserEntity(1, "admin", "admin123", "15888888888"));
        users.put(2, new UserEntity(2, "ry", "admin123", "15666666666"));
    }

    @ApiOperation("Get user list")
    @GetMapping("/list")
    public AjaxResult userList()
    {
        List<UserEntity> userList = new ArrayList<UserEntity>(users.values());
        return AjaxResult.success(userList);
    }

    @ApiOperation("Get user details")
    @ApiImplicitParam(name = "userId", value = "用户ID", required = true, dataType = "int", paramType = "path")
    @GetMapping("/{userId}")
    public AjaxResult getUser(@PathVariable Integer userId)
    {
        if (!users.isEmpty() && users.containsKey(userId))
        {
            return AjaxResult.success(users.get(userId));
        }
        else
        {
            return error("Người dùng không tồn tại!");
        }
    }

    @ApiOperation("New users")
    @ApiImplicitParam(name = "userEntity", value = "Add user information", dataType = "UserEntity")
    @PostMapping("/save")
    public AjaxResult save(UserEntity user)
    {
        if (StringUtils.isNull(user) || StringUtils.isNull(user.getUserId()))
        {
            return error("ID người dùng không được để trống!");
        }
        return AjaxResult.success(users.put(user.getUserId(), user));
    }

    @ApiOperation("Update user")
    @ApiImplicitParam(name = "userEntity", value = "Add user information", dataType = "UserEntity")
    @PutMapping("/update")
    public AjaxResult update(UserEntity user)
    {
        if (StringUtils.isNull(user) || StringUtils.isNull(user.getUserId()))
        {
            return error("ID người dùng không được để trống!");
        }
        if (users.isEmpty() || !users.containsKey(user.getUserId()))
        {
            return error("Người dùng không tồn tại!");
        }
        users.remove(user.getUserId());
        return AjaxResult.success(users.put(user.getUserId(), user));
    }

    @ApiOperation("Delete user information")
    @ApiImplicitParam(name = "userId", value = "User ID", required = true, dataType = "int", paramType = "path")
    @DeleteMapping("/{userId}")
    public AjaxResult delete(@PathVariable Integer userId)
    {
        if (!users.isEmpty() && users.containsKey(userId))
        {
            users.remove(userId);
            return success();
        }
        else
        {
            return error("Người dùng không tồn tại!");
        }
    }
}

@ApiModel("User entity")
class UserEntity
{
    @ApiModelProperty("User ID")
    private Integer userId;

    @ApiModelProperty("User Name")
    private String username;

    @ApiModelProperty("User Password")
    private String password;

    @ApiModelProperty("User Phone")
    private String mobile;

    public UserEntity()
    {

    }

    public UserEntity(Integer userId, String username, String password, String mobile)
    {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.mobile = mobile;
    }

    public Integer getUserId()
    {
        return userId;
    }

    public void setUserId(Integer userId)
    {
        this.userId = userId;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getMobile()
    {
        return mobile;
    }

    public void setMobile(String mobile)
    {
        this.mobile = mobile;
    }
}
