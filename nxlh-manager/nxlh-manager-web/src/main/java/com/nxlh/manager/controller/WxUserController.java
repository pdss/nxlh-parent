package com.nxlh.manager.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.nxlh.common.annotation.ApiController;
import com.nxlh.common.enums.HttpResponseEnums;
import com.nxlh.common.exceptions.UnAuthorizeException;
import com.nxlh.common.model.MyResult;
import com.nxlh.common.utils.DateUtils;
import com.nxlh.common.utils.StringUtils;
import com.nxlh.manager.model.UpdateUserScoreVO;
import com.nxlh.manager.model.WxUserQueryVO;
import com.nxlh.manager.model.dto.WxUserDTO;
import com.nxlh.manager.model.vo.shop.ShopQueryVO;
import com.nxlh.manager.service.UserCouponService;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFDataFormat;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.bouncycastle.cms.PasswordRecipientId;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import com.nxlh.manager.service.WxUserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@ApiController("/api/web/wxuser")
public class WxUserController extends BaseController {

    @Reference()
    private WxUserService wxUserService;

    @Reference
    private UserCouponService userCouponService;


    @GetMapping("listbypage")
    public MyResult listByPage(Optional<WxUserQueryVO> queryVO) {
        var page = this.makePage(queryVO);

        Map<String, Object> map = new HashMap<>();
        if (StringUtils.isNotEmpty(queryVO.get().getSearchFilter())) {
            map.put("nickname", queryVO.get().getSearchFilter());
            map.put("phone", queryVO.get().getSearchFilter());
        }
        if (!CollectionUtils.isEmpty(queryVO.get().getVipType())) map.put("viptype", queryVO.get().getVipType());

        var result = this.wxUserService.page(page, map, defaultOrderBy, 1);
        return ok(result);
    }

    @PostMapping("sendcoupons")
    public MyResult sendCoupons(@RequestBody WxUserDTO wxUserDTO) {
        boolean isOk = userCouponService.insertWxuserCoupons(wxUserDTO);
        return isOk ? ok(true) : json(HttpResponseEnums.InternalServerError, false);
    }


    @PostMapping("setuserscore")
    public MyResult updateUserScore(@RequestBody UpdateUserScoreVO req) throws UnAuthorizeException {
        return this.wxUserService.updateUserScore(req.getUserId(), req.getAddvscore());
    }

    @GetMapping("searchbyphone")
    public MyResult searchByPhone(@RequestParam String phone, @RequestParam(defaultValue = "5") Integer count) {
        return this.wxUserService.searchByPhone(phone, count);
    }


    @PostMapping("editusertype")
    public MyResult editUserType(@RequestBody WxUserDTO wxUserDTO) throws IOException {
        return this.wxUserService.editUserType(wxUserDTO);
    }

    @GetMapping("edit")
    public MyResult edit() throws IOException {
        var model = this.wxUserService.getByUnionId("2048");
        return this.wxUserService.editUserType(model);
    }

    @GetMapping("test")
    public void test() throws Exception {
        boolean model;
        if (this.wxUserService.checkVipIsInvalidation()) model = true;
        else model = false;
    }

    /**
     * 导出本日会员信息详情表
     * 暂时弃用
     *
     * @param request
     * @param response
     * @return
     * @throws IOException
     */
    @GetMapping("exportexcel")
    public MyResult exportExcel(HttpServletRequest request, HttpServletResponse response) throws IOException {

        Date now = new Date();
        var allDetails = this.wxUserService.queryAllVIP();
        InputStream in = this.getClass().getClassLoader().getResourceAsStream("vip.xlsx");
        //创建工作簿
        XSSFWorkbook wb = new XSSFWorkbook(in);
        //读取第一个sheet
        Sheet sheet = wb.getSheetAt(0);
        //获取第一行
        Row rowTitle = sheet.getRow(0);
        rowTitle.getCell(0).setCellValue(now.toString() + rowTitle.getCell(0));
        int index = 2;
        XSSFCellStyle cellStyle = wb.createCellStyle();
        XSSFCellStyle numCellStyle = wb.createCellStyle();
        XSSFDataFormat format = wb.createDataFormat();
        numCellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("0.00"));

        cellStyle.setDataFormat(format.getFormat("yyyy年m月d日 HH:mm"));
        for (int i = 0; i < allDetails.size(); i++) {
            Row row = sheet.createRow(i + 2);
            row.createCell(0).setCellValue(allDetails.get(i).getId());//id
            row.createCell(1).setCellValue(allDetails.get(i).getNickname());//微信昵称
            row.createCell(2).setCellValue(allDetails.get(i).getPhone());//微信手机号
            row.createCell(3).setCellValue(allDetails.get(i).getVipid());//会员等级
            row.createCell(4).setCellValue(allDetails.get(i).getVscore().doubleValue());//用户积分
            row.getCell(4).setCellStyle(numCellStyle);
            row.createCell(5).setCellValue(allDetails.get(i).getSumpay().doubleValue());//
            row.getCell(5).setCellStyle(numCellStyle);
        }

        OutputStream output = response.getOutputStream();
        response.reset();
        String agent = request.getHeader("USER-AGENT").toLowerCase();
        response.setContentType("application/json");
        String fileName = now.toString() + "会员详情表";
        String codedFileName = java.net.URLEncoder.encode(fileName, "UTF-8");
        if (agent.contains("firefox")) {
            response.setCharacterEncoding("utf-8");
            response.setHeader("content-disposition", "attachment;filename=" + new String(fileName.getBytes(), "ISO8859-1") + ".xlsx");
        } else {
            response.setHeader("content-disposition", "attachment;filename=" + codedFileName + ".xls");
        }
        wb.write(output);
        output.close();

        return null;
    }
}
