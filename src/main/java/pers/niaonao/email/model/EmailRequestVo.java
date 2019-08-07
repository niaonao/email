package pers.niaonao.email.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;
import java.util.List;

/**
 * @className: EmailRequestVo
 * @description: 封装请求Vo
 * @author: niaonao
 * @date: 2019/7/25
 **/
@Setter
@Getter
@ToString
public class EmailRequestVo {
    /**
     * 附件路径
     */
    private List<String> fileUrlList;
    /**
     * 收件人邮件集合
     */
    private List<String> receiverList;
    /**
     * 抄送人邮件集合
     */
    private List<String> carbonCopyList;
    /**
     * 密送人邮件集合
     */
    private List<String> blindCarbonCopyList;
    /**
     * 邮件标题
     */
    private String subject;
    /**
     * 普通文本内容邮件
     */
    private String text;
    /**
     * 网页格式文本
     */
    private String html;
    /**
     * 邮件发送日期
     */
    private Date sendDate;
}
