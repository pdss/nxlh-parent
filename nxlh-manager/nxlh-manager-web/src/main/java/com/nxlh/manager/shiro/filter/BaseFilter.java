package com.nxlh.manager.shiro.filter;


import com.nxlh.common.enums.HttpResponseEnums;
import com.nxlh.common.model.MyResult;
import com.nxlh.common.utils.JsonUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface BaseFilter {
    default void responseJson(HttpServletResponse httpServletResponse, HttpResponseEnums e) throws IOException {
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setContentType("application/json");
        var resp = new MyResult(e, null);
        httpServletResponse.getWriter().write(JsonUtils.objectToJson(resp));
    }
}
