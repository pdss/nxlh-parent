package com.nxlh.minapp.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.nxlh.common.annotation.ApiController;
import com.nxlh.common.exceptions.UnAuthorizeException;
import com.nxlh.common.model.MyResult;
import com.nxlh.common.utils.StringUtils;
import com.nxlh.manager.model.dto.WxUserAddressDTO;
import com.nxlh.manager.service.WxUserAddressService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@ApiController("/api/wx/addr")
public class AddressController extends BaseController {

    @Reference
    private WxUserAddressService wxUserAddressService;


    /**
     * 获取用户的收货地地址
     *
     * @return
     * @throws UnAuthorizeException
     */
    @GetMapping("getall")
    public MyResult getAll() throws UnAuthorizeException {

        var list = this.wxUserAddressService.getByWxUId(this.getUserId());
        return ok(list);
    }


    /**
     * 添加或者编辑收货地址
     *
     * @return
     */
    @PostMapping("save")
    public MyResult save(@RequestBody WxUserAddressDTO req) throws UnAuthorizeException {
        req.setWxuserid(this.getUserId());
        var result = this.wxUserAddressService.addOrUpdateAddress(req);
        return ok(result);
    }

    /**
     * 根据ID获取详情
     *
     * @return
     */
    @GetMapping("getbyid")
    public MyResult getById(@RequestParam String id) {
        var address = this.wxUserAddressService.getById(id);
        return ok(address);
    }

    /**
     * 删除地址
     *
     * @return
     */
    @GetMapping("delete")
    public MyResult delete(@RequestParam String id) {
        if (StringUtils.isEmpty(id)) {
            return badreq("无效参数");
        }
        var result = this.wxUserAddressService.deleteById(id);
        if (result) {
            return MyResult.ok();
        }
        return MyResult.error("系统异常");
    }

}
