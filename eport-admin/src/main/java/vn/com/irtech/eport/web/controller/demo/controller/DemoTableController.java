package vn.com.irtech.eport.web.controller.demo.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.fasterxml.jackson.annotation.JsonFormat;
import vn.com.irtech.eport.common.core.controller.BaseController;
import vn.com.irtech.eport.common.core.page.PageDomain;
import vn.com.irtech.eport.common.core.page.TableDataInfo;
import vn.com.irtech.eport.common.core.page.TableSupport;
import vn.com.irtech.eport.common.utils.DateUtils;
import vn.com.irtech.eport.common.utils.StringUtils;

/**
 * Table related
 *
 * @author admin
 */
@Controller
@RequestMapping("/demo/table")
public class DemoTableController extends BaseController
{
    private String prefix = "demo/table";

    private final static List<UserTableModel> users = new ArrayList<UserTableModel>();
    {
        users.add(new UserTableModel(1, "1000001", "Test 1", "0", "15888888888", "ry@qq.com", 150.0, "0"));
        users.add(new UserTableModel(2, "1000002", "Test 2", "1", "15666666666", "ry@qq.com", 180.0, "1"));
        users.add(new UserTableModel(3, "1000003", "Test 3", "0", "15666666666", "ry@qq.com", 110.0, "1"));
        users.add(new UserTableModel(4, "1000004", "Test 4", "1", "15666666666", "ry@qq.com", 220.0, "1"));
        users.add(new UserTableModel(5, "1000005", "Test 5", "0", "15666666666", "ry@qq.com", 140.0, "1"));
        users.add(new UserTableModel(6, "1000006", "Test 6", "1", "15666666666", "ry@qq.com", 330.0, "1"));
        users.add(new UserTableModel(7, "1000007", "Test 7", "0", "15666666666", "ry@qq.com", 160.0, "1"));
        users.add(new UserTableModel(8, "1000008", "Test 8", "1", "15666666666", "ry@qq.com", 170.0, "1"));
        users.add(new UserTableModel(9, "1000009", "Test 9", "0", "15666666666", "ry@qq.com", 180.0, "1"));
        users.add(new UserTableModel(10, "1000010", "Test 10", "0", "15666666666", "ry@qq.com", 210.0, "1"));
        users.add(new UserTableModel(11, "1000011", "Test 11", "1", "15666666666", "ry@qq.com", 110.0, "1"));
        users.add(new UserTableModel(12, "1000012", "Test 12", "0", "15666666666", "ry@qq.com", 120.0, "1"));
        users.add(new UserTableModel(13, "1000013", "Test 13", "1", "15666666666", "ry@qq.com", 380.0, "1"));
        users.add(new UserTableModel(14, "1000014", "Test 14", "0", "15666666666", "ry@qq.com", 280.0, "1"));
        users.add(new UserTableModel(15, "1000015", "Test 15", "0", "15666666666", "ry@qq.com", 570.0, "1"));
        users.add(new UserTableModel(16, "1000016", "Test 16", "1", "15666666666", "ry@qq.com", 260.0, "1"));
        users.add(new UserTableModel(17, "1000017", "Test 17", "1", "15666666666", "ry@qq.com", 210.0, "1"));
        users.add(new UserTableModel(18, "1000018", "Test 18", "1", "15666666666", "ry@qq.com", 340.0, "1"));
        users.add(new UserTableModel(19, "1000019", "Test 19", "1", "15666666666", "ry@qq.com", 160.0, "1"));
        users.add(new UserTableModel(20, "1000020", "Test 20", "1", "15666666666", "ry@qq.com", 220.0, "1"));
        users.add(new UserTableModel(21, "1000021", "Test 21", "1", "15666666666", "ry@qq.com", 120.0, "1"));
        users.add(new UserTableModel(22, "1000022", "Test 22", "1", "15666666666", "ry@qq.com", 130.0, "1"));
        users.add(new UserTableModel(23, "1000023", "Test 23", "1", "15666666666", "ry@qq.com", 490.0, "1"));
        users.add(new UserTableModel(24, "1000024", "Test 24", "1", "15666666666", "ry@qq.com", 570.0, "1"));
        users.add(new UserTableModel(25, "1000025", "Test 25", "1", "15666666666", "ry@qq.com", 250.0, "1"));
        users.add(new UserTableModel(26, "1000026", "Test 26", "1", "15666666666", "ry@qq.com", 250.0, "1"));
    }

    /**
     * Search related
     */
    @GetMapping("/search")
    public String search()
    {
        return prefix + "/search";
    }

    /**
     * Data summary
     */
    @GetMapping("/footer")
    public String footer()
    {
        return prefix + "/footer";
    }

    /**
     * Combination header
     */
    @GetMapping("/groupHeader")
    public String groupHeader()
    {
        return prefix + "/groupHeader";
    }

    /**
     * Table export
     */
    @GetMapping("/export")
    public String export()
    {
        return prefix + "/export";
    }

