package com.ngnsoft.ngp;


/**
 *
 * @author fcy
 */
public class EngineConfig {
    
    private String id = "1";
    
    private Long load = 1000L;
    
    private String protocol = "http://";
    
    private String serverPort = "8080";
    
    private String serverContext = "/ngs";
    
    private String fileServer = "http://fs.ngnsoft.com";
    
    private String ipDataFile = "/var/ngn/ip/IP-COUNTRY.BIN";
    
    private String ipLicenseFile = "/var/ngn/ip/license.key";
    
    private String certPath = "/var/ngn/cert/push/";
    
    private boolean prodStage = false;
    
    private boolean lsenEnable= false;
    
    private boolean ucEnable = false;
    
    private static final String certSuffixProd = "_prod";
    
    private static final String certSuffix = "";

    public String getFileServer() {
        return fileServer;
    }

    public void setFileServer(String fileServer) {
        this.fileServer = fileServer;
    }

    public String getIpDataFile() {
        return ipDataFile;
    }

    public void setIpDataFile(String ipDataFile) {
        this.ipDataFile = ipDataFile;
    }

    public String getIpLicenseFile() {
        return ipLicenseFile;
    }

    public void setIpLicenseFile(String ipLicenseFile) {
        this.ipLicenseFile = ipLicenseFile;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getServerContext() {
        return serverContext;
    }

    public void setServerContext(String serverContext) {
        this.serverContext = serverContext;
    }

    public String getServerPort() {
        return serverPort;
    }

    public void setServerPort(String serverPort) {
        this.serverPort = serverPort;
    }

    public String getCertPath() {
        return certPath;
    }

    public void setCertPath(String certPath) {
        this.certPath = certPath;
    }

    public boolean isProdStage() {
        return prodStage;
    }

    public void setProdStage(boolean prodStage) {
        this.prodStage = prodStage;
    }

    public boolean isLsenEnable() {
        return lsenEnable;
    }

    public void setLsenEnable(boolean lsenEnable) {
        this.lsenEnable = lsenEnable;
    }

    public boolean isUcEnable() {
        return ucEnable;
    }

    public void setUcEnable(boolean ucEnable) {
        this.ucEnable = ucEnable;
    }

    public Long getLoad() {
        return load;
    }

    public void setLoad(Long load) {
        this.load = load;
    }
    
    public String getCertSuffix() {
        if(prodStage) {
            return certSuffixProd;
        } else {
            return certSuffix;
        }
    }
    
}
