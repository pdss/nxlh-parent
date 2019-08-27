package com.nxlh.manager.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.nxlh.common.annotation.ApiController;
import com.nxlh.common.model.MyResult;
import com.nxlh.common.utils.DateUtils;
import com.nxlh.common.utils.StringUtils;
import com.nxlh.manager.model.dbo.TbOrder;
import com.nxlh.manager.model.dbo.TbOrderRefund;
import com.nxlh.manager.model.dto.OrderItemDTO;
import com.nxlh.manager.model.enums.OrderEnums;
import com.nxlh.manager.model.vo.order.OrderQueryVO;
import com.nxlh.manager.model.vo.order.OrderRefundVO;
import com.nxlh.manager.model.vo.order.OrderTransitVO;
import com.nxlh.manager.service.WebOrderService;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFDataFormat;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import com.nxlh.manager.service.OrderService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.*;
import java.math.BigDecimal;
import java.util.*;

import static java.util.stream.Collectors.toList;

@ApiController("/api/web/order")
public class OrderController extends BaseController {


    @Reference(timeout = 30000, retries = 1)
    private OrderService orderService;

    @Reference()
    private WebOrderService webOrderService;

    /**
     * 订单列表
     *
     * @param queryVO
     * @return
     */
    @GetMapping("listbypage")
    public MyResult listByPage(OrderQueryVO queryVO) throws Exception {
        var page = this.makePage(queryVO);
        Map<String, Object> map = new HashMap<>();
        map.put("type", queryVO.getFilter());
        if (StringUtils.isNotEmpty(queryVO.getSearchFilter())) {
            map.put("filter", queryVO.getSearchFilter());
        }
        var result = this.orderService.query(page, map, defaultOrderBy, new Integer[]{0});
        return ok(result);
    }

    /**
     * 订单详情
     *
     * @param id
     * @return
     */
    @GetMapping("getdetails")
    public MyResult getById(@RequestParam String id) {
        var obj = this.orderService.getDetails(id);
        return this.ok(obj);
    }


    /**
     * 发货
     *
     * @return
     */
    @PostMapping("transit")
    public MyResult transit(@RequestBody OrderItemDTO request) {
        return this.orderService.transit(request.getId(), request.getExpress(), request.getTracknumber());
    }

    /**
     * 同意退款
     *
     * @return
     */
    @PostMapping("accessrefund")
    public MyResult accessRefund(@RequestBody OrderRefundVO request) throws WxPayException {
        return this.orderService.accessRefund(request.getRefundId(), request.getRefundprice());
    }

    /**
     * 拒绝退款
     *
     * @return
     */
    @PostMapping("rejectrefund")
    public MyResult rejectRefund(@RequestBody OrderRefundVO request) {
        return this.orderService.rejectRefund(request.getRefundId(), "");
    }

//    /**
//     * 退款列表
//     *
//     * @param id
//     * @return
//     */
//    @GetMapping("getrefund")
//    public MyResult getRefund(@RequestParam String id) {
//        return this.orderService.getRefundByOrderId(id);
//    }

    /**
     * 退还租借商品
     *
     * @param id
     * @return
     * @throws WxPayException
     */
    @GetMapping("sendback")
    public MyResult sendBack(@RequestParam String id, @RequestParam BigDecimal price) throws WxPayException {
        return this.orderService.refundRentOrder(id, price);
    }

    @PostMapping("refundbyshop")
    public Boolean refundByShop(HttpServletRequest request, HttpServletResponse response) {
        String id = request.getParameter("id");
//        return false;
        return this.webOrderService.refundById(Integer.parseInt(id));
    }

    @PostMapping("closeorder")
    public Boolean closeOrder(@RequestParam List<String> ids) {
        if (ids.size() > 0) {
            List<Long> longs = new ArrayList<Long>();
            ids.forEach(e -> {
                longs.add(Long.parseLong(e));
            });
            if (longs.size() > 0) {
                var res = this.orderService.closeWebOrder(longs);
                return res;
        }
        }
        return false;
    }


