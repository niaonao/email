package pers.niaonao.email.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SMTPEmailUtilTest {

    @Test
    public void contextLoads() {
    }

    /**
     * @description: 测试邮件发送,单接收人
     * @author: niaonao
     * @date: 2019/7/25
     */
    @Test
    public void sendEmailByText() {
        String receiverEmail = "22221111@163.com";
        String subject = "七里香";
        String text = "窗外的麻雀 在电线杆上多嘴\n" +
                "你说这一句 很有夏天的感觉\n" +
                "手中的铅笔 在纸上来来回回\n" +
                "我用几行字形容你是我的谁\n" +
                "秋刀鱼的滋味 猫跟你都想了解\n" +
                "初恋的香味就这样被我们寻回\n" +
                "那温暖的阳光 像刚摘的鲜艳草莓\n" +
                "你说你舍不得吃掉这一种感觉\n" +
                "雨下整夜 我的爱溢出就像雨水\n" +
                "院子落叶 跟我的思念厚厚一叠\n" +
                "几句是非 也无法将我的热情冷却\n" +
                "你出现在我诗的每一页\n" +
                "雨下整夜 我的爱溢出就像雨水\n" +
                "窗台蝴蝶 像诗里纷飞的美丽章节\n" +
                "我接着写\n" +
                "把永远爱你写进诗的结尾\n" +
                "你是我唯一想要的了解\n" +
                "雨下整夜 我的爱溢出就像雨水\n" +
                "院子落叶 跟我的思念厚厚一叠\n" +
                "几句是非 也无法将我的热情冷却\n" +
                "你出现在我诗的每一页\n" +
                "那饱满的稻穗 幸福了这个季节\n" +
                "而你的脸颊像田里熟透的蕃茄\n" +
                "你突然对我说 七里香的名字很美\n" +
                "我此刻却只想亲吻你倔强的嘴\n" +
                "雨下整夜 我的爱溢出就像雨水\n" +
                "院子落叶 跟我的思念厚厚一叠\n" +
                "几句是非 也无法将我的热情冷却\n" +
                "你出现在我诗的每一页\n" +
                "整夜 我的爱溢出就像雨水\n" +
                "窗台蝴蝶 像诗里纷飞的美丽章节\n" +
                "我接着写\n" +
                "把永远爱你写进诗的结尾\n" +
                "你是我唯一想要的了解";
        // 立即发送
        SMTPEmailUtil.sendEmailByText(subject, text, receiverEmail, new Date());
    }

    /**
     * @description: 测试邮件发送多接收
     * @param
     * @return: void
     * @author: niaonao
     * @date: 2019/7/25
     */
    @Test
    public void sendEmailToMoreByText() {
        List<String> receiverList = Arrays.asList("22221111@163.com", "22221111@qq.com");
        String subject = "哪都通内部邮件";
        String text = "宝儿于罗天大醮战略性败于张楚岚, 请知悉!";
        SMTPEmailUtil.sendEmailByText(subject, text, receiverList,null);
    }

    /**
     * @description: 测试邮件发送及抄送
     * @return: void
     * @author: niaonao
     * @date: 2019/7/25
     */
    @Test
    public void sendEmailToCCBCCMoreByText() {
        List<String> receiverList = Arrays.asList("22221111@163.com");
        List<String> carbonCopyList = Arrays.asList("22221111@qq.com");
        String subject = "哪都通内部邮件";
        String text = "宝儿于罗天大醮战略性败于张楚岚, 请知悉!";
        SMTPEmailUtil.sendEmailByText(subject, text, receiverList, carbonCopyList,null,null);
    }

    /**
     * @description: 发送HTML 内容邮件
     * @param
     * @return: void
     * @author: niaonao
     * @date: 2019/7/25
     */
    @Test
    public void sendEmailByHtml() {
        List<String> receiverList = Arrays.asList("22221111@163.com");
        List<String> carbonCopyList = Arrays.asList("22221111@qq.com");
        String subject = "其实我一点都不瓜";
        String content = "<body>客官雅间但坐<br/>我这就去请宝儿姐<br/>" +
                "<img  width=\"400\" height=\"600\" src=\"http://5b0988e595225.cdn.sohucs.com/images/20180112/a1bff411d05e46dd8b399dfaa800eb16.jpeg\"/>" +
                "</body>";
        SMTPEmailUtil.sendEmailByHtml(subject, content, receiverList, carbonCopyList,null,null);
    }

    /**
     * @description: 发送附件
     *      附件不限于项目本身内部文件, 支持相对路径和全路径
     * @param
     * @return: void
     * @author: niaonao
     * @date: 2019/7/25
     */
    @Test
    public void sendEmailByEnclosure() {
        List<String> receiverList = Arrays.asList("22221111@163.com");
        List<String> carbonCopyList = Arrays.asList("22221111@qq.com");
        String subject = "宝儿姐喊你查阅附件";
        String content = "<img width=\"300\" height=\"400\" src=\"https://ss0.bdstatic.com/94oJfD_bAAcT8t7mm9GUKT-xh_/timg?image&quality=100&size=b4000_4000&sec=1564045091&di=57335f2e16c0a670b26c72aa9eb61b0c&src=http://imglf5.nosdn.127.net/img/UUZTMkZCa0pWZEE1Ri9yT1Rhc0hmMnUzdU1KellDZTNOOE1WMlE3N01XMCtrdm1HM0pSZVFBPT0.jpg?imageView&thumbnail=500x0&quality=96&stripmeta=0&type=jpg\"/>";
        // 项目内相对路径
        String smtpFileUrl = "src\\main\\resources\\static\\smtp.properties";
        // 本机D盘文件
        String LocalFileUrl = "D:\\代码检测规则.xlsx";
        List<String> fileDataSourceList = Arrays.asList(smtpFileUrl, LocalFileUrl);
        SMTPEmailUtil.sendEmailByEnclosure(subject, content, fileDataSourceList, receiverList, carbonCopyList,null,null);
    }
}
