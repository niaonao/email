package pers.niaonao.email.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pers.niaonao.email.model.EmailRequestVo;
import pers.niaonao.email.util.SMTPEmailUtil;

/**
 * @className: EmailController
 * @description: 邮件发送控制类
 * @author: niaonao
 * @date: 2019/7/25
 **/
@RestController
@RequestMapping(path = "/email")
@Slf4j
public class EmailController {

    /**
     * @description: 接口演示, 返回结果暂未做封装
     * @param emailRequest
     * @return: java.lang.String
     * @author: niaonao
     * @date: 2019/7/25
     */
    @PostMapping("/sendEmailByEnclosure")
    public String sendEmailByEnclosure(@RequestBody EmailRequestVo emailRequest){
        if (StringUtils.isEmpty(emailRequest.getSubject())){
            return "请维护邮件主题!";
        }
        if (CollectionUtils.isEmpty(emailRequest.getFileUrlList())) {
            return "请添加附件!";
        }
        if (CollectionUtils.isEmpty(emailRequest.getReceiverList())) {
            return "请维护收件人!";
        }
        SMTPEmailUtil.sendEmailByEnclosure(emailRequest.getSubject(), emailRequest.getHtml(), emailRequest.getFileUrlList(), emailRequest.getReceiverList(), emailRequest.getCarbonCopyList(), emailRequest.getBlindCarbonCopyList(), emailRequest.getSendDate());
        return "发送完成!";
    }
}