    //region 弃用
    @GetMapping("exportexcel")
    public MyResult exportExcel2(@RequestParam String startDateStr, @RequestParam String endDateStr, HttpServletRequest request, HttpServletResponse response) throws IOException {
//        String startDateStr = Date.get("startDate");
//        String endDateStr = Date.get("endDate");
        Date startDate = DateUtils.getDateByStr(startDateStr);
//        Date endDate = DateUtils.getDateByStr(endDateStr);
        Date endDate = DateUtils.addDay(DateUtils.getDateByStr(endDateStr), 1);
        var allDetails = this.orderService.createExcelByOrder(startDate, endDate);
        //将文件读入
//        InputStream in = new FileInputStream(ResourceUtils.getFile(filterPath));
        InputStream in = this.getClass().getClassLoader().getResourceAsStream("123.xlsx");
        //创建工作簿
        XSSFWorkbook wb = new XSSFWorkbook(in);
        //读取第一个sheet
        Sheet sheet = wb.getSheetAt(0);
        //获取第一行
//        Row rowTitle = sheet.getRow(0);
//        rowTitle.getCell(0).setCellValue(startDateStr + "/" + endDateStr + "   " + rowTitle.getCell(0));
        int index = 2;
        XSSFCellStyle cellStyle = wb.createCellStyle();
        XSSFCellStyle numCellStyle = wb.createCellStyle();
        XSSFDataFormat format = wb.createDataFormat();
        numCellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("0.00"));

//        cellStyle.setDataFormat(format.getFormat("yyyy年m月d日 HH:mm"));
        for (int i = 0; i < allDetails.size(); i++) {
            Row row = sheet.createRow(i + 1);
//            System.out.println(i);
            row.createCell(0).setCellValue(allDetails.get(i).get("orderno").toString());//订单号
            row.createCell(1).setCellValue(allDetails.get(i).get("productname").toString());//商品名称
            row.createCell(2).setCellValue(allDetails.get(i).get("buycount").toString());//商品数量
            row.createCell(3).setCellValue((double) allDetails.get(i).get("productprice"));//商品金额
            row.getCell(3).setCellStyle(numCellStyle);
            row.createCell(4).setCellValue((double) allDetails.get(i).get("sumprice"));//合计金额
            row.getCell(4).setCellStyle(numCellStyle);
            row.createCell(5).setCellValue((double) allDetails.get(i).get("refundPrice"));//退款金额
            row.getCell(5).setCellStyle(numCellStyle);
            row.createCell(6).setCellValue((Date) allDetails.get(i).get("addtime"));//下单日期
            row.getCell(6).setCellStyle(cellStyle);
            row.createCell(7).setCellValue(allDetails.get(i).get("ordertype").toString());//订单类型
            row.createCell(8).setCellValue(allDetails.get(i).get("status").toString());//订单状态
            row.createCell(9).setCellValue(allDetails.get(i).get("nickname").toString());//微信昵称
            row.createCell(10).setCellValue(allDetails.get(i).get("phone").toString());//微信手机号
            row.createCell(11).setCellValue(allDetails.get(i).get("receivername").toString());//收件人姓名
            row.createCell(12).setCellValue(allDetails.get(i).get("receiverphone").toString());//收件人手机号
            row.createCell(13).setCellValue(allDetails.get(i).get("receiveprovince").toString());//省
            row.createCell(14).setCellValue(allDetails.get(i).get("receivecity").toString());//市
            row.createCell(15).setCellValue(allDetails.get(i).get("receivearea").toString());//区
            row.createCell(16).setCellValue(allDetails.get(i).get("receiveaddress").toString());//详细地址
            row.createCell(17).setCellValue(allDetails.get(i).get("remark") == null ? "" : allDetails.get(i).get("remark").toString());//备注

        }
//
//        ByteArrayOutputStream output = new ByteArrayOutputStream();
//        wb.write(output);
//        byte[] b = output.toByteArray();
//        output.close();
//        return this.ok(b);

