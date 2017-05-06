package net.myspring.cloud.modules.input.utils;

import com.google.common.collect.Maps;
import net.myspring.cloud.GlobalCloudApplication;
import net.myspring.cloud.common.utils.Const;
import net.myspring.cloud.common.utils.SecurityUtils;
import net.myspring.cloud.modules.input.dto.K3CloudSave;
import net.myspring.cloud.modules.remote.dto.AccountDto;
import net.myspring.cloud.modules.sys.domain.KingdeeBook;
import net.myspring.cloud.modules.sys.mapper.KingdeeBookMapper;
import net.myspring.common.exception.ServiceException;
import net.myspring.util.json.ObjectMapperUtils;
import net.myspring.util.text.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Base64Utils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

public class K3cloudUtils {

    protected static Logger logger = LoggerFactory.getLogger(K3cloudUtils.class);
    // Cookie 值
    private static String CookieVal = null;
    private static Map<String, String> map = Maps.newHashMap();

    static {
        map.put("save", "Kingdee.BOS.WebApi.ServicesStub.DynamicFormService.Save.common.kdsvc");
        map.put("view", "Kingdee.BOS.WebApi.ServicesStub.DynamicFormService.View.common.kdsvc");
        map.put("submit", "Kingdee.BOS.WebApi.ServicesStub.DynamicFormService.Submit.common.kdsvc");
        map.put("audit", "Kingdee.BOS.WebApi.ServicesStub.DynamicFormService.Audit.common.kdsvc");
        map.put("unAudit", "Kingdee.BOS.WebApi.ServicesStub.DynamicFormService.UnAudit.common.kdsvc");
        map.put("statusConvert", "Kingdee.BOS.WebApi.ServicesStub.DynamicFormService.StatusConvert.common.kdsvc");
    }

    // HttpURLConnection
    private static HttpURLConnection initUrlConn(String url, JSONArray paras) throws Exception {
        KingdeeBookMapper kingdeeBookMapper = GlobalCloudApplication.getApplicationContext().getBean(KingdeeBookMapper.class);
        KingdeeBook kingdeeBook = kingdeeBookMapper.findByCompanyId(SecurityUtils.getCompanyId());
        URL postUrl = new URL(kingdeeBook.getKingdeePostUrl().concat(url));
        HttpURLConnection connection = (HttpURLConnection) postUrl.openConnection();
        if (CookieVal != null) {
            connection.setRequestProperty("Cookie", CookieVal);
        }
        if (!connection.getDoOutput()) {
            connection.setDoOutput(true);
        }
        connection.setRequestMethod("POST");
        connection.setUseCaches(false);
        connection.setInstanceFollowRedirects(true);
        connection.setRequestProperty("Content-Type", "application/json");
        DataOutputStream out = new DataOutputStream(connection.getOutputStream());

        UUID uuid = UUID.randomUUID();
        int hashCode = uuid.toString().hashCode();

        JSONObject jObj = new JSONObject();

        jObj.put("format", 1);
        jObj.put("useragent", "ApiClient");
        jObj.put("rid", hashCode);
//        jObj.put("parameters", StringUtils.chinaToUnicode(paras.toString()));
        jObj.put("timestamp", new Date().toString());
        jObj.put("v", "1.0");

        out.writeBytes(jObj.toString());
        out.flush();
        out.close();

        return connection;
    }

