package net.myspring.future.modules.api.web.controller;

import net.myspring.future.modules.api.service.B2b2Service;
import net.myspring.future.modules.api.web.form.B2b2Form;
import net.myspring.util.text.StringUtils;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping(value = "future/b2b2")
public class B2b2Controller {

    @Autowired
    private B2b2Service b2b2Service;

    @RequestMapping(value = "getYzmPic",method = RequestMethod.GET)
    public void getYzmPic(HttpServletResponse response) throws IOException {
        response.setContentType("image/gif");
        Response pic = b2b2Service.getYzmPic();
        FileCopyUtils.copy(pic.body().bytes(), response.getOutputStream());
        pic.close();
    }

    @RequestMapping(value = "login",method = RequestMethod.POST)
    public boolean login(B2b2Form b2b2Form) {
        boolean status=b2b2Service.login(b2b2Form);
        return status;
    }

    @RequestMapping(value = "setStatus", method = RequestMethod.GET)
    public void setStatus(B2b2Form b2b2Form) {
        if(b2b2Service.checkLogin(b2b2Form.getUsername())){
            b2b2Service.setStatus(b2b2Form);
        }
    }

    @RequestMapping(value = "tuiTask", method = RequestMethod.GET)
    public void tuiTask(B2b2Form b2b2Form) {
        if(b2b2Service.checkLogin(b2b2Form.getUsername())){
            if (StringUtils.isNotEmpty(b2b2Form.getCodeStr())) {
                b2b2Service.tuiTask(b2b2Form);
            }
        }
    }

    @RequestMapping(value = "startCdTask")
    public void startCdTask(B2b2Form b2b2Form) {
        if(StringUtils.isNotEmpty(b2b2Form.getOrderIdStr())){
            b2b2Service.startCdTask(b2b2Form);
        }
    }
}
