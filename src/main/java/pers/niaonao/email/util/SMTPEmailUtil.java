package pers.niaonao.email.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import pers.niaonao.email.constant.SystemConstant;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;
import java.util.Properties;

/**
 * @className: SMTPEmailUtil
 * @description: 邮件工具类
 *      SMTP 协议发送邮件
 *
 *      推荐文章: <<手工体验smtp和pop3协议>> https://www.cnblogs.com/ysocean/p/7653252.html
 *
 *      (1) 获取会话 Session, 配置环境信息及用户授权验证
 *      (2) 创建邮件 Message, 完善邮件对象消息`主题`附件`日期`发送者接收者抄送者密送者等信息
 *      (3) 发送邮件 Transport, 提供网络传输对象发送邮件
 *
 *      邮件交互会话 javax.mail.Session: Session 对象保存了邮件系统的配置信息及提供用户验证
 *      密码验证类 Authenticator, 是抽象类, 可以新建实现类去覆盖原方法, 也可以像下面静态代码块直接使用.
 *      编写邮件需要 MimeMessage 对象, 该对象是抽象类的 javax.mail.Message 的一个实现类;
 *
 * @author: niaonao
 * @date: 2019/7/24
 **/
@Slf4j
public class SMTPEmailUtil {

    /**
     * 邮件发送者账户
     * 邮件发送协议 smtp 账号密码/账号授权码
     * 发件人
     * (邮件发送者从业务上看应支持系统配置, 此处就直接写好了数据; 你可以维护在配置文件,系统参数或数据库)
     */
    private static final String emailSenderAccount = "2171128382@qq.com";
    private static final String emailSenderAuthCode = "nmfatlwourbhdihh";
    private static final String emailSenderName = "哪都通物流有限公司";

    /**
     * 环境信息
     */
    private static Properties props = new Properties();

    static {
        // 设置用户的认证方式, 邮箱账户, 授权码
        props.put(SystemConstant.MAIL_SMTP_HOST, getHost(emailSenderAccount));
        props.setProperty(SystemConstant.MAIL_SMTP_AUTH, String.valueOf(Boolean.TRUE));
        props.put(SystemConstant.MAIL_SMTP_USER, emailSenderAccount);
        props.put(SystemConstant.MAIL_SMTP_PASSWORD, emailSenderAuthCode);
    }

    /**
     * @description: 邮箱服务器域名通用转换, 格式支持多数邮件服务器, 部分不支持
     * @param email
     * @return: java.lang.String
     * @author: niaonao
     * @date: 2019/7/25
     */
    private static String getHost(String email) {
        return new StringBuffer(SystemConstant.SMTP)
                .append(SystemConstant.SYMBOL_POINT)
                .append(email.split(SystemConstant.SYMBOL_AT)[1])
                .toString();
    }