    // Login
    public static boolean login(String dbid, String user, String pwd) {
        boolean bResult = false;
        StringBuilder sb = new StringBuilder();
        String sUrl = "Kingdee.BOS.WebApi.ServicesStub.AuthService.ValidateUser.common.kdsvc";
        JSONArray jParas = new JSONArray();
        jParas.put(dbid);// 帐套Id
        jParas.put(user);// 用户名
        jParas.put(pwd);// 密码
        jParas.put(Const.K3Cloud_LANG);// 语言
        HttpURLConnection connection = null;
        try {
            connection = initUrlConn(sUrl, jParas);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            return false;
        }
        // 获取Cookie
        String key = null;
        for (int i = 1; (key = connection.getHeaderFieldKey(i)) != null; i++) {
            if (key.equalsIgnoreCase("Set-Cookie")) {
                String tempCookieVal = connection.getHeaderField(i);
                if (tempCookieVal.startsWith("kdservice-sessionid")) {
                    CookieVal = tempCookieVal;
                    break;
                }
            }
        }
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            System.out.println(" ============================= ");
            System.out.println(" Contents of post request ");
            System.out.println(" ============================= ");
            while ((line = reader.readLine()) != null) {
                String sResult = new String(line.getBytes(), "utf-8");
                sb.append(sResult);
            }
            bResult = sb.toString().contains("\"LoginResultType\":1");
            System.out.println(sb.toString());
            System.out.println(" ============================= ");
            System.out.println(" Contents of post request ends ");
            System.out.println(" ============================= ");
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            return false;
        }
        connection.disconnect();
        return bResult;
    }

    // Save
    public static String save(String formId, String content) {
        logger.info("金蝶保存开始----------------------------------" + formId);
        logger.info("content：" + content);
        String invoke = invoke("save", formId, content);
        logger.info("invoke：" + content);
        logger.info("金蝶保存结束-----------------------------------" + formId);
        return invoke;
    }

    // View
    public static String view(String formId, String content) {
        return invoke("view", formId, content);
    }

    // Submit
    public static String submit(String formId, String content) {
        logger.info("金蝶提交开始----------------------------------" + formId);
        logger.info("content：" + content);
        String invoke = invoke("submit", formId, content);
        logger.info("invoke：" + content);
        logger.info("金蝶提交结束-----------------------------------" + formId);
        return invoke;
    }

    // Audit
    public static String audit(String formId, String content) {
        logger.info("金蝶审核开始----------------------------------" + formId);
        logger.info("content：" + content);
        String invoke = invoke("audit", formId, content);
        logger.info("invoke：" + content);
        logger.info("金蝶审核结束-----------------------------------" + formId);
        return invoke;
    }

    // UnAudit
    public static String unAudit(String formId, String content) {
        return invoke("unAudit", formId, content);
    }

    // StatusConvert
    public static String statusConvert(String formId, String content) {
        return invoke("statusConvert", formId, content);
    }

    private static String invoke(String deal, String formId, String content) {
        StringBuilder sb = new StringBuilder();
        String sUrl = map.get(deal).toString();
        JSONArray jParas = new JSONArray();
        jParas.put(formId);
        jParas.put(content);
        HttpURLConnection connectionInvoke = null;
        try {
            connectionInvoke = initUrlConn(sUrl, jParas);
            BufferedReader reader = new BufferedReader(new InputStreamReader(connectionInvoke.getInputStream()));
            String line;
            System.out.println(" ============================= ");
            System.out.println(" Contents of post request ");
            System.out.println(" ============================= ");
            while ((line = reader.readLine()) != null) {
                String sResult = new String(line.getBytes(), "utf-8");
                sb.append(sResult);
            }
            System.out.println(sb.toString());
            System.out.println(" ============================= ");
            System.out.println(" Contents of post request ends ");
            System.out.println(" ============================= ");
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            return "";
        }
        connectionInvoke.disconnect();
        return sb.toString();
    }

    public static Boolean login(AccountDto accountDto) {
        String outId = accountDto.getOutId();
        String outPassword = "";
        if (StringUtils.isNotBlank(accountDto.getOutPassword())) {
            byte[] resultBytes = Base64Utils.decodeFromString(accountDto.getOutPassword());
            outPassword=new String(resultBytes);
        }
        if (StringUtils.isBlank(outId) || StringUtils.isBlank(outPassword)) {
            return false;
        }
        KingdeeBookMapper kingdeeBookMapper = GlobalCloudApplication.getApplicationContext().getBean(KingdeeBookMapper.class);
        KingdeeBook kingdeeBook = kingdeeBookMapper.findByCompanyId(SecurityUtils.getCompanyId());
        return login(kingdeeBook.getKingdeeDbid(), outId, outPassword);
    }


    // Save
    public static K3CloudSave save(K3CloudSave k3CloudSave,AccountDto accountDto) {
        if (StringUtils.isBlank(accountDto.getOutId()) || StringUtils.isBlank(accountDto.getOutPassword())) {
            throw new ServiceException("请确保已填写财务账号和密码");
        }
        if (!login(accountDto)) {
            throw new ServiceException("财务账户或者密码不正确");
        }
        String result = K3cloudUtils.save(k3CloudSave.getFormId(), k3CloudSave.getContent());
        net.sf.json.JSONObject jsonObject = net.sf.json.JSONObject.fromObject(result);
        if ("TRUE".equals(jsonObject.getJSONObject("Result").getJSONObject("ResponseStatus").getString("IsSuccess"))) {
            String billNo = jsonObject.getJSONObject("Result").getString("Number");
            Map<String, Object> root = Maps.newLinkedHashMap();
            root.put("CreateOrgId", 0);
            root.put("Numbers", billNo);
            String content = ObjectMapperUtils.writeValueAsString(root);
            K3cloudUtils.submit(k3CloudSave.getFormId(), content);
            if (k3CloudSave.getAutoAudit()) {
                K3cloudUtils.audit(k3CloudSave.getFormId(), content);
            }
            k3CloudSave.setBillNo(billNo);
            return k3CloudSave;
        } else {
            throw new ServiceException("金蝶开单失败：" + result);
        }
    }

    // Save
    public static K3CloudSaveExtend save(K3CloudSaveExtend k3CloudSaveExtend,AccountDto accountDto) {
        if (StringUtils.isBlank(accountDto.getOutId()) || StringUtils.isBlank(accountDto.getOutPassword())) {
            throw new ServiceException("请确保已填写财务账号和密码");
        }
        if (!login(accountDto)) {
            throw new ServiceException("财务账户或者密码不正确");
        }
        String result = K3cloudUtils.save(k3CloudSaveExtend.getFormId(), k3CloudSaveExtend.getContent());
        net.sf.json.JSONObject jsonObject = net.sf.json.JSONObject.fromObject(result);
        if ("TRUE".equals(jsonObject.getJSONObject("Result").getJSONObject("ResponseStatus").getString("IsSuccess"))) {
            String billNo = jsonObject.getJSONObject("Result").getString("Number");
            Map<String, Object> root = Maps.newLinkedHashMap();
            root.put("CreateOrgId", 0);
            root.put("Numbers", billNo);
            String content = ObjectMapperUtils.writeValueAsString(root);
            K3cloudUtils.submit(k3CloudSaveExtend.getFormId(), content);
            K3cloudUtils.audit(k3CloudSaveExtend.getFormId(), content);
            k3CloudSaveExtend.setBillNo(billNo);
            String nextBillNo = k3CloudSaveExtend.getNextBillNo();
            root = Maps.newLinkedHashMap();
            root.put("CreateOrgId", 0);
            root.put("Numbers", nextBillNo);
            k3CloudSaveExtend.setBillNo(k3CloudSaveExtend.getBillNo() + "," + "应收单:" + nextBillNo);
            content = ObjectMapperUtils.writeValueAsString(root);
            K3cloudUtils.submit(k3CloudSaveExtend.getNextFormId(), content);
            K3cloudUtils.audit(k3CloudSaveExtend.getNextFormId(), content);
            return k3CloudSaveExtend;
        } else {
            throw new ServiceException("金蝶开单失败：" + result);
        }
    }

    public static Map<String, Object> getMap(String key, Object value) {
        Map<String, Object> map = Maps.newHashMap();
        map.put(key, value);
        return map;
    }

}
