package com.lxs.legou.pay.config;

import java.io.FileWriter;
import java.io.IOException;

public class AlipayUtils {

    // 应用ID,您的APPID，收款账号既是您的APPID对应支付宝账号
    public static String app_id = "2021000116662476";

    //todo 在这里放置您的商户私钥。
    // 注意，这里的配置是无效的，因为支付宝的私钥不是公开的，所以，这里的配置只是提供一个参考，如果您的商户私钥不是这里的，请自行替换。

    // 商户私钥，您的PKCS8格式RSA2私钥
    public static String merchant_private_key="MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCPgnMRjF3gOcDmf3VrzSxSPsksD5r0+ZBzvOwhNQsTOGpEwTo4NU8acegvSeSeiRcyI0bp5tL2SNxzPQ3nIWAVwolTV8QUW/5GdPaZRDF7md2u3cv6XDP0K/nivR1o5+FveLZe+ik3l0yo7qiFZFt+1yCBflr4R/sfr6xN4bln1r2f3j3vqG0ZgPkN3dGXC5yucQKkOmjPSBb7dQH+juzHa2ThZQVkOUfVqVSWaP4GSVy8pN1po8nc9w/l8+O9ZtYsoqvc2B/qlVjS2t6H2Wb8bAEMgKAscmg95TAkS9Obr7c6whdI6K47MDd8arvcrboHHzAwXQz9aE2Dz29Oht+3AgMBAAECggEAWK/gVsbtOOXYHVpFgZJxQn6EqlMWizAzoiLttnPmXUFvAwB670OamBguyhKbU5IbIKQGbgR0+Tj768eecOkCTVBEt5m0lnRnhrk/sGYhQmZLRobRSO50OQB4VDRVGYC7o7zwz28/y5gYCwo6YP0tQgzGjOA+bdNmESXqkNLfVpjBJ9WGQ2XXk+IZv3b5zpcWbmjghUrvnVSgHktQWxCCv7cT7YSfs0R5BgioqnRH/KDbipfjCvjDTc68hCK2ENhkMMsq97MB5CyVm9K8V4cc2rbocS28qkpt/fPgcDLD4qNFGIWOK1KQU30Gnoiq/3eFd0kdjKpmh39RATXKxH+80QKBgQDlZR+7t0F+W5PCgi3ndNEXxTu3y4NzMplFekFmLoEzkMPp6oygKPFmRL7MIS/bUrueFRFLB4rhUnCcmtDoo71KverOK8/jeUrBQ3CbREhv6dhLEPt8VTnPOTKu5zWAryypq9ckxy+6x0qZQAUVEWV3X2muOxj7NVRrA67h4G/srwKBgQCgJ1W9gp1CojsTTa56YLkrZ6ROOiQe6r/9Ps+o2AgF9aiAD7bbELCfLNjEX1JkC/Uz+qElGm02wGDglnrBySaldePd5qVHndtG+S3sTN4fAJogIk22r6OyNFaVt7dkDQABPYOUD8dNLtO15CmreHt/WVVWVURaSDopGdHIS0pPeQKBgQClqacGKKz6tOX7xYmjKsDKGt0quaWfrv3zp/vM8G7ML5Lcvk96ZrcW9TSZNKpcCN6Hpo1LkxHf8Wp0HnYaXA2bCZal+Av1KiYrdim/Z8usTsOuSoBj5SSPm64Xk9AWs+tcSUZhQBwfFkewbwR8l4igcd5fPjynRndFpW5Jn40U7wKBgDwN4/HXS58T9HENnRKYuHbVO2B2HttVDb8p0Xav43pQnum+esP0ozZc+7/POrb3YPAn2EtweGmig/FTnB6UjiBXb6Xi+jE7uWEIyTalsFkRwbmxA1Ew1YJaa+liZatn0IumhHN94y9HEu5O6204FZ8wYXapLKNqGB0XaHnh+bCJAoGACJtUJPRGaqSr0mjL4CeYCWur+/4bgQE278wLulyxtVrCUH2TKiyv/ZC8h/JilvC4poDyMR/R6zKdWkMSvmk1e15Xy8OiuvQKeQxms5RhvQ41nL0lYJBLb3BMeRxPEqKfg4lTVHf1QIBTgCGpqZUSnDX8b/dgGCkZHpoWeLvMeqg=";

    // 支付宝公钥,查看地址：https://openhome.alipay.com/platform/keyManage.htm 对应APPID下的支付宝公钥。
    public static String alipay_public_key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAjuJdySleZTVc4xnu8REpx7buh6vnY3Qb8UEgeBVVVYxvAModesuqUGLZwTzd2aliVQh7q45f6e+j/wTpW0DcSff0S+GPrj1fi0sVFsVwav9Hw9v6eDSrVRql/3lyRcunmeyuyafQKK1rYv71mjlfK13DajDbXXtM0FAc+CV74IcFwFZ1y1iQci8bUdQaLs7haneJkmY4TlCKTYkMyX4Kznr+2zojJtWI3mEWrFkYbIdkLBbVDtYYdJwrDNFlyOEA8PCa18iZ9nGHTgoDPg7uK8pK7JszgxdExo9o+lp5QARmbwx8flNd6vUC5ny3zRbDVN6L4lTdoy1RYG7YKbPK7wIDAQAB";

    // 服务器异步通知页面路径
    //需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    public static String notify_url = "http://mnt5dx.natappfree.cc/getnotify";

    // 页面跳转同步通知页面路径
    //需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    public static String return_url = "http://mnt5dx.natappfree.cc/getreturn";

    // 签名方式
    public static String sign_type = "RSA2";

    // 字符编码格式
    public static String charset = "utf-8";

    // 支付宝网关,注意这些使用的是沙箱的支付宝网关，与正常网关的区别是多了dev
    public static String gatewayUrl = "https://openapi.alipaydev.com/gateway.do";

    // 支付宝网关
    public static String log_path = "C:\\";


    //↑↑↑↑↑↑↑↑↑↑请在这里配置您的基本信息↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

    /**
     * 写日志，方便测试（看网站需求，也可以改成把记录存入数据库）
     * @param sWord 要写入日志里的文本内容
     */
    public static void logResult(String sWord) {
        FileWriter writer = null;
        try {
            writer = new FileWriter(log_path + "alipay_log_" + System.currentTimeMillis()+".txt");
            writer.write(sWord);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