    /**
     * @description: 获取共享的 Session
     * @return: javax.mail.Session
     * @author: niaonao
     * @date: 2019/7/25
     */
    private static Session getSession() {
        return Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(emailSenderAccount, emailSenderAuthCode);
            }
        });
    }

    /**
     * @description: 获取非共享的Session, 方法内部使用了 synchronized 关键字
     * lambda的使用条件是‘a functional interface has exactly one abstract method’
     * abstract class 使用lambda 的话要改造满足该条件
     * @return: javax.mail.Session
     * @author: niaonao
     * @date: 2019/7/25
     */
    private static Session getDefaultInstance() {
        return Session.getDefaultInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(emailSenderAccount, emailSenderAuthCode);
            }
        });
    }

    /**
     * @description: 默认邮件发送者
     *      即配置邮箱和密码\授权码的账户
     * @param
     * @return: javax.mail.internet.InternetAddress
     * @author: niaonao
     * @date: 2019/7/25
     */
    private static InternetAddress getDefaultSender() {
        try {
            return new InternetAddress(emailSenderAccount, emailSenderName, SystemConstant.CHARSET_UTF_8);
        } catch (UnsupportedEncodingException e) {
            log.error("[邮件工具]: 初始化异常{}", e.getMessage());
            throw new RuntimeException("获取默认邮件发送人错误!");
        }
    }

    /**
     * @description: 传输工具
     * @return: javax.mail.Transport
     * @author: niaonao
     * @date: 2019/7/25
     */
    private static Transport getTransport() throws MessagingException {
        // 设置邮件协议 smtp
        Transport transport = getSession().getTransport(SystemConstant.SMTP);
        // 此处 QQ 邮箱, 通过账号和授权码链接
        transport.connect(getHost(emailSenderAccount), emailSenderAccount, emailSenderAuthCode);
        return transport;
    }


    /**
     * @description: 收件人转换 Address 对象
     * @param receiverEmailList
     * @return: javax.mail.internet.InternetAddress[]
     * @author: niaonao
     * @date: 2019/7/25
     */
    private static InternetAddress[] tranAddressByList(List<String> receiverEmailList) {
        /*List<InternetAddress> internetAddressList = new ArrayList<>();
        receiverEmailList.forEach(item -> {
            InternetAddress internetAddress = new InternetAddress();
            internetAddress.setAddress(item);
            internetAddressList.add(internetAddress);
        });
        InternetAddress[] addresses = internetAddressList.stream().toArray(InternetAddress[]::new);*/

        if (CollectionUtils.isEmpty(receiverEmailList)) {
            return null;
        }

        InternetAddress[] address = new InternetAddress[receiverEmailList.size()];
        for (int index = 0; index < receiverEmailList.size(); index++) {
            InternetAddress internetAddress = new InternetAddress();
            internetAddress.setAddress(receiverEmailList.get(index));
            address[index] = internetAddress;
        }
        return address;
    }

    /**
     * @description: 发送普通文本
     * @param subject 邮件主题
     * @param text  邮件普通文本内容
     * @param emailReceiverAccount  接收者
     * @param sendDate  邮件发送时间
     * @return: void
     * @author: niaonao
     * @date: 2019/7/24
     */
    public static void sendEmailByText(String subject, String text, String emailReceiverAccount, Date sendDate) {
        try {
            // 邮件对象
            MimeMessage msg = new MimeMessage(getSession());
            // 发送人
            msg.setFrom(getDefaultSender());
            // 发送方式,接收人
            msg.setRecipients(Message.RecipientType.TO, emailReceiverAccount);
            // 消息
            msg.setSubject(subject);
            msg.setText(text);
            if (null != sendDate) {
                msg.setSentDate(sendDate.before(new Date()) ? new Date() : sendDate);
            } else {
                // 邮件默认立即发送
                msg.setSentDate(new Date());
            }
            //发送邮件
            Transport.send(msg);
            log.info("[邮件发送]: 邮件已发送!");
        } catch (MessagingException e) {
            log.error("[邮件发送]: 普通文本邮件发送异常, {}", e.getMessage());
        }
    }

    /**
     * @description: 发送普通消息邮件(多收件人)
     * @param subject   邮件标题
     * @param text      邮件内容
     * @param receiverEmailList 接收人
     * @param sendDate  邮件发送时间
     * @return: void
     * @author: niaonao
     * @date: 2019/7/25
     */
    public static void sendEmailByText(String subject, String text, List<String> receiverEmailList, Date sendDate) {
        InternetAddress[] addresses = tranAddressByList(receiverEmailList);
        try {
            // 邮件对象
            MimeMessage msg = new MimeMessage(getSession());
            msg.setFrom(getDefaultSender());
            msg.setRecipients(Message.RecipientType.TO, addresses);
            msg.setSubject(subject);
            msg.setText(text);
            if (null != sendDate) {
                msg.setSentDate(sendDate.before(new Date()) ? new Date() : sendDate);
            }
            // 开启传输,发送邮件
            Transport transport = getTransport();
            transport.sendMessage(msg, msg.getRecipients(Message.RecipientType.TO));
            // 关闭链接
            transport.close();
            log.info("[邮件发送]: 邮件已发送!");
        } catch (MessagingException e) {
            log.error("[邮件发送]: 普通文本邮件发送异常, {}", e.getMessage());
        }
    }

    /**
     * @description: 发送普通文本消息(指定接收人,抄送人,密送人)
     * @param subject   邮件主题
     * @param text      邮件内容
     * @param receiverEmailList 邮件接收人
     * @param carbonCopyList    邮件抄送人
     * @param blindCarbonCopyList   邮件密送人
     * @param sendDate  邮件发送时间
     * @return: void
     * @author: niaonao
     * @date: 2019/7/25
     */
    public static void sendEmailByText(String subject, String text, List<String> receiverEmailList, List<String> carbonCopyList, List<String> blindCarbonCopyList, Date sendDate) {
        InternetAddress[] receiverAddresses = tranAddressByList(receiverEmailList);
        InternetAddress[] carbonCopyAddresses = tranAddressByList(carbonCopyList);
        InternetAddress[] blindCarbonCopyAddresses = tranAddressByList(blindCarbonCopyList);
        try {
            // 邮件对象
            MimeMessage msg = new MimeMessage(getSession());
            msg.setFrom(getDefaultSender());
            // 发送方式,接收人
            msg.setRecipients(Message.RecipientType.TO, receiverAddresses);
            msg.setRecipients(Message.RecipientType.CC, carbonCopyAddresses);
            msg.setRecipients(Message.RecipientType.BCC, blindCarbonCopyAddresses);
            // 消息
            msg.setSubject(subject);
            msg.setText(text);
            if (null != sendDate) {
                msg.setSentDate(sendDate.before(new Date()) ? new Date() : sendDate);
            }
            // 开启传输,发送邮件
            Transport transport = getTransport();
            transport.sendMessage(msg, msg.getAllRecipients());
            // 关闭链接
            transport.close();
            log.info("[邮件发送]: 邮件已发送!");
        } catch (MessagingException e) {
            log.error("[邮件发送]: 普通文本邮件发送异常, {}", e.getMessage());
        }
    }

    /**
     * @description: 发送网页HTML 格式内容
     * @param subject   邮件标题
     * @param html      邮件内容, 语法HTML
     * @param receiverEmailList
     * @param carbonCopyList
     * @param blindCarbonCopyList
     * @param sendDate
     * @return: void
     * @author: niaonao
     * @date: 2019/7/25
     */
    public static void sendEmailByHtml(String subject, String html, List<String> receiverEmailList, List<String> carbonCopyList, List<String> blindCarbonCopyList, Date sendDate) {
        InternetAddress[] receiverAddresses = tranAddressByList(receiverEmailList);
        InternetAddress[] carbonCopyAddresses = tranAddressByList(carbonCopyList);
        InternetAddress[] blindCarbonCopyaddresses = tranAddressByList(blindCarbonCopyList);
        try {
            MimeMessage mimeMessage = new MimeMessage(getSession());

            // 发送方式,接收人
            mimeMessage.setRecipients(Message.RecipientType.TO, receiverAddresses);
            mimeMessage.setRecipients(Message.RecipientType.CC, carbonCopyAddresses);
            mimeMessage.setRecipients(Message.RecipientType.BCC, blindCarbonCopyaddresses);

            mimeMessage.setFrom(getDefaultSender());
            mimeMessage.setSubject(subject);
            mimeMessage.setContent(html, SystemConstant.CHARSET_TYPE_HTML);
            if (null != sendDate) {
                mimeMessage.setSentDate(sendDate.before(new Date()) ? new Date() : sendDate);
            }

            Transport transport = getTransport();
            transport.sendMessage(mimeMessage, mimeMessage.getAllRecipients());
            transport.close();
            log.info("[邮件发送]: HTML 网页邮件已发送!");
        } catch (MessagingException e) {
            log.error("[邮件发送]: HTML 网页邮件发送异常, {}", e.getMessage());
            e.printStackTrace();
        }
    }


    /**
     * @description: 发送携带附件的邮件
     * @param subject   邮件标题
     * @param html      邮件内容, 语法HTML
     * @param receiverEmailList
     * @param carbonCopyList
     * @param blindCarbonCopyList
     * @param sendDate
     * @return: void
     * @author: niaonao
     * @date: 2019/7/25
     */
    public static void sendEmailByEnclosure(String subject, String html, List<String> fileUrlList, List<String> receiverEmailList, List<String> carbonCopyList, List<String> blindCarbonCopyList, Date sendDate) {
        InternetAddress[] receiverAddresses = tranAddressByList(receiverEmailList);
        InternetAddress[] carbonCopyAddresses = tranAddressByList(carbonCopyList);
        InternetAddress[] blindCarbonCopyaddresses = tranAddressByList(blindCarbonCopyList);
        try {
            // html 节点
            MimeBodyPart contentPart = new MimeBodyPart();
            contentPart.setContent(html, SystemConstant.CHARSET_TYPE_HTML);

            // 组合节点
            MimeMultipart multipart = new MimeMultipart();
            for (String url:fileUrlList) {
                // 附件节点
                MimeBodyPart enclosure = new MimeBodyPart();
                // 读取文件
                // 将附件数据添加到"节点"
                DataHandler dataHandler = new DataHandler(new FileDataSource(url));
                enclosure.setDataHandler(dataHandler);
                enclosure.setFileName(MimeUtility.encodeText(dataHandler.getName()));
                multipart.addBodyPart(enclosure);
            }
            multipart.addBodyPart(contentPart);
            multipart.setSubType(SystemConstant.SUB_TYPE_MIXED);

            // 邮件对象
            MimeMessage mimeMessage = new MimeMessage(getSession());
            mimeMessage.setRecipients(Message.RecipientType.TO, receiverAddresses);
            mimeMessage.setRecipients(Message.RecipientType.CC, carbonCopyAddresses);
            mimeMessage.setRecipients(Message.RecipientType.BCC, blindCarbonCopyaddresses);
            mimeMessage.setFrom(getDefaultSender());
            mimeMessage.setSubject(subject);
            mimeMessage.setContent(multipart);
            if (null != sendDate) {
                mimeMessage.setSentDate(sendDate.before(new Date()) ? new Date() : sendDate);
            }

            Transport transport = getTransport();
            transport.sendMessage(mimeMessage, mimeMessage.getAllRecipients());
            transport.close();
            log.info("[邮件发送]: HTML 网页邮件已发送!");
        } catch (MessagingException e) {
            log.error("[邮件发送]: HTML 网页邮件发送异常, {}", e.getMessage());
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