    /**
     * Turn page to remember choice
     */
    @GetMapping("/remember")
    public String remember()
    {
        return prefix + "/remember";
    }

    /**
     * Jump to the specified page
     */
    @GetMapping("/pageGo")
    public String pageGo()
    {
        return prefix + "/pageGo";
    }

    /**
     * Custom query parameters
     */
    @GetMapping("/params")
    public String params()
    {
        return prefix + "/params";
    }

    /**
     * Multiple forms
     */
    @GetMapping("/multi")
    public String multi()
    {
        return prefix + "/multi";
    }

    /**
     * Click the button to load the form
     */
    @GetMapping("/button")
    public String button()
    {
        return prefix + "/button";
    }

    /**
     * Table freeze column
     */
    @GetMapping("/fixedColumns")
    public String fixedColumns()
    {
        return prefix + "/fixedColumns";
    }

    /**
     * Custom trigger event
     */
    @GetMapping("/event")
    public String event()
    {
        return prefix + "/event";
    }

    /**
     * Form detail view
     */
    @GetMapping("/detail")
    public String detail()
    {
        return prefix + "/detail";
    }
    
    /**
     * Table parent-child view
     */
    @GetMapping("/child")
    public String child()
    {
        return prefix + "/child";
    }

    /**
     * Table picture preview
     */
    @GetMapping("/image")
    public String image()
    {
        return prefix + "/image";
    }

    /**
     * Dynamic addition, deletion and modification
     */
    @GetMapping("/curd")
    public String curd()
    {
        return prefix + "/curd";
    }

    /**
     * Table drag and drop operation
     */
    @GetMapping("/reorder")
    public String reorder()
    {
        return prefix + "/reorder";
    }

    /**
     * Edit operation in table row
     */
    @GetMapping("/editable")
    public String editable()
    {
        return prefix + "/editable";
    }

    /**
     * Other operations
     */
    @GetMapping("/other")
    public String other()
    {
        return prefix + "/other";
    }

    /**
     * Query data
     */
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(UserTableModel userModel)
    {
        TableDataInfo rspData = new TableDataInfo();
        List<UserTableModel> userList = new ArrayList<UserTableModel>(Arrays.asList(new UserTableModel[users.size()]));
        Collections.copy(userList, users);
        // Query filter
        if (StringUtils.isNotEmpty(userModel.getUserName()))
        {
            userList.clear();
            for (UserTableModel user : users)
            {
                if (user.getUserName().equals(userModel.getUserName()))
                {
                    userList.add(user);
                }
            }
        }
        PageDomain pageDomain = TableSupport.buildPageRequest();
        if (null == pageDomain.getPageNum() || null == pageDomain.getPageSize())
        {
            rspData.setRows(userList);
            rspData.setTotal(userList.size());
            return rspData;
        }
        Integer pageNum = (pageDomain.getPageNum() - 1) * 10;
        Integer pageSize = pageDomain.getPageNum() * 10;
        if (pageSize > userList.size())
        {
            pageSize = userList.size();
        }
        rspData.setRows(userList.subList(pageNum, pageSize));
        rspData.setTotal(userList.size());
        return rspData;
    }
}

class UserTableModel
{
    /** User ID */
    private int userId;

    /** user ID */
    private String userCode;

    /** username */
    private String userName;

    /** User gender */
    private String userSex;

    /** User phone */
    private String userPhone;

    /** User email */
    private String userEmail;

    /** User balance */
    private double userBalance;

    /** User status (0 normal 1 disabled) */
    private String status;

    /** Creation time */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    public UserTableModel()
    {

    }

    public UserTableModel(int userId, String userCode, String userName, String userSex, String userPhone,
            String userEmail, double userBalance, String status)
    {
        this.userId = userId;
        this.userCode = userCode;
        this.userName = userName;
        this.userSex = userSex;
        this.userPhone = userPhone;
        this.userEmail = userEmail;
        this.userBalance = userBalance;
        this.status = status;
        this.createTime = DateUtils.getNowDate();
    }

    public int getUserId()
    {
        return userId;
    }

    public void setUserId(int userId)
    {
        this.userId = userId;
    }

    public String getUserCode()
    {
        return userCode;
    }

    public void setUserCode(String userCode)
    {
        this.userCode = userCode;
    }

    public String getUserName()
    {
        return userName;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public String getUserSex()
    {
        return userSex;
    }

    public void setUserSex(String userSex)
    {
        this.userSex = userSex;
    }

    public String getUserPhone()
    {
        return userPhone;
    }

    public void setUserPhone(String userPhone)
    {
        this.userPhone = userPhone;
    }

    public String getUserEmail()
    {
        return userEmail;
    }

    public void setUserEmail(String userEmail)
    {
        this.userEmail = userEmail;
    }

    public double getUserBalance()
    {
        return userBalance;
    }

    public void setUserBalance(double userBalance)
    {
        this.userBalance = userBalance;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public Date getCreateTime()
    {
        return createTime;
    }

    public void setCreateTime(Date createTime)
    {
        this.createTime = createTime;
    }
}