        OutputStream output = response.getOutputStream();
        response.reset();
        String agent = request.getHeader("USER-AGENT").toLowerCase();
        response.setContentType("application/json");
        String fileName = startDateStr + "-" + endDateStr + "订单报表";
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

//    @GetMapping("exportexcel2")
//    public MyResult exportExcel2(HttpServletRequest request, HttpServletResponse response) throws IOException {
////        String startDateStr = Date.get("startDate");
////        String endDateStr = Date.get("endDate");
//
//        Date startDate = DateUtils.addDay(new Date(), -3);
////        Date endDate = DateUtils.getDateByStr(endDateStr);
//        Date endDate = new Date();
//        var allDetails = this.orderService.createExcelByOrder(startDate, endDate);
//        //将文件读入
////        InputStream in = new FileInputStream(ResourceUtils.getFile(filterPath));
//        InputStream in = this.getClass().getClassLoader().getResourceAsStream("mb.xls");
//        //创建工作簿
//        XSSFWorkbook wb = new XSSFWorkbook(in);
//        //读取第一个sheet
//        Sheet sheet = wb.getSheetAt(0);
//        //获取第一行
////        Row rowTitle = sheet.getRow(0);
////        rowTitle.getCell(0).setCellValue(startDateStr + "/" + endDateStr + "   " + rowTitle.getCell(0));
////        int index = 2;
//        XSSFCellStyle cellStyle = wb.createCellStyle();
//        XSSFCellStyle numCellStyle = wb.createCellStyle();
//        XSSFDataFormat format = wb.createDataFormat();
//        numCellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("0.00"));
//
////        cellStyle.setDataFormat(format.getFormat("yyyy年m月d日 HH:mm"));
//        for (int i = 0; i < allDetails.size(); i++) {
//            Row row = sheet.createRow(i + 1);
////            row.createCell(0).setCellValue(allDetails.get(i).get("orderno").toString());//订单号
////            row.createCell(1).setCellValue(allDetails.get(i).get("productname").toString());//商品名称
////            row.createCell(2).setCellValue((double) allDetails.get(i).get("productprice"));//商品金额
////            row.getCell(2).setCellStyle(numCellStyle);
////            row.createCell(3).setCellValue((double) allDetails.get(i).get("sumprice"));//合计金额
////            row.getCell(3).setCellStyle(numCellStyle);
////            row.createCell(4).setCellValue((double) allDetails.get(i).get("refundPrice"));//退款金额
////            row.getCell(4).setCellStyle(numCellStyle);
////            row.createCell(5).setCellValue(allDetails.get(i).get("buycount").toString());//数量
////            row.createCell(6).setCellValue((Date) allDetails.get(i).get("addtime"));//下单日期
////            row.getCell(6).setCellStyle(cellStyle);
////            row.createCell(7).setCellValue(allDetails.get(i).get("ordertype").toString());//订单类型
////            row.createCell(8).setCellValue(allDetails.get(i).get("status").toString());//订单状态
////            row.createCell(9).setCellValue(allDetails.get(i).get("nickname").toString());//微信昵称
////            row.createCell(10).setCellValue(allDetails.get(i).get("phone").toString());//微信手机号
////            row.createCell(11).setCellValue(allDetails.get(i).get("receivername").toString());//收件人姓名
////            row.createCell(12).setCellValue(allDetails.get(i).get("receiverphone").toString());//收件人手机号
//            row.createCell(0).setCellValue(allDetails.get(i).get("orderno").toString());//订单号
//           row.createCell(1).setCellValue(allDetails.get(i).get("nickname").toString());//微信昵称
//            row.createCell(3).setCellValue(allDetails.get(i).get("receiveprovince").toString());//省
//            row.createCell(4).setCellValue(allDetails.get(i).get("receivecity").toString());//市
//            row.createCell(5).setCellValue(allDetails.get(i).get("receivearea").toString());//区
//            row.createCell(6).setCellValue(allDetails.get(i).get("receiveaddress").toString());//详细地址
//            row.createCell(7).setCellValue(allDetails.get(i).get("receivername").toString());//收件人姓名
//            row.createCell(8).setCellValue(allDetails.get(i).get("receiverphone").toString());//收件人手机号
//            row.createCell(8).setCellValue(allDetails.get(i).get("receiverphone").toString());//收件人手机号
//            row.createCell(15).setCellValue((int) allDetails.get(i).get("buycount"));//购买数量
//
//            row.createCell(0).setCellValue(allDetails.get(i).get("productname").toString());//商品名称
//            row.createCell(2).setCellValue(allDetails.get(i).get("status").toString());//订单状态
//            row.createCell(3).setCellValue((double) allDetails.get(i).get("sumprice"));//商品金额
//            row.getCell(3).setCellStyle(numCellStyle);
//            row.createCell(6).setCellValue((Double.valueOf(allDetails.get(i).get("expressprice").toString())));//邮费
//            row.getCell(6).setCellStyle(numCellStyle);
//            row.createCell(10).setCellValue(Double.valueOf(allDetails.get(i).get("payprice").toString()));//商家实收金额
//            row.getCell(10).setCellStyle(numCellStyle);
//            row.createCell(11).setCellValue((int) allDetails.get(i).get("buycount"));//购买数量
//            row.createCell(15).setCellValue(allDetails.get(i).get("receiverphone").toString());//收件人手机号
//            row.createCell(16).setCellValue("正常");//是否正常
//
//            row.createCell(22).setCellValue((Date)allDetails.get(i).get("addtime"));//下单时间
//            row.getCell(22).setCellStyle(cellStyle);
//            row.createCell(26).setCellValue(allDetails.get(i).get("productId").toString());//商品id
//
//            allDetails.get(i).get("deliverytime");
//
//        }
////
////        ByteArrayOutputStream output = new ByteArrayOutputStream();
////        wb.write(output);
////        byte[] b = output.toByteArray();
////        output.close();
////        return this.ok(b);
//
//        OutputStream output = response.getOutputStream();
//        response.reset();
//        String agent = request.getHeader("USER-AGENT").toLowerCase();
//        response.setContentType("application/json");
//        String fileName = "test";
//        String codedFileName = java.net.URLEncoder.encode(fileName, "UTF-8");
//        if (agent.contains("firefox")) {
//            response.setCharacterEncoding("utf-8");
//            response.setHeader("content-disposition", "attachment;filename=" + new String(fileName.getBytes(), "ISO8859-1") + ".xlsx");
//        } else {
//            response.setHeader("content-disposition", "attachment;filename=" + codedFileName + ".xls");
//        }
//        wb.write(output);
//
//        output.close();
//        return null;
//    }
    //endregion


}
