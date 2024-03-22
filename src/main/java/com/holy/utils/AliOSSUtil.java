package com.holy;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.PutObjectResult;

import java.io.InputStream;

public class AliOSSUtil {

    // Endpoint以华东1（杭州）为例，其它Region请按实际情况填写。
    private static final String ENDPOINT = "https://oss-cn-beijing.aliyuncs.com";
    private static final String ENDPOINT_WITHOUT_HTTP = "oss-cn-beijing.aliyuncs.com";
    // 从环境变量中获取访问凭证。运行本代码示例之前，请确保已设置环境变量OSS_ACCESS_KEY_ID和OSS_ACCESS_KEY_SECRET。
    private static final String ACCESS_KEY_ID = "LTAI5tNV9WtiKDdS6WafcHRx";
    private static final String ACCESS_KEY_SECRET = "DeLTWySuaeNOaJDD5j7ZJS0G3dtLly";
    // 填写Bucket名称
    private static final String BUCKET_NAME = "restaurant-picture";

    public static final String DEFAULT_URL = "https://restaurant-picture.oss-cn-beijing.aliyuncs.com/default.jpg";

    public static String uploadFile(String objectName, InputStream inputStream) throws Exception {

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(ENDPOINT,ACCESS_KEY_ID,ACCESS_KEY_SECRET);

        String url = "";

        try {

            // 创建PutObjectRequest对象。
            PutObjectRequest putObjectRequest = new PutObjectRequest(BUCKET_NAME, objectName, inputStream);

            // 上传字符串。
            PutObjectResult result = ossClient.putObject(putObjectRequest);

            url = "https://" + BUCKET_NAME + "." + ENDPOINT_WITHOUT_HTTP + "/" + objectName;
        } catch (OSSException oe) {
            System.out.println("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            System.out.println("Error Message:" + oe.getErrorMessage());
            System.out.println("Error Code:" + oe.getErrorCode());
            System.out.println("Request ID:" + oe.getRequestId());
            System.out.println("Host ID:" + oe.getHostId());
        } catch (ClientException ce) {
            System.out.println("Caught an ClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network.");
            System.out.println("Error Message:" + ce.getMessage());
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }

        return url;
    }
}