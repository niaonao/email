package pers.niaonao.email.constant;

/**
 * @className: SystemConstant
 * @description: 常量类
 * @author: niaonao
 * @date: 2019/7/24
 **/
public class SystemConstant {

    /**
     * 符号常量
     */
    public static final String SYMBOL_POINT;
    public static final String SYMBOL_AT;

    public static final String CHARSET_UTF_8;
    /**
     * MIME_TYPE 类型
     *      以 网页HTML 输出
     *      以 普通文档输出
     */
    public static final String CHARSET_TYPE_HTML;
    public static final String CHARSET_TYPE_PLAIN;

    /**
     * 邮件对象节点关系
     *      混合关系
     *      关联关系
     */
    public static final String SUB_TYPE_MIXED;
    public static final String SUB_TYPE_RELATED;

    /**
     * 邮件协议服务器域名
     */
    public static final String MAIL_SMTP_HOST;

    /**
     * 设置是否验证
     */
    public static final String MAIL_SMTP_AUTH;

    /**
     * 邮件账户
     * SMTP 协议授权码(部分邮箱支持独立密码, 部分邮箱支持授权码)
     */
    public static final String MAIL_SMTP_USER;
    public static final String MAIL_SMTP_PASSWORD;

    /**
     * SMTP 协议服务器域名，一般的xxxxx@xx.xxx邮箱服务器域名通用格式smtp.xx.xxx（具体查看邮箱设置）
     *      QQ 邮箱
     *      163 邮箱
     */
    public static final String SMTP;
    public static final String SMTP_QQ;
    public static final String SMTP_NETEASE;

    static {
        SYMBOL_POINT = ".";
        SYMBOL_AT = "@";
        CHARSET_UTF_8 = "utf-8";

        CHARSET_TYPE_HTML = "text/html; charset=utf-8";
        CHARSET_TYPE_PLAIN = "text/plain; charset=utf-8";

        SUB_TYPE_MIXED = "mixed";
        SUB_TYPE_RELATED = "related";

        MAIL_SMTP_HOST = "mail.smtp.host";
        MAIL_SMTP_AUTH = "mail.smtp.auth";

        MAIL_SMTP_USER = "mail.smtp.user";
        MAIL_SMTP_PASSWORD = "mail.smtp.password";

        SMTP = "smtp";
        SMTP_QQ = "smtp.qq.com";
        SMTP_NETEASE = "smtp.163.com";
    }

}
